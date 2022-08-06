package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Iterates over {@link SequencedData} elements in a CHAMP trie in the order of the
 * sequence numbers.
 * <p>
 * Uses a bucket array for ordering the elements. The size of the array is
 * {@code last - first} sequence number.
 * This approach is fast, if the sequence numbers are dense, that is when
 * {@literal last - first <= size * 4}.
 * <p>
 * Performance characteristics:
 * <ul>
 *     <li>new instance: O(N)</li>
 *     <li>iterator.next: O(1)</li>
 * </ul>
 *
 * @param <E> the type parameter of the  CHAMP trie {@link Node}s
 * @param <X> the type parameter of the {@link Iterator} interface
 */
class BucketSequencedIterator<E extends SequencedData, X> implements Iterator<X>, io.vavr.collection.Iterator<X> {
    private int next;
    private int remaining;
    private E current;
    private final E[] buckets;
    private final Function<E, X> mappingFunction;
    private final Consumer<E> removeFunction;

    /**
     * Creates a new instance.
     *
     * @param size            the size of the trie
     * @param first           a sequence number which is smaller or equal the first sequence
     *                        number in the trie
     * @param last            a sequence number which is greater or equal the last sequence
     *                        number in the trie
     * @param rootNode        the root node of the trie
     * @param reversed        whether to iterate in the reversed sequence
     * @param removeFunction  this function is called when {@link Iterator#remove()}
     *                        is called
     * @param mappingFunction mapping function from {@code E} to {@code X}
     * @throws IllegalArgumentException if {@code last - first} is greater than
     *                                  {@link Integer#MAX_VALUE}.
     * @throws IllegalArgumentException if {@code size} is negative or
     *                                  greater than {@code last - first}..
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public BucketSequencedIterator(int size, int first, int last, Node<? extends E> rootNode,
                                   boolean reversed,
                                   Consumer<E> removeFunction,
                                   Function<E, X> mappingFunction) {
        long extent = (long) last - first;
        Preconditions.checkArgument(extent >= 0, "first=%s, last=%s", first, last);
        Preconditions.checkArgument(0 <= size && size <= extent, "size=%s, extent=%s", size, extent);
        this.removeFunction = removeFunction;
        this.mappingFunction = mappingFunction;
        this.remaining = size;
        if (size == 0) {
            buckets = (E[]) new SequencedData[0];
        } else {
            buckets = (E[]) new SequencedData[last - first];
            if (reversed) {
                int length = buckets.length;
                for (Iterator<? extends E> it = new KeyIterator<>(rootNode, null); it.hasNext(); ) {
                    E k = it.next();
                    buckets[length - 1 - k.getSequenceNumber() + first] = k;
                }
            } else {
                for (Iterator<? extends E> it = new KeyIterator<>(rootNode, null); it.hasNext(); ) {
                    E k = it.next();
                    buckets[k.getSequenceNumber() - first] = k;
                }
            }
        }
    }

    public static <E extends SequencedData> E getFirst(Node<? extends E> root, int first, int last) {
        int minSeq = last;
        E minKey = null;
        for (KeyIterator<? extends E> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            E k = i.next();
            int seq = k.getSequenceNumber();
            if (seq <= minSeq) {
                minSeq = seq;
                minKey = k;
                if (seq == first) {
                    break;
                }
            }
        }
        if (minKey == null) {
            throw new NoSuchElementException();
        }
        return minKey;
    }

    public static <E extends SequencedData> E getLast(Node<? extends E> root, int first, int last) {
        int maxSeq = first;
        E maxKey = null;
        for (KeyIterator<? extends E> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            E k = i.next();
            int seq = k.getSequenceNumber();
            if (seq >= maxSeq) {
                maxSeq = seq;
                maxKey = k;
                if (seq == last - 1) {
                    break;
                }
            }
        }
        if (maxKey == null) {
            throw new NoSuchElementException();
        }
        return maxKey;
    }

    @Override
    public boolean hasNext() {
        return remaining > 0;
    }

    @Override
    public X next() {
        if (remaining == 0) {
            throw new NoSuchElementException();
        }
        do {
            current = buckets[next++];
        } while (current == null);
        remaining--;
        return mappingFunction.apply(current);
    }

    @Override
    public void remove() {
        if (removeFunction == null) {
            throw new UnsupportedOperationException();
        }
        if (current == null) {
            throw new IllegalStateException();
        }
        removeFunction.accept(current);
        current = null;
    }

    public static boolean isSupported(int size, int first, int last) {
        long extent = (long) last - first;
        return extent <= Integer.MAX_VALUE / 2
                && extent <= size * 4L;
    }
}
