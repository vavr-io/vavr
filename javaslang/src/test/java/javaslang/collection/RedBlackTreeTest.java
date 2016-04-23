/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.junit.Test;

import java.util.Comparator;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.control.Option.none;
import static javaslang.control.Option.of;
import static javaslang.control.Option.some;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RedBlackTreeTest {

    // Rudimentary tests

    // empty tree

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.isEmpty()).isTrue();
        assertThat(tree.size()).isEqualTo(0);
        assertThat(tree.color()).isEqualTo(BLACK);
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
        assertThat(tree.toLispString()).isEqualTo("()");
        assertThat(tree.size()).isEqualTo(0);

        tree = tree.insert(2);
        assertThat(tree.toLispString()).isEqualTo("(B:2)");
        assertThat(tree.size()).isEqualTo(1);

        tree = tree.insert(1);
        assertThat(tree.toLispString()).isEqualTo("(B:2 R:1)");
        assertThat(tree.size()).isEqualTo(2);

        tree = tree.insert(4);
        assertThat(tree.toLispString()).isEqualTo("(B:2 R:1 R:4)");
        assertThat(tree.size()).isEqualTo(3);

        tree = tree.insert(5);
        assertThat(tree.toLispString()).isEqualTo("(B:4 (B:2 R:1) B:5)");
        assertThat(tree.size()).isEqualTo(4);

        tree = tree.insert(9);
        assertThat(tree.toLispString()).isEqualTo("(B:4 (B:2 R:1) (B:5 R:9))");
        assertThat(tree.size()).isEqualTo(5);

        tree = tree.insert(3);
        assertThat(tree.toLispString()).isEqualTo("(B:4 (B:2 R:1 R:3) (B:5 R:9))");
        assertThat(tree.size()).isEqualTo(6);

        tree = tree.insert(6);
        assertThat(tree.toLispString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 B:9))");
        assertThat(tree.size()).isEqualTo(7);

        tree = tree.insert(7);
        assertThat(tree.toLispString()).isEqualTo("(B:4 (B:2 R:1 R:3) (R:6 B:5 (B:9 R:7)))");
        assertThat(tree.size()).isEqualTo(8);
    }

    @Test
    public void shouldInsertSortedSequenceAndEndUpWithPerfectTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        assertThat(tree.toLispString()).isEqualTo("(B:8 (B:4 (B:2 B:1 B:3) (B:6 B:5 B:7)) (B:12 (B:10 B:9 B:11) (B:14 B:13 B:15)))");
    }

// Not sure what is the purpose of this test - we shouldn't be able to insert nulls
//    @Test
//    public void shouldInsertNullIntoEmptyTreeBecauseComparatorNotCalled() {
//        final RedBlackTree<Integer> actual = RedBlackTree.<Integer> empty().insert(null);
//        final RedBlackTree<Integer> expected = RedBlackTree.of((Integer) null);
//        assertThat(actual).isEqualTo(expected);
//    }

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
        final RedBlackTree<Integer> unit = RedBlackTree.of(1, 2, 3);
        final RedBlackTree<Integer> actual = unit.insert(2);
        assertThat(actual).isEqualTo(unit);
    }

    // delete

    @Test
    public void shouldDelete_2_from_2_1_4_5_9_3_6_7() {
        final RedBlackTree<Integer> unit = RedBlackTree.of(2, 1, 4, 5, 9, 3, 6, 7);
        final RedBlackTree<Integer> actual = unit.delete(2);
        assertThat(actual.toLispString()).isEqualTo("(B:4 (B:3 R:1) (R:6 B:5 (B:9 R:7)))");
        assertThat(actual.size()).isEqualTo(7);
    }

    // difference()

    @Test
    public void shouldSubtractEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.empty();
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldSubtractNonEmptyFromEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        assertThat(actual).isEqualTo(t1);
    }

    @Test
    public void shouldSubtractNonEmptyFromNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.difference(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3);
        assertThat(actual).isEqualTo(expected);
    }

    // intersection()

    @Test
    public void shouldIntersectOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.empty();
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersectOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.empty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyUnbalancedHeightLeft() {
        // Node::mergeGT
        //
        // Trees have
        // - different values
        // - similar to each other left children
        // - and unlike each other right children
        final RedBlackTree<Integer> t1 = RedBlackTree.of(1, 2, 3, 4, 5, 6, 7, 8, 60, 66, 67);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(1, 2, 3, 10, 11, 12, 13, 14, 60, 76, 77);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(1, 2, 3, 60);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyUnbalancedHeightRight() {
        // Node::mergeLT
        //
        // Trees have
        // - different values
        // - unlike each other left children
        // - and similar to each other right children
        final RedBlackTree<Integer> t1 = RedBlackTree.of(1, 2, 3, 4, 40, 61, 62, 63, 64, 65);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(2, 7, 8, 9, 50, 61, 62, 63, 64, 65);
        final RedBlackTree<Integer> actual = t1.intersection(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(2, 61, 62, 63, 64, 65);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIntersectOnNonEmptyGivenNonEmptyBalancedHeightRight() {
        // Node::mergeEQ && isRed(n1.right)
        //
        final RedBlackTree<Integer> t1 = RedBlackTree.of(-10, -20, -30, -40, -50, 1, 10, 20, 30);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(-10, -20, -30, -40, -50, 2, 10, 20, 30);
        assertThat(t1.intersection(t2)).isEqualTo(t1.delete(1));
    }

    // union()

    @Test
    public void shouldUnionOnNonEmptyGivenEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.empty();
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3, 5);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnionOnEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.empty();
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(5, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnionOnNonEmptyGivenNonEmpty() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(3, 5);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(5, 7);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(3, 5, 7);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComputeUnionAndEqualTreesOfDifferentShapeButSameElements() {
        final RedBlackTree<Integer> t1 = RedBlackTree.of(-1, -1, 0, 1);
        final RedBlackTree<Integer> t2 = RedBlackTree.of(-2, -1, 0, 1);
        final RedBlackTree<Integer> actual = t1.union(t2);
        final RedBlackTree<Integer> expected = RedBlackTree.of(-2, -1, 0, 1);
        assertThat(actual).isEqualTo(expected);
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  TREE ACCESSORS AND ITERATOR (iterator, find, contains, isEmpty, size)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // iterator()

    @Test
    public void givenEmptyTree_ThenCanIterate() {
        assertThat(RedBlackTree.empty().iterator().hasNext()).isFalse();
    }

    @Test
    public void givenATree_WhenIterate_ThenItemsAreSorted() {
        final RedBlackTree<Integer> unit = RedBlackTree.of(18, 4, 10, 6, 8, 2, 12, 16, 20, 14);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20));
    }

    @Test
    public void givenATree_WhenIterate_ThenItemsAreSorted2() {
        final RedBlackTree<Integer> unit = RedBlackTree.of(8, 18, 26, 4, 6, 24, 2, 10, 12, 16, 20, 14, 22);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26));
    }

    @Test
    public void shouldIterateDescending() {
        final RedBlackTree<Integer> unit = RedBlackTree.of(8, 18, 26, 4, 6, 24, 2, 10, 12, 16, 20, 14, 22);
        assertThat(unit.descendingIterator().toList()).isEqualTo(List.of(26, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2));
    }

    // Find

    @Test
    public void givenEmptyTree_WhenFind_ThenReturnNone() {
        assertThat(RedBlackTree.<Integer>empty().floor(5)).isEqualTo(Option.<Integer>none());
    }

    @Test
    public void givenNonNaturalComparator_WhenFindValues_ThenUseIt() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value);
        assertThat(unit.find(Tuple.of("one", null))).isEqualTo(some(value));
    }

    @Test
    public void givenValueDoesNotExist_WhenFind_ThenReturnNone() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.find( 1)).isEqualTo(none());
        assertThat(tree.find( 3)).isEqualTo(none());
        assertThat(tree.find( 5)).isEqualTo(none());
        assertThat(tree.find( 7)).isEqualTo(none());
        assertThat(tree.find( 9)).isEqualTo(none());
        assertThat(tree.find(11)).isEqualTo(none());
        assertThat(tree.find(13)).isEqualTo(none());
        assertThat(tree.find(15)).isEqualTo(none());
        assertThat(tree.find(17)).isEqualTo(none());
        assertThat(tree.find(19)).isEqualTo(none());
        assertThat(tree.find(21)).isEqualTo(none());
    }

    @Test
    public void givenValueExists_WhenFind_ThenReturnValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.find( 2)).isEqualTo(of(2));
        assertThat(tree.find( 4)).isEqualTo(of(4));
        assertThat(tree.find( 6)).isEqualTo(of(6));
        assertThat(tree.find( 8)).isEqualTo(of(8));
        assertThat(tree.find(10)).isEqualTo(of(10));
        assertThat(tree.find(12)).isEqualTo(of(12));
        assertThat(tree.find(14)).isEqualTo(of(14));
        assertThat(tree.find(16)).isEqualTo(of(16));
        assertThat(tree.find(18)).isEqualTo(of(18));
        assertThat(tree.find(20)).isEqualTo(of(20));
    }

    // Contains

    @Test
    public void givenEmptyTree_WhenContains_ThenReturnFalse() {
        assertThat(RedBlackTree.<Integer>empty().contains(5)).isFalse();
    }

    @Test
    public void givenValueDoesNotExist_WhenContains_ThenReturnNone() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.contains( 1)).isFalse();
        assertThat(tree.contains( 5)).isFalse();
        assertThat(tree.contains( 11)).isFalse();
        assertThat(tree.contains( 15)).isFalse();
        assertThat(tree.contains( 21)).isFalse();
    }

    @Test
    public void givenValueExists_WhenContains_ThenReturnTrue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.contains( 2)).isTrue();
        assertThat(tree.contains( 6)).isTrue();
        assertThat(tree.contains(10)).isTrue();
        assertThat(tree.contains(14)).isTrue();
        assertThat(tree.contains(20)).isTrue();
    }

    // Size

    @Test
    public void canReturnSize() {
        assertThat(RedBlackTree.empty().size()).isEqualTo(0);
        assertThat(RedBlackTree.of(1).size()).isEqualTo(1);
        assertThat(RedBlackTree.of(1, 2).size()).isEqualTo(2);
        assertThat(RedBlackTree.of(1, 2, 3).size()).isEqualTo(3);
        assertThat(RedBlackTree.of(1, 2, 3, 4).size()).isEqualTo(4);
    }

    // IsEmpty

    @Test
    public void canReturnIsEmpty() {
        assertThat(RedBlackTree.empty().isEmpty()).isTrue();
        assertThat(RedBlackTree.of().isEmpty()).isTrue();
        assertThat(RedBlackTree.of(1).isEmpty()).isFalse();
        assertThat(RedBlackTree.of(1, 2).isEmpty()).isFalse();
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  TREE NAVIGATION (floor, ceiling, higher, lower, min, max)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // floor
    @Test
    public void givenEmptyTree_WhenFloor_ThenReturnNone() {
        assertThat(RedBlackTree.<Integer>empty().floor(5)).isEqualTo(Option.<Integer>none());
    }

    @Test
    public void givenNonNaturalComparator_WhenFloor_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value);
        assertThat(unit.floor(Tuple.of("one", null))).isEqualTo(some(value));
    }

    @Test
    public void givenValueExists_WhenFloor_ThenReturnSameValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.floor( 2)).isEqualTo(of(2));
        assertThat(tree.floor( 4)).isEqualTo(of(4));
        assertThat(tree.floor( 6)).isEqualTo(of(6));
        assertThat(tree.floor( 8)).isEqualTo(of(8));
        assertThat(tree.floor(10)).isEqualTo(of(10));
        assertThat(tree.floor(12)).isEqualTo(of(12));
        assertThat(tree.floor(14)).isEqualTo(of(14));
        assertThat(tree.floor(16)).isEqualTo(of(16));
        assertThat(tree.floor(18)).isEqualTo(of(18));
        assertThat(tree.floor(20)).isEqualTo(of(20));
    }

    @Test
    public void givenValueDoesNotExist_WhenFloor_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.floor( 1)).isEqualTo(none());
        assertThat(tree.floor( 3)).isEqualTo(of(2));
        assertThat(tree.floor( 5)).isEqualTo(of(4));
        assertThat(tree.floor( 7)).isEqualTo(of(6));
        assertThat(tree.floor( 9)).isEqualTo(of(8));
        assertThat(tree.floor(11)).isEqualTo(of(10));
        assertThat(tree.floor(13)).isEqualTo(of(12));
        assertThat(tree.floor(15)).isEqualTo(of(14));
        assertThat(tree.floor(17)).isEqualTo(of(16));
        assertThat(tree.floor(19)).isEqualTo(of(18));
        assertThat(tree.floor(21)).isEqualTo(of(20));
    }

    // ceiling
    @Test
    public void givenEmptyTree_WhenCeiling_ThenReturnNone() {
        assertThat(RedBlackTree.<Integer>empty().ceiling(5)).isEqualTo(Option.<Integer>none());
    }

    @Test
    public void givenNonNaturalComparator_WhenCeiling_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value);
        assertThat(unit.ceiling(Tuple.of("one", null))).isEqualTo(some(value));
    }

    @Test
    public void givenValueExists_WhenCeiling_ThenReturnSameValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.ceiling( 2)).isEqualTo(of(2));
        assertThat(tree.ceiling( 4)).isEqualTo(of(4));
        assertThat(tree.ceiling( 6)).isEqualTo(of(6));
        assertThat(tree.ceiling( 8)).isEqualTo(of(8));
        assertThat(tree.ceiling(10)).isEqualTo(of(10));
        assertThat(tree.ceiling(12)).isEqualTo(of(12));
        assertThat(tree.ceiling(14)).isEqualTo(of(14));
        assertThat(tree.ceiling(16)).isEqualTo(of(16));
        assertThat(tree.ceiling(18)).isEqualTo(of(18));
        assertThat(tree.ceiling(20)).isEqualTo(of(20));
    }

    @Test
    public void givenValueDoesNotExist_WhenCeiling_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.ceiling( 1)).isEqualTo(of(2));
        assertThat(tree.ceiling( 3)).isEqualTo(of(4));
        assertThat(tree.ceiling( 5)).isEqualTo(of(6));
        assertThat(tree.ceiling( 7)).isEqualTo(of(8));
        assertThat(tree.ceiling( 9)).isEqualTo(of(10));
        assertThat(tree.ceiling(11)).isEqualTo(of(12));
        assertThat(tree.ceiling(13)).isEqualTo(of(14));
        assertThat(tree.ceiling(15)).isEqualTo(of(16));
        assertThat(tree.ceiling(17)).isEqualTo(of(18));
        assertThat(tree.ceiling(19)).isEqualTo(of(20));
        assertThat(tree.ceiling(21)).isEqualTo(none());
    }

    // higher
    @Test
    public void givenEmptyTree_WhenHigher_ThenReturnNone() {
        assertThat(RedBlackTree.<Integer>empty().higher(5)).isEqualTo(Option.<Integer>none());
    }

    @Test
    public void givenNonNaturalComparator_WhenHigher_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value);
        assertThat(unit.higher(Tuple.of("_previous", null))).isEqualTo(some(value));
    }

    @Test
    public void givenValueExists_WhenHigher_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.higher( 2)).isEqualTo(of(4));
        assertThat(tree.higher( 4)).isEqualTo(of(6));
        assertThat(tree.higher( 6)).isEqualTo(of(8));
        assertThat(tree.higher( 8)).isEqualTo(of(10));
        assertThat(tree.higher(10)).isEqualTo(of(12));
        assertThat(tree.higher(12)).isEqualTo(of(14));
        assertThat(tree.higher(14)).isEqualTo(of(16));
        assertThat(tree.higher(16)).isEqualTo(of(18));
        assertThat(tree.higher(18)).isEqualTo(of(20));
    }

    @Test
    public void givenValueDoesNotExist_WhenHigher_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.higher( 1)).isEqualTo(of(2));
        assertThat(tree.higher( 3)).isEqualTo(of(4));
        assertThat(tree.higher( 5)).isEqualTo(of(6));
        assertThat(tree.higher( 7)).isEqualTo(of(8));
        assertThat(tree.higher( 9)).isEqualTo(of(10));
        assertThat(tree.higher(11)).isEqualTo(of(12));
        assertThat(tree.higher(13)).isEqualTo(of(14));
        assertThat(tree.higher(15)).isEqualTo(of(16));
        assertThat(tree.higher(17)).isEqualTo(of(18));
        assertThat(tree.higher(19)).isEqualTo(of(20));
        assertThat(tree.higher(21)).isEqualTo(none());
    }

    // lower
    @Test
    public void givenEmptyTree_WhenLower_ThenReturnNone() {
        assertThat(RedBlackTree.<Integer>empty().floor(5)).isEqualTo(Option.<Integer>none());
    }

    @Test
    public void givenNonNaturalComparator_WhenLower_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value);
        assertThat(unit.lower(Tuple.of("zz_later", null))).isEqualTo(some(value));
    }

    @Test
    public void givenValueExists_WhenLower_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.lower( 2)).isEqualTo(none());
        assertThat(tree.lower( 4)).isEqualTo(of(2));
        assertThat(tree.lower( 6)).isEqualTo(of(4));
        assertThat(tree.lower( 8)).isEqualTo(of(6));
        assertThat(tree.lower(10)).isEqualTo(of(8));
        assertThat(tree.lower(12)).isEqualTo(of(10));
        assertThat(tree.lower(14)).isEqualTo(of(12));
        assertThat(tree.lower(16)).isEqualTo(of(14));
        assertThat(tree.lower(18)).isEqualTo(of(16));
        assertThat(tree.lower(20)).isEqualTo(of(18));
    }

    @Test
    public void givenValueDoesNotExist_WhenLower_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.lower( 1)).isEqualTo(none());
        assertThat(tree.lower( 3)).isEqualTo(of(2));
        assertThat(tree.lower( 5)).isEqualTo(of(4));
        assertThat(tree.lower( 7)).isEqualTo(of(6));
        assertThat(tree.lower( 9)).isEqualTo(of(8));
        assertThat(tree.lower(11)).isEqualTo(of(10));
        assertThat(tree.lower(13)).isEqualTo(of(12));
        assertThat(tree.lower(15)).isEqualTo(of(14));
        assertThat(tree.lower(17)).isEqualTo(of(16));
        assertThat(tree.lower(19)).isEqualTo(of(18));
        assertThat(tree.lower(21)).isEqualTo(of(20));
    }

    // min / max

    @Test
    public void givenEmptyTree_ThenMinReturnsNone() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.min()).isEqualTo(none());
    }

    @Test
    public void givenNonEmptyTree_ThenCanFindMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.min()).isEqualTo(of(2));
    }

    @Test
    public void givenEmptyTree_ThenMaxReturnsNone() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.max()).isEqualTo(none());
    }

    @Test
    public void givenNonEmptyTree_ThenCanFindMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        assertThat(tree.max()).isEqualTo(of(20));
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  SUBTREE ACCESSORS AND ITERATOR (iterate, find, contains, isEmpty, size)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree - Iterate Ascending

    @Test
    public void givenFromStartAndToEndInclusiveRange_ThenCanCreateAndIterateSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(1), true, of(15), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(2), true, of(14), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(3), true, of(13), true).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(4), true, of(12), true).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(6), true, of(10), true).iterator().toList()).containsExactly(6, 8, 10);
        assertThat(tree.subTree(of(2), true, of(4), true).iterator().toList()).containsExactly(2, 4);
        assertThat(tree.subTree(of(12), true, of(14), true).iterator().toList()).containsExactly(12, 14);
        assertThat(tree.subTree(of(8), true, of(8), true).iterator().toList()).containsExactly(8);
        assertThat(tree.subTree(of(3), true, of(3), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), true, of(1), true).iterator().toList()).isEmpty();

        for (int from = 1; from <=15; from++) {
            for (int to = from; to <=15; to++) {
                final List<Integer> expected = List.rangeClosed(from, to).filter(value -> value % 2 == 0);
                assertThat(tree.subTree(of(from), true, of(to), true))
                        .as(String.format("subTree(%d, true, %d, true)", from, to))
                        .containsExactlyElementsOf(expected);
            }
        }
    }

    @Test
    public void givenFromStartInclusiveAndToEndExclusiveRange_ThenCanCreateAndIterateOverSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(1), true, of(15), false).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(2), true, of(14), false).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(3), true, of(13), false).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(4), true, of(12), false).iterator().toList()).containsExactly(4, 6, 8, 10);
        assertThat(tree.subTree(of(6), true, of(10), false).iterator().toList()).containsExactly(6, 8);
        assertThat(tree.subTree(of(2), true, of(4), false).iterator().toList()).containsExactly(2);
        assertThat(tree.subTree(of(12), true, of(14), false).iterator().toList()).containsExactly(12);
        assertThat(tree.subTree(of(8), true, of(8), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), true, of(3), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), true, of(1), false).iterator().toList()).isEmpty();
    }

    @Test
    public void givenFromStartExclusiveAndToEndInclusiveRange_ThenCanCreateAndIterateOverSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(1), false, of(15), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(2), false, of(14), true).iterator().toList()).containsExactly(4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(3), false, of(13), true).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(4), false, of(12), true).iterator().toList()).containsExactly(6, 8, 10, 12);
        assertThat(tree.subTree(of(6), false, of(10), true).iterator().toList()).containsExactly(8, 10);
        assertThat(tree.subTree(of(2), false, of(4), true).iterator().toList()).containsExactly(4);
        assertThat(tree.subTree(of(12), false, of(14), true).iterator().toList()).containsExactly(14);
        assertThat(tree.subTree(of(8), false, of(8), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), false, of(3), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), false, of(1), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenFromStartExclusiveAndToEndExclusiveRange_ThenCanCreateAndIterateOverSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(1), false, of(15), false).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(2), false, of(14), false).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(3), false, of(13), false).iterator().toList()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(4), false, of(12), false).iterator().toList()).containsExactly(6, 8, 10);
        assertThat(tree.subTree(of(6), false, of(10), false).iterator().toList()).containsExactly(8);
        assertThat(tree.subTree(of(2), false, of(4), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(12), false, of(14), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(8), false, of(8), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), false, of(3), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), false, of(1), false).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundedFromStart_ThenCanCreateAndIterateOverSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(none(), true, of(15), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(none(), true, of(10), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10);
        assertThat(tree.subTree(none(), true, of(6), true).iterator().toList()).containsExactly(2, 4, 6);
        assertThat(tree.subTree(none(), true, of(2), true).iterator().toList()).containsExactly(2);
        assertThat(tree.subTree(none(), true, of(1), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundedToEnd_ThenCanCreateAndIterateOverSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(1), true, none(), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(2), true, none(), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(6), true, none(), true).iterator().toList()).containsExactly(6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(14), true, none(), true).iterator().toList()).containsExactly(14);
        assertThat(tree.subTree(of(15), true, none(), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundedRange_ThenCanCreateSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(none(), true, none(), true).iterator().toList()).containsExactly(2, 4, 6, 8, 10, 12, 14);

    }

    @Test
    public void givenFromStartGreaterThanToEndRange_ThenThrowsExceptionWhenCreateSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThatThrownBy(() -> tree.subTree(of(10), true, of(2), true)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(
                () -> tree.subTree(of(2), true, of(12), true).subTree(of(8), true, of(2), true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // SubTree - Accessors

    @Test
    public void givenSubTree_ThenCanFindNode() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(8), true, of(24), true);
        assertThat(subtree.find(6)).isEqualTo(none());
        assertThat(subtree.find(7)).isEqualTo(none());
        assertThat(subtree.find(8)).isEqualTo(of(8));
        assertThat(subtree.find(12)).isEqualTo(of(12));
        assertThat(subtree.find(15)).isEqualTo(none());
        assertThat(subtree.find(20)).isEqualTo(of(20));
        assertThat(subtree.find(24)).isEqualTo(of(24));
        assertThat(subtree.find(4)).isEqualTo(none());
        assertThat(subtree.find(25)).isEqualTo(none());
        assertThat(subtree.find(26)).isEqualTo(none());
    }

    @Test
    public void givenSubTree_ThenCanCheckIfSubTreeContainsNode() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(8), true, of(24), true);
        assertThat(subtree.contains(6)).isFalse();
        assertThat(subtree.contains(7)).isFalse();
        assertThat(subtree.contains(8)).isTrue();
        assertThat(subtree.contains(12)).isTrue();
        assertThat(subtree.contains(15)).isFalse();
        assertThat(subtree.contains(20)).isTrue();
        assertThat(subtree.contains(24)).isTrue();
        assertThat(subtree.contains(4)).isFalse();
        assertThat(subtree.contains(25)).isFalse();
        assertThat(subtree.contains(26)).isFalse();
    }

    @Test
    public void givenSubTree_ThenCanDetermineIfEmpty() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(8), true, of(24), true).isEmpty()).isFalse();
        assertThat(tree.subTree(of(8), true, of(8), true).isEmpty()).isFalse();
        assertThat(tree.subTree(of(8), false, of(8), false).isEmpty()).isTrue();
        assertThat(tree.subTree(of(7), true, of(7), true).isEmpty()).isTrue();
        assertThat(tree.subTree(of(0), true, of(1), true).isEmpty()).isTrue();
        assertThat(tree.subTree(of(32), true, of(40), true).isEmpty()).isTrue();
    }

    @Test
    public void givenSubTree_ThenCanDetermineSize() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(2), true, of(30), true).size()).isEqualTo(15);
        assertThat(tree.subTree(of(4), true, of(28), true).size()).isEqualTo(13);
        assertThat(tree.subTree(of(6), true, of(26), true).size()).isEqualTo(11);
        assertThat(tree.subTree(of(8), true, of(24), true).size()).isEqualTo(9);
        assertThat(tree.subTree(of(10), true, of(22), true).size()).isEqualTo(7);
        assertThat(tree.subTree(of(12), true, of(20), true).size()).isEqualTo(5);
        assertThat(tree.subTree(of(14), true, of(18), true).size()).isEqualTo(3);
        assertThat(tree.subTree(of(16), true, of(16), true).size()).isEqualTo(1);
        assertThat(tree.subTree(of(4), true, of(6), true).size()).isEqualTo(2);
        assertThat(tree.subTree(of(1), true, of(7), true).size()).isEqualTo(3);
        assertThat(tree.subTree(of(32), true, of(40), true).size()).isEqualTo(0);
        assertThat(tree.subTree(of(7), true, of(7), true).size()).isEqualTo(0);
        assertThat(tree.subTree(of(0), true, of(1), true).size()).isEqualTo(0);
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  SUBTREE NAVIGATORS (min, max, floor, ceiling, higher, lower)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree - Min / Max
    @Test
    public void givenSubTree_ThenShouldReturnMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(6), true, of(26), true).max()).isEqualTo(of(26));
        assertThat(tree.subTree(of(10), true, of(20), true).max()).isEqualTo(of(20));
        assertThat(tree.subTree(of(7), true, of(25), true).max()).isEqualTo(of(24));
        assertThat(tree.subTree(of(7), true, of(40), true).max()).isEqualTo(of(30));
        assertThat(tree.subTree(of(7), true, none(), true).max()).isEqualTo(of(30));
        assertThat(tree.subTree(of(0), true, of(1), true).max()).isEqualTo(none());
    }

    @Test
    public void givenSubTreeWithToEndEndExclusive_ThenShouldReturnMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(6), true, of(26), false).max()).isEqualTo(of(24));
    }

    @Test
    public void givenSubTree_ThenShouldReturnMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(6), true, of(26), true).min()).isEqualTo(of(6));
        assertThat(tree.subTree(of(14), true, of(26), true).min()).isEqualTo(of(14));
        assertThat(tree.subTree(of(13), true, of(26), true).min()).isEqualTo(of(14));
        assertThat(tree.subTree(of(3), true, of(26), true).min()).isEqualTo(of(4));
        assertThat(tree.subTree(of(0), true, of(26), true).min()).isEqualTo(of(2));
        assertThat(tree.subTree(none(), true, of(26), true).min()).isEqualTo(of(2));
        assertThat(tree.subTree(of(0), true, of(1), true).min()).isEqualTo(none());
        assertThat(tree.subTree(of(40), true, of(50), true).min()).isEqualTo(none());
    }

    @Test
    public void givenSubTreeWithFromStartExclusive_ThenShouldReturnMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        assertThat(tree.subTree(of(6), false, of(26), false).min()).isEqualTo(of(8));
    }

    // SubTree - Floor / Ceiling / Higher/ Lower
    @Test
    public void givenSubTree_ThenShouldReturnFloor() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), true);
        assertThat(subtree.floor(1)).isEqualTo(none());
        assertThat(subtree.floor(2)).isEqualTo(none());
        assertThat(subtree.floor(4)).isEqualTo(none());
        assertThat(subtree.floor(6)).isEqualTo(of(6));
        assertThat(subtree.floor(7)).isEqualTo(of(6));
        assertThat(subtree.floor(8)).isEqualTo(of(8));
        assertThat(subtree.floor(9)).isEqualTo(of(8));
        assertThat(subtree.floor(10)).isEqualTo(of(10));
        assertThat(subtree.floor(11)).isEqualTo(of(10));
        assertThat(subtree.floor(24)).isEqualTo(of(24));
        assertThat(subtree.floor(25)).isEqualTo(of(24));
        assertThat(subtree.floor(26)).isEqualTo(of(26));
        assertThat(subtree.floor(27)).isEqualTo(of(26));
        assertThat(subtree.floor(28)).isEqualTo(of(26));
        assertThat(subtree.floor(31)).isEqualTo(of(26));
    }

    @Test
    public void givenSubTreeWithFromStartExclusive_ThenShouldReturnFloor() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), false, of(26), true);
        assertThat(subtree.floor(4)).isEqualTo(none());
        assertThat(subtree.floor(6)).isEqualTo(none());
        assertThat(subtree.floor(7)).isEqualTo(none());
        assertThat(subtree.floor(8)).isEqualTo(of(8));
        assertThat(subtree.floor(9)).isEqualTo(of(8));
    }

    @Test
    public void givenSubTree_ThenShouldReturnCeiling() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), true);
        assertThat(subtree.ceiling(1)).isEqualTo(of(6));
        assertThat(subtree.ceiling(2)).isEqualTo(of(6));
        assertThat(subtree.ceiling(4)).isEqualTo(of(6));
        assertThat(subtree.ceiling(6)).isEqualTo(of(6));
        assertThat(subtree.ceiling(7)).isEqualTo(of(8));
        assertThat(subtree.ceiling(8)).isEqualTo(of(8));
        assertThat(subtree.ceiling(9)).isEqualTo(of(10));
        assertThat(subtree.ceiling(10)).isEqualTo(of(10));
        assertThat(subtree.ceiling(11)).isEqualTo(of(12));
        assertThat(subtree.ceiling(24)).isEqualTo(of(24));
        assertThat(subtree.ceiling(25)).isEqualTo(of(26));
        assertThat(subtree.ceiling(26)).isEqualTo(of(26));
        assertThat(subtree.ceiling(27)).isEqualTo(none());
        assertThat(subtree.ceiling(28)).isEqualTo(none());
        assertThat(subtree.ceiling(31)).isEqualTo(none());
    }

    @Test
    public void givenSubTreeWithToEndExclusive_ThenShouldReturnCeiling() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), false);
        assertThat(subtree.ceiling(23)).isEqualTo(of(24));
        assertThat(subtree.ceiling(24)).isEqualTo(of(24));
        assertThat(subtree.ceiling(25)).isEqualTo(none());
        assertThat(subtree.ceiling(26)).isEqualTo(none());
    }

    @Test
    public void givenSubTree_ThenShouldReturnLower() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), true);
        assertThat(subtree.lower(1)).isEqualTo(none());
        assertThat(subtree.lower(2)).isEqualTo(none());
        assertThat(subtree.lower(4)).isEqualTo(none());
        assertThat(subtree.lower(6)).isEqualTo(none());
        assertThat(subtree.lower(7)).isEqualTo(of(6));
        assertThat(subtree.lower(8)).isEqualTo(of(6));
        assertThat(subtree.lower(9)).isEqualTo(of(8));
        assertThat(subtree.lower(10)).isEqualTo(of(8));
        assertThat(subtree.lower(11)).isEqualTo(of(10));
        assertThat(subtree.lower(24)).isEqualTo(of(22));
        assertThat(subtree.lower(25)).isEqualTo(of(24));
        assertThat(subtree.lower(26)).isEqualTo(of(24));
        assertThat(subtree.lower(27)).isEqualTo(of(26));
        assertThat(subtree.lower(28)).isEqualTo(of(26));
        assertThat(subtree.lower(31)).isEqualTo(of(26));
    }

    @Test
    public void givenSubTreeWithFromStartExclusive_ThenShouldReturnLower() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), false, of(26), true);
        assertThat(subtree.lower(6)).isEqualTo(none());
        assertThat(subtree.lower(7)).isEqualTo(none());
        assertThat(subtree.lower(8)).isEqualTo(none());
        assertThat(subtree.lower(9)).isEqualTo(of(8));
        assertThat(subtree.lower(10)).isEqualTo(of(8));
    }

    @Test
    public void givenSubTree_ThenShouldReturnHigher() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), true);
        assertThat(subtree.higher(1)).isEqualTo(of(6));
        assertThat(subtree.higher(2)).isEqualTo(of(6));
        assertThat(subtree.higher(4)).isEqualTo(of(6));
        assertThat(subtree.higher(6)).isEqualTo(of(8));
        assertThat(subtree.higher(7)).isEqualTo(of(8));
        assertThat(subtree.higher(8)).isEqualTo(of(10));
        assertThat(subtree.higher(9)).isEqualTo(of(10));
        assertThat(subtree.higher(10)).isEqualTo(of(12));
        assertThat(subtree.higher(11)).isEqualTo(of(12));
        assertThat(subtree.higher(24)).isEqualTo(of(26));
        assertThat(subtree.higher(25)).isEqualTo(of(26));
        assertThat(subtree.higher(26)).isEqualTo(none());
        assertThat(subtree.higher(27)).isEqualTo(none());
        assertThat(subtree.higher(28)).isEqualTo(none());
        assertThat(subtree.higher(31)).isEqualTo(none());
    }

    @Test
    public void givenSubTreeWithToEndExclusive_ThenShouldReturnHigher() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), true, of(26), false);
        assertThat(subtree.higher(22)).isEqualTo(of(24));
        assertThat(subtree.higher(23)).isEqualTo(of(24));
        assertThat(subtree.higher(24)).isEqualTo(none());
        assertThat(subtree.higher(25)).isEqualTo(none());
        assertThat(subtree.higher(26)).isEqualTo(none());
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  SUBTREE MODIFIERS (insert, delete)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree - Insert / Delete
    @Test
    public void givenSubTreeWithClosedRange_WhenInsertItemInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.subTree(of(4), true, of(10), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.insert(4).insert(5).insert(6).insert(9).insert(10);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(4, 5, 6, 8, 9, 10);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenSubTreeWithIsOpenEnded_WhenInsertItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10);
        RedBlackTree<Integer> subtree = tree.subTree(of(4), true, none(), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.insert(5).insert(7).insert(15).insert(33);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(4, 5, 6, 7, 8, 10, 15, 33);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenSubTree_WhenInsertItemsOutsideOfRange_ThenCreateNewTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), false, of(12), false);
        assertThat(subtree.insert(3).insert(1).iterator().toList()).containsExactly(1, 3, 8, 10);
        assertThat(subtree.insert(3).insert(1).isView()).isFalse();
        assertThat(subtree.insert(16).insert(18).iterator().toList()).containsExactly(8, 10, 16, 18);
        assertThat(subtree.insert(16).insert(18).isView()).isFalse();
        assertThat(subtree.insert(6).iterator().toList()).containsExactly(6, 8, 10);
        assertThat(subtree.insert(6).isView()).isFalse();
        assertThat(subtree.insert(12).iterator().toList()).containsExactly(8, 10, 12);
        assertThat(subtree.insert(12).isView()).isFalse();
    }

    @Test
    public void givenSubTreeWithClosedRange_WhenDeleteItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.subTree(of(4), true, of(10), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.delete(4).delete(5).delete(8).delete(12);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(6, 10);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenSubTreeWithIsOpenEnded_WhenDeleteItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10);
        RedBlackTree<Integer> subtree = tree.subTree(of(4), true, none(), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.delete(8).delete(15).delete(33);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(4, 6, 10);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenSubTree_WhenDeleteItemsOutsideRange_ThenDoNothing() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16);
        RedBlackTree<Integer> subtree = tree.subTree(of(6), false, of(14), false);
        assertThat(subtree.delete(1).delete(4).iterator().toList()).containsExactly(8, 10, 12);
        assertThat(subtree.delete(1).delete(4).isView()).isTrue();
        assertThat(subtree.delete(6).iterator().toList()).containsExactly(8, 10, 12);
        assertThat(subtree.delete(6).isView()).isTrue();
        assertThat(subtree.delete(14).delete(16).delete(18).iterator().toList()).containsExactly(8, 10, 12);
        assertThat(subtree.delete(14).delete(16).delete(18).isView()).isTrue();
        assertThat(subtree.delete(8).delete(10).delete(12).size()).isEqualTo(0);
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  SUBTREE OF SUBTREEs
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTrees of SubTrees
    @Test
    public void givenSubTree_ThenCanCreateAdditionalSubTreesWithInnerRange() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree1 = tree.subTree(of(4), true, of(28), true);
        RedBlackTree<Integer> subtree2 = subtree1.subTree(of(10), true, of(20), false);
        RedBlackTree<Integer> subtree3 = subtree2.subTree(of(12), false, of(16), true);
        assertThat(subtree1).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree2).containsExactly(10, 12, 14, 16, 18);
        assertThat(subtree3).containsExactly(14, 16);
    }

    @Test
    public void givenSubTree_ThenCanCreateAdditionalSubTreesWithOuterRange() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree1 = tree.subTree(of(4), true, of(28), true);
        RedBlackTree<Integer> subtree2 = subtree1.subTree(none(), true, none(), true);
        RedBlackTree<Integer> subtree3 = subtree2.subTree(of(0), true, of(16), true);
        RedBlackTree<Integer> subtree4 = subtree3.subTree(of(7), true, of(50), true);
        RedBlackTree<Integer> subtree5 = subtree4.subTree(of(40), true, of(50), true);
        assertThat(subtree1).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree2).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree3).containsExactly(4, 6, 8, 10, 12, 14, 16);
        assertThat(subtree4).containsExactly(8, 10, 12, 14, 16);
        assertThat(subtree5).isEmpty();
    }

    @Test
    public void givenSubTreeWithFromAndToInclusive_ThenShouldCreateSubTrees() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree1 = tree.subTree(of(4), true, of(28), true);
        RedBlackTree<Integer> subtree2 = subtree1.subTree(of(4), true, of(28), true);
        RedBlackTree<Integer> subtree3 = subtree1.subTree(of(4), false, of(28), true);
        RedBlackTree<Integer> subtree4 = subtree1.subTree(of(4), true, of(28), false);
        RedBlackTree<Integer> subtree5 = subtree1.subTree(of(4), false, of(28), false);
        assertThat(subtree1).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree2).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree3).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28);
        assertThat(subtree4).containsExactly(4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
        assertThat(subtree5).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
    }

    @Test
    public void givenSubTreeWithFromAndToExclusive_ThenShouldCreateSubTrees() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree1 = tree.subTree(of(4), false, of(28), false);
        RedBlackTree<Integer> subtree2 = subtree1.subTree(of(4), true, of(28), true);
        RedBlackTree<Integer> subtree3 = subtree1.subTree(of(4), false, of(28), true);
        RedBlackTree<Integer> subtree4 = subtree1.subTree(of(4), true, of(28), false);
        RedBlackTree<Integer> subtree5 = subtree1.subTree(of(4), false, of(28), false);
        assertThat(subtree1).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
        assertThat(subtree2).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
        assertThat(subtree3).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
        assertThat(subtree4).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
        assertThat(subtree5).containsExactly(6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26);
    }

    @Test
    public void givenEmptySubTree_ThenAnyAdditionalSubTressRemainEmpty() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30);
        RedBlackTree<Integer> subtree1 = tree.subTree(of(40), true, of(50), true);
        RedBlackTree<Integer> subtree2 = subtree1.subTree(of(8), true, of(20), true);
        assertThat(subtree1).isEmpty();
        assertThat(subtree2).isEmpty();
    }

    @Test
    public void givenEmptyTree_ThenAnySubTreesAreAlsoEmpty() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        RedBlackTree<Integer> subTree = tree.subTree(of(4), true, of(28), true);
        assertThat(subTree).isEmpty();
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  DESCENDING TREE ITERATOR
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // Create and Iterate over a descending tree

    @Test
    public void givenEmptyTree_ThenCanReturnDescendingTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.descendingTree()).isEqualTo(RedBlackTree.empty());
    }

    @Test
    public void givenTree_ThenCanReturnDescendingTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.descendingTree()).containsExactly(14, 12, 10, 8, 6, 4, 2);
    }

    @Test
    public void givenDescendingTree_WhenCreatingAnotherTree_ThenUseDescendingComparator() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        final RedBlackTree<Integer> descendingTree = tree.descendingTree();
        final RedBlackTree<Integer> newDescendingTree = RedBlackTree.ofAll(descendingTree.comparator(), descendingTree);
        assertThat(newDescendingTree).containsExactly(14, 12, 10, 8, 6, 4, 2);
    }

    @Test
    public void givenTree_ThenTheDescendingTreeOfADescendingTreeIsTheOriginalTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.descendingTree().descendingTree()).containsExactly(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.descendingTree().descendingTree().descendingTree()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.descendingTree().descendingTree().descendingTree().descendingTree()).containsExactly(2, 4, 6, 8, 10, 12, 14);
    }

    @Test
    public void givenSubTree_ThenCanReturnDescendingTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(4), true, of(12), true).descendingTree()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(4), false, of(12), true).descendingTree()).containsExactly(12, 10, 8, 6);
        assertThat(tree.subTree(of(4), true, of(12), false).descendingTree()).containsExactly(10, 8, 6, 4);
        assertThat(tree.subTree(of(4), false, of(12), false).descendingTree()).containsExactly(10, 8, 6);
        assertThat(tree.subTree(none(), false, of(12), false).descendingTree()).containsExactly(10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(4), true, none(), true).descendingTree()).containsExactly(14, 12, 10, 8, 6, 4);
        assertThat(tree.subTree(none(), true, none(), true).descendingTree()).containsExactly(14, 12, 10, 8, 6, 4, 2);
    }

    @Test
    public void givenSubTree_ThenTheDescendingTreeOfADescendingTreeIsTheOriginalSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(of(4), true, of(12), true).descendingTree().descendingTree()).containsExactly(4, 6, 8, 10, 12);
        assertThat(tree.subTree(of(4), false, of(12), true).descendingTree().descendingTree()).containsExactly(6, 8, 10, 12);
        assertThat(tree.subTree(of(4), true, of(12), false).descendingTree().descendingTree()).containsExactly(4, 6, 8, 10);
        assertThat(tree.subTree(of(4), false, of(12), false).descendingTree().descendingTree()).containsExactly(6, 8, 10);
        assertThat(tree.subTree(none(), false, of(12), false).descendingTree().descendingTree()).containsExactly(2, 4, 6, 8, 10);
        assertThat(tree.subTree(of(4), true, none(), true).descendingTree().descendingTree()).containsExactly(4, 6, 8, 10, 12, 14);
        assertThat(tree.subTree(none(), true, none(), true).descendingTree().descendingTree()).containsExactly(2, 4, 6, 8, 10, 12, 14);
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  DESCENDING TREE NAVIGATORS (floor, ceiling higher, lower, min, max)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // floor

    @Test
    public void givenDescendingTreeAndNonNaturalComparator_WhenFloor_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value).descendingTree();
        assertThat(unit.floor(Tuple.of("one", null))).isEqualTo(some(value));
    }

    @Test
    public void givenDescendingTreeAndValueExists_WhenFloor_ThenReturnSameValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.floor( 2)).isEqualTo(of(2));
        assertThat(tree.floor( 4)).isEqualTo(of(4));
        assertThat(tree.floor( 6)).isEqualTo(of(6));
        assertThat(tree.floor( 8)).isEqualTo(of(8));
        assertThat(tree.floor(10)).isEqualTo(of(10));
        assertThat(tree.floor(12)).isEqualTo(of(12));
        assertThat(tree.floor(14)).isEqualTo(of(14));
        assertThat(tree.floor(16)).isEqualTo(of(16));
        assertThat(tree.floor(18)).isEqualTo(of(18));
        assertThat(tree.floor(20)).isEqualTo(of(20));
    }

    @Test
    public void givenDescendingTreeAndValueDoesNotExist_WhenFloor_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.floor( 1)).isEqualTo(of(2));
        assertThat(tree.floor( 3)).isEqualTo(of(4));
        assertThat(tree.floor( 5)).isEqualTo(of(6));
        assertThat(tree.floor( 7)).isEqualTo(of(8));
        assertThat(tree.floor( 9)).isEqualTo(of(10));
        assertThat(tree.floor(11)).isEqualTo(of(12));
        assertThat(tree.floor(13)).isEqualTo(of(14));
        assertThat(tree.floor(15)).isEqualTo(of(16));
        assertThat(tree.floor(17)).isEqualTo(of(18));
        assertThat(tree.floor(19)).isEqualTo(of(20));
        assertThat(tree.floor(21)).isEqualTo(none());
    }

    // ceiling
    @Test
    public void givenDescendingTreeAndNonNaturalComparator_WhenCeiling_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value).descendingTree();
        assertThat(unit.ceiling(Tuple.of("one", null))).isEqualTo(some(value));
    }

    @Test
    public void givenDescendingTreeAndValueExists_WhenCeiling_ThenReturnSameValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.ceiling( 2)).isEqualTo(of(2));
        assertThat(tree.ceiling( 4)).isEqualTo(of(4));
        assertThat(tree.ceiling( 6)).isEqualTo(of(6));
        assertThat(tree.ceiling( 8)).isEqualTo(of(8));
        assertThat(tree.ceiling(10)).isEqualTo(of(10));
        assertThat(tree.ceiling(12)).isEqualTo(of(12));
        assertThat(tree.ceiling(14)).isEqualTo(of(14));
        assertThat(tree.ceiling(16)).isEqualTo(of(16));
        assertThat(tree.ceiling(18)).isEqualTo(of(18));
        assertThat(tree.ceiling(20)).isEqualTo(of(20));
    }

    @Test
    public void givenDescendingTreeAndValueDoesNotExist_WhenCeiling_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.ceiling( 1)).isEqualTo(none());
        assertThat(tree.ceiling( 3)).isEqualTo(of(2));
        assertThat(tree.ceiling( 5)).isEqualTo(of(4));
        assertThat(tree.ceiling( 7)).isEqualTo(of(6));
        assertThat(tree.ceiling( 9)).isEqualTo(of(8));
        assertThat(tree.ceiling(11)).isEqualTo(of(10));
        assertThat(tree.ceiling(13)).isEqualTo(of(12));
        assertThat(tree.ceiling(15)).isEqualTo(of(14));
        assertThat(tree.ceiling(17)).isEqualTo(of(16));
        assertThat(tree.ceiling(19)).isEqualTo(of(18));
        assertThat(tree.ceiling(21)).isEqualTo(of(20));
    }

    // higher
    @Test
    public void givenDescendingTreeAndNonNaturalComparator_WhenHigher_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value).descendingTree();
        assertThat(unit.higher(Tuple.of("zzPrevious", null))).isEqualTo(some(value));
    }

    @Test
    public void givenDescendingTreeAndValueExists_WhenHigher_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.higher( 2)).isEqualTo(none());
        assertThat(tree.higher( 4)).isEqualTo(of(2));
        assertThat(tree.higher( 6)).isEqualTo(of(4));
        assertThat(tree.higher( 8)).isEqualTo(of(6));
        assertThat(tree.higher(10)).isEqualTo(of(8));
        assertThat(tree.higher(12)).isEqualTo(of(10));
        assertThat(tree.higher(14)).isEqualTo(of(12));
        assertThat(tree.higher(16)).isEqualTo(of(14));
        assertThat(tree.higher(18)).isEqualTo(of(16));
        assertThat(tree.higher(20)).isEqualTo(of(18));
    }

    @Test
    public void givenDescendingTreeAndValueDoesNotExist_WhenHigher_ThenReturnHigherValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.higher( 1)).isEqualTo(none());
        assertThat(tree.higher( 3)).isEqualTo(of(2));
        assertThat(tree.higher( 5)).isEqualTo(of(4));
        assertThat(tree.higher( 7)).isEqualTo(of(6));
        assertThat(tree.higher( 9)).isEqualTo(of(8));
        assertThat(tree.higher(11)).isEqualTo(of(10));
        assertThat(tree.higher(13)).isEqualTo(of(12));
        assertThat(tree.higher(15)).isEqualTo(of(14));
        assertThat(tree.higher(17)).isEqualTo(of(16));
        assertThat(tree.higher(19)).isEqualTo(of(18));
        assertThat(tree.higher(21)).isEqualTo(of(20));
    }

    // lower

    @Test
    public void givenDescendingTreeAndNonNaturalComparator_WhenLower_ThenUseThatComparator() {
        final Comparator<Tuple2<String, Integer>> keyComparator = (k1, k2) -> k1._1.compareTo(k2._1);
        final Tuple2<String, Integer> value = Tuple.of("one", 1);
        final RedBlackTree<Tuple2<String, Integer>> unit = RedBlackTree.of(keyComparator, value).descendingTree();
        assertThat(unit.lower(Tuple.of("_later", null))).isEqualTo(some(value));
    }

    @Test
    public void givenDescendingTreeAndValueExists_WhenLower_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.lower( 2)).isEqualTo(of(4));
        assertThat(tree.lower( 4)).isEqualTo(of(6));
        assertThat(tree.lower( 6)).isEqualTo(of(8));
        assertThat(tree.lower( 8)).isEqualTo(of(10));
        assertThat(tree.lower(10)).isEqualTo(of(12));
        assertThat(tree.lower(12)).isEqualTo(of(14));
        assertThat(tree.lower(14)).isEqualTo(of(16));
        assertThat(tree.lower(16)).isEqualTo(of(18));
        assertThat(tree.lower(18)).isEqualTo(of(20));
        assertThat(tree.lower(20)).isEqualTo(none());
    }

    @Test
    public void givenDescendingTreeAndValueDoesNotExist_WhenLower_ThenReturnLowerValue() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.lower( 1)).isEqualTo(of(2));
        assertThat(tree.lower( 3)).isEqualTo(of(4));
        assertThat(tree.lower( 5)).isEqualTo(of(6));
        assertThat(tree.lower( 7)).isEqualTo(of(8));
        assertThat(tree.lower( 9)).isEqualTo(of(10));
        assertThat(tree.lower(11)).isEqualTo(of(12));
        assertThat(tree.lower(13)).isEqualTo(of(14));
        assertThat(tree.lower(15)).isEqualTo(of(16));
        assertThat(tree.lower(17)).isEqualTo(of(18));
        assertThat(tree.lower(19)).isEqualTo(of(20));
        assertThat(tree.lower(21)).isEqualTo(none());
    }

    // min / max

    @Test
    public void givenDescendingTree_ThenCanFindMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.min()).isEqualTo(of(20));
    }

    @Test
    public void givenDescendingTree_ThenCanFindMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of( 2, 4, 6, 8, 10, 12, 14, 16, 18, 20).descendingTree();
        assertThat(tree.max()).isEqualTo(of(2));
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  DESCENDING SUBTREE MODIFIERS (insert, delete)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree - Insert / Delete
    @Test
    public void givenDescendingSubTreeWithClosedRange_WhenInsertItemInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(of(10), true, of(4), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.insert(4).insert(5).insert(6).insert(9).insert(10);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(10, 9, 8, 6, 5, 4);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenDescendingSubTreeWithIsOpenEnded_WhenInsertItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(none(), true, of(4), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.insert(5).insert(7).insert(15).insert(33);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(33, 15, 10, 8, 7, 6, 5, 4);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenDescendingSubTree_WhenInsertItemsOutsideOfRange_ThenCreateNewTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(of(12), false, of(6), false);
        assertThat(subtree.insert(3).insert(1).iterator().toList()).containsExactly(10, 8, 3, 1);
        assertThat(subtree.insert(3).insert(1).isView()).isFalse();
        assertThat(subtree.insert(16).insert(18).iterator().toList()).containsExactly(18, 16, 10, 8);
        assertThat(subtree.insert(16).insert(18).isView()).isFalse();
        assertThat(subtree.insert(6).iterator().toList()).containsExactly(10, 8, 6);
        assertThat(subtree.insert(6).isView()).isFalse();
        assertThat(subtree.insert(12).iterator().toList()).containsExactly(12, 10, 8);
        assertThat(subtree.insert(12).isView()).isFalse();
    }

    @Test
    public void givenDescendingSubTreeWithClosedRange_WhenDeleteItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(of(10), true, of(4), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.delete(4).delete(5).delete(8).delete(12);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(10, 6);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenDescendingSubTreeWithIsOpenEnded_WhenDeleteItemsInsideRange_ThenKeepSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(none(), true, of(4), true);
        RedBlackTree<Integer> subTreeWithInserts = subtree.delete(8).delete(15).delete(33);
        assertThat(subTreeWithInserts.iterator().toList()).containsExactly(10, 6, 4);
        assertThat(subTreeWithInserts.isView()).isTrue();
    }

    @Test
    public void givenDescendingSubTree_WhenDeleteItemsOutsideRange_ThenDoNothing() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16);
        RedBlackTree<Integer> subtree = tree.descendingTree().subTree(of(14), false, of(6), false);
        assertThat(subtree.delete(1).delete(4).iterator().toList()).containsExactly(12, 10, 8);
        assertThat(subtree.delete(1).delete(4).isView()).isTrue();
        assertThat(subtree.delete(6).iterator().toList()).containsExactly(12, 10, 8);
        assertThat(subtree.delete(6).isView()).isTrue();
        assertThat(subtree.delete(14).delete(16).delete(18).iterator().toList()).containsExactly(12, 10, 8);
        assertThat(subtree.delete(14).delete(16).delete(18).isView()).isTrue();
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  DESCENDING SUBTREEs
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree a Descending Tree

    @Test
    public void givenFromStartAndToEndInclusiveRange_ThenCanCreateAndIterateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(of(15), true, of(1), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(14), true, of(2), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(13), true, of(3), true).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(12), true, of(4), true).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(10), true, of(6), true).iterator().toList()).containsExactly(10, 8, 6);
        assertThat(tree.subTree(of(4), true, of(2), true).iterator().toList()).containsExactly(4, 2);
        assertThat(tree.subTree(of(14), true, of(12), true).iterator().toList()).containsExactly(14, 12);
        assertThat(tree.subTree(of(8), true, of(8), true).iterator().toList()).containsExactly(8);
        assertThat(tree.subTree(of(3), true, of(3), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), true, of(1), true).iterator().toList()).isEmpty();

        for (int from = 15; from >=1; from--) {
            for (int to = from; to >=1; to--) {
                final List<Integer> expected = List.rangeClosed(to, from).filter(value -> value % 2 == 0).reverse();
                assertThat(tree.subTree(of(from), true, of(to), true))
                        .as(String.format("descendingTree().subTree(%d, true, %d, true)", from, to))
                        .containsExactlyElementsOf(expected);
            }
        }
    }

    @Test
    public void givenFromStartInclusiveAndToEndExclusiveRange_ThenCanCreateAndIterateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(of(15), true, of(1), false).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(14), true, of(2), false).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(13), true, of(3), false).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(12), true, of(4), false).iterator().toList()).containsExactly(12, 10, 8, 6);
        assertThat(tree.subTree(of(10), true, of(6), false).iterator().toList()).containsExactly(10, 8);
        assertThat(tree.subTree(of(4), true, of(2), false).iterator().toList()).containsExactly(4);
        assertThat(tree.subTree(of(14), true, of(12), false).iterator().toList()).containsExactly(14);
        assertThat(tree.subTree(of(8), true, of(8), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), true, of(3), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), true, of(1), false).iterator().toList()).isEmpty();
    }

    @Test
    public void givenFromStartExclusiveAndToEndInclusiveRange_ThenCanCreateAndIterateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(of(15), false, of(1), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(14), false, of(2), true).iterator().toList()).containsExactly(12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(13), false, of(3), true).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(12), false, of(4), true).iterator().toList()).containsExactly(10, 8, 6, 4);
        assertThat(tree.subTree(of(10), false, of(6), true).iterator().toList()).containsExactly(8, 6);
        assertThat(tree.subTree(of(4), false, of(2), true).iterator().toList()).containsExactly(2);
        assertThat(tree.subTree(of(14), false, of(12), true).iterator().toList()).containsExactly(12);
        assertThat(tree.subTree(of(8), false, of(8), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), false, of(3), true).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), false, of(1), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenFromStartAndToEndExclusiveRange_ThenCanCreateAndIterateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(of(15), false, of(1), false).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(14), false, of(2), false).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(13), false, of(3), false).iterator().toList()).containsExactly(12, 10, 8, 6, 4);
        assertThat(tree.subTree(of(12), false, of(4), false).iterator().toList()).containsExactly(10, 8, 6);
        assertThat(tree.subTree(of(10), false, of(6), false).iterator().toList()).containsExactly(8);
        assertThat(tree.subTree(of(4), false, of(2), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(14), false, of(12), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(8), false, of(8), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(3), false, of(3), false).iterator().toList()).isEmpty();
        assertThat(tree.subTree(of(1), false, of(1), false).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundedFromStartRange_ThenCanCreateAndIterateOverDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(none(), true, of(1), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(none(), true, of(2), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(none(), true, of(6), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6);
        assertThat(tree.subTree(none(), true, of(10), true).iterator().toList()).containsExactly(14, 12, 10);
        assertThat(tree.subTree(none(), true, of(15), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundedToEndRange_ThenCanCreateAndIterateOverDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(of(15), true, none(), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(10), true, none(), true).iterator().toList()).containsExactly(10, 8, 6, 4, 2);
        assertThat(tree.subTree(of(6), true, none(), true).iterator().toList()).containsExactly(6, 4, 2);
        assertThat(tree.subTree(of(2), true, none(), true).iterator().toList()).containsExactly(2);
        assertThat(tree.subTree(of(1), true, none(), true).iterator().toList()).isEmpty();
    }

    @Test
    public void givenUnboundRange_ThenCanCreateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThat(tree.subTree(none(), true, none(), true).iterator().toList()).containsExactly(14, 12, 10, 8, 6, 4, 2);

    }

    @Test
    public void givenFromStartGreaterThanToEndRange_ThenThrowsExceptionWhenCreateDescendingSubTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14).descendingTree();
        assertThatThrownBy(() -> tree.subTree(of(2), true, of(10), true)).isInstanceOf(IllegalArgumentException.class);
    }

    /* ****************************************************************************************************************
       ****************************************************************************************************************
       *
       *  DESCENDING SUBTREE NAVIGATORS (min, max, floor, ceiling, higher, lower)
       *
       ****************************************************************************************************************
       ****************************************************************************************************************/

    // SubTree - Min / Max
    @Test
    public void givenDescendingSubTree_ThenShouldReturnMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        assertThat(tree.subTree(of(26), true, of(6), true).max()).isEqualTo(of(6));
        assertThat(tree.subTree(of(26), true, of(14), true).max()).isEqualTo(of(14));
        assertThat(tree.subTree(of(26), true, of(13), true).max()).isEqualTo(of(14));
        assertThat(tree.subTree(of(26), true, of(3), true).max()).isEqualTo(of(4));
        assertThat(tree.subTree(of(26), true, of(2), true).max()).isEqualTo(of(2));
        assertThat(tree.subTree(of(26), true, of(0), true).max()).isEqualTo(of(2));
        assertThat(tree.subTree(of(26), true, none(), true).max()).isEqualTo(of(2));
        assertThat(tree.subTree(of(1), true, of(0), true).max()).isEqualTo(none());
        assertThat(tree.subTree(of(50), true, of(40), true).max()).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTreeWithToEndExclusive_ThenShouldReturnMax() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        assertThat(tree.subTree(of(26), true, of(6), false).max()).isEqualTo(of(8));
        assertThat(tree.subTree(of(26), true, of(14), false).max()).isEqualTo(of(16));
        assertThat(tree.subTree(of(26), true, of(13), false).max()).isEqualTo(of(14));
        assertThat(tree.subTree(of(26), true, of(15), false).max()).isEqualTo(of(16));
        assertThat(tree.subTree(of(26), true, of(3), false).max()).isEqualTo(of(4));
        assertThat(tree.subTree(of(26), true, of(2), false).max()).isEqualTo(of(4));
        assertThat(tree.subTree(of(26), true, of(0), false).max()).isEqualTo(of(2));
        assertThat(tree.subTree(of(26), true, none(), false).max()).isEqualTo(of(2));
        assertThat(tree.subTree(of(1), true, of(0), false).max()).isEqualTo(none());
        assertThat(tree.subTree(of(50), true, of(40), false).max()).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTree_ThenShouldReturnMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        assertThat(tree.subTree(of(26), true, of(6), true).min()).isEqualTo(of(26));
        assertThat(tree.subTree(of(20), true, of(10), true).min()).isEqualTo(of(20));
        assertThat(tree.subTree(of(25), true, of(7), true).min()).isEqualTo(of(24));
        assertThat(tree.subTree(of(40), true, of(7), true).min()).isEqualTo(of(30));
        assertThat(tree.subTree(none(), true, of(7), true).min()).isEqualTo(of(30));
        assertThat(tree.subTree(of(1), true, of(0), true).min()).isEqualTo(none());
        assertThat(tree.subTree(of(50), true, of(40), true).min()).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTreeWithFromStartExclusive_ThenShouldReturnMin() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        assertThat(tree.subTree(of(26), false, of(6), true).min()).isEqualTo(of(24));
        assertThat(tree.subTree(of(20), false, of(10), true).min()).isEqualTo(of(18));
        assertThat(tree.subTree(of(25), false, of(7), true).min()).isEqualTo(of(24));
        assertThat(tree.subTree(of(40), false, of(7), true).min()).isEqualTo(of(30));
        assertThat(tree.subTree(none(), false, of(7), true).min()).isEqualTo(of(30));
        assertThat(tree.subTree(of(1), false, of(0), true).min()).isEqualTo(none());
        assertThat(tree.subTree(of(50), false, of(40), true).min()).isEqualTo(none());
    }

    // SubTree - Floor / Ceiling / Higher/ Lower
    @Test
    public void givenDescendingSubTree_ThenShouldReturnFloor() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), true);
        assertThat(subtree.floor(1)).isEqualTo(of(6));
        assertThat(subtree.floor(2)).isEqualTo(of(6));
        assertThat(subtree.floor(4)).isEqualTo(of(6));
        assertThat(subtree.floor(6)).isEqualTo(of(6));
        assertThat(subtree.floor(7)).isEqualTo(of(8));
        assertThat(subtree.floor(8)).isEqualTo(of(8));
        assertThat(subtree.floor(9)).isEqualTo(of(10));
        assertThat(subtree.floor(10)).isEqualTo(of(10));
        assertThat(subtree.floor(11)).isEqualTo(of(12));
        assertThat(subtree.floor(24)).isEqualTo(of(24));
        assertThat(subtree.floor(25)).isEqualTo(of(26));
        assertThat(subtree.floor(26)).isEqualTo(of(26));
        assertThat(subtree.floor(27)).isEqualTo(none());
        assertThat(subtree.floor(28)).isEqualTo(none());
        assertThat(subtree.floor(31)).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTreeWithFromStartExclusive_ThenShouldReturnFloor() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), false, of(6), true);
        assertThat(subtree.floor(22)).isEqualTo(of(22));
        assertThat(subtree.floor(23)).isEqualTo(of(24));
        assertThat(subtree.floor(24)).isEqualTo(of(24));
        assertThat(subtree.floor(25)).isEqualTo(none());
        assertThat(subtree.floor(26)).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTree_ThenShouldReturnCeiling() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), true);
        assertThat(subtree.ceiling(1)).isEqualTo(none());
        assertThat(subtree.ceiling(2)).isEqualTo(none());
        assertThat(subtree.ceiling(4)).isEqualTo(none());
        assertThat(subtree.ceiling(6)).isEqualTo(of(6));
        assertThat(subtree.ceiling(7)).isEqualTo(of(6));
        assertThat(subtree.ceiling(8)).isEqualTo(of(8));
        assertThat(subtree.ceiling(9)).isEqualTo(of(8));
        assertThat(subtree.ceiling(10)).isEqualTo(of(10));
        assertThat(subtree.ceiling(11)).isEqualTo(of(10));
        assertThat(subtree.ceiling(24)).isEqualTo(of(24));
        assertThat(subtree.ceiling(25)).isEqualTo(of(24));
        assertThat(subtree.ceiling(26)).isEqualTo(of(26));
        assertThat(subtree.ceiling(27)).isEqualTo(of(26));
        assertThat(subtree.ceiling(28)).isEqualTo(of(26));
        assertThat(subtree.ceiling(31)).isEqualTo(of(26));
    }

    @Test
    public void givenDescendingSubTreeWithToEndExclusive_ThenShouldReturnCeiling() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), false);
        assertThat(subtree.ceiling(4)).isEqualTo(none());
        assertThat(subtree.ceiling(6)).isEqualTo(none());
        assertThat(subtree.ceiling(7)).isEqualTo(none());
        assertThat(subtree.ceiling(8)).isEqualTo(of(8));
        assertThat(subtree.ceiling(9)).isEqualTo(of(8));
    }

    @Test
    public void givenDescendingSubTree_ThenShouldReturnLower() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), true);
        assertThat(subtree.lower(1)).isEqualTo(of(6));
        assertThat(subtree.lower(2)).isEqualTo(of(6));
        assertThat(subtree.lower(4)).isEqualTo(of(6));
        assertThat(subtree.lower(6)).isEqualTo(of(8));
        assertThat(subtree.lower(7)).isEqualTo(of(8));
        assertThat(subtree.lower(8)).isEqualTo(of(10));
        assertThat(subtree.lower(9)).isEqualTo(of(10));
        assertThat(subtree.lower(10)).isEqualTo(of(12));
        assertThat(subtree.lower(11)).isEqualTo(of(12));
        assertThat(subtree.lower(24)).isEqualTo(of(26));
        assertThat(subtree.lower(25)).isEqualTo(of(26));
        assertThat(subtree.lower(26)).isEqualTo(none());
        assertThat(subtree.lower(27)).isEqualTo(none());
        assertThat(subtree.lower(28)).isEqualTo(none());
        assertThat(subtree.lower(31)).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTreeWithFromStartExclusive_ThenShouldReturnLower() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), false, of(6), true);
        assertThat(subtree.lower(22)).isEqualTo(of(24));
        assertThat(subtree.lower(23)).isEqualTo(of(24));
        assertThat(subtree.lower(24)).isEqualTo(none());
        assertThat(subtree.lower(25)).isEqualTo(none());
        assertThat(subtree.lower(26)).isEqualTo(none());
    }

    @Test
    public void givenDescendingSubTree_ThenShouldReturnHigher() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), true);
        assertThat(subtree.higher(1)).isEqualTo(none());
        assertThat(subtree.higher(2)).isEqualTo(none());
        assertThat(subtree.higher(4)).isEqualTo(none());
        assertThat(subtree.higher(6)).isEqualTo(none());
        assertThat(subtree.higher(7)).isEqualTo(of(6));
        assertThat(subtree.higher(8)).isEqualTo(of(6));
        assertThat(subtree.higher(9)).isEqualTo(of(8));
        assertThat(subtree.higher(10)).isEqualTo(of(8));
        assertThat(subtree.higher(11)).isEqualTo(of(10));
        assertThat(subtree.higher(24)).isEqualTo(of(22));
        assertThat(subtree.higher(25)).isEqualTo(of(24));
        assertThat(subtree.higher(26)).isEqualTo(of(24));
        assertThat(subtree.higher(27)).isEqualTo(of(26));
        assertThat(subtree.higher(28)).isEqualTo(of(26));
        assertThat(subtree.higher(31)).isEqualTo(of(26));
    }

    @Test
    public void givenDescendingSubTreeWithToEndExclusive_ThenShouldReturnHigher() {
        final RedBlackTree<Integer> tree = RedBlackTree.of(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30).descendingTree();
        RedBlackTree<Integer> subtree = tree.subTree(of(26), true, of(6), false);
        assertThat(subtree.higher(6)).isEqualTo(none());
        assertThat(subtree.higher(7)).isEqualTo(none());
        assertThat(subtree.higher(8)).isEqualTo(none());
        assertThat(subtree.higher(9)).isEqualTo(of(8));
        assertThat(subtree.higher(10)).isEqualTo(of(8));
    }

}
