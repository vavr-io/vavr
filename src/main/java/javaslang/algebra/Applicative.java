/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Kind2;
import javaslang.collection.List;

import java.util.function.Function;

public interface Applicative<TYPE extends Kind2<TYPE, ?, ?>, E, T> extends Functor<T> {

    <U> Applicative<TYPE, List<E>, U> ap(Kind2<TYPE, List<E>, ? extends Function<? super T, ? extends U>> f);

    @Override
    <U> Applicative<TYPE, E, U> map(Function<? super T, ? extends U> f);

}
