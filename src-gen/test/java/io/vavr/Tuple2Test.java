/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import org.junit.Test;

public class Tuple2Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple2<Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple2<Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(2);
    }

    @Test
    public void shouldReturnElements() {
        final Tuple2<Integer, Integer> tuple = createIntTuple(1, 2);
        assertThat(tuple._1).isEqualTo(1);
        assertThat(tuple._2).isEqualTo(2);
    }

    @Test
    public void shouldUpdate1() {
      final Tuple2<Integer, Integer> tuple = createIntTuple(1, 2).update1(42);
      assertThat(tuple._1).isEqualTo(42);
      assertThat(tuple._2).isEqualTo(2);
    }

    @Test
    public void shouldUpdate2() {
      final Tuple2<Integer, Integer> tuple = createIntTuple(1, 2).update2(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(42);
    }

    @Test
    public void shouldRemove1() {
      final Tuple1<Integer> tuple = createIntTuple(1, 2).remove1();
      assertThat(tuple._1).isEqualTo(2);
    }

    @Test
    public void shouldRemove2() {
      final Tuple1<Integer> tuple = createIntTuple(1, 2).remove2();
      assertThat(tuple._1).isEqualTo(1);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1, 0).toSeq();
        assertThat(actual).isEqualTo(List.of(1, 0));
    }

    @Test
    public void shouldSwap() {
        assertThat(createIntTuple(1, 2).swap()).isEqualTo(createIntTuple(2, 1));
    }

    @Test
    public void shouldConvertToEntry() {
        Tuple2<Integer, Integer> tuple = createIntTuple(1,2);
        Map.Entry<Integer, Integer> entry = new AbstractMap.SimpleEntry<>(1, 2);
        assertThat(tuple.toEntry().equals(entry));
    }

    @Test
    public void shouldMap() {
        final Tuple2<Object, Object> tuple = createTuple();
        final Tuple2<Object, Object> actual = tuple.map((o1, o2) -> tuple);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple2<Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Tuple2<Object, Object> actual = tuple.map(f1, f2);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldReturnTuple2OfSequence2() {
      final Seq<Tuple2<Integer, Integer>> iterable = List.of(Tuple.of(2, 3), Tuple.of(4, 5));
      final Tuple2<Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(2, 4), Stream.of(3, 5));
      assertThat(Tuple.sequence2(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTuple2OfSequence1() {
      final Seq<Tuple2<Integer, Integer>> iterable = List.of(Tuple.of(1, 2));
      final Tuple2<Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(1), Stream.of(2));
      assertThat(Tuple.sequence2(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldMap1stComponent() {
      final Tuple2<String, Integer> actual = Tuple.of(1, 1).map1(i -> "X");
      final Tuple2<String, Integer> expected = Tuple.of("X", 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap2ndComponent() {
      final Tuple2<Integer, String> actual = Tuple.of(1, 1).map2(i -> "X");
      final Tuple2<Integer, String> expected = Tuple.of(1, "X");
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple2<Object, Object> tuple = createTuple();
        final Tuple0 actual = tuple.apply((o1, o2) -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldPrependValue() {
        final Tuple3<Integer, Integer, Integer> actual = Tuple.of(1, 2).prepend(3);
        final Tuple3<Integer, Integer, Integer> expected = Tuple.of(3, 1, 2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendValue() {
        final Tuple3<Integer, Integer, Integer> actual = Tuple.of(1, 2).append(3);
        final Tuple3<Integer, Integer, Integer> expected = Tuple.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple1() {
        final Tuple3<Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3));
        final Tuple3<Integer, Integer, Integer> expected = Tuple.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple2() {
        final Tuple4<Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3, 4));
        final Tuple4<Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple3() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3, 4, 5));
        final Tuple5<Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple4() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3, 4, 5, 6));
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple5() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3, 4, 5, 6, 7));
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple6() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2).concat(Tuple.of(3, 4, 5, 6, 7, 8));
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple2<Object, Object> tuple1 = createTuple();
        final Tuple2<Object, Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple2<Object, Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldRecognizeNonEqualityPerComponent() {
        final Tuple2<String, String> tuple = Tuple.of("1", "2");
        assertThat(tuple.equals(Tuple.of("X", "2"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "X"))).isFalse();
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple2<Integer, Integer>> intTupleComparator = Tuple2.comparator(Integer::compare, Integer::compare);

    private Tuple2<Object, Object> createTuple() {
        return new Tuple2<>(null, null);
    }

    private Tuple2<Integer, Integer> createIntTuple(Integer i1, Integer i2) {
        return new Tuple2<>(i1, i2);
    }
}