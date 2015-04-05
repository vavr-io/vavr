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
import javaslang.Function24;
import javaslang.Tuple24;

/**
 * <p>Defines a Functor by generalizing the map function which maps 24 elements.</p>
 *
 * All instances of the Functor24 interface should obey the two functor laws:
 * <ul>
 *     <li>{@code m.map(a -> a) ≡ m}</li>
 *     <li>{@code m.map(f.compose(g)) ≡ m.map(g).map(f)}</li>
 * </ul>
 * where {@code f, g ∈ Tuple24 → Tuple24}.
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
 * @param <T17> 17th component type of this monad
 * @param <T18> 18th component type of this monad
 * @param <T19> 19th component type of this monad
 * @param <T20> 20th component type of this monad
 * @param <T21> 1st component type of this monad
 * @param <T22> 2nd component type of this monad
 * @param <T23> 3rd component type of this monad
 * @param <T24> 24th component type of this monad
 * @see <a href="http://www.haskellforall.com/2012/09/the-functor-design-pattern.html">The functor design pattern</a>
 */
public interface Functor24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {

    /**
     * Applies a function f to the components of this functor.
     *
     * @param <U1> type of the 1st component of the resulting functor
     * @param <U2> type of the 2nd component of the resulting functor
     * @param <U3> type of the 3rd component of the resulting functor
     * @param <U4> type of the 4th component of the resulting functor
     * @param <U5> type of the 5th component of the resulting functor
     * @param <U6> type of the 6th component of the resulting functor
     * @param <U7> type of the 7th component of the resulting functor
     * @param <U8> type of the 8th component of the resulting functor
     * @param <U9> type of the 9th component of the resulting functor
     * @param <U10> type of the 10th component of the resulting functor
     * @param <U11> type of the 11th component of the resulting functor
     * @param <U12> type of the 12th component of the resulting functor
     * @param <U13> type of the 13th component of the resulting functor
     * @param <U14> type of the 14th component of the resulting functor
     * @param <U15> type of the 15th component of the resulting functor
     * @param <U16> type of the 16th component of the resulting functor
     * @param <U17> type of the 17th component of the resulting functor
     * @param <U18> type of the 18th component of the resulting functor
     * @param <U19> type of the 19th component of the resulting functor
     * @param <U20> type of the 20th component of the resulting functor
     * @param <U21> type of the 1st component of the resulting functor
     * @param <U22> type of the 2nd component of the resulting functor
     * @param <U23> type of the 3rd component of the resulting functor
     * @param <U24> type of the 24th component of the resulting functor
     * @param f a 24-ary function which maps the components of this functor
     * @return a new functor with 24 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> Functor24<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> map(Function24<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, Tuple24<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22, ? extends U23, ? extends U24>> f);

    /**
     * Applies a separate function to each component of this functor.
     *
     * @param <U1> type of the 1st component of the resulting functor
     * @param <U2> type of the 2nd component of the resulting functor
     * @param <U3> type of the 3rd component of the resulting functor
     * @param <U4> type of the 4th component of the resulting functor
     * @param <U5> type of the 5th component of the resulting functor
     * @param <U6> type of the 6th component of the resulting functor
     * @param <U7> type of the 7th component of the resulting functor
     * @param <U8> type of the 8th component of the resulting functor
     * @param <U9> type of the 9th component of the resulting functor
     * @param <U10> type of the 10th component of the resulting functor
     * @param <U11> type of the 11th component of the resulting functor
     * @param <U12> type of the 12th component of the resulting functor
     * @param <U13> type of the 13th component of the resulting functor
     * @param <U14> type of the 14th component of the resulting functor
     * @param <U15> type of the 15th component of the resulting functor
     * @param <U16> type of the 16th component of the resulting functor
     * @param <U17> type of the 17th component of the resulting functor
     * @param <U18> type of the 18th component of the resulting functor
     * @param <U19> type of the 19th component of the resulting functor
     * @param <U20> type of the 20th component of the resulting functor
     * @param <U21> type of the 1st component of the resulting functor
     * @param <U22> type of the 2nd component of the resulting functor
     * @param <U23> type of the 3rd component of the resulting functor
     * @param <U24> type of the 24th component of the resulting functor
     * @param f1 the function applied to the 1st component of this functor
     * @param f2 the function applied to the 2nd component of this functor
     * @param f3 the function applied to the 3rd component of this functor
     * @param f4 the function applied to the 4th component of this functor
     * @param f5 the function applied to the 5th component of this functor
     * @param f6 the function applied to the 6th component of this functor
     * @param f7 the function applied to the 7th component of this functor
     * @param f8 the function applied to the 8th component of this functor
     * @param f9 the function applied to the 9th component of this functor
     * @param f10 the function applied to the 10th component of this functor
     * @param f11 the function applied to the 11th component of this functor
     * @param f12 the function applied to the 12th component of this functor
     * @param f13 the function applied to the 13th component of this functor
     * @param f14 the function applied to the 14th component of this functor
     * @param f15 the function applied to the 15th component of this functor
     * @param f16 the function applied to the 16th component of this functor
     * @param f17 the function applied to the 17th component of this functor
     * @param f18 the function applied to the 18th component of this functor
     * @param f19 the function applied to the 19th component of this functor
     * @param f20 the function applied to the 20th component of this functor
     * @param f21 the function applied to the 1st component of this functor
     * @param f22 the function applied to the 2nd component of this functor
     * @param f23 the function applied to the 3rd component of this functor
     * @param f24 the function applied to the 24th component of this functor
     * @return a new functor with 24 component types U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24.
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> Functor24<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22, Function1<? super T23, ? extends U23> f23, Function1<? super T24, ? extends U24> f24);
}