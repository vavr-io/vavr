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
        Class<?> type = toPrimitive(array.getClass().getComponentType());

        final int firstSize = Math.min(groupSize, length);
        Object results = newInstance(type, firstSize);
        System.arraycopy(array, 0, results, 0, firstSize);

        if (firstSize < length) {
            final Object[] parentArray = new Object[1 + ((length - 1) / groupSize)];
            parentArray[0] = results;

            for (int start = firstSize, i = 1; start < length; i++) {
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

    static <T> Object drop(Class<?> type, Object array, int index) {
        final int length = getLength(type, array);
        Object copy = newInstance(type, length);
        System.arraycopy(array, index, copy, index, length - index);
        return copy;
    }

    /** Store the content of an iterable in an array */
    static <T> Object asArray(java.util.Iterator<T> it, int length) {
        assert length > 0;

        Object first = it.next();
        Object[] array = new Object[length];
        array[0] = first;

        for (int i = 1; i < length; i++) {
            array[i] = it.next();
        }
        return array;
    }

    static Object asArray(Class<?> type, Object element) {
        Object newTrailing = newInstance(type, 1);
        set(type, newTrailing, 0, element);
        return newTrailing;
    }

    static Object newInstance(Class<?> type, int size) {
        if (type.isPrimitive()) {
            if (int.class.equals(type)) return new int[size];
            else if (char.class.equals(type)) return new char[size];
            else if (byte.class.equals(type)) return new byte[size];
            else if (long.class.equals(type)) return new long[size];
            else if (double.class.equals(type)) return new double[size];
            else return java.lang.reflect.Array.newInstance(type, size);
        } else {
            return new Object[size];
        }
    }

    static int getLength(Class<?> type, Object array) {
        if (type.isPrimitive()) {
            if (array instanceof int[]) return ((int[]) array).length;
            else if (array instanceof char[]) return ((char[]) array).length;
            else if (array instanceof byte[]) return ((byte[]) array).length;
            else if (array instanceof long[]) return ((long[]) array).length;
            else if (array instanceof double[]) return ((double[]) array).length;
            else return java.lang.reflect.Array.getLength(array);
        } else {
            final Object[] objectArray = (Object[]) array;
            return objectArray.length;
        }
    }

    static <T> T get(Class<?> type, Object array, int index) {
        if (type.isPrimitive()) {
            if (array instanceof int[]) return (T) (Object) ((int[]) array)[index];
            else if (array instanceof char[]) return (T) (Object) ((char[]) array)[index];
            else if (array instanceof byte[]) return (T) (Object) ((byte[]) array)[index];
            else if (array instanceof long[]) return (T) (Object) ((long[]) array)[index];
            else if (array instanceof double[]) return (T) (Object) ((double[]) array)[index];
            else return (T) java.lang.reflect.Array.get(array, index);
        } else {
            final Object[] objectArray = (Object[]) array;
            return (T) objectArray[index];
        }
    }

    static void set(Class<?> type, Object array, int index, Object value) {
        if (type.isPrimitive()) {
            if (array instanceof int[]) ((int[]) array)[index] = (int) value;
            else if (array instanceof char[]) ((char[]) array)[index] = (char) value;
            else if (array instanceof byte[]) ((byte[]) array)[index] = (byte) value;
            else if (array instanceof long[]) ((long[]) array)[index] = (long) value;
            else if (array instanceof double[]) ((double[]) array)[index] = (double) value;
            else java.lang.reflect.Array.set(array, index, value);
        } else {
            final Object[] objectArray = (Object[]) array;
            objectArray[index] = value;
        }
    }

    static Object toPrimitiveArray(Class<?> type, Object[] array) {
        Object results = newInstance(type, array.length);
        for (int i = 0; i < array.length; i++) {
            set(type, results, i, array[i]);
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
