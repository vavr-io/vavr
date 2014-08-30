/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.collection.Node.node;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class NodeTest {

	// -- core (single node properties)

	@Test
	public void shouldBeLeafWhenCreatedWithValue() {
		assertThat(new Node<>(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenCreatedWithValue() {
		assertThat(new Node<>(1).getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenCreatedWithValue() {
		assertThat(new Node<>(1).getValue()).isEqualTo(1);
	}

	@Test
	public void shouldContainCorrectValueWhenSetValue() {
		assertThat(new Node<>(1).setValue(2).getValue()).isEqualTo(2);
	}

	// -- building trees

	@Test
	public void shouldBuildATree() {
		final String actual = node("A", node("B"), node("C", node("D"), node("E", node("F", node("G")))))
				.asTree()
				.toString();
		final String expected = "(\"A\" \"B\" (\"C\" \"D\" (\"E\" (\"F\" \"G\"))))";
		assertThat(actual).isEqualTo(expected);
	}

}
