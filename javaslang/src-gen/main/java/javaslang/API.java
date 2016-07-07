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
     *     Case(is(0), i -&gt; run(() -&gt; System.out.println("zero"))),
     *     Case(is(1), i -&gt; run(() -&gt; System.out.println("one"))),
     *     Case($(), o -&gt; run(() -&gt; System.out.println("many")))
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
        return new For1<>(ts1);
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
        return new For2<>(ts1, ts2);
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
        return new For3<>(ts1, ts2, ts3);
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
        return new For4<>(ts1, ts2, ts3, ts4);
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
        return new For5<>(ts1, ts2, ts3, ts4, ts5);
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
        return new For6<>(ts1, ts2, ts3, ts4, ts5, ts6);
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
        return new For7<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7);
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
        return new For8<>(ts1, ts2, ts3, ts4, ts5, ts6, ts7, ts8);
    }

    /**
     * For-comprehension with one Iterable.
     */
    public static class For1<T1> {

        private final Iterable<T1> ts1;

        private For1(Iterable<T1> ts1) {
            this.ts1 = ts1;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function<? super T1, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return Iterator.ofAll(ts1).map(f);
        }
    }

    /**
     * For-comprehension with two Iterables.
     */
    public static class For2<T1, T2> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;

        private For2(Iterable<T1> ts1, Iterable<T2> ts2) {
            this.ts1 = ts1;
            this.ts2 = ts2;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(BiFunction<? super T1, ? super T2, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).map(t2 -> f.apply(t1, t2)));
        }
    }

    /**
     * For-comprehension with three Iterables.
     */
    public static class For3<T1, T2, T3> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;

        private For3(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).map(t3 -> f.apply(t1, t2, t3))));
        }
    }

    /**
     * For-comprehension with 4 Iterables.
     */
    public static class For4<T1, T2, T3, T4> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;
        private final Iterable<T4> ts4;

        private For4(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
            this.ts4 = ts4;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).flatMap(t3 ->
                Iterator.ofAll(ts4).map(t4 -> f.apply(t1, t2, t3, t4)))));
        }
    }

    /**
     * For-comprehension with 5 Iterables.
     */
    public static class For5<T1, T2, T3, T4, T5> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;
        private final Iterable<T4> ts4;
        private final Iterable<T5> ts5;

        private For5(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
            this.ts4 = ts4;
            this.ts5 = ts5;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).flatMap(t3 ->
                Iterator.ofAll(ts4).flatMap(t4 ->
                Iterator.ofAll(ts5).map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
        }
    }

    /**
     * For-comprehension with 6 Iterables.
     */
    public static class For6<T1, T2, T3, T4, T5, T6> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;
        private final Iterable<T4> ts4;
        private final Iterable<T5> ts5;
        private final Iterable<T6> ts6;

        private For6(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
            this.ts4 = ts4;
            this.ts5 = ts5;
            this.ts6 = ts6;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).flatMap(t3 ->
                Iterator.ofAll(ts4).flatMap(t4 ->
                Iterator.ofAll(ts5).flatMap(t5 ->
                Iterator.ofAll(ts6).map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
        }
    }

    /**
     * For-comprehension with 7 Iterables.
     */
    public static class For7<T1, T2, T3, T4, T5, T6, T7> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;
        private final Iterable<T4> ts4;
        private final Iterable<T5> ts5;
        private final Iterable<T6> ts6;
        private final Iterable<T7> ts7;

        private For7(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
            this.ts4 = ts4;
            this.ts5 = ts5;
            this.ts6 = ts6;
            this.ts7 = ts7;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).flatMap(t3 ->
                Iterator.ofAll(ts4).flatMap(t4 ->
                Iterator.ofAll(ts5).flatMap(t5 ->
                Iterator.ofAll(ts6).flatMap(t6 ->
                Iterator.ofAll(ts7).map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
        }
    }

    /**
     * For-comprehension with 8 Iterables.
     */
    public static class For8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private final Iterable<T1> ts1;
        private final Iterable<T2> ts2;
        private final Iterable<T3> ts3;
        private final Iterable<T4> ts4;
        private final Iterable<T5> ts5;
        private final Iterable<T6> ts6;
        private final Iterable<T7> ts7;
        private final Iterable<T8> ts8;

        private For8(Iterable<T1> ts1, Iterable<T2> ts2, Iterable<T3> ts3, Iterable<T4> ts4, Iterable<T5> ts5, Iterable<T6> ts6, Iterable<T7> ts7, Iterable<T8> ts8) {
            this.ts1 = ts1;
            this.ts2 = ts2;
            this.ts3 = ts3;
            this.ts4 = ts4;
            this.ts5 = ts5;
            this.ts6 = ts6;
            this.ts7 = ts7;
            this.ts8 = ts8;
        }

        /**
         * Yields a result for elements of the cross product of the underlying Iterables.
         *
         * @param f a function that maps an element of the cross product to a result
         * @param <R> type of the resulting {@code Iterator} elements
         * @return an {@code Iterator} of mapped results
         */
        public <R> Iterator<R> yield(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            Objects.requireNonNull(f, "f is null");
            return
                Iterator.ofAll(ts1).flatMap(t1 ->
                Iterator.ofAll(ts2).flatMap(t2 ->
                Iterator.ofAll(ts3).flatMap(t3 ->
                Iterator.ofAll(ts4).flatMap(t4 ->
                Iterator.ofAll(ts5).flatMap(t5 ->
                Iterator.ofAll(ts6).flatMap(t6 ->
                Iterator.ofAll(ts7).flatMap(t7 ->
                Iterator.ofAll(ts8).map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
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
    @GwtIncompatible
    public static <T> Match<T> Match(T value) {
        return new Match<>(value);
    }

    // -- static Case API

    // - Value

    // Note: The signature `<T, R> Case<T, R> Case(T value, $FunctionType<? super T, ? extends R> f)` leads to ambiguities!

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(T value, Supplier<? extends R> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case0<>($(value), ignored -> supplier.get());
    }

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(T value, R retVal) {
        return new Case0<>($(value), ignored -> retVal);
    }

    // - Predicate

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Predicate<? super T> predicate, Function<? super T, ? extends R> f) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(f, "f is null");
        return new Case0<>($(predicate), f);
    }

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Predicate<? super T> predicate, Supplier<? extends R> supplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case0<>($(predicate), ignored -> supplier.get());
    }

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Predicate<? super T> predicate, R retVal) {
        Objects.requireNonNull(predicate, "predicate is null");
        return new Case0<>($(predicate), ignored -> retVal);
    }

    // - Pattern0

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, Function<? super T, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case0<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case0<>(pattern, ignored -> supplier.get());
    }

    @GwtIncompatible
    public static <T, R> Case<T, R> Case(Pattern0<T> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case0<>(pattern, ignored -> retVal);
    }

    // - Pattern1

    @GwtIncompatible
    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case1<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case1<>(pattern, _1 -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case1<>(pattern, _1 -> retVal);
    }

    // - Pattern2

    @GwtIncompatible
    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case2<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case2<>(pattern, (_1, _2) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case2<>(pattern, (_1, _2) -> retVal);
    }

    // - Pattern3

    @GwtIncompatible
    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case3<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case3<>(pattern, (_1, _2, _3) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case3<>(pattern, (_1, _2, _3) -> retVal);
    }

    // - Pattern4

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case4<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case4<>(pattern, (_1, _2, _3, _4) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case4<>(pattern, (_1, _2, _3, _4) -> retVal);
    }

    // - Pattern5

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case5<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case5<>(pattern, (_1, _2, _3, _4, _5) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case5<>(pattern, (_1, _2, _3, _4, _5) -> retVal);
    }

    // - Pattern6

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case6<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case6<>(pattern, (_1, _2, _3, _4, _5, _6) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case6<>(pattern, (_1, _2, _3, _4, _5, _6) -> retVal);
    }

    // - Pattern7

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case7<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case7<>(pattern, (_1, _2, _3, _4, _5, _6, _7) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, R retVal) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case7<>(pattern, (_1, _2, _3, _4, _5, _6, _7) -> retVal);
    }

    // - Pattern8

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(f, "f is null");
        return new Case8<>(pattern, f);
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Supplier<? extends R> supplier) {
        Objects.requireNonNull(pattern, "pattern is null");
        Objects.requireNonNull(supplier, "supplier is null");
        return new Case8<>(pattern, (_1, _2, _3, _4, _5, _6, _7, _8) -> supplier.get());
    }

    @GwtIncompatible
    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, R retVal) {
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
    @GwtIncompatible
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
    @GwtIncompatible
    public static <T> Pattern0<T> $(T prototype) {
        return new Pattern0<T>() {
            @Override
            public Option<T> apply(T obj) {
                return Objects.equals(obj, prototype) ? Option.some(obj) : Option.none();
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
    @GwtIncompatible
    public static <T> Pattern0<T> $(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return new Pattern0<T>() {
            @Override
            public Option<T> apply(T obj) {
                return predicate.test(obj) ? Option.some(obj) : Option.none();
            }
        };
    }

    /**
     * Scala-like structural pattern matching for Java. Instances are obtained via {@link API#Match(Object)}.
     * @param <T> type of the object that is matched
     */
    @GwtIncompatible
    public static final class Match<T> {

        private final T value;

        private Match(T value) {
            this.value = value;
        }

        // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> R of(Case<? extends T, ? extends R>... cases) {
            return option(cases).getOrElseThrow(() -> new MatchError(value));
        }

        // JDK fails here without "unchecked", Eclipse complains that it is unnecessary
        @SuppressWarnings({ "unchecked", "varargs" })
        @SafeVarargs
        public final <R> Option<R> option(Case<? extends T, ? extends R>... cases) {
            Objects.requireNonNull(cases, "cases is null");
            for (Case<? extends T, ? extends R> _case : cases) {
                final Option<R> result = ((Case<T, R>) _case).apply(value);
                if (result.isDefined()) {
                    return result;
                }
            }
            return Option.none();
        }

        // -- CASES

        // javac needs fqn's here
        public interface Case<T, R> extends java.util.function.Function<T, javaslang.control.Option<R>> {
        }

        public static final class Case0<T, R> implements Case<T, R> {

            private final Pattern0<T> pattern;
            private final Function<? super T, ? extends R> f;

            private Case0(Pattern0<T> pattern, Function<? super T, ? extends R> f) {
                this.pattern = pattern;
                this.f = f;
            }

            @Override
            public Option<R> apply(T o) {
                return pattern.apply(o).map(f);
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(f);
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3, t._4));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
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
            public Option<R> apply(T obj) {
                return pattern.apply(obj).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
            }
        }

        // -- PATTERNS

        /**
         * A Pattern is a partial {@link Function} in the sense that a function applications returns an
         * optional result of type {@code Option<R>}.
         *
         * @param <T> Class type that is matched by this pattern
         * @param <R> Type of the single or composite part this pattern decomposes
         */
        // javac needs fqn's here
        public interface Pattern<T, R> extends java.util.function.Function<T, javaslang.control.Option<R>> {
        }

        // These can't be @FunctionalInterfaces because of ambiguities.
        // For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

        public static abstract class Pattern0<T> implements Pattern<T, T> {

            private static final Pattern0<Object> ANY = new Pattern0<Object>() {
                @Override
                public Option<Object> apply(Object o) {
                    return Option.some(o);
                }
            };

            @SuppressWarnings("unchecked")
            public static <T> Pattern0<T> any() {
                return (Pattern0<T>) ANY;
            }

            // DEV-NOTE: We need the lower bound `Class<? super T>` instead of the more appropriate `Class<T>`
            //           because it allows us to create patterns for generic types, which would otherwise not be
            //           possible: `Pattern0<Some<String>> p = Pattern0.of(Some.class);`
            public static <T> Pattern0<T> of(Class<? super T> type) {
                return new Pattern0<T>() {
                    @Override
                    public Option<T> apply(T obj) {
                        return (obj != null && type.isAssignableFrom(obj.getClass())) ? Option.some(obj) : Option.none();
                    }
                };
            }

            private Pattern0() {
            }
        }

        public static abstract class Pattern1<T, T1> implements Pattern<T, T1> {

            public static <T, T1 extends U1, U1> Pattern1<T, T1> of(Class<? super T> type, Pattern<T1, ?> p1, Function<T, Tuple1<U1>> unapply) {
                return new Pattern1<T, T1>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<T1> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            return unapply.apply(obj).apply(u1 -> ((Pattern<U1, ?>) p1).apply(u1).map(_1 -> (T1) u1));
                        }
                    }
                };
            }

            private Pattern1() {
            }
        }

        public static abstract class Pattern2<T, T1, T2> implements Pattern<T, Tuple2<T1, T2>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2> Pattern2<T, T1, T2> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Function<T, Tuple2<U1, U2>> unapply) {
                return new Pattern2<T, T1, T2>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple2<T1, T2>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple2<U1, U2> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).map(_2 -> (Tuple2<T1, T2>) unapplied)
                            ));
                        }
                    }
                };
            }

            private Pattern2() {
            }
        }

        public static abstract class Pattern3<T, T1, T2, T3> implements Pattern<T, Tuple3<T1, T2, T3>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3> Pattern3<T, T1, T2, T3> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Function<T, Tuple3<U1, U2, U3>> unapply) {
                return new Pattern3<T, T1, T2, T3>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple3<T1, T2, T3>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple3<U1, U2, U3> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).map(_3 -> (Tuple3<T1, T2, T3>) unapplied)
                            )));
                        }
                    }
                };
            }

            private Pattern3() {
            }
        }

        public static abstract class Pattern4<T, T1, T2, T3, T4> implements Pattern<T, Tuple4<T1, T2, T3, T4>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4> Pattern4<T, T1, T2, T3, T4> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Function<T, Tuple4<U1, U2, U3, U4>> unapply) {
                return new Pattern4<T, T1, T2, T3, T4>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple4<T1, T2, T3, T4>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple4<U1, U2, U3, U4> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3, u4) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).flatMap(_3 ->
                                    ((Pattern<U4, ?>) p4).apply(u4).map(_4 -> (Tuple4<T1, T2, T3, T4>) unapplied)
                            ))));
                        }
                    }
                };
            }

            private Pattern4() {
            }
        }

        public static abstract class Pattern5<T, T1, T2, T3, T4, T5> implements Pattern<T, Tuple5<T1, T2, T3, T4, T5>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5> Pattern5<T, T1, T2, T3, T4, T5> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Function<T, Tuple5<U1, U2, U3, U4, U5>> unapply) {
                return new Pattern5<T, T1, T2, T3, T4, T5>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple5<T1, T2, T3, T4, T5>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple5<U1, U2, U3, U4, U5> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3, u4, u5) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).flatMap(_3 ->
                                    ((Pattern<U4, ?>) p4).apply(u4).flatMap(_4 ->
                                    ((Pattern<U5, ?>) p5).apply(u5).map(_5 -> (Tuple5<T1, T2, T3, T4, T5>) unapplied)
                            )))));
                        }
                    }
                };
            }

            private Pattern5() {
            }
        }

        public static abstract class Pattern6<T, T1, T2, T3, T4, T5, T6> implements Pattern<T, Tuple6<T1, T2, T3, T4, T5, T6>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6> Pattern6<T, T1, T2, T3, T4, T5, T6> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Function<T, Tuple6<U1, U2, U3, U4, U5, U6>> unapply) {
                return new Pattern6<T, T1, T2, T3, T4, T5, T6>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple6<T1, T2, T3, T4, T5, T6>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple6<U1, U2, U3, U4, U5, U6> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3, u4, u5, u6) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).flatMap(_3 ->
                                    ((Pattern<U4, ?>) p4).apply(u4).flatMap(_4 ->
                                    ((Pattern<U5, ?>) p5).apply(u5).flatMap(_5 ->
                                    ((Pattern<U6, ?>) p6).apply(u6).map(_6 -> (Tuple6<T1, T2, T3, T4, T5, T6>) unapplied)
                            ))))));
                        }
                    }
                };
            }

            private Pattern6() {
            }
        }

        public static abstract class Pattern7<T, T1, T2, T3, T4, T5, T6, T7> implements Pattern<T, Tuple7<T1, T2, T3, T4, T5, T6, T7>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6, T7 extends U7, U7> Pattern7<T, T1, T2, T3, T4, T5, T6, T7> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Function<T, Tuple7<U1, U2, U3, U4, U5, U6, U7>> unapply) {
                return new Pattern7<T, T1, T2, T3, T4, T5, T6, T7>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple7<U1, U2, U3, U4, U5, U6, U7> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3, u4, u5, u6, u7) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).flatMap(_3 ->
                                    ((Pattern<U4, ?>) p4).apply(u4).flatMap(_4 ->
                                    ((Pattern<U5, ?>) p5).apply(u5).flatMap(_5 ->
                                    ((Pattern<U6, ?>) p6).apply(u6).flatMap(_6 ->
                                    ((Pattern<U7, ?>) p7).apply(u7).map(_7 -> (Tuple7<T1, T2, T3, T4, T5, T6, T7>) unapplied)
                            )))))));
                        }
                    }
                };
            }

            private Pattern7() {
            }
        }

        public static abstract class Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> implements Pattern<T, Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

            public static <T, T1 extends U1, U1, T2 extends U2, U2, T3 extends U3, U3, T4 extends U4, U4, T5 extends U5, U5, T6 extends U6, U6, T7 extends U7, U7, T8 extends U8, U8> Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> of(Class<? super T> type, Pattern<T1, ?> p1, Pattern<T2, ?> p2, Pattern<T3, ?> p3, Pattern<T4, ?> p4, Pattern<T5, ?> p5, Pattern<T6, ?> p6, Pattern<T7, ?> p7, Pattern<T8, ?> p8, Function<T, Tuple8<U1, U2, U3, U4, U5, U6, U7, U8>> unapply) {
                return new Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8>() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> apply(T obj) {
                        if (obj == null || !type.isAssignableFrom(obj.getClass())) {
                            return Option.none();
                        } else {
                            final Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> unapplied = unapply.apply(obj);
                            return unapplied.apply((u1, u2, u3, u4, u5, u6, u7, u8) ->
                                    ((Pattern<U1, ?>) p1).apply(u1).flatMap(_1 ->
                                    ((Pattern<U2, ?>) p2).apply(u2).flatMap(_2 ->
                                    ((Pattern<U3, ?>) p3).apply(u3).flatMap(_3 ->
                                    ((Pattern<U4, ?>) p4).apply(u4).flatMap(_4 ->
                                    ((Pattern<U5, ?>) p5).apply(u5).flatMap(_5 ->
                                    ((Pattern<U6, ?>) p6).apply(u6).flatMap(_6 ->
                                    ((Pattern<U7, ?>) p7).apply(u7).flatMap(_7 ->
                                    ((Pattern<U8, ?>) p8).apply(u8).map(_8 -> (Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>) unapplied)
                            ))))))));
                        }
                    }
                };
            }

            private Pattern8() {
            }
        }
    }
}