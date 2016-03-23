/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
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
import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

public class APITest {

    @Test
    public void shouldNotBeInstantiable() {
        AssertionsExtensions.assertThat(API.class).isNotInstantiable();
    }

    // -- run

    @Test
    public void shouldRunUnit() {
        int[] i = { 0 };
        run(() -> i[0]++).run();
        assertThat(i[0]).isEqualTo(1);
    }

    // -- For

    @Test
    public void shouldIterateFor1() {
        final List<Integer> result = For(
            List.of(1, 2, 3)
        ).yield(i1 -> i1).toList();
        assertThat(result.head()).isEqualTo(1);
        assertThat(result.tail().head()).isEqualTo(2);
    }

    @Test
    public void shouldIterateFor2() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2) -> i1 + i2).toList();
        assertThat(result.head()).isEqualTo(2);
        assertThat(result.tail().head()).isEqualTo(3);
    }

    @Test
    public void shouldIterateFor3() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3).toList();
        assertThat(result.head()).isEqualTo(3);
        assertThat(result.tail().head()).isEqualTo(4);
    }

    @Test
    public void shouldIterateFor4() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4).toList();
        assertThat(result.head()).isEqualTo(4);
        assertThat(result.tail().head()).isEqualTo(5);
    }

    @Test
    public void shouldIterateFor5() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5).toList();
        assertThat(result.head()).isEqualTo(5);
        assertThat(result.tail().head()).isEqualTo(6);
    }

    @Test
    public void shouldIterateFor6() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6).toList();
        assertThat(result.head()).isEqualTo(6);
        assertThat(result.tail().head()).isEqualTo(7);
    }

    @Test
    public void shouldIterateFor7() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7).toList();
        assertThat(result.head()).isEqualTo(7);
        assertThat(result.tail().head()).isEqualTo(8);
    }

    @Test
    public void shouldIterateFor8() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8).toList();
        assertThat(result.head()).isEqualTo(8);
        assertThat(result.tail().head()).isEqualTo(9);
    }

    @Test
    public void shouldStreamNestedFor() {
        final List<String> result =
                For(Arrays.asList(1, 2), i ->
                        For(CharSeq.of('a', 'b')).yield(c -> i + ":" + c)).toList();
        assertThat(result).isEqualTo(List.of("1:a", "1:b", "2:a", "2:b"));
    }

    // -- Match

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndSupplier() {
        assertThat(Case(ignored -> true, ignored -> 1).apply(null)).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndSupplier() {
        assertThat(Case(ignored -> false, ignored -> 1).apply(null)).isEqualTo(Option.none());
    }

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndValue() {
        assertThat(Case(ignored -> true, 1).apply(null)).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndValue() {
        assertThat(Case(ignored -> false, 1).apply(null)).isEqualTo(Option.none());
    }

}