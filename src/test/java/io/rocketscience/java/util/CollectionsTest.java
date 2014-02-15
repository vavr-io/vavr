package io.rocketscience.java.util;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class CollectionsTest {
	
	@Test
	public void shoudRecognizeNullList() {
		final List<?> list = null;
		final boolean actual = Collections.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final boolean actual = Collections.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shoudRecognizeNoneEmptyList() {
		final List<?> list = java.util.Arrays.asList(1, 2, 3);
		final boolean actual = Collections.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(false);
	}

	@Test
	public void shouldReturnNoneOnLastElementOfNullList() throws Exception {
		final List<?> list = null;
		final Option<?> actual = Collections.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnNoneOnLastElementOfEmptyList() throws Exception {
		final List<?> list = java.util.Collections.emptyList();
		final Option<?> actual = Collections.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnLastElementOfNoneEmptyList() throws Exception {
		final List<Integer> list = java.util.Arrays.asList(1, 2, 3);
		final Option<Integer> actual = Collections.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(true);
		assertThat(actual.get()).isEqualTo(3);
	}
	
}
