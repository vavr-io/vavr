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

public class LinkedChampChampSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, LinkedChampChampSet<T>> collector() {
        return LinkedChampChampSet.collector();
    }

    @Override
    protected <T> LinkedChampChampSet<T> empty() {
        return LinkedChampChampSet.empty();
    }

    @Override
    protected <T> LinkedChampChampSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> LinkedChampChampSet<T> of(T element) {
        return LinkedChampChampSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> LinkedChampChampSet<T> of(T... elements) {
        return LinkedChampChampSet.of(elements);
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
    protected <T> LinkedChampChampSet<T> ofAll(Iterable<? extends T> elements) {
        return LinkedChampChampSet.ofAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> LinkedChampChampSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return LinkedChampChampSet.ofAll(javaStream.collect(Collectors.toList()));
    }

    @Override
    protected LinkedChampChampSet<Boolean> ofAll(boolean... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Byte> ofAll(byte... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Character> ofAll(char... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Double> ofAll(double... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Float> ofAll(float... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Integer> ofAll(int... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Long> ofAll(long... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected LinkedChampChampSet<Short> ofAll(short... elements) {
        return LinkedChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected <T> LinkedChampChampSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Collections.tabulate(n, f, LinkedChampChampSet.empty(), LinkedChampChampSet::of);
    }

    @Override
    protected <T> LinkedChampChampSet<T> fill(int n, Supplier<? extends T> s) {
        return Collections.fill(n, s, LinkedChampChampSet.empty(), LinkedChampChampSet::of);
    }

    @Override
    protected LinkedChampChampSet<Character> range(char from, char toExclusive) {
        return LinkedChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampChampSet<Character> rangeBy(char from, char toExclusive, int step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Double> rangeBy(double from, double toExclusive, double step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Integer> range(int from, int toExclusive) {
        return LinkedChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampChampSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Long> range(long from, long toExclusive) {
        return LinkedChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected LinkedChampChampSet<Long> rangeBy(long from, long toExclusive, long step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Character> rangeClosed(char from, char toInclusive) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampChampSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Integer> rangeClosed(int from, int toInclusive) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampChampSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected LinkedChampChampSet<Long> rangeClosed(long from, long toInclusive) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected LinkedChampChampSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return LinkedChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Test
    public void shouldKeepOrder() {
        final List<Integer> actual = LinkedChampChampSet.<Integer>empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.of(3, 2, 1));
    }

    // -- static narrow

    @Test
    public void shouldNarrowLinkedHashSet() {
        final LinkedChampChampSet<Double> doubles = of(1.0d);
        final LinkedChampChampSet<Number> numbers = LinkedChampChampSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- replace

    @Test
    public void shouldReturnSameInstanceIfReplacingNonExistingElement() {
        final Set<Integer> set = LinkedChampChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(4, 0);
        assertThat(actual).isSameAs(set);
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElement() {
        final Set<Integer> set = LinkedChampChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(2, 0);
        final Set<Integer> expected = LinkedChampChampSet.of(1, 0, 3);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElementAndRemoveOtherIfElementAlreadyExists() {
        final Set<Integer> set = LinkedChampChampSet.of(1, 2, 3, 4, 5);
        final Set<Integer> actual = set.replace(2, 4);
        final Set<Integer> expected = LinkedChampChampSet.of(1, 4, 3, 5);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldReturnSameInstanceWhenReplacingExistingElementWithIdentity() {
        final Set<Integer> set = LinkedChampChampSet.of(1, 2, 3);
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
        final LinkedChampChampSet<Integer> value = of(1, 2, 3);
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
