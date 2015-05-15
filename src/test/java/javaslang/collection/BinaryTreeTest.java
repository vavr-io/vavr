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

public class BinaryTreeTest extends AbstractTreeTest {

    final BinaryTree<Integer> tree = BinaryTree.branch(BinaryTree.branch(BinaryTree.branch(BinaryTree.leaf(7), 4, BinaryTree.nil()), 2, BinaryTree.leaf(5)), 1, BinaryTree.branch(BinaryTree.branch(BinaryTree.leaf(8), 6, BinaryTree.leaf(9)), 3, BinaryTree.nil()));

    @Override
    protected BinaryTree<Integer> nil() {
        return BinaryTree.nil();
    }

    @Override
    protected BinaryTree<Integer> leaf() {
        return BinaryTree.leaf(1);
    }

    @Override
    protected BinaryTree<Integer> tree() {
        return tree;
    }

    // -- balance

    @Test
    public void shouldBalanceIterable() {
        assertThat(BinaryTree.balance(List.of(1, 2, 3, 4, 5, 6)).toLispString()).isEqualTo("(1 (2 3 4) (5 6))");
    }

    @Test
    public void shouldBalanceVarargs() {
        assertThat(BinaryTree.balance(1, 2, 3, 4, 5, 6).toLispString()).isEqualTo("(1 (2 3 4) (5 6))");
    }

    @Test
    public void shouldBalanceTree() {
        assertThat(tree.balance().toLispString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8) 9))");
    }

    // -- Branch test

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBranchWithEmptySubTrees() {
        new BinaryTree.Branch<>(BinaryTree.nil(), 1, BinaryTree.nil());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBranchUsingBranchFactoryMethodAndBothSubtreesAreNil() {
        BinaryTree.branch(nil(), 1, nil());
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotCallReadObjectOnBranchInstance() throws Throwable {
        Serializables.callReadObject(tree());
    }

    // -- Nil test

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLeftOfNil() {
        BinaryTree.nil().left();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenRightOfNil() {
        BinaryTree.nil().right();
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
        assertThat(nil().toString()).isEqualTo("BinaryTree()");
    }

    @Test
    public void shouldReturnStringRepresentationOfBranch() {
        assertThat(tree().toString()).isEqualTo("BinaryTree(1 (2 (4 7) 5) (3 (6 8 9)))");
    }
}
