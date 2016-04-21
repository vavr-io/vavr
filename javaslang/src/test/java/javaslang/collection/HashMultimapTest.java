package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class HashMultimapTest extends AbstractMultimapTest {

    @Override
    protected String className() {
        return "HashMultimap[" + containerName() + "]";
    }

    @Override
    <T1, T2> Map<T1, T2> javaEmptyMap() {
        return new java.util.HashMap<>();
    }

    @Override
    protected <T1 extends Comparable<T1>, T2> Multimap<T1, T2> emptyMap() {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().empty();
            case SET:
                return HashMultimap.withSet().empty();
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).empty();
        }
        throw new RuntimeException();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().collector();
            case SET:
                return HashMultimap.withSet().collector();
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).collector();
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofEntries(entries);
            case SET:
                return HashMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<K>, V> Multimap<K, V> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().ofEntries(entries);
            case SET:
                return HashMultimap.withSet().ofEntries(entries);
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapOfPairs(Object... pairs) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().of(pairs);
            case SET:
                return HashMultimap.withSet().of(pairs);
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).of(pairs);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapOf(K key, V value) {
        return HashMultimap.withSeq().of(key, value);
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().tabulate(n, f);
            case SET:
                return HashMultimap.withSet().tabulate(n, f);
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).tabulate(n, f);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K extends Comparable<K>, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        switch (containerType) {
            case SEQ:
                return HashMultimap.withSeq().fill(n, s);
            case SET:
                return HashMultimap.withSet().fill(n, s);
            case SORTED_SET:
                return HashMultimap.withSortedSet(TreeSetTest.toStringComparator()).fill(n, s);
        }
        throw new RuntimeException();
    }

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final HashMultimap<Integer, Number> int2doubleMap = (HashMultimap<Integer, Number>) this.<Integer, Number>emptyMap().put(1, 1.0d);
        final HashMultimap<Number, Number> number2numberMap = HashMultimap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(new BigDecimal("2"), new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

}
