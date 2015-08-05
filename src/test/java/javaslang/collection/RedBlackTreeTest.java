/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.RedBlackTree.TreeNode;
import javaslang.test.*;
import org.junit.Test;

import java.util.Random;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;
import static org.assertj.core.api.Assertions.assertThat;

public class RedBlackTreeTest {

    // Generates constantly growing random RedBlackTrees
    static final Arbitrary<RedBlackTree<Integer>> TREES = size -> {
        final Random random = Checkable.RNG.get();
        final Gen<Integer> intGen = Arbitrary.integer().apply(size);
        return Gen.<RedBlackTree<Integer>> of(RedBlackTree.empty(), tree -> tree.add(intGen.apply(random)));
    };

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.isEmpty()).isTrue();
    }

    @Test
    public void shouldInsert_2_1_4_5_9_3_6_7() {

        RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.toString()).isEqualTo("()");

        tree = tree.add(2);
        assertThat(tree.toString()).isEqualTo("(B:2)");

        tree = tree.add(1);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1)");

        tree = tree.add(4);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1 R:4)");

        tree = tree.add(5);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) B:5)");

        tree = tree.add(9);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) (B:5 R:9))");

        tree = tree.add(3);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (B:5 R:9))");

        tree = tree.add(6);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 B:9))");

        tree = tree.add(7);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 (B:9 R:7)))");
    }

    @Test
    public void shouldObeyInvariant1() {
        final CheckResult checkResult = new Property("No red node has a red child")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::invariant1)
                .check();
        CheckResultAssertions.assertThat(checkResult).isSatisfied();
    }

    // Red/Black Tree Invariants

    private static Boolean invariant1(RedBlackTree<?> tree) {
        if (tree.isEmpty()) {
            return true;
        } else {
            final TreeNode<?> node = (TreeNode<?>) tree;
            if (node.color == RED && ((!node.left.isEmpty() && ((TreeNode<?>) node.left).color == RED) ||
                    (!node.right.isEmpty() && ((TreeNode<?>) node.right).color == RED))) {
                return false;
            } else {
                return invariant1(node.left) && invariant1(node.right);
            }
        }
    }

    @Test
    public void shouldObeyInvariant2() {
        final CheckResult checkResult = new Property("Every path from the root to an empty node contains the same number of black nodes")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::invariant2)
                .check();
        CheckResultAssertions.assertThat(checkResult).isSatisfied();
    }

    private static <T> Boolean invariant2(RedBlackTree<?> tree) {
        class Util {
            List<List<TreeNode<?>>> paths(RedBlackTree<?> tree) {
                if (tree.isEmpty()) {
                    return List.empty();
                } else {
                    final TreeNode<?> node = (TreeNode<?>) tree;
                    final boolean isLeaf = node.left.isEmpty() && node.right.isEmpty();
                    if (isLeaf) {
                        return List.of(List.of(node));
                    } else {
                        return paths(node.left).prependAll(paths(node.right)).map(path -> path.prepend(node));
                    }
                }
            }
        }
        return new Util().paths(tree)
                .map(path -> path.filter(node -> node.color == BLACK).length())
                .distinct()
                .length() <= 1;
    }
}
