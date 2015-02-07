/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedIntUnaryOperatorTest {

    @Test
    public void shouldComposeTwoCheckedIntUnaryOperatorsWithCompose() {
        final CheckedIntUnaryOperator ciuo1 = i -> i + 1;
        final CheckedIntUnaryOperator ciuo2 = i -> i * 2;
        try {
            final double actual = ciuo1.compose(ciuo2).applyAsInt(0);
            assertThat(actual).isEqualTo(1);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldComposeTwoCheckedIntUnaryOperatorsWithAndThen() {
        final CheckedIntUnaryOperator ciuo1 = i -> i + 1;
        final CheckedIntUnaryOperator ciuo2 = i -> i * 2;
        try {
            final double actual = ciuo1.andThen(ciuo2).applyAsInt(0);
            assertThat(actual).isEqualTo(2);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCreateIdentity() {
        final CheckedIntUnaryOperator identity = CheckedIntUnaryOperator.identity();
        try {
            final double actual = identity.applyAsInt(0);
            assertThat(actual).isEqualTo(0);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
