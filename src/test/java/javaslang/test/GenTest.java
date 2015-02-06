/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.collection.Stream;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class GenTest {

    @Test
    public void shouldUseCustomRandomNumberGenerator() {
        @SuppressWarnings("SerializableInnerClassWithNonSerializableOuterClass")
        final Random rng = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt(int bound) {
                return 0;
            }
        };
        final Gen<Integer> gen = Gen.choose(1, 2);
        final int actual = Stream.gen(() -> gen.apply(rng)).take(10).sum();
        assertThat(actual).isEqualTo(10);
    }
}
