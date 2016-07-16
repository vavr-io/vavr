/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Random;

import static java.util.Arrays.copyOfRange;

/**
 * Internal class, containing helpers.
 *
 * @author Pap Lőrinc
 * @since 3.0.0
 */
@SuppressWarnings({"unchecked", "SuspiciousArrayCast"})
final class Arrays2 { // TODO reuse these in `Array` also
    private static final Object[] EMPTY = {};

    static <T> T[] emptyArray()                  { return (T[]) EMPTY; }
    static boolean isNullOrEmpty(Object[] array) { return (array == null) || (array.length == 0); }

    static <T> T getOrDefault(Object[] array, int i, T defaultValue) {
        return (isNullOrEmpty(array) || (i < 0) || (i >= array.length))
               ? defaultValue
               : (T) array[i];
    }

    /** Repeatedly group an array into equal sized sub-trees */
    static Object[] grouped(Object[] array, int length, int size) {
        Object[] results = new Object[Math.min(size, length)];
        System.arraycopy(array, 0, results, 0, results.length);

        if (results.length < array.length) {
            final Object[] parentArray = new Object[1 + ((length - 1) / size)];
            parentArray[0] = results;

            for (int start = results.length, i = 1; start < array.length; i++) {
                final int nextLength = Math.min(size, length - (i * size));
                Object[] next = new Object[nextLength];
                System.arraycopy(array, start, next, 0, nextLength);
                parentArray[i] = next;
                start += nextLength;
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

    static <T> T[] copyPrepend(T[] array, T element) {
        final T[] copy = (T[]) new Object[array.length + 1];
        System.arraycopy(array, 0, copy, 1, array.length);
        copy[0] = element;
        return copy;
    }

    static <T> T[] copyUpdate(T[] array, int index, T element) {
        if (array == null) { array = emptyArray(); }
        final T[] copy = (T[]) new Object[Math.max(array.length, index + 1)];
        System.arraycopy(array, 0, copy, 0, array.length);
        copy[index] = element;
        return copy;
    }

    static <T> T[] copyAppend(T[] array, T element) {
        final T[] copy = (T[]) new Object[array.length + 1];
        System.arraycopy(array, 0, copy, 0, array.length); // why is this a *LOT* slower than prepend???
        copy[array.length] = element;
        return copy;
    }

    static <T> T[] drop(T[] array, int index) {
        final T[] copy = (T[]) new Object[array.length];
        System.arraycopy(array, index, copy, index, array.length - index);
        return copy;
    }

    static <T> T[] take(T[] array, int length) {
        return (length <= 0) ? emptyArray()
                             : copyOfRange(array, 0, length);
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
