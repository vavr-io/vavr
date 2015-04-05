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
import javaslang.Function9;
import javaslang.Tuple9;

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
 * <li>an object {@code m} of type {@code HigherKinded9<T1, T2, T3, T4, T5, T6, T7, T8, T9, M>}</li>
 * <li>an object {@code a} consisting of values of type {@code T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5 ✕ T6 ✕ T7 ✕ T8 ✕ T9}</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code M}</li>
 * <li>a function {@code f: T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5 ✕ T6 ✕ T7 ✕ T8 ✕ T9 → M}
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
 * @param <M> placeholder for the type that implements this
 * @since 1.1.0
 */
public interface Monad9<T1, T2, T3, T4, T5, T6, T7, T8, T9, M extends HigherKinded9<?, ?, ?, ?, ?, ?, ?, ?, ?, M>> extends Functor9<T1, T2, T3, T4, T5, T6, T7, T8, T9>, HigherKinded9<T1, T2, T3, T4, T5, T6, T7, T8, T9, M> {

    /**
     * Returns the result of applying f to M's values of type T1,…,T9 and returns a new M with
     * values of type U1,…,U9.
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
     * @param <MONAD> placeholder for the monad type of component types T1,…,T9 and container type M
     * @param f a function that maps the monad values to a new monad instance
     * @return a new monad instance of component types U1,…,U9 and container type M
     */
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, MONAD extends HigherKinded9<U1, U2, U3, U4, U5, U6, U7, U8, U9, M>> Monad9<U1, U2, U3, U4, U5, U6, U7, U8, U9, M> flatMap(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9> Monad9<U1, U2, U3, U4, U5, U6, U7, U8, U9, M> map(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, Tuple9<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9>> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9> Monad9<U1, U2, U3, U4, U5, U6, U7, U8, U9, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9);
}