/*
 * @(#)SequencedEntry.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

import java.util.AbstractMap;
import java.util.Objects;

/**
 * A {@code SequencedEntry} stores an entry of a map and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the key and the value
 * of the entry - the sequence number is not included.
 */
class SequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
        implements SequencedData {
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

    static <K, V> boolean keyEquals(@NonNull SequencedEntry<K, V> a, @NonNull SequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    static <K, V> boolean keyAndValueEquals(@NonNull SequencedEntry<K, V> a, @NonNull SequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey()) && Objects.equals(a.getValue(), b.getValue());
    }

    static <V, K> int keyHash(@NonNull SequencedEntry<K, V> a) {
        return Objects.hashCode(a.getKey());
    }
}
