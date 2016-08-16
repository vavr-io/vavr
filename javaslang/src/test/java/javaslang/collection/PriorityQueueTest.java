/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Value;
import javaslang.collection.Comparators.SerializableComparator;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.stream.Stream;

import static java.lang.Integer.bitCount;
import static java.util.stream.Collectors.toList;
import static javaslang.collection.Comparators.naturalComparator;

public class PriorityQueueTest extends AbstractTraversableTest {
    private final List<Integer> values = List.of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6, 4, 3, 3, 8, 3, 2, 7, 9, 5, 0, 2, 8, 8, 4, 1, 9, 7, 1, 6, 9, 3, 9, 9, 3, 7, 5, 1, 0);

    @Override
    protected <T> Collector<T, ArrayList<T>, PriorityQueue<T>> collector() {
        return PriorityQueue.collector();
    }

    @Override
    protected <T> PriorityQueue<T> empty() {
        return PriorityQueue.empty(toStringComparator());
    }

    @Override
    protected <T> PriorityQueue<T> of(T element) {
        return PriorityQueue.ofAll(toStringComparator(), List.of(element));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final <T> PriorityQueue<T> of(T... elements) {
        return PriorityQueue.ofAll(toStringComparator(), List.of(elements));
    }

    @Override
    protected <T> PriorityQueue<T> ofAll(Iterable<? extends T> elements) {
        return PriorityQueue.ofAll(toStringComparator(), elements);
    }

    @Override
    protected <T> Traversable<T> ofJavaStream(Stream<? extends T> javaStream) {
        return PriorityQueue.ofAll(toStringComparator(), javaStream);
    }

    @Override
    protected PriorityQueue<Boolean> ofAll(boolean[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Byte> ofAll(byte[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Character> ofAll(char[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Double> ofAll(double[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Float> ofAll(float[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Integer> ofAll(int[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Long> ofAll(long[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected PriorityQueue<Short> ofAll(short[] array) {
        return PriorityQueue.ofAll(List.ofAll(array));
    }

    @Override
    protected <T> PriorityQueue<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return PriorityQueue.tabulate(n, f);
    }

    @Override
    protected <T> PriorityQueue<T> fill(int n, Supplier<? extends T> s) {
        return PriorityQueue.fill(n, s);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    @Override
    protected boolean isOrdered() {
        return true;
    }

    private static SerializableComparator<Object> toStringComparator() {
        return (o1, o2) -> String.valueOf(o1).compareTo(String.valueOf(o2));
    }

    private static Comparator<Integer> composedComparator() {
        final Comparator<Integer> bitCountComparator = (o1, o2) -> Integer.compare(bitCount(o1), bitCount(o2));
        return bitCountComparator.thenComparing(naturalComparator());
    }

    @Test
    @Override
    public void shouldScanLeftWithNonComparable() {
        // The resulting type would need a comparator
    }

    @Test
    @Override
    public void shouldScanRightWithNonComparable() {
        // The resulting type would need a comparator
    }

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // The empty PriorityQueue encapsulates a comparator and therefore cannot be a singleton
    }

    // -- static narrow

    @Test
    public void shouldNarrowQueue() {
        final PriorityQueue<Double> doubles = of(1.0d);
        final PriorityQueue<Number> numbers = PriorityQueue.narrow(doubles);
        final int actual = numbers.enqueue(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- toList

    @Test
    public void toListIsSortedAccordingToComparator() {
        final Comparator<Integer> comparator = composedComparator();
        final PriorityQueue<Integer> queue = PriorityQueue.ofAll(comparator, values);
        assertThat(queue.toList()).isEqualTo(values.sorted(comparator));
    }

    // -- merge

    @Test
    public void shouldMergeTwoPriorityQueues() {
        final PriorityQueue<Integer> source = of(3, 1, 4, 1, 5);
        final PriorityQueue<Integer> target = of(9, 2, 6, 5, 3);
        assertThat(source.merge(target)).isEqualTo(of(3, 1, 4, 1, 5, 9, 2, 6, 5, 3));

        assertThat(PriorityQueue.of(3).merge(PriorityQueue.of(toStringComparator(), 1))).isEqualTo(of(3, 1));
    }

    // -- distinct

    @Test
    public void shouldComputeDistinctOfNonEmptyTraversableUsingKeyExtractor() {
        final Comparator<String> comparator = (o1, o2) -> Integer.compare(o1.charAt(1), o2.charAt(1));
        assertThat(PriorityQueue.of(comparator, "5c", "1a", "3a", "1a", "2a", "4b", "3b").distinct().map(s -> s.substring(1))).isEqualTo(of("a", "b", "c"));
    }

    // -- removeAll

    @Test
    public void shouldRemoveAllElements() {
        assertThat(of(3, 1, 4, 1, 5, 9, 2, 6).removeAll(of(1, 9, 1, 2))).isEqualTo(of(3, 4, 5, 6));
    }

    // -- enqueueAll

    @Test
    public void shouldEnqueueAllElements() {
        assertThat(of(3, 1, 4).enqueueAll(of(1, 5, 9, 2))).isEqualTo(of(3, 1, 4, 1, 5, 9, 2));
    }

    // -- peek

    @Test(expected = NoSuchElementException.class)
    public void shouldFailPeekOfEmpty() {
        empty().peek();
    }

    // -- dequeue

    @Test
    public void shouldDeque() {
        assertThat(of(3, 1, 4, 1, 5).dequeue()).isEqualTo(Tuple.of(1, of(3, 4, 1, 5)));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldFailDequeueOfEmpty() {
        empty().dequeue();
    }

    // -- toPriorityQueue

    @Test
    public void shouldKeepInstanceOfPriorityQueue() {
        final PriorityQueue<Integer> queue = PriorityQueue.of(1, 3, 2);
        assertThat(queue.toPriorityQueue()).isSameAs(queue);
    }

    // -- property based tests

    @Test
    public void shouldBehaveExactlyLikeAnotherPriorityQueue() {
        for (int i = 0; i < 10; i++) {
            final Random random = getRandom(-1);

            final java.util.PriorityQueue<Integer> mutablePriorityQueue = new java.util.PriorityQueue<>();
            javaslang.collection.PriorityQueue<Integer> functionalPriorityQueue = javaslang.collection.PriorityQueue.empty();

            final int size = 100_000;
            for (int j = 0; j < size; j++) {
                /* Insert */
                if (random.nextInt() % 3 == 0) {
                    assertMinimumsAreEqual(mutablePriorityQueue, functionalPriorityQueue);

                    final int value = random.nextInt(size) - (size / 2);
                    mutablePriorityQueue.add(value);
                    functionalPriorityQueue = functionalPriorityQueue.enqueue(value);
                }

                assertMinimumsAreEqual(mutablePriorityQueue, functionalPriorityQueue);

                /* Delete */
                if (random.nextInt() % 5 == 0) {
                    if (!mutablePriorityQueue.isEmpty()) { mutablePriorityQueue.poll(); }
                    if (!functionalPriorityQueue.isEmpty()) { functionalPriorityQueue = functionalPriorityQueue.tail(); }

                    assertMinimumsAreEqual(mutablePriorityQueue, functionalPriorityQueue);
                }
            }

            final Collection<Integer> oldValues = mutablePriorityQueue.stream().sorted().collect(toList());
            final Collection<Integer> newValues = functionalPriorityQueue.toJavaList();
            assertThat(oldValues).isEqualTo(newValues);
        }
    }

    private void assertMinimumsAreEqual(java.util.PriorityQueue<Integer> oldQueue, PriorityQueue<Integer> newQueue) {
        assertThat(oldQueue.isEmpty()).isEqualTo(newQueue.isEmpty());
        if (!newQueue.isEmpty()) {
            assertThat(oldQueue.peek()).isEqualTo(newQueue.head());
        }
    }
}