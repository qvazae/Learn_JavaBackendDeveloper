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
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        final boolean keepClasses = args != null && args.length > 0 && "--keep-classfiles".equals(args[0]);

        // ÐÐµ Ð±Ñ‹Ð»Ð¾ ÑƒÐºÐ°Ð·Ð°Ð½Ð¾ Ñ€ÐµÐ°Ð»Ð¸Ð·Ð¾Ð²Ð°Ñ‚ÑŒ Ð²Ð²Ð¾Ð´ Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð², Ð·Ð°Ð¿Ð¾Ð»Ð½ÑÐµÐ¼ Ñ‚ÑƒÑ‚
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
            System.out.println("[INFO] ÐšÐ»Ð°ÑÑÑ‹ ÑÐ¾Ñ…Ñ€Ð°Ð½ÐµÐ½Ñ‹ (Ñ„Ð»Ð°Ð³ --keep-classfiles).");
        }
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }

    private static void cleanupOwnClassFiles() {
        File dir = new File(".");
        File[] list = dir.listFiles((d, name) -> name.startsWith(CLASS_PREFIX) && name.endsWith(CLASS_EXT));
        if (list == null || list.length == 0) {
            System.out.println("[CLEAN] ÐÐµÑ‚ .class Ð´Ð»Ñ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ñ.");
            return;
        }
        int ok = 0, fail = 0;
        for (File f : list) {
            try {
                Files.delete(f.toPath());
                ok++;
            } catch (IOException e) {
                fail++;
                System.out.println("[CLEAN][WARN] ÐÐµ ÑƒÐ´Ð°Ð»Ð¾ÑÑŒ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ: " + f.getName() + " -> " + e.getMessage());
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

        // Ð”Ð»Ð¸Ð½Ð° Ð²ÐµÐºÑ‚Ð¾Ñ€Ð° (ÑƒÑÑ‚Ð¾Ð¹Ñ‡Ð¸Ð²Ð°Ñ Ðº Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ð¸ÑŽ)
        public double length() {
            // ÐœÐ°ÑÑˆÑ‚Ð°Ð±Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ Ð¿Ð¾ Ð½Ð°Ð¸Ð±Ð¾Ð»ÑŒÑˆÐµÐ¼Ñƒ Ð¼Ð¾Ð´ÑƒÐ»ÑŽ ÐºÐ¾Ð¼Ð¿Ð¾Ð½ÐµÐ½Ñ‚Ñ‹:
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

        // Ð¡ÐºÐ°Ð»ÑÑ€Ð½Ð¾Ðµ Ð¿Ñ€Ð¾Ð¸Ð·Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ this Â· other
        public double dot(Vector3 other) {
            Objects.requireNonNull(other, "other");
            // Ð˜ÑÐ¿Ð¾Ð»ÑŒÐ·ÑƒÐµÐ¼ fma Ð´Ð»Ñ ÑÐ»ÐµÐ³ÐºÐ° Ð»ÑƒÑ‡ÑˆÐµÐ¹ Ñ‚Ð¾Ñ‡Ð½Ð¾ÑÑ‚Ð¸, ÐºÐ¾Ð³Ð´Ð° Ð´Ð¾ÑÑ‚ÑƒÐ¿Ð½Ð¾ Ð² JVM
            double v = Math.fma(this.x, other.x, Math.fma(this.y, other.y, this.z * other.z));
            ensureFinite(v, "dot");
            return v;
        }

        // Ð’ÐµÐºÑ‚Ð¾Ñ€Ð½Ð¾Ðµ Ð¿Ñ€Ð¾Ð¸Ð·Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ this Ã— other. Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°ÐµÑ‚ Ð½Ð¾Ð²Ñ‹Ð¹ Vector3
        public Vector3 cross(Vector3 other) {
            Objects.requireNonNull(other, "other");
            // ÐšÐ»Ð°ÑÑÐ¸Ñ‡ÐµÑÐºÐ°Ñ Ñ„Ð¾Ñ€Ð¼ÑƒÐ»Ð°:
            // (y*z2 - z*y2, z*x2 - x*z2, x*y2 - y*x2)
            double cx = Math.fma(this.y, other.z, -this.z * other.y);
            double cy = Math.fma(this.z, other.x, -this.x * other.z);
            double cz = Math.fma(this.x, other.y, -this.y * other.x);
            // Ð’Ð°Ð»Ð¸Ð´Ð°Ñ†Ð¸Ñ Ð½Ð° ÐºÐ¾Ð½ÐµÑ‡Ð½Ð¾ÑÑ‚ÑŒ:
            ensureFinite(cx, "cross.x");
            ensureFinite(cy, "cross.y");
            ensureFinite(cz, "cross.z");
            return new Vector3(cx, cy, cz);
        }

         // ÐšÐ¾ÑÐ¸Ð½ÑƒÑ ÑƒÐ³Ð»Ð° Ð¼ÐµÐ¶Ð´Ñƒ this Ð¸ other.
         // Ð‘Ñ€Ð¾ÑÐ°ÐµÑ‚ IllegalArgumentException, ÐµÑÐ»Ð¸ Ð¾Ð´Ð¸Ð½ Ð¸Ð· Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð² â€” Ð½ÑƒÐ»ÐµÐ²Ð¾Ð¹
        public double cosAngle(Vector3 other) {
            Objects.requireNonNull(other, "other");
            double len1 = this.length();
            double len2 = other.length();
            if (len1 == 0.0 || len2 == 0.0) {
                throw new IllegalArgumentException("ÐšÐ¾ÑÐ¸Ð½ÑƒÑ ÑƒÐ³Ð»Ð° Ð½Ðµ Ð¾Ð¿Ñ€ÐµÐ´ÐµÐ»Ñ‘Ð½ Ð´Ð»Ñ Ð½ÑƒÐ»ÐµÐ²Ð¾Ð³Ð¾ Ð²ÐµÐºÑ‚Ð¾Ñ€Ð°.");
            }
            double cos = this.dot(other) / (len1 * len2);
            // Ð—Ð°Ñ‰Ð¸Ñ‚Ð° Ð¾Ñ‚ Ð½Ð°ÐºÐ¾Ð¿Ð»ÐµÐ½Ð½Ñ‹Ñ… Ð¾ÑˆÐ¸Ð±Ð¾Ðº: Ð·Ð°Ð¶Ð¸Ð¼Ð°ÐµÐ¼ Ðº [-1, 1].
            if (cos > 1.0) cos = 1.0;
            else if (cos < -1.0) cos = -1.0;
            ensureFinite(cos, "cosAngle");
            return cos;
        }

         // Ð£Ð³Ð¾Ð» Ð¼ÐµÐ¶Ð´Ñƒ this Ð¸ other Ð² Ñ€Ð°Ð´Ð¸Ð°Ð½Ð°Ñ….
         // Ð‘Ñ€Ð¾ÑÐ°ÐµÑ‚ IllegalArgumentException, ÐµÑÐ»Ð¸ Ð¾Ð´Ð¸Ð½ Ð¸Ð· Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð² â€” Ð½ÑƒÐ»ÐµÐ²Ð¾Ð¹
        public double angle(Vector3 other) {
            double cos = cosAngle(other);
            double ang = Math.acos(cos);
            ensureFinite(ang, "angle");
            return ang;
        }

        // Ð¡ÑƒÐ¼Ð¼Ð° Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð²: this + other. Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°ÐµÑ‚ Ð½Ð¾Ð²Ñ‹Ð¹ Vector3
        public Vector3 add(Vector3 other) {
            Objects.requireNonNull(other, "other");
            double sx = this.x + other.x;
            double sy = this.y + other.y;
            double sz = this.z + other.z;
            // ÐŸÑ€Ð¾Ð²ÐµÑ€ÐºÐ° Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ñ double (Infinity/NaN)
            ensureFinite(sx, "add.x");
            ensureFinite(sy, "add.y");
            ensureFinite(sz, "add.z");
            return new Vector3(sx, sy, sz);
        }

        // Ð Ð°Ð·Ð½Ð¾ÑÑ‚ÑŒ Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð²: this - other. Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°ÐµÑ‚ Ð½Ð¾Ð²Ñ‹Ð¹ Vector3
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

         // Ð¡Ð¾Ð·Ð´Ð°Ñ‘Ñ‚ Ð¼Ð°ÑÑÐ¸Ð² Ð¸Ð· n ÑÐ»ÑƒÑ‡Ð°Ð¹Ð½Ñ‹Ñ… Ð²ÐµÐºÑ‚Ð¾Ñ€Ð¾Ð².
         // ÐžÐ³Ñ€Ð°Ð½Ð¸Ñ‡ÐµÐ½Ð¸Ðµ Ð¿Ð¾ n, Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¸ÑÐºÐ»ÑŽÑ‡Ð¸Ñ‚ÑŒ OOM
        public static Vector3[] randomArray(int n) {
            if (n < 0) {
                throw new IllegalArgumentException("N Ð´Ð¾Ð»Ð¶Ð½Ð¾ Ð±Ñ‹Ñ‚ÑŒ >= 0");
            }
            final int MAX_N = 1_000_000;
            if (n > MAX_N) {
                throw new IllegalArgumentException("N ÑÐ»Ð¸ÑˆÐºÐ¾Ð¼ Ð²ÐµÐ»Ð¸ÐºÐ¾ (Ð¼Ð°ÐºÑ. " + MAX_N + ")");
            }
            Vector3[] arr = new Vector3[n];
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            // Ð”Ð¸Ð°Ð¿Ð°Ð·Ð¾Ð½ Ð·Ð½Ð°Ñ‡ÐµÐ½Ð¸Ð¹ Ð²Ñ‹Ð±Ñ€Ð°Ð½ ÑƒÐ¼ÐµÑ€ÐµÐ½Ð½Ñ‹Ð¼, Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¼Ð¸Ð½Ð¸Ð¼Ð¸Ð·Ð¸Ñ€Ð¾Ð²Ð°Ñ‚ÑŒ Ñ€Ð¸ÑÐº Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ñ Ð¿Ñ€Ð¸ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸ÑÑ…:
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
            // ÐžÐ±Ñ‹Ñ‡Ð½Ð¾Ðµ ÑÑ€Ð°Ð²Ð½ÐµÐ½Ð¸Ðµ, Ð½Ðµ Ð¿Ð¾ Ð±Ð¸Ñ‚Ð°Ð¼
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

        // Ð’ÑÐ¿Ð¾Ð¼Ð¾Ð³Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ðµ Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸

        private static void validateFinite(double v, String name) {
            if (!Double.isFinite(v)) {
                throw new IllegalArgumentException(name + " Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ ÐºÐ¾Ð½ÐµÑ‡Ð½Ñ‹Ð¼ Ñ‡Ð¸ÑÐ»Ð¾Ð¼ (Ð½Ðµ NaN/Infinity)");
            }
        }

        private static void ensureFinite(double v, String opName) {
            if (!Double.isFinite(v)) {
                throw new ArithmeticException("Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸ '" + opName + "' Ð½Ðµ ÑÐ²Ð»ÑÐµÑ‚ÑÑ ÐºÐ¾Ð½ÐµÑ‡Ð½Ñ‹Ð¼ Ñ‡Ð¸ÑÐ»Ð¾Ð¼");
            }
        }
    }
}

