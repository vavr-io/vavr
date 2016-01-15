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
public interface BiMonad<M extends Kind2<M, ?, ?>, T1, T2> extends Kind2<M, T1, T2>, BiFunctor<T1, T2> {

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
    static <M extends BiMonad<M, ?, ?>, T1, T2, R1, R2> Function1<BiMonad<M, T1, T2>, BiMonad<M, R1, R2>> lift(Function<Tuple2<T1, T2>, Tuple2<R1, R2>> f) {
        return mT -> mT.bimap(f);
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, R1, R2> Function2<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, R1, R2>> lift(BiFunction<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<R1, R2>> f) {
        return (mT1, mT2) ->
                mT1.flatMapM(t1 ->
                mT2.bimap(t2 -> f.apply(t1, t2)));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, R1, R2> Function3<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, R1, R2>> lift(Function3<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.bimap(t3 -> f.apply(t1, t2, t3))));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, R1, R2> Function4<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, T41, T42>, BiMonad<M, R1, R2>> lift(Function4<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<T41, T42>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3, mT4) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.bimap(t4 -> f.apply(t1, t2, t3, t4)))));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, R1, R2> Function5<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, T41, T42>, BiMonad<M, T51, T52>, BiMonad<M, R1, R2>> lift(Function5<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<T41, T42>, Tuple2<T51, T52>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.bimap(t5 -> f.apply(t1, t2, t3, t4, t5))))));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, R1, R2> Function6<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, T41, T42>, BiMonad<M, T51, T52>, BiMonad<M, T61, T62>, BiMonad<M, R1, R2>> lift(Function6<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<T41, T42>, Tuple2<T51, T52>, Tuple2<T61, T62>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.bimap(t6 -> f.apply(t1, t2, t3, t4, t5, t6)))))));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, T71, T72, R1, R2> Function7<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, T41, T42>, BiMonad<M, T51, T52>, BiMonad<M, T61, T62>, BiMonad<M, T71, T72>, BiMonad<M, R1, R2>> lift(Function7<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<T41, T42>, Tuple2<T51, T52>, Tuple2<T61, T62>, Tuple2<T71, T72>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.flatMapM(t6 ->
                mT7.bimap(t7 -> f.apply(t1, t2, t3, t4, t5, t6, t7))))))));
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
    static <M extends BiMonad<M, ?, ?>, T11, T12, T21, T22, T31, T32, T41, T42, T51, T52, T61, T62, T71, T72, T81, T82, R1, R2> Function8<BiMonad<M, T11, T12>, BiMonad<M, T21, T22>, BiMonad<M, T31, T32>, BiMonad<M, T41, T42>, BiMonad<M, T51, T52>, BiMonad<M, T61, T62>, BiMonad<M, T71, T72>, BiMonad<M, T81, T82>, BiMonad<M, R1, R2>> lift(Function8<Tuple2<T11, T12>, Tuple2<T21, T22>, Tuple2<T31, T32>, Tuple2<T41, T42>, Tuple2<T51, T52>, Tuple2<T61, T62>, Tuple2<T71, T72>, Tuple2<T81, T82>, Tuple2<R1, R2>> f) {
        return (mT1, mT2, mT3, mT4, mT5, mT6, mT7, mT8) ->
                mT1.flatMapM(t1 ->
                mT2.flatMapM(t2 ->
                mT3.flatMapM(t3 ->
                mT4.flatMapM(t4 ->
                mT5.flatMapM(t5 ->
                mT6.flatMapM(t6 ->
                mT7.flatMapM(t7 ->
                mT8.bimap(t8 -> f.apply(t1, t2, t3, t4, t5, t6, t7, t8)))))))));
    }

    /**
     * FlatMaps this BiMonad to a new BiMonad with different component types.
     *
     * @param mapper A mapper
     * @param <U1>   1st component type of the resulting BiMonad
     * @param <U2>   2nd component type of the resulting BiMonad
     * @return a mapped {@code BiMonad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U1, U2> BiMonad<M, U1, U2> flatMapM(BiFunction<? super T1, ? super T2, ? extends Kind2<M, U1, U2>> mapper);

    /**
     * FlatMaps this BiMonad to a new BiMonad with different component types.
     *
     * @param mapper A mapper
     * @param <U1>   1st component type of the resulting BiMonad
     * @param <U2>   2nd component type of the resulting BiMonad
     * @return a mapped {@code BiMonad}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U1, U2> BiMonad<M, U1, U2> flatMapM(Function<Tuple2<T1, T2>, ? extends Kind2<M, U1, U2>> mapper);

    // -- adjusting return types of super interface methods

    @Override
    <U1, U2> BiMonad<M, U1, U2> bimap(BiFunction<? super T1, ? super T2, Tuple2<U1, U2>> mapper);

    @Override
    <U1, U2> BiMonad<M, U1, U2> bimap(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2);

    @Override
    <U1, U2> BiMonad<M, U1, U2> bimap(Function<Tuple2<T1, T2>, Tuple2<U1, U2>> f);

}