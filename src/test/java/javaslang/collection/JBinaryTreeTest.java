/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Serializables;
import javaslang.Tuple;
import org.junit.Test;

import java.io.InvalidObjectException;

import static org.assertj.core.api.Assertions.assertThat;

public class JBinaryTreeTest extends AbstractJTreeTest {

    final JBinaryTree<Integer> tree = JBinaryTree.branch(JBinaryTree.branch(JBinaryTree.branch(JBinaryTree.leaf(7), 4, JBinaryTree.nil()), 2, JBinaryTree.leaf(5)), 1, JBinaryTree.branch(JBinaryTree.branch(JBinaryTree.leaf(8), 6, JBinaryTree.leaf(9)), 3, JBinaryTree.nil()));

    @Override
    protected JBinaryTree<Integer> nil() {
        return JBinaryTree.nil();
    }

    @Override
    protected JBinaryTree<Integer> leaf() {
        return JBinaryTree.leaf(1);
    }

    @Override
    protected JBinaryTree<Integer> tree() {
        return tree;
    }

    // -- balance

    @Test
    public void shouldBalanceIterable() {
        assertThat(JBinaryTree.balance(JList.of(1, 2, 3, 4, 5, 6)).toLispString()).isEqualTo("(1 (2 3 4) (5 6))");
    }

    @Test
    public void shouldBalanceVarargs() {
        assertThat(JBinaryTree.balance(1, 2, 3, 4, 5, 6).toLispString()).isEqualTo("(1 (2 3 4) (5 6))");
    }

    @Test
    public void shouldBalanceTree() {
        assertThat(tree.balance().toLispString()).isEqualTo("(1 (2 (4 7) 5) (3 (6 8) 9))");
    }

    // -- Leaf test

    @Test
    public void shouldUnapplyLeaf() {
        assertThat(JBinaryTree.leaf(1).unapply()).isEqualTo(Tuple.of(1));
    }

    // -- Branch test

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBranchWithEmptySubTrees() {
        new JBinaryTree.Branch<>(JBinaryTree.nil(), 1, JBinaryTree.nil());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBranchUsingBranchFactoryMethodAndBothSubtreesAreNil() {
        JBinaryTree.branch(nil(), 1, nil());
    }

    @Test
    public void shouldUnapplyBranch() {
        final Tuple actual = JBinaryTree.branch(JBinaryTree.leaf(2), 1, JBinaryTree.leaf(3)).unapply();
        final Tuple expected = Tuple.of(JBinaryTree.leaf(2), 1, JBinaryTree.leaf(3));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotCallReadObjectOnBranchInstance() throws Throwable {
        Serializables.callReadObject(tree());
    }

    // -- Nil test

    @Test
    public void shouldUnapplyNil() {
        assertThat(JBinaryTree.nil().unapply()).isEqualTo(Tuple.empty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenLeftOfNil() {
        JBinaryTree.nil().left();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenRightOfNil() {
        JBinaryTree.nil().right();
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
        assertThat(nil().toString()).isEqualTo("JBinaryTree()");
    }

    @Test
    public void shouldReturnStringRepresentationOfBranch() {
        assertThat(tree().toString()).isEqualTo("JBinaryTree(1 (2 (4 7) 5) (3 (6 8 9)))");
    }
}
