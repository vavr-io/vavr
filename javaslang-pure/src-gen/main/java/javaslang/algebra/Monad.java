/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.BiFunction;
import java.util.function.Function;
import javaslang.*;
import javaslang.collection.List;

/**
 * Defines a Monad by generalizing the flatMap function.
 * <p>
 * A {@code Monad} is a {@link Functor} with a {@code flatMap} method that satisfies the Monad laws, also known
 * as the three control laws:
 * <p>
 * Let
 * <ul>
 * <li>{@code A}, {@code B}, {@code C} be types</li>
 * <li>{@code unit: A -> Monad<A>} a constructor</li>
 * <li>{@code f: A -> Monad<B>}, {@code g: B -> Monad<C>} functions</li>
 * <li>{@code a} be an object of type {@code A}</li>
 * <li>{@code m} be an object of type {@code Monad<A>}</li>
 * </ul>
 * Then all instances of the {@code Monad} interface should obey the three control laws:
 * <ul>
 * <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 * <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 * <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))}</li>
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
 *
 * @param <M> Type M&lt;?&gt; of a monadic instance, which is covered by this Monad
 * @param <T> component type of this monad
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public interface Monad<M extends Kind1<M, ?>, T> extends Functor<T> {

    /**
     * FlatMaps this Monad to a new Monad with different component type.
     * <p>
     * FlatMap is the sequence operation for functions and behaves like the imperative {@code ;}.
     * <p>
     * If the previous results are needed, flatMap cascades:
     * <pre>
     * <code>
     * m1().flatMap(result1 -&gt;
     *      m2(result1).flatMap(result2 -&gt;
     *          m3(result1, result2).flatMap(result3 -&gt;
     *              ...
     *          )
     *      )
     * );
     * </code>
     * </pre>
     * If only the last result is needed, flatMap may be used sequentially:
     * <pre>
     * <code>
     * m1().flatMap(this::m2)
     *     .flatMap(this::m3)
     *     .flatMap(...);
     * </code>
     * </pre>
     *
     * @param mapper A mapper
     * @param <U>    Component type of the mapped {@code Monad}
     * @return a mapped {@code Monad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> Monad<M, U> flatMap(Function<? super T, ? extends Monad<M, U>> mapper);

    @Override
    <U> Monad<M, U> map(Function<? super T, ? extends U> mapper);

    /**
     * Pulls the monadic instance of type M&lt;?&gt; out of this Monad view.
     * This instance can be safely casted to type M&lt;T&gt;.
     *
     * @return the monadic instance wrapped by this Monad
     */
    Kind1<M, T> narrow();

    // static factory methods

    static <T> Monad<List<?>, T> of(List<T> list) {
        return new Monad<List<?>, T>() {
            @Override
            public <U> Monad<List<?>, U> flatMap(Function<? super T, ? extends Monad<List<?>, U>> f) {
                return Monad.of(list.flatMap((T t) -> (List<U>) f.apply(t).narrow()));
            }
            @Override
            public <U> Monad<List<?>, U> map(Function<? super T, ? extends U> f) {
                return Monad.of(list.map(f));
            }
            @Override
            public List<T> narrow() {
                return list;
            }
        };
    }

    // lifting functions

    /**
     * Lifts a {@code Function} to a higher {@code Lifted1} function that operates on Monads.
     *
     * @param <T> 1st argument type of f
     * @param <R> result type of f
     * @param f a Function
     * @return a new Lifted1 function that lifts the given function f in a layer that operates on monads.
     */
    static <T, R> Lifted1<T, R> lift(Function<? super T, ? extends R> f) {
        return new Lifted1<T, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T> m) {
                return m.map(f);
            }
        };
    }

    /**
     * Lifts a {@code BiFunction} to a higher {@code Lifted2} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <R> result type of f
     * @param f a BiFunction
     * @return a new Lifted2 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, R> Lifted2<T1, T2, R> lift(BiFunction<? super T1, ? super T2, ? extends R> f) {
        return new Lifted2<T1, T2, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2) {
                return
                    m1.flatMap(t1 ->
                    m2.map(t2 -> f.apply(t1, t2)));
            }
        };
    }

    /**
     * Lifts a {@code Function3} to a higher {@code Lifted3} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <R> result type of f
     * @param f a Function3
     * @return a new Lifted3 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, R> Lifted3<T1, T2, T3, R> lift(Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        return new Lifted3<T1, T2, T3, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.map(t3 -> f.apply(t1, t2, t3))));
            }
        };
    }

    /**
     * Lifts a {@code Function4} to a higher {@code Lifted4} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <R> result type of f
     * @param f a Function4
     * @return a new Lifted4 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, R> Lifted4<T1, T2, T3, T4, R> lift(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        return new Lifted4<T1, T2, T3, T4, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.flatMap(t3 ->
                    m4.map(t4 -> f.apply(t1, t2, t3, t4)))));
            }
        };
    }

    /**
     * Lifts a {@code Function5} to a higher {@code Lifted5} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <R> result type of f
     * @param f a Function5
     * @return a new Lifted5 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, R> Lifted5<T1, T2, T3, T4, T5, R> lift(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        return new Lifted5<T1, T2, T3, T4, T5, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.flatMap(t3 ->
                    m4.flatMap(t4 ->
                    m5.map(t5 -> f.apply(t1, t2, t3, t4, t5))))));
            }
        };
    }

    /**
     * Lifts a {@code Function6} to a higher {@code Lifted6} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <R> result type of f
     * @param f a Function6
     * @return a new Lifted6 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, R> Lifted6<T1, T2, T3, T4, T5, T6, R> lift(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        return new Lifted6<T1, T2, T3, T4, T5, T6, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.flatMap(t3 ->
                    m4.flatMap(t4 ->
                    m5.flatMap(t5 ->
                    m6.map(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
            }
        };
    }

    /**
     * Lifts a {@code Function7} to a higher {@code Lifted7} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <T7> 7th argument type of f
     * @param <R> result type of f
     * @param f a Function7
     * @return a new Lifted7 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, T7, R> Lifted7<T1, T2, T3, T4, T5, T6, T7, R> lift(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        return new Lifted7<T1, T2, T3, T4, T5, T6, T7, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6, Monad<M, T7> m7) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.flatMap(t3 ->
                    m4.flatMap(t4 ->
                    m5.flatMap(t5 ->
                    m6.flatMap(t6 ->
                    m7.map(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
            }
        };
    }

    /**
     * Lifts a {@code Function8} to a higher {@code Lifted8} function that operates on Monads.
     *
     * @param <T1> 1st argument type of f
     * @param <T2> 2nd argument type of f
     * @param <T3> 3rd argument type of f
     * @param <T4> 4th argument type of f
     * @param <T5> 5th argument type of f
     * @param <T6> 6th argument type of f
     * @param <T7> 7th argument type of f
     * @param <T8> 8th argument type of f
     * @param <R> result type of f
     * @param f a Function8
     * @return a new Lifted8 function that lifts the given function f in a layer that operates on monads.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, R> Lifted8<T1, T2, T3, T4, T5, T6, T7, T8, R> lift(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        return new Lifted8<T1, T2, T3, T4, T5, T6, T7, T8, R>() {
            @Override
            public <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6, Monad<M, T7> m7, Monad<M, T8> m8) {
                return
                    m1.flatMap(t1 ->
                    m2.flatMap(t2 ->
                    m3.flatMap(t3 ->
                    m4.flatMap(t4 ->
                    m5.flatMap(t5 ->
                    m6.flatMap(t6 ->
                    m7.flatMap(t7 ->
                    m8.map(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
            }
        };
    }

    // -- lifted types

    /**
     * Represents a function {@code T -> R}
     * lifted to {@code M<T> -> M<R>}.
     *
     * @param <T> 1st argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted1<T, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T> m);
    }

    /**
     * Represents a function {@code (T1, T2) -> R}
     * lifted to {@code (M<T1>, M<T2>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted2<T1, T2, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2);
    }

    /**
     * Represents a function {@code (T1, T2, T3) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted3<T1, T2, T3, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3);
    }

    /**
     * Represents a function {@code (T1, T2, T3, T4) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>, M<T4>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <T4> 4th argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted4<T1, T2, T3, T4, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4);
    }

    /**
     * Represents a function {@code (T1, T2, T3, T4, T5) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>, M<T4>, M<T5>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <T4> 4th argument type
     * @param <T5> 5th argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted5<T1, T2, T3, T4, T5, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5);
    }

    /**
     * Represents a function {@code (T1, T2, T3, T4, T5, T6) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>, M<T4>, M<T5>, M<T6>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <T4> 4th argument type
     * @param <T5> 5th argument type
     * @param <T6> 6th argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted6<T1, T2, T3, T4, T5, T6, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6);
    }

    /**
     * Represents a function {@code (T1, T2, T3, T4, T5, T6, T7) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>, M<T4>, M<T5>, M<T6>, M<T7>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <T4> 4th argument type
     * @param <T5> 5th argument type
     * @param <T6> 6th argument type
     * @param <T7> 7th argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted7<T1, T2, T3, T4, T5, T6, T7, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6, Monad<M, T7> m7);
    }

    /**
     * Represents a function {@code (T1, T2, T3, T4, T5, T6, T7, T8) -> R}
     * lifted to {@code (M<T1>, M<T2>, M<T3>, M<T4>, M<T5>, M<T6>, M<T7>, M<T8>) -> M<R>}.
     *
     * @param <T1> 1st argument type
     * @param <T2> 2nd argument type
     * @param <T3> 3rd argument type
     * @param <T4> 4th argument type
     * @param <T5> 5th argument type
     * @param <T6> 6th argument type
     * @param <T7> 7th argument type
     * @param <T8> 8th argument type
     * @param <R> result type
     */
    // DEV-NOTE: intentionally not a @FunctionalInterface
    interface Lifted8<T1, T2, T3, T4, T5, T6, T7, T8, R> {
        <M extends Kind1<M, ?>> Monad<M, R> apply(Monad<M, T1> m1, Monad<M, T2> m2, Monad<M, T3> m3, Monad<M, T4> m4, Monad<M, T5> m5, Monad<M, T6> m6, Monad<M, T7> m7, Monad<M, T8> m8);
    }
}