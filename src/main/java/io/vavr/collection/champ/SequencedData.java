package io.vavr.collection.champ;

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
     * {@code first} or {@code last} are at risk of overflowing, or the
     * extent from {@code first - last} is not densely filled enough for an
     * efficient bucket sort.
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
        long extent = (long) last - first;
        return size == 0 && (first != -1 || last != 0)
                || last > Integer.MAX_VALUE - 2
                || first < Integer.MIN_VALUE + 2
                || extent > 16 && extent > size * 4L;
    }
}
