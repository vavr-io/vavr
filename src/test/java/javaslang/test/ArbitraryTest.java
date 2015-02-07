/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.BinaryTree;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ArbitraryTest {

    @Test
    public void shouldApplyIntegerObject() {
        final Gen<BinaryTree<Integer>> gen = new ArbitraryBinaryTree(0, 0).apply(Integer.valueOf(0));
        assertThat(gen).isNotNull();
    }

    @Test
    public void shouldFlatMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.flatMap(i -> new ArbitraryBinaryTree(-i, i));
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    @Test
    public void shouldMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.map(BinaryTree::leaf);
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    @Test
    public void shouldFilterArbitrary() {
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = new ArbitraryBinaryTree(0, 1000);
        final Arbitrary<BinaryTree<Integer>> arbitraryTreeWithEvenNodeCount = arbitraryTree.filter(tree -> tree.nodeCount() % 2 == 0);
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    /**
     * Represents arbitrary binary trees of a certain depth n with values of type int.
     */
    static class ArbitraryBinaryTree implements Arbitrary<BinaryTree<Integer>> {

        final int minValue;
        final int maxValue;

        ArbitraryBinaryTree(int minValue, int maxValue) {
            this.minValue = Math.min(minValue, maxValue);
            this.maxValue = Math.max(minValue, maxValue);
        }

        @Override
        public Gen<BinaryTree<Integer>> apply(int n) {
            return random -> Gen.choose(minValue, maxValue).flatMap(value -> {
                        if (n == 0) {
                            return Gen.of(BinaryTree.leaf(value));
                        } else {
                            return Gen.frequency(
                                    Tuple.of(1, Gen.of(BinaryTree.leaf(value))),
                                    Tuple.of(4, Gen.of(BinaryTree.branch(apply(n / 2).apply(random), value, apply(n / 2).apply(random))))
                            );
                        }
                    }
            ).apply(random);
        }
    }
}
