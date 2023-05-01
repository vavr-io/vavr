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

import io.vavr.Tuple2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampBitmapIndexedNode.emptyNode;

/**
 * A {@code SequencedData} stores a sequence number plus some data.
 * <p>
 * {@code SequencedData} objects are used to store sequenced data in a CHAMP
 * trie (see {@link ChampNode}).
 * <p>
 * The kind of data is specified in concrete implementations of this
 * interface.
 * <p>
 * All sequence numbers of {@code SequencedData} objects in the same CHAMP trie
 * are unique. Sequence numbers range from {@link Integer#MIN_VALUE} (exclusive)
 * to {@link Integer#MAX_VALUE} (inclusive).
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
 */
 interface ChampSequencedData {
    /**
     * We use {@link Integer#MIN_VALUE} to detect overflows in the sequence number.
     * <p>
     * {@link Integer#MIN_VALUE} is the only integer number which can not
     * be negated.
     * <p>
     * Therefore, we can not use {@link Integer#MIN_VALUE} as a sequence number
     * anyway.
     */
    int NO_SEQUENCE_NUMBER = Integer.MIN_VALUE;

    static <K extends ChampSequencedData> ChampBitmapIndexedNode<K> buildSequencedTrie(ChampBitmapIndexedNode<K> root, ChampIdentityObject mutator) {
        ChampBitmapIndexedNode<K> seqRoot = emptyNode();
        ChampChangeEvent<K> details = new ChampChangeEvent<>();
        for (ChampSpliterator<K, K> i = new ChampSpliterator<K, K>(root, null, 0, 0); i.moveNext(); ) {
            K elem = i.current();
            seqRoot = seqRoot.update(mutator, elem, seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, ChampSequencedData::seqEquals, ChampSequencedData::seqHash);
        }
        return seqRoot;
    }

    /**
     * Returns true if the sequenced elements must be renumbered because
     * {@code first} or {@code last} are at risk of overflowing.
     * <p>
     * {@code first} and {@code last} are estimates of the first and last
     * sequence numbers in the trie. The estimated extent may be larger
     * than the actual extent, but not smaller.
     *
     * @param size  the size of the trie
     * @param first the estimated first sequence number
     * @param last  the estimated last sequence number
     * @return
     */
    static boolean mustRenumber(int size, int first, int last) {
        return size == 0 && (first != -1 || last != 0)
                || last > Integer.MAX_VALUE - 2
                || first < Integer.MIN_VALUE + 2;
    }

    static <K extends ChampSequencedData> Vector<Object> vecBuildSequencedTrie(ChampBitmapIndexedNode<K> root, ChampIdentityObject mutator, int size) {
        ArrayList<K> list = new ArrayList<>(size);
        for (var i = new ChampSpliterator<K, K>(root, Function.identity(), 0, Long.MAX_VALUE); i.moveNext(); ) {
            list.add(i.current());
        }
        list.sort(Comparator.comparing(ChampSequencedData::getSequenceNumber));
        return Vector.ofAll(list);
    }

    static boolean vecMustRenumber(int size, int offset, int vectorSize) {
        return size == 0
                || vectorSize >>> 1 > size
                || (long) vectorSize - offset > Integer.MAX_VALUE - 2
                || offset < Integer.MIN_VALUE + 2;
    }

    /**
     * Renumbers the sequence numbers in all nodes from {@code 0} to {@code size}.
     * <p>
     * Afterwards the sequence number for the next inserted entry must be
     * set to the value {@code size};
     *
     * @param size            the size of the trie
     * @param root            the root of the trie
     * @param sequenceRoot    the sequence root of the trie
     * @param mutator         the mutator that will own the renumbered trie
     * @param hashFunction    the hash function for data elements
     * @param equalsFunction  the equals function for data elements
     * @param factoryFunction the factory function for data elements
     * @param <K>
     * @return a new renumbered root
     */
    static <K extends ChampSequencedData> ChampBitmapIndexedNode<K> renumber(int size,
                                                                             ChampBitmapIndexedNode<K> root,
                                                                             ChampBitmapIndexedNode<K> sequenceRoot,
                                                                             ChampIdentityObject mutator,
                                                                             ToIntFunction<K> hashFunction,
                                                                             BiPredicate<K, K> equalsFunction,
                                                                             BiFunction<K, Integer, K> factoryFunction

    ) {
        if (size == 0) {
            return root;
        }
        ChampBitmapIndexedNode<K> newRoot = root;
        ChampChangeEvent<K> details = new ChampChangeEvent<>();
        int seq = 0;

        for (var i = new ChampSpliterator<>(sequenceRoot, Function.identity(), 0, 0); i.moveNext(); ) {
            K e = i.current();
            K newElement = factoryFunction.apply(e, seq);
            newRoot = newRoot.update(mutator,
                    newElement,
                    Objects.hashCode(e), 0, details,
                    (oldk, newk) -> oldk.getSequenceNumber() == newk.getSequenceNumber() ? oldk : newk,
                    equalsFunction, hashFunction);
            seq++;
        }
        return newRoot;
    }

    /**
     * Renumbers the sequence numbers in all nodes from {@code 0} to {@code size}.
     * <p>
     * Afterward, the sequence number for the next inserted entry must be
     * set to the value {@code size};
     *
     * @param <K>
     * @param size            the size of the trie
     * @param root            the root of the trie
     * @param vector          the sequence root of the trie
     * @param mutator         the mutator that will own the renumbered trie
     * @param hashFunction    the hash function for data elements
     * @param equalsFunction  the equals function for data elements
     * @param factoryFunction the factory function for data elements
     * @return a new renumbered root and a new vector with matching entries
     */
    @SuppressWarnings("unchecked")
    static <K extends ChampSequencedData> Tuple2<ChampBitmapIndexedNode<K>, Vector<Object>> vecRenumber(
            int size,
             ChampBitmapIndexedNode<K> root,
             Vector<Object> vector,
             ChampIdentityObject mutator,
             ToIntFunction<K> hashFunction,
             BiPredicate<K, K> equalsFunction,
             BiFunction<K, Integer, K> factoryFunction) {
        if (size == 0) {
            new Tuple2<>(root, vector);
        }
        ChampBitmapIndexedNode<K> renumberedRoot = root;
        Vector<Object> renumberedVector = Vector.of();
        ChampChangeEvent<K> details = new ChampChangeEvent<>();
        BiFunction<K, K, K> forceUpdate = (oldk, newk) -> newk;
        int seq = 0;
        for (var i = new ChampVectorSpliterator<K>(vector, o -> (K) o, 0, Long.MAX_VALUE, 0); i.moveNext(); ) {
            K current = i.current();
            K data = factoryFunction.apply(current, seq++);
            renumberedVector = renumberedVector.append(data);
            renumberedRoot = renumberedRoot.update(mutator, data, hashFunction.applyAsInt(current), 0, details, forceUpdate, equalsFunction, hashFunction);
        }

        return new Tuple2<>(renumberedRoot, renumberedVector);
    }


    static <K extends ChampSequencedData> boolean seqEquals(K a, K b) {
        return a.getSequenceNumber() == b.getSequenceNumber();
    }

    static <K extends ChampSequencedData> int seqHash(K e) {
        return seqHash(e.getSequenceNumber());
    }

    /**
     * Computes a hash code from the sequence number, so that we can
     * use it for iteration in a CHAMP trie.
     * <p>
     * Convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE.
     * Then reorders its bits from 66666555554444433333222221111100 to
     * 00111112222233333444445555566666.
     *
     * @param sequenceNumber a sequence number
     * @return a hash code
     */
    static int seqHash(int sequenceNumber) {
        int u = sequenceNumber + Integer.MIN_VALUE;
        return (u >>> 27)
                | ((u & 0b00000_11111_00000_00000_00000_00000_00) >>> 17)
                | ((u & 0b00000_00000_11111_00000_00000_00000_00) >>> 7)
                | ((u & 0b00000_00000_00000_11111_00000_00000_00) << 3)
                | ((u & 0b00000_00000_00000_00000_11111_00000_00) << 13)
                | ((u & 0b00000_00000_00000_00000_00000_11111_00) << 23)
                | ((u & 0b00000_00000_00000_00000_00000_00000_11) << 30);
    }

    static <K extends ChampSequencedData> ChampBitmapIndexedNode<K> seqRemove(ChampBitmapIndexedNode<K> seqRoot, ChampIdentityObject mutator,
                                                                              K key, ChampChangeEvent<K> details) {
        return seqRoot.remove(mutator,
                key, seqHash(key.getSequenceNumber()), 0, details,
                ChampSequencedData::seqEquals);
    }

    static <K extends ChampSequencedData> ChampBitmapIndexedNode<K> seqUpdate(ChampBitmapIndexedNode<K> seqRoot, ChampIdentityObject mutator,
                                                                              K key, ChampChangeEvent<K> details,
                                                                              BiFunction<K, K, K> replaceFunction) {
        return seqRoot.update(mutator,
                key, seqHash(key.getSequenceNumber()), 0, details,
                replaceFunction,
                ChampSequencedData::seqEquals, ChampSequencedData::seqHash);
    }

    final static ChampTombstone TOMB_ZERO_ZERO = new ChampTombstone(0, 0);

    static <K extends ChampSequencedData> Tuple2<Vector<Object>, Integer> vecRemove(Vector<Object> vector, ChampIdentityObject mutator, K oldElem, ChampChangeEvent<K> details, int offset) {
        // If the element is the first, we can remove it and its neighboring tombstones from the vector.
        int size = vector.size();
        int index = oldElem.getSequenceNumber() + offset;
        if (index == 0) {
            if (size > 1) {
                Object o = vector.get(1);
                if (o instanceof ChampTombstone t) {
                    return new Tuple2<>(vector.removeRange(0, 2 + t.after()), offset - 2 - t.after());
                }
            }
            return new Tuple2<>(vector.tail(), offset - 1);
        }

        // If the element is the last , we can remove it and its neighboring tombstones from the vector.
        if (index == size - 1) {
            Object o = vector.get(size - 2);
            if (o instanceof ChampTombstone t) {
                return new Tuple2<>(vector.removeRange(size - 2 - t.before(), size), offset);
            }
            return new Tuple2<>(vector.init(), offset);
        }

        // Otherwise, we replace the element with a tombstone, and we update before/after skip counts
        assert index > 0 && index < size - 1;
        Object before = vector.get(index - 1);
        Object after = vector.get(index + 1);
        if (before instanceof ChampTombstone tb && after instanceof ChampTombstone ta) {
            vector = vector.update(index - 1 - tb.before(), new ChampTombstone(0, 2 + tb.before() + ta.after()));
            vector = vector.update(index, TOMB_ZERO_ZERO);
            vector = vector.update(index + 1 + ta.after(), new ChampTombstone(2 + tb.before() + ta.after(), 0));
        } else if (before instanceof ChampTombstone tb) {
            vector = vector.update(index - 1 - tb.before(), new ChampTombstone(0, 1 + tb.before()));
            vector = vector.update(index, new ChampTombstone(1 + tb.before(), 0));
        } else if (after instanceof ChampTombstone ta) {
            vector = vector.update(index, new ChampTombstone(0, 1 + ta.after()));
            vector = vector.update(index + 1 + ta.after(), new ChampTombstone(1 + ta.after(), 0));
        } else {
            vector = vector.update(index, TOMB_ZERO_ZERO);
        }
        return new Tuple2<>(vector, offset);
    }


    static <T> Vector<T> removeRange(Vector<T> v, int fromIndex, int toIndex) {
        Objects.checkIndex(fromIndex, toIndex + 1);
        Objects.checkIndex(toIndex, v.size() + 1);
        if (fromIndex == 0) {
            return v.slice(toIndex, v.size());
        }
        if (toIndex == v.size()) {
            return v.slice(0, fromIndex);
        }
        final Vector<T> begin = v.slice(0, fromIndex);
        return begin.appendAll(() -> v.iterator(toIndex));
    }


    static <K extends ChampSequencedData> Vector<Object> vecUpdate(Vector<Object> newSeqRoot, ChampIdentityObject mutator, K newElem, ChampChangeEvent<K> details,
                                                                   BiFunction<K, K, K> replaceFunction) {
        return newSeqRoot;
    }

    /**
     * Gets the sequence number of the data.
     *
     * @return sequence number in the range from {@link Integer#MIN_VALUE}
     * (exclusive) to {@link Integer#MAX_VALUE} (inclusive).
     */
    int getSequenceNumber();


}
