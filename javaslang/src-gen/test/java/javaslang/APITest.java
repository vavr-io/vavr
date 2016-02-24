/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang contributors
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static javaslang.API.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import javaslang.collection.CharSeq;
import javaslang.collection.Stream;
import org.junit.Test;

public class APITest {

    @Test
    public void shouldStreamFor1() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3)
        ).yield(i1 -> i1);
        assertThat(result.head()).isEqualTo(1);
        assertThat(result.tail().head()).isEqualTo(2);
    }

    @Test
    public void shouldStreamFor2() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2) -> i1 + i2);
        assertThat(result.head()).isEqualTo(2);
        assertThat(result.tail().head()).isEqualTo(3);
    }

    @Test
    public void shouldStreamFor3() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3);
        assertThat(result.head()).isEqualTo(3);
        assertThat(result.tail().head()).isEqualTo(4);
    }

    @Test
    public void shouldStreamFor4() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4);
        assertThat(result.head()).isEqualTo(4);
        assertThat(result.tail().head()).isEqualTo(5);
    }

    @Test
    public void shouldStreamFor5() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5);
        assertThat(result.head()).isEqualTo(5);
        assertThat(result.tail().head()).isEqualTo(6);
    }

    @Test
    public void shouldStreamFor6() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6);
        assertThat(result.head()).isEqualTo(6);
        assertThat(result.tail().head()).isEqualTo(7);
    }

    @Test
    public void shouldStreamFor7() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7);
        assertThat(result.head()).isEqualTo(7);
        assertThat(result.tail().head()).isEqualTo(8);
    }

    @Test
    public void shouldStreamFor8() {
        final Stream<Integer> result = For(
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3),
            Stream.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8);
        assertThat(result.head()).isEqualTo(8);
        assertThat(result.tail().head()).isEqualTo(9);
    }

    @Test
    public void shouldStreamNestedFor() {
        final Stream<String> result =
                For(Arrays.asList(1, 2), i ->
                        For(CharSeq.of('a', 'b')).yield(c -> i + ":" + c));
        assertThat(result).isEqualTo(Stream.of("1:a", "1:b", "2:a", "2:b"));
    }
}