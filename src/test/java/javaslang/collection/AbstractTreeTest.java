/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests all methods defined in {@link javaslang.collection.Tree}.
 */
public abstract class AbstractTreeTest {

    abstract protected Tree<Integer> empty();

    /**
     * @return A Leaf(0)
     */
    abstract protected Tree<Integer> leaf();

    /**
     * @return 1
     * / \
     * /   \
     * /     \
     * 2       3
     * / \     /
     * 4   5   6
     * /       / \
     * 7       8   9
     */
    abstract protected Tree<Integer> tree();

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

    // -- flatten

    @Test
    public void shouldFlattenNil() {
        assertThat(empty().traverse()).isEqualTo(List.empty());
    }

    // -- flatten(Order)

    @Test
    public void shouldFlattenTreeUsingPreOrder() {
        assertThat(tree().traverse(Tree.Order.PRE_ORDER)).isEqualTo(List.of(1, 2, 4, 7, 5, 3, 6, 8, 9));
    }

    @Test
    public void shouldFlattenTreeUsingInOrder() {
        assertThat(tree().traverse(Tree.Order.IN_ORDER)).isEqualTo(List.of(7, 4, 2, 5, 1, 8, 6, 9, 3));
    }

    @Test
    public void shouldFlattenTreeUsingPostOrder() {
        assertThat(tree().traverse(Tree.Order.POST_ORDER)).isEqualTo(List.of(7, 4, 5, 2, 8, 9, 6, 3, 1));
    }

    @Test
    public void shouldFlattenTreeUsingLevelOrder() {
        assertThat(tree().traverse(Tree.Order.LEVEL_ORDER)).isEqualTo(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }

    // -- map

    @Test
    public void shouldMapNil() {
        assertThat(empty().map(i -> i)).isEqualTo(empty());
    }

    @Test
    public void shouldMapTree() {
        assertThat(tree().map(i -> (char) (i + 64)).toString()).isEqualTo("(A (B (D G) E) (C (F H I)))");
    }

    // -- toListString

    @Test
    public void shouldConvertNilToLispString() {
        assertThat(empty().toString()).isEqualTo("()");
    }

    @Test
    public void shouldConvertNonNilToLispString() {
        assertThat(tree().toString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8 9)))");
    }

    // -- Serializable interface

    @Test
    public void shouldSerializeDeserializeNil() {
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
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(tree()));
        final Object expected = tree();
        assertThat(actual).isEqualTo(expected);
    }
}
