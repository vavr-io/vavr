/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import org.assertj.core.api.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collector;

import static org.junit.Assert.assertTrue;

public class HashSetTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(java.lang.Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                java.lang.Iterable<T> expected = (java.lang.Iterable<T>) obj;
                java.util.Map<T, Integer> actualMap = countMap(actual);
                java.util.Map<T, Integer> expectedMap = countMap(expected);
                assertThat(actualMap.size()).isEqualTo(expectedMap.size());
                actualMap.keySet().forEach(k -> assertThat(actualMap.get(k)).isEqualTo(expectedMap.get(k)));
                return this;
            }

            private java.util.Map<T, Integer> countMap(java.lang.Iterable<? extends T> it) {
                java.util.HashMap<T, Integer> cnt = new java.util.HashMap<>();
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
    protected <T> Collector<T, ArrayList<T>, Set<T>> collector() {
        return HashSet.collector();
    }

    @Override
    protected <T> HashSet<T> empty() {
        return HashSet.empty();
    }

    @Override
    protected <T> HashSet<T> of(T element) {
        return HashSet.of(element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> HashSet<T> of(T... elements) {
        return HashSet.of(elements);
    }

    @Override
    protected <T> HashSet<T> ofAll(java.lang.Iterable<? extends T> elements) {
        return HashSet.ofAll(elements);
    }

    @Override
    protected HashSet<Boolean> ofAll(boolean[] array) {
        return null;
    }

    @Override
    protected HashSet<Byte> ofAll(byte[] array) {
        return null;
    }

    @Override
    protected HashSet<Character> ofAll(char[] array) {
        return null;
    }

    @Override
    protected HashSet<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected HashSet<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected HashSet<Integer> ofAll(int[] array) {
        return null;
    }

    @Override
    protected HashSet<Long> ofAll(long[] array) {
        return null;
    }

    @Override
    protected HashSet<Short> ofAll(short[] array) {
        return null;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // TODO move to traversable
    // -- zip

    @Test
    public void shouldZipNils() {
        final HashSet<Tuple2<Object, Object>> actual = empty().zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final HashSet<Tuple2<Object, Integer>> actual = empty().zip(of(1));
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final HashSet<Tuple2<Integer, Integer>> actual = of(1).zip(empty());
        assertThat(actual).isEqualTo(empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b"));
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2, 3).zip(of("a", "b", "c"));
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
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
        final HashSet<?> actual = of(1).zipAll(empty(), null, null);
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, Object>> expected = of(Tuple.of(1, null));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(9, "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b"), 9, "z");
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final HashSet<Tuple2<Integer, String>> actual = of(1, 2, 3).zipAll(of("a", "b", "c"), 9, "z");
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<Integer, String>> expected = of(Tuple.of(1, "a"), Tuple.of(2, "b"), Tuple.of(3, "c"));
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
        assertThat(this.<String> empty().zipWithIndex()).isEqualTo(this.<Tuple2<String, Integer>> empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final HashSet<Tuple2<String, Integer>> actual = of("a", "b", "c").zipWithIndex();
        @SuppressWarnings("unchecked")
        final HashSet<Tuple2<String, Integer>> expected = of(Tuple.of("a", 0), Tuple.of("b", 1), Tuple.of("c", 2));
        assertThat(actual).isEqualTo(expected);
    }

    // HashSet special cases

    @Test
    public void shouldDropRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).dropRight(2)).isEqualTo(of(3));
    }

    @Test
    public void shouldTakeRightAsExpectedIfCountIsLessThanSize() {
        assertThat(of(1, 2, 3).takeRight(2)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldGetInitOfNonNil() {
        assertThat(of(1, 2, 3).init()).isEqualTo(of(2, 3));
    }

    @Override
    public void shouldFoldRightNonNil() {
        // TODO
//        String actual = of("a", "b", "c").foldRight("", (x, xs) -> x + xs);
//        assertThat(List.of("abc", "acb", "bac", "bca", "cab", "cba")).contains(actual);
    }

    @Override
    public void shouldReduceRightNonNil() {
        // TODO
//        String actual = of("a", "b", "c").reduceRight((x, xs) -> x + xs);
//        assertThat(List.of("abc", "acb", "bac", "bca", "cab", "cba")).contains(actual);
    }

    @Override
    public void shouldMkStringWithDelimiterNonNil() {
        String actual = of('a', 'b', 'c').mkString(",");
        assertThat(List.of("a,b,c", "a,c,b", "b,a,c", "b,c,a", "c,a,b", "c,b,a")).contains(actual);
    }

    @Override
    public void shouldMkStringWithDelimiterAndPrefixAndSuffixNonNil() {
        String actual = of('a', 'b', 'c').mkString(",", "[", "]");
        assertThat(List.of("[a,b,c]", "[a,c,b]", "[b,a,c]", "[b,c,a]", "[c,a,b]", "[c,b,a]")).contains(actual);
    }

    @Override
    public void shouldBeAwareOfExistingNonUniqueElement() {
        // TODO
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
        // TODO
//        int actual = of(1, 2, 3, 4).findLast(i -> i % 2 == 0).get();
//        assertThat(List.of(1, 2, 3, 4)).contains(actual);
    }

    @Override
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        // TODO
    }

    @Override
    public void shouldThrowWhenFoldRightNullOperator() {
        throw new NullPointerException(); // TODO
    }

    @Override
    public void shouldFoldRightNil() {
        // TODO
    }

    @Override
    public void shouldThrowWhenReduceRightNullOperator() {
        throw new NullPointerException(); // TODO
    }

    @Override
    public void shouldThrowWhenReduceRightNil() {
        throw new NoSuchElementException(); // TODO
    }

    @Override
    public void shouldFindLastOfNil() {
        // TODO
    }

    @Override
    public void shouldSlide4ElementsBySize5AndStep3() {
        // TODO
    }

    @Override
    public void shouldSlide5ElementsBySize2AndStep3() {
        // TODO
    }

    @Override
    public void shouldSlide5ElementsBySize2AndStep4() {
        // TODO
    }

    @Override
    public void shouldSlide5ElementsBySize2AndStep5() {
        // TODO
    }

    @Override
    public void shouldSerializeDeserializeNonNil() {
        // TODO
    }

    @Override
    public void shouldPartitionIntsInOddAndEvenHavingOddAndEventNumbers() {
        // TODO
    }

    @Override
    public void shouldPartitionIntsInOddAndEvenHavingOnlyEvenNumbers() {
        // TODO
    }

    @Override
    public void shouldSpanNonNil() {
// TODO
    }

    @Override
    public void shouldReturnSomeInitWhenCallingInitOptionOnNonNil() {
// TODO
    }

    @Override
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
// TODO
    }

    @Override
    public void shouldRecognizeEqualityOfNonNils() {
// TODO
    }

    @Override
    public void shouldPartitionIntsInOddAndEvenHavingOnlyOddNumbers() {
// TODO
    }

    @Override
    public void shouldSlideNonNilBySize1() {
// TODO
    }

    @Override
    public void shouldSlideNonNilBySize2() {
// TODO
    }

    @Test
    public void shouldBeEqual() {
        assertTrue(HashSet.of(1).equals(HashSet.of(1)));
    }

    boolean isThisLazyCollection() {
        return false;
    }
}
