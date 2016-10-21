/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * Internal class, containing Java array manipulation helpers.
 * Many arrays are represented as simple Objects, to avoid casts at the client site, and to work with all types of arrays, including primitive ones.
 *
 * @author Pap Lőrinc
 * @since 2.1.0
 */
final class ArrayType {
    private static final Object[] EMPTY = {};
    static Object empty() { return EMPTY; }

    @SuppressWarnings("unchecked")
    static <T> T newInstance(int length) { return (T) copy(empty(), length); }

    /** Create a single element array */
    static Object asArray(Object element) {
        final Object copy = newInstance(1);
        setAt(copy, 0, element);
        return copy;
    }

    /** Store the content of an iterable in an array */
    static Object asArray(java.util.Iterator<?> it, int length) {
        final Object array = newInstance(length);
        for (int i = 0; i < length; i++) {
            setAt(array, i, it.next());
        }
        return array;
    }

    /** System.arrayCopy with same source and destination */
    static Object copyRange(Object array, int from, int to) {
        final int length = to - from;
        return arrayCopy(length, array, from, 0, length);
    }

    /** Repeatedly group an array into equal sized sub-trees */
    static Object grouped(Object array, int groupSize) {
        final int arrayLength = lengthOf(array);
        assert arrayLength > groupSize;
        final Object results = newInstance(1 + ((arrayLength - 1) / groupSize));
        setAt(results, 0, copyRange(array, 0, groupSize));

        for (int start = groupSize, i = 1; start < arrayLength; i++) {
            final int nextLength = Math.min(groupSize, arrayLength - (i * groupSize));
            setAt(results, i, copyRange(array, start, start + nextLength));
            start += nextLength;
        }

        return results;
    }

    /** clone the source and set the value at the given position */
    static Object copyUpdate(Object array, int index, Object element) {
        final Object copy = copy(array, index + 1);
        setAt(copy, index, element);
        return copy;
    }

    static Object copy(Object array, int minLength) {
        final int arrayLength = (array == null) ? 0 : lengthOf(array);
        final int length = Math.max(arrayLength, minLength);
        return arrayCopy(length, array, 0, 0, arrayLength);
    }

    /** clone the source and keep everything after the index (pre-padding the values with null) */
    static Object copyDrop(Object array, int index) {
        final int length = lengthOf(array);
        return arrayCopy(length, array, index, index, length - index);
    }

    /** clone the source and keep everything before and including the index */
    static Object copyTake(Object array, int lastIndex) {
        return copyRange(array, 0, lastIndex + 1);
    }

    /** for performance reasons the array allocation and the System.arraycopy must be next to each other */
    static Object arrayCopy(int arraySize, Object source, int sourceFrom, int destinationFrom, int size) {
        if (size == 0) {
            return new Object[arraySize];
        } else {
            final Object[] result = new Object[arraySize];
            System.arraycopy(source, sourceFrom, result, destinationFrom, size);
            return result;
        }
    }

    /** array access, avoiding call-site casts */
    @SuppressWarnings("unchecked")
    static <T> T getAt(Object array, int index) { return (T) ((Object[]) array)[index]; }
    @SuppressWarnings("unchecked")
    static void setAt(Object array, int index, Object value) { ((Object[]) array)[index] = value; }
    static int lengthOf(Object array) {
        return ((Object[]) array).length;
    }
}
