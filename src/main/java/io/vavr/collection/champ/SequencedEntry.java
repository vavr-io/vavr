package io.vavr.collection.champ;


import java.util.AbstractMap;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

class SequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
        implements SequencedData {
    private final static long serialVersionUID = 0L;
    private final int sequenceNumber;

    public SequencedEntry(K key) {
        this(key, null, NO_SEQUENCE_NUMBER);
    }

    public SequencedEntry(K key, V value) {
        this(key, value, NO_SEQUENCE_NUMBER);
    }

    public SequencedEntry(K key, V value, int sequenceNumber) {
        super(key, value);
        this.sequenceNumber = sequenceNumber;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Renumbers the sequence numbers in all nodes from {@code 0} to {@code size}.
     * <p>
     * Afterwards the sequence number for the next inserted entry must be
     * set to the value {@code size};
     *
     * @param <K>     the key type
     * @param root    the root of the trie
     * @param mutator the mutator which will own all nodes of the trie
     * @return the new root
     */
    public static <K, V> BitmapIndexedNode<SequencedEntry<K, V>> renumber(int size, BitmapIndexedNode<SequencedEntry<K, V>> root, UniqueId mutator,
                                                                          ToIntFunction<SequencedEntry<K, V>> hashFunction,
                                                                          BiPredicate<SequencedEntry<K, V>, SequencedEntry<K, V>> equalsFunction) {
        if (size == 0) {
            return root;
        }
        BitmapIndexedNode<SequencedEntry<K, V>> newRoot = root;
        ChangeEvent<SequencedEntry<K, V>> details = new ChangeEvent<>();
        int seq = 0;
        BiFunction<SequencedEntry<K, V>, SequencedEntry<K, V>, SequencedEntry<K, V>> updateFunction = (oldk, newk) -> oldk.getSequenceNumber() == newk.getSequenceNumber() ? oldk : newk;
        for (HeapSequencedIterator<SequencedEntry<K, V>, SequencedEntry<K, V>> i = new HeapSequencedIterator<>(size, root, false, null, Function.identity()); i.hasNext(); ) {
            SequencedEntry<K, V> e = i.next();
            SequencedEntry<K, V> newElement = new SequencedEntry<>(e.getKey(), e.getValue(), seq);
            newRoot = newRoot.update(mutator,
                    newElement,
                    Objects.hashCode(e.getKey()), 0, details,
                    updateFunction,
                    equalsFunction, hashFunction);
            seq++;
        }
        return newRoot;
    }

}
