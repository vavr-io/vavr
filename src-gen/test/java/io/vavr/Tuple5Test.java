/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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
import java.util.Comparator;
import java.util.Objects;
import org.junit.jupiter.api.Test;

public class Tuple5Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(5);
    }

    @Test
    public void shouldReturnElements() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5);
        assertThat(tuple._1).isEqualTo(1);
        assertThat(tuple._2).isEqualTo(2);
        assertThat(tuple._3).isEqualTo(3);
        assertThat(tuple._4).isEqualTo(4);
        assertThat(tuple._5).isEqualTo(5);
    }

    @Test
    public void shouldUpdate1() {
      final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).update1(42);
      assertThat(tuple._1).isEqualTo(42);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
    }

    @Test
    public void shouldUpdate2() {
      final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).update2(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(42);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
    }

    @Test
    public void shouldUpdate3() {
      final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).update3(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(42);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
    }

    @Test
    public void shouldUpdate4() {
      final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).update4(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(42);
      assertThat(tuple._5).isEqualTo(5);
    }

    @Test
    public void shouldUpdate5() {
      final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).update5(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(42);
    }

    @Test
    public void shouldRemove1() {
      final Tuple4<Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).remove1();
      assertThat(tuple._1).isEqualTo(2);
      assertThat(tuple._2).isEqualTo(3);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
    }

    @Test
    public void shouldRemove2() {
      final Tuple4<Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).remove2();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(3);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
    }

    @Test
    public void shouldRemove3() {
      final Tuple4<Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).remove3();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
    }

    @Test
    public void shouldRemove4() {
      final Tuple4<Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).remove4();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(5);
    }

    @Test
    public void shouldRemove5() {
      final Tuple4<Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5).remove5();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1, 0, 0, 0, 0).toSeq();
        assertThat(actual).isEqualTo(List.of(1, 0, 0, 0, 0));
    }

    @Test
    public void shouldMap() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Tuple5<Object, Object, Object, Object, Object> actual = tuple.map((o1, o2, o3, o4, o5) -> tuple);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Function1<Object, Object> f3 = Function1.identity();
      final Function1<Object, Object> f4 = Function1.identity();
      final Function1<Object, Object> f5 = Function1.identity();
      final Tuple5<Object, Object, Object, Object, Object> actual = tuple.map(f1, f2, f3, f4, f5);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldReturnTuple5OfSequence5() {
      final Seq<Tuple5<Integer, Integer, Integer, Integer, Integer>> iterable = List.of(Tuple.of(2, 3, 4, 5, 6), Tuple.of(4, 5, 6, 7, 8), Tuple.of(6, 7, 8, 9, 10), Tuple.of(8, 9, 10, 11, 12), Tuple.of(10, 11, 12, 13, 14));
      final Tuple5<Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(2, 4, 6, 8, 10), Stream.of(3, 5, 7, 9, 11), Stream.of(4, 6, 8, 10, 12), Stream.of(5, 7, 9, 11, 13), Stream.of(6, 8, 10, 12, 14));
      assertThat(Tuple.sequence5(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTuple5OfSequence1() {
      final Seq<Tuple5<Integer, Integer, Integer, Integer, Integer>> iterable = List.of(Tuple.of(1, 2, 3, 4, 5));
      final Tuple5<Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(1), Stream.of(2), Stream.of(3), Stream.of(4), Stream.of(5));
      assertThat(Tuple.sequence5(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldMap1stComponent() {
      final Tuple5<String, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1).map1(i -> "X");
      final Tuple5<String, Integer, Integer, Integer, Integer> expected = Tuple.of("X", 1, 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap2ndComponent() {
      final Tuple5<Integer, String, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1).map2(i -> "X");
      final Tuple5<Integer, String, Integer, Integer, Integer> expected = Tuple.of(1, "X", 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap3rdComponent() {
      final Tuple5<Integer, Integer, String, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1).map3(i -> "X");
      final Tuple5<Integer, Integer, String, Integer, Integer> expected = Tuple.of(1, 1, "X", 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap4thComponent() {
      final Tuple5<Integer, Integer, Integer, String, Integer> actual = Tuple.of(1, 1, 1, 1, 1).map4(i -> "X");
      final Tuple5<Integer, Integer, Integer, String, Integer> expected = Tuple.of(1, 1, 1, "X", 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap5thComponent() {
      final Tuple5<Integer, Integer, Integer, Integer, String> actual = Tuple.of(1, 1, 1, 1, 1).map5(i -> "X");
      final Tuple5<Integer, Integer, Integer, Integer, String> expected = Tuple.of(1, 1, 1, 1, "X");
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Tuple0 actual = tuple.apply((o1, o2, o3, o4, o5) -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldPrependValue() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2, 3, 4, 5).prepend(6);
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(6, 1, 2, 3, 4, 5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldAppendValue() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2, 3, 4, 5).append(6);
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple1() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2, 3, 4, 5).concat(Tuple.of(6));
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple2() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2, 3, 4, 5).concat(Tuple.of(6, 7));
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConcatTuple3() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 2, 3, 4, 5).concat(Tuple.of(6, 7, 8));
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple5<Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple5<Object, Object, Object, Object, Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldRecognizeNonEqualityPerComponent() {
        final Tuple5<String, String, String, String, String> tuple = Tuple.of("1", "2", "3", "4", "5");
        assertThat(tuple.equals(Tuple.of("X", "2", "3", "4", "5"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "X", "3", "4", "5"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "X", "4", "5"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "X", "5"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "4", "X"))).isFalse();
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple5<Integer, Integer, Integer, Integer, Integer>> intTupleComparator = Tuple5.comparator(Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare);

    private Tuple5<Object, Object, Object, Object, Object> createTuple() {
        return new Tuple5<>(null, null, null, null, null);
    }

    private Tuple5<Integer, Integer, Integer, Integer, Integer> createIntTuple(Integer i1, Integer i2, Integer i3, Integer i4, Integer i5) {
        return new Tuple5<>(i1, i2, i3, i4, i5);
    }
}