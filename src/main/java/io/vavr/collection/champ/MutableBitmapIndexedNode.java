/*
 * @(#)MutableBitmapIndexedNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


final class MutableBitmapIndexedNode<K> extends BitmapIndexedNode<K> {
    private final static long serialVersionUID = 0L;
    private final UniqueId mutator;

    MutableBitmapIndexedNode(UniqueId mutator, int nodeMap, int dataMap, Object[] nodes) {
        super(nodeMap, dataMap, nodes);
        this.mutator = mutator;
    }

    @Override
    protected UniqueId getMutator() {
        return mutator;
    }
}
