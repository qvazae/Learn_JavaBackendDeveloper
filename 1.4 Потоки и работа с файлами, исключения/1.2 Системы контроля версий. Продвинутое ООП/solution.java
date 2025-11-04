?import java.util.*;

// Основной класс с точкой входа. Имя совпадает с именем файла.
public class solution {
    public static void main(String[] args) {
    p    // ??????? ??? ?????? ?? ????????????? ????? ? ?????? ??????     p    try {
        new ConsoleApp().run();
    p    } catch (Exception e) {     p        System.out.println("????????? ?????? ?????/??????: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // ??????????? ??????: ????????? ????????? ??? ???????     p    }
    }
}

// ==========================
//      ИЕРАРХИЯ ТРАНСПОРТА
// ==========================

// Пример применения Enum: виды топлива
enum FuelType {
    GASOLINE("Бензин"),
    DIESEL("Дизель"),
    JET_FUEL("Авиа-керосин"),
    ELECTRIC("Электричество");

    private final String title;

    FuelType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

// Запечатанный абстрактный базовый класс для всех видов транспорта
sealed abstract class Transport permits Car, Airplane, Ship, Bicycle, ElectricCar {
    protected final String model;
    protected final int capacity; // пассажировместимость

    protected Transport(String model, int capacity) {
        this.model = Objects.requireNonNullElse(model, "Нет-модели");
        if (capacity < 0) throw new IllegalArgumentException("Вместимость не может быть отрицательной");
        this.capacity = capacity;
    }

    public String model() { return model; }
    public int capacity() { return capacity; }

    public abstract double getMaxSpeed();
    public abstract void move();

    public String basicInfo() {
        return "%s (вместимость: %d)".formatted(model, capacity);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + basicInfo();
    }
}

// Интерфейс для транспорта с двигателем
interface EnginePowered {
    Engine getEngine();

    default void startEngine() {
        if (!getEngine().isRunning()) {
            getEngine().start();
            System.out.println("Двигатель запущен.");
        } else {
            System.out.println("Двигатель уже работает.");
        }
    }

    default void stopEngine() {
        if (getEngine().isRunning()) {
            getEngine().stop();
            System.out.println("Двигатель остановлен.");
        } else {
            System.out.println("Двигатель уже остановлен.");
        }
    }
}

// Запечатанный абстрактный класс «Двигатель»
sealed abstract class Engine permits CombustionEngine, ElectricMotor {
    protected final int powerHP;
    private boolean running;

    protected Engine(int powerHP) {
        if (powerHP <= 0) throw new IllegalArgumentException("Мощность должна быть положительной");
        this.powerHP = powerHP;
        this.running = false;
    }

    public int getPowerHP() { return powerHP; }
    public boolean isRunning() { return running; }
    public void start() { this.running = true; }
    public void stop() { this.running = false; }
}

// Конечная реализация для ДВС
final class CombustionEngine extends Engine {
    private final FuelType fuelType;

    public CombustionEngine(int powerHP, FuelType fuelType) {
        super(powerHP);
        this.fuelType = Objects.requireNonNull(fuelType);
    }

    public FuelType getFuelType() { return fuelType; }

    @Override
    public String toString() {
        return "ДВС %s, %d л.с.".formatted(
                fuelType,
                getPowerHP()
        );
    }
}

// Конечная реализация для электрического мотора
final class ElectricMotor extends Engine {
    private final double batteryKWh;

    public ElectricMotor(int powerHP, double batteryKWh) {
        super(powerHP);
        if (batteryKWh <= 0) throw new IllegalArgumentException("Ёмкость должна быть положительной");
        this.batteryKWh = batteryKWh;
    }

    public double getBatteryKWh() { return batteryKWh; }

    @Override
    public String toString() {
        return "Электромотор, %d л.с., %.1f кВт·ч".formatted(getPowerHP(), batteryKWh);
    }
}

// --------------------------
// Конечные виды транспорта
// --------------------------

final class Car extends Transport implements EnginePowered {
    private final CombustionEngine engine;
    private final int doors;

    public Car(String model, int capacity, CombustionEngine engine, int doors) {
        super(model, capacity);
        if (doors <= 0) throw new IllegalArgumentException("Количество дверей должно быть положительным");
        this.engine = Objects.requireNonNull(engine);
        this.doors = doors;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 200; }

    @Override
    public void move() {
        System.out.println("Автомобиль едет по дороге...");
    }

    @Override
    public String toString() {
        return super.toString() + ", двери: " + doors + ", двигатель: " + engine;
    }
}

final class Airplane extends Transport implements EnginePowered {
    private final CombustionEngine engine; // упрощённо: один двигатель
    private final double wingspan; // размах крыла, м

    public Airplane(String model, int capacity, CombustionEngine engine, double wingspan) {
        super(model, capacity);
        if (wingspan <= 0) throw new IllegalArgumentException("Размах крыла должен быть положительным");
        this.engine = Objects.requireNonNull(engine);
        this.wingspan = wingspan;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 900; }

    @Override
    public void move() {
        System.out.println("Самолёт разгоняется и взлетает...");
    }

    @Override
    public String toString() {
        return super.toString() + ", крыло: " + wingspan + " м, двигатель: " + engine;
    }
}

final class Ship extends Transport implements EnginePowered {
    private final CombustionEngine engine;
    private final double displacementTons; // водоизмещение, т

    public Ship(String model, int capacity, CombustionEngine engine, double displacementTons) {
        super(model, capacity);
        if (displacementTons <= 0) throw new IllegalArgumentException("Водоизмещение должно быть положительным");
        this.engine = Objects.requireNonNull(engine);
        this.displacementTons = displacementTons;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 60; }

    @Override
    public void move() {
        System.out.println("Корабль идёт по воде...");
    }

    @Override
    public String toString() {
        return super.toString() + ", водоизмещение: " + displacementTons + " т, двигатель: " + engine;
    }
}

final class Bicycle extends Transport {
    private final int gears;

    public Bicycle(String model, int capacity, int gears) {
        super(model, capacity);
        if (gears <= 0) throw new IllegalArgumentException("Число передач должно быть положительным");
        this.gears = gears;
    }

    @Override
    public double getMaxSpeed() { return 40; }

    @Override
    public void move() {
        System.out.println("Велосипедист крутит педали...");
    }

    @Override
    public String toString() {
        return super.toString() + ", передач: " + gears;
    }
}

final class ElectricCar extends Transport implements EnginePowered {
    private final ElectricMotor motor;
    private final int doors;

    public ElectricCar(String model, int capacity, ElectricMotor motor, int doors) {
        super(model, capacity);
        if (doors <= 0) throw new IllegalArgumentException("Количество дверей должно быть положительным");
        this.motor = Objects.requireNonNull(motor);
        this.doors = doors;
    }

    @Override
    public Engine getEngine() { return motor; }

    @Override
    public double getMaxSpeed() { return 220; }

    @Override
    public void move() {
        System.out.println("Электромобиль тихо разгоняется...");
    }

    @Override
    public String toString() {
        return super.toString() + ", двери: " + doors + ", двигатель: " + motor;
    }
}

// ==========================
//       КОНСОЛЬНОЕ ПРИЛОЖЕНИЕ
// ==========================

class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Transport> fleet = new ArrayList<>();

    public void run() {
        System.out.println("Транспортная система (Sealed/Enum/Abstraction)\n");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Выберите пункт: ");
            try {
                switch (choice) {
                    case 1 -> createCar();
                    case 2 -> createAirplane();
                    case 3 -> createShip();
                    case 4 -> createBicycle();
                    case 5 -> createElectricCar();
                    case 6 -> listFleet();
                    case 7 -> operateTransport();
                    case 0 -> running = false;
                    default -> System.out.println("Неизвестный пункт меню");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Ошибка ввода: " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Выход. До свидания!");
    }

    private void printMenu() {
        System.out.println("Меню:");
        System.out.println("  1) Создать автомобиль");
        System.out.println("  2) Создать самолёт");
        System.out.println("  3) Создать корабль");
        System.out.println("  4) Создать велосипед");
        System.out.println("  5) Создать электромобиль");
        System.out.println("  6) Показать весь транспорт");
        System.out.println("  7) Управление транспортом");
        System.out.println("  0) Выход");
    }

    private void createCar() {
        System.out.println("Создание автомобиля");
        String model = readLine("Модель: ");
        int cap = readInt("Вместимость (кол-во мест): ");
        int hp = readInt("Мощность (л.с.): ");
        FuelType fuel = chooseFuel(List.of(FuelType.GASOLINE, FuelType.DIESEL));
        int doors = readInt("Количество дверей: ");
        Car car = new Car(model, cap, new CombustionEngine(hp, fuel), doors);
        fleet.add(car);
        System.out.println("Добавлено: " + car);
    }

    private void createAirplane() {
        System.out.println("Создание самолёта");
        String model = readLine("Модель: ");
        int cap = readInt("Вместимость (кол-во мест): ");
        int hp = readInt("Мощность двигателя (л.с.): ");
        double wing = readDouble("Размах крыла (м): ");
        Airplane plane = new Airplane(model, cap, new CombustionEngine(hp, FuelType.JET_FUEL), wing);
        fleet.add(plane);
        System.out.println("Добавлено: " + plane);
    }

    private void createShip() {
        System.out.println("Создание корабля");
        String model = readLine("Модель: ");
        int cap = readInt("Вместимость (кол-во мест): ");
        int hp = readInt("Мощность двигателя (л.с.): ");
        double disp = readDouble("Водоизмещение (т): ");
        Ship ship = new Ship(model, cap, new CombustionEngine(hp, FuelType.DIESEL), disp);
        fleet.add(ship);
        System.out.println("Добавлено: " + ship);
    }

    private void createBicycle() {
        System.out.println("Создание велосипеда");
        String model = readLine("Модель: ");
        int cap = readInt("Вместимость (обычно 1): ");
        int gears = readInt("Количество передач: ");
        Bicycle bicycle = new Bicycle(model, cap, gears);
        fleet.add(bicycle);
        System.out.println("Добавлено: " + bicycle);
    }

    private void createElectricCar() {
        System.out.println("Создание электромобиля");
        String model = readLine("Модель: ");
        int cap = readInt("Вместимость (кол-во мест): ");
        int hp = readInt("Мощность электромотора (л.с.): ");
        double kwh = readDouble("Ёмкость батареи (кВт·ч): ");
        int doors = readInt("Количество дверей: ");
        ElectricCar ev = new ElectricCar(model, cap, new ElectricMotor(hp, kwh), doors);
        fleet.add(ev);
        System.out.println("Добавлено: " + ev);
    }

    private void listFleet() {
        if (fleet.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }
        System.out.println("Текущий парк:");
        for (int i = 0; i < fleet.size(); i++) {
            Transport t = fleet.get(i);
            System.out.printf("%d) %s%n", i + 1, t);
        }
    }

    private void operateTransport() {
        if (fleet.isEmpty()) {
            System.out.println("Нет доступного транспорта.");
            return;
        }
        listFleet();
        int idx = readIntWithin("Выберите номер транспорта: ", 1, fleet.size()) - 1;
        Transport t = fleet.get(idx);
        System.out.println("Вы выбрали: " + t);

        boolean back = false;
        while (!back) {
            System.out.println("  Действия:");
            System.out.println("   1) Информация");
            System.out.println("   2) Двигатель: старт");
            System.out.println("   3) Двигатель: стоп");
            System.out.println("   4) Поехать/Плыть/Лететь");
            System.out.println("   0) Назад");
            int c = readInt("Выбор: ");
            switch (c) {
                case 1 -> System.out.println(t);
                case 2 -> {
                    if (t instanceof EnginePowered ep) ep.startEngine();
                    else System.out.println("Двигателя нет.");
                }
                case 3 -> {
                    if (t instanceof EnginePowered ep) ep.stopEngine();
                    else System.out.println("Двигателя нет.");
                }
                case 4 -> t.move();
                case 0 -> back = true;
                default -> System.out.println("Неизвестная команда");
            }
        }
    }

    // --------- Вспомогательные методы ввода ---------

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
            String s = scanner.nextLine();
            try {
                return Integer.parseInt(s.trim());
            } catch (Exception e) {
                System.out.println("Введите целое число.");
            }
        }
    }

    private int readIntWithin(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt + "[" + min + "-" + max + "]: ");
            if (v >= min && v <= max) return v;
            System.out.println("Число вне диапазона.");
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Double.parseDouble(s.trim().replace(",", "."));
            } catch (Exception e) {
                System.out.println("Введите число (можно с точкой).");
            }
        }
    }

    private FuelType chooseFuel(List<FuelType> options) {
        System.out.println("Тип топлива:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, options.get(i));
        }
        int idx = readIntWithin("Выбор: ", 1, options.size());
        return options.get(idx - 1);
    }
}

