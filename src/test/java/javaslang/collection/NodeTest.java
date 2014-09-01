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

	// -- core

	@Test
	public void shouldBeLeafWhenCreatedWithValue() {
		assertThat(node(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenCreatedWithValue() {
		assertThat(node(1).getChildren()).isEqualTo(List.empty());
	}

	@Test
	public void shouldContainCorrectValueWhenCreatedWithValue() {
		assertThat(node(1).getValue()).isEqualTo(1);
	}

	@Test
	public void shouldContainCorrectValueWhenSetValue() {
		assertThat(node(1).setValue(2).getValue()).isEqualTo(2);
	}

	@Test
	public void shouldCountNoChildren() {
		assertThat(node(1).getChildCount()).isEqualTo(0);
	}

	@Test
	public void shouldCountSomeChildren() {
		final Node<?> node = node(1).setChildren(List.of(node(2), node(3)));
		assertThat(node.getChildCount()).isEqualTo(2);
	}

	@Test
	public void shouldSetChildren() {
		final Node<?> node = node(1).setChildren(List.of(node(2), node(3)));
		assertThat(node.toString()).isEqualTo("Node(1 2 3)");
	}

	// -- tree operations

	// attach

	@Test
	public void shouldAttachChild() {
		final Node<Integer> node = node(1, node(2), node(3));
		assertThat(node.attach(node(4)).toString()).isEqualTo("Node(1 2 3 4)");
	}

	@Test
	public void shouldAttachChildren() {
		final Node<Integer> node = node(1, node(2), node(3));
		assertThat(node.attach(List.of(node(4), node(5))).toString()).isEqualTo("Node(1 2 3 4 5)");
	}

	// detach

	@Test
	public void shouldDetachFirstChild() {
		final Node<Integer> node = node(1, node(2), node(3));
		assertThat(node.detach(node(2)).toString()).isEqualTo("Node(1 3)");
	}

	@Test
	public void shouldDetachLastChild() {
		final Node<Integer> node = node(1, node(2), node(3));
		assertThat(node.detach(node(3)).toString()).isEqualTo("Node(1 2)");
	}

	@Test
	public void shouldDetachChildren() {
		final Node<Integer> node = node(1, node(2), node(3), node(4));
		assertThat(node.detach(List.of(node(2), node(3))).toString()).isEqualTo("Node(1 4)");
	}

	// subtree

	@Test
	public void shouldGetChildAsSubtree() {
		final Node<Integer> node = node(1, node(2, node(3), node(4)), node(5));
		assertThat(node.getChild(0).subtree().toString()).isEqualTo("Node(2 3 4)");
	}

	@Test
	public void shouldNotModifyCurrentParentWhenSubtreeOnChild() {
		final Node<Integer> node = node(1, node(2, node(3), node(4)), node(5));
		node.getChild(0).subtree();
		assertThat(node.toString()).isEqualTo("Node(1 (2 3 4) 5)");
	}

	// -- conversion

	@Test
	public void shouldBuildATree() {
		final String actual = node("A", node("B"), node("C", node("D"), node("E", node("F", node("G")))))
				.asTree()
				.toString();
		final String expected = "Tree(\"A\" \"B\" (\"C\" \"D\" (\"E\" (\"F\" \"G\"))))";
		assertThat(actual).isEqualTo(expected);
	}
}
