/*
 * @(#)ArrayHelper.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Provides static helper methods for arrays.
 */
class ArrayHelper {
    /**
     * Don't let anyone instantiate this class.
     */
    private ArrayHelper() {
    }

    /**
     * Checks if the elements in two sub-arrays are equal to one another
     * in the same order.
     *
     * @param a     array a
     * @param aFrom from-index
     * @param aTo   to-index
     * @param b     array b (can be the same as array a)
     * @param bFrom from-index
     * @param bTo   to-index
     * @return true if the two sub-arrays have the same length and
     * if the elements are equal to one another in the same order
     */
    public static <T> boolean equals(T[] a, int aFrom, int aTo,
                                     T[] b, int bFrom, int bTo) {
        return equals(a, aFrom, aTo, b, bFrom, bTo, Objects::equals);
    }

    /**
     * Checks if the elements in two sub-arrays are equal to one another
     * in the same order.
     *
     * @param a     array a
     * @param aFrom from-index
     * @param aTo   to-index
     * @param b     array b (can be the same as array a)
     * @param bFrom from-index
     * @param bTo   to-index
     * @param cmp   the predicate that checks if two elements are equal
     * @return true if the two sub-arrays have the same length and
     * if the elements are equal to one another in the same order
     */
    public static <T> boolean equals(T[] a, int aFrom, int aTo,
                                     T[] b, int bFrom, int bTo,
                                     BiPredicate<T, T> cmp) {
        Preconditions.checkFromToIndex(aFrom, aTo, a.length);
        Preconditions.checkFromToIndex(bFrom, bTo, b.length);
        int aLength = aTo - aFrom;
        int bLength = bTo - bFrom;
        if (aLength != bLength) {
            return false;
        }

        for (int i = 0; i < aLength; i++) {
            if (!cmp.test(a[aFrom++], b[bFrom++])) {
                return false;
            }
        }

        return true;
    }

    /**
     * Copies 'src' and inserts 'value' at position 'index'.
     *
     * @param src   an array
     * @param index an index
     * @param value a value
     * @param <T>   the array type
     * @return a new array
     */
    public static <T> T[] copyAdd(T[] src, int index, T value) {
        final T[] dst = copyComponentAdd(src, index, 1);
        dst[index] = value;
        return dst;
    }

    /**
     * Copies 'src' and inserts 'values' at position 'index'.
     *
     * @param src    an array
     * @param index  an index
     * @param values the values
     * @param <T>    the array type
     * @return a new array
     */
    public static <T> T[] copyAddAll(T[] src, int index, T[] values) {
        final T[] dst = copyComponentAdd(src, index, values.length);
        System.arraycopy(values, 0, dst, index, values.length);
        return dst;
    }

    /**
     * Copies 'src' and inserts 'numComponents' at position 'index'.
     * <p>
     * The new components will have a null value.
     *
     * @param src           an array
     * @param index         an index
     * @param numComponents the number of array components to be added
     * @param <T>           the array type
     * @return a new array
     */
    public static <T> T[] copyComponentAdd(T[] src, int index, int numComponents) {
        if (index == src.length) {
            return Arrays.copyOf(src, src.length + numComponents);
        }
        @SuppressWarnings("unchecked") final T[] dst = (T[]) Array.newInstance(src.getClass().getComponentType(), src.length + numComponents);
        System.arraycopy(src, 0, dst, 0, index);
        System.arraycopy(src, index, dst, index + numComponents, src.length - index);
        return dst;
    }

    /**
     * Copies 'src' and removes 'numComponents' at position 'index'.
     *
     * @param src           an array
     * @param index         an index
     * @param numComponents the number of array components to be removed
     * @param <T>           the array type
     * @return a new array
     */
    public static <T> T[] copyComponentRemove(T[] src, int index, int numComponents) {
        if (index == src.length - numComponents) {
            return Arrays.copyOf(src, src.length - numComponents);
        }
        @SuppressWarnings("unchecked") final T[] dst = (T[]) Array.newInstance(src.getClass().getComponentType(), src.length - numComponents);
        System.arraycopy(src, 0, dst, 0, index);
        System.arraycopy(src, index + numComponents, dst, index, src.length - index - numComponents);
        return dst;
    }

    /**
     * Copies 'src' and removes one component at position 'index'.
     *
     * @param src   an array
     * @param index an index
     * @param <T>   the array type
     * @return a new array
     */
    public static <T> T[] copyRemove(T[] src, int index) {
        return copyComponentRemove(src, index, 1);
    }

    /**
     * Copies 'src' and sets 'value' at position 'index'.
     *
     * @param src   an array
     * @param index an index
     * @param value a value
     * @param <T>   the array type
     * @return a new array
     */
    public static <T> T[] copySet(T[] src, int index, T value) {
        final T[] dst = Arrays.copyOf(src, src.length);
        dst[index] = value;
        return dst;
    }
}
