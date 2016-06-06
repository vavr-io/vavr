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
    public void shouldRunUnitAndReturnVoid() {
        int[] i = { 0 };
        @SuppressWarnings("unused")
        Void nothing = run(() -> i[0]++);
        assertThat(i[0]).isEqualTo(1);
    }

    // -- For

    @Test
    public void shouldIterateFor1() {
        final List<Integer> result = For(
            List.of(1, 2, 3)
        ).yield(i1 -> i1).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 1));
        assertThat(result.head()).isEqualTo(1);
        assertThat(result.last()).isEqualTo(3 * 1);
    }

    @Test
    public void shouldIterateFor2() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2) -> i1 + i2).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 2));
        assertThat(result.head()).isEqualTo(2);
        assertThat(result.last()).isEqualTo(3 * 2);
    }

    @Test
    public void shouldIterateFor3() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 3));
        assertThat(result.head()).isEqualTo(3);
        assertThat(result.last()).isEqualTo(3 * 3);
    }

    @Test
    public void shouldIterateFor4() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 4));
        assertThat(result.head()).isEqualTo(4);
        assertThat(result.last()).isEqualTo(3 * 4);
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
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 5));
        assertThat(result.head()).isEqualTo(5);
        assertThat(result.last()).isEqualTo(3 * 5);
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
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 6));
        assertThat(result.head()).isEqualTo(6);
        assertThat(result.last()).isEqualTo(3 * 6);
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
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 7));
        assertThat(result.head()).isEqualTo(7);
        assertThat(result.last()).isEqualTo(3 * 7);
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
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 8));
        assertThat(result.head()).isEqualTo(8);
        assertThat(result.last()).isEqualTo(3 * 8);
    }

    @Test
    public void shouldIterateNestedFor() {
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

    // -- Match patterns

    static class ClzMatch {}
    static class ClzMatch1 extends ClzMatch {}
    static class ClzMatch2 extends ClzMatch {}

    @Test
    public void shouldMatchPattern1() {
        final Tuple1<Integer> tuple = Tuple.of(1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), (m1) -> "fail"),
                Case(Patterns.Tuple1($()), (m1) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), () -> "fail"),
                Case(Patterns.Tuple1($()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), "fail"),
                Case(Patterns.Tuple1($()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern1.of(ClzMatch1.class, $(), t -> Tuple.of(null)), "fail"),
                Case(API.Match.Pattern1.of(ClzMatch2.class, $(), t -> Tuple.of(null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern2() {
        final Tuple2<Integer, Integer> tuple = Tuple.of(1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), (m1, m2) -> "fail"),
                Case(Patterns.Tuple2($(), $()), (m1, m2) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), () -> "fail"),
                Case(Patterns.Tuple2($(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), "fail"),
                Case(Patterns.Tuple2($(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern2.of(ClzMatch1.class, $(), $(), t -> Tuple.of(null, null)), "fail"),
                Case(API.Match.Pattern2.of(ClzMatch2.class, $(), $(), t -> Tuple.of(null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern3() {
        final Tuple3<Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), (m1, m2, m3) -> "fail"),
                Case(Patterns.Tuple3($(), $(), $()), (m1, m2, m3) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), () -> "fail"),
                Case(Patterns.Tuple3($(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), "fail"),
                Case(Patterns.Tuple3($(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern3.of(ClzMatch1.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "fail"),
                Case(API.Match.Pattern3.of(ClzMatch2.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern4() {
        final Tuple4<Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), (m1, m2, m3, m4) -> "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), (m1, m2, m3, m4) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern4.of(ClzMatch1.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "fail"),
                Case(API.Match.Pattern4.of(ClzMatch2.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern5() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern5.of(ClzMatch1.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern5.of(ClzMatch2.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern6() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern6.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern6.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern7() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern7.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern7.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern8() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern8.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern8.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }
}