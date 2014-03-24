package javaslang.collection;

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

}
