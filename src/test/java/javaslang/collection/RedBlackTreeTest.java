/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.test.Arbitrary;
import javaslang.test.Gen;
import javaslang.test.Property;
import org.junit.Test;

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
                                Tuple.of(3, rnd -> t.insert(ints.apply(rnd)))
                        ).apply(random)
        );

        int count = Gen.choose(1, size).apply(random);

        RedBlackTree<Integer> tree;

        do {
            tree = gen.apply(random);
        } while (--count > 0);

        return tree;
    };

    // Rudimentary tests

    // empty tree

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.isEmpty()).isTrue();
    }

    // isEmpty

    @Test
    public void shouldRecognizeEmptyTree() {
        assertThat(RedBlackTree.empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyTree() {
        assertThat(RedBlackTree.of(1).isEmpty()).isFalse();
    }

    // contains

    @Test
    public void shouldRecognizeContainedElement() {
        assertThat(RedBlackTree.of(1, 2, 3).contains(2)).isTrue();
    }

    @Test
    public void shouldRecognizeNonContainedElementOfEmptyTree() {
        assertThat(RedBlackTree.<Integer> empty().contains(1)).isFalse();
    }

    @Test
    public void shouldRecognizeNonContainedElementOfNonEmptyTree() {
        assertThat(RedBlackTree.of(1, 2, 3).contains(0)).isFalse();
    }

    // insert

    @Test
    public void shouldInsert_2_1_4_5_9_3_6_7() {

        RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.toString()).isEqualTo("()");

        tree = tree.insert(2);
        assertThat(tree.toString()).isEqualTo("(B:2)");

        tree = tree.insert(1);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1)");

        tree = tree.insert(4);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1 R:4)");

        tree = tree.insert(5);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) B:5)");

        tree = tree.insert(9);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) (B:5 R:9))");

        tree = tree.insert(3);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (B:5 R:9))");

        tree = tree.insert(6);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 B:9))");

        tree = tree.insert(7);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 (B:9 R:7)))");
    }

    @Test
    public void shouldInsertNullIntoEmptyTreeBecauseComparatorNotCalled() {
        final RedBlackTree<Integer> actual = RedBlackTree.<Integer> empty().insert(null);
        final RedBlackTree<Integer> expected = RedBlackTree.of((Integer) null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotInsertNullTwoTimesIntoEmptyTreeBecauseComparatorCalled() {
        RedBlackTree.<Integer> empty().insert(null).insert(null);
    }

    @Test
    public void shouldInsertNonNullIntoEmptyTree() {
        final RedBlackTree<Integer> actual = RedBlackTree.<Integer> empty().insert(2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnTheSameInstanceWhenInsertingAnAlreadyContainedELement() {
        final RedBlackTree<Integer> testee = RedBlackTree.of(1, 2, 3);
        final RedBlackTree<Integer> actual = testee.insert(2);
        assertThat(actual).isEqualTo(testee);
    }

    // delete

    @Test
    public void shouldDelete_2_from_2_1_4_5_9_3_6_7() {
        final RedBlackTree<Integer> testee = RedBlackTree.of(2, 1, 4, 5, 9, 3, 6, 7);
        final RedBlackTree<Integer> actual = testee.delete(2);
        assertThat(actual.toString()).isEqualTo("(B:4 (B:3 R:1) (R:6 B:5 (B:9 R:7)))");
    }

    // difference()

    @Test
    public void shouldShouldSubtractEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldShouldSubtractNonEmptyFromEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldShouldSubtractNonEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3);
        assertThat(actual).isEqualTo(expected);
    }

    // intersection()

    @Test
    public void shouldShouldIntersectOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldIntersectOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.<Integer> empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldIntersectOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(5);
        assertThat(actual).isEqualTo(expected);
    }

    // union()

    @Test
    public void shouldShouldUnionOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3, 5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldUnionOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.<Integer> empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(5, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldShouldUnionOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3, 5, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComputeUnionAndEqualTreesOfDifferentShapeButSameELements() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(-1, -1, 0, 1);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(-2, -1, 0, 1);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(-2, -1, 0, 1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComputeUnion() {
        Property.def("union")
                .forAll(TREES, TREES)
                .suchThat((t1, t2) -> {
                    final List<Integer> actual = List.ofAll(t1.union(t2));
                    final List<Integer> expected = List.ofAll(t1).appendAll(t2).distinct().sort();
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
        final RedBlackTree<Integer> testee = RedBlackTree.of(7, 1, 6, 2, 5, 3, 4);
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
            final boolean isRed = tree.color() == RED;
            final RedBlackTree<?> left = tree.left();
            final RedBlackTree<?> right = tree.right();
            final boolean isLeftRed = !left.isEmpty() && left.color() == RED;
            final boolean isRightRed = !right.isEmpty() && right.color() == RED;
            return !(isRed && (isLeftRed || isRightRed)) && invariant1(left) && invariant1(right);
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
                .map(path -> path.filter(node -> node.color() == BLACK).length())
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
            return values.length() == values.distinctBy(tree.comparator()).length();
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

        static List<List<RedBlackTree<?>>> paths(RedBlackTree<?> tree) {
            if (tree.isEmpty()) {
                return List.empty();
            } else {
                final boolean isLeaf = tree.left().isEmpty() && tree.right().isEmpty();
                if (isLeaf) {
                    return List.of(List.of(tree));
                } else {
                    return paths(tree.left()).prependAll(paths(tree.right())).map(path -> path.prepend(tree));
                }
            }
        }

        static <T> List<RedBlackTree<T>> nodes(RedBlackTree<T> tree) {
            if (tree.isEmpty()) {
                return List.empty();
            } else {
                return nodes(tree.left()).prependAll(nodes(tree.right())).prepend(tree);
            }
        }

        static <T> List<T> values(RedBlackTree<T> tree) {
            return nodes(tree).map(RedBlackTree::value);
        }

        static int size(RedBlackTree<?> tree) {
            return nodes(tree).length();
        }
    }
}
