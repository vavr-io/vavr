/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedBiPredicateTest {

    @Test
    public void shouldCombineCheckedBiPredicatesWithAnd() {
        final CheckedBiPredicate<Object, Object> cbp1 = (o1, o2) -> o1 == null && o2 == null;
        final CheckedBiPredicate<Object, Object> cbp2 = (o1, o2) -> o1 == o2;
        try {
            final boolean actual = cbp1.and(cbp2).test(null, null);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldNegateCheckedBiPredicate() {
        final CheckedBiPredicate<Object, Object> cbp = (o1, o2) -> o1 == null && o2 == null;
        try {
            final boolean actual = cbp.negate().test(null, null);
            assertThat(actual).isFalse();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCombineCheckedBiPredicatesWithOr() {
        final CheckedBiPredicate<Object, Object> cbp1 = (o1, o2) -> o1 != o2;
        final CheckedBiPredicate<Object, Object> cbp2 = (o1, o2) -> o1 == o2;
        try {
            final boolean actual = cbp1.or(cbp2).test(null, null);
            assertThat(actual).isTrue();
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
