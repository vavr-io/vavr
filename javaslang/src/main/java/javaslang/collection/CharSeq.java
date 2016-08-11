/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind1;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
import javaslang.control.Option;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collector;

import static javaslang.collection.Arrays2.toPrimitiveArray;

/**
 * The CharSeq (read: character sequence) collection essentially is a rich String wrapper having all operations
 * we know from the functional Javaslang collections.
 *
 * @author Ruslan Sennov, Daniel Dietrich, Pap Lőrinc
 * @since 2.0.0
 */
public final class CharSeq implements Kind1<CharSeq, Character>, CharSequence, IndexedSeq<Character>, Serializable {
    private static final long serialVersionUID = 1L;

    private static final CharSeq EMPTY = new CharSeq(Vector.empty());

    final Vector<Character> delegate;

    private CharSeq(Vector<Character> chars) {
        if (chars.nonEmpty() && !chars.type.isPrimitive()) {
            this.delegate = Vector.ofAll(Arrays2.<char[]> toPrimitiveArray(char.class, chars.toJavaArray()));
        } else {
            this.delegate = chars;
        }
    }

    private CharSeq(String javaString) {
        this(Vector.ofAll(javaString.toCharArray()));
    }

    private static CharSeq of(Vector<Character> chars) {
        return chars.isEmpty() ? empty()
                               : new CharSeq(chars);
    }

    private CharSeq from(Vector<Character> chars) {
        return (chars == delegate) ? this
                                   : of(chars);
    }

    public static CharSeq empty() {
        return EMPTY;
    }

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link CharSeq}.
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
     * Creates a String of {@code CharSequence}.
     *
     * @param sequence {@code CharSequence} instance.
     * @return A new {@code javaslang.String}
     */
    // DEV-NOTE: Needs to be 'of' instead of 'ofAll' because 'ofAll(CharSeq)' is ambiguous.
    public static CharSeq of(CharSequence sequence) {
        Objects.requireNonNull(sequence, "sequence is null");
        if (sequence instanceof CharSeq) {
            return (CharSeq) sequence;
        } else {
            return (sequence.length() == 0) ? empty()
                                            : new CharSeq(sequence.toString());
        }
    }

    /**
     * Returns a singleton {@code CharSeq}, i.e. a {@code CharSeq} of one character.
     *
     * @param character A character.
     * @return A new {@code CharSeq} instance containing the given element
     */
    public static CharSeq of(char character) {
        return of(Vector.ofAll(new char[] {character}));
    }

    /**
     * Creates a String of the given characters.
     *
     * @param characters Zero or more characters.
     * @return A string containing the given characters in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static CharSeq of(char... characters) {
        return of(Vector.ofAll(characters));
    }

    /**
     * Creates a String of the given elements.
     * <p>
     * The resulting string has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param elements An Iterable of elements.
     * @return A string containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    public static CharSeq ofAll(Iterable<? extends Character> elements) {
        final char[] array = toPrimitiveArray(char.class, Vector.ofAll(elements).toJavaArray());
        return of(Vector.ofAll(array));
    }

    /**
     * Returns a CharSeq containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param n The number of elements in the CharSeq
     * @param f The Function computing element values
     * @return A CharSeq consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    public static CharSeq tabulate(int n, Function<? super Integer, ? extends Character> f) {
        return of(Vector.tabulate(n, f));
    }

    /**
     * Returns a CharSeq containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param n The number of elements in the CharSeq
     * @param s The Supplier computing element values
     * @return A CharSeq of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    public static CharSeq fill(int n, Supplier<? extends Character> s) {
        return of(Vector.fill(n, s));
    }

    /**
     * Creates a CharSeq starting from character {@code from}, extending to character {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * CharSeq.range('a', 'c')  // = "ab"
     * CharSeq.range('c', 'a')  // = ""
     * </code>
     * </pre>
     *
     * @param from        the first character
     * @param toExclusive the successor of the last character
     * @return a range of characters as specified or the empty range if {@code from >= toExclusive}
     */
    public static CharSeq range(char from, char toExclusive) {
        return of(Vector.range(from, toExclusive));
    }

    public static CharSeq rangeBy(char from, char toExclusive, int step) {
        return of(Vector.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a CharSeq starting from character {@code from}, extending to character {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * CharSeq.rangeClosed('a', 'c')  // = "abc"
     * CharSeq.rangeClosed('c', 'a')  // = ""
     * </code>
     * </pre>
     *
     * @param from        the first character
     * @param toInclusive the last character
     * @return a range of characters as specified or the empty range if {@code from > toInclusive}
     */
    public static CharSeq rangeClosed(char from, char toInclusive) {
        return of(Vector.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a CharSeq starting from character {@code from}, extending to character {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * CharSeq.rangeClosedBy('a', 'c', 1)  // = ('a', 'b', 'c')
     * CharSeq.rangeClosedBy('a', 'd', 2)  // = ('a', 'c')
     * CharSeq.rangeClosedBy('d', 'a', -2) // = ('d', 'b')
     * CharSeq.rangeClosedBy('d', 'a', 2)  // = ()
     * </code>
     * </pre>
     *
     * @param from        the first character
     * @param toInclusive the last character
     * @param step        the step
     * @return a range of characters as specified or the empty range if {@code step * (from - toInclusive) > 0}.
     * @throws IllegalArgumentException if {@code step} is zero
     */
    public static CharSeq rangeClosedBy(char from, char toInclusive, int step) {
        return of(Vector.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a CharSeq from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the CharSeq, otherwise {@code Some} {@code Tuple}
     * of the element for the next call and the value to add to the
     * resulting CharSeq.
     * <p>
     * Example:
     * <pre>
     * <code>
     * CharSeq.unfoldRight('j', x -&gt; x == 'a'
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(new Character(x), (char)(x-1))));
     * // CharSeq.of("jihgfedcb"))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a CharSeq with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static <T> CharSeq unfoldRight(T seed, Function<? super T, Option<Tuple2<? extends Character, ? extends T>>> f) {
        return of(Vector.unfoldRight(seed, f));
    }

    /**
     * Creates a CharSeq from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the CharSeq, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting CharSeq and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * CharSeq.unfoldLeft('j', x -&gt; x == 'a'
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;((char)(x-1), new Character(x))));
     * // CharSeq.of("bcdefghij"))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a CharSeq with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static <T> CharSeq unfoldLeft(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends Character>>> f) {
        return of(Vector.unfoldLeft(seed, f));
    }

    /**
     * Creates a CharSeq from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the CharSeq, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting CharSeq and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * CharSeq.unfold('j', x -&gt; x == 'a'
     *                ? Option.none()
     *                : Option.of(new Tuple2&lt;&gt;((char)(x-1), new Character(x))));
     * // CharSeq.of("bcdefghij"))
     * </code>
     * </pre>
     *
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a CharSeq with the values built up by the iteration
     * @throws IllegalArgumentException if {@code f} is null
     */
    public static CharSeq unfold(Character seed, Function<? super Character, Option<Tuple2<? extends Character, ? extends Character>>> f) {
        return of(Vector.unfold(seed, f));
    }

    /**
     * Repeats a character {@code times} times.
     *
     * @param character A character
     * @param times     Repetition count
     * @return A CharSeq representing {@code character * times}
     */
    public static CharSeq repeat(char character, int times) {
        return of(character).repeat(times);
    }

    /**
     * Repeats this CharSeq {@code times} times.
     * <p>
     * Example: {@code CharSeq.of("ja").repeat(13) = "jajajajajajajajajajajajaja"}
     *
     * @param times Repetition count
     * @return A CharSeq representing {@code this * times}
     */
    public CharSeq repeat(int times) {
        Vector<Character> result = Vector.empty();
        for (int i = 0; i < times; i++) {
            result = result.appendAll(delegate);
        }
        return from(result);
    }

    //
    //
    // IndexedSeq
    //
    //

    @Override
    public CharSeq append(Character element) {
        return from(delegate.append(element));
    }

    @Override
    public CharSeq appendAll(Iterable<? extends Character> elements) {
        return from(delegate.appendAll(elements));
    }

    @Override
    public Vector<CharSeq> combinations() {
        return delegate.combinations().map(CharSeq::of);
    }

    @Override
    public Vector<CharSeq> combinations(int k) {
        return delegate.combinations(k).map(CharSeq::of);
    }

    @Override
    public Iterator<CharSeq> crossProduct(int power) {
        return Collections.crossProduct(empty(), this, power);
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
        return from(delegate.drop(n));
    }

    @Override
    public CharSeq dropRight(int n) {
        return from(delegate.dropRight(n));
    }

    @Override
    public CharSeq dropUntil(Predicate<? super Character> predicate) {
        return from(delegate.dropUntil(predicate));
    }

    @Override
    public CharSeq dropWhile(Predicate<? super Character> predicate) {
        return from(delegate.dropWhile(predicate));
    }

    @Override
    public CharSeq filter(Predicate<? super Character> predicate) {
        return from(delegate.filter(predicate));
    }

    @Override
    public <U> Vector<U> flatMap(Function<? super Character, ? extends Iterable<? extends U>> mapper) {
        return delegate.flatMap(mapper);
    }

    public CharSeq flatMapChars(CharFunction<? extends CharSequence> mapper) {
        return from(delegate.flatMap(c -> of(mapper.apply(c))));
    }

    @Override
    public <C> Map<C, CharSeq> groupBy(Function<? super Character, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, CharSeq::ofAll);
    }

    @Override
    public Iterator<CharSeq> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public CharSeq init() {
        return from(delegate.init());
    }

    @Override
    public Option<CharSeq> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public CharSeq insert(int index, Character element) {
        return from(delegate.insert(index, element));
    }

    @Override
    public CharSeq insertAll(int index, Iterable<? extends Character> elements) {
        return from(delegate.insertAll(index, elements));
    }

    @Override
    public Iterator<Character> iterator() {
        return delegate.iterator();
    }

    @Override
    public CharSeq intersperse(Character element) {
        return from(delegate.intersperse(element));
    }

    @Override
    public <U> Vector<U> map(Function<? super Character, ? extends U> mapper) {
        return delegate.map(mapper);
    }

    @Override
    public CharSeq padTo(int length, Character element) {
        return from(delegate.padTo(length, element));
    }

    @Override
    public CharSeq leftPadTo(int length, Character element) {
        return from(delegate.leftPadTo(length, element));
    }

    @Override
    public CharSeq patch(int from, Iterable<? extends Character> that, int replaced) {
        return from(delegate.patch(from, that, replaced));
    }

    public CharSeq mapChars(CharUnaryOperator mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return from(delegate.map(mapper::apply));
    }

    @Override
    public Tuple2<CharSeq, CharSeq> partition(Predicate<? super Character> predicate) {
        return delegate.partition(predicate)
                       .map((l, r) -> Tuple.of(from(l), from(r)));
    }

    @Override
    public CharSeq peek(Consumer<? super Character> action) {
        delegate.peek(action);
        return this;
    }

    @Override
    public Vector<CharSeq> permutations() {
        return delegate.permutations().map(CharSeq::of);
    }

    @Override
    public CharSeq prepend(Character element) {
        return from(delegate.prepend(element));
    }

    @Override
    public CharSeq prependAll(Iterable<? extends Character> elements) {
        return from(delegate.prependAll(elements));
    }

    @Override
    public CharSeq remove(Character element) {
        return from(delegate.remove(element));
    }

    @Override
    public CharSeq removeFirst(Predicate<Character> predicate) {
        return from(delegate.removeFirst(predicate));
    }

    @Override
    public CharSeq removeLast(Predicate<Character> predicate) {
        return from(delegate.removeLast(predicate));
    }

    @Override
    public CharSeq removeAt(int index) {
        return from(delegate.removeAt(index));
    }

    @Override
    public CharSeq removeAll(Character element) {
        return Collections.removeAll(this, element);
    }

    @Override
    public CharSeq removeAll(Iterable<? extends Character> elements) {
        return Collections.removeAll(this, elements);
    }

    @Override
    public CharSeq removeAll(Predicate<? super Character> predicate) {
        return Collections.removeAll(this, predicate);
    }

    @Override
    public CharSeq replace(Character currentElement, Character newElement) {
        for (int i = 0; i < length(); ) {
            final char[] leaf = (char[]) delegate.getLeafUnsafe(i);
            for (char value : leaf) {
                if (value == currentElement) {
                    return update(i, newElement);
                }
                i++;
            }
        }
        return this;
    }

    @Override
    public CharSeq replaceAll(Character currentElement, Character newElement) {
        return from(delegate.replaceAll(currentElement, newElement));
    }

    @Override
    public CharSeq retainAll(Iterable<? extends Character> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    public CharSeq reverse() {
        return from(delegate.reverse());
    }

    @Override
    public Vector<Character> scan(Character zero, BiFunction<? super Character, ? super Character, ? extends Character> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    public <U> Vector<U> scanLeft(U zero, BiFunction<? super U, ? super Character, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanLeft(this, zero, operation, Vector.empty(), Vector::append, Function.identity());
    }

    @Override
    public <U> Vector<U> scanRight(U zero, BiFunction<? super Character, ? super U, ? extends U> operation) {
        Objects.requireNonNull(operation, "operation is null");
        return Collections.scanRight(this, zero, operation, Vector.empty(), Vector::prepend, Function.identity());
    }

    @Override
    public CharSeq slice(int beginIndex, int endIndex) {
        return from(delegate.slice(beginIndex, endIndex));
    }

    @Override
    public Iterator<CharSeq> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    public Iterator<CharSeq> sliding(int size, int step) {
        return delegate.sliding(size, step).map(CharSeq::ofAll);
    }

    @Override
    public CharSeq sorted() {
        return from(delegate.sorted());
    }

    @Override
    public CharSeq sorted(Comparator<? super Character> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return from(delegate.sorted(comparator));
    }

    @Override
    public <U extends Comparable<? super U>> CharSeq sortBy(Function<? super Character, ? extends U> mapper) {
        return sortBy(U::compareTo, mapper);
    }

    @Override
    public <U> CharSeq sortBy(Comparator<? super U> comparator, Function<? super Character, ? extends U> mapper) {
        return from(delegate.sortBy(comparator, mapper));
    }

    @Override
    public Tuple2<CharSeq, CharSeq> span(Predicate<? super Character> predicate) {
        return delegate.span(predicate)
                       .map((l, r) -> Tuple.of(from(l), from(r)));
    }

    @Override
    public Spliterator<Character> spliterator() {
        return delegate.spliterator();
    }

    @Override
    public CharSeq subSequence(int beginIndex) {
        return from(delegate.subSequence(beginIndex));
    }

    @Override
    public CharSeq tail() {
        return from(delegate.tail());
    }

    @Override
    public Option<CharSeq> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    public CharSeq take(int n) {
        return from(delegate.take(n));
    }

    @Override
    public CharSeq takeRight(int n) {
        return from(delegate.takeRight(n));
    }

    @Override
    public CharSeq takeUntil(Predicate<? super Character> predicate) {
        return from(delegate.takeUntil(predicate));
    }

    @Override
    public CharSeq takeWhile(Predicate<? super Character> predicate) {
        return from(delegate.takeWhile(predicate));
    }

    /**
     * Transforms this {@code CharSeq}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super CharSeq, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public <U> Vector<U> unit(Iterable<? extends U> iterable) {
        return Vector.ofAll(iterable);
    }

    @Override
    public <T1, T2> Tuple2<Vector<T1>, Vector<T2>> unzip(Function<? super Character, Tuple2<? extends T1, ? extends T2>> unzipper) {
        return delegate.unzip(unzipper);
    }

    @Override
    public <T1, T2, T3> Tuple3<Vector<T1>, Vector<T2>, Vector<T3>> unzip3(Function<? super Character, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        return delegate.unzip3(unzipper);
    }

    @Override
    public CharSeq update(int index, Character element) {
        return from(delegate.update(index, element));
    }

    @Override
    public <U> Vector<Tuple2<Character, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    public <U, R> Vector<R> zipWith(Iterable<? extends U> that, BiFunction<? super Character, ? super U, ? extends R> mapper) {
        return delegate.zipWith(that, mapper);
    }

    @Override
    public <U> Vector<Tuple2<Character, U>> zipAll(Iterable<? extends U> that, Character thisElem, U thatElem) {
        return delegate.zipAll(that, thisElem, thatElem);
    }

    @Override
    public Vector<Tuple2<Character, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    public <U> Vector<U> zipWithIndex(BiFunction<? super Character, ? super Integer, ? extends U> mapper) {
        return delegate.zipWithIndex(mapper);
    }

    @Override
    public Character get(int index) {
        return delegate.get(index);
    }

    @Override
    public int indexOf(Character element, int from) {
        return delegate.indexOf(element, from);
    }

    @Override
    public int lastIndexOf(Character element, int end) {
        return delegate.lastIndexOf(element, end);
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAt(int n) {
        return delegate.splitAt(n)
                       .map((l, r) -> Tuple.of(from(l), from(r)));
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAt(Predicate<? super Character> predicate) {
        return delegate.splitAt(predicate)
                       .map((l, r) -> Tuple.of(from(l), from(r)));
    }

    @Override
    public Tuple2<CharSeq, CharSeq> splitAtInclusive(Predicate<? super Character> predicate) {
        return delegate.splitAtInclusive(predicate)
                       .map((l, r) -> Tuple.of(from(l), from(r)));
    }

    @Override
    public Character head() {
        return delegate.head();
    }

    @Override
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    private Object readResolve() {
        return isEmpty() ? EMPTY : this;
    }

    @Override
    public boolean equals(Object that) {
        return (that == this) || ((that instanceof CharSeq) && ((CharSeq) that).delegate.equals(delegate));
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    //
    //
    // CharSequence
    //
    //

    /**
     * Returns the {@code char} value at the
     * specified index. An index ranges from {@code 0} to
     * {@code length() - 1}. The first {@code char} value of the sequence
     * is at index {@code 0}, the next at index {@code 1},
     * and so on, as for array indexing.
     * <p>
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
        return get(index);
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
        return delegate.length();
    }

    //
    //
    // String
    //
    //

    /**
     * Returns the character (Unicode code point) at the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 0} to
     * {@link #length()}{@code  - 1}.
     * <p>
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
        return toString().codePointAt(index);
    }

    /**
     * Returns the character (Unicode code point) before the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 1} to {@link
     * CharSequence#length() length}.
     * <p>
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
        return toString().codePointBefore(index);
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
        return toString().codePointCount(beginIndex, endIndex);
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
        return toString().offsetByCodePoints(index, codePointOffset);
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
        toString().getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the named
     * charset, storing the result into a new byte array.
     * <p>
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
    public byte[] getBytes(String charsetName) throws UnsupportedEncodingException {
        return toString().getBytes(charsetName);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the given
     * {@linkplain java.nio.charset.Charset charset}, storing the result into a
     * new byte array.
     * <p>
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
        return toString().getBytes(charset);
    }

    /**
     * Encodes this {@code CharSeq} into a sequence of bytes using the
     * platform's default charset, storing the result into a new byte array.
     * <p>
     * <p> The behavior of this method when this string cannot be encoded in
     * the default charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     *
     * @return The resultant byte array
     */
    public byte[] getBytes() {
        return toString().getBytes();
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
        return toString().contentEquals(sb);
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
        return toString().contentEquals(cs);
    }

    /**
     * Compares this {@code CharSeq} to another {@code CharSeq}, ignoring case
     * considerations.  Two strings are considered equal ignoring case if they
     * are of the same length and corresponding characters in the two strings
     * are equal ignoring case.
     * <p>
     * <p> Two characters {@code c1} and {@code c2} are considered the same
     * ignoring case if at least one of the following is true:
     * <ul>
     * <li> The two characters are the same (as compared by the
     * {@code ==} operator)
     * <li> Applying the method {@link
     * Character#toUpperCase(char)} to each character
     * produces the same result
     * <li> Applying the method {@link
     * Character#toLowerCase(char)} to each character
     * produces the same result
     * </ul>
     *
     * @param that The {@code CharSeq} to compare this {@code CharSeq} against
     * @return {@code true} if the argument is not {@code null} and it
     * represents an equivalent {@code CharSeq} ignoring case; {@code
     * false} otherwise
     * @see #equals(Object)
     */
    public boolean equalsIgnoreCase(CharSeq that) {
        if (this.size() != that.size()) {
            return false;
        }

        for (int i = 0, size = size(); i < size; i++) {
            final Character source = get(i);
            final Character target = that.get(i);
            if ((source != target) && (Character.toLowerCase(source) != Character.toLowerCase(target))) {
                return false;
            }
        }
        return true;
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
        return toString().compareTo(anotherString.toString());
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
        return toString().compareToIgnoreCase(str.toString());
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
        return toString().regionMatches(toffset, other.toString(), ooffset, len);
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
        return toString().regionMatches(ignoreCase, toffset, other.toString(), ooffset, len);
    }

    @Override
    public CharSeq subSequence(int beginIndex, int endIndex) {
        return from(delegate.subSequence(beginIndex, endIndex));
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
        return delegate.startsWith(prefix);
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
        return delegate.indexOf((char) ch);
    }

    /**
     * Returns the index of the first occurrence of the given element as an {@code Option}.
     *
     * @param ch a character (Unicode code point).
     * @return {@code Some(index)} or {@code None} if not found.
     */
    Option<Integer> indexOfOption(int ch) {
        return Collections.indexOption(indexOf(ch));
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
     * <p>
     * <p>
     * There is no restriction on the value of {@code fromIndex}. If it
     * is negative, it has the same effect as if it were zero: this entire
     * string may be searched. If it is greater than the length of this
     * string, it has the same effect as if it were equal to the length of
     * this string: {@code -1} is returned.
     * <p>
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
        return delegate.indexOf((char) ch, fromIndex);
    }

    /**
     * Returns the index of the first occurrence of the given element as an {@code Option},
     * starting the search at the specified index.
     *
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    Option<Integer> indexOfOption(int ch, int fromIndex) {
        return Collections.indexOption(indexOf(ch, fromIndex));
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
        return delegate.lastIndexOf((char) ch);
    }

    /**
     * Returns the index of the last occurrence of the given element as an {@code Option}.
     *
     * @param ch a character (Unicode code point).
     * @return {@code Some(index)} or {@code None} if not found.
     */
    Option<Integer> lastIndexOfOption(int ch) {
        return Collections.indexOption(lastIndexOf(ch));
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
     * <p>
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
        return delegate.lastIndexOf((char) ch, fromIndex);
    }

    /**
     * Returns the index of the last occurrence of the given element as an {@code Option},
     * starting the search at the specified index.
     *
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    public Option<Integer> lastIndexOfOption(int ch, int fromIndex) {
        return Collections.indexOption(lastIndexOf(ch, fromIndex));
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     * <p>
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
        return delegate.indexOfSlice(str);
    }

    /**
     * Returns the index of the first occurrence of the given element as an {@code Option}.
     *
     * @param str the substring to search for.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    public Option<Integer> indexOfOption(CharSeq str) {
        return Collections.indexOption(indexOf(str));
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     * <p>
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
        return delegate.indexOfSlice(str, fromIndex);
    }

    /**
     * Returns the index of the first occurrence of the given element as an {@code Option},
     * starting the search at the specified index.
     *
     * @param str       the substring to search for.
     * @param fromIndex the index from which to start the search.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    public Option<Integer> indexOfOption(CharSeq str, int fromIndex) {
        return Collections.indexOption(indexOf(str, fromIndex));
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring.  The last occurrence of the empty string ""
     * is considered to occur at the index value {@code this.length()}.
     * <p>
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
        return delegate.lastIndexOfSlice(str);
    }

    /**
     * Returns the index of the last occurrence of the given element as an {@code Option}.
     *
     * @param str the substring to search for.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    public Option<Integer> lastIndexOfOption(CharSeq str) {
        return Collections.indexOption(lastIndexOf(str));
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring, searching backward starting at the specified index.
     * <p>
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
        return delegate.lastIndexOfSlice(str, fromIndex);
    }

    /**
     * Returns the index of the last occurrence of the given element as an {@code Option},
     * starting the search at the specified index.
     *
     * @param str       the substring to search for.
     * @param fromIndex the index to start the search from.
     * @return {@code Some(index)} or {@code None} if not found.
     */
    public Option<Integer> lastIndexOfOption(CharSeq str, int fromIndex) {
        return Collections.indexOption(lastIndexOf(str, fromIndex));
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
        return from(delegate.subSequence(beginIndex));
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
        return from(delegate.subSequence(beginIndex, endIndex));
    }

    @Override
    public String stringPrefix() {
        return "CharSeq";
    }

    /**
     * Returns a string containing the characters in this sequence in the same
     * order as this sequence.  The length of the string will be the length of
     * this sequence.
     *
     * @return a string consisting of exactly this sequence of characters
     */
    @Override
    public String toString() {
        return delegate.mkString();
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
        return appendAll(str);
    }

    /**
     * Tells whether or not this string matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p>
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .matches(}<i>regex</i>{@code )} yields exactly the
     * same result as the expression
     * <p>
     * <blockquote>
     * {@link Pattern}.{@link Pattern#matches(String, CharSequence)
     * matches(<i>regex</i>, <i>str</i>)}
     * </blockquote>
     *
     * @param regex the regular expression to which this string is to be matched
     * @return {@code true} if, and only if, this string matches the
     * given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see Pattern
     */
    public boolean matches(String regex) {
        return toString().matches(regex);
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     *
     * @param s the sequence to search for
     * @return true if this string contains {@code s}, false otherwise
     */
    public boolean contains(CharSequence s) {
        return indexOfSliceOption(of(s)).isDefined();
    }

    /**
     * Replaces the first substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     * <p>
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceFirst(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     * <p>
     * <blockquote>
     * <code>
     * {@link Pattern}.{@link
     * Pattern#compile compile}(<i>regex</i>).{@link
     * Pattern#matcher(CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceFirst replaceFirst}(<i>repl</i>)
     * </code>
     * </blockquote>
     * <p>
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
     * @see Pattern
     */
    public CharSeq replaceFirst(String regex, String replacement) {
        return of(toString().replaceFirst(regex, replacement));
    }

    /**
     * Replaces each substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     * <p>
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceAll(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     * <p>
     * <blockquote>
     * <code>
     * {@link Pattern}.{@link
     * Pattern#compile compile}(<i>regex</i>).{@link
     * Pattern#matcher(CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceAll replaceAll}(<i>repl</i>)
     * </code>
     * </blockquote>
     * <p>
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
     * @see Pattern
     */
    public CharSeq replaceAll(String regex, String replacement) {
        return of(toString().replaceAll(regex, replacement));
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
        return of(toString().replace(target, replacement));
    }

    /**
     * Splits this string around matches of the given
     * <a href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p>
     * <p> The array returned by this method contains each substring of this
     * string that is terminated by another substring that matches the given
     * expression or is terminated by the end of the string.  The substrings in
     * the array are in the order in which they occur in this string.  If the
     * expression does not match any part of the input then the resulting array
     * has just one element, namely this string.
     * <p>
     * <p> When there is a positive-width match at the beginning of this
     * string then an empty leading substring is included at the beginning
     * of the resulting array. A zero-width match at the beginning however
     * never produces such empty leading substring.
     * <p>
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
     * <p>
     * <p> The string {@code "boo:and:foo"}, for example, yields the
     * following results with these parameters:
     * <p>
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
     * <p>
     * <p> An invocation of this method of the form
     * <i>str.</i>{@code split(}<i>regex</i>{@code ,}&nbsp;<i>n</i>{@code )}
     * yields the same result as the expression
     * <p>
     * <blockquote>
     * <code>
     * {@link Pattern}.{@link
     * Pattern#compile compile}(<i>regex</i>).{@link
     * Pattern#split(CharSequence, int) split}(<i>str</i>,&nbsp;<i>n</i>)
     * </code>
     * </blockquote>
     *
     * @param regex the delimiting regular expression
     * @param limit the result threshold, as described above
     * @return the Seq of strings computed by splitting this string
     * around matches of the given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see Pattern
     */
    public Seq<CharSeq> split(String regex, int limit) {
        final Seq<String> split = Array.wrap(toString().split(regex, limit));
        return split.map(CharSeq::of);
    }

    /**
     * Splits this string around matches of the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p>
     * <p> This method works as if by invoking the two-argument {@link
     * #split(String, int) split} method with the given expression and a limit
     * argument of zero.  Trailing empty strings are therefore not included in
     * the resulting {@link javaslang.collection.Seq}.
     * <p>
     * <p> The string {@code "boo:and:foo"}, for example, yields the following
     * results with these expressions:
     * <p>
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
     * @return the Seq of strings computed by splitting this string
     * around matches of the given regular expression
     * @throws PatternSyntaxException if the regular expression's syntax is invalid
     * @see Pattern
     */
    public Seq<CharSeq> split(String regex) {
        return split(regex, 0);
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to lower
     * case using the rules of the given {@code Locale}.  Case mapping is based
     * on the Unicode Standard version specified by the {@link Character Character}
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
     * @see String#toLowerCase()
     * @see String#toUpperCase()
     * @see String#toUpperCase(Locale)
     */
    public CharSeq toLowerCase(Locale locale) {
        return of(toString().toLowerCase(locale));
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
     * @see String#toLowerCase(Locale)
     */
    public CharSeq toLowerCase() {
        return from(delegate.map(Character::toLowerCase));
    }

    /**
     * Converts all of the characters in this {@code CharSeq} to upper
     * case using the rules of the given {@code Locale}. Case mapping is based
     * on the Unicode Standard version specified by the {@link Character Character}
     * class. Since case mappings are not always 1:1 char mappings, the resulting
     * {@code CharSeq} may be a different length than the original {@code CharSeq}.
     * <p>
     * Examples of locale-sensitive and 1:M case mappings are in the following table.
     * <p>
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
     * @see String#toUpperCase()
     * @see String#toLowerCase()
     * @see String#toLowerCase(Locale)
     */
    public CharSeq toUpperCase(Locale locale) {
        return of(toString().toUpperCase(locale));
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
     * @see String#toUpperCase(Locale)
     */
    public CharSeq toUpperCase() {
        return from(delegate.map(Character::toUpperCase));
    }

    /**
     * Returns a string whose value is this {@code CharSeq}Seq, with any leading and trailing
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
        return trimLeft().trimRight();
    }

    /**
     * Returns a string whose value is this {@code CharSeq}, with any leading whitespace removed.
     */
    public CharSeq trimLeft() {
        return from(delegate.dropWhile(Character::isWhitespace));
    }

    /**
     * Returns a string whose value is this {@code CharSeq}, with any trailing whitespace removed.
     */
    public CharSeq trimRight() {
        return from(delegate.dropRightWhile(Character::isWhitespace));
    }

    /**
     * Converts this string to a new character array.
     *
     * @return a newly allocated character array whose length is the length
     * of this string and whose contents are initialized to contain
     * the character sequence represented by this string.
     */
    public char[] toCharArray() {
        return toString().toCharArray();
    }

    @FunctionalInterface
    public interface CharUnaryOperator {
        char apply(char c);
    }

    @FunctionalInterface
    public interface CharFunction<R> {
        R apply(char c);
    }
}
