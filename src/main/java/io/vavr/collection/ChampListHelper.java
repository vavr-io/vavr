/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.vavr.collection;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Provides helper methods for lists that are based on arrays.
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
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
    static <T> T[] copyComponentAdd(T[] src, int index, int numComponents) {
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
    static <T> T[] copyComponentRemove(T[] src, int index, int numComponents) {
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
    static <T> T[] copySet(T[] src, int index, T value) {
        final T[] dst = Arrays.copyOf(src, src.length);
        dst[index] = value;
        return dst;
    }

    /**
     * Checks if the specified array ranges are equal.
     *
     * @param a     array a
     * @param aFrom from index in array a
     * @param aTo   to index in array a
     * @param b     array b
     * @param bFrom from index in array b
     * @param bTo   to index in array b
     * @return true if equal
     */
    static boolean arrayEquals(Object[] a, int aFrom, int aTo,
                               Object[] b, int bFrom, int bTo) {
        if (aTo - aFrom != bTo - bFrom) return false;
        int bOffset = bFrom - aFrom;
        for (int i = aFrom; i < aTo; i++) {
            if (!Objects.equals(a[i], b[i + bOffset])) {
                return false;
            }
        }
        return true;
    }
    /**
     * Checks if the specified array ranges are equal.
     *
     * @param a     array a
     * @param aFrom from index in array a
     * @param aTo   to index in array a
     * @param b     array b
     * @param bFrom from index in array b
     * @param bTo   to index in array b
     * @return true if equal
     */
    static boolean arrayEquals(Object[] a, int aFrom, int aTo,
                               Object[] b, int bFrom, int bTo,
                               BiPredicate<Object,Object> c) {
        if (aTo - aFrom != bTo - bFrom) return false;
        int bOffset = bFrom - aFrom;
        for (int i = aFrom; i < aTo; i++) {
            if (!c.test(a[i], b[i + bOffset])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the provided index is {@literal >= 0} and {@literal <=} size;
     *
     * @param index the index
     * @param size  the size
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    static void checkIndex(int index, int size) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index=" + index + " size=" + size);
    }
}
