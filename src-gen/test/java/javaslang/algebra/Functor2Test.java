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
import javaslang.Function2;
import javaslang.Tuple2;
import org.junit.Test;

public class Functor2Test {

    @Test
    public <T1, T2> void shouldMapComponentsSeparately() {
        final Functor2<T1, T2> functor = new Functor2<T1, T2>() {
            @SuppressWarnings("unchecked")
            @Override
            public <U1, U2> Functor2<U1, U2> map(Function2<? super T1, ? super T2, Tuple2<? extends U1, ? extends U2>> f) {
                return (Functor2<U1, U2>) this;
            }
        };
        final Functor2<T1, T2> actual = functor.map(Function1.identity(), Function1.identity());
        assertThat(actual).isNotNull();
    }
}