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
import javaslang.Function4;
import javaslang.Tuple4;

/**
 * <p>Defines a 4-ary Functor by generalizing the map function which maps 4 elements.</p>
 *
 * All instances of the Functor4 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where {@code f, g ∈ Tuple4 → Tuple4}.
 *
 * @param <T1> 1st component type of this monad
 * @param <T2> 2nd component type of this monad
 * @param <T3> 3rd component type of this monad
 * @param <T4> 4th component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface Functor4<T1, T2, T3, T4> {

    /**
     * Applies a function f to the components of this Functor.
     *
     * @param <U1> type of the 1st component of the resulting Functor
     * @param <U2> type of the 2nd component of the resulting Functor
     * @param <U3> type of the 3rd component of the resulting Functor
     * @param <U4> type of the 4th component of the resulting Functor
     * @param f a 4-ary Function which maps the components of this Functor
     * @return a new Functor4 with 4 component types U1, U2, U3, U4.
     */
    <U1, U2, U3, U4> Functor4<U1, U2, U3, U4> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f);

    /**
     * Applies a separate function to each component of this Functor.
     *
     * @param <U1> type of the 1st component of the resulting Functor
     * @param <U2> type of the 2nd component of the resulting Functor
     * @param <U3> type of the 3rd component of the resulting Functor
     * @param <U4> type of the 4th component of the resulting Functor
     * @param f1 the Function applied to the 1st component of this Functor
     * @param f2 the Function applied to the 2nd component of this Functor
     * @param f3 the Function applied to the 3rd component of this Functor
     * @param f4 the Function applied to the 4th component of this Functor
     * @return a new Functor4 with 4 component types U1, U2, U3, U4.
     */
    <U1, U2, U3, U4> Functor4<U1, U2, U3, U4> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4);
}