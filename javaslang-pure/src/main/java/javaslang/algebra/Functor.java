/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import java.util.function.Function;

/**
 * Defines a Functor by generalizing the {@code map} function.
 * <p>
 * All instances of the Functor interface should obey the two functor laws:
 * <ul>
 * <li>{@code m.map(a -> a) ≡ m}</li>
 * <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where "f, g ∈ Function".
 *
 * @param <T> component type of this functor
 * @author Daniel Dietrich
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface Functor<T> {

    /**
     * Applies a function {@code f} to the components of this Functor.
     *
     * @param <U> 1st component type of the resulting Functor
     * @param f   a Function which maps the component of this Functor
     * @return a new Functor
     * @throws NullPointerException if {@code f} is null
     */
    <U> Functor<U> map(Function<? super T, ? extends U> f);
}
