/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedPredicateTest {

    @Test
    public void shouldCreateEqualityCheckForNull() {
        final CheckedPredicate<Object> cp = CheckedPredicate.isEqual(null);
        try {
            assertThat(cp.test(null)).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCreateEqualityCheckForNonNull() {
        final Object o = new Object();
        final CheckedPredicate<Object> cp = CheckedPredicate.isEqual(o);
        try {
            assertThat(cp.test(o)).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedPredicatesWithAnd() {
        final CheckedPredicate<Object> cp1 = o -> o == null;
        final CheckedPredicate<Object> cp2 = o -> o == null;
        try {
            final boolean actual = cp1.and(cp2).test(null);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldNegateCheckedPredicate() {
        final CheckedPredicate<Object> cp = o -> o == null;
        try {
            final boolean actual = cp.negate().test(null);
            assertThat(actual).isFalse();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedPredicatesWithOr() {
        final CheckedPredicate<Object> cp1 = o -> o != null;
        final CheckedPredicate<Object> cp2 = o -> o == null;
        try {
            final boolean actual = cp1.or(cp2).test(null);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
