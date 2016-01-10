/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.algebra.Functor;
import javaslang.collection.List;

import java.util.function.Function;

public interface Applicative<V extends Kind<V, ?, ?>, E, T> extends Functor<T> {

    <U> Applicative<V, List<E>, U> ap(Kind<V, List<E>, ? extends Function<? super T, ? extends U>> f);

    @Override
    <U> Applicative<V, E, U> map(Function<? super T, ? extends U> f);

}
