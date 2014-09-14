/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javaslang.AssertionsExtensions;

import org.junit.Test;

public class SetsTest {

	@Test
	public void shouldNotBeInstantiable() {
		AssertionsExtensions.assertThat(Sets.class).isNotInstantiable();
	}

	// -- complement

	@Test
	public void shouldComputeComplementOfTwoNonEmptySets() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.complement(set1, set2)).isEqualTo(Sets.of(1));
	}

	@Test
	public void shouldLeaveSet1UntouchedWhenComputingComplement() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.complement(set1, set2);
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
	}

	@Test
	public void shouldLeaveSet2UntouchedWhenComputingComplement() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.complement(set1, set2);
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeEmptyComplementWhenSet1IsEmpty() {
		final Set<Integer> set1 = Sets.of();
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.complement(set1, set2)).isEqualTo(Sets.of());
	}

	@Test
	public void shouldComputeComplementEqualToSet1WhenSet2IsEmpty() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of();
		assertThat(Sets.complement(set1, set2)).isEqualTo(Sets.of(1, 2, 3));
	}

	// -- intersection

	@Test
	public void shouldComputeIntersectionOfTwoNonEmptySets() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.intersection(set1, set2)).isEqualTo(Sets.of(2, 3));
	}

	@Test
	public void shouldLeaveSet1UntouchedWhenComputingIntersection() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.intersection(set1, set2);
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
	}

	@Test
	public void shouldLeaveSet2UntouchedWhenComputingIntersection() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.intersection(set1, set2);
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeEmptyIntersectionWhenSet1IsEmpty() {
		final Set<Integer> set1 = Sets.of();
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.intersection(set1, set2)).isEqualTo(Sets.of());
	}

	@Test
	public void shouldComputeEmptyIntersectionWhenSet2IsEmpty() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of();
		assertThat(Sets.intersection(set1, set2)).isEqualTo(Sets.of());
	}

	// -- union

	@Test
	public void shouldComputeUnionOfTwoNonEmptySets() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.union(set1, set2)).isEqualTo(Sets.of(1, 2, 3, 4));
	}

	@Test
	public void shouldLeaveSet1UntouchedWhenComputingUnion() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.union(set1, set2);
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
	}

	@Test
	public void shouldLeaveSet2UntouchedWhenComputingUnion() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		Sets.union(set1, set2);
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeUnionWhenSet1IsEmpty() {
		final Set<Integer> set1 = Sets.of();
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		assertThat(Sets.union(set1, set2)).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeUnionWhenSet2IsEmpty() {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of();
		assertThat(Sets.union(set1, set2)).isEqualTo(Sets.of(1, 2, 3));
	}
}
