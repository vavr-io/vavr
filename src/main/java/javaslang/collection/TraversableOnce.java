/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
import javaslang.control.Match;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.function.*;

/**
 * An interface for data structures that are traversable once.
 *
 * <p>
 * Basic operations:
 *
 * <ul>
 * <li>{@link #clear()}</li>
 * <li>{@link #contains(Object)}</li>
 * <li>{@link #containsAll(java.lang.Iterable)}</li>
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
 *
 * Filtering:
 *
 * <ul>
 * <li>{@link #filter(Predicate)}</li>
 * <li>{@link #retainAll(java.lang.Iterable)}</li>
 * </ul>
 *
 * Numeric operations:
 *
 * <ul>
 * <li>{@link #average()}</li>
 * <li>{@link #max()}</li>
 * <li>{@link #maxBy(Comparator)}</li>
 * <li>{@link #maxBy(Function)}</li>
 * <li>{@link #min()}</li>
 * <li>{@link #minBy(Comparator)}</li>
 * <li>{@link #minBy(Function)}</li>
 * <li>{@link #product()}</li>
 * <li>{@link #sum()}</li>
 * </ul>
 *
 * Reduction:
 *
 * <ul>
 * <li>{@link #fold(Object, BiFunction)}</li>
 * <li>{@link #foldLeft(Object, BiFunction)}</li>
 * <li>{@link #foldRight(Object, BiFunction)}</li>
 * <li>{@link #mkString()}</li>
 * <li>{@link #mkString(CharSequence)}</li>
 * <li>{@link #mkString(CharSequence, CharSequence, CharSequence)}</li>
 * <li>{@link #reduce(BiFunction)}</li>
 * <li>{@link #reduceLeft(BiFunction)}</li>
 * <li>{@link #reduceRight(BiFunction)}</li>
 * </ul>
 *
 * Selection:
 *
 * <ul>
 * <li>{@link #drop(int)}</li>
 * <li>{@link #dropRight(int)}</li>
 * <li>{@link #dropWhile(Predicate)}</li>
 * <li>{@link #findFirst(Predicate)}</li>
 * <li>{@link #findLast(Predicate)}</li>
 * <li>{@link #take(int)}</li>
 * <li>{@link #takeRight(int)}</li>
 * <li>{@link #takeWhile(Predicate)}</li>
 * </ul>
 *
 * Tests:
 *
 * <ul>
 * <li>{@link #existsUnique(Predicate)}</li>
 * </ul>
 *
 * Transformation:
 *
 * <ul>
 * <li>{@link #distinct()}</li>
 * <li>{@link #distinctBy(Comparator)}</li>
 * <li>{@link #distinctBy(Function)}</li>
 * <li>{@link #flatMap(Function)}</li>
 * <li>{@link #flatten()}</li>
 * <li>{@link #groupBy(Function)}</li>
 * <li>{@link #map(Function)}</li>
 * <li>{@link #partition(Predicate)}</li>
 * <li>{@link #replace(Object, Object)}</li>
 * <li>{@link #replaceAll(Object, Object)}</li>
 * <li>{@link #replaceAll(UnaryOperator)}</li>
 * <li>{@link #sliding(int)}</li>
 * <li>{@link #sliding(int, int)}</li>
 * <li>{@link #span(Predicate)}</li>
 * </ul>
 *
 * @param <T> Component type
 * @since 2.0.0
 */
public interface TraversableOnce<T> extends Value<T> {

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
            final java.util.stream.Stream<Number> numbers = ((java.util.stream.Stream<Number>) toJavaStream());
            return Match.of(head())
                    .whenTypeIn(Byte.class, Integer.class, Short.class).then(() -> numbers.mapToInt(Number::intValue).average())
                    .whenTypeIn(Double.class, Float.class, BigDecimal.class).then(() -> numbers.mapToDouble(Number::doubleValue).average())
                    .whenTypeIn(Long.class, BigInteger.class).then(() -> numbers.mapToLong(Number::longValue).average())
                    .otherwise(() -> {
                        throw new UnsupportedOperationException("not numeric");
                    })
                    .map(OptionalDouble::getAsDouble)
                    .toOption();
        }
    }

    /**
     * Returns an empty version of this traversable, i.e. {@code this.clear().isEmpty() == true}.
     *
     * @return an empty TraversableOnce.
     */
    TraversableOnce<T> clear();

    /**
     * Tests if this TraversableOnce contains a given value.
     *
     * @param element An Object of type A, may be null.
     * @return true, if element is in this TraversableOnce, false otherwise.
     */
    default boolean contains(T element) {
        return findFirst(e -> java.util.Objects.equals(e, element)).isDefined();
    }

    /**
     * <p>
     * Tests if this TraversableOnce contains all given elements.
     * </p>
     * <p>
     * The result is equivalent to
     * {@code elements.isEmpty() ? true : contains(elements.head()) && containsAll(elements.tail())} but implemented
     * without recursion.
     * </p>
     *
     * @param elements A List of values of type T.
     * @return true, if this List contains all given elements, false otherwise.
     * @throws NullPointerException if {@code elements} is null
     */
    default boolean containsAll(java.lang.Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return List.ofAll(elements)
                .distinct()
                .findFirst(e -> !this.contains(e))
                .isEmpty();
    }

    /**
     * Returns a new version of this which contains no duplicates. Elements are compared using {@code equals}.
     *
     * @return a new {@code TraversableOnce} containing this elements without duplicates
     */
    TraversableOnce<T> distinct();

    /**
     * Returns a new version of this which contains no duplicates. Elements are compared using the given
     * {@code comparator}.
     *
     * @param comparator A comparator
     * @return a new {@code TraversableOnce} containing this elements without duplicates
     */
    TraversableOnce<T> distinctBy(Comparator<? super T> comparator);

    /**
     * Returns a new version of this which contains no duplicates. Elements mapped to keys which are compared using
     * {@code equals}.
     * <p>
     * The elements of the result are determined in the order of their occurrence - first match wins.
     *
     * @param keyExtractor A key extractor
     * @param <U>          key type
     * @return a new {@code TraversableOnce} containing this elements without duplicates
     * @throws NullPointerException if {@code keyExtractor} is null
     */
    <U> TraversableOnce<T> distinctBy(Function<? super T, ? extends U> keyExtractor);

    /**
     * Drops the first n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the first n ones, or else the empty instance,
     * if this has less than n elements.
     */
    TraversableOnce<T> drop(int n);

    /**
     * Drops the last n elements of this or all elements, if this length &lt; n.
     *
     * @param n The number of elements to drop.
     * @return a new instance consisting of all elements of this except the last n ones, or else the empty instance,
     * if this has less than n elements.
     */
    TraversableOnce<T> dropRight(int n);

    /**
     * Drops elements while the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for this elements starting with the first.
     * @return a new instance consisting of all elements starting from the first one which does not satisfy the
     * given predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    TraversableOnce<T> dropWhile(Predicate<? super T> predicate);

    /**
     * Checks, if a unique elements exists such that the predicate holds.
     *
     * @param predicate A Predicate
     * @return true, if predicate holds for a unique element, false otherwise
     * @throws NullPointerException if {@code predicate} is null
     */
    default boolean existsUnique(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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
     * Returns a new traversable consisting of all elements which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new traversable
     * @throws NullPointerException if {@code predicate} is null
     */
    @Override
    TraversableOnce<T> filter(Predicate<? super T> predicate);

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
    Option<T> findLast(Predicate<? super T> predicate);

    <U> TraversableOnce<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper);

    @Override
    <U> TraversableOnce<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper);

    @Override
    TraversableOnce<Object> flatten();

    /**
     * <p>
     * Accumulates the elements of this TraversableOnce by successively calling the given operator {@code op}.
     * </p>
     * <p>
     * Example: {@code List("a", "b", "c").fold("", (xs, x) -> xs + x) = "abc"}
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
     * Accumulates the elements of this TraversableOnce by successively calling the given function {@code f} from the left,
     * starting with a value {@code zero} of type B.
     * <p>
     * Example: Reverse and map a TraversableOnce in one pass
     * <pre><code>
     * List.of("a", "b", "c").foldLeft(List.empty(), (xs, x) -&gt; xs.prepend(x.toUpperCase()))
     * // = List("C", "B", "A")
     * </code></pre>
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
     * Accumulates the elements of this TraversableOnce by successively calling the given function {@code f} from the right,
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
    <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f);

    @Override
    default T get() {
        return iterator().next();
    }

    /**
     * Groups this elements by classifying the elements.
     *
     * @param classifier A function which classifies elements into classes
     * @param <C>        classified class type
     * @return A Map containing the grouped elements
     */
    <C> Map<C, ? extends TraversableOnce<T>> groupBy(Function<? super T, ? extends C> classifier);

    /**
     * Returns the first element of a non-empty TraversableOnce.
     *
     * @return The first element of this TraversableOnce.
     * @throws NoSuchElementException if this is empty
     */
    T head();

    /**
     * Returns the first element of a non-empty TraversableOnce as {@code Option}.
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
    TraversableOnce<T> init();

    /**
     * Dual of {@linkplain #tailOption()}, returning all elements except the last as {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends TraversableOnce<T>> initOption();

    /**
     * Checks if this TraversableOnce is empty.
     *
     * @return true, if this TraversableOnce contains no elements, falso otherwise.
     */
    @Override
    boolean isEmpty();

    /**
     * An iterator by means of head() and tail(). Subclasses may want to override this method.
     *
     * @return A new Iterator of this TraversableOnce elements.
     */
    @Override
    default Iterator<T> iterator() {
        final TraversableOnce<T> that = this;
        return new Iterator.AbstractIterator<T>() {

            TraversableOnce<T> traversable = that;

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
     * Dual of {@linkplain #head()}, returning the last element.
     *
     * @return the last element.
     * @throws NoSuchElementException is this is empty
     */
    default T last() {
        if (isEmpty()) {
            throw new NoSuchElementException("last of Nil");
        } else {
            TraversableOnce<T> traversable = this;
            { // don't let escape tail
                TraversableOnce<T> tail;
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
    int length();

    /**
     * Maps the elements of this traversable to elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the target TraversableOnce
     * @return a mapped TraversableOnce
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> TraversableOnce<U> map(Function<? super T, ? extends U> mapper);

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
     * Calculates the maximum of this elements within the co-domain of a specific function.
     *
     * @param f   A function that maps this elements to comparable elements
     * @param <U> The type where elements are compared
     * @return The element of type T which is the maximum within U
     */
    default <U extends Comparable<? super U>> Option<T> maxBy(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            return maxBy((t1, t2) -> f.apply(t1).compareTo(f.apply(t2)));
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

    /**
     * Calculates the minimum of this elements within the co-domain of a specific function.
     *
     * @param f   A function that maps this elements to comparable elements
     * @param <U> The type where elements are compared
     * @return The element of type T which is the minimum within U
     */
    default <U extends Comparable<? super U>> Option<T> minBy(Function<? super T, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        if (isEmpty()) {
            return None.instance();
        } else {
            return minBy((t1, t2) -> f.apply(t1).compareTo(f.apply(t2)));
        }
    }

    /**
     * Joins the elements of this by concatenating their string representations.
     * <p>
     * This has the same effect as calling {@code mkString("", "", "")}.
     *
     * @return a new String
     */
    default String mkString() {
        return mkString("", "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter.
     * <p>
     * This has the same effect as calling {@code mkString(delimiter, "", "")}.
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @return A new String
     */
    default String mkString(CharSequence delimiter) {
        return mkString(delimiter, "", "");
    }

    /**
     * Joins the string representations of this elements using a specific delimiter, prefix and suffix.
     * <p>
     * Example: {@code List.of("a", "b", "c").mkString(", ", "Chars(", ")") = "Chars(a, b, c)"}
     *
     * @param delimiter A delimiter string put between string representations of elements of this
     * @param prefix    prefix of the resulting string
     * @param suffix    suffix of the resulting string
     * @return a new String
     */
    default String mkString(CharSequence delimiter,
                        CharSequence prefix,
                        CharSequence suffix) {
        final StringBuilder builder = new StringBuilder(prefix);
        iterator().map(String::valueOf).intersperse(String.valueOf(delimiter)).forEach(builder::append);
        return builder.append(suffix).toString();
    }

    /**
     * Creates a partition of this {@code TraversableOnce} by splitting this elements in two in distinct tarversables
     * according to a predicate.
     *
     * @param predicate A predicate which classifies an element if it is in the first or the second traversable.
     * @return A disjoint union of two traversables. The first {@code TraversableOnce} contains all elements that satisfy the given {@code predicate}, the second {@code TraversableOnce} contains all elements that don't. The original order of elements is preserved.
     * @throws NullPointerException if predicate is null
     */
    Tuple2<? extends TraversableOnce<T>, ? extends TraversableOnce<T>> partition(Predicate<? super T> predicate);

    @Override
    TraversableOnce<T> peek(Consumer<? super T> action);

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
            final java.util.stream.Stream<Number> numbers = ((java.util.stream.Stream<Number>) toJavaStream());
            return Match.of(head()).as(Number.class)
                    .whenTypeIn(Byte.class, Integer.class, Short.class).then(() -> numbers.mapToInt(Number::intValue).reduce(1, (i1, i2) -> i1 * i2))
                    .whenTypeIn(Double.class, Float.class, BigDecimal.class).then(() -> numbers.mapToDouble(Number::doubleValue).reduce(1.0, (d1, d2) -> d1 * d2))
                    .whenTypeIn(Long.class, BigInteger.class).then(ignored -> numbers.mapToLong(Number::longValue).reduce(1L, (l1, l2) -> l1 * l2))
                    .orElseThrow(() -> new UnsupportedOperationException("not numeric"));
        }
    }

    /**
     * Accumulates the elements of this TraversableOnce by successively calling the given operation {@code op}.
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
     * Accumulates the elements of this TraversableOnce by successively calling the given operation {@code op} from the left.
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
     * Accumulates the elements of this TraversableOnce by successively calling the given operation {@code op} from the right.
     *
     * @param op An operation of type T
     * @return the reduced value.
     * @throws NoSuchElementException if this is empty
     * @throws NullPointerException   if {@code op} is null
     */
    T reduceRight(BiFunction<? super T, ? super T, ? extends T> op);

    /**
     * Replaces the first occurrence (if exists) of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a TraversableOnce containing all elements of this where the first occurrence of currentElement is replaced with newELement.
     */
    TraversableOnce<T> replace(T currentElement, T newElement);

    /**
     * Replaces all occurrences of the given currentElement with newElement.
     *
     * @param currentElement An element to be substituted.
     * @param newElement     A replacement for currentElement.
     * @return a TraversableOnce containing all elements of this where all occurrences of currentElement are replaced with newELement.
     */
    TraversableOnce<T> replaceAll(T currentElement, T newElement);

    /**
     * Replaces all occurrences of this TraversableOnce by applying the given operator to the elements, which is
     * essentially a special case of {@link #map(Function)}.
     *
     * @param operator An operator.
     * @return a TraversableOnce containing all elements of this transformed within the same domain.
     * @throws NullPointerException if {@code operator} is null
     */
    TraversableOnce<T> replaceAll(UnaryOperator<T> operator);

    /**
     * Keeps all occurrences of the given elements from this.
     *
     * @param elements Elements to be kept.
     * @return a TraversableOnce containing all occurreces of the given elements.
     * @throws NullPointerException if {@code elements} is null
     */
    TraversableOnce<T> retainAll(java.lang.Iterable<? extends T> elements);

    /**
     * Returns a tuple where the first element is the longest prefix of elements that satisfy p and the second element is the remainder.
     *
     * @param predicate A predicate.
     * @return a Tuple containing the longest prefix of elements that satisfy p and the remainder.
     * @throws NullPointerException if {@code predicate} is null
     */
    Tuple2<? extends TraversableOnce<T>, ? extends TraversableOnce<T>> span(Predicate<? super T> predicate);

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
            final java.util.stream.Stream<Number> numbers = ((java.util.stream.Stream<Number>) toJavaStream());
            return Match.of(head()).as(Number.class)
                    .whenTypeIn(Byte.class, Integer.class, Short.class).then(() -> numbers.mapToInt(Number::intValue).sum())
                    .whenTypeIn(Double.class, Float.class, BigDecimal.class).then(() -> numbers.mapToDouble(Number::doubleValue).sum())
                    .whenTypeIn(Long.class, BigInteger.class).then(ignored -> numbers.mapToLong(Number::longValue).sum())
                    .orElseThrow(() -> new UnsupportedOperationException("not numeric"));
        }
    }

    /**
     * Drops the first element of a non-empty TraversableOnce.
     *
     * @return A new instance of TraversableOnce containing all elements except the first.
     * @throws UnsupportedOperationException if this is empty
     */
    TraversableOnce<T> tail();

    /**
     * Drops the first element of a non-empty TraversableOnce and returns an {@code Option}.
     *
     * @return {@code Some(traversable)} or {@code None} if this is empty.
     */
    Option<? extends TraversableOnce<T>> tailOption();

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
    TraversableOnce<T> take(int n);

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
    TraversableOnce<T> takeRight(int n);

    /**
     * Takes elements while the predicate holds for the current element.
     *
     * @param predicate A condition tested subsequently for this elements starting with the last.
     * @return a new instance consisting of all elements starting from the last one which does not satisfy the
     * given predicate.
     * @throws NullPointerException if {@code predicate} is null
     */
    TraversableOnce<T> takeWhile(Predicate<? super T> predicate);

}
