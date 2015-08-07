/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import org.junit.Test;

import java.io.InvalidObjectException;

import static org.assertj.core.api.Assertions.assertThat;

public class TreeTest extends AbstractTreeTest {

    final Tree<Integer> tree = Tree.of(1, Tree.of(2, Tree.of(4, Tree.of(7)), Tree.of(5)), Tree.of(3, Tree.of(6, Tree.of(8), Tree.of(9))));

    @Override
    protected Tree<Integer> empty() {
        return Tree.empty();
    }

    @Override
    protected Tree<Integer> leaf() {
        return Tree.of(0);
    }

    @Override
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

    // -- AbstractBinaryTree test

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
        assertThat(leaf().equals(new Object())).isFalse();
    }

    // hashCode

    @Test
    public void shouldBeAwareThatHashCodeOfNilIsOne() {
        assertThat(empty().hashCode()).isEqualTo(1);
    }

    @Test
    public void shouldBeAwareThatHashCodeOfLeafIsGreaterThanOne() {
        assertThat(leaf().hashCode()).isGreaterThan(1);
    }

    // toString

    @Test
    public void shouldReturnStringRepresentationOfNil() {
        assertThat(empty().toString()).isEqualTo("()");
    }

    @Test
    public void shouldReturnStringRepresentationOfNode() {
        assertThat(tree().toString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8 9)))");
    }
}
