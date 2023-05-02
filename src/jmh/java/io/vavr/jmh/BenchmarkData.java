package io.vavr.jmh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * This class provides collections that can be used in JMH benchmarks.
 */
@SuppressWarnings("JmhInspections")
public class BenchmarkData {
    /**
     * List 'a'.
     * <p>
     * The elements have been shuffled, so that they
     * are not in contiguous memory addresses.
     */
    public final List<Key> listA;
    private final List<Integer> indicesA;
    /**
     * Set 'a'.
     */
    public final Set<Key> setA;
    /**
     * Map 'a'.
     */
    public final Map<Key, Boolean> mapA;
    /** List 'b'.
     * <p>
     * The elements have been shuffled, so that they
     * are not in contiguous memory addresses.
     */
    public final  List<Key> listB;


    private int index;
    private final int size;

    public BenchmarkData(int size, int mask) {
        this.size=size;
        Random rng = new Random(0);
        Set<Integer> preventDuplicates=new HashSet<>(size*2);
        ArrayList<Key> keysInSet=new ArrayList<>(size);
        mapA=new HashMap<>(size*2);
        ArrayList<Key> keysNotInSet = new ArrayList<>(size);
        Map<Key, Integer> indexMap = new HashMap<>(size*2);
        for (int i = 0; i < size; i++) {
            Key key = createKey(rng, preventDuplicates, mask);
            keysInSet.add(key);
            mapA.put(key,Boolean.TRUE);
            indexMap.put(key, i);
            keysNotInSet.add(createKey(rng, preventDuplicates, mask));
        }
        setA = new HashSet<>(keysInSet);
        Collections.shuffle(keysInSet);
        Collections.shuffle(keysNotInSet);
        this.listA = Collections.unmodifiableList(keysInSet);
        this.listB = Collections.unmodifiableList(keysNotInSet);
        indicesA = new ArrayList<>(keysInSet.size());
        for (var k : keysInSet) {
            indicesA.add(indexMap.get(k));
        }
    }

    private Key createKey(Random rng, Set<Integer> preventDuplicates, int mask) {
        int candidate = rng.nextInt();
        while (!preventDuplicates.add(candidate)) {
            candidate = rng.nextInt();
        }
        return new Key(candidate, mask);
    }

    public Key nextKeyInA() {
        index = index < size - 1 ? index + 1 : 0;
        return listA.get(index);
    }

    public int nextIndexInA() {
        index = index < size - 1 ? index + 1 : 0;
        return indicesA.get(index);
    }

    public Key nextKeyInB() {
        index = index < size - 1 ? index + 1 : 0;
        return listA.get(index);
    }
}
