/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.RedBlackTree.TreeNode;
import javaslang.test.Arbitrary;
import javaslang.test.Checkable;
import javaslang.test.Gen;
import javaslang.test.Property;
import org.junit.Test;

import java.util.Comparator;
import java.util.Random;
import java.util.function.IntUnaryOperator;

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

    // Rudimentary tests

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

    // Red/Black Tree invariants

    @Test
    public void shouldObeyInvariant1() {
        Property.def("No red node has a red child")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::invariant1)
                .check()
                .assertIsSatisfied();
    }

    private static boolean invariant1(RedBlackTree<?> tree) {
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
        Property.def("Every path from the root to an empty node contains the same number of black nodes")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::invariant2)
                .check()
                .assertIsSatisfied();
    }

    private static boolean invariant2(RedBlackTree<?> tree) {
        return TreeUtil.paths(tree)
                .map(path -> path.filter(node -> node.color == BLACK).length())
                .distinct()
                .length() <= 1;
    }

    // Essential Red/Black Tree properties

    @Test
    public void shouldNotContainDuplicates() {
        Property.def("tree contains no duplicate values")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::containsNoDuplicates)
                .check()
                .assertIsSatisfied();
    }

    private static <T> boolean containsNoDuplicates(RedBlackTree<T> tree) {
        if (tree.isEmpty()) {
            return true;
        } else {
            final List<T> values = TreeUtil.values(tree);
            final Comparator<? super T> comparator = ((TreeNode<T>) tree).comparator;
            return values.length() == values.distinctBy(comparator).length();
        }
    }

    @Test
    public void shouldNotExceedMaximumDepth() {
        Property.def("n := size(tree) => depth(node) <= 2 * floor(log2(n + 1)), for all nodes of tree")
                .forAll(TREES)
                .suchThat(RedBlackTreeTest::doesNotExceedMaximumDepth)
                .check()
                .assertIsSatisfied();
    }

    private static boolean doesNotExceedMaximumDepth(RedBlackTree<?> tree) {
        final int n = TreeUtil.size(tree);
        final int depth = TreeUtil.paths(tree).map(Traversable::length).max().orElse(0);
        final IntUnaryOperator log2 = i -> (int) (Math.log(i) / Math.log(2));
        return depth <= 2 * log2.applyAsInt(n + 1);
    }

    // some helpful tree functions
    static class TreeUtil {

        static List<List<TreeNode<?>>> paths(RedBlackTree<?> tree) {
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

        static <T> List<TreeNode<T>> nodes(RedBlackTree<T> tree) {
            if (tree.isEmpty()) {
                return List.empty();
            } else {
                final TreeNode<T> node = (TreeNode<T>) tree;
                return nodes(node.left).prependAll(nodes(node.right)).prepend(node);
            }
        }

        static <T> List<T> values(RedBlackTree<T> tree) {
            return nodes(tree).map(node -> node.value);
        }

        static int size(RedBlackTree<?> tree) {
            return nodes(tree).length();
        }
    }
}
