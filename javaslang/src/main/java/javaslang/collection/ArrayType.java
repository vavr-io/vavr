/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

abstract class ArrayType<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    static <T> ArrayType<T> obj() { return (ArrayType<T>) ObjectArrayType.INSTANCE; }

    abstract Class<T> type();
    abstract int lengthOf(Object array);
    abstract T getAt(Object array, int index);

    abstract Object empty();
    abstract void setAt(Object array, int index, Object value);
    abstract Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size);

    @SuppressWarnings("unchecked")
    static <T> ArrayType<T> of(Object array) { return of((Class<T>) array.getClass().getComponentType()); }
    @SuppressWarnings("unchecked")
    private static <T> ArrayType<T> of(Class<T> type) {
        if (!type.isPrimitive()) {
            return (ArrayType<T>) obj();
        } else {
            if (boolean.class == type) {
                return (ArrayType<T>) BooleanArrayType.INSTANCE;
            } else if (byte.class == type) {
                return (ArrayType<T>) ByteArrayType.INSTANCE;
            } else if (char.class == type) {
                return (ArrayType<T>) CharArrayType.INSTANCE;
            } else if (double.class == type) {
                return (ArrayType<T>) DoubleArrayType.INSTANCE;
            } else if (float.class == type) {
                return (ArrayType<T>) FloatArrayType.INSTANCE;
            } else if (int.class == type) {
                return (ArrayType<T>) IntArrayType.INSTANCE;
            } else if (long.class == type) {
                return (ArrayType<T>) LongArrayType.INSTANCE;
            } else if (short.class == type) {
                return (ArrayType<T>) ShortArrayType.INSTANCE;
            } else {
                throw new IllegalArgumentException("Unknown type: " + type);
            }
        }
    }

    Object newInstance(int length) { return copy(empty(), length); }

    /** System.arrayCopy with same source and destination */
    Object copyRange(Object array, int from, int to) {
        final int length = to - from;
        return copy(array, length, from, 0, length);
    }

    /** Repeatedly group an array into equal sized sub-trees */
    Object grouped(Object array, int groupSize) {
        final int arrayLength = lengthOf(array);
        assert arrayLength > groupSize;
        final Object results = obj().newInstance(1 + ((arrayLength - 1) / groupSize));
        obj().setAt(results, 0, copyRange(array, 0, groupSize));

        for (int start = groupSize, i = 1; start < arrayLength; i++) {
            final int nextLength = Math.min(groupSize, arrayLength - (i * groupSize));
            obj().setAt(results, i, copyRange(array, start, start + nextLength));
            start += nextLength;
        }

        return results;
    }

    /** clone the source and set the value at the given position */
    Object copyUpdate(Object array, int index, T element) {
        final Object copy = copy(array, index + 1);
        setAt(copy, index, element);
        return copy;
    }

    Object copy(Object array, int minLength) {
        final int arrayLength = lengthOf(array);
        final int length = Math.max(arrayLength, minLength);
        return copy(array, length, 0, 0, arrayLength);
    }

    /** clone the source and keep everything after the index (pre-padding the values with null) */
    Object copyDrop(Object array, int index) {
        final int length = lengthOf(array);
        return copy(array, length, index, index, length - index);
    }

    /** clone the source and keep everything before and including the index */
    Object copyTake(Object array, int lastIndex) {
        return copyRange(array, 0, lastIndex + 1);
    }

    /** Create a single element array */
    Object asArray(T element) {
        final Object result = newInstance(1);
        setAt(result, 0, element);
        return result;
    }

    /** Store the content of an iterable in an array */
    static Object[] asArray(java.util.Iterator<?> it, int length) {
        final Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = it.next();
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    static <T> T asPrimitives(Class<?> primitiveClass, Iterable<?> values) {
        final Object[] array = Array.ofAll(values).toJavaArray();
        assert (array.length == 0) || (primitiveClass == asPrimitive(array[0].getClass())) && !primitiveClass.isArray();
        final ArrayType<T> type = of((Class<T>) primitiveClass);
        final Object results = type.newInstance(array.length);
        for (int i = 0; i < array.length; i++) {
            type.setAt(results, i, array[i]);
        }
        return (T) results;
    }

    /* convert to primitive */
    private static final Class<?>[] WRAPPERS = { Boolean.class, Byte.class, Character.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Void.class };
    private static final Class<?>[] PRIMITIVES = { boolean.class, byte.class, char.class, double.class, float.class, int.class, long.class, short.class, void.class };

    static Class<?> asPrimitive(Class<?> wrapper) {
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
}
