/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.algebra.Functor;

import java.util.function.Function;

public interface Applicative<E extends Kind<E, ?>, T> extends Functor<T> {

    <U> Applicative<E, U> ap(Kind<E, ? extends Function<? super T, ? extends U>> f);

    @Override
    <U> Applicative<E, U> map(Function<? super T, ? extends U> f);

}
