/*
 * @(#)KeySpliterator.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

import java.util.function.Consumer;
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
 class ChampSpliterator<K, E> extends ChampAbstractChampSpliterator<K, E> {
    public ChampSpliterator(ChampNode<K> root, Function<K, E> mappingFunction, int characteristics, long size) {
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
    boolean isDone( StackElement<K> elem) {
        return elem.index >= elem.size;
    }

    @Override
    int moveIndex( StackElement<K> elem) {
        return elem.index++;
    }
}
