/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Lazy;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashArrayMappedTrieModule.EmptyNode;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import static javaslang.collection.HashArrayMappedTrieModule.Action.PUT;
import static javaslang.collection.HashArrayMappedTrieModule.Action.REMOVE;

/**
 * An immutable <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @author Ruslan Sennov
 * @since 2.0.0
 */
interface HashArrayMappedTrie<K, V> extends java.lang.Iterable<Tuple2<K, V>> {

    static <K, V> HashArrayMappedTrie<K, V> empty() {
        return EmptyNode.instance();
    }

    boolean isEmpty();

    int size();

    Option<V> get(K key);

    boolean containsKey(K key);

    HashArrayMappedTrie<K, V> put(K key, V value);

    HashArrayMappedTrie<K, V> remove(K key);

    // this is a javaslang.collection.Iterator!
    @Override
    Iterator<Tuple2<K, V>> iterator();

}

interface HashArrayMappedTrieModule {

    enum Action {
        PUT, REMOVE
    }

    /**
     * An abstract base class for nodes of a HAMT.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    abstract class AbstractNode<K, V> implements HashArrayMappedTrie<K, V> {

        static final int SIZE = 5;
        static final int BUCKET_SIZE = 1 << SIZE;
        static final int MAX_INDEX_NODE = BUCKET_SIZE / 2;
        static final int MIN_ARRAY_NODE = BUCKET_SIZE / 4;

        private static final int M1 = 0x55555555;
        private static final int M2 = 0x33333333;
        private static final int M4 = 0x0f0f0f0f;

        static int bitCount(int x) {
            x = x - ((x >> 1) & M1);
            x = (x & M2) + ((x >> 2) & M2);
            x = (x + (x >> 4)) & M4;
            x = x + (x >> 8);
            x = x + (x >> 16);
            return x & 0x7f;
        }

        static int hashFragment(int shift, int hash) {
            return (hash >>> shift) & (BUCKET_SIZE - 1);
        }

        static int toBitmap(int hash) {
            return 1 << hash;
        }

        static int fromBitmap(int bitmap, int bit) {
            return bitCount(bitmap & (bit - 1));
        }

        abstract boolean isLeaf();

        abstract Option<V> lookup(int shift, K key);

        abstract AbstractNode<K, V> modify(int shift, K key, V value, Action action);

        @Override
        public Option<V> get(K key) {
            return lookup(0, key);
        }

        @Override
        public boolean containsKey(K key) {
            return get(key).isDefined();
        }

        @Override
        public HashArrayMappedTrie<K, V> put(K key, V value) {
            return modify(0, key, value, PUT);
        }

        @Override
        public HashArrayMappedTrie<K, V> remove(K key) {
            return modify(0, key, null, REMOVE);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof HashArrayMappedTrie) {
                final Iterator<?> iter1 = this.iterator();
                final Iterator<?> iter2 = ((HashArrayMappedTrie<?, ?>) o).iterator();
                while (iter1.hasNext() && iter2.hasNext()) {
                    if (!Objects.equals(iter1.next(), iter2.next())) {
                        return false;
                    }
                }
                return !iter1.hasNext() && !iter2.hasNext();
            } else {
                return false;
            }
        }

        @Override
        public abstract int hashCode();

        @Override
        public String toString() {
            return iterator().map(t -> t._1 + " -> " + t._2).mkString("HashArrayMappedTrie(", ", ", ")");
        }
    }

    /**
     * The empty node.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class EmptyNode<K, V> extends AbstractNode<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        private static final EmptyNode<?, ?> INSTANCE = new EmptyNode<>();

        private EmptyNode() {
        }

        @SuppressWarnings("unchecked")
        static <K, V> EmptyNode<K, V> instance() {
            return (EmptyNode<K, V>) INSTANCE;
        }

        @Override
        Option<V> lookup(int shift, K key) {
            return None.instance();
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value, Action action) {
            return (action == REMOVE) ? this : new LeafSingleton<>(Objects.hashCode(key), key, value);
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        boolean isLeaf() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return Iterator.empty();
        }

        /**
         * Instance control for object serialization.
         *
         * @return The singleton instance of EmptyNode.
         * @see java.io.Serializable
         */
        private Object readResolve() {
            return INSTANCE;
        }
    }

    /**
     * Representation of a HAMT leaf.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    abstract class LeafNode<K, V> extends AbstractNode<K, V> {

        abstract K key();
        abstract V value();
        abstract int hash();

        abstract AbstractNode<K, V> removeElement(K key);

        static <K, V> AbstractNode<K, V> mergeLeaves(int shift, LeafNode<K, V> leaf1, LeafSingleton<K, V> leaf2) {
            final int h1 = leaf1.hash();
            final int h2 = leaf2.hash();
            if (h1 == h2) {
                return new LeafList<>(h1, leaf2.key(), leaf2.value(), leaf1);
            }
            final int subH1 = hashFragment(shift, h1);
            final int subH2 = hashFragment(shift, h2);
            final int newBitmap = toBitmap(subH1) | toBitmap(subH2);
            if (subH1 == subH2) {
                AbstractNode<K, V> newLeaves = mergeLeaves(shift + SIZE, leaf1, leaf2);
                return new IndexedNode<>(newBitmap, newLeaves.size(), List.of(newLeaves));
            } else {
                return new IndexedNode<>(newBitmap, leaf1.size() + leaf2.size(),
                        subH1 < subH2 ? List.ofAll(leaf1, leaf2) : List.ofAll(leaf2, leaf1));
            }
        }

        @Override
        boolean isLeaf() {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }
    }

    /**
     * Representation of a HAMT leaf node with single element.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class LeafSingleton<K, V> extends LeafNode<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final int hash;
        private final K key;
        private final V value;
        private final Lazy<Integer> hashCode;

        LeafSingleton(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.hashCode = Lazy.of(() -> Objects.hash(key, value));
        }

        @Override
        Option<V> lookup(int shift, K key) {
            if (Objects.equals(key, this.key)) {
                return new Some<>(value);
            } else {
                return None.instance();
            }
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value, Action action) {
            if (Objects.equals(key, this.key)) {
                return (action == REMOVE) ? EmptyNode.instance() : new LeafSingleton<>(hash, key, value);
            } else {
                return (action == REMOVE) ? this : mergeLeaves(shift, this, new LeafSingleton<>(key.hashCode(), key, value));
            }
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            Tuple2<K, V> tuple = Tuple.of(key, value);
            return Iterator.of(tuple);
        }

        @Override
        public int hashCode() {
            return hashCode.get();
        }

        @Override
        AbstractNode<K, V> removeElement(K key) {
            return Objects.equals(key, this.key) ? EmptyNode.instance() : this;
        }

        @Override
        int hash() {
            return hash;
        }

        @Override
        K key() {
            return key;
        }

        @Override
        V value() {
            return value;
        }
    }

    /**
     * Representation of a HAMT leaf node with more than one element.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class LeafList<K, V> extends LeafNode<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final int hash;
        private final K key;
        private final V value;
        private final int size;
        private final LeafNode<K, V> tail;
        private final Lazy<Integer> hashCode;

        LeafList(int hash, K key, V value, LeafNode<K, V> tail) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.size = 1 + tail.size();
            this.tail = tail;
            this.hashCode = Lazy.of(() -> Objects.hash(key, value, tail));
        }

        @Override
        Option<V> lookup(int shift, K key) {
            if (hash != key.hashCode()) {
                return None.instance();
            }
            return iterator().findFirst(t -> Objects.equals(t._1, key)).map(t -> t._2);
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value, Action action) {
            if (key.hashCode() == hash) {
                AbstractNode<K, V> filtered = removeElement(key);
                if(action == REMOVE) {
                    return filtered;
                } else {
                    if (filtered.isEmpty()) {
                        return new LeafSingleton<>(hash, key, value);
                    } else {
                        return new LeafList<>(hash, key, value, (LeafNode<K, V>) filtered);
                    }
                }
            } else {
                return (action == REMOVE) ? this : mergeLeaves(shift, this, new LeafSingleton<>(key.hashCode(), key, value));
            }
        }

        @Override
        AbstractNode<K, V> removeElement(K k) {
            if(Objects.equals(k, this.key)) {
                return tail;
            } else {
                // recurrent calls is OK but can be improved
                AbstractNode<K, V> newTail = tail.removeElement(k);
                return newTail.isEmpty() ? new LeafSingleton<>(hash, key, value) : new LeafList<>(hash, key, value, (LeafNode<K, V>) newTail);
            }
        }

        @Override
        public int hashCode() {
            return hashCode.get();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return new AbstractIterator<Tuple2<K,V>>() {
                LeafNode<K, V> node = LeafList.this;

                @Override
                public boolean hasNext() {
                    return node != null;
                }

                @Override
                public Tuple2<K, V> next() {
                    if (!hasNext()) {
                        EMPTY.next();
                    }
                    Tuple2<K,V> tuple = Tuple.of(node.key(), node.value());
                    if(node instanceof LeafSingleton) {
                        node = null;
                    } else {
                        node = ((LeafList<K, V>) node).tail;
                    }
                    return tuple;
                }
            };
        }

        @Override
        int hash() {
            return hash;
        }

        @Override
        K key() {
            return key;
        }

        @Override
        V value() {
            return value;
        }
    }

    /**
     * Representation of a HAMT indexed node.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class IndexedNode<K, V> extends AbstractNode<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final int bitmap;
        private final int size;
        private final List<AbstractNode<K, V>> subNodes;

        IndexedNode(int bitmap, int size, List<AbstractNode<K, V>> subNodes) {
            this.bitmap = bitmap;
            this.size = size;
            this.subNodes = subNodes;
        }

        @Override
        Option<V> lookup(int shift, K key) {
            int h = Objects.hashCode(key);
            int frag = hashFragment(shift, h);
            int bit = toBitmap(frag);
            return ((bitmap & bit) != 0) ? subNodes.get(fromBitmap(bitmap, bit)).lookup(shift + SIZE, key) : None.instance();
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value, Action action) {
            final int frag = hashFragment(shift, Objects.hashCode(key));
            final int bit = toBitmap(frag);
            final int index = fromBitmap(bitmap, bit);
            final int mask = bitmap;
            final boolean exists = (mask & bit) != 0;
            final AbstractNode<K, V> atIndx = exists ? subNodes.get(index) : null;
            AbstractNode<K, V> child = exists ? atIndx.modify(shift + SIZE, key, value, action)
                    : EmptyNode.<K, V> instance().modify(shift + SIZE, key, value, action);
            boolean removed = exists && child.isEmpty();
            boolean added = !exists && !child.isEmpty();
            int newBitmap = removed ? mask & ~bit : added ? mask | bit : mask;
            if (newBitmap == 0) {
                return EmptyNode.instance();
            } else if (removed) {
                if (subNodes.length() <= 2 && subNodes.get(index ^ 1).isLeaf()) {
                    return subNodes.get(index ^ 1); // collapse
                } else {
                    return new IndexedNode<>(newBitmap, size - atIndx.size(), subNodes.removeAt(index));
                }
            } else if (added) {
                if (subNodes.length() >= MAX_INDEX_NODE) {
                    return expand(frag, child, mask, subNodes);
                } else {
                    return new IndexedNode<>(newBitmap, size + child.size(), subNodes.insert(index, child));
                }
            } else {
                if (!exists) {
                    return this;
                } else {
                    return new IndexedNode<>(newBitmap, size - atIndx.size() + child.size(), subNodes.update(index, child));
                }
            }
        }

        @Override
        public int hashCode() {
            return subNodes.hashCode();
        }

        private ArrayNode<K, V> expand(int frag, AbstractNode<K, V> child, int mask, List<AbstractNode<K, V>> subNodes) {
            int bit = mask;
            int count = 0;
            List<AbstractNode<K, V>> sub = subNodes;
            final Object[] arr = new Object[BUCKET_SIZE];
            for (int i = 0; i < BUCKET_SIZE; i++) {
                if ((bit & 1) != 0) {
                    arr[i] = sub.head();
                    sub = sub.tail();
                    count++;
                } else if (i == frag) {
                    arr[i] = child;
                    count++;
                } else {
                    arr[i] = EmptyNode.instance();
                }
                bit = bit >>> 1;
            }
            return new ArrayNode<>(count, size + child.size(), arr);
        }

        @Override
        boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return Iterator.concat(subNodes);
        }
    }

    /**
     * Representation of a HAMT array node.
     *
     * @param <K> Key type
     * @param <V> Value type
     */
    final class ArrayNode<K, V> extends AbstractNode<K, V> implements Serializable {

        private static final long serialVersionUID = 1L;

        private final transient Lazy<Integer> hashCode = Lazy.of(() -> Traversable.hash(this));
        private final Object[] subNodes;
        private final int count;
        private final int size;

        ArrayNode(int count, int size, Object[] subNodes) {
            this.subNodes = subNodes;
            this.count = count;
            this.size = size;
        }

        @SuppressWarnings("unchecked")
        @Override
        Option<V> lookup(int shift, K key) {
            int frag = hashFragment(shift, key.hashCode());
            AbstractNode<K, V> child = (AbstractNode<K, V>) subNodes[frag];
            return child.lookup(shift + SIZE, key);
        }

        @SuppressWarnings("unchecked")
        @Override
        AbstractNode<K, V> modify(int shift, K key, V value, Action action) {
            int frag = hashFragment(shift, key.hashCode());
            AbstractNode<K, V> child = (AbstractNode<K, V>) subNodes[frag];
            AbstractNode<K, V> newChild = child.modify(shift + SIZE, key, value, action);
            if (child.isEmpty() && !newChild.isEmpty()) {
                return new ArrayNode<>(count + 1, size + newChild.size(), update(subNodes, frag, newChild));
            } else if (!child.isEmpty() && newChild.isEmpty()) {
                if (count - 1 <= MIN_ARRAY_NODE) {
                    return pack(frag, subNodes);
                } else {
                    return new ArrayNode<>(count - 1, size - child.size(), update(subNodes, frag, EmptyNode.instance()));
                }
            } else {
                return new ArrayNode<>(count, size - child.size() + newChild.size(), update(subNodes, frag, newChild));
            }
        }

        @Override
        public int hashCode() {
            return hashCode.get();
        }

        private static Object[] update(Object[] arr, int index, Object newElement) {
            Object[] newArr = Arrays.copyOf(arr, arr.length);
            newArr[index] = newElement;
            return newArr;
        }

        @SuppressWarnings("unchecked")
        private IndexedNode<K, V> pack(int idx, Object[] elements) {
            List<AbstractNode<K, V>> arr = List.empty();
            int bitmap = 0;
            int size = 0;
            for (int i = BUCKET_SIZE - 1; i >= 0; i--) {
                AbstractNode<K, V> elem = (AbstractNode<K, V>) elements[i];
                if (i != idx && !elem.isEmpty()) {
                    size += elem.size();
                    arr = arr.prepend(elem);
                    bitmap = bitmap | (1 << i);
                }
            }
            return new IndexedNode<>(bitmap, size, arr);
        }

        @Override
        boolean isLeaf() {
            return false;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return Iterator.concat(Array.wrap(subNodes));
        }
    }
}