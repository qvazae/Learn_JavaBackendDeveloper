import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class variant_1 {

    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            ClassCleaner.tryDeleteAllFromSameSource(variant_1.class)
        ));

        ConsoleUI.run();
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }
}

// UI ÑÐ»Ð¾Ð¹
final class ConsoleUI {
    private static final Scanner SC = new Scanner(System.in);
    private static final Calculator CALC = new Calculator();
    private static final ExpressionValidator VALIDATOR = new ExpressionValidator();

    static void run() {
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

            try {
                Operation op = VALIDATOR.parse(line);
                String result = CALC.calculate(op);
                System.out.println("Ð ÐµÐ·ÑƒÐ»ÑŒÑ‚Ð°Ñ‚: " + result);
            } catch (ValidationException ve) {
                String msg = ve.getMessage();
                if (msg == null || msg.isBlank()) {
                    System.out.println("ÐÐµÐ²ÐµÑ€Ð½Ð¾Ðµ Ð²Ñ‹Ñ€Ð°Ð¶ÐµÐ½Ð¸Ðµ. Ð’Ð²ÐµÐ´Ð¸Ñ‚Ðµ ÐµÑ‰Ñ‘ Ñ€Ð°Ð·:");
                } else {
                    System.out.println(msg);
                }
            } catch (ArithmeticException ae) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð²Ñ‹Ñ‡Ð¸ÑÐ»ÐµÐ½Ð¸Ñ: " + ae.getMessage());
            } catch (IllegalArgumentException iae) {
                System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ°: " + iae.getMessage());
            } catch (Exception e) {
                System.out.println("ÐÐµÐ¿Ñ€ÐµÐ´Ð²Ð¸Ð´ÐµÐ½Ð½Ð°Ñ Ð¾ÑˆÐ¸Ð±ÐºÐ°: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("ÐŸÐ¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÐ¼Ñ‹Ðµ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸:");
        System.out.println("  +  : ÑÐ»Ð¾Ð¶ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  -  : Ð²Ñ‹Ñ‡Ð¸Ñ‚Ð°Ð½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  *  : ÑƒÐ¼Ð½Ð¾Ð¶ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  /  : Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ (Ð´ÐµÑÑÑ‚Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¸ Ñ†ÐµÐ»Ñ‹Ðµ), Ð¾ÐºÑ€ÑƒÐ³Ð»ÐµÐ½Ð¸Ðµ Ð´Ð¾ " + Calculator.DIV_SCALE + " Ð·Ð½Ð°ÐºÐ¾Ð²");
        System.out.println("  // : Ñ†ÐµÐ»Ð¾Ñ‡Ð¸ÑÐ»ÐµÐ½Ð½Ð¾Ðµ Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  %  : Ð¾ÑÑ‚Ð°Ñ‚Ð¾Ðº Ð¾Ñ‚ Ð´ÐµÐ»ÐµÐ½Ð¸Ñ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ)");
        System.out.println("  ^  : Ð²Ð¾Ð·Ð²ÐµÐ´ÐµÐ½Ð¸Ðµ Ð² ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ (Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ñ†ÐµÐ»Ñ‹Ðµ, Ð¿Ð¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸ â€” Ñ†ÐµÐ»Ð¾Ðµ Ð½ÐµÐ¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð¾Ðµ)");
        System.out.println("Ð¤Ð¾Ñ€Ð¼Ð°Ñ‚ Ð²Ð²Ð¾Ð´Ð°: <Ñ‡Ð¸ÑÐ»Ð¾> <Ð¾Ð¿ÐµÑ€Ð°Ñ‚Ð¾Ñ€> <Ñ‡Ð¸ÑÐ»Ð¾>, Ð½Ð°Ð¿Ñ€Ð¸Ð¼ÐµÑ€: 15 + 29");
        System.out.println("Ð”Ð»Ñ Ð²Ñ‹Ñ…Ð¾Ð´Ð° Ð²Ð²ÐµÐ´Ð¸Ñ‚Ðµ: exit");
        System.out.println();
    }
}

// ÐžÑÐ½Ð¾Ð²Ð½Ð°Ñ Ð»Ð¾Ð³Ð¸ÐºÐ°
final class Calculator {
    static final MathContext MC = MathContext.DECIMAL128;
    static final int DIV_SCALE = 50;

    String calculate(Operation op) {
        return op.execute();
    }
}

interface Operation {
    String execute();
    String symbol();
}

abstract class BinaryOperation implements Operation {
    protected final String leftRaw;
    protected final String rightRaw;

    protected BinaryOperation(String leftRaw, String rightRaw) {
        this.leftRaw = leftRaw;
        this.rightRaw = rightRaw;
    }

    protected final BigDecimal dec(String s) { return new BigDecimal(s); }

    protected final String requireIntLike(String s) {
        if (!NumberUtils.isIntegerLike(s)) throw new NumberFormatException("not integer");
        return s;
    }

    protected final BigInteger bi(String s) { return new BigInteger(requireIntLike(s)); }

    protected final String fmt(BigDecimal d) { return d.stripTrailingZeros().toPlainString(); }
}

// ÐšÐ¾Ð½ÐºÑ€ÐµÑ‚Ð½Ñ‹Ðµ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸
final class Addition extends BinaryOperation {
    Addition(String l, String r) { super(l, r); }
    public String symbol() { return "+"; }
    public String execute() {
        BigDecimal a = dec(leftRaw), b = dec(rightRaw);
        return fmt(a.add(b, Calculator.MC));
    }
}

final class Subtraction extends BinaryOperation {
    Subtraction(String l, String r) { super(l, r); }
    public String symbol() { return "-"; }
    public String execute() {
        BigDecimal a = dec(leftRaw), b = dec(rightRaw);
        return fmt(a.subtract(b, Calculator.MC));
    }
}

final class Multiplication extends BinaryOperation {
    Multiplication(String l, String r) { super(l, r); }
    public String symbol() { return "*"; }
    public String execute() {
        BigDecimal a = dec(leftRaw), b = dec(rightRaw);
        return fmt(a.multiply(b, Calculator.MC));
    }
}

final class Division extends BinaryOperation {
    Division(String l, String r) { super(l, r); }
    public String symbol() { return "/"; }
    public String execute() {
        BigDecimal a = dec(leftRaw), b = dec(rightRaw);
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        try {
            return fmt(a.divide(b, Calculator.MC));
        } catch (ArithmeticException e) {
            return fmt(a.divide(b, Calculator.DIV_SCALE, RoundingMode.HALF_UP));
        }
    }
}

final class IntDivision extends BinaryOperation {
    IntDivision(String l, String r) { super(l, r); }
    public String symbol() { return "//"; }
    public String execute() {
        BigInteger a = bi(leftRaw), b = bi(rightRaw);
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        return a.divide(b).toString();
    }
}

final class Modulo extends BinaryOperation {
    Modulo(String l, String r) { super(l, r); }
    public String symbol() { return "%"; }
    public String execute() {
        BigInteger a = bi(leftRaw), b = bi(rightRaw);
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° Ð½Ð¾Ð»ÑŒ");
        return a.remainder(b).toString();
    }
}

final class Power extends BinaryOperation {
    Power(String l, String r) { super(l, r); }
    public String symbol() { return "^"; }
    public String execute() {
        BigInteger base = bi(leftRaw);
        BigInteger expBI = bi(rightRaw);
        if (expBI.signum() < 0) {
            throw new IllegalArgumentException("ÐžÑ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ð°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ Ð´Ð»Ñ Ñ†ÐµÐ»Ñ‹Ñ… Ð½Ðµ Ð¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÑ‚ÑÑ.");
        }
        if (expBI.compareTo(BigInteger.valueOf(10000)) > 0) {
            throw new IllegalArgumentException("Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð±Ð¾Ð»ÑŒÑˆÐ°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ (Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ 10000 Ð´Ð»Ñ Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ð¾ÑÑ‚Ð¸).");
        }
        int exp = expBI.intValue();
        return base.pow(exp).toString();
    }
}

// Ð’Ð°Ð»Ð¸Ð´Ð°Ñ†Ð¸Ñ/ÐŸÐ°Ñ€ÑÐ¸Ð½Ð³
final class ValidationException extends RuntimeException {
    ValidationException(String message) { super(message); }
}

final class ExpressionValidator {
    private static final Pattern EXPR = Pattern.compile(
        "^\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*(//|[+\\-*/%^])\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*$"
    );

    Operation parse(String s) {
        Matcher m = EXPR.matcher(s);
        if (!m.matches()) throw new ValidationException(null);

        String left = m.group(1);
        String op = m.group(2);
        String right = m.group(3);

        try {
            new BigDecimal(left);
            new BigDecimal(right);
        } catch (NumberFormatException e) {
            throw new ValidationException(null);
        }

        switch (op) {
            case "/" -> {
                if (NumberUtils.isZeroDecimal(right)) {
                    throw new ValidationException("ÐžÑˆÐ¸Ð±ÐºÐ° - Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° 0.");
                }
                return new Division(left, right);
            }
            case "//", "%" -> {
                if (!NumberUtils.isIntegerLike(left) || !NumberUtils.isIntegerLike(right)) {
                    throw new ValidationException("Ð”Ð»Ñ // Ð¸ % Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑÑ Ñ†ÐµÐ»Ñ‹Ðµ Ñ‡Ð¸ÑÐ»Ð°.");
                }
                if (NumberUtils.isZeroInteger(right)) {
                    throw new ValidationException("ÐžÑˆÐ¸Ð±ÐºÐ° - Ð´ÐµÐ»ÐµÐ½Ð¸Ðµ Ð½Ð° 0.");
                }
                return op.equals("//") ? new IntDivision(left, right) : new Modulo(left, right);
            }
            case "^" -> {
                if (!NumberUtils.isIntegerLike(left) || !NumberUtils.isIntegerLike(right)) {
                    throw new ValidationException("Ð”Ð»Ñ ^ Ñ‚Ñ€ÐµÐ±ÑƒÑŽÑ‚ÑÑ Ñ†ÐµÐ»Ñ‹Ðµ Ñ‡Ð¸ÑÐ»Ð°.");
                }
                try {
                    BigInteger exp = new BigInteger(right);
                    if (exp.signum() < 0) {
                        throw new ValidationException("ÐŸÐ¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸ Ð´Ð¾Ð»Ð¶ÐµÐ½ Ð±Ñ‹Ñ‚ÑŒ Ð½ÐµÐ¾Ñ‚Ñ€Ð¸Ñ†Ð°Ñ‚ÐµÐ»ÑŒÐ½Ñ‹Ð¼.");
                    }
                    if (exp.compareTo(BigInteger.valueOf(10000)) > 0) {
                        throw new ValidationException("Ð¡Ð»Ð¸ÑˆÐºÐ¾Ð¼ Ð±Ð¾Ð»ÑŒÑˆÐ°Ñ ÑÑ‚ÐµÐ¿ÐµÐ½ÑŒ (Ð¼Ð°ÐºÑÐ¸Ð¼ÑƒÐ¼ 10000 Ð´Ð»Ñ Ð±ÐµÐ·Ð¾Ð¿Ð°ÑÐ½Ð¾ÑÑ‚Ð¸).");
                    }
                } catch (NumberFormatException ex) {
                    throw new ValidationException("ÐÐµÐ²ÐµÑ€Ð½Ñ‹Ð¹ Ð¿Ð¾ÐºÐ°Ð·Ð°Ñ‚ÐµÐ»ÑŒ ÑÑ‚ÐµÐ¿ÐµÐ½Ð¸.");
                }
                return new Power(left, right);
            }
            case "+" -> { return new Addition(left, right); }
            case "-" -> { return new Subtraction(left, right); }
            case "*" -> { return new Multiplication(left, right); }
            default -> throw new ValidationException("ÐÐµÐ¿Ð¾Ð´Ð´ÐµÑ€Ð¶Ð¸Ð²Ð°ÐµÐ¼Ð°Ñ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ñ.");
        }
    }
}

// Ð£Ñ‚Ð¸Ð»Ð¸Ñ‚Ñ‹ Ñ‡Ð¸ÑÐµÐ»
final class NumberUtils {
    private static final Pattern INT_LIKE = Pattern.compile("[+-]?\\d+");

    static boolean isIntegerLike(String s) {
        return INT_LIKE.matcher(s).matches();
    }

    static boolean isZeroInteger(String s) {
        try {
            return new BigInteger(s).equals(BigInteger.ZERO);
        } catch (Exception e) {
            return false;
        }
    }

    static boolean isZeroDecimal(String s) {
        try {
            return new BigDecimal(s).compareTo(BigDecimal.ZERO) == 0;
        } catch (Exception e) {
            return false;
        }
    }
}

// ÐžÑ‡Ð¸ÑÑ‚ÐºÐ° .class 
final class ClassCleaner {
    static void tryDeleteAllFromSameSource(Class<?> anchor) {
        try {
            String anchorClassFile = anchor.getSimpleName() + ".class";
            var url = anchor.getResource(anchorClassFile);
            if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
                System.out.println("ÐŸÑ€Ð¾Ð¿ÑƒÑÐºÐ°ÑŽ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ðµ .class: Ñ€ÐµÑÑƒÑ€Ñ Ð½ÐµÐ´Ð¾ÑÑ‚ÑƒÐ¿ÐµÐ½ Ð¸Ð»Ð¸ Ð½Ðµ Ñ„Ð°Ð¹Ð»Ð¾Ð²Ð°Ñ ÑÐ¸ÑÑ‚ÐµÐ¼Ð°.");
                return;
            }

            Path anchorPath = Paths.get(url.toURI());
            String sourceName = readSourceFileName(anchorPath);
            if (sourceName == null) {
                deleteByPrefix(anchorPath.getParent(), anchor.getSimpleName());
                System.out.println("SourceFile Ð½Ðµ Ð½Ð°Ð¹Ð´ÐµÐ½ â€” Ð²Ñ‹Ð¿Ð¾Ð»Ð½ÐµÐ½Ð° Ñ‡Ð°ÑÑ‚Ð¸Ñ‡Ð½Ð°Ñ Ð¾Ñ‡Ð¸ÑÑ‚ÐºÐ° Ð¿Ð¾ Ð¸Ð¼ÐµÐ½Ð¸ ÐºÐ»Ð°ÑÑÐ°.");
                return;
            }

            Path dir = anchorPath.getParent();
            int deleted = 0;
            try (Stream<Path> files = Files.list(dir)) {
                for (Path p : (Iterable<Path>) files.filter(f -> f.getFileName().toString().endsWith(".class"))::iterator) {
                    String src = readSourceFileName(p);
                    if (sourceName.equals(src)) {
                        try {
                            boolean ok = Files.deleteIfExists(p);
                            if (ok) {
                                deleted++;
                                System.out.println("Ð¤Ð°Ð¹Ð» " + p.getFileName() + " Ð±Ñ‹Ð» ÑƒÐ´Ð°Ð»Ñ‘Ð½.");
                            }
                        } catch (Exception e) {
                            System.out.println("ÐÐµ ÑƒÐ´Ð°Ð»Ð¾ÑÑŒ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ " + p.getFileName() + ": " + e.getMessage());
                        }
                    }
                }
            }
            if (deleted == 0) {
                System.out.println("Ð£Ð´Ð°Ð»ÐµÐ½Ð¸Ðµ Ñ„Ð°Ð¹Ð»Ð¾Ð² .class Ð·Ð°Ð²ÐµÑ€ÑˆÐµÐ½Ð¾.");
            }
        } catch (Exception e) {
            System.out.println("ÐžÑˆÐ¸Ð±ÐºÐ° Ð¿Ñ€Ð¸ ÑƒÐ´Ð°Ð»ÐµÐ½Ð¸Ð¸ .class: " + e.getMessage());
        }
    }

    private static void deleteByPrefix(Path dir, String simpleName) {
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(p -> {
                String fn = p.getFileName().toString();
                return fn.equals(simpleName + ".class") || fn.startsWith(simpleName + "$");
            }).forEach(p -> {
                try { if (Files.deleteIfExists(p)) {
                    System.out.println("Ð¤Ð°Ð¹Ð» " + p.getFileName() + " Ð±Ñ‹Ð» ÑƒÐ´Ð°Ð»Ñ‘Ð½.");
                }} catch (Exception e) {
                    System.out.println("ÐÐµ ÑƒÐ´Ð°Ð»Ð¾ÑÑŒ ÑƒÐ´Ð°Ð»Ð¸Ñ‚ÑŒ " + p.getFileName() + ": " + e.getMessage());
                }
            });
        } catch (IOException ignored) { }
    }

    // Ð§Ð¸Ñ‚Ð°ÐµÑ‚ Ð¸Ð· .class Ð°Ñ‚Ñ€Ð¸Ð±ÑƒÑ‚ SourceFile. Ð’Ð¾Ð·Ð²Ñ€Ð°Ñ‰Ð°ÐµÑ‚ Ð¸Ð¼Ñ Ð¸ÑÑ…Ð¾Ð´Ð½Ð¸ÐºÐ° (Ð½Ð°Ð¿Ñ€Ð¸Ð¼ÐµÑ€, "variant_1.java") Ð¸Ð»Ð¸ null
    private static String readSourceFileName(Path classFile) {
        try (DataInputStream in = new DataInputStream(Files.newInputStream(classFile))) {
            if (in.readInt() != 0xCAFEBABE) return null;
            in.readUnsignedShort(); in.readUnsignedShort();

            int cpCount = in.readUnsignedShort();
            String[] utf8 = new String[cpCount];
            for (int i = 1; i < cpCount; i++) {
                int tag = in.readUnsignedByte();
                switch (tag) {
                    case 1 -> {
                        int len = in.readUnsignedShort();
                        byte[] bytes = in.readNBytes(len);
                        utf8[i] = new String(bytes, StandardCharsets.UTF_8);
                    }
                    case 3, 4 -> in.readInt();
                    case 5, 6 -> { in.readLong(); i++; }
                    case 7, 8, 16, 19, 20 -> in.readUnsignedShort();
                    case 9, 10, 11, 12, 18 -> { in.readUnsignedShort(); in.readUnsignedShort(); } // *ref, NameAndType, InvokeDynamic
                    case 15 -> { in.readUnsignedByte(); in.readUnsignedShort(); }
                    default -> { return null; }
                }
            }

            in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();

            int interfacesCount = in.readUnsignedShort();
            for (int i = 0; i < interfacesCount; i++) in.readUnsignedShort();

            int fieldsCount = in.readUnsignedShort();
            skipMembers(in, fieldsCount);

            int methodsCount = in.readUnsignedShort();
            skipMembers(in, methodsCount);

            int attributesCount = in.readUnsignedShort();
            for (int i = 0; i < attributesCount; i++) {
                int nameIndex = in.readUnsignedShort();
                long length = Integer.toUnsignedLong(in.readInt());
                String name = (nameIndex > 0 && nameIndex < utf8.length) ? utf8[nameIndex] : null;

                if ("SourceFile".equals(name) && length == 2) {
                    int sourceIndex = in.readUnsignedShort();
                    return (sourceIndex > 0 && sourceIndex < utf8.length) ? utf8[sourceIndex] : null;
                } else {
                    long skipped = 0;
                    while (skipped < length) {
                        long s = in.skip(length - skipped);
                        if (s <= 0) { in.readByte(); skipped++; } else skipped += s;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return null;
        }

    private static void skipMembers(DataInputStream in, int count) throws IOException {
        for (int i = 0; i < count; i++) {
            in.readUnsignedShort();
            in.readUnsignedShort();
            in.readUnsignedShort();
            int ac = in.readUnsignedShort();
            for (int j = 0; j < ac; j++) {
                in.readUnsignedShort();
                int len = in.readInt();
                long skipped = 0;
                while (skipped < len) {
                    long s = in.skip(len - skipped);
                    if (s <= 0) { in.readByte(); skipped++; } else skipped += s;
                }
            }
        }
    }
}

