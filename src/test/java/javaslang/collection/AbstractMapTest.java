/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.assertj.core.api.IterableAssert;
import org.junit.Test;

import java.util.*;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;

public abstract class AbstractMapTest extends AbstractTraversableTest {

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @Override
            public IterableAssert<T> isEqualTo(Object obj) {
                @SuppressWarnings("unchecked")
                Iterable<T> expected = (Iterable<T>) obj;
                java.util.Map<T, Integer> actualMap = countMap(actual);
                java.util.Map<T, Integer> expectedMap = countMap(expected);
                assertThat(actualMap.size()).isEqualTo(expectedMap.size());
                actualMap.forEach((k, v) -> assertThat(v).isEqualTo(expectedMap.get(k)));
                return this;
            }

            private java.util.Map<T, Integer> countMap(Iterable<? extends T> it) {
                java.util.HashMap<T, Integer> cnt = new java.util.HashMap<>();
                it.forEach(i -> cnt.merge(i, 1, (v1, v2) -> v1 + v2));
                return cnt;
            }
        };
    }

    @Override
    protected <T> Collector<T, ArrayList<T>, IntMap<T>> collector() {
        final Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector = mapCollector();
        return new Collector<T, ArrayList<T>, IntMap<T>>() {
            @Override
            public Supplier<ArrayList<T>> supplier() {
                return ArrayList::new;
            }

            @Override
            public BiConsumer<ArrayList<T>, T> accumulator() {
                return ArrayList::add;
            }

            @Override
            public BinaryOperator<ArrayList<T>> combiner() {
                return (left, right) -> fromTuples(mapCollector.combiner().apply(toTuples(left), toTuples(right)));
            }

            @Override
            public Function<ArrayList<T>, IntMap<T>> finisher() {
                return AbstractMapTest.this::ofAll;
            }

            @Override
            public Set<Characteristics> characteristics() {
                return mapCollector.characteristics();
            }

            private ArrayList<Tuple2<Integer, T>> toTuples(java.util.List<T> list) {
                final ArrayList<Tuple2<Integer, T>> result = new ArrayList<>();
                Stream.ofAll(list)
                        .zipWithIndex()
                        .map(tu -> Tuple.of(tu._2, tu._1))
                        .forEach(result::add);
                return result;
            }

            private ArrayList<T> fromTuples(java.util.List<Tuple2<Integer, T>> list) {
                final ArrayList<T> result = new ArrayList<>();
                Stream.ofAll(list)
                        .map(tu -> tu._2)
                        .forEach(result::add);
                return result;
            }
        };
    }

    @Override
    protected <T> IntMap<T> empty() {
        return IntMap.of(emptyMap());
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    private <T> Map<Integer, T> emptyInt() {
        return emptyMap();
    }

    protected Map<Integer, Integer> emptyIntInt() {
        return emptyMap();
    }

    private Map<Integer, String> emptyIntString() {
        return emptyMap();
    }

    abstract protected String className();

    abstract protected <T1, T2> Map<T1, T2> emptyMap();

    protected boolean emptyMapShouldBeSingleton() {
        return true;
    }

    abstract protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Map<Integer, T>> mapCollector();

    @SuppressWarnings("unchecked")
    abstract protected <K, V> Map<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries);

    @SuppressWarnings("unchecked")
    abstract protected <K, V> Map<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries);

    abstract protected <K, V> Map<K, V> mapOfPairs(Object... pairs);

    abstract protected <K extends Comparable<? super K>, V> Map<K, V> mapOf(K key, V value);

    abstract protected <K, V> Map<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f);

    abstract protected <K, V> Map<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s);

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> IntMap<T> of(T element) {
        Map<Integer, T> map = emptyMap();
        map = map.put(0, element);
        return IntMap.of(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> IntMap<T> of(T... elements) {
        Map<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return IntMap.of(map);
    }

    @Override
    protected <T> IntMap<T> ofAll(Iterable<? extends T> elements) {
        Map<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return IntMap.of(map);
    }

    @Override
    protected IntMap<Boolean> ofAll(boolean[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Byte> ofAll(byte[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Character> ofAll(char[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Double> ofAll(double[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Float> ofAll(float[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Integer> ofAll(int[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Long> ofAll(long[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMap<Short> ofAll(short[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected <T> IntMap<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Map<Integer, T> map = emptyMap();
        for (int i = 0; i < n; i++) {
            map = map.put(map.size(), f.apply(i));
        }
        return IntMap.of(map);
    }

    @Override
    protected <T> IntMap<T> fill(int n, Supplier<? extends T> s) {
        return tabulate(n, anything -> s.get());
    }

    // -- construction

    @Test
    public void shouldBeTheSame() {
        assertThat(mapOf(1, 2)).isEqualTo(emptyMap().put(1, 2));
    }

    private static java.util.Map.Entry<String, Integer> entry(String key, Integer value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConstructFromEntries() {
        Map<String, Integer> map = mapOfEntries(entry("1", 1), entry("2", 2), entry("3", 3));
        assertThat(map).isEqualTo(emptyMap().put("1", 1).put("2", 2).put("3", 3));
    }

    @Test
    public void shouldConstructFromPairs() {
        Map<String, Integer> map = mapOfPairs("1", 1, "2", 2, "3", 3);
        assertThat(map).isEqualTo(emptyMap().put("1", 1).put("2", 2).put("3", 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructFromOddPairsLen() {
        mapOfPairs("1", 1, "2", 2, 3);
    }

    // -- toString

    @Test
    public void shouldMakeString() {
        assertThat(emptyMap().toString()).isEqualTo(className() + "()");
        assertThat(emptyMap().put(1, 2).toString()).isEqualTo(className() + "(" + Tuple.of(1, 2) + ")");
    }

    // -- apply

    @Test
    public void shouldApplyExistingKey() {
        assertThat(emptyMap().put(1, 2).apply(1)).isEqualTo(2);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldApplyNonExistingKey() {
        emptyMap().put(1, 2).apply(3);
    }

    // -- contains

    @Test
    public void shouldFindKey() {
        assertThat(emptyMap().put(1, 2).containsKey(1)).isTrue();
        assertThat(emptyMap().put(1, 2).containsKey(2)).isFalse();
    }

    @Test
    public void shouldFindValue() {
        assertThat(emptyMap().put(1, 2).containsValue(2)).isTrue();
        assertThat(emptyMap().put(1, 2).containsValue(1)).isFalse();
    }

    @Test
    public void shouldRecognizeNotContainedKeyValuePair() {
        final TreeMap<String, Integer> testee = TreeMap.of(Tuple.of("one", 1));
        assertThat(testee.contains(Tuple.of("one", 0))).isFalse();
    }

    @Test
    public void shouldRecognizeContainedKeyValuePair() {
        final TreeMap<String, Integer> testee = TreeMap.of(Tuple.of("one", 1));
        assertThat(testee.contains(Tuple.of("one", 1))).isTrue();
    }

    // -- flatMap

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFlatMapUsingBiFunction() {
        final Map<Integer, Integer> testee = mapOfTuples(Tuple.of(1, 11), Tuple.of(2, 22), Tuple.of(3, 33));
        final Map<String, String> actual = testee
                .flatMap((k, v) -> List.of(Tuple.of(String.valueOf(k), String.valueOf(v)),
                        Tuple.of(String.valueOf(k * 10), String.valueOf(v * 10))));
        final Map<String, String> expected = mapOfTuples(Tuple.of("1", "11"), Tuple.of("10", "110"), Tuple.of("2", "22"),
                Tuple.of("20", "220"), Tuple.of("3", "33"), Tuple.of("30", "330"));
        assertThat(actual).isEqualTo(expected);
    }

    // -- keySet

    @SuppressWarnings("unchecked")
    @Test
    public void shouldReturnsKeySet() {
        final javaslang.collection.Set<Integer> actual = mapOfTuples(Tuple.of(1, 11), Tuple.of(2, 22), Tuple.of(3, 33)).keySet();
        assertThat(actual).isEqualTo(HashSet.of(1, 2, 3));
    }

    // -- map

    @Test
    public void shouldMapEmpty() {
        assertThat(emptyInt().map(Tuple2::_1)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldMapNonEmpty() {
        final javaslang.collection.Seq<Integer> expected = Vector.of(1, 2);
        final javaslang.collection.Seq<Integer> actual = emptyInt().put(1, "1").put(2, "2").map(Tuple2::_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnEmptySetWhenAskedForTuple2SetOfAnEmptyMap() {
        assertThat(emptyMap().toSet()).isEqualTo(HashSet.empty());
    }

    @Test
    public void shouldReturnTuple2SetOfANonEmptyMap() {
        assertThat(emptyMap().put(1, "1").put(2, "2").toSet()).isEqualTo(HashSet.of(Tuple.of(1, "1"), Tuple.of(2, "2")));
    }

    @Test
    public void shouldReturnModifiedValuesMap() {
        assertThat(emptyIntString().put(1, "1").put(2, "2").mapValues(Integer::parseInt)).isEqualTo(emptyMap().put(1, 1).put(2, 2));
    }

    @Test
    public void shouldReturnListWithMappedValues() {
        assertThat(emptyIntInt().put(1, 1).put(2, 2).traverse((a, b) -> a + b)).isEqualTo(List.of(2, 4));
    }

    // -- merge

    @Test
    public void shouldMerge() {
        Map<Integer, Integer> m1 = emptyIntInt().put(1, 1).put(2, 2);
        Map<Integer, Integer> m2 = emptyIntInt().put(1, 1).put(4, 4);
        Map<Integer, Integer> m3 = emptyIntInt().put(3, 3).put(4, 4);
        assertThat(emptyIntInt().merge(m2)).isEqualTo(m2);
        assertThat(m2.merge(emptyIntInt())).isEqualTo(m2);
        assertThat(m1.merge(m2)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(4, 4));
        assertThat(m1.merge(m3)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(3, 3).put(4, 4));
    }

    @Test
    public void shouldMergeCollisions() {
        Map<Integer, Integer> m1 = emptyIntInt().put(1, 1).put(2, 2);
        Map<Integer, Integer> m2 = emptyIntInt().put(1, 2).put(4, 4);
        Map<Integer, Integer> m3 = emptyIntInt().put(3, 3).put(4, 4);
        assertThat(emptyIntInt().merge(m2, Math::max)).isEqualTo(m2);
        assertThat(m2.merge(emptyIntInt(), Math::max)).isEqualTo(m2);
        assertThat(m1.merge(m2, Math::max)).isEqualTo(emptyIntInt().put(1, 2).put(2, 2).put(4, 4));
        assertThat(m1.merge(m3, Math::max)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(3, 3).put(4, 4));
    }

    // -- equality

    @Test
    public void shouldIgnoreOrderOfEntriesWhenComparingForEquality() {
        final Map<?, ?> map1 = emptyMap().put(1, 'a').put(2, 'b').put(3, 'c');
        final Map<?, ?> map2 = emptyMap().put(3, 'c').put(2, 'b').put(1, 'a').remove(2).put(2, 'b');
        assertThat(map1).isEqualTo(map2);
    }

    // -- put

    @Test
    public void shouldPutTuple() {
        assertThat(emptyIntInt().put(Tuple.of(1, 2))).isEqualTo(emptyIntInt().put(1, 2));
    }

    // -- remove

    @Test
    public void shouldRemoveKey() {
        Map<Integer, Object> src = emptyInt().put(1, 'a').put(2, 'b').put(3, 'c');
        assertThat(src.remove(2)).isEqualTo(emptyMap().put(1, 'a').put(3, 'c'));
        assertThat(src.remove(33)).isSameAs(src);
    }

    // -- removeAll

    @Test
    public void shouldRemoveAllKeys() {
        Map<Integer, Object> src = emptyInt().put(1, 'a').put(2, 'b').put(3, 'c');
        assertThat(src.removeAll(List.of(1, 3))).isEqualTo(emptyMap().put(2, 'b'));
        assertThat(src.removeAll(List.of(33))).isSameAs(src);
        assertThat(src.removeAll(List.empty())).isSameAs(src);
    }

    // -- transform

    @Test
    public void shouldTransform() {
        Map<?, ?> actual = emptyIntInt().put(1, 11).transform(map -> map.put(2, 22));
        assertThat(actual).isEqualTo(emptyIntInt().put(1, 11).put(2, 22));
    }

    // -- unzip

    @Test
    public void shouldUnzipNil() {
        assertThat(emptyMap().unzip(x -> Tuple.of(x, x))).isEqualTo(Tuple.of(Stream.empty(), Stream.empty()));
        assertThat(emptyMap().unzip((k, v) -> Tuple.of(Tuple.of(k, v), Tuple.of(k, v))))
                .isEqualTo(Tuple.of(Stream.empty(), Stream.empty()));
    }

    @Test
    public void shouldUnzipNonNil() {
        Map<Integer, Integer> map = emptyIntInt().put(0, 0).put(1, 1);
        final Tuple actual = map.unzip(entry -> Tuple.of(entry._1, entry._2 + 1));
        final Tuple expected = Tuple.of(Stream.of(0, 1), Stream.of(1, 2));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldUnzip3Nil() {
        assertThat(emptyMap().unzip3(x -> Tuple.of(x, x, x))).isEqualTo(Tuple.of(Stream.empty(), Stream.empty(), Stream.empty()));
        assertThat(emptyMap().unzip3((k, v) -> Tuple.of(Tuple.of(k, v), Tuple.of(k, v), Tuple.of(k, v))))
                .isEqualTo(Tuple.of(Stream.empty(), Stream.empty(), Stream.empty()));
    }

    @Test
    public void shouldUnzip3NonNil() {
        Map<Integer, Integer> map = emptyIntInt().put(0, 0).put(1, 1);
        final Tuple actual = map.unzip3(entry -> Tuple.of(entry._1, entry._2 + 1, entry._2 + 5));
        final Tuple expected = Tuple.of(Stream.of(0, 1), Stream.of(1, 2), Stream.of(5, 6));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @Test
    public void shouldZipNils() {
        final Seq<Tuple2<Tuple2<Object, Object>, Object>> actual = emptyMap().zip(List.empty());
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Seq<Tuple2<Tuple2<Object, Object>, Integer>> actual = emptyMap().zip(List.of(1));
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipNonEmptyAndNil() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Object>> actual = emptyIntInt().put(0, 1).zip(List.empty());
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipNonNilsIfThisIsSmaller() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Integer>> actual = emptyIntInt()
                .put(0, 0)
                .put(1, 1)
                .zip(List.of(5, 6, 7));
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(Tuple.of(0, 0), 5), Tuple.of(Tuple.of(1, 1), 6)));
    }

    @Test
    public void shouldZipNonNilsIfThatIsSmaller() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Integer>> actual = emptyIntInt()
                .put(0, 0)
                .put(1, 1)
                .put(2, 2)
                .zip(List.of(5, 6));
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(Tuple.of(0, 0), 5), Tuple.of(Tuple.of(1, 1), 6)));
    }

    @Test
    public void shouldZipNonNilsOfSameSize() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Integer>> actual = emptyIntInt()
                .put(0, 0)
                .put(1, 1)
                .put(2, 2)
                .zip(List.of(5, 6, 7));
        assertThat(actual).isEqualTo(
                Stream.of(Tuple.of(Tuple.of(0, 0), 5), Tuple.of(Tuple.of(1, 1), 6), Tuple.of(Tuple.of(2, 2), 7)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipWithThatIsNull() {
        emptyMap().zip(null);
    }

    // -- zipWithIndex

    @Test
    public void shouldZipNilWithIndex() {
        assertThat(emptyMap().zipWithIndex()).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipNonNilWithIndex() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Integer>> actual = emptyIntInt()
                .put(0, 0)
                .put(1, 1)
                .put(2, 2)
                .zipWithIndex();
        assertThat(actual).isEqualTo(
                Stream.of(Tuple.of(Tuple.of(0, 0), 0), Tuple.of(Tuple.of(1, 1), 1), Tuple.of(Tuple.of(2, 2), 2)));
    }

    // -- zipAll

    @Test
    public void shouldZipAllNils() {
        final Seq<Tuple2<Tuple2<Object, Object>, Object>> actual = emptyMap().zipAll(empty(), null, null);
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        final Seq<Tuple2<Tuple2<Object, Object>, Object>> actual = emptyMap().zipAll(List.of(1), null, null);
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(null, 1)));
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Seq<Tuple2<Tuple2<Object, Object>, Object>> actual = emptyMap().put(0, 1).zipAll(empty(), null, null);
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(Tuple.of(0, 1), null)));
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Seq<Tuple2<Tuple2<Object, Object>, String>> actual = emptyMap()
                .put(1, 1)
                .put(2, 2)
                .zipAll(of("a", "b", "c"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(9, 10), "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsMoreSmaller() {
        final Seq<Tuple2<Tuple2<Object, Object>, String>> actual = emptyMap()
                .put(1, 1)
                .put(2, 2)
                .zipAll(of("a", "b", "c", "d"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(9, 10), "c"), Tuple.of(Tuple.of(9, 10), "d"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Seq<Tuple2<Tuple2<Object, Object>, String>> actual = emptyMap()
                .put(1, 1)
                .put(2, 2)
                .put(3, 3)
                .zipAll(this.of("a", "b"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(3, 3), "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsMoreSmaller() {
        final Seq<Tuple2<Tuple2<Object, Object>, String>> actual = emptyMap()
                .put(1, 1)
                .put(2, 2)
                .put(3, 3)
                .put(4, 4)
                .zipAll(of("a", "b"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(3, 3), "z"), Tuple.of(Tuple.of(4, 4), "z"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsOfSameSize() {
        final Seq<Tuple2<Tuple2<Object, Object>, String>> actual = emptyMap()
                .put(1, 1)
                .put(2, 2)
                .put(3, 3)
                .zipAll(of("a", "b", "c"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(3, 3), "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowIfZipAllWithThatIsNull() {
        emptyMap().zipAll(null, null, null);
    }

    // -- special cases

    @Override
    public void shouldComputeDistinctOfNonEmptyTraversable() {
        final Map<Object, Object> testee = emptyMap().put(1, 1).put(2, 2).put(3, 3);
        assertThat(testee.distinct()).isEqualTo(testee);
    }

    @Override
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(of(1, 2, 3).tailOption().get()).isEqualTo(Option.some(of(2, 3)).get());
    }

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        Map<?, ?> obj = deserialize(serialize(emptyMap()));
        final boolean actual = obj == emptyMap();
        assertThat(actual).isTrue();
    }

    @Override
    public void shouldFoldRightNonNil() {
        final String actual = of('a', 'b', 'c').foldRight("", (x, xs) -> x + xs);
        final List<String> expected = List.of('a', 'b', 'c').permutations().map(List::mkString);
        assertThat(actual).isIn(expected);
    }

    // -- forEach

    @Test
    public void forEachByKeyValue() {
        Map<Integer, Integer> map = mapOf(1, 2).put(3, 4);
        final int[] result = { 0 };
        map.forEach((k, v) -> {
            result[0] += k + v;
        });
        assertThat(result[0]).isEqualTo(10);
    }

    @Test
    public void forEachByTuple() {
        Map<Integer, Integer> map = mapOf(1, 2).put(3, 4);
        final int[] result = { 0 };
        map.forEach(t -> {
            result[0] += t._1 + t._2;
        });
        assertThat(result[0]).isEqualTo(10);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTabulateTheSeq() {
        Function<Number, Tuple2<Long, Float>> f = i -> new Tuple2<>(i.longValue(), i.floatValue());
        Map<Number, Number> map = mapTabulate(3, f);
        assertThat(map).isEqualTo(mapOfTuples(new Tuple2<>(0l, 0f), new Tuple2<>(1l, 1f), new Tuple2<>(2l, 2f)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTabulateTheSeqCallingTheFunctionInTheRightOrder() {
        java.util.LinkedList<Integer> ints = new java.util.LinkedList<>(Arrays.asList(0, 0, 1, 1, 2, 2));
        Function<Integer, Tuple2<Long, Float>> f = i -> new Tuple2<>(ints.remove().longValue(), ints.remove().floatValue());
        Map<Number, Number> map = mapTabulate(3, f);
        assertThat(map).isEqualTo(mapOfTuples(new Tuple2<>(0l, 0f), new Tuple2<>(1l, 1f), new Tuple2<>(2l, 2f)));
    }

    @Test
    public void shouldTabulateTheSeqWith0Elements() {
        assertThat(mapTabulate(0, i -> new Tuple2<>(i, i))).isEqualTo(empty());
    }

    @Test
    public void shouldTabulateTheSeqWith0ElementsWhenNIsNegative() {
        assertThat(mapTabulate(-1, i -> new Tuple2<>(i, i))).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldFillTheSeqCallingTheSupplierInTheRightOrder() {
        java.util.LinkedList<Integer> ints = new java.util.LinkedList<>(Arrays.asList(0, 0, 1, 1, 2, 2));
        Supplier<Tuple2<Long, Float>> s = () -> new Tuple2<>(ints.remove().longValue(), ints.remove().floatValue());
        Map<Number, Number> actual = mapFill(3, s);
        assertThat(actual).isEqualTo(mapOfTuples(new Tuple2<>(0l, 0f), new Tuple2<>(1l, 1f), new Tuple2<>(2l, 2f)));
    }

    @Test
    public void shouldFillTheSeqWith0Elements() {
        assertThat(mapFill(0, () -> new Tuple2<>(1, 1))).isEqualTo(empty());
    }

    @Test
    public void shouldFillTheSeqWith0ElementsWhenNIsNegative() {
        assertThat(mapFill(-1, () -> new Tuple2<>(1, 1))).isEqualTo(empty());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void mapOfTuplesShouldReturnTheSingletonEmpty() {
        if (!emptyMapShouldBeSingleton()) return;
        assertThat(mapOfTuples()).isSameAs(emptyMap());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void mapOfEntriesShouldReturnTheSingletonEmpty() {
        if (!emptyMapShouldBeSingleton()) return;
        assertThat(mapOfEntries()).isSameAs(emptyMap());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void mapOfPairsShouldReturnTheSingletonEmpty() {
        if (!emptyMapShouldBeSingleton()) return;
        assertThat(mapOfPairs()).isSameAs(emptyMap());
    }

}
