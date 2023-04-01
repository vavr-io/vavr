/*
 * @(#)MutableHashCollisionNode.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


 class MutableHashCollisionNode<K> extends HashCollisionNode<K> {
     private final static long serialVersionUID = 0L;
     private final IdentityObject mutator;

     MutableHashCollisionNode(IdentityObject mutator, int hash, Object[] entries) {
         super(hash, entries);
         this.mutator = mutator;
     }

     @Override
     protected IdentityObject getMutator() {
         return mutator;
     }
}
