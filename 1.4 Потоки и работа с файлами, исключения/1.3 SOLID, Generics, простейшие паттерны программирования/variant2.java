?/**
 * Вариант 2 — паттерн «Наблюдатель».
 *
 * Цель:
 * - Реализовать собственный StringBuilder с возможностью уведомления
 *   подписчиков (наблюдателей) об изменениях состояния.
 * Подход:
 * - Делегирование всех операций стандартному StringBuilder;
 * - Встроенный механизм подписки/уведомления наблюдателей.
 * Демонстрация:
 * - В методе main показана подписка двух наблюдателей, серия операций,
 *   отписка одного наблюдателя и повторное изменение.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// ==========================
//          Демонстрация
// ==========================
public class variant2 {
    public static void main(String[] args) {
    p    // ??????? ??? ?????? ?? ????????????? ????? ? ?????? ??????     p    try {
        ObservableStringBuilder osb = new ObservableStringBuilder();

        // Добавим пару наблюдателей
        osb.addListener(event -> System.out.println(
                "[Наблюдатель#1] " + event.operation() +
                " | было='" + event.before() + "'" +
                " -> стало='" + event.after() + "'"));

        osb.addListener(event -> System.out.println(
                "[Наблюдатель#2] длина: " + event.before().length() +
                " -> " + event.after().length()));

        // Демонстрация: выполним различные операции изменения
        osb.append("Hello").append(',').append(' ').append("world!");
        osb.insert(5, " brave");
        osb.replace(0, 5, "Hi");
        osb.delete(osb.length() - 1, osb.length());
        osb.setCharAt(0, 'h');
        osb.reverse();
        osb.setLength(osb.length()); // без изменения содержимого — уведомления не будет

        System.out.println("Итоговое содержимое: " + osb);

        // Отпишем одного наблюдателя и сделаем ещё одно изменение
        List<StringBuilderListener> listeners = new ArrayList<>(osb.listeners());
        if (!listeners.isEmpty()) {
            osb.removeListener(listeners.get(0));
        }
        osb.append("!");
        System.out.println("После отписки: " + osb);
    p    } catch (Exception e) {     p        System.out.println("????????? ?????? ?????/??????: " + (e.getMessage() == null ? e.toString() : e.getMessage()));     p        // ??????????? ??????: ????????? ????????? ??? ???????     p    }
    }
}

// ==========================
//        API наблюдателя
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
//  Наблюдаемый StringBuilder
//    (делегирование + наблюдатель)
// ==========================
final class ObservableStringBuilder implements CharSequence, Appendable {
    private final StringBuilder delegate;
    private final List<StringBuilderListener> listeners = new ArrayList<>();

    public ObservableStringBuilder() { this.delegate = new StringBuilder(); }
    public ObservableStringBuilder(int capacity) { this.delegate = new StringBuilder(capacity); }
    public ObservableStringBuilder(String str) { this.delegate = new StringBuilder(str); }
    public ObservableStringBuilder(CharSequence seq) { this.delegate = new StringBuilder(seq); }

    // Управление подписчиками
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
            // Снимок списка, чтобы избежать ConcurrentModificationException при изменении подписчиков во время уведомления
            List<StringBuilderListener> snapshot = new ArrayList<>(listeners);
            StringBuilderChange evt = new StringBuilderChange(this, op, before, after);
            for (StringBuilderListener l : snapshot) l.onChange(evt);
        }
    }

    // Методы только для чтения (без уведомлений)
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

    // Мутации удобства
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

    // Appendable + различные перегрузки
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

    // Перегрузки для insert
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

    // Базовые переопределения
    @Override public String toString() { return delegate.toString(); }
}

