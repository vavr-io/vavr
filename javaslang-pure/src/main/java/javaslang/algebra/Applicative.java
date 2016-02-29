/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Kind2;
import javaslang.collection.List;

import java.util.function.Function;

/**
 * An implementation similar to scalaz's <a href="http://eed3si9n.com/learning-scalaz/Applicative.html">Applicative</a> type.
 * Unlike the 'map' function in {@code Functor}s, which applies a pure function to a value inside of a context, {@code Applicative}s
 * allow a function wrapped inside of a context to be applied to a value inside of a context, returning the result inside
 * of the same context.
 *
 * @param <TYPE> Type of recursive self-type which {@code Applicative} describes
 * @param <T1> First type described by this {@code Applicative} (usually the error type)
 * @param <T2> Second type described by this {@code Applicative} (usually the success type)
 *
 * @author Daniel Dietrich, Eric Nelson
 * @since 2.0.0
 * @see <a href="http://eed3si9n.com/learning-scalaz/Applicative.html">Applicative</a>
 */
public interface Applicative<TYPE extends Kind2<TYPE, ?, ?>, T1, T2> extends Kind2<TYPE, T1, T2>, Functor<T2> {

    /**
     * Applies a function wrapped inside of an {@code Applicative}, f, to a value inside of this
     * {@code Applicative}.
     *
     * @param <U> type of the result from this mapping
     * @param f an {@code Applicative} containing a function which maps this value
     * @return an {@code Applicative} containing either an error, or the result of the mapping
     */
    <U> Applicative<TYPE, List<T1>, U> ap(Kind2<TYPE, List<T1>, ? extends Function<? super T2, ? extends U>> f);

    @Override
    <U> Applicative<TYPE, T1, U> map(Function<? super T2, ? extends U> f);

}
