?/**
 * Вариант 1 — учебное решение.
 * Комментарии приведены к единому стилю (заголовок файла, разделители секций).
 */
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class variant_1 {
    private static final Scanner SC = new Scanner(System.in);

    // Порог, когда не печатаем весь массив, а показываем только небольшую часть
    private static final int PRINT_SAMPLE_LIMIT = 2000;     // >2k элементов — печатаем только начало/конец
    private static final int SAMPLE_HEAD_TAIL   = 20;       // сколько элементов показать с начала/конца

    // "Операционные" потолки по количеству элементов (чтобы не умереть по времени/памяти)
    private static final int OP_LIMIT_INT    = 5_000_000;   // int[]
    private static final int OP_LIMIT_DOUBLE = 3_000_000;   // double[]

    public static void main(String[] args) {
    p    // ??????? ??? ?????? ?? ????????????? ????? ? ?????? ??????     p    try {
        SC.useLocale(Locale.US);

        String type = askType(); // "int" или "double"
        int n = askPositiveSizeForType(type);

        if (type.equals("int")) {
            int[] bounds = askIntBounds();
            int[] arr = safeGenerateIntArray(n, bounds[0], bounds[1]);

            printlnIntArray("Сгенерированный массив (int)", arr);
            System.out.printf("Минимум: %d%n", min(arr));
            System.out.printf("Максимум: %d%n", max(arr));
            System.out.printf("Среднее: %.6f%n", avg(arr));

            // СОРТИРОВКА: для очень больших массивов сортируем "на месте" без клонирования
            if (arr.length <= OP_LIMIT_INT) {
                int[] asc = arr.clone();
                sortAsc(asc);
                printlnIntArray("Сортировка по возрастанию", asc);

                int[] desc = asc.clone();
                reverse(desc);
                printlnIntArray("Сортировка по убыванию", desc);
            } else {
                System.out.println("Замечание: массив очень большой — сортируем без копирования (может занять время).");
                sortAsc(arr);
                printlnIntArray("Сортировка по возрастанию (in-place)", arr);

                reverse(arr); // теперь убывание
                printlnIntArray("Сортировка по убыванию (in-place)", arr);
            }
        } else {
            double[] bounds = askDoubleBounds();
            double[] arr = safeGenerateDoubleArray(n, bounds[0], bounds[1]);

            printlnDoubleArray("Сгенерированный массив (double)", arr);
            System.out.printf("Минимум: %.6f%n", min(arr));
            System.out.printf("Максимум: %.6f%n", max(arr));
            System.out.printf("Среднее: %.6f%n", avg(arr));

            if (arr.length <= OP_LIMIT_DOUBLE) {
                double[] asc = arr.clone();
                sortAsc(asc);
                printlnDoubleArray("Сортировка по возрастанию", asc);

                double[] desc = asc.clone();
                reverse(desc);
                printlnDoubleArray("Сортировка по убыванию", desc);
            } else {
                System.out.println("Замечание: массив очень большой — сортируем без копирования (может занять время).");
                sortAsc(arr);
                printlnDoubleArray("Сортировка по возрастанию (in-place)", arr);

                reverse(arr);
                printlnDoubleArray("Сортировка по убыванию (in-place)", arr);
            }
        }

        System.out.println("Готово.");

        // Чистим за собой .class
        tryDeleteOwnClassFile();
    p    } catch (Exception e) {     p        System.out.println("????????? ?????? ?????/??????: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // ??????????? ??????: ????????? ????????? ??? ???????     p    }
    }

    // Функция удаления .class
    private static void tryDeleteOwnClassFile() {
        try {
            String resName = variant_1.class.getSimpleName() + ".class";
            // URL ресурса .class (относительно пакета; здесь пакета нет)
            java.net.URL url = variant_1.class.getResource(resName);
            if (url == null) {
                // Может быть запущено из нестандартного classloader'а
                System.out.println("Пропускаю удаление .class: ресурс не найден.");
                return;
            }
            if (!"file".equalsIgnoreCase(url.getProtocol())) {
                // Например, запуск из JAR/zip — удалять нечего/нельзя
                System.out.println("Пропускаю удаление .class: не файловая система (" + url.getProtocol() + ").");
                return;
            }
            java.nio.file.Path p = java.nio.file.Paths.get(url.toURI());
            boolean deleted = java.nio.file.Files.deleteIfExists(p);
            if (deleted) {
                System.out.println("Файл " + p.getFileName() + " был удалён.");
            } else {
                System.out.println("Не удалось удалить " + p.getFileName() + " (возможно, файл уже отсутствует).");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении .class: " + e.getMessage());
        }
    }

    // Ввод параметров 

    private static String askType() {
        while (true) {
            System.out.print("Тип массива (int/double): ");
            String s = SC.next().trim().toLowerCase();
            if (s.equals("int") || s.equals("double")) return s;
            System.out.println("Ошибка: введите 'int' или 'double'.");
        }
    }

    // Спрашиваем размер с учётом безопасного лимита под выбранный тип
    private static int askPositiveSizeForType(String type) {
        final int bytesPerElement = type.equals("int") ? 4 : 8;
        final int hardUpperBound = Integer.MAX_VALUE - 8; // технический предел Java-массивов
        final long maxHeap = Runtime.getRuntime().maxMemory(); // максимум heap для JVM

        // Примерно 30% heap — под сам массив, остальное — на строки/временные буферы/стек и т.д.
        long softCapByHeap = (long) (maxHeap * 0.30 / bytesPerElement);

        // Операционный лимит (по времени и печати), чтобы программа была отзывчивой
        int opLimit = type.equals("int") ? OP_LIMIT_INT : OP_LIMIT_DOUBLE;

        // Итоговый безопасный максимум
        long cap = Math.max(1, Math.min(Math.min(softCapByHeap, hardUpperBound), opLimit));

        while (true) {
            System.out.printf("Введите размер массива (> 0, разумный максимум для %s до %d): ",
                    type, cap);
            if (!SC.hasNextLong()) {
                System.out.println("Ошибка: нужно целое число.");
                SC.next();
                continue;
            }
            long nLong = SC.nextLong();
            if (nLong <= 0) {
                System.out.println("Ошибка: размер должен быть > 0.");
                continue;
            }
            if (nLong > cap) {
                System.out.printf("Слишком большой размер. Для типа %s безопасный максимум до %d.%n",
                        type, cap);
                continue;
            }
            return (int) nLong;
        }
    }

    private static int[] askIntBounds() {
        while (true) {
            System.out.print("Введите ГРАНИЦЫ генерации (целые): min max : ");
            if (!SC.hasNextInt()) {
                System.out.println("Ошибка: min должен быть целым.");
                SC.next();
                continue;
            }
            int min = SC.nextInt();
            if (!SC.hasNextInt()) {
                System.out.println("Ошибка: max должен быть целым.");
                SC.next();
                continue;
            }
            int max = SC.nextInt();
            if (min == max) {
                System.out.println("Предупреждение: min == max, массив будет константным.");
                return new int[]{min, max};
            }
            if (min > max) {
                System.out.println("Замечание: min > max, границы будут поменяны местами.");
                int t = min; min = max; max = t;
            }
            return new int[]{min, max};
        }
    }

    private static double[] askDoubleBounds() {
        while (true) {
            System.out.print("Введите ГРАНИЦЫ генерации (вещественные): min max : ");
            if (!SC.hasNextDouble()) {
                System.out.println("Ошибка: min должен быть числом (пример: 1.5).");
                SC.next();
                continue;
            }
            double min = SC.nextDouble();
            if (!SC.hasNextDouble()) {
                System.out.println("Ошибка: max должен быть числом (пример: 10).");
                SC.next();
                continue;
            }
            double max = SC.nextDouble();
            if (min == max) {
                System.out.println("Предупреждение: min == max, массив будет константным.");
                return new double[]{min, max};
            }
            if (min > max) {
                System.out.println("Замечание: min > max, границы будут поменяны местами.");
                double t = min; min = max; max = t;
            }
            return new double[]{min, max};
        }
    }

    // Генерация массивов (с защитой от OOME)

    private static int randIntInclusive(int min, int max) {
        return (int) (Math.random() * (max - min + 1L)) + min;
    }

    private static double randDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    private static int[] safeGenerateIntArray(int n, int min, int max) {
        try {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = randIntInclusive(min, max);
            return a;
        } catch (OutOfMemoryError e) {
            System.out.println("Недостаточно памяти для выделения int-массива такой длины.");
            System.out.println("Уменьшите размер массива или увеличьте -Xmx (максимальный heap) JVM.");
            throw e;
        }
    }

    private static double[] safeGenerateDoubleArray(int n, double min, double max) {
        try {
            double[] a = new double[n];
            for (int i = 0; i < n; i++) a[i] = randDouble(min, max);
            return a;
        } catch (OutOfMemoryError e) {
            System.out.println("Недостаточно памяти для выделения double-массива такой длины.");
            System.out.println("Уменьшите размер массива или увеличьте -Xmx (максимальный heap) JVM.");
            throw e;
        }
    }

    // Статистики: перегрузки

    public static int min(int[] a) {
        int m = a[0];
        for (int v : a) if (v < m) m = v;
        return m;
    }

    public static double min(double[] a) {
        double m = a[0];
        for (double v : a) if (v < m) m = v;
        return m;
    }

    public static int max(int[] a) {
        int m = a[0];
        for (int v : a) if (v > m) m = v;
        return m;
    }

    public static double max(double[] a) {
        double m = a[0];
        for (double v : a) if (v > m) m = v;
        return m;
    }

    public static double avg(int[] a) {
        long sum = 0;
        for (int v : a) sum += v;
        return (double) sum / a.length;
    }

    public static double avg(double[] a) {
        double sum = 0.0;
        for (double v : a) sum += v;
        return sum / a.length;
    }

    // Сортировки

    public static void sortAsc(int[] a)    { sort(a, true); }
    public static void sortDesc(int[] a)   { sort(a, false); }
    public static void sortAsc(double[] a) { sort(a, true); }
    public static void sortDesc(double[] a){ sort(a, false); }

    private static void sort(int[] a, boolean ascending) {
        Arrays.sort(a);
        if (!ascending) reverse(a);
    }

    private static void sort(double[] a, boolean ascending) {
        Arrays.sort(a);
        if (!ascending) reverse(a);
    }

    private static void reverse(int[] a) {
        for (int l = 0, r = a.length - 1; l < r; l++, r--) {
            int t = a[l]; a[l] = a[r]; a[r] = t;
        }
    }

    private static void reverse(double[] a) {
        for (int l = 0, r = a.length - 1; l < r; l++, r--) {
            double t = a[l]; a[l] = a[r]; a[r] = t;
        }
    }

    // Печать с сэмплом (чтобы не падать на огромных массивах)

    private static void printlnIntArray(String title, int[] a) {
        if (a.length <= PRINT_SAMPLE_LIMIT) {
            System.out.println(title + ": " + Arrays.toString(a));
            return;
        }
        System.out.println(title + " (len=" + a.length + "): " + sample(a));
    }

    private static void printlnDoubleArray(String title, double[] a) {
        if (a.length <= PRINT_SAMPLE_LIMIT) {
            System.out.println(title + ": " + format(a, 4));
            return;
        }
        System.out.println(title + " (len=" + a.length + "): " + sample(a, 4));
    }

    private static String sample(int[] a) {
        StringBuilder sb = new StringBuilder("[");
        int k = Math.min(SAMPLE_HEAD_TAIL, a.length);
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(", ");
            sb.append(a[i]);
        }
        if (a.length > 2 * k) sb.append(", ..., ");
        else if (a.length > k) sb.append(", ");
        int startTail = Math.max(k, a.length - k);
        for (int i = startTail; i < a.length; i++) {
            if (i > startTail) sb.append(", ");
            sb.append(a[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    private static String sample(double[] a, int precision) {
        String fmt = "%." + precision + "f";
        StringBuilder sb = new StringBuilder("[");
        int k = Math.min(SAMPLE_HEAD_TAIL, a.length);
        for (int i = 0; i < k; i++) {
            if (i > 0) sb.append(", ");
            sb.append(String.format(Locale.US, fmt, a[i]));
        }
        if (a.length > 2 * k) sb.append(", ..., ");
        else if (a.length > k) sb.append(", ");
        int startTail = Math.max(k, a.length - k);
        for (int i = startTail; i < a.length; i++) {
            if (i > startTail) sb.append(", ");
            sb.append(String.format(Locale.US, fmt, a[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    // Форматирование вывода для небольших double[]
    private static String format(double[] a, int precision) {
        StringBuilder sb = new StringBuilder("[");
        String fmt = "%." + precision + "f";
        for (int i = 0; i < a.length; i++) {
            sb.append(String.format(Locale.US, fmt, a[i]));
            if (i < a.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}

