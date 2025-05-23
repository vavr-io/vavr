/*
 * ____  ______________  ________________________  __________
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

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static io.vavr.collection.ChampTrie.BitmapIndexedNode.emptyNode;

/**
 * Provides data elements for sequenced CHAMP tries.
 */
class ChampSequenced {
    /**
     * A spliterator for a {@code VectorMap} or {@code VectorSet}.
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
     * @param <K> the key type
     */
    static class ChampVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
        private final BitMappedTrie.BitMappedTrieSpliterator<Object> vector;
        private final Function<Object, K> mapper;
        private K current;

         ChampVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, int fromIndex, long est, int additionalCharacteristics) {
            super(est, additionalCharacteristics);
            this.vector = new BitMappedTrie.BitMappedTrieSpliterator<>(vector.trie, fromIndex, 0);
            this.mapper = mapper;
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            if (moveNext()) {
                action.accept(current);
                return true;
            }
            return false;
        }

         K current() {
            return current;
        }

         boolean moveNext() {
            boolean success = vector.moveNext();
            if (!success) return false;
            if (vector.current() instanceof ChampTombstone) {
                ChampTombstone t = (ChampTombstone) vector.current();
                vector.skip(t.after());
                vector.moveNext();
            }
            current = mapper.apply(vector.current());
            return true;
        }
    }

    /**
     * @param <K>
     */
    static class ChampReverseVectorSpliterator<K> extends Spliterators.AbstractSpliterator<K> {
        private final Vector<Object> vector;
        private final Function<Object, K> mapper;
        private int index;
        private K current;

         ChampReverseVectorSpliterator(Vector<Object> vector, Function<Object, K> mapper, int fromIndex, int additionalCharacteristics, long est) {
            super(est, additionalCharacteristics);
            this.vector = vector;
            this.mapper = mapper;
            index = vector.size() - 1-fromIndex;
        }

        @Override
        public boolean tryAdvance(Consumer<? super K> action) {
            if (moveNext()) {
                action.accept(current);
                return true;
            }
            return false;
        }

         boolean moveNext() {
            if (index < 0) {
                return false;
            }
            Object o = vector.get(index--);
            if (o instanceof ChampTombstone) {
                ChampTombstone t = (ChampTombstone) o;
                index -= t.before();
                o = vector.get(index--);
            }
            current = mapper.apply(o);
            return true;
        }

         K current() {
            return current;
        }
    }

    /**
     * A {@code SequencedData} stores a sequence number plus some data.
     * <p>
     * {@code SequencedData} objects are used to store sequenced data in a CHAMP
     * trie (see {@link ChampTrie.Node}).
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
    static interface ChampSequencedData {
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

        static <K extends ChampSequencedData> ChampTrie.BitmapIndexedNode<K> buildSequencedTrie(ChampTrie.BitmapIndexedNode<K> root, ChampTrie.IdentityObject owner) {
            ChampTrie.BitmapIndexedNode<K> seqRoot = emptyNode();
            ChampTrie.ChangeEvent<K> details = new ChampTrie.ChangeEvent<>();
            for (ChampIteration.ChampSpliterator<K, K> i = new ChampIteration.ChampSpliterator<K, K>(root, null, 0, 0); i.moveNext(); ) {
                K elem = i.current();
                seqRoot = seqRoot.put(owner, elem, seqHash(elem.getSequenceNumber()),
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

        static <K extends ChampSequencedData> Vector<Object> vecBuildSequencedTrie(ChampTrie.BitmapIndexedNode<K> root, ChampTrie.IdentityObject owner, int size) {
            ArrayList<K> list = new ArrayList<>(size);
            for (ChampIteration.ChampSpliterator<K, K> i = new ChampIteration.ChampSpliterator<K, K>(root, Function.identity(), 0, Long.MAX_VALUE); i.moveNext(); ) {
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
         * @param owner         the owner that will own the renumbered trie
         * @param hashFunction    the hash function for data elements
         * @param equalsFunction  the equals function for data elements
         * @param factoryFunction the factory function for data elements
         * @param <K>
         * @return a new renumbered root
         */
        static <K extends ChampSequencedData> ChampTrie.BitmapIndexedNode<K> renumber(int size,
                                                                                      ChampTrie.BitmapIndexedNode<K> root,
                                                                                      ChampTrie.BitmapIndexedNode<K> sequenceRoot,
                                                                                      ChampTrie.IdentityObject owner,
                                                                                      ToIntFunction<K> hashFunction,
                                                                                      BiPredicate<K, K> equalsFunction,
                                                                                      BiFunction<K, Integer, K> factoryFunction

        ) {
            if (size == 0) {
                return root;
            }
            ChampTrie.BitmapIndexedNode<K> newRoot = root;
            ChampTrie.ChangeEvent<K> details = new ChampTrie.ChangeEvent<>();
            int seq = 0;

            for (ChampIteration.ChampSpliterator<K, K> i = new ChampIteration.ChampSpliterator<>(sequenceRoot, Function.identity(), 0, 0); i.moveNext(); ) {
                K e = i.current();
                K newElement = factoryFunction.apply(e, seq);
                newRoot = newRoot.put(owner,
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
         * @param owner         the owner that will own the renumbered trie
         * @param hashFunction    the hash function for data elements
         * @param equalsFunction  the equals function for data elements
         * @param factoryFunction the factory function for data elements
         * @return a new renumbered root and a new vector with matching entries
         */
        @SuppressWarnings("unchecked")
        static <K extends ChampSequencedData> Tuple2<ChampTrie.BitmapIndexedNode<K>, Vector<Object>> vecRenumber(
                int size,
                 ChampTrie.BitmapIndexedNode<K> root,
                 Vector<Object> vector,
                 ChampTrie.IdentityObject owner,
                 ToIntFunction<K> hashFunction,
                 BiPredicate<K, K> equalsFunction,
                 BiFunction<K, Integer, K> factoryFunction) {
            if (size == 0) {
                new Tuple2<>(root, vector);
            }
            ChampTrie.BitmapIndexedNode<K> renumberedRoot = root;
            Vector<Object> renumberedVector = Vector.of();
            ChampTrie.ChangeEvent<K> details = new ChampTrie.ChangeEvent<>();
            BiFunction<K, K, K> forceUpdate = (oldk, newk) -> newk;
            int seq = 0;
            for (ChampVectorSpliterator<K> i = new ChampVectorSpliterator<K>(vector, o -> (K) o, 0, Long.MAX_VALUE, 0); i.moveNext(); ) {
                K current = i.current();
                K data = factoryFunction.apply(current, seq++);
                renumberedVector = renumberedVector.append(data);
                renumberedRoot = renumberedRoot.put(owner, data, hashFunction.applyAsInt(current), 0, details, forceUpdate, equalsFunction, hashFunction);
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

        static <K extends ChampSequencedData> ChampTrie.BitmapIndexedNode<K> seqRemove(ChampTrie.BitmapIndexedNode<K> seqRoot, ChampTrie.IdentityObject owner,
                                                                                       K key, ChampTrie.ChangeEvent<K> details) {
            return seqRoot.remove(owner,
                    key, seqHash(key.getSequenceNumber()), 0, details,
                    ChampSequencedData::seqEquals);
        }

        static <K extends ChampSequencedData> ChampTrie.BitmapIndexedNode<K> seqUpdate(ChampTrie.BitmapIndexedNode<K> seqRoot, ChampTrie.IdentityObject owner,
                                                                                       K key, ChampTrie.ChangeEvent<K> details,
                                                                                       BiFunction<K, K, K> replaceFunction) {
            return seqRoot.put(owner,
                    key, seqHash(key.getSequenceNumber()), 0, details,
                    replaceFunction,
                    ChampSequencedData::seqEquals, ChampSequencedData::seqHash);
        }

        final static ChampTombstone TOMB_ZERO_ZERO = new ChampTombstone(0, 0);

        static <K extends ChampSequencedData> Tuple2<Vector<Object>, Integer> vecRemove(Vector<Object> vector, K oldElem, int offset) {
            // If the element is the first, we can remove it and its neighboring tombstones from the vector.
            int size = vector.size();
            int index = oldElem.getSequenceNumber() + offset;
            if (index == 0) {
                if (size > 1) {
                    Object o = vector.get(1);
                    if (o instanceof ChampTombstone) {
                        ChampTombstone t = (ChampTombstone) o;
                        return new Tuple2<>(vector.removeRange(0, 2 + t.after()), offset - 2 - t.after());
                    }
                }
                return new Tuple2<>(vector.tail(), offset - 1);
            }

            // If the element is the last , we can remove it and its neighboring tombstones from the vector.
            if (index == size - 1) {
                Object o = vector.get(size - 2);
                if (o instanceof ChampTombstone) {
                    ChampTombstone t = (ChampTombstone) o;
                    return new Tuple2<>(vector.removeRange(size - 2 - t.before(), size), offset);
                }
                return new Tuple2<>(vector.init(), offset);
            }

            // Otherwise, we replace the element with a tombstone, and we update before/after skip counts
            assert index > 0 && index < size - 1;
            Object before = vector.get(index - 1);
            Object after = vector.get(index + 1);
            if (before instanceof ChampTombstone && after instanceof ChampTombstone) {
                ChampTombstone tb = (ChampTombstone) before;
                ChampTombstone ta = (ChampTombstone) after;
                vector = vector.update(index - 1 - tb.before(), new ChampTombstone(0, 2 + tb.before() + ta.after()));
                vector = vector.update(index, TOMB_ZERO_ZERO);
                vector = vector.update(index + 1 + ta.after(), new ChampTombstone(2 + tb.before() + ta.after(), 0));
            } else if (before instanceof ChampTombstone) {
                ChampTombstone tb = (ChampTombstone) before;
                vector = vector.update(index - 1 - tb.before(), new ChampTombstone(0, 1 + tb.before()));
                vector = vector.update(index, new ChampTombstone(1 + tb.before(), 0));
            } else if (after instanceof ChampTombstone) {
                ChampTombstone ta = (ChampTombstone) after;
                vector = vector.update(index, new ChampTombstone(0, 1 + ta.after()));
                vector = vector.update(index + 1 + ta.after(), new ChampTombstone(1 + ta.after(), 0));
            } else {
                vector = vector.update(index, TOMB_ZERO_ZERO);
            }
            return new Tuple2<>(vector, offset);
        }


        static <T> Vector<T> removeRange(Vector<T> v, int fromIndex, int toIndex) {
            ChampTrie.ChampListHelper.checkIndex(fromIndex, toIndex + 1);
            ChampTrie.ChampListHelper.checkIndex(toIndex, v.size() + 1);
            if (fromIndex == 0) {
                return v.slice(toIndex, v.size());
            }
            if (toIndex == v.size()) {
                return v.slice(0, fromIndex);
            }
            final Vector<T> begin = v.slice(0, fromIndex);
            return begin.appendAll(() -> v.iterator(toIndex));
        }


        static <K extends ChampSequencedData> Vector<Object> vecUpdate(Vector<Object> newSeqRoot, ChampTrie.IdentityObject owner, K newElem, ChampTrie.ChangeEvent<K> details,
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

    /**
     * A {@code SequencedElement} stores an element of a set and a sequence number.
     * <p>
     * {@code hashCode} and {@code equals} are based on the element - the sequence
     * number is not included.
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
    static class ChampSequencedElement<E> implements ChampSequencedData {

        private final  E element;
        private final int sequenceNumber;

         ChampSequencedElement(E element) {
            this.element = element;
            this.sequenceNumber = NO_SEQUENCE_NUMBER;
        }

         ChampSequencedElement(E element, int sequenceNumber) {
            this.element = element;
            this.sequenceNumber = sequenceNumber;
        }
        public static int keyHash( Object a) {
            return Objects.hashCode(a);
        }


         static <E> ChampSequencedElement<E> forceUpdate(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
            return newK;
        }

         static <E> ChampSequencedElement<E> update(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
            return oldK;
        }


         static <E> ChampSequencedElement<E> updateAndMoveToFirst(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
            return oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
        }


         static <E> ChampSequencedElement<E> updateAndMoveToLast(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
            return oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            ChampSequencedElement<?> that = (ChampSequencedElement<?>) o;
            return Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(element);
        }

          E getElement() {
            return element;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        @Override
        public String toString() {
            return "{" +
                    "" + element +
                    ", seq=" + sequenceNumber +
                    '}';
        }
    }

    /**
     * A {@code ChampSequencedEntry} stores an entry of a map and a sequence number.
     * <p>
     * {@code hashCode} and {@code equals} are based on the key and the value
     * of the entry - the sequence number is not included.
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
    static class ChampSequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
            implements ChampSequencedData {

        private static final long serialVersionUID = 0L;
        private final int sequenceNumber;

         ChampSequencedEntry(K key) {
            super(key, null);
            sequenceNumber = NO_SEQUENCE_NUMBER;
        }

         ChampSequencedEntry(K key, V value) {
            super(key, value);
            sequenceNumber = NO_SEQUENCE_NUMBER;
        }
         ChampSequencedEntry(K key, V value, int sequenceNumber) {
            super(key, value);
            this.sequenceNumber = sequenceNumber;
        }

         static <K, V> ChampSequencedEntry<K, V> forceUpdate(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
            return newK;
        }
         static <K, V> boolean keyEquals(ChampSequencedEntry<K, V> a, ChampSequencedEntry<K, V> b) {
            return Objects.equals(a.getKey(), b.getKey());
        }

         static <K, V> boolean keyAndValueEquals(ChampSequencedEntry<K, V> a, ChampSequencedEntry<K, V> b) {
            return Objects.equals(a.getKey(), b.getKey()) && Objects.equals(a.getValue(), b.getValue());
        }

         static <V, K> int entryKeyHash(ChampSequencedEntry<K, V> a) {
            return Objects.hashCode(a.getKey());
        }

        static <V, K> int keyHash( Object key) {
            return Objects.hashCode(key);
        }
         static <K, V> ChampSequencedEntry<K, V> update(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
            return Objects.equals(oldK.getValue(), newK.getValue()) ? oldK :
                    new ChampSequencedEntry<>(oldK.getKey(), newK.getValue(), oldK.getSequenceNumber());
        }


         static <K, V> ChampSequencedEntry<K, V> updateAndMoveToFirst(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
            return Objects.equals(oldK.getValue(), newK.getValue())
                    && oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
        }


         static <K, V> ChampSequencedEntry<K, V> updateAndMoveToLast(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
            return Objects.equals(oldK.getValue(), newK.getValue())
                    && oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
        }

        // FIXME This behavior is enforced by AbstractMapTest.shouldPutExistingKeyAndNonEqualValue().<br>
        //     This behavior replaces the existing key with the new one if it has not the same identity.<br>
        //     This behavior does not match the behavior of java.util.HashMap.put().
        //     This behavior violates the contract of the map: we do create a new instance of the map,
        //     although it is equal to the previous instance.
         static <K, V> ChampSequencedEntry<K, V> updateWithNewKey(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
            return Objects.equals(oldK.getValue(), newK.getValue())
                    && oldK.getKey() == newK.getKey()
                    ? oldK
                    : new ChampSequencedEntry<>(newK.getKey(), newK.getValue(), oldK.getSequenceNumber());
        }
        public int getSequenceNumber() {
            return sequenceNumber;
        }
    }

    /**
     * A tombstone is used by {@code VectorSet} to mark a deleted slot in its Vector.
     * <p>
     * A tombstone stores the minimal number of neighbors 'before' and 'after' it in the
     * Vector.
     * <p>
     * When we insert a new tombstone, we update 'before' and 'after' values only on
     * the first and last tombstone of a sequence of tombstones. Therefore, a delete
     * operation requires reading of up to 3 neighboring elements in the vector, and
     * updates of up to 3 elements.
     * <p>
     * There are no tombstones at the first and last element of the vector. When we
     * remove the first or last element of the vector, we remove the tombstones.
     * <p>
     * Example: Tombstones are shown as <i>before</i>.<i>after</i>.
     * <pre>
     *
     *
     *                              Indices:  0   1   2   3   4   5   6   7   8   9
     * Initial situation:           Values:  'a' 'b' 'c' 'd' 'e' 'f' 'g' 'h' 'i' 'j'
     *
     * Deletion of element 5:
     * - read elements at indices 4, 5, 6                    'e' 'f' 'g'
     * - notice that none of them are tombstones
     * - put tombstone 0.0 at index 5                            0.0
     *
     * After deletion of element 5:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 'h' 'i' 'j'
     *
     * After deletion of element 7:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 0.0 'i' 'j'
     *
     * Deletion of element 8:
     * - read elements at indices 7, 8, 9                                0.0 'i' 'j'
     * - notice that 7 is a tombstone 0.0
     * - put tombstones 0.1, 1.0 at indices 7 and 8
     *
     * After deletion of element 8:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 0.1 1.0 'j'
     *
     * Deletion of element 6:
     * - read elements at indices 5, 6, 7                        0.0 'g' 0.1
     * - notice that two of them are tombstones
     * - put tombstones 0.3, 0.0, 3.0 at indices 5, 6 and 8
     *
     * After deletion of element 6:          'a' 'b' 'c' 'd' 'e' 0.3 0.0 0.1 3.0 'j'
     *
     * Deletion of the last element 9:
     * - read elements at index 8                                            3.0
     * - notice that it is a tombstone
     * - remove the last element and the neighboring tombstone sequence
     *
     * After deletion of element 9:          'a' 'b' 'c' 'd' 'e'
     * </pre>
     * References:
     * <p>
     * The code in this class has been derived from JHotDraw 8.
     * <p>
     * The design of this class is inspired by 'VectorMap.scala'.
     * <dl>
     *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
     *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
     *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a>
     *     </dd>
     *     <dt>VectorMap.scala
     *     <br>The Scala library. Copyright EPFL and Lightbend, Inc. Apache License 2.0.</dt>
     *     <dd><a href="https://github.com/scala/scala/blob/28eef15f3cc46f6d3dd1884e94329d7601dc20ee/src/library/scala/collection/immutable/VectorMap.scala">github.com</a>
     *     </dd>
     * </dl>
     */
    static final class ChampTombstone {
        private final int before;
        private final int after;

        /**
         * @param before minimal number of neighboring tombstones before this one
         * @param after  minimal number of neighboring tombstones after this one
         */
        ChampTombstone(int before, int after) {
            this.before = before;
            this.after = after;
        }

        public int before() {
            return before;
        }

        public int after() {
            return after;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            ChampTombstone that = (ChampTombstone) obj;
            return this.before == that.before &&
                    this.after == that.after;
        }

        @Override
        public int hashCode() {
            return Objects.hash(before, after);
        }

        @Override
        public String toString() {
            return "ChampTombstone[" +
                    "before=" + before + ", " +
                    "after=" + after + ']';
        }


    }
}
