/**
 * Вариант 2 — учебное решение.
 * Комментарии приведены к единому стилю (заголовок файла, разделители секций).
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

    // Настройки вычислений
    private static final MathContext MC = MathContext.DECIMAL128;
    private static final int DIV_SCALE = 50;

    private static final Scanner SC = new Scanner(System.in);

    // Выражение вида: <число> <оператор> <число>
    private static final Pattern EXPR = Pattern.compile(
            "^\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*(//|[+\\-*/%^])\\s*([+-]?(?:\\d+(?:\\.\\d+)?|\\.\\d+))\\s*$"
    );
    private static final Pattern INT_LIKE = Pattern.compile("[+-]?\\d+");

    public static void main(String[] args) {
    // Обёртка для защиты от некорректного ввода и ошибок логики
    try {
        // Hook на завершение — попробуем удалить свои .class даже при аварийном выходе
        Runtime.getRuntime().addShutdownHook(new Thread(variant_2::tryDeleteOwnClassFile));

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

            // Валидация выражения
            ParseResult pr = validateExpression(line);
            if (!pr.ok) {
                // Покажем конкретную причину, если она есть
                if (pr.errorMessage != null && !pr.errorMessage.isBlank()) {
                    System.out.println(pr.errorMessage);
                } else {
                    System.out.println("Неверное выражение. Введите ещё раз:");
                }
                continue;
            }

            // Вычисление результата
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
                        System.out.println("Неподдерживаемая операция.");
                        continue;
                    }
                }
                System.out.println("Результат: " + result);
            } catch (ArithmeticException ae) {
                System.out.println("Ошибка вычисления: " + ae.getMessage());
            } catch (NumberFormatException nfe) {
                System.out.println("Ошибка ввода: для операций //, % и ^ требуются целые числа.");
            } catch (IllegalArgumentException iae) {
                System.out.println("Ошибка: " + iae.getMessage());
            } catch (Exception e) {
                System.out.println("Непредвиденная ошибка: " + e.getMessage());
            }
        }

        // Удаление .class в конце обычного завершения
        tryDeleteOwnClassFile();
    } catch (Exception e) {
        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));
    }
    }

    // Справка

    private static void printHelp() {
        System.out.println("Поддерживаемые операции:");
        System.out.println("  +  : сложение (десятичные и целые)");
        System.out.println("  -  : вычитание (десятичные и целые)");
        System.out.println("  *  : умножение (десятичные и целые)");
        System.out.println("  /  : деление (десятичные и целые), округление до " + DIV_SCALE + " знаков");
        System.out.println("  // : целочисленное деление (только целые)");
        System.out.println("  %  : остаток от деления (только целые)");
        System.out.println("  ^  : возведение в степень (только целые, показатель степени — целое неотрицательное)");
        System.out.println("Формат ввода: <число> <оператор> <число>, например: 15 + 29");
        System.out.println("Для выхода введите: exit");
        System.out.println();
    }

    // Валидация выражения

    private static ParseResult validateExpression(String s) {
        Matcher m = EXPR.matcher(s);
        if (!m.matches()) {
            return ParseResult.error();
        }
        String left = m.group(1);
        String op = m.group(2);
        String right = m.group(3);

        // Пробуем распарсить числа как BigDecimal (без переполнений примитивов)
        try {
            new BigDecimal(left);
            new BigDecimal(right);
        } catch (NumberFormatException e) {
            return ParseResult.error();
        }

        // Доп. проверки по типу операции
        switch (op) {
            case "/" -> {
                if (isZeroDecimal(right)) {
                    return ParseResult.error("Ошибка - деление на 0.");
                }
            }
            case "//", "%" -> {
                if (!isIntegerLike(left) || !isIntegerLike(right)) {
                    return ParseResult.error("Для // и % требуются целые числа.");
                }
                if (isZeroInteger(right)) {
                    return ParseResult.error("Ошибка - деление на 0.");
                }
            }
            case "^" -> {
                if (!isIntegerLike(left) || !isIntegerLike(right)) {
                    return ParseResult.error("Для ^ требуются целые числа.");
                }
                try {
                    BigInteger exp = new BigInteger(requireInteger(right));
                    if (exp.signum() < 0) {
                        return ParseResult.error("Показатель степени должен быть неотрицательным.");
                    }
                    if (exp.compareTo(BigInteger.valueOf(10000)) > 0) {
                        return ParseResult.error("Слишком большая степень (максимум 10000 для безопасности).");
                    }
                } catch (NumberFormatException ex) {
                    return ParseResult.error("Неверный показатель степени.");
                }
            }
        }

        return ParseResult.ok(left, op, right);
    }

    // Помощники проверки чисел

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

    // Бросаем NumberFormatException, если число не целое — чтобы корректно отловить
    private static String requireInteger(String s) {
        if (!isIntegerLike(s)) throw new NumberFormatException("not integer");
        return s;
    }

    // Операции

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
        if (b.compareTo(BigDecimal.ZERO) == 0) throw new ArithmeticException("деление на ноль");
        try {
            return a.divide(b, MC).stripTrailingZeros().toPlainString();
        } catch (ArithmeticException e) {
            return a.divide(b, DIV_SCALE, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        }
    }

    private static String intDivide(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("деление на ноль");
        return a.divide(b).toString();
    }

    private static String mod(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) throw new ArithmeticException("деление на ноль");
        return a.remainder(b).toString();
    }

    private static String pow(BigInteger base, BigInteger exponent) {
        if (exponent.signum() < 0) {
            throw new IllegalArgumentException("Отрицательная степень для целых не поддерживается.");
        }
        if (exponent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new IllegalArgumentException("Слишком большая степень.");
        }
        int exp = exponent.intValue();
        return base.pow(exp).toString();
    }

    // Результат парсинга

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

    // Удаление собственных .class

    private static void tryDeleteOwnClassFile() {
        try {
            String mainName = variant_2.class.getSimpleName() + ".class";
            java.net.URL url = variant_2.class.getResource(mainName);
            if (url == null) {
                System.out.println("Удаление файлов .class завершено.");
                return;
            }
            if (!"file".equalsIgnoreCase(url.getProtocol())) {
                System.out.println("Пропускаю удаление .class: не файловая система (" + url.getProtocol() + ").");
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
                            System.out.println("Файл " + p.getFileName() + " был удалён.");
                        }
                    } catch (Exception e) {
                        System.out.println("Не удалось удалить " + p.getFileName() + ": " + e.getMessage());
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("Ошибка при удалении .class: " + e.getMessage());
        }
    }
}

