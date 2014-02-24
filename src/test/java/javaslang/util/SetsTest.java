package javaslang.util;

import static javaslang.util.Sets.newSet;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Set;

import org.junit.Test;

public class SetsTest {

	@Test
	public void shouldComputeComplement() throws Exception {
		final Set<Integer> set1 = newSet(1, 2, 3);
		final Set<Integer> set2 = newSet(2, 3, 4);
		final Set<Integer> actual = Sets.complement(set1, set2);
		assertThat(actual).isEqualTo(newSet(1));
		assertThat(set1).isEqualTo(newSet(1, 2, 3));
		assertThat(set2).isEqualTo(newSet(2, 3, 4));
	}

	@Test
	public void shouldComputeIntersection() throws Exception {
		final Set<Integer> set1 = newSet(1, 2, 3);
		final Set<Integer> set2 = newSet(2, 3, 4);
		final Set<Integer> actual = Sets.intersection(set1, set2);
		assertThat(actual).isEqualTo(newSet(2, 3));
		assertThat(set1).isEqualTo(newSet(1, 2, 3));
		assertThat(set2).isEqualTo(newSet(2, 3, 4));
	}

	@Test
	public void shouldComputeUnion() throws Exception {
		final Set<Integer> set1 = newSet(1, 2, 3);
		final Set<Integer> set2 = newSet(2, 3, 4);
		final Set<Integer> actual = Sets.union(set1, set2);
		assertThat(actual).isEqualTo(newSet(1, 2, 3, 4));
		assertThat(set1).isEqualTo(newSet(1, 2, 3));
		assertThat(set2).isEqualTo(newSet(2, 3, 4));
	}

}
