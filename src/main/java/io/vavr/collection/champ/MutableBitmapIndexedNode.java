package io.vavr.collection.champ;


public class MutableBitmapIndexedNode<K> extends BitmapIndexedNode<K> {
    private final static long serialVersionUID = 0L;
    private final IdentityObject mutator;

    MutableBitmapIndexedNode(IdentityObject mutator, int nodeMap, int dataMap, Object[] nodes) {
        super(nodeMap, dataMap, nodes);
        this.mutator = mutator;
    }

    @Override
    protected IdentityObject getMutator() {
        return mutator;
    }
}
