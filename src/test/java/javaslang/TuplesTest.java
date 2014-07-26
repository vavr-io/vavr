/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static javaslang.Tuples.of;
import static org.fest.assertions.api.Assertions.assertThat;
import javaslang.Tuples.Tuple;
import javaslang.Tuples.Tuple0;
import javaslang.collection.List;

import org.junit.Test;

public class TuplesTest {

	@Test
	public void shouldCreateEmptyTuple() {
		assertThat(of().toString()).isEqualTo("()");
	}

	@Test
	public void shouldCreateSingle() {
		assertThat(of(1).toString()).isEqualTo("(1)");
	}

	@Test
	public void shouldCreatePair() {
		assertThat(of(1, 2).toString()).isEqualTo("(1, 2)");
	}

	@Test
	public void shouldCreateTriple() {
		assertThat(of(1, 2, 3).toString()).isEqualTo("(1, 2, 3)");
	}

	@Test
	public void shouldCreateQuadruple() {
		assertThat(of(1, 2, 3, 4).toString()).isEqualTo("(1, 2, 3, 4)");
	}

	@Test
	public void shouldCreateQuintuple() {
		assertThat(of(1, 2, 3, 4, 5).toString()).isEqualTo("(1, 2, 3, 4, 5)");
	}

	@Test
	public void shouldCreateSextuple() {
		assertThat(of(1, 2, 3, 4, 5, 6).toString()).isEqualTo("(1, 2, 3, 4, 5, 6)");
	}

	@Test
	public void shouldCreateSeptuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7)");
	}

	@Test
	public void shouldCreateOctuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8)");
	}

	@Test
	public void shouldCreateNonuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9)");
	}

	@Test
	public void shouldCreateDecuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)");
	}

	@Test
	public void shouldCreateUndecuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)");
	}

	@Test
	public void shouldCreateDuodecuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)");
	}

	@Test
	public void shouldCreateTredecuple() {
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13).toString()).isEqualTo(
				"(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13)");
	}

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

		System.out.println("ser(List.empty()).length = " + serialize(List.empty()).length);
		System.out.println("ser(List.of(1)).length = " + serialize(List.of(1, 2, 3, 4, 5, 6)).length);
		System.out.println("ser(Tuple.of()).length = " + serialize(Tuples.of()).length);
		System.out.println("ser(Tuple.of(1)).length = " + serialize(Tuples.of(1, 2, 3, 4, 5, 6)).length);

		final Object actual = deserialize(serialize(Tuples.of(1, 2, 3)));
		final Object expected = Tuples.of(1, 2, 3);
		assertThat(actual).isEqualTo(expected);
	}

}
