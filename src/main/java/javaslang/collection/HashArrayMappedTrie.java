package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;

/**
 * Hash array mapped trie
 * https://en.wikipedia.org/wiki/Hash_array_mapped_trie
 */
public interface HashArrayMappedTrie<K,V> {

    EmptyNode<?, ?> EMPTY = new EmptyNode<>();

    static <K, V> AbstractNode<K, V> empty() {
        @SuppressWarnings("unchecked")
        final EmptyNode<K, V> instance = (EmptyNode<K, V>) EMPTY;
        return instance;
    }

    default boolean isEmpty() {
        return this == empty();
    }

    int size();

    default Option<V> get(K key) {
        return ((AbstractNode<K, V>) this).lookup(0, key);
    }

    default HashArrayMappedTrie<K,V> add(K key, V value) {
        return ((AbstractNode<K, V>) this).modify(0, key, value);
    }

    default HashArrayMappedTrie<K,V> remove(K key) {
        return ((AbstractNode<K, V>) this).modify(0, key, null);
    }

    abstract class AbstractNode<K, V> implements HashArrayMappedTrie<K, V> {
        final static int SIZE = 5;
        final static int BUCKET_SIZE = 1 << SIZE;
        final static int MAX_INDEX_NODE = BUCKET_SIZE / 2;
        final static int MIN_ARRAY_NODE = BUCKET_SIZE / 4;
        final static int M1 = 0x55555555;
        final static int M2 = 0x33333333;
        final static int M4 = 0x0f0f0f0f;

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
        abstract AbstractNode<K,V> modify(int shift, K key, V value);
    }

    class EmptyNode<K, V> extends AbstractNode<K, V> {

        private EmptyNode() {}

        @Override
        public Option<V> lookup(int shift, K key) {
            return None.instance();
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value) {
            return value == null ? this : new LeafNode<>(key.hashCode(), key, value);
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        @Override
        public int size() {
            return 0;
        }
    }

    class LeafNode<K, V> extends AbstractNode<K, V> {
        final int hash;
        final List<Tuple2<K, V>> tuples;

        private LeafNode(int hash, K key, V value) {
            this(hash, List.of(Tuple.of(key, value)));
        }

        private LeafNode(int hash, List<Tuple2<K, V>> tuples) {
            this.hash = hash;
            this.tuples = tuples;
        }

        // TODO
        AbstractNode<K, V> update(K key, V value) {
            List<Tuple2<K, V>> filtered = tuples.filter(t -> !t._1.equals(key));
            if(value == null) {
                return filtered.isEmpty() ? empty() : new LeafNode<>(hash, filtered);
            } else {
                return new LeafNode<>(hash, filtered.append(Tuple.of(key, value)));
            }
        }

        @Override
        Option<V> lookup(int shift, K key) {
            if(hash != key.hashCode()) {
                return None.instance();
            }
            return tuples.filter(t -> t._1.equals(key)).headOption().map(t -> t._2);
        }

        @Override
        AbstractNode<K, V> modify(int shift, K key, V value) {
            if(key.hashCode() == hash) {
                return update(key, value);
            } else {
                return value == null ? this : mergeLeaves(shift, new LeafNode<>(key.hashCode(), key, value));
            }
        }

        AbstractNode<K, V> mergeLeaves(int shift, LeafNode<K, V> other) {
            int h1 = this.hash;
            int h2 = other.hash;
            if (h1 == h2) {
                List<Tuple2<K, V>> newList = List.empty();
                newList.appendAll(this.tuples);
                newList.appendAll(other.tuples);
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
        public boolean isLeaf() {
            return true;
        }

        @Override
        public int size() {
            return tuples.length();
        }
    }

    class IndexedNode<K, V> extends AbstractNode<K, V> {
        final int bitmap;
        final List<AbstractNode<K, V>> subNodes;
        final int size;

        IndexedNode(int bitmap, List<AbstractNode<K, V>> subNodes) {
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
        AbstractNode<K, V> modify(int shift, K key, V value) {
            int frag = hashFragment(shift, key.hashCode());
            int bit = toBitmap(frag);
            int indx = fromBitmap(bitmap, bit);
            int mask = bitmap;
            boolean exists = (mask & bit) != 0;
            AbstractNode<K, V> child = exists ? subNodes.get(indx).modify(shift + SIZE, key, value)
                    : HashArrayMappedTrie.<K,V>empty().modify(shift + SIZE, key, value);
            boolean removed = exists && child.isEmpty();
            boolean added = !exists && !child.isEmpty();
            int newBitmap = removed ? mask & ~bit : added ? mask | bit : mask;
            if(newBitmap == 0) {
                return empty();
            } else if (removed) {
                if (subNodes.length() <= 2 && subNodes.get(indx ^ 1).isLeaf()) {
                    return subNodes.get(indx ^ 1); // collapse
                } else {
                    Tuple2<List<AbstractNode<K,V>>,List<AbstractNode<K,V>>> spl = subNodes.splitAt(indx);
                    List<AbstractNode<K,V>> rem = spl._1;
                    if(!spl._2.isEmpty()) {
                        rem = rem.appendAll(spl._2.tail());
                    }
                    return new IndexedNode<>(newBitmap, rem);
                }
            } else if (added) {
                if(subNodes.length() >= MAX_INDEX_NODE) {
                    return expand(frag, child, mask, subNodes);
                } else {
                    return new IndexedNode<>(newBitmap, subNodes.insert(indx, child));
                }
            } else {
                return new IndexedNode<>(newBitmap, subNodes.set(indx, child));
            }
        }

        ArrayNode<K, V> expand(int frag, AbstractNode<K, V> child, int mask, List<AbstractNode<K, V>> subNodes)  {
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
                    arr = arr.append(empty());
                }
                bit = bit >>> 1;
            }
            return new ArrayNode<>(count, arr);
        }
        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public int size() {
            return size;
        }
    }

    class ArrayNode<K, V> extends AbstractNode<K, V> {
        final List<AbstractNode<K, V>> subNodes;
        final int count;
        final int size;

        ArrayNode(int count, List<AbstractNode<K, V>> subNodes) {
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
        AbstractNode<K, V> modify(int shift, K key, V value) {
            int frag = hashFragment(shift, key.hashCode());
            AbstractNode<K, V> child = subNodes.get(frag);
            AbstractNode<K, V> newChild = child.modify(shift + SIZE, key, value);
            if (child.isEmpty() && !newChild.isEmpty()) {
                return new ArrayNode<>(count + 1, subNodes.set(frag, newChild));
            } else if(!child.isEmpty() && newChild.isEmpty()) {
                if(count - 1 <= MIN_ARRAY_NODE) {
                    return pack(frag, this.subNodes);
                } else {
                    return new ArrayNode<>(count - 1, subNodes.set(frag, empty()));
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
        public boolean isLeaf() {
            return false;
        }

        @Override
        public int size() {
            return size;
        }
    }

}
