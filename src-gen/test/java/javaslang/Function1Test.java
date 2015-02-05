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

public class Function1Test {

    @Test
    public void shouldGetArity() {
        final Function1 f = (t1) -> null;
        assertThat(f.arity()).isEqualTo(1);
    }

    @Test
    public void shouldCurry() {
        final Function1 f = (t1) -> null;
        @SuppressWarnings("unchecked")
        final Function1<?, ?> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function1 f = (t1) -> null;
        @SuppressWarnings("unchecked")
        final Function1<Tuple1, ?> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function1 f = (t1) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function1 f = (t1) -> null;
        final Function after = o -> null;
        @SuppressWarnings("unchecked")
        final Function1 composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }
}