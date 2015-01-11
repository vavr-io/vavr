/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.Tuple;
import org.junit.Test;

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

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBranchWithToEmptySubTrees() {
        new BinaryTree.Branch<>(BinaryTree.nil(), 1, BinaryTree.nil());
    }

    @Test
    public void shouldUnapplyLeaf() {
        assertThat(BinaryTree.leaf(1).unapply()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldUnapplyBranch() {
        final Tuple actual = BinaryTree.branch(BinaryTree.leaf(2), 1, BinaryTree.leaf(3)).unapply();
        final Tuple expected = Tuple.of(BinaryTree.leaf(2), 1, BinaryTree.leaf(3));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnapplyNil() {
        assertThat(BinaryTree.nil().unapply()).isEqualTo(Tuple.empty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLeftOfNil() {
        BinaryTree.nil().left();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenRightOfNil() {
        BinaryTree.nil().right();
    }

    @Test
    public void shouldNilEqualsNil() {
        assertThat(BinaryTree.nil().equals(BinaryTree.nil())).isTrue();
    }

    @Test
    public void shouldComputeHashCodeOfNil() {
        assertThat(BinaryTree.nil().hashCode()).isEqualTo(1);
    }

    @Test
    public void shouldConvertNilToString() {
        assertThat(BinaryTree.nil().toString()).isEqualTo("BinaryTree()");
    }
}
