/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.assertj.core.api.*;

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
    protected <T> Traversable<T> of(T element) {
        return HashSet.of(element);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> Traversable<T> of(T... elements) {
        return HashSet.of(elements);
    }

    @Override
    protected <T> Traversable<T> ofAll(Iterable<? extends T> elements) {
        return HashSet.ofAll(elements);
    }

    @Override
    protected Traversable<Boolean> ofAll(boolean[] array) {
        return null;
    }

    @Override
    protected Traversable<Byte> ofAll(byte[] array) {
        return null;
    }

    @Override
    protected Traversable<Character> ofAll(char[] array) {
        return null;
    }

    @Override
    protected Traversable<Double> ofAll(double[] array) {
        return null;
    }

    @Override
    protected Traversable<Float> ofAll(float[] array) {
        return null;
    }

    @Override
    protected Traversable<Integer> ofAll(int[] array) {
        return null;
    }

    @Override
    protected Traversable<Long> ofAll(long[] array) {
        return null;
    }

    @Override
    protected Traversable<Short> ofAll(short[] array) {
        return null;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- ignore
    // TODO

    @Override
    public void shouldFoldRightNonNil() {
        // ignore
    }

    @Override
    public void shouldReduceRightNonNil() {
        // ignore
    }

    @Override
    public void shouldJoinWithDelimiterNonNil() {
        // ignore
    }

    @Override
    public void shouldJoinWithDelimiterAndPrefixAndSuffixNonNil() {
        // ignore
    }


    @Override
    public void shouldBeAwareOfExistingNonUniqueElement() {
        // ignore
    }

    @Override
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingComparator() {
        // ignore
    }

    @Override
    public void shouldComputeDistinctByOfNonEmptyTraversableUsingKeyExtractor() {
        // ignore
    }

    @Override
    public void shouldFindLastOfNonNil() {
        // ignore
    }

    @Override
    public void shouldReplaceElementOfNonNilUsingCurrNew() {
        // ignore
    }

}
