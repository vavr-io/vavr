/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Function1;
import javaslang.Tuple;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import java.util.Comparator;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ArbitraryTest {

    // equally distributed random number generator
    static final Random RANDOM = new Random();

    // -- apply

    @Test
    public void shouldApplyIntegerObject() {
        final Gen<BinaryTree<Integer>> gen = new ArbitraryBinaryTree(0, 0).apply(0);
        assertThat(gen).isNotNull();
    }

    // -- flatMap

    @Test
    public void shouldFlatMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.flatMap(i -> new ArbitraryBinaryTree(-i, i));
        assertThat(arbitraryTree.apply(0).apply(RANDOM)).isNotNull();
    }

    // -- map

    @Test
    public void shouldMapArbitrary() {
        final Arbitrary<Integer> arbitraryInt = size -> Gen.choose(-size, size);
        final Arbitrary<BinaryTree<Integer>> arbitraryTree = arbitraryInt.map(BinaryTree::leaf);
        assertThat(arbitraryTree.apply(0).apply(RANDOM)).isNotNull();
    }

    // -- filter

    @Test
    public void shouldFilterArbitrary() {
        final Arbitrary<Integer> ints = Arbitrary.integer();
        final Arbitrary<Integer> evenInts = ints.filter(i -> i % 2 == 0);
        assertThat(evenInts.apply(10).apply(RANDOM)).isNotNull();
    }

    // -- peek

    @Test
    public void shouldPeekArbitrary() {
        final int[] actual = new int[] { Integer.MIN_VALUE };
        final int expected = Arbitrary.integer().peek(i -> actual[0] = i).apply(10).apply(RANDOM);
        assertThat(actual[0]).isEqualTo(expected);
    }

    // factory methods

    @Test
    public void shouldCreateArbitraryInteger() {
        final Arbitrary<Integer> arbitrary = Arbitrary.integer();
        final Integer actual = arbitrary.apply(10).apply(RANDOM);
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryString() {
        final Arbitrary<String> arbitrary = Arbitrary.string(Gen.choose('a', 'z'));
        final String actual = arbitrary.apply(10).apply(RANDOM);
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryList() {
        final Arbitrary<List<Integer>> arbitrary = Arbitrary.list(Arbitrary.integer());
        final List<Integer> actual = arbitrary.apply(10).apply(RANDOM);
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateArbitraryStream() {
        final Arbitrary<Stream<Integer>> arbitrary = Arbitrary.stream(Arbitrary.integer());
        final Stream<Integer> actual = arbitrary.apply(10).apply(RANDOM);
        assertThat(actual).isNotNull();
    }

    @Test
    public void shouldCreateFixedContentArbitrary() {
        final Gen<String> arbitrary = Arbitrary.of("test", "content").apply(10);
        for (int i = 0; i < 100; i++) {
            assertThat(arbitrary.apply(RANDOM)).isIn("test", "content");
        }
    }

    @Test
    public void shouldCreateNonDistinctArbitrary() {
        final Gen<String> arbitrary = Arbitrary.string(Gen.choose('a', 'b')).apply(2);

        Stream.range(0, 100)
              .toList()
              .map(i -> arbitrary.apply(RANDOM))
              .groupBy(Function1.identity())
              .forEach((key, value) -> assertThat(value.length())
                  .describedAs(key)
                  .isGreaterThan(1));
    }

    @Test
    public void shouldCreateDistinctArbitrary() {

        final Gen<String> distinctArbitrary = Arbitrary.string(Gen.choose('a', 'b')).distinct().apply(100);

        Stream.range(0, 10000)
              .toList()
              .map(i -> distinctArbitrary.apply(RANDOM))
              .groupBy(Function1.identity())
              .forEach((key, value) -> assertThat(value.length())
                  .describedAs(key)
                  .isEqualTo(1));

    }

    @Test
    public void shouldCreateDistinctByArbitrary() {

        final Gen<String> distinctByArbitrary = Arbitrary.string(Gen.choose('a', 'b'))
                                                         .distinctBy(Comparator.naturalOrder()).apply(100);

        Stream.range(0, 10000)
              .toList()
              .map(i -> distinctByArbitrary.apply(RANDOM))
              .groupBy(Function1.identity())
              .forEach((key, value) -> assertThat(value.length())
                  .describedAs(key)
                  .isEqualTo(1));

    }

    @Test
    public void shouldCreateInterspersedFixedContentArbitrary() {
        final Gen<String> arbitrary = Arbitrary.of("test")
                                               .intersperse(Arbitrary.of("content"))
                                               .apply(10);

        for (int i = 0; i < 100; i++) {
            assertThat(arbitrary.apply(RANDOM)).isIn("test", "content");
        }
    }

    @Test
    public void shouldCreateInterspersedFixedContentArbitraryWithConstantOrder() {

        final Gen<String> arbitrary = Arbitrary.of("test")
                                               .intersperse(Arbitrary.of("content"))
                                               .apply(10);

        Iterator<Stream<String>> generatedStringPairss = Stream.range(0, 10)
                                                               .map(i -> arbitrary.apply(RANDOM))
                                                               .grouped(2);

        for (Stream<String> stringPairs : generatedStringPairss) {
            assertThat(stringPairs.mkString(",")).isEqualTo("test,content");
        }

    }

    @Test
    public void shouldCreateCharArrayArbitrary() {
        final Gen<String> arbitrary = Arbitrary.string(Gen.choose("test".toCharArray()))
                                               .filter(s -> !"".equals(s))
                                               .apply(1);

        for (int i = 0; i < 100; i++) {
            assertThat(arbitrary.apply(RANDOM)).isIn("t", "e", "s");
        }
    }

    @Test
    public void shouldCreateArbitraryStreamAndEvaluateAllElements() {
        final Arbitrary<Stream<Integer>> arbitrary = Arbitrary.stream(Arbitrary.integer());
        final Stream<Integer> actual = arbitrary.apply(10).apply(new Random() {
            private static final long serialVersionUID = 1L;
            @Override
            public int nextInt(int bound) {
                return bound - 1;
            }
        });
        assertThat(actual.length()).isEqualTo(10);
    }

    // -- transform

    @Test
    public void shouldTransformArbitrary() {
        final Arbitrary<Integer> arbitrary = ignored -> Gen.of(1);
        final String s = arbitrary.transform(a -> a.apply(0).apply(RANDOM).toString());
        assertThat(s).isEqualTo("1");
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
