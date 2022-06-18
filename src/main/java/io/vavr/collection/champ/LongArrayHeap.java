/*
 * @(#)AbstractSequencedMap.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */
package io.vavr.collection.champ;


import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;

/**
 * An optimized array-based binary heap with long keys.
 * <p>
 * This is a highly optimized implementation which uses
 * <ol type="a">
 * <li>the Wegener bottom-up heuristic and</li>
 * <li>sentinel values</li>
 * </ol>
 * The implementation uses an array
 * in order to store the elements, providing amortized O(log(n)) time for the
 * {@link #addAsLong} and {@link #removeAsLong} operations.
 * Operation {@code findMin},
 * is a worst-case O(1) operation. All bounds are worst-case if the user
 * initializes the heap with a capacity larger or equal to the total number of
 * elements that are going to be inserted into the heap.
 *
 * <p>
 * See the following papers for details about the optimizations:
 * <ul>
 * <li>Ingo Wegener. BOTTOM-UP-HEAPSORT, a new variant of HEAPSORT beating, on
 * an average, QUICKSORT (if n is not very small). Theoretical Computer Science,
 * 118(1), 81--98, 1993.</li>
 * <li>Peter Sanders. Fast Priority Queues for Cached Memory. Algorithms
 * Engineering and Experiments (ALENEX), 312--327, 1999.</li>
 * </ul>
 *
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access a heap concurrently, and at least one of the threads
 * modifies the heap structurally, it <em>must</em> be synchronized externally.
 * (A structural modification is any operation that adds or deletes one or more
 * elements or changing the key of some element.) This is typically accomplished
 * by synchronizing on some object that naturally encapsulates the heap.
 *
 * @author Dimitrios Michail
 *
 * <dl>
 *      <dt>JHeaps Library
 *      <br>Copyright (c) 2014-2022 Dimitrios Michail. Apache License 2.0.</dt>
 *      <dd><a href="https://github.com/d-michail/jheaps">github.com</a>
 * </dl>
 */
public class LongArrayHeap extends AbstractCollection<Long>
        implements /*LongQueue,*/ Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * The array used for representing the heap.
     */
    private long[] array;

    /**
     * Number of elements in the heap.
     */
    private int size;

    /**
     * Constructs a new, empty heap, using the natural ordering of its keys.
     *
     * <p>
     * The initial capacity of the heap is {@code 16} and
     * adjusts automatically based on the sequence of insertions and deletions.
     */
    public LongArrayHeap() {
        this(16);
    }

    /**
     * Constructs a new, empty heap, with a provided initial capacity using the
     * natural ordering of its keys.
     *
     * <p>
     * The initial capacity of the heap is provided by the user and is adjusted
     * automatically based on the sequence of insertions and deletions. The
     * capacity will never become smaller than the initial requested capacity.
     *
     * @param capacity the initial heap capacity
     */
    public LongArrayHeap(int capacity) {
        Preconditions.checkIndex(capacity + 1, Integer.MAX_VALUE - 8 - 1);
        this.array = new long[capacity + 1];
        this.array[0] = Long.MIN_VALUE;
        this.size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<Long> iterator() {
        return Spliterators.iterator(Arrays.spliterator(array, 1, size + 1));
    }

    //@Override
    public boolean containsAsLong(long e) {
        for (int i = size; i > 0; i--) {
            long l = array[i];
            if (l == e) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        size = 0;
    }

    //@Override
    public long elementAsLong() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return array[1];
    }

    //@Override
    public boolean offerAsLong(long key) {
        return addAsLong(key);
    }

    //@Override
    public boolean addAsLong(long key) {
        if (size == array.length - 1) {
            array = Arrays.copyOf(array, array.length * 2);
        }

        size++;
        int hole = size;
        int pred = hole >>> 1;
        long predElem = array[pred];

        while (predElem > key) {
            array[hole] = predElem;

            hole = pred;
            pred >>>= 1;
            predElem = array[pred];
        }

        array[hole] = key;
        return true;
    }

    /**
     * {@inheritDoc}
     */
    //@Override
    public long removeAsLong() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        long result = array[1];

        // first move up elements on a min-path
        int hole = 1;
        int succ = 2;
        int sz = size;
        while (succ < sz) {
            long key1 = array[succ];
            long key2 = array[succ + 1];
            if (key1 > key2) {
                succ++;
                array[hole] = key2;
            } else {
                array[hole] = key1;
            }
            hole = succ;
            succ <<= 1;
        }

        // bubble up rightmost element
        long bubble = array[sz];
        int pred = hole >>> 1;
        while (array[pred] > bubble) {
            array[hole] = array[pred];
            hole = pred;
            pred >>>= 1;
        }

        // finally move data to hole
        array[hole] = bubble;

        array[size] = Long.MAX_VALUE;
        size = sz - 1;

        return result;
    }

    //@Override
    public boolean removeAsLong(long e) {
        long[] buf = new long[size];
        boolean removed = false;
        int i = 0;
        for (; i < size; i++) {
            long l = removeAsLong();
            if (l >= e) {
                removed = l == e;
                break;
            }
            buf[i] = l;
        }
        for (int j = 0; j < i; j++) {
            addAsLong(buf[j]);
        }

        return removed;
    }

    @Override
    public LongArrayHeap clone() {
        try {
            LongArrayHeap that = (LongArrayHeap) super.clone();
            that.array = this.array.clone();
            return that;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}