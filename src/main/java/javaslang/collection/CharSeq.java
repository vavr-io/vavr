/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.Iterable;
import java.nio.charset.Charset;
import java.util.*;
import java.util.HashSet;
import java.util.function.*;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collector;

/**
 * TODO javadoc
 */
public final class CharSeq implements CharSequence, IndexedSeq<Character>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final CharSeq EMPTY = new CharSeq("");

    private final java.lang.String back;

    private CharSeq(java.lang.String javaString) {
        this.back = javaString;
    }

    public static CharSeq empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link CharSeq}s.
     *
     * @return A {@code CharSeq} Collector.
     */
    public static Collector<Character, ArrayList<Character>, CharSeq> collector() {
        final Supplier<ArrayList<Character>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<Character>, Character> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<Character>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<Character>, CharSeq> finisher = CharSeq::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a singleton {@code CharSeq}, i.e. a {@code CharSeq} of one element.
     *
     * @param element An element.
     * @return A new {@code CharSeq} instance containing the given element
     */
    public static CharSeq of(Character element) {
        return new CharSeq(new java.lang.String(new char[] { element }));
    }

    /**
     * Creates a String of the given elements.
     *
     * @param elements Zero or more elements.
     * @return A string containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static CharSeq of(Character... elements) {
        Objects.requireNonNull(elements, "elements is null");
        final char[] chrs = new char[elements.length];
        for (int i = 0; i < elements.length; i++) {
            chrs[i] = elements[i];
        }
        return new CharSeq(new java.lang.String(chrs));
    }

    /**
     * Creates a String of {@code CharSequence}.
     *
     * @param sequence {@code CharSequence} instance.
     * @return A new {@code javaslang.String}
     */
    public static CharSeq of(CharSequence sequence) {
        Objects.requireNonNull(sequence, "sequence is null");
        return sequence.length() == 0 ? empty() : new CharSeq(sequence.toString());
    }

    /**
     * Creates a String of the given elements.
     *
     * The resulting string has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param elements An Iterable of elements.
     * @return A string containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static CharSeq ofAll(java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final StringBuilder sb = new StringBuilder();
        for (Character character : elements) {
            sb.append(character);
        }
        return sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    /**
     * Creates a CharSeq based on the elements of a char array.
     *
     * @param array a char array
     * @return A new List of Character values
     */
    static CharSeq ofAll(char[] array) {
        Objects.requireNonNull(array, "array is null");
        return new CharSeq(String.valueOf(array));
    }

    //
    //
    // IndexedSeq
    //
    //

    @Override
    public CharSeq append(Character element) {
        return of(back + element);
    }

    @Override
    public CharSeq appendAll(java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final StringBuilder sb = new StringBuilder(back);
        for (char element : elements) {
            sb.append(element);
        }
        return of(sb.toString());
    }

    @Override
    public CharSeq clear() {
        return EMPTY;
    }

    @Override
    public Vector<Tuple2<Character, Character>> crossProduct() {
        return crossProduct(this);
    }

    @Override
    public <U> Vector<Tuple2<Character, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        Objects.requireNonNull(that, "that is null");
        final Vector<U> other = Vector.ofAll(that);
        return flatMap(a -> other.map(b -> Tuple.of(a, b)));
    }

    @Override
    public Vector<CharSeq> combinations() {
        return Vector.rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    public Vector<CharSeq> combinations(int k) {
        class Recursion {
            Vector<CharSeq> combinations(CharSeq elements, int k) {
                return (k == 0)
                        ? Vector.of(CharSeq.empty())
                        : elements.zipWithIndex().flatMap(t -> combinations(elements.drop(t._2 + 1), (k - 1))
                        .map((CharSeq c) -> c.prepend(t._1)));
            }
        }
        return new Recursion().combinations(this, Math.max(k, 0));
    }

    @Override
    public CharSeq distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    public CharSeq distinctBy(Comparator<? super Character> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<Character> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    public <U> CharSeq distinctBy(Function<? super Character, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    public CharSeq drop(int n) {
        if (n >= length()) {
            return EMPTY;
        }
        if (n <= 0) {
            return this;
        } else {
            return of(back.substring(n));
        }
    }

    @Override
    public CharSeq dropRight(int n) {
        if (n >= length()) {
            return EMPTY;
        }
        if (n <= 0) {
            return this;
        } else {
            return of(back.substring(0, length() - n));
        }
    }

    @Override
    public CharSeq dropWhile(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        int index = 0;
        while (index < length() && predicate.test(charAt(index))) {
            index++;
        }
        return index < length() ? (index == 0 ? this : of(back.substring(index))) : empty();
    }

    @Override
    public CharSeq filter(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < back.length(); i++) {
            final char ch = back.charAt(i);
            if (predicate.test(ch)) {
                sb.append(ch);
            }
        }
        return sb.length() == 0 ? EMPTY : sb.length() == length() ? this : of(sb.toString());
    }

    @Override
    public <U> Vector<U> flatMap(Function<? super Character, ? extends java.lang.Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return Vector.empty();
        } else {
            Vector<U> result = Vector.empty();
            for (int i = 0; i < length(); i++) {
                for (U u : mapper.apply(get(i))) {
                    result = result.append(u);
                }
            }
            return result;
        }
    }

    public CharSeq flatMapChars(CharFunction<? extends CharSequence> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return this;
        } else {
            final StringBuilder builder = new StringBuilder();
            back.chars().forEach(c -> builder.append(mapper.apply((char) c)));
            return new CharSeq(builder.toString());
        }
    }

    @Override
    public <U> Vector<U> flatMapVal(Function<? super Character, ? extends Value<? extends U>> mapper) {
        return flatMap(mapper);
    }

    @Override
    public Vector<Object> flatten() {
        return Vector.ofAll(iterator());
    }

    @Override
    public <C> Map<C, CharSeq> groupBy(Function<? super Character, ? extends C> classifier) {
        Objects.requireNonNull(classifier, "classifier is null");
        return foldLeft(HashMap.empty(), (map, t) -> {
            final C key = classifier.apply(t);
            final CharSeq values = map.get(key).map(ts -> ts.append(t)).orElse(CharSeq.of(t));
            return map.put(key, values);
        });
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public CharSeq init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty string");
        } else {
            return of(back.substring(0, length() - 1));
        }
    }

    @Override
    public Option<CharSeq> initOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(init());
        }
    }

    @Override
    public CharSeq insert(int index, Character element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e) on String of length " + length());
        }
        return of(new StringBuilder(back).insert(index, element).toString());
    }

    @Override
    public CharSeq insertAll(int index, java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        if (index > length()) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on String of length " + length());
        }
        final java.lang.String javaString = back;
        final StringBuilder sb = new StringBuilder(javaString.substring(0, index));
        for (Character element : elements) {
            sb.append(element);
        }
        sb.append(javaString.substring(index));
        return of(sb.toString());
    }

    @Override
    public Iterator<Character> iterator() {
        return new Iterator<Character>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < back.length();
            }

            @Override
            public Character next() {
                if (index >= back.length()) {
                    throw new NoSuchElementException();
                }
                return back.charAt(index++);
            }
        };
    }

    @Override
    public CharSeq intersperse(Character element) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            if (i > 0) {
                sb.append(element);
            }
            sb.append(get(i));
        }
        return sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    @Override
    public <U> Vector<U> map(Function<? super Character, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        Vector<U> result = Vector.empty();
        for (int i = 0; i < length(); i++) {
            result = result.append(mapper.apply(get(i)));
        }
        return result;
    }

    @Override
    public CharSeq padTo(int length, Character element) {
        if(length <= back.length()) {
            return this;
        }
        final StringBuilder sb = new StringBuilder(back);
        final int limit = length - back.length();
        for (int i = 0; i < limit; i++) {
            sb.append(element);
        }
        return new CharSeq(sb.toString());
    }

    public CharSeq mapChars(CharUnaryOperator mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isEmpty()) {
            return this;
        } else {
            final char[] chars = back.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                chars[i] = mapper.apply(chars[i]);
            }
            return CharSeq.ofAll(chars);
        }
    }

    @Override
    public Tuple2<CharSeq, CharSeq> partition(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(EMPTY, EMPTY);
        }
        final StringBuilder left = new StringBuilder();
        final StringBuilder right = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            Character t = get(i);
            (predicate.test(t) ? left : right).append(t);
        }
        if (left.length() == 0) {
            return Tuple.of(EMPTY, of(right.toString()));
        } else if (right.length() == 0) {
            return Tuple.of(of(left.toString()), EMPTY);
        } else {
            return Tuple.of(of(left.toString()), of(right.toString()));
        }
    }

    @Override
    public CharSeq peek(Consumer<? super Character> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(back.charAt(0));
        }
        return this;
    }

    @Override
    public Vector<CharSeq> permutations() {
        if (isEmpty()) {
            return Vector.empty();
        } else {
            if (length() == 1) {
                return Vector.of(this);
            } else {
                Vector<CharSeq> result = Vector.empty();
                for (Character t : distinct()) {
                    for (CharSeq ts : remove(t).permutations()) {
                        result = result.append(ts);
                    }
                }
                return result;
            }
        }
    }

    @Override
    public CharSeq prepend(Character element) {
        return of(element + back);
    }

    @Override
    public CharSeq prependAll(java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final StringBuilder sb = new StringBuilder();
        for (Character element : elements) {
            sb.append(element);
        }
        sb.append(back);
        return sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    @Override
    public CharSeq remove(Character element) {
        final StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            char c = get(i);
            if (!found && c == element) {
                found = true;
            } else {
                sb.append(c);
            }
        }
        return sb.length() == 0 ? EMPTY : sb.length() == length() ? this : of(sb.toString());
    }

    @Override
    public CharSeq removeFirst(Predicate<Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < back.length(); i++) {
            final char ch = back.charAt(i);
            if (predicate.test(ch)) {
                if (found) {
                    sb.append(ch);
                }
                found = true;
            } else {
                sb.append(ch);
            }
        }
        return found ? (sb.length() == 0 ? EMPTY : of(sb.toString())) : this;
    }

    @Override
    public CharSeq removeLast(Predicate<Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        for (int i = length() - 1; i >= 0; i--) {
            if (predicate.test(back.charAt(i))) {
                return removeAt(i);
            }
        }
        return this;
    }

    @Override
    public CharSeq removeAt(int indx) {
        final java.lang.String removed = back.substring(0, indx) + back.substring(indx + 1);
        return removed.isEmpty() ? EMPTY : of(removed);
    }

    @Override
    public CharSeq removeAll(Character element) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (c != element) {
                sb.append(c);
            }
        }
        return sb.length() == 0 ? EMPTY : sb.length() == length() ? this : of(sb.toString());
    }

    @Override
    public CharSeq removeAll(java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final java.util.Set<Character> distinct = new HashSet<>();
        for (Character element : elements) {
            distinct.add(element);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (!distinct.contains(c)) {
                sb.append(c);
            }
        }
        return sb.length() == 0 ? EMPTY : sb.length() == length() ? this : of(sb.toString());
    }

    @Override
    public CharSeq replace(Character currentElement, Character newElement) {
        final StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (c == currentElement && !found) {
                sb.append(newElement);
                found = true;
            } else {
                sb.append(c);
            }
        }
        return found ? (sb.length() == 0 ? EMPTY : of(sb.toString())) : this;
    }

    @Override
    public CharSeq replaceAll(Character currentElement, Character newElement) {
        final StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (c == currentElement) {
                sb.append(newElement);
                found = true;
            } else {
                sb.append(c);
            }
        }
        return found ? (sb.length() == 0 ? EMPTY : of(sb.toString())) : this;
    }

    @Override
    public CharSeq replaceAll(UnaryOperator<Character> operator) {
        Objects.requireNonNull(operator, "operator is null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            sb.append(operator.apply(back.charAt(i)));
        }
        return sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    @Override
    public CharSeq retainAll(java.lang.Iterable<? extends Character> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final java.util.Set<Character> keeped = new HashSet<>();
        for (Character element : elements) {
            keeped.add(element);
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (keeped.contains(c)) {
                sb.append(c);
            }
        }
        return sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    @Override
    public CharSeq reverse() {
        return of(new StringBuilder(back).reverse().toString());
    }

    @Override
    public CharSeq set(int index, Character element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        if (index >= length()) {
            throw new IndexOutOfBoundsException("set(" + index + ")");
        }
        return of(back.substring(0, index) + element + back.substring(index + 1));
    }

    @Override
    public CharSeq slice(int beginIndex) {
        if (beginIndex < 0) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        if (beginIndex > length()) {
            throw new IndexOutOfBoundsException("slice(" + beginIndex + ")");
        }
        return of(back.substring(beginIndex));
    }

    @Override
    public CharSeq slice(int beginIndex, int endIndex) {
        if (beginIndex < 0 || beginIndex > endIndex || endIndex > length()) {
            throw new IndexOutOfBoundsException(
                    java.lang.String.format("slice(%s, %s) on List of length %s", beginIndex, endIndex, length()));
        }
        if (beginIndex == endIndex) {
            return EMPTY;
        }
        return of(back.substring(beginIndex, endIndex));
    }

    @Override
    public CharSeq sort() {
        return isEmpty() ? this : toJavaStream().sorted().collect(CharSeq.collector());
    }

    @Override
    public CharSeq sort(Comparator<? super Character> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(CharSeq.collector());
    }

    @Override
    public Tuple2<CharSeq, CharSeq> span(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            final char c = back.charAt(i);
            if (predicate.test(c)) {
                sb.append(c);
            } else {
                break;
            }
        }
        if (sb.length() == 0) {
            return Tuple.of(EMPTY, this);
        } else if (sb.length() == length()) {
            return Tuple.of(this, EMPTY);
        } else {
            return Tuple.of(of(sb.toString()), of(back.substring(sb.length())));
        }
    }

    @Override
    public Spliterator<Character> spliterator() {
        return Spliterators.spliterator(iterator(), length(), Spliterator.ORDERED | Spliterator.IMMUTABLE);
    }

    @Override
    public CharSeq tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty string");
        } else {
            return of(back.substring(1));
        }
    }

    @Override
    public Option<CharSeq> tailOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(of(back.substring(1)));
        }
    }

    @Override
    public CharSeq take(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return EMPTY;
        } else {
            return of(back.substring(0, n));
        }
    }

    @Override
    public CharSeq takeRight(int n) {
        if (n >= length()) {
            return this;
        }
        if (n <= 0) {
            return EMPTY;
        } else {
            return of(back.substring(length() - n));
        }
    }

    @Override
    public CharSeq takeWhile(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            char c = back.charAt(i);
            if (!predicate.test(c)) {
                break;
            }
            sb.append(c);
        }
        return sb.length() == length() ? this : sb.length() == 0 ? EMPTY : of(sb.toString());
    }

    @Override
    public <U> Vector<U> unit(java.lang.Iterable<? extends U> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        return Vector.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(Function<? super Character, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        Vector<T1> xs = Vector.empty();
        Vector<T2> ys = Vector.empty();
        for (int i = 0; i < length(); i++) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(back.charAt(i));
            xs = xs.append(t._1);
            ys = ys.append(t._2);
        }
        return Tuple.of(xs.length() == 0 ? Vector.<T1> empty() : xs, ys.length() == 0 ? Vector.<T2> empty() : ys);
    }

    @Override
    public <U> Vector<Tuple2<Character, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        Vector<Tuple2<Character, U>> result = Vector.empty();
        Iterator<Character> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() && list2.hasNext()) {
            result = result.append(Tuple.of(list1.next(), list2.next()));
        }
        return result;
    }

    @Override
    public <U> Vector<Tuple2<Character, U>> zipAll(java.lang.Iterable<U> that, Character thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        Vector<Tuple2<Character, U>> result = Vector.empty();
        Iterator<Character> list1 = iterator();
        java.util.Iterator<U> list2 = that.iterator();
        while (list1.hasNext() || list2.hasNext()) {
            final Character elem1 = list1.hasNext() ? list1.next() : thisElem;
            final U elem2 = list2.hasNext() ? list2.next() : thatElem;
            result = result.append(Tuple.of(elem1, elem2));
        }
        return result;
    }

    @Override
    public Vector<Tuple2<Character, Integer>> zipWithIndex() {
        Vector<Tuple2<Character, Integer>> result = Vector.empty();
        for (int i = 0; i < length(); i++) {
            result = result.append(Tuple.of(get(i), i));
        }
        return result;
    }

    @Override
    public Character get(int index) {
        return back.charAt(index);
    }

    @Override
    public int indexOf(Character element, int from) {
        return back.indexOf(element, from);
    }

    @Override
    public int lastIndexOf(Character element, int end) {
        return back.lastIndexOf(element, end);
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAt(int n) {
        if (n <= 0) {
            return Tuple.of(EMPTY, this);
        } else if (n >= length()) {
            return Tuple.of(this, EMPTY);
        } else {
            return Tuple.of(of(back.substring(0, n)), of(back.substring(n)));
        }
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAt(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(EMPTY, EMPTY);
        }
        final StringBuilder left = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            Character t = get(i);
            if (!predicate.test(t)) {
                left.append(t);
            } else {
                break;
            }
        }
        if (left.length() == 0) {
            return Tuple.of(EMPTY, this);
        } else if (left.length() == length()) {
            return Tuple.of(this, EMPTY);
        } else {
            return Tuple.of(of(left.toString()), of(back.substring(left.length())));
        }
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAtInclusive(Predicate<? super Character> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return Tuple.of(EMPTY, EMPTY);
        }
        final StringBuilder left = new StringBuilder();
        for (int i = 0; i < length(); i++) {
            Character t = get(i);
            left.append(t);
            if (predicate.test(t)) {
                break;
            }
        }
        if (left.length() == 0) {
            return Tuple.of(EMPTY, this);
        } else if (left.length() == length()) {
            return Tuple.of(this, EMPTY);
        } else {
            return Tuple.of(of(left.toString()), of(back.substring(left.length())));
        }
    }

    @Override
    public boolean startsWith(Iterable<? extends Character> that, int offset) {
        return startsWith(CharSeq.ofAll(that), offset);
    }

    @Override
    public Character head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty string");
        } else {
            return back.charAt(0);
        }
    }

    @Override
    public Option<Character> headOption() {
        if (isEmpty()) {
            return None.instance();
        } else {
            return new Some<>(back.charAt(0));
        }
    }

    @Override
    public boolean isEmpty() {
        return back.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof CharSeq) {
            return ((CharSeq) o).back.equals(back);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return back.hashCode();
    }

    //
    //
    // java.lang.CharSequence
    //
    //

    /**
     * Returns the {@code char} value at the
     * specified index. An index ranges from {@code 0} to
     * {@code length() - 1}. The first {@code char} value of the sequence
     * is at index {@code 0}, the next at index {@code 1},
     * and so on, as for array indexing.
     *
     * <p>If the {@code char} value specified by the index is a
     * <a href="Character.html#unicode">surrogate</a>, the surrogate
     * value is returned.
     *
     * @param index the index of the {@code char} value.
     * @return the {@code char} value at the specified index of this string.
     * The first {@code char} value is at index {@code 0}.
     * @throws IndexOutOfBoundsException if the {@code index}
     *                                   argument is negative or not less than the length of this
     *                                   string.
     */
    @Override
    public char charAt(int index) {
        return back.charAt(index);
    }

    /**
     * Returns the length of this string.
     * The length is equal to the number of <a href="Character.html#unicode">Unicode
     * code units</a> in the string.
     *
     * @return the length of the sequence of characters represented by this
     * object.
     */
    @Override
    public int length() {
        return back.length();
    }

    //
    //
    // java.lang.String
    //
    //

    /**
     * Returns the character (Unicode code point) at the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 0} to
     * {@link #length()}{@code  - 1}.
     *
     * <p> If the {@code char} value specified at the given index
     * is in the high-surrogate range, the following index is less
     * than the length of this {@code CharSeq}, and the
     * {@code char} value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the {@code char} value at the given index is returned.
     *
     * @param index the index to the {@code char} values
     * @return the code point value of the character at the
     * {@code index}
     * @throws IndexOutOfBoundsException if the {@code index}
     *                                   argument is negative or not less than the length of this
     *                                   string.
     */
    public int codePointAt(int index) {
        return back.codePointAt(index);
    }

    /**
     * Returns the character (Unicode code point) before the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 1} to {@link
     * CharSequence#length() length}.
     *
     * <p> If the {@code char} value at {@code (index - 1)}
     * is in the low-surrogate range, {@code (index - 2)} is not
     * negative, and the {@code char} value at {@code (index -
     * 2)} is in the high-surrogate range, then the
     * supplementary code point value of the surrogate pair is
     * returned. If the {@code char} value at {@code index -
     * 1} is an unpaired low-surrogate or a high-surrogate, the
     * surrogate value is returned.
     *
     * @param index the index following the code point that should be returned
     * @return the Unicode code point value before the given index.
     * @throws IndexOutOfBoundsException if the {@code index}
     *                                   argument is less than 1 or greater than the length
     *                                   of this string.
     */
    public int codePointBefore(int index) {
        return back.codePointBefore(index);
    }

    /**
     * Returns the number of Unicode code points in the specified text
     * range of this {@code CharSeq}. The text range begins at the
     * specified {@code beginIndex} and extends to the
     * {@code char} at index {@code endIndex - 1}. Thus the
     * length (in {@code char}s) of the text range is
     * {@code endIndex-beginIndex}. Unpaired surrogates within
     * the text range count as one code point each.
     *
     * @param beginIndex the index to the first {@code char} of
     *                   the text range.
     * @param endIndex   the index after the last {@code char} of
     *                   the text range.
     * @return the number of Unicode code points in the specified text
     * range
     * @throws IndexOutOfBoundsException if the
     *                                   {@code beginIndex} is negative, or {@code endIndex}
     *                                   is larger than the length of this {@code CharSeq}, or
     *                                   {@code beginIndex} is larger than {@code endIndex}.
     */
    public int codePointCount(int beginIndex, int endIndex) {
        return back.codePointCount(beginIndex, endIndex);
    }

    /**
     * Returns the index within this {@code CharSeq} that is
     * offset from the given {@code index} by
     * {@code codePointOffset} code points. Unpaired surrogates
     * within the text range given by {@code index} and
     * {@code codePointOffset} count as one code point each.
     *
     * @param index           the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within this {@code CharSeq}
     * @throws IndexOutOfBoundsException if {@code index}
     *                                   is negative or larger then the length of this
     *                                   {@code CharSeq}, or if {@code codePointOffset} is positive
     *                                   and the substring starting with {@code index} has fewer
     *                                   than {@code codePointOffset} code points,
     *                                   or if {@code codePointOffset} is negative and the substring
     *                                   before {@code index} has fewer than the absolute value
     *                                   of {@code codePointOffset} code points.
     */
    public int offsetByCodePoints(int index, int codePointOffset) {
        return back.offsetByCodePoints(index, codePointOffset);
    }

    /**
     * Copies characters from this string into the destination character
     * array.
     * <p>
     * The first character to be copied is at index {@code srcBegin};
     * the last character to be copied is at index {@code srcEnd-1}
     * (thus the total number of characters to be copied is
     * {@code srcEnd-srcBegin}). The characters are copied into the
     * subarray of {@code dst} starting at index {@code dstBegin}
     * and ending at index:
     * <blockquote><pre>
     *     dstbegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     *
     * @param srcBegin index of the first character in the string
     *                 to copy.
     * @param srcEnd   index after the last character in the string
     *                 to copy.
     * @param dst      the destination array.
     * @param dstBegin the start offset in the destination array.
     * @throws IndexOutOfBoundsException If any of the following
     *                                   is true:
     *                                   <ul><li>{@code srcBegin} is negative.
     *                                   <li>{@code srcBegin} is greater than {@code srcEnd}
     *                                   <li>{@code srcEnd} is greater than the length of this
     *                                   string
     *                                   <li>{@code dstBegin} is negative
     *                                   <li>{@code dstBegin+(srcEnd-srcBegin)} is larger than
     *                                   {@code dst.length}</ul>
     */
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {
        back.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the named
     * charset, storing the result into a new byte array.
     *
     * <p> The behavior of this method when this string cannot be encoded in
     * the given charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     *
     * @param charsetName The name of a supported {@linkplain java.nio.charset.Charset
     *                    charset}
     * @return The resultant byte array
     * @throws UnsupportedEncodingException If the named charset is not supported
     */
    public byte[] getBytes(java.lang.String charsetName) throws UnsupportedEncodingException {
        return back.getBytes(charsetName);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the given
     * {@linkplain java.nio.charset.Charset charset}, storing the result into a
     * new byte array.
     *
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement byte array.  The
     * {@link java.nio.charset.CharsetEncoder} class should be used when more
     * control over the encoding process is required.
     *
     * @param charset The {@linkplain java.nio.charset.Charset} to be used to encode
     *                the {@code CharSeq}
     * @return The resultant byte array
     */
    public byte[] getBytes(Charset charset) {
        return back.getBytes(charset);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the
     * platform's default charset, storing the result into a new byte array.
     *
     * <p> The behavior of this method when this string cannot be encoded in
     * the default charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     *
     * @return The resultant byte array
     */
    public byte[] getBytes() {
        return back.getBytes();
    }

    /**
     * Compares this string to the specified {@code StringBuffer}.  The result
     * is {@code true} if and only if this {@code CharSeq} represents the same
     * sequence of characters as the specified {@code StringBuffer}. This method
     * synchronizes on the {@code StringBuffer}.
     *
     * @param sb The {@code StringBuffer} to compare this {@code CharSeq} against
     * @return {@code true} if this {@code CharSeq} represents the same
     * sequence of characters as the specified {@code StringBuffer},
     * {@code false} otherwise
     */
    public boolean contentEquals(StringBuffer sb) {
        return back.contentEquals(sb);
    }

    /**
     * Compares this string to the specified {@code CharSequence}.  The
     * result is {@code true} if and only if this {@code CharSeq} represents the
     * same sequence of char values as the specified sequence. Note that if the
     * {@code CharSequence} is a {@code StringBuffer} then the method
     * synchronizes on it.
     *
     * @param cs The sequence to compare this {@code CharSeq} against
     * @return {@code true} if this {@code CharSeq} represents the same
     * sequence of char values as the specified sequence, {@code
     * false} otherwise
     */
    public boolean contentEquals(CharSequence cs) {
        return back.contentEquals(cs);
    }

    /**
     * Compares this {@code CharSeq} to another {@code CharSeq}, ignoring case
     * considerations.  Two strings are considered equal ignoring case if they
     * are of the same length and corresponding characters in the two strings
     * are equal ignoring case.
     *
     * <p> Two characters {@code c1} and {@code c2} are considered the same
     * ignoring case if at least one of the following is true:
     * <ul>
     * <li> The two characters are the same (as compared by the
     * {@code ==} operator)
     * <li> Applying the method {@link
     * java.lang.Character#toUpperCase(char)} to each character
     * produces the same result
     * <li> Applying the method {@link
     * java.lang.Character#toLowerCase(char)} to each character
     * produces the same result
     * </ul>
     *
     * @param anotherString The {@code CharSeq} to compare this {@code CharSeq} against
     * @return {@code true} if the argument is not {@code null} and it
     * represents an equivalent {@code CharSeq} ignoring case; {@code
     * false} otherwise
     * @see #equals(Object)
     */
    public boolean equalsIgnoreCase(CharSeq anotherString) {
        return back.equalsIgnoreCase(anotherString.back);
    }

    /**
     * Compares two strings lexicographically.
     * The comparison is based on the Unicode value of each character in
     * the strings. The character sequence represented by this
     * {@code CharSeq} object is compared lexicographically to the
     * character sequence represented by the argument string. The result is
     * a negative integer if this {@code CharSeq} object
     * lexicographically precedes the argument string. The result is a
     * positive integer if this {@code CharSeq} object lexicographically
     * follows the argument string. The result is zero if the strings
     * are equal; {@code compareTo} returns {@code 0} exactly when
     * the {@link #equals(Object)} method would return {@code true}.
     * <p>
     * This is the definition of lexicographic ordering. If two strings are
     * different, then either they have different characters at some index
     * that is a valid index for both strings, or their lengths are different,
     * or both. If they have different characters at one or more index
     * positions, let <i>k</i> be the smallest such index; then the string
     * whose character at position <i>k</i> has the smaller value, as
     * determined by using the &lt; operator, lexicographically precedes the
     * other string. In this case, {@code compareTo} returns the
     * difference of the two character values at position {@code k} in
     * the two string -- that is, the value:
     * <blockquote><pre>
     * this.charAt(k)-anotherString.charAt(k)
     * </pre></blockquote>
     * If there is no index position at which they differ, then the shorter
     * string lexicographically precedes the longer string. In this case,
     * {@code compareTo} returns the difference of the lengths of the
     * strings -- that is, the value:
     * <blockquote><pre>
     * this.length()-anotherString.length()
     * </pre></blockquote>
     *
     * @param anotherString the {@code CharSeq} to be compared.
     * @return the value {@code 0} if the argument string is equal to
     * this string; a value less than {@code 0} if this string
     * is lexicographically less than the string argument; and a
     * value greater than {@code 0} if this string is
     * lexicographically greater than the string argument.
     */
    public int compareTo(CharSeq anotherString) {
        return back.compareTo(anotherString.back);
    }

    /**
     * Compares two strings lexicographically, ignoring case
     * differences. This method returns an integer whose sign is that of
     * calling {@code compareTo} with normalized versions of the strings
     * where case differences have been eliminated by calling
     * {@code Character.toLowerCase(Character.toUpperCase(character))} on
     * each character.
     * <p>
     * Note that this method does <em>not</em> take locale into account,
     * and will result in an unsatisfactory ordering for certain locales.
     * The java.text package provides <em>collators</em> to allow
     * locale-sensitive ordering.
     *
     * @param str the {@code CharSeq} to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * specified String is greater than, equal to, or less
     * than this String, ignoring case considerations.
     */
    public int compareToIgnoreCase(CharSeq str) {
        return back.compareToIgnoreCase(str.back);
    }

    /**
     * Tests if two string regions are equal.
     * <p>
     * A substring of this {@code CharSeq} object is compared to a substring
     * of the argument other. The result is true if these substrings
     * represent identical character sequences. The substring of this
     * {@code CharSeq} object to be compared begins at index {@code toffset}
     * and has length {@code len}. The substring of other to be compared
     * begins at index {@code ooffset} and has length {@code len}. The
     * result is {@code false} if and only if at least one of the following
     * is true:
     * <ul><li>{@code toffset} is negative.
     * <li>{@code ooffset} is negative.
     * <li>{@code toffset+len} is greater than the length of this
     * {@code CharSeq} object.
     * <li>{@code ooffset+len} is greater than the length of the other
     * argument.
     * <li>There is some nonnegative integer <i>k</i> less than {@code len}
     * such that:
     * {@code this.charAt(toffset + }<i>k</i>{@code ) != other.charAt(ooffset + }
     * <i>k</i>{@code )}
     * </ul>
     *
     * @param toffset the starting offset of the subregion in this string.
     * @param other   the string argument.
     * @param ooffset the starting offset of the subregion in the string
     *                argument.
     * @param len     the number of characters to compare.
     * @return {@code true} if the specified subregion of this string
     * exactly matches the specified subregion of the string argument;
     * {@code false} otherwise.
     */
    public boolean regionMatches(int toffset, CharSeq other, int ooffset, int len) {
        return back.regionMatches(toffset, other.back, ooffset, len);
    }

    /**
     * Tests if two string regions are equal.
     * <p>
     * A substring of this {@code CharSeq} object is compared to a substring
     * of the argument {@code other}. The result is {@code true} if these
     * substrings represent character sequences that are the same, ignoring
     * case if and only if {@code ignoreCase} is true. The substring of
     * this {@code CharSeq} object to be compared begins at index
     * {@code toffset} and has length {@code len}. The substring of
     * {@code other} to be compared begins at index {@code ooffset} and
     * has length {@code len}. The result is {@code false} if and only if
     * at least one of the following is true:
     * <ul><li>{@code toffset} is negative.
     * <li>{@code ooffset} is negative.
     * <li>{@code toffset+len} is greater than the length of this
     * {@code CharSeq} object.
     * <li>{@code ooffset+len} is greater than the length of the other
     * argument.
     * <li>{@code ignoreCase} is {@code false} and there is some nonnegative
     * integer <i>k</i> less than {@code len} such that:
     * <blockquote><pre>
     * this.charAt(toffset+k) != other.charAt(ooffset+k)
     * </pre></blockquote>
     * <li>{@code ignoreCase} is {@code true} and there is some nonnegative
     * integer <i>k</i> less than {@code len} such that:
     * <blockquote><pre>
     * Character.toLowerCase(this.charAt(toffset+k)) !=
     * Character.toLowerCase(other.charAt(ooffset+k))
     * </pre></blockquote>
     * and:
     * <blockquote><pre>
     * Character.toUpperCase(this.charAt(toffset+k)) !=
     *         Character.toUpperCase(other.charAt(ooffset+k))
     * </pre></blockquote>
     * </ul>
     *
     * @param ignoreCase if {@code true}, ignore case when comparing
     *                   characters.
     * @param toffset    the starting offset of the subregion in this
     *                   string.
     * @param other      the string argument.
     * @param ooffset    the starting offset of the subregion in the string
     *                   argument.
     * @param len        the number of characters to compare.
     * @return {@code true} if the specified subregion of this string
     * matches the specified subregion of the string argument;
     * {@code false} otherwise. Whether the matching is exact
     * or case insensitive depends on the {@code ignoreCase}
     * argument.
     */
    public boolean regionMatches(boolean ignoreCase, int toffset, CharSeq other, int ooffset, int len) {
        return back.regionMatches(ignoreCase, toffset, other.back, ooffset, len);
    }

    @Override
    public CharSeq subSequence(int beginIndex, int endIndex) {
        return slice(beginIndex, endIndex);
    }

    /**
     * Tests if the substring of this string beginning at the
     * specified index starts with the specified prefix.
     *
     * @param prefix  the prefix.
     * @param toffset where to begin looking in this string.
     * @return {@code true} if the character sequence represented by the
     * argument is a prefix of the substring of this object starting
     * at index {@code toffset}; {@code false} otherwise.
     * The result is {@code false} if {@code toffset} is
     * negative or greater than the length of this
     * {@code CharSeq} object; otherwise the result is the same
     * as the result of the expression
     * <pre>
     *          this.substring(toffset).startsWith(prefix)
     *          </pre>
     */
    public boolean startsWith(CharSeq prefix, int toffset) {
        return back.startsWith(prefix.back, toffset);
    }

    /**
     * Tests if this string starts with the specified prefix.
     *
     * @param prefix the prefix.
     * @return {@code true} if the character sequence represented by the
     * argument is a prefix of the character sequence represented by
     * this string; {@code false} otherwise.
     * Note also that {@code true} will be returned if the
     * argument is an empty string or is equal to this
     * {@code CharSeq} object as determined by the
     * {@link #equals(Object)} method.
     */
    public boolean startsWith(CharSeq prefix) {
        return back.startsWith(prefix.back);
    }

    /**
     * Tests if this string ends with the specified suffix.
     *
     * @param suffix the suffix.
     * @return {@code true} if the character sequence represented by the
     * argument is a suffix of the character sequence represented by
     * this object; {@code false} otherwise. Note that the
     * result will be {@code true} if the argument is the
     * empty string or is equal to this {@code CharSeq} object
     * as determined by the {@link #equals(Object)} method.
     */
    public boolean endsWith(CharSeq suffix) {
        return back.endsWith(suffix.back);
    }

    /**
     * Returns the index within this string of the first occurrence of
     * the specified character. If a character with value
     * {@code ch} occurs in the character sequence represented by
     * this {@code CharSeq} object, then the index (in Unicode
     * code units) of the first such occurrence is returned. For
     * values of {@code ch} in the range from 0 to 0xFFFF
     * (inclusive), this is the smallest value <i>k</i> such that:
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * smallest value <i>k</i> such that:
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this
     * string, then {@code -1} is returned.
     *
     * @param ch a character (Unicode code point).
     * @return the index of the first occurrence of the character in the
     * character sequence represented by this object, or
     * {@code -1} if the character does not occur.
     */
    public int indexOf(int ch) {
        return back.indexOf(ch);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified character, starting the search at the specified index.
     * <p>
     * If a character with value {@code ch} occurs in the
     * character sequence represented by this {@code CharSeq}
     * object at an index no smaller than {@code fromIndex}, then
     * the index of the first such occurrence is returned. For values
     * of {@code ch} in the range from 0 to 0xFFFF (inclusive),
     * this is the smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this
     * string at or after position {@code fromIndex}, then
     * {@code -1} is returned.
     *
     * <p>
     * There is no restriction on the value of {@code fromIndex}. If it
     * is negative, it has the same effect as if it were zero: this entire
     * string may be searched. If it is greater than the length of this
     * string, it has the same effect as if it were equal to the length of
     * this string: {@code -1} is returned.
     *
     * <p>All indices are specified in {@code char} values
     * (Unicode code units).
     *
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from.
     * @return the index of the first occurrence of the character in the
     * character sequence represented by this object that is greater
     * than or equal to {@code fromIndex}, or {@code -1}
     * if the character does not occur.
     */
    public int indexOf(int ch, int fromIndex) {
        return back.indexOf(ch, fromIndex);
    }

    /**
     * Returns the index within this string of the last occurrence of
     * the specified character. For values of {@code ch} in the
     * range from 0 to 0xFFFF (inclusive), the index (in Unicode code
     * units) returned is the largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true.  In either case, if no such character occurs in this
     * string, then {@code -1} is returned.  The
     * {@code CharSeq} is searched backwards starting at the last
     * character.
     *
     * @param ch a character (Unicode code point).
     * @return the index of the last occurrence of the character in the
     * character sequence represented by this object, or
     * {@code -1} if the character does not occur.
     */
    public int lastIndexOf(int ch) {
        return back.lastIndexOf(ch);
    }

    /**
     * Returns the index within this string of the last occurrence of
     * the specified character, searching backward starting at the
     * specified index. For values of {@code ch} in the range
     * from 0 to 0xFFFF (inclusive), the index returned is the largest
     * value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * largest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this
     * string at or before position {@code fromIndex}, then
     * {@code -1} is returned.
     *
     * <p>All indices are specified in {@code char} values
     * (Unicode code units).
     *
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from. There is no
     *                  restriction on the value of {@code fromIndex}. If it is
     *                  greater than or equal to the length of this string, it has
     *                  the same effect as if it were equal to one less than the
     *                  length of this string: this entire string may be searched.
     *                  If it is negative, it has the same effect as if it were -1:
     *                  -1 is returned.
     * @return the index of the last occurrence of the character in the
     * character sequence represented by this object that is less
     * than or equal to {@code fromIndex}, or {@code -1}
     * if the character does not occur before that point.
     */
    public int lastIndexOf(int ch, int fromIndex) {
        return back.lastIndexOf(ch, fromIndex);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     *
     * <p>The returned index is the smallest value <i>k</i> for which:
     * <blockquote><pre>
     * this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     *
     * @param str the substring to search for.
     * @return the index of the first occurrence of the specified substring,
     * or {@code -1} if there is no such occurrence.
     */
    public int indexOf(CharSeq str) {
        return back.indexOf(str.back);
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     *
     * <p>The returned index is the smallest value <i>k</i> for which:
     * <blockquote><pre>
     * <i>k</i> &gt;= fromIndex {@code &&} this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     *
     * @param str       the substring to search for.
     * @param fromIndex the index from which to start the search.
     * @return the index of the first occurrence of the specified substring,
     * starting at the specified index,
     * or {@code -1} if there is no such occurrence.
     */
    public int indexOf(CharSeq str, int fromIndex) {
        return back.indexOf(str.back, fromIndex);
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring.  The last occurrence of the empty string ""
     * is considered to occur at the index value {@code this.length()}.
     *
     * <p>The returned index is the largest value <i>k</i> for which:
     * <blockquote><pre>
     * this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     *
     * @param str the substring to search for.
     * @return the index of the last occurrence of the specified substring,
     * or {@code -1} if there is no such occurrence.
     */
    public int lastIndexOf(CharSeq str) {
        return back.lastIndexOf(str.back);
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring, searching backward starting at the specified index.
     *
     * <p>The returned index is the largest value <i>k</i> for which:
     * <blockquote><pre>
     * <i>k</i> {@code <=} fromIndex {@code &&} this.startsWith(str, <i>k</i>)
     * </pre></blockquote>
     * If no such value of <i>k</i> exists, then {@code -1} is returned.
     *
     * @param str       the substring to search for.
     * @param fromIndex the index to start the search from.
     * @return the index of the last occurrence of the specified substring,
     * searching backward from the specified index,
     * or {@code -1} if there is no such occurrence.
     */
    public int lastIndexOf(CharSeq str, int fromIndex) {
        return back.lastIndexOf(str.back, fromIndex);
    }

    /**
     * Returns a string that is a substring of this string. The
     * substring begins with the character at the specified index and
     * extends to the end of this string. <p>
     * Examples:
     * <blockquote><pre>
     * "unhappy".substring(2) returns "happy"
     * "Harbison".substring(3) returns "bison"
     * "emptiness".substring(9) returns "" (an empty string)
     * </pre></blockquote>
     *
     * @param beginIndex the beginning index, inclusive.
     * @return the specified substring.
     * @throws IndexOutOfBoundsException if
     *                                   {@code beginIndex} is negative or larger than the
     *                                   length of this {@code CharSeq} object.
     */
    public CharSeq substring(int beginIndex) {
        return of(back.substring(beginIndex));
    }

    /**
     * Returns a string that is a substring of this string. The
     * substring begins at the specified {@code beginIndex} and
     * extends to the character at index {@code endIndex - 1}.
     * Thus the length of the substring is {@code endIndex-beginIndex}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * "hamburger".substring(4, 8) returns "urge"
     * "smiles".substring(1, 5) returns "mile"
     * </pre></blockquote>
     *
     * @param beginIndex the beginning index, inclusive.
     * @param endIndex   the ending index, exclusive.
     * @return the specified substring.
     * @throws IndexOutOfBoundsException if the
     *                                   {@code beginIndex} is negative, or
     *                                   {@code endIndex} is larger than the length of
     *                                   this {@code CharSeq} object, or
     *                                   {@code beginIndex} is larger than
     *                                   {@code endIndex}.
     */
    public CharSeq substring(int beginIndex, int endIndex) {
        return of(back.substring(beginIndex, endIndex));
    }

    /**
     * Returns a string containing the characters in this sequence in the same
     * order as this sequence.  The length of the string will be the length of
     * this sequence.
     *
     * @return a string consisting of exactly this sequence of characters
     */
    @Override
    public java.lang.String toString() {
        return back;
    }

    /**
     * Concatenates the specified string to the end of this string.
     * <p>
     * If the length of the argument string is {@code 0}, then this
     * {@code CharSeq} object is returned. Otherwise, a
     * {@code CharSeq} object is returned that represents a character
     * sequence that is the concatenation of the character sequence
     * represented by this {@code CharSeq} object and the character
     * sequence represented by the argument string.<p>
     * Examples:
     * <blockquote><pre>
     * "cares".concat("s") returns "caress"
     * "to".concat("get").concat("her") returns "together"
     * </pre></blockquote>
     *
     * @param str the {@code CharSeq} that is concatenated to the end
     *            of this {@code CharSeq}.
     * @return a string that represents the concatenation of this object's
     * characters followed by the string argument's characters.
     */
    public CharSeq concat(CharSeq str) {
        return of(back.concat(str.back));
    }

    /**
     * Tells whether or not this string matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     *
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .matches(}<i>regex</i>{@code )} yields exactly the
     * same result as the expression
     *
     * <blockquote>
     * {@link java.util.regex.Pattern}.{@link java.util.regex.Pattern#matches(java.lang.String, CharSequence)
     * matches(<i>regex</i>, <i>str</i>)}
     * </blockquote>
     *
     * @param regex the regular expression to which this string is to be matched
     * @return {@code true} if, and only if, this string matches the
     * given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     */
    public boolean matches(java.lang.String regex) {
        return back.matches(regex);
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     *
     * @param s the sequence to search for
     * @return true if this string contains {@code s}, false otherwise
     */
    public boolean contains(CharSequence s) {
        return back.contains(s);
    }

    /**
     * Replaces the first substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     *
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceFirst(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(java.lang.CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceFirst replaceFirst}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     * <p>
     * Note that backslashes ({@code \}) and dollar signs ({@code $}) in the
     * replacement string may cause the results to be different than if it were
     * being treated as a literal replacement string; see
     * {@link java.util.regex.Matcher#replaceFirst}.
     * Use {@link java.util.regex.Matcher#quoteReplacement} to suppress the special
     * meaning of these characters, if desired.
     *
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for the first match
     * @return The resulting {@code CharSeq}
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     */
    public CharSeq replaceFirst(java.lang.String regex, java.lang.String replacement) {
        return of(back.replaceFirst(regex, replacement));
    }

    /**
     * Replaces each substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     *
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceAll(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(java.lang.CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceAll replaceAll}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     * <p>
     * Note that backslashes ({@code \}) and dollar signs ({@code $}) in the
     * replacement string may cause the results to be different than if it were
     * being treated as a literal replacement string; see
     * {@link java.util.regex.Matcher#replaceAll Matcher.replaceAll}.
     * Use {@link java.util.regex.Matcher#quoteReplacement} to suppress the special
     * meaning of these characters, if desired.
     *
     * @param regex       the regular expression to which this string is to be matched
     * @param replacement the string to be substituted for each match
     * @return The resulting {@code CharSeq}
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     */
    public CharSeq replaceAll(java.lang.String regex, java.lang.String replacement) {
        return of(back.replaceAll(regex, replacement));
    }

    /**
     * Replaces each substring of this string that matches the literal target
     * sequence with the specified literal replacement sequence. The
     * replacement proceeds from the beginning of the string to the end, for
     * example, replacing "aa" with "b" in the string "aaa" will result in
     * "ba" rather than "ab".
     *
     * @param target      The sequence of char values to be replaced
     * @param replacement The replacement sequence of char values
     * @return The resulting string
     */
    public CharSeq replace(CharSequence target, CharSequence replacement) {
        return of(back.replace(target, replacement));
    }

    /**
     * Splits this string around matches of the given
     * <a href="../util/regex/Pattern.html#sum">regular expression</a>.
     *
     * <p> The array returned by this method contains each substring of this
     * string that is terminated by another substring that matches the given
     * expression or is terminated by the end of the string.  The substrings in
     * the array are in the order in which they occur in this string.  If the
     * expression does not match any part of the input then the resulting array
     * has just one element, namely this string.
     *
     * <p> When there is a positive-width match at the beginning of this
     * string then an empty leading substring is included at the beginning
     * of the resulting array. A zero-width match at the beginning however
     * never produces such empty leading substring.
     *
     * <p> The {@code limit} parameter controls the number of times the
     * pattern is applied and therefore affects the length of the resulting
     * array.  If the limit <i>n</i> is greater than zero then the pattern
     * will be applied at most <i>n</i>&nbsp;-&nbsp;1 times, the array's
     * length will be no greater than <i>n</i>, and the array's last entry
     * will contain all input beyond the last matched delimiter.  If <i>n</i>
     * is non-positive then the pattern will be applied as many times as
     * possible and the array can have any length.  If <i>n</i> is zero then
     * the pattern will be applied as many times as possible, the array can
     * have any length, and trailing empty strings will be discarded.
     *
     * <p> The string {@code "boo:and:foo"}, for example, yields the
     * following results with these parameters:
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split example showing regex, limit, and result">
     * <tr>
     * <th>Regex</th>
     * <th>Limit</th>
     * <th>Result</th>
     * </tr>
     * <tr><td align=center>:</td>
     * <td align=center>2</td>
     * <td>{@code { "boo", "and:foo" }}</td></tr>
     * <tr><td align=center>:</td>
     * <td align=center>5</td>
     * <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>:</td>
     * <td align=center>-2</td>
     * <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>o</td>
     * <td align=center>5</td>
     * <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><td align=center>o</td>
     * <td align=center>-2</td>
     * <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><td align=center>o</td>
     * <td align=center>0</td>
     * <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </table></blockquote>
     *
     * <p> An invocation of this method of the form
     * <i>str.</i>{@code split(}<i>regex</i>{@code ,}&nbsp;<i>n</i>{@code )}
     * yields the same result as the expression
     *
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#split(java.lang.CharSequence, int) split}(<i>str</i>,&nbsp;<i>n</i>)
     * </code>
     * </blockquote>
     *
     * @param regex the delimiting regular expression
     * @param limit the result threshold, as described above
     * @return the array of strings computed by splitting this string
     * around matches of the given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     */
    public CharSeq[] split(java.lang.String regex, int limit) {
        final java.lang.String[] javaStrings = back.split(regex, limit);
        final CharSeq[] strings = new CharSeq[javaStrings.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = of(javaStrings[i]);
        }
        return strings;
    }

    /**
     * Splits this string around matches of the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     *
     * <p> This method works as if by invoking the two-argument {@link
     * #split(java.lang.String, int) split} method with the given expression and a limit
     * argument of zero.  Trailing empty strings are therefore not included in
     * the resulting array.
     *
     * <p> The string {@code "boo:and:foo"}, for example, yields the following
     * results with these expressions:
     *
     * <blockquote><table cellpadding=1 cellspacing=0 summary="Split examples showing regex and result">
     * <tr>
     * <th>Regex</th>
     * <th>Result</th>
     * </tr>
     * <tr><td align=center>:</td>
     * <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><td align=center>o</td>
     * <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </table></blockquote>
     *
     * @param regex the delimiting regular expression
     * @return the array of strings computed by splitting this string
     * around matches of the given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     */
    public CharSeq[] split(java.lang.String regex) {
        return split(regex, 0);
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to lower
     * case using the rules of the given {@code Locale}.  Case mapping is based
     * on the Unicode Standard version specified by the {@link java.lang.Character Character}
     * class. Since case mappings are not always 1:1 char mappings, the resulting
     * {@code CharSeq} may be a different length than the original {@code CharSeq}.
     * <p>
     * Examples of lowercase  mappings are in the following table:
     * <table border="1" summary="Lowercase mapping examples showing language code of locale, upper case, lower case, and description">
     * <tr>
     * <th>Language Code of Locale</th>
     * <th>Upper Case</th>
     * <th>Lower Case</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>tr (Turkish)</td>
     * <td>&#92;u0130</td>
     * <td>&#92;u0069</td>
     * <td>capital letter I with dot above -&gt; small letter i</td>
     * </tr>
     * <tr>
     * <td>tr (Turkish)</td>
     * <td>&#92;u0049</td>
     * <td>&#92;u0131</td>
     * <td>capital letter I -&gt; small letter dotless i </td>
     * </tr>
     * <tr>
     * <td>(all)</td>
     * <td>French Fries</td>
     * <td>french fries</td>
     * <td>lowercased all chars in String</td>
     * </tr>
     * <tr>
     * <td>(all)</td>
     * <td><img src="doc-files/capiota.gif" alt="capiota"><img src="doc-files/capchi.gif" alt="capchi">
     * <img src="doc-files/captheta.gif" alt="captheta"><img src="doc-files/capupsil.gif" alt="capupsil">
     * <img src="doc-files/capsigma.gif" alt="capsigma"></td>
     * <td><img src="doc-files/iota.gif" alt="iota"><img src="doc-files/chi.gif" alt="chi">
     * <img src="doc-files/theta.gif" alt="theta"><img src="doc-files/upsilon.gif" alt="upsilon">
     * <img src="doc-files/sigma1.gif" alt="sigma"></td>
     * <td>lowercased all chars in String</td>
     * </tr>
     * </table>
     *
     * @param locale use the case transformation rules for this locale
     * @return the {@code CharSeq}, converted to lowercase.
     * @see java.lang.String#toLowerCase()
     * @see java.lang.String#toUpperCase()
     * @see java.lang.String#toUpperCase(Locale)
     */
    public CharSeq toLowerCase(Locale locale) {
        return of(back.toLowerCase(locale));
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to lower
     * case using the rules of the default locale. This is equivalent to calling
     * {@code toLowerCase(Locale.getDefault())}.
     * <p>
     * <b>Note:</b> This method is locale sensitive, and may produce unexpected
     * results if used for strings that are intended to be interpreted locale
     * independently.
     * Examples are programming language identifiers, protocol keys, and HTML
     * tags.
     * For instance, {@code "TITLE".toLowerCase()} in a Turkish locale
     * returns {@code "t\u005Cu0131tle"}, where '\u005Cu0131' is the
     * LATIN SMALL LETTER DOTLESS I character.
     * To obtain correct results for locale insensitive strings, use
     * {@code toLowerCase(Locale.ROOT)}.
     * <p>
     *
     * @return the {@code CharSeq}, converted to lowercase.
     * @see java.lang.String#toLowerCase(Locale)
     */
    public CharSeq toLowerCase() {
        return toLowerCase(Locale.getDefault());
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to upper
     * case using the rules of the given {@code Locale}. Case mapping is based
     * on the Unicode Standard version specified by the {@link java.lang.Character Character}
     * class. Since case mappings are not always 1:1 char mappings, the resulting
     * {@code CharSeq} may be a different length than the original {@code CharSeq}.
     * <p>
     * Examples of locale-sensitive and 1:M case mappings are in the following table.
     *
     * <table border="1" summary="Examples of locale-sensitive and 1:M case mappings. Shows Language code of locale, lower case, upper case, and description.">
     * <tr>
     * <th>Language Code of Locale</th>
     * <th>Lower Case</th>
     * <th>Upper Case</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>tr (Turkish)</td>
     * <td>&#92;u0069</td>
     * <td>&#92;u0130</td>
     * <td>small letter i -&gt; capital letter I with dot above</td>
     * </tr>
     * <tr>
     * <td>tr (Turkish)</td>
     * <td>&#92;u0131</td>
     * <td>&#92;u0049</td>
     * <td>small letter dotless i -&gt; capital letter I</td>
     * </tr>
     * <tr>
     * <td>(all)</td>
     * <td>&#92;u00df</td>
     * <td>&#92;u0053 &#92;u0053</td>
     * <td>small letter sharp s -&gt; two letters: SS</td>
     * </tr>
     * <tr>
     * <td>(all)</td>
     * <td>Fahrvergn&uuml;gen</td>
     * <td>FAHRVERGN&Uuml;GEN</td>
     * <td></td>
     * </tr>
     * </table>
     *
     * @param locale use the case transformation rules for this locale
     * @return the {@code CharSeq}, converted to uppercase.
     * @see java.lang.String#toUpperCase()
     * @see java.lang.String#toLowerCase()
     * @see java.lang.String#toLowerCase(Locale)
     */
    public CharSeq toUpperCase(Locale locale) {
        return of(back.toUpperCase(locale));
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to upper
     * case using the rules of the default locale. This method is equivalent to
     * {@code toUpperCase(Locale.getDefault())}.
     * <p>
     * <b>Note:</b> This method is locale sensitive, and may produce unexpected
     * results if used for strings that are intended to be interpreted locale
     * independently.
     * Examples are programming language identifiers, protocol keys, and HTML
     * tags.
     * For instance, {@code "title".toUpperCase()} in a Turkish locale
     * returns {@code "T\u005Cu0130TLE"}, where '\u005Cu0130' is the
     * LATIN CAPITAL LETTER I WITH DOT ABOVE character.
     * To obtain correct results for locale insensitive strings, use
     * {@code toUpperCase(Locale.ROOT)}.
     * <p>
     *
     * @return the {@code CharSeq}, converted to uppercase.
     * @see java.lang.String#toUpperCase(Locale)
     */
    public CharSeq toUpperCase() {
        return toUpperCase(Locale.getDefault());
    }

    /**
     * Returns a string whose value is this string, with any leading and trailing
     * whitespace removed.
     * <p>
     * If this {@code CharSeq} object represents an empty character
     * sequence, or the first and last characters of character sequence
     * represented by this {@code CharSeq} object both have codes
     * greater than {@code '\u005Cu0020'} (the space character), then a
     * reference to this {@code CharSeq} object is returned.
     * <p>
     * Otherwise, if there is no character with a code greater than
     * {@code '\u005Cu0020'} in the string, then a
     * {@code CharSeq} object representing an empty string is
     * returned.
     * <p>
     * Otherwise, let <i>k</i> be the index of the first character in the
     * string whose code is greater than {@code '\u005Cu0020'}, and let
     * <i>m</i> be the index of the last character in the string whose code
     * is greater than {@code '\u005Cu0020'}. A {@code CharSeq}
     * object is returned, representing the substring of this string that
     * begins with the character at index <i>k</i> and ends with the
     * character at index <i>m</i>-that is, the result of
     * {@code this.substring(k, m + 1)}.
     * <p>
     * This method may be used to trim whitespace (as defined above) from
     * the beginning and end of a string.
     *
     * @return A string whose value is this string, with any leading and trailing white
     * space removed, or this string if it has no leading or
     * trailing white space.
     */
    public CharSeq trim() {
        return of(back.trim());
    }

    /**
     * Converts this string to a new character array.
     *
     * @return a newly allocated character array whose length is the length
     * of this string and whose contents are initialized to contain
     * the character sequence represented by this string.
     */
    public char[] toCharArray() {
        return back.toCharArray();
    }

    @FunctionalInterface
    interface CharUnaryOperator {
        char apply(char c);
    }

    @FunctionalInterface
    interface CharFunction<R> {
        R apply(char c);
    }
}
