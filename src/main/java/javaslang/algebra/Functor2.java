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
 * Functor with two components. See {@link Functor}.
 *
 * @param <T1> 1st component type of this functor
 * @param <T2> 2nd component type of this functor
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Functor2<T1, T2> {

    /**
     * Applies a function {@code f} to the components of this Functor.
     *
     * @param <U1> 1st component type of the resulting Functor
     * @param <U2> 2nd component type of the resulting Functor
     * @param f    a Function which maps the components of this Functor
     * @return a new Functor
     * @throws NullPointerException if {@code f} is null
     */
    <U1, U2> Functor2<U1, U2> map(BiFunction<? super T1, ? super T2, ? extends Tuple2<? extends U1, ? extends U2>> f);

    /**
     * Applies two functions {@code f1}, {@code f2} to the components of this Functor.
     *
     * @param <U1> 1st component type of the resulting Functor
     * @param <U2> 2nd component type of the resulting Functor
     * @param f1   mapper of 1st component
     * @param f2   mapper of 2nd component
     * @return a new Functor
     * @throws NullPointerException if {@code f1} or {@code f2} is null
     */
    <U1, U2> Functor2<U1, U2> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2);

    /**
     * Applies a function {@code f} to the components of this Functor.
     * <p>
     * Note: The name is {@code map2} to avoid ambiguities with {@code map(Function)} in general.
     *
     * @param <U1> 1st component type of the resulting Functor
     * @param <U2> 2nd component type of the resulting Functor
     * @param f    a Function which maps the components of this Functor
     * @return a new Functor
     * @throws NullPointerException if {@code f} is null
     */
    <U1, U2> Functor2<U1, U2> map2(Function<? super Tuple2<? super T1, ? super T2>, ? extends Tuple2<? extends U1, ? extends U2>> f);

}
