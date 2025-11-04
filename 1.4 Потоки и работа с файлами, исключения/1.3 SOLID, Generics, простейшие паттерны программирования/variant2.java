/**
 * Ð’Ð°Ñ€Ð¸Ð°Ð½Ñ‚ 2 â€” Ð¿Ð°Ñ‚Ñ‚ÐµÑ€Ð½ Â«ÐÐ°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÑŒÂ».
 *
 * Ð¦ÐµÐ»ÑŒ:
 * - Ð ÐµÐ°Ð»Ð¸Ð·Ð¾Ð²Ð°Ñ‚ÑŒ ÑÐ¾Ð±ÑÑ‚Ð²ÐµÐ½Ð½Ñ‹Ð¹ StringBuilder Ñ Ð²Ð¾Ð·Ð¼Ð¾Ð¶Ð½Ð¾ÑÑ‚ÑŒÑŽ ÑƒÐ²ÐµÐ´Ð¾Ð¼Ð»ÐµÐ½Ð¸Ñ
 *   Ð¿Ð¾Ð´Ð¿Ð¸ÑÑ‡Ð¸ÐºÐ¾Ð² (Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÐµÐ¹) Ð¾Ð± Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸ÑÑ… ÑÐ¾ÑÑ‚Ð¾ÑÐ½Ð¸Ñ.
 * ÐŸÐ¾Ð´Ñ…Ð¾Ð´:
 * - Ð”ÐµÐ»ÐµÐ³Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ Ð²ÑÐµÑ… Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¹ ÑÑ‚Ð°Ð½Ð´Ð°Ñ€Ñ‚Ð½Ð¾Ð¼Ñƒ StringBuilder;
 * - Ð’ÑÑ‚Ñ€Ð¾ÐµÐ½Ð½Ñ‹Ð¹ Ð¼ÐµÑ…Ð°Ð½Ð¸Ð·Ð¼ Ð¿Ð¾Ð´Ð¿Ð¸ÑÐºÐ¸/ÑƒÐ²ÐµÐ´Ð¾Ð¼Ð»ÐµÐ½Ð¸Ñ Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÐµÐ¹.
 * Ð”ÐµÐ¼Ð¾Ð½ÑÑ‚Ñ€Ð°Ñ†Ð¸Ñ:
 * - Ð’ Ð¼ÐµÑ‚Ð¾Ð´Ðµ main Ð¿Ð¾ÐºÐ°Ð·Ð°Ð½Ð° Ð¿Ð¾Ð´Ð¿Ð¸ÑÐºÐ° Ð´Ð²ÑƒÑ… Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÐµÐ¹, ÑÐµÑ€Ð¸Ñ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¹,
 *   Ð¾Ñ‚Ð¿Ð¸ÑÐºÐ° Ð¾Ð´Ð½Ð¾Ð³Ð¾ Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»Ñ Ð¸ Ð¿Ð¾Ð²Ñ‚Ð¾Ñ€Ð½Ð¾Ðµ Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ðµ.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// ==========================
//          Ð”ÐµÐ¼Ð¾Ð½ÑÑ‚Ñ€Ð°Ñ†Ð¸Ñ
// ==========================
public class variant2 {
    public static void main(String[] args) {
    p    // Обёртка для защиты от некорректного ввода и ошибок логики     p    try {
        ObservableStringBuilder osb = new ObservableStringBuilder();

        // Ð”Ð¾Ð±Ð°Ð²Ð¸Ð¼ Ð¿Ð°Ñ€Ñƒ Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÐµÐ¹
        osb.addListener(event -> System.out.println(
                "[ÐÐ°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÑŒ#1] " + event.operation() +
                " | Ð±Ñ‹Ð»Ð¾='" + event.before() + "'" +
                " -> ÑÑ‚Ð°Ð»Ð¾='" + event.after() + "'"));

        osb.addListener(event -> System.out.println(
                "[ÐÐ°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÑŒ#2] Ð´Ð»Ð¸Ð½Ð°: " + event.before().length() +
                " -> " + event.after().length()));

        // Ð”ÐµÐ¼Ð¾Ð½ÑÑ‚Ñ€Ð°Ñ†Ð¸Ñ: Ð²Ñ‹Ð¿Ð¾Ð»Ð½Ð¸Ð¼ Ñ€Ð°Ð·Ð»Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¾Ð¿ÐµÑ€Ð°Ñ†Ð¸Ð¸ Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ
        osb.append("Hello").append(',').append(' ').append("world!");
        osb.insert(5, " brave");
        osb.replace(0, 5, "Hi");
        osb.delete(osb.length() - 1, osb.length());
        osb.setCharAt(0, 'h');
        osb.reverse();
        osb.setLength(osb.length()); // Ð±ÐµÐ· Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ñ ÑÐ¾Ð´ÐµÑ€Ð¶Ð¸Ð¼Ð¾Ð³Ð¾ â€” ÑƒÐ²ÐµÐ´Ð¾Ð¼Ð»ÐµÐ½Ð¸Ñ Ð½Ðµ Ð±ÑƒÐ´ÐµÑ‚

        System.out.println("Ð˜Ñ‚Ð¾Ð³Ð¾Ð²Ð¾Ðµ ÑÐ¾Ð´ÐµÑ€Ð¶Ð¸Ð¼Ð¾Ðµ: " + osb);

        // ÐžÑ‚Ð¿Ð¸ÑˆÐµÐ¼ Ð¾Ð´Ð½Ð¾Ð³Ð¾ Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»Ñ Ð¸ ÑÐ´ÐµÐ»Ð°ÐµÐ¼ ÐµÑ‰Ñ‘ Ð¾Ð´Ð½Ð¾ Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ðµ
        List<StringBuilderListener> listeners = new ArrayList<>(osb.listeners());
        if (!listeners.isEmpty()) {
            osb.removeListener(listeners.get(0));
        }
        osb.append("!");
        System.out.println("ÐŸÐ¾ÑÐ»Ðµ Ð¾Ñ‚Ð¿Ð¸ÑÐºÐ¸: " + osb);
    p    } catch (Exception e) {     p        System.out.println("Произошла ошибка ввода/логики: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // Продолжение работы: завершаем безопасно без падения     p    }
    }
}

// ==========================
//        API Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»Ñ
// ==========================
interface StringBuilderListener {
    void onChange(StringBuilderChange event);
}

enum Operation {
    APPEND, INSERT, DELETE, DELETE_CHAR_AT, REPLACE, SET_CHAR_AT, SET_LENGTH, REVERSE, CLEAR, OTHER
}

final class StringBuilderChange {
    private final ObservableStringBuilder source;
    private final Operation operation;
    private final String before;
    private final String after;

    StringBuilderChange(ObservableStringBuilder source, Operation operation, String before, String after) {
        this.source = Objects.requireNonNull(source);
        this.operation = Objects.requireNonNull(operation);
        this.before = before;
        this.after = after;
    }

    public ObservableStringBuilder source() { return source; }
    public Operation operation() { return operation; }
    public String before() { return before; }
    public String after() { return after; }
}

// ==========================
//  ÐÐ°Ð±Ð»ÑŽÐ´Ð°ÐµÐ¼Ñ‹Ð¹ StringBuilder
//    (Ð´ÐµÐ»ÐµÐ³Ð¸Ñ€Ð¾Ð²Ð°Ð½Ð¸Ðµ + Ð½Ð°Ð±Ð»ÑŽÐ´Ð°Ñ‚ÐµÐ»ÑŒ)
// ==========================
final class ObservableStringBuilder implements CharSequence, Appendable {
    private final StringBuilder delegate;
    private final List<StringBuilderListener> listeners = new ArrayList<>();

    public ObservableStringBuilder() { this.delegate = new StringBuilder(); }
    public ObservableStringBuilder(int capacity) { this.delegate = new StringBuilder(capacity); }
    public ObservableStringBuilder(String str) { this.delegate = new StringBuilder(str); }
    public ObservableStringBuilder(CharSequence seq) { this.delegate = new StringBuilder(seq); }

    // Ð£Ð¿Ñ€Ð°Ð²Ð»ÐµÐ½Ð¸Ðµ Ð¿Ð¾Ð´Ð¿Ð¸ÑÑ‡Ð¸ÐºÐ°Ð¼Ð¸
    public void addListener(StringBuilderListener listener) {
        if (listener != null) listeners.add(listener);
    }

    public void removeListener(StringBuilderListener listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public List<StringBuilderListener> listeners() {
        return Collections.unmodifiableList(listeners);
    }

    private void notifyIfChanged(Operation op, String before) {
        String after = delegate.toString();
        if (!Objects.equals(before, after)) {
            // Ð¡Ð½Ð¸Ð¼Ð¾Ðº ÑÐ¿Ð¸ÑÐºÐ°, Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¸Ð·Ð±ÐµÐ¶Ð°Ñ‚ÑŒ ConcurrentModificationException Ð¿Ñ€Ð¸ Ð¸Ð·Ð¼ÐµÐ½ÐµÐ½Ð¸Ð¸ Ð¿Ð¾Ð´Ð¿Ð¸ÑÑ‡Ð¸ÐºÐ¾Ð² Ð²Ð¾ Ð²Ñ€ÐµÐ¼Ñ ÑƒÐ²ÐµÐ´Ð¾Ð¼Ð»ÐµÐ½Ð¸Ñ
            List<StringBuilderListener> snapshot = new ArrayList<>(listeners);
            StringBuilderChange evt = new StringBuilderChange(this, op, before, after);
            for (StringBuilderListener l : snapshot) l.onChange(evt);
        }
    }

    // ÐœÐµÑ‚Ð¾Ð´Ñ‹ Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð´Ð»Ñ Ñ‡Ñ‚ÐµÐ½Ð¸Ñ (Ð±ÐµÐ· ÑƒÐ²ÐµÐ´Ð¾Ð¼Ð»ÐµÐ½Ð¸Ð¹)
    @Override public int length() { return delegate.length(); }
    public int capacity() { return delegate.capacity(); }
    @Override public char charAt(int index) { return delegate.charAt(index); }
    @Override public CharSequence subSequence(int start, int end) { return delegate.subSequence(start, end); }
    public String substring(int start) { return delegate.substring(start); }
    public String substring(int start, int end) { return delegate.substring(start, end); }
    public int indexOf(String str) { return delegate.indexOf(str); }
    public int indexOf(String str, int fromIndex) { return delegate.indexOf(str, fromIndex); }
    public int lastIndexOf(String str) { return delegate.lastIndexOf(str); }
    public int lastIndexOf(String str, int fromIndex) { return delegate.lastIndexOf(str, fromIndex); }
    public void getChars(int srcBegin, int srcEnd, char[] dst, int dstBegin) { delegate.getChars(srcBegin, srcEnd, dst, dstBegin); }

    // ÐœÑƒÑ‚Ð°Ñ†Ð¸Ð¸ ÑƒÐ´Ð¾Ð±ÑÑ‚Ð²Ð°
    public ObservableStringBuilder clear() {
        String before = delegate.toString();
        delegate.setLength(0);
        notifyIfChanged(Operation.CLEAR, before);
        return this;
    }

    public void ensureCapacity(int minimumCapacity) { delegate.ensureCapacity(minimumCapacity); }
    public void trimToSize() { delegate.trimToSize(); }

    public void setLength(int newLength) {
        String before = delegate.toString();
        delegate.setLength(newLength);
        notifyIfChanged(Operation.SET_LENGTH, before);
    }

    public void setCharAt(int index, char ch) {
        String before = delegate.toString();
        delegate.setCharAt(index, ch);
        notifyIfChanged(Operation.SET_CHAR_AT, before);
    }

    public ObservableStringBuilder replace(int start, int end, String str) {
        String before = delegate.toString();
        delegate.replace(start, end, str);
        notifyIfChanged(Operation.REPLACE, before);
        return this;
    }

    public ObservableStringBuilder reverse() {
        String before = delegate.toString();
        delegate.reverse();
        notifyIfChanged(Operation.REVERSE, before);
        return this;
    }

    public ObservableStringBuilder delete(int start, int end) {
        String before = delegate.toString();
        delegate.delete(start, end);
        notifyIfChanged(Operation.DELETE, before);
        return this;
    }

    public ObservableStringBuilder deleteCharAt(int index) {
        String before = delegate.toString();
        delegate.deleteCharAt(index);
        notifyIfChanged(Operation.DELETE_CHAR_AT, before);
        return this;
    }

    // Appendable + Ñ€Ð°Ð·Ð»Ð¸Ñ‡Ð½Ñ‹Ðµ Ð¿ÐµÑ€ÐµÐ³Ñ€ÑƒÐ·ÐºÐ¸
    @Override
    public ObservableStringBuilder append(CharSequence csq) {
        String before = delegate.toString();
        delegate.append(csq);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    @Override
    public ObservableStringBuilder append(CharSequence csq, int start, int end) {
        String before = delegate.toString();
        delegate.append(csq, start, end);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    @Override
    public ObservableStringBuilder append(char c) {
        String before = delegate.toString();
        delegate.append(c);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(String str) {
        String before = delegate.toString();
        delegate.append(str);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(StringBuffer sb) {
        String before = delegate.toString();
        delegate.append(sb);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(Object obj) {
        String before = delegate.toString();
        delegate.append(obj);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(boolean b) {
        String before = delegate.toString();
        delegate.append(b);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(int i) {
        String before = delegate.toString();
        delegate.append(i);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(long l) {
        String before = delegate.toString();
        delegate.append(l);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(float f) {
        String before = delegate.toString();
        delegate.append(f);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(double d) {
        String before = delegate.toString();
        delegate.append(d);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(char[] str) {
        String before = delegate.toString();
        delegate.append(str);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder append(char[] str, int offset, int len) {
        String before = delegate.toString();
        delegate.append(str, offset, len);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    public ObservableStringBuilder appendCodePoint(int codePoint) {
        String before = delegate.toString();
        delegate.appendCodePoint(codePoint);
        notifyIfChanged(Operation.APPEND, before);
        return this;
    }

    // ÐŸÐµÑ€ÐµÐ³Ñ€ÑƒÐ·ÐºÐ¸ Ð´Ð»Ñ insert
    public ObservableStringBuilder insert(int index, char c) {
        String before = delegate.toString();
        delegate.insert(index, c);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, String str) {
        String before = delegate.toString();
        delegate.insert(index, str);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, CharSequence s) {
        String before = delegate.toString();
        delegate.insert(index, s);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int dstOffset, CharSequence s, int start, int end) {
        String before = delegate.toString();
        delegate.insert(dstOffset, s, start, end);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, boolean b) {
        String before = delegate.toString();
        delegate.insert(index, b);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, int i) {
        String before = delegate.toString();
        delegate.insert(index, i);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, long l) {
        String before = delegate.toString();
        delegate.insert(index, l);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, float f) {
        String before = delegate.toString();
        delegate.insert(index, f);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, double d) {
        String before = delegate.toString();
        delegate.insert(index, d);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, Object obj) {
        String before = delegate.toString();
        delegate.insert(index, obj);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, char[] str) {
        String before = delegate.toString();
        delegate.insert(index, str);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    public ObservableStringBuilder insert(int index, char[] str, int offset, int len) {
        String before = delegate.toString();
        delegate.insert(index, str, offset, len);
        notifyIfChanged(Operation.INSERT, before);
        return this;
    }

    // Ð‘Ð°Ð·Ð¾Ð²Ñ‹Ðµ Ð¿ÐµÑ€ÐµÐ¾Ð¿Ñ€ÐµÐ´ÐµÐ»ÐµÐ½Ð¸Ñ
    @Override public String toString() { return delegate.toString(); }
}

