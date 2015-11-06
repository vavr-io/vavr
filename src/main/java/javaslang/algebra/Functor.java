/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.function.Function;

/**
 * <p>Defines a Functor by generalizing the map.</p>
 *
 * All instances of the Functor interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where "f, g ∈ Function".
 *
 * @param <T> component type of this functor
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface Functor<T> {

    /**
     * Applies a function f to the components of this Functor.
     *
     * @param <U> type of the component of the resulting Functor
     * @param mapper a Function which maps the component of this Functor
     * @return a new Functor
     * @throws NullPointerException if {@code f} is null
     */
    <U> Functor<U> map(Function<? super T, ? extends U> mapper);
}