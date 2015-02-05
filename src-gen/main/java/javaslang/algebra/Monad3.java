/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function3;

public interface Monad3<T1, T2, T3, M extends HigherKinded3<?, ?, ?, M>> extends Functor3<T1, T2, T3>, HigherKinded3<T1, T2, T3, M> {

    <U1, U2, U3, MONAD extends HigherKinded3<U1, U2, U3, M>> Monad3<U1, U2, U3, M> flatMap(Function3<? super T1, ? super T2, ? super T3, MONAD> f);
}