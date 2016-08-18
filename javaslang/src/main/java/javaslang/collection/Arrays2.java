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
        Object results = arrayCopy(type, firstSize, array, 0, 0, firstSize);

        if (firstSize < length) {
            final Object[] parentArray = new Object[1 + ((length - 1) / groupSize)];
            parentArray[0] = results;

            for (int start = firstSize, i = 1; start < length; i++) {
                final int nextSize = Math.min(groupSize, length - (i * groupSize));
                final Object next = arrayCopy(type, nextSize, array, start, 0, nextSize);
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
        final Object copy = arrayCopy(type, 1, null, 0, 0, 0);
        setAt(copy, 0, element);
        return copy;
    }

    static Object copyRange(Object array, int from, int to) {
        final int length = to - from;
        return arrayCopy(arrayType(array), length, array, from, 0, length);
    }

    static <T> Object copyPrepend(Class<?> type, Object array, T element) {
        final int length = getLength(array);
        final Object copy = arrayCopy(type, length + 1, array, 0, 1, length);
        setAt(copy, 0, element);
        return copy;
    }

    static <T> Object copyUpdate(Object array, int index, T element) {
        if (array == null) { array = emptyArray(); }
        final int length = getLength(array);
        final Object copy = arrayCopy(arrayType(array), Math.max(length, index + 1), array, 0, 0, length);
        setAt(copy, index, element);
        return copy;
    }

    static <T> Object copyAppend(Class<?> type, Object array, T element) {
        final int length = getLength(array);
        final Object copy = arrayCopy(type, length + 1, array, 0, 0, length);
        setAt(copy, length, element);
        return copy;
    }

    static <T> Object copyDrop(Object array, int index) {
        final int length = getLength(array);
        return arrayCopy(arrayType(array), length, array, index, index, length - index);
    }

    static Object copyTake(Object array, int length) {
        return (length <= 0) ? emptyArray()
                             : copyRange(array, 0, length);
    }

    static Class<?> arrayType(Object array) {
        return array.getClass().getComponentType();
    }

    /** for performance reasons the array allocation and the System.arraycopy must be next to each other - even an if (size > 0) will make it slower */
    @SuppressWarnings("ObjectEquality")
    static Object arrayCopy(Class<?> type, int arraySize, Object source, int sourceFrom, int destinationFrom, int size) {
        assert arraySize >= size;
        if (!type.isPrimitive()) {
            if (size == 0) {
                return new Object[arraySize];
            } else {
                final Object[] result = new Object[arraySize];
                System.arraycopy(source, sourceFrom, result, destinationFrom, size);
                return result;
            }
        } else if (type == int.class) {
            if (size == 0) {
                return new int[arraySize];
            } else {
                final int[] result = new int[arraySize];
                System.arraycopy(source, sourceFrom, result, destinationFrom, size);
                return result;
            }
        } else if (type == char.class) {
            if (size == 0) {
                return new char[arraySize];
            } else {
                final char[] result = new char[arraySize];
                System.arraycopy(source, sourceFrom, result, destinationFrom, size);
                return result;
            }
        } else {
            final Object result = java.lang.reflect.Array.newInstance(type, arraySize);
            if (size > 0) {
                System.arraycopy(source, sourceFrom, result, destinationFrom, size);
            }
            return result;
        }
    }

    static int getLength(Object array) {
        if (array instanceof Object[]) return ((Object[]) array).length;
        else if (array instanceof int[]) return ((int[]) array).length;
        else if (array instanceof char[]) return ((char[]) array).length;
        else return java.lang.reflect.Array.getLength(array);
    }

    static <T> T getAt(Object array, int index) {
        if (array instanceof Object[]) return (T) ((Object[]) array)[index];
        else if (array instanceof int[]) return (T) (Object) ((int[]) array)[index];
        else if (array instanceof char[]) return (T) (Object) ((char[]) array)[index];
        else return (T) java.lang.reflect.Array.get(array, index);
    }

    static void setAt(Object array, int index, Object value) {
        if (array instanceof Object[]) ((Object[]) array)[index] = value;
        else if (array instanceof int[]) ((int[]) array)[index] = (int) value;
        else if (array instanceof char[]) ((char[]) array)[index] = (char) value;
        else java.lang.reflect.Array.set(array, index, value);
    }

    static <T> T toPrimitiveArray(Class<?> type, Object[] array) {
        assert isNullOrEmpty(array) || (type == toPrimitive(array[0].getClass()));
        assert !type.isArray();
        final Object results = arrayCopy(type, array.length, null, 0, 0, 0);
        for (int i = 0; i < array.length; i++) {
            setAt(results, i, array[i]);
        }
        return (T) results;
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
