/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedDoubleUnaryOperatorTest {

    @Test
    public void shouldComposeTwoCheckedDoubleUnaryOperatorsWithCompose() {
        final CheckedDoubleUnaryOperator cduo1 = d -> d + 1;
        final CheckedDoubleUnaryOperator cduo2 = d -> d * 2;
        try {
            final double actual = cduo1.compose(cduo2).applyAsDouble(0d);
            assertThat(actual).isEqualTo(1d);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldComposeTwoCheckedDoubleUnaryOperatorsWithAndThen() {
        final CheckedDoubleUnaryOperator cduo1 = d -> d + 1;
        final CheckedDoubleUnaryOperator cduo2 = d -> d * 2;
        try {
            final double actual = cduo1.andThen(cduo2).applyAsDouble(0d);
            assertThat(actual).isEqualTo(2d);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }

    @Test
    public void shouldCreateIdentity() {
        final CheckedDoubleUnaryOperator identity = CheckedDoubleUnaryOperator.identity();
        try {
            final double actual = identity.applyAsDouble(0d);
            assertThat(actual).isEqualTo(0d);
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
