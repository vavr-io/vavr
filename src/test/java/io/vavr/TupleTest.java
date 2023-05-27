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

import io.vavr.collection.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleTest {

    // -- Tuple0

    @Test
    public void shouldCreateEmptyTuple() {
        assertThat(Tuple.empty().toString()).isEqualTo("()");
    }

    @Test
    public void shouldHashTuple0() {
        assertThat(tuple0().hashCode()).isEqualTo(Objects.hash());
    }

    @Test
    public void shouldReturnCorrectArityOfTuple0() {
        assertThat(tuple0().arity()).isEqualTo(0);
    }

    @Test
    public void shouldReturnCorrectSeqOfTuple0() {
        Assertions.assertThat(tuple0().toSeq()).isEqualTo(List.empty());
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple0Instances() {
        final Tuple0 t = tuple0();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple0EqualsNull() {
        assertThat(tuple0().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple0EqualsObject() {
        assertThat(tuple0().equals(new Object())).isFalse();
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldTuple0EqualTuple0() {
        assertThat(tuple0().equals(tuple0())).isTrue();
    }

    @Test
    public void shouldDeserializeSingletonOfTuple0() {
        final Object tuple0 = Serializables.deserialize(Serializables.serialize(Tuple0.instance()));
        assertThat(tuple0 == Tuple0.instance()).isTrue();
    }

    @Test
    public void shouldReturnComparator() {
        assertThat(Tuple0.comparator().compare(Tuple0.instance(), Tuple0.instance())).isEqualTo(0);
    }

    @Test
    public void shouldApplyTuple0() {
        assertThat(Tuple0.instance().apply(() -> 1) == 1).isTrue();
    }

    // -- Tuple1

    @Test
    public void shouldCreateSingle() {
        assertThat(tuple1().toString()).isEqualTo("(1)");
    }

    @Test
    public void shouldHashTuple1() {
        final Tuple1<?> t = tuple1();
        assertThat(t.hashCode()).isEqualTo(Tuple.hash(t._1));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple1() {
        assertThat(tuple1().arity()).isEqualTo(1);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple1Instances() {
        final Tuple1<?> t = tuple1();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple1EqualsNull() {
        assertThat(tuple1().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple1EqualsObject() {
        assertThat(tuple1().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple1EqualTuple1() {
        assertThat(tuple1().equals(tuple1())).isTrue();
    }

    @Test
    public void shouldNarrowTuple1() {
        final Tuple1<Double> wideTuple = Tuple.of(1.0d);
        final Tuple1<Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo(1.0d);
    }

    // -- Tuple2

    @Test
    public void shouldCreatePair() {
        assertThat(tuple2().toString()).isEqualTo("(1, 2)");
    }

    @Test
    public void shouldCreateTuple2FromEntry() {
        final Tuple2<Integer, Integer> tuple2FromEntry = Tuple.fromEntry(new AbstractMap.SimpleEntry<>(1, 2));
        assertThat(tuple2FromEntry.toString()).isEqualTo("(1, 2)");
    }

    @Test
    public void shouldHashTuple2() {
        final Tuple2<?, ?> t = tuple2();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple2() {
        assertThat(tuple2().arity()).isEqualTo(2);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple2Instances() {
        final Tuple2<?, ?> t = tuple2();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple2EqualsNull() {
        assertThat(tuple2().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple2EqualsObject() {
        assertThat(tuple2().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple2EqualTuple2() {
        assertThat(tuple2().equals(tuple2())).isTrue();
    }

    @Test
    public void shouldNarrowTuple2() {
        final Tuple2<String, Double> wideTuple = Tuple.of("test", 1.0d);
        final Tuple2<CharSequence, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("test");
        assertThat(narrowTuple._2()).isEqualTo(1.0d);
    }

    // -- Tuple3

    @Test
    public void shouldCreateTriple() {
        assertThat(tuple3().toString()).isEqualTo("(1, 2, 3)");
    }

    @Test
    public void shouldHashTuple3() {
        final Tuple3<?, ?, ?> t = tuple3();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple3() {
        assertThat(tuple3().arity()).isEqualTo(3);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple3Instances() {
        final Tuple3<?, ?, ?> t = tuple3();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple3EqualsNull() {
        assertThat(tuple3().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple3EqualsObject() {
        assertThat(tuple3().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple3EqualTuple3() {
        assertThat(tuple3().equals(tuple3())).isTrue();
    }

    @Test
    public void shouldNarrowTuple3() {
        final Tuple3<String, Double, Float> wideTuple = Tuple.of("zero", 1.0D, 2.0F);
        final Tuple3<CharSequence, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
    }

    // -- Tuple4

    @Test
    public void shouldCreateQuadruple() {
        assertThat(tuple4().toString()).isEqualTo("(1, 2, 3, 4)");
    }

    @Test
    public void shouldHashTuple4() {
        final Tuple4<?, ?, ?, ?> t = tuple4();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple4() {
        assertThat(tuple4().arity()).isEqualTo(4);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple4Instances() {
        final Tuple4<?, ?, ?, ?> t = tuple4();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple4EqualsNull() {
        assertThat(tuple4().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple4EqualsObject() {
        assertThat(tuple4().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple4EqualTuple4() {
        assertThat(tuple4().equals(tuple4())).isTrue();
    }

    @Test
    public void shouldNarrowTuple4() {
        final Tuple4<String, Double, Float, Integer> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3);
        final Tuple4<CharSequence, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
        assertThat(narrowTuple._4()).isEqualTo(3);
    }

    // -- Tuple5

    @Test
    public void shouldCreateQuintuple() {
        assertThat(tuple5().toString()).isEqualTo("(1, 2, 3, 4, 5)");
    }

    @Test
    public void shouldHashTuple5() {
        final Tuple5<?, ?, ?, ?, ?> t = tuple5();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple5() {
        assertThat(tuple5().arity()).isEqualTo(5);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple5Instances() {
        final Tuple5<?, ?, ?, ?, ?> t = tuple5();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple5EqualsNull() {
        assertThat(tuple5().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple5EqualsObject() {
        assertThat(tuple5().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple5EqualTuple5() {
        assertThat(tuple5().equals(tuple5())).isTrue();
    }

    @Test
    public void shouldNarrowTuple5() {
        final Tuple5<String, Double, Float, Integer, Long> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L);
        final Tuple5<CharSequence, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
        assertThat(narrowTuple._4()).isEqualTo(3);
        assertThat(narrowTuple._5()).isEqualTo(4L);
    }

    // -- Tuple6

    @Test
    public void shouldCreateSextuple() {
        assertThat(tuple6().toString()).isEqualTo("(1, 2, 3, 4, 5, 6)");
    }

    @Test
    public void shouldHashTuple6() {
        final Tuple6<?, ?, ?, ?, ?, ?> t = tuple6();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple6() {
        assertThat(tuple6().arity()).isEqualTo(6);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple6Instances() {
        final Tuple6<?, ?, ?, ?, ?, ?> t = tuple6();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple6EqualsNull() {
        assertThat(tuple6().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple6EqualsObject() {
        assertThat(tuple6().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple6EqualTuple6() {
        assertThat(tuple6().equals(tuple6())).isTrue();
    }

    @Test
    public void shouldNarrowTuple6() {
        final Tuple6<String, Double, Float, Integer, Long, Byte> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5);
        final Tuple6<CharSequence, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
        assertThat(narrowTuple._4()).isEqualTo(3);
        assertThat(narrowTuple._5()).isEqualTo(4L);
        assertThat(narrowTuple._6()).isEqualTo((byte) 5);
    }

    // -- Tuple7

    @Test
    public void shouldCreateTuple7() {
        assertThat(tuple7().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7)");
    }

    @Test
    public void shouldHashTuple7() {
        final Tuple7<?, ?, ?, ?, ?, ?, ?> t = tuple7();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple7() {
        assertThat(tuple7().arity()).isEqualTo(7);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple7Instances() {
        final Tuple7<?, ?, ?, ?, ?, ?, ?> t = tuple7();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple7EqualsNull() {
        assertThat(tuple7().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple7EqualsObject() {
        assertThat(tuple7().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple7EqualTuple7() {
        assertThat(tuple7().equals(tuple7())).isTrue();
    }

    @Test
    public void shouldNarrowTuple7() {
        final Tuple7<String, Double, Float, Integer, Long, Byte, Short> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5, (short) 6);
        final Tuple7<CharSequence, Number, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
        assertThat(narrowTuple._4()).isEqualTo(3);
        assertThat(narrowTuple._5()).isEqualTo(4L);
        assertThat(narrowTuple._6()).isEqualTo((byte) 5);
        assertThat(narrowTuple._7()).isEqualTo((short) 6);
    }

    // -- Tuple8

    @Test
    public void shouldCreateTuple8() {
        assertThat(tuple8().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8)");
    }

    @Test
    public void shouldHashTuple8() {
        final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> t = tuple8();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple8() {
        assertThat(tuple8().arity()).isEqualTo(8);
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void shouldEqualSameTuple8Instances() {
        final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> t = tuple8();
        assertThat(t.equals(t)).isTrue();
    }

    @SuppressWarnings("ObjectEqualsNull")
    @Test
    public void shouldNotTuple8EqualsNull() {
        assertThat(tuple8().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple8EqualsObject() {
        assertThat(tuple8().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple8EqualTuple8() {
        assertThat(tuple8().equals(tuple8())).isTrue();
    }

    @Test
    public void shouldNarrowTuple8() {
        final Tuple8<String, Double, Float, Integer, Long, Byte, Short, BigDecimal> wideTuple = Tuple.of("zero", 1.0D, 2.0F, 3, 4L, (byte) 5, (short) 6, new BigDecimal(7));
        final Tuple8<CharSequence, Number, Number, Number, Number, Number, Number, Number> narrowTuple = Tuple.narrow(wideTuple);
        assertThat(narrowTuple._1()).isEqualTo("zero");
        assertThat(narrowTuple._2()).isEqualTo(1.0D);
        assertThat(narrowTuple._3()).isEqualTo(2.0F);
        assertThat(narrowTuple._4()).isEqualTo(3);
        assertThat(narrowTuple._5()).isEqualTo(4L);
        assertThat(narrowTuple._6()).isEqualTo((byte) 5);
        assertThat(narrowTuple._7()).isEqualTo((short) 6);
        assertThat(narrowTuple._8()).isEqualTo(new BigDecimal(7));
    }

    // -- nested tuples

    @Test
    public void shouldDetectEqualityOnTupleOfTuples() {

        final Tuple tupleA = Tuple.of(Tuple.of(1), Tuple.of(1));
        final Tuple tupleB = Tuple.of(Tuple.of(1), Tuple.of(1));

        assertThat(tupleA.equals(tupleB)).isTrue();
    }

    @Test
    public void shouldDetectUnequalityOnTupleOfTuples() {

        final Tuple tupleA = Tuple.of(Tuple.of(1), Tuple.of(1));
        final Tuple tupleB = Tuple.of(Tuple.of(1), Tuple.of(2));

        assertThat(tupleA.equals(tupleB)).isFalse();
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeTuple0() {
        final Object actual = Serializables.deserialize(Serializables.serialize(Tuple0.instance()));
        final Object expected = Tuple0.instance();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = Serializables.deserialize(Serializables.serialize(Tuple0.instance())) == Tuple0.instance();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonEmptyTuple() {
        final Object actual = Serializables.deserialize(Serializables.serialize(Tuple.of(1, 2, 3)));
        final Object expected = Tuple.of(1, 2, 3);
        assertThat(actual).isEqualTo(expected);
    }

    // -- helpers

    private Tuple0 tuple0() {
        return Tuple.empty();
    }

    private Tuple1<?> tuple1() {
        return Tuple.of(1);
    }

    private Tuple2<?, ?> tuple2() {
        return Tuple.of(1, 2);
    }

    private Tuple3<?, ?, ?> tuple3() {
        return Tuple.of(1, 2, 3);
    }

    private Tuple4<?, ?, ?, ?> tuple4() {
        return Tuple.of(1, 2, 3, 4);
    }

    private Tuple5<?, ?, ?, ?, ?> tuple5() {
        return Tuple.of(1, 2, 3, 4, 5);
    }

    private Tuple6<?, ?, ?, ?, ?, ?> tuple6() {
        return Tuple.of(1, 2, 3, 4, 5, 6);
    }

    private Tuple7<?, ?, ?, ?, ?, ?, ?> tuple7() {
        return Tuple.of(1, 2, 3, 4, 5, 6, 7);
    }

    private Tuple8<?, ?, ?, ?, ?, ?, ?, ?> tuple8() {
        return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
    }
}
