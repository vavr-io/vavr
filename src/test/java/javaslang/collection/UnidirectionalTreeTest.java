/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

public class UnidirectionalTreeTest {

	// -- core (single node properties)

	@Test
	public void shouldBeLeafWhenSingleNode() {
		assertThat(Tree.of(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenSingleNode() {
		assertThat(Tree.of(1).getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenSingleNode() {
		assertThat(Tree.of(1).getValue()).isEqualTo(1);
	}

}
