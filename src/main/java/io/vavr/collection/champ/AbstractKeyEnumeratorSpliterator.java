package io.vavr.collection.champ;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
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
abstract class AbstractKeyEnumeratorSpliterator<K, E> implements EnumeratorSpliterator<E> {
    private final long size;

    @Override
    public Spliterator<E> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return size;
    }

    @Override
    public int characteristics() {
        return characteristics;
    }

    static class StackElement<K> {
        final @NonNull Node<K> node;
        int index;
        final int size;
        int map;

        public StackElement(@NonNull Node<K> node, boolean reverse) {
            this.node = node;
            this.size = node.nodeArity() + node.dataArity();
            this.index = reverse ? size - 1 : 0;
            this.map = (node instanceof BitmapIndexedNode<K> bin)
                    ? (bin.dataMap() | bin.nodeMap()) : 0;
        }
    }

    private final @NonNull Deque<StackElement<K>> stack = new ArrayDeque<>(Node.MAX_DEPTH);
    private K current;
    private final int characteristics;
    private final @NonNull Function<K, E> mappingFunction;

    public AbstractKeyEnumeratorSpliterator(@NonNull Node<K> root, @NonNull Function<K, E> mappingFunction, int characteristics, long size) {
        if (root.nodeArity() + root.dataArity() > 0) {
            stack.push(new StackElement<>(root, isReverse()));
        }
        this.characteristics = characteristics;
        this.mappingFunction = mappingFunction;
        this.size = size;
    }

    abstract boolean isReverse();


    @Override
    public boolean moveNext() {
        while (!stack.isEmpty()) {
            StackElement<K> elem = stack.peek();
            Node<K> node = elem.node;

            if (node instanceof HashCollisionNode<K> hcn) {
                current = hcn.getData(moveIndex(elem));
                if (isDone(elem)) {
                    stack.pop();
                }
                return true;
            } else if (node instanceof BitmapIndexedNode<K> bin) {
                int bitpos = getNextBitpos(elem);
                elem.map ^= bitpos;
                moveIndex(elem);
                if (isDone(elem)) {
                    stack.pop();
                }
                if ((bin.nodeMap() & bitpos) != 0) {
                    stack.push(new StackElement<>(bin.nodeAt(bitpos), isReverse()));
                } else {
                    current = bin.dataAt(bitpos);
                    return true;
                }
            }
        }
        return false;
    }

    abstract int getNextBitpos(StackElement<K> elem);

    abstract int moveIndex(@NonNull StackElement<K> elem);

    abstract boolean isDone(@NonNull StackElement<K> elem);

    @Override
    public E current() {
        return mappingFunction.apply(current);
    }
}
