/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ArbitraryTest {

    // -- apply

    @Test
    public void shouldApplyIntegerObject() {
        final Gen<BinaryTree<Integer>> gen = new ArbitraryBinaryTree(0, 0).apply(Integer.valueOf(0));
        assertThat(gen).isNotNull();
    }

    // -- flatMap

    @Test
    public void shouldFlatMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.flatMap(i -> new ArbitraryBinaryTree(-i, i));
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    // -- map

    @Test
    public void shouldMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.map(BinaryTree::leaf);
        assertThat(arbitraryTree.apply(0).apply(new Random())).isNotNull();
    }

    // -- filter

    @Test
    public void shouldFilterArbitrary() {
        final Arbitrary<Integer> ints = Arbitrary.integer();
        final Arbitrary<Integer> evenInts = ints.filter(i -> i % 2 == 0);
        assertThat(evenInts.apply(10).apply(new Random())).isNotNull();
    }

    // -- flatten()

    @Test
    public void shouldFlatteningArbitraryIntegerUsingFunction() {
        assertThat(Arbitrary.integer().flatten().apply(1).apply(new Random())).isNotNull();
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
        Arbitrary.integer().forEach(i -> {
            throw new RuntimeException("OK");
        });
    }

    // -- peek

    @Test
    public void shouldPeekArbitrary() {
        final int[] actual = new int[] { Integer.MIN_VALUE };
        final int expected = Arbitrary.integer().peek(i -> actual[0] = i).apply(10).apply(new Random());
        assertThat(actual[0]).isEqualTo(expected);
    }

    // -- isEmpty

    @Test
    public void shouldNotBeEmpty() {
        assertThat(Arbitrary.integer().isEmpty()).isFalse();
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
        final Arbitrary<List<Integer>> arbitrary = Arbitrary.list(Arbitrary.integer());
        final List<Integer> actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryStream() {
        final Arbitrary<Stream<Integer>> arbitrary = Arbitrary.stream(Arbitrary.integer());
        final Stream<Integer> actual = arbitrary.apply(10).apply(new Random());
        assertThat(actual).isNotNull();
    }

    // helpers

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

    interface BinaryTree<T> {

        static <T> Branch<T> branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
            return new Branch<>(left, value, right);
        }

        static <T> Branch<T> leaf(T value) {
            return new Branch<>(empty(), value, empty());
        }

        static <T> Empty<T> empty() {
            return Empty.instance();
        }

        class Branch<T> implements BinaryTree<T> {

            final BinaryTree<T> left;
            final T value;
            final BinaryTree<T> right;

            Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
                this.left = left;
                this.value = value;
                this.right = right;
            }
        }

        class Empty<T> implements BinaryTree<T> {

            private static final Empty<?> INSTANCE = new Empty<>();

            @SuppressWarnings("unchecked")
            static <T> Empty<T> instance() {
                return (Empty<T>) INSTANCE;
            }
        }
    }
}
