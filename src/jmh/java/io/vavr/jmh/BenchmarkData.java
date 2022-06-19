package io.vavr.jmh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("JmhInspections")
public class BenchmarkData {
    /** List 'a'.
     * <p>
     * The elements have been shuffled, so that they
     * are not in contiguous memory addresses.
      */
    public final List<Key> listA;
    /** Set 'a'.
     */
    public final Set<Key> setA;
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
        Set<Integer> preventDuplicates=new HashSet<>();
        ArrayList<Key> keysInSet=new ArrayList<>();
        ArrayList<Key> keysNotInSet=new ArrayList<>();
        for (int i=0;i<size;i++){
            keysInSet.add(createKey(rng,preventDuplicates,mask));
            keysNotInSet.add(createKey(rng,preventDuplicates,mask));
        }
        setA=new HashSet<>(keysInSet);
        Collections.shuffle(keysInSet);
        Collections.shuffle(keysNotInSet);
        this.listA =Collections.unmodifiableList(keysInSet);
        this.listB =Collections.unmodifiableList(keysNotInSet);
    }
    private Key createKey(Random rng, Set<Integer> preventDuplicates,int mask){
        int candidate = rng.nextInt();
        while (!preventDuplicates.add(candidate)) {
            candidate=rng.nextInt();
        }
        return new Key(candidate,mask);
    }
    public Key nextKeyInA() {
        index = index < size - 1 ? index+1 : 0;
        return listA.get(index);
    }
    public Key nextKeyInB() {
        index = index < size - 1 ? index+1 : 0;
        return listA.get(index);
    }
}
