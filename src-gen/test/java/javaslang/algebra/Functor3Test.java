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
import javaslang.Function3;
import javaslang.Tuple3;
import org.junit.Test;

public class Functor3Test {

    @Test
    public <T1, T2, T3> void shouldMapComponentsSeparately() {
        final Functor3<T1, T2, T3> functor = new Functor3<T1, T2, T3>() {
            @SuppressWarnings("unchecked")
            @Override
            public <U1, U2, U3> Functor3<U1, U2, U3> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<? extends U1, ? extends U2, ? extends U3>> f) {
                return (Functor3<U1, U2, U3>) this;
            }
        };
        final Functor3<T1, T2, T3> actual = functor.map(Function1.identity(), Function1.identity(), Function1.identity());
        assertThat(actual).isNotNull();
    }
}