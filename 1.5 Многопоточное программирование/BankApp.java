import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class BankApp {
    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank(3);
        bank.addObserver(new ConsoleLogger());

        bank.registerClient(new Client(1, 1_200.0, "USD"));
        bank.registerClient(new Client(2, 950.0, "EUR"));
        bank.registerClient(new Client(3, 150_000.0, "RUB"));

        bank.deposit(1, 300.0);
        bank.withdraw(2, 70.0);
        bank.exchangeCurrency(3, "RUB", "USD", 50_000.0);
        bank.transferFunds(1, 2, 200.0);
        bank.transferFunds(2, 3, 50.0);

        Thread.sleep(4_000);
        bank.shutdown();
    }
}

interface Observer {
    void update(String message);
}

class ConsoleLogger implements Observer {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Override
    public synchronized void update(String message) {
        System.out.printf("[%s] %s%n", LocalTime.now().format(FORMATTER), message);
    }
}

final class Transaction {
    private final String description;
    private final Runnable action;

    Transaction(String description, Runnable action) {
        this.description = description;
        this.action = action;
    }

    String description() {
        return description;
    }

    void execute() {
        action.run();
    }
}

class Cashier extends Thread {
    private final int id;
    private final Bank bank;
    private final BlockingQueue<Transaction> queue;
    private final AtomicBoolean running;

    Cashier(int id, Bank bank, BlockingQueue<Transaction> queue, AtomicBoolean running) {
        super("cashier-" + id);
        this.id = id;
        this.bank = bank;
        this.queue = queue;
        this.running = running;
    }

    @Override
    public void run() {
        try {
            while (running.get() || !queue.isEmpty()) {
                Transaction tx = queue.poll(500, TimeUnit.MILLISECONDS);
                if (tx == null) {
                    continue;
                }
                bank.notifyObservers("Cashier " + id + " processing: " + tx.description());
                try {
                    tx.execute();
                    bank.notifyObservers("Cashier " + id + " done: " + tx.description());
                } catch (Exception e) {
                    bank.notifyObservers("Cashier " + id + " failed: " + e.getMessage());
                }
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        } finally {
            bank.notifyObservers("Cashier " + id + " stopped");
        }
    }

    void shutdown() {
        interrupt();
    }
}

class Bank {
    private final Map<Integer, Client> clients = new java.util.concurrent.ConcurrentHashMap<>();
    private final Map<String, Double> exchangeRates = new java.util.concurrent.ConcurrentHashMap<>();
    private final BlockingQueue<Transaction> transactionQueue = new LinkedBlockingQueue<>();
    private final List<Observer> observers = new CopyOnWriteArrayList<>();
    private final List<Cashier> cashiers = new ArrayList<>();
    private final ScheduledThreadPoolExecutor rateUpdater = new ScheduledThreadPoolExecutor(1);
    private final AtomicBoolean running = new AtomicBoolean(true);

    Bank(int cashierCount) {
        seedRates();
        startRateUpdater();
        startCashiers(cashierCount);
    }

    void addObserver(Observer observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    void registerClient(Client client) {
        Objects.requireNonNull(client, "client");
        Client prev = clients.putIfAbsent(client.id(), client);
        if (prev != null) {
            throw new IllegalArgumentException("Client " + client.id() + " already registered");
        }
        notifyObservers("Registered client " + client.id() + " with balances " + client.balancesSnapshot());
    }

    void deposit(int clientId, double amount) {
        ensurePositive(amount, "Deposit amount must be positive");
        enqueue("Deposit " + amount + " to " + clientId, () -> {
            Client client = requireClient(clientId);
            String currency = client.primaryCurrency();
            client.lock().lock();
            try {
                client.add(currency, amount);
                double balance = client.balance(currency);
                notifyObservers(String.format("Deposit: client %d +%.2f %s (balance %.2f %s)",
                        clientId, amount, currency, balance, currency));
            } finally {
                client.lock().unlock();
            }
        });
    }

    void withdraw(int clientId, double amount) {
        ensurePositive(amount, "Withdraw amount must be positive");
        enqueue("Withdraw " + amount + " from " + clientId, () -> {
            Client client = requireClient(clientId);
            String currency = client.primaryCurrency();
            client.lock().lock();
            try {
                if (!client.withdraw(currency, amount)) {
                    notifyObservers(String.format("Withdraw declined: client %d lacks %.2f %s",
                            clientId, amount, currency));
                    return;
                }
                double balance = client.balance(currency);
                notifyObservers(String.format("Withdraw: client %d -%.2f %s (balance %.2f %s)",
                        clientId, amount, currency, balance, currency));
            } finally {
                client.lock().unlock();
            }
        });
    }

    void exchangeCurrency(int clientId, String fromCurrency, String toCurrency, double amount) {
        ensurePositive(amount, "Exchange amount must be positive");
        enqueue("Exchange " + amount + " " + fromCurrency + " -> " + toCurrency + " for " + clientId, () -> {
            Client client = requireClient(clientId);
            String from = normalizeCurrency(fromCurrency);
            String to = normalizeCurrency(toCurrency);
            client.lock().lock();
            try {
                if (!client.withdraw(from, amount)) {
                    notifyObservers(String.format("Exchange declined: client %d lacks %.2f %s",
                            clientId, amount, from));
                    return;
                }
                double converted = convert(amount, from, to);
                client.add(to, converted);
                double fromLeft = client.balance(from);
                if (fromLeft < 0.0001 && !to.equals(client.primaryCurrency())) {
                    client.setPrimaryCurrency(to);
                }
                notifyObservers(String.format("Exchange: client %d %.2f %s -> %.2f %s (rest %.2f %s)",
                        clientId, amount, from, converted, to, fromLeft, from));
            } finally {
                client.lock().unlock();
            }
        });
    }

    void transferFunds(int senderId, int receiverId, double amount) {
        ensurePositive(amount, "Transfer amount must be positive");
        if (senderId == receiverId) {
            throw new IllegalArgumentException("Sender and receiver must differ");
        }
        enqueue("Transfer " + amount + " from " + senderId + " to " + receiverId, () -> {
            Client sender = requireClient(senderId);
            Client receiver = requireClient(receiverId);
            List<Client> ordered = sender.id() < receiver.id()
                    ? List.of(sender, receiver)
                    : List.of(receiver, sender);
            ordered.get(0).lock().lock();
            ordered.get(1).lock().lock();
            try {
                String fromCurrency = sender.primaryCurrency();
                String toCurrency = receiver.primaryCurrency();
                if (!sender.withdraw(fromCurrency, amount)) {
                    notifyObservers(String.format("Transfer declined: client %d lacks %.2f %s",
                            senderId, amount, fromCurrency));
                    return;
                }
                double converted = convert(amount, fromCurrency, toCurrency);
                receiver.add(toCurrency, converted);
                notifyObservers(String.format("Transfer: %d -> %d %.2f %s (%.2f %s credited)",
                        senderId, receiverId, amount, fromCurrency, converted, toCurrency));
            } finally {
                ordered.get(1).lock().unlock();
                ordered.get(0).lock().unlock();
            }
        });
    }

    void shutdown() {
        if (running.compareAndSet(true, false)) {
            notifyObservers("Bank shutting down");
            rateUpdater.shutdownNow();
            for (Cashier cashier : cashiers) {
                cashier.shutdown();
            }
            for (Cashier cashier : cashiers) {
                try {
                    cashier.join(1_000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    private void enqueue(String description, Runnable action) {
        if (!running.get()) {
            throw new IllegalStateException("Bank is stopped");
        }
        transactionQueue.offer(new Transaction(description, action));
        notifyObservers("Queued: " + description);
    }

    private void startCashiers(int count) {
        for (int i = 1; i <= count; i++) {
            Cashier cashier = new Cashier(i, this, transactionQueue, running);
            cashiers.add(cashier);
            cashier.start();
        }
    }

    private void seedRates() {
        exchangeRates.put("USD", 1.0);
        exchangeRates.put("EUR", 1.08);
        exchangeRates.put("GBP", 1.25);
        exchangeRates.put("RUB", 0.011);
    }

    private void startRateUpdater() {
        rateUpdater.setRemoveOnCancelPolicy(true);
        rateUpdater.scheduleAtFixedRate(() -> {
            try {
                ThreadLocalRandom rnd = ThreadLocalRandom.current();
                for (String code : new ArrayList<>(exchangeRates.keySet())) {
                    if ("USD".equals(code)) {
                        continue; // base currency
                    }
                    double drift = 1 + rnd.nextDouble(-0.01, 0.01);
                    exchangeRates.compute(code, (k, oldVal) -> {
                        double next = (oldVal == null ? 1.0 : oldVal) * drift;
                        return Math.max(0.0001, next);
                    });
                    double updated = exchangeRates.get(code);
                    notifyObservers(String.format("FX tick: 1 %s = %.4f USD", code, updated));
                }
            } catch (Exception e) {
                notifyObservers("FX update failed: " + e.getMessage());
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private double convert(double amount, String from, String to) {
        String fromCode = normalizeCurrency(from);
        String toCode = normalizeCurrency(to);
        Double fromRate = exchangeRates.get(fromCode);
        Double toRate = exchangeRates.get(toCode);
        if (fromRate == null || toRate == null) {
            throw new IllegalArgumentException("Unknown currency " + fromCode + " or " + toCode);
        }
        double usd = amount * fromRate;
        return usd / toRate;
    }

    private Client requireClient(int clientId) {
        Client client = clients.get(clientId);
        if (client == null) {
            throw new IllegalArgumentException("Unknown client " + clientId);
        }
        return client;
    }

    private static void ensurePositive(double amount, String message) {
        if (amount <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    static String normalizeCurrency(String code) {
        return code == null ? null : code.trim().toUpperCase(Locale.ROOT);
    }
}

class Client {
    private final int id;
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Map<String, Double> balances = new HashMap<>();
    private String primaryCurrency;

    Client(int id, double balance, String currency) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        Objects.requireNonNull(currency, "currency");
        this.id = id;
        this.primaryCurrency = Bank.normalizeCurrency(currency);
        balances.put(this.primaryCurrency, balance);
    }

    int id() {
        return id;
    }

    ReentrantLock lock() {
        return lock;
    }

    String primaryCurrency() {
        return primaryCurrency;
    }

    void setPrimaryCurrency(String currency) {
        this.primaryCurrency = Bank.normalizeCurrency(currency);
    }

    double balance(String currency) {
        return balances.getOrDefault(Bank.normalizeCurrency(currency), 0.0);
    }

    void add(String currency, double amount) {
        String key = Bank.normalizeCurrency(currency);
        balances.merge(key, amount, Double::sum);
    }

    boolean withdraw(String currency, double amount) {
        String key = Bank.normalizeCurrency(currency);
        double current = balances.getOrDefault(key, 0.0);
        if (current + 1e-9 < amount) {
            return false;
        }
        double next = current - amount;
        if (next < 1e-9) {
            balances.remove(key);
        } else {
            balances.put(key, next);
        }
        return true;
    }

    Map<String, Double> balancesSnapshot() {
        return Collections.unmodifiableMap(new HashMap<>(balances));
    }
}
