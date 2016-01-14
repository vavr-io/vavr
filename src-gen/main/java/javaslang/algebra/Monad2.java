/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.BiFunction;
import java.util.function.Function;
import javaslang.*;

/**
 * Monad with two components. See {@link Monad}.
 *
 * @param <T1> 1st component type of this monad
 * @param <T2> 2nd component type of this monad
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Monad2<M extends Kind2<M, ?, ?>, T1, T2> extends Kind2<M, T1, T2>, Functor2<T1, T2> {

    /**
     * Lifts a {@code Function} to a higher {@code Function1} that operates on Monads.
     *
     * @param <T1> 1st component type of 1st argument of f
     * @param <T2> 2nd component type of 1st argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function
     * @return a new Function1 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T1, T2, R1, R2> Function1<Monad2<M, T1, T2>, Monad2<M, R1, R2>> lift(Function<? super Tuple2<? super T1, ? super T2>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return mT -> mT.map2(t -> f.apply(t));
    }

    /**
     * Lifts a {@code BiFunction} to a higher {@code Function2} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a BiFunction
     * @return a new Function2 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, R1, R2> Function2<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, R1, R2>> lift(BiFunction<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2) ->
                mT1.flatMapM(t1 ->
                mT2.map2(t2 -> f.apply(t1, t2)));
    }

    /**
     * Lifts a {@code Function3} to a higher {@code Function3} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function3
     * @return a new Function3 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, R1, R2> Function3<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, R1, R2>> lift(Function3<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.map2(t3 -> f.apply(t1, t2, t3))));
    }

    /**
     * Lifts a {@code Function4} to a higher {@code Function4} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <T41> 1st component type of 4th argument of f
     * @param <T42> 2nd component type of 4th argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function4
     * @return a new Function4 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, R1, R2> Function4<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, T41, T42>, Monad2<M, R1, R2>> lift(Function4<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? super Tuple2<? super T41, ? super T42>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3, mT4) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.map2(t4 -> f.apply(t1, t2, t3, t4)))));
    }

    /**
     * Lifts a {@code Function5} to a higher {@code Function5} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <T41> 1st component type of 4th argument of f
     * @param <T42> 2nd component type of 4th argument of f
     * @param <T51> 1st component type of 5th argument of f
     * @param <T52> 2nd component type of 5th argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function5
     * @return a new Function5 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, R1, R2> Function5<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, T41, T42>, Monad2<M, T51, T52>, Monad2<M, R1, R2>> lift(Function5<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? super Tuple2<? super T41, ? super T42>, ? super Tuple2<? super T51, ? super T52>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.map2(t5 -> f.apply(t1, t2, t3, t4, t5))))));
    }

    /**
     * Lifts a {@code Function6} to a higher {@code Function6} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <T41> 1st component type of 4th argument of f
     * @param <T42> 2nd component type of 4th argument of f
     * @param <T51> 1st component type of 5th argument of f
     * @param <T52> 2nd component type of 5th argument of f
     * @param <T61> 1st component type of 6th argument of f
     * @param <T62> 2nd component type of 6th argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function6
     * @return a new Function6 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, R1, R2> Function6<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, T41, T42>, Monad2<M, T51, T52>, Monad2<M, T61, T62>, Monad2<M, R1, R2>> lift(Function6<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? super Tuple2<? super T41, ? super T42>, ? super Tuple2<? super T51, ? super T52>, ? super Tuple2<? super T61, ? super T62>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.map2(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
    }

    /**
     * Lifts a {@code Function7} to a higher {@code Function7} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <T41> 1st component type of 4th argument of f
     * @param <T42> 2nd component type of 4th argument of f
     * @param <T51> 1st component type of 5th argument of f
     * @param <T52> 2nd component type of 5th argument of f
     * @param <T61> 1st component type of 6th argument of f
     * @param <T62> 2nd component type of 6th argument of f
     * @param <T71> 1st component type of 7th argument of f
     * @param <T72> 2nd component type of 7th argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function7
     * @return a new Function7 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, T71, T72, R1, R2> Function7<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, T41, T42>, Monad2<M, T51, T52>, Monad2<M, T61, T62>, Monad2<M, T71, T72>, Monad2<M, R1, R2>> lift(Function7<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? super Tuple2<? super T41, ? super T42>, ? super Tuple2<? super T51, ? super T52>, ? super Tuple2<? super T61, ? super T62>, ? super Tuple2<? super T71, ? super T72>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.flatMapM(t6 ->
                mT7.map2(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
    }

    /**
     * Lifts a {@code Function8} to a higher {@code Function8} that operates on Monads.
     *
     * @param <T11> 1st component type of 1st argument of f
     * @param <T12> 2nd component type of 1st argument of f
     * @param <T21> 1st component type of 2nd argument of f
     * @param <T22> 2nd component type of 2nd argument of f
     * @param <T31> 1st component type of 3rd argument of f
     * @param <T32> 2nd component type of 3rd argument of f
     * @param <T41> 1st component type of 4th argument of f
     * @param <T42> 2nd component type of 4th argument of f
     * @param <T51> 1st component type of 5th argument of f
     * @param <T52> 2nd component type of 5th argument of f
     * @param <T61> 1st component type of 6th argument of f
     * @param <T62> 2nd component type of 6th argument of f
     * @param <T71> 1st component type of 7th argument of f
     * @param <T72> 2nd component type of 7th argument of f
     * @param <T81> 1st component type of 8th argument of f
     * @param <T82> 2nd component type of 8th argument of f
     * @param <M>  Monad type
     * @param <R1> 1st component type of result
     * @param <R2> 2nd component type of result
     * @param f a Function8
     * @return a new Function8 that lifts the given function f in a layer that operates on monads.
     */
    static <M extends Monad2<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, T71, T72, T81, T82, R1, R2> Function8<Monad2<M, T11, T12>, Monad2<M, T21, T22>, Monad2<M, T31, T32>, Monad2<M, T41, T42>, Monad2<M, T51, T52>, Monad2<M, T61, T62>, Monad2<M, T71, T72>, Monad2<M, T81, T82>, Monad2<M, R1, R2>> lift(Function8<? super Tuple2<? super T11, ? super T12>, ? super Tuple2<? super T21, ? super T22>, ? super Tuple2<? super T31, ? super T32>, ? super Tuple2<? super T41, ? super T42>, ? super Tuple2<? super T51, ? super T52>, ? super Tuple2<? super T61, ? super T62>, ? super Tuple2<? super T71, ? super T72>, ? super Tuple2<? super T81, ? super T82>, ? extends Tuple2<? extends R1, ? extends R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7, mT8) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.flatMapM(t6 ->
                mT7.flatMapM(t7 ->
                mT8.map2(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
    }

    /**
     * FlatMaps this Monad2 to a new Monad2 with different component types.
     *
     * @param mapper A mapper
     * @param <U1>   1st component type of the resulting Monad2
     * @param <U2>   2nd component type of the resulting Monad2
     * @return a mapped {@code Monad2}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U1, U2> Monad2<M, U1, U2> flatMapM(BiFunction<? super T1, ? super T2, ? extends Kind2<? extends M, ? extends U1, ? extends U2>> mapper);

    /**
     * FlatMaps this Monad2 to a new Monad2 with different component types.
     *
     * @param mapper A mapper
     * @param <U1>   1st component type of the resulting Monad2
     * @param <U2>   2nd component type of the resulting Monad2
     * @return a mapped {@code Monad2}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U1, U2> Monad2<M, U1, U2> flatMapM(Function<? super Tuple2<? super T1, ? super T2>, ? extends Kind2<? extends M, ? extends U1, ? extends U2>> mapper);

    // -- adjusting return types of super interface methods

    @Override
    <U1, U2> Monad2<M, U1, U2> map(BiFunction<? super T1, ? super T2, ? extends Tuple2<? extends U1, ? extends U2>> mapper);

    @Override
    <U1, U2> Monad2<M, U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2);

    @Override
    <U1, U2> Monad2<M, U1, U2> map2(Function<? super Tuple2<? super T1, ? super T2>, ? extends Tuple2<? extends U1, ? extends U2>> f);

}