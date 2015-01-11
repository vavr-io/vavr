/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

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
}
