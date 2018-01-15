/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import java.util.Comparator;
import java.util.Objects;
import org.junit.Test;

public class Tuple3Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple3<Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple3<Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(3);
    }

    @Test
    public void shouldReturnElements() {
        final Tuple3<Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3);
        assertThat(tuple._1).isEqualTo(1);
        assertThat(tuple._2).isEqualTo(2);
        assertThat(tuple._3).isEqualTo(3);
    }

    @Test
    public void shouldUpdate1() {
      final Tuple3<Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3).update1(42);
      assertThat(tuple._1).isEqualTo(42);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
    }

    @Test
    public void shouldUpdate2() {
      final Tuple3<Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3).update2(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(42);
      assertThat(tuple._3).isEqualTo(3);
    }

    @Test
    public void shouldUpdate3() {
      final Tuple3<Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3).update3(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(42);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1, 0, 0).toSeq();
        assertThat(actual).isEqualTo(List.of(1, 0, 0));
    }

    @Test
    public void shouldCompareEqual() {
        final Tuple3<Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0);
        assertThat(t0.compareTo(t0)).isZero();
        assertThat(intTupleComparator.compare(t0, t0)).isZero();
    }

    @Test
    public void shouldCompare1stArg() {
        final Tuple3<Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0);
        final Tuple3<Integer, Integer, Integer> t1 = createIntTuple(1, 0, 0);
        assertThat(t0.compareTo(t1)).isNegative();
        assertThat(t1.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t1)).isNegative();
        assertThat(intTupleComparator.compare(t1, t0)).isPositive();
    }

    @Test
    public void shouldCompare2ndArg() {
        final Tuple3<Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0);
        final Tuple3<Integer, Integer, Integer> t2 = createIntTuple(0, 1, 0);
        assertThat(t0.compareTo(t2)).isNegative();
        assertThat(t2.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t2)).isNegative();
        assertThat(intTupleComparator.compare(t2, t0)).isPositive();
    }

    @Test
    public void shouldCompare3rdArg() {
        final Tuple3<Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0);
        final Tuple3<Integer, Integer, Integer> t3 = createIntTuple(0, 0, 1);
        assertThat(t0.compareTo(t3)).isNegative();
        assertThat(t3.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t3)).isNegative();
        assertThat(intTupleComparator.compare(t3, t0)).isPositive();
    }

    @Test
    public void shouldMap() {
        final Tuple3<Object, Object, Object> tuple = createTuple();
        final Tuple3<Object, Object, Object> actual = tuple.map((o1, o2, o3) -> tuple);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple3<Object, Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Function1<Object, Object> f3 = Function1.identity();
      final Tuple3<Object, Object, Object> actual = tuple.map(f1, f2, f3);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldReturnTuple3OfSequence3() {
      final Seq<Tuple3<Integer, Integer, Integer>> iterable = List.of(Tuple.of(2, 3, 4), Tuple.of(4, 5, 6), Tuple.of(6, 7, 8));
      final Tuple3<Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(2, 4, 6), Stream.of(3, 5, 7), Stream.of(4, 6, 8));
      assertThat(Tuple.sequence3(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTuple3OfSequence1() {
      final Seq<Tuple3<Integer, Integer, Integer>> iterable = List.of(Tuple.of(1, 2, 3));
      final Tuple3<Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(1), Stream.of(2), Stream.of(3));
      assertThat(Tuple.sequence3(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldMap1stComponent() {
      final Tuple3<String, Integer, Integer> actual = Tuple.of(1, 1, 1).map1(i -> "X");
      final Tuple3<String, Integer, Integer> expected = Tuple.of("X", 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap2ndComponent() {
      final Tuple3<Integer, String, Integer> actual = Tuple.of(1, 1, 1).map2(i -> "X");
      final Tuple3<Integer, String, Integer> expected = Tuple.of(1, "X", 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap3rdComponent() {
      final Tuple3<Integer, Integer, String> actual = Tuple.of(1, 1, 1).map3(i -> "X");
      final Tuple3<Integer, Integer, String> expected = Tuple.of(1, 1, "X");
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple3<Object, Object, Object> tuple = createTuple();
        final Tuple0 actual = tuple.apply((o1, o2, o3) -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple3<Object, Object, Object> tuple1 = createTuple();
        final Tuple3<Object, Object, Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple3<Object, Object, Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldRecognizeNonEqualityPerComponent() {
        final Tuple3<String, String, String> tuple = Tuple.of("1", "2", "3");
        assertThat(tuple.equals(Tuple.of("X", "2", "3"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "X", "3"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "X"))).isFalse();
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple3<Integer, Integer, Integer>> intTupleComparator = Tuple3.comparator(Integer::compare, Integer::compare, Integer::compare);

    private Tuple3<Object, Object, Object> createTuple() {
        return new Tuple3<>(null, null, null);
    }

    private Tuple3<Integer, Integer, Integer> createIntTuple(Integer i1, Integer i2, Integer i3) {
        return new Tuple3<>(i1, i2, i3);
    }
}