package io.vavr.collection.champ;


import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;

/**
 * Stores an element and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the key only.
 */
class SequencedElement<E> implements Sequenced {

    private final E element;
    private final int sequenceNumber;

    public SequencedElement(E element) {
        this.element = element;
        this.sequenceNumber = NO_SEQUENCE_NUMBER;
    }

    public SequencedElement(E element, int sequenceNumber) {
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

    /**
     * Renumbers the sequence numbers in all nodes from {@code 0} to {@code size}.
     * <p>
     * Afterwards the sequence number for the next inserted entry must be
     * set to the value {@code size};
     *
     * @param root    the root of the trie
     * @param mutator the mutator which will own all nodes of the trie
     * @param <K>     the key type
     * @return the new root
     */
    public static <K> BitmapIndexedNode<SequencedElement<K>> renumber(int size, BitmapIndexedNode<SequencedElement<K>> root, UniqueId mutator,
                                                                      ToIntFunction<SequencedElement<K>> hashFunction,
                                                                      BiPredicate<SequencedElement<K>, SequencedElement<K>> equalsFunction) {
        if (size == 0) {
            return root;
        }

        BitmapIndexedNode<SequencedElement<K>> newRoot = root;
        ChangeEvent<SequencedElement<K>> details = new ChangeEvent<>();
        int seq = 0;
        for (HeapSequencedIterator<SequencedElement<K>, K> i = new HeapSequencedIterator<>(size, root, false, null, SequencedElement::getElement); i.hasNext(); ) {
            K e = i.next();
            SequencedElement<K> newElement = new SequencedElement<>(e, seq);
            newRoot = newRoot.update(mutator,
                    newElement,
                    Objects.hashCode(e), 0, details,
                    (oldk, newk) -> oldk.getSequenceNumber() == newk.getSequenceNumber() ? oldk : newk,
                    equalsFunction, hashFunction);
            seq++;
        }
        return newRoot;
    }
}
