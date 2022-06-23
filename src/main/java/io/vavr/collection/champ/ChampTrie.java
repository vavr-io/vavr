/*
 * @(#)ChampTrie.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


/**
 * Provides static utility methods for CHAMP tries.
 */
class ChampTrie {

    /**
     * Don't let anyone instantiate this class.
     */
    private ChampTrie() {
    }

    static <K> BitmapIndexedNode<K> newBitmapIndexedNode(
            UniqueId mutator, final int nodeMap,
            final int dataMap, final Object[] nodes) {
        return mutator == null
                ? new BitmapIndexedNode<>(nodeMap, dataMap, nodes)
                : new MutableBitmapIndexedNode<>(mutator, nodeMap, dataMap, nodes);
    }

    static <K> HashCollisionNode<K> newHashCollisionNode(
            UniqueId mutator, int hash, Object[] entries) {
        return mutator == null
                ? new HashCollisionNode<>(hash, entries)
                : new MutableHashCollisionNode<>(mutator, hash, entries);
    }
}