/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedConsumerTest {

    @Test
    public void shouldChainCheckedConsumersWithAndThen() {
        final byte[] i = new byte[]{0};
        final byte[] results = new byte[]{0, 0};
        final CheckedConsumer<Object> cc1 = o -> results[i[0]++] = 1;
        final CheckedConsumer<Object> cc2 = o -> results[i[0]++] = 2;
        try {
            cc1.andThen(cc2).accept(null);
            assertThat(results).isEqualTo(new byte[]{1, 2});
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
