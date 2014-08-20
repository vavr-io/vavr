/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class TreeTest {

	// -- core (single node properties)

	@Test
	public void shouldBeRootWhenCreatedWithValue() {
		assertThat(new Tree<>(1).isRoot()).isTrue();
	}

	@Test
	public void shouldEqualRootWhenCreatedWithValue() {
		final Tree<Integer> tree = new Tree<>(1);
		assertThat(tree.getRoot()).isEqualTo(tree);
	}

	@Test
	public void shouldHaveNoParentWhenCreatedWithValue() {
		final Tree<Integer> tree = new Tree<>(1);
		assertThat(tree.getParent().isPresent()).isFalse();
	}

	@Test
	public void shouldBeLeafWhenCreatedWithValue() {
		assertThat(new Tree<>(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenCreatedWithValue() {
		assertThat(new Tree<>(1).getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenCreatedWithValue() {
		assertThat(new Tree<>(1).getValue()).isEqualTo(1);
	}

	@Test
	public void shouldContainCorrectValueWhenSetValue() {
		assertThat(new Tree<>(1).setValue(2).getValue()).isEqualTo(2);
	}

}
