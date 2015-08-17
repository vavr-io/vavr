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
 * <p>
 * Likewise to Scala, a FilterMonadic type is not a real monad.
 * See <a href="http://stackoverflow.com/questions/27750046/is-a-collection-with-flatmap-a-monad">Is a collection with flatMap a monad?</a>.
 *
 * @since 2.0.0
 */
// DEV-NOTE: We implement Kind<M, T> in order to prevent misusage like Option<T> extends FilterMonadic<Try<?>, T>, Kind<Try<?>, T>
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
