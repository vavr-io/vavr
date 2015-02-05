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

public class CheckedFunction8Test {

    @Test
    public void shouldGetArity() {
        final CheckedFunction8 f = (t1, t2, t3, t4, t5, t6, t7, t8) -> null;
        assertThat(f.arity()).isEqualTo(8);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction8 f = (t1, t2, t3, t4, t5, t6, t7, t8) -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, ?>>>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction8 f = (t1, t2, t3, t4, t5, t6, t7, t8) -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<Tuple8, ?> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction8 f = (t1, t2, t3, t4, t5, t6, t7, t8) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction8 f = (t1, t2, t3, t4, t5, t6, t7, t8) -> null;
        final Function after = o -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction8 composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }
}