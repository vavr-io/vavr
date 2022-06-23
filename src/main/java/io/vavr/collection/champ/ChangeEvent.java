/*
 * @(#)ChangeEvent.java
 * Copyright © 2022 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection.champ;

 class ChangeEvent<V> {

     public boolean modified;
     private V oldValue;
     public boolean updated;
     public int numInBothCollections;

     public ChangeEvent() {
     }

     void found(V oldValue) {
        this.oldValue = oldValue;
    }

    public V getOldValue() {
        return oldValue;
    }

    public boolean isUpdated() {
        return updated;
    }

    /**
     * Returns true if a value has been inserted, replaced or removed.
     */
    public boolean isModified() {
        return modified;
    }

    void setValueUpdated(V oldValue) {
        this.oldValue = oldValue;
        this.updated = true;
        this.modified = true;
    }

    void setValueRemoved(V oldValue) {
        this.oldValue = oldValue;
        this.modified = true;
    }

    void setValueAdded() {
        this.modified = true;
    }
}
