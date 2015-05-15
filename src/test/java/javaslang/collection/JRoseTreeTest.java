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

public class JRoseTreeTest extends AbstractJTreeTest {

    final JRoseTree<Integer> tree = JRoseTree.branch(1, JRoseTree.branch(2, JRoseTree.branch(4, JRoseTree.leaf(7)), JRoseTree.leaf(5)), JRoseTree.branch(3, JRoseTree.branch(6, JRoseTree.leaf(8), JRoseTree.leaf(9))));

    @Override
    protected JRoseTree<Integer> nil() {
        return JRoseTree.nil();
    }

    @Override
    protected JRoseTree<Integer> leaf() {
        return JRoseTree.leaf(0);
    }

    @Override
    protected JRoseTree<Integer> tree() {
        return tree;
    }

    // -- RoseTree test

    @Test
    public void shouldInstantiateRoseTreeBranchWithOf() {
        final JRoseTree<Integer> actual = JRoseTree.of(1, JRoseTree.leaf(2), JRoseTree.leaf(3));
        final JRoseTree<Integer> expected = new JRoseTree.Branch<>(1, JList.of(new JRoseTree.Leaf<>(2), new JRoseTree.Leaf<>(3)));
        assertThat(actual).isEqualTo(expected);
    }

    // -- Leaf test

    @Test
    public void shouldInstantiateRoseTreeLeafWithOf() {
        final JRoseTree<Integer> actual = JRoseTree.of(1);
        final JRoseTree<Integer> expected = new JRoseTree.Leaf<>(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnapplyLeaf() {
        assertThat(JRoseTree.leaf(1).unapply()).isEqualTo(Tuple.of(1));
    }

    // -- Branch test

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateABranchWithoutChildren() {
        new JRoseTree.Branch<>(1, JList.nil());
    }

    @Test
    public void shouldUnapplyBranch() {
        final Tuple actual = JRoseTree.branch(1, JRoseTree.leaf(2), JRoseTree.leaf(3)).unapply();
        final Tuple expected = Tuple.of(1, JList.of(JRoseTree.leaf(2), JRoseTree.leaf(3)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = InvalidObjectException.class)
    public void shouldNotCallReadObjectOnBranchInstance() throws Throwable {
        Serializables.callReadObject(tree());
    }

    // -- Nil test

    @Test
    public void shouldUnapplyNil() {
        assertThat(JRoseTree.nil().unapply()).isEqualTo(Tuple.empty());
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
