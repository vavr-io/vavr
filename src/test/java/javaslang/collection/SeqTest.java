/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class SeqTest {

    // -- static of(Iterable)

    @Test(expected = NullPointerException.class)
    public void shouldThenWhenConstructingSeqOfNull() {
        Seq.of(null);
    }

    @Test
    public void shouldCreateSeqOfJavaslangList() {
        final Seq<Integer> actual = Seq.of(List.of(1, 2));
        final Seq<Integer> expected = List.of(1, 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldCreateSeqOfJavasUtilList() {
        final Seq<Integer> actual = Seq.of(Arrays.asList(1, 2));
        final Seq<Integer> expected = Stream.of(1, 2);
        assertThat(actual).isEqualTo(expected);
    }
}
