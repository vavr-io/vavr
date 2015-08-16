/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.assertj.core.api.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collector;

public class HashSetTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                Iterable<T> expected = (Iterable<T>) obj;
                java.util.Map<T, Integer> actualMap = countMap(actual);
                java.util.Map<T, Integer> expectedMap = countMap(expected);
                assertThat(actualMap.size()).isEqualTo(expectedMap.size());
                actualMap.keySet().forEach(k -> assertThat(actualMap.get(k)).isEqualTo(expectedMap.get(k)));
                return this;
            }

            private java.util.Map<T, Integer> countMap(Iterable<? extends T> it) {
                java.util.HashMap<T, Integer> cnt = new java.util.HashMap<>();
                it.forEach(i -> cnt.merge(i, 1, (v1, v2) -> v1 + v2));
                return cnt;
            }
        };
    }

    @Override
    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {};
    }

    @Override
    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {};
    }

    @Override
    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {};
    }

    @Override
    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {};
    }

    @Override
    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {};
    }

    @Override
    protected StringAssert assertThat(String actual) {
        return new StringAssert(actual) {};
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
    protected <T> HashSet<T> ofAll(Iterable<? extends T> elements) {
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

    // HashSet special cases

    @Test
    public void shouldRemoveFirstElementByPredicateBeginM() {
        assertThat(of(1, 2, 1, 3).removeFirst(v -> v == 1)).isEqualTo(of(2, 3));
    }

    @Test
    public void shouldRemoveFirstElementByPredicateInnerM() {
        assertThat(of(1, 2, 3, 2, 5).removeFirst(v -> v == 2)).isEqualTo(of(1, 3, 5));
    }

    @Test
    public void shouldRemoveLastElementByPredicateEndM() {
        assertThat(of(1, 3, 2, 3).removeLast(v -> v == 3)).isEqualTo(of(1, 2));
    }

    @Test
    public void shouldRemoveLastElementByPredicateInnerM() {
        assertThat(of(1, 2, 3, 2, 5).removeLast(v -> v == 2)).isEqualTo(of(1, 3, 5));
    }

    @Override
    public void shouldFoldRightNonNil() {
        String actual = of("a", "b", "c").foldRight("", (x, xs) -> x + xs);
        assertThat(List.of("abc", "acb", "bac", "bca", "cab", "cba")).contains(actual);
    }

    @Override
    public void shouldReduceRightNonNil() {
        String actual = of("a", "b", "c").reduceRight((x, xs) -> x + xs);
        assertThat(List.of("abc", "acb", "bac", "bca", "cab", "cba")).contains(actual);
    }

    @Override
    public void shouldJoinWithDelimiterNonNil() {
        String actual = of('a', 'b', 'c').join(",");
        assertThat(List.of("a,b,c", "a,c,b", "b,a,c", "b,c,a", "c,a,b", "c,b,a")).contains(actual);
    }

    @Override
    public void shouldJoinWithDelimiterAndPrefixAndSuffixNonNil() {
        String actual = of('a', 'b', 'c').join(",", "[", "]");
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
        int actual = of(1, 2, 3, 4).findLast(i -> i % 2 == 0).get();
        assertThat(List.of(1, 2, 3, 4)).contains(actual);
    }

    @Override
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        // TODO
    }

    @Override
    boolean isThisLazyCollection() {
        return false;
    }

}
