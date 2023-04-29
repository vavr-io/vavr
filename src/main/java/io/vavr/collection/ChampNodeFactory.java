/*
 * @(#)NodeFactory.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

/**
 * Provides factory methods for {@link ChampNode}s.
 */
class ChampNodeFactory {

    /**
     * Don't let anyone instantiate this class.
     */
    private ChampNodeFactory() {
    }

    static <K> ChampBitmapIndexedNode<K> newBitmapIndexedNode(
            ChampIdentityObject mutator, int nodeMap,
            int dataMap, Object[] nodes) {
        return mutator == null
                ? new ChampBitmapIndexedNode<>(nodeMap, dataMap, nodes)
                : new ChampMutableBitmapIndexedNode<>(mutator, nodeMap, dataMap, nodes);
    }

    static <K> ChampHashCollisionNode<K> newHashCollisionNode(
            ChampIdentityObject mutator, int hash, Object  [] entries) {
        return mutator == null
                ? new ChampHashCollisionNode<>(hash, entries)
                : new ChampMutableHashCollisionNode<>(mutator, hash, entries);
    }
}