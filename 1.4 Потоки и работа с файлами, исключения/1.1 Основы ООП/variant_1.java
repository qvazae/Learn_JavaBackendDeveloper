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
    // Обёртка для защиты от некорректного ввода и ошибок логики
    try {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
            ClassCleaner.tryDeleteAllFromSameSource(variant_1.class)
        ));

        ConsoleUI.run();
    } catch (Exception e) {
        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
    }
    }
}

// UI слой
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
                    System.out.println("Входные данные закончились. Выход...");
                    break;
                }
                line = SC.nextLine();
            } catch (NoSuchElementException | IllegalStateException e) {
                System.out.println("Входные данные недоступны. Выход...");
                break;
            }

            line = line.trim();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Выход...");
                break;
            }
            if (line.isEmpty()) {
                System.out.println("Неверное выражение. Введите ещё раз:");
                continue;
            }

            try {
                Operation op = VALIDATOR.parse(line);
                String result = CALC.calculate(op);
                System.out.println("Результат: " + result);
            } catch (ValidationException ve) {
                String msg = ve.getMessage();
                if (msg == null || msg.isBlank()) {
                    System.out.println("Неверное выражение. Введите ещё раз:");
                } else {
                    System.out.println(msg);
                }
            } catch (ArithmeticException ae) {
                System.out.println("Ошибка вычисления: " + ae.getMessage());
            } catch (IllegalArgumentException iae) {
                System.out.println("Ошибка: " + iae.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
            }
        }
    }

    private static void printHelp() {
        System.out.println("Поддерживаемые операции:");
        System.out.println("  +  : сложение (десятичные и целые)");
        System.out.println("  -  : вычитание (десятичные и целые)");
        System.out.println("  *  : умножение (десятичные и целые)");
        System.out.println("  /  : деление (десятичные и целые), округление до " + Calculator.DIV_SCALE + " знаков");
        System.out.println("  // : целочисленное деление (только целые)");
        System.out.println("  %  : остаток от деления (только целые)");
        System.out.println("  ^  : возведение в степень (только целые, показатель степени — целое неотрицательное)");
        System.out.println("Формат ввода: <число> <оператор> <число>, например: 15 + 29");
        System.out.println("Для выхода введите: exit");
        System.out.println();
    }
}

// Основная логика
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

// Конкретные операции
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
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new ArithmeticException("деление на ноль");
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
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("деление на ноль");
        return a.divide(b).toString();
    }
}

final class Modulo extends BinaryOperation {
    Modulo(String l, String r) { super(l, r); }
    public String symbol() { return "%"; }
    public String execute() {
        BigInteger a = bi(leftRaw), b = bi(rightRaw);
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("деление на ноль");
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
            throw new IllegalArgumentException("Отрицательная степень для целых не поддерживается.");
        }
        if (expBI.compareTo(BigInteger.valueOf(10000)) > 0) {
            throw new IllegalArgumentException("Слишком большая степень (максимум 10000 для безопасности).");
        }
        int exp = expBI.intValue();
        return base.pow(exp).toString();
    }
}

// Валидация/Парсинг
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
                    throw new ValidationException("Ошибка - деление на 0.");
                }
                return new Division(left, right);
            }
            case "//", "%" -> {
                if (!NumberUtils.isIntegerLike(left) || !NumberUtils.isIntegerLike(right)) {
                    throw new ValidationException("Для // и % требуются целые числа.");
                }
                if (NumberUtils.isZeroInteger(right)) {
                    throw new ValidationException("Ошибка - деление на 0.");
                }
                return op.equals("//") ? new IntDivision(left, right) : new Modulo(left, right);
            }
            case "^" -> {
                if (!NumberUtils.isIntegerLike(left) || !NumberUtils.isIntegerLike(right)) {
                    throw new ValidationException("Для ^ требуются целые числа.");
                }
                try {
                    BigInteger exp = new BigInteger(right);
                    if (exp.signum() < 0) {
                        throw new ValidationException("Показатель степени должен быть неотрицательным.");
                    }
                    if (exp.compareTo(BigInteger.valueOf(10000)) > 0) {
                        throw new ValidationException("Слишком большая степень (максимум 10000 для безопасности).");
                    }
                } catch (NumberFormatException ex) {
                    throw new ValidationException("Неверный показатель степени.");
                }
                return new Power(left, right);
            }
            case "+" -> { return new Addition(left, right); }
            case "-" -> { return new Subtraction(left, right); }
            case "*" -> { return new Multiplication(left, right); }
            default -> throw new ValidationException("Неподдерживаемая операция.");
        }
    }
}

// Утилиты чисел
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

// Очистка .class 
final class ClassCleaner {
    static void tryDeleteAllFromSameSource(Class<?> anchor) {
        try {
            String anchorClassFile = anchor.getSimpleName() + ".class";
            var url = anchor.getResource(anchorClassFile);
            if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
                System.out.println("Пропускаю удаление .class: ресурс недоступен или не файловая система.");
                return;
            }

            Path anchorPath = Paths.get(url.toURI());
            String sourceName = readSourceFileName(anchorPath);
            if (sourceName == null) {
                deleteByPrefix(anchorPath.getParent(), anchor.getSimpleName());
                System.out.println("SourceFile не найден — выполнена частичная очистка по имени класса.");
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
                                System.out.println("Файл " + p.getFileName() + " был удалён.");
                            }
                        } catch (Exception e) {
                            System.out.println("Не удалось удалить " + p.getFileName() + ": " + e.getMessage());
                        }
                    }
                }
            }
            if (deleted == 0) {
                System.out.println("Удаление файлов .class завершено.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении .class: " + e.getMessage());
        }
    }

    private static void deleteByPrefix(Path dir, String simpleName) {
        try (Stream<Path> files = Files.list(dir)) {
            files.filter(p -> {
                String fn = p.getFileName().toString();
                return fn.equals(simpleName + ".class") || fn.startsWith(simpleName + "$");
            }).forEach(p -> {
                try { if (Files.deleteIfExists(p)) {
                    System.out.println("Файл " + p.getFileName() + " был удалён.");
                }} catch (Exception e) {
                    System.out.println("Не удалось удалить " + p.getFileName() + ": " + e.getMessage());
                }
            });
        } catch (IOException ignored) { }
    }

    // Читает из .class атрибут SourceFile. Возвращает имя исходника (например, "variant_1.java") или null
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

