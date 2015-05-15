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

public class RoseTreeTest extends AbstractTreeTest {

    final RoseTree<Integer> tree = RoseTree.branch(1, RoseTree.branch(2, RoseTree.branch(4, RoseTree.leaf(7)), RoseTree.leaf(5)), RoseTree.branch(3, RoseTree.branch(6, RoseTree.leaf(8), RoseTree.leaf(9))));

    @Override
    protected RoseTree<Integer> nil() {
        return RoseTree.nil();
    }

    @Override
    protected RoseTree<Integer> leaf() {
        return RoseTree.leaf(0);
    }

    @Override
    protected RoseTree<Integer> tree() {
        return tree;
    }

    // -- RoseTree test

    @Test
    public void shouldInstantiateRoseTreeBranchWithOf() {
        final RoseTree<Integer> actual = RoseTree.of(1, RoseTree.leaf(2), RoseTree.leaf(3));
        final RoseTree<Integer> expected = new RoseTree.Branch<>(1, List.of(new RoseTree.Leaf<>(2), new RoseTree.Leaf<>(3)));
        assertThat(actual).isEqualTo(expected);
    }

    // -- Leaf test

    @Test
    public void shouldInstantiateRoseTreeLeafWithOf() {
        final RoseTree<Integer> actual = RoseTree.of(1);
        final RoseTree<Integer> expected = new RoseTree.Leaf<>(1);
        assertThat(actual).isEqualTo(expected);
    }

    // -- Branch test

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateABranchWithoutChildren() {
        new RoseTree.Branch<>(1, List.nil());
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotCallReadObjectOnBranchInstance() throws Throwable {
        Serializables.callReadObject(tree());
    }

    // -- AbstractBinaryTree test

    // equals

    @Test
    public void shouldBeAwareThatTwoTreesOfSameInstanceAreEqual() {
        assertThat(nil().equals(nil())).isTrue();
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
        assertThat(nil().hashCode()).isEqualTo(1);
    }

    @Test
    public void shouldBeAwareThatHashCodeOfLeafIsGreaterThanOne() {
        assertThat(leaf().hashCode()).isGreaterThan(1);
    }

    // toString

    @Test
    public void shouldReturnStringRepresentationOfNil() {
        assertThat(nil().toString()).isEqualTo("RoseTree()");
    }

    @Test
    public void shouldReturnStringRepresentationOfBranch() {
        assertThat(tree().toString()).isEqualTo("RoseTree(1 (2 (4 7) 5) (3 (6 8 9)))");
    }
}
