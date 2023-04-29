/*
 * @(#)MutableBitmapIndexedNode.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

class ChampMutableBitmapIndexedNode<K> extends ChampBitmapIndexedNode<K> {
    private static final long serialVersionUID = 0L;
    private final ChampIdentityObject mutator;

    ChampMutableBitmapIndexedNode(ChampIdentityObject mutator, int nodeMap, int dataMap, Object  [] nodes) {
        super(nodeMap, dataMap, nodes);
        this.mutator = mutator;
    }

    @Override
    protected ChampIdentityObject getMutator() {
        return mutator;
    }
}
