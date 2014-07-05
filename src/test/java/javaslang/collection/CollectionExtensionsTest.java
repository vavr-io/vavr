/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

public class CollectionExtensionsTest {
	
	@Test
	public void shoudRecognizeNullList() {
		final List<?> list = null;
		final boolean actual = CollectionExtensions.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(true);
	}

	@Test
	public void shoudRecognizeEmptyList() {
		final List<?> list = java.util.Collections.emptyList();
		final boolean actual = CollectionExtensions.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(true);
	}
	
	@Test
	public void shoudRecognizeNoneEmptyList() {
		final List<?> list = java.util.Arrays.asList(1, 2, 3);
		final boolean actual = CollectionExtensions.isNullOrEmpty(list);
		assertThat(actual).isEqualTo(false);
	}

}
