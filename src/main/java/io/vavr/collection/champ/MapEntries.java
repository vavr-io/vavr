package io.vavr.collection.champ;


import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Static utility-methods for creating a list of map entries.
 */
public class MapEntries {
    /**
     * Don't let anyone instantiate this class.
     */
    private MapEntries() {
    }


    /**
     * Returns a list containing 0  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @return a list containing the entries
     */
    public static @NonNull <K, V> ArrayList<Map.Entry<K, V>> of() {
        return new ArrayList<>();
    }

    /**
     * Returns a list containing 1 map entry.
     * <p>
     * Key and value can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        return l;
    }


    /**
     * Returns a list containing 2  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        return l;
    }

    /**
     * Returns a list containing 3  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        return l;
    }


    /**
     * Returns a list containing 4  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        return l;
    }

    /**
     * Returns a list containing 5  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        return l;
    }

    /**
     * Returns a list containing 6  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @param k6  key 6
     * @param v6  value 6
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                                           K k6, V v6) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k6, v6));
        return l;
    }

    /**
     * Returns a list containing 7  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @param k6  key 6
     * @param v6  value 6
     * @param k7  key 7
     * @param v7  value 7
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                                           K k6, V v6, K k7, V v7) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k6, v6));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k7, v7));
        return l;
    }

    /**
     * Returns a list containing 8  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @param k6  key 6
     * @param v6  value 6
     * @param k7  key 7
     * @param v7  value 7
     * @param k8  key 8
     * @param v8  value 8
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                                           K k6, V v6, K k7, V v7, K k8, V v8) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k6, v6));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k7, v7));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k8, v8));
        return l;
    }

    /**
     * Returns a list containing 9  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param <K> the key type
     * @param <V> the value type
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @param k6  key 6
     * @param v6  value 6
     * @param k7  key 7
     * @param v7  value 7
     * @param k8  key 8
     * @param v8  value 8
     * @param k9  key 9
     * @param v9  value 9
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                                           K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k6, v6));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k7, v7));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k8, v8));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k9, v9));
        return l;
    }

    /**
     * Returns a list containing 10  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param k1  key 1
     * @param v1  value 1
     * @param k2  key 2
     * @param v2  value 2
     * @param k3  key 3
     * @param v3  value 3
     * @param k4  key 4
     * @param v4  value 4
     * @param k5  key 5
     * @param v5  value 5
     * @param k6  key 6
     * @param v6  value 6
     * @param k7  key 7
     * @param v7  value 7
     * @param k8  key 8
     * @param v8  value 8
     * @param k9  key 9
     * @param v9  value 9
     * @param k10 key 10
     * @param v10 value 10
     * @param <K> the key type
     * @param <V> the value type
     * @return a list containing the entries
     */
    public static @NonNull <K, V> List<Map.Entry<K, V>> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5,
                                                           K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
        ArrayList<Map.Entry<K, V>> l = new ArrayList<>();
        l.add(new AbstractMap.SimpleImmutableEntry<>(k1, v1));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k2, v2));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k3, v3));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k4, v4));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k5, v5));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k6, v6));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k7, v7));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k8, v8));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k9, v9));
        l.add(new AbstractMap.SimpleImmutableEntry<>(k10, v10));
        return l;
    }

    /**
     * Returns a list containing the specified  map entries.
     * <p>
     * Keys and values can be null.
     *
     * @param entries the entries
     * @param <K>     the key type
     * @param <V>     the value type
     * @return a list containing the entries
     */
    @SafeVarargs
    @SuppressWarnings({"varargs", "unchecked"})
    public static <K, V> @NonNull List<Map.Entry<K, V>> ofEntries(Map.Entry<? extends K, ? extends V>... entries) {
        return (List<Map.Entry<K, V>>) (List<?>) Arrays.asList(entries);
    }

    /**
     * Creates a new linked hash map from a list of entries.
     *
     * @param l   a list of entries
     * @param <K> the key type
     * @param <V> the value type
     * @return a new linked hash map
     */
    public static <K, V> java.util.LinkedHashMap<K, V> linkedHashMap(@NonNull List<? extends Map.Entry<? extends K, ? extends V>> l) {
        return map(LinkedHashMap::new, l);
    }

    /**
     * Creates a new map from a list of entries.
     *
     * @param l   a list of entries
     * @param <K> the key type
     * @param <V> the value type
     * @return a new linked hash map
     */
    public static <K, V, M extends Map<K, V>> M map(@NonNull Supplier<M> factory, @NonNull List<? extends Map.Entry<? extends K, ? extends V>> l) {
        M m = factory.get();
        for (Map.Entry<? extends K, ? extends V> entry : l) {
            m.put(entry.getKey(), entry.getValue());
        }
        return m;
    }

    /**
     * Creates a new map entry.
     * <p>
     * Key and value can be null.
     *
     * @param k   the key
     * @param v   the value
     * @param <K> the key type
     * @param <V> the value type
     * @return a new map entry
     */
    public static <K, V> Map.@NonNull Entry<K, V> entry(K k, V v) {
        return new AbstractMap.SimpleEntry<>(k, v);
    }
}
