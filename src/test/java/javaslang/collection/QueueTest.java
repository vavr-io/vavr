/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.control.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class QueueTest extends AbstractSeqTest {

    // -- construction

    @Override
    protected <T> Collector<T, ArrayList<T>, Queue<T>> collector() {
        return Queue.collector();
    }

    @Override
    protected <T> Queue<T> empty() {
        return Queue.empty();
    }

    @Override
    protected <T> Queue<T> of(T element) {
        return Queue.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> Queue<T> of(T... elements) {
        return Queue.of(elements);
    }

    @Override
    protected <T> Queue<T> ofAll(Iterable<? extends T> elements) {
        return Queue.ofAll(elements);
    }

    @Override
    protected Queue<Boolean> ofAll(boolean[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Byte> ofAll(byte[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Character> ofAll(char[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Double> ofAll(double[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Float> ofAll(float[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Integer> ofAll(int[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Long> ofAll(long[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected Queue<Short> ofAll(short[] array) {
        return Queue.ofAll(array);
    }

    @Override
    protected <T> Queue<T> fill(Integer n, Function<Integer, ? extends T> f) {
        return Queue.fill(n, f);
    }

    @Override
    protected <T> Queue<T> fill(Integer n, Supplier<? extends T> s) {
        return Queue.fill(n, s);
    }

    @Override
    protected Queue<Character> range(char from, char toExclusive) {
        return Queue.range(from, toExclusive);
    }

    @Override
    protected Queue<Character> rangeBy(char from, char toExclusive, int step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Double> rangeBy(double from, double toExclusive, double step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Integer> range(int from, int toExclusive) {
        return Queue.range(from, toExclusive);
    }

    @Override
    protected Queue<Integer> rangeBy(int from, int toExclusive, int step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Long> range(long from, long toExclusive) {
        return Queue.range(from, toExclusive);
    }

    @Override
    protected Queue<Long> rangeBy(long from, long toExclusive, long step) {
        return Queue.rangeBy(from, toExclusive, step);
    }

    @Override
    protected Queue<Character> rangeClosed(char from, char toInclusive) {
        return Queue.rangeClosed(from, toInclusive);
    }

    @Override
    protected Queue<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Queue<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Queue<Integer> rangeClosed(int from, int toInclusive) {
        return Queue.rangeClosed(from, toInclusive);
    }

    @Override
    protected Queue<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected Queue<Long> rangeClosed(long from, long toInclusive) {
        return Queue.rangeClosed(from, toInclusive);
    }

    @Override
    protected Queue<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return Queue.rangeClosedBy(from, toInclusive, step);
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    // -- peek

    @Test(expected = NoSuchElementException.class)
    public void shouldFailPeekOfEmpty() {
        Queue.empty().peek();
    }

    @Test
    public void shouldReturnPeekOfNonEmpty() {
        assertThat(Queue.of(1).peek()).isEqualTo(1);
    }

    @Test
    public void shouldReturnPeekOption() {
        assertThat(Queue.empty().peekOption()).isEqualTo(Option.none());
        assertThat(Queue.of(1).peekOption()).isEqualTo(Option.of(1));
    }

    // -- dequeue

    @Test(expected = NoSuchElementException.class)
    public void shouldFailDequeueOfEmpty() {
        Queue.empty().dequeue();
    }

    @Test
    public void shouldDequeueOfNonEmpty() {
        assertThat(Queue.of(1, 2, 3).dequeue()).isEqualTo(Tuple.of(1, Queue.of(2, 3)));
    }

    @Test
    public void shouldDequeueOption() {
        assertThat(Queue.empty().dequeueOption()).isEqualTo(Option.none());
        assertThat(Queue.of(1, 2, 3).dequeueOption()).isEqualTo(Option.of(Tuple.of(1, Queue.of(2, 3))));
    }

    // -- special cases

    private Queue<Integer> enqueued() {
        return Queue.of(1).enqueue(2, 3, 1, 5, 6);
    }

    // -- get

    @Test
    public void shouldGetFrontEnc() {
        assertThat(enqueued().get(0)).isEqualTo(1);
    }

    @Test
    public void shouldGetRearEnc() {
        assertThat(enqueued().get(1)).isEqualTo(2);
    }

    // -- take

    @Test
    public void shouldTakeFrontEnc() {
        assertThat(enqueued().take(1)).isEqualTo(of(1));
    }

    // -- insertAll

    @Test
    public void shouldInsertAllEnc() {
        assertThat(enqueued().insertAll(0, List.of(91, 92))).isEqualTo(of(91, 92, 1, 2, 3, 1, 5, 6));
        assertThat(enqueued().insertAll(1, List.of(91, 92))).isEqualTo(of(1, 91, 92, 2, 3, 1, 5, 6));
        assertThat(enqueued().insertAll(2, List.of(91, 92))).isEqualTo(of(1, 2, 91, 92, 3, 1, 5, 6));
        assertThat(enqueued().insertAll(6, List.of(91, 92))).isEqualTo(of(1, 2, 3, 1, 5, 6, 91, 92));
    }

    // -- insert

    @Test
    public void shouldInsertEnc() {
        assertThat(enqueued().insert(0, 9)).isEqualTo(of(9, 1, 2, 3, 1, 5, 6));
        assertThat(enqueued().insert(1, 9)).isEqualTo(of(1, 9, 2, 3, 1, 5, 6));
        assertThat(enqueued().insert(2, 9)).isEqualTo(of(1, 2, 9, 3, 1, 5, 6));
        assertThat(enqueued().insert(6, 9)).isEqualTo(of(1, 2, 3, 1, 5, 6, 9));
    }

    // -- intersperse

    @Test
    public void shouldIntersperseEnc() {
        assertThat(enqueued().intersperse(9)).isEqualTo(of(1, 9, 2, 9, 3, 9, 1, 9, 5, 9, 6));
    }

    // -- indexOf

    @Test
    public void shouldNotFindIndexOfElementWhenStartIsGreaterEnc() {
        assertThat(enqueued().indexOf(2, 2)).isEqualTo(-1);
    }

    @Test
    public void shouldFindIndexOfFirstElementEnc() {
        assertThat(enqueued().indexOf(1)).isEqualTo(0);
    }

    @Test
    public void shouldFindIndexOfInnerElementEnc() {
        assertThat(enqueued().indexOf(2)).isEqualTo(1);
    }

    @Test
    public void shouldFindIndexOfLastElementEnc() {
        assertThat(enqueued().indexOf(3)).isEqualTo(2);
    }

    // -- lastIndexOf

    @Test
    public void shouldNotFindLastIndexOfElementWhenEndIdLessEnc() {
        assertThat(enqueued().lastIndexOf(3, 1)).isEqualTo(-1);
    }

    @Test
    public void shouldFindLastIndexOfElementEnc() {
        assertThat(enqueued().lastIndexOf(1)).isEqualTo(3);
    }

    @Test
    public void shouldFindLastIndexOfElementWithEndEnc() {
        assertThat(enqueued().lastIndexOf(1, 1)).isEqualTo(0);
    }

    // -- equals

    @Test
    public void shouldCheckHashCodeWhenComparing() {
        assertThat(Queue.of(0, null).equals(Queue.of(0, 0))).isFalse();
    }

}
