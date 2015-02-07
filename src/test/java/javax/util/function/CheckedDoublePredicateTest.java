/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedDoublePredicateTest {

    @Test
    public void shouldCombineCheckedDoublePredicatesWithAnd() {
        final CheckedDoublePredicate cdp1 = d -> d == 0L;
        final CheckedDoublePredicate cdp2 = d -> d == 0L;
        try {
            final boolean actual = cdp1.and(cdp2).test(0L);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldNegateCheckedDoublePredicate() {
        final CheckedDoublePredicate cdp = d -> d == 0d;
        try {
            final boolean actual = cdp.negate().test(0d);
            assertThat(actual).isFalse();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedDoublePredicatesWithOr() {
        final CheckedDoublePredicate cdp1 = d -> d == 1d;
        final CheckedDoublePredicate cdp2 = d -> d == 0d;
        try {
            final boolean actual = cdp1.or(cdp2).test(0d);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
