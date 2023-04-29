/*
 * @(#)ChangeEvent.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

import java.util.Objects;

/**
 * This class is used to report a change (or no changes) of data in a CHAMP trie.
 *
 * @param <D> the data type
 */
 class ChampChangeEvent<D> {
    enum Type {
        UNCHANGED,
        ADDED,
        REMOVED,
        REPLACED
    }

    private  Type type = Type.UNCHANGED;
    private  D oldData;
    private  D newData;

    public ChampChangeEvent() {
    }

    void reset(){
        type=Type.UNCHANGED;
        oldData=null;
        newData=null;
    }
    void found(D data) {
        this.oldData = data;
    }

    public  D getOldData() {
        return oldData;
    }

    public  D getNewData() {
        return newData;
    }

    public  D getOldDataNonNull() {
        return Objects.requireNonNull(oldData);
    }

    public  D getNewDataNonNull() {
        return Objects.requireNonNull(newData);
    }

    /**
     * Call this method to indicate that the value of an element has changed.
     *
     * @param oldData the old value of the element
     * @param newData the new value of the element
     */
    void setReplaced( D oldData,  D newData) {
        this.oldData = oldData;
        this.newData = newData;
        this.type = Type.REPLACED;
    }

    /**
     * Call this method to indicate that an element has been removed.
     *
     * @param oldData the value of the removed element
     */
    void setRemoved( D oldData) {
        this.oldData = oldData;
        this.type = Type.REMOVED;
    }

    /**
     * Call this method to indicate that a data element has been added.
     */
    void setAdded( D newData) {
        this.newData = newData;
        this.type = Type.ADDED;
    }

    /**
     * Returns true if the CHAMP trie has been modified.
     */
    public boolean isModified() {
        return type != Type.UNCHANGED;
    }

    /**
     * Returns true if the data element has been replaced.
     */
    public boolean isReplaced() {
        return type == Type.REPLACED;
    }
}
