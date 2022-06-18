/*
 * @(#)MutableHashCollisionNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


final class MutableHashCollisionNode<K> extends HashCollisionNode<K> {
    private final static long serialVersionUID = 0L;
    private final UniqueId mutator;

    MutableHashCollisionNode(UniqueId mutator, int hash, Object[] entries) {
        super(hash, entries);
        this.mutator = mutator;
    }

    @Override
    protected UniqueId getMutator() {
        return mutator;
    }
}
