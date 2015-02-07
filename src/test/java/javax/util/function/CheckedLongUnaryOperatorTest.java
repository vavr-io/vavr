/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedLongUnaryOperatorTest {

    @Test
    public void shouldComposeTwoCheckedLongUnaryOperatorsWithCompose() {
        final CheckedLongUnaryOperator cluo1 = l -> l + 1;
        final CheckedLongUnaryOperator cluo2 = l -> l * 2;
        try {
            final double actual = cluo1.compose(cluo2).applyAsLong(0L);
            assertThat(actual).isEqualTo(1L);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldComposeTwoCheckedLongUnaryOperatorsWithAndThen() {
        final CheckedLongUnaryOperator cluo1 = l -> l + 1;
        final CheckedLongUnaryOperator cluo2 = l -> l * 2;
        try {
            final double actual = cluo1.andThen(cluo2).applyAsLong(0L);
            assertThat(actual).isEqualTo(2L);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCreateIdentity() {
        final CheckedLongUnaryOperator identity = CheckedLongUnaryOperator.identity();
        try {
            final double actual = identity.applyAsLong(0L);
            assertThat(actual).isEqualTo(0L);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
