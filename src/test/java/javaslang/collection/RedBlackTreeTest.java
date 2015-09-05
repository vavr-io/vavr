/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.collection.RedBlackTree.Node;
import javaslang.test.Arbitrary;
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

    // Generates random RedBlackTrees, adding values with freq 3, deleting with freq 1
    static final Arbitrary<RedBlackTree<Integer>> TREES = size -> random -> {

        final Gen<Integer> ints = Arbitrary.integer().apply(size);

        final Gen<RedBlackTree<Integer>> gen = Gen.<RedBlackTree<Integer>> of(RedBlackTree.empty(), t ->
                        Gen.<RedBlackTree<Integer>> frequency(
                                Tuple.of(1, rnd -> t.delete(ints.apply(rnd))),
                                Tuple.of(3, rnd -> t.add(ints.apply(rnd)))
                        ).apply(random)
        );

        int count = Gen.choose(1, 3).apply(random);

        RedBlackTree<Integer> tree;

        do {
            tree = gen.apply(random);
        } while(--count > 0);

        return tree;
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

    @Test
    public void shouldDelete_2_from_2_1_4_5_9_3_6_7() {
        final RedBlackTree<Integer> testee = RedBlackTree.<Integer> empty().add(2).add(1).add(4).add(5).add(9).add(3).add(6).add(7);
        final RedBlackTree<Integer> actual = testee.delete(2);
        assertThat(actual.toString()).isEqualTo("(B:4 (B:3 R:1) (R:6 B:5 (B:9 R:7)))");
    }

    // difference()

    @Test
    public void shouldShouldSubtractEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = t1;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldSubtractNonEmptyFromEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = t1;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldSubtractNonEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(3);
        assertThat(actual).isEqualTo(expected);
    }

    // intersection()

    @Test
    public void shouldShouldIntersectOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty();;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldIntersectOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty();;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldIntersectOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(5);
        assertThat(actual).isEqualTo(expected);
    }

    // union()

    @Test
    public void shouldShouldUnionOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(3).add(5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldUnionOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(5).add(7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldUnionOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(3).add(5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(5).add(7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(3).add(5).add(7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComputeUnionAndEqualTreesOfDifferentShapeButSameELements() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty().add(-1).add(-1).add(0).add(1);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty().add(-2).add(-1).add(0).add(1);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty().add(-2).add(-1).add(0).add(1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComputeUnion() {
        Property.def("union")
                .forAll(TREES, TREES)
                .suchThat((t1, t2) -> {
                    final List<Integer> actual = List.ofAll(t1.union(t2));
                    final List<Integer> expected = List.ofAll(t1).appendAll(t2).distinct().sort();
                    if (!actual.equals(expected)) {
                        System.out.println("t1           = " + t1);
                        System.out.println("t2           = " + t2);
                        System.out.println("t1.union(t2) = " + t1.union(t2));
                        System.out.println("actual       = " + actual);
                        System.out.println("expected     = " + expected);
                    }
                    return actual.equals(expected);
                })
                .check()
                .assertIsSatisfied();
    }

    // iterator()

    @Test
    public void shouldIterateEmptyTree() {
        assertThat(RedBlackTree.empty().iterator().hasNext()).isFalse();
    }

    @Test
    public void shouldIterateNonEmptyTree() {
        final RedBlackTree<Integer> testee = RedBlackTree.<Integer> empty().add(7).add(1).add(6).add(2).add(5).add(3).add(4);
        final List<Integer> actual = testee.iterator().toList();
        assertThat(actual.toString()).isEqualTo("List(1, 2, 3, 4, 5, 6, 7)");
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
            final Node<?> node = (Node<?>) tree;
            if (node.color == RED && ((!node.left.isEmpty() && ((Node<?>) node.left).color == RED) ||
                    (!node.right.isEmpty() && ((Node<?>) node.right).color == RED))) {
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
            final Comparator<? super T> comparator = ((Node<T>) tree).empty.comparator;
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

        static List<List<Node<?>>> paths(RedBlackTree<?> tree) {
            if (tree.isEmpty()) {
                return List.empty();
            } else {
                final Node<?> node = (Node<?>) tree;
                final boolean isLeaf = node.left.isEmpty() && node.right.isEmpty();
                if (isLeaf) {
                    return List.of(List.of(node));
                } else {
                    return paths(node.left).prependAll(paths(node.right)).map(path -> path.prepend(node));
                }
            }
        }

        static <T> List<Node<T>> nodes(RedBlackTree<T> tree) {
            if (tree.isEmpty()) {
                return List.empty();
            } else {
                final Node<T> node = (Node<T>) tree;
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
