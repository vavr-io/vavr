/*
 * @(#)SequencedElement.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;


import java.util.Objects;

/**
 * A {@code SequencedElement} stores an element of a set and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the element - the sequence
 * number is not included.
 */
 class ChampSequencedElement<E> implements ChampSequencedData {

    private final  E element;
    private final int sequenceNumber;

    public ChampSequencedElement(E element) {
        this.element = element;
        this.sequenceNumber = NO_SEQUENCE_NUMBER;
    }

    public ChampSequencedElement(E element, int sequenceNumber) {
        this.element = element;
        this.sequenceNumber = sequenceNumber;
    }

    
    public static <E> ChampSequencedElement<E> update(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return oldK;
    }

    
    public static <E> ChampSequencedElement<E> updateAndMoveToFirst(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    
    public static <E> ChampSequencedElement<E> updateAndMoveToLast(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
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

    public  E getElement() {
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
