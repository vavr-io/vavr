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
class KeyEnumeratorSpliterator<K, E> extends AbstractKeyEnumeratorSpliterator<K, E> {
    public KeyEnumeratorSpliterator(@NonNull Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
        super(root, mappingFunction, characteristics, size);
    }

    @Override
    boolean isReverse() {
        return false;
    }

    @Override
    int getNextBitpos(StackElement<K> elem) {
        return 1 << Integer.numberOfTrailingZeros(elem.map);
    }

    @Override
    boolean isDone(@NonNull StackElement<K> elem) {
        return elem.index >= elem.size;
    }

    @Override
    int moveIndex(@NonNull StackElement<K> elem) {
        return elem.index++;
    }

}
