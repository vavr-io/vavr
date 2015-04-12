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
import javaslang.Function16;
import javaslang.Tuple16;

/**
 * <p>Defines a 16-ary Functor by generalizing the map function which maps 16 elements.</p>
 *
 * All instances of the Functor16 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where {@code f, g ∈ Tuple16 → Tuple16}.
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
 * @param <T11> 11th component type of this monad
 * @param <T12> 12th component type of this monad
 * @param <T13> 13th component type of this monad
 * @param <T14> 14th component type of this monad
 * @param <T15> 15th component type of this monad
 * @param <T16> 16th component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 * @since 1.1.0
 */
public interface Functor16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {

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
     * @param <U11> type of the 11th component of the resulting Functor
     * @param <U12> type of the 12th component of the resulting Functor
     * @param <U13> type of the 13th component of the resulting Functor
     * @param <U14> type of the 14th component of the resulting Functor
     * @param <U15> type of the 15th component of the resulting Functor
     * @param <U16> type of the 16th component of the resulting Functor
     * @param f a 16-ary Function which maps the components of this Functor
     * @return a new Functor16 with 16 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> Functor16<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> map(Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, Tuple16<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16>> f);

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
     * @param <U11> type of the 11th component of the resulting Functor
     * @param <U12> type of the 12th component of the resulting Functor
     * @param <U13> type of the 13th component of the resulting Functor
     * @param <U14> type of the 14th component of the resulting Functor
     * @param <U15> type of the 15th component of the resulting Functor
     * @param <U16> type of the 16th component of the resulting Functor
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
     * @param f11 the Function applied to the 11th component of this Functor
     * @param f12 the Function applied to the 12th component of this Functor
     * @param f13 the Function applied to the 13th component of this Functor
     * @param f14 the Function applied to the 14th component of this Functor
     * @param f15 the Function applied to the 15th component of this Functor
     * @param f16 the Function applied to the 16th component of this Functor
     * @return a new Functor16 with 16 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> Functor16<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16);
}