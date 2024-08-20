/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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

import java.io.Serializable;
import java.util.Comparator;

/**
 * INTERNAL: Common {@code Comparator} related functions (not intended to be public).
 */
final class Comparators {

    private Comparators() {
    }

    /**
     * Returns the natural comparator for type U, i.e. treating it as {@code Comparable<U>}.
     * The returned comparator is also {@code java.io.Serializable}.
     * <p>
     * Please note that this will lead to runtime exceptions, if U is not Comparable.
     *
     * @param <U> The type
     * @return The natural Comparator of type U
     */
    @SuppressWarnings("unchecked")
    static <U> Comparator<U> naturalComparator() {
        return NaturalComparator.instance();
    }
}

final class NaturalComparator<T> implements Comparator<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final NaturalComparator<?> INSTANCE = new NaturalComparator<>();

    private NaturalComparator() {
    }

    @SuppressWarnings("unchecked")
    static <T> NaturalComparator<T> instance() {
        return (NaturalComparator<T>) INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T o1, T o2) {
        return ((Comparable<T>) o1).compareTo(o2);
    }

    /** @see Comparator#equals(Object) */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof NaturalComparator;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of NaturalComparator.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
