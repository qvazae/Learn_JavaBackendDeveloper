import java.util.*;

// ÐžÑÐ½Ð¾Ð²Ð½Ð¾Ð¹ ÐºÐ»Ð°ÑÑ Ñ Ñ‚Ð¾Ñ‡ÐºÐ¾Ð¹ Ð²Ñ…Ð¾Ð´Ð°. Ð˜Ð¼Ñ ÑÐ¾Ð²Ð¿Ð°Ð´Ð°ÐµÑ‚ Ñ Ð¸Ð¼ÐµÐ½ÐµÐ¼ Ñ„Ð°Ð¹Ð»Ð°.
public class solution {
    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        new ConsoleApp().run();
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }
}

// ==========================
//      Ð˜Ð•Ð ÐÐ Ð¥Ð˜Ð¯ Ð¢Ð ÐÐÐ¡ÐŸÐžÐ Ð¢Ð
// ==========================

// ÐŸÑ€Ð¸Ð¼ÐµÑ€ Ð¿Ñ€Ð¸Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ Enum: Ð²Ð¸Ð´Ñ‹ Ñ‚Ð¾Ð¿Ð»Ð¸Ð²Ð°
enum FuelType {
    GASOLINE("Ð‘ÐµÐ½Ð·Ð¸Ð½"),
    DIESEL("Ð”Ð¸Ð·ÐµÐ»ÑŒ"),
    JET_FUEL("ÐÐ²Ð¸Ð°-ÐºÐµÑ€Ð¾ÑÐ¸Ð½"),
    ELECTRIC("Ð­Ð»ÐµÐºÑ‚Ñ€Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾");

    private final String title;

    FuelType(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}

// Ð—Ð°Ð¿ÐµÑ‡Ð°Ñ‚Ð°Ð½Ð½Ñ‹Ð¹ Ð°Ð±ÑÑ‚Ñ€Ð°ÐºÑ‚Ð½Ñ‹Ð¹ Ð±Ð°Ð·Ð¾Ð²Ñ‹Ð¹ ÐºÐ»Ð°ÑÑ Ð´Ð»Ñ Ð²ÑÐµÑ… Ð²Ð¸Ð´Ð¾Ð² Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°
sealed abstract class Transport permits Car, Airplane, Ship, Bicycle, ElectricCar {
    protected final String model;
    protected final int capacity; // Ð¿Ð°ÑÑÐ°Ð¶Ð¸Ñ€Ð¾Ð²Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ

    protected Transport(String model, int capacity) {
        this.model = Objects.requireNonNullElse(model, "ÐÐµÑ‚-Ð¼Ð¾Ð´ÐµÐ»Ð¸");
        if (capacity < 0) throw new IllegalArgumentException("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ Ð½Ðµ Ð¼Ð¾Ð¶ÐµÑ‚ Ð±Ñ‹Ñ‚ÑŒ Ð¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹");
        this.capacity = capacity;
    }

    public String model() { return model; }
    public int capacity() { return capacity; }

    public abstract double getMaxSpeed();
    public abstract void move();

    public String basicInfo() {
        return "%s (Ð²Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ: %d)".formatted(model, capacity);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + basicInfo();
    }
}

// Ð˜Ð½Ñ‚ÐµÑ€Ñ„ÐµÐ¹Ñ Ð´Ð»Ñ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð° Ñ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÐµÐ¼
interface EnginePowered {
    Engine getEngine();

    default void startEngine() {
        if (!getEngine().isRunning()) {
            getEngine().start();
            System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½.");
        } else {
            System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ ÑƒÐ¶Ðµ Ñ€Ð°Ð±Ð¾Ñ‚Ð°ÐµÑ‚.");
        }
    }

    default void stopEngine() {
        if (getEngine().isRunning()) {
            getEngine().stop();
            System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ Ð¾ÑÑ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½.");
        } else {
            System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ ÑƒÐ¶Ðµ Ð¾ÑÑ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½.");
        }
    }
}

// Ð—Ð°Ð¿ÐµÑ‡Ð°Ñ‚Ð°Ð½Ð½Ñ‹Ð¹ Ð°Ð±ÑÑ‚Ñ€Ð°ÐºÑ‚Ð½Ñ‹Ð¹ ÐºÐ»Ð°ÑÑ Â«Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒÂ»
sealed abstract class Engine permits CombustionEngine, ElectricMotor {
    protected final int powerHP;
    private boolean running;

    protected Engine(int powerHP) {
        if (powerHP <= 0) throw new IllegalArgumentException("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹");
        this.powerHP = powerHP;
        this.running = false;
    }

    public int getPowerHP() { return powerHP; }
    public boolean isRunning() { return running; }
    public void start() { this.running = true; }
    public void stop() { this.running = false; }
}

// ÐšÐ¾Ð½ÐµÑ‡Ð½Ð°Ñ Ñ€ÐµÐ°Ð»Ð¸Ð·Ð°Ñ†Ð¸Ñ Ð´Ð»Ñ Ð”Ð’Ð¡
final class CombustionEngine extends Engine {
    private final FuelType fuelType;

    public CombustionEngine(int powerHP, FuelType fuelType) {
        super(powerHP);
        this.fuelType = Objects.requireNonNull(fuelType);
    }

    public FuelType getFuelType() { return fuelType; }

    @Override
    public String toString() {
        return "Ð”Ð’Ð¡ %s, %d Ð».Ñ.".formatted(
                fuelType,
                getPowerHP()
        );
    }
}

// ÐšÐ¾Ð½ÐµÑ‡Ð½Ð°Ñ Ñ€ÐµÐ°Ð»Ð¸Ð·Ð°Ñ†Ð¸Ñ Ð´Ð»Ñ ÑÐ»ÐµÐºÑ‚Ñ€Ð¸Ñ‡ÐµÑÐºÐ¾Ð³Ð¾ Ð¼Ð¾Ñ‚Ð¾Ñ€Ð°
final class ElectricMotor extends Engine {
    private final double batteryKWh;

    public ElectricMotor(int powerHP, double batteryKWh) {
        super(powerHP);
        if (batteryKWh <= 0) throw new IllegalArgumentException("ÐÐ¼ÐºÐ¾ÑÑ‚ÑŒ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ð¹");
        this.batteryKWh = batteryKWh;
    }

    public double getBatteryKWh() { return batteryKWh; }

    @Override
    public String toString() {
        return "Ð­Ð»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ñ‚Ð¾Ñ€, %d Ð».Ñ., %.1f ÐºÐ’Ñ‚Â·Ñ‡".formatted(getPowerHP(), batteryKWh);
    }
}

// --------------------------
// ÐšÐ¾Ð½ÐµÑ‡Ð½Ñ‹Ðµ Ð²Ð¸Ð´Ñ‹ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°
// --------------------------

final class Car extends Transport implements EnginePowered {
    private final CombustionEngine engine;
    private final int doors;

    public Car(String model, int capacity, CombustionEngine engine, int doors) {
        super(model, capacity);
        if (doors <= 0) throw new IllegalArgumentException("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼");
        this.engine = Objects.requireNonNull(engine);
        this.doors = doors;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 200; }

    @Override
    public void move() {
        System.out.println("ÐÐ²Ñ‚Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ ÐµÐ´ÐµÑ‚ Ð¿Ð¾ Ð´Ð¾Ñ€Ð¾Ð³Ðµ...");
    }

    @Override
    public String toString() {
        return super.toString() + ", Ð´Ð²ÐµÑ€Ð¸: " + doors + ", Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: " + engine;
    }
}

final class Airplane extends Transport implements EnginePowered {
    private final CombustionEngine engine; // ÑƒÐ¿Ñ€Ð¾Ñ‰Ñ‘Ð½Ð½Ð¾: Ð¾Ð´Ð¸Ð½ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ
    private final double wingspan; // Ñ€Ð°Ð·Ð¼Ð°Ñ… ÐºÑ€Ñ‹Ð»Ð°, Ð¼

    public Airplane(String model, int capacity, CombustionEngine engine, double wingspan) {
        super(model, capacity);
        if (wingspan <= 0) throw new IllegalArgumentException("Ð Ð°Ð·Ð¼Ð°Ñ… ÐºÑ€Ñ‹Ð»Ð° Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼");
        this.engine = Objects.requireNonNull(engine);
        this.wingspan = wingspan;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 900; }

    @Override
    public void move() {
        System.out.println("Ð¡Ð°Ð¼Ð¾Ð»Ñ‘Ñ‚ Ñ€Ð°Ð·Ð³Ð¾Ð½ÑÐµÑ‚ÑÑ Ð¸ Ð²Ð·Ð»ÐµÑ‚Ð°ÐµÑ‚...");
    }

    @Override
    public String toString() {
        return super.toString() + ", ÐºÑ€Ñ‹Ð»Ð¾: " + wingspan + " Ð¼, Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: " + engine;
    }
}

final class Ship extends Transport implements EnginePowered {
    private final CombustionEngine engine;
    private final double displacementTons; // Ð²Ð¾Ð´Ð¾Ð¸Ð·Ð¼ÐµÑ‰ÐµÐ½Ð¸Ðµ, Ñ‚

    public Ship(String model, int capacity, CombustionEngine engine, double displacementTons) {
        super(model, capacity);
        if (displacementTons <= 0) throw new IllegalArgumentException("Ð’Ð¾Ð´Ð¾Ð¸Ð·Ð¼ÐµÑ‰ÐµÐ½Ð¸Ðµ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼");
        this.engine = Objects.requireNonNull(engine);
        this.displacementTons = displacementTons;
    }

    @Override
    public Engine getEngine() { return engine; }

    @Override
    public double getMaxSpeed() { return 60; }

    @Override
    public void move() {
        System.out.println("ÐšÐ¾Ñ€Ð°Ð±Ð»ÑŒ Ð¸Ð´Ñ‘Ñ‚ Ð¿Ð¾ Ð²Ð¾Ð´Ðµ...");
    }

    @Override
    public String toString() {
        return super.toString() + ", Ð²Ð¾Ð´Ð¾Ð¸Ð·Ð¼ÐµÑ‰ÐµÐ½Ð¸Ðµ: " + displacementTons + " Ñ‚, Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: " + engine;
    }
}

final class Bicycle extends Transport {
    private final int gears;

    public Bicycle(String model, int capacity, int gears) {
        super(model, capacity);
        if (gears <= 0) throw new IllegalArgumentException("Ð§Ð¸ÑÐ»Ð¾ Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼");
        this.gears = gears;
    }

    @Override
    public double getMaxSpeed() { return 40; }

    @Override
    public void move() {
        System.out.println("Ð’ÐµÐ»Ð¾ÑÐ¸Ð¿ÐµÐ´Ð¸ÑÑ‚ ÐºÑ€ÑƒÑ‚Ð¸Ñ‚ Ð¿ÐµÐ´Ð°Ð»Ð¸...");
    }

    @Override
    public String toString() {
        return super.toString() + ", Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡: " + gears;
    }
}

final class ElectricCar extends Transport implements EnginePowered {
    private final ElectricMotor motor;
    private final int doors;

    public ElectricCar(String model, int capacity, ElectricMotor motor, int doors) {
        super(model, capacity);
        if (doors <= 0) throw new IllegalArgumentException("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼");
        this.motor = Objects.requireNonNull(motor);
        this.doors = doors;
    }

    @Override
    public Engine getEngine() { return motor; }

    @Override
    public double getMaxSpeed() { return 220; }

    @Override
    public void move() {
        System.out.println("Ð­Ð»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ Ñ‚Ð¸Ñ…Ð¾ Ñ€Ð°Ð·Ð³Ð¾Ð½ÑÐµÑ‚ÑÑ...");
    }

    @Override
    public String toString() {
        return super.toString() + ", Ð´Ð²ÐµÑ€Ð¸: " + doors + ", Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: " + motor;
    }
}

// ==========================
//       ÐšÐžÐÐ¡ÐžÐ›Ð¬ÐÐžÐ• ÐŸÐ Ð˜Ð›ÐžÐ–Ð•ÐÐ˜Ð•
// ==========================

class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final List<Transport> fleet = new ArrayList<>();

    public void run() {
        System.out.println("Ð¢Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð½Ð°Ñ ÑÐ¸ÑÑ‚ÐµÐ¼Ð° (Sealed/Enum/Abstraction)\n");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð¿ÑƒÐ½ÐºÑ‚: ");
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
                    default -> System.out.println("ÐÐµÐ¸Ð·Ð²ÐµÑÑ‚Ð½Ñ‹Ð¹ Ð¿ÑƒÐ½ÐºÑ‚ Ð¼ÐµÐ½ÑŽ");
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð²Ð²Ð¾Ð´Ð°: " + ex.getMessage());
            }
            System.out.println();
        }
        System.out.println("Ð’Ñ‹Ñ…Ð¾Ð´. Ð”Ð¾ ÑÐ²Ð¸Ð´Ð°Ð½Ð¸Ñ!");
    }

    private void printMenu() {
        System.out.println("ÐœÐµÐ½ÑŽ:");
        System.out.println("  1) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ Ð°Ð²Ñ‚Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ");
        System.out.println("  2) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÑÐ°Ð¼Ð¾Ð»Ñ‘Ñ‚");
        System.out.println("  3) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ");
        System.out.println("  4) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ Ð²ÐµÐ»Ð¾ÑÐ¸Ð¿ÐµÐ´");
        System.out.println("  5) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ");
        System.out.println("  6) ÐŸÐ¾ÐºÐ°Ð·Ð°Ñ‚ÑŒ Ð²ÐµÑÑŒ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚");
        System.out.println("  7) Ð£Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð¾Ð¼");
        System.out.println("  0) Ð’Ñ‹Ñ…Ð¾Ð´");
    }

    private void createCar() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð°Ð²Ñ‚Ð¾Ð¼Ð¾Ð±Ð¸Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readInt("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ (ÐºÐ¾Ð»-Ð²Ð¾ Ð¼ÐµÑÑ‚): ");
        int hp = readInt("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ (Ð».Ñ.): ");
        FuelType fuel = chooseFuel(List.of(FuelType.GASOLINE, FuelType.DIESEL));
        int doors = readInt("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹: ");
        Car car = new Car(model, cap, new CombustionEngine(hp, fuel), doors);
        fleet.add(car);
        System.out.println("Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾: " + car);
    }

    private void createAirplane() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÑÐ°Ð¼Ð¾Ð»Ñ‘Ñ‚Ð°");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readInt("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ (ÐºÐ¾Ð»-Ð²Ð¾ Ð¼ÐµÑÑ‚): ");
        int hp = readInt("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ (Ð».Ñ.): ");
        double wing = readDouble("Ð Ð°Ð·Ð¼Ð°Ñ… ÐºÑ€Ñ‹Ð»Ð° (Ð¼): ");
        Airplane plane = new Airplane(model, cap, new CombustionEngine(hp, FuelType.JET_FUEL), wing);
        fleet.add(plane);
        System.out.println("Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾: " + plane);
    }

    private void createShip() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readInt("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ (ÐºÐ¾Ð»-Ð²Ð¾ Ð¼ÐµÑÑ‚): ");
        int hp = readInt("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ (Ð».Ñ.): ");
        double disp = readDouble("Ð’Ð¾Ð´Ð¾Ð¸Ð·Ð¼ÐµÑ‰ÐµÐ½Ð¸Ðµ (Ñ‚): ");
        Ship ship = new Ship(model, cap, new CombustionEngine(hp, FuelType.DIESEL), disp);
        fleet.add(ship);
        System.out.println("Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾: " + ship);
    }

    private void createBicycle() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð²ÐµÐ»Ð¾ÑÐ¸Ð¿ÐµÐ´Ð°");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readInt("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ (Ð¾Ð±Ñ‹Ñ‡Ð½Ð¾ 1): ");
        int gears = readInt("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡: ");
        Bicycle bicycle = new Bicycle(model, cap, gears);
        fleet.add(bicycle);
        System.out.println("Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾: " + bicycle);
    }

    private void createElectricCar() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ð±Ð¸Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readInt("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ (ÐºÐ¾Ð»-Ð²Ð¾ Ð¼ÐµÑÑ‚): ");
        int hp = readInt("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ñ‚Ð¾Ñ€Ð° (Ð».Ñ.): ");
        double kwh = readDouble("ÐÐ¼ÐºÐ¾ÑÑ‚ÑŒ Ð±Ð°Ñ‚Ð°Ñ€ÐµÐ¸ (ÐºÐ’Ñ‚Â·Ñ‡): ");
        int doors = readInt("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹: ");
        ElectricCar ev = new ElectricCar(model, cap, new ElectricMotor(hp, kwh), doors);
        fleet.add(ev);
        System.out.println("Ð”Ð¾Ð±Ð°Ð²Ð»ÐµÐ½Ð¾: " + ev);
    }

    private void listFleet() {
        if (fleet.isEmpty()) {
            System.out.println("Ð¡Ð¿Ð¸ÑÐ¾Ðº Ð¿ÑƒÑÑ‚.");
            return;
        }
        System.out.println("Ð¢ÐµÐºÑƒÑ‰Ð¸Ð¹ Ð¿Ð°Ñ€Ðº:");
        for (int i = 0; i < fleet.size(); i++) {
            Transport t = fleet.get(i);
            System.out.printf("%d) %s%n", i + 1, t);
        }
    }

    private void operateTransport() {
        if (fleet.isEmpty()) {
            System.out.println("ÐÐµÑ‚ Ð´Ð¾ÑÑ‚ÑƒÐ¿Ð½Ð¾Ð³Ð¾ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°.");
            return;
        }
        listFleet();
        int idx = readIntWithin("Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð½Ð¾Ð¼ÐµÑ€ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°: ", 1, fleet.size()) - 1;
        Transport t = fleet.get(idx);
        System.out.println("Ð’Ñ‹ Ð²Ñ‹Ð±Ñ€Ð°Ð»Ð¸: " + t);

        boolean back = false;
        while (!back) {
            System.out.println("  Ð”ÐµÐ¹ÑÑ‚Ð²Ð¸Ñ:");
            System.out.println("   1) Ð˜Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ");
            System.out.println("   2) Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: ÑÑ‚Ð°Ñ€Ñ‚");
            System.out.println("   3) Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ: ÑÑ‚Ð¾Ð¿");
            System.out.println("   4) ÐŸÐ¾ÐµÑ…Ð°Ñ‚ÑŒ/ÐŸÐ»Ñ‹Ñ‚ÑŒ/Ð›ÐµÑ‚ÐµÑ‚ÑŒ");
            System.out.println("   0) ÐÐ°Ð·Ð°Ð´");
            int c = readInt("Ð’Ñ‹Ð±Ð¾Ñ€: ");
            switch (c) {
                case 1 -> System.out.println(t);
                case 2 -> {
                    if (t instanceof EnginePowered ep) ep.startEngine();
                    else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ Ð½ÐµÑ‚.");
                }
                case 3 -> {
                    if (t instanceof EnginePowered ep) ep.stopEngine();
                    else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ Ð½ÐµÑ‚.");
                }
                case 4 -> t.move();
                case 0 -> back = true;
                default -> System.out.println("ÐÐµÐ¸Ð·Ð²ÐµÑÑ‚Ð½Ð°Ñ ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°");
            }
        }
    }

    // --------- Ð’ÑÐ¿Ð¾Ð¼Ð¾Ð³Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ðµ Ð¼ÐµÑ‚Ð¾Ð´Ñ‹ Ð²Ð²Ð¾Ð´Ð° ---------

    private String readLine(String prompt) {
        System.out.print(prompt);
        String s = scanner.nextLine();
        while (s == null || s.isBlank()) {
            System.out.print("ÐŸÐ¾Ð²Ñ‚Ð¾Ñ€Ð¸Ñ‚Ðµ Ð²Ð²Ð¾Ð´: ");
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
                System.out.println("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ†ÐµÐ»Ð¾Ðµ Ñ‡Ð¸ÑÐ»Ð¾.");
            }
        }
    }

    private int readIntWithin(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt + "[" + min + "-" + max + "]: ");
            if (v >= min && v <= max) return v;
            System.out.println("Ð§Ð¸ÑÐ»Ð¾ Ð²Ð½Ðµ Ð´Ð¸Ð°Ð¿Ð°Ð·Ð¾Ð½Ð°.");
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = scanner.nextLine();
            try {
                return Double.parseDouble(s.trim().replace(",", "."));
            } catch (Exception e) {
                System.out.println("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ‡Ð¸ÑÐ»Ð¾ (Ð¼Ð¾Ð¶Ð½Ð¾ Ñ Ñ‚Ð¾Ñ‡ÐºÐ¾Ð¹).");
            }
        }
    }

    private FuelType chooseFuel(List<FuelType> options) {
        System.out.println("Ð¢Ð¸Ð¿ Ñ‚Ð¾Ð¿Ð»Ð¸Ð²Ð°:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, options.get(i));
        }
        int idx = readIntWithin("Ð’Ñ‹Ð±Ð¾Ñ€: ", 1, options.size());
        return options.get(idx - 1);
    }
}

