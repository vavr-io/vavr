/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import javaslang.collection.Setz;

import org.junit.Test;

public class SetzTest {

	@Test
	public void shouldComputeComplement() throws Exception {
		final Set<Integer> set1 = Setz.of(1, 2, 3);
		final Set<Integer> set2 = Setz.of(2, 3, 4);
		final Set<Integer> actual = Setz.complement(set1, set2);
		assertThat(actual).isEqualTo(Setz.of(1));
		assertThat(set1).isEqualTo(Setz.of(1, 2, 3));
		assertThat(set2).isEqualTo(Setz.of(2, 3, 4));
	}

	@Test
	public void shouldComputeIntersection() throws Exception {
		final Set<Integer> set1 = Setz.of(1, 2, 3);
		final Set<Integer> set2 = Setz.of(2, 3, 4);
		final Set<Integer> actual = Setz.intersection(set1, set2);
		assertThat(actual).isEqualTo(Setz.of(2, 3));
		assertThat(set1).isEqualTo(Setz.of(1, 2, 3));
		assertThat(set2).isEqualTo(Setz.of(2, 3, 4));
	}

	@Test
	public void shouldComputeUnion() throws Exception {
		final Set<Integer> set1 = Setz.of(1, 2, 3);
		final Set<Integer> set2 = Setz.of(2, 3, 4);
		final Set<Integer> actual = Setz.union(set1, set2);
		assertThat(actual).isEqualTo(Setz.of(1, 2, 3, 4));
		assertThat(set1).isEqualTo(Setz.of(1, 2, 3));
		assertThat(set2).isEqualTo(Setz.of(2, 3, 4));
	}

}
