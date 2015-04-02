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
import javaslang.Function8;
import javaslang.Tuple8;
import org.junit.Test;

public class Functor8Test {

    @Test
    public <T1, T2, T3, T4, T5, T6, T7, T8> void shouldMapComponentsSeparately() {
        final Functor8<T1, T2, T3, T4, T5, T6, T7, T8> functor = new Functor8<T1, T2, T3, T4, T5, T6, T7, T8>() {
            @SuppressWarnings("unchecked")
            @Override
            public <U1, U2, U3, U4, U5, U6, U7, U8> Functor8<U1, U2, U3, U4, U5, U6, U7, U8> map(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, Tuple8<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8>> f) {
                return (Functor8<U1, U2, U3, U4, U5, U6, U7, U8>) this;
            }
        };
        final Functor8<T1, T2, T3, T4, T5, T6, T7, T8> actual = functor.map(Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity());
        assertThat(actual).isNotNull();
    }
}