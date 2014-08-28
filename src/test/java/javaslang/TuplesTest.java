/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Assertions.assertThat;
import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Objects;

import javaslang.Tuples.Tuple;
import javaslang.Tuples.Tuple0;
import javaslang.Tuples.Tuple1;
import javaslang.Tuples.Tuple10;
import javaslang.Tuples.Tuple11;
import javaslang.Tuples.Tuple12;
import javaslang.Tuples.Tuple13;
import javaslang.Tuples.Tuple2;
import javaslang.Tuples.Tuple3;
import javaslang.Tuples.Tuple4;
import javaslang.Tuples.Tuple5;
import javaslang.Tuples.Tuple6;
import javaslang.Tuples.Tuple7;
import javaslang.Tuples.Tuple8;
import javaslang.Tuples.Tuple9;

import org.junit.Test;

public class TuplesTest {

	// -- requirements on instantiation

	@Test
	public void shouldNotInstantiable() {
		assertThat(Tuples.class).isNotInstantiable();
	}

	// -- Tuple0

	@Test
	public void shouldCreateEmptyTuple() {
		assertThat(Tuples.empty().toString()).isEqualTo("()");
	}

	@Test
	public void shouldHashCodeTuple0() {
		assertThat(tuple0().hashCode()).isEqualTo(Objects.hash());
	}

	// -- Tuple1

	@Test
	public void shouldCreateSingle() {
		assertThat(tuple1().toString()).isEqualTo("(1)");
	}

	@Test
	public void shouldHashCodeTuple1() {
		final Tuple1<Integer> t = tuple1();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1));
	}

	// -- Tuple2

	@Test
	public void shouldCreatePair() {
		assertThat(tuple2().toString()).isEqualTo("(1, 2)");
	}

	@Test
	public void shouldHashCodeTuple2() {
		final Tuple2<Integer, Integer> t = tuple2();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2));
	}

	// -- Tuple3

	@Test
	public void shouldCreateTriple() {
		assertThat(tuple3().toString()).isEqualTo("(1, 2, 3)");
	}

	@Test
	public void shouldHashCodeTuple3() {
		final Tuple3<Integer, Integer, Integer> t = tuple3();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3));
	}

	// -- Tuple4

	@Test
	public void shouldCreateQuadruple() {
		assertThat(tuple4().toString()).isEqualTo("(1, 2, 3, 4)");
	}

	@Test
	public void shouldHashCodeTuple4() {
		final Tuple4<Integer, Integer, Integer, Integer> t = tuple4();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4));
	}

	// -- Tuple5

	@Test
	public void shouldCreateQuintuple() {
		assertThat(tuple5().toString()).isEqualTo("(1, 2, 3, 4, 5)");
	}

	@Test
	public void shouldHashCodeTuple5() {
		final Tuple5<Integer, Integer, Integer, Integer, Integer> t = tuple5();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5));
	}

	// -- Tuple6

	@Test
	public void shouldCreateSextuple() {
		assertThat(tuple6().toString()).isEqualTo("(1, 2, 3, 4, 5, 6)");
	}

	@Test
	public void shouldHashCodeTuple6() {
		final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> t = tuple6();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6));
	}

	// -- Tuple7

	@Test
	public void shouldCreateSeptuple() {
		assertThat(tuple7().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7)");
	}

	@Test
	public void shouldHashCodeTuple7() {
		final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple7();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
	}

	// -- Tuple8

	@Test
	public void shouldCreateOctuple() {
		assertThat(tuple8().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8)");
	}

	@Test
	public void shouldHashCodeTuple8() {
		final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple8();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
	}

	// -- Tuple9

	@Test
	public void shouldCreateNonuple() {
		assertThat(tuple9().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9)");
	}

	@Test
	public void shouldHashCodeTuple9() {
		final Tuple9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple9();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9));
	}

	// -- Tuple10

	@Test
	public void shouldCreateDecuple() {
		assertThat(tuple10().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)");
	}

	@Test
	public void shouldHashCodeTuple10() {
		final Tuple10<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple10();
		assertThat(t.hashCode()).isEqualTo(Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10));
	}

	// -- Tuple11

	@Test
	public void shouldCreateUndecuple() {
		assertThat(tuple11().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)");
	}

	@Test
	public void shouldHashCodeTuple11() {
		final Tuple11<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple11();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11));
	}

	// -- Tuple12

	@Test
	public void shouldCreateDuodecuple() {
		assertThat(tuple12().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)");
	}

	@Test
	public void shouldHashCodeTuple12() {
		final Tuple12<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple12();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12));
	}

	// -- Tuple13

	@Test
	public void shouldCreateTredecuple() {
		assertThat(tuple13().toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)");
	}

	@Test
	public void shouldHashCodeTuple13() {
		final Tuple13<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> t = tuple13();
		assertThat(t.hashCode()).isEqualTo(
				Objects.hash(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8, t._9, t._10, t._11, t._12, t._13));
	}

	// -- nested tuples

	@Test
	public void shouldDetectEqualityOnTupleOfTuples() {

		final Tuple tupleA = Tuples.of(Tuples.of(1), Tuples.of(1));
		final Tuple tupleB = Tuples.of(Tuples.of(1), Tuples.of(1));

		assertThat(tupleA.equals(tupleB)).isTrue();
	}

	@Test
	public void shouldDetectUnequalityOnTupleOfTuples() {

		final Tuple tupleA = Tuples.of(Tuples.of(1), Tuples.of(1));
		final Tuple tupleB = Tuples.of(Tuples.of(1), Tuples.of(2));

		assertThat(tupleA.equals(tupleB)).isFalse();
	}

	// -- Serializable interface

	@Test
	public void shouldSerializeDeserializeTuple0() throws Exception {
		final Object actual = deserialize(serialize(Tuple0.instance()));
		final Object expected = Tuple0.instance();
		assertThat(actual).isEqualTo(expected);
	}

	@Test
	public void shouldPreserveSingletonInstanceOnDeserialization() throws Exception {
		final boolean actual = deserialize(serialize(Tuple0.instance())) == Tuple0.instance();
		assertThat(actual).isTrue();
	}

	@Test
	public void shouldSerializeDeserializeNonEmptyList() throws Exception {
		final Object actual = deserialize(serialize(Tuples.of(1, 2, 3)));
		final Object expected = Tuples.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

	// -- helpers

	private Tuple0 tuple0() {
		return Tuples.empty();
	}

	private Tuple1<Integer> tuple1() {
		return Tuples.of(1);
	}

	private Tuple2<Integer, Integer> tuple2() {
		return Tuples.of(1, 2);
	}

	private Tuple3<Integer, Integer, Integer> tuple3() {
		return Tuples.of(1, 2, 3);
	}

	private Tuple4<Integer, Integer, Integer, Integer> tuple4() {
		return Tuples.of(1, 2, 3, 4);
	}

	private Tuple5<Integer, Integer, Integer, Integer, Integer> tuple5() {
		return Tuples.of(1, 2, 3, 4, 5);
	}

	private Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple6() {
		return Tuples.of(1, 2, 3, 4, 5, 6);
	}

	private Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple7() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7);
	}

	private Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple8() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8);
	}

	private Tuple9<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple9() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
	}

	private Tuple10<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple10() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	}

	private Tuple11<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple11() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11);
	}

	private Tuple12<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple12() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
	}

	private Tuple13<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple13() {
		return Tuples.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13);
	}
}
