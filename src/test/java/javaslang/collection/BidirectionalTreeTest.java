/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class BidirectionalTreeTest {

	// -- core (single node properties)

	@Test
	public void shouldBeRootWhenSingleNode() {
		assertThat(Tree.of(1).bidirectional().isRoot()).isTrue();
	}

	@Test
	public void shouldEqualRootWhenSingleNode() {
		final BidirectionalTree<Integer> tree = Tree.of(1).bidirectional();
		assertThat(tree.getRoot()).isEqualTo(tree);
	}

	@Test
	public void shouldHaveNoParentWhenSingleNode() {
		final BidirectionalTree<Integer> tree = Tree.of(1).bidirectional();
		assertThat(tree.getParent().isPresent()).isFalse();
	}

	@Test
	public void shouldBeLeafWhenSingleNode() {
		assertThat(Tree.of(1).bidirectional().isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenSingleNode() {
		assertThat(Tree.of(1).bidirectional().getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenSingleNode() {
		assertThat(Tree.of(1).bidirectional().getValue()).isEqualTo(1);
	}

}
