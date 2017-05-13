/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.*;
import io.vavr.control.Option;
import io.vavr.collection.List.Nil;
import io.vavr.collection.ListModule.Combinations;
import io.vavr.collection.ListModule.SplitAt;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static io.vavr.collection.JavaConverters.ChangePolicy.IMMUTABLE;
import static io.vavr.collection.JavaConverters.ChangePolicy.MUTABLE;

/**
 * An immutable {@code List} is an eager sequence of elements. Its immutability makes it suitable for concurrent programming.
 * <p>
 * A {@code List} is composed of a {@code head} element and a {@code tail} {@code List}.
 * <p>
 * There are two implementations of the {@code List} interface:
 *
 * <ul>
 * <li>{@link Nil}, which represents the empty {@code List}.</li>
 * <li>{@link Cons}, which represents a {@code List} containing one or more elements.</li>
 * </ul>
 *
 * A {@code List} is a {@code Stack} in the sense that it stores elements allowing a last-in-first-out (LIFO) retrieval.
 * <p>
 * Stack API:
 *
 * <ul>
 * <li>{@link #peek()}</li>
 * <li>{@link #peekOption()}</li>
 * <li>{@link #pop()}</li>
 * <li>{@link #popOption()}</li>
 * <li>{@link #pop2()}</li>
 * <li>{@link #pop2Option()}</li>
 * <li>{@link #push(Object)}</li>
 * <li>{@link #push(Object[])}</li>
 * <li>{@link #pushAll(Iterable)}</li>
 * </ul>
 *
 * Methods to obtain a {@code List}:
 *
 * <pre>
 * <code>
 * // factory methods
 * List.empty()                        // = List.of() = Nil.instance()
 * List.of(x)                          // = new Cons&lt;&gt;(x, Nil.instance())
 * List.of(Object...)                  // e.g. List.of(1, 2, 3)
 * List.ofAll(Iterable)                // e.g. List.ofAll(Stream.of(1, 2, 3)) = 1, 2, 3
 * List.ofAll(&lt;primitive array&gt;) // e.g. List.of(new int[] {1, 2, 3}) = 1, 2, 3
 *
 * // int sequences
 * List.range(0, 3)              // = 0, 1, 2
 * List.rangeClosed(0, 3)        // = 0, 1, 2, 3
 * </code>
 * </pre>
 *
 * Note: A {@code List} is primarily a {@code Seq} and extends {@code Stack} for technical reasons (so {@code Stack} does not need to wrap {@code List}).
 * <p>
 * If operating on a {@code List}, please prefer
 *
 * <ul>
 * <li>{@link #prepend(Object)} over {@link #push(Object)}</li>
 * <li>{@link #prependAll(Iterable)} over {@link #pushAll(Iterable)}</li>
 * <li>{@link #tail()} over {@link #pop()}</li>
 * <li>{@link #tailOption()} over {@link #popOption()}</li>
 * </ul>
 *
 * Factory method applications:
 *
 * <pre>
 * <code>
 * List&lt;Integer&gt;       s1 = List.of(1);
 * List&lt;Integer&gt;       s2 = List.of(1, 2, 3);
 *                           // = List.of(new Integer[] {1, 2, 3});
 *
 * List&lt;int[]&gt;         s3 = List.ofAll(1, 2, 3);
 * List&lt;List&lt;Integer&gt;&gt; s4 = List.ofAll(List.of(1, 2, 3));
 *
 * List&lt;Integer&gt;       s5 = List.ofAll(1, 2, 3);
 * List&lt;Integer&gt;       s6 = List.ofAll(List.of(1, 2, 3));
 *
 * // cuckoo's egg
 * List&lt;Integer[]&gt;     s7 = List.&lt;Integer[]&gt; of(new Integer[] {1, 2, 3});
 * </code>
 * </pre>
 *
 * Example: Converting a String to digits
 *
 * <pre>
 * <code>
 * // = List(1, 2, 3)
 * List.of("123".toCharArray()).map(c -&gt; Character.digit(c, 10))
 * </code>
 * </pre>
 *
 * See Okasaki, Chris: <em>Purely Functional Data Structures</em> (p. 7 ff.). Cambridge, 2003.
 *
 * @param <T> Component type of the List
 * @author Daniel Dietrich
 */
public interface List<T> extends LinearSeq<T> {

    long serialVersionUID = 1L;

    /**
     * Returns a {@link java.util.stream.Collector} which may be used in conjunction with
     * {@link java.util.stream.Stream#collect(java.util.stream.Collector)} to obtain a {@link List}.
     *
     * @param <T> Component type of the List.
     * @return A io.vavr.collection.List Collector.
     */
    static <T> Collector<T, ArrayList<T>, List<T>> collector() {
        final Supplier<ArrayList<T>> supplier = ArrayList::new;
        final BiConsumer<ArrayList<T>, T> accumulator = ArrayList::add;
        final BinaryOperator<ArrayList<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        final Function<ArrayList<T>, List<T>> finisher = List::ofAll;
        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns the single instance of Nil. Convenience method for {@code Nil.instance()} .
     * <p>
     * Note: this method intentionally returns type {@code List} and not {@code Nil}. This comes handy when folding.
     * If you explicitly need type {@code Nil} use {@linkplain Nil#instance()}.
     *
     * @param <T> Component type of Nil, determined by type inference in the particular context.
     * @return The empty list.
     */
    static <T> List<T> empty() {
        return Nil.instance();
    }

    /**
     * A {@code List} is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    @Override
    boolean isEmpty();

    /**
     * A {@code List} is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * Narrows a widened {@code List<? extends T>} to {@code List<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param list A {@code List}.
     * @param <T>  Component type of the {@code List}.
     * @return the given {@code list} instance as narrowed type {@code List<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> List<T> narrow(List<? extends T> list) {
        return (List<T>) list;
    }

    /**
     * Returns a singleton {@code List}, i.e. a {@code List} of one element.
     *
     * @param element An element.
     * @param <T>     The component type
     * @return A new List instance containing the given element
     */
    static <T> List<T> of(T element) {
        return new Cons<>(element, Nil.instance());
    }

    /**
     * Creates a List of the given elements.
     * <pre>
     * <code>
     *   List.of(1, 2, 3, 4)
     * = Nil.instance().prepend(4).prepend(3).prepend(2).prepend(1)
     * = new Cons(1, new Cons(2, new Cons(3, new Cons(4, Nil.instance()))))
     * </code>
     * </pre>
     *
     * @param <T>      Component type of the List.
     * @param elements Zero or more elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SafeVarargs
    static <T> List<T> of(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = Nil.instance();
        for (int i = elements.length - 1; i >= 0; i--) {
            result = result.prepend(elements[i]);
        }
        return result;
    }

    /**
     * Creates a List of the given elements.
     * <p>
     * The resulting list has the same iteration order as the given iterable of elements
     * if the iteration order of the elements is stable.
     *
     * @param <T>      Component type of the List.
     * @param elements An Iterable of elements.
     * @return A list containing the given elements in the same order.
     * @throws NullPointerException if {@code elements} is null
     */
    @SuppressWarnings("unchecked")
    static <T> List<T> ofAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (elements instanceof List) {
            return (List<T>) elements;
        } else if (elements instanceof java.util.List) {
            List<T> result = Nil.instance();
            final java.util.List<T> list = (java.util.List<T>) elements;
            final ListIterator<T> iterator = list.listIterator(list.size());
            while (iterator.hasPrevious()) {
                result = result.prepend(iterator.previous());
            }
            return result;
        } else if (elements instanceof NavigableSet) {
            List<T> result = Nil.instance();
            final java.util.Iterator<T> iterator = ((NavigableSet<T>) elements).descendingIterator();
            while (iterator.hasNext()) {
                result = result.prepend(iterator.next());
            }
            return result;
        } else {
            List<T> result = Nil.instance();
            for (T element : elements) {
                result = result.prepend(element);
            }
            return result.reverse();
        }
    }

    /**
     * Creates a List that contains the elements of the given {@link java.util.stream.Stream}.
     *
     * @param javaStream A {@link java.util.stream.Stream}
     * @param <T>        Component type of the Stream.
     * @return A List containing the given elements in the same order.
     */
    static <T> List<T> ofAll(java.util.stream.Stream<? extends T> javaStream) {
        Objects.requireNonNull(javaStream, "javaStream is null");
        final java.util.Iterator<? extends T> iterator = javaStream.iterator();
        List<T> list = List.empty();
        while (iterator.hasNext()) {
            list = list.prepend(iterator.next());
        }
        return list.reverse();
    }

    /**
     * Creates a List from boolean values.
     *
     * @param elements boolean values
     * @return A new List of Boolean values
     * @throws NullPointerException if elements is null
     */
    static List<Boolean> ofAll(boolean... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from byte values.
     *
     * @param elements byte values
     * @return A new List of Byte values
     * @throws NullPointerException if elements is null
     */
    static List<Byte> ofAll(byte... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from char values.
     *
     * @param elements char values
     * @return A new List of Character values
     * @throws NullPointerException if elements is null
     */
    static List<Character> ofAll(char... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from double values.
     *
     * @param elements double values
     * @return A new List of Double values
     * @throws NullPointerException if elements is null
     */
    static List<Double> ofAll(double... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from float values.
     *
     * @param elements a float values
     * @return A new List of Float values
     * @throws NullPointerException if elements is null
     */
    static List<Float> ofAll(float... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from int values.
     *
     * @param elements int values
     * @return A new List of Integer values
     * @throws NullPointerException if elements is null
     */
    static List<Integer> ofAll(int... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from long values.
     *
     * @param elements long values
     * @return A new List of Long values
     * @throws NullPointerException if elements is null
     */
    static List<Long> ofAll(long... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Creates a List from short values.
     *
     * @param elements short values
     * @return A new List of Short values
     * @throws NullPointerException if elements is null
     */
    static List<Short> ofAll(short... elements) {
        Objects.requireNonNull(elements, "elements is null");
        return ofAll(Iterator.ofAll(elements));
    }

    /**
     * Returns a List containing {@code n} values of a given Function {@code f}
     * over a range of integer values from 0 to {@code n - 1}.
     *
     * @param <T> Component type of the List
     * @param n   The number of elements in the List
     * @param f   The Function computing element values
     * @return A List consisting of elements {@code f(0),f(1), ..., f(n - 1)}
     * @throws NullPointerException if {@code f} is null
     */
    static <T> List<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        return Collections.tabulate(n, f, empty(), List::of);
    }

    /**
     * Returns a List containing {@code n} values supplied by a given Supplier {@code s}.
     *
     * @param <T> Component type of the List
     * @param n   The number of elements in the List
     * @param s   The Supplier computing element values
     * @return A List of size {@code n}, where each element contains the result supplied by {@code s}.
     * @throws NullPointerException if {@code s} is null
     */
    static <T> List<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return Collections.fill(n, s, empty(), List::of);
    }

    static List<Character> range(char from, char toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    static List<Character> rangeBy(char from, char toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @GwtIncompatible
    static List<Double> rangeBy(double from, double toExclusive, double step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0, 0)  // = List()
     * List.range(2, 0)  // = List()
     * List.range(-2, 2) // = List(-2, -1, 0, 1)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of int values as specified or the empty range if {@code from >= toExclusive}
     */
    static List<Integer> range(int from, int toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1, 3, 1)  // = List(1, 2)
     * List.rangeBy(1, 4, 2)  // = List(1, 3)
     * List.rangeBy(4, 1, -2) // = List(4, 2)
     * List.rangeBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static List<Integer> rangeBy(int from, int toExclusive, int step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.range(0L, 0L)  // = List()
     * List.range(2L, 0L)  // = List()
     * List.range(-2L, 2L) // = List(-2L, -1L, 0L, 1L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @return a range of long values as specified or the empty range if {@code from >= toExclusive}
     */
    static List<Long> range(long from, long toExclusive) {
        return ofAll(Iterator.range(from, toExclusive));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toExclusive - 1},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeBy(1L, 3L, 1L)  // = List(1L, 2L)
     * List.rangeBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toExclusive the last number + 1
     * @param step        the step
     * @return a range of long values as specified or the empty range if<br>
     * {@code from >= toInclusive} and {@code step > 0} or<br>
     * {@code from <= toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static List<Long> rangeBy(long from, long toExclusive, long step) {
        return ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    static List<Character> rangeClosed(char from, char toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    static List<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @GwtIncompatible
    static List<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0, 0)  // = List(0)
     * List.rangeClosed(2, 0)  // = List()
     * List.rangeClosed(-2, 2) // = List(-2, -1, 0, 1, 2)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of int values as specified or the empty range if {@code from > toInclusive}
     */
    static List<Integer> rangeClosed(int from, int toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a List of int numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1, 3, 1)  // = List(1, 2, 3)
     * List.rangeClosedBy(1, 4, 2)  // = List(1, 3)
     * List.rangeClosedBy(4, 1, -2) // = List(4, 2)
     * List.rangeClosedBy(4, 1, 2)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static List<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosed(0L, 0L)  // = List(0L)
     * List.rangeClosed(2L, 0L)  // = List()
     * List.rangeClosed(-2L, 2L) // = List(-2L, -1L, 0L, 1L, 2L)
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @return a range of long values as specified or the empty range if {@code from > toInclusive}
     */
    static List<Long> rangeClosed(long from, long toInclusive) {
        return ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    /**
     * Creates a List of long numbers starting from {@code from}, extending to {@code toInclusive},
     * with {@code step}.
     * <p>
     * Examples:
     * <pre>
     * <code>
     * List.rangeClosedBy(1L, 3L, 1L)  // = List(1L, 2L, 3L)
     * List.rangeClosedBy(1L, 4L, 2L)  // = List(1L, 3L)
     * List.rangeClosedBy(4L, 1L, -2L) // = List(4L, 2L)
     * List.rangeClosedBy(4L, 1L, 2L)  // = List()
     * </code>
     * </pre>
     *
     * @param from        the first number
     * @param toInclusive the last number
     * @param step        the step
     * @return a range of int values as specified or the empty range if<br>
     * {@code from > toInclusive} and {@code step > 0} or<br>
     * {@code from < toInclusive} and {@code step < 0}
     * @throws IllegalArgumentException if {@code step} is zero
     */
    static List<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    /**
     * Transposes the rows and columns of a {@link List} matrix.
     *
     * @param <T> matrix element type
     * @param matrix to be transposed.
     * @return a transposed {@link List} matrix.
     * @throws IllegalArgumentException if the row lengths of {@code matrix} differ.
     * <p>
     * ex: {@code
     * List.transpose(List(List(1,2,3), List(4,5,6))) → List(List(1,4), List(2,5), List(3,6))
     * }
     */
    static <T> List<List<T>> transpose(List<List<T>> matrix) {
        return Collections.transpose(matrix, List::ofAll, List::of);
    }

    /**
     * Creates a list from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the element for the next call and the value to add to the
     * resulting list.
     * <p>
     * Example:
     * <pre>
     * <code>
     * List.unfoldRight(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x, x-1)));
     * // List(10, 9, 8, 7, 6, 5, 4, 3, 2, 1))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds
     * @param <U>  type of unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a list with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    static <T, U> List<U> unfoldRight(T seed, Function<? super T, Option<Tuple2<? extends U, ? extends T>>> f) {
        return Iterator.unfoldRight(seed, f).toList();
    }

    /**
     * Creates a list from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting list and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * List.unfoldLeft(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
     * // List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds
     * @param <U>  type of unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a list with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    static <T, U> List<U> unfoldLeft(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends U>>> f) {
        return Iterator.unfoldLeft(seed, f).toList();
    }

    /**
     * Creates a list from a seed value and a function.
     * The function takes the seed at first.
     * The function should return {@code None} when it's
     * done generating the list, otherwise {@code Some} {@code Tuple}
     * of the value to add to the resulting list and
     * the element for the next call.
     * <p>
     * Example:
     * <pre>
     * <code>
     * List.unfold(10, x -&gt; x == 0
     *             ? Option.none()
     *             : Option.of(new Tuple2&lt;&gt;(x-1, x)));
     * // List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
     * </code>
     * </pre>
     *
     * @param <T>  type of seeds and unfolded values
     * @param seed the start value for the iteration
     * @param f    the function to get the next step of the iteration
     * @return a list with the values built up by the iteration
     * @throws NullPointerException if {@code f} is null
     */
    static <T> List<T> unfold(T seed, Function<? super T, Option<Tuple2<? extends T, ? extends T>>> f) {
        return Iterator.unfold(seed, f).toList();
    }

    @Override
    default List<T> append(T element) {
        return foldRight(of(element), (x, xs) -> xs.prepend(x));
    }

    @Override
    default List<T> appendAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return List.<T> ofAll(elements).prependAll(this);
    }

    @GwtIncompatible
    @Override
    default java.util.List<T> asJava() {
        return JavaConverters.asJava(this, IMMUTABLE);
    }

    @GwtIncompatible
    @Override
    default List<T> asJava(Consumer<? super java.util.List<T>> action) {
        return Collections.asJava(this, action, IMMUTABLE);
    }

    @GwtIncompatible
    @Override
    default java.util.List<T> asJavaMutable() {
        return JavaConverters.asJava(this, MUTABLE);
    }

    @GwtIncompatible
    @Override
    default List<T> asJavaMutable(Consumer<? super java.util.List<T>> action) {
        return Collections.asJava(this, action, MUTABLE);
    }

    @Override
    default <R> List<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
        return ofAll(iterator().<R> collect(partialFunction));
    }

    @Override
    default List<List<T>> combinations() {
        return rangeClosed(0, length()).map(this::combinations).flatMap(Function.identity());
    }

    @Override
    default List<List<T>> combinations(int k) {
        return Combinations.apply(this, Math.max(k, 0));
    }

    @Override
    default Iterator<List<T>> crossProduct(int power) {
        return Collections.crossProduct(empty(), this, power);
    }

    @Override
    default List<T> distinct() {
        return distinctBy(Function.identity());
    }

    @Override
    default List<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        final java.util.Set<T> seen = new java.util.TreeSet<>(comparator);
        return filter(seen::add);
    }

    @Override
    default <U> List<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        final java.util.Set<U> seen = new java.util.HashSet<>();
        return filter(t -> seen.add(keyExtractor.apply(t)));
    }

    @Override
    default List<T> drop(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= size()) {
            return empty();
        }
        List<T> list = this;
        for (long i = n; i > 0 && !list.isEmpty(); i--) {
            list = list.tail();
        }
        return list;
    }

    @Override
    default List<T> dropUntil(Predicate<? super T> predicate) {
        return Collections.dropUntil(this, predicate);
    }

    @Override
    default List<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropUntil(predicate.negate());
    }

    @Override
    default List<T> dropRight(int n) {
        if (n <= 0) {
            return this;
        }
        if (n >= length()) {
            return empty();
        }
        return ofAll(iterator().dropRight(n));
    }

    @Override
    default List<T> dropRightUntil(Predicate<? super T> predicate) {
        return Collections.dropUntil(reverse(), predicate).reverse();
    }

    @Override
    default List<T> dropRightWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropRightUntil(predicate.negate());
    }

    @Override
    default List<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return this;
        } else {
            final List<T> filtered = foldLeft(empty(), (xs, x) -> predicate.test(x) ? xs.prepend(x) : xs);
            if (filtered.isEmpty()) {
                return empty();
            } else if (filtered.length() == length()) {
                return this;
            } else {
                return filtered.reverse();
            }
        }
    }

    @Override
    default <U> List<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        List<U> list = empty();
        for (T t : this) {
            for (U u : mapper.apply(t)) {
                list = list.prepend(u);
            }
        }
        return list.reverse();
    }

    @Override
    default T get(int index) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("get(" + index + ") on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        List<T> list = this;
        for (int i = index - 1; i >= 0; i--) {
            list = list.tail();
            if (list.isEmpty()) {
                throw new IndexOutOfBoundsException("get(" + index + ") on List of length " + (index - i));
            }
        }
        return list.head();
    }

    @Override
    default <C> Map<C, List<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, List::ofAll);
    }

    @Override
    default Iterator<List<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    default int indexOf(T element, int from) {
        int index = 0;
        for (List<T> list = this; !list.isEmpty(); list = list.tail(), index++) {
            if (index >= from && Objects.equals(list.head(), element)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    default List<T> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty list");
        } else {
            return dropRight(1);
        }
    }

    @Override
    default Option<List<T>> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    int length();

    @Override
    default List<T> insert(int index, T element) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("insert(" + index + ", e)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insert(" + index + ", e) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<T> result = tail.prepend(element);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> insertAll(int index, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (index < 0) {
            throw new IndexOutOfBoundsException("insertAll(" + index + ", elements)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("insertAll(" + index + ", elements) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        List<T> result = tail.prependAll(elements);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> intersperse(T element) {
        return ofAll(iterator().intersperse(element));
    }

    @Override
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    default int lastIndexOf(T element, int end) {
        int result = -1, index = 0;
        for (List<T> list = this; index <= end && !list.isEmpty(); list = list.tail(), index++) {
            if (Objects.equals(list.head(), element)) {
                result = index;
            }
        }
        return result;
    }

    @Override
    default <U> List<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        List<U> list = empty();
        for (T t : this) {
            list = list.prepend(mapper.apply(t));
        }
        return list.reverse();
    }

    @Override
    default List<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? ofAll(other) : this;
    }

    @Override
    default List<T> orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? ofAll(supplier.get()) : this;
    }

    @Override
    default List<T> padTo(int length, T element) {
        final int actualLength = length();
        if (length <= actualLength) {
            return this;
        } else {
            return appendAll(Iterator.continually(element).take(length - actualLength));
        }
    }

    @Override
    default List<T> leftPadTo(int length, T element) {
        final int actualLength = length();
        if (length <= actualLength) {
            return this;
        } else {
            return prependAll(Iterator.continually(element).take(length - actualLength));
        }
    }

    @Override
    default List<T> patch(int from, Iterable<? extends T> that, int replaced) {
        from = from < 0 ? 0 : from;
        replaced = replaced < 0 ? 0 : replaced;
        List<T> result = take(from).appendAll(that);
        from += replaced;
        result = result.appendAll(drop(from));
        return result;
    }

    @Override
    default Tuple2<List<T>, List<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> left = empty(), right = empty();
        for (T t : this) {
            if (predicate.test(t)) {
                left = left.prepend(t);
            } else {
                right = right.prepend(t);
            }
        }
        return Tuple.of(left.reverse(), right.reverse());
    }

    /**
     * Returns the head element without modifying the List.
     *
     * @return the first element
     * @throws java.util.NoSuchElementException if this List is empty
     */
    default T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("peek of empty list");
        }
        return head();
    }

    /**
     * Returns the head element without modifying the List.
     *
     * @return {@code None} if this List is empty, otherwise a {@code Some} containing the head element
     */
    default Option<T> peekOption() {
        return isEmpty() ? Option.none() : Option.some(head());
    }

    /**
     * Performs an action on the head element of this {@code List}.
     *
     * @param action A {@code Consumer}
     * @return this {@code List}
     */
    @Override
    default List<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return this;
    }

    @Override
    default List<List<T>> permutations() {
        if (isEmpty()) {
            return Nil.instance();
        } else {
            final List<T> tail = tail();
            if (tail.isEmpty()) {
                return of(this);
            } else {
                final List<List<T>> zero = Nil.instance();
                return distinct().foldLeft(zero, (xs, x) -> {
                    final Function<List<T>, List<T>> prepend = l -> l.prepend(x);
                    return xs.appendAll(remove(x).permutations().map(prepend));
                });
            }
        }
    }

    /**
     * Removes the head element from this List.
     *
     * @return the elements of this List without the head element
     * @throws java.util.NoSuchElementException if this List is empty
     */
    default List<T> pop() {
        if (isEmpty()) {
            throw new NoSuchElementException("pop of empty list");
        }
        return tail();
    }

    /**
     * Removes the head element from this List.
     *
     * @return {@code None} if this List is empty, otherwise a {@code Some} containing the elements of this List without the head element
     */
    default Option<List<T>> popOption() {
        return isEmpty() ? Option.none() : Option.some(pop());
    }

    /**
     * Removes the head element from this List.
     *
     * @return a tuple containing the head element and the remaining elements of this List
     * @throws java.util.NoSuchElementException if this List is empty
     */
    default Tuple2<T, List<T>> pop2() {
        if (isEmpty()) {
            throw new NoSuchElementException("pop2 of empty list");
        }
        return Tuple.of(head(), tail());
    }

    /**
     * Removes the head element from this List.
     *
     * @return {@code None} if this List is empty, otherwise {@code Some} {@code Tuple} containing the head element and the remaining elements of this List
     */
    default Option<Tuple2<T, List<T>>> pop2Option() {
        return isEmpty() ? Option.none() : Option.some(Tuple.of(head(), pop()));
    }

    @Override
    default List<T> prepend(T element) {
        return new Cons<>(element, this);
    }

    @Override
    default List<T> prependAll(Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return isEmpty() ? ofAll(elements) : ofAll(elements).reverse().foldLeft(this, List::prepend);
    }

    /**
     * Pushes a new element on top of this List.
     *
     * @param element The new element
     * @return a new {@code List} instance, containing the new element on top of this List
     */
    default List<T> push(T element) {
        return new Cons<>(element, this);
    }

    /**
     * Pushes the given elements on top of this List. A List has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements Elements, may be empty
     * @return a new {@code List} instance, containing the new elements on top of this List
     * @throws NullPointerException if elements is null
     */
    @SuppressWarnings("unchecked")
    default List<T> push(T... elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = this;
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    /**
     * Pushes the given elements on top of this List. A List has LIFO order, i.e. the last of the given elements is
     * the first which will be retrieved.
     *
     * @param elements An Iterable of elements, may be empty
     * @return a new {@code List} instance, containing the new elements on top of this List
     * @throws NullPointerException if elements is null
     */
    default List<T> pushAll(Iterable<T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        List<T> result = this;
        for (T element : elements) {
            result = result.prepend(element);
        }
        return result;
    }

    @Override
    default List<T> remove(T element) {
        final Deque<T> preceding = new ArrayDeque<>(size());
        List<T> result = this;
        boolean found = false;
        while (!found && !result.isEmpty()) {
            final T head = result.head();
            if (Objects.equals(head, element)) {
                found = true;
            } else {
                preceding.addFirst(head);
            }
            result = result.tail();
        }
        if (!found) {
            return this;
        }
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> removeFirst(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> init = empty();
        List<T> tail = this;
        while (!tail.isEmpty() && !predicate.test(tail.head())) {
            init = init.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        } else {
            return init.foldLeft(tail.tail(), List::prepend);
        }
    }

    @Override
    default List<T> removeLast(Predicate<T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final List<T> removedAndReversed = reverse().removeFirst(predicate);
        return removedAndReversed.length() == length() ? this : removedAndReversed.reverse();
    }

    @Override
    default List<T> removeAt(int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ")");
        }
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("removeAt(" + index + ") on Nil");
        }
        List<T> init = Nil.instance();
        List<T> tail = this;
        while (index > 0 && !tail.isEmpty()) {
            init = init.prepend(tail.head());
            tail = tail.tail();
            index--;
        }
        if (index > 0) {
            throw new IndexOutOfBoundsException("removeAt() on Nil");
        }
        return init.reverse().appendAll(tail.tail());
    }

    @Override
    default List<T> removeAll(T element) {
        return Collections.removeAll(this, element);
    }

    @Override
    default List<T> removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll(this, elements);
    }

    @Override
    default List<T> removeAll(Predicate<? super T> predicate) {
        return Collections.removeAll(this, predicate);
    }

    @Override
    default List<T> replace(T currentElement, T newElement) {
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        while (!tail.isEmpty() && !Objects.equals(tail.head(), currentElement)) {
            preceding = preceding.prepend(tail.head());
            tail = tail.tail();
        }
        if (tail.isEmpty()) {
            return this;
        }
        // skip the current head element because it is replaced
        List<T> result = tail.tail().prepend(newElement);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> replaceAll(T currentElement, T newElement) {
        List<T> result = Nil.instance();
        boolean changed = false;
        for (List<T> list = this; !list.isEmpty(); list = list.tail()) {
            final T head = list.head();
            if (Objects.equals(head, currentElement)) {
                result = result.prepend(newElement);
                changed = true;
            } else {
                result = result.prepend(head);
            }
        }
        return changed ? result.reverse() : this;
    }

    @Override
    default List<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    default List<T> reverse() {
        return (length() <= 1) ? this : foldLeft(empty(), List::prepend);
    }

    @Override
    default List<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    default <U> List<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, Iterator::toList);
    }

    @Override
    default <U> List<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, Iterator::toList);
    }

    @Override
    default List<T> shuffle() {
        return Collections.shuffle(this, List::ofAll);
    }

    @Override
    default List<T> slice(int beginIndex, int endIndex) {
        if (beginIndex >= endIndex || beginIndex >= length() || isEmpty()) {
            return empty();
        } else {
            List<T> result = Nil.instance();
            List<T> list = this;
            final long lowerBound = Math.max(beginIndex, 0);
            final long upperBound = Math.min(endIndex, length());
            for (int i = 0; i < upperBound; i++) {
                if (i >= lowerBound) {
                    result = result.prepend(list.head());
                }
                list = list.tail();
            }
            return result.reverse();
        }
    }

    @Override
    default Iterator<List<T>> slideBy(Function<? super T, ?> classifier) {
        return iterator().slideBy(classifier).map(List::ofAll);
    }

    @Override
    default Iterator<List<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Iterator<List<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(List::ofAll);
    }

    @Override
    default List<T> sorted() {
        return isEmpty() ? this : toJavaStream().sorted().collect(collector());
    }

    @Override
    default List<T> sorted(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return isEmpty() ? this : toJavaStream().sorted(comparator).collect(collector());
    }

    @Override
    default <U extends Comparable<? super U>> List<T> sortBy(Function<? super T, ? extends U> mapper) {
        return sortBy(U::compareTo, mapper);
    }

    @Override
    default <U> List<T> sortBy(Comparator<? super U> comparator, Function<? super T, ? extends U> mapper) {
        final Function<? super T, ? extends U> domain = Function1.of(mapper::apply).memoized();
        return toJavaStream()
                .sorted((e1, e2) -> comparator.compare(domain.apply(e1), domain.apply(e2)))
                .collect(collector());
    }

    @Override
    default Tuple2<List<T>, List<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> itt = iterator().span(predicate);
        return Tuple.of(ofAll(itt._1), ofAll(itt._2));
    }

    @Override
    default Tuple2<List<T>, List<T>> splitAt(int n) {
        if (isEmpty()) {
            return Tuple.of(empty(), empty());
        } else {
            List<T> init = Nil.instance();
            List<T> tail = this;
            while (n > 0 && !tail.isEmpty()) {
                init = init.prepend(tail.head());
                tail = tail.tail();
                n--;
            }
            return Tuple.of(init.reverse(), tail);
        }
    }

    @Override
    default Tuple2<List<T>, List<T>> splitAt(Predicate<? super T> predicate) {
        if (isEmpty()) {
            return Tuple.of(empty(), empty());
        } else {
            final Tuple2<List<T>, List<T>> t = SplitAt.splitByPredicateReversed(this, predicate);
            if (t._2.isEmpty()) {
                return Tuple.of(this, empty());
            } else {
                return Tuple.of(t._1.reverse(), t._2);
            }
        }
    }

    @Override
    default Tuple2<List<T>, List<T>> splitAtInclusive(Predicate<? super T> predicate) {
        if (isEmpty()) {
            return Tuple.of(empty(), empty());
        } else {
            final Tuple2<List<T>, List<T>> t = SplitAt.splitByPredicateReversed(this, predicate);
            if (t._2.isEmpty() || t._2.tail().isEmpty()) {
                return Tuple.of(this, empty());
            } else {
                return Tuple.of(t._1.prepend(t._2.head()).reverse(), t._2.tail());
            }
        }
    }

    @Override
    default String stringPrefix() {
        return "List";
    }

    @Override
    default List<T> subSequence(int beginIndex) {
        if (beginIndex < 0 || beginIndex > length()) {
            throw new IndexOutOfBoundsException("subSequence(" + beginIndex + ")");
        } else {
            return drop(beginIndex);
        }
    }

    @Override
    default List<T> subSequence(int beginIndex, int endIndex) {
        Collections.subSequenceRangeCheck(beginIndex, endIndex, length());
        if (beginIndex == endIndex) {
            return empty();
        } else if (beginIndex == 0 && endIndex == length()) {
            return this;
        } else {
            List<T> result = Nil.instance();
            List<T> list = this;
            for (int i = 0; i < endIndex; i++, list = list.tail()) {
                if (i >= beginIndex) {
                    result = result.prepend(list.head());
                }
            }
            return result.reverse();
        }
    }

    @Override
    List<T> tail();

    @Override
    default Option<List<T>> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    default List<T> take(int n) {
        if (n <= 0) {
            return empty();
        }
        if (n >= length()) {
            return this;
        }
        List<T> result = Nil.instance();
        List<T> list = this;
        for (int i = 0; i < n; i++, list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.reverse();
    }

    @Override
    default List<T> takeRight(int n) {
        if (n <= 0) {
            return empty();
        }
        if (n >= length()) {
            return this;
        }
        return reverse().take(n).reverse();
    }

    @Override
    default List<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    default List<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        List<T> result = Nil.instance();
        for (List<T> list = this; !list.isEmpty() && predicate.test(list.head()); list = list.tail()) {
            result = result.prepend(list.head());
        }
        return result.length() == length() ? this : result.reverse();
    }

    /**
     * Transforms this {@code List}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super List<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    default <T1, T2> Tuple2<List<T1>, List<T2>> unzip(
            Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        List<T1> xs = Nil.instance();
        List<T2> ys = Nil.instance();
        for (T element : this) {
            final Tuple2<? extends T1, ? extends T2> t = unzipper.apply(element);
            xs = xs.prepend(t._1);
            ys = ys.prepend(t._2);
        }
        return Tuple.of(xs.reverse(), ys.reverse());
    }

    @Override
    default <T1, T2, T3> Tuple3<List<T1>, List<T2>, List<T3>> unzip3(
            Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        List<T1> xs = Nil.instance();
        List<T2> ys = Nil.instance();
        List<T3> zs = Nil.instance();
        for (T element : this) {
            final Tuple3<? extends T1, ? extends T2, ? extends T3> t = unzipper.apply(element);
            xs = xs.prepend(t._1);
            ys = ys.prepend(t._2);
            zs = zs.prepend(t._3);
        }
        return Tuple.of(xs.reverse(), ys.reverse(), zs.reverse());
    }

    @Override
    default List<T> update(int index, T element) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("update(" + index + ", e) on Nil");
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("update(" + index + ", e)");
        }
        List<T> preceding = Nil.instance();
        List<T> tail = this;
        for (int i = index; i > 0; i--, tail = tail.tail()) {
            if (tail.isEmpty()) {
                throw new IndexOutOfBoundsException("update(" + index + ", e) on List of length " + length());
            }
            preceding = preceding.prepend(tail.head());
        }
        if (tail.isEmpty()) {
            throw new IndexOutOfBoundsException("update(" + index + ", e) on List of length " + length());
        }
        // skip the current head element because it is replaced
        List<T> result = tail.tail().prepend(element);
        for (T next : preceding) {
            result = result.prepend(next);
        }
        return result;
    }

    @Override
    default List<T> update(int index, Function<? super T, ? extends T> updater) {
        Objects.requireNonNull(updater, "updater is null");
        return update(index, updater.apply(get(index)));
    }

    @Override
    default <U> List<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    default <U, R> List<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    default <U> List<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default List<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    default <U> List<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return ofAll(iterator().zipWithIndex(mapper));
    }

    /**
     * Representation of the singleton empty {@code List}.
     *
     * @param <T> Component type of the List.
     */
    final class Nil<T> implements List<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private static final Nil<?> INSTANCE = new Nil<>();

        // hidden
        private Nil() {
        }

        /**
         * Returns the singleton instance of the linked list.
         *
         * @param <T> Component type of the List
         * @return the singleton instance of the linked list.
         */
        @SuppressWarnings("unchecked")
        public static <T> Nil<T> instance() {
            return (Nil<T>) INSTANCE;
        }

        @Override
        public T head() {
            throw new NoSuchElementException("head of empty list");
        }

        @Override
        public int length() {
            return 0;
        }

        @Override
        public List<T> tail() {
            throw new UnsupportedOperationException("tail of empty list");
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean equals(Object o) {
            return Collections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return Collections.hashOrdered(this);
        }

        @Override
        public String toString() {
            return stringPrefix() + "()";
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of Nil.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }

    /**
     * Non-empty {@code List}, consisting of a {@code head} and a {@code tail}.
     *
     * @param <T> Component type of the List.
     */
    // DEV NOTE: class declared final because of serialization proxy pattern (see Effective Java, 2nd ed., p. 315)
    final class Cons<T> implements List<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T head;
        private final List<T> tail;
        private final int length;

        /**
         * Creates a List consisting of a head value and a trailing List.
         *
         * @param head The head
         * @param tail The tail
         */
        private Cons(T head, List<T> tail) {
            this.head = head;
            this.tail = tail;
            this.length = 1 + tail.length();
        }

        @Override
        public T head() {
            return head;
        }

        @Override
        public int length() {
            return length;
        }

        @Override
        public List<T> tail() {
            return tail;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean equals(Object o) {
            return Collections.equals(this, o);
        }

        @Override
        public int hashCode() {
            return Collections.hashOrdered(this);
        }

        @Override
        public String toString() {
            return mkString(stringPrefix() + "(", ", ", ")");
        }

        /**
         * {@code writeReplace} method for the serialization proxy pattern.
         * <p>
         * The presence of this method causes the serialization system to emit a SerializationProxy instance instead of
         * an instance of the enclosing class.
         *
         * @return A SerializationProxy for this enclosing class.
         */
        @GwtIncompatible("The Java serialization protocol is explicitly not supported")
        private Object writeReplace() {
            return new SerializationProxy<>(this);
        }

        /**
         * {@code readObject} method for the serialization proxy pattern.
         * <p>
         * Guarantees that the serialization system will never generate a serialized instance of the enclosing class.
         *
         * @param stream An object serialization stream.
         * @throws java.io.InvalidObjectException This method will throw with the message "Proxy required".
         */
        @GwtIncompatible("The Java serialization protocol is explicitly not supported")
        private void readObject(ObjectInputStream stream) throws InvalidObjectException {
            throw new InvalidObjectException("Proxy required");
        }

        /**
         * A serialization proxy which, in this context, is used to deserialize immutable, linked Lists with final
         * instance fields.
         *
         * @param <T> The component type of the underlying list.
         */
        // DEV NOTE: The serialization proxy pattern is not compatible with non-final, i.e. extendable,
        // classes. Also, it may not be compatible with circular object graphs.
        @GwtIncompatible("The Java serialization protocol is explicitly not supported")
        private static final class SerializationProxy<T> implements Serializable {

            private static final long serialVersionUID = 1L;

            // the instance to be serialized/deserialized
            private transient Cons<T> list;

            /**
             * Constructor for the case of serialization, called by {@link Cons#writeReplace()}.
             * <p/>
             * The constructor of a SerializationProxy takes an argument that concisely represents the logical state of
             * an instance of the enclosing class.
             *
             * @param list a Cons
             */
            SerializationProxy(Cons<T> list) {
                this.list = list;
            }

            /**
             * Write an object to a serialization stream.
             *
             * @param s An object serialization stream.
             * @throws java.io.IOException If an error occurs writing to the stream.
             */
            private void writeObject(ObjectOutputStream s) throws IOException {
                s.defaultWriteObject();
                s.writeInt(list.length());
                for (List<T> l = list; !l.isEmpty(); l = l.tail()) {
                    s.writeObject(l.head());
                }
            }

            /**
             * Read an object from a deserialization stream.
             *
             * @param s An object deserialization stream.
             * @throws ClassNotFoundException If the object's class read from the stream cannot be found.
             * @throws InvalidObjectException If the stream contains no list elements.
             * @throws IOException            If an error occurs reading from the stream.
             */
            private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
                s.defaultReadObject();
                final int size = s.readInt();
                if (size <= 0) {
                    throw new InvalidObjectException("No elements");
                }
                List<T> temp = Nil.instance();
                for (int i = 0; i < size; i++) {
                    @SuppressWarnings("unchecked")
                    final T element = (T) s.readObject();
                    temp = temp.prepend(element);
                }
                list = (Cons<T>) temp.reverse();
            }

            /**
             * {@code readResolve} method for the serialization proxy pattern.
             * <p>
             * Returns a logically equivalent instance of the enclosing class. The presence of this method causes the
             * serialization system to translate the serialization proxy back into an instance of the enclosing class
             * upon deserialization.
             *
             * @return A deserialized instance of the enclosing class.
             */
            private Object readResolve() {
                return list;
            }
        }
    }
}

interface ListModule {

    interface Combinations {

        static <T> List<List<T>> apply(List<T> elements, int k) {
            if (k == 0) {
                return List.of(List.empty());
            } else {
                return elements.zipWithIndex().flatMap(
                        t -> apply(elements.drop(t._2 + 1), (k - 1)).map(c -> c.prepend(t._1))
                );
            }
        }
    }

    interface SplitAt {

        static <T> Tuple2<List<T>, List<T>> splitByPredicateReversed(List<T> source, Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            List<T> init = Nil.instance();
            List<T> tail = source;
            while (!tail.isEmpty() && !predicate.test(tail.head())) {
                init = init.prepend(tail.head());
                tail = tail.tail();
            }
            return Tuple.of(init, tail);
        }
    }
}
