/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.Tuple5;
import javaslang.function.Lambda5;

public interface Functor5<T1, T2, T3, T4, T5> {

    <U1, U2, U3, U4, U5> Functor5<U1, U2, U3, U4, U5> map(javaslang.function.Lambda5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, Tuple5<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5>> f);
}