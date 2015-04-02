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
import javaslang.Function2;
import javaslang.Tuple;
import javaslang.Tuple2;

public interface Functor2<T1, T2> {

    <U1, U2> Functor2<U1, U2> map(Function2<? super T1, ? super T2, Tuple2<? extends U1, ? extends U2>> f);

    default <U1, U2> Functor2<U1, U2> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2) {
        return map((t1, t2) -> Tuple.of(f1.apply(t1), f2.apply(t2)));
    }
}