/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.Option;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * {@code FilterMonadic} is an abstraction for filter-monadic operations.
 *
 * @since 2.0.0
 */
public interface FilterMonadic<M extends Kind<M, ?>, T> extends Kind<M, T> {

    /**
     * Returns a new {@code FilterMonadic} consisting of all elements which satisfy the given predicate.
     *
     * @param predicate A predicate
     * @return a new FilterMonadic instance
     * @throws NullPointerException if {@code predicate} is null
     */
    FilterMonadic<M, T> filter(Predicate<? super T> predicate);

    /**
     * Returns a new {@code FilterMonadic} consisting of {@code Some} elements if there are elements which satisfy the
     * given predicate. Otherwise the results contains one {@code None} element. Please note, that the result
     * will not contain a mixture of {@code Some} and {@code None} elements.
     * <p>
     * This method is intended to be used with monadic types which may have only one value,
     * e.g. {@code Lazy} and {@code Match} (resp. {@code MatchMonad}).
     *
     * @param predicate A predicate
     * @return a new FilterMonadic instance
     * @throws NullPointerException if {@code predicate} is null
     */
    FilterMonadic<M, ? extends Option<T>> filterOption(Predicate<? super T> predicate);

    /**
     * Flat maps the elements of this elements of a new type preserving their order, if any.
     * <p>
     * This method effectively does call
     *
     * <pre><code>this.flatMap((Function&lt;? super T, ? extends M&lt;? extends U&gt;&gt;) mapper);</code></pre>
     *
     * It exists, because Java's type system (the lack of higher-order kinds) does not allow us to
     * define an interface containing the mentioned <code>flatMap</code> method.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the flat mapped {@code FilterMonadic}
     * @return a flat mapped {@code FilterMonadic}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> FilterMonadic<M, U> flatMapM(Function<? super T, ? extends Kind<? extends M, ? extends U>> mapper);

    /**
     * Flattens a {@code FilterMonadic}.
     * <p>
     * The semantics may vary from class to class. The commonality is,
     * that some kind of wrapped state is recursively unwrapped.
     * <p>
     * Example:
     * <pre><code>((1, 2), 3, (4, (5, 6))).flatten() = (1, 2, 3, 4, 5, 6)</code></pre>
     *
     * @return A flattened version of this {@code FilterMonadic}.
     */
    FilterMonadic<M, Object> flatten();

    /**
     * Maps the elements of this elements of a new type preserving their order, if any.
     *
     * @param mapper A mapper.
     * @param <U>    Component type of the mapped {@code FilterMonadic}
     * @return a mapped {@code FilterMonadic}
     * @throws NullPointerException if {@code mapper} is null
     */
    <U> FilterMonadic<M, U> map(Function<? super T, ? extends U> mapper);

}
