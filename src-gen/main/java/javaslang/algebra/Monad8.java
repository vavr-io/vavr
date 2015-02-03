/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Function8;

public interface Monad8<T1, T2, T3, T4, T5, T6, T7, T8, M extends HigherKinded8<?, ?, ?, ?, ?, ?, ?, ?, M>> extends Functor8<T1, T2, T3, T4, T5, T6, T7, T8>, HigherKinded8<T1, T2, T3, T4, T5, T6, T7, T8, M> {

    <U1, U2, U3, U4, U5, U6, U7, U8, MONAD extends HigherKinded8<U1, U2, U3, U4, U5, U6, U7, U8, M>> Monad8<U1, U2, U3, U4, U5, U6, U7, U8, M> flatMap(javaslang.Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, MONAD> f);
}