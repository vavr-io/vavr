/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function11;

public interface Monad11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, M extends HigherKinded11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M>> extends Functor11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>, HigherKinded11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, M> {

    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, MONAD extends HigherKinded11<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, M>> Monad11<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, M> flatMap(javaslang.Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, MONAD> f);
}