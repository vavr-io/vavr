/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import org.junit.Test;

import java.io.InvalidObjectException;
import java.util.NoSuchElementException;
import java.util.Objects;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests all methods defined in {@link javaslang.collection.Tree}.
 */
public class TreeTest {

    /**
     * <pre><code>
     *         1
     *        / \
     *       /   \
     *      /     \
     *     2       3
     *    / \     /
     *   4   5   6
     *  /       / \
     * 7       8   9
     * </code></pre>
     */
    final Tree<Integer> tree = Tree.of(1, Tree.of(2, Tree.of(4, Tree.of(7)), Tree.of(5)), Tree.of(3, Tree.of(6, Tree.of(8), Tree.of(9))));

    protected Tree<Integer> empty() {
        return Tree.empty();
    }

    /**
     * @return A Leaf(0)
     */
    protected Tree<Integer> leaf() {
        return Tree.of(0);
    }

    protected Tree<Integer> tree() {
        return tree;
    }


    // -- Tree test

    @Test
    public void shouldInstantiateTreeBranchWithOf() {
        final Tree<Integer> actual = Tree.of(1, Tree.of(2), Tree.of(3));
        final Tree<Integer> expected = new Tree.Node<>(1, List.of(new Tree.Node<>(2, List.empty()), new Tree.Node<>(3, List.empty())));
        assertThat(actual).isEqualTo(expected);
    }

    // -- Leaf test

    @Test
    public void shouldInstantiateTreeLeafWithOf() {
        final Tree<Integer> actual = Tree.of(1);
        final Tree<Integer> expected = new Tree.Node<>(1, List.empty());
        assertThat(actual).isEqualTo(expected);
    }

    // -- Node test

    @Test
    public void shouldCreateANodeWithoutChildren() {
        new Tree.Node<>(1, List.empty());
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotCallReadObjectOnNodeInstance() throws Throwable {
        Serializables.callReadObject(tree());
    }

    // -- getValue

    @Test(expected = UnsupportedOperationException.class)
    public void shouldNotGetValueOfNil() {
        empty().getValue();
    }

    @Test
    public void shouldNotGetValueOfNonNil() {
        assertThat(tree().getValue()).isEqualTo(1);
    }

    // -- isEmpty

    @Test
    public void shouldIdentifyNilAsEmpty() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldIdentifyNonNilAsNotEmpty() {
        assertThat(tree().isEmpty()).isFalse();
    }

    // -- isLeaf

    @Test
    public void shouldIdentifiyLeafAsLeaf() {
        assertThat(leaf().isLeaf()).isTrue();
    }

    @Test
    public void shouldIdentifyNonLeafAsNonLeaf() {
        assertThat(tree().isLeaf()).isFalse();
    }

    @Test
    public void shouldIdentifiyNilAsNonLeaf() {
        assertThat(empty().isLeaf()).isFalse();
    }

    // -- isBranch

    @Test
    public void shouldIdentifiyLeafAsNonBranch() {
        assertThat(leaf().isBranch()).isFalse();
    }

    @Test
    public void shouldIdentifyNonLeafAsBranch() {
        assertThat(tree().isBranch()).isTrue();
    }

    @Test
    public void shouldIdentifiyNilAsNonBranch() {
        assertThat(empty().isBranch()).isFalse();
    }

    // -- getChildren

    @Test
    public void shouldGetChildrenOfLeaf() {
        assertThat(leaf().getChildren()).isEqualTo(List.empty());
    }

    @Test
    public void shouldGetChildrenOfBranch() {
        final List<? extends Tree<Integer>> children = tree().getChildren();
        assertThat(children.length()).isEqualTo(2);
        assertThat(children.get(0).toString()).isEqualTo("(2 (4 7) 5)");
        assertThat(children.get(1).toString()).isEqualTo("(3 (6 8 9))");
    }

    @Test
    public void shouldIGetChildrenOfNil() {
        assertThat(empty().getChildren()).isEqualTo(List.empty());
    }

    // -- branchCount

    @Test
    public void shouldCountBranchesOfNil() {
        assertThat(Tree.branchCount(empty())).isEqualTo(0);
    }

    @Test
    public void shouldCountBranchesOfNonNil() {
        assertThat(Tree.branchCount(tree())).isEqualTo(5);
    }

    // -- leafCount

    @Test
    public void shouldCountLeavesOfNil() {
        assertThat(Tree.leafCount(empty())).isEqualTo(0);
    }

    @Test
    public void shouldCountLeavesOfNonNil() {
        assertThat(Tree.leafCount(tree())).isEqualTo(4);
    }

    // -- nodeCount

    @Test
    public void shouldCountNodesOfNil() {
        assertThat(Tree.nodeCount(empty())).isEqualTo(0);
    }

    @Test
    public void shouldCountNodesOfNonNil() {
        assertThat(Tree.nodeCount(tree())).isEqualTo(9);
    }

    // -- cotains

    @Test
    public void shouldNotFindNodeInNil() {
        assertThat(empty().contains(1)).isFalse();
    }

    @Test
    public void shouldFindExistingNodeInNonNil() {
        assertThat(tree().contains(5)).isTrue();
    }

    @Test
    public void shouldNotFindNonExistingNodeInNonNil() {
        assertThat(tree().contains(0)).isFalse();
    }

    // -- iterator

    @Test
    public void shouldNotHasNextWhenNilIterator() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowOnNextWhenNilIterator() {
        empty().iterator().next();
    }

    @Test
    public void shouldIterateFirstElementOfNonNil() {
        assertThat(tree().iterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldFullyIterateNonNil() {
        final int length = List.of(1, 2, 4, 7, 5, 3, 6, 8, 9).zip(() -> tree().iterator()).filter(t -> Objects.equals(t._1, t._2)).length();
        assertThat(length).isEqualTo(9);
    }

    // -- map

    @Test
    public void shouldMapEmpty() {
        assertThat(empty().map(i -> i)).isEqualTo(empty());
    }

    @Test
    public void shouldMapTree() {
        assertThat(tree().map(i -> (char) (i + 64)).toString()).isEqualTo("(A (B (D G) E) (C (F H I)))");
    }

    // -- traverse

    @Test
    public void shouldTraverseEmpty() {
        assertThat(empty().traverse()).isEqualTo(Stream.empty());
    }

    // -- traverse(Order)

    @Test
    public void shouldTraverseTreeUsingPreOrder() {
        assertThat(tree().traverse(Tree.Order.PRE_ORDER)).isEqualTo(Stream.of(1, 2, 4, 7, 5, 3, 6, 8, 9));
    }

    @Test
    public void shouldTraverseTreeUsingInOrder() {
        assertThat(tree().traverse(Tree.Order.IN_ORDER)).isEqualTo(Stream.of(7, 4, 2, 5, 1, 8, 6, 9, 3));
    }

    @Test
    public void shouldTraverseTreeUsingPostOrder() {
        assertThat(tree().traverse(Tree.Order.POST_ORDER)).isEqualTo(Stream.of(7, 4, 5, 2, 8, 9, 6, 3, 1));
    }

    @Test
    public void shouldTraverseTreeUsingLevelOrder() {
        assertThat(tree().traverse(Tree.Order.LEVEL_ORDER)).isEqualTo(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    // -- toLispString

    @Test
    public void shouldConvertEmptyToLispString() {
        assertThat(empty().toString()).isEqualTo("()");
    }

    @Test
    public void shouldConvertNonEmptyToLispString() {
        assertThat(tree().toString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8 9)))");
    }


    // equals

    @Test
    public void shouldBeAwareThatTwoTreesOfSameInstanceAreEqual() {
        assertThat(empty().equals(empty())).isTrue();
    }

    @Test
    public void shouldBeAwareOfTwoDifferentEqualTrees() {
        assertThat(leaf().equals(leaf())).isTrue();
    }

    @Test
    public void shouldBeAwareThatTreeNotEqualsObject() {
        assertThat(leaf()).isNotEqualTo(new Object());
    }

    // hashCode

    @Test
    public void shouldBeAwareThatHashCodeOfEmptyIsOne() {
        assertThat(empty().hashCode()).isEqualTo(1);
    }

    @Test
    public void shouldBeAwareThatHashCodeOfLeafIsGreaterThanOne() {
        assertThat(leaf().hashCode()).isGreaterThan(1);
    }

    // toString

    @Test
    public void shouldReturnStringRepresentationOfEmpty() {
        assertThat(empty().toString()).isEqualTo("()");
    }

    @Test
    public void shouldReturnStringRepresentationOfNode() {
        assertThat(tree().toString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8 9)))");
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeEmpty() {
        final Object actual = deserialize(serialize(empty()));
        final Object expected = empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(empty())) == empty();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonEmpty() {
        final Object actual = deserialize(serialize(tree()));
        final Object expected = tree();
        assertThat(actual).isEqualTo(expected);
    }
}
