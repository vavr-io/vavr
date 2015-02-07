/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedIntPredicateTest {

    @Test
    public void shouldCombineCheckedIntPredicatesWithAnd() {
        final CheckedIntPredicate cip1 = i -> i == 0;
        final CheckedIntPredicate cip2 = i -> i == 0;
        try {
            final boolean actual = cip1.and(cip2).test(0);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldNegateCheckedIntPredicate() {
        final CheckedIntPredicate cip = i -> i == 0;
        try {
            final boolean actual = cip.negate().test(0);
            assertThat(actual).isFalse();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedIntPredicatesWithOr() {
        final CheckedIntPredicate cip1 = i -> i == 1;
        final CheckedIntPredicate cip2 = i -> i == 0;
        try {
            final boolean actual = cip1.or(cip2).test(0);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
