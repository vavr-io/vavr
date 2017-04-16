/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.BiFunction;
import java.util.function.Function;
import javaslang.collection.List;
import javaslang.concurrent.Future;

import javaslang.Function3;
import javaslang.Function4;
import javaslang.Function5;
import javaslang.Function6;
import javaslang.Function7;
import javaslang.Function8;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.concurrent.Future;

/**
 * Applicative helpers
 *
 * @author Daniel Dietrich
 * @since 2.1.0
 */
public final class Applicative {

    private Applicative() {}

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2> Function<Option<T1>, Option<T2>> liftOption(Function<? super T1, ? extends T2> f) {
      return (a1) -> a1.map(b1 -> f.apply(b1));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3> BiFunction<Option<T1>, Option<T2>, Option<T3>> liftOption(BiFunction<? super T1, ? super T2, ? extends T3> f) {
      return (a1, a2) -> a1.flatMap(b1 -> a2.map(b2 -> f.apply(b1, b2)));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4> Function3<Option<T1>, Option<T2>, Option<T3>, Option<T4>> liftOption(Function3<? super T1, ? super T2, ? super T3, ? extends T4> f) {
      return (a1, a2, a3) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.map(b3 -> f.apply(b1, b2, b3))));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5> Function4<Option<T1>, Option<T2>, Option<T3>, Option<T4>, Option<T5>> liftOption(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T5> f) {
      return (a1, a2, a3, a4) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.map(b4 -> f.apply(b1, b2, b3, b4)))));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6> Function5<Option<T1>, Option<T2>, Option<T3>, Option<T4>, Option<T5>, Option<T6>> liftOption(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends T6> f) {
      return (a1, a2, a3, a4, a5) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.map(b5 -> f.apply(b1, b2, b3, b4, b5))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Function6<Option<T1>, Option<T2>, Option<T3>, Option<T4>, Option<T5>, Option<T6>, Option<T7>> liftOption(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends T7> f) {
      return (a1, a2, a3, a4, a5, a6) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.map(b6 -> f.apply(b1, b2, b3, b4, b5, b6)))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Function7<Option<T1>, Option<T2>, Option<T3>, Option<T4>, Option<T5>, Option<T6>, Option<T7>, Option<T8>> liftOption(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends T8> f) {
      return (a1, a2, a3, a4, a5, a6, a7) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.map(b7 -> f.apply(b1, b2, b3, b4, b5, b6, b7))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Option} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Function8<Option<T1>, Option<T2>, Option<T3>, Option<T4>, Option<T5>, Option<T6>, Option<T7>, Option<T8>, Option<T9>> liftOption(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends T9> f) {
      return (a1, a2, a3, a4, a5, a6, a7, a8) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.flatMap(b7 -> a8.map(b8 -> f.apply(b1, b2, b3, b4, b5, b6, b7, b8)))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2> Function<Try<T1>, Try<T2>> liftTry(Function<? super T1, ? extends T2> f) {
      return (a1) -> a1.map(b1 -> f.apply(b1));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3> BiFunction<Try<T1>, Try<T2>, Try<T3>> liftTry(BiFunction<? super T1, ? super T2, ? extends T3> f) {
      return (a1, a2) -> a1.flatMap(b1 -> a2.map(b2 -> f.apply(b1, b2)));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4> Function3<Try<T1>, Try<T2>, Try<T3>, Try<T4>> liftTry(Function3<? super T1, ? super T2, ? super T3, ? extends T4> f) {
      return (a1, a2, a3) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.map(b3 -> f.apply(b1, b2, b3))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5> Function4<Try<T1>, Try<T2>, Try<T3>, Try<T4>, Try<T5>> liftTry(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T5> f) {
      return (a1, a2, a3, a4) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.map(b4 -> f.apply(b1, b2, b3, b4)))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6> Function5<Try<T1>, Try<T2>, Try<T3>, Try<T4>, Try<T5>, Try<T6>> liftTry(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends T6> f) {
      return (a1, a2, a3, a4, a5) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.map(b5 -> f.apply(b1, b2, b3, b4, b5))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Function6<Try<T1>, Try<T2>, Try<T3>, Try<T4>, Try<T5>, Try<T6>, Try<T7>> liftTry(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends T7> f) {
      return (a1, a2, a3, a4, a5, a6) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.map(b6 -> f.apply(b1, b2, b3, b4, b5, b6)))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Function7<Try<T1>, Try<T2>, Try<T3>, Try<T4>, Try<T5>, Try<T6>, Try<T7>, Try<T8>> liftTry(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends T8> f) {
      return (a1, a2, a3, a4, a5, a6, a7) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.map(b7 -> f.apply(b1, b2, b3, b4, b5, b6, b7))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Try} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Function8<Try<T1>, Try<T2>, Try<T3>, Try<T4>, Try<T5>, Try<T6>, Try<T7>, Try<T8>, Try<T9>> liftTry(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends T9> f) {
      return (a1, a2, a3, a4, a5, a6, a7, a8) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.flatMap(b7 -> a8.map(b8 -> f.apply(b1, b2, b3, b4, b5, b6, b7, b8)))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2> Function<Future<T1>, Future<T2>> liftFuture(Function<? super T1, ? extends T2> f) {
      return (a1) -> a1.map(b1 -> f.apply(b1));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3> BiFunction<Future<T1>, Future<T2>, Future<T3>> liftFuture(BiFunction<? super T1, ? super T2, ? extends T3> f) {
      return (a1, a2) -> a1.flatMap(b1 -> a2.map(b2 -> f.apply(b1, b2)));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4> Function3<Future<T1>, Future<T2>, Future<T3>, Future<T4>> liftFuture(Function3<? super T1, ? super T2, ? super T3, ? extends T4> f) {
      return (a1, a2, a3) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.map(b3 -> f.apply(b1, b2, b3))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5> Function4<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>> liftFuture(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T5> f) {
      return (a1, a2, a3, a4) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.map(b4 -> f.apply(b1, b2, b3, b4)))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6> Function5<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>> liftFuture(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends T6> f) {
      return (a1, a2, a3, a4, a5) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.map(b5 -> f.apply(b1, b2, b3, b4, b5))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Function6<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>> liftFuture(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends T7> f) {
      return (a1, a2, a3, a4, a5, a6) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.map(b6 -> f.apply(b1, b2, b3, b4, b5, b6)))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Function7<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>, Future<T8>> liftFuture(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends T8> f) {
      return (a1, a2, a3, a4, a5, a6, a7) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.map(b7 -> f.apply(b1, b2, b3, b4, b5, b6, b7))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Future} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Function8<Future<T1>, Future<T2>, Future<T3>, Future<T4>, Future<T5>, Future<T6>, Future<T7>, Future<T8>, Future<T9>> liftFuture(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends T9> f) {
      return (a1, a2, a3, a4, a5, a6, a7, a8) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.flatMap(b7 -> a8.map(b8 -> f.apply(b1, b2, b3, b4, b5, b6, b7, b8)))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2> Function<List<T1>, List<T2>> liftList(Function<? super T1, ? extends T2> f) {
      return (a1) -> a1.map(b1 -> f.apply(b1));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3> BiFunction<List<T1>, List<T2>, List<T3>> liftList(BiFunction<? super T1, ? super T2, ? extends T3> f) {
      return (a1, a2) -> a1.flatMap(b1 -> a2.map(b2 -> f.apply(b1, b2)));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4> Function3<List<T1>, List<T2>, List<T3>, List<T4>> liftList(Function3<? super T1, ? super T2, ? super T3, ? extends T4> f) {
      return (a1, a2, a3) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.map(b3 -> f.apply(b1, b2, b3))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5> Function4<List<T1>, List<T2>, List<T3>, List<T4>, List<T5>> liftList(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T5> f) {
      return (a1, a2, a3, a4) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.map(b4 -> f.apply(b1, b2, b3, b4)))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6> Function5<List<T1>, List<T2>, List<T3>, List<T4>, List<T5>, List<T6>> liftList(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends T6> f) {
      return (a1, a2, a3, a4, a5) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.map(b5 -> f.apply(b1, b2, b3, b4, b5))))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Function6<List<T1>, List<T2>, List<T3>, List<T4>, List<T5>, List<T6>, List<T7>> liftList(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends T7> f) {
      return (a1, a2, a3, a4, a5, a6) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.map(b6 -> f.apply(b1, b2, b3, b4, b5, b6)))))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Function7<List<T1>, List<T2>, List<T3>, List<T4>, List<T5>, List<T6>, List<T7>, List<T8>> liftList(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends T8> f) {
      return (a1, a2, a3, a4, a5, a6, a7) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.map(b7 -> f.apply(b1, b2, b3, b4, b5, b6, b7))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link List} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Function8<List<T1>, List<T2>, List<T3>, List<T4>, List<T5>, List<T6>, List<T7>, List<T8>, List<T9>> liftList(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends T9> f) {
      return (a1, a2, a3, a4, a5, a6, a7, a8) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.flatMap(b7 -> a8.map(b8 -> f.apply(b1, b2, b3, b4, b5, b6, b7, b8)))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2> Function<Either<E, T1>, Either<E, T2>> liftEither(Function<? super T1, ? extends T2> f) {
      return (a1) -> a1.map(b1 -> f.apply(b1));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3> BiFunction<Either<E, T1>, Either<E, T2>, Either<E, T3>> liftEither(BiFunction<? super T1, ? super T2, ? extends T3> f) {
      return (a1, a2) -> a1.flatMap(b1 -> a2.map(b2 -> f.apply(b1, b2)));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4> Function3<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>> liftEither(Function3<? super T1, ? super T2, ? super T3, ? extends T4> f) {
      return (a1, a2, a3) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.map(b3 -> f.apply(b1, b2, b3))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4, T5> Function4<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>, Either<E, T5>> liftEither(Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends T5> f) {
      return (a1, a2, a3, a4) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.map(b4 -> f.apply(b1, b2, b3, b4)))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4, T5, T6> Function5<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>, Either<E, T5>, Either<E, T6>> liftEither(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends T6> f) {
      return (a1, a2, a3, a4, a5) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.map(b5 -> f.apply(b1, b2, b3, b4, b5))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4, T5, T6, T7> Function6<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>, Either<E, T5>, Either<E, T6>, Either<E, T7>> liftEither(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends T7> f) {
      return (a1, a2, a3, a4, a5, a6) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.map(b6 -> f.apply(b1, b2, b3, b4, b5, b6)))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4, T5, T6, T7, T8> Function7<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>, Either<E, T5>, Either<E, T6>, Either<E, T7>, Either<E, T8>> liftEither(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends T8> f) {
      return (a1, a2, a3, a4, a5, a6, a7) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.map(b7 -> f.apply(b1, b2, b3, b4, b5, b6, b7))))))));
    }

    /**
     * Lift a function: promote it to operate on {@link Either} functors
     *
     * @param f the function to be lifted
     * @returns a function performing the same as the input function, but operating on functors
     */
    public static <E,T1, T2, T3, T4, T5, T6, T7, T8, T9> Function8<Either<E, T1>, Either<E, T2>, Either<E, T3>, Either<E, T4>, Either<E, T5>, Either<E, T6>, Either<E, T7>, Either<E, T8>, Either<E, T9>> liftEither(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends T9> f) {
      return (a1, a2, a3, a4, a5, a6, a7, a8) -> a1.flatMap(b1 -> a2.flatMap(b2 -> a3.flatMap(b3 -> a4.flatMap(b4 -> a5.flatMap(b5 -> a6.flatMap(b6 -> a7.flatMap(b7 -> a8.map(b8 -> f.apply(b1, b2, b3, b4, b5, b6, b7, b8)))))))));
    }

}