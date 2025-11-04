import java.util.*;

/**
 * Ð’Ð°Ñ€Ð¸Ð°Ð½Ñ‚ 1
 *
 * Ð§Ñ‚Ð¾ Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¾ Ð¿Ð¾ ÑÑ€Ð°Ð²Ð½ÐµÐ½Ð¸ÑŽ Ñ Ð¿Ñ€ÐµÐ´Ñ‹Ð´ÑƒÑ‰Ð¸Ð¼ Ñ€ÐµÑˆÐµÐ½Ð¸ÐµÐ¼ (1.2):
 * - Single Responsibility: Ð´Ð¾Ð¼ÐµÐ½Ð½Ð°Ñ Ð¼Ð¾Ð´ÐµÐ»ÑŒ Ð¾Ñ‚Ð´ÐµÐ»ÐµÐ½Ð° Ð¾Ñ‚ Ð²Ð²Ð¾Ð´Ð°/Ð²Ñ‹Ð²Ð¾Ð´Ð°; ÑÑƒÑ‰Ð½Ð¾ÑÑ‚Ð¸ ÑƒÐ·ÐºÐ¾ÑÐ¿ÐµÑ†Ð¸Ð°Ð»Ð¸Ð·Ð¸Ñ€Ð¾Ð²Ð°Ð½Ñ‹.
 * - Open/Closed: Ð´Ð»Ñ Ð½Ð¾Ð²Ð¾Ð³Ð¾ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°/Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ Ð´Ð¾ÑÑ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ Ð´Ð¾Ð±Ð°Ð²Ð¸Ñ‚ÑŒ ÐºÐ»Ð°ÑÑ, Ð±ÐµÐ· Ð¿Ñ€Ð°Ð²ÐºÐ¸ ÑÑƒÑ‰ÐµÑÑ‚Ð²ÑƒÑŽÑ‰ÐµÐ³Ð¾ ÐºÐ¾Ð´Ð°.
 * - Liskov Substitution: Ð²ÑÐµ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ñ‹ Ð²Ð·Ð°Ð¸Ð¼Ð¾Ð·Ð°Ð¼ÐµÐ½ÑÐµÐ¼Ñ‹ Ð¿Ð¾ Ð¸Ð½Ñ‚ÐµÑ€Ñ„ÐµÐ¹ÑÑƒ Transport.
 * - Interface Segregation: Ð½ÐµÐ±Ð¾Ð»ÑŒÑˆÐ¸Ðµ Ð¸Ð½Ñ‚ÐµÑ€Ñ„ÐµÐ¹ÑÑ‹ (Transport, HasEngine, MovementStrategy, Repository, Factory).
 * - Dependency Inversion: Transport Ð·Ð°Ð²Ð¸ÑÐ¸Ñ‚ Ð¾Ñ‚ Ð°Ð±ÑÑ‚Ñ€Ð°ÐºÑ†Ð¸Ð¹ (ÑÑ‚Ñ€Ð°Ñ‚ÐµÐ³Ð¸Ð¹/Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÐµÐ¹), Ð²Ð½ÐµÐ´Ñ€ÑÐµÐ¼Ñ‹Ñ… Ð¸Ð·Ð²Ð½Ðµ.
 * - Generics: ÑÑ‚Ñ€Ð°Ñ‚ÐµÐ³Ð¸Ð¸, Ñ€ÐµÐ¿Ð¾Ð·Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸, Ñ„Ð°Ð±Ñ€Ð¸ÐºÐ¸ Ð¿Ð°Ñ€Ð°Ð¼ÐµÑ‚Ñ€Ð¸Ð·Ð¾Ð²Ð°Ð½Ñ‹ Ñ‚Ð¸Ð¿Ð°Ð¼Ð¸.
 * - ÐŸÐ°Ñ‚Ñ‚ÐµÑ€Ð½Ñ‹: Strategy (Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ), Factory (ÑÐ¾Ð·Ð´Ð°Ð½Ð¸Ðµ), Repository (Ñ…Ñ€Ð°Ð½Ð¸Ð»Ð¸Ñ‰Ðµ).
 */

public class variant1 {
    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        new ConsoleApp().run();
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }
}

// ==========================
//      Ð”Ð¾Ð¼ÐµÐ½Ð½Ñ‹Ðµ Ð°Ð±ÑÑ‚Ñ€Ð°ÐºÑ†Ð¸Ð¸
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
 * Ð¡Ð°Ð¼Ð¾Ñ‚Ð¸Ð¿Ð¸Ð·Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ð°Ñ Ð±Ð°Ð·Ð¾Ð²Ð°Ñ Ð¼Ð¾Ð´ÐµÐ»ÑŒ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°, Ð¸ÑÐ¿Ð¾Ð»ÑŒÐ·ÑƒÑŽÑ‰Ð°Ñ ÑÑ‚Ñ€Ð°Ñ‚ÐµÐ³Ð¸ÑŽ Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ.
 */
abstract class AbstractTransport<T extends AbstractTransport<T>> implements Transport {
    private final String model;
    private final int capacity;
    private final MovementStrategy<T> movement;

    protected AbstractTransport(String model, int capacity, MovementStrategy<T> movement) {
        if (model == null || model.isBlank()) throw new IllegalArgumentException("Ð¼Ð¾Ð´ÐµÐ»ÑŒ Ð½Ðµ Ð·Ð°Ð´Ð°Ð½Ð°");
        if (capacity < 0) throw new IllegalArgumentException("Ð²Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ â‰¥ 0");
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
        return getClass().getSimpleName() + "(" + model + ", Ð²Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ=" + capacity + ")";
    }
}

// ==========================
//         Ð”Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ (Strategy)
// ==========================

interface MovementStrategy<T extends Transport> {
    void move(T transport);
}

final class RoadMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": ÐµÐ´ÐµÑ‚ Ð¿Ð¾ Ð´Ð¾Ñ€Ð¾Ð³Ðµ");
    }
}

final class AirMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": Ð»ÐµÑ‚Ð¸Ñ‚ Ð¿Ð¾ Ð²Ð¾Ð·Ð´ÑƒÑ…Ñƒ");
    }
}

final class WaterMovement<T extends Transport> implements MovementStrategy<T> {
    @Override public void move(T transport) {
        System.out.println(transport.model() + ": Ð¿Ð»Ñ‹Ð²Ñ‘Ñ‚ Ð¿Ð¾ Ð²Ð¾Ð´Ðµ");
    }
}

// ==========================
//            Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ð¸
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
        if (powerHP <= 0) throw new IllegalArgumentException("Ð¼Ð¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ > 0");
        this.powerHP = powerHP;
        this.fuel = Objects.requireNonNull(fuel);
    }

    public FuelType fuel() { return fuel; }
    @Override public int powerHP() { return powerHP; }
    @Override public boolean isRunning() { return running; }
    @Override public void start() { running = true; }
    @Override public void stop() { running = false; }

    @Override public String toString() { return "Ð”Ð’Ð¡{" + fuel + ", " + powerHP + " Ð».Ñ.}"; }
}

final class ElectricMotor implements Engine {
    private final int powerHP;
    private final double batteryKWh;
    private boolean running;

    ElectricMotor(int powerHP, double batteryKWh) {
        if (powerHP <= 0) throw new IllegalArgumentException("Ð¼Ð¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ > 0");
        if (batteryKWh <= 0) throw new IllegalArgumentException("Ñ‘Ð¼ÐºÐ¾ÑÑ‚ÑŒ Ð±Ð°Ñ‚Ð°Ñ€ÐµÐ¸ Ð´Ð¾Ð»Ð¶Ð½Ð° Ð±Ñ‹Ñ‚ÑŒ > 0");
        this.powerHP = powerHP;
        this.batteryKWh = batteryKWh;
    }

    public double batteryKWh() { return batteryKWh; }
    @Override public int powerHP() { return powerHP; }
    @Override public boolean isRunning() { return running; }
    @Override public void start() { running = true; }
    @Override public void stop() { running = false; }

    @Override public String toString() { return "Ð­Ð»ÐµÐºÑ‚Ñ€Ð¾{" + batteryKWh + " ÐºÐ’Ñ‚Â·Ñ‡, " + powerHP + " Ð».Ñ.}"; }
}

// ==========================
//         ÐšÐ¾Ð½ÐºÑ€ÐµÑ‚Ð½Ñ‹Ðµ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ñ‹
// ==========================

final class Car extends AbstractTransport<Car> implements HasEngine<CombustionEngine> {
    private final CombustionEngine engine;
    private final int doors;

    Car(String model, int capacity, MovementStrategy<Car> movement, CombustionEngine engine, int doors) {
        super(model, capacity, movement);
        if (doors <= 0) throw new IllegalArgumentException("Ñ‡Ð¸ÑÐ»Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ > 0");
        this.engine = Objects.requireNonNull(engine);
        this.doors = doors;
    }

    public int doors() { return doors; }
    @Override public CombustionEngine engine() { return engine; }
    @Override protected Car self() { return this; }

    @Override public String toString() {
        return super.toString() + ", " + engine + ", Ð´Ð²ÐµÑ€ÐµÐ¹=" + doors;
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
        if (gears <= 0) throw new IllegalArgumentException("Ñ‡Ð¸ÑÐ»Ð¾ Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ > 0");
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
//           Ð ÐµÐ¿Ð¾Ð·Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¹
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
//            Ð¤Ð°Ð±Ñ€Ð¸ÐºÐ¸
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
        // Ð¿Ð¾ ÑƒÐ¼Ð¾Ð»Ñ‡Ð°Ð½Ð¸ÑŽ 21 Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡Ð° â€” Ð½Ð°ÑÑ‚Ñ€Ð¾Ð¹ÐºÐ° Ñ„Ð°Ð±Ñ€Ð¸ÐºÐ¸, Ð° Ð½Ðµ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°
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
//     ÐšÐ¾Ð½ÑÐ¾Ð»ÑŒÐ½Ð¾Ðµ Ð¿Ñ€Ð¸Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ
// ==========================

/**
 * ÐÐµÐ±Ð¾Ð»ÑŒÑˆÐ¾Ðµ ÐºÐ¾Ð½ÑÐ¾Ð»ÑŒÐ½Ð¾Ðµ Ð¿Ñ€Ð¸Ð»Ð¾Ð¶ÐµÐ½Ð¸Ðµ Ð¿Ð¾Ð²ÐµÑ€Ñ… Ð´Ð¾Ð¼ÐµÐ½Ð½Ð¾Ð¹ Ð¼Ð¾Ð´ÐµÐ»Ð¸:
 * - ÑÐ¾Ð·Ð´Ð°Ñ‘Ñ‚ Ñ€Ð°Ð·Ð½Ñ‹Ðµ Ñ‚Ð¸Ð¿Ñ‹ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð° Ð¿Ð¾ Ð²Ð²Ð¾Ð´Ñƒ Ð¿Ð¾Ð»ÑŒÐ·Ð¾Ð²Ð°Ñ‚ÐµÐ»Ñ;
 * - Ñ…Ñ€Ð°Ð½Ð¸Ñ‚ Ð¸Ñ… Ð² Ñ€ÐµÐ¿Ð¾Ð·Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸ Ð² Ð¿Ð°Ð¼ÑÑ‚Ð¸;
 * - Ð¿Ð¾Ð·Ð²Ð¾Ð»ÑÐµÑ‚ Ð·Ð°Ð¿ÑƒÑÐºÐ°Ñ‚ÑŒ/Ð¾ÑÑ‚Ð°Ð½Ð°Ð²Ð»Ð¸Ð²Ð°Ñ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ (ÐµÑÐ»Ð¸ Ð¾Ð½ ÐµÑÑ‚ÑŒ) Ð¸ Ð²Ñ‹Ð¿Ð¾Ð»Ð½ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ (Strategy).
 *
 * UI Ð¸Ð·Ð¾Ð»Ð¸Ñ€Ð¾Ð²Ð°Ð½ Ð¾Ñ‚ Ð´Ð¾Ð¼ÐµÐ½Ð°: Ð´Ð¾Ð¼ÐµÐ½Ð½Ñ‹Ðµ ÐºÐ»Ð°ÑÑÑ‹ Ð½Ðµ Ð·Ð½Ð°ÑŽÑ‚ Ð¾ ÐºÐ¾Ð½ÑÐ¾Ð»Ð¸.
 */
final class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final Repository<Transport> fleet = new InMemoryRepository<>();

    // Ð“Ð¾Ñ‚Ð¾Ð²Ñ‹Ðµ ÑÑ‚Ñ€Ð°Ñ‚ÐµÐ³Ð¸Ð¸ Ð´Ð²Ð¸Ð¶ÐµÐ½Ð¸Ñ â€” Ð²Ð½ÐµÐ´Ñ€ÑÑŽÑ‚ÑÑ Ð² Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ñ‹
    private final MovementStrategy<Car> roadCar = new RoadMovement<>();
    private final MovementStrategy<ElectricCar> roadEv = new RoadMovement<>();
    private final MovementStrategy<Bicycle> roadBike = new RoadMovement<>();
    private final MovementStrategy<Airplane> air = new AirMovement<>();
    private final MovementStrategy<Ship> water = new WaterMovement<>();

    public void run() {
        boolean exit = false;
        while (!exit) {
            System.out.println();
            System.out.println("ÐœÐµÐ½ÑŽ:");
            System.out.println(" 1) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ Ð°Ð²Ñ‚Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ");
            System.out.println(" 2) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÑÐ°Ð¼Ð¾Ð»Ñ‘Ñ‚");
            System.out.println(" 3) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÐºÐ¾Ñ€Ð°Ð±Ð»ÑŒ");
            System.out.println(" 4) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ Ð²ÐµÐ»Ð¾ÑÐ¸Ð¿ÐµÐ´");
            System.out.println(" 5) Ð¡Ð¾Ð·Ð´Ð°Ñ‚ÑŒ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ð±Ð¸Ð»ÑŒ");
            System.out.println(" 6) Ð¡Ð¿Ð¸ÑÐ¾Ðº Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°");
            System.out.println(" 7) ÐžÐ¿ÐµÑ€Ð°Ñ†Ð¸Ð¸ Ñ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð¾Ð¼");
            System.out.println(" 0) Ð’Ñ‹Ñ…Ð¾Ð´");
            int c = readInt("Ð’Ñ‹Ð±Ð¾Ñ€: ");
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
                    default -> System.out.println("ÐÐµÐ¸Ð·Ð²ÐµÑÑ‚Ð½Ð°Ñ ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°");
                }
            } catch (Exception e) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: " + e.getMessage());
            }
        }
    }

    private void createCar() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð°Ð²Ñ‚Ð¾Ð¼Ð¾Ð±Ð¸Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readIntNonNegative("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ: ");
        int hp = readIntPositive("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ (Ð».Ñ.): ");
        FuelType fuel = chooseFuel(List.of(FuelType.GASOLINE, FuelType.DIESEL));
        int doors = readIntPositive("ÐšÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ð¾ Ð´Ð²ÐµÑ€ÐµÐ¹: ");
        Car car = new Car(model, cap, roadCar, new CombustionEngine(hp, fuel), doors);
        fleet.save(car);
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¾: " + car);
    }

    private void createAirplane() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÑÐ°Ð¼Ð¾Ð»Ñ‘Ñ‚Ð°");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readIntNonNegative("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ: ");
        int hp = readIntPositive("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ (Ð».Ñ.): ");
        Airplane airplane = new Airplane(model, cap, air, new CombustionEngine(hp, FuelType.JET_FUEL));
        fleet.save(airplane);
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¾: " + airplane);
    }

    private void createShip() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÐºÐ¾Ñ€Ð°Ð±Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readIntNonNegative("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ: ");
        int hp = readIntPositive("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ (Ð».Ñ.): ");
        Ship ship = new Ship(model, cap, water, new CombustionEngine(hp, FuelType.DIESEL));
        fleet.save(ship);
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¾: " + ship);
    }

    private void createBicycle() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ Ð²ÐµÐ»Ð¾ÑÐ¸Ð¿ÐµÐ´Ð°");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readIntWithin("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ [1..4]: ", 1, 4);
        int gears = readIntPositive("Ð§Ð¸ÑÐ»Ð¾ Ð¿ÐµÑ€ÐµÐ´Ð°Ñ‡: ");
        Bicycle bicycle = new Bicycle(model, cap, roadBike, gears);
        fleet.save(bicycle);
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¾: " + bicycle);
    }

    private void createElectricCar() {
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¸Ðµ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ð±Ð¸Ð»Ñ");
        String model = readLine("ÐœÐ¾Ð´ÐµÐ»ÑŒ: ");
        int cap = readIntNonNegative("Ð’Ð¼ÐµÑÑ‚Ð¸Ð¼Ð¾ÑÑ‚ÑŒ: ");
        int hp = readIntPositive("ÐœÐ¾Ñ‰Ð½Ð¾ÑÑ‚ÑŒ ÑÐ»ÐµÐºÑ‚Ñ€Ð¾Ð¼Ð¾Ñ‚Ð¾Ñ€Ð° (Ð».Ñ.): ");
        double kwh = readDoublePositive("ÐÐ¼ÐºÐ¾ÑÑ‚ÑŒ Ð±Ð°Ñ‚Ð°Ñ€ÐµÐ¸ (ÐºÐ’Ñ‚Â·Ñ‡): ");
        ElectricCar ev = new ElectricCar(model, cap, roadEv, new ElectricMotor(hp, kwh));
        fleet.save(ev);
        System.out.println("Ð¡Ð¾Ð·Ð´Ð°Ð½Ð¾: " + ev);
    }

    private void listFleet() {
        List<Transport> all = fleet.findAll();
        if (all.isEmpty()) {
            System.out.println("ÐŸÐ°Ñ€Ðº Ð¿ÑƒÑÑ‚.");
            return;
        }
        System.out.println("Ð¡Ð¾Ð´ÐµÑ€Ð¶Ð¸Ð¼Ð¾Ðµ Ð¿Ð°Ñ€ÐºÐ°:");
        for (int i = 0; i < all.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, all.get(i));
        }
    }

    private void operateTransport() {
        List<Transport> all = fleet.findAll();
        if (all.isEmpty()) {
            System.out.println("ÐÐµÑ‡ÐµÐ³Ð¾ Ð²Ñ‹Ð±Ð¸Ñ€Ð°Ñ‚ÑŒ â€” Ð¿Ð°Ñ€Ðº Ð¿ÑƒÑÑ‚.");
            return;
        }
        listFleet();
        int idx = readIntWithin("Ð’Ñ‹Ð±ÐµÑ€Ð¸Ñ‚Ðµ Ð½Ð¾Ð¼ÐµÑ€ Ñ‚Ñ€Ð°Ð½ÑÐ¿Ð¾Ñ€Ñ‚Ð°: ", 1, all.size()) - 1;
        Transport t = all.get(idx);
        System.out.println("Ð’Ñ‹Ð±Ñ€Ð°Ð½: " + t);

        boolean back = false;
        while (!back) {
            System.out.println("  Ð”ÐµÐ¹ÑÑ‚Ð²Ð¸Ñ:");
            System.out.println("   1) Ð˜Ð½Ñ„Ð¾Ñ€Ð¼Ð°Ñ†Ð¸Ñ");
            System.out.println("   2) Ð—Ð°Ð¿ÑƒÑÑ‚Ð¸Ñ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ");
            System.out.println("   3) Ð—Ð°Ð³Ð»ÑƒÑˆÐ¸Ñ‚ÑŒ Ð´Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ");
            System.out.println("   4) Ð”Ð²Ð¸Ð¶ÐµÐ½Ð¸Ðµ");
            System.out.println("   0) ÐÐ°Ð·Ð°Ð´");
            int c = readInt("Ð’Ñ‹Ð±Ð¾Ñ€: ");
            switch (c) {
                case 1 -> System.out.println(t);
                case 2 -> {
                    if (t instanceof HasEngine<?> he) {
                        Engine e = ((HasEngine<?>) t).engine();
                        if (!e.isRunning()) { e.start(); System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½."); }
                        else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ ÑƒÐ¶Ðµ Ñ€Ð°Ð±Ð¾Ñ‚Ð°ÐµÑ‚.");
                    } else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ Ð½ÐµÑ‚.");
                }
                case 3 -> {
                    if (t instanceof HasEngine<?> he) {
                        Engine e = ((HasEngine<?>) t).engine();
                        if (e.isRunning()) { e.stop(); System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ Ð¾ÑÑ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½."); }
                        else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»ÑŒ ÑƒÐ¶Ðµ Ð¾ÑÑ‚Ð°Ð½Ð¾Ð²Ð»ÐµÐ½.");
                    } else System.out.println("Ð”Ð²Ð¸Ð³Ð°Ñ‚ÐµÐ»Ñ Ð½ÐµÑ‚.");
                }
                case 4 -> t.move();
                case 0 -> back = true;
                default -> System.out.println("ÐÐµÐ¸Ð·Ð²ÐµÑÑ‚Ð½Ð°Ñ ÐºÐ¾Ð¼Ð°Ð½Ð´Ð°");
            }
        }
    }

    // --------- Ð’Ð²Ð¾Ð´ Ñ Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¾Ð¹ ---------

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
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ†ÐµÐ»Ð¾Ðµ Ñ‡Ð¸ÑÐ»Ð¾.");
            }
        }
    }

    private int readIntPositive(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v > 0) return v;
            System.out.println("Ð§Ð¸ÑÐ»Ð¾ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ > 0.");
        }
    }

    private int readIntNonNegative(String prompt) {
        while (true) {
            int v = readInt(prompt);
            if (v >= 0) return v;
            System.out.println("Ð§Ð¸ÑÐ»Ð¾ Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ â‰¥ 0.");
        }
    }

    private int readIntWithin(String prompt, int min, int max) {
        while (true) {
            int v = readInt(prompt);
            if (v >= min && v <= max) return v;
            System.out.printf("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ‡Ð¸ÑÐ»Ð¾ Ð² Ð´Ð¸Ð°Ð¿Ð°Ð·Ð¾Ð½Ðµ [%d..%d].%n", min, max);
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
            System.out.println("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð¿Ð¾Ð»Ð¾Ð¶Ð¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ðµ Ñ‡Ð¸ÑÐ»Ð¾ (Ð´Ñ€Ð¾Ð±Ð½Ð°Ñ Ñ‡Ð°ÑÑ‚ÑŒ Ð´Ð¾Ð¿ÑƒÑÑ‚Ð¸Ð¼Ð°).");
        }
    }

    private FuelType chooseFuel(List<FuelType> options) {
        System.out.println("Ð¢Ð¾Ð¿Ð»Ð¸Ð²Ð¾:");
        for (int i = 0; i < options.size(); i++) {
            System.out.printf("  %d) %s%n", i + 1, options.get(i));
        }
        int idx = readIntWithin("Ð’Ñ‹Ð±Ð¾Ñ€: ", 1, options.size());
        return options.get(idx - 1);
    }
}

