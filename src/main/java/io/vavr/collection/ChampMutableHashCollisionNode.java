/*
 * @(#)MutableHashCollisionNode.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

class ChampMutableHashCollisionNode<K> extends ChampHashCollisionNode<K> {
    private static final long serialVersionUID = 0L;
    private final ChampIdentityObject mutator;

    ChampMutableHashCollisionNode(ChampIdentityObject mutator, int hash, Object  [] entries) {
        super(hash, entries);
        this.mutator = mutator;
    }

    @Override
    protected ChampIdentityObject getMutator() {
        return mutator;
    }
}
