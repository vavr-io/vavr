/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function6;
import javaslang.Tuple6;

public interface Monad6<T1, T2, T3, T4, T5, T6, M extends HigherKinded6<?, ?, ?, ?, ?, ?, M>> extends Functor6<T1, T2, T3, T4, T5, T6>, HigherKinded6<T1, T2, T3, T4, T5, T6, M> {

    <U1, U2, U3, U4, U5, U6, MONAD extends HigherKinded6<U1, U2, U3, U4, U5, U6, M>> Monad6<U1, U2, U3, U4, U5, U6, M> flatMap(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6> Monad6<U1, U2, U3, U4, U5, U6, M> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6>> f);
}