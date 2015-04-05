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
 * <li>an object {@code m} of type {@code HigherKinded4<T1, T2, T3, T4, M>}</li>
 * <li>an object {@code a} consisting of values of type {@code T1 ✕ T2 ✕ T3 ✕ T4}</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code M}</li>
 * <li>a function {@code f: T1 ✕ T2 ✕ T3 ✕ T4 → M}
 * </ul>
 *
 * To read further about monads in Java please refer to
 * <a href="http://java.dzone.com/articles/whats-wrong-java-8-part-iv">What's Wrong in Java 8, Part IV: Monads</a>.
 *
 * @param <T1> 1st component type of this monad
 * @param <T2> 2nd component type of this monad
 * @param <T3> 3rd component type of this monad
 * @param <T4> 4th component type of this monad
 * @param <M> placeholder for the type that implements this
 */
public interface Monad4<T1, T2, T3, T4, M extends HigherKinded4<?, ?, ?, ?, M>> extends Functor4<T1, T2, T3, T4>, HigherKinded4<T1, T2, T3, T4, M> {

    /**
     * Returns the result of applying f to M's values of type T1,…,T4 and returns a new M with
     * values of type U1,…,U4.
     *
     * @param <U1> 1st component type of this monad
     * @param <U2> 2nd component type of this monad
     * @param <U3> 3rd component type of this monad
     * @param <U4> 4th component type of this monad
     * @param <MONAD> placeholder for the monad type of component types T1,…,T4 and container type M
     * @param f a function that maps the monad values to a new monad instance
     * @return a new monad instance of component types U1,…,U4 and container type M
     */
    <U1, U2, U3, U4, MONAD extends HigherKinded4<U1, U2, U3, U4, M>> Monad4<U1, U2, U3, U4, M> flatMap(Function4<? super T1, ? super T2, ? super T3, ? super T4, MONAD> f);

    @Override
    <U1, U2, U3, U4> Monad4<U1, U2, U3, U4, M> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f);

    @Override
    <U1, U2, U3, U4> Monad4<U1, U2, U3, U4, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4);
}