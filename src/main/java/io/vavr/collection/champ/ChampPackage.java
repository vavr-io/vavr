package io.vavr.collection.champ;

import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.HashSet;
import io.vavr.collection.Maps;
import io.vavr.collection.Tree;
import io.vavr.control.Option;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

import static io.vavr.collection.champ.ChampPackage.BitmapIndexedNode.emptyNode;
import static io.vavr.collection.champ.ChampPackage.NodeFactory.newBitmapIndexedNode;
import static io.vavr.collection.champ.ChampPackage.NodeFactory.newHashCollisionNode;
import static java.lang.Integer.max;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * This package-private class lumps all the code together that would be in a non-exported package if we had Java 9 or
 * later.
 * <p>
 * Provides collections which use a Compressed Hash-Array Mapped Prefix-tree (CHAMP) as their internal data structure.
 * <p>
 * References:
 * <dl>
 *      <dt>Michael J. Steindorfer (2017).
 *      Efficient Immutable Collections.</dt>
 *      <dd><a href="https://michael.steindorfer.name/publications/phd-thesis-efficient-immutable-collections">michael.steindorfer.name</a>
 *      <dt>The Capsule Hash Trie Collections Library.
 *      Copyright (c) Michael Steindorfer. BSD-2-Clause License</dt>
 *      <dd><a href="https://github.com/usethesource/capsule">github.com</a>
 * </dl>
 */
class ChampPackage {
    /**
     * Abstract base class for CHAMP maps.
     *
     * @param <K> the key type of the map
     * @param <V> the value typeof the map
     */
    abstract static class AbstractChampMap<K, V, X> extends AbstractMap<K, V>
            implements Serializable, Cloneable {
        @Serial
        private final static long serialVersionUID = 0L;

        /**
         * The current mutator id of this map.
         * <p>
         * All nodes that have the same non-null mutator id, are exclusively owned
         * by this map, and therefore can be mutated without affecting other map.
         * <p>
         * If this mutator id is null, then this map does not own any nodes.
         */
        IdentityObject mutator;

        /**
         * The root of this CHAMP trie.
         */
        BitmapIndexedNode<X> root;

        /**
         * The number of entries in this map.
         */
        int size;

        /**
         * The number of times this map has been structurally modified.
         */
        int modCount;

        IdentityObject getOrCreateIdentity() {
            if (mutator == null) {
                mutator = new IdentityObject();
            }
            return mutator;
        }

        @Override
        @SuppressWarnings("unchecked")
        public AbstractChampMap<K, V, X> clone() {
            try {
                mutator = null;
                return (AbstractChampMap<K, V, X>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e);
            }
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof AbstractChampMap<?, ?, ?>) {
                AbstractChampMap<?, ?, ?> that = (AbstractChampMap<?, ?, ?>) o;
                return size == that.size && root.equivalent(that.root);
            }
            return super.equals(o);
        }

        @Override
        public V getOrDefault(Object key, V defaultValue) {
            return super.getOrDefault(key, defaultValue);
        }


        public Iterator<Entry<K, V>> iterator() {
            return entrySet().iterator();
        }

        @SuppressWarnings("unchecked")
        boolean removeEntry(final Object o) {
            if (containsEntry(o)) {
                assert o != null;
                remove(((Entry<K, V>) o).getKey());
                return true;
            }
            return false;
        }

        /**
         * Returns true if this map contains the specified entry.
         *
         * @param o an entry
         * @return true if this map contains the entry
         */
        protected boolean containsEntry(Object o) {
            if (o instanceof java.util.Map.Entry) {
                @SuppressWarnings("unchecked") Entry<K, V> entry = (Entry<K, V>) o;
                return containsKey(entry.getKey())
                        && Objects.equals(entry.getValue(), get(entry.getKey()));
            }
            return false;
        }
    }

    abstract static class AbstractChampSet<E, X> extends AbstractSet<E> implements Serializable, Cloneable {
        @Serial
        private final static long serialVersionUID = 0L;
        /**
         * The current mutator id of this set.
         * <p>
         * All nodes that have the same non-null mutator id, are exclusively owned
         * by this set, and therefore can be mutated without affecting other sets.
         * <p>
         * If this mutator id is null, then this set does not own any nodes.
         */
        IdentityObject mutator;

        /**
         * The root of this CHAMP trie.
         */
        BitmapIndexedNode<X> root;

        /**
         * The number of elements in this set.
         */
        int size;

        /**
         * The number of times this set has been structurally modified.
         */
        transient int modCount;

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return addAll((Iterable<? extends E>) c);
        }

        /**
         * Adds all specified elements that are not already in this set.
         *
         * @param c an iterable of elements
         * @return {@code true} if this set changed
         */
        public boolean addAll(Iterable<? extends E> c) {
            if (c == this) {
                return false;
            }
            boolean modified = false;
            for (E e : c) {
                modified |= add(e);
            }
            return modified;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof AbstractChampSet<?, ?>) {
                AbstractChampSet<?, ?> that = (AbstractChampSet<?, ?>) o;
                return size == that.size && root.equivalent(that.root);
            }
            return super.equals(o);
        }

        @Override
        public int size() {
            return size;
        }

        IdentityObject getOrCreateIdentity() {
            if (mutator == null) {
                mutator = new IdentityObject();
            }
            return mutator;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return removeAll((Iterable<?>) c);
        }

        /**
         * Removes all specified elements that are in this set.
         *
         * @param c an iterable of elements
         * @return {@code true} if this set changed
         */
        public boolean removeAll(Iterable<?> c) {
            if (isEmpty()) {
                return false;
            }
            if (c == this) {
                clear();
                return true;
            }
            boolean modified = false;
            for (Object o : c) {
                modified |= remove(o);
            }
            return modified;
        }


        @Override
        @SuppressWarnings("unchecked")
        public AbstractChampSet<E, X> clone() {
            try {
                mutator = null;
                return (AbstractChampSet<E, X>) super.clone();
            } catch (CloneNotSupportedException e) {
                throw new InternalError(e);
            }
        }
    }

    /**
     * Key iterator over a CHAMP trie.
     * <p>
     * Uses a stack with a fixed maximal depth.
     * Iterates over keys in preorder sequence.
     * <p>
     * Supports the {@code remove} operation. The remove function must
     * create a new version of the trie, so that iterator does not have
     * to deal with structural changes of the trie.
     */
    abstract static class AbstractKeySpliterator<K, E> implements EnumeratorSpliterator<E> {
        private final long size;

        @Override
        public Spliterator<E> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return size;
        }

        @Override
        public int characteristics() {
            return characteristics;
        }

        static class StackElement<K> {
            final @NonNull Node<K> node;
            int index;
            final int size;
            int map;

            public StackElement(@NonNull Node<K> node, boolean reverse) {
                this.node = node;
                this.size = node.nodeArity() + node.dataArity();
                this.index = reverse ? size - 1 : 0;
                this.map = (node instanceof BitmapIndexedNode<K> bin)
                        ? (bin.dataMap() | bin.nodeMap()) : 0;
            }
        }

        private final @NonNull Deque<StackElement<K>> stack = new ArrayDeque<>(Node.MAX_DEPTH);
        private K current;
        private final int characteristics;
        private final @NonNull Function<K, E> mappingFunction;

        public AbstractKeySpliterator(@NonNull Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
            if (root.nodeArity() + root.dataArity() > 0) {
                stack.push(new StackElement<>(root, isReverse()));
            }
            this.characteristics = characteristics;
            this.mappingFunction = mappingFunction;
            this.size = size;
        }

        abstract boolean isReverse();


        @Override
        public boolean moveNext() {
            while (!stack.isEmpty()) {
                StackElement<K> elem = stack.peek();
                Node<K> node = elem.node;

                if (node instanceof HashCollisionNode<K> hcn) {
                    current = hcn.getData(moveIndex(elem));
                    if (isDone(elem)) {
                        stack.pop();
                    }
                    return true;
                } else if (node instanceof BitmapIndexedNode<K> bin) {
                    int bitpos = getNextBitpos(elem);
                    elem.map ^= bitpos;
                    moveIndex(elem);
                    if (isDone(elem)) {
                        stack.pop();
                    }
                    if ((bin.nodeMap() & bitpos) != 0) {
                        stack.push(new StackElement<>(bin.nodeAt(bitpos), isReverse()));
                    } else {
                        current = bin.dataAt(bitpos);
                        return true;
                    }
                }
            }
            return false;
        }

        abstract int getNextBitpos(StackElement<K> elem);

        abstract int moveIndex(@NonNull ChampPackage.AbstractKeySpliterator.StackElement<K> elem);

        abstract boolean isDone(@NonNull ChampPackage.AbstractKeySpliterator.StackElement<K> elem);

        @Override
        public E current() {
            return mappingFunction.apply(current);
        }
    }

    /**
     * Represents a bitmap-indexed node in a CHAMP trie.
     *
     * @param <D> the data type
     */
    static class BitmapIndexedNode<D> extends Node<D> {
        static final @NonNull ChampPackage.BitmapIndexedNode<?> EMPTY_NODE = newBitmapIndexedNode(null, (0), (0), new Object[]{});

        final Object @NonNull [] mixed;
        private final int nodeMap;
        private final int dataMap;

        protected BitmapIndexedNode(int nodeMap,
                                    int dataMap, @NonNull Object @NonNull [] mixed) {
            this.nodeMap = nodeMap;
            this.dataMap = dataMap;
            this.mixed = mixed;
            assert mixed.length == nodeArity() + dataArity();
        }

        @SuppressWarnings("unchecked")
        static <K> @NonNull BitmapIndexedNode<K> emptyNode() {
            return (BitmapIndexedNode<K>) EMPTY_NODE;
        }

        @NonNull ChampPackage.BitmapIndexedNode<D> copyAndInsertData(@Nullable IdentityObject mutator, int bitpos,
                                                                     D data) {
            int idx = dataIndex(bitpos);
            Object[] dst = ListHelper.copyComponentAdd(this.mixed, idx, 1);
            dst[idx] = data;
            return newBitmapIndexedNode(mutator, nodeMap, dataMap | bitpos, dst);
        }

        @NonNull ChampPackage.BitmapIndexedNode<D> copyAndMigrateFromDataToNode(@Nullable IdentityObject mutator,
                                                                                int bitpos, Node<D> node) {

            int idxOld = dataIndex(bitpos);
            int idxNew = this.mixed.length - 1 - nodeIndex(bitpos);
            assert idxOld <= idxNew;

            // copy 'src' and remove entryLength element(s) at position 'idxOld' and
            // insert 1 element(s) at position 'idxNew'
            Object[] src = this.mixed;
            Object[] dst = new Object[src.length];
            System.arraycopy(src, 0, dst, 0, idxOld);
            System.arraycopy(src, idxOld + 1, dst, idxOld, idxNew - idxOld);
            System.arraycopy(src, idxNew + 1, dst, idxNew + 1, src.length - idxNew - 1);
            dst[idxNew] = node;
            return newBitmapIndexedNode(mutator, nodeMap | bitpos, dataMap ^ bitpos, dst);
        }

        @NonNull ChampPackage.BitmapIndexedNode<D> copyAndMigrateFromNodeToData(@Nullable IdentityObject mutator,
                                                                                int bitpos, @NonNull Node<D> node) {
            int idxOld = this.mixed.length - 1 - nodeIndex(bitpos);
            int idxNew = dataIndex(bitpos);

            // copy 'src' and remove 1 element(s) at position 'idxOld' and
            // insert entryLength element(s) at position 'idxNew'
            Object[] src = this.mixed;
            Object[] dst = new Object[src.length];
            assert idxOld >= idxNew;
            System.arraycopy(src, 0, dst, 0, idxNew);
            System.arraycopy(src, idxNew, dst, idxNew + 1, idxOld - idxNew);
            System.arraycopy(src, idxOld + 1, dst, idxOld + 1, src.length - idxOld - 1);
            dst[idxNew] = node.getData(0);
            return newBitmapIndexedNode(mutator, nodeMap ^ bitpos, dataMap | bitpos, dst);
        }

        @NonNull ChampPackage.BitmapIndexedNode<D> copyAndSetNode(@Nullable IdentityObject mutator, int bitpos,
                                                                  Node<D> node) {

            int idx = this.mixed.length - 1 - nodeIndex(bitpos);
            if (isAllowedToUpdate(mutator)) {
                // no copying if already editable
                this.mixed[idx] = node;
                return this;
            } else {
                // copy 'src' and set 1 element(s) at position 'idx'
                final Object[] dst = ListHelper.copySet(this.mixed, idx, node);
                return newBitmapIndexedNode(mutator, nodeMap, dataMap, dst);
            }
        }

        @Override
        int dataArity() {
            return Integer.bitCount(dataMap);
        }

        int dataIndex(int bitpos) {
            return Integer.bitCount(dataMap & (bitpos - 1));
        }

        int dataMap() {
            return dataMap;
        }

        @SuppressWarnings("unchecked")
        @Override
        boolean equivalent(@NonNull Object other) {
            if (this == other) {
                return true;
            }
            BitmapIndexedNode<?> that = (BitmapIndexedNode<?>) other;
            Object[] thatNodes = that.mixed;
            // nodes array: we compare local data from 0 to splitAt (excluded)
            // and then we compare the nested nodes from splitAt to length (excluded)
            int splitAt = dataArity();
            return nodeMap() == that.nodeMap()
                    && dataMap() == that.dataMap()
                    && Arrays.equals(mixed, 0, splitAt, thatNodes, 0, splitAt)
                    && Arrays.equals(mixed, splitAt, mixed.length, thatNodes, splitAt, thatNodes.length,
                    (a, b) -> ((Node<D>) a).equivalent(b) ? 0 : 1);
        }


        @Override
        @Nullable Object find(D key, int dataHash, int shift, @NonNull BiPredicate<D, D> equalsFunction) {
            int bitpos = bitpos(mask(dataHash, shift));
            if ((nodeMap & bitpos) != 0) {
                return nodeAt(bitpos).find(key, dataHash, shift + BIT_PARTITION_SIZE, equalsFunction);
            }
            if ((dataMap & bitpos) != 0) {
                D k = getData(dataIndex(bitpos));
                if (equalsFunction.test(k, key)) {
                    return k;
                }
            }
            return NO_DATA;
        }


        @Override
        @SuppressWarnings("unchecked")
        @NonNull
        D getData(int index) {
            return (D) mixed[index];
        }


        @Override
        @SuppressWarnings("unchecked")
        @NonNull
        Node<D> getNode(int index) {
            return (Node<D>) mixed[mixed.length - 1 - index];
        }

        @Override
        boolean hasData() {
            return dataMap != 0;
        }

        @Override
        boolean hasDataArityOne() {
            return Integer.bitCount(dataMap) == 1;
        }

        @Override
        boolean hasNodes() {
            return nodeMap != 0;
        }

        @Override
        int nodeArity() {
            return Integer.bitCount(nodeMap);
        }

        @SuppressWarnings("unchecked")
        @NonNull
        Node<D> nodeAt(int bitpos) {
            return (Node<D>) mixed[mixed.length - 1 - nodeIndex(bitpos)];
        }

        @SuppressWarnings("unchecked")
        @NonNull
        D dataAt(int bitpos) {
            return (D) mixed[dataIndex(bitpos)];
        }

        int nodeIndex(int bitpos) {
            return Integer.bitCount(nodeMap & (bitpos - 1));
        }

        int nodeMap() {
            return nodeMap;
        }

        @Override
        @NonNull ChampPackage.BitmapIndexedNode<D> remove(@Nullable IdentityObject mutator,
                                                          D data,
                                                          int dataHash, int shift,
                                                          @NonNull ChangeEvent<D> details, @NonNull BiPredicate<D, D> equalsFunction) {
            int mask = mask(dataHash, shift);
            int bitpos = bitpos(mask);
            if ((dataMap & bitpos) != 0) {
                return removeData(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
            }
            if ((nodeMap & bitpos) != 0) {
                return removeSubNode(mutator, data, dataHash, shift, details, bitpos, equalsFunction);
            }
            return this;
        }

        private @NonNull ChampPackage.BitmapIndexedNode<D> removeData(@Nullable IdentityObject mutator, D data, int dataHash, int shift, @NonNull ChangeEvent<D> details, int bitpos, @NonNull BiPredicate<D, D> equalsFunction) {
            int dataIndex = dataIndex(bitpos);
            int entryLength = 1;
            if (!equalsFunction.test(getData(dataIndex), data)) {
                return this;
            }
            D currentVal = getData(dataIndex);
            details.setRemoved(currentVal);
            if (dataArity() == 2 && !hasNodes()) {
                int newDataMap =
                        (shift == 0) ? (dataMap ^ bitpos) : bitpos(mask(dataHash, 0));
                Object[] nodes = {getData(dataIndex ^ 1)};
                return newBitmapIndexedNode(mutator, 0, newDataMap, nodes);
            }
            int idx = dataIndex * entryLength;
            Object[] dst = ListHelper.copyComponentRemove(this.mixed, idx, entryLength);
            return newBitmapIndexedNode(mutator, nodeMap, dataMap ^ bitpos, dst);
        }

        private @NonNull ChampPackage.BitmapIndexedNode<D> removeSubNode(@Nullable IdentityObject mutator, D data, int dataHash, int shift,
                                                                         @NonNull ChangeEvent<D> details,
                                                                         int bitpos, @NonNull BiPredicate<D, D> equalsFunction) {
            Node<D> subNode = nodeAt(bitpos);
            Node<D> updatedSubNode =
                    subNode.remove(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, equalsFunction);
            if (subNode == updatedSubNode) {
                return this;
            }
            if (!updatedSubNode.hasNodes() && updatedSubNode.hasDataArityOne()) {
                if (!hasData() && nodeArity() == 1) {
                    return (BitmapIndexedNode<D>) updatedSubNode;
                }
                return copyAndMigrateFromNodeToData(mutator, bitpos, updatedSubNode);
            }
            return copyAndSetNode(mutator, bitpos, updatedSubNode);
        }

        @Override
        @NonNull ChampPackage.BitmapIndexedNode<D> update(@Nullable IdentityObject mutator,
                                                          @Nullable D data,
                                                          int dataHash, int shift,
                                                          @NonNull ChangeEvent<D> details,
                                                          @NonNull BiFunction<D, D, D> replaceFunction,
                                                          @NonNull BiPredicate<D, D> equalsFunction,
                                                          @NonNull ToIntFunction<D> hashFunction) {
            int mask = mask(dataHash, shift);
            int bitpos = bitpos(mask);
            if ((dataMap & bitpos) != 0) {
                final int dataIndex = dataIndex(bitpos);
                final D oldKey = getData(dataIndex);
                if (equalsFunction.test(oldKey, data)) {
                    D updatedKey = replaceFunction.apply(oldKey, data);
                    if (updatedKey == oldKey) {
                        details.found(oldKey);
                        return this;
                    }
                    details.setReplaced(oldKey);
                    return copyAndSetData(mutator, dataIndex, updatedKey);
                }
                Node<D> updatedSubNode =
                        mergeTwoDataEntriesIntoNode(mutator,
                                oldKey, hashFunction.applyAsInt(oldKey),
                                data, dataHash, shift + BIT_PARTITION_SIZE);
                details.setAdded();
                return copyAndMigrateFromDataToNode(mutator, bitpos, updatedSubNode);
            } else if ((nodeMap & bitpos) != 0) {
                Node<D> subNode = nodeAt(bitpos);
                Node<D> updatedSubNode = subNode
                        .update(mutator, data, dataHash, shift + BIT_PARTITION_SIZE, details, replaceFunction, equalsFunction, hashFunction);
                return subNode == updatedSubNode ? this : copyAndSetNode(mutator, bitpos, updatedSubNode);
            }
            details.setAdded();
            return copyAndInsertData(mutator, bitpos, data);
        }

        @NonNull
        private ChampPackage.BitmapIndexedNode<D> copyAndSetData(@Nullable IdentityObject mutator, int dataIndex, D updatedData) {
            if (isAllowedToUpdate(mutator)) {
                this.mixed[dataIndex] = updatedData;
                return this;
            }
            Object[] newMixed = ListHelper.copySet(this.mixed, dataIndex, updatedData);
            return newBitmapIndexedNode(mutator, nodeMap, dataMap, newMixed);
        }
    }

    /**
     * This class is used to report a change (or no changes) of data in a CHAMP trie.
     *
     * @param <D> the data type
     */
    static class ChangeEvent<D> {
        enum Type {
            UNCHANGED,
            ADDED,
            REMOVED,
            REPLACED
        }

        private Type type = Type.UNCHANGED;
        private D data;

        public ChangeEvent() {
        }

        void found(D data) {
            this.data = data;
        }

        public D getData() {
            return data;
        }

        /**
         * Call this method to indicate that a data object has been
         * replaced.
         *
         * @param oldData the replaced data object
         */
        void setReplaced(D oldData) {
            this.data = oldData;
            this.type = Type.REPLACED;
        }

        /**
         * Call this method to indicate that a data object has been removed.
         *
         * @param oldData the removed data object
         */
        void setRemoved(D oldData) {
            this.data = oldData;
            this.type = Type.REMOVED;
        }

        /**
         * Call this method to indicate that an element has been added.
         */
        void setAdded() {
            this.type = Type.ADDED;
        }

        /**
         * Returns true if the CHAMP trie has been modified.
         */
        boolean isModified() {
            return type != Type.UNCHANGED;
        }

        /**
         * Returns true if the value of an element has been replaced.
         */
        boolean isReplaced() {
            return type == Type.REPLACED;
        }
    }

    /**
     * Interface for enumerating elements of a collection.
     * <p>
     * The protocol for accessing elements via a {@code Enumerator} imposes smaller per-element overhead than
     * {@link Iterator}, and avoids the inherent race involved in having separate methods for
     * {@code hasNext()} and {@code next()}.
     *
     * @param <E> the element type
     * @author Werner Randelshofer
     */
    static interface Enumerator<E> {
        /**
         * Advances the enumerator to the next element of the collection.
         *
         * @return true if the enumerator was successfully advanced to the next element;
         * false if the enumerator has passed the end of the collection.
         */
        boolean moveNext();

        /**
         * Gets the element in the collection at the current position of the enumerator.
         * <p>
         * Current is undefined under any of the following conditions:
         * <ul>
         * <li>The enumerator is positioned before the first element in the collection.
         * Immediately after the enumerator is created {@link #moveNext} must be called to advance
         * the enumerator to the first element of the collection before reading the value of Current.</li>
         *
         * <li>The last call to {@link #moveNext} returned false, which indicates the end
         * of the collection.</li>
         *
         * <li>The enumerator is invalidated due to changes made in the collection,
         * such as adding, modifying, or deleting elements.</li>
         * </ul>
         * Current returns the same object until MoveNext is called.MoveNext
         * sets Current to the next element.
         *
         * @return current
         */
        E current();
    }

    /**
     * Interface for enumerating elements of a collection.
     * <p>
     * The protocol for accessing elements via a {@code Enumerator} imposes smaller per-element overhead than
     * {@link Iterator}, and avoids the inherent race involved in having separate methods for
     * {@code hasNext()} and {@code next()}.
     *
     * @param <E> the element type
     * @author Werner Randelshofer
     */
    static interface EnumeratorSpliterator<E> extends Enumerator<E>, Spliterator<E> {
        @Override
        default boolean tryAdvance(@NonNull Consumer<? super E> action) {
            if (moveNext()) {
                action.accept(current());
                return true;
            }
            return false;
        }


    }

    static class FailFastIterator<E> implements Iterator<E>, io.vavr.collection.Iterator<E> {
        private final Iterator<? extends E> i;
        private int expectedModCount;
        private final IntSupplier modCountSupplier;

        public FailFastIterator(Iterator<? extends E> i, IntSupplier modCountSupplier) {
            this.i = i;
            this.modCountSupplier = modCountSupplier;
            this.expectedModCount = modCountSupplier.getAsInt();
        }

        @Override
        public boolean hasNext() {
            ensureUnmodified();
            return i.hasNext();
        }

        @Override
        public E next() {
            ensureUnmodified();
            return i.next();
        }

        protected void ensureUnmodified() {
            if (expectedModCount != modCountSupplier.getAsInt()) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void remove() {
            ensureUnmodified();
            i.remove();
            expectedModCount = modCountSupplier.getAsInt();
        }
    }

    /**
     * Represents a hash-collision node in a CHAMP trie.
     *
     * @param <D> the data type
     */
    static class HashCollisionNode<D> extends Node<D> {
        private final int hash;
        @NonNull Object[] data;

        HashCollisionNode(int hash, Object @NonNull [] data) {
            this.data = data;
            this.hash = hash;
        }

        @Override
        int dataArity() {
            return data.length;
        }

        @Override
        boolean hasDataArityOne() {
            return false;
        }

        @SuppressWarnings("unchecked")
        @Override
        boolean equivalent(@NonNull Object other) {
            if (this == other) {
                return true;
            }
            HashCollisionNode<?> that = (HashCollisionNode<?>) other;
            @NonNull Object[] thatEntries = that.data;
            if (hash != that.hash || thatEntries.length != data.length) {
                return false;
            }

            // Linear scan for each key, because of arbitrary element order.
            @NonNull Object[] thatEntriesCloned = thatEntries.clone();
            int remainingLength = thatEntriesCloned.length;
            outerLoop:
            for (Object key : data) {
                for (int j = 0; j < remainingLength; j += 1) {
                    Object todoKey = thatEntriesCloned[j];
                    if (Objects.equals((D) todoKey, (D) key)) {
                        // We have found an equal entry. We do not need to compare
                        // this entry again. So we replace it with the last entry
                        // from the array and reduce the remaining length.
                        System.arraycopy(thatEntriesCloned, remainingLength - 1, thatEntriesCloned, j, 1);
                        remainingLength -= 1;

                        continue outerLoop;
                    }
                }
                return false;
            }

            return true;
        }

        @SuppressWarnings("unchecked")
        @Override
        @Nullable
        Object find(D key, int dataHash, int shift, @NonNull BiPredicate<D, D> equalsFunction) {
            for (Object entry : data) {
                if (equalsFunction.test(key, (D) entry)) {
                    return entry;
                }
            }
            return NO_DATA;
        }

        @Override
        @SuppressWarnings("unchecked")
        @NonNull
        D getData(int index) {
            return (D) data[index];
        }

        @Override
        @NonNull
        Node<D> getNode(int index) {
            throw new IllegalStateException("Is leaf node.");
        }


        @Override
        boolean hasData() {
            return true;
        }

        @Override
        boolean hasNodes() {
            return false;
        }

        @Override
        int nodeArity() {
            return 0;
        }


        @SuppressWarnings("unchecked")
        @Override
        @Nullable
        Node<D> remove(@Nullable IdentityObject mutator, D data,
                       int dataHash, int shift, @NonNull ChampPackage.ChangeEvent<D> details, @NonNull BiPredicate<D, D> equalsFunction) {
            for (int idx = 0, i = 0; i < this.data.length; i += 1, idx++) {
                if (equalsFunction.test((D) this.data[i], data)) {
                    @SuppressWarnings("unchecked") D currentVal = (D) this.data[i];
                    details.setRemoved(currentVal);

                    if (this.data.length == 1) {
                        return BitmapIndexedNode.emptyNode();
                    } else if (this.data.length == 2) {
                        // Create root node with singleton element.
                        // This node will be a) either be the new root
                        // returned, or b) unwrapped and inlined.
                        return newBitmapIndexedNode(mutator, 0, bitpos(mask(dataHash, 0)),
                                new Object[]{getData(idx ^ 1)});
                    }
                    // copy keys and remove 1 element at position idx
                    Object[] entriesNew = ListHelper.copyComponentRemove(this.data, idx, 1);
                    if (isAllowedToUpdate(mutator)) {
                        this.data = entriesNew;
                        return this;
                    }
                    return newHashCollisionNode(mutator, dataHash, entriesNew);
                }
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override
        @Nullable
        Node<D> update(@Nullable IdentityObject mutator, D data,
                       int dataHash, int shift, @NonNull ChampPackage.ChangeEvent<D> details,
                       @NonNull BiFunction<D, D, D> replaceFunction, @NonNull BiPredicate<D, D> equalsFunction,
                       @NonNull ToIntFunction<D> hashFunction) {
            assert this.hash == dataHash;

            for (int i = 0; i < this.data.length; i++) {
                D oldKey = (D) this.data[i];
                if (equalsFunction.test(oldKey, data)) {
                    D updatedKey = replaceFunction.apply(oldKey, data);
                    if (updatedKey == oldKey) {
                        details.found(data);
                        return this;
                    }
                    details.setReplaced(oldKey);
                    if (isAllowedToUpdate(mutator)) {
                        this.data[i] = updatedKey;
                        return this;
                    }
                    final Object[] newKeys = ListHelper.copySet(this.data, i, updatedKey);
                    return newHashCollisionNode(mutator, dataHash, newKeys);
                }
            }

            // copy entries and add 1 more at the end
            Object[] entriesNew = ListHelper.copyComponentAdd(this.data, this.data.length, 1);
            entriesNew[this.data.length] = data;
            details.setAdded();
            if (isAllowedToUpdate(mutator)) {
                this.data = entriesNew;
                return this;
            }
            return newHashCollisionNode(mutator, dataHash, entriesNew);
        }
    }

    /**
     * An object with a unique identity within this VM.
     */
    static class IdentityObject implements Serializable {
        @Serial
        private final static long serialVersionUID = 0L;

        public IdentityObject() {
        }
    }

    /**
     * Wraps an {@link Enumerator} into an {@link Iterator} interface.
     *
     * @param <E> the element type
     */
    static class IteratorFacade<E> implements Iterator<E> {
        private final @NonNull ChampPackage.Enumerator<E> e;
        private final @Nullable Consumer<E> removeFunction;
        private boolean valueReady;
        private boolean canRemove;
        private E current;

        public IteratorFacade(@NonNull ChampPackage.Enumerator<E> e, @Nullable Consumer<E> removeFunction) {
            this.e = e;
            this.removeFunction = removeFunction;
        }

        @Override
        public boolean hasNext() {
            if (!valueReady) {
                // e.moveNext() changes e.current().
                // But the contract of hasNext() does not allow, that we change
                // the current value of the iterator.
                // This is why, we need a 'current' field in this facade.
                valueReady = e.moveNext();
            }
            return valueReady;
        }

        @Override
        public E next() {
            if (!valueReady && !hasNext()) {
                throw new NoSuchElementException();
            } else {
                valueReady = false;
                canRemove = true;
                return current = e.current();
            }
        }

        @Override
        public void remove() {
            if (!canRemove) throw new IllegalStateException();
            if (removeFunction != null) {
                removeFunction.accept(current);
                canRemove = false;
            } else {
                Iterator.super.remove();
            }
        }
    }

    /**
     * Wraps {@code Set} functions into the {@link Set} interface.
     *
     * @param <E> the element type of the set
     * @author Werner Randelshofer
     */
    static class JavaSetFacade<E> extends AbstractSet<E> {
        protected final Supplier<Iterator<E>> iteratorFunction;
        protected final IntSupplier sizeFunction;
        protected final Predicate<Object> containsFunction;
        protected final Predicate<E> addFunction;
        protected final Runnable clearFunction;
        protected final Predicate<Object> removeFunction;


        public JavaSetFacade(Set<E> backingSet) {
            this(backingSet::iterator, backingSet::size,
                    backingSet::contains, backingSet::clear, backingSet::add, backingSet::remove);
        }

        public JavaSetFacade(Supplier<Iterator<E>> iteratorFunction,
                             IntSupplier sizeFunction,
                             Predicate<Object> containsFunction) {
            this(iteratorFunction, sizeFunction, containsFunction, null, null, null);
        }

        public JavaSetFacade(Supplier<Iterator<E>> iteratorFunction,
                             IntSupplier sizeFunction,
                             Predicate<Object> containsFunction,
                             Runnable clearFunction,
                             Predicate<E> addFunction,
                             Predicate<Object> removeFunction) {
            this.iteratorFunction = iteratorFunction;
            this.sizeFunction = sizeFunction;
            this.containsFunction = containsFunction;
            this.clearFunction = clearFunction == null ? () -> {
                throw new UnsupportedOperationException();
            } : clearFunction;
            this.removeFunction = removeFunction == null ? o -> {
                throw new UnsupportedOperationException();
            } : removeFunction;
            this.addFunction = addFunction == null ? o -> {
                throw new UnsupportedOperationException();
            } : addFunction;
        }

        @Override
        public boolean remove(Object o) {
            return removeFunction.test(o);
        }

        @Override
        public void clear() {
            clearFunction.run();
        }

        @Override
        public Spliterator<E> spliterator() {
            return super.spliterator();
        }

        @Override
        public Stream<E> stream() {
            return super.stream();
        }

        @Override
        public Iterator<E> iterator() {
            return iteratorFunction.get();
        }

        /*
        //@Override  since 11
        public <T> T[] toArray(IntFunction<T[]> generator) {
            return super.toArray(generator);
        }*/

        @Override
        public int size() {
            return sizeFunction.getAsInt();
        }

        @Override
        public boolean contains(Object o) {
            return containsFunction.test(o);
        }

        @Override
        public boolean add(E e) {
            return addFunction.test(e);
        }

        public <U> U transform(Function<? super io.vavr.collection.Set<Long>, ? extends U> f) {
            // XXX CodingConventions.shouldHaveTransformMethodWhenIterable
            //     wants us to have a transform() method although this class
            //     is a standard Collection class.
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Key iterator over a CHAMP trie.
     * <p>
     * Uses a fixed stack in depth.
     * Iterates first over inlined data entries and then continues depth first.
     * <p>
     * Supports the {@code remove} operation. The functions that are
     * passed to this iterator must not change the trie structure that the iterator
     * currently uses.
     */
    static class KeyIterator<K> implements Iterator<K>, io.vavr.collection.Iterator<K> {

        private final int[] nodeCursorsAndLengths = new int[Node.MAX_DEPTH * 2];
        private int nextValueCursor;
        private int nextValueLength;
        private int nextStackLevel = -1;
        private Node<K> nextValueNode;
        private K current;
        private boolean canRemove = false;
        private final Consumer<K> removeFunction;
        @SuppressWarnings({"unchecked", "rawtypes"})
        private Node<K>[] nodes = new Node[Node.MAX_DEPTH];

        /**
         * Constructs a new instance.
         *
         * @param root           the root node of the trie
         * @param removeFunction a function that removes an entry from a field;
         *                       the function must not change the trie that was passed
         *                       to this iterator
         */
        public KeyIterator(Node<K> root, Consumer<K> removeFunction) {
            this.removeFunction = removeFunction;
            if (root.hasNodes()) {
                nextStackLevel = 0;
                nodes[0] = root;
                nodeCursorsAndLengths[0] = 0;
                nodeCursorsAndLengths[1] = root.nodeArity();
            }
            if (root.hasData()) {
                nextValueNode = root;
                nextValueCursor = 0;
                nextValueLength = root.dataArity();
            }
        }

        @Override
        public boolean hasNext() {
            if (nextValueCursor < nextValueLength) {
                return true;
            } else {
                return searchNextValueNode();
            }
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else {
                canRemove = true;
                current = nextValueNode.getData(nextValueCursor++);
                return current;
            }
        }

        /*
         * Searches for the next node that contains values.
         */
        private boolean searchNextValueNode() {
            while (nextStackLevel >= 0) {
                final int currentCursorIndex = nextStackLevel * 2;
                final int currentLengthIndex = currentCursorIndex + 1;
                final int nodeCursor = nodeCursorsAndLengths[currentCursorIndex];
                final int nodeLength = nodeCursorsAndLengths[currentLengthIndex];
                if (nodeCursor < nodeLength) {
                    final Node<K> nextNode = nodes[nextStackLevel].getNode(nodeCursor);
                    nodeCursorsAndLengths[currentCursorIndex]++;
                    if (nextNode.hasNodes()) {
                        // put node on next stack level for depth-first traversal
                        final int nextStackLevel = ++this.nextStackLevel;
                        final int nextCursorIndex = nextStackLevel * 2;
                        final int nextLengthIndex = nextCursorIndex + 1;
                        nodes[nextStackLevel] = nextNode;
                        nodeCursorsAndLengths[nextCursorIndex] = 0;
                        nodeCursorsAndLengths[nextLengthIndex] = nextNode.nodeArity();
                    }

                    if (nextNode.hasData()) {
                        //found next node that contains values
                        nextValueNode = nextNode;
                        nextValueCursor = 0;
                        nextValueLength = nextNode.dataArity();
                        return true;
                    }
                } else {
                    nextStackLevel--;
                }
            }
            return false;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            if (removeFunction == null) {
                throw new UnsupportedOperationException("remove");
            }
            K toRemove = current;
            removeFunction.accept(toRemove);
            canRemove = false;
            current = null;
        }
    }

    /**
     * Key iterator over a CHAMP trie.
     * <p>
     * Uses a stack with a fixed maximal depth.
     * Iterates over keys in preorder sequence.
     * <p>
     * Supports the {@code remove} operation. The remove function must
     * create a new version of the trie, so that iterator does not have
     * to deal with structural changes of the trie.
     */
    static class KeySpliterator<K, E> extends AbstractKeySpliterator<K, E> {
        public KeySpliterator(@NonNull Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
            super(root, mappingFunction, characteristics, size);
        }

        @Override
        boolean isReverse() {
            return false;
        }

        @Override
        int getNextBitpos(StackElement<K> elem) {
            return 1 << Integer.numberOfTrailingZeros(elem.map);
        }

        @Override
        boolean isDone(@NonNull StackElement<K> elem) {
            return elem.index >= elem.size;
        }

        @Override
        int moveIndex(@NonNull StackElement<K> elem) {
            return elem.index++;
        }

    }

    /**
     * Provides helper methods for lists that are based on arrays.
     *
     * @author Werner Randelshofer
     */
    static class ListHelper {
        /**
         * Don't let anyone instantiate this class.
         */
        private ListHelper() {

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
        public static <T> @NonNull T @NonNull [] copyAddAll(@NonNull T @NonNull [] src, int index, @NonNull T @NonNull [] values) {
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
        public static <T> @NonNull T @NonNull [] copyComponentAdd(@NonNull T @NonNull [] src, int index, int numComponents) {
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
        public static <T> @NonNull T @NonNull [] copyComponentRemove(@NonNull T @NonNull [] src, int index, int numComponents) {
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
        public static <T> @NonNull T @NonNull [] copySet(@NonNull T @NonNull [] src, int index, T value) {
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
        public static @NonNull Object @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final Object @NonNull [] items) {
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
        public static @NonNull double @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final double @NonNull [] items) {
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
        public static @NonNull byte @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final byte @NonNull [] items) {
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
        public static @NonNull short @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final short @NonNull [] items) {
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
        public static @NonNull int @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final int @NonNull [] items) {
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
        public static @NonNull long @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final long @NonNull [] items) {
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
        public static @NonNull char @NonNull [] grow(final int targetCapacity, final int itemSize, @NonNull final char @NonNull [] items) {
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
        public static @NonNull Object @NonNull [] trimToSize(final int size, final int itemSize, @NonNull final Object @NonNull [] items) {
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
        public static @NonNull int @NonNull [] trimToSize(final int size, final int itemSize, @NonNull final int @NonNull [] items) {
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
        public static @NonNull long @NonNull [] trimToSize(final int size, final int itemSize, @NonNull final long @NonNull [] items) {
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
        public static @NonNull double @NonNull [] trimToSize(final int size, final int itemSize, @NonNull final double @NonNull [] items) {
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
        public static @NonNull byte @NonNull [] trimToSize(final int size, final int itemSize, @NonNull final byte @NonNull [] items) {
            int newLength = size * itemSize;
            if (items.length == newLength) {
                return items;
            }
            return Arrays.copyOf(items, newLength);
        }
    }

    /**
     * Maps an {@link Iterator} in an {@link Iterator} of a different element type.
     * <p>
     * The underlying iterator is referenced - not copied.
     *
     * @param <E> the mapped element type
     * @param <F> the original element type
     * @author Werner Randelshofer
     */
    static class MappedIterator<E, F> implements Iterator<E>, io.vavr.collection.Iterator<E> {
        private final Iterator<F> i;

        private final Function<F, E> mappingFunction;

        public MappedIterator(Iterator<F> i, Function<F, E> mappingFunction) {
            this.i = i;
            this.mappingFunction = mappingFunction;
        }

        @Override
        public boolean hasNext() {
            return i.hasNext();
        }

        @Override
        public E next() {
            return mappingFunction.apply(i.next());
        }

        @Override
        public void remove() {
            i.remove();
        }
    }

    /**
     * A serialization proxy that serializes a map independently of its internal
     * structure.
     * <p>
     * Usage:
     * <pre>
     * class MyMap&lt;K, V&gt; implements Map&lt;K, V&gt;, Serializable {
     *   private final static long serialVersionUID = 0L;
     *
     *   private Object writeReplace() throws ObjectStreamException {
     *      return new SerializationProxy&lt;&gt;(this);
     *   }
     *
     *   static class SerializationProxy&lt;K, V&gt;
     *                  extends MapSerializationProxy&lt;K, V&gt; {
     *      private final static long serialVersionUID = 0L;
     *      SerializationProxy(Map&lt;K, V&gt; target) {
     *          super(target);
     *      }
     *     {@literal @Override}
     *      protected Object readResolve() {
     *          return new MyMap&lt;&gt;(deserialized);
     *      }
     *   }
     * }
     * </pre>
     * <p>
     * References:
     * <dl>
     *     <dt>Java Object Serialization Specification: 2 - Object Output Classes,
     *     2.5 The writeReplace Method</dt>
     *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/output.html#the-writereplace-method"></a>oracle.com</dd>
     *
     *     <dt>Java Object Serialization Specification: 3 - Object Input Classes,
     *     3.7 The readResolve Method</dt>
     *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/input.html#the-readresolve-method"></a>oracle.com</dd>
     * </dl>
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    abstract static class MapSerializationProxy<K, V> implements Serializable {
        private final transient Map<K, V> serialized;
        protected transient List<Map.Entry<K, V>> deserialized;
        @Serial
        private final static long serialVersionUID = 0L;

        protected MapSerializationProxy(Map<K, V> serialized) {
            this.serialized = serialized;
        }

        @Serial
        private void writeObject(ObjectOutputStream s)
                throws IOException {
            s.writeInt(serialized.size());
            for (Map.Entry<K, V> entry : serialized.entrySet()) {
                s.writeObject(entry.getKey());
                s.writeObject(entry.getValue());
            }
        }

        @Serial
        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            int n = s.readInt();
            deserialized = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                @SuppressWarnings("unchecked")
                K key = (K) s.readObject();
                @SuppressWarnings("unchecked")
                V value = (V) s.readObject();
                deserialized.add(new AbstractMap.SimpleImmutableEntry<>(key, value));
            }
        }

        @Serial
        protected abstract Object readResolve();
    }

    static class MutableBitmapIndexedNode<K> extends BitmapIndexedNode<K> {
        private final static long serialVersionUID = 0L;
        private final IdentityObject mutator;

        MutableBitmapIndexedNode(IdentityObject mutator, int nodeMap, int dataMap, Object[] nodes) {
            super(nodeMap, dataMap, nodes);
            this.mutator = mutator;
        }

        @Override
        protected IdentityObject getMutator() {
            return mutator;
        }
    }

    static class MutableHashCollisionNode<K> extends HashCollisionNode<K> {
        private final static long serialVersionUID = 0L;
        private final IdentityObject mutator;

        MutableHashCollisionNode(IdentityObject mutator, int hash, Object[] entries) {
            super(hash, entries);
            this.mutator = mutator;
        }

        @Override
        protected IdentityObject getMutator() {
            return mutator;
        }
    }

    /**
     * Represents a node in a 'Compressed Hash-Array Mapped Prefix-tree'
     * (CHAMP) trie.
     * <p>
     * A trie is a tree structure that stores a set of data objects; the
     * path to a data object is determined by a bit sequence derived from the data
     * object.
     * <p>
     * In a CHAMP trie, the bit sequence is derived from the hash code of a data
     * object. A hash code is a bit sequence with a fixed length. This bit sequence
     * is split up into parts. Each part is used as the index to the next child node
     * in the tree, starting from the root node of the tree.
     * <p>
     * The nodes of a CHAMP trie are compressed. Instead of allocating a node for
     * each data object, the data objects are stored directly in the ancestor node
     * at which the path to the data object starts to become unique. This means,
     * that in most cases, only a prefix of the bit sequence is needed for the
     * path to a data object in the tree.
     * <p>
     * If the hash code of a data object in the set is not unique, then it is
     * stored in a {@link HashCollisionNode}, otherwise it is stored in a
     * {@link BitmapIndexedNode}. Since the hash codes have a fixed length,
     * all {@link HashCollisionNode}s are located at the same, maximal depth
     * of the tree.
     * <p>
     * In this implementation, a hash code has a length of
     * {@value #HASH_CODE_LENGTH} bits, and is split up in little-endian order into parts of
     * {@value #BIT_PARTITION_SIZE} bits (the last part contains the remaining bits).
     *
     * @param <D> the type of the data objects that are stored in this trie
     */
    abstract static class Node<D> {
        /**
         * Represents no data.
         * We can not use {@code null}, because we allow storing null-data in the
         * trie.
         */
        static final Object NO_DATA = new Object();
        static final int HASH_CODE_LENGTH = 32;
        /**
         * Bit partition size in the range [1,5].
         * <p>
         * The bit-mask must fit into the 32 bits of an int field ({@code 32 = 1<<5}).
         * (You can use a size of 6, if you replace the bit-mask fields with longs).
         */
        static final int BIT_PARTITION_SIZE = 5;
        static final int BIT_PARTITION_MASK = (1 << BIT_PARTITION_SIZE) - 1;
        static final int MAX_DEPTH = (HASH_CODE_LENGTH + BIT_PARTITION_SIZE - 1) / BIT_PARTITION_SIZE + 1;


        Node() {
        }

        /**
         * Given a masked dataHash, returns its bit-position
         * in the bit-map.
         * <p>
         * For example, if the bit partition is 5 bits, then
         * we 2^5 == 32 distinct bit-positions.
         * If the masked dataHash is 3 then the bit-position is
         * the bit with index 3. That is, 1<<3 = 0b0100.
         *
         * @param mask masked data hash
         * @return bit position
         */
        static int bitpos(int mask) {
            return 1 << mask;
        }

        static <E> @NonNull E getFirst(@NonNull ChampPackage.Node<E> node) {
            while (node instanceof BitmapIndexedNode<E> bxn) {
                int nodeMap = bxn.nodeMap();
                int dataMap = bxn.dataMap();
                if ((nodeMap | dataMap) == 0) {
                    break;
                }
                int firstNodeBit = Integer.numberOfTrailingZeros(nodeMap);
                int firstDataBit = Integer.numberOfTrailingZeros(dataMap);
                if (nodeMap != 0 && firstNodeBit < firstDataBit) {
                    node = node.getNode(0);
                } else {
                    return node.getData(0);
                }
            }
            if (node instanceof HashCollisionNode<E> hcn) {
                return hcn.getData(0);
            }
            throw new NoSuchElementException();
        }

        static <E> @NonNull E getLast(@NonNull ChampPackage.Node<E> node) {
            while (node instanceof BitmapIndexedNode<E> bxn) {
                int nodeMap = bxn.nodeMap();
                int dataMap = bxn.dataMap();
                if ((nodeMap | dataMap) == 0) {
                    break;
                }
                int lastNodeBit = 32 - Integer.numberOfLeadingZeros(nodeMap);
                int lastDataBit = 32 - Integer.numberOfLeadingZeros(dataMap);
                if (lastNodeBit > lastDataBit) {
                    node = node.getNode(node.nodeArity() - 1);
                } else {
                    return node.getData(node.dataArity() - 1);
                }
            }
            if (node instanceof HashCollisionNode<E> hcn) {
                return hcn.getData(hcn.dataArity() - 1);
            }
            throw new NoSuchElementException();
        }

        static int mask(int dataHash, int shift) {
            return (dataHash >>> shift) & BIT_PARTITION_MASK;
        }

        static <K> @NonNull Node<K> mergeTwoDataEntriesIntoNode(IdentityObject mutator,
                                                                K k0, int keyHash0,
                                                                K k1, int keyHash1,
                                                                int shift) {
            if (shift >= HASH_CODE_LENGTH) {
                Object[] entries = new Object[2];
                entries[0] = k0;
                entries[1] = k1;
                return newHashCollisionNode(mutator, keyHash0, entries);
            }

            int mask0 = mask(keyHash0, shift);
            int mask1 = mask(keyHash1, shift);

            if (mask0 != mask1) {
                // both nodes fit on same level
                int dataMap = bitpos(mask0) | bitpos(mask1);

                Object[] entries = new Object[2];
                if (mask0 < mask1) {
                    entries[0] = k0;
                    entries[1] = k1;
                    return newBitmapIndexedNode(mutator, (0), dataMap, entries);
                } else {
                    entries[0] = k1;
                    entries[1] = k0;
                    return newBitmapIndexedNode(mutator, (0), dataMap, entries);
                }
            } else {
                Node<K> node = mergeTwoDataEntriesIntoNode(mutator,
                        k0, keyHash0,
                        k1, keyHash1,
                        shift + BIT_PARTITION_SIZE);
                // values fit on next level

                int nodeMap = bitpos(mask0);
                return newBitmapIndexedNode(mutator, nodeMap, (0), new Object[]{node});
            }
        }

        abstract int dataArity();

        /**
         * Checks if this trie is equivalent to the specified other trie.
         *
         * @param other the other trie
         * @return true if equivalent
         */
        abstract boolean equivalent(@NonNull Object other);

        /**
         * Finds a data object in the CHAMP trie, that matches the provided data
         * object and data hash.
         *
         * @param data           the provided data object
         * @param dataHash       the hash code of the provided data
         * @param shift          the shift for this node
         * @param equalsFunction a function that tests data objects for equality
         * @return the found data, returns {@link #NO_DATA} if no data in the trie
         * matches the provided data.
         */
        abstract Object find(D data, int dataHash, int shift, @NonNull BiPredicate<D, D> equalsFunction);

        abstract @Nullable D getData(int index);

        @Nullable ChampPackage.IdentityObject getMutator() {
            return null;
        }

        abstract @NonNull ChampPackage.Node<D> getNode(int index);

        abstract boolean hasData();

        abstract boolean hasDataArityOne();

        abstract boolean hasNodes();

        boolean isAllowedToUpdate(@Nullable ChampPackage.IdentityObject y) {
            IdentityObject x = getMutator();
            return x != null && x == y;
        }

        abstract int nodeArity();

        /**
         * Removes a data object from the trie.
         *
         * @param mutator        A non-null value means, that this method may update
         *                       nodes that are marked with the same unique id,
         *                       and that this method may create new mutable nodes
         *                       with this unique id.
         *                       A null value means, that this method must not update
         *                       any node and may only create new immutable nodes.
         * @param data           the data to be removed
         * @param dataHash       the hash-code of the data object
         * @param shift          the shift of the current node
         * @param details        this method reports the changes that it performed
         *                       in this object
         * @param equalsFunction a function that tests data objects for equality
         * @return the updated trie
         */
        abstract @NonNull ChampPackage.Node<D> remove(@Nullable ChampPackage.IdentityObject mutator, D data,
                                                      int dataHash, int shift,
                                                      @NonNull ChampPackage.ChangeEvent<D> details,
                                                      @NonNull BiPredicate<D, D> equalsFunction);

        /**
         * Inserts or replaces a data object in the trie.
         *
         * @param mutator         A non-null value means, that this method may update
         *                        nodes that are marked with the same unique id,
         *                        and that this method may create new mutable nodes
         *                        with this unique id.
         *                        A null value means, that this method must not update
         *                        any node and may only create new immutable nodes.
         * @param data            the data to be inserted,
         *                        or to be used for merging if there is already
         *                        a matching data object in the trie
         * @param dataHash        the hash-code of the data object
         * @param shift           the shift of the current node
         * @param details         this method reports the changes that it performed
         *                        in this object
         * @param replaceFunction only used if there is a matching data object
         *                        in the trie.
         *                        Given the existing data object (first argument) and
         *                        the new data object (second argument), yields a
         *                        new data object or returns either of the two.
         *                        In all cases, the update function must return
         *                        a data object that has the same data hash
         *                        as the existing data object.
         * @param equalsFunction  a function that tests data objects for equality
         * @param hashFunction    a function that computes the hash-code for a data
         *                        object
         * @return the updated trie
         */
        abstract @NonNull ChampPackage.Node<D> update(@Nullable ChampPackage.IdentityObject mutator, D data,
                                                      int dataHash, int shift, @NonNull ChampPackage.ChangeEvent<D> details,
                                                      @NonNull BiFunction<D, D, D> replaceFunction,
                                                      @NonNull BiPredicate<D, D> equalsFunction,
                                                      @NonNull ToIntFunction<D> hashFunction);
    }

    /**
     * Provides factory methods for {@link Node}s.
     */
    static class NodeFactory {

        /**
         * Don't let anyone instantiate this class.
         */
        private NodeFactory() {
        }

        static <K> @NonNull BitmapIndexedNode<K> newBitmapIndexedNode(
                @Nullable ChampPackage.IdentityObject mutator, int nodeMap,
                int dataMap, @NonNull Object[] nodes) {
            return mutator == null
                    ? new BitmapIndexedNode<>(nodeMap, dataMap, nodes)
                    : new MutableBitmapIndexedNode<>(mutator, nodeMap, dataMap, nodes);
        }

        static <K> @NonNull HashCollisionNode<K> newHashCollisionNode(
                @Nullable ChampPackage.IdentityObject mutator, int hash, @NonNull Object @NonNull [] entries) {
            return mutator == null
                    ? new HashCollisionNode<>(hash, entries)
                    : new MutableHashCollisionNode<>(mutator, hash, entries);
        }
    }

    /**
     * The Nullable annotation indicates that the {@code null} value is
     * allowed for the annotated element.
     */
    @Documented
    @Retention(CLASS)
    @Target({TYPE_USE, TYPE_PARAMETER, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
    static @interface Nullable {
    }

    static class MutableMapEntry<K, V> extends AbstractMap.SimpleEntry<K, V> {
        @Serial
        private final static long serialVersionUID = 0L;
        private final BiConsumer<K, V> putFunction;

        public MutableMapEntry(BiConsumer<K, V> putFunction, K key, V value) {
            super(key, value);
            this.putFunction = putFunction;
        }

        @Override
        public V setValue(V value) {
            V oldValue = super.setValue(value);
            putFunction.accept(getKey(), value);
            return oldValue;
        }
    }

    /**
     * Wraps {@code Set}  functions into the {@link io.vavr.collection.Set} interface.
     *
     * @param <E> the element type of the set
     */
    static class VavrSetFacade<E> implements VavrSetMixin<E, VavrSetFacade<E>> {
        @Serial
        private static final long serialVersionUID = 1L;
        protected final Function<E, io.vavr.collection.Set<E>> addFunction;
        protected final IntFunction<io.vavr.collection.Set<E>> dropRightFunction;
        protected final IntFunction<io.vavr.collection.Set<E>> takeRightFunction;
        protected final Predicate<E> containsFunction;
        protected final Function<E, io.vavr.collection.Set<E>> removeFunction;
        protected final Function<Iterable<? extends E>, io.vavr.collection.Set<E>> addAllFunction;
        protected final Supplier<io.vavr.collection.Set<E>> clearFunction;
        protected final Supplier<io.vavr.collection.Set<E>> initFunction;
        protected final Supplier<io.vavr.collection.Iterator<E>> iteratorFunction;
        protected final IntSupplier lengthFunction;
        protected final BiFunction<Object, BiFunction<? super E, ? super Object, Object>, Object> foldRightFunction;

        /**
         * Wraps the keys of the specified {@link io.vavr.collection.Map} into a {@link io.vavr.collection.Set} interface.
         *
         * @param map the map
         */
        public VavrSetFacade(io.vavr.collection.Map<E, ?> map) {
            this.addFunction = e -> new VavrSetFacade<>(map.put(e, null));
            this.foldRightFunction = (u, f) -> map.foldRight(u, (tuple, uu) -> f.apply(tuple._1(), uu));
            this.dropRightFunction = n -> new VavrSetFacade<>(map.dropRight(n));
            this.takeRightFunction = n -> new VavrSetFacade<>(map.takeRight(n));
            this.containsFunction = map::containsKey;
            this.clearFunction = () -> new VavrSetFacade<>(map.dropRight(map.length()));
            this.initFunction = () -> new VavrSetFacade<>(map.init());
            this.iteratorFunction = map::keysIterator;
            this.lengthFunction = map::length;
            this.removeFunction = e -> new VavrSetFacade<>(map.remove(e));
            this.addAllFunction = i -> {
                io.vavr.collection.Map<E, ?> m = map;
                for (E e : i) {
                    m = m.put(e, null);
                }
                return new VavrSetFacade<>(m);
            };
        }

        public VavrSetFacade(Function<E, io.vavr.collection.Set<E>> addFunction,
                             IntFunction<io.vavr.collection.Set<E>> dropRightFunction,
                             IntFunction<io.vavr.collection.Set<E>> takeRightFunction,
                             Predicate<E> containsFunction,
                             Function<E, io.vavr.collection.Set<E>> removeFunction,
                             Function<Iterable<? extends E>, io.vavr.collection.Set<E>> addAllFunction,
                             Supplier<io.vavr.collection.Set<E>> clearFunction,
                             Supplier<io.vavr.collection.Set<E>> initFunction,
                             Supplier<io.vavr.collection.Iterator<E>> iteratorFunction, IntSupplier lengthFunction,
                             BiFunction<Object, BiFunction<? super E, ? super Object, Object>, Object> foldRightFunction) {
            this.addFunction = addFunction;
            this.dropRightFunction = dropRightFunction;
            this.takeRightFunction = takeRightFunction;
            this.containsFunction = containsFunction;
            this.removeFunction = removeFunction;
            this.addAllFunction = addAllFunction;
            this.clearFunction = clearFunction;
            this.initFunction = initFunction;
            this.iteratorFunction = iteratorFunction;
            this.lengthFunction = lengthFunction;
            this.foldRightFunction = foldRightFunction;
        }

        @Override
        public io.vavr.collection.Set<E> add(E element) {
            return addFunction.apply(element);
        }

        @Override
        public io.vavr.collection.Set<E> addAll(Iterable<? extends E> elements) {
            return addAllFunction.apply(elements);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <R> io.vavr.collection.Set<R> create() {
            return (io.vavr.collection.Set<R>) clearFunction.get();
        }

        @Override
        public <R> io.vavr.collection.Set<R> createFromElements(Iterable<? extends R> elements) {
            return this.<R>create().addAll(elements);
        }

        @Override
        public io.vavr.collection.Set<E> remove(E element) {
            return removeFunction.apply(element);
        }

        @Override
        public boolean contains(E element) {
            return containsFunction.test(element);
        }

        @Override
        public io.vavr.collection.Set<E> dropRight(int n) {
            return dropRightFunction.apply(n);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> U foldRight(U zero, BiFunction<? super E, ? super U, ? extends U> combine) {
            return (U) foldRightFunction.apply(zero, (BiFunction<? super E, ? super Object, Object>) combine);
        }

        @Override
        public io.vavr.collection.Set<E> init() {
            return initFunction.get();
        }

        @Override
        public io.vavr.collection.Iterator<E> iterator() {
            return iteratorFunction.get();
        }

        @Override
        public int length() {
            return lengthFunction.getAsInt();
        }

        @Override
        public io.vavr.collection.Set<E> takeRight(int n) {
            return takeRightFunction.apply(n);
        }

        @Serial
        private Object writeReplace() {
            // FIXME WrappedVavrSet is not serializable. We convert
            //            it into a SequencedChampSet.
            return new LinkedHashSet.SerializationProxy<E>(this.toJavaSet());
        }
    }

    /**
     * A serialization proxy that serializes a set independently of its internal
     * structure.
     * <p>
     * Usage:
     * <pre>
     * class MySet&lt;E&gt; implements Set&lt;E&gt;, Serializable {
     *   private final static long serialVersionUID = 0L;
     *
     *   private Object writeReplace() throws ObjectStreamException {
     *      return new SerializationProxy&lt;&gt;(this);
     *   }
     *
     *   static class SerializationProxy&lt;E&gt;
     *                  extends SetSerializationProxy&lt;E&gt; {
     *      private final static long serialVersionUID = 0L;
     *      SerializationProxy(Set&lt;E&gt; target) {
     *          super(target);
     *      }
     *     {@literal @Override}
     *      protected Object readResolve() {
     *          return new MySet&lt;&gt;(deserialized);
     *      }
     *   }
     * }
     * </pre>
     * <p>
     * References:
     * <dl>
     *     <dt>Java Object Serialization Specification: 2 - Object Output Classes,
     *     2.5 The writeReplace Method</dt>
     *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/output.html#the-writereplace-method"></a>oracle.com</dd>
     *
     *     <dt>Java Object Serialization Specification: 3 - Object Input Classes,
     *     3.7 The readResolve Method</dt>
     *     <dd><a href="https://docs.oracle.com/en/java/javase/17/docs/specs/serialization/input.html#the-readresolve-method"></a>oracle.com</dd>
     * </dl>
     *
     * @param <E> the element type
     */
    abstract static class SetSerializationProxy<E> implements Serializable {
        @Serial
        private final static long serialVersionUID = 0L;
        private final transient Set<E> serialized;
        protected transient List<E> deserialized;

        protected SetSerializationProxy(Set<E> serialized) {
            this.serialized = serialized;
        }

        @Serial
        private void writeObject(ObjectOutputStream s)
                throws IOException {
            s.writeInt(serialized.size());
            for (E e : serialized) {
                s.writeObject(e);
            }
        }

        @Serial
        private void readObject(ObjectInputStream s)
                throws IOException, ClassNotFoundException {
            int n = s.readInt();
            deserialized = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                @SuppressWarnings("unchecked")
                E e = (E) s.readObject();
                deserialized.add(e);
            }
        }

        @Serial
        protected abstract Object readResolve();
    }

    /**
     * A {@code SequencedElement} stores an element of a set and a sequence number.
     * <p>
     * {@code hashCode} and {@code equals} are based on the element - the sequence
     * number is not included.
     */
    static class SequencedElement<E> implements SequencedData {

        private final @Nullable E element;
        private final int sequenceNumber;

        public SequencedElement(@Nullable E element) {
            this.element = element;
            this.sequenceNumber = NO_SEQUENCE_NUMBER;
        }

        public SequencedElement(@Nullable E element, int sequenceNumber) {
            this.element = element;
            this.sequenceNumber = sequenceNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SequencedElement<?> that = (SequencedElement<?>) o;
            return Objects.equals(element, that.element);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(element);
        }

        public E getElement() {
            return element;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }


    }

    /**
     * A {@code SequencedEntry} stores an entry of a map and a sequence number.
     * <p>
     * {@code hashCode} and {@code equals} are based on the key and the value
     * of the entry - the sequence number is not included.
     */
    static class SequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
            implements SequencedData {
        @Serial
        private final static long serialVersionUID = 0L;
        private final int sequenceNumber;

        public SequencedEntry(@Nullable K key) {
            this(key, null, NO_SEQUENCE_NUMBER);
        }

        public SequencedEntry(@Nullable K key, @Nullable V value) {
            this(key, value, NO_SEQUENCE_NUMBER);
        }

        public SequencedEntry(@Nullable K key, @Nullable V value, int sequenceNumber) {
            super(key, value);
            this.sequenceNumber = sequenceNumber;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        static <K, V> boolean keyEquals(@NonNull ChampPackage.SequencedEntry<K, V> a, @NonNull ChampPackage.SequencedEntry<K, V> b) {
            return Objects.equals(a.getKey(), b.getKey());
        }

        static <K, V> boolean keyAndValueEquals(@NonNull ChampPackage.SequencedEntry<K, V> a, @NonNull ChampPackage.SequencedEntry<K, V> b) {
            return Objects.equals(a.getKey(), b.getKey()) && Objects.equals(a.getValue(), b.getValue());
        }

        static <V, K> int keyHash(@NonNull ChampPackage.SequencedEntry<K, V> a) {
            return Objects.hashCode(a.getKey());
        }
    }

    /**
     * Wraps an {@link Enumerator} into an {@link io.vavr.collection.Iterator} interface.
     *
     * @param <E> the element type
     */
    static class VavrIteratorFacade<E> implements io.vavr.collection.Iterator<E> {
        private final @NonNull ChampPackage.Enumerator<E> e;
        private final @Nullable Consumer<E> removeFunction;
        private boolean valueReady;
        private boolean canRemove;
        private E current;

        public VavrIteratorFacade(@NonNull ChampPackage.Enumerator<E> e, @Nullable Consumer<E> removeFunction) {
            this.e = e;
            this.removeFunction = removeFunction;
        }

        @Override
        public boolean hasNext() {
            if (!valueReady) {
                // e.moveNext() changes e.current().
                // But the contract of hasNext() does not allow, that we change
                // the current value of the iterator.
                // This is why, we need a 'current' field in this facade.
                valueReady = e.moveNext();
            }
            return valueReady;
        }

        @Override
        public E next() {
            if (!valueReady && !hasNext()) {
                throw new NoSuchElementException();
            } else {
                valueReady = false;
                canRemove = true;
                return current = e.current();
            }
        }

        @Override
        public void remove() {
            if (!canRemove) throw new IllegalStateException();
            if (removeFunction != null) {
                removeFunction.accept(current);
                canRemove = false;
            } else {
                io.vavr.collection.Iterator.super.remove();
            }
        }
    }

    /**
     * Key iterator over a CHAMP trie.
     * <p>
     * Uses a stack with a fixed maximal depth.
     * Iterates over keys in preorder sequence.
     * <p>
     * Supports the {@code remove} operation. The remove function must
     * create a new version of the trie, so that iterator does not have
     * to deal with structural changes of the trie.
     */
    static class ReversedKeySpliterator<K, E> extends AbstractKeySpliterator<K, E> {
        public ReversedKeySpliterator(@NonNull ChampPackage.Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
            super(root, mappingFunction, characteristics, size);
        }

        @Override
        boolean isReverse() {
            return true;
        }

        @Override
        boolean isDone(@NonNull StackElement<K> elem) {
            return elem.index < 0;
        }

        @Override
        int moveIndex(@NonNull StackElement<K> elem) {
            return elem.index--;
        }

        @Override
        int getNextBitpos(StackElement<K> elem) {
            return 1 << (31 - Integer.numberOfLeadingZeros(elem.map));
        }

    }

    /**
     * This mixin-interface defines a {@link #create} method and a {@link #createFromEntries}
     * method, and provides default implementations for methods defined in the
     * {@link io.vavr.collection.Set} interface.
     *
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     */
    static interface VavrMapMixin<K, V> extends io.vavr.collection.Map<K, V> {
        long serialVersionUID = 1L;

        /**
         * Creates an empty map of the specified key and value types.
         *
         * @param <L> the key type of the map
         * @param <U> the value type of the map
         * @return a new empty map.
         */
        <L, U> io.vavr.collection.Map<L, U> create();

        /**
         * Creates an empty map of the specified key and value types,
         * and adds all the specified entries.
         *
         * @param entries the entries
         * @param <L>     the key type of the map
         * @param <U>     the value type of the map
         * @return a new map contains the specified entries.
         */
        <L, U> io.vavr.collection.Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries);

        @Override
        default <K2, V2> io.vavr.collection.Map<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
            Objects.requireNonNull(keyMapper, "keyMapper is null");
            Objects.requireNonNull(valueMapper, "valueMapper is null");
            final io.vavr.collection.Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
            return createFromEntries(entries);
        }

        @Override
        default Tuple2<V, ? extends io.vavr.collection.Map<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>computeIfAbsent(this, key, mappingFunction);
        }

        @Override
        default Tuple2<Option<V>, ? extends io.vavr.collection.Map<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>computeIfPresent(this, key, remappingFunction);
        }


        @Override
        default io.vavr.collection.Map<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filter(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterNot(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterKeys(Predicate<? super K> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterKeys(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterNotKeys(Predicate<? super K> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterNotKeys(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterValues(Predicate<? super V> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterValues(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterNotValues(Predicate<? super V> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterNotValues(this, this::createFromEntries, predicate);
        }

        @Override
        default <K2, V2> io.vavr.collection.Map<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return foldLeft(create(), (acc, entry) -> {
                for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                    acc = acc.put(mappedEntry);
                }
                return acc;
            });
        }

        @Override
        default V getOrElse(K key, V defaultValue) {
            return get(key).getOrElse(defaultValue);
        }

        @Override
        default Tuple2<K, V> last() {
            return Collections.last(this);
        }

        @Override
        default <K2, V2> io.vavr.collection.Map<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return foldLeft(create(), (acc, entry) -> acc.put(entry.map(mapper)));

        }

        @Override
        default <K2> io.vavr.collection.Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper) {
            Objects.requireNonNull(keyMapper, "keyMapper is null");
            return map((k, v) -> Tuple.of(keyMapper.apply(k), v));
        }

        @Override
        default <K2> io.vavr.collection.Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
            return Collections.mapKeys(this, create(), keyMapper, valueMerge);
        }

        @Override
        default <V2> io.vavr.collection.Map<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
            Objects.requireNonNull(valueMapper, "valueMapper is null");
            return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
        }

        @SuppressWarnings("unchecked")
        @Override
        default io.vavr.collection.Map<K, V> merge(io.vavr.collection.Map<? extends K, ? extends V> that) {
            if (that.isEmpty()) {
                return this;
            }
            if (isEmpty()) {
                return (io.vavr.collection.Map<K, V>) that;
            }
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>merge(this, this::createFromEntries, that);
        }

        @SuppressWarnings("unchecked")
        @Override
        default <U extends V> io.vavr.collection.Map<K, V> merge(io.vavr.collection.Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
            if (that.isEmpty()) {
                return this;
            }
            if (isEmpty()) {
                return (io.vavr.collection.Map<K, V>) that;
            }
            // Type parameters are needed by javac!
            return Maps.<K, V, U, io.vavr.collection.Map<K, V>>merge(this, this::createFromEntries, that, collisionResolution);
        }


        @Override
        default io.vavr.collection.Map<K, V> put(Tuple2<? extends K, ? extends V> entry) {
            return put(entry._1, entry._2);
        }

        @Override
        default <U extends V> io.vavr.collection.Map<K, V> put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge) {
            return Maps.put(this, key, value, merge);
        }

        @Override
        default <U extends V> io.vavr.collection.Map<K, V> put(Tuple2<? extends K, U> entry, BiFunction<? super V, ? super U, ? extends V> merge) {
            return Maps.put(this, entry, merge);
        }


        @Override
        default io.vavr.collection.Map<K, V> distinct() {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>distinct(this);
        }

        @Override
        default io.vavr.collection.Map<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>distinctBy(this, this::createFromEntries, comparator);
        }

        @Override
        default <U> io.vavr.collection.Map<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
            // Type parameters are needed by javac!
            return Maps.<K, V, U, io.vavr.collection.Map<K, V>>distinctBy(this, this::createFromEntries, keyExtractor);
        }

        @Override
        default io.vavr.collection.Map<K, V> drop(int n) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>drop(this, this::createFromEntries, this::create, n);
        }

        @Override
        default io.vavr.collection.Map<K, V> dropRight(int n) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>dropRight(this, this::createFromEntries, this::create, n);
        }

        @Override
        default io.vavr.collection.Map<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>dropUntil(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>dropWhile(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filter(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>filterNot(this, this::createFromEntries, predicate);
        }

        @Override
        default <C> io.vavr.collection.Map<C, ? extends io.vavr.collection.Map<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
            // Type parameters are needed by javac!
            return Maps.<K, V, C, io.vavr.collection.Map<K, V>>groupBy(this, this::createFromEntries, classifier);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Map<K, V>> grouped(int size) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>grouped(this, this::createFromEntries, size);
        }

        @Override
        default Tuple2<K, V> head() {
            if (isEmpty()) {
                throw new NoSuchElementException("head of empty HashMap");
            } else {
                return iterator().next();
            }
        }

        @Override
        default io.vavr.collection.Map<K, V> init() {
            if (isEmpty()) {
                throw new UnsupportedOperationException("init of empty HashMap");
            } else {
                return remove(last()._1);
            }
        }

        @Override
        default Option<? extends io.vavr.collection.Map<K, V>> initOption() {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>initOption(this);
        }

        @Override
        default io.vavr.collection.Map<K, V> orElse(Iterable<? extends Tuple2<K, V>> other) {
            return isEmpty() ? createFromEntries(other) : this;
        }

        @Override
        default io.vavr.collection.Map<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
            return isEmpty() ? createFromEntries(supplier.get()) : this;
        }

        @Override
        default Tuple2<? extends io.vavr.collection.Map<K, V>, ? extends io.vavr.collection.Map<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
            // Type parameters are needed by javac!
            return Maps.<K, V, io.vavr.collection.Map<K, V>>partition(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>peek(this, action);
        }

        @Override
        default io.vavr.collection.Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>replace(this, currentElement, newElement);
        }

        @Override
        default io.vavr.collection.Map<K, V> replaceValue(K key, V value) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>replaceValue(this, key, value);
        }

        @Override
        default io.vavr.collection.Map<K, V> replace(K key, V oldValue, V newValue) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>replace(this, key, oldValue, newValue);
        }

        @Override
        default io.vavr.collection.Map<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>replaceAll(this, function);
        }

        @Override
        default io.vavr.collection.Map<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>replaceAll(this, currentElement, newElement);
        }


        @Override
        default io.vavr.collection.Map<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>scan(this, zero, operation, this::createFromEntries);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Map<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>slideBy(this, this::createFromEntries, classifier);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Map<K, V>> sliding(int size) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>sliding(this, this::createFromEntries, size);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Map<K, V>> sliding(int size, int step) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>sliding(this, this::createFromEntries, size, step);
        }

        @Override
        default Tuple2<? extends io.vavr.collection.Map<K, V>, ? extends io.vavr.collection.Map<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>span(this, this::createFromEntries, predicate);
        }

        @Override
        default Option<? extends io.vavr.collection.Map<K, V>> tailOption() {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>tailOption(this);
        }

        @Override
        default io.vavr.collection.Map<K, V> take(int n) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>take(this, this::createFromEntries, n);
        }

        @Override
        default io.vavr.collection.Map<K, V> takeRight(int n) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>takeRight(this, this::createFromEntries, n);
        }

        @Override
        default io.vavr.collection.Map<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>takeUntil(this, this::createFromEntries, predicate);
        }

        @Override
        default io.vavr.collection.Map<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
            return Maps.<K, V, io.vavr.collection.Map<K, V>>takeWhile(this, this::createFromEntries, predicate);
        }

        @Override
        default boolean isAsync() {
            return false;
        }

        @Override
        default boolean isLazy() {
            return false;
        }

        @Override
        default String stringPrefix() {
            return getClass().getSimpleName();
        }
    }

    /**
     * A {@code SequencedData} stores a sequence number plus some data.
     * <p>
     * {@code SequencedData} objects are used to store sequenced data in a CHAMP
     * trie (see {@link Node}).
     * <p>
     * The kind of data is specified in concrete implementations of this
     * interface.
     * <p>
     * All sequence numbers of {@code SequencedData} objects in the same CHAMP trie
     * are unique. Sequence numbers range from {@link Integer#MIN_VALUE} (exclusive)
     * to {@link Integer#MAX_VALUE} (inclusive).
     */
    static interface SequencedData {
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

        /**
         * Gets the sequence number of the data.
         *
         * @return sequence number in the range from {@link Integer#MIN_VALUE}
         * (exclusive) to {@link Integer#MAX_VALUE} (inclusive).
         */
        int getSequenceNumber();

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
        static <K extends SequencedData> BitmapIndexedNode<K> renumber(int size,
                                                                       @NonNull ChampPackage.BitmapIndexedNode<K> root,
                                                                       @NonNull ChampPackage.BitmapIndexedNode<K> sequenceRoot,
                                                                       @NonNull ChampPackage.IdentityObject mutator,
                                                                       @NonNull ToIntFunction<K> hashFunction,
                                                                       @NonNull BiPredicate<K, K> equalsFunction,
                                                                       @NonNull BiFunction<K, Integer, K> factoryFunction

        ) {
            if (size == 0) {
                return root;
            }
            BitmapIndexedNode<K> newRoot = root;
            ChangeEvent<K> details = new ChangeEvent<>();
            int seq = 0;

            for (var i = new KeySpliterator<>(sequenceRoot, Function.identity(), 0, 0); i.moveNext(); ) {
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

        static <K extends SequencedData> BitmapIndexedNode<K> buildSequenceRoot(@NonNull ChampPackage.BitmapIndexedNode<K> root, @NonNull ChampPackage.IdentityObject mutator) {
            BitmapIndexedNode<K> seqRoot = emptyNode();
            ChangeEvent<K> details = new ChangeEvent<>();
            for (KeyIterator<K> i = new KeyIterator<>(root, null); i.hasNext(); ) {
                K elem = i.next();
                seqRoot = seqRoot.update(mutator, elem, SequencedData.seqHash(elem.getSequenceNumber()),
                        0, details, (oldK, newK) -> oldK, SequencedData::seqEquals, SequencedData::seqHash);
            }
            return seqRoot;
        }

        static <K extends SequencedData> boolean seqEquals(@NonNull K a, @NonNull K b) {
            return a.getSequenceNumber() == b.getSequenceNumber();
        }

        static <K extends SequencedData> int seqHash(K e) {
            return SequencedData.seqHash(e.getSequenceNumber());
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

    }

    /**
     * This mixin-interface defines a {@link #create} method and a {@link #createFromElements}
     * method, and provides default implementations for methods defined in the
     * {@link io.vavr.collection.Set} interface.
     *
     * @param <T> the element type of the set
     */
    @SuppressWarnings("unchecked")
    static
    interface VavrSetMixin<T, SELF extends VavrSetMixin<T, SELF>> extends io.vavr.collection.Set<T> {
        long serialVersionUID = 0L;

        /**
         * Creates an empty set of the specified element type.
         *
         * @param <R> the element type
         * @return a new empty set.
         */
        <R> io.vavr.collection.Set<R> create();

        /**
         * Creates an empty set of the specified element type, and adds all
         * the specified elements.
         *
         * @param elements the elements
         * @param <R>      the element type
         * @return a new set that contains the specified elements.
         */
        <R> io.vavr.collection.Set<R> createFromElements(Iterable<? extends R> elements);

        @Override
        default <R> io.vavr.collection.Set<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
            return createFromElements(iterator().<R>collect(partialFunction));
        }

        @Override
        default SELF diff(io.vavr.collection.Set<? extends T> that) {
            return removeAll(that);
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF distinct() {
            return (SELF) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF distinctBy(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator, "comparator is null");
            return (SELF) createFromElements(iterator().distinctBy(comparator));
        }

        @SuppressWarnings("unchecked")
        @Override
        default <U> SELF distinctBy(Function<? super T, ? extends U> keyExtractor) {
            Objects.requireNonNull(keyExtractor, "keyExtractor is null");
            return (SELF) createFromElements(iterator().distinctBy(keyExtractor));
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF drop(int n) {
            if (n <= 0) {
                return (SELF) this;
            }
            return (SELF) createFromElements(iterator().drop(n));
        }


        @Override
        default SELF dropUntil(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return dropWhile(predicate.negate());
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF dropWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final SELF dropped = (SELF) createFromElements(iterator().dropWhile(predicate));
            return dropped.length() == length() ? (SELF) this : dropped;
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF filter(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final SELF filtered = (SELF) createFromElements(iterator().filter(predicate));

            if (filtered.isEmpty()) {
                return (SELF) create();
            } else if (filtered.length() == length()) {
                return (SELF) this;
            } else {
                return filtered;
            }
        }

        @Override
        default SELF tail() {
            // XXX Traversable.tail() specifies that we must throw
            //     UnsupportedOperationException instead of
            //     NoSuchElementException.
            if (isEmpty()) {
                throw new UnsupportedOperationException();
            }
            return (SELF) remove(iterator().next());
        }

        @Override
        default <U> io.vavr.collection.Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
            io.vavr.collection.Set<U> flatMapped = this.create();
            for (T t : this) {
                for (U u : mapper.apply(t)) {
                    flatMapped = flatMapped.add(u);
                }
            }
            return flatMapped;
        }

        @Override
        default <U> io.vavr.collection.Set<U> map(Function<? super T, ? extends U> mapper) {
            io.vavr.collection.Set<U> mapped = this.create();
            for (T t : this) {
                mapped = mapped.add(mapper.apply(t));
            }
            return mapped;
        }

        @Override
        default SELF filterNot(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return filter(predicate.negate());
        }


        @Override
        default <C> io.vavr.collection.Map<C, ? extends io.vavr.collection.Set<T>> groupBy(Function<? super T, ? extends C> classifier) {
            return Collections.groupBy(this, classifier, this::createFromElements);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Set<T>> grouped(int size) {
            return sliding(size, size);
        }

        @Override
        default boolean hasDefiniteSize() {
            return true;
        }

        @Override
        default T head() {
            return iterator().next();
        }


        @Override
        default Option<? extends io.vavr.collection.Set<T>> initOption() {
            return tailOption();
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF intersect(io.vavr.collection.Set<? extends T> elements) {
            Objects.requireNonNull(elements, "elements is null");
            if (isEmpty() || elements.isEmpty()) {
                return (SELF) create();
            } else {
                final int size = size();
                if (size <= elements.size()) {
                    return retainAll(elements);
                } else {
                    final SELF results = (SELF) this.<T>createFromElements(elements).retainAll(this);
                    return (size == results.size()) ? (SELF) this : results;
                }
            }
        }

        @Override
        default boolean isAsync() {
            return false;
        }

        @Override
        default boolean isLazy() {
            return false;
        }

        @Override
        default boolean isTraversableAgain() {
            return true;
        }

        @Override
        default T last() {
            return Collections.last(this);
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF orElse(Iterable<? extends T> other) {
            return isEmpty() ? (SELF) createFromElements(other) : (SELF) this;
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF orElse(Supplier<? extends Iterable<? extends T>> supplier) {
            return isEmpty() ? (SELF) createFromElements(supplier.get()) : (SELF) this;
        }

        @Override
        default Tuple2<? extends io.vavr.collection.Set<T>, ? extends io.vavr.collection.Set<T>> partition(Predicate<? super T> predicate) {
            return Collections.partition(this, this::createFromElements, predicate);
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF peek(Consumer<? super T> action) {
            Objects.requireNonNull(action, "action is null");
            if (!isEmpty()) {
                action.accept(iterator().head());
            }
            return (SELF) this;
        }

        @Override
        default SELF removeAll(Iterable<? extends T> elements) {
            return (SELF) Collections.removeAll(this, elements);
        }

        @SuppressWarnings("unchecked")
        @Override
        default SELF replace(T currentElement, T newElement) {
            if (contains(currentElement)) {
                return (SELF) remove(currentElement).add(newElement);
            } else {
                return (SELF) this;
            }
        }

        @Override
        default SELF replaceAll(T currentElement, T newElement) {
            return replace(currentElement, newElement);
        }

        @Override
        default SELF retainAll(Iterable<? extends T> elements) {
            return (SELF) Collections.retainAll(this, elements);
        }

        @Override
        default SELF scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
            return (SELF) scanLeft(zero, operation);
        }

        @Override
        default <U> io.vavr.collection.Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
            return Collections.scanLeft(this, zero, operation, this::createFromElements);
        }

        @Override
        default <U> io.vavr.collection.Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
            return Collections.scanRight(this, zero, operation, this::createFromElements);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Set<T>> slideBy(Function<? super T, ?> classifier) {
            return iterator().slideBy(classifier).map(this::createFromElements);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Set<T>> sliding(int size) {
            return sliding(size, 1);
        }

        @Override
        default io.vavr.collection.Iterator<? extends io.vavr.collection.Set<T>> sliding(int size, int step) {
            return iterator().sliding(size, step).map(this::createFromElements);
        }

        @Override
        default Tuple2<? extends io.vavr.collection.Set<T>, ? extends io.vavr.collection.Set<T>> span(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final Tuple2<io.vavr.collection.Iterator<T>, io.vavr.collection.Iterator<T>> t = iterator().span(predicate);
            return Tuple.of(HashSet.ofAll(t._1), createFromElements(t._2));
        }

        @Override
        default String stringPrefix() {
            return getClass().getSimpleName();
        }

        @Override
        default Option<? extends io.vavr.collection.Set<T>> tailOption() {
            if (isEmpty()) {
                return Option.none();
            } else {
                return Option.some(tail());
            }
        }

        @Override
        default SELF take(int n) {
            if (n >= size() || isEmpty()) {
                return (SELF) this;
            } else if (n <= 0) {
                return (SELF) create();
            } else {
                return (SELF) createFromElements(() -> iterator().take(n));
            }
        }


        @Override
        default SELF takeUntil(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return takeWhile(predicate.negate());
        }

        @Override
        default SELF takeWhile(Predicate<? super T> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            final io.vavr.collection.Set<T> taken = createFromElements(iterator().takeWhile(predicate));
            return taken.length() == length() ? (SELF) this : (SELF) taken;
        }

        @Override
        default Set<T> toJavaSet() {
            return toJavaSet(java.util.HashSet::new);
        }

        @Override
        default SELF union(io.vavr.collection.Set<? extends T> that) {
            return (SELF) addAll(that);
        }

        @Override
        default <U> io.vavr.collection.Set<Tuple2<T, U>> zip(Iterable<? extends U> that) {
            return zipWith(that, Tuple::of);
        }

        /**
         * Transforms this {@code Set}.
         *
         * @param f   A transformation
         * @param <U> Type of transformation result
         * @return An instance of type {@code U}
         * @throws NullPointerException if {@code f} is null
         */
        default <U> U transform(Function<? super io.vavr.collection.Set<T>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        @Override
        default T get() {
            // XXX LinkedChampSetTest.shouldThrowWhenInitOfNil wants us to throw
            //     UnsupportedOperationException instead of NoSuchElementException
            //     when this set is empty.
            // XXX LinkedChampSetTest.shouldConvertEmptyToTry wants us to throw
            //     NoSuchElementException when this set is empty.
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            return head();
        }

        @Override
        default <U> io.vavr.collection.Set<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
            Objects.requireNonNull(that, "that is null");
            return createFromElements(iterator().zipAll(that, thisElem, thatElem));
        }

        @Override
        default <U, R> io.vavr.collection.Set<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
            Objects.requireNonNull(that, "that is null");
            Objects.requireNonNull(mapper, "mapper is null");
            return createFromElements(iterator().zipWith(that, mapper));
        }

        @Override
        default io.vavr.collection.Set<Tuple2<T, Integer>> zipWithIndex() {
            return zipWithIndex(Tuple::of);
        }

        @Override
        default <U> io.vavr.collection.Set<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
            Objects.requireNonNull(mapper, "mapper is null");
            return createFromElements(iterator().zipWithIndex(mapper));
        }

        @Override
        default SELF toSet() {
            return (SELF) this;
        }

        @Override
        default <ID> io.vavr.collection.List<Tree.Node<T>> toTree(Function<? super T, ? extends ID> idMapper, Function<? super T, ? extends ID> parentMapper) {
            // XXX AbstractTraversableTest.shouldConvertToTree() wants us to
            //     sort the elements by hash code.
            List<T> list = new ArrayList<T>(this.size());
            for (T t : this) {
                list.add(t);
            }
            list.sort(Comparator.comparing(Objects::hashCode));
            return Tree.build(list, idMapper, parentMapper);
        }
    }

    /**
     * The NonNull annotation indicates that the {@code null} value is
     * forbidden for the annotated element.
     */
    @Documented
    @Retention(CLASS)
    @Target({TYPE_USE, TYPE_PARAMETER, FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
    static @interface NonNull {
    }
}
