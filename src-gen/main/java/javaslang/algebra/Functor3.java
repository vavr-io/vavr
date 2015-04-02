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
import javaslang.Function3;
import javaslang.Tuple;
import javaslang.Tuple3;

public interface Functor3<T1, T2, T3> {

    <U1, U2, U3> Functor3<U1, U2, U3> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<? extends U1, ? extends U2, ? extends U3>> f);

    default <U1, U2, U3> Functor3<U1, U2, U3> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3) {
        return map((t1, t2, t3) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3)));
    }
}