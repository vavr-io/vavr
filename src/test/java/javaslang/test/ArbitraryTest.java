/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.JBinaryTree;
import javaslang.collection.JList;
import javaslang.collection.JStream;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ArbitraryTest {

    // -- apply

    @Test
    public void shouldApplyIntegerObject() {
        final Gen<JBinaryTree<Integer>> gen = new ArbitraryBinaryTree(0, 0).apply(Integer.valueOf(0));
        assertThat(gen).isNotNull();
    }

    // -- flatMap

    @Test
    public void shouldFlatMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<JBinaryTree<Integer>> arbitraryTree = arbitraryInt.flatMap(i -> new ArbitraryBinaryTree(-i, i));
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    // -- map

    @Test
    public void shouldMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<JBinaryTree<Integer>> arbitraryTree = arbitraryInt.map(JBinaryTree::leaf);
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    // -- filter

    @Test
    public void shouldFilterArbitrary() {
        final Arbitrary<JBinaryTree<Integer>> arbitraryTree = new ArbitraryBinaryTree(0, 1000);
        final Arbitrary<JBinaryTree<Integer>> arbitraryTreeWithEvenNodeCount = arbitraryTree.filter(tree -> tree.nodeCount() % 3 == 0);
        assertThat(arbitraryTreeWithEvenNodeCount.apply(10).apply(new Random())).isNotNull();
    }

    // -- flatten(Function)

    @Test
    public void shouldFlatteningArbitraryIntegerUsingFunction() {
        assertThat(Arbitrary.integer().flatten(i -> Gen.of(i).arbitrary()).apply(1).apply(new Random())).isNotNull();
    }

    // -- exists

    @Test
    public void shouldThrowOnExists() {
        Arbitrary.integer().exists(i -> true);
    }

    // -- forAll

    @Test
    public void shouldThrowOnForAll() {
        Arbitrary.integer().forAll(i -> false);
    }

    // -- forEach

    @Test(expected = RuntimeException.class)
    public void shouldThrowOnForEach() {
        Arbitrary.integer().forEach(i -> { throw new RuntimeException("OK"); });
    }

    // -- peek

    @Test
    public void shouldPeekArbitrary() {
        final int[] actual = new int[] { Integer.MIN_VALUE };
        final int expected = Arbitrary.integer().peek(i -> actual[0] = i).apply(10).apply(new Random());
        assertThat(actual[0]).isEqualTo(expected);
    }

    // factory methods

    @Test
    public void shouldCreateArbitraryInteger() {
        final Arbitrary<Integer> arbitrary = Arbitrary.integer();
        final Integer actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryString() {
        final Arbitrary<String> arbitrary = Arbitrary.string(Gen.choose('a', 'z'));
        final String actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryList() {
        final Arbitrary<JList<Integer>> arbitrary = Arbitrary.list(Arbitrary.integer());
        final JList<Integer> actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryStream() {
        final Arbitrary<JStream<Integer>> arbitrary = Arbitrary.stream(Arbitrary.integer());
        final JStream<Integer> actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    // helpers

    /**
     * Represents arbitrary binary trees of a certain depth n with values of type int.
     */
    static class ArbitraryBinaryTree implements Arbitrary<JBinaryTree<Integer>> {

        private static final long serialVersionUID = 1L;

        final int minValue;
        final int maxValue;

        ArbitraryBinaryTree(int minValue, int maxValue) {
            this.minValue = Math.min(minValue, maxValue);
            this.maxValue = Math.max(minValue, maxValue);
        }

        @Override
        public Gen<JBinaryTree<Integer>> apply(int n) {
            return random -> Gen.choose(minValue, maxValue).flatMap(value -> {
                        if (n == 0) {
                            return Gen.of(JBinaryTree.leaf(value));
                        } else {
                            return Gen.frequency(
                                    Tuple.of(1, Gen.of(JBinaryTree.leaf(value))),
                                    Tuple.of(4, Gen.of(JBinaryTree.branch(apply(n / 2).apply(random), value, apply(n / 2).apply(random))))
                            );
                        }
                    }
            ).apply(random);
        }
    }
}
