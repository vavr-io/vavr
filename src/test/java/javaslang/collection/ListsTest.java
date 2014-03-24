package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import javaslang.option.Option;

import org.junit.Test;

public class ListsTest {

	@Test
	public void shouldReturnNoneOnFirstElementOfNullList() {
		final List<?> list = null;
		final Option<?> actual = Lists.firstElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnNoneOnFirstElementOfEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final Option<?> actual = Lists.firstElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnFirstElementOfNoneEmptyList() {
		final List<Integer> list = java.util.Arrays.asList(1, 2, 3);
		final Option<Integer> actual = Lists.firstElement(list);
		assertThat(actual.get()).isEqualTo(1);
	}
	
	@Test
	public void shouldReturnNoneOnLastElementOfNullList() {
		final List<?> list = null;
		final Option<?> actual = Lists.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnNoneOnLastElementOfEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final Option<?> actual = Lists.lastElement(list);
		assertThat(actual.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void shouldReturnLastElementOfNoneEmptyList() {
		final List<Integer> list = java.util.Arrays.asList(1, 2, 3);
		final Option<Integer> actual = Lists.lastElement(list);
		assertThat(actual.get()).isEqualTo(3);
	}

}
