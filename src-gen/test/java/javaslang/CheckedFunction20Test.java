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

public class CheckedFunction20Test {

    @Test
    public void shouldGetArity() {
        final CheckedFunction20 f = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> null;
        assertThat(f.arity()).isEqualTo(20);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction20 f = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, CheckedFunction1<?, ?>>>>>>>>>>>>>>>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction20 f = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction1<Tuple20, ?> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction20 f = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction20 f = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> null;
        final Function after = o -> null;
        @SuppressWarnings("unchecked")
        final CheckedFunction20 composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }
}