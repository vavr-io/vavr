/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.junit.Test;

import java.util.Objects;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
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
    public void shouldEqualSameTuple0Instances() {
        final Tuple0 t = tuple0();
        assertThat(t.equals(t)).isTrue();
    }

    @Test
    public void shouldNotTuple0EqualsNull() {
        assertThat(tuple0().equals(null)).isFalse();
    }

    @Test
    public void shouldNotTuple0EqualsObject() {
        assertThat(tuple0().equals(new Object())).isFalse();
    }

    @Test
    public void shouldTuple0EqualTuple0() {
        assertThat(tuple0().equals(tuple0())).isTrue();
    }

    @Test
    public void shouldDeserializeSingletonOfTuple0() {
        Object tuple0 = Serializables.deserialize(Serializables.serialize(Tuple0.instance()));
        assertThat(tuple0 == Tuple0.instance()).isTrue();
    }

    // -- Tuple1

    @Test
    public void shouldCreateSingle() {
        assertThat(tuple1().toString()).isEqualTo("(1)");
    }

    @Test
    public void shouldHashTuple1() {
        final Tuple1<?> t = tuple1();
        assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1));
    }

    @Test
    public void shouldReturnCorrectArityOfTuple1() {
        assertThat(tuple1().arity()).isEqualTo(1);
    }

    @Test
    public void shouldEqualSameTuple1Instances() {
        final Tuple1<?> t = tuple1();
        assertThat(t.equals(t)).isTrue();
    }

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

    // -- Tuple2

    @Test
    public void shouldCreatePair() {
        assertThat(tuple2().toString()).isEqualTo("(1, 2)");
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

    @Test
    public void shouldEqualSameTuple2Instances() {
        final Tuple2<?, ?> t = tuple2();
        assertThat(t.equals(t)).isTrue();
    }

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

    @Test
    public void shouldEqualSameTuple3Instances() {
        final Tuple3<?, ?, ?> t = tuple3();
        assertThat(t.equals(t)).isTrue();
    }

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

    @Test
    public void shouldEqualSameTuple4Instances() {
        final Tuple4<?, ?, ?, ?> t = tuple4();
        assertThat(t.equals(t)).isTrue();
    }

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

    @Test
    public void shouldEqualSameTuple5Instances() {
        final Tuple5<?, ?, ?, ?, ?> t = tuple5();
        assertThat(t.equals(t)).isTrue();
    }

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

    @Test
    public void shouldEqualSameTuple6Instances() {
        final Tuple6<?, ?, ?, ?, ?, ?> t = tuple6();
        assertThat(t.equals(t)).isTrue();
    }

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
        final Object actual = deserialize(serialize(Tuple0.instance()));
        final Object expected = Tuple0.instance();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(Tuple0.instance())) == Tuple0.instance();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonEmptyTuple() {
        final Object actual = deserialize(serialize(Tuple.of(1, 2, 3)));
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
}
