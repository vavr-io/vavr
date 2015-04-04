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

public interface Monad4<T1, T2, T3, T4, M extends HigherKinded4<?, ?, ?, ?, M>> extends Functor4<T1, T2, T3, T4>, HigherKinded4<T1, T2, T3, T4, M> {

    <U1, U2, U3, U4, MONAD extends HigherKinded4<U1, U2, U3, U4, M>> Monad4<U1, U2, U3, U4, M> flatMap(Function4<? super T1, ? super T2, ? super T3, ? super T4, MONAD> f);

    @Override
    <U1, U2, U3, U4> Monad4<U1, U2, U3, U4, M> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f);

    @Override
    <U1, U2, U3, U4> Monad4<U1, U2, U3, U4, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4);
}