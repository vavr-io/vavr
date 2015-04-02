/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.Function1;
import javaslang.Function4;
import javaslang.Tuple4;
import org.junit.Test;

public class Functor4Test {

    @Test
    public <T1, T2, T3, T4> void shouldMapComponentsSeparately() {
        final Functor4<T1, T2, T3, T4> functor = new Functor4<T1, T2, T3, T4>() {
            @SuppressWarnings("unchecked")
            @Override
            public <U1, U2, U3, U4> Functor4<U1, U2, U3, U4> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f) {
                return (Functor4<U1, U2, U3, U4>) this;
            }
        };
        final Functor4<T1, T2, T3, T4> actual = functor.map(Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity());
        assertThat(actual).isNotNull();
    }
}