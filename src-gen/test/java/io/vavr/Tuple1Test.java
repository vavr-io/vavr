/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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
    public void shouldRemove1() {
      final Tuple0 tuple = createIntTuple(1).remove1();

    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1).toSeq();
        assertThat(actual).isEqualTo(List.of(1));
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
    public void shouldPrependValue() {
        final Tuple2<Integer, Integer> actual = Tuple.of(1).prepend(2);
        final Tuple2<Integer, Integer> expected = Tuple.of(2, 1);
        assertThat(actual).isEqualTo(expected);
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

    private Tuple1<Object> createTuple() {
        return new Tuple1<>(null);
    }

    private Tuple1<Integer> createIntTuple(Integer i1) {
        return new Tuple1<>(i1);
    }
}