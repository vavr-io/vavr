/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RedBlackTreeTest {

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.isEmpty()).isTrue();
    }

    @Test
    public void shouldAddDistinctElements() {
        final RedBlackTree<Integer> tree1 = RedBlackTree.<Integer> empty().add(1).add(2).add(3).add(4).add(5);
        final RedBlackTree<Integer> tree2 = RedBlackTree.<Integer> empty().add(5).add(4).add(3).add(2).add(1).add(8).add(10).add(0);
        // TODO
        System.out.println(tree1); // = RedBlackTree(1 (2 (3 (4 5))))
        System.out.println(tree2); // = RedBlackTree(5 (4 (3 (2 (1 0)))) (8 10))
    }
}
