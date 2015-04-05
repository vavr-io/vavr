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
import javaslang.Function21;
import javaslang.Tuple21;

/**
 * Defines a Monad by generalizing the flatMap function.
 * <p>
 * All instances of the Monad interface should obey the three control laws:
 * <ul>
 *     <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 *     <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 *     <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
 * </ul>
 * given
 * <ul>
 * <li>an object {@code m} of type {@code HigherKinded21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, M>}</li>
 * <li>an object {@code a} consisting of values of type {@code T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5 ✕ T6 ✕ T7 ✕ T8 ✕ T9 ✕ T10 ✕ T11 ✕ T12 ✕ T13 ✕ T14 ✕ T15 ✕ T16 ✕ T17 ✕ T18 ✕ T19 ✕ T20 ✕ T21}</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code M}</li>
 * <li>a function {@code f: T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5 ✕ T6 ✕ T7 ✕ T8 ✕ T9 ✕ T10 ✕ T11 ✕ T12 ✕ T13 ✕ T14 ✕ T15 ✕ T16 ✕ T17 ✕ T18 ✕ T19 ✕ T20 ✕ T21 → M}
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
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
 * @param <M> placeholder for the type that implements this
 * @since 1.1.0
 */
public interface Monad21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, M extends HigherKinded21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M>> extends Functor21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>, HigherKinded21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, M> {

    /**
     * Returns the result of applying f to M's values of type T1,…,T21 and returns a new M with
     * values of type U1,…,U21.
     *
     * @param <U1> 1st component type of this monad
     * @param <U2> 2nd component type of this monad
     * @param <U3> 3rd component type of this monad
     * @param <U4> 4th component type of this monad
     * @param <U5> 5th component type of this monad
     * @param <U6> 6th component type of this monad
     * @param <U7> 7th component type of this monad
     * @param <U8> 8th component type of this monad
     * @param <U9> 9th component type of this monad
     * @param <U10> 10th component type of this monad
     * @param <U11> 11th component type of this monad
     * @param <U12> 12th component type of this monad
     * @param <U13> 13th component type of this monad
     * @param <U14> 14th component type of this monad
     * @param <U15> 15th component type of this monad
     * @param <U16> 16th component type of this monad
     * @param <U17> 17th component type of this monad
     * @param <U18> 18th component type of this monad
     * @param <U19> 19th component type of this monad
     * @param <U20> 20th component type of this monad
     * @param <U21> 1st component type of this monad
     * @param <MONAD> placeholder for the monad type of component types T1,…,T21 and container type M
     * @param f a function that maps the monad values to a new monad instance
     * @return a new monad instance of component types U1,…,U21 and container type M
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, MONAD extends HigherKinded21<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, M>> Monad21<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, M> flatMap(Function21<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21> Monad21<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, M> map(Function21<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, Tuple21<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21>> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21> Monad21<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21);
}