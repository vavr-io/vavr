/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

public class Tuple8Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(8);
    }

    @Test
    public void shouldReturnElements() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8);
        assertThat(tuple._1).isEqualTo(1);
        assertThat(tuple._2).isEqualTo(2);
        assertThat(tuple._3).isEqualTo(3);
        assertThat(tuple._4).isEqualTo(4);
        assertThat(tuple._5).isEqualTo(5);
        assertThat(tuple._6).isEqualTo(6);
        assertThat(tuple._7).isEqualTo(7);
        assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate1() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update1(42);
      assertThat(tuple._1).isEqualTo(42);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate2() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update2(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(42);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate3() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update3(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(42);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate4() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update4(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(42);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate5() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update5(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(42);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate6() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update6(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(42);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate7() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update7(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(42);
      assertThat(tuple._8).isEqualTo(8);
    }

    @Test
    public void shouldUpdate8() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).update8(42);
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
      assertThat(tuple._8).isEqualTo(42);
    }

    @Test
    public void shouldRemove1() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove1();
      assertThat(tuple._1).isEqualTo(2);
      assertThat(tuple._2).isEqualTo(3);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
      assertThat(tuple._5).isEqualTo(6);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove2() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove2();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(3);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
      assertThat(tuple._5).isEqualTo(6);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove3() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove3();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(4);
      assertThat(tuple._4).isEqualTo(5);
      assertThat(tuple._5).isEqualTo(6);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove4() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove4();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(5);
      assertThat(tuple._5).isEqualTo(6);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove5() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove5();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(6);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove6() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove6();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(7);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove7() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove7();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(8);
    }

    @Test
    public void shouldRemove8() {
      final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = createIntTuple(1, 2, 3, 4, 5, 6, 7, 8).remove8();
      assertThat(tuple._1).isEqualTo(1);
      assertThat(tuple._2).isEqualTo(2);
      assertThat(tuple._3).isEqualTo(3);
      assertThat(tuple._4).isEqualTo(4);
      assertThat(tuple._5).isEqualTo(5);
      assertThat(tuple._6).isEqualTo(6);
      assertThat(tuple._7).isEqualTo(7);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1, 0, 0, 0, 0, 0, 0, 0).toSeq();
        assertThat(actual).isEqualTo(List.of(1, 0, 0, 0, 0, 0, 0, 0));
    }

    @Test
    public void shouldMap() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map((o1, o2, o3, o4, o5, o6, o7, o8) -> tuple);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Function1<Object, Object> f3 = Function1.identity();
      final Function1<Object, Object> f4 = Function1.identity();
      final Function1<Object, Object> f5 = Function1.identity();
      final Function1<Object, Object> f6 = Function1.identity();
      final Function1<Object, Object> f7 = Function1.identity();
      final Function1<Object, Object> f8 = Function1.identity();
      final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map(f1, f2, f3, f4, f5, f6, f7, f8);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldReturnTuple8OfSequence8() {
      final Seq<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> iterable = List.of(Tuple.of(2, 3, 4, 5, 6, 7, 8, 9), Tuple.of(4, 5, 6, 7, 8, 9, 10, 11), Tuple.of(6, 7, 8, 9, 10, 11, 12, 13), Tuple.of(8, 9, 10, 11, 12, 13, 14, 15), Tuple.of(10, 11, 12, 13, 14, 15, 16, 17), Tuple.of(12, 13, 14, 15, 16, 17, 18, 19), Tuple.of(14, 15, 16, 17, 18, 19, 20, 21), Tuple.of(16, 17, 18, 19, 20, 21, 22, 23));
      final Tuple8<Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(2, 4, 6, 8, 10, 12, 14, 16), Stream.of(3, 5, 7, 9, 11, 13, 15, 17), Stream.of(4, 6, 8, 10, 12, 14, 16, 18), Stream.of(5, 7, 9, 11, 13, 15, 17, 19), Stream.of(6, 8, 10, 12, 14, 16, 18, 20), Stream.of(7, 9, 11, 13, 15, 17, 19, 21), Stream.of(8, 10, 12, 14, 16, 18, 20, 22), Stream.of(9, 11, 13, 15, 17, 19, 21, 23));
      assertThat(Tuple.sequence8(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTuple8OfSequence1() {
      final Seq<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> iterable = List.of(Tuple.of(1, 2, 3, 4, 5, 6, 7, 8));
      final Tuple8<Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>, Seq<Integer>> expected = Tuple.of(Stream.of(1), Stream.of(2), Stream.of(3), Stream.of(4), Stream.of(5), Stream.of(6), Stream.of(7), Stream.of(8));
      assertThat(Tuple.sequence8(iterable)).isEqualTo(expected);
    }

    @Test
    public void shouldMap1stComponent() {
      final Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map1(i -> "X");
      final Tuple8<String, Integer, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of("X", 1, 1, 1, 1, 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap2ndComponent() {
      final Tuple8<Integer, String, Integer, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map2(i -> "X");
      final Tuple8<Integer, String, Integer, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, "X", 1, 1, 1, 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap3rdComponent() {
      final Tuple8<Integer, Integer, String, Integer, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map3(i -> "X");
      final Tuple8<Integer, Integer, String, Integer, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 1, "X", 1, 1, 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap4thComponent() {
      final Tuple8<Integer, Integer, Integer, String, Integer, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map4(i -> "X");
      final Tuple8<Integer, Integer, Integer, String, Integer, Integer, Integer, Integer> expected = Tuple.of(1, 1, 1, "X", 1, 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap5thComponent() {
      final Tuple8<Integer, Integer, Integer, Integer, String, Integer, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map5(i -> "X");
      final Tuple8<Integer, Integer, Integer, Integer, String, Integer, Integer, Integer> expected = Tuple.of(1, 1, 1, 1, "X", 1, 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap6thComponent() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, String, Integer, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map6(i -> "X");
      final Tuple8<Integer, Integer, Integer, Integer, Integer, String, Integer, Integer> expected = Tuple.of(1, 1, 1, 1, 1, "X", 1, 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap7thComponent() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, String, Integer> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map7(i -> "X");
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, String, Integer> expected = Tuple.of(1, 1, 1, 1, 1, 1, "X", 1);
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMap8thComponent() {
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, String> actual = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1).map8(i -> "X");
      final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, String> expected = Tuple.of(1, 1, 1, 1, 1, 1, 1, "X");
      assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldApplyTuple() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Tuple0 actual = tuple.apply((o1, o2, o3, o4, o5, o6, o7, o8) -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldRecognizeNonEqualityPerComponent() {
        final Tuple8<String, String, String, String, String, String, String, String> tuple = Tuple.of("1", "2", "3", "4", "5", "6", "7", "8");
        assertThat(tuple.equals(Tuple.of("X", "2", "3", "4", "5", "6", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "X", "3", "4", "5", "6", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "X", "4", "5", "6", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "X", "5", "6", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "4", "X", "6", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "4", "5", "X", "7", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "4", "5", "6", "X", "8"))).isFalse();
        assertThat(tuple.equals(Tuple.of("1", "2", "3", "4", "5", "6", "7", "X"))).isFalse();
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null, null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null, null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> intTupleComparator = Tuple8.comparator(Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare);

    private Tuple8<Object, Object, Object, Object, Object, Object, Object, Object> createTuple() {
        return new Tuple8<>(null, null, null, null, null, null, null, null);
    }

    private Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> createIntTuple(Integer i1, Integer i2, Integer i3, Integer i4, Integer i5, Integer i6, Integer i7, Integer i8) {
        return new Tuple8<>(i1, i2, i3, i4, i5, i6, i7, i8);
    }
}