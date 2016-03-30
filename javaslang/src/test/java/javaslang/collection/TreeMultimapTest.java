package javaslang.collection;

import javaslang.Tuple2;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static javaslang.collection.Comparators.naturalComparator;

public class TreeMultimapTest extends AbstractMultimapTest {

    @Override
    protected String className() {
        return "TreeMultimap[" + containerName() + "]";
    }

    @Override
    protected <T1, T2> Multimap<T1, T2> emptyMap() {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).empty();
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).empty();
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).empty();
        }
        throw new RuntimeException();
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector() {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).collector();
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).collector();
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).collector();
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).ofEntries(entries);
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).ofEntries(entries);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K, V> Multimap<K, V> mapOfEntries(Map.Entry<? extends K, ? extends V>... entries) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).ofEntries(entries);
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).ofEntries(entries);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).ofEntries(entries);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K, V> Multimap<K, V> mapOfPairs(Object... pairs) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).of(pairs);
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).of(pairs);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).of(pairs);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K, V> Multimap<K, V> mapOf(K key, V value) {
        final Multimap<K, V> map = emptyMap();
        return map.put(key, value);
    }

    @Override
    protected <K, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).tabulate(n, f);
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).tabulate(n, f);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).tabulate(n, f);
        }
        throw new RuntimeException();
    }

    @Override
    protected <K, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        switch (containerType) {
            case SEQ:
                return TreeMultimap.withSeq().withKeyComparator(naturalComparator()).fill(n, s);
            case SET:
                return TreeMultimap.withSet().withKeyComparator(naturalComparator()).fill(n, s);
            case SORTED_SET:
                return TreeMultimap.withSortedSet(TreeSetTest.toStringComparator()).withKeyComparator(naturalComparator()).fill(n, s);
        }
        throw new RuntimeException();
    }

    // -- narrow

    @Test
    public void shouldNarrowMap() {
        final TreeMultimap<Integer, Number> int2doubleMap = (TreeMultimap<Integer, Number>) this.<Integer, Number>emptyMap().put(1, 1.0d);
        final TreeMultimap<Number, Number> number2numberMap = TreeMultimap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(2, new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

}
