/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedLongPredicateTest {

    @Test
    public void shouldCombineCheckedLongPredicatesWithAnd() {
        final CheckedLongPredicate clp1 = l -> l == 0L;
        final CheckedLongPredicate clp2 = l -> l == 0L;
        try {
            final boolean actual = clp1.and(clp2).test(0L);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldNegateCheckedLongPredicate() {
        final CheckedLongPredicate clp = l -> l == 0L;
        try {
            final boolean actual = clp.negate().test(0L);
            assertThat(actual).isFalse();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedLongPredicatesWithOr() {
        final CheckedLongPredicate clp1 = l -> l == 1L;
        final CheckedLongPredicate clp2 = l -> l == 0L;
        try {
            final boolean actual = clp1.or(clp2).test(0L);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
