/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;

/**
 * An immutable <a href="https://en.wikipedia.org/wiki/Hash_array_mapped_trie">Hash array mapped trie (HAMT)</a>.
 *
 * @since 2.0.0
 */
abstract class HashArrayMappedTrie<K, V> implements Iterable<Tuple2<K, V>>, Serializable {

    private static final long serialVersionUID = 1L;

    static <K, V> HashArrayMappedTrie<K, V> empty() {
        return EmptyNode.instance();
    }

    boolean isEmpty() {
        return this == empty();
    }

    abstract int size();

    Option<V> get(K key) {
        return ((AbstractNode<K, V>) this).lookup(0, key);
    }

    boolean containsKey(K key) {
        return get(key).isDefined();
    }

    HashArrayMappedTrie<K, V> put(K key, V value) {
        return ((AbstractNode<K, V>) this).modify(0, key, new Some<V>(value));
    }

    HashArrayMappedTrie<K, V> remove(K key) {
        return ((AbstractNode<K, V>) this).modify(0, key, None.instance());
    }

    private static abstract class AbstractNode<K, V> extends HashArrayMappedTrie<K, V> {

        private static final long serialVersionUID = 1L;

        static final int SIZE = 5;
        static final int BUCKET_SIZE = 1 << SIZE;
        static final int MAX_INDEX_NODE = BUCKET_SIZE / 2;
        static final int MIN_ARRAY_NODE = BUCKET_SIZE / 4;

        private static final int M1 = 0x55555555;
        private static final int M2 = 0x33333333;
        private static final int M4 = 0x0f0f0f0f;

        int bitCount(int x) {
            x = x - ((x >> 1) & M1);
            x = (x & M2) + ((x >> 2) & M2);
            x = (x + (x >> 4)) & M4;
            x = x + (x >> 8);
            x = x + (x >> 16);
            return x & 0x7f;
        }

        int hashFragment(int shift, int hash) {
            return (hash >>> shift) & (BUCKET_SIZE - 1);
        }

        int toBitmap(int hash) {
            return 1 << hash;
        }

        int fromBitmap(int bitmap, int bit) {
            return bitCount(bitmap & (bit - 1));
        }

        abstract boolean isLeaf();

        abstract Option<V> lookup(int shift, K key);

        abstract AbstractNode<K, V> modify(int shift, K key, Option<V> value);

        @Override
        public boolean equals(Object o) {
// TODO:
//            if (o == this) {
//                return true;
//            } else if (o instanceof AbstractNode) {
//                final AbstractNode<?, ?> that = (AbstractNode<?, ?>) o;
//                return ...;
//            } else {
//                return false;
//            }
            return super.equals(o);
        }

        @Override
        public int hashCode() {
            // TODO
            return super.hashCode();
        }

        @Override
        public String toString() {
            // TODO: return join(", ", "HashMap(", ")");
            return "HashMap";
        }
    }

    private static class EmptyNode<K, V> extends AbstractNode<K, V> {

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
        AbstractNode<K, V> modify(int shift, K key, Option<V> value) {
            return value.isEmpty() ? this : new LeafNode<>(key.hashCode(), key, value.get());
        }

        @Override
        boolean isLeaf() {
            return true;
        }

        @Override
        int size() {
            return 0;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return Collections.emptyIterator();
        }

    }

    private static class LeafNode<K, V> extends AbstractNode<K, V> {

        private static final long serialVersionUID = 1L;

        private final int hash;
        private final List<Tuple2<K, V>> entries;

        private LeafNode(int hash, K key, V value) {
            this(hash, List.of(Tuple.of(key, value)));
        }

        private LeafNode(int hash, List<Tuple2<K, V>> entries) {
            this.hash = hash;
            this.entries = entries;
        }

        // TODO
        AbstractNode<K, V> update(K key, Option<V> value) {
            List<Tuple2<K, V>> filtered = entries.filter(t -> !t._1.equals(key));
            if (value.isEmpty()) {
                return filtered.isEmpty() ? EmptyNode.instance() : new LeafNode<>(hash, filtered);
            } else {
                return new LeafNode<>(hash, filtered.append(Tuple.of(key, value.get())));
            }
        }

        @Override
        Option<V> lookup(int shift, K key) {
            if (hash != key.hashCode()) {
                return None.instance();
            }
            return entries.filter(t -> t._1.equals(key)).headOption().map(t -> t._2);
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, Option<V> value) {
            if (key.hashCode() == hash) {
                return update(key, value);
            } else {
                return value.isEmpty() ? this : mergeLeaves(shift, new LeafNode<>(key.hashCode(), key, value.get()));
            }
        }

        AbstractNode<K, V> mergeLeaves(int shift, LeafNode<K, V> other) {
            int h1 = this.hash;
            int h2 = other.hash;
            if (h1 == h2) {
                List<Tuple2<K, V>> newList = List.empty();
                newList.appendAll(this.entries);
                newList.appendAll(other.entries);
                return new LeafNode<>(h1, newList);
            }
            int subH1 = hashFragment(shift, h1);
            int subH2 = hashFragment(shift, h2);
            return new IndexedNode<>(toBitmap(subH1) | toBitmap(subH2),
                    subH1 == subH2 ?
                            List.of(mergeLeaves(shift + SIZE, other))
                            : subH1 < subH2 ? List.of(this, other) : List.of(other, this));
        }

        @Override
        boolean isLeaf() {
            return true;
        }

        @Override
        int size() {
            return entries.length();
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return entries.iterator();
        }
    }

    private static class IndexedNode<K, V> extends AbstractNode<K, V> {

        private static final long serialVersionUID = 1L;

        private final int bitmap;
        private final List<AbstractNode<K, V>> subNodes;
        private final int size;

        private IndexedNode(int bitmap, List<AbstractNode<K, V>> subNodes) {
            this.bitmap = bitmap;
            this.subNodes = subNodes;
            this.size = subNodes.map(HashArrayMappedTrie::size).sum().intValue();
        }

        @Override
        Option<V> lookup(int shift, K key) {
            int h = key.hashCode();
            int frag = hashFragment(shift, h);
            int bit = toBitmap(frag);
            return ((bitmap & bit) != 0) ? subNodes.get(fromBitmap(bitmap, bit)).lookup(shift + SIZE, key) : None.instance();
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, Option<V> value) {
            int frag = hashFragment(shift, key.hashCode());
            int bit = toBitmap(frag);
            int indx = fromBitmap(bitmap, bit);
            int mask = bitmap;
            boolean exists = (mask & bit) != 0;
            AbstractNode<K, V> child = exists ? subNodes.get(indx).modify(shift + SIZE, key, value)
                    : EmptyNode.<K, V> instance().modify(shift + SIZE, key, value);
            boolean removed = exists && child.isEmpty();
            boolean added = !exists && !child.isEmpty();
            int newBitmap = removed ? mask & ~bit : added ? mask | bit : mask;
            if (newBitmap == 0) {
                return EmptyNode.instance();
            } else if (removed) {
                if (subNodes.length() <= 2 && subNodes.get(indx ^ 1).isLeaf()) {
                    return subNodes.get(indx ^ 1); // collapse
                } else {
                    return new IndexedNode<>(newBitmap, removeIndx(subNodes, indx));
                }
            } else if (added) {
                if (subNodes.length() >= MAX_INDEX_NODE) {
                    return expand(frag, child, mask, subNodes);
                } else {
                    return new IndexedNode<>(newBitmap, subNodes.insert(indx, child));
                }
            } else {
                if (!exists) {
                    return this;
                } else {
                    return new IndexedNode<>(newBitmap, subNodes.set(indx, child));
                }
            }
        }

        List<AbstractNode<K, V>> removeIndx(List<AbstractNode<K, V>> l, int idx) {
            Tuple2<List<AbstractNode<K, V>>, List<AbstractNode<K, V>>> spl = l.splitAt(idx);
            List<AbstractNode<K, V>> rem = spl._1;
            if (!spl._2.isEmpty()) {
                rem = rem.appendAll(spl._2.tail());
            }
            return rem;
        }

        ArrayNode<K, V> expand(int frag, AbstractNode<K, V> child, int mask, List<AbstractNode<K, V>> subNodes) {
            int bit = mask;
            int count = 0;
            List<AbstractNode<K, V>> sub = subNodes;
            List<AbstractNode<K, V>> arr = List.empty();
            for (int i = 0; i < 32; i++) {
                if ((bit & 1) != 0) {
                    arr = arr.append(sub.head());
                    sub = sub.tail();
                    count++;
                } else if (i == frag) {
                    arr = arr.append(child);
                    count++;
                } else {
                    arr = arr.append(EmptyNode.instance());
                }
                bit = bit >>> 1;
            }
            return new ArrayNode<>(count, arr);
        }

        @Override
        boolean isLeaf() {
            return false;
        }

        @Override
        int size() {
            return size;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return subNodes.flatMap(child -> child::iterator).iterator();
        }

    }

    private static class ArrayNode<K, V> extends AbstractNode<K, V> {

        private static final long serialVersionUID = 1L;

        private final List<AbstractNode<K, V>> subNodes;
        private final int count;
        private final int size;

        private ArrayNode(int count, List<AbstractNode<K, V>> subNodes) {
            this.subNodes = subNodes;
            this.count = count;
            this.size = subNodes.map(HashArrayMappedTrie::size).sum().intValue();
        }

        @Override
        Option<V> lookup(int shift, K key) {
            int frag = hashFragment(shift, key.hashCode());
            AbstractNode<K, V> child = subNodes.get(frag);
            return child.lookup(shift + SIZE, key);
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, Option<V> value) {
            int frag = hashFragment(shift, key.hashCode());
            AbstractNode<K, V> child = subNodes.get(frag);
            AbstractNode<K, V> newChild = child.modify(shift + SIZE, key, value);
            if (child.isEmpty() && !newChild.isEmpty()) {
                return new ArrayNode<>(count + 1, subNodes.set(frag, newChild));
            } else if (!child.isEmpty() && newChild.isEmpty()) {
                if (count - 1 <= MIN_ARRAY_NODE) {
                    return pack(frag, subNodes);
                } else {
                    return new ArrayNode<>(count - 1, subNodes.set(frag, EmptyNode.instance()));
                }
            } else {
                return new ArrayNode<>(count, subNodes.set(frag, newChild));
            }
        }

        IndexedNode<K, V> pack(int idx, List<AbstractNode<K, V>> elements) {
            List<AbstractNode<K, V>> sub = elements;
            List<AbstractNode<K, V>> arr = List.empty();
            int bitmap = 0;
            for (int i = 0; !sub.isEmpty(); i++) {
                AbstractNode<K, V> elem = sub.head();
                sub = sub.tail();
                if (i != idx && elem != empty()) {
                    arr = arr.append(elem);
                    bitmap = bitmap | (1 << i);
                }
            }
            return new IndexedNode<>(bitmap, arr);
        }

        @Override
        boolean isLeaf() {
            return false;
        }

        @Override
        int size() {
            return size;
        }

        @Override
        public Iterator<Tuple2<K, V>> iterator() {
            return subNodes.flatMap(child -> child::iterator).iterator();
        }
    }
}
