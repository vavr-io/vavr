/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2022 Vavr, https://vavr.io
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
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.api.DoubleAssert;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.api.IterableAssert;
import org.assertj.core.api.LongAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.StringAssert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

public class ChampSetTest extends AbstractSetTest {

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked") final Iterable<T> expected = (Iterable<T>) obj;
                final java.util.Map<T, Integer> actualMap = countMap(actual);
                final java.util.Map<T, Integer> expectedMap = countMap(expected);
                assertThat(actualMap.size()).isEqualTo(expectedMap.size());
                actualMap.keySet().forEach(k -> assertThat(actualMap.get(k)).isEqualTo(expectedMap.get(k)));
                return this;
            }

            private java.util.Map<T, Integer> countMap(Iterable<? extends T> it) {
                final java.util.HashMap<T, Integer> cnt = new java.util.HashMap<>();
                it.forEach(i -> cnt.merge(i, 1, (v1, v2) -> v1 + v2));
                return cnt;
            }
        };
    }

    @Override
    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {
        };
    }

    @Override
    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {
        };
    }

    @Override
    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {
        };
    }

    @Override
    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {
        };
    }

    @Override
    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {
        };
    }

    @Override
    protected StringAssert assertThat(String actual) {
        return new StringAssert(actual) {
        };
    }

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, ChampSet<T>> collector() {
        return ChampSet.collector();
    }

    @Override
    protected <T> ChampSet<T> empty() {
        return ChampSet.empty();
    }

    @Override
    protected <T> ChampSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> ChampSet<T> of(T element) {
        return ChampSet.<T>empty().add(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> ChampSet<T> of(T... elements) {
        return ChampSet.of(elements);
    }

    @Override
    protected <T> ChampSet<T> ofAll(Iterable<? extends T> elements) {
        return ChampSet.<T>empty().addAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> ChampSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return ChampSet.<T>empty().addAll(javaStream.collect(Collectors.toList()));
    }

    @Override
    protected ChampSet<Boolean> ofAll(boolean... elements) {
        return ChampSet.<Boolean>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Byte> ofAll(byte... elements) {
        return ChampSet.<Byte>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Character> ofAll(char... elements) {
        return ChampSet.<Character>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Double> ofAll(double... elements) {
        return ChampSet.<Double>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Float> ofAll(float... elements) {
        return ChampSet.<Float>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Integer> ofAll(int... elements) {
        return ChampSet.<Integer>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Long> ofAll(long... elements) {
        return ChampSet.<Long>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampSet<Short> ofAll(short... elements) {
        return ChampSet.<Short>empty().addAll(Iterator.ofAll(elements));
    }

    @Override
    protected <T> ChampSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Collections.tabulate(n, f, ChampSet.empty(), ChampSet::of);
    }

    @Override
    protected <T> ChampSet<T> fill(int n, Supplier<? extends T> s) {
        return Collections.fill(n, s, ChampSet.empty(), ChampSet::of);
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- static narrow

    @Test
    public void shouldNarrowHashSet() {
        final ChampSet<Double> doubles = of(1.0d);
        final ChampSet<Number> numbers = ChampSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- slideBy is not expected to work for larger subsequences, due to unspecified iteration order
    @Test
    public void shouldSlideNonNilBySomeClassifier() {
        // ignore
    }

    // TODO move to traversable
    // -- zip

    @Test
    public void shouldZipNils() {
        final Set<Tuple2<Object, Object>> actual = empty().zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Set<Tuple2<Object, Integer>> actual = empty().zip(of(1));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Set<Tuple2<Integer, Integer>> actual = of(1).zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2).zip(of("a", "b", "c"));
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b"));
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b", "c"));
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipWithThatIsNull() {
        empty().zip(null);
    }

    // TODO move to traversable
    // -- zipAll

    @Test
    public void shouldZipAllNils() {
        // ignore
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        // ignore
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Set<?> actual = of(1).zipAll(empty(), null, null);
        final Set<Tuple2<Integer, Object>> expected = of(Tuple.of(1, null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2).zipAll(of("a", "b", "c"), 9, "z");
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b"), 9, "z");
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final Set<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b", "c"), 9, "z");
        final Set<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        empty().zipAll(null, null, null);
    }

    // TODO move to traversable
    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(this.<String>empty().zipWithIndex()).isEqualTo(this.<Tuple2<String, Integer>>empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final Set<Tuple2<String, Integer>> actual = of("a", "b", "c").zipWithIndex();
        final Set<Tuple2<String, Integer>> expected = of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        final String transformed = of(42).transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("42");
    }

    // ChampSet special cases

    @Override
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).dropRight(2)).isEqualTo(of(3));
    }

    @Override
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).takeRight(2)).isEqualTo(of(1, 2));
    }

    @Override
    public void shouldGetInitOfNonNil() {
        assertThat(of(1, 2, 3).init()).isEqualTo(of(2, 3));
    }

    @Override
    public void shouldFoldRightNonNil() {
        final String actual = of('a', 'b', 'c').foldRight("", (x, xs) -> x + xs);
        final List<String> expected = List.of('a', 'b', 'c').permutations().map(List::mkString);
        assertThat(actual).isIn(expected);
    }

    @Override
    public void shouldReduceRightNonNil() {
        final String actual = of("a", "b", "c").reduceRight((x, xs) -> x + xs);
        final List<String> expected = List.of("a", "b", "c").permutations().map(List::mkString);
        assertThat(actual).isIn(expected);
    }

    @Override
    public void shouldMkStringWithDelimiterNonNil() {
        final String actual = of('a', 'b', 'c').mkString(",");
        final List<String> expected = List.of('a', 'b', 'c').permutations().map(l -> l.mkString(","));
        assertThat(actual).isIn(expected);
    }

    @Override
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        final String actual = of('a', 'b', 'c').mkString("[", ",", "]");
        final List<String> expected = List.of('a', 'b', 'c').permutations().map(l -> l.mkString("[", ",", "]"));
        assertThat(actual).isIn(expected);
    }

    @Override
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        // TODO
    }

    @Override
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        // TODO
    }

    @Override
    public void shouldFindLastOfNonNil() {
        final int actual = of(1, 2, 3, 4).findLast(i -> i % 2 == 0).get();
        assertThat(actual).isIn(List.of(1, 2, 3, 4));
    }

    @Override
    public void shouldThrowWhenFoldRightNullOperator() {
        throw new NullPointerException(); // TODO
    }

    @Override
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
        // TODO
    }

    @Test
    public void shouldBeEqual() {
        assertTrue(ChampSet.<Integer>empty().add(1).equals(ChampSet.<Integer>empty().add(1)));
    }

    //fixme: delete, when useIsEqualToInsteadOfIsSameAs() will be eliminated from AbstractValueTest class
    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected ChampSet<Character> range(char from, char toExclusive) {
        return ChampSet.<Character>empty().addAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampSet<Character> rangeBy(char from, char toExclusive, int step) {
        return ChampSet.<Character>empty().addAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampSet<Double> rangeBy(double from, double toExclusive, double step) {
        return ChampSet.<Double>empty().addAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampSet<Integer> range(int from, int toExclusive) {
        return ChampSet.<Integer>empty().addAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return ChampSet.<Integer>empty().addAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampSet<Long> range(long from, long toExclusive) {
        return ChampSet.<Long>empty().addAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampSet<Long> rangeBy(long from, long toExclusive, long step) {
        return ChampSet.<Long>empty().addAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampSet<Character> rangeClosed(char from, char toInclusive) {
        return ChampSet.<Character>empty().addAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ChampSet.<Character>empty().addAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return ChampSet.<Double>empty().addAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampSet<Integer> rangeClosed(int from, int toInclusive) {
        return ChampSet.<Integer>empty().addAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return ChampSet.<Integer>empty().addAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampSet<Long> rangeClosed(long from, long toInclusive) {
        return ChampSet.<Long>empty().addAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return ChampSet.<Long>empty().addAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    // -- toSet

    @Test
    public void shouldReturnSelfOnConvertToSet() {
        final ChampSet<Integer> value = of(1, 2, 3);
        assertThat(value.toSet()).isSameAs(value);
    }

    // -- spliterator

    @Test
    public void shouldNotHaveSortedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SORTED)).isFalse();
    }

    @Test
    public void shouldNotHaveOrderedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.ORDERED)).isFalse();
    }

    // -- isSequential()

    @Test
    public void shouldReturnFalseWhenIsSequentialCalled() {
        assertThat(of(1, 2, 3).isSequential()).isFalse();
    }

}
