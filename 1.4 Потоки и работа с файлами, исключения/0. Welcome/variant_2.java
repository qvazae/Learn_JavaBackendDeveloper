/**
 * Ð’Ð°Ñ€Ð¸Ð°Ð½Ñ‚ 2 â€” ÑƒÑ‡ÐµÐ±Ð½Ð¾Ðµ Ñ€ÐµÑˆÐµÐ½Ð¸Ðµ.
 * ÐšÐ¾Ð¼Ð¼ÐµÐ½Ñ‚Ð°Ñ€Ð¸Ð¸ Ð¿Ñ€Ð¸Ð²ÐµÐ´ÐµÐ½Ñ‹ Ðº ÐµÐ´Ð¸Ð½Ð¾Ð¼Ñƒ ÑÑ‚Ð¸Ð»ÑŽ (Ð·Ð°Ð³Ð¾Ð»Ð¾Ð²Ð¾Ðº Ñ„Ð°Ð¹Ð»Ð°, Ñ€Ð°Ð·Ð´ÐµÐ»Ð¸Ñ‚ÐµÐ»Ð¸ ÑÐµÐºÑ†Ð¸Ð¹).
 */
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class variant_2 {

    // ÐÐ°ÑÑ‚Ñ€Ð¾Ð¹ÐºÐ¸ Ð²Ñ‹Ñ‡Ð¸ÑÐ»ÐµÐ½Ð¸Ð¹
    private static final MathContext MC = MathContext.DECIMAL128;
    private static final int DIV_SCALE = 50;

    private static final Scanner SC = new Scanner(System.in);

    // Ð’Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ðµ Ð²Ð¸Ð´Ð°: <Ñ‡Ð¸ÑÐ»Ð¾> <Ð¾Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€> <Ñ‡Ð¸ÑÐ»Ð¾>
    private static final Pattern EXPR = Pattern.compile(
            "^\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*(//|[+\\-*/%^])\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*$"
    );
    private static final Pattern INT_LIKE = Pattern.compile("[+-]?\\d+");

    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        // Hook Ð½Ð° Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ðµ â€” Ð¿Ð¾Ð¿Ñ€Ð¾Ð±ÑƒÐµÐ¼ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ ÑÐ²Ð¾Ð¸ .class Ð´Ð°Ð¶Ðµ Ð¿Ñ€Ð¸ Ð°Ð²Ð°Ñ€Ð¸Ð¹Ð½Ð¾Ð¼ Ð²Ñ‹Ñ…Ð¾Ð´Ðµ
        Runtime.getRuntime().addShutdownHook(new Thread(variant_2::tryDeleteOwnClassFile));

        printHelp();

        while (true) {
            System.out.print("> ");
            String line;
            try {
                if (!SC.hasNextLine()) {
                    System.out.println("Ð’Ñ…Ð¾Ð´Ð½Ñ‹Ðµ Ð´Ð°Ð½Ð½Ñ‹Ðµ Ð·Ð°ÐºÐ¾Ð½Ñ‡Ð¸Ð»Ð¸ÑÑŒ. Ð’Ñ‹Ñ…Ð¾Ð´...");
                    break;
                }
                line = SC.nextLine();
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Ð’Ñ…Ð¾Ð´Ð½Ñ‹Ðµ Ð´Ð°Ð½Ð½Ñ‹Ðµ Ð½ÐµÐ´Ð¾ÑÑ‚ÑƒÐ¿Ð½Ñ‹. Ð’Ñ‹Ñ…Ð¾Ð´...");
                break;
            }

            line = line.trim();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Ð’Ñ‹Ñ…Ð¾Ð´...");
                break;
            }
            if (line.isEmpty()) {
                System.out.println("ÐÐµÐ²ÐµÑ€Ð½Ð¾Ðµ Ð²Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ðµ. Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ ÐµÑ‰Ñ‘ Ñ€Ð°Ð·:");
                continue;
            }

            // Ð’Ð°Ð»Ð¸Ð´Ð°Ñ†Ð¸Ñ Ð²Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ñ
            ParseResult pr = validateExpression(line);
            if (!pr.ok) {
                // ÐŸÐ¾ÐºÐ°Ð¶ÐµÐ¼ ÐºÐ¾Ð½ÐºÑ€ÐµÑ‚Ð½ÑƒÑŽ Ð¿Ñ€Ð¸Ñ‡Ð¸Ð½Ñƒ, ÐµÑÐ»Ð¸ Ð¾Ð½Ð° ÐµÑÑ‚ÑŒ
                if (pr.errorMessage != null && !pr.errorMessage.isBlank()) {
                    System.out.println(pr.errorMessage);
                } else {
                    System.out.println("ÐÐµÐ²ÐµÑ€Ð½Ð¾Ðµ Ð²Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ðµ. Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ ÐµÑ‰Ñ‘ Ñ€Ð°Ð·:");
                }
                continue;
            }

            // Ð’Ñ‹Ñ‡Ð¸ÑÐ»ÐµÐ½Ð¸Ðµ Ñ€ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚Ð°
            try {
                String op = pr.operator;
                String result;
                switch (op) {
                    case "+" -> result = sum(new BigDecimal(pr.left), new BigDecimal(pr.right));
                    case "-" -> result = subtract(new BigDecimal(pr.left), new BigDecimal(pr.right));
                    case "*" -> result = multiply(new BigDecimal(pr.left), new BigDecimal(pr.right));
                    case "/" -> result = divide(new BigDecimal(pr.left), new BigDecimal(pr.right));
                    case "//" -> result = intDivide(new BigInteger(requireInteger(pr.left)),
                                                    new BigInteger(requireInteger(pr.right)));
                    case "%" -> result = mod(new BigInteger(requireInteger(pr.left)),
                                             new BigInteger(requireInteger(pr.right)));
                    case "^" -> result = pow(new BigInteger(requireInteger(pr.left)),
                                             new BigInteger(requireInteger(pr.right)));
                    default -> {
                        System.out.println("ÐÐµÐ¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÐ¼Ð°Ñ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ñ.");
                        continue;
                    }
                }
                System.out.println("Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚: " + result);
            } catch (ArithmeticException ae) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð²Ñ‹Ñ‡Ð¸ÑÐ»ÐµÐ½Ð¸Ñ: " + ae.getMessage());
            } catch (NumberFormatException nfe) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð²Ð²Ð¾Ð´Ð°: Ð´Ð»Ñ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¹ //, % Ð¸ ^ Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑÑ Ñ†ÐµÐ»Ñ‹Ðµ Ñ‡Ð¸ÑÐ»Ð°.");
            } catch (IllegalArgumentException iae) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: " + iae.getMessage());
            } catch (Exception e) {
                System.out.println("ÐÐµÐ¿Ñ€ÐµÐ´Ð²Ð¸Ð´ÐµÐ½Ð½Ð°Ñ Ð¾ÑˆÐ¸Ð±ÐºÐ°: " + e.getMessage());
            }
        }

        // Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ .class Ð² ÐºÐ¾Ð½Ñ†Ðµ Ð¾Ð±Ñ‹Ñ‡Ð½Ð¾Ð³Ð¾ Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¸Ñ
        tryDeleteOwnClassFile();
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }

    // Ð¡Ð¿Ñ€Ð°Ð²ÐºÐ°

    private static void printHelp() {
        System.out.println("ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÐ¼Ñ‹Ðµ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸:");
        System.out.println("  +  : ÑÐ»Ð¾Ð¶ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  -  : Ð²Ñ‹Ñ‡Ð¸Ñ‚Ð°Ð½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  *  : ÑƒÐ¼Ð½Ð¾Ð¶ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  /  : Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ), Ð¾ÐºÑ€ÑƒÐ³Ð»ÐµÐ½Ð¸Ðµ Ð´Ð¾ " + DIV_SCALE + " Ð·Ð½Ð°ÐºÐ¾Ð²");
        System.out.println("  // : Ñ†ÐµÐ»Ð¾Ñ‡Ð¸ÑÐ»ÐµÐ½Ð½Ð¾Ðµ Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  %  : Ð¾ÑÑ‚Ð°Ñ‚Ð¾Ðº Ð¾Ñ‚ Ð´ÐµÐ»ÐµÐ½Ð¸Ñ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  ^  : Ð²Ð¾Ð·Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ Ð² ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ, Ð¿Ð¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸ â€” Ñ†ÐµÐ»Ð¾Ðµ Ð½ÐµÐ¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ðµ)");
        System.out.println("Ð¤Ð¾Ñ€Ð¼Ð°Ñ‚ Ð²Ð²Ð¾Ð´Ð°: <Ñ‡Ð¸ÑÐ»Ð¾> <Ð¾Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€> <Ñ‡Ð¸ÑÐ»Ð¾>, Ð½Ð°Ð¿Ñ€Ð¸Ð¼ÐµÑ€: 15 + 29");
        System.out.println("Ð”Ð»Ñ Ð²Ñ‹Ñ…Ð¾Ð´Ð° Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ: exit");
        System.out.println();
    }

    // Ð’Ð°Ð»Ð¸Ð´Ð°Ñ†Ð¸Ñ Ð²Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ñ

    private static ParseResult validateExpression(String s) {
        Matcher m = EXPR.matcher(s);
        if (!m.matches()) {
            return ParseResult.error();
        }
        String left = m.group(1);
        String op = m.group(2);
        String right = m.group(3);

        // ÐŸÑ€Ð¾Ð±ÑƒÐµÐ¼ Ñ€Ð°ÑÐ¿Ð°Ñ€ÑÐ¸Ñ‚ÑŒ Ñ‡Ð¸ÑÐ»Ð° ÐºÐ°Ðº BigDecimal (Ð±ÐµÐ· Ð¿ÐµÑ€ÐµÐ¿Ð¾Ð»Ð½ÐµÐ½Ð¸Ð¹ Ð¿Ñ€Ð¸Ð¼Ð¸Ñ‚Ð¸Ð²Ð¾Ð²)
        try {
            new BigDecimal(left);
            new BigDecimal(right);
        } catch (NumberFormatException e) {
            return ParseResult.error();
        }

        // Ð”Ð¾Ð¿. Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸ Ð¿Ð¾ Ñ‚Ð¸Ð¿Ñƒ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸
        switch (op) {
            case "/" -> {
                if (isZeroDecimal(right)) {
                    return ParseResult.error("ÐžÑˆÐ¸Ð±ÐºÐ° - Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° 0.");
                }
            }
            case "//", "%" -> {
                if (!isIntegerLike(left) || !isIntegerLike(right)) {
                    return ParseResult.error("Ð”Ð»Ñ // Ð¸ % Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑÑ Ñ†ÐµÐ»Ñ‹Ðµ Ñ‡Ð¸ÑÐ»Ð°.");
                }
                if (isZeroInteger(right)) {
                    return ParseResult.error("ÐžÑˆÐ¸Ð±ÐºÐ° - Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° 0.");
                }
            }
            case "^" -> {
                if (!isIntegerLike(left) || !isIntegerLike(right)) {
                    return ParseResult.error("Ð”Ð»Ñ ^ Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑÑ Ñ†ÐµÐ»Ñ‹Ðµ Ñ‡Ð¸ÑÐ»Ð°.");
                }
                try {
                    BigInteger exp = new BigInteger(requireInteger(right));
                    if (exp.signum() < 0) {
                        return ParseResult.error("ÐŸÐ¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸ Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ð½ÐµÐ¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼.");
                    }
                    if (exp.compareTo(BigInteger.valueOf(10000)) > 0) {
                        return ParseResult.error("Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð±Ð¾Ð»ÑŒÑˆÐ°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ (Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ 10000 Ð´Ð»Ñ Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ð¾ÑÑ‚Ð¸).");
                    }
                } catch (NumberFormatException ex) {
                    return ParseResult.error("ÐÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹ Ð¿Ð¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸.");
                }
            }
        }

        return ParseResult.ok(left, op, right);
    }

    // ÐŸÐ¾Ð¼Ð¾Ñ‰Ð½Ð¸ÐºÐ¸ Ð¿Ñ€Ð¾Ð²ÐµÑ€ÐºÐ¸ Ñ‡Ð¸ÑÐµÐ»

    private static boolean isIntegerLike(String s) {
        return INT_LIKE.matcher(s).matches();
    }

    private static boolean isZeroInteger(String s) {
        try {
            return new BigInteger(requireInteger(s)).equals(BigInteger.ZERO);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isZeroDecimal(String s) {
        try {
            return new BigDecimal(s).compareTo(BigDecimal.ZERO) == 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Ð‘Ñ€Ð¾ÑÐ°ÐµÐ¼ NumberFormatException, ÐµÑÐ»Ð¸ Ñ‡Ð¸ÑÐ»Ð¾ Ð½Ðµ Ñ†ÐµÐ»Ð¾Ðµ â€” Ñ‡Ñ‚Ð¾Ð±Ñ‹ ÐºÐ¾Ñ€Ñ€ÐµÐºÑ‚Ð½Ð¾ Ð¾Ñ‚Ð»Ð¾Ð²Ð¸Ñ‚ÑŒ
    private static String requireInteger(String s) {
        if (!isIntegerLike(s)) throw new NumberFormatException("not integer");
        return s;
    }

    // ÐžÐ¿ÐµÑ€Ð°Ñ†Ð¸Ð¸

    private static String sum(BigDecimal a, BigDecimal b) {
        return a.add(b, MC).stripTrailingZeros().toPlainString();
    }

    private static String subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b, MC).stripTrailingZeros().toPlainString();
    }

    private static String multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b, MC).stripTrailingZeros().toPlainString();
    }

    private static String divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        try {
            return a.divide(b, MC).stripTrailingZeros().toPlainString();
        } catch (ArithmeticException e) {
            return a.divide(b, DIV_SCALE, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        }
    }

    private static String intDivide(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        return a.divide(b).toString();
    }

    private static String mod(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        return a.remainder(b).toString();
    }

    private static String pow(BigInteger base, BigInteger exponent) {
        if (exponent.signum() < 0) {
            throw new IllegalArgumentException("ÐžÑ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ Ð´Ð»Ñ Ñ†ÐµÐ»Ñ‹Ñ… Ð½Ðµ Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚ÑÑ.");
        }
        if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð±Ð¾Ð»ÑŒÑˆÐ°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ.");
        }
        int exp = exponent.intValue();
        return base.pow(exp).toString();
    }

    // Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚ Ð¿Ð°Ñ€ÑÐ¸Ð½Ð³Ð°

    private static class ParseResult {
        final boolean ok;
        final String left;
        final String operator;
        final String right;
        final String errorMessage;

        private ParseResult(boolean ok, String left, String operator, String right, String errorMessage) {
            this.ok = ok;
            this.left = left;
            this.operator = operator;
            this.right = right;
            this.errorMessage = errorMessage;
        }

        static ParseResult ok(String left, String operator, String right) {
            return new ParseResult(true, left, operator, right, null);
        }

        static ParseResult error() {
            return new ParseResult(false, null, null, null, null);
        }

        static ParseResult error(String msg) {
            return new ParseResult(false, null, null, null, msg);
        }
    }

    // Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ ÑÐ¾Ð±ÑÑ‚Ð²ÐµÐ½Ð½Ñ‹Ñ… .class

    private static void tryDeleteOwnClassFile() {
        try {
            String mainName = variant_2.class.getSimpleName() + ".class";
            java.net.URL url = variant_2.class.getResource(mainName);
            if (url == null) {
                System.out.println("Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ Ñ„Ð°Ð¹Ð»Ð¾Ð² .class Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¾.");
                return;
            }
            if (!"file".equalsIgnoreCase(url.getProtocol())) {
                System.out.println("ÐŸÑ€Ð¾Ð¿ÑƒÑÐºÐ°ÑŽ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ .class: Ð½Ðµ Ñ„Ð°Ð¹Ð»Ð¾Ð²Ð°Ñ ÑÐ¸ÑÑ‚ÐµÐ¼Ð° (" + url.getProtocol() + ").");
                return;
            }

            java.nio.file.Path mainPath = java.nio.file.Paths.get(url.toURI());
            java.nio.file.Path dir = mainPath.getParent();
            String baseName = variant_2.class.getSimpleName();

            try (java.util.stream.Stream<java.nio.file.Path> files = java.nio.file.Files.list(dir)) {
                files.filter(p -> {
                    String fn = p.getFileName().toString();
                    return fn.equals(baseName + ".class") || fn.startsWith(baseName + "$");
                }).forEach(p -> {
                    try {
                        boolean deleted = java.nio.file.Files.deleteIfExists(p);
                        if (deleted) {
                            System.out.println("Ð¤Ð°Ð¹Ð» " + p.getFileName() + " Ð±Ñ‹Ð» ÑƒÐ´Ð°Ð»Ñ‘Ð½.");
                        }
                    } catch (Exception e) {
                        System.out.println("ÐÐµ ÑƒÐ´Ð°Ð»Ð¾ÑÑŒ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ " + p.getFileName() + ": " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ð¸ .class: " + e.getMessage());
        }
    }
}

