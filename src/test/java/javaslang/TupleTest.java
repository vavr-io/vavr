/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;

import javaslang.Tuple;
import javaslang.Tuple.Tuple0;
import javaslang.Tuple.Tuple1;
import javaslang.Tuple.Tuple10;
import javaslang.Tuple.Tuple11;
import javaslang.Tuple.Tuple12;
import javaslang.Tuple.Tuple13;
import javaslang.Tuple.Tuple2;
import javaslang.Tuple.Tuple3;
import javaslang.Tuple.Tuple4;
import javaslang.Tuple.Tuple5;
import javaslang.Tuple.Tuple6;
import javaslang.Tuple.Tuple7;
import javaslang.Tuple.Tuple8;
import javaslang.Tuple.Tuple9;

import org.junit.Test;

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

	// -- Tuple7

	@Test
	public void shouldCreateSeptuple() {
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

	@Test
	public void shouldEqualSameTuple7Instances() {
		final Tuple7<?, ?, ?, ?, ?, ?, ?> t = tuple7();
		assertThat(t.equals(t)).isTrue();
	}

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

	// -- Tuple8

	@Test
	public void shouldCreateOctuple() {
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

	@Test
	public void shouldEqualSameTuple8Instances() {
		final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> t = tuple8();
		assertThat(t.equals(t)).isTrue();
	}

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

	// -- Tuple9

	@Test
	public void shouldCreateNonuple() {
		assertThat(tuple9().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9)");
	}

	@Test
	public void shouldHashTuple9() {
		final Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple9();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9));
	}

	@Test
	public void shouldReturnCorrectArityOfTuple9() {
		assertThat(tuple9().arity()).isEqualTo(9);
	}

	@Test
	public void shouldEqualSameTuple9Instances() {
		final Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple9();
		assertThat(t.equals(t)).isTrue();
	}

	@Test
	public void shouldNotTuple9EqualsNull() {
		assertThat(tuple9().equals(null)).isFalse();
	}

	@Test
	public void shouldNotTuple9EqualsObject() {
		assertThat(tuple9().equals(new Object())).isFalse();
	}

	@Test
	public void shouldTuple9EqualTuple9() {
		assertThat(tuple9().equals(tuple9())).isTrue();
	}

	// -- Tuple10

	@Test
	public void shouldCreateDecuple() {
		assertThat(tuple10().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)");
	}

	@Test
	public void shouldHashTuple10() {
		final Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple10();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10));
	}

	@Test
	public void shouldReturnCorrectArityOfTuple10() {
		assertThat(tuple10().arity()).isEqualTo(10);
	}

	@Test
	public void shouldEqualSameTuple10Instances() {
		final Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple10();
		assertThat(t.equals(t)).isTrue();
	}

	@Test
	public void shouldNotTuple10EqualsNull() {
		assertThat(tuple10().equals(null)).isFalse();
	}

	@Test
	public void shouldNotTuple10EqualsObject() {
		assertThat(tuple10().equals(new Object())).isFalse();
	}

	@Test
	public void shouldTuple10EqualTuple10() {
		assertThat(tuple10().equals(tuple10())).isTrue();
	}

	// -- Tuple11

	@Test
	public void shouldCreateUndecuple() {
		assertThat(tuple11().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)");
	}

	@Test
	public void shouldHashTuple11() {
		final Tuple11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple11();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11));
	}

	@Test
	public void shouldReturnCorrectArityOfTuple11() {
		assertThat(tuple11().arity()).isEqualTo(11);
	}

	@Test
	public void shouldEqualSameTuple11Instances() {
		final Tuple11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple11();
		assertThat(t.equals(t)).isTrue();
	}

	@Test
	public void shouldNotTuple11EqualsNull() {
		assertThat(tuple11().equals(null)).isFalse();
	}

	@Test
	public void shouldNotTuple11EqualsObject() {
		assertThat(tuple11().equals(new Object())).isFalse();
	}

	@Test
	public void shouldTuple11EqualTuple11() {
		assertThat(tuple11().equals(tuple11())).isTrue();
	}

	// -- Tuple12

	@Test
	public void shouldCreateDuodecuple() {
		assertThat(tuple12().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)");
	}

	@Test
	public void shouldHashTuple12() {
		final Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple12();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12));
	}

	@Test
	public void shouldReturnCorrectArityOfTuple12() {
		assertThat(tuple12().arity()).isEqualTo(12);
	}

	@Test
	public void shouldEqualSameTuple12Instances() {
		final Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple12();
		assertThat(t.equals(t)).isTrue();
	}

	@Test
	public void shouldNotTuple12EqualsNull() {
		assertThat(tuple12().equals(null)).isFalse();
	}

	@Test
	public void shouldNotTuple12EqualsObject() {
		assertThat(tuple12().equals(new Object())).isFalse();
	}

	@Test
	public void shouldTuple12EqualTuple12() {
		assertThat(tuple12().equals(tuple12())).isTrue();
	}

	// -- Tuple13

	@Test
	public void shouldCreateTredecuple() {
		assertThat(tuple13().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)");
	}

	@Test
	public void shouldHashTuple13() {
		final Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple13();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13));
	}

	@Test
	public void shouldReturnCorrectArityOfTuple13() {
		assertThat(tuple13().arity()).isEqualTo(13);
	}

	@Test
	public void shouldEqualSameTuple13Instances() {
		final Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> t = tuple13();
		assertThat(t.equals(t)).isTrue();
	}

	@Test
	public void shouldNotTuple13EqualsNull() {
		assertThat(tuple13().equals(null)).isFalse();
	}

	@Test
	public void shouldNotTuple13EqualsObject() {
		assertThat(tuple13().equals(new Object())).isFalse();
	}

	@Test
	public void shouldTuple13EqualTuple13() {
		assertThat(tuple13().equals(tuple13())).isTrue();
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

	private Tuple7<?, ?, ?, ?, ?, ?, ?> tuple7() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7);
	}

	private Tuple8<?, ?, ?, ?, ?, ?, ?, ?> tuple8() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
	}

	private Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> tuple9() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	private Tuple10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple10() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

	private Tuple11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple11() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
	}

	private Tuple12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple12() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
	}

	private Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> tuple13() {
		return Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
	}
}
