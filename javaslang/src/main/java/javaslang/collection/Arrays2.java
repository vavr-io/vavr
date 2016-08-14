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

    static <T> T[] emptyArray()                { return (T[]) EMPTY; }
    static boolean isNullOrEmpty(Object array) { return (array == null) || (getLength(array) == 0); }

    static <T> T getOrDefault(Object array, int i, T defaultValue) {
        return (isNullOrEmpty(array) || (i < 0) || (i >= getLength(array)))
               ? defaultValue
               : (T) getAt(array, i);
    }

    /** Repeatedly group an array into equal sized sub-trees */
    static Object grouped(Object array, int length, int groupSize) {
        final Class<?> type = arrayType(array);

        final int firstSize = Math.min(groupSize, length);
        Object results = newInstance(type, firstSize);
        System.arraycopy(array, 0, results, 0, firstSize);

        if (firstSize < length) {
            final Object[] parentArray = new Object[1 + ((length - 1) / groupSize)];
            parentArray[0] = results;

            for (int start = firstSize, i = 1; start < length; i++) {
                final int nextSize = Math.min(groupSize, length - (i * groupSize));
                final Object next = newInstance(type, nextSize);
                System.arraycopy(array, start, next, 0, nextSize);
                parentArray[i] = next;
                start += nextSize;
            }

            results = parentArray;
        }

        return results;
    }

    /** Store the content of an iterable in an array */
    static <T> T[] asArray(java.util.Iterator<T> it, int length) {
        final T[] array = (T[]) new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = it.next();
        }
        return array;
    }

    static Object asArray(Class<?> type, Object element) {
        final Object newTrailing = newInstance(type, 1);
        setAt(newTrailing, 0, element);
        return newTrailing;
    }

    static Object copyRange(Object array, int from, int to) {
        final int length = to - from;
        final Object copy = newInstance(arrayType(array), length);
        System.arraycopy(array, from, copy, 0, length);
        return copy;
    }

    static <T> Object copyPrepend(Class<?> type, Object array, T element) {
        final int length = getLength(array);
        final Object copy = newInstance(type, length + 1);
        System.arraycopy(array, 0, copy, 1, length);
        setAt(copy, 0, element);
        return copy;
    }

    static <T> Object copyUpdate(Object array, int index, T element) {
        if (array == null) { array = emptyArray(); }
        final int length = getLength(array);
        final Object copy = newInstance(arrayType(array), Math.max(length, index + 1));
        System.arraycopy(array, 0, copy, 0, length);
        setAt(copy, index, element);
        return copy;
    }

    static <T> Object copyAppend(Class<?> type, Object array, T element) {
        final int length = getLength(array);
        final Object copy = newInstance(type, length + 1);
        if (length > 0)
            System.arraycopy(array, 0, copy, 0, length); // why is this a *LOT* slower than prepend???
        setAt(copy, length, element);
        return copy;
    }

    static <T> Object copyDrop(Object array, int index) {
        final int length = getLength(array);
        final Object copy = newInstance(arrayType(array), length);
        System.arraycopy(array, index, copy, index, length - index);
        return copy;
    }

    static Object copyTake(Object array, int length) {
        return (length <= 0) ? emptyArray()
                             : copyRange(array, 0, length);
    }

    static Class<?> arrayType(Object array) {
        return array.getClass().getComponentType();
    }

    @SuppressWarnings("ObjectEquality")
    static Object newInstance(Class<?> type, int size) {
        if (!type.isPrimitive()) return new Object[size];
        else if (type == int.class) return new int[size];
        else if (type == char.class) return new char[size];
        else if (type == byte.class) return new byte[size];
        else return java.lang.reflect.Array.newInstance(type, size);
    }

    static int getLength(Object array) {
        if (array instanceof Object[]) return ((Object[]) array).length;
        else if (array instanceof int[]) return ((int[]) array).length;
        else if (array instanceof char[]) return ((char[]) array).length;
        else if (array instanceof byte[]) return ((byte[]) array).length;
        else return java.lang.reflect.Array.getLength(array);
    }

    static <T> T getAt(Object array, int index) {
        if (array instanceof Object[]) return (T) ((Object[]) array)[index];
        else if (array instanceof int[]) return (T) (Object) ((int[]) array)[index];
        else if (array instanceof char[]) return (T) (Object) ((char[]) array)[index];
        else if (array instanceof byte[]) return (T) (Object) ((byte[]) array)[index];
        else return (T) java.lang.reflect.Array.get(array, index);
    }

    static void setAt(Object array, int index, Object value) {
        if (array instanceof Object[]) ((Object[]) array)[index] = value;
        else if (array instanceof int[]) ((int[]) array)[index] = (int) value;
        else if (array instanceof char[]) ((char[]) array)[index] = (char) value;
        else if (array instanceof byte[]) ((byte[]) array)[index] = (byte) value;
        else java.lang.reflect.Array.set(array, index, value);
    }

    static <T> T toPrimitiveArray(Class<?> type, Object[] array) {
        assert !type.isArray();
        final T results = (T) newInstance(type, array.length);
        for (int i = 0; i < array.length; i++) {
            setAt(results, i, array[i]);
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
