/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;

import java.util.Collection;

/**
 * Helper to replace reflective array access.
 *
 * @author Pap Lőrinc
 * @since 2.1.0
 */
interface ArrayType<T> {
    @SuppressWarnings("unchecked")
    static <T> ArrayType<T> obj() { return (ArrayType<T>) ObjectArrayType.INSTANCE; }

    Class<T> type();
    int lengthOf(Object array);
    T getAt(Object array, int index);

    Object empty();
    void setAt(Object array, int index, T value) throws ClassCastException;
    Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size);

    @SuppressWarnings("unchecked")
    static <T> ArrayType<T> of(Object array)  { return of((Class<T>) array.getClass().getComponentType()); }
    static <T> ArrayType<T> of(Class<T> type) { return !type.isPrimitive() ? obj() : ofPrimitive(type); }
    @SuppressWarnings("unchecked")
    static <T> ArrayType<T> ofPrimitive(Class<T> type) {
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
            throw new IllegalArgumentException(String.valueOf(type));
        }
    }

    default Object newInstance(int length) { return copy(empty(), length); }

    /** System.arrayCopy with same source and destination */
    default Object copyRange(Object array, int from, int to) {
        final int length = to - from;
        return copy(array, length, from, 0, length);
    }

    /** Repeatedly group an array into equal sized sub-trees */
    default Object grouped(Object array, int groupSize) {
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
    default Object copyUpdate(Object array, int index, T element) {
        final Object copy = copy(array, index + 1);
        setAt(copy, index, element);
        return copy;
    }

    default Object copy(Object array, int minLength) {
        final int arrayLength = lengthOf(array);
        final int length = Math.max(arrayLength, minLength);
        return copy(array, length, 0, 0, arrayLength);
    }

    /** clone the source and keep everything after the index (pre-padding the values with null) */
    default Object copyDrop(Object array, int index) {
        final int length = lengthOf(array);
        return copy(array, length, index, index, length - index);
    }

    /** clone the source and keep everything before and including the index */
    default Object copyTake(Object array, int lastIndex) {
        return copyRange(array, 0, lastIndex + 1);
    }

    /** Create a single element array */
    default Object asArray(T element) {
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
    static <T> Object[] asArray(Iterable<? extends T> iterable) {
        if (iterable instanceof Collection<?>) {
            final Collection<? extends T> collection = (Collection<? extends T>) iterable;
            return collection.toArray();
        } else {
            return Collections.withSize(iterable).toArray();
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T asPrimitives(Class<?> primitiveClass, Iterable<?> values) {
        final Object[] array = Array.ofAll(values).toJavaArray();
        assert (array.length == 0) || !primitiveClass.isArray();
        final ArrayType<T> type = of((Class<T>) primitiveClass);
        final Object results = type.newInstance(array.length);
        for (int i = 0; i < array.length; i++) {
            type.setAt(results, i, (T) array[i]);
        }
        return (T) results;
    }

    final class BooleanArrayType implements ArrayType<Boolean>, Serializable {
        private static final long serialVersionUID = 1L;
        static final BooleanArrayType INSTANCE = new BooleanArrayType();
        static final boolean[] EMPTY = new boolean[0];

        private static boolean[] cast(Object array) { return (boolean[]) array; }

        @Override
        public Class<Boolean> type() { return boolean.class; }

        @Override
        public boolean[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Boolean getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Boolean value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new boolean[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final boolean[] result = new boolean[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class ByteArrayType implements ArrayType<Byte>, Serializable {
        private static final long serialVersionUID = 1L;
        static final ByteArrayType INSTANCE = new ByteArrayType();
        static final byte[] EMPTY = new byte[0];

        private static byte[] cast(Object array) { return (byte[]) array; }

        @Override
        public Class<Byte> type() { return byte.class; }

        @Override
        public byte[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Byte getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Byte value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new byte[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final byte[] result = new byte[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class CharArrayType implements ArrayType<Character>, Serializable {
        private static final long serialVersionUID = 1L;
        static final CharArrayType INSTANCE = new CharArrayType();
        static final char[] EMPTY = new char[0];

        private static char[] cast(Object array) { return (char[]) array; }

        @Override
        public Class<Character> type() { return char.class; }

        @Override
        public char[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Character getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Character value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new char[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final char[] result = new char[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class DoubleArrayType implements ArrayType<Double>, Serializable {
        private static final long serialVersionUID = 1L;
        static final DoubleArrayType INSTANCE = new DoubleArrayType();
        static final double[] EMPTY = new double[0];

        private static double[] cast(Object array) { return (double[]) array; }

        @Override
        public Class<Double> type() { return double.class; }

        @Override
        public double[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Double getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Double value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new double[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final double[] result = new double[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class FloatArrayType implements ArrayType<Float>, Serializable {
        private static final long serialVersionUID = 1L;
        static final FloatArrayType INSTANCE = new FloatArrayType();
        static final float[] EMPTY = new float[0];

        private static float[] cast(Object array) { return (float[]) array; }

        @Override
        public Class<Float> type() { return float.class; }

        @Override
        public float[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Float getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Float value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new float[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final float[] result = new float[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class IntArrayType implements ArrayType<Integer>, Serializable {
        private static final long serialVersionUID = 1L;
        static final IntArrayType INSTANCE = new IntArrayType();
        static final int[] EMPTY = new int[0];

        private static int[] cast(Object array) { return (int[]) array; }

        @Override
        public Class<Integer> type() { return int.class; }

        @Override
        public int[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Integer getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Integer value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new int[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final int[] result = new int[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class LongArrayType implements ArrayType<Long>, Serializable {
        private static final long serialVersionUID = 1L;
        static final LongArrayType INSTANCE = new LongArrayType();
        static final long[] EMPTY = new long[0];

        private static long[] cast(Object array) { return (long[]) array; }

        @Override
        public Class<Long> type() { return long.class; }

        @Override
        public long[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Long getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Long value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new long[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final long[] result = new long[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class ShortArrayType implements ArrayType<Short>, Serializable {
        private static final long serialVersionUID = 1L;
        static final ShortArrayType INSTANCE = new ShortArrayType();
        static final short[] EMPTY = new short[0];

        private static short[] cast(Object array) { return (short[]) array; }

        @Override
        public Class<Short> type() { return short.class; }

        @Override
        public short[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Short getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Short value) throws ClassCastException {
            if (value != null) {
                cast(array)[index] = value;
            } else {
                throw new ClassCastException();
            }
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new short[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final short[] result = new short[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }

    final class ObjectArrayType implements ArrayType<Object>, Serializable {
        private static final long serialVersionUID = 1L;
        static final ObjectArrayType INSTANCE = new ObjectArrayType();
        static final Object[] EMPTY = new Object[0];

        private static Object[] cast(Object array) { return (Object[]) array; }

        @Override
        public Class<Object> type() { return Object.class; }

        @Override
        public Object[] empty() { return EMPTY; }

        @Override
        public int lengthOf(Object array) { return (array != null) ? cast(array).length : 0; }

        @Override
        public Object getAt(Object array, int index) { return cast(array)[index]; }

        @Override
        public void setAt(Object array, int index, Object value) {
            cast(array)[index] = value;
        }

        @Override
        public Object copy(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            return (size > 0)
                    ? copyNonEmpty(array, arraySize, sourceFrom, destinationFrom, size)
                    : new Object[arraySize];
        }
        private static Object copyNonEmpty(Object array, int arraySize, int sourceFrom, int destinationFrom, int size) {
            final Object[] result = new Object[arraySize];
            System.arraycopy(array, sourceFrom, result, destinationFrom, size); /* has to be near the object allocation to avoid zeroing out the array */
            return result;
        }
    }
}