/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.control.Try.CheckedFunction;

/**
 * <p>Defines a CheckedFunctor by generalizing the map.</p>
 *
 * All instances of the CheckedFunctor interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where "f, g ∈ CheckedFunction".
 *
 * @param <T> component type of this functor
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface CheckedFunctor<T> {

    /**
     * Applies a function f to the components of this CheckedFunctor.
     *
     * @param <U> type of the component of the resulting CheckedFunctor
     * @param mapper a CheckedFunction which maps the component of this CheckedFunctor
     * @return a new CheckedFunctor
     * @throws NullPointerException if {@code f} is null
     */
    <U> CheckedFunctor<U> map(CheckedFunction<? super T, ? extends U> mapper);
}