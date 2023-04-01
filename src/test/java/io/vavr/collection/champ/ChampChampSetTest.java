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

public class ChampChampSetTest extends AbstractSetTest {

    @Override
    protected <T> Collector<T, ArrayList<T>, ChampChampSet<T>> collector() {
        return ChampChampSet.collector();
    }

    @Override
    protected <T> ChampChampSet<T> empty() {
        return ChampChampSet.empty();
    }

    @Override
    protected <T> ChampChampSet<T> emptyWithNull() {
        return empty();
    }

    @Override
    protected <T> ChampChampSet<T> of(T element) {
        return ChampChampSet.of(element);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <T> ChampChampSet<T> of(T... elements) {
        return ChampChampSet.of(elements);
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
    protected <T> ChampChampSet<T> ofAll(Iterable<? extends T> elements) {
        return ChampChampSet.ofAll(elements);
    }

    @Override
    protected <T extends Comparable<? super T>> ChampChampSet<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return ChampChampSet.ofAll(javaStream.collect(Collectors.toList()));
    }

    @Override
    protected ChampChampSet<Boolean> ofAll(boolean... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Byte> ofAll(byte... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Character> ofAll(char... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Double> ofAll(double... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Float> ofAll(float... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Integer> ofAll(int... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Long> ofAll(long... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected ChampChampSet<Short> ofAll(short... elements) {
        return ChampChampSet.ofAll(Iterator.ofAll(elements));
    }

    @Override
    protected <T> ChampChampSet<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        return Collections.tabulate(n, f, ChampChampSet.empty(), ChampChampSet::of);
    }

    @Override
    protected <T> ChampChampSet<T> fill(int n, Supplier<? extends T> s) {
        return Collections.fill(n, s, ChampChampSet.empty(), ChampChampSet::of);
    }

    @Override
    protected ChampChampSet<Character> range(char from, char toExclusive) {
        return ChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampChampSet<Character> rangeBy(char from, char toExclusive, int step) {
        return ChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampChampSet<Double> rangeBy(double from, double toExclusive, double step) {
        return ChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampChampSet<Integer> range(int from, int toExclusive) {
        return ChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampChampSet<Integer> rangeBy(int from, int toExclusive, int step) {
        return ChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampChampSet<Long> range(long from, long toExclusive) {
        return ChampChampSet.ofAll(Iterator.range(from, toExclusive));
    }

    @Override
    protected ChampChampSet<Long> rangeBy(long from, long toExclusive, long step) {
        return ChampChampSet.ofAll(Iterator.rangeBy(from, toExclusive, step));
    }

    @Override
    protected ChampChampSet<Character> rangeClosed(char from, char toInclusive) {
        return ChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampChampSet<Character> rangeClosedBy(char from, char toInclusive, int step) {
        return ChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampChampSet<Double> rangeClosedBy(double from, double toInclusive, double step) {
        return ChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampChampSet<Integer> rangeClosed(int from, int toInclusive) {
        return ChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampChampSet<Integer> rangeClosedBy(int from, int toInclusive, int step) {
        return ChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Override
    protected ChampChampSet<Long> rangeClosed(long from, long toInclusive) {
        return ChampChampSet.ofAll(Iterator.rangeClosed(from, toInclusive));
    }

    @Override
    protected ChampChampSet<Long> rangeClosedBy(long from, long toInclusive, long step) {
        return ChampChampSet.ofAll(Iterator.rangeClosedBy(from, toInclusive, step));
    }

    @Test
    public void shouldKeepOrder() {
        final List<Integer> actual = ChampChampSet.<Integer>empty().add(3).add(2).add(1).toList();
        assertThat(actual).isEqualTo(List.of(3, 2, 1));
    }

    // -- static narrow

    @Test
    public void shouldNarrowLinkedHashSet() {
        final ChampChampSet<Double> doubles = of(1.0d);
        final ChampChampSet<Number> numbers = ChampChampSet.narrow(doubles);
        final int actual = numbers.add(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    // -- replace

    @Test
    public void shouldReturnSameInstanceIfReplacingNonExistingElement() {
        final Set<Integer> set = ChampChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(4, 0);
        assertThat(actual).isSameAs(set);
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElement() {
        final Set<Integer> set = ChampChampSet.of(1, 2, 3);
        final Set<Integer> actual = set.replace(2, 0);
        final Set<Integer> expected = ChampChampSet.of(1, 0, 3);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldPreserveOrderWhenReplacingExistingElementAndRemoveOtherIfElementAlreadyExists() {
        final Set<Integer> set = ChampChampSet.of(1, 2, 3, 4, 5);
        final Set<Integer> actual = set.replace(2, 4);
        final Set<Integer> expected = ChampChampSet.of(1, 4, 3, 5);
        assertThat(actual).isEqualTo(expected);
        Assertions.assertThat(List.ofAll(actual)).isEqualTo(List.ofAll(expected));
    }

    @Test
    public void shouldReturnSameInstanceWhenReplacingExistingElementWithIdentity() {
        final Set<Integer> set = ChampChampSet.of(1, 2, 3);
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
        final ChampChampSet<Integer> value = of(1, 2, 3);
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
