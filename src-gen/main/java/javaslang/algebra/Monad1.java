/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.function.Lambda1;

/**
 * Defines a Monad by generalizing the flatMap and unit functions.
 * <p>
 * All instances of the Monad interface should obey the three control laws:
 * <ul>
 *     <li><strong>Left identity:</strong> {@code unit(a).flatMap(f) ≡ f a}</li>
 *     <li><strong>Right identity:</strong> {@code m.flatMap(unit) ≡ m}</li>
 *     <li><strong>Associativity:</strong> {@code m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g)}</li>
 * </ul>
 * <p>
 *
 * @param <T1> Component type of this Monad1.
 */
public interface Monad1<T1, M extends HigherKinded1<?, M>> extends Functor1<T1>, HigherKinded1<T1, M> {

    <U1, MONAD extends HigherKinded1<U1, M>> Monad1<U1, M> flatMap(java.util.function.Function<? super T1, MONAD> f);
}