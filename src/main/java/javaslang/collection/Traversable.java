/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.algebra.HigherKinded;
import javaslang.algebra.Monad;
import javaslang.algebra.Monoid;
import javaslang.control.Match;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.*;

/**
 * <p>An interface for inherently recursive data structures. The order of elements is determined by
 * {@link java.lang.Iterable#iterator()}, which may vary each time it is called.</p>
 * <p>Conversion:</p>
 * <ul>
 * <li>{@link #toJavaArray(Class)}</li>
 * <li>{@link #toJavaList()}</li>
 * <li>{@link #toJavaMap(Function)}</li>
 * <li>{@link #toJavaSet()}</li>
 * <li>{@link #toJavaStream()}</li>
 * </ul>
 * <p>Basic operations:</p>
 * <ul>
 * <li>{@link #clear()}</li>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #containsAll(Iterable)}</li>
 * <li>{@link #head()}</li>
 * <li>{@link #init()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #last()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #tail()}</li>
 * </ul>
 * <p>Filtering</p>
 * <ul>
 * <li>{@link #filter(Predicate)}</li>
 * <li>{@link #remove(Object)}</li>
 * <li>{@link #removeAll(Object)}</li>
 * <li>{@link #removeAll(Iterable)}</li>
 * <li>{@link #retainAll(Iterable)}</li>
 * </ul>
 * <p>Numeric operations:</p>
 * <ul>
 * <li>{@link #average()}</li>
 * <li>{@link #max()}</li>
 * <li>{@link #maxBy(Comparator)}</li>
 * <li>{@link #min()}</li>
 * <li>{@link #minBy(Comparator)}</li>
 * <li>{@link #product()}</li>
 * <li>{@link #sum()}</li>
 * </ul>
 * <p>Reduction:</p>
 * <ul>
 * <li>{@link #fold(Object, BiFunction)}</li>
 * <li>{@link #foldLeft(Object, BiFunction)}</li>
 * <li>{@link #foldRight(Object, BiFunction)}</li>
 * <li>{@link #foldMap(javaslang.algebra.Monoid, Function)}</li>
 * <li>{@link #join()}</li>
 * <li>{@link #join(CharSequence)}</li>
 * <li>{@link #join(CharSequence, CharSequence, CharSequence)}</li>
 * <li>{@link #reduce(BiFunction)}</li>
 * <li>{@link #reduceLeft(BiFunction)}</li>
 * <li>{@link #reduceRight(BiFunction)}</li>
 * </ul>
 * <p>Selection:</p>
 * <ul>
 * <li>{@link #drop(int)}</li>
 * <li>{@link #dropRight(int)}</li>
 * <li>{@link #dropWhile(Predicate)}</li>
 * <li>{@link #findAll(Predicate)}</li>
 * <li>{@link #findFirst(Predicate)}</li>
 * <li>{@link #findLast(Predicate)}</li>
 * <li>{@link #take(int)}</li>
 * <li>{@link #takeRight(int)}</li>
 * <li>{@link #takeWhile(Predicate)}</li>
 * </ul>
 * <p>Side-effects:</p>
 * <ul>
 * <li>{@link #forEach(Consumer)}</li>
 * <li>{@link #peek(Consumer)}</li>
 * <li>{@link #stderr()}</li>
 * <li>{@link #stdout()}</li>
 * </ul>
 * <p>Tests:</p>
 * <ul>
 * <li>{@link #exists(Predicate)}</li>
 * <li>{@link #existsUnique(Predicate)}</li>
 * <li>{@link #forAll(Predicate)}</li>
 * </ul>
 * <p>Transformation:</p>
 * <ul>
 * <li>{@link #combinations()}</li>
 * <li>{@link #combinations(int)}</li>
 * <li>{@link #distinct()}</li>
 * <li>{@link #distinct(Function)}</li>
 * <li>{@link #flatMap(Function)}</li>
 * <li>{@link #flatten()}</li>
 * <li>{@link #flatten(Function)}</li>
 * <li>{@link #grouped(int)}</li>
 * <li>TODO(#110): #groupBy</li>
 * <li>{@link #intersperse(Object)}</li>
 * <li>{@link #map(Function)}</li>
 * <li>TODO(#110): #partition (generalization of groupBy)</li>
 * <li>{@link #replace(Object, Object)}</li>
 * <li>{@link #replaceAll(Object, Object)}</li>
 * <li>{@link #replaceAll(UnaryOperator)}</li>
 * <li>{@link #reverse()}</li>
 * <li>{@link #sliding(int)}</li>
 * <li>{@link #sliding(int, int)}</li>
 * <li>{@link #span(Predicate)}</li>
 * <li>{@link #unzip(Function)}</li>
 * <li>{@link #zip(Iterable)}</li>
 * <li>{@link #zipAll(Iterable, Object, Object)}</li>
 * <li>{@link #zipWithIndex()}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 1.1.0
 */
public interface Traversable<T> extends Iterable<T>, Monad<T, Traversable<?>> {

    /**
     * Returns a Traversable based on an Iterable. Returns the given Iterable, if it is already a Traversable,
     * otherwise {@link Stream#of(Iterable)}.
     *
     * @param iterable An Iterable.
     * @param <T>      Component type
     * @return a Traversable
     */
    static <T> Traversable<T> of(Iterable<? extends T> iterable) {
        Objects.requireNonNull(iterable, "iterable is null");
        if (iterable instanceof Traversable) {
            @SuppressWarnings("unchecked")
            final Traversable<T> traversable = (Traversable<T>) iterable;
            return traversable;
        } else {
            return Stream.of(iterable);
        }
    }

    /**
     * <p>Calculates the average of this elements by computing the arithmetic mean.</p>
     * <p>Supported component types are boolean, byte, char, double, float, int, long, short, BigInteger, BigDecimal.</p>
     * <p>The arithmetic mean of boolean is defined here to be {@code this.filter(Boolean::booleanValue).length() >= n / 2)}.</p>
     * <p>In order to preserve summation results, the value space is expanded as follows:</p>
     * <ul>
     * <li><strong>element type, summation type</strong></li>
     * <li>byte, long</li>
     * <li>char, int</li>
     * <li>double, double</li>
     * <li>float, double</li>
     * <li>int, long</li>
     * <li>short, int</li>
     * </ul>
     * <p>BigInteger and BigDecimal are not treated special.</p>
     *
     * @return the average of this elements.
     * @throws java.lang.UnsupportedOperationException if no elements are present or the elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default T average() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("average of nothing");
        } else {
            final T head = head();
            final int n = length();
            return Match
                    .<T>caze((boolean t) -> (T) (Boolean) (((Traversable<Boolean>) this).filter(Boolean::booleanValue).length() >= n / 2))
                    .caze((byte t) -> (T) (Byte) (byte) (((Traversable<Byte>) this).foldLeft(0, (i, j) -> i + j) / n))
                    .caze((char t) -> (T) (Character) (char) (((Traversable<Character>) this).foldLeft(0, (i, j) -> i + j) / n))
                    .caze((double t) -> (T) (Double) (((Traversable<Double>) this).foldLeft(0d, (i, j) -> i + j) / n))
                    .caze((float t) -> (T) (Float) (float) (((Traversable<Float>) this).foldLeft(0d, (i, j) -> i + j) / n))
                    .caze((int t) -> (T) (Integer) (int) (((Traversable<Integer>) this).foldLeft(0L, (i, j) -> i + j) / n))
                    .caze((long t) -> (T) (Long) (((Traversable<Long>) this).foldLeft(0L, (i, j) -> i + j) / n))
                    .caze((short t) -> (T) (Short) (short) (((Traversable<Short>) this).foldLeft(0, (i, j) -> i + j) / n))
                    .caze((BigInteger t) -> (T) ((Traversable<BigInteger>) this).reduce(BigInteger::add).divide(BigInteger.valueOf(n)))
                    .caze((BigDecimal t) -> {
                        final Traversable<BigDecimal> traversable = (Traversable<BigDecimal>) this;
                        final int scale = traversable.map(BigDecimal::scale).max();
                        return (T) traversable.reduce(BigDecimal::add).divide(BigDecimal.valueOf(n), scale + 1, BigDecimal.ROUND_HALF_EVEN);
                    })
                    .orElse(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head);
        }
    }

    /**
     * Returns an empty version of this traversable, i.e. {@code this.clear().isEmpty() == true}.
     *
     * @return an empty Traversable.
     */
    Traversable<T> clear();

    /**
     * Returns the union of all combinations from k = 0 to length().
     * <p>
     * Examples:
     * <pre>
     * <code>
     * [].combinations() = [[]]
     *
     * [1,2,3].combinations() = [
     *   [],                  // k = 0
     *   [1], [2], [3],       // k = 1
     *   [1,2], [1,3], [2,3], // k = 2
     *   [1,2,3]              // k = 3
     * ]
     * </code>
     * </pre>
     *
     * @return the combinations of this
     */
    Traversable<? extends Traversable<T>> combinations();

    /**
     * Returns the k-combination of this traversable, i.e. all subset of this of k distinct elements.
     *
     * @param k Size of subsets
     * @return the k-combination of this elements
     * @see <a href="http://en.wikipedia.org/wiki/Combination">Combination</a>
     */
    Traversable<? extends Traversable<T>> combinations(int k);

    /**
     * Tests if this Traversable contains a given value.
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is in this Traversable, false otherwise.
     */
    default boolean contains(T element) {
        return findFirst(e -> java.util.Objects.equals(e, element)).isDefined();
    }

    /**
     * <p>
     * Tests if this Traversable contains all given elements.
     * </p>
     * <p>
     * The result is equivalent to
     * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())} but implemented
     * without recursion.
     * </p>
     *
     * @param elements A List of values of type E.
     * @return true, if this List contains all given elements, false otherwise.
     * @throws NullPointerException if elements is null
     */
    default boolean containsAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return List.of(elements)
                .distinct()
                .findFirst(e -> !this.contains(e))
                .isEmpty();
    }

    /**
     * Returns a new version of this which contains no duplicates. Elements are compared using {@code equals}.
     *
     * @return a new {@code Traversable} containing this elements without duplicates
     */
    Traversable<T> distinct();

    /**
     * Returns a new version of this which contains no duplicates. Elements mapped to keys which are compared using
     * {@code equals}.
     * <p>
     * The elements of the result are determined in the order of their occurrence - first match wins.
     *
     * @param keyExtractor A key extractor
     * @param <U> key type
     * @return a new {@code Traversable} containing this elements without duplicates
     */
    <U> Traversable<T> distinct(Function<? super T, ? extends U> keyExtractor);

    /**
     * Drops the first n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the first n ones, or else the empty instance,
     * if this has less than n elements.
     */
    Traversable<T> drop(int n);

    /**
     * Drops the last n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the last n ones, or else the empty instance,
     * if this has less than n elements.
     */
    Traversable<T> dropRight(int n);

    /**
     * Drops elements while the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for this elements starting with the first.
     * @return a new instance consisting of all elements starting from the first one which does not satisfy the
     * given predicate.
     */
    Traversable<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Returns a new traversable consisting of all elements of this which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new traversable
     */
    Traversable<T> filter(Predicate<? super T> predicate);

    /**
     * Essentially the same as {@link #filter(Predicate)} but the result type may differ,
     * i.e. tree.findAll() may be a List.
     *
     * @param predicate A predicate.
     * @return all elements of this which satisfy the given predicate.
     */
    Traversable<T> findAll(Predicate<? super T> predicate);

    /**
     * Returns the first element of this which satisfies the given predicate.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     */
    default Option<T> findFirst(Predicate<? super T> predicate) {
        for (T a : this) {
            if (predicate.test(a)) {
                return new Some<>(a); // may be Some(null)
            }
        }
        return Option.none();
    }

    /**
     * <p>
     * Returns the last element of this which satisfies the given predicate.
     * </p>
     * <p>
     * Same as {@code reverse().findFirst(predicate)}.
     * </p>
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     */
    default Option<T> findLast(Predicate<? super T> predicate) {
        return reverse().findFirst(predicate);
    }

    @Override
    <U, TRAVERSABLE extends HigherKinded<U, Traversable<?>>> Traversable<U> flatMap(Function<? super T, ? extends TRAVERSABLE> mapper);

    /**
     * <p>Flattens this Traversable, i.e. unboxes iterable elements. If this Traversable contains elements and
     * iterables of elements of the same type, flattening works as expected.</p>
     * Examples:
     * <ul>
     * <li>{@code List.of(1, 2, 3).flatten() = List.of(1, 2, 3)}</li>
     * <li>{@code List.of(List.of(1), Stream.of(2, 3)).flatten() = List.of(1, 2, 3)}</li>
     * <li>{@code List.of(1, List.of(2), Stream.of(3, 4)).flatten() = List.of(1, 2, 3, 4)}</li>
     * </ul>
     *
     * @param <U> new component type (!!UNSAFE!! {@code of(1, 2, 3).map(Object::toString).<Integer> flatten()})
     * @return a flattened version of this traversable
     */
    <U> Traversable<U> flatten();

    /**
     * <p>Flattens this Traversable using a function as hint how to obtain iterable elements.</p>
     * Examples:
     * <ul>
     * <li>{@code List.of(1, 2, 3).flatten(i -> List.of(i)) = List.of(1, 2, 3)}</li>
     * <li>{@code List.of(List.of(1), Stream.of(2, 3)).flatten(Function.identity()) = List.of(1, 2, 3)}</li>
     * <li>
     * <pre><code>List.of(1, List.of(2), Stream.of(3, 4))
     *     .flatten(x -&gt; Match
     *         .caze((Iterable&lt;Integer&gt; ys) -&gt; ys)
     *         .caze((Integer i) -&gt; List.of(i))
     *         .apply(x)
     *     ) = List.of(1, 2, 3, 4)}</code></pre>
     * </li>
     * </ul>
     *
     * @param <U> new component type (UNSAFE!)
     * @param f   An unboxing function that maps elements T to Iterables of elements U that will be unboxed.
     * @return a flattened version of this traversable
     */
    <U, TRAVERSABLE extends HigherKinded<U, Traversable<?>>> Traversable<U> flatten(Function<? super T, ? extends TRAVERSABLE> f);

    /**
     * <p>
     * Accumulates the elements of this Traversable by successively calling the given operator {@code op}.
     * </p>
     * <p>
     * Example: {@code List("a", "b", "c").fold("", (a, b) -> a + b) = "abc"}
     * </p>
     *
     * @param zero Value to start the accumulation with.
     * @param op   The accumulator operator.
     * @return an accumulated version of this.
     */
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> op) {
        return foldLeft(zero, op);
    }

    /**
     * <p>
     * Accumulates the elements of this Traversable by successively calling the given function {@code f} from the left,
     * starting with a value {@code zero} of type B.
     * </p>
     * <p>
     * Example: {@code List.of("a", "b", "c").foldLeft("", (xs, x) -> xs + x) = "abc"}
     * </p>
     *
     * @param zero Value to start the accumulation with.
     * @param f    The accumulator function.
     * @param <U>  Result type of the accumulator.
     * @return an accumulated version of this.
     */
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> f) {
        Objects.requireNonNull(f, "function is null");
        U xs = zero;
        for (T x : this) {
            xs = f.apply(xs, x);
        }
        return xs;
    }

    /**
     * Maps this elements to a Monoid and applies foldLeft, starting with monoid.zero():
     * <pre>
     * <code>foldLeft(monoid.zero(), (ys, x) -&gt; monoid.combine(ys, mapper.apply(x)))</code>
     * </pre>
     *
     * @param monoid A Monoid
     * @param mapper A mapper
     * @param <U>    Component type of the given monoid.
     * @return the folded monoid value.
     * @throws java.lang.NullPointerException if monoid or mapper is null
     */
    default <U> U foldMap(Monoid<U> monoid, Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(monoid, "monoid is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(monoid.zero(), (ys, x) -> monoid.combine(ys, mapper.apply(x)));
    }

    /**
     * <p>
     * Accumulates the elements of this Traversable by successively calling the given function {@code f} from the right,
     * starting with a value {@code zero} of type B.
     * </p>
     * <p>
     * Example: {@code List.of("a", "b", "c").foldRight("", (x, xs) -> x + xs) = "abc"}
     * </p>
     * <p>
     * In order to prevent recursive calls, foldRight is implemented based on reverse and foldLeft. A recursive variant
     * is based on foldMap, using the monoid of function composition (endo monoid).
     * </p>
     * <pre>
     * <code>
     * foldRight = reverse().foldLeft(zero, (b, a) -&gt; f.apply(a, b));
     * foldRight = foldMap(Algebra.Monoid.endoMonoid(), a -&gt; b -&gt; f.apply(a, b)).apply(zero);
     * </code>
     * </pre>
     *
     * @param zero Value to start the accumulation with.
     * @param f    The accumulator function.
     * @param <U>  Result type of the accumulator.
     * @return an accumulated version of this.
     * @throws java.lang.NullPointerException if f is null
     */
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "function is null");
        return reverse().foldLeft(zero, (xs, x) -> f.apply(x, xs));
    }

    /**
     * Calls {@code Iterable.super.forEach(action)}.
     *
     * @param action A Consumer
     */
    @Override
    default void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    /**
     * Checks, if at least one element exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for an element of this, false otherwise
     */
    default boolean exists(Predicate<? super T> predicate) {
        for (T t : this) {
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks, if a unique elements exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for a unique element of this, false otherwise
     */
    default boolean existsUnique(Predicate<? super T> predicate) {
        boolean exists = false;
        for (T t : this) {
            if (predicate.test(t)) {
                if (exists) {
                    return false;
                } else {
                    exists = true;
                }
            }
        }
        return exists;
    }

    /**
     * Checks, if the given predicate holds for all elements of this.
     *
     * @param predicate A Predicate
     * @return true, if the predicate holds for all elements of this, false otherwise
     */
    default boolean forAll(Predicate<? super T> predicate) {
        for (T t : this) {
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Groups this {@code Traversable} into fixed size blocks like so:
     * <ul>
     * <li>If {@code this.isEmpty()}, the resulting {@code Traversable} is empty.</li>
     * <li>If {@code size <= this.length()}, the resulting {@code Traversable} will contain {@code this.length() / size}
     * blocks of size {@code size} and maybe a non-empty block of size {@code this.length() % size}, if there are
     * remaining elements.</li>
     * <li>If {@code size > this.length()}, the resulting {@code Traversable} will contain one block of size
     * {@code this.length()}.</li>
     * </ul>
     * Examples:
     * <pre>
     * <code>
     * [].grouped(1) = []
     * [].grouped(0) throws
     * [].grouped(-1) throws
     * [1,2,3,4].grouped(2) = [[1,2],[3,4]]
     * [1,2,3,4,5].grouped(2) = [[1,2],[3,4],[5]]
     * [1,2,3,4].grouped(5) = [[1,2,3,4]]
     * </code>
     * </pre>
     *
     * Please note that {@code grouped(int)} is a special case of {@linkplain #sliding(int, int)}, i.e.
     * {@code grouped(size)} is the same as {@code sliding(size, size)}.
     *
     * @param size a positive block size
     * @return A new Traversable of sliced blocks of the given size
     * @throws IllegalArgumentException if size is negative or zero
     */
    Traversable<? extends Traversable<T>> grouped(int size);

    /**
     * Returns the first element of a non-empty Traversable.
     *
     * @return The first element of this Traversable.
     * @throws UnsupportedOperationException if this Traversable is empty
     */
    T head();

    /**
     * Dual of {@linkplain #tail()}, returning all elements except the last.
     *
     * @return a new instance containing all elements except the last.
     */
    Traversable<T> init();

    /**
     * Inserts an element between all elements of this Traversable.
     *
     * @param element An element.
     * @return an interspersed version of this
     */
    Traversable<T> intersperse(T element);

    /**
     * Checks if this Traversable is empty.
     *
     * @return true, if this Traversable contains no elements, falso otherwise.
     */
    boolean isEmpty();

    /**
     * Joins the elements of this by concatenating their string representations.
     * <p>
     * This has the same effect as calling {@code join("", "", "")}.
     *
     * @return a new String
     */
    default String join() {
        return join("", "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter.
     * <p>
     * This has the same effect as calling {@code join(delimiter, "", "")}.
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @return A new String
     */
    default String join(CharSequence delimiter) {
        return join(delimiter, "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter, prefix and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").join(", ", "Chars(", ")") = "Chars(a, b, c)"}
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @param prefix    prefix of the resulting string
     * @param suffix    suffix of the resulting string
     * @return a new String
     */
    default String join(CharSequence delimiter,
                        CharSequence prefix,
                        CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        map(String::valueOf).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }

    /**
     * Dual of {@linkplain #head()}, returning the last element.
     *
     * @return the last element.
     * @throws UnsupportedOperationException is this is empty
     */
    default T last() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("last of empty Traversable");
        } else {
            Traversable<T> traversable = this;
            { // don't let escape tail
                Traversable<T> tail;
                while (!(tail = traversable.tail()).isEmpty()) {
                    traversable = tail;
                }
            }
            return traversable.head();
        }
    }

    /**
     * Computes the number of elements of this.
     *
     * @return the number of elements
     */
    default int length() {
        return foldLeft(0, (n, ignored) -> n + 1);
    }

    /**
     * Maps the elements of this traversable to elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the target Traversable
     * @return a mapped Traversable
     */
    @Override
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    /**
     * <p>Calculates the maximum of this elements.</p>
     * <p>Supported component types are boolean, byte, char, double, float, int, long, short, BigInteger, BigDecimal.</p>
     * <p>If the component type is boolean, the maximum is defined to be {@code this.reduce((i, j) -> i || j)}.</p>
     *
     * @return the maximum of this elements.
     * @throws java.lang.UnsupportedOperationException if no elements are present or the elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default T max() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("max of nothing");
        } else {
            final T head = head();
            return Match
                    .<T>caze((boolean t) -> (T) ((Traversable<Boolean>) this).reduce((i, j) -> i || j))
                    .caze((byte t) -> (T) ((Traversable<Byte>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((char t) -> (T) ((Traversable<Character>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((double t) -> (T) ((Traversable<Double>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((float t) -> (T) ((Traversable<Float>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((int t) -> (T) ((Traversable<Integer>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((long t) -> (T) ((Traversable<Long>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((short t) -> (T) ((Traversable<Short>) this).reduce((i, j) -> (i > j) ? i : j))
                    .caze((BigInteger t) -> (T) ((Traversable<BigInteger>) this).reduce(BigInteger::max))
                    .caze((BigDecimal t) -> (T) ((Traversable<BigDecimal>) this).reduce(BigDecimal::max))
                    .orElse(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head);
        }
    }

    /**
     * <p>Calculates the maximum of this elements.</p>
     *
     * @param comparator A non-null element comparator
     * @return the maximum of this elements.
     * @throws java.lang.NullPointerException          if comparator is null
     * @throws java.lang.UnsupportedOperationException if no elements are present
     */
    default T maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("maxBy of nothing");
        }
        return reduce((t1, t2) -> comparator.compare(t1, t2) >= 0 ? t1 : t2);
    }

    /**
     * Calculates the minimum of this elements.
     * <p>Supported component types are boolean, byte, char, double, float, int, long, short, BigInteger, BigDecimal.</p>
     * <p>If the component type is boolean, the minimum is defined to be {@code this.reduce((i, j) -> i && j)}.</p>
     *
     * @return the minimum of this elements.
     * @throws java.lang.UnsupportedOperationException if no elements are present or the elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default T min() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("min of nothing");
        } else {
            final T head = head();
            return Match
                    .<T>caze((boolean t) -> (T) ((Traversable<Boolean>) this).reduce((i, j) -> i && j))
                    .caze((byte t) -> (T) ((Traversable<Byte>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((char t) -> (T) ((Traversable<Character>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((double t) -> (T) ((Traversable<Double>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((float t) -> (T) ((Traversable<Float>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((int t) -> (T) ((Traversable<Integer>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((long t) -> (T) ((Traversable<Long>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((short t) -> (T) ((Traversable<Short>) this).reduce((i, j) -> (i < j) ? i : j))
                    .caze((BigInteger t) -> (T) ((Traversable<BigInteger>) this).reduce(BigInteger::min))
                    .caze((BigDecimal t) -> (T) ((Traversable<BigDecimal>) this).reduce(BigDecimal::min))
                    .orElse(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head);
        }
    }

    /**
     * <p>Calculates the minimum of this elements.</p>
     *
     * @param comparator A non-null element comparator
     * @return the minimum of this elements.
     * @throws java.lang.NullPointerException          if comparator is null
     * @throws java.lang.UnsupportedOperationException if no elements are present
     */
    default T minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("minBy of nothing");
        }
        return reduce((t1, t2) -> comparator.compare(t1, t2) <= 0 ? t1 : t2);
    }

    @Override
    Traversable<T> peek(Consumer<? super T> action);

    /**
     * <p>Calculates the product of this elements.</p>
     * <p>Supported component types are boolean, byte, char, double, float, int, long, short, BigInteger, BigDecimal.</p>
     * <p>If b1 and b2 are of type boolean, {@code b1 * b2 := b1 &amp;&amp; b2}.</p>
     * <p>The product operation is applied to this elements according to the Java Language Secification
     * <a href="http://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.6.2">Chapter 5. Conversions and Contexts</a>.
     * The resulting value is converted to the component type of this Traversable.</p>
     *
     * @return the product of this elements.
     * @throws java.lang.UnsupportedOperationException if no elements are present or the elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default T product() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("product of nothing");
        } else {
            T head = head();
            return Match
                    .<T>caze((boolean t) -> (T) ((Traversable<Boolean>) this).reduce((i, j) -> i && j))
                    .caze((byte t) -> (T) Byte.valueOf(((Traversable<Byte>) this).foldLeft(1, (i, j) -> i * j).byteValue()))
                    .caze((char t) -> (T) Character.valueOf((char) ((Traversable<Character>) this).foldLeft(1, (i, j) -> i * j).shortValue()))
                    .caze((double t) -> (T) ((Traversable<Double>) this).reduce((i, j) -> i * j))
                    .caze((float t) -> (T) ((Traversable<Float>) this).reduce((i, j) -> i * j))
                    .caze((int t) -> (T) ((Traversable<Integer>) this).reduce((i, j) -> i * j))
                    .caze((long t) -> (T) ((Traversable<Long>) this).reduce((i, j) -> i * j))
                    .caze((short t) -> (T) Short.valueOf(((Traversable<Short>) this).foldLeft(1, (i, j) -> i * j).shortValue()))
                    .caze((BigInteger t) -> (T) ((Traversable<BigInteger>) this).reduce(BigInteger::multiply))
                    .caze((BigDecimal t) -> (T) ((Traversable<BigDecimal>) this).reduce(BigDecimal::multiply))
                    .orElse(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head);
        }
    }

    /**
     * Removes the first occurrence of the given element.
     *
     * @param element An element to be removed from this Traversable.
     * @return a Traversable containing all elements of this without the first occurrence of the given element.
     */
    Traversable<T> remove(T element);


    /**
     * Removes all occurrences of the given element.
     *
     * @param element An element to be removed from this Traversable.
     * @return a Traversable containing all elements of this but not the given element.
     */
    Traversable<T> removeAll(T element);

    /**
     * Removes all occurrences of the given elements.
     *
     * @param elements Elements to be removed from this Traversable.
     * @return a Traversable containing all elements of this but none of the given elements.
     */
    Traversable<T> removeAll(Iterable<? extends T> elements);

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws UnsupportedOperationException  if this Traversable is empty
     * @throws java.lang.NullPointerException if op is null
     */
    default T reduce(BiFunction<? super T, ? super T, ? extends T> op) {
        return reduceLeft(op);
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws UnsupportedOperationException  if this Traversable is empty
     * @throws java.lang.NullPointerException if op is null
     */
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "operator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceLeft on Nil");
        } else {
            return tail().foldLeft(head(), op);
        }
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws UnsupportedOperationException  if this Traversable is empty
     * @throws java.lang.NullPointerException if op is null
     */
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "operator is null");
        if (isEmpty()) {
            throw new UnsupportedOperationException("reduceRight on empty List");
        } else {
            final Traversable<T> reversed = reverse();
            return reversed.tail().foldLeft(reversed.head(), (xs, x) -> op.apply(x, xs));
        }
    }

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a Traversable containing all elements of this where the first occurrence of currentElement is replaced with newELement.
     */
    Traversable<T> replace(T currentElement, T newElement);

    /**
     * Replaces all occurrences of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a Traversable containing all elements of this where all occurrences of currentElement are replaced with newELement.
     */
    Traversable<T> replaceAll(T currentElement, T newElement);

    /**
     * Replaces all occurrences of this Traversable by applying the given operator to the elements, which is
     * essentially a special case of {@link #map(Function)}.
     *
     * @param operator An operator.
     * @return a Traversable containing all elements of this transformed within the same domain.
     */
    Traversable<T> replaceAll(UnaryOperator<T> operator);

    /**
     * Keeps all occurrences of the given elements from this.
     *
     * @param elements Elements to be kept.
     * @return a Traversable containing all occurreces of the given elements.
     */
    Traversable<T> retainAll(Iterable<? extends T> elements);

    /**
     * Reverses the order of elements.
     *
     * @return the reversed elements.
     */
    Traversable<T> reverse();

    /**
     * Slides a window of a specific {@code size} and step size 1 over this {@code Traversable} by calling
     * {@link #sliding(int, int)}.
     *
     * @param size a positive window size
     * @return a new Traversable of windows of a specific size using step size 1
     * @throws IllegalArgumentException if size is negative or zero
     */
    Traversable<? extends Traversable<T>> sliding(int size);

    /**
     * Slides a window of a specific {@code size} and {@code step} size over this {@code Traversable}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * [].sliding(1,1) = []
     * [1,2,3,4,5].sliding(2,3) = [[1,2],[4,5]]
     * [1,2,3,4,5].sliding(2,4) = [[1,2],[5]]
     * [1,2,3,4,5].sliding(2,5) = [[1,2]]
     * [1,2,3,4].sliding(5,3) = [[1,2,3,4],[4]]
     * </code>
     * </pre>
     *
     * @param size a positive window size
     * @param step a positive step size
     * @return a new Traversable of windows of a specific size using a specific step size
     * @throws IllegalArgumentException if size or step are negative or zero
     */
    Traversable<? extends Traversable<T>> sliding(int size, int step);

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return a Tuple containing the longest prefix of elements that satisfy p and the remainder.
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    /**
     * <p>Calculates the sum of this elements.</p>
     * <p>Supported component types are boolean, byte, char, double, float, int, long, short, BigInteger, BigDecimal.</p>
     * <p>If b1 and b2 are of type boolean, {@code b1 + b2 := b1 || b2}.</p>
     * <p>The sum operation is applied to this elements according to the Java Language Secification
     * <a href="http://docs.oracle.com/javase/specs/jls/se8/html/jls-5.html#jls-5.6.2">Chapter 5. Conversions and Contexts</a>.
     * The resulting value is converted to the component type of this Traversable.</p>
     *
     * @return the sum of this elements.
     * @throws java.lang.UnsupportedOperationException if no elements are present or the elements are not numeric
     */
    @SuppressWarnings({"unchecked"})
    default T sum() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("sum of nothing");
        } else {
            T head = head();
            return Match
                    .<T>caze((boolean t) -> (T) ((Traversable<Boolean>) this).reduce((i, j) -> i || j))
                    .caze((byte t) -> (T) Byte.valueOf(((Traversable<Byte>) this).foldLeft(0, (i, j) -> i + j).byteValue()))
                    .caze((char t) -> (T) Character.valueOf((char) ((Traversable<Character>) this).foldLeft(0, (i, j) -> i + j).shortValue()))
                    .caze((double t) -> (T) ((Traversable<Double>) this).reduce((i, j) -> i + j))
                    .caze((float t) -> (T) ((Traversable<Float>) this).reduce((i, j) -> i + j))
                    .caze((int t) -> (T) ((Traversable<Integer>) this).reduce((i, j) -> i + j))
                    .caze((long t) -> (T) ((Traversable<Long>) this).reduce((i, j) -> i + j))
                    .caze((short t) -> (T) Short.valueOf(((Traversable<Short>) this).foldLeft(0, (i, j) -> i + j).shortValue()))
                    .caze((BigInteger t) -> (T) ((Traversable<BigInteger>) this).reduce(BigInteger::add))
                    .caze((BigDecimal t) -> (T) ((Traversable<BigDecimal>) this).reduce(BigDecimal::add))
                    .orElse(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head);
        }
    }

    /**
     * Sends the string representations of these elements to the sandard error stream {@linkplain System#err},
     * each in a new line.
     */
    default void stderr() {
        try (PrintStream writer = System.err) {
            for (T t : this) {
                writer.println(String.valueOf(t));
                if (writer.checkError()) {
                    throw new IllegalStateException("Error writing to stderr");
                }
            }
        }
    }

    /**
     * Sends the string representations of these elements to the sandard output stream {@linkplain System#out},
     * each in a new line.
     */
    // TODO(#79): See also http://stackoverflow.com/questions/3643939/java-process-with-input-output-stream.
    @SuppressWarnings("InfiniteLoopStatement")
    default void stdout() {
        try (PrintStream writer = System.out) {
            for (T t : this) {
                writer.println(String.valueOf(t));
                if (writer.checkError()) {
                    throw new IllegalStateException("Error writing to stdout");
                }
            }
        }
    }

    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this Traversable is empty
     */
    Traversable<T> tail();

    /**
     * Takes the first n elements of this or all elements, if this length &lt; n.
     * <p>
     * The result is equivalent to {@code sublist(0, max(0, min(length(), n)))} but does not throw if {@code n < 0} or
     * {@code n > length()}.
     * <p>
     * In the case of {@code n < 0} the empty instance is returned, in the case of {@code n > length()} this is returned.
     *
     * @param n The number of elements to take.
     * @return A new instance consisting the first n elements of this or all elements, if this has less than n elements.
     */
    Traversable<T> take(int n);

    /**
     * Takes the last n elements of this or all elements, if this length &lt; n.
     * <p>
     * The result is equivalent to {@code sublist(max(0, min(length(), length() - n)), n)}, i.e. takeRight will not
     * throw if {@code n < 0} or {@code n > length()}.
     * <p>
     * In the case of {@code n < 0} the empty instance is returned, in the case of {@code n > length()} this is returned.
     *
     * @param n The number of elements to take.
     * @return A new instance consisting the first n elements of this or all elements, if this has less than n elements.
     */
    Traversable<T> takeRight(int n);

    /**
     * Takes elements while the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for this elements starting with the last.
     * @return a new instance consisting of all elements starting from the last one which does not satisfy the
     * given predicate.
     */
    Traversable<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Converts this to a Java array.
     * <p>
     * Tip: Given a {@code Traversable<M<T>> t} use {@code t.toJavaArray((Class<M<T>>) (Class) M.class)}.
     *
     * @param componentType Type of resulting array's elements.
     * @return a new array containing this elements
     */
    default T[] toJavaArray(Class<T> componentType) {
        final java.util.List<T> list = toJavaList();
        @SuppressWarnings("unchecked")
        final T[] array = list.toArray((T[]) java.lang.reflect.Array.newInstance(componentType, list.size()));
        return array;
    }

    /**
     * Converts this to a {@link java.util.List}.
     *
     * @return a new {@linkplain java.util.ArrayList} containing this elements
     */
    default java.util.List<T> toJavaList() {
        final java.util.List<T> result = new java.util.ArrayList<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    /**
     * Converts this to a {@link java.util.Map} by converting this elements to key-value pairs.
     *
     * @param <K> key type
     * @param <V> value type
     * @param f   a function which converts elements of this to key-value pairs inserted into the resulting Map
     * @return a new {@linkplain java.util.HashMap} containing this key-value representations of this elements
     */
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, Tuple2<K, V>> f) {
        final java.util.Map<K, V> map = new java.util.HashMap<>();
        for (T a : this) {
            final Tuple2<K, V> entry = f.apply(a);
            map.put(entry._1, entry._2);
        }
        return map;
    }

    /**
     * Converts this to a {@link java.util.Set}.
     *
     * @return a new {@linkplain java.util.HashSet} containing this elements
     */
    default java.util.Set<T> toJavaSet() {
        final java.util.Set<T> result = new java.util.HashSet<>();
        for (T a : this) {
            result.add(a);
        }
        return result;
    }

    /**
     * Converts this to a sequential {@link java.util.stream.Stream} by calling {@code this.toJavaList().stream()}.
     *
     * @return a new {@linkplain java.util.stream.Stream} containing this elements
     */
    default java.util.stream.Stream<T> toJavaStream() {
        return toJavaList().stream();
    }

    /**
     * Unzips this elements by mapping this elements to pairs which are subsequentially split into to distinct
     * traversables.
     *
     * @param unzipper a function which converts elements of this to pairs
     * @param <T1>     1st element type of a pair returned by unzipper
     * @param <T2>     2nd element type of a pair returned by unzipper
     * @return A pair of traversables containing elements split by unzipper
     */
    <T1, T2> Tuple2<? extends Traversable<T1>, ? extends Traversable<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable collection by combining corresponding elements
     * in pairs. If one of the two Traversables is longer than the other, its remaining elements are ignored.
     *
     * @param <U>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new Traversable containing pairs consisting of corresponding elements of this list and that.
     * The length of the returned collection is the minimum of the lengths of this Traversable and that.
     * @throws java.lang.NullPointerException if that is null.
     */
    <U> Traversable<Tuple2<T, U>> zip(Iterable<U> that);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     *
     * @param <U>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this Traversable is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this Traversable.
     * @return A new Traversable containing pairs consisting of corresponding elements of this Traversable and that.
     * The length of the returned Traversable is the maximum of the lengths of this Traversable and that.
     * If this Traversable is shorter than that, thisElem values are used to fill the result.
     * If that is shorter than this Traversable, thatElem values are used to fill the result.
     * @throws java.lang.NullPointerException if that is null.
     */
    <U> Traversable<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    /**
     * Zips this List with its indices.
     *
     * @return A new List containing all elements of this List paired with their index, starting with 0.
     */
    Traversable<Tuple2<T, Integer>> zipWithIndex();
}
