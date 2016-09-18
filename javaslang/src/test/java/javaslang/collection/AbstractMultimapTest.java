/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.Option;
import org.assertj.core.api.IterableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collector;

import static java.util.Arrays.asList;

@RunWith(Parameterized.class)
public abstract class AbstractMultimapTest extends AbstractTraversableTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return asList(new Object[][] {
                { Multimap.ContainerType.SEQ },
                { Multimap.ContainerType.SET },
                { Multimap.ContainerType.SORTED_SET }
        });
    }

    @Parameterized.Parameter
    public Multimap.ContainerType containerType;

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
    protected <T> Collector<T, ArrayList<T>, IntMultimap<T>> collector() {
        final Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector = mapCollector();
        return new Collector<T, ArrayList<T>, IntMultimap<T>>() {
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
            public Function<ArrayList<T>, IntMultimap<T>> finisher() {
                return AbstractMultimapTest.this::ofAll;
            }

            @Override
            public java.util.Set<Characteristics> characteristics() {
                return mapCollector.characteristics();
            }

            private ArrayList<Tuple2<Integer, T>> toTuples(java.util.List<T> list) {
                final ArrayList<Tuple2<Integer, T>> result = new ArrayList<>();
                Stream.ofAll(list)
                        .zipWithIndex()
                        .map(tu -> Tuple.of(tu._2.intValue(), tu._1))
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
    protected <T> IntMultimap<T> empty() {
        return IntMultimap.of(emptyMap());
    }

    @Override
    protected boolean emptyShouldBeSingleton() {
        return false;
    }

    private <T> Multimap<Integer, T> emptyInt() {
        return emptyMap();
    }

    protected Multimap<Integer, Integer> emptyIntInt() {
        return emptyMap();
    }

    private Multimap<Integer, String> emptyIntString() {
        return emptyMap();
    }

    abstract protected String className();

    abstract <T1, T2> java.util.Map<T1, T2> javaEmptyMap();

    protected String containerName() {
        switch (containerType) {
            case SEQ:
                return "List";
            case SET:
                return "HashSet";
            case SORTED_SET:
                return "TreeSet";
        }
        throw new RuntimeException();
    }

    abstract protected <T1 extends Comparable<T1>, T2> Multimap<T1, T2> emptyMap();

    protected boolean emptyMapShouldBeSingleton() {
        return true;
    }

    abstract protected <T> Collector<Tuple2<Integer, T>, ArrayList<Tuple2<Integer, T>>, ? extends Multimap<Integer, T>> mapCollector();

    @SuppressWarnings("unchecked")
    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapOfTuples(Tuple2<? extends K, ? extends V>... entries);

    @SuppressWarnings("unchecked")
    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapOfEntries(java.util.Map.Entry<? extends K, ? extends V>... entries);

    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapOfPairs(Object... pairs);

    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapOf(K key, V value);

    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapTabulate(int n, Function<? super Integer, ? extends Tuple2<? extends K, ? extends V>> f);

    abstract protected <K extends Comparable<K>, V> Multimap<K, V> mapFill(int n, Supplier<? extends Tuple2<? extends K, ? extends V>> s);

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    protected <T> IntMultimap<T> of(T element) {
        Multimap<Integer, T> map = emptyMap();
        map = map.put(0, element);
        return IntMultimap.of(map);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <T> IntMultimap<T> of(T... elements) {
        Multimap<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return IntMultimap.of(map);
    }

    @Override
    protected <T> IntMultimap<T> ofAll(Iterable<? extends T> elements) {
        Multimap<Integer, T> map = emptyMap();
        for (T element : elements) {
            map = map.put(map.size(), element);
        }
        return IntMultimap.of(map);
    }

    @Override
    protected <T> IntMultimap<T> ofJavaStream(java.util.stream.Stream<? extends T> javaStream) {
        return ofAll(Iterator.ofAll(javaStream.iterator()));
    }

    @Override
    protected IntMultimap<Boolean> ofAll(boolean[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Byte> ofAll(byte[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Character> ofAll(char[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Double> ofAll(double[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Float> ofAll(float[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Integer> ofAll(int[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Long> ofAll(long[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected IntMultimap<Short> ofAll(short[] array) {
        return ofAll(Iterator.ofAll(array));
    }

    @Override
    protected <T> IntMultimap<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Multimap<Integer, T> map = emptyMap();
        for (int i = 0; i < n; i++) {
            map = map.put(map.size(), f.apply(i));
        }
        return IntMultimap.of(map);
    }

    @Override
    protected <T> IntMultimap<T> fill(int n, Supplier<? extends T> s) {
        return tabulate(n, anything -> s.get());
    }

    // -- construction

    @Test
    public void shouldBeTheSame() {
        assertThat(mapOf(1, 2)).isEqualTo(emptyIntInt().put(1, 2));
    }

    private static java.util.Map.Entry<String, Integer> entry(String key, Integer value) {
        return new java.util.AbstractMap.SimpleEntry<>(key, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConstructFromEntries() {
        Multimap<String, Integer> map = mapOfEntries(entry("1", 1), entry("2", 2), entry("3", 3));
        assertThat(map).isEqualTo(this.<String, Integer>emptyMap().put("1", 1).put("2", 2).put("3", 3));
    }

    @Test
    public void shouldConstructFromPairs() {
        Multimap<String, Integer> map = mapOfPairs("1", 1, "2", 2, "3", 3);
        assertThat(map).isEqualTo(this.<String, Integer>emptyMap().put("1", 1).put("2", 2).put("3", 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldFailConstructFromOddPairsLen() {
        mapOfPairs("1", 1, "2", 2, 3);
    }

    // -- toString

    @Test
    public void shouldMakeString() {
        assertThat(emptyMap().toString()).isEqualTo(className() + "()");
        assertThat(emptyIntInt().put(1, 2).toString()).isEqualTo(className() + "(" + Tuple.of(1, 2) + ")");
    }

    // -- toJavaMap

    @Test
    public void shouldConvertToJavaMap() {
        Multimap<String, Integer> javaslang = mapOfPairs("1", 1, "2", 2, "3", 3);
        java.util.Map<String, java.util.Collection<Integer>> java = javaEmptyMap();
        java.put("1", javaListOf(1));
        java.put("2", javaListOf(2));
        java.put("3", javaListOf(3));
        assertThat(javaslang.toJavaMap()).isEqualTo(java);
    }

    private java.util.Collection<Integer> javaListOf(Integer i) {
        java.util.Collection<Integer> list;
        switch (containerType) {
            case SEQ:
                list = new ArrayList<>();
                break;
            case SET:
                list = new java.util.HashSet<>();
                break;
            case SORTED_SET:
                list = new java.util.TreeSet<>();
                break;
            default:
                throw new RuntimeException();
        }
        list.add(i);
        return list;
    }

    // -- apply

    @Test
    public void shouldApplyExistingKey() {
        assertThat(emptyIntInt().put(1, 2).apply(1)).isEqualTo(HashSet.of(2));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldApplyNonExistingKey() {
        emptyIntInt().put(1, 2).apply(3);
    }

    // -- contains

    @Test
    public void shouldFindKey() {
        assertThat(emptyIntInt().put(1, 2).containsKey(1)).isTrue();
        assertThat(emptyIntInt().put(1, 2).containsKey(2)).isFalse();
    }

    @Test
    public void shouldFindValue() {
        assertThat(emptyIntInt().put(1, 2).containsValue(2)).isTrue();
        assertThat(emptyIntInt().put(1, 2).containsValue(1)).isFalse();
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
        final Multimap<Integer, Integer> testee = mapOfTuples(Tuple.of(1, 11), Tuple.of(2, 22), Tuple.of(3, 33));
        final Multimap<String, String> actual = testee
                .flatMap((k, v) -> List.of(Tuple.of(String.valueOf(k), String.valueOf(v)),
                        Tuple.of(String.valueOf(k * 10), String.valueOf(v * 10))));
        final Multimap<String, String> expected = mapOfTuples(Tuple.of("1", "11"), Tuple.of("10", "110"), Tuple.of("2", "22"),
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

    // -- biMap

    @Test
    public void shouldBiMapEmpty() {
        assertThat(emptyInt().bimap(i -> i + 1, o -> o)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldBiMapNonEmpty() {
        final javaslang.collection.Seq<Tuple2<Integer, String>> expected = Stream.of(Tuple.of(2, "1!"), Tuple.of(3, "2!"));
        final javaslang.collection.Seq<Tuple2<Integer, String>> actual = emptyInt().put(1, "1").put(2, "2").bimap(i -> i + 1, s -> s + "!").toStream();
        assertThat(actual).isEqualTo(expected);
    }

    // -- map

    @Test
    public void shouldMapEmpty() {
        assertThat(emptyInt().map(Tuple2::_1)).isEqualTo(Vector.empty());
    }

    @Test
    public void shouldMapNonEmpty() {
        final Seq<Integer> expected = Vector.of(1, 2);
        final Seq<Integer> actual = emptyInt().put(1, "1").put(2, "2").map(Tuple2::_1);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnEmptySetWhenAskedForTuple2SetOfAnEmptyMap() {
        assertThat(emptyMap().toSet()).isEqualTo(HashSet.empty());
    }

    @Test
    public void shouldReturnTuple2SetOfANonEmptyMap() {
        assertThat(emptyInt().put(1, "1").put(2, "2").toSet()).isEqualTo(HashSet.of(Tuple.of(1, "1"), Tuple.of(2, "2")));
    }

    @Test
    public void shouldReturnModifiedValuesMap() {
        assertThat(emptyIntString().put(1, "1").put(2, "2").mapValues(Integer::parseInt)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2));
    }

    @Test
    public void shouldReturnListWithMappedValues() {
        assertThat(emptyIntInt().put(1, 1).put(2, 2).traverse((a, b) -> a + b)).isEqualTo(List.of(2, 4));
    }

    // -- merge

    @Test
    public void shouldMerge() {
        Multimap<Integer, Integer> m1 = emptyIntInt().put(1, 1).put(2, 2);
        Multimap<Integer, Integer> m2 = emptyIntInt().put(1, 1).put(4, 4);
        Multimap<Integer, Integer> m3 = emptyIntInt().put(3, 3).put(4, 4);
        assertThat(emptyIntInt().merge(m2)).isEqualTo(m2);
        assertThat(m2.merge(emptyIntInt())).isEqualTo(m2);
        if(containerType == Multimap.ContainerType.SEQ) {
            assertThat(m1.merge(m2)).isEqualTo(emptyIntInt().put(1, 1).put(1, 1).put(2, 2).put(4, 4));
            assertThat(m1.merge(m3)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(3, 3).put(4, 4));
        } else {
            assertThat(m1.merge(m2)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(4, 4));
            assertThat(m1.merge(m3)).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(3, 3).put(4, 4));
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMergeCollisions() {
        Multimap<Integer, Integer> m1 = emptyIntInt().put(1, 1).put(2, 2);
        Multimap<Integer, Integer> m2 = emptyIntInt().put(1, 2).put(4, 4);
        Multimap<Integer, Integer> m3 = emptyIntInt().put(3, 3).put(4, 4);
        assertThat(emptyIntInt().merge(m2, (s1, s2) -> Iterator.concat(s1, s2))).isEqualTo(m2);
        assertThat(m2.merge(emptyIntInt(), (s1, s2) -> Iterator.concat(s1, s2))).isEqualTo(m2);
        assertThat(m1.merge(m2, (s1, s2) -> Iterator.concat(s1, s2))).isEqualTo(emptyIntInt().put(1, 1).put(1, 2).put(2, 2).put(4, 4));
        assertThat(m1.merge(m3, (s1, s2) -> Iterator.concat(s1, s2))).isEqualTo(emptyIntInt().put(1, 1).put(2, 2).put(3, 3).put(4, 4));
    }

    // -- equality

    @Test
    public void shouldIgnoreOrderOfEntriesWhenComparingForEquality() {
        final Multimap<?, ?> map1 = emptyInt().put(1, 'a').put(2, 'b').put(3, 'c');
        final Multimap<?, ?> map2 = emptyInt().put(3, 'c').put(2, 'b').put(1, 'a').remove(2).put(2, 'b');
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
        Multimap<Integer, Object> src = emptyInt().put(1, 'a').put(2, 'b').put(3, 'c');
        assertThat(src.remove(2)).isEqualTo(emptyInt().put(1, 'a').put(3, 'c'));
        assertThat(src.remove(33)).isSameAs(src);
    }

    // -- replace

    @Test
    public void shouldReplaceEntity() {
        Multimap<Integer, Object> actual = emptyInt().put(1, "a").put(1, "b").replace(Tuple.of(1, "a"), Tuple.of(1, "c"));
        Multimap<Integer, Object> expected = emptyInt().put(1, "c").put(1, "b");
        assertThat(actual).isEqualTo(expected);
    }

    // -- removeAll

    @Test
    public void shouldRemoveAllKeys() {
        Multimap<Integer, Object> src = emptyInt().put(1, 'a').put(2, 'b').put(3, 'c');
        assertThat(src.removeAll(List.of(1, 3))).isEqualTo(emptyInt().put(2, 'b'));
        assertThat(src.removeAll(List.of(33))).isSameAs(src);
        assertThat(src.removeAll(List.empty())).isSameAs(src);
    }

    // -- transform

    @Test
    public void shouldTransform() {
        Multimap<?, ?> actual = emptyIntInt().put(1, 11).transform(map -> map.put(2, 22));
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
        Multimap<Integer, Integer> map = emptyIntInt().put(0, 0).put(1, 1);
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
        Multimap<Integer, Integer> map = emptyIntInt().put(0, 0).put(1, 1);
        final Tuple actual = map.unzip3(entry -> Tuple.of(entry._1, entry._2 + 1, entry._2 + 5));
        final Tuple expected = Tuple.of(Stream.of(0, 1), Stream.of(1, 2), Stream.of(5, 6));
        assertThat(actual).isEqualTo(expected);
    }

    // -- zip

    @Test
    public void shouldZipNils() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Object>> actual = emptyIntInt().zip(List.empty());
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipEmptyAndNonNil() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Integer>> actual = emptyIntInt().zip(List.of(1));
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
        final Seq<Tuple2<Tuple2<Integer, Integer>, Object>> actual = emptyIntInt().zipAll(empty(), null, null);
        assertThat(actual).isEqualTo(Stream.empty());
    }

    @Test
    public void shouldZipAllEmptyAndNonNil() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Object>> actual = emptyIntInt().zipAll(List.of(1), null, null);
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(null, 1)));
    }

    @Test
    public void shouldZipAllNonEmptyAndNil() {
        final Seq<Tuple2<Tuple2<Integer, Integer>, Object>> actual = emptyIntInt().put(0, 1).zipAll(empty(), null, null);
        assertThat(actual).isEqualTo(Stream.of(Tuple.of(Tuple.of(0, 1), null)));
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsSmaller() {
        final Seq<Tuple2<Tuple2<Integer, Object>, String>> actual = this.<Integer, Object>emptyMap()
                .put(1, 1)
                .put(2, 2)
                .zipAll(of("a", "b", "c"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(9, 10), "c"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThisIsMoreSmaller() {
        final Seq<Tuple2<Tuple2<Integer, Object>, String>> actual = this.<Integer, Object>emptyMap()
                .put(1, 1)
                .put(2, 2)
                .zipAll(of("a", "b", "c", "d"), Tuple.of(9, 10), "z");
        final Seq<Tuple2<Tuple2<Object, Object>, String>> expected = Stream.of(Tuple.of(Tuple.of(1, 1), "a"),
                Tuple.of(Tuple.of(2, 2), "b"), Tuple.of(Tuple.of(9, 10), "c"), Tuple.of(Tuple.of(9, 10), "d"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldZipAllNonNilsIfThatIsSmaller() {
        final Seq<Tuple2<Tuple2<Integer, Object>, String>> actual = this.<Integer, Object>emptyMap()
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
        final Seq<Tuple2<Tuple2<Integer, Object>, String>> actual = this.<Integer, Object>emptyMap()
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
        final Seq<Tuple2<Tuple2<Integer, Object>, String>> actual = this.<Integer, Object>emptyMap()
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
        final Multimap<Integer, Object> testee = this.<Integer, Object>emptyMap().put(1, 1).put(2, 2).put(3, 3);
        assertThat(testee.distinct()).isEqualTo(testee);
    }

    @Override
    public void shouldReturnSomeTailWhenCallingTailOptionOnNonNil() {
        assertThat(of(1, 2, 3).tailOption().get()).isEqualTo(Option.some(of(2, 3)).get());
    }

    @Override
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        // The empty Multimap encapsulates a container type and map type and therefore cannot be a singleton
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
        Multimap<Integer, Integer> map = mapOf(1, 2).put(3, 4);
        final int[] result = { 0 };
        map.forEach((k, v) -> {
            result[0] += k + v;
        });
        assertThat(result[0]).isEqualTo(10);
    }

    @Test
    public void forEachByTuple() {
        Multimap<Integer, Integer> map = mapOf(1, 2).put(3, 4);
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
        Multimap<Long, Float> map = mapTabulate(3, f);
        assertThat(map).isEqualTo(mapOfTuples(new Tuple2<>(0l, 0f), new Tuple2<>(1l, 1f), new Tuple2<>(2l, 2f)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTabulateTheSeqCallingTheFunctionInTheRightOrder() {
        LinkedList<Integer> ints = new LinkedList<>(asList(0, 0, 1, 1, 2, 2));
        Function<Integer, Tuple2<Long, Float>> f = i -> new Tuple2<>(ints.remove().longValue(), ints.remove().floatValue());
        Multimap<Long, Float> map = mapTabulate(3, f);
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
        LinkedList<Integer> ints = new LinkedList<>(asList(0, 0, 1, 1, 2, 2));
        Supplier<Tuple2<Long, Float>> s = () -> new Tuple2<>(ints.remove().longValue(), ints.remove().floatValue());
        Multimap<Long, Float> actual = mapFill(3, s);
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

    /////////////////////////////////////////////////////////////////

    @Test
    public void shouldHoldEqualsElements() {
        Multimap<Integer, String> multimap = emptyMap();
        multimap = multimap.put(1, "a").put(1, "b").put(1, "b");
        if(containerType == Multimap.ContainerType.SEQ) {
            assertThat(multimap.toString()).isEqualTo(className() + "((1, a), (1, b), (1, b))");
        } else {
            assertThat(multimap.toString()).isEqualTo(className() + "((1, a), (1, b))");
        }
    }

    // -- filter

    @Test
    public void shouldBiFilterWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(20, n -> Tuple.of(n % 10, Integer.toHexString(n)));
        Pattern isDigits = Pattern.compile("^\\d+$");
        Multimap<Integer, String> dst = src.filter((k, v) -> k % 2 == 0 && isDigits.matcher(v).matches());
        assertThat(dst).isEqualTo(emptyIntString().put(0, "0").put(2, "2").put(4, "4").put(6, "6").put(6, "10").put(8, "8").put(8, "12"));
    }

    @Test
    public void shouldKeyFilterWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(20, n -> Tuple.of(n % 10, Integer.toHexString(n)));
        Multimap<Integer, String> dst = src.filterKeys(k -> k % 2 == 0);
        assertThat(dst).isEqualTo(emptyIntString().put(0, "0").put(0, "a").put(2, "2").put(2, "c").put(4, "4").put(4, "e").put(6, "6").put(6, "10").put(8, "8").put(8, "12"));
    }

    @Test
    public void shouldValueFilterWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(10, n -> Tuple.of(n % 5, Integer.toHexString(n)));
        Pattern isDigits = Pattern.compile("^\\d+$");
        Multimap<Integer, String> dst = src.filterValues(v -> isDigits.matcher(v).matches());
        assertThat(dst).isEqualTo(emptyIntString().put(0, "0").put(0, "5").put(1, "1").put(1, "6").put(2, "2").put(2, "7").put(3, "3").put(3, "8").put(4, "4").put(4, "9"));
    }

    // -- remove by filter

    @Test
    public void shouldBiRemoveWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(20, n -> Tuple.of(n % 10, Integer.toHexString(n)));
        Pattern isDigits = Pattern.compile("^\\d+$");
        Multimap<Integer, String> dst = src.removeAll((k, v) -> k % 2 == 0 && isDigits.matcher(v).matches());
        assertThat(dst).isEqualTo(emptyIntString().put(0, "a").put(1, "1").put(1, "b").put(2, "c").put(3, "3").put(3, "d").put(4, "e").put(5, "5").put(5, "f").put(7, "7").put(7, "11").put(9, "9").put(9, "13"));
    }

    @Test
    public void shouldKeyRemoveWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(20, n -> Tuple.of(n % 10, Integer.toHexString(n)));
        Multimap<Integer, String> dst = src.removeKeys(k -> k % 2 == 0);
        assertThat(dst).isEqualTo(emptyIntString().put(1, "1").put(1, "b").put(3, "3").put(3, "d").put(5, "5").put(5, "f").put(7, "7").put(7, "11").put(9, "9").put(9, "13"));
    }

    @Test
    public void shouldValueRemoveWork() throws Exception {
        Multimap<Integer, String> src = mapTabulate(20, n -> Tuple.of(n % 10, Integer.toHexString(n)));
        Pattern isDigits = Pattern.compile("^\\d+$");
        Multimap<Integer, String> dst = src.removeValues(v -> isDigits.matcher(v).matches());
        assertThat(dst).isEqualTo(emptyIntString().put(0, "a").put(1, "b").put(2, "c").put(3, "d").put(4, "e").put(5, "f"));
    }

    // -- getOrElse

    @Test
    public void shouldReturnDefaultValue() {
        final Multimap<String, String> map = mapOf("1", "a").put("2", "b");
        assertThat(map.getOrElse("3", List.of("3"))).isEqualTo(List.of("3"));
    }
}
