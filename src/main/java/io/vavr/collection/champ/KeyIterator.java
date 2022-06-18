/*
 * @(#)BaseTrieIterator.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;


import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * Entry iterator over a CHAMP trie.
 * <p>
 * Uses a fixed stack in depth.
 * Iterates first over inlined data entries and then continues depth first.
 * <p>
 * Supports remove and {@link Map.Entry#setValue}. The functions that are
 * passed to this iterator must not change the trie structure that the iterator
 * currently uses.
 */
public class KeyIterator<K> implements Iterator<K>, io.vavr.collection.Iterator<K> {

    private final int[] nodeCursorsAndLengths = new int[Node.MAX_DEPTH * 2];
    int nextValueCursor;
    private int nextValueLength;
    private int nextStackLevel = -1;
    Node<K> nextValueNode;
    K current;
    private boolean canRemove = false;
    private final Consumer<K> removeFunction;
    @SuppressWarnings({"unchecked", "rawtypes"})
    Node<K>[] nodes = new Node[Node.MAX_DEPTH];

    /**
     * Creates a new instance.
     *
     * @param root the root node of the trie
     */
    public KeyIterator(Node<K> root) {
        this(root, null);
    }

    /**
     * Creates a new instance.
     *
     * @param root           the root node of the trie
     * @param removeFunction a function that removes an entry from a field;
     *                       the function must not change the trie that was passed
     *                       to this iterator
     */
    public KeyIterator(Node<K> root, Consumer<K> removeFunction) {
        this.removeFunction = removeFunction;
        if (root.hasNodes()) {
            nextStackLevel = 0;
            nodes[0] = root;
            nodeCursorsAndLengths[0] = 0;
            nodeCursorsAndLengths[1] = root.nodeArity();
        }
        if (root.hasData()) {
            nextValueNode = root;
            nextValueCursor = 0;
            nextValueLength = root.dataArity();
        }
    }

    @Override
    public boolean hasNext() {
        if (nextValueCursor < nextValueLength) {
            return true;
        } else {
            return searchNextValueNode();
        }
    }

    @Override
    public K next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        } else {
            canRemove = true;
            current = nextValueNode.getKey(nextValueCursor++);
            return current;
        }
    }

    /*
     * Searches for the next node that contains values.
     */
    private boolean searchNextValueNode() {
        while (nextStackLevel >= 0) {
            final int currentCursorIndex = nextStackLevel * 2;
            final int currentLengthIndex = currentCursorIndex + 1;
            final int nodeCursor = nodeCursorsAndLengths[currentCursorIndex];
            final int nodeLength = nodeCursorsAndLengths[currentLengthIndex];
            if (nodeCursor < nodeLength) {
                final Node<K> nextNode = nodes[nextStackLevel].getNode(nodeCursor);
                nodeCursorsAndLengths[currentCursorIndex]++;
                if (nextNode.hasNodes()) {
                    // put node on next stack level for depth-first traversal
                    final int nextStackLevel = ++this.nextStackLevel;
                    final int nextCursorIndex = nextStackLevel * 2;
                    final int nextLengthIndex = nextCursorIndex + 1;
                    nodes[nextStackLevel] = nextNode;
                    nodeCursorsAndLengths[nextCursorIndex] = 0;
                    nodeCursorsAndLengths[nextLengthIndex] = nextNode.nodeArity();
                }

                if (nextNode.hasData()) {
                    //found next node that contains values
                    nextValueNode = nextNode;
                    nextValueCursor = 0;
                    nextValueLength = nextNode.dataArity();
                    return true;
                }
            } else {
                nextStackLevel--;
            }
        }
        return false;
    }

    @Override
    public void remove() {
        if (!canRemove) {
            throw new IllegalStateException();
        }
        if (removeFunction == null) {
            throw new UnsupportedOperationException("remove");
        }
        K toRemove = current;
        removeFunction.accept(toRemove);
        canRemove = false;
        current = null;
    }
}
