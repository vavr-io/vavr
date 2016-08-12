/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Random;

/**
 * Internal class, containing helpers.
 *
 * @author Pap Lőrinc
 * @since 3.0.0
 */
@SuppressWarnings({"unchecked", "SuspiciousArrayCast"})
final class Arrays2 { // TODO reuse these in `Array` also
    private static final Object[] EMPTY = {};

    static <T> Object[] emptyArray() { return EMPTY; }

    /** Repeatedly group an array into equal sized sub-trees */
    static Object grouped(Object array, int length, int groupSize) {
        Class<?> type = toPrimitive(get(array, 0).getClass());

        final int firstSize = Math.min(groupSize, length);
        Object results = newInstance(type, firstSize);
        System.arraycopy(array, 0, results, 0, firstSize);

        if (firstSize < getLength(array)) {
            final Object[] parentArray = new Object[1 + ((length - 1) / groupSize)];
            parentArray[0] = results;

            for (int start = firstSize, i = 1; start < getLength(array); i++) {
                int nextSize = Math.min(groupSize, length - (i * groupSize));
                Object next = newInstance(type, nextSize);
                System.arraycopy(array, start, next, 0, nextSize);
                parentArray[i] = next;
                start += nextSize;
            }

            results = parentArray;
        }

        return results;
    }

    /** Store the content of an iterable in an array */
    static <T> Object asArray(java.util.Iterator<T> it, int length) {
        assert length > 0;

        Object first = it.next();
        final Class<?> type = toPrimitive(first.getClass());
        Object array = newInstance(type, length);
        set(array, 0, first);

        for (int i = 1; i < length; i++) {
            set(array, i, it.next());
        }
        return array;
    }

    static Object asArray(Class<?> type, Object element) {
        Object newTrailing = newInstance(type, 1);
        set(newTrailing, 0, element);
        return newTrailing;
    }

    static Object newInstance(Class<?> type, int size) {
        if (int.class.equals(type)) {
            return new int[size];
        } else {
            return new Object[size];
        }
    }
    static int getLength(Object array) {
        if (array instanceof int[]) {
            final int[] intArray = (int[]) array;
            return intArray.length;
        } else {
            final Object[] objectArray = (Object[]) array;
            return objectArray.length;
        }
    }
    static Object get(Object array, int index) {
        if (array instanceof int[]) {
            final int[] intArray = (int[]) array;
            return intArray[index];
        } else {
            final Object[] objectArray = (Object[]) array;
            return objectArray[index];
        }
    }
    static void set(Object array, int index, Object value) {
        if (array instanceof int[]) {
            final int[] intArray = (int[]) array;
            intArray[index] = (Integer) value;
        } else {
            final Object[] objectArray = (Object[]) array;
            objectArray[index] = value;
        }
    }

    static Object toPrimitiveArray(Class<?> type, Object[] array) {
        Object results = newInstance(type, array.length);
        for (int i = 0; i < array.length; i++) {
            set(results, i, array[i]);
        }
        return results;
    }

    /* convert to primitive */
    private static final Class<?>[] WRAPPERS = {Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Void.class};
    private static final Class<?>[] PRIMITIVES = {boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, void.class};

    static Class<?> toPrimitive(Class<?> wrapper) {
        final int i = primitiveIndex(wrapper);
        return (i < 0) ? wrapper
                       : PRIMITIVES[i];
    }

    private static int primitiveIndex(Class<?> wrapper) { /* linear search is faster than binary search here */
        for (int j = 0; j < WRAPPERS.length; j++) {
            if (wrapper == WRAPPERS[j]) {
                return j;
            }
        }
        return -1;
    }

    /** Randomly mutate array positions */
    static <T> int[] shuffle(int[] array, Random random) {
        for (int i = array.length; i > 1; i--) {
            swap(array, i - 1, random.nextInt(i));
        }
        return array;
    }

    static <T> void swap(int[] array, int i, int j) {
        final int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
