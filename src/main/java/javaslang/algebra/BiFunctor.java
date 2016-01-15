/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Tuple2;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A BiFunctor is a Functor having two components. See also {@link Functor}.
 *
 * @param <T1> 1st component type of this BiFunctor
 * @param <T2> 2nd component type of this BiFunctor
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface BiFunctor<T1, T2> {

    /**
     * Applies a function {@code f} to the components of this BiFunctor.
     *
     * @param <U1> 1st component type of the resulting BiFunctor
     * @param <U2> 2nd component type of the resulting BiFunctor
     * @param f    a Function which maps the components of this BiFunctor
     * @return a new BiFunctor
     * @throws NullPointerException if {@code f} is null
     */
    <U1, U2> BiFunctor<U1, U2> bimap(BiFunction<? super T1, ? super T2, Tuple2<U1, U2>> f);

    /**
     * Applies two functions {@code f1}, {@code f2} to the components of this BiFunctor.
     *
     * @param <U1> 1st component type of the resulting BiFunctor
     * @param <U2> 2nd component type of the resulting BiFunctor
     * @param f1   mapper of 1st component
     * @param f2   mapper of 2nd component
     * @return a new BiFunctor
     * @throws NullPointerException if {@code f1} or {@code f2} is null
     */
    <U1, U2> BiFunctor<U1, U2> bimap(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2);

    /**
     * Applies a function {@code f} to the components of this BiFunctor.
     *
     * @param <U1> 1st component type of the resulting BiFunctor
     * @param <U2> 2nd component type of the resulting BiFunctor
     * @param f    a Function which maps the components of this BiFunctor
     * @return a new BiFunctor
     * @throws NullPointerException if {@code f} is null
     */
    <U1, U2> BiFunctor<U1, U2> bimap(Function<Tuple2<T1, T2>, Tuple2<U1, U2>> f);

}
