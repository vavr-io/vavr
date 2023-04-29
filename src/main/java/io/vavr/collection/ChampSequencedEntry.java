/*
 * @(#)SequencedEntry.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;



import java.io.Serial;
import java.util.AbstractMap;
import java.util.Objects;

/**
 * A {@code SequencedEntry} stores an entry of a map and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the key and the value
 * of the entry - the sequence number is not included.
 */
 class ChampSequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
        implements ChampSequencedData {
    @Serial
    private static final long serialVersionUID = 0L;
    private final int sequenceNumber;

    public ChampSequencedEntry(K key) {
        super(key, null);
        sequenceNumber = NO_SEQUENCE_NUMBER;
    }

    public ChampSequencedEntry(K key, V value, int sequenceNumber) {
        super(key, value);
        this.sequenceNumber = sequenceNumber;
    }

    public static <K, V> boolean keyEquals(ChampSequencedEntry<K, V> a, ChampSequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    public static <V, K> int keyHash( ChampSequencedEntry<K, V> a) {
        return Objects.hashCode(a.getKey());
    }

    
    public static <K, V> ChampSequencedEntry<K, V> update(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue()) ? oldK :
                new ChampSequencedEntry<>(oldK.getKey(), newK.getValue(), oldK.getSequenceNumber());
    }

    
    public static <K, V> ChampSequencedEntry<K, V> updateAndMoveToFirst(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    
    public static <K, V> ChampSequencedEntry<K, V> updateAndMoveToLast(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
