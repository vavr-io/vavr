/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedUnaryOperatorTest {

    @Test
    public void shouldCreateIdentity() {
        final Object o = new Object();
        final CheckedUnaryOperator<Object> identity = CheckedUnaryOperator.identity();
        try {
            assertThat(identity.apply(o)).isEqualTo(o);
        } catch(Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
