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
import javaslang.Function10;
import javaslang.Tuple10;

/**
 * <p>Defines a 10-ary Functor by generalizing the map function which maps 10 elements.</p>
 *
 * All instances of the Functor10 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where {@code f, g ∈ Tuple10 → Tuple10}.
 *
 * @param <T1> 1st component type of this monad
 * @param <T2> 2nd component type of this monad
 * @param <T3> 3rd component type of this monad
 * @param <T4> 4th component type of this monad
 * @param <T5> 5th component type of this monad
 * @param <T6> 6th component type of this monad
 * @param <T7> 7th component type of this monad
 * @param <T8> 8th component type of this monad
 * @param <T9> 9th component type of this monad
 * @param <T10> 10th component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface Functor10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {

    /**
     * Applies a function f to the components of this Functor.
     *
     * @param <U1> type of the 1st component of the resulting Functor
     * @param <U2> type of the 2nd component of the resulting Functor
     * @param <U3> type of the 3rd component of the resulting Functor
     * @param <U4> type of the 4th component of the resulting Functor
     * @param <U5> type of the 5th component of the resulting Functor
     * @param <U6> type of the 6th component of the resulting Functor
     * @param <U7> type of the 7th component of the resulting Functor
     * @param <U8> type of the 8th component of the resulting Functor
     * @param <U9> type of the 9th component of the resulting Functor
     * @param <U10> type of the 10th component of the resulting Functor
     * @param f a 10-ary Function which maps the components of this Functor
     * @return a new Functor10 with 10 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> Functor10<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> map(Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, Tuple10<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10>> f);

    /**
     * Applies a separate function to each component of this Functor.
     *
     * @param <U1> type of the 1st component of the resulting Functor
     * @param <U2> type of the 2nd component of the resulting Functor
     * @param <U3> type of the 3rd component of the resulting Functor
     * @param <U4> type of the 4th component of the resulting Functor
     * @param <U5> type of the 5th component of the resulting Functor
     * @param <U6> type of the 6th component of the resulting Functor
     * @param <U7> type of the 7th component of the resulting Functor
     * @param <U8> type of the 8th component of the resulting Functor
     * @param <U9> type of the 9th component of the resulting Functor
     * @param <U10> type of the 10th component of the resulting Functor
     * @param f1 the Function applied to the 1st component of this Functor
     * @param f2 the Function applied to the 2nd component of this Functor
     * @param f3 the Function applied to the 3rd component of this Functor
     * @param f4 the Function applied to the 4th component of this Functor
     * @param f5 the Function applied to the 5th component of this Functor
     * @param f6 the Function applied to the 6th component of this Functor
     * @param f7 the Function applied to the 7th component of this Functor
     * @param f8 the Function applied to the 8th component of this Functor
     * @param f9 the Function applied to the 9th component of this Functor
     * @param f10 the Function applied to the 10th component of this Functor
     * @return a new Functor10 with 10 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> Functor10<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10);
}