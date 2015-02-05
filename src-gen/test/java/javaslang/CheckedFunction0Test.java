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

public class CheckedFunction0Test {

    @Test
    public void shouldGetArity() {
        final CheckedFunction0 f = () -> null;
        assertThat(f.arity()).isEqualTo(0);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction0 f = () -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<?, ?> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction0 f = () -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<Tuple0, ?> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction0 f = () -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction0 f = () -> null;
        final Function after = o -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction0 composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }
}