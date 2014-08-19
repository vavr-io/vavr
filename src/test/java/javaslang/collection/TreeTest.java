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
	public void shouldBeRootWhenSingleNode() {
		assertThat(new Tree<>(1).isRoot()).isTrue();
	}

	@Test
	public void shouldEqualRootWhenSingleNode() {
		final Tree<Integer> tree = new Tree<>(1);
		assertThat(tree.getRoot()).isEqualTo(tree);
	}

	@Test
	public void shouldHaveNoParentWhenSingleNode() {
		final Tree<Integer> tree = new Tree<>(1);
		assertThat(tree.getParent().isPresent()).isFalse();
	}

	@Test
	public void shouldBeLeafWhenSingleNode() {
		assertThat(new Tree<>(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenSingleNode() {
		assertThat(new Tree<>(1).getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenSingleNode() {
		assertThat(new Tree<>(1).getValue()).isEqualTo(1);
	}

}
