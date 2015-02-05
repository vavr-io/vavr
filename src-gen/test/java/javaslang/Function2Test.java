/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.Function;
import org.junit.Test;

public class Function2Test {

    @Test
    public void shouldGetArity() {
        final Function2 f = (t1, t2) -> null;
        assertThat(f.arity()).isEqualTo(2);
    }

    @Test
    public void shouldCurry() {
        final Function2 f = (t1, t2) -> null;
        @SuppressWarnings("unchecked")
        final Function1<?, Function1<?, ?>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function2 f = (t1, t2) -> null;
        @SuppressWarnings("unchecked")
        final Function1<Tuple2, ?> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function2 f = (t1, t2) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function2 f = (t1, t2) -> null;
        final Function after = o -> null;
        @SuppressWarnings("unchecked")
        final Function2 composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }
}