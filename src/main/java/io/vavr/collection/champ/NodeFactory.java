package io.vavr.collection.champ;


/**
 * Provides factory methods for {@link Node}s.
 */
public class NodeFactory {

    /**
     * Don't let anyone instantiate this class.
     */
    private NodeFactory() {
    }

    static <K> @NonNull BitmapIndexedNode<K> newBitmapIndexedNode(
            @Nullable IdentityObject mutator, int nodeMap,
            int dataMap, @NonNull Object[] nodes) {
        return mutator == null
                ? new BitmapIndexedNode<>(nodeMap, dataMap, nodes)
                : new MutableBitmapIndexedNode<>(mutator, nodeMap, dataMap, nodes);
    }

    static <K> @NonNull HashCollisionNode<K> newHashCollisionNode(
            @Nullable IdentityObject mutator, int hash, @NonNull Object @NonNull [] entries) {
        return mutator == null
                ? new HashCollisionNode<>(hash, entries)
                : new MutableHashCollisionNode<>(mutator, hash, entries);
    }
}
