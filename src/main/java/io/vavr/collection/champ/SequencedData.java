/*
 * @(#)Sequenced.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import static io.vavr.collection.champ.BitmapIndexedNode.emptyNode;


/**
 * A {@code SequencedData} stores a sequence number plus some data.
 * <p>
 * {@code SequencedData} objects are used to store sequenced data in a CHAMP
 * trie (see {@link Node}).
 * <p>
 * The kind of data is specified in concrete implementations of this
 * interface.
 * <p>
 * All sequence numbers of {@code SequencedData} objects in the same CHAMP trie
 * are unique. Sequence numbers range from {@link Integer#MIN_VALUE} (exclusive)
 * to {@link Integer#MAX_VALUE} (inclusive).
 */
interface SequencedData {
    /**
     * We use {@link Integer#MIN_VALUE} to detect overflows in the sequence number.
     * <p>
     * {@link Integer#MIN_VALUE} is the only integer number which can not
     * be negated.
     * <p>
     * Therefore, we can not use {@link Integer#MIN_VALUE} as a sequence number
     * anyway.
     */
    int NO_SEQUENCE_NUMBER = Integer.MIN_VALUE;

    /**
     * Gets the sequence number of the data.
     *
     * @return sequence number in the range from {@link Integer#MIN_VALUE}
     * (exclusive) to {@link Integer#MAX_VALUE} (inclusive).
     */
    int getSequenceNumber();

    /**
     * Returns true if the sequenced elements must be renumbered because
     * {@code first} or {@code last} are at risk of overflowing.
     * <p>
     * {@code first} and {@code last} are estimates of the first and last
     * sequence numbers in the trie. The estimated extent may be larger
     * than the actual extent, but not smaller.
     *
     * @param size  the size of the trie
     * @param first the estimated first sequence number
     * @param last  the estimated last sequence number
     * @return
     */
    static boolean mustRenumber(int size, int first, int last) {
        return size == 0 && (first != -1 || last != 0)
                || last > Integer.MAX_VALUE - 2
                || first < Integer.MIN_VALUE + 2;
    }

    /**
     * Renumbers the sequence numbers in all nodes from {@code 0} to {@code size}.
     * <p>
     * Afterwards the sequence number for the next inserted entry must be
     * set to the value {@code size};
     *
     * @param size            the size of the trie
     * @param root            the root of the trie
     * @param sequenceRoot    the sequence root of the trie
     * @param mutator         the mutator that will own the renumbered trie
     * @param hashFunction    the hash function for data elements
     * @param equalsFunction  the equals function for data elements
     * @param factoryFunction the factory function for data elements
     * @param <K>
     * @return a new renumbered root
     */
    static <K extends SequencedData> BitmapIndexedNode<K> renumber(int size,
                                                                   @NonNull BitmapIndexedNode<K> root,
                                                                   @NonNull BitmapIndexedNode<K> sequenceRoot,
                                                                   @NonNull IdentityObject mutator,
                                                                   @NonNull ToIntFunction<K> hashFunction,
                                                                   @NonNull BiPredicate<K, K> equalsFunction,
                                                                   @NonNull BiFunction<K, Integer, K> factoryFunction

    ) {
        if (size == 0) {
            return root;
        }
        BitmapIndexedNode<K> newRoot = root;
        ChangeEvent<K> details = new ChangeEvent<>();
        int seq = 0;

        for (var i = new KeySpliterator<>(sequenceRoot, Function.identity(), 0, 0); i.moveNext(); ) {
            K e = i.current();
            K newElement = factoryFunction.apply(e, seq);
            newRoot = newRoot.update(mutator,
                    newElement,
                    Objects.hashCode(e), 0, details,
                    (oldk, newk) -> oldk.getSequenceNumber() == newk.getSequenceNumber() ? oldk : newk,
                    equalsFunction, hashFunction);
            seq++;
        }
        return newRoot;
    }

    static <K extends SequencedData> BitmapIndexedNode<K> buildSequenceRoot(@NonNull BitmapIndexedNode<K> root, @NonNull IdentityObject mutator) {
        BitmapIndexedNode<K> seqRoot = emptyNode();
        ChangeEvent<K> details = new ChangeEvent<>();
        for (KeyIterator<K> i = new KeyIterator<>(root, null); i.hasNext(); ) {
            K elem = i.next();
            seqRoot = seqRoot.update(mutator, elem, SequencedData.seqHash(elem.getSequenceNumber()),
                    0, details, (oldK, newK) -> oldK, SequencedData::seqEquals, SequencedData::seqHash);
        }
        return seqRoot;
    }

    static <K extends SequencedData> boolean seqEquals(@NonNull K a, @NonNull K b) {
        return a.getSequenceNumber() == b.getSequenceNumber();
    }

    static <K extends SequencedData> int seqHash(K e) {
        return SequencedData.seqHash(e.getSequenceNumber());
    }


    /**
     * Computes a hash code from the sequence number, so that we can
     * use it for iteration in a CHAMP trie.
     * <p>
     * Convert the sequence number to unsigned 32 by adding Integer.MIN_VALUE.
     * Then reorders its bits from 66666555554444433333222221111100 to
     * 00111112222233333444445555566666.
     *
     * @param sequenceNumber a sequence number
     * @return a hash code
     */
    static int seqHash(int sequenceNumber) {
        int u = sequenceNumber + Integer.MIN_VALUE;
        return (u >>> 27)
                | ((u & 0b00000_11111_00000_00000_00000_00000_00) >>> 17)
                | ((u & 0b00000_00000_11111_00000_00000_00000_00) >>> 7)
                | ((u & 0b00000_00000_00000_11111_00000_00000_00) << 3)
                | ((u & 0b00000_00000_00000_00000_11111_00000_00) << 13)
                | ((u & 0b00000_00000_00000_00000_00000_11111_00) << 23)
                | ((u & 0b00000_00000_00000_00000_00000_00000_11) << 30);
    }

}
