/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.collection.Tree;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

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
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = new ArbitraryBinaryTree(0, 1000);
        final Arbitrary<BinaryTree<Integer>> arbitraryTreeWithEvenNodeCount = arbitraryTree.filter(tree -> tree.nodeCount() % 3 == 0);
        assertThat(arbitraryTreeWithEvenNodeCount.apply(10).apply(new Random())).isNotNull();
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

    interface BinaryTree<T> extends Tree<T> {

        static <T> BinaryTree<T> of(BinaryTree<T> left, T value, BinaryTree<T> right) {
            Objects.requireNonNull(left, "left is null");
            Objects.requireNonNull(right, "right is null");
            if (left.isEmpty() && right.isEmpty()) {
                return new Leaf<>(value);
            } else {
                return new Branch<>(left, value, right);
            }
        }

        static <T> Branch<T> branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
            Objects.requireNonNull(left, "left is null");
            Objects.requireNonNull(right, "right is null");
            if (left.isEmpty() && right.isEmpty()) {
                throw new IllegalArgumentException("left and right are Nil - use BinaryTree.of(left, value, right) if in doubt.");
            }
            return new Branch<>(left, value, right);
        }

        static <T> Leaf<T> leaf(T value) {
            return new Leaf<>(value);
        }

        static <T> Nil<T> empty() {
            return Nil.instance();
        }

        @Override
        List<BinaryTree<T>> getChildren();

        BinaryTree<T> left();

        BinaryTree<T> right();

        @Override
        default <U> BinaryTree<U> map(Function<? super T, ? extends U> mapper) {
            if (isEmpty()) {
                return Nil.instance();
            } else {
                return BinaryTree.of(left().map(mapper), mapper.apply(getValue()), right().map(mapper));
            }
        }

        final class Leaf<T> implements BinaryTree<T> {

            private final T value;

            public Leaf(T value) {
                this.value = value;
            }

            @Override
            public BinaryTree<T> left() {
                return BinaryTree.empty();
            }

            @Override
            public BinaryTree<T> right() {
                return BinaryTree.empty();
            }

            @Override
            public T getValue() {
                return value;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean isLeaf() {
                return true;
            }

            @Override
            public List<BinaryTree<T>> getChildren() {
                return List.empty();
            }
        }

        final class Branch<T> implements BinaryTree<T> {

            private final BinaryTree<T> left;
            private final BinaryTree<T> right;
            private final T value;

            public Branch(BinaryTree<T> left, T value, BinaryTree<T> right) {
                Objects.requireNonNull(left, "left is null");
                Objects.requireNonNull(right, "right is null");
                if (left.isEmpty() && right.isEmpty()) {
                    throw new IllegalArgumentException("left and right are Nil - use Leaf instead of Branch");
                }
                this.left = left;
                this.right = right;
                this.value = value;
            }

            @Override
            public BinaryTree<T> left() {
                return left;
            }

            @Override
            public BinaryTree<T> right() {
                return right;
            }

            @Override
            public T getValue() {
                return value;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public List<BinaryTree<T>> getChildren() {
                return List.of(left, right).filter(tree -> !tree.isEmpty());
            }
        }

        final class Nil<T> implements BinaryTree<T> {

            private static final Nil<?> INSTANCE = new Nil<>();

            private Nil() {
            }

            public static <T> Nil<T> instance() {
                @SuppressWarnings("unchecked")
                final Nil<T> instance = (Nil<T>) INSTANCE;
                return instance;
            }

            @Override
            public BinaryTree<T> left() {
                throw new UnsupportedOperationException("left of Nil");
            }

            @Override
            public BinaryTree<T> right() {
                throw new UnsupportedOperationException("right of Nil");
            }

            @Override
            public T getValue() {
                throw new UnsupportedOperationException("getValue of Nil");
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }

            @Override
            public List<BinaryTree<T>> getChildren() {
                return List.empty();
            }
        }
    }
}
