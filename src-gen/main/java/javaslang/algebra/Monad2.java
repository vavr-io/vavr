/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.function.Lambda2;

public interface Monad2<T1, T2, M extends HigherKinded2<?, ?, M>> extends Functor2<T1, T2>, HigherKinded2<T1, T2, M> {

    <U1, U2, MONAD extends HigherKinded2<U1, U2, M>> Monad2<U1, U2, M> flatMap(java.util.function.BiFunction<? super T1, ? super T2, MONAD> f);
}