/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.Arrays;
import java.util.Collections;

import javaslang.collection.List;
import javaslang.collection.Queue;
import javaslang.collection.Stream;
import javaslang.control.None;
import javaslang.control.Some;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Specific tests. For general tests, see AbstractIterableTest.
public class IterableTest {

    // -- eq

    @Test
    public void shouldEqNoneAndEmptyList() {
        assertThat(None.instance().eq(List.empty())).isTrue();
        assertThat(None.instance().eq(List.of(1))).isFalse();
    }

    @Test
    public void shouldEqSomeAndNonEmptyList() {
        assertThat(new Some<>(1).eq(List.of(1))).isTrue();
        assertThat(new Some<>(1).eq(List.of(2))).isFalse();
        assertThat(new Some<>(1).eq(List.empty())).isFalse();
    }

    @Test
    public void shouldEqIterableAndJavaIterable() {
        assertThat(List.ofAll(1, 2, 3).eq(Arrays.asList(1, 2, 3))).isTrue();
    }

    @Test
    public void shouldEqNestedIterables() {
        // ((1, 2), ((3)))
        final Iterable<?> i1 = List.ofAll(List.ofAll(1, 2), Collections.singletonList(List.of(3)));
        final Iterable<?> i2 = Queue.ofAll(Stream.ofAll(1, 2), List.of(Lazy.of(() -> 3)));
        final Iterable<?> i3 = Queue.ofAll(Stream.ofAll(1, 2), List.of(List.ofAll()));
        assertThat(i1.eq(i2)).isTrue();
        assertThat(i1.eq(i3)).isFalse();
    }

}
