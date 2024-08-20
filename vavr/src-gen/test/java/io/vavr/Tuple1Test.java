/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
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

public class Tuple1Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple1<Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple1<Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(1);
    }

    @Test
    public void shouldReturnElements() {
        final Tuple1<Integer> tuple = createIntTuple(1);
        assertThat(tuple._1).isEqualTo(1);
    }

    @Test
    public void shouldUpdate1() {
      final Tuple1<Integer> tuple = createIntTuple(1).update1(42);
      assertThat(tuple._1).isEqualTo(42);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1).toSeq();
        assertThat(actual).isEqualTo(List.of(1));
    }

    @Test
    public void shouldCompareEqual() {
        final Tuple1<Integer> t0 = createIntTuple(0);
        assertThat(t0.compareTo(t0)).isZero();
        assertThat(intTupleComparator.compare(t0, t0)).isZero();
    }

    @Test
    public void shouldCompare1stArg() {
        final Tuple1<Integer> t0 = createIntTuple(0);
        final Tuple1<Integer> t1 = createIntTuple(1);
        assertThat(t0.compareTo(t1)).isNegative();
        assertThat(t1.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t1)).isNegative();
        assertThat(intTupleComparator.compare(t1, t0)).isPositive();
    }

    @Test
    public void shouldMap() {
        final Tuple1<Object> tuple = createTuple();
        final Tuple1<Object> actual = tuple.map(o -> o);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple1<Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Tuple1<Object> actual = tuple.map(f1);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldReturnTuple1OfSequence1() {
      final Seq<Tuple1<Integer>> iterable = List.of(Tuple.of(2));
      final Tuple1<Seq<Integer>> expected = Tuple.of(Stream.of(2));
      assertThat(Tuple.sequence1(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple1<Object> tuple = createTuple();
        final Tuple0 actual = tuple.apply(o1 -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldAppendValue() {
        final Tuple2<Integer, Integer> actual = Tuple.of(1).append(2);
        final Tuple2<Integer, Integer> expected = Tuple.of(1, 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple1() {
        final Tuple2<Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2));
        final Tuple2<Integer, Integer> expected = Tuple.of(1, 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple2() {
        final Tuple3<Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3));
        final Tuple3<Integer, Integer, Integer> expected = Tuple.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple3() {
        final Tuple4<Integer, Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3, 4));
        final Tuple4<Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple4() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3, 4, 5));
        final Tuple5<Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple5() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3, 4, 5, 6));
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple6() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3, 4, 5, 6, 7));
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple7() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1).concat(Tuple.of(2, 3, 4, 5, 6, 7, 8));
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple1<Object> tuple1 = createTuple();
        final Tuple1<Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple1<Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldRecognizeNonEqualityPerComponent() {
        final Tuple1<String> tuple = Tuple.of("1");
        assertThat(tuple.equals(Tuple.of("X"))).isFalse();
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hashCode(null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple1<Integer>> intTupleComparator = Tuple1.comparator(Integer::compare);

    private Tuple1<Object> createTuple() {
        return new Tuple1<>(null);
    }

    private Tuple1<Integer> createIntTuple(Integer i1) {
        return new Tuple1<>(i1);
    }
}