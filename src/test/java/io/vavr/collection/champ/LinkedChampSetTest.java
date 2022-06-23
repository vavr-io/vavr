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

public class LinkedChampSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, LinkedChampSet<T>> collector() {
        return LinkedChampSet.collector();
    }

    @Override
    protected <T> LinkedChampSet<T> empty() {
        return LinkedChampSet.empty();
    }

    @Override
    protected <T> LinkedChampSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> LinkedChampSet<T> of(T element) {
        return LinkedChampSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> LinkedChampSet<T> of(T... elements) {
        return LinkedChampSet.of(elements);
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
    protected <T> LinkedChampSet<T> ofAll(Iterable<? extends T> elements) {
        return LinkedChampSet.ofAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> LinkedChampSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return LinkedChampSet.ofAll(javaStream.collect(Collectors.toList()));
    }

    @Override
    protected LinkedChampSet<Boolean> ofAll(boolean... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Byte> ofAll(byte... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Character> ofAll(char... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Double> ofAll(double... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Float> ofAll(float... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Integer> ofAll(int... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Long> ofAll(long... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampSet<Short> ofAll(short... elements) {
        return LinkedChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected <T> LinkedChampSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Collections.tabulate(n, f, LinkedChampSet.empty(), LinkedChampSet::of);
    }

    @Override
    protected <T> LinkedChampSet<T> fill(int n, Supplier<? extends T> s) {
        return Collections.fill(n, s, LinkedChampSet.empty(), LinkedChampSet::of);
    }

    @Override
    protected LinkedChampSet<Character> range(char from, char toExclusive) {
        return LinkedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampSet<Integer> range(int from, int toExclusive) {
        return LinkedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampSet<Long> range(long from, long toExclusive) {
        return LinkedChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Test
    public void shouldKeepOrder() {
        final List<Integer> actual = LinkedChampSet.<Integer>empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.of(3, 2, 1));
    }

    // -- static narrow

    @Test
    public void shouldNarrowLinkedHashSet() {
        final LinkedChampSet<Double> doubles = of(1.0d);
        final LinkedChampSet<Number> numbers = LinkedChampSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- replace

    @Test
    public void shouldReturnSameInstanceIfReplacingNonExistingElement() {
        final Set<Integer> set = LinkedChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(4, 0);
        assertThat(actual).isSameAs(set);
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElement() {
        final Set<Integer> set = LinkedChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(2, 0);
        final Set<Integer> expected = LinkedChampSet.of(1, 0, 3);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElementAndRemoveOtherIfElementAlreadyExists() {
        final Set<Integer> set = LinkedChampSet.of(1, 2, 3, 4, 5);
        final Set<Integer> actual = set.replace(2, 4);
        final Set<Integer> expected = LinkedChampSet.of(1, 4, 3, 5);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldReturnSameInstanceWhenReplacingExistingElementWithIdentity() {
        final Set<Integer> set = LinkedChampSet.of(1, 2, 3);
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
        final LinkedChampSet<Integer> value = of(1, 2, 3);
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
