package io.vavr.collection.champ;


import java.util.Objects;

/**
 * A {@code SequencedElement} stores an element of a set and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the element - the sequence
 * number is not included.
 */
public class SequencedElement<E> implements SequencedData {

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

    @NonNull
    public static <E> SequencedElement<E> update(@NonNull SequencedElement<E> oldK, @NonNull SequencedElement<E> newK) {
        return oldK;
    }

    @NonNull
    public static <E> SequencedElement<E> forceUpdate(@NonNull SequencedElement<E> oldK, @NonNull SequencedElement<E> newK) {
        return newK;
    }

    @NonNull
    public static <E> SequencedElement<E> updateAndMoveToFirst(@NonNull SequencedElement<E> oldK, @NonNull SequencedElement<E> newK) {
        return oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    @NonNull
    public static <E> SequencedElement<E> updateAndMoveToLast(@NonNull SequencedElement<E> oldK, @NonNull SequencedElement<E> newK) {
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
