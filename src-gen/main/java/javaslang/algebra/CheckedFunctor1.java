/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.CheckedFunction1;

/**
 * <p>Defines a 1-ary CheckedFunctor by generalizing the map function which maps one element.</p>
 *
 * All instances of the CheckedFunctor1 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where f, g ∈ $function1Type.
 *
 * @param <T1> 1st component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface CheckedFunctor1<T1> {

    /**
     * Applies a function f to the components of this CheckedFunctor.
     *
     * @param <U1> type of the 1st component of the resulting CheckedFunctor
     * @param f a 1-ary CheckedFunction which maps the components of this CheckedFunctor
     * @return a new CheckedFunctor1 with one component type U1.
     */
    <U1> CheckedFunctor1<U1> map(CheckedFunction1<? super T1, ? extends U1> f);

}