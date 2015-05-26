/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Match;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.stream.StreamSupport;

/**
 * <p>An interface for inherently recursive data structures. The order of elements is determined by
 * {@link Iterable#iterator()}, which may vary each time it is called.</p>
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
 * <li>{@link #headOption()}</li>
 * <li>{@link #init()}</li>
 * <li>{@link #initOption()}</li>
 * <li>{@link #isEmpty()}</li>
 * <li>{@link #last()}</li>
 * <li>{@link #lastOption()}</li>
 * <li>{@link #length()}</li>
 * <li>{@link #tail()}</li>
 * <li>{@link #tailOption()}</li>
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
 * <li>{@link #flatten(Function)}</li>
 * <li>{@link #grouped(int)}</li>
 * <li>TODO(#110): #groupBy</li>
 * <li>{@link #intersperse(Object)}</li>
 * <li>{@link #map(Function)}</li>
 * <li>{@link #partition(Predicate)}</li>
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
public interface Traversable<T> extends TraversableOnce<T> {

    /**
     * Calculates the average of this elements. Returns {@code None} if this is empty, otherwise {@code Some(average)}.
     * Supported component types are {@code Byte}, {@code Double}, {@code Float}, {@code Integer}, {@code Long},
     * {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().average()              // = None
     * List.of(1, 2, 3).average()          // = Some(2.0)
     * List.of(0.1, 0.2, 0.3).average()    // = Some(0.2)
     * List.of("apple", "pear").average()  // throws
     * </code>
     * </pre>
     *
     * @return {@code Some(average)} or {@code None}, if there are no elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Option<Double> average() {
        if (isEmpty()) {
            return None.instance();
        } else {
            final OptionalDouble average = Match
                    .when((byte t) -> ((java.util.stream.Stream<Byte>) toJavaStream()).mapToInt(Number::intValue).average())
                    .when((double t) -> ((java.util.stream.Stream<Double>) toJavaStream()).mapToDouble(Number::doubleValue).average())
                    .when((float t) -> ((java.util.stream.Stream<Float>) toJavaStream()).mapToDouble(Number::doubleValue).average())
                    .when((int t) -> ((java.util.stream.Stream<Integer>) toJavaStream()).mapToInt(Number::intValue).average())
                    .when((long t) -> ((java.util.stream.Stream<Long>) toJavaStream()).mapToLong(Number::longValue).average())
                    .when((short t) -> ((java.util.stream.Stream<Short>) toJavaStream()).mapToInt(Number::intValue).average())
                    .when((BigInteger t) -> ((java.util.stream.Stream<BigInteger>) toJavaStream()).mapToLong(Number::longValue).average())
                    .when((BigDecimal t) -> ((java.util.stream.Stream<BigDecimal>) toJavaStream()).mapToDouble(Number::doubleValue).average())
                    .otherwise(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head());
            return new Some<>(average.getAsDouble());
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
     * @throws NullPointerException if {@code elements} is null
     */
    default boolean containsAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return List.ofAll(elements)
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
     * @param <U>          key type
     * @return a new {@code Traversable} containing this elements without duplicates
     * @throws NullPointerException if {@code keyExtractor} is null
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
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Returns a new traversable consisting of all elements of this which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new traversable
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> filter(Predicate<? super T> predicate);

    /**
     * Essentially the same as {@link #filter(Predicate)} but the result type may differ,
     * i.e. tree.findAll() may be a List.
     *
     * @param predicate A predicate.
     * @return all elements of this which satisfy the given predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> findAll(Predicate<? super T> predicate);

    /**
     * Returns the first element of this which satisfies the given predicate.
     *
     * @param predicate A predicate.
     * @return Some(element) or None, where element may be null (i.e. {@code List.of(null).findFirst(e -> e == null)}).
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> findFirst(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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
     * @throws NullPointerException if {@code predicate} is null
     */
    default Option<T> findLast(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return reverse().findFirst(predicate);
    }

    <U> Traversable<U> flatMap(Function<? super T, ? extends Iterable<U>> mapper);

    /**
     * Flattens a {@code Traversable} using a function.
     *
     * @param <U>           component type of the result {@code Traversable}
     * @param f             a function which maps elements of this Traversable to Traversables
     * @return a new {@code Traversable}
     * @throws NullPointerException if {@code f} is null
     */
    <U> Traversable<U> flatten(Function<? super T, ? extends Iterable<U>> f);

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
     * @throws NullPointerException if {@code op} is null
     */
    default T fold(T zero, BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
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
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U foldLeft(U zero, BiFunction<? super U, ? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        U xs = zero;
        for (T x : this) {
            xs = f.apply(xs, x);
        }
        return xs;
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
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return reverse().foldLeft(zero, (xs, x) -> f.apply(x, xs));
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
     * @throws IllegalArgumentException if {@code size} is negative or zero
     */
    Traversable<? extends Traversable<T>> grouped(int size);

    /**
     * Returns the first element of a non-empty Traversable.
     *
     * @return The first element of this Traversable.
     * @throws NoSuchElementException if this is empty
     */
    T head();

    /**
     * Returns the first element of a non-empty Traversable as {@code Option}.
     *
     * @return {@code Some(element)} or {@code None} if this is empty.
     */
    Option<T> headOption();

    /**
     * Dual of {@linkplain #tail()}, returning all elements except the last.
     *
     * @return a new instance containing all elements except the last.
     * @throws UnsupportedOperationException if this is empty
     */
    Traversable<T> init();

    /**
     * Dual of {@linkplain #tailOption()}, returning all elements except the last as {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends Traversable<T>> initOption();

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

    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {

            Traversable<T> traversable = Traversable.this;

            @Override
            public boolean hasNext() {
                return !traversable.isEmpty();
            }

            @Override
            public T next() {
                if (traversable.isEmpty()) {
                    throw new NoSuchElementException();
                } else {
                    final T result = traversable.head();
                    traversable = traversable.tail();
                    return result;
                }
            }
        };
    }

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
     * @throws NoSuchElementException is this is empty
     */
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of empty Traversable");
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
     * Dual of {@linkplain #headOption()}, returning the last element as {@code Opiton}.
     *
     * @return {@code Some(element)} or {@code None} if this is empty.
     */
    default Option<T> lastOption() {
        return isEmpty() ? None.instance() : new Some<>(last());
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
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Traversable<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Creates a partition of this {@code Traversable} by splitting this elements in two in distinct tarversables
     * according to a predicate.
     *
     * @param predicate A predicate which classifies an element if it is in the first or the second traversable.
     * @return A disjoint union of two traversables. The first {@code Traversable} contains all elements that satisfy the given {@code predicate}, the second {@code Traversable} contains all elements that don't. The original order of elements is preserved.
     * @throws NullPointerException if predicate is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> partition(Predicate<? super T> predicate);

    /**
     * Calculates the maximum of this elements according to their natural order.
     *
     * @return {@code Some(maximum)} of this elements or {@code None} if this is empty or this elements are not comparable
     */
    @SuppressWarnings("unchecked")
    default Option<T> max() {
        if (isEmpty() || !(head() instanceof Comparable)) {
            return None.instance();
        } else {
            return maxBy((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));
        }
    }

    /**
     * Calculates the maximum of this elements using a specific comparator.
     *
     * @param comparator A non-null element comparator
     * @return {@code Some(maximum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) >= 0 ? t1 : t2);
            return new Some<>(value);
        }
    }

    /**
     * Calculates the minimum of this elements according to their natural order.
     *
     * @return {@code Some(minimum)} of this elements or {@code None} if this is empty or this elements are not comparable
     */
    @SuppressWarnings("unchecked")
    default Option<T> min() {
        if (isEmpty() || !(head() instanceof Comparable)) {
            return None.instance();
        } else {
            return minBy((o1, o2) -> ((Comparable<T>) o1).compareTo(o2));
        }
    }

    /**
     * Calculates the minimum of this elements using a specific comparator.
     *
     * @param comparator A non-null element comparator
     * @return {@code Some(minimum)} of this elements or {@code None} if this is empty
     * @throws NullPointerException if {@code comparator} is null
     */
    default Option<T> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            final T value = reduce((t1, t2) -> comparator.compare(t1, t2) <= 0 ? t1 : t2);
            return new Some<>(value);
        }
    }

    Traversable<T> peek(Consumer<? super T> action);

    /**
     * Calculates the product of this elements. Supported component types are {@code Byte}, {@code Double}, {@code Float},
     * {@code Integer}, {@code Long}, {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().product()              // = 1
     * List.of(1, 2, 3).product()          // = 6
     * List.of(0.1, 0.2, 0.3).product()    // = 0.006
     * List.of("apple", "pear").product()  // throws
     * </code>
     * </pre>
     *
     * @return a {@code Number} representing the sum of this elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number product() {
        if (isEmpty()) {
            return 1;
        } else {
            return Match.as(Number.class)
                    .when((byte t) -> ((java.util.stream.Stream<Byte>) toJavaStream()).mapToInt(Number::intValue).reduce(1, (i1, i2) -> i1 * i2))
                    .when((double t) -> ((java.util.stream.Stream<Double>) toJavaStream()).mapToDouble(Number::doubleValue).reduce(1.0, (d1, d2) -> d1 * d2))
                    .when((float t) -> ((java.util.stream.Stream<Float>) toJavaStream()).mapToDouble(Number::doubleValue).reduce(1.0, (d1, d2) -> d1 * d2))
                    .when((int t) -> ((java.util.stream.Stream<Integer>) toJavaStream()).mapToInt(Number::intValue).reduce(1, (i1, i2) -> i1 * i2))
                    .when((long t) -> ((java.util.stream.Stream<Long>) toJavaStream()).mapToLong(Number::longValue).reduce(1L, (l1, l2) -> l1 * l2))
                    .when((short t) -> ((java.util.stream.Stream<Short>) toJavaStream()).mapToInt(Number::intValue).reduce(1, (i1, i2) -> i1 * i2))
                    .when((BigInteger t) -> ((java.util.stream.Stream<BigInteger>) toJavaStream()).mapToLong(Number::longValue).reduce(1L, (l1, l2) -> l1 * l2))
                    .when((BigDecimal t) -> ((java.util.stream.Stream<BigDecimal>) toJavaStream()).mapToDouble(Number::doubleValue).reduce(1.0, (d1, d2) -> d1 * d2))
                    .otherwise(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head());
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
     * @throws NullPointerException if {@code elements} is null
     */
    Traversable<T> removeAll(Iterable<? extends T> elements);

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op}.
     * The order of element iteration is undetermined.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws UnsupportedOperationException if this is empty
     * @throws NullPointerException          if {@code op} is null
     */
    default T reduce(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return reduceLeft(op);
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the left.
     *
     * @param op A BiFunction of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    default T reduceLeft(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceLeft on Nil");
        } else {
            return tail().foldLeft(head(), op);
        }
    }

    /**
     * Accumulates the elements of this Traversable by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    default T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        if (isEmpty()) {
            throw new NoSuchElementException("reduceRight on empty List");
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
     * @throws NullPointerException if {@code operator} is null
     */
    Traversable<T> replaceAll(UnaryOperator<T> operator);

    /**
     * Keeps all occurrences of the given elements from this.
     *
     * @param elements Elements to be kept.
     * @return a Traversable containing all occurreces of the given elements.
     * @throws NullPointerException if {@code elements} is null
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
     * @throws IllegalArgumentException if {@code size} is negative or zero
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
     * @throws IllegalArgumentException if {@code size} or {@code step} are negative or zero
     */
    Traversable<? extends Traversable<T>> sliding(int size, int step);

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return a Tuple containing the longest prefix of elements that satisfy p and the remainder.
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends Traversable<T>, ? extends Traversable<T>> span(Predicate<? super T> predicate);

    /**
     * Calculates the sum of this elements. Supported component types are {@code Byte}, {@code Double}, {@code Float},
     * {@code Integer}, {@code Long}, {@code Short}, {@code BigInteger} and {@code BigDecimal}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.empty().sum()              // = 0
     * List.of(1, 2, 3).sum()          // = 6
     * List.of(0.1, 0.2, 0.3).sum()    // = 0.6
     * List.of("apple", "pear").sum()  // throws
     * </code>
     * </pre>
     *
     * @return a {@code Number} representing the sum of this elements
     * @throws UnsupportedOperationException if this elements are not numeric
     */
    @SuppressWarnings("unchecked")
    default Number sum() {
        if (isEmpty()) {
            return 0;
        } else {
            return Match.as(Number.class)
                    .when((byte t) -> ((java.util.stream.Stream<Byte>) toJavaStream()).mapToInt(Number::intValue).sum())
                    .when((double t) -> ((java.util.stream.Stream<Double>) toJavaStream()).mapToDouble(Number::doubleValue).sum())
                    .when((float t) -> ((java.util.stream.Stream<Float>) toJavaStream()).mapToDouble(Number::doubleValue).sum())
                    .when((int t) -> ((java.util.stream.Stream<Integer>) toJavaStream()).mapToInt(Number::intValue).sum())
                    .when((long t) -> ((java.util.stream.Stream<Long>) toJavaStream()).mapToLong(Number::longValue).sum())
                    .when((short t) -> ((java.util.stream.Stream<Short>) toJavaStream()).mapToInt(Number::intValue).sum())
                    .when((BigInteger t) -> ((java.util.stream.Stream<BigInteger>) toJavaStream()).mapToLong(Number::longValue).sum())
                    .when((BigDecimal t) -> ((java.util.stream.Stream<BigDecimal>) toJavaStream()).mapToDouble(Number::doubleValue).sum())
                    .otherwise(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .apply(head());
        }
    }

    /**
     * Sends the string representations of these elements to the standard error stream {@linkplain System#err},
     * each in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stderr.
     */
    default void stderr() {
        for (T t : this) {
            System.err.println(String.valueOf(t));
            if (System.err.checkError()) {
                throw new IllegalStateException("Error writing to stderr");
            }
        }
    }

    /**
     * Sends the string representations of these elements to the standard output stream {@linkplain System#out},
     * each in a new line.
     *
     * @throws IllegalStateException if {@code PrintStream.checkError()} is true after writing to stdout.
     */
    default void stdout() {
        for (T t : this) {
            System.out.println(String.valueOf(t));
            if (System.out.checkError()) {
                throw new IllegalStateException("Error writing to stdout");
            }
        }
    }

    /**
     * Drops the first element of a non-empty Traversable.
     *
     * @return A new instance of Traversable containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    Traversable<T> tail();

    /**
     * Drops the first element of a non-empty Traversable and returns an {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends Traversable<T>> tailOption();

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
     * @throws NullPointerException if {@code predicate} is null
     */
    Traversable<T> takeWhile(Predicate<? super T> predicate);

    /**
     * Converts this to a Java array.
     * <p>
     * Tip: Given a {@code Traversable<M<T>> t} use {@code t.toJavaArray((Class<M<T>>) (Class) M.class)}.
     *
     * @param componentType Type of resulting array's elements.
     * @return a new array containing this elements
     * @throws NullPointerException if {@code componentType} is null
     */
    default T[] toJavaArray(Class<T> componentType) {
        Objects.requireNonNull(componentType, "componentType is null");
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
     * @throws NullPointerException if {@code f} is null
     */
    default <K, V> java.util.Map<K, V> toJavaMap(Function<? super T, Tuple2<K, V>> f) {
        Objects.requireNonNull(f, "f is null");
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
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Unzips this elements by mapping this elements to pairs which are subsequentially split into to distinct
     * traversables.
     *
     * @param unzipper a function which converts elements of this to pairs
     * @param <T1>     1st element type of a pair returned by unzipper
     * @param <T2>     2nd element type of a pair returned by unzipper
     * @return A pair of traversables containing elements split by unzipper
     * @throws NullPointerException if {@code unzipper} is null
     */
    <T1, T2> Tuple2<? extends Traversable<T1>, ? extends Traversable<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable collection by combining corresponding elements
     * in pairs. If one of the two Traversables is longer than the other, its remaining elements are ignored.
     * <p>
     * The length of the returned collection is the minimum of the lengths of this Traversable and that.
     *
     * @param <U>  The type of the second half of the returned pairs.
     * @param that The Iterable providing the second half of each result pair.
     * @return a new Traversable containing pairs consisting of corresponding elements of this list and that.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zip(Iterable<U> that);

    /**
     * Returns a Traversable formed from this Traversable and another Iterable by combining corresponding elements in
     * pairs. If one of the two collections is shorter than the other, placeholder elements are used to extend the
     * shorter collection to the length of the longer.
     * <p>
     * The length of the returned Traversable is the maximum of the lengths of this Traversable and that.
     * If this Traversable is shorter than that, thisElem values are used to fill the result.
     * If that is shorter than this Traversable, thatElem values are used to fill the result.
     *
     * @param <U>      The type of the second half of the returned pairs.
     * @param that     The Iterable providing the second half of each result pair.
     * @param thisElem The element to be used to fill up the result if this Traversable is shorter than that.
     * @param thatElem The element to be used to fill up the result if that is shorter than this Traversable.
     * @return A new Traversable containing pairs consisting of corresponding elements of this Traversable and that.
     * @throws NullPointerException if {@code that} is null
     */
    <U> Traversable<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem);

    /**
     * Zips this List with its indices.
     *
     * @return A new List containing all elements of this List paired with their index, starting with 0.
     */
    Traversable<Tuple2<T, Integer>> zipWithIndex();
}
