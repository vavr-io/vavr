/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.fest.assertions.api.Assertions.assertThat;
import static javaslang.Tuples.*;

import java.util.ArrayList;
import java.util.List;

import javaslang.Tuples;

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
		assertThat(of(1, 2, 3, 4, 5, 6, 7, 8, 9).toString()).isEqualTo("(1, 2, 3, 4, 5, 6, 7, 8, 9)");
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
	public void shouldDetectLoopOnEquals() {
		
		// create recursive structure1
		final List<Tuple> listA = new ArrayList<>();
		final Tuple tupleA = Tuples.of(listA);
		listA.add(tupleA);

		// create recursive structure2
		final List<Tuple> listB = new ArrayList<>();
		final Tuple tupleB = Tuples.of(listB);
		listB.add(tupleB);
		
		// detect that structures are not the same
		final boolean actual = tupleA.equals(tupleB);
		assertThat(actual).isFalse();
	}

	@Test
	public void shouldDetectLoopOnHashCode() {
		
		// create recursive structure
		final List<Tuple1<?>> list = new ArrayList<>();
		final Tuple1<List<?>> tuple = Tuples.of(list);
		list.add(tuple);
		
		// detect loop on hashCode
		final int actual = tuple.hashCode();
		assertThat(actual).isNotNull();
	}

}
