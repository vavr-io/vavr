/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.Tuple;
import org.junit.Test;

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

    @Test
    public void shouldInstantiateRoseTreeBranchWithOf() {
        final RoseTree<Integer> actual = RoseTree.of(1, RoseTree.leaf(2), RoseTree.leaf(3));
        final RoseTree<Integer> expected = new RoseTree.Branch<>(1, List.of(new RoseTree.Leaf<>(2), new RoseTree.Leaf<>(3)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldInstantiateRoseTreeLeafWithOf() {
        final RoseTree<Integer> actual = RoseTree.of(1);
        final RoseTree<Integer> expected = new RoseTree.Leaf<>(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateABranchWithoutChildren() {
        new RoseTree.Branch<>(1, List.nil());
    }

    @Test
    public void shouldUnapplyLeaf() {
        assertThat(RoseTree.leaf(1).unapply()).isEqualTo(Tuple.of(1));
    }

    @Test
    public void shouldUnapplyBranch() {
        final Tuple actual = RoseTree.branch(1, RoseTree.leaf(2), RoseTree.leaf(3)).unapply();
        final Tuple expected = Tuple.of(1, List.of(RoseTree.leaf(2), RoseTree.leaf(3)));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnapplyNil() {
        assertThat(RoseTree.nil().unapply()).isEqualTo(Tuple.empty());
    }
}
