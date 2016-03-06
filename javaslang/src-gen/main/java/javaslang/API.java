/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static javaslang.API.Match.*;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javaslang.collection.Iterator;
import javaslang.control.Option;

/**
 * The most basic Javaslang functionality is accessed through this API class.
 *
 * <pre><code>
 * import static javaslang.API.*;
 * </code></pre>
 *
 * <h3>For-comprehension</h3>
 * <p>
 * The {@code For}-comprehension is syntactic sugar for nested for-loops. We write
 *
 * <pre><code>
 * // lazily evaluated
 * Iterator&lt;R&gt; result = For(iterable1, iterable2, ..., iterableN).yield(f);
 * </code></pre>
 *
 * or
 *
 * <pre><code>
 * Iterator&lt;R&gt; result =
 *     For(iterable1, v1 -&gt;
 *         For(iterable2, v2 -&gt;
 *             ...
 *             For(iterableN).yield(vN -&gt; f.apply(v1, v2, ..., vN))
 *         )
 *     );
 * </code></pre>
 *
 * instead of
 *
 * <pre><code>
 * for(T1 v1 : iterable1) {
 *     for (T2 v2 : iterable2) {
 *         ...
 *         for (TN vN : iterableN) {
 *             R result = f.apply(v1, v2, ..., VN);
 *             //
 *             // We are forced to perform side effects to do s.th. meaningful with the result.
 *             //
 *         }
 *     }
 * }
 * </code></pre>
 *
 * Please note that values like Option, Try, Future, etc. are also iterable.
 * <p>
 * Given a suitable function
 * f: {@code (v1, v2, ..., vN) -> ...} and 1 &lt;= N &lt;= 8 iterables, the result is a Stream of the
 * mapped cross product elements.
 *
 * <pre><code>
 * { f(v1, v2, ..., vN) | v1 &isin; iterable1, ... vN &isin; iterableN }
 * </code></pre>
 *
 * As with all Javaslang Values, the result of a For-comprehension can be converted
 * to standard Java library and Javaslang types.
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public final class API {

    private API() {
    }

    //
    // Java type tweaks
    //

    /**
     * Runs a {@code unit} of work and returns {@code Void}. This is helpful when a return value is expected,
     * e.g. by {@code Match}:
     *
     * <pre><code>Match(i).of(
     *     Case(0, run(() -&gt; System.out.println("zero"))),
     *     Case(1, run(() -&gt; System.out.println("one"))),
     *     Case($(), run(() -&gt; System.out.println("many")))
     * )</code></pre>
     *
     * @param unit A block of code to be run.
     * @return the single instance of {@code Void}, namely {@code null}
     */
    public static Void run(Runnable unit) {
        unit.run();
        return null;
    }

    //
    // For-Comprehension
    //

    /**
     * A shortcut for {@code Iterator.ofAll(ts).flatMap(f)} which allows us to write real for-comprehensions using
     * {@code For(...).yield(...)}.
     * <p>
     * Example:
     * <pre><code>
     * For(getPersons(), person -&gt;
     *     For(person.getTweets(), tweet -&gt;
     *         For(tweet.getReplies())
     *             .yield(reply -&gt; person + ", " + tweet + ", " + reply)));
     * </code></pre>
     *
     * @param ts An iterable
     * @param f A function {@code T -> Iterable<U>}
     * @param <T> element type of {@code ts}
     * @param <U> component type of the resulting {@code Iterator}
     * @return A new Iterator
     */
    public static <T, U> Iterator<U> For(Iterable<T> ts, Function<? super T, ? extends Iterable<U>> f) {
        return Iterator.ofAll(ts).flatMap(f);
    }

    /**
     * Creates a {@code For}-comprehension of one Iterable.
     *
     * @param ts1 the 1st Iterable
     * @param <T1> component type of the 1st Iterable
     * @return a new {@code For}-comprehension of arity 1
     */
    public static <T1> For1<T1> For(Iterable<T1> ts1) {
        Objects.requireNonNull(ts1, "ts1 is null");
        return new For1<>(Iterator.ofAll(ts1));
    }

    /**
     * Creates a {@code For}-comprehension of two Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @return a new {@code For}-comprehension of arity 2
     */
    public static <T1, T2> For2<T1, T2> For(Iterable<T1> ts1, Iterable<T2> ts2) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        return new For2<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2));
    }

    /**
     * Creates a {@code For}-comprehension of three Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @return a new {@code For}-comprehension of arity 3
     */
    public static <T1, T2, T3> For3<T1, T2, T3> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        return new For3<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3));
    }

    /**
     * Creates a {@code For}-comprehension of 4 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @param <T4> component type of the 4th Iterable
     * @return a new {@code For}-comprehension of arity 4
     */
    public static <T1, T2, T3, T4> For4<T1, T2, T3, T4> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        return new For4<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3), Iterator.ofAll(ts4));
    }

    /**
     * Creates a {@code For}-comprehension of 5 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @param <T4> component type of the 4th Iterable
     * @param <T5> component type of the 5th Iterable
     * @return a new {@code For}-comprehension of arity 5
     */
    public static <T1, T2, T3, T4, T5> For5<T1, T2, T3, T4, T5> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        return new For5<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3), Iterator.ofAll(ts4), Iterator.ofAll(ts5));
    }

    /**
     * Creates a {@code For}-comprehension of 6 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @param <T4> component type of the 4th Iterable
     * @param <T5> component type of the 5th Iterable
     * @param <T6> component type of the 6th Iterable
     * @return a new {@code For}-comprehension of arity 6
     */
    public static <T1, T2, T3, T4, T5, T6> For6<T1, T2, T3, T4, T5, T6> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        return new For6<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3), Iterator.ofAll(ts4), Iterator.ofAll(ts5), Iterator.ofAll(ts6));
    }

    /**
     * Creates a {@code For}-comprehension of 7 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable
     * @param ts7 the 7th Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @param <T4> component type of the 4th Iterable
     * @param <T5> component type of the 5th Iterable
     * @param <T6> component type of the 6th Iterable
     * @param <T7> component type of the 7th Iterable
     * @return a new {@code For}-comprehension of arity 7
     */
    public static <T1, T2, T3, T4, T5, T6, T7> For7<T1, T2, T3, T4, T5, T6, T7> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        return new For7<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3), Iterator.ofAll(ts4), Iterator.ofAll(ts5), Iterator.ofAll(ts6), Iterator.ofAll(ts7));
    }

    /**
     * Creates a {@code For}-comprehension of 8 Iterables.
     *
     * @param ts1 the 1st Iterable
     * @param ts2 the 2nd Iterable
     * @param ts3 the 3rd Iterable
     * @param ts4 the 4th Iterable
     * @param ts5 the 5th Iterable
     * @param ts6 the 6th Iterable
     * @param ts7 the 7th Iterable
     * @param ts8 the 8th Iterable
     * @param <T1> component type of the 1st Iterable
     * @param <T2> component type of the 2nd Iterable
     * @param <T3> component type of the 3rd Iterable
     * @param <T4> component type of the 4th Iterable
     * @param <T5> component type of the 5th Iterable
     * @param <T6> component type of the 6th Iterable
     * @param <T7> component type of the 7th Iterable
     * @param <T8> component type of the 8th Iterable
     * @return a new {@code For}-comprehension of arity 8
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> For8<T1, T2, T3, T4, T5, T6, T7, T8> For(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7, Iterable<T8> ts8) {
        Objects.requireNonNull(ts1, "ts1 is null");
        Objects.requireNonNull(ts2, "ts2 is null");
        Objects.requireNonNull(ts3, "ts3 is null");
        Objects.requireNonNull(ts4, "ts4 is null");
        Objects.requireNonNull(ts5, "ts5 is null");
        Objects.requireNonNull(ts6, "ts6 is null");
        Objects.requireNonNull(ts7, "ts7 is null");
        Objects.requireNonNull(ts8, "ts8 is null");
        return new For8<>(Iterator.ofAll(ts1), Iterator.ofAll(ts2), Iterator.ofAll(ts3), Iterator.ofAll(ts4), Iterator.ofAll(ts5), Iterator.ofAll(ts6), Iterator.ofAll(ts7), Iterator.ofAll(ts8));
    }

    /**
     * For-comprehension with one Iterable.
     */
    public static class For1<T1> {

        private final Iterator<T1> stream1;

        private For1(Iterator<T1> stream1) {
            this.stream1 = stream1;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function<? super T1, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return stream1.map(f::apply);
        }
    }

    /**
     * For-comprehension with two Iterables.
     */
    public static class For2<T1, T2> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;

        private For2(Iterator<T1> stream1, Iterator<T2> stream2) {
            this.stream1 = stream1;
            this.stream2 = stream2;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 -> stream2.map(t2 -> f.apply(t1, t2)));
        }
    }

    /**
     * For-comprehension with three Iterables.
     */
    public static class For3<T1, T2, T3> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;

        private For3(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 -> stream3.map(t3 -> f.apply(t1, t2, t3))));
        }
    }

    /**
     * For-comprehension with 4 Iterables.
     */
    public static class For4<T1, T2, T3, T4> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;
        private final Iterator<T4> stream4;

        private For4(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3, Iterator<T4> stream4) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 -> stream4.map(t4 -> f.apply(t1, t2, t3, t4)))));
        }
    }

    /**
     * For-comprehension with 5 Iterables.
     */
    public static class For5<T1, T2, T3, T4, T5> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;
        private final Iterator<T4> stream4;
        private final Iterator<T5> stream5;

        private For5(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3, Iterator<T4> stream4, Iterator<T5> stream5) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 -> stream5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
        }
    }

    /**
     * For-comprehension with 6 Iterables.
     */
    public static class For6<T1, T2, T3, T4, T5, T6> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;
        private final Iterator<T4> stream4;
        private final Iterator<T5> stream5;
        private final Iterator<T6> stream6;

        private For6(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3, Iterator<T4> stream4, Iterator<T5> stream5, Iterator<T6> stream6) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 -> stream6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
        }
    }

    /**
     * For-comprehension with 7 Iterables.
     */
    public static class For7<T1, T2, T3, T4, T5, T6, T7> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;
        private final Iterator<T4> stream4;
        private final Iterator<T5> stream5;
        private final Iterator<T6> stream6;
        private final Iterator<T7> stream7;

        private For7(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3, Iterator<T4> stream4, Iterator<T5> stream5, Iterator<T6> stream6, Iterator<T7> stream7) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
            this.stream7 = stream7;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 ->
                stream6.flatMap(t6 -> stream7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
        }
    }

    /**
     * For-comprehension with 8 Iterables.
     */
    public static class For8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private final Iterator<T1> stream1;
        private final Iterator<T2> stream2;
        private final Iterator<T3> stream3;
        private final Iterator<T4> stream4;
        private final Iterator<T5> stream5;
        private final Iterator<T6> stream6;
        private final Iterator<T7> stream7;
        private final Iterator<T8> stream8;

        private For8(Iterator<T1> stream1, Iterator<T2> stream2, Iterator<T3> stream3, Iterator<T4> stream4, Iterator<T5> stream5, Iterator<T6> stream6, Iterator<T7> stream7, Iterator<T8> stream8) {
            this.stream1 = stream1;
            this.stream2 = stream2;
            this.stream3 = stream3;
            this.stream4 = stream4;
            this.stream5 = stream5;
            this.stream6 = stream6;
            this.stream7 = stream7;
            this.stream8 = stream8;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting Stream elements
         * @return a Stream of mapped results
         */
        public <R> Iterator<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                stream1.flatMap(t1 ->
                stream2.flatMap(t2 ->
                stream3.flatMap(t3 ->
                stream4.flatMap(t4 ->
                stream5.flatMap(t5 ->
                stream6.flatMap(t6 ->
                stream7.flatMap(t7 -> stream8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
        }
    }

    //
    // Structural Pattern Matching
    //

    // -- static Match API

    /**
     * Entry point of the match API.
     *
     * @param value a value to be matched
     * @param <T> type of the value
     * @return a new {@code Match} instance
     */
    public static <T> Match<T> Match(T value) {
        return new Match<>(value);
    }

    // -- static Case API

    // syntactic sugar for {@code Case($(predicate), f)}
    public static <T, R> Case<? super T, ? extends R> Case(Predicate<? super T> predicate, Supplier<? extends R> f) {
        Objects.requireNonNull(f, "f is null");
        return new Case0<>($(predicate), f);
    }

    // syntactic sugar for {@code Case($(predicate), () -> retVal)}
    public static <T, R> Case<? super T, ? extends R> Case(Predicate<? super T> predicate, R retVal) {
        return new Case0<>($(predicate), () -> retVal);
    }

    // syntactic sugar for {@code Case($(value), f)}
    public static <T, R> Case<? super T, ? extends R> Case(T value, Supplier<? extends R> f) {
        Objects.requireNonNull(f, "f is null");
        return new Case0<>($(value), f);
    }

    // syntactic sugar for {@code Case($(value), () -> retVal)}
    public static <T, R> Case<? super T, ? extends R> Case(T value, R retVal) {
        return new Case0<>($(value), () -> retVal);
    }

    public static <T, R> Case<? super T, ? extends R> Case(Pattern0<T> pattern, Supplier<? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case0<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern0, Supplier)}
    public static <T, R> Case<? super T, ? extends R> Case(Pattern0<T> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case0<>(pattern, () -> retVal);
    }

    public static <T, T1, R> Case<? super T, ? extends R> Case(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case1<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern1, Function)}
    public static <T, T1, R> Case<? super T, ? extends R> Case(Pattern1<T, T1> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case1<>(pattern, _1 -> retVal);
    }

    public static <T, T1, T2, R> Case<? super T, ? extends R> Case(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case2<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern2, BiFunction)}
    public static <T, T1, T2, R> Case<? super T, ? extends R> Case(Pattern2<T, T1, T2> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case2<>(pattern, (_1, _2) -> retVal);
    }

    public static <T, T1, T2, T3, R> Case<? super T, ? extends R> Case(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case3<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern3, Function3)}
    public static <T, T1, T2, T3, R> Case<? super T, ? extends R> Case(Pattern3<T, T1, T2, T3> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case3<>(pattern, (_1, _2, _3) -> retVal);
    }

    public static <T, T1, T2, T3, T4, R> Case<? super T, ? extends R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case4<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern4, Function4)}
    public static <T, T1, T2, T3, T4, R> Case<? super T, ? extends R> Case(Pattern4<T, T1, T2, T3, T4> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case4<>(pattern, (_1, _2, _3, _4) -> retVal);
    }

    public static <T, T1, T2, T3, T4, T5, R> Case<? super T, ? extends R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case5<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern5, Function5)}
    public static <T, T1, T2, T3, T4, T5, R> Case<? super T, ? extends R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case5<>(pattern, (_1, _2, _3, _4, _5) -> retVal);
    }

    public static <T, T1, T2, T3, T4, T5, T6, R> Case<? super T, ? extends R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case6<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern6, Function6)}
    public static <T, T1, T2, T3, T4, T5, T6, R> Case<? super T, ? extends R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case6<>(pattern, (_1, _2, _3, _4, _5, _6) -> retVal);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<? super T, ? extends R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case7<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern7, Function7)}
    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<? super T, ? extends R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case7<>(pattern, (_1, _2, _3, _4, _5, _6, _7) -> retVal);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<? super T, ? extends R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case8<>(pattern, f);
    }

    // syntactic sugar for {@link #Case(Pattern8, Function8)}
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<? super T, ? extends R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case8<>(pattern, (_1, _2, _3, _4, _5, _6, _7, _8) -> retVal);
    }

    // PRE-DEFINED PATTERNS

    // 1) Atomic patterns $(), $(value), $(predicate)

    /**
     * Wildcard pattern, matches any value.
     *
     * @param <T> injected type of the underlying value
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $() {
        return Pattern0.any();
    }

    /**
     * Value pattern, checks for equality.
     *
     * @param <T>       type of the prototype
     * @param prototype the value that should be equal to the underlying object
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $(T prototype) {
        return new Pattern0<T>() {
            @Override
            public boolean isApplicable(T obj) {
                return Objects.equals(obj, prototype);
            }
        };
    }

    /**
     * Guard pattern, checks if a predicate is satisfied.
     *
     * @param <T>       type of the prototype
     * @param predicate the predicate that tests a given value
     * @return a new {@code Pattern0} instance
     */
    public static <T> Pattern0<T> $(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return new Pattern0<T>() {
            @Override
            public boolean isApplicable(T obj) {
                return predicate.test(obj);
            }
        };
    }

    /**
     * Scala-like structural pattern matching for Java. Instances are obtained via {@link API#Match(Object)}.
     * @param <T> type of the object that is matched
     */
    public static final class Match<T> {

        private final T value;

        private Match(T value) {
            this.value = value;
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> R of(Case<? super T, ? extends R>... cases) {
            return option(cases).getOrElseThrow(() -> new MatchError(value));
        }

        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> Option<R> option(Case<? super T, ? extends R>... cases) {
            Objects.requireNonNull(cases, "cases is null");
            for (Case<? super T, ? extends R> _case : cases) {
                if (_case.isApplicable(value)) {
                    return Option.some(_case.apply(value));
                }
            }
            return Option.none();
        }

        // -- CASES

        public interface Case<T, R> extends PartialFunction<T, R> {
        }

        public static final class Case0<T, R> implements Case<T, R> {

            private final Pattern0<T> pattern;
            private final Supplier<? extends R> f;

            private Case0(Pattern0<T> pattern, Supplier<? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T o) {
                return f.get();
            }
        }

        public static final class Case1<T, T1, R> implements Case<T, R> {

            private final Pattern1<T, T1> pattern;
            private final Function<? super T1, ? extends R> f;

            private Case1(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                return f.apply(pattern.apply(t));
            }
        }

        public static final class Case2<T, T1, T2, R> implements Case<T, R> {

            private final Pattern2<T, T1, T2> pattern;
            private final BiFunction<? super T1, ? super T2, ? extends R> f;

            private Case2(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple2<T1, T2> r = pattern.apply(t);
                return f.apply(r._1, r._2);
            }
        }

        public static final class Case3<T, T1, T2, T3, R> implements Case<T, R> {

            private final Pattern3<T, T1, T2, T3> pattern;
            private final Function3<? super T1, ? super T2, ? super T3, ? extends R> f;

            private Case3(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple3<T1, T2, T3> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3);
            }
        }

        public static final class Case4<T, T1, T2, T3, T4, R> implements Case<T, R> {

            private final Pattern4<T, T1, T2, T3, T4> pattern;
            private final Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f;

            private Case4(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple4<T1, T2, T3, T4> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3, r._4);
            }
        }

        public static final class Case5<T, T1, T2, T3, T4, T5, R> implements Case<T, R> {

            private final Pattern5<T, T1, T2, T3, T4, T5> pattern;
            private final Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f;

            private Case5(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple5<T1, T2, T3, T4, T5> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3, r._4, r._5);
            }
        }

        public static final class Case6<T, T1, T2, T3, T4, T5, T6, R> implements Case<T, R> {

            private final Pattern6<T, T1, T2, T3, T4, T5, T6> pattern;
            private final Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f;

            private Case6(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple6<T1, T2, T3, T4, T5, T6> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3, r._4, r._5, r._6);
            }
        }

        public static final class Case7<T, T1, T2, T3, T4, T5, T6, T7, R> implements Case<T, R> {

            private final Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern;
            private final Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f;

            private Case7(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple7<T1, T2, T3, T4, T5, T6, T7> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3, r._4, r._5, r._6, r._7);
            }
        }

        public static final class Case8<T, T1, T2, T3, T4, T5, T6, T7, T8, R> implements Case<T, R> {

            private final Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern;
            private final Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f;

            private Case8(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public boolean isApplicable(T t) {
                return pattern.isApplicable(t);
            }

            @Override
            public R apply(T t) {
                final Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> r = pattern.apply(t);
                return f.apply(r._1, r._2, r._3, r._4, r._5, r._6, r._7, r._8);
            }
        }

        // -- PATTERNS

        /**
         * A Pattern is a {@link PartialFunction}. By calling {@link #isApplicable(Object)} we check, if
         * {@link #apply(Object)} can be called. If a Pattern is not applicable to an object, {@linkplain #apply(Object)}
         * must not be called.
         *
         * @param <T> An object type that has a part of type R than is matched and decomposed by this pattern
         * @param <R> Type of the part this pattern matches/decomposes
         */
        public interface Pattern<T, R> extends PartialFunction<T, R> {
        }

        // These can't be @FunctionalInterfaces because of ambiguities.
        // For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

        public static abstract class Pattern0<T> implements Pattern<T, T> {

            private static final Pattern0<Object> ANY = new Pattern0<Object>() {
                @Override
                public boolean isApplicable(Object o) {
                    return true;
                }
            };

            @SuppressWarnings("unchecked")
            public static <T> Pattern0<T> any() {
                return (Pattern0<T>) ANY;
            }

            // DEV-NOTE: We need the lower bound `Class<? super T>` instead of the more appropriate `Class<T>`
            //           because it allows us to create patterns for generic types, which would otherwise not be
            //           possible: `Pattern0<Some<String>> p = Pattern0.of(Some.class);`
            @SuppressWarnings("unchecked")
            public static <T> Pattern0<T> of(Class<? super T> type) {
                return new Pattern0<T>() {
                    @Override
                    public boolean isApplicable(T obj) {
                        return obj != null && type.isAssignableFrom(obj.getClass());
                    }
                };
            }

            private Pattern0() {
            }

            @Override
            public T apply(T obj) {
                return obj;
            }
        }

        public static abstract class Pattern1<T, T1> implements Pattern<T, T1> {

            public static <T, T1> Pattern1<T, T1> of(Class<? super T> type, Pattern<T1, ?> p1, Function<? super T, T1> unapply) {
                return new Pattern1<T, T1>() {

                    // the unapplied object
                    T1 part = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        part = unapply.apply(obj);
                        return p1.isApplicable(part);
                    }

                    @Override
                    public T1 apply(T obj) {
                        return part;
                    }
                };
            }

            private Pattern1() {
            }
        }

        public static abstract class Pattern2<T, T1, T2> implements Pattern<T, Tuple2<T1, T2>> {

            public static <T, T1, T2> Pattern2<T, T1, T2> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Function<? super T, Tuple2<T1, T2>> unapply) {
                return new Pattern2<T, T1, T2>() {

                    // the unapplied object
                    Tuple2<T1, T2> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2);
                    }

                    @Override
                    public Tuple2<T1, T2> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern2() {
            }
        }

        public static abstract class Pattern3<T, T1, T2, T3> implements Pattern<T, Tuple3<T1, T2, T3>> {

            public static <T, T1, T2, T3> Pattern3<T, T1, T2, T3> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Function<? super T, Tuple3<T1, T2, T3>> unapply) {
                return new Pattern3<T, T1, T2, T3>() {

                    // the unapplied object
                    Tuple3<T1, T2, T3> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3);
                    }

                    @Override
                    public Tuple3<T1, T2, T3> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern3() {
            }
        }

        public static abstract class Pattern4<T, T1, T2, T3, T4> implements Pattern<T, Tuple4<T1, T2, T3, T4>> {

            public static <T, T1, T2, T3, T4> Pattern4<T, T1, T2, T3, T4> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Function<? super T, Tuple4<T1, T2, T3, T4>> unapply) {
                return new Pattern4<T, T1, T2, T3, T4>() {

                    // the unapplied object
                    Tuple4<T1, T2, T3, T4> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3) &&
                                p4.isApplicable(parts._4);
                    }

                    @Override
                    public Tuple4<T1, T2, T3, T4> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern4() {
            }
        }

        public static abstract class Pattern5<T, T1, T2, T3, T4, T5> implements Pattern<T, Tuple5<T1, T2, T3, T4, T5>> {

            public static <T, T1, T2, T3, T4, T5> Pattern5<T, T1, T2, T3, T4, T5> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Function<? super T, Tuple5<T1, T2, T3, T4, T5>> unapply) {
                return new Pattern5<T, T1, T2, T3, T4, T5>() {

                    // the unapplied object
                    Tuple5<T1, T2, T3, T4, T5> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3) &&
                                p4.isApplicable(parts._4) &&
                                p5.isApplicable(parts._5);
                    }

                    @Override
                    public Tuple5<T1, T2, T3, T4, T5> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern5() {
            }
        }

        public static abstract class Pattern6<T, T1, T2, T3, T4, T5, T6> implements Pattern<T, Tuple6<T1, T2, T3, T4, T5, T6>> {

            public static <T, T1, T2, T3, T4, T5, T6> Pattern6<T, T1, T2, T3, T4, T5, T6> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Function<? super T, Tuple6<T1, T2, T3, T4, T5, T6>> unapply) {
                return new Pattern6<T, T1, T2, T3, T4, T5, T6>() {

                    // the unapplied object
                    Tuple6<T1, T2, T3, T4, T5, T6> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3) &&
                                p4.isApplicable(parts._4) &&
                                p5.isApplicable(parts._5) &&
                                p6.isApplicable(parts._6);
                    }

                    @Override
                    public Tuple6<T1, T2, T3, T4, T5, T6> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern6() {
            }
        }

        public static abstract class Pattern7<T, T1, T2, T3, T4, T5, T6, T7> implements Pattern<T, Tuple7<T1, T2, T3, T4, T5, T6, T7>> {

            public static <T, T1, T2, T3, T4, T5, T6, T7> Pattern7<T, T1, T2, T3, T4, T5, T6, T7> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Function<? super T, Tuple7<T1, T2, T3, T4, T5, T6, T7>> unapply) {
                return new Pattern7<T, T1, T2, T3, T4, T5, T6, T7>() {

                    // the unapplied object
                    Tuple7<T1, T2, T3, T4, T5, T6, T7> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3) &&
                                p4.isApplicable(parts._4) &&
                                p5.isApplicable(parts._5) &&
                                p6.isApplicable(parts._6) &&
                                p7.isApplicable(parts._7);
                    }

                    @Override
                    public Tuple7<T1, T2, T3, T4, T5, T6, T7> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern7() {
            }
        }

        public static abstract class Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> implements Pattern<T, Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

            public static <T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Pattern<T8, ?> p8, Function<? super T, Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> unapply) {
                return new Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8>() {

                    // the unapplied object
                    Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> parts = null;

                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean isApplicable(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return false;
                        }
                        parts = unapply.apply(obj);
                        return
                                p1.isApplicable(parts._1) &&
                                p2.isApplicable(parts._2) &&
                                p3.isApplicable(parts._3) &&
                                p4.isApplicable(parts._4) &&
                                p5.isApplicable(parts._5) &&
                                p6.isApplicable(parts._6) &&
                                p7.isApplicable(parts._7) &&
                                p8.isApplicable(parts._8);
                    }

                    @Override
                    public Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> apply(T obj) {
                        return parts;
                    }
                };
            }

            private Pattern8() {
            }
        }
    }
}