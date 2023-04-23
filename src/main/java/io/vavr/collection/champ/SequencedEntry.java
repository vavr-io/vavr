package io.vavr.collection.champ;


import java.io.Serial;
import java.util.AbstractMap;
import java.util.Objects;

/**
 * A {@code SequencedEntry} stores an entry of a map and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the key and the value
 * of the entry - the sequence number is not included.
 */
public class SequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
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

    public static <K, V> boolean keyAndValueEquals(@NonNull SequencedEntry<K, V> a, @NonNull SequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey()) && Objects.equals(a.getValue(), b.getValue());
    }

    public static <K, V> boolean keyEquals(@NonNull SequencedEntry<K, V> a, @NonNull SequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

    public static <V, K> int keyHash(@NonNull SequencedEntry<K, V> a) {
        return Objects.hashCode(a.getKey());
    }

    @NonNull
    public static <K, V> SequencedEntry<K, V> update(@NonNull SequencedEntry<K, V> oldK, @NonNull SequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue()) ? oldK :
                new SequencedEntry<>(oldK.getKey(), newK.getValue(), oldK.getSequenceNumber());
    }

    // FIXME This behavior is enforced by AbstractMapTest.shouldPutExistingKeyAndNonEqualValue().<br>
    //     This behavior replaces the existing key with the new one if it has not the same identity.<br>
    //     This behavior does not match the behavior of java.util.HashMap.put().
    //     This behavior violates the contract of the map: we do create a new instance of the map,
    //     although it is equal to the previous instance.
    @NonNull
    public static <K, V> SequencedEntry<K, V> updateWithNewKey(@NonNull SequencedEntry<K, V> oldK, @NonNull SequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getKey() == newK.getKey()
                ? oldK
                : new SequencedEntry<>(newK.getKey(), newK.getValue(), oldK.getSequenceNumber());
    }

    @NonNull
    public static <K, V> SequencedEntry<K, V> forceUpdate(@NonNull SequencedEntry<K, V> oldK, @NonNull SequencedEntry<K, V> newK) {
        return newK;
    }

    @NonNull
    public static <K, V> SequencedEntry<K, V> updateAndMoveToFirst(@NonNull SequencedEntry<K, V> oldK, @NonNull SequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    @NonNull
    public static <K, V> SequencedEntry<K, V> updateAndMoveToLast(@NonNull SequencedEntry<K, V> oldK, @NonNull SequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
