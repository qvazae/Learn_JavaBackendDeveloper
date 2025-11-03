import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class variant_2 {

    private static final String CLASS_PREFIX = "variant_2";
    private static final String CLASS_EXT = ".class";

    public static void main(String[] args) {
        final boolean keepClasses = args != null && args.length > 0 && "--keep-classfiles".equals(args[0]);

        // Не было указано реализовать ввод векторов, заполняем тут
        Vector3 a = new Vector3(3, -2, 6);
        Vector3 b = new Vector3(-1.5, 4, 0.5);

        System.out.println("a = " + a);
        System.out.println("b = " + b);

        System.out.printf(Locale.US, "len(a) = %.6f%n", a.length());
        System.out.printf(Locale.US, "len(b) = %.6f%n", b.length());
        System.out.printf(Locale.US, "dot(a,b) = %.6f%n", a.dot(b));
        System.out.println("cross(a,b) = " + a.cross(b));
        System.out.printf(Locale.US, "cos(a,b) = %.9f%n", a.cosAngle(b));
        System.out.printf(Locale.US, "angle(a,b) [rad] = %.9f%n", a.angle(b));
        System.out.println("a + b = " + a.add(b));
        System.out.println("a - b = " + a.subtract(b));

        Vector3[] rnd = Vector3.randomArray(5);
        System.out.println("Random vectors:");
        for (Vector3 v : rnd) {
            System.out.println("  " + v);
        }

        if (!keepClasses) {
            cleanupOwnClassFiles();
        } else {
            System.out.println("[INFO] Классы сохранены (флаг --keep-classfiles).");
        }
    }

    private static void cleanupOwnClassFiles() {
        File dir = new File(".");
        File[] list = dir.listFiles((d, name) -> name.startsWith(CLASS_PREFIX) && name.endsWith(CLASS_EXT));
        if (list == null || list.length == 0) {
            System.out.println("[CLEAN] Нет .class для удаления.");
            return;
        }
        int ok = 0, fail = 0;
        for (File f : list) {
            try {
                Files.delete(f.toPath());
                ok++;
            } catch (IOException e) {
                fail++;
                System.out.println("[CLEAN][WARN] Не удалось удалить: " + f.getName() + " -> " + e.getMessage());
            }
        }
    }

    public static final class Vector3 {
        public final double x;
        public final double y;
        public final double z;

        public Vector3(double x, double y, double z) {
            validateFinite(x, "x");
            validateFinite(y, "y");
            validateFinite(z, "z");
            this.x = x;
            this.y = y;
            this.z = z;
        }

        // Длина вектора (устойчивая к переполнению)
        public double length() {
            // Масштабирование по наибольшему модулю компоненты:
            // len = max * sqrt((x/max)^2 + (y/max)^2 + (z/max)^2)
            double ax = Math.abs(x), ay = Math.abs(y), az = Math.abs(z);
            double max = Math.max(ax, Math.max(ay, az));
            if (max == 0.0) return 0.0;
            double sx = x / max, sy = y / max, sz = z / max;
            double sum = sx * sx + sy * sy + sz * sz;
            double len = max * Math.sqrt(sum);
            ensureFinite(len, "length");
            return len;
        }

        // Скалярное произведение this · other
        public double dot(Vector3 other) {
            Objects.requireNonNull(other, "other");
            // Используем fma для слегка лучшей точности, когда доступно в JVM
            double v = Math.fma(this.x, other.x, Math.fma(this.y, other.y, this.z * other.z));
            ensureFinite(v, "dot");
            return v;
        }

        // Векторное произведение this × other. Возвращает новый Vector3
        public Vector3 cross(Vector3 other) {
            Objects.requireNonNull(other, "other");
            // Классическая формула:
            // (y*z2 - z*y2, z*x2 - x*z2, x*y2 - y*x2)
            double cx = Math.fma(this.y, other.z, -this.z * other.y);
            double cy = Math.fma(this.z, other.x, -this.x * other.z);
            double cz = Math.fma(this.x, other.y, -this.y * other.x);
            // Валидация на конечность:
            ensureFinite(cx, "cross.x");
            ensureFinite(cy, "cross.y");
            ensureFinite(cz, "cross.z");
            return new Vector3(cx, cy, cz);
        }

         // Косинус угла между this и other.
         // Бросает IllegalArgumentException, если один из векторов — нулевой
        public double cosAngle(Vector3 other) {
            Objects.requireNonNull(other, "other");
            double len1 = this.length();
            double len2 = other.length();
            if (len1 == 0.0 || len2 == 0.0) {
                throw new IllegalArgumentException("Косинус угла не определён для нулевого вектора.");
            }
            double cos = this.dot(other) / (len1 * len2);
            // Защита от накопленных ошибок: зажимаем к [-1, 1].
            if (cos > 1.0) cos = 1.0;
            else if (cos < -1.0) cos = -1.0;
            ensureFinite(cos, "cosAngle");
            return cos;
        }

         // Угол между this и other в радианах.
         // Бросает IllegalArgumentException, если один из векторов — нулевой
        public double angle(Vector3 other) {
            double cos = cosAngle(other);
            double ang = Math.acos(cos);
            ensureFinite(ang, "angle");
            return ang;
        }

        // Сумма векторов: this + other. Возвращает новый Vector3
        public Vector3 add(Vector3 other) {
            Objects.requireNonNull(other, "other");
            double sx = this.x + other.x;
            double sy = this.y + other.y;
            double sz = this.z + other.z;
            // Проверка переполнения double (Infinity/NaN)
            ensureFinite(sx, "add.x");
            ensureFinite(sy, "add.y");
            ensureFinite(sz, "add.z");
            return new Vector3(sx, sy, sz);
        }

        // Разность векторов: this - other. Возвращает новый Vector3
        public Vector3 subtract(Vector3 other) {
            Objects.requireNonNull(other, "other");
            double dx = this.x - other.x;
            double dy = this.y - other.y;
            double dz = this.z - other.z;
            ensureFinite(dx, "subtract.x");
            ensureFinite(dy, "subtract.y");
            ensureFinite(dz, "subtract.z");
            return new Vector3(dx, dy, dz);
        }

         // Создаёт массив из n случайных векторов.
         // Ограничение по n, чтобы исключить OOM
        public static Vector3[] randomArray(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("N должно быть >= 0");
            }
            final int MAX_N = 1_000_000;
            if (n > MAX_N) {
                throw new IllegalArgumentException("N слишком велико (макс. " + MAX_N + ")");
            }
            Vector3[] arr = new Vector3[n];
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            // Диапазон значений выбран умеренным, чтобы минимизировать риск переполнения при операциях:
            final double MIN = -1e6;
            final double MAX = 1e6;
            for (int i = 0; i < n; i++) {
                double x = rnd.nextDouble(MIN, MAX);
                double y = rnd.nextDouble(MIN, MAX);
                double z = rnd.nextDouble(MIN, MAX);
                arr[i] = new Vector3(x, y, z);
            }
            return arr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Vector3 v)) return false;
            // Обычное сравнение, не по битам
            return Double.doubleToLongBits(x) == Double.doubleToLongBits(v.x)
                && Double.doubleToLongBits(y) == Double.doubleToLongBits(v.y)
                && Double.doubleToLongBits(z) == Double.doubleToLongBits(v.z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(Double.doubleToLongBits(x),
                                Double.doubleToLongBits(y),
                                Double.doubleToLongBits(z));
        }

        @Override
        public String toString() {
            return String.format(Locale.US, "Vector3{x=%.6f, y=%.6f, z=%.6f}", x, y, z);
        }

        // Вспомогательные проверки

        private static void validateFinite(double v, String name) {
            if (!Double.isFinite(v)) {
                throw new IllegalArgumentException(name + " должен быть конечным числом (не NaN/Infinity)");
            }
        }

        private static void ensureFinite(double v, String opName) {
            if (!Double.isFinite(v)) {
                throw new ArithmeticException("Результат операции '" + opName + "' не является конечным числом");
            }
        }
    }
}
