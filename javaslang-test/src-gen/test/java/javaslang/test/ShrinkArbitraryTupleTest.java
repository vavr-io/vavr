/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.function.BiPredicate;
import javaslang.*;
import org.junit.Test;

public class ShrinkArbitraryTupleTest {
    @Test
    public void shouldShrinkArbitraryTuple1() throws Exception {
        final Shrink<Tuple1<Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer());

        final Arbitrary<Tuple1<Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple1: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple1: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple2() throws Exception {
        final Shrink<Tuple2<Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple2<Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple2: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple2: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple3() throws Exception {
        final Shrink<Tuple3<Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple3<Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple3: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple3: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple4() throws Exception {
        final Shrink<Tuple4<Integer, Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple4<Integer, Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple4: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple4: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0) &&
                        shrinks.exists(t -> t._4 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple5() throws Exception {
        final Shrink<Tuple5<Integer, Integer, Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple5<Integer, Integer, Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple5: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple5: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0) &&
                        shrinks.exists(t -> t._4 == 0) &&
                        shrinks.exists(t -> t._5 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple6() throws Exception {
        final Shrink<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple6: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple6: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0) &&
                        shrinks.exists(t -> t._4 == 0) &&
                        shrinks.exists(t -> t._5 == 0) &&
                        shrinks.exists(t -> t._6 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple7() throws Exception {
        final Shrink<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple7: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple7: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0) &&
                        shrinks.exists(t -> t._4 == 0) &&
                        shrinks.exists(t -> t._5 == 0) &&
                        shrinks.exists(t -> t._6 == 0) &&
                        shrinks.exists(t -> t._7 == 0))
                .check()
                .assertIsSatisfied();
    }

    @Test
    public void shouldShrinkArbitraryTuple8() throws Exception {
        final Shrink<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> shrink = ShrinkTuple.of(
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer(),
                        Shrink.integer());

        final Arbitrary<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> tuples = ArbitraryTuple.of(
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0),
                        Arbitrary.integer().filter(i -> i > 0));

        Property.def("tuple8: Not contains initial value")
                .forAll(tuples.map(tuple -> Tuple.of(tuple, shrink.apply(tuple))))
                .suchThat(pair((tuple, shrinks) -> !shrinks.contains(tuple)))
                .check()
                .assertIsSatisfied();

        Property.def("tuple8: Shrinks each component")
                .forAll(tuples.map(shrink::apply))
                .suchThat(shrinks ->
                        shrinks.exists(t -> t._1 == 0) &&
                        shrinks.exists(t -> t._2 == 0) &&
                        shrinks.exists(t -> t._3 == 0) &&
                        shrinks.exists(t -> t._4 == 0) &&
                        shrinks.exists(t -> t._5 == 0) &&
                        shrinks.exists(t -> t._6 == 0) &&
                        shrinks.exists(t -> t._7 == 0) &&
                        shrinks.exists(t -> t._8 == 0))
                .check()
                .assertIsSatisfied();
    }

    private static <L, R> CheckedFunction1<Tuple2<L, R>, Boolean> pair(BiPredicate<L, R> predicate) {
        return t -> predicate.test(t._1, t._2);
    }
}