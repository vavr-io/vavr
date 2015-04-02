/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function13;
import javaslang.Tuple13;

public interface Monad13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, M extends HigherKinded13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M>> extends Functor13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>, HigherKinded13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, M> {

    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, MONAD extends HigherKinded13<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, M>> Monad13<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, M> flatMap(Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13> Monad13<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, M> map(Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, Tuple13<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13>> f);
}