/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.test;

import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArbitraryTest {

    // equally distributed random number generator
    private static final Random RANDOM = new Random();

    // predictable random number generator (seed = 1)
    private Random predictableRandom = new Random(1L);
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
        List.range(0, 1000)
                .map(i -> arbitrary.apply(RANDOM))
                .groupBy(Function1.identity())
                .forEach((key, value) -> assertThat(value.length())
                        .describedAs(key)
                        .isGreaterThan(1));
    }

    @Test
    public void shouldCreateDistinctArbitrary() {
        final Gen<String> distinctArbitrary = Arbitrary.string(Gen.choose('a', 'b')).distinct().apply(100);
        List.range(0, 1000)
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
        List.range(0, 10000)
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
        final Iterator<Stream<String>> generatedStringPairs = Stream.range(0, 10)
                .map(i -> arbitrary.apply(RANDOM))
                .grouped(2);
        for (Stream<String> stringPairs : generatedStringPairs) {
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

    @Test
    public void shouldCreateArbitraryLocalDateTime(){
        final Arbitrary<LocalDateTime> date = Arbitrary.localDateTime();

        assertThat(date).isNotNull();
    }


    @Test
    public void shouldNotAcceptNullMedianLocalDateTime(){
        assertThrows(NullPointerException.class, () -> Arbitrary.localDateTime(null, ChronoUnit.DAYS));
    }

    @Test
    public void shouldNotAcceptNullChronoUnit(){
        assertThrows(NullPointerException.class, () -> Arbitrary.localDateTime(LocalDateTime.now(), null));
    }

    @Test
    public void shouldCreateArbitraryLocalDateTimeAdjustedWithGivenChronoUnit(){
        final LocalDateTime median = LocalDateTime.of(2017, 2, 17, 3, 40);
        final Arbitrary<LocalDateTime> arbitrary = Arbitrary.localDateTime(median, ChronoUnit.YEARS);

        final LocalDateTime date = arbitrary.apply(100).apply(predictableRandom);

        assertThat(date).isEqualTo("2063-04-22T01:46:10.312");
    }

    @Test
    public void shouldCreateMedianLocalDateTimeIfSizeIsZero(){
        final LocalDateTime median = LocalDateTime.now();

        final Arbitrary<LocalDateTime> arbitrary = Arbitrary.localDateTime(median, ChronoUnit.DAYS);

        final LocalDateTime date = arbitrary.apply(0).apply(RANDOM);

        assertThat(date).isEqualTo(median);
    }

    @Test
    public void shouldCreateDatesInInRangeOfSize(){
        final LocalDateTime median = LocalDateTime.now();
        final Arbitrary<LocalDateTime> arbitrary = Arbitrary.localDateTime(median, ChronoUnit.DAYS);

        Property.def("With size of 100 days, dates should be in range of +/- 100 days")
                .forAll(arbitrary)
                .suchThat(d -> d.isAfter(median.minusDays(100)) && d.isBefore(median.plusDays(100)))
                .check(100, 1000);
    }

    @Test
    public void shouldIgnoreNegativeSignInRangeOfDates(){
        final LocalDateTime median = LocalDateTime.now();
        final Arbitrary<LocalDateTime> arbitrary = Arbitrary.localDateTime(median, ChronoUnit.DAYS);

        Property.def("With negative size of -100 days, dates should be in range of +/- 100 days")
                .forAll(arbitrary)
                .suchThat(d -> d.isAfter(median.minusDays(100)) && d.isBefore(median.plusDays(100)))
                .check(-100, 1000);
    }

    @Test
    public void shouldGenerateTwoDifferentSuccessiveDates(){
        final Arbitrary<LocalDateTime> dates = Arbitrary.localDateTime();
        final LocalDateTime firstDate = dates.apply(100).apply(RANDOM);
        final LocalDateTime secondDate = dates.apply(100).apply(RANDOM);

        assertThat(firstDate).isNotEqualTo(secondDate);
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
