/*
 * @(#)ChangeEvent.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

/**
 * This class is used to report a change (or no changes) in a CHAMP trie.
 *
 * @param <V> the value type of elements stored in the CHAMP trie. Only
 *            elements that are 'map entries' do have a value type. If a CHAMP
 *            trie is used to store 'elements' of a set, then the value
 *            type should be the {@link Void} type.
 */
class ChangeEvent<V> {
    enum Type {
        UNCHANGED,
        ADDED,
        REMOVED,
        UPDATED
    }

    private Type type = Type.UNCHANGED;
    private V oldValue;

    public ChangeEvent() {
    }

    void found(V oldValue) {
        this.oldValue = oldValue;
    }

    public V getOldValue() {
        return oldValue;
    }

    /**
     * Call this method to indicate that the value of an element has changed.
     *
     * @param oldValue the old value of the element
     */
    void setUpdated(V oldValue) {
        this.oldValue = oldValue;
        this.type = Type.UPDATED;
    }

    /**
     * Call this method to indicate that an element has been removed.
     *
     * @param oldValue the value of the removed element
     */
    void setRemoved(V oldValue) {
        this.oldValue = oldValue;
        this.type = Type.REMOVED;
    }

    /**
     * Call this method to indicate that an element has been added.
     */
    void setAdded() {
        this.type = Type.ADDED;
    }

    /**
     * Returns true if the CHAMP trie has been modified.
     */
    boolean isModified() {
        return type != Type.UNCHANGED;
    }

    /**
     * Returns true if the value of an element has been updated.
     */
    boolean isUpdated() {
        return type == Type.UPDATED;
    }
}
