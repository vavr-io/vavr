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
 * <li>an object {@code m} of type {@code HigherKinded5<T1, T2, T3, T4, T5, M>}</li>
 * <li>an object {@code a} consisting of values of type {@code T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5}</li>
 * <li>a constructor {@code unit} taking an {@code a} and producing an object of type {@code M}</li>
 * <li>a function {@code f: T1 ✕ T2 ✕ T3 ✕ T4 ✕ T5 → M}
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
 * @param <M> placeholder for the type that implements this
 */
public interface Monad5<T1, T2, T3, T4, T5, M extends HigherKinded5<?, ?, ?, ?, ?, M>> extends Functor5<T1, T2, T3, T4, T5>, HigherKinded5<T1, T2, T3, T4, T5, M> {

    /**
     * Returns the result of applying f to M's values of type T1,…,T5 and returns a new M with
     * values of type U1,…,U5.
     *
     * @param <U1> 1st component type of this monad
     * @param <U2> 2nd component type of this monad
     * @param <U3> 3rd component type of this monad
     * @param <U4> 4th component type of this monad
     * @param <U5> 5th component type of this monad
     * @param <MONAD> placeholder for the monad type of component types T1,…,T5 and container type M
     * @param f a function that maps the monad values to a new monad instance
     * @return a new monad instance of component types U1,…,U5 and container type M
     */
    <U1, U2, U3, U4, U5, MONAD extends HigherKinded5<U1, U2, U3, U4, U5, M>> Monad5<U1, U2, U3, U4, U5, M> flatMap(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5> Monad5<U1, U2, U3, U4, U5, M> map(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, Tuple5<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5>> f);

    @Override
    <U1, U2, U3, U4, U5> Monad5<U1, U2, U3, U4, U5, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5);
}