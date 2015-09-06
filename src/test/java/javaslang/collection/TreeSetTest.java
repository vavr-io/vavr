/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collector;

public class TreeSetTest /*TODO: extends AbstractTraversableTest*/ {
    /*
    @Override
    protected <T> Collector<T, ArrayList<T>, ? extends TreeSet<T>> collector() {
        return null;
    }

    @Override
    protected <T> TreeSet<T> empty() {
        return TreeSet.empty(naturalComparator());
    }

    @Override
    protected <T> TreeSet<T> of(T element) {
        return TreeSet.of(naturalComparator(), element);
    }

    @SafeVarargs
    @Override
    protected final <T> TreeSet<T> of(T... elements) {
        // Note: TreeSet.ofAll(Comparator, T...) does not work with T[]
        return TreeSet.ofAll(naturalComparator(), Iterator.of(elements));
    }

    @Override
    protected <T> TreeSet<T> ofAll(Iterable<? extends T> elements) {
        return TreeSet.ofAll(naturalComparator(), elements);
    }

    @Override
    protected TreeSet<Boolean> ofAll(boolean[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Byte> ofAll(byte[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Character> ofAll(char[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Double> ofAll(double[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Float> ofAll(float[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Integer> ofAll(int[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Long> ofAll(long[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    protected TreeSet<Short> ofAll(short[] array) {
        return TreeSet.ofAll(array);
    }

    @Override
    boolean isThisLazyJavaslangObject() {
        return false;
    }

    @Override
    int getPeekNonNilPerformingAnAction() {
        return 0;
    }
    */

    // Duplicate of TreeSet.naturalComparator(). Don't want to expose it.
    @SuppressWarnings("unchecked")
    private static <U> Comparator<? super U> naturalComparator() {
        return (o1, o2) -> ((Comparable<? super U>) o1).compareTo(o2);
    }
}
