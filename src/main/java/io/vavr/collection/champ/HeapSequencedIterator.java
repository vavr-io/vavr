package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Iterates over {@link SequencedData} elements in a CHAMP trie in the
 * order of the sequence numbers.
 * <p>
 * Uses a {@link LongArrayHeap} and a data array for
 * ordering the elements. This approach uses more memory than
 * a {@link java.util.PriorityQueue} but is about twice as fast.
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>new instance: O(N)</li>
 *     <li>iterator.next: O(log N)</li>
 * </ul>
 *
 * @param <E> the type parameter of the  CHAMP trie {@link Node}s
 * @param <X> the type parameter of the {@link Iterator} interface
 */
class HeapSequencedIterator<E extends SequencedData, X> implements Iterator<X>, io.vavr.collection.Iterator<X> {
    private final LongArrayHeap queue;
    private E current;
    private boolean canRemove;
    private final E[] array;
    private final Function<E, X> mappingFunction;
    private final Consumer<E> removeFunction;

    /**
     * Creates a new instance.
     *
     * @param size            the size of the trie
     * @param rootNode        the root node of the trie
     * @param reversed        whether to iterate in the reversed sequence
     * @param removeFunction  this function is called when {@link Iterator#remove()}
     *                        is called
     * @param mappingFunction mapping function from {@code E} to {@code X}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public HeapSequencedIterator(int size, Node<? extends E> rootNode,
                                 boolean reversed,
                                 Consumer<E> removeFunction,
                                 Function<E, X> mappingFunction) {
        Preconditions.checkArgument(size >= 0, "size=%s", size);

        this.removeFunction = removeFunction;
        this.mappingFunction = mappingFunction;
        queue = new LongArrayHeap(size);
        array = (E[]) new SequencedData[size];
        int i = 0;
        for (Iterator<? extends E> it = new KeyIterator<>(rootNode, null); it.hasNext(); i++) {
            E k = it.next();
            array[i] = k;
            int sequenceNumber = k.getSequenceNumber();
            queue.addAsLong(((long) (reversed ? -sequenceNumber : sequenceNumber) << 32) | i);
        }
    }

    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public X next() {
        current = array[(int) queue.removeAsLong()];
        canRemove = true;
        return mappingFunction.apply(current);
    }

    @Override
    public void remove() {
        if (removeFunction == null) {
            throw new UnsupportedOperationException();
        }
        if (!canRemove) {
            throw new IllegalStateException();
        }
        removeFunction.accept(current);
        canRemove = false;
    }
}
