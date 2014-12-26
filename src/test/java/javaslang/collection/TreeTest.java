/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import static javaslang.collection.BinaryTree.*;

public class TreeTest {

    /**
     *         1
     *        / \
     *       /   \
     *      /     \
     *     2       3
     *    / \     /
     *   4   5   6
     *  /       / \
     * 7       8   9
     *
     *
     */
    final Tree tree = Branch(Branch(Branch(Leaf(7), 4, Nil()), 2, Leaf(5)), 1, Branch(Branch(Leaf(8), 6, Leaf(9)), 3, Nil()));

    @Test
    public void shouldFlattenTreeUsingPreOrder() {
        Assertions.assertThat(tree.flatten(Traversal.PRE_ORDER)).isEqualTo(List.of(1, 2, 4, 7, 5, 3, 6, 8, 9));
    }

    @Test
    public void shouldFlattenTreeUsingInOrder() {
        Assertions.assertThat(tree.flatten(Traversal.IN_ORDER)).isEqualTo(List.of(7, 4, 2, 5, 1, 8, 6, 9, 3));
    }

    @Test
    public void shouldFlattenTreeUsingPostOrder() {
        Assertions.assertThat(tree.flatten(Traversal.POST_ORDER)).isEqualTo(List.of(7, 4, 5, 2, 8, 9, 6, 3, 1));
    }

    @Test
    public void shouldFlattenTreeUsingLevelOrder() {
        Assertions.assertThat(tree.flatten(Traversal.LEVEL_ORDER)).isEqualTo(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9));
    }
}
