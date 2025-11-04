/**
 * Ð’Ð°Ñ€Ð¸Ð°Ð½Ñ‚ 1 â€” ÑƒÑ‡ÐµÐ±Ð½Ð¾Ðµ Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ.
 * ÐšÐ¾Ð¼Ð¼ÐµÐ½Ñ‚Ð°Ñ€Ð¸Ð¸ Ð¿Ñ€Ð¸Ð²ÐµÐ´ÐµÐ½Ñ‹ Ðº ÐµÐ´Ð¸Ð½Ð¾Ð¼Ñƒ ÑÑ‚Ð¸Ð»ÑŽ (Ð·Ð°Ð³Ð¾Ð»Ð¾Ð²Ð¾Ðº Ñ„Ð°Ð¹Ð»Ð°, Ñ€Ð°Ð·Ð´ÐµÐ»Ð¸Ñ‚ÐµÐ»Ð¸ ÑÐµÐºÑ†Ð¸Ð¹).
 */
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class variant_1 {
    private static final Scanner SC = new Scanner(System.in);

    // ÐŸÐ¾Ñ€Ð¾Ð³, ÐºÐ¾Ð³Ð´Ð° Ð½Ðµ Ð¿ÐµÑ‡Ð°Ñ‚Ð°ÐµÐ¼ Ð²ÐµÑÑŒ Ð¼Ð°ÑÑÐ¸Ð², Ð° Ð¿Ð¾ÐºÐ°Ð·Ñ‹Ð²Ð°ÐµÐ¼ Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð½ÐµÐ±Ð¾Ð»ÑŒÑˆÑƒÑŽ Ñ‡Ð°ÑÑ‚ÑŒ
    private static final int PRINT_SAMPLE_LIMIT = 2000;     // >2k ÑÐ»ÐµÐ¼ÐµÐ½Ñ‚Ð¾Ð² â€” Ð¿ÐµÑ‡Ð°Ñ‚Ð°ÐµÐ¼ Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð½Ð°Ñ‡Ð°Ð»Ð¾/ÐºÐ¾Ð½ÐµÑ†
    private static final int SAMPLE_HEAD_TAIL   = 20;       // ÑÐºÐ¾Ð»ÑŒÐºÐ¾ ÑÐ»ÐµÐ¼ÐµÐ½Ñ‚Ð¾Ð² Ð¿Ð¾ÐºÐ°Ð·Ð°Ñ‚ÑŒ Ñ Ð½Ð°Ñ‡Ð°Ð»Ð°/ÐºÐ¾Ð½Ñ†Ð°

    // "ÐžÐ¿ÐµÑ€Ð°Ñ†Ð¸Ð¾Ð½Ð½Ñ‹Ðµ" Ð¿Ð¾Ñ‚Ð¾Ð»ÐºÐ¸ Ð¿Ð¾ ÐºÐ¾Ð»Ð¸Ñ‡ÐµÑÑ‚Ð²Ñƒ ÑÐ»ÐµÐ¼ÐµÐ½Ñ‚Ð¾Ð² (Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð½Ðµ ÑƒÐ¼ÐµÑ€ÐµÑ‚ÑŒ Ð¿Ð¾ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸/Ð¿Ð°Ð¼ÑÑ‚Ð¸)
    private static final int OP_LIMIT_INT    = 5_000_000;   // int[]
    private static final int OP_LIMIT_DOUBLE = 3_000_000;   // double[]

    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        SC.useLocale(Locale.US);

        String type = askType(); // "int" Ð¸Ð»Ð¸ "double"
        int n = askPositiveSizeForType(type);

        if (type.equals("int")) {
            int[] bounds = askIntBounds();
            int[] arr = safeGenerateIntArray(n, bounds[0], bounds[1]);

            printlnIntArray("Ð¡Ð³ÐµÐ½ÐµÑ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¹ Ð¼Ð°ÑÑÐ¸Ð² (int)", arr);
            System.out.printf("ÐœÐ¸Ð½Ð¸Ð¼ÑƒÐ¼: %d%n", min(arr));
            System.out.printf("ÐœÐ°ÐºÑÐ¸Ð¼ÑƒÐ¼: %d%n", max(arr));
            System.out.printf("Ð¡Ñ€ÐµÐ´Ð½ÐµÐµ: %.6f%n", avg(arr));

            // Ð¡ÐžÐ Ð¢Ð˜Ð ÐžÐ’ÐšÐ: Ð´Ð»Ñ Ð¾Ñ‡ÐµÐ½ÑŒ Ð±Ð¾Ð»ÑŒÑˆÐ¸Ñ… Ð¼Ð°ÑÑÐ¸Ð²Ð¾Ð² ÑÐ¾Ñ€Ñ‚Ð¸Ñ€ÑƒÐµÐ¼ "Ð½Ð° Ð¼ÐµÑÑ‚Ðµ" Ð±ÐµÐ· ÐºÐ»Ð¾Ð½Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ
            if (arr.length <= OP_LIMIT_INT) {
                int[] asc = arr.clone();
                sortAsc(asc);
                printlnIntArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ Ð²Ð¾Ð·Ñ€Ð°ÑÑ‚Ð°Ð½Ð¸ÑŽ", asc);

                int[] desc = asc.clone();
                reverse(desc);
                printlnIntArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ ÑƒÐ±Ñ‹Ð²Ð°Ð½Ð¸ÑŽ", desc);
            } else {
                System.out.println("Ð—Ð°Ð¼ÐµÑ‡Ð°Ð½Ð¸Ðµ: Ð¼Ð°ÑÑÐ¸Ð² Ð¾Ñ‡ÐµÐ½ÑŒ Ð±Ð¾Ð»ÑŒÑˆÐ¾Ð¹ â€” ÑÐ¾Ñ€Ñ‚Ð¸Ñ€ÑƒÐµÐ¼ Ð±ÐµÐ· ÐºÐ¾Ð¿Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ (Ð¼Ð¾Ð¶ÐµÑ‚ Ð·Ð°Ð½ÑÑ‚ÑŒ Ð²Ñ€ÐµÐ¼Ñ).");
                sortAsc(arr);
                printlnIntArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ Ð²Ð¾Ð·Ñ€Ð°ÑÑ‚Ð°Ð½Ð¸ÑŽ (in-place)", arr);

                reverse(arr); // Ñ‚ÐµÐ¿ÐµÑ€ÑŒ ÑƒÐ±Ñ‹Ð²Ð°Ð½Ð¸Ðµ
                printlnIntArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ ÑƒÐ±Ñ‹Ð²Ð°Ð½Ð¸ÑŽ (in-place)", arr);
            }
        } else {
            double[] bounds = askDoubleBounds();
            double[] arr = safeGenerateDoubleArray(n, bounds[0], bounds[1]);

            printlnDoubleArray("Ð¡Ð³ÐµÐ½ÐµÑ€Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð½Ñ‹Ð¹ Ð¼Ð°ÑÑÐ¸Ð² (double)", arr);
            System.out.printf("ÐœÐ¸Ð½Ð¸Ð¼ÑƒÐ¼: %.6f%n", min(arr));
            System.out.printf("ÐœÐ°ÐºÑÐ¸Ð¼ÑƒÐ¼: %.6f%n", max(arr));
            System.out.printf("Ð¡Ñ€ÐµÐ´Ð½ÐµÐµ: %.6f%n", avg(arr));

            if (arr.length <= OP_LIMIT_DOUBLE) {
                double[] asc = arr.clone();
                sortAsc(asc);
                printlnDoubleArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ Ð²Ð¾Ð·Ñ€Ð°ÑÑ‚Ð°Ð½Ð¸ÑŽ", asc);

                double[] desc = asc.clone();
                reverse(desc);
                printlnDoubleArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ ÑƒÐ±Ñ‹Ð²Ð°Ð½Ð¸ÑŽ", desc);
            } else {
                System.out.println("Ð—Ð°Ð¼ÐµÑ‡Ð°Ð½Ð¸Ðµ: Ð¼Ð°ÑÑÐ¸Ð² Ð¾Ñ‡ÐµÐ½ÑŒ Ð±Ð¾Ð»ÑŒÑˆÐ¾Ð¹ â€” ÑÐ¾Ñ€Ñ‚Ð¸Ñ€ÑƒÐµÐ¼ Ð±ÐµÐ· ÐºÐ¾Ð¿Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ñ (Ð¼Ð¾Ð¶ÐµÑ‚ Ð·Ð°Ð½ÑÑ‚ÑŒ Ð²Ñ€ÐµÐ¼Ñ).");
                sortAsc(arr);
                printlnDoubleArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ Ð²Ð¾Ð·Ñ€Ð°ÑÑ‚Ð°Ð½Ð¸ÑŽ (in-place)", arr);

                reverse(arr);
                printlnDoubleArray("Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ° Ð¿Ð¾ ÑƒÐ±Ñ‹Ð²Ð°Ð½Ð¸ÑŽ (in-place)", arr);
            }
        }

        System.out.println("Ð“Ð¾Ñ‚Ð¾Ð²Ð¾.");

        // Ð§Ð¸ÑÑ‚Ð¸Ð¼ Ð·Ð° ÑÐ¾Ð±Ð¾Ð¹ .class
        tryDeleteOwnClassFile();
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }

    // Ð¤ÑƒÐ½ÐºÑ†Ð¸Ñ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ñ .class
    private static void tryDeleteOwnClassFile() {
        try {
            String resName = variant_1.class.getSimpleName() + ".class";
            // URL Ñ€ÐµÑÑƒÑ€ÑÐ° .class (Ð¾Ñ‚Ð½Ð¾ÑÐ¸Ñ‚ÐµÐ»ÑŒÐ½Ð¾ Ð¿Ð°ÐºÐµÑ‚Ð°; Ð·Ð´ÐµÑÑŒ Ð¿Ð°ÐºÐµÑ‚Ð° Ð½ÐµÑ‚)
            java.net.URL url = variant_1.class.getResource(resName);
            if (url == null) {
                // ÐœÐ¾Ð¶ÐµÑ‚ Ð±Ñ‹Ñ‚ÑŒ Ð·Ð°Ð¿ÑƒÑ‰ÐµÐ½Ð¾ Ð¸Ð· Ð½ÐµÑÑ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð½Ð¾Ð³Ð¾ classloader'Ð°
                System.out.println("ÐŸÑ€Ð¾Ð¿ÑƒÑÐºÐ°ÑŽ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ .class: Ñ€ÐµÑÑƒÑ€Ñ Ð½Ðµ Ð½Ð°Ð¹Ð´ÐµÐ½.");
                return;
            }
            if (!"file".equalsIgnoreCase(url.getProtocol())) {
                // ÐÐ°Ð¿Ñ€Ð¸Ð¼ÐµÑ€, Ð·Ð°Ð¿ÑƒÑÐº Ð¸Ð· JAR/zip â€” ÑƒÐ´Ð°Ð»ÑÑ‚ÑŒ Ð½ÐµÑ‡ÐµÐ³Ð¾/Ð½ÐµÐ»ÑŒÐ·Ñ
                System.out.println("ÐŸÑ€Ð¾Ð¿ÑƒÑÐºÐ°ÑŽ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ .class: Ð½Ðµ Ñ„Ð°Ð¹Ð»Ð¾Ð²Ð°Ñ ÑÐ¸ÑÑ‚ÐµÐ¼Ð° (" + url.getProtocol() + ").");
                return;
            }
            java.nio.file.Path p = java.nio.file.Paths.get(url.toURI());
            boolean deleted = java.nio.file.Files.deleteIfExists(p);
            if (deleted) {
                System.out.println("Ð¤Ð°Ð¹Ð» " + p.getFileName() + " Ð±Ñ‹Ð» ÑƒÐ´Ð°Ð»Ñ‘Ð½.");
            } else {
                System.out.println("ÐÐµ ÑƒÐ´Ð°Ð»Ð¾ÑÑŒ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ " + p.getFileName() + " (Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾, Ñ„Ð°Ð¹Ð» ÑƒÐ¶Ðµ Ð¾Ñ‚ÑÑƒÑ‚ÑÑ‚Ð²ÑƒÐµÑ‚).");
            }
        } catch (Exception e) {
            System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ð¸ .class: " + e.getMessage());
        }
    }

    // Ð’Ð²Ð¾Ð´ Ð¿Ð°Ñ€Ð°Ð¼ÐµÑ‚Ñ€Ð¾Ð² 

    private static String askType() {
        while (true) {
            System.out.print("Ð¢Ð¸Ð¿ Ð¼Ð°ÑÑÐ¸Ð²Ð° (int/double): ");
            String s = SC.next().trim().toLowerCase();
            if (s.equals("int") || s.equals("double")) return s;
            System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ 'int' Ð¸Ð»Ð¸ 'double'.");
        }
    }

    // Ð¡Ð¿Ñ€Ð°ÑˆÐ¸Ð²Ð°ÐµÐ¼ Ñ€Ð°Ð·Ð¼ÐµÑ€ Ñ ÑƒÑ‡Ñ‘Ñ‚Ð¾Ð¼ Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ð¾Ð³Ð¾ Ð»Ð¸Ð¼Ð¸Ñ‚Ð° Ð¿Ð¾Ð´ Ð²Ñ‹Ð±Ñ€Ð°Ð½Ð½Ñ‹Ð¹ Ñ‚Ð¸Ð¿
    private static int askPositiveSizeForType(String type) {
        final int bytesPerElement = type.equals("int") ? 4 : 8;
        final int hardUpperBound = Integer.MAX_VALUE - 8; // Ñ‚ÐµÑ…Ð½Ð¸Ñ‡ÐµÑÐºÐ¸Ð¹ Ð¿Ñ€ÐµÐ´ÐµÐ» Java-Ð¼Ð°ÑÑÐ¸Ð²Ð¾Ð²
        final long maxHeap = Runtime.getRuntime().maxMemory(); // Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ heap Ð´Ð»Ñ JVM

        // ÐŸÑ€Ð¸Ð¼ÐµÑ€Ð½Ð¾ 30% heap â€” Ð¿Ð¾Ð´ ÑÐ°Ð¼ Ð¼Ð°ÑÑÐ¸Ð², Ð¾ÑÑ‚Ð°Ð»ÑŒÐ½Ð¾Ðµ â€” Ð½Ð° ÑÑ‚Ñ€Ð¾ÐºÐ¸/Ð²Ñ€ÐµÐ¼ÐµÐ½Ð½Ñ‹Ðµ Ð±ÑƒÑ„ÐµÑ€Ñ‹/ÑÑ‚ÐµÐº Ð¸ Ñ‚.Ð´.
        long softCapByHeap = (long) (maxHeap * 0.30 / bytesPerElement);

        // ÐžÐ¿ÐµÑ€Ð°Ñ†Ð¸Ð¾Ð½Ð½Ñ‹Ð¹ Ð»Ð¸Ð¼Ð¸Ñ‚ (Ð¿Ð¾ Ð²Ñ€ÐµÐ¼ÐµÐ½Ð¸ Ð¸ Ð¿ÐµÑ‡Ð°Ñ‚Ð¸), Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¿Ñ€Ð¾Ð³Ñ€Ð°Ð¼Ð¼Ð° Ð±Ñ‹Ð»Ð° Ð¾Ñ‚Ð·Ñ‹Ð²Ñ‡Ð¸Ð²Ð¾Ð¹
        int opLimit = type.equals("int") ? OP_LIMIT_INT : OP_LIMIT_DOUBLE;

        // Ð˜Ñ‚Ð¾Ð³Ð¾Ð²Ñ‹Ð¹ Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ñ‹Ð¹ Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼
        long cap = Math.max(1, Math.min(Math.min(softCapByHeap, hardUpperBound), opLimit));

        while (true) {
            System.out.printf("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ñ€Ð°Ð·Ð¼ÐµÑ€ Ð¼Ð°ÑÑÐ¸Ð²Ð° (> 0, Ñ€Ð°Ð·ÑƒÐ¼Ð½Ñ‹Ð¹ Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ Ð´Ð»Ñ %s Ð´Ð¾ %d): ",
                    type, cap);
            if (!SC.hasNextLong()) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: Ð½ÑƒÐ¶Ð½Ð¾ Ñ†ÐµÐ»Ð¾Ðµ Ñ‡Ð¸ÑÐ»Ð¾.");
                SC.next();
                continue;
            }
            long nLong = SC.nextLong();
            if (nLong <= 0) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: Ñ€Ð°Ð·Ð¼ÐµÑ€ Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ > 0.");
                continue;
            }
            if (nLong > cap) {
                System.out.printf("Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð±Ð¾Ð»ÑŒÑˆÐ¾Ð¹ Ñ€Ð°Ð·Ð¼ÐµÑ€. Ð”Ð»Ñ Ñ‚Ð¸Ð¿Ð° %s Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ñ‹Ð¹ Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ Ð´Ð¾ %d.%n",
                        type, cap);
                continue;
            }
            return (int) nLong;
        }
    }

    private static int[] askIntBounds() {
        while (true) {
            System.out.print("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð“Ð ÐÐÐ˜Ð¦Ð« Ð³ÐµÐ½ÐµÑ€Ð°Ñ†Ð¸Ð¸ (Ñ†ÐµÐ»Ñ‹Ðµ): min max : ");
            if (!SC.hasNextInt()) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: min Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ñ†ÐµÐ»Ñ‹Ð¼.");
                SC.next();
                continue;
            }
            int min = SC.nextInt();
            if (!SC.hasNextInt()) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: max Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ñ†ÐµÐ»Ñ‹Ð¼.");
                SC.next();
                continue;
            }
            int max = SC.nextInt();
            if (min == max) {
                System.out.println("ÐŸÑ€ÐµÐ´ÑƒÐ¿Ñ€ÐµÐ¶Ð´ÐµÐ½Ð¸Ðµ: min == max, Ð¼Ð°ÑÑÐ¸Ð² Ð±ÑƒÐ´ÐµÑ‚ ÐºÐ¾Ð½ÑÑ‚Ð°Ð½Ñ‚Ð½Ñ‹Ð¼.");
                return new int[]{min, max};
            }
            if (min > max) {
                System.out.println("Ð—Ð°Ð¼ÐµÑ‡Ð°Ð½Ð¸Ðµ: min > max, Ð³Ñ€Ð°Ð½Ð¸Ñ†Ñ‹ Ð±ÑƒÐ´ÑƒÑ‚ Ð¿Ð¾Ð¼ÐµÐ½ÑÐ½Ñ‹ Ð¼ÐµÑÑ‚Ð°Ð¼Ð¸.");
                int t = min; min = max; max = t;
            }
            return new int[]{min, max};
        }
    }

    private static double[] askDoubleBounds() {
        while (true) {
            System.out.print("Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ Ð“Ð ÐÐÐ˜Ð¦Ð« Ð³ÐµÐ½ÐµÑ€Ð°Ñ†Ð¸Ð¸ (Ð²ÐµÑ‰ÐµÑÑ‚Ð²ÐµÐ½Ð½Ñ‹Ðµ): min max : ");
            if (!SC.hasNextDouble()) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: min Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ñ‡Ð¸ÑÐ»Ð¾Ð¼ (Ð¿Ñ€Ð¸Ð¼ÐµÑ€: 1.5).");
                SC.next();
                continue;
            }
            double min = SC.nextDouble();
            if (!SC.hasNextDouble()) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: max Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ñ‡Ð¸ÑÐ»Ð¾Ð¼ (Ð¿Ñ€Ð¸Ð¼ÐµÑ€: 10).");
                SC.next();
                continue;
            }
            double max = SC.nextDouble();
            if (min == max) {
                System.out.println("ÐŸÑ€ÐµÐ´ÑƒÐ¿Ñ€ÐµÐ¶Ð´ÐµÐ½Ð¸Ðµ: min == max, Ð¼Ð°ÑÑÐ¸Ð² Ð±ÑƒÐ´ÐµÑ‚ ÐºÐ¾Ð½ÑÑ‚Ð°Ð½Ñ‚Ð½Ñ‹Ð¼.");
                return new double[]{min, max};
            }
            if (min > max) {
                System.out.println("Ð—Ð°Ð¼ÐµÑ‡Ð°Ð½Ð¸Ðµ: min > max, Ð³Ñ€Ð°Ð½Ð¸Ñ†Ñ‹ Ð±ÑƒÐ´ÑƒÑ‚ Ð¿Ð¾Ð¼ÐµÐ½ÑÐ½Ñ‹ Ð¼ÐµÑÑ‚Ð°Ð¼Ð¸.");
                double t = min; min = max; max = t;
            }
            return new double[]{min, max};
        }
    }

    // Ð“ÐµÐ½ÐµÑ€Ð°Ñ†Ð¸Ñ Ð¼Ð°ÑÑÐ¸Ð²Ð¾Ð² (Ñ Ð·Ð°Ñ‰Ð¸Ñ‚Ð¾Ð¹ Ð¾Ñ‚ OOME)

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
            System.out.println("ÐÐµÐ´Ð¾ÑÑ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ Ð¿Ð°Ð¼ÑÑ‚Ð¸ Ð´Ð»Ñ Ð²Ñ‹Ð´ÐµÐ»ÐµÐ½Ð¸Ñ int-Ð¼Ð°ÑÑÐ¸Ð²Ð° Ñ‚Ð°ÐºÐ¾Ð¹ Ð´Ð»Ð¸Ð½Ñ‹.");
            System.out.println("Ð£Ð¼ÐµÐ½ÑŒÑˆÐ¸Ñ‚Ðµ Ñ€Ð°Ð·Ð¼ÐµÑ€ Ð¼Ð°ÑÑÐ¸Ð²Ð° Ð¸Ð»Ð¸ ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÑŒÑ‚Ðµ -Xmx (Ð¼Ð°ÐºÑÐ¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ð¹ heap) JVM.");
            throw e;
        }
    }

    private static double[] safeGenerateDoubleArray(int n, double min, double max) {
        try {
            double[] a = new double[n];
            for (int i = 0; i < n; i++) a[i] = randDouble(min, max);
            return a;
        } catch (OutOfMemoryError e) {
            System.out.println("ÐÐµÐ´Ð¾ÑÑ‚Ð°Ñ‚Ð¾Ñ‡Ð½Ð¾ Ð¿Ð°Ð¼ÑÑ‚Ð¸ Ð´Ð»Ñ Ð²Ñ‹Ð´ÐµÐ»ÐµÐ½Ð¸Ñ double-Ð¼Ð°ÑÑÐ¸Ð²Ð° Ñ‚Ð°ÐºÐ¾Ð¹ Ð´Ð»Ð¸Ð½Ñ‹.");
            System.out.println("Ð£Ð¼ÐµÐ½ÑŒÑˆÐ¸Ñ‚Ðµ Ñ€Ð°Ð·Ð¼ÐµÑ€ Ð¼Ð°ÑÑÐ¸Ð²Ð° Ð¸Ð»Ð¸ ÑƒÐ²ÐµÐ»Ð¸Ñ‡ÑŒÑ‚Ðµ -Xmx (Ð¼Ð°ÐºÑÐ¸Ð¼Ð°Ð»ÑŒÐ½Ñ‹Ð¹ heap) JVM.");
            throw e;
        }
    }

    // Ð¡Ñ‚Ð°Ñ‚Ð¸ÑÑ‚Ð¸ÐºÐ¸: Ð¿ÐµÑ€ÐµÐ³Ñ€ÑƒÐ·ÐºÐ¸

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

    // Ð¡Ð¾Ñ€Ñ‚Ð¸Ñ€Ð¾Ð²ÐºÐ¸

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

    // ÐŸÐµÑ‡Ð°Ñ‚ÑŒ Ñ ÑÑÐ¼Ð¿Ð»Ð¾Ð¼ (Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð½Ðµ Ð¿Ð°Ð´Ð°Ñ‚ÑŒ Ð½Ð° Ð¾Ð³Ñ€Ð¾Ð¼Ð½Ñ‹Ñ… Ð¼Ð°ÑÑÐ¸Ð²Ð°Ñ…)

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

    // Ð¤Ð¾Ñ€Ð¼Ð°Ñ‚Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ Ð²Ñ‹Ð²Ð¾Ð´Ð° Ð´Ð»Ñ Ð½ÐµÐ±Ð¾Ð»ÑŒÑˆÐ¸Ñ… double[]
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

