/*
 * @(#)ChangeEvent.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

/**
 * This class is used to report a change (or no changes) of data in a CHAMP trie.
 *
 * @param <D> the data type
 */
class ChangeEvent<D> {
    enum Type {
        UNCHANGED,
        ADDED,
        REMOVED,
        REPLACED
    }

    private Type type = Type.UNCHANGED;
    private D data;

    public ChangeEvent() {
    }

    void found(D data) {
        this.data = data;
    }

    public D getData() {
        return data;
    }

    /**
     * Call this method to indicate that a data object has been
     * replaced.
     *
     * @param oldData the replaced data object
     */
    void setReplaced(D oldData) {
        this.data = oldData;
        this.type = Type.REPLACED;
    }

    /**
     * Call this method to indicate that a data object has been removed.
     *
     * @param oldData the removed data object
     */
    void setRemoved(D oldData) {
        this.data = oldData;
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
        return type == Type.REPLACED;
    }
}
