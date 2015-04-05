/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function1;
import javaslang.Function5;
import javaslang.Tuple5;

/**
 * <p>Defines a Functor by generalizing the map function which maps 5 elements.</p>
 *
 * All instances of the Functor5 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where {@code f, g ∈ Tuple5 → Tuple5}.
 *
 * @param <T1> 1st component type of this monad
 * @param <T2> 2nd component type of this monad
 * @param <T3> 3rd component type of this monad
 * @param <T4> 4th component type of this monad
 * @param <T5> 5th component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 */
public interface Functor5<T1, T2, T3, T4, T5> {

    /**
     * Applies a function f to the components of this functor.
     *
     * @param <U1> type of the 1st component of the resulting functor
     * @param <U2> type of the 2nd component of the resulting functor
     * @param <U3> type of the 3rd component of the resulting functor
     * @param <U4> type of the 4th component of the resulting functor
     * @param <U5> type of the 5th component of the resulting functor
     * @param f a 5-ary function which maps the components of this functor
     * @return a new functor with 5 component types U1, U2, U3, U4, U5.
     */
    <U1, U2, U3, U4, U5> Functor5<U1, U2, U3, U4, U5> map(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, Tuple5<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5>> f);

    /**
     * Applies a separate function to each component of this functor.
     *
     * @param <U1> type of the 1st component of the resulting functor
     * @param <U2> type of the 2nd component of the resulting functor
     * @param <U3> type of the 3rd component of the resulting functor
     * @param <U4> type of the 4th component of the resulting functor
     * @param <U5> type of the 5th component of the resulting functor
     * @param f1 the function applied to the 1st component of this functor
     * @param f2 the function applied to the 2nd component of this functor
     * @param f3 the function applied to the 3rd component of this functor
     * @param f4 the function applied to the 4th component of this functor
     * @param f5 the function applied to the 5th component of this functor
     * @return a new functor with 5 component types U1, U2, U3, U4, U5.
     */
    <U1, U2, U3, U4, U5> Functor5<U1, U2, U3, U4, U5> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5);
}