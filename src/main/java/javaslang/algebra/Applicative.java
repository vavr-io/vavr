/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Kind2;
import javaslang.collection.List;

import java.util.function.Function;

public interface Applicative<TYPE extends Kind2<TYPE, ?, ?>, T1, T2> extends Functor<T2> {

    <U> Applicative<TYPE, List<T1>, U> ap(Kind2<TYPE, List<T1>, ? extends Function<? super T2, ? extends U>> f);

    @Override
    <U> Applicative<TYPE, T1, U> map(Function<? super T2, ? extends U> f);

}
