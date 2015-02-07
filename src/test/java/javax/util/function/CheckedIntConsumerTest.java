/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedIntConsumerTest {

    @Test
    public void shouldChainCheckedIntConsumersWithAndThen() {
        final byte[] j = new byte[]{0};
        final byte[] results = new byte[]{0, 0};
        final CheckedIntConsumer cic1 = i -> results[j[0]++] = 1;
        final CheckedIntConsumer cic2 = i -> results[j[0]++] = 2;
        try {
            cic1.andThen(cic2).accept(0);
            assertThat(results).isEqualTo(new byte[]{1, 2});
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
