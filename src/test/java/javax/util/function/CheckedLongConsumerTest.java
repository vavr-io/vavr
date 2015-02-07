/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CheckedLongConsumerTest {

    @Test
    public void shouldChainCheckedLongConsumersWithAndThen() {
        final byte[] i = new byte[]{0};
        final byte[] results = new byte[]{0, 0};
        final CheckedLongConsumer clc1 = l -> results[i[0]++] = 1;
        final CheckedLongConsumer clc2 = l -> results[i[0]++] = 2;
        try {
            clc1.andThen(clc2).accept(0L);
            assertThat(results).isEqualTo(new byte[]{1, 2});
        } catch (Throwable x) {
            fail("Exception occurred", x);
        }
    }
}
