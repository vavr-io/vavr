package io.vavr.collection.champ;

import java.util.function.Function;

/**
 * Key iterator over a CHAMP trie.
 * <p>
 * Uses a stack with a fixed maximal depth.
 * Iterates over keys in preorder sequence.
 * <p>
 * Supports the {@code remove} operation. The remove function must
 * create a new version of the trie, so that iterator does not have
 * to deal with structural changes of the trie.
 */
class ReversedKeyEnumeratorSpliterator<K, E> extends AbstractKeyEnumeratorSpliterator<K, E> {
    public ReversedKeyEnumeratorSpliterator(@NonNull Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
        super(root, mappingFunction, characteristics, size);
    }

    @Override
    boolean isReverse() {
        return true;
    }

    @Override
    boolean isDone(AbstractKeyEnumeratorSpliterator.@NonNull StackElement<K> elem) {
        return elem.index < 0;
    }

    @Override
    int moveIndex(@NonNull StackElement<K> elem) {
        return elem.index--;
    }

    @Override
    int getNextBitpos(StackElement<K> elem) {
        return 1 << (31 - Integer.numberOfLeadingZeros(elem.map));
    }

}
