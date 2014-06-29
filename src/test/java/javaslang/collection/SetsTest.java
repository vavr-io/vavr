package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import javaslang.collection.Sets;

import org.junit.Test;

public class SetsTest {

	@Test
	public void shouldComputeComplement() throws Exception {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		final Set<Integer> actual = Sets.complement(set1, set2);
		assertThat(actual).isEqualTo(Sets.of(1));
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeIntersection() throws Exception {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		final Set<Integer> actual = Sets.intersection(set1, set2);
		assertThat(actual).isEqualTo(Sets.of(2, 3));
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

	@Test
	public void shouldComputeUnion() throws Exception {
		final Set<Integer> set1 = Sets.of(1, 2, 3);
		final Set<Integer> set2 = Sets.of(2, 3, 4);
		final Set<Integer> actual = Sets.union(set1, set2);
		assertThat(actual).isEqualTo(Sets.of(1, 2, 3, 4));
		assertThat(set1).isEqualTo(Sets.of(1, 2, 3));
		assertThat(set2).isEqualTo(Sets.of(2, 3, 4));
	}

}
