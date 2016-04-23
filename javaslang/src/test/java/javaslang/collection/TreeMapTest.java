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
    private final Tuple2<String, Integer> aaron = Tuple.of("aaron", 5);
    private final Tuple2<String, Integer> carter = Tuple.of("carter", null);
    private final Tuple2<String, Integer> john = Tuple.of("john", 2);
    private final Tuple2<String, Integer> nancy = Tuple.of("nancy", 3);
    private final Tuple2<String, Integer> simon = Tuple.of("simon", 7);

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
        return TreeMap.<Integer, T> collector();
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
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, simon, john, carter, nancy);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(aaron, carter, john, nancy, simon));
    }

    @Test
    public void shouldReturnKeySetAscending() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, simon, john, carter, nancy);
        assertThat(unit.keySet()).isEqualTo(TreeSet.of("aaron", "carter", "john", "nancy", "simon"));
    }

    @Test
    public void givenEmptyTreeMap_ThenShouldIterate() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        assertThat(unit.iterator().toList()).isEqualTo(List.empty());
    }

    // SubMap - Option Keys
    @Test
    public void canCreateSubMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap(Option.of("carter"), true, Option.of("nancy"), true)).isEqualTo(TreeMap.ofEntries(carter, john, nancy));
        assertThat(unit.subMap(Option.of("carter"), false, Option.of("nancy"), true)).isEqualTo(TreeMap.ofEntries(john, nancy));
        assertThat(unit.subMap(Option.of("carter"), true, Option.of("nancy"), false)).isEqualTo(TreeMap.ofEntries(carter, john));
        assertThat(unit.subMap(Option.of("carter"), false, Option.of("nancy"), false)).isEqualTo(TreeMap.ofEntries(john));
        assertThat(unit.subMap(Option.none(), true, Option.of("nancy"), true)).isEqualTo(TreeMap.ofEntries(aaron, carter, john, nancy));
        assertThat(unit.subMap(Option.of("carter"), false, Option.none(), true)).isEqualTo(TreeMap.ofEntries(john, nancy, simon));
        assertThat(unit.subMap(Option.none(), false, Option.none(), false)).isEqualTo(TreeMap.ofEntries(aaron, carter, john, nancy, simon));
    }

    @Test
    public void givenFromAndToKeysEqualAndExclusive_ThenReturnEmptySubMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap(Option.of("john"), false, Option.of("john"), false)).isEqualTo(TreeMap.empty());
    }

    @Test
    public void givenFromAndToKeysAreEqualAndInclusive_ThenReturnSubMapWithOneEntry() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap(Option.of("john"), true, Option.of("john"), true)).isEqualTo(TreeMap.of(john));
    }

    @Test
    public void givenFromKeyLowerThanLowestKey_ThenCreateSubMapWithAllHeadElements() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap(Option.of("aaa"), false, Option.of("john"), true)).isEqualTo(TreeMap.ofEntries(aaron, carter, john));
    }

    @Test
    public void givenToKeyGreaterThanHighestKey_ThenCreateSubMapWithAllTailElements() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap(Option.of("john"), true, Option.of("zzz"), false)).isEqualTo(TreeMap.ofEntries(john, nancy, simon));
    }

    @Test
    public void givenFromKeyGreaterThanToKey_WhenCreateSubMap_ThenThrowException() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        Assertions.assertThatThrownBy(() -> unit.subMap(Option.of("john"), true, Option.of("aaron"), false)).isInstanceOf(IllegalArgumentException.class);
    }

    // SubMap - Keys with Inclusive Flags

    @Test
    public void canCreateSubMapUsingKeys() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap("carter", true, "nancy", true)).isEqualTo(TreeMap.ofEntries(carter, john, nancy));
        assertThat(unit.subMap("carter", false, "nancy", true)).isEqualTo(TreeMap.ofEntries(john, nancy));
        assertThat(unit.subMap("carter", true, "nancy", false)).isEqualTo(TreeMap.ofEntries(carter, john));
        assertThat(unit.subMap("carter", false, "nancy", false)).isEqualTo(TreeMap.ofEntries(john));
    }

    // SubMap - Keys without Inclusive Flags

    @Test
    public void canCreateSubMapWithoutFromAndToKeys() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.subMap("carter", "nancy")).isEqualTo(TreeMap.ofEntries(carter, john));
        assertThat(unit.subMap("john", "john")).isEqualTo(TreeMap.empty());
    }

    // SubMap - Can insert and delete

    @Test
    public void canInsertFromASubMap() {
        final Tuple2<String, Integer> maria = Tuple.of("maria", 5);
        final Tuple2<String, Integer> eduard = Tuple.of("eduard", 23);
        final Tuple2<String, Integer> first = Tuple.of("_first", 7);
        final Tuple2<String, Integer> last = Tuple.of("zzLast", 3);
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        SortedMap<String, Integer> subMap = unit.subMap("carter", true, "nancy", true);
        subMap = subMap.put(maria).put(eduard).put(first).put(last);
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(first, carter, eduard, john, maria, nancy, last));
    }

    @Test
    public void canDeleteFromASubMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        SortedMap<String, Integer> subMap = unit.subMap("carter", true, "nancy", true);
        subMap = subMap.remove("john");
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(carter, nancy));
        subMap = subMap.remove("nancy");
        assertThat(subMap).isEqualTo(TreeMap.ofEntries(carter));
        subMap = subMap.remove("carter");
        assertThat(subMap).isEqualTo(TreeMap.empty());
    }

    // HeadMap

    @Test
    public void canCreateHeadMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.headMap("nancy", true)).isEqualTo(TreeMap.ofEntries(aaron, carter, john, nancy));
        assertThat(unit.headMap("nancy", false)).isEqualTo(TreeMap.ofEntries(aaron, carter, john));
        assertThat(unit.headMap("nancy")).isEqualTo(TreeMap.ofEntries(aaron, carter, john));
    }

    // TailMap

    @Test
    public void canCreateTailMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.tailMap("carter", true)).isEqualTo(TreeMap.ofEntries(carter, john, nancy, simon));
        assertThat(unit.tailMap("carter", false)).isEqualTo(TreeMap.ofEntries(john, nancy, simon));
        assertThat(unit.tailMap("carter")).isEqualTo(TreeMap.ofEntries(carter, john, nancy, simon));
    }

    // Floor

    @Test
    public void canFloor() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.floor("john")).isEqualTo(Option.of(john));
        assertThat(unit.floor("eddie")).isEqualTo(Option.of(carter));
        assertThat(unit.floor("aaa")).isEqualTo(Option.none());
    }

    @Test
    public void canFloorKey() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.floorKey("john")).isEqualTo(Option.of("john"));
        assertThat(unit.floorKey("eddie")).isEqualTo(Option.of("carter"));
        assertThat(unit.floor("aaa")).isEqualTo(Option.none());
    }

    // Ceiling

    @Test
    public void canCeiling() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.ceiling("john")).isEqualTo(Option.of(john));
        assertThat(unit.ceiling("eddie")).isEqualTo(Option.of(john));
        assertThat(unit.ceiling("xxx")).isEqualTo(Option.none());
    }

    @Test
    public void canCeilingKey() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.ceilingKey("john")).isEqualTo(Option.of("john"));
        assertThat(unit.ceilingKey("eddie")).isEqualTo(Option.of("john"));
        assertThat(unit.ceilingKey("xxx")).isEqualTo(Option.none());
    }

    // Lower

    @Test
    public void canLower() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.lower("john")).isEqualTo(Option.of(carter));
        assertThat(unit.lower("eddie")).isEqualTo(Option.of(carter));
        assertThat(unit.lower("aaa")).isEqualTo(Option.none());
    }

    @Test
    public void canLowerKey() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.lowerKey("john")).isEqualTo(Option.of("carter"));
        assertThat(unit.lowerKey("eddie")).isEqualTo(Option.of("carter"));
        assertThat(unit.lowerKey("aaa")).isEqualTo(Option.none());
    }

    // Higher

    @Test
    public void canHigher() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.higher("carter")).isEqualTo(Option.of(john));
        assertThat(unit.higher("eddie")).isEqualTo(Option.of(john));
        assertThat(unit.higher("xxx")).isEqualTo(Option.none());
    }

    @Test
    public void canHigherKey() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.higherKey("carter")).isEqualTo(Option.of("john"));
        assertThat(unit.higherKey("eddie")).isEqualTo(Option.of("john"));
        assertThat(unit.higherKey("xxx")).isEqualTo(Option.none());
    }

    // Min / Max

    @Test
    public void givenTreeMap_ThenCanMinMax() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.min()).isEqualTo(Option.of(aaron));
        assertThat(unit.max()).isEqualTo(Option.of(simon));
    }

    @Test
    public void givenTreeMapEmpty_ThenMinMaxReturnsNone() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        assertThat(unit.min()).isEqualTo(Option.none());
        assertThat(unit.max()).isEqualTo(Option.none());
    }

    // Head / Last

    @Test
    public void givenTreeMap_ThenCanReturnHeadAndLast() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.head()).isEqualTo(aaron);
        assertThat(unit.last()).isEqualTo(simon);
    }

    @Test
    public void givenTreeMapEmpty_WhenHeadOrTail_ThenThrowExceptionIfHeadLastNotFound() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        Assertions.assertThatThrownBy(unit::head).isInstanceOf(NoSuchElementException.class);
        Assertions.assertThatThrownBy(unit::last).isInstanceOf(NoSuchElementException.class);
    }

    // Descending Iterator, Key Set and Map

    @Test
    public void givenTreeMap_ThenCanReturnDescendingIterator() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, simon, john, carter, nancy);
        assertThat(unit.iterator().toList()).isEqualTo(List.of(simon, nancy, john, carter, aaron));
    }

    @Test
    public void givenEmptyTreeMap_ThenCanReturnDescendingIterator() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingIterator().toList()).isEqualTo(List.empty());
    }

    @Test
    public void givenTreeMap_ThenCanReturnDescendingTreeSet() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, simon, john, carter, nancy);
        assertThat(unit.descendingKeySet().toList()).isEqualTo(List.of("simon", "nancy", "john", "carter", "aaron"));
    }

    @Test
    public void givenEmptyTreeMap_ThenCanReturnDescendingKeySet() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingKeySet()).isEqualTo(TreeSet.empty());
    }

    // Descending Maps

    @Test
    public void shouldReturnDescendingMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, simon, john, carter, nancy);
        assertThat(unit.descendingMap().keySet().toList()).isEqualTo(List.of("aaron", "carter", "john", "nancy", "simon"));
        assertThat(unit.descendingMap()).isEqualTo(TreeMap.ofEntries(aaron, carter, john, nancy, simon));
    }

    @Test
    public void givenEmptyMap_ThenCanReturnDescendingMap() {
        final TreeMap<String, Integer> unit = TreeMap.empty();
        assertThat(unit.descendingMap()).isEqualTo(TreeMap.empty());
    }

    @Test
    public void givenDescendingMap_ThenCanSubMap() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.descendingMap().subMap("nancy", true, "carter", true)).isEqualTo(TreeMap.ofEntries(nancy, john, carter));
        assertThat(unit.descendingMap().headMap("john", true)).isEqualTo(TreeMap.ofEntries(simon, nancy, john));
        assertThat(unit.descendingMap().tailMap("john", true)).isEqualTo(TreeMap.ofEntries(john, carter, aaron));
    }

    @Test
    public void givenDescendingMap_ThenCanNavigate() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        assertThat(unit.descendingMap().floorKey("eddie")).isEqualTo(Option.of("john"));
        assertThat(unit.descendingMap().ceilingKey("eddie")).isEqualTo(Option.of("carter"));
        assertThat(unit.descendingMap().higherKey("john")).isEqualTo(Option.of("carter"));
        assertThat(unit.descendingMap().lowerKey("john")).isEqualTo(Option.of("nancy"));
        assertThat(unit.descendingMap().min()).isEqualTo(Option.of(simon));
        assertThat(unit.descendingMap().max()).isEqualTo(Option.of(aaron));
        assertThat(unit.descendingMap().head()).isEqualTo(simon);
        assertThat(unit.descendingMap().last()).isEqualTo(aaron);
    }

    @Test
    public void givenDescendingMap_WhenCreateSubMapWithFromKeyLowerThanToKey_ThenThrowException() {
        final TreeMap<String, Integer> unit = TreeMap.ofEntries(aaron, carter, john, nancy, simon);
        Assertions.assertThatThrownBy(() -> unit.descendingMap().subMap(Option.of("aaron"), true, Option.of("from"), false)).isInstanceOf(IllegalArgumentException.class);
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
