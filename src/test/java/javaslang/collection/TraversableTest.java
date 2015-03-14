/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TraversableTest {

    @Test
    public void shouldCreateTraversableOfTraversable() {
        assertThat(Traversable.of(List.nil())).isNotNull();
    }

    @Test
    public void shouldCreateTraversableOfIterable() {
        assertThat(Traversable.of(() -> List.nil().iterator())).isNotNull();
    }
}
