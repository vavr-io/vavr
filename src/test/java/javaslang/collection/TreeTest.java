/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.collection.Node.node;
import static javaslang.collection.Tree.tree;
import static org.assertj.core.api.Assertions.assertThat;
import javaslang.Serializables;

import org.junit.Test;

public class TreeTest {

	// -- constructors

	@Test
	public void shouldCreateTreeByValue() {
		assertThat(new Tree<>(1).toString()).isEqualTo("Tree(1)");
	}

	@Test
	public void shouldCreateTreeByValueAndChildren() {
		final Tree<Integer> tree = new Tree<>(1, List.of(new Tree<>(2)));
		assertThat(tree.toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldCreateTreeByParentAndValue() {
		final Tree<Integer> tree = new Tree<>(new Tree<>(1), 2);
		assertThat(tree.getRoot().toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldCreateTreeByParentAndValueAndChildren() {
		final Tree<Integer> tree = new Tree<>(new Tree<>(1), 2, List.of(new Tree<>(3), new Tree<>(4)));
		assertThat(tree.getRoot().toString()).isEqualTo("Tree(1 (2 3 4))");
	}

	@Test
	public void shouldCreateTreeByFactoryMethod() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)), tree(4));
		assertThat(tree.toString()).isEqualTo("Tree(1 (2 3) 4)");
	}

	// -- core

	@Test
	public void shouldBeRootWhenCreatedWithValue() {
		assertThat(tree(1).isRoot()).isTrue();
	}

	@Test
	public void shouldEqualRootWhenCreatedWithValue() {
		final Tree<Integer> tree = tree(1);
		assertThat(tree.getRoot()).isEqualTo(tree);
	}

	@Test
	public void shouldGetRootOfInnerNode() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)));
		assertThat(tree.getChild(0).getChild(0).getRoot()).isEqualTo(tree);
	}

	@Test
	public void shouldHaveNoParentWhenCreatedWithValue() {
		final Tree<Integer> tree = tree(1);
		assertThat(tree.getParent().isPresent()).isFalse();
	}

	@Test
	public void shouldSetNewParentWhenParentIsNull() {
		final Tree<Integer> tree = tree(2).setParent(tree(1));
		assertThat(tree.getRoot().toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldSetNewParentWhenParentIsNotNull() {
		final Tree<Integer> tree = tree(2).setParent(tree(0)).setParent(tree(1));
		assertThat(tree.getRoot().toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldSetNewSameParentTwice() {
		final Tree<Integer> tree = tree(2).setParent(tree(1));
		final Tree<Integer> actual = tree.setParent(tree.getParent().get());
		assertThat(actual.getRoot().toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldBeLeafWhenCreatedWithValue() {
		assertThat(tree(1).isLeaf()).isTrue();
	}

	@Test
	public void shouldHaveNoChildrenWhenCreatedWithValue() {
		assertThat(tree(1).getChildren()).isEqualTo(List.nil());
	}

	@Test
	public void shouldContainCorrectValueWhenCreatedWithValue() {
		assertThat(tree(1).getValue()).isEqualTo(1);
	}

	@Test
	public void shouldContainCorrectValueWhenSetValue() {
		assertThat(tree(1).setValue(2).getValue()).isEqualTo(2);
	}

	@Test
	public void shouldCountNoChildren() {
		assertThat(tree(1).getChildCount()).isEqualTo(0);
	}

	@Test
	public void shouldCountSomeChildren() {
		assertThat(tree(1).setChildren(List.of(tree(2), tree(3))).getChildCount()).isEqualTo(2);
	}

	@Test
	public void shouldSetChildren() {
		assertThat(tree(1).setChildren(List.of(tree(2), tree(3))).toString()).isEqualTo("Tree(1 2 3)");
	}

	// -- tree operations

	// attach

	@Test
	public void shouldAttachChild() {
		final Tree<Integer> tree = tree(1, tree(2), tree(3));
		assertThat(tree.attach(tree(4)).toString()).isEqualTo("Tree(1 2 3 4)");
	}

	@Test
	public void shouldAttachSameChildTwiceBecauseOfDuplication() {
		final Tree<Integer> child = tree(2);
		final Tree<Integer> tree = tree(1).attach(child).attach(child);
		assertThat(tree.toString()).isEqualTo("Tree(1 2 2)");
		assertThat(tree.getChild(0).getParent().get().toString()).isEqualTo("Tree(1 2 2)");
		assertThat(tree.getChild(1).getParent().get().toString()).isEqualTo("Tree(1 2 2)");
	}

	@Test
	public void shouldAttachChildren() {
		final Tree<Integer> tree = tree(1, tree(2), tree(3));
		assertThat(tree.attach(List.of(tree(4), tree(5))).toString()).isEqualTo("Tree(1 2 3 4 5)");
	}

	@Test
	public void shouldAttachedTreeShouldHaveSameParentStructure() {
		final Tree<Integer> tree = tree(0, tree(1, tree(2, tree(3)), tree(4, tree(5))));
		final Tree<Integer> newTree = tree.getChild(0).getChild(0).attach(tree(6));
		assertThat(newTree.getRoot().toString()).isEqualTo("Tree(0 (1 (2 3 6) (4 5)))");
	}

	@Test
	public void shouldAttachedTreeShouldHaveSameChildStructure() {
		final Tree<Integer> tree = tree(0, tree(1, tree(2, tree(3)), tree(4, tree(5))));
		final Tree<Integer> newTree = tree.getChild(0).getChild(0).attach(tree(6));
		assertThat(newTree.getParent().get().getChild(1).toString()).isEqualTo("Tree(4 5)");
	}

	// detach

	@Test
	public void shouldDetachFirstChild() {
		final Tree<Integer> tree = tree(1, tree(2), tree(3));
		assertThat(tree.detach(tree(2)).toString()).isEqualTo("Tree(1 3)");
	}

	@Test
	public void shouldDetachLastChild() {
		final Tree<Integer> tree = tree(1, tree(2), tree(3));
		assertThat(tree.detach(tree(3)).toString()).isEqualTo("Tree(1 2)");
	}

	@Test
	public void shouldDetachChildren() {
		final Tree<Integer> tree = tree(1, tree(2), tree(3), tree(4));
		assertThat(tree.detach(List.of(tree(2), tree(3))).toString()).isEqualTo("Tree(1 4)");
	}

	@Test
	public void shouldDetachedTreeShouldHaveSameParentStructure() {
		final Tree<Integer> tree = tree(0, tree(1, tree(2, tree(3)), tree(4, tree(5))));
		final Tree<Integer> newTree = tree.getChild(0).getChild(0).detach(tree(3));
		assertThat(newTree.getRoot().toString()).isEqualTo("Tree(0 (1 2 (4 5)))");
	}

	@Test
	public void shouldDetachedTreeShouldHaveSameChildStructure() {
		final Tree<Integer> tree = tree(0, tree(1, tree(2, tree(3)), tree(4, tree(5))));
		final Tree<Integer> newTree = tree.getChild(0).getChild(0).detach(tree(3));
		assertThat(newTree.getParent().get().getChild(1).toString()).isEqualTo("Tree(4 5)");
	}

	// subtree

	@Test
	public void shouldGetChildAsSubtree() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3), tree(4)), tree(5));
		assertThat(tree.getChild(0).subtree().toString()).isEqualTo("Tree(2 3 4)");
	}

	@Test
	public void shouldNotModifyCurrentParentWhenSubtreeOnChild() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3), tree(4)), tree(5));
		tree.getChild(0).subtree();
		assertThat(tree.toString()).isEqualTo("Tree(1 (2 3 4) 5)");
	}

	@Test
	public void shouldBeNoParentAtSubtree() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3), tree(4)), tree(5));
		assertThat(tree.getChild(0).subtree().isRoot()).isTrue();
	}

	// -- conversion

	@Test
	public void shouldBuildANode() {
		final String actual = tree("A", tree("B"), tree("C", tree("D"), tree("E", tree("F", tree("G")))))
				.asNode()
				.toString();
		final String expected = "Node(\"A\" \"B\" (\"C\" \"D\" (\"E\" (\"F\" \"G\"))))";
		assertThat(actual).isEqualTo(expected);
	}

	// -- Object.*

	// equals

	@Test
	public void shouldEqualSameTreeInstance() {
		final Tree<?> tree = tree(1);
		assertThat(tree.equals(tree)).isTrue();
	}

	@Test
	public void shouldTreeNotEqualsNull() {
		assertThat(tree(1).equals(null)).isFalse();
	}

	@Test
	public void shouldTreeNotEqualsDifferentType() {
		assertThat(tree(1).equals(node(1))).isFalse();
	}

	@Test
	public void shouldRecognizeEqualityOfTrees() {
		assertThat(tree(1).equals(tree(1))).isTrue();
	}

	@Test
	public void shouldRecognizeNonEqualityOfDifferentTreesOfSameSize() {
		assertThat(tree(1).equals(tree(2))).isFalse();
	}

	@Test
	public void shouldRecognizeNonEqualityOfDifferentTreesOfDifferentSize() {
		assertThat(tree(1).equals(tree(1, tree(2)))).isFalse();
	}

	// hashCode

	@Test
	public void shouldCalculateHashCodeOfSingleTreeNode() {
		assertThat(tree(1).hashCode() == tree(1).hashCode()).isTrue();
	}

	@Test
	public void shouldCalculateHashCodeOfTreeWithChildren() {
		assertThat(tree(1, tree(2)).hashCode() == tree(1, tree(2)).hashCode()).isTrue();
	}

	@Test
	public void shouldCalculateDifferentHashCodesForDifferentTrees() {
		assertThat(tree(1, tree(2)).hashCode() != tree(2, tree(3)).hashCode()).isTrue();
	}

	// toString

	@Test
	public void shouldConvertTreeToString() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)), tree(4));
		assertThat(tree.toString()).isEqualTo("Tree(1 (2 3) 4)");
	}

	@Test
	public void shouldConvertTreeToSinglelineLispString() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)), tree(4));
		assertThat(tree.toLispString()).isEqualTo("Tree(1 (2 3) 4)");
	}

	@Test
	public void shouldConvertTreeToMultilineLispString() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)), tree(4));
		assertThat(tree.toCoffeeScriptString()).isEqualTo("Tree:\n1\n  2\n    3\n  4");
	}

	// -- Serializable

	@Test
	public void shouldSerializeDeserializeTree() {
		final Tree<Integer> tree = tree(1, tree(2, tree(3)), tree(4));
		final Tree<Integer> actual = Serializables.deserialize(Serializables.serialize(tree));
		assertThat(actual).isEqualTo(tree);
	}
}
