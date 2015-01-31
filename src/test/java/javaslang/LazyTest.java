/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class LazyTest {

    @Test
    public void shouldMemoizeValues() {
        final Lazy<Double> testee = Lazy.of(Math::random);
        for (int i = 0; i < 100; i++) {
            final double actual = testee.get();
            final double expected = testee.get();
            assertThat(actual).isEqualTo(expected);
        }
    }
}
