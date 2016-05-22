/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;


import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class TreeMapTest extends AbstractSortedMapTest {
    private final Tuple2<Integer, Integer> one = Tuple.of(1, 1);
    private final Tuple2<Integer, Integer> three = Tuple.of(3, 3);
    private final Tuple2<Integer, Integer> five = Tuple.of(5, 5);
    private final Tuple2<Integer, Integer> seven = Tuple.of(7, 7);
    private final Tuple2<Integer, Integer> nine = Tuple.of(9, 9);

    @Override
    protected String className() {
        return "TreeMap";
    }

    @Override
    <T1, T2> java.util.Map<T1, T2> javaEmptyMap() {
        return new java.util.TreeMap<>();
    }

    @Override
    protected <T1 extends Comparable<? super T1>, T2> TreeMap<T1, T2> emptyMap() {
        return TreeMap.empty();
    }

    @Override
    protected boolean emptyMapShouldBeSingleton() {
        return false;
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    @Override
    protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector() {
        return TreeMap.collector();
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<? super K>, V> TreeMap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(entries);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    @Override
    protected final <K extends Comparable<? super K>, V> TreeMap<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries) {
        return TreeMap.ofEntries(entries);
    }

    @Override
    protected <K extends Comparable<? super K>, V> TreeMap<K, V> mapOfPairs(Object... pairs) {
        return TreeMap.of(pairs);
    }

    @Override
    protected <K extends Comparable<? super K>, V> TreeMap<K, V> mapOf(K key, V value) {
        return TreeMap.of(key, value);
    }

    @Override
    protected <K extends Comparable<? super K>, V> TreeMap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f) {
        return TreeMap.tabulate(n, f);
    }

    @Override
    protected <K extends Comparable<? super K>, V> TreeMap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s) {
        return TreeMap.fill(n, s);
    }

    // -- static narrow

    @Test
    public void shouldNarrowTreeMap() {
        final TreeMap<Integer, Double> int2doubleMap = mapOf(1, 1.0d);
        final TreeMap<Integer, Number> number2numberMap = TreeMap.narrow(int2doubleMap);
        final int actual = number2numberMap.put(2, new BigDecimal("2.0")).values().sum().intValue();
        assertThat(actual).isEqualTo(3);
    }

    @Test
    public void shouldScan() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
        final TreeMap<String, Integer> result = tm.scan(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(TreeMap.ofEntries(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanLeft() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
        final Seq<Tuple2<String, Integer>> result = tm.scanLeft(Tuple.of("z", 0), (t1, t2) -> Tuple.of(t1._1 + t2._1, t1._2 + t2._2));
        assertThat(result).isEqualTo(List.of(Tuple.of("z", 0), Tuple.of("zone", 1), Tuple.of("zonetwo", 3)));
    }

    @Test
    public void shouldScanRight() {
        final TreeMap<String, Integer> tm = TreeMap.ofEntries(Tuple.of("one", 1), Tuple.of("two", 2));
        final Seq<String> result = tm.scanRight("z", (t1, acc) -> acc + CharSeq.of(t1._1).reverse());
        assertThat(result).isEqualTo(List.of("zowteno", "zowt", "z"));
    }

    @Test
    public void shouldWrapMap() {
        java.util.Map<Integer, Integer> source = new HashMap<>();
        source.put(1, 2);
        source.put(3, 4);
        assertThat(TreeMap.ofAll(source)).isEqualTo(emptyIntInt().put(1, 2).put(3, 4));
    }

    // Iterators

    @Test
    public void shouldIterateAscending() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, nine, five, three, seven);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(one, three, five, seven, nine));
    }

    @Test
    public void shouldReturnKeySetAscending() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, nine, five, three, seven);
        assertThat(unit.keySet()).isEqualTo(TreeSet.of(1, 3, 5, 7, 9));
    }

    @Test
    public void givenEmptyTreeMap_ThenShouldIterate() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        assertThat(unit.iterator().toList()).isEqualTo(List.empty());
    }

    // SubMap - Option Keys
    @Test
    public void canCreateSubMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(Option.of(3), true, Option.of(7), true)).isEqualTo(TreeMap.ofEntries(three, five, seven));
        assertThat(unit.subMap(Option.of(3), false, Option.of(7), true)).isEqualTo(TreeMap.ofEntries(five, seven));
        assertThat(unit.subMap(Option.of(3), true, Option.of(7), false)).isEqualTo(TreeMap.ofEntries(three, five));
        assertThat(unit.subMap(Option.of(3), false, Option.of(7), false)).isEqualTo(TreeMap.ofEntries(five));
        assertThat(unit.subMap(Option.none(), true, Option.of(7), true)).isEqualTo(TreeMap.ofEntries(one, three, five, seven));
        assertThat(unit.subMap(Option.of(3), false, Option.none(), true)).isEqualTo(TreeMap.ofEntries(five, seven, nine));
        assertThat(unit.subMap(Option.none(), false, Option.none(), false)).isEqualTo(TreeMap.ofEntries(one, three, five, seven, nine));
    }

    @Test
    public void givenFromAndToKeysEqualAndExclusive_ThenReturnEmptySubMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(Option.of(5), false, Option.of(5), false)).isEqualTo(TreeMap.empty());
    }

    @Test
    public void givenFromAndToKeysAreEqualAndInclusive_ThenReturnSubMapWithOneEntry() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(Option.of(5), true, Option.of(5), true)).isEqualTo(TreeMap.of(five));
    }

    @Test
    public void givenFromKeyLowerThanLowestKey_ThenCreateSubMapWithAllHeadElements() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(Option.of(0), false, Option.of(5), true)).isEqualTo(TreeMap.ofEntries(one, three, five));
    }

    @Test
    public void givenToKeyGreaterThanHighestKey_ThenCreateSubMapWithAllTailElements() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(Option.of(5), true, Option.of(10), false)).isEqualTo(TreeMap.ofEntries(five, seven, nine));
    }

    @Test
    public void givenFromKeyGreaterThanToKey_WhenCreateSubMap_ThenThrowException() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        Assertions.assertThatThrownBy(() -> unit.subMap(Option.of(5), true, Option.of(1), false)).isInstanceOf(IllegalArgumentException.class);
    }

    // SubMap - Keys with Inclusive Flags

    @Test
    public void canCreateSubMapUsingKeys() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(3, true, 7, true)).isEqualTo(TreeMap.ofEntries(three, five, seven));
        assertThat(unit.subMap(3, false, 7, true)).isEqualTo(TreeMap.ofEntries(five, seven));
        assertThat(unit.subMap(3, true, 7, false)).isEqualTo(TreeMap.ofEntries(three, five));
        assertThat(unit.subMap(3, false, 7, false)).isEqualTo(TreeMap.ofEntries(five));
    }

    // SubMap - Keys without Inclusive Flags

    @Test
    public void canCreateSubMapWithoutFromAndToKeys() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.subMap(3, 7)).isEqualTo(TreeMap.ofEntries(three, five));
        assertThat(unit.subMap(5, 5)).isEqualTo(TreeMap.empty());
    }

    // SubMap - Can insert and delete

    @Test
    public void canInsertFromASubMap() {
        final Tuple2<Integer, Integer> six = Tuple.of(6, 6);
        final Tuple2<Integer, Integer> four = Tuple.of(4, 4);
        final Tuple2<Integer, Integer> first = Tuple.of(0, 0);
        final Tuple2<Integer, Integer> last = Tuple.of(10, 10);
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        SortedMap<Integer, Integer> subMap = unit.subMap(3, true, 7, true);
        subMap = subMap.put(six).put(four).put(first).put(last);
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(first, three, four, five, six, seven, last));
    }

    @Test
    public void canDeleteFromASubMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        SortedMap<Integer, Integer> subMap = unit.subMap(3, true, 7, true);
        subMap = subMap.remove(5);
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(three, seven));
        subMap = subMap.remove(7);
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(three));
        subMap = subMap.remove(3);
        assertThat(subMap).isEqualTo(TreeMap.empty());
    }

    // HeadMap

    @Test
    public void canCreateHeadMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.headMap(7, true)).isEqualTo(TreeMap.ofEntries(one, three, five, seven));
        assertThat(unit.headMap(7, false)).isEqualTo(TreeMap.ofEntries(one, three, five));
        assertThat(unit.headMap(7)).isEqualTo(TreeMap.ofEntries(one, three, five));
    }

    // TailMap

    @Test
    public void canCreateTailMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.tailMap(3, true)).isEqualTo(TreeMap.ofEntries(three, five, seven, nine));
        assertThat(unit.tailMap(3, false)).isEqualTo(TreeMap.ofEntries(five, seven, nine));
        assertThat(unit.tailMap(3)).isEqualTo(TreeMap.ofEntries(three, five, seven, nine));
    }

    // Floor

    @Test
    public void canFloor() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.floor(5)).isEqualTo(Option.of(five));
        assertThat(unit.floor(4)).isEqualTo(Option.of(three));
        assertThat(unit.floor(0)).isEqualTo(Option.none());
    }

    @Test
    public void canFloorKey() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.floorKey(5)).isEqualTo(Option.of(5));
        assertThat(unit.floorKey(4)).isEqualTo(Option.of(3));
        assertThat(unit.floor(0)).isEqualTo(Option.none());
    }

    // Ceiling

    @Test
    public void canCeiling() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.ceiling(5)).isEqualTo(Option.of(five));
        assertThat(unit.ceiling(4)).isEqualTo(Option.of(five));
        assertThat(unit.ceiling(10)).isEqualTo(Option.none());
    }

    @Test
    public void canCeilingKey() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.ceilingKey(5)).isEqualTo(Option.of(5));
        assertThat(unit.ceilingKey(4)).isEqualTo(Option.of(5));
        assertThat(unit.ceilingKey(10)).isEqualTo(Option.none());
    }

    // Lower

    @Test
    public void canLower() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.lower(5)).isEqualTo(Option.of(three));
        assertThat(unit.lower(4)).isEqualTo(Option.of(three));
        assertThat(unit.lower(0)).isEqualTo(Option.none());
    }

    @Test
    public void canLowerKey() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.lowerKey(5)).isEqualTo(Option.of(3));
        assertThat(unit.lowerKey(4)).isEqualTo(Option.of(3));
        assertThat(unit.lowerKey(0)).isEqualTo(Option.none());
    }

    // Higher

    @Test
    public void canHigher() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.higher(3)).isEqualTo(Option.of(five));
        assertThat(unit.higher(4)).isEqualTo(Option.of(five));
        assertThat(unit.higher(10)).isEqualTo(Option.none());
    }

    @Test
    public void canHigherKey() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.higherKey(3)).isEqualTo(Option.of(5));
        assertThat(unit.higherKey(4)).isEqualTo(Option.of(5));
        assertThat(unit.higherKey(10)).isEqualTo(Option.none());
    }

    // Min / Max

    @Test
    public void givenTreeMap_ThenCanMinMax() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.min()).isEqualTo(Option.of(one));
        assertThat(unit.max()).isEqualTo(Option.of(nine));
    }

    @Test
    public void givenTreeMapEmpty_ThenMinMaxReturnsNone() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        assertThat(unit.min()).isEqualTo(Option.none());
        assertThat(unit.max()).isEqualTo(Option.none());
    }

    // Head / Last

    @Test
    public void givenTreeMap_ThenCanReturnHeadAndLast() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.head()).isEqualTo(one);
        assertThat(unit.last()).isEqualTo(nine);
    }

    @Test
    public void givenTreeMapEmpty_WhenHeadOrTail_ThenThrowExceptionIfHeadLastNotFound() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        Assertions.assertThatThrownBy(unit::head).isInstanceOf(NoSuchElementException.class);
        Assertions.assertThatThrownBy(unit::last).isInstanceOf(NoSuchElementException.class);
    }

    // Descending Iterator, Key Set and Map

    @Test
    public void givenTreeMap_ThenCanReturnDescendingIterator() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, nine, five, three, seven);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(nine, seven, five, three, one));
    }

    @Test
    public void givenEmptyTreeMap_ThenCanReturnDescendingIterator() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingIterator().toList()).isEqualTo(List.empty());
    }

    @Test
    public void givenTreeMap_ThenCanReturnDescendingTreeSet() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, nine, five, three, seven);
        assertThat(unit.descendingKeySet().toList()).isEqualTo(List.of(9, 7, 5, 3, 1));
    }

    @Test
    public void givenEmptyTreeMap_ThenCanReturnDescendingKeySet() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingKeySet()).isEqualTo(TreeSet.empty());
    }

    // Descending Maps

    @Test
    public void shouldReturnDescendingMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, nine, five, three, seven);
        assertThat(unit.descendingMap().keySet().toList()).isEqualTo(List.of(1, 3, 5, 7, 9));
        assertThat(unit.descendingMap()).isEqualTo(TreeMap.ofEntries(one, three, five, seven, nine));
    }

    @Test
    public void givenEmptyMap_ThenCanReturnDescendingMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingMap()).isEqualTo(TreeMap.empty());
    }

    @Test
    public void givenDescendingMap_ThenCanSubMap() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.descendingMap().subMap(7, true, 3, true)).isEqualTo(TreeMap.ofEntries(seven, five, three));
        assertThat(unit.descendingMap().headMap(5, true)).isEqualTo(TreeMap.ofEntries(nine, seven, five));
        assertThat(unit.descendingMap().tailMap(5, true)).isEqualTo(TreeMap.ofEntries(five, three, one));
    }

    @Test
    public void givenDescendingMap_ThenCanNavigate() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        assertThat(unit.descendingMap().floorKey(4)).isEqualTo(Option.of(5));
        assertThat(unit.descendingMap().ceilingKey(4)).isEqualTo(Option.of(3));
        assertThat(unit.descendingMap().higherKey(5)).isEqualTo(Option.of(3));
        assertThat(unit.descendingMap().lowerKey(5)).isEqualTo(Option.of(7));
        assertThat(unit.descendingMap().min()).isEqualTo(Option.of(nine));
        assertThat(unit.descendingMap().max()).isEqualTo(Option.of(one));
        assertThat(unit.descendingMap().head()).isEqualTo(nine);
        assertThat(unit.descendingMap().last()).isEqualTo(one);
    }

    @Test
    public void givenDescendingMap_WhenCreateSubMapWithFromKeyLowerThanToKey_ThenThrowException() {
        final TreeMap<Integer, Integer> unit = TreeMap.ofEntries(one, three, five, seven, nine);
        Assertions.assertThatThrownBy(() -> unit.descendingMap().subMap(Option.of(1), true, Option.of(5), false)).isInstanceOf(IllegalArgumentException.class);
    }

    // -- obsolete tests

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // This empty test is overriding an AbstractMapTest.java test that does not need to execute.
        // The empty TreeMap encapsulates a comparator and therefore cannot be a singleton
    }

    private static Comparator<Object> toStringComparator() { // moveup
        return (Comparator<Object> & Serializable) (o1, o2) -> String.valueOf(o1).compareTo(String.valueOf(o2));
    }
}
