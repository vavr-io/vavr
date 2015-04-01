/**    / \____  _    ______   _____ / \____   ____  _____
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
import javaslang.Function6;
import javaslang.Tuple6;
import org.junit.Test;

public class Functor6Test {

    @Test
    public <T1, T2, T3, T4, T5, T6> void shouldMapComponentsSeparately() {
        final Functor6<T1, T2, T3, T4, T5, T6> functor = new Functor6<T1, T2, T3, T4, T5, T6>() {
            @SuppressWarnings("unchecked")
            @Override
            public <U1, U2, U3, U4, U5, U6> Functor6<U1, U2, U3, U4, U5, U6> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6>> f) {
                return (Functor6<U1, U2, U3, U4, U5, U6>) this;
            }
        };
        final Functor6<T1, T2, T3, T4, T5, T6> actual = functor.map(Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity(), Function1.identity());
        assertThat(actual).isNotNull();
    }
}