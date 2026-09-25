/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.collection.RedBlackTree.Color;
import io.vavr.collection.RedBlackTreeModule.Node;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.BinaryOperator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RedBlackTreeTest {

    private static <T> RedBlackTree<T> empty() {
        return RedBlackTree.empty(Comparators.naturalComparator());
    }

    private static <T> RedBlackTree<T> of(T value) {
        return RedBlackTree.of(Comparators.naturalComparator(), value);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    private static <T> RedBlackTree<T> of(T... values) {
        return RedBlackTree.<T> of(Comparators.naturalComparator(), values);
    }

    // Rudimentary tests

    // empty tree

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = empty();
        assertThat(tree.isEmpty()).isTrue();
        assertThat(tree.size()).isEqualTo(0);
        assertThat(tree.color()).isEqualTo(RedBlackTree.Color.BLACK);
    }

    @Test
    public void shouldFailLeftOfEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> empty().left());
    }

    @Test
    public void shouldFailRightOfEmpty() {
        assertThrows(UnsupportedOperationException.class, () -> empty().right());
    }

    @Test
    public void shouldFailValueOfEmpty() {
        assertThrows(NoSuchElementException.class, () -> empty().value());
    }

    // isEmpty

    @Test
    public void shouldRecognizeEmptyTree() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyTree() {
        assertThat(of(1).isEmpty()).isFalse();
    }

    // contains

    @Test
    public void shouldRecognizeContainedElement() {
        assertThat(of(1, 2, 3).contains(2)).isTrue();
    }

    @Test
    public void shouldRecognizeNonContainedElementOfEmptyTree() {
        assertThat(RedBlackTreeTest.<Integer> empty().contains(1)).isFalse();
    }

    @Test
    public void shouldRecognizeNonContainedElementOfNonEmptyTree() {
        assertThat(of(1, 2, 3).contains(0)).isFalse();
    }

    // insert

    @Test
    public void shouldInsert_2_1_4_5_9_3_6_7() {

        RedBlackTree<Integer> tree = empty();
        assertThat(tree.toString()).isEqualTo("()");
        assertThat(tree.size()).isEqualTo(0);

        tree = tree.insert(2);
        assertThat(tree.toString()).isEqualTo("(B:2)");
        assertThat(tree.size()).isEqualTo(1);

        tree = tree.insert(1);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1)");
        assertThat(tree.size()).isEqualTo(2);

        tree = tree.insert(4);
        assertThat(tree.toString()).isEqualTo("(B:2 R:1 R:4)");
        assertThat(tree.size()).isEqualTo(3);

        tree = tree.insert(5);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) B:5)");
        assertThat(tree.size()).isEqualTo(4);

        tree = tree.insert(9);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1) (B:5 R:9))");
        assertThat(tree.size()).isEqualTo(5);

        tree = tree.insert(3);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (B:5 R:9))");
        assertThat(tree.size()).isEqualTo(6);

        tree = tree.insert(6);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 B:9))");
        assertThat(tree.size()).isEqualTo(7);

        tree = tree.insert(7);
        assertThat(tree.toString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 (B:9 R:7)))");
        assertThat(tree.size()).isEqualTo(8);
    }

    @Test
    public void shouldInsertNullIntoEmptyTreeBecauseComparatorNotCalled() {
        final RedBlackTree<Integer> actual = RedBlackTreeTest.<Integer> empty().insert(null);
        final RedBlackTree<Integer> expected = of((Integer) null);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldNotInsertNullTwoTimesIntoEmptyTreeBecauseComparatorCalled() {
        assertThrows(NullPointerException.class, () -> RedBlackTreeTest.<Integer> empty().insert(null).insert(null));
    }

    @Test
    public void shouldInsertNonNullIntoEmptyTree() {
        final RedBlackTree<Integer> actual = RedBlackTreeTest.<Integer> empty().insert(2);
        final RedBlackTree<Integer> expected = of(2);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldReturnTheSameInstanceWhenInsertingAnAlreadyContainedElement() {
        final RedBlackTree<Integer> testee = of(1, 2, 3);
        final RedBlackTree<Integer> actual = testee.insert(2);
        assertThat(Collections.areEqual(actual, testee)).isTrue();
    }

    // delete

    @Test
    public void shouldDelete_2_from_2_1_4_5_9_3_6_7() {
        final RedBlackTree<Integer> testee = of(2, 1, 4, 5, 9, 3, 6, 7);
        final RedBlackTree<Integer> actual = testee.delete(2);
        assertThat(actual.toString()).isEqualTo("(B:4 (B:3 R:1) (R:6 B:5 (B:9 R:7)))");
        assertThat(actual.size()).isEqualTo(7);
    }

    // difference()

    @Test
    public void shouldSubtractEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = empty();
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldSubtractNonEmptyFromEmpty() {
        final RedBlackTree<Integer> t1 = empty();
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldSubtractNonEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = of(3);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    // intersection()

    @Test
    public void shouldIntersectOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = empty();
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = empty();
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldIntersectOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = empty();
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = empty();
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = of(5);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyUnbalancedHeightLeft() {
        // Trees have
        // - different values
        // - similar to each other left children
        // - and unlike each other right children
        final RedBlackTree<Integer> t1 = of(1, 2, 3, 4, 5, 6, 7, 8, 60, 66, 67);
        final RedBlackTree<Integer> t2 = of(1, 2, 3, 10, 11, 12, 13, 14, 60, 76, 77);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = of(1, 2, 3, 60);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyUnbalancedHeightRight() {
        // Trees have
        // - different values
        // - unlike each other left children
        // - and similar to each other right children
        final RedBlackTree<Integer> t1 = of(1, 2, 3, 4, 40, 61, 62, 63, 64, 65);
        final RedBlackTree<Integer> t2 = of(2, 7, 8, 9, 50, 61, 62, 63, 64, 65);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = of(2, 61, 62, 63, 64, 65);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyBalancedHeightRight() {
        final RedBlackTree<Integer> t1 = of(-10, -20, -30, -40, -50, 1, 10, 20, 30);
        final RedBlackTree<Integer> t2 = of(-10, -20, -30, -40, -50, 2, 10, 20, 30);
        assertThat(Collections.areEqual(t1.intersection(t2), t1.delete(1))).isTrue();
    }

    /*
     * > let tree1 = fromList [8, 14, 0, 7, 9, 3]
     * > let tree2 = fromList [7, 9, 14, 6, 0, 5, 11, 10, 4, 12, 8, 13]
     * > tree1 `intersection` tree2
     * Node B 2 (Node B 1 (Node R 1 Leaf 0 Leaf) 7 Leaf) 8 (Node B 1 (Node R 1 Leaf 9 Leaf) 14 Leaf)
     * > printSet (tree1 `intersection` tree2)
     * B 8 (2)
     * + B 7 (1)
     *   + R 0 (1)
     *     +
     *     +
     *   +
     * + B 14 (1)
     *   + R 9 (1)
     *     +
     *     +
     *   +
     */
    @Test
    public void shouldPassIntersectionRegression1_Issue2098() {
        final RedBlackTree<Integer> tree1 = of(8, 14, 0, 7, 9, 3);
        final RedBlackTree<Integer> tree2 = of(7, 9, 14, 6, 0, 5, 11, 10, 4, 12, 8, 13);
        final RedBlackTree<Integer> actual = tree1.intersection(tree2);
        assertValid(actual);
        assertThat(actual).containsExactly(0, 7, 8, 9, 14);
    }

    /*
     * > let tree1 = fromList [8, 14, 0, 7, 9, 3]
     * > let tree2 = fromList [7, 9, 14, 6, 0, 5, 11, 10, 4, 12, 8, 13]
     * > let tree3 = fromList [1, 2]
     * > (tree1 `intersection` tree2) `intersection` tree3
     * Leaf
     * > tree1 `intersection` (tree2 `intersection` tree3)
     * Leaf
     */
    @Test
    public void shouldPassIntersectionRegression2_Issue2098() {
        final RedBlackTree<Integer> tree1 = of(8, 14, 0, 7, 9, 3);
        final RedBlackTree<Integer> tree2 = of(7, 9, 14, 6, 0, 5, 11, 10, 4, 12, 8, 13);
        final RedBlackTree<Integer> tree3 = of(1, 2);
        final RedBlackTree<Integer> actual = tree1.intersection(tree2).intersection(tree3);
        final RedBlackTree<Integer> expected = tree1.intersection(tree2.intersection(tree3));
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    /*
     * > let tree1 = [1462193440, 0, 2147483647, -2147483648, 0, 637669539, -1612766076, -1, 1795938819, 1, 0, -420800448, -2147483648, 497885405, 0, 1084073832, 1, 1439964148, 1961646330]
     * > let tree2 = [-1, 1, 2147483647, -1434983536, -2147483648, -1452486079, 1365799971, 231691980, -1780534767, -2147483648, 1448658704, 0, 1526591298]
     * > tree1 `intersection` tree2
     * Node B 2 (Node B 1 (Node R 1 Leaf (-2147483648) Leaf) (-1) Leaf) 0 (Node B 1 (Node R 1 Leaf 1 Leaf) 2147483647 Leaf)
     */
    @Test
    public void shouldPassIntersectionRegression3_Issue2098() {
        final RedBlackTree<Integer> tree1 = of(1462193440, 0, 2147483647, -2147483648, 0, 637669539, -1612766076, -1, 1795938819, 1, 0, -420800448, -2147483648, 497885405, 0, 1084073832, 1, 1439964148, 1961646330);
        final RedBlackTree<Integer> tree2 = of(-1, 1, 2147483647, -1434983536, -2147483648, -1452486079, 1365799971, 231691980, -1780534767, -2147483648, 1448658704, 0, 1526591298);
        final RedBlackTree<Integer> actual = tree1.intersection(tree2);
        assertValid(actual);
        assertThat(actual).containsExactly(-2147483648, -1, 0, 1, 2147483647);
    }

    // union()

    @Test
    public void shouldUnionOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = empty();
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = of(3, 5);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldUnionOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = empty();
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = of(5, 7);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldUnionOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = of(3, 5);
        final RedBlackTree<Integer> t2 = of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = of(3, 5, 7);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    @Test
    public void shouldComputeUnionAndEqualTreesOfDifferentShapeButSameElements() {
        final RedBlackTree<Integer> t1 = of(-1, -1, 0, 1);
        final RedBlackTree<Integer> t2 = of(-2, -1, 0, 1);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = of(-2, -1, 0, 1);
        assertThat(Collections.areEqual(actual, expected)).isTrue();
    }

    // iterator()

    @Test
    public void shouldIterateEmptyTree() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @Test
    public void shouldIterateNonEmptyTree() {
        final RedBlackTree<Integer> testee = of(7, 1, 6, 2, 5, 3, 4);
        final List<Integer> actual = testee.iterator().toList();
        assertThat(actual.toString()).isEqualTo("List(1, 2, 3, 4, 5, 6, 7)");
    }

    // validity of set operation results

    @Test
    public void shouldKeepTreeSetValidAfterDiff() {
        final Comparator<Integer> comparator = Comparator.naturalOrder();
        final TreeSet<Integer> set = TreeSet.ofAll(comparator, List.of(56, 31, 20, 10, 6, 42, 11, 28, 23));
        final TreeSet<Integer> other = TreeSet.ofAll(comparator, List.of(32, 19));
        final TreeSet<Integer> diff = set.diff(other);
        assertThat(diff).containsExactly(6, 10, 11, 20, 23, 28, 31, 42, 56);

        TreeSet<Integer> remaining = diff;
        for (Integer value : diff) {
            remaining = remaining.remove(value);
        }
        assertThat(remaining).isEmpty();

        final TreeSet<Integer> sameComparator = TreeSet.of(comparator, 0);
        assertThat(diff.union(sameComparator)).containsExactly(0, 6, 10, 11, 20, 23, 28, 31, 42, 56);
        assertThat(diff.diff(sameComparator)).containsExactly(6, 10, 11, 20, 23, 28, 31, 42, 56);
        assertThat(diff.intersect(sameComparator)).isEmpty();

        final RedBlackTree<Integer> tree = RedBlackTree.ofAll(comparator, List.of(56, 31, 20, 10, 6, 42, 11, 28, 23))
                .difference(RedBlackTree.ofAll(comparator, List.of(32, 19)));
        assertValid(tree);
    }

    @Test
    public void shouldKeepTreesValidInRandomizedSetOperations() {
        final java.util.List<String> failures = new ArrayList<>();
        int naturalFailures = 0;
        final Random random = new Random(20260925L);
        for (int i = 0; i < 20_000; i++) {
            final boolean natural = i % 2 == 0;
            final Comparator<Integer> comparator = natural ? Comparator.naturalOrder() : Comparator.reverseOrder();
            final RedBlackTree<Integer> t1 = randomTree(random, comparator);
            final RedBlackTree<Integer> t2 = randomTree(random, comparator);
            final RedBlackTree<Integer> t3 = randomTree(random, comparator);
            try {
                checkSetOperations(random, t1, t2, t3);
            } catch (Throwable t) {
                naturalFailures += natural ? 1 : 0;
                failures.add("case " + i + " (" + (natural ? "natural" : "reversed") + "): " + t);
            }
        }
        assertThat(failures).as("%d of 20000 cases fail (%d natural, %d reversed), first ones: %s", failures.size(),
                naturalFailures, failures.size() - naturalFailures,
                failures.subList(0, Math.min(3, failures.size()))).isEmpty();
    }

    private static void checkSetOperations(Random random, RedBlackTree<Integer> t1, RedBlackTree<Integer> t2,
            RedBlackTree<Integer> t3) {
        final RedBlackTree<Integer> difference = checkOperation(t1, t2, RedBlackTree::difference, java.util.Set::removeAll);
        final RedBlackTree<Integer> intersection = checkOperation(t1, t2, RedBlackTree::intersection, java.util.Set::retainAll);
        final RedBlackTree<Integer> union = checkOperation(t1, t2, RedBlackTree::union, java.util.Set::addAll);
        for (RedBlackTree<Integer> result : List.of(difference, intersection, union)) {
            checkOperation(result, t3, RedBlackTree::difference, java.util.Set::removeAll);
            checkOperation(result, t3, RedBlackTree::intersection, java.util.Set::retainAll);
            checkOperation(result, t3, RedBlackTree::union, java.util.Set::addAll);
            checkOperation(t3, result, RedBlackTree::difference, java.util.Set::removeAll);
            final int value = random.nextInt(200);
            checkOperation(result, RedBlackTree.of(result.comparator(), value), (a, b) -> a.insert(value), java.util.Set::addAll);
            checkOperation(result, RedBlackTree.of(result.comparator(), value), (a, b) -> a.delete(value), java.util.Set::removeAll);
            final java.util.List<Integer> elements = result.iterator().toJavaList();
            final java.util.TreeSet<Integer> expected = toJavaSet(result);
            RedBlackTree<Integer> remaining = result;
            for (int i = 0; i < 10 && !elements.isEmpty(); i++) {
                final Integer element = elements.get(random.nextInt(elements.size()));
                remaining = remaining.delete(element);
                expected.remove(element);
                assertValid(remaining);
                assertSameElements(remaining, expected);
            }
        }
    }

    private static RedBlackTree<Integer> checkOperation(RedBlackTree<Integer> t1, RedBlackTree<Integer> t2,
            BinaryOperator<RedBlackTree<Integer>> operation,
            java.util.function.BiConsumer<java.util.Set<Integer>, java.util.Set<Integer>> javaOperation) {
        final java.util.TreeSet<Integer> expected = toJavaSet(t1);
        javaOperation.accept(expected, toJavaSet(t2));
        final RedBlackTree<Integer> actual = operation.apply(t1, t2);
        assertValid(actual);
        assertSameElements(actual, expected);
        return actual;
    }

    private static RedBlackTree<Integer> randomTree(Random random, Comparator<Integer> comparator) {
        final int size = random.nextInt(101);
        RedBlackTree<Integer> tree = RedBlackTree.empty(comparator);
        for (int i = 0; i < size; i++) {
            tree = tree.insert(random.nextInt(200));
        }
        return tree;
    }

    private static java.util.TreeSet<Integer> toJavaSet(RedBlackTree<Integer> tree) {
        final java.util.TreeSet<Integer> set = new java.util.TreeSet<>(tree.comparator());
        tree.forEach(set::add);
        return set;
    }

    private static void assertSameElements(RedBlackTree<Integer> actual, java.util.TreeSet<Integer> expected) {
        assertThat(actual.iterator().toJavaList()).isEqualTo(new ArrayList<>(expected));
    }

    private static <T> void assertValid(RedBlackTree<T> tree) {
        if (tree.color() != Color.BLACK) {
            fail(tree, "root is red");
        }
        checkNode(tree, tree.comparator(), tree);
        final java.util.List<T> elements = tree.iterator().toJavaList();
        for (int i = 1; i < elements.size(); i++) {
            if (tree.comparator().compare(elements.get(i - 1), elements.get(i)) >= 0) {
                fail(tree, "elements out of order");
            }
        }
    }

    // returns {black height, size} of the given subtree, where the black height counts the black nodes on a path
    // from the subtree's root down to an empty leaf
    private static <T> int[] checkNode(RedBlackTree<T> tree, Comparator<T> comparator, RedBlackTree<T> root) {
        if (tree.isEmpty()) {
            return new int[] { 0, 0 };
        }
        final Node<T> node = (Node<T>) tree;
        if (node.color == Color.RED && (node.left.color() == Color.RED || node.right.color() == Color.RED)) {
            fail(root, "red node " + node + " has a red child");
        }
        final int[] left = checkNode(node.left, comparator, root);
        final int[] right = checkNode(node.right, comparator, root);
        if (left[0] != right[0]) {
            fail(root, "black heights differ below " + node);
        }
        if (node.blackHeight != left[0] + 1) {
            fail(root, "stored black height of " + node + " is " + node.blackHeight + ", expected " + (left[0] + 1));
        }
        final int size = left[1] + right[1] + 1;
        if (node.size != size) {
            fail(root, "stored size of " + node + " is " + node.size + ", expected " + size);
        }
        if (node.empty.comparator != comparator) {
            fail(root, "comparator differs at " + node);
        }
        return new int[] { left[0] + (node.color == Color.BLACK ? 1 : 0), size };
    }

    private static void fail(RedBlackTree<?> tree, String message) {
        throw new AssertionError("invalid tree " + tree + ": " + message);
    }
}
