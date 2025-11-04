?import java.util.*;

/**
 * Вариант 1
 *
 * Что изменено по сравнению с предыдущим решением (1.2):
 * - Single Responsibility: доменная модель отделена от ввода/вывода; сущности узкоспециализированы.
 * - Open/Closed: для нового транспорта/движения достаточно добавить класс, без правки существующего кода.
 * - Liskov Substitution: все транспорты взаимозаменяемы по интерфейсу Transport.
 * - Interface Segregation: небольшие интерфейсы (Transport, HasEngine, MovementStrategy, Repository, Factory).
 * - Dependency Inversion: Transport зависит от абстракций (стратегий/двигателей), внедряемых извне.
 * - Generics: стратегии, репозитории, фабрики параметризованы типами.
 * - Паттерны: Strategy (движение), Factory (создание), Repository (хранилище).
 */

public class variant1 {
    public static void main(String[] args) {
    p    // ??????? ??? ?????? ?? ????????????? ????? ? ?????? ??????     p    try {
        new ConsoleApp().run();
    p    } catch (Exception e) {     p        System.out.println("????????? ?????? ?????/??????: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // ??????????? ??????: ????????? ????????? ??? ???????     p    }
    }
}

// ==========================
//      Доменные абстракции
// ==========================

interface Transport {
    String model();
    int capacity();
    void move();
}

interface HasEngine<E extends Engine> {
    E engine();
}

/**
 * Самотипизированная базовая модель транспорта, использующая стратегию движения.
 */
abstract class AbstractTransport<T extends AbstractTransport<T>> implements Transport {
    private final String model;
    private final int capacity;
    private final MovementStrategy<T> movement;

    protected AbstractTransport(String model, int capacity, MovementStrategy<T> movement) {
        if (model == null || model.isBlank()) throw new IllegalArgumentException("модель не задана");
        if (capacity < 0) throw new IllegalArgumentException("вместимость должна быть ≥ 0");
        this.model = model;
        this.capacity = capacity;
        this.movement = Objects.requireNonNull(movement);
    }

    protected abstract T self();

    @Override public String model() { return model; }
    @Override public int capacity() { return capacity; }

    @Override
    public void move() {
        movement.move(self());
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + model + ", вместимость=" + capacity + ")";
    }
}

// ==========================
//         Движение (Strategy)
// ==========================

interface MovementStrategy<T extends Transport> {
    void move(T transport);
}

final class RoadMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": едет по дороге");
    }
}

final class AirMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": летит по воздуху");
    }
}

final class WaterMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": плывёт по воде");
    }
}

// ==========================
//            Двигатели
// ==========================

enum FuelType { GASOLINE, DIESEL, JET_FUEL, ELECTRIC }

interface Engine {
    int powerHP();
    boolean isRunning();
    void start();
    void stop();
}

final class CombustionEngine implements Engine {
    private final int powerHP;
    private final FuelType fuel;
    private boolean running;

    CombustionEngine(int powerHP, FuelType fuel) {
        if (powerHP <= 0) throw new IllegalArgumentException("мощность должна быть > 0");
        this.powerHP = powerHP;
        this.fuel = Objects.requireNonNull(fuel);
    }

    public FuelType fuel() { return fuel; }
    @Override public int powerHP() { return powerHP; }
    @Override public boolean isRunning() { return running; }
    @Override public void start() { running = true; }
    @Override public void stop() { running = false; }

    @Override public String toString() { return "ДВС{" + fuel + ", " + powerHP + " л.с.}"; }
}

final class ElectricMotor implements Engine {
    private final int powerHP;
    private final double batteryKWh;
    private boolean running;

    ElectricMotor(int powerHP, double batteryKWh) {
        if (powerHP <= 0) throw new IllegalArgumentException("мощность должна быть > 0");
        if (batteryKWh <= 0) throw new IllegalArgumentException("ёмкость батареи должна быть > 0");
        this.powerHP = powerHP;
        this.batteryKWh = batteryKWh;
    }

    public double batteryKWh() { return batteryKWh; }
    @Override public int powerHP() { return powerHP; }
    @Override public boolean isRunning() { return running; }
    @Override public void start() { running = true; }
    @Override public void stop() { running = false; }

    @Override public String toString() { return "Электро{" + batteryKWh + " кВт·ч, " + powerHP + " л.с.}"; }
}

// ==========================
//         Конкретные транспорты
// ==========================

final class Car extends AbstractTransport<Car> implements HasEngine<CombustionEngine> {
    private final CombustionEngine engine;
    private final int doors;

    Car(String model, int capacity, MovementStrategy<Car> movement, CombustionEngine engine, int doors) {
        super(model, capacity, movement);
        if (doors <= 0) throw new IllegalArgumentException("число дверей должно быть > 0");
        this.engine = Objects.requireNonNull(engine);
        this.doors = doors;
    }

    public int doors() { return doors; }
    @Override public CombustionEngine engine() { return engine; }
    @Override protected Car self() { return this; }

    @Override public String toString() {
        return super.toString() + ", " + engine + ", дверей=" + doors;
    }
}

final class ElectricCar extends AbstractTransport<ElectricCar> implements HasEngine<ElectricMotor> {
    private final ElectricMotor engine;

    ElectricCar(String model, int capacity, MovementStrategy<ElectricCar> movement, ElectricMotor engine) {
        super(model, capacity, movement);
        this.engine = Objects.requireNonNull(engine);
    }

    @Override public ElectricMotor engine() { return engine; }
    @Override protected ElectricCar self() { return this; }

    @Override public String toString() { return super.toString() + ", " + engine; }
}

final class Bicycle extends AbstractTransport<Bicycle> {
    private final int gears;

    Bicycle(String model, int capacity, MovementStrategy<Bicycle> movement, int gears) {
        super(model, capacity, movement);
        if (gears <= 0) throw new IllegalArgumentException("число передач должно быть > 0");
        this.gears = gears;
    }

    public int gears() { return gears; }
    @Override protected Bicycle self() { return this; }
}

final class Airplane extends AbstractTransport<Airplane> implements HasEngine<CombustionEngine> {
    private final CombustionEngine engine;

    Airplane(String model, int capacity, MovementStrategy<Airplane> movement, CombustionEngine engine) {
        super(model, capacity, movement);
        this.engine = Objects.requireNonNull(engine);
    }

    @Override public CombustionEngine engine() { return engine; }
    @Override protected Airplane self() { return this; }
}

final class Ship extends AbstractTransport<Ship> implements HasEngine<CombustionEngine> {
    private final CombustionEngine engine;

    Ship(String model, int capacity, MovementStrategy<Ship> movement, CombustionEngine engine) {
        super(model, capacity, movement);
        this.engine = Objects.requireNonNull(engine);
    }

    @Override public CombustionEngine engine() { return engine; }
    @Override protected Ship self() { return this; }
}

// ==========================
//           Репозиторий
// ==========================

interface Repository<T> {
    T save(T entity);
    List<T> findAll();
}

final class InMemoryRepository<T> implements Repository<T> {
    private final List<T> data = new ArrayList<>();
    @Override public T save(T entity) { data.add(entity); return entity; }
    @Override public List<T> findAll() { return Collections.unmodifiableList(data); }
}

// ==========================
//            Фабрики
// ==========================

interface TransportFactory<T extends Transport> {
    T create(String model, int capacity);
}

interface EngineSupplier<E extends Engine> { E get(); }

final class CarFactory implements TransportFactory<Car> {
    private final MovementStrategy<Car> movement;
    private final EngineSupplier<CombustionEngine> engineSupplier;

    CarFactory(MovementStrategy<Car> movement, EngineSupplier<CombustionEngine> engineSupplier) {
        this.movement = Objects.requireNonNull(movement);
        this.engineSupplier = Objects.requireNonNull(engineSupplier);
    }

    @Override public Car create(String model, int capacity) {
        return new Car(model, capacity, movement, engineSupplier.get(), 4);
    }
}

final class ElectricCarFactory implements TransportFactory<ElectricCar> {
    private final MovementStrategy<ElectricCar> movement;
    private final EngineSupplier<ElectricMotor> engineSupplier;

    ElectricCarFactory(MovementStrategy<ElectricCar> movement, EngineSupplier<ElectricMotor> engineSupplier) {
        this.movement = Objects.requireNonNull(movement);
        this.engineSupplier = Objects.requireNonNull(engineSupplier);
    }

    @Override public ElectricCar create(String model, int capacity) {
        return new ElectricCar(model, capacity, movement, engineSupplier.get());
    }
}

final class BicycleFactory implements TransportFactory<Bicycle> {
    private final MovementStrategy<Bicycle> movement;

    BicycleFactory(MovementStrategy<Bicycle> movement) {
        this.movement = Objects.requireNonNull(movement);
    }

    @Override public Bicycle create(String model, int capacity) {
        // по умолчанию 21 передача — настройка фабрики, а не транспорта
        return new Bicycle(model, capacity, movement, 21);
    }
}

final class AirplaneFactory implements TransportFactory<Airplane> {
    private final MovementStrategy<Airplane> movement;
    private final EngineSupplier<CombustionEngine> engineSupplier;

    AirplaneFactory(MovementStrategy<Airplane> movement, EngineSupplier<CombustionEngine> engineSupplier) {
        this.movement = Objects.requireNonNull(movement);
        this.engineSupplier = Objects.requireNonNull(engineSupplier);
    }

    @Override public Airplane create(String model, int capacity) {
        return new Airplane(model, capacity, movement, engineSupplier.get());
    }
}

final class ShipFactory implements TransportFactory<Ship> {
    private final MovementStrategy<Ship> movement;
    private final EngineSupplier<CombustionEngine> engineSupplier;

    ShipFactory(MovementStrategy<Ship> movement, EngineSupplier<CombustionEngine> engineSupplier) {
        this.movement = Objects.requireNonNull(movement);
        this.engineSupplier = Objects.requireNonNull(engineSupplier);
    }

    @Override public Ship create(String model, int capacity) {
        return new Ship(model, capacity, movement, engineSupplier.get());
    }
}

// ==========================
//     Консольное приложение
// ==========================

/**
 * Небольшое консольное приложение поверх доменной модели:
 * - создаёт разные типы транспорта по вводу пользователя;
 * - хранит их в репозитории в памяти;
 * - позволяет запускать/останавливать двигатель (если он есть) и выполнять движение (Strategy).
 *
 * UI изолирован от домена: доменные классы не знают о консоли.
 */
final class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Repository<Transport> fleet = new InMemoryRepository<>();

    // Готовые стратегии движения — внедряются в транспорты
    private final MovementStrategy<Car> roadCar = new RoadMovement<>();
    private final MovementStrategy<ElectricCar> roadEv = new RoadMovement<>();
    private final MovementStrategy<Bicycle> roadBike = new RoadMovement<>();
    private final MovementStrategy<Airplane> air = new AirMovement<>();
    private final MovementStrategy<Ship> water = new WaterMovement<>();

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("Меню:");
            System.out.println(" 1) Создать автомобиль");
            System.out.println(" 2) Создать самолёт");
            System.out.println(" 3) Создать корабль");
            System.out.println(" 4) Создать велосипед");
            System.out.println(" 5) Создать электромобиль");
            System.out.println(" 6) Список транспорта");
            System.out.println(" 7) Операции с транспортом");
            System.out.println(" 0) Выход");
            int c = readInt("Выбор: ");
            try {
                switch (c) {
                    case 1 -> createCar();
                    case 2 -> createAirplane();
                    case 3 -> createShip();
                    case 4 -> createBicycle();
                    case 5 -> createElectricCar();
                    case 6 -> listFleet();
                    case 7 -> operateTransport();
                    case 0 -> exit = true;
                    default -> System.out.println("Неизвестная команда");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void createCar() {
        System.out.println("Создание автомобиля");
        String model = readLine("Модель: ");
        int cap = readIntNonNegative("Вместимость: ");
        int hp = readIntPositive("Мощность двигателя (л.с.): ");
        FuelType fuel = chooseFuel(List.of(FuelType.GASOLINE, FuelType.DIESEL));
        int doors = readIntPositive("Количество дверей: ");
        Car car = new Car(model, cap, roadCar, new CombustionEngine(hp, fuel), doors);
        fleet.save(car);
        System.out.println("Создано: " + car);
    }

    private void createAirplane() {
        System.out.println("Создание самолёта");
        String model = readLine("Модель: ");
        int cap = readIntNonNegative("Вместимость: ");
        int hp = readIntPositive("Мощность двигателя (л.с.): ");
        Airplane airplane = new Airplane(model, cap, air, new CombustionEngine(hp, FuelType.JET_FUEL));
        fleet.save(airplane);
        System.out.println("Создано: " + airplane);
    }

    private void createShip() {
        System.out.println("Создание корабля");
        String model = readLine("Модель: ");
        int cap = readIntNonNegative("Вместимость: ");
        int hp = readIntPositive("Мощность двигателя (л.с.): ");
        Ship ship = new Ship(model, cap, water, new CombustionEngine(hp, FuelType.DIESEL));
        fleet.save(ship);
        System.out.println("Создано: " + ship);
    }

    private void createBicycle() {
        System.out.println("Создание велосипеда");
        String model = readLine("Модель: ");
        int cap = readIntWithin("Вместимость [1..4]: ", 1, 4);
        int gears = readIntPositive("Число передач: ");
        Bicycle bicycle = new Bicycle(model, cap, roadBike, gears);
        fleet.save(bicycle);
        System.out.println("Создано: " + bicycle);
    }

    private void createElectricCar() {
        System.out.println("Создание электромобиля");
        String model = readLine("Модель: ");
        int cap = readIntNonNegative("Вместимость: ");
        int hp = readIntPositive("Мощность электромотора (л.с.): ");
        double kwh = readDoublePositive("Ёмкость батареи (кВт·ч): ");
        ElectricCar ev = new ElectricCar(model, cap, roadEv, new ElectricMotor(hp, kwh));
        fleet.save(ev);
        System.out.println("Создано: " + ev);
    }

    private void listFleet() {
        List<Transport> all = fleet.findAll();
        if (all.isEmpty()) {
            System.out.println("Парк пуст.");
            return;
        }
        System.out.println("Содержимое парка:");
        for (int i = 0; i < all.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, all.get(i));
        }
    }

    private void operateTransport() {
        List<Transport> all = fleet.findAll();
        if (all.isEmpty()) {
            System.out.println("Нечего выбирать — парк пуст.");
            return;
        }
        listFleet();
        int idx = readIntWithin("Выберите номер транспорта: ", 1, all.size()) - 1;
        Transport t = all.get(idx);
        System.out.println("Выбран: " + t);

        boolean back = false;
        while (!back) {
            System.out.println("  Действия:");
            System.out.println("   1) Информация");
            System.out.println("   2) Запустить двигатель");
            System.out.println("   3) Заглушить двигатель");
            System.out.println("   4) Движение");
            System.out.println("   0) Назад");
            int c = readInt("Выбор: ");
            switch (c) {
                case 1 -> System.out.println(t);
                case 2 -> {
                    if (t instanceof HasEngine<?> he) {
                        Engine e = ((HasEngine<?>) t).engine();
                        if (!e.isRunning()) { e.start(); System.out.println("Двигатель запущен."); }
                        else System.out.println("Двигатель уже работает.");
                    } else System.out.println("Двигателя нет.");
                }
                case 3 -> {
                    if (t instanceof HasEngine<?> he) {
                        Engine e = ((HasEngine<?>) t).engine();
                        if (e.isRunning()) { e.stop(); System.out.println("Двигатель остановлен."); }
                        else System.out.println("Двигатель уже остановлен.");
                    } else System.out.println("Двигателя нет.");
                }
                case 4 -> t.move();
                case 0 -> back = true;
                default -> System.out.println("Неизвестная команда");
            }
        }
    }

    // --------- Ввод с проверкой ---------

    private String readLine(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("Повторите ввод: ");
            s = scanner.nextLine();
        }
        return s.trim();
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private int readIntPositive(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v > 0) return v;
            System.out.println("Число должно быть > 0.");
        }
    }

    private int readIntNonNegative(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v >= 0) return v;
            System.out.println("Число должно быть ≥ 0.");
        }
    }

    private int readIntWithin(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v >= min && v <= max) return v;
            System.out.printf("Введите число в диапазоне [%d..%d].%n", min, max);
        }
    }

    private double readDoublePositive(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String s = scanner.nextLine().trim().replace(',', '.');
                double v = Double.parseDouble(s);
                if (v > 0) return v;
            } catch (Exception ignored) { }
            System.out.println("Введите положительное число (дробная часть допустима).");
        }
    }

    private FuelType chooseFuel(List<FuelType> options) {
        System.out.println("Топливо:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, options.get(i));
        }
        int idx = readIntWithin("Выбор: ", 1, options.size());
        return options.get(idx - 1);
    }
}

