/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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
}
