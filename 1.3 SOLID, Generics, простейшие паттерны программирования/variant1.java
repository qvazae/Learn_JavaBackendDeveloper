import java.util.*;

/**
 * Вариант 1 — Архитектура (SOLID + Generics + простые паттерны).
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
        // Демонстрация: сборка транспорта через фабрики, хранение в репозитории, запуск движения
        Repository<Transport> fleet = new InMemoryRepository<>();

        TransportFactory<Car> carFactory = new CarFactory(
                new RoadMovement<>(),
                () -> new CombustionEngine(150, FuelType.GASOLINE)
        );

        TransportFactory<ElectricCar> evFactory = new ElectricCarFactory(
                new RoadMovement<>(),
                () -> new ElectricMotor(200, 75.0)
        );

        TransportFactory<Bicycle> bicycleFactory = new BicycleFactory(new RoadMovement<>());
        TransportFactory<Airplane> airplaneFactory = new AirplaneFactory(new AirMovement<>(),
                () -> new CombustionEngine(1200, FuelType.JET_FUEL));
        TransportFactory<Ship> shipFactory = new ShipFactory(new WaterMovement<>(),
                () -> new CombustionEngine(5000, FuelType.DIESEL));

        fleet.save(carFactory.create("Toyota Camry", 5));
        fleet.save(evFactory.create("Tesla Model 3", 5));
        fleet.save(bicycleFactory.create("Trek Marlin 6", 1));
        fleet.save(airplaneFactory.create("Boeing 737", 160));
        fleet.save(shipFactory.create("MSC Seaside", 5000));

        for (Transport t : fleet.findAll()) {
            System.out.println(t);
            if (t instanceof HasEngine<?> engineHolder) {
                engineHolder.engine().start();
            }
            t.move();
        }
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
