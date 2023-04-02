package io.vavr.collection.champ;

import io.vavr.collection.AbstractSetTest;
import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Spliterator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SequencedChampSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, SequencedChampSet<T>> collector() {
        return SequencedChampSet.collector();
    }

    @Override
    protected <T> SequencedChampSet<T> empty() {
        return SequencedChampSet.empty();
    }

    @Override
    protected <T> SequencedChampSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> SequencedChampSet<T> of(T element) {
        return SequencedChampSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> SequencedChampSet<T> of(T... elements) {
        return SequencedChampSet.of(elements);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return false;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> SequencedChampSet<T> ofAll(Iterable<? extends T> elements) {
        return SequencedChampSet.ofAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> SequencedChampSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return SequencedChampSet.ofAll(javaStream.collect(Collectors.toList()));
    }

    @Override
    protected SequencedChampSet<Boolean> ofAll(boolean... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Byte> ofAll(byte... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Character> ofAll(char... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Double> ofAll(double... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Float> ofAll(float... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Integer> ofAll(int... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Long> ofAll(long... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected SequencedChampSet<Short> ofAll(short... elements) {
        return SequencedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected <T> SequencedChampSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Collections.tabulate(n, f, SequencedChampSet.empty(), SequencedChampSet::of);
    }

    @Override
    protected <T> SequencedChampSet<T> fill(int n, Supplier<? extends T> s) {
        return Collections.fill(n, s, SequencedChampSet.empty(), SequencedChampSet::of);
    }

    @Override
    protected SequencedChampSet<Character> range(char from, char toExclusive) {
        return SequencedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected SequencedChampSet<Character> rangeBy(char from, char toExclusive, int step) {
        return SequencedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected SequencedChampSet<Double> rangeBy(double from, double toExclusive, double step) {
        return SequencedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected SequencedChampSet<Integer> range(int from, int toExclusive) {
        return SequencedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected SequencedChampSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return SequencedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected SequencedChampSet<Long> range(long from, long toExclusive) {
        return SequencedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected SequencedChampSet<Long> rangeBy(long from, long toExclusive, long step) {
        return SequencedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected SequencedChampSet<Character> rangeClosed(char from, char toInclusive) {
        return SequencedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected SequencedChampSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return SequencedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected SequencedChampSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return SequencedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected SequencedChampSet<Integer> rangeClosed(int from, int toInclusive) {
        return SequencedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected SequencedChampSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return SequencedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected SequencedChampSet<Long> rangeClosed(long from, long toInclusive) {
        return SequencedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected SequencedChampSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return SequencedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Test
    public void shouldKeepOrder() {
        final List<Integer> actual = SequencedChampSet.<Integer>empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.of(3, 2, 1));
    }

    // -- static narrow

    @Test
    public void shouldNarrowLinkedHashSet() {
        final SequencedChampSet<Double> doubles = of(1.0d);
        final SequencedChampSet<Number> numbers = SequencedChampSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- replace

    @Test
    public void shouldReturnSameInstanceIfReplacingNonExistingElement() {
        final Set<Integer> set = SequencedChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(4, 0);
        assertThat(actual).isSameAs(set);
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElement() {
        final Set<Integer> set = SequencedChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(2, 0);
        final Set<Integer> expected = SequencedChampSet.of(1, 0, 3);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElementAndRemoveOtherIfElementAlreadyExists() {
        final Set<Integer> set = SequencedChampSet.of(1, 2, 3, 4, 5);
        final Set<Integer> actual = set.replace(2, 4);
        final Set<Integer> expected = SequencedChampSet.of(1, 4, 3, 5);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldReturnSameInstanceWhenReplacingExistingElementWithIdentity() {
        final Set<Integer> set = SequencedChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(2, 2);
        assertThat(actual).isSameAs(set);
    }

    // -- transform

    @Test
    public void shouldTransform() {
        final String transformed = of(42).transform(v -> String.valueOf(v.get()));
        assertThat(transformed).isEqualTo("42");
    }

    // -- toLinkedSet

    @Test
    public void shouldReturnSelfOnConvertToLinkedSet() {
        final SequencedChampSet<Integer> value = of(1, 2, 3);
        assertThat(value.toLinkedSet()).isSameAs(value);
    }

    // -- spliterator

    @Test
    public void shouldNotHaveSortedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SORTED)).isFalse();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    // -- isSequential()

    @Test
    public void shouldReturnTrueWhenIsSequentialCalled() {
        assertThat(of(1, 2, 3).isSequential()).isTrue();
    }

}
