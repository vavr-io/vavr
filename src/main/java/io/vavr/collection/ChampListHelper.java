/*
 * @(#)ListHelper.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;



import java.lang.reflect.Array;
import java.util.Arrays;

import static java.lang.Integer.max;

/**
 * Provides helper methods for lists that are based on arrays.
 *
 * @author Werner Randelshofer
 */
 class ChampListHelper {
    /**
     * Don't let anyone instantiate this class.
     */
    private ChampListHelper() {

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
    public static <T>  T  [] copyAddAll( T  [] src, int index,  T  [] values) {
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
    public static <T>  T  [] copyComponentAdd( T  [] src, int index, int numComponents) {
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
    public static <T>  T  [] copyComponentRemove( T  [] src, int index, int numComponents) {
        if (index == src.length - numComponents) {
            return Arrays.copyOf(src, src.length - numComponents);
        }
        @SuppressWarnings("unchecked") final T[] dst = (T[]) Array.newInstance(src.getClass().getComponentType(), src.length - numComponents);
        System.arraycopy(src, 0, dst, 0, index);
        System.arraycopy(src, index + numComponents, dst, index, src.length - index - numComponents);
        return dst;
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
    public static <T>  T  [] copySet( T  [] src, int index, T value) {
        final T[] dst = Arrays.copyOf(src, src.length);
        dst[index] = value;
        return dst;
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static  Object  [] grow(final int targetCapacity, final int itemSize,  final Object  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength, items.getClass());
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static double  [] grow(final int targetCapacity, final int itemSize, final double  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static byte  [] grow(final int targetCapacity, final int itemSize, final byte  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static short  [] grow(final int targetCapacity, final int itemSize, final short  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static int  [] grow(final int targetCapacity, final int itemSize, final int  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static long  [] grow(final int targetCapacity, final int itemSize, final long  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Grows an items array.
     *
     * @param targetCapacity {@literal >= 0}
     * @param itemSize       number of array elements that an item occupies
     * @param items          the items array
     * @return a new item array of larger size or the same if no resizing is necessary
     */
    public static char  [] grow(final int targetCapacity, final int itemSize, final char  [] items) {
        if (targetCapacity * itemSize <= items.length) {
            return items;
        }
        int newLength = max(targetCapacity * itemSize, items.length * 2);
        return Arrays.copyOf(items, newLength);
    }


    /**
     * Resizes an array to fit the number of items.
     *
     * @param size     the size to fit
     * @param itemSize number of array elements that an item occupies
     * @param items    the items array
     * @return a new item array of smaller size or the same if no resizing is necessary
     */
    public static  Object  [] trimToSize(final int size, final int itemSize,  final Object  [] items) {
        int newLength = size * itemSize;
        if (items.length == newLength) {
            return items;
        }
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Resizes an array to fit the number of items.
     *
     * @param size     the size to fit
     * @param itemSize number of array elements that an item occupies
     * @param items    the items array
     * @return a new item array of smaller size or the same if no resizing is necessary
     */
    public static int  [] trimToSize(final int size, final int itemSize, final int  [] items) {
        int newLength = size * itemSize;
        if (items.length == newLength) {
            return items;
        }
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Resizes an array to fit the number of items.
     *
     * @param size     the size to fit
     * @param itemSize number of array elements that an item occupies
     * @param items    the items array
     * @return a new item array of smaller size or the same if no resizing is necessary
     */
    public static long  [] trimToSize(final int size, final int itemSize, final long  [] items) {
        int newLength = size * itemSize;
        if (items.length == newLength) {
            return items;
        }
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Resizes an array to fit the number of items.
     *
     * @param size     the size to fit
     * @param itemSize number of array elements that an item occupies
     * @param items    the items array
     * @return a new item array of smaller size or the same if no resizing is necessary
     */
    public static double  [] trimToSize(final int size, final int itemSize, final double  [] items) {
        int newLength = size * itemSize;
        if (items.length == newLength) {
            return items;
        }
        return Arrays.copyOf(items, newLength);
    }

    /**
     * Resizes an array to fit the number of items.
     *
     * @param size     the size to fit
     * @param itemSize number of array elements that an item occupies
     * @param items    the items array
     * @return a new item array of smaller size or the same if no resizing is necessary
     */
    public static byte  [] trimToSize(final int size, final int itemSize, final byte  [] items) {
        int newLength = size * itemSize;
        if (items.length == newLength) {
            return items;
        }
        return Arrays.copyOf(items, newLength);
    }
}
