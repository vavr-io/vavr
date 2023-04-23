package io.vavr.collection.champ;


import java.util.Objects;

/**
 * This class is used to report a change (or no changes) of data in a CHAMP trie.
 *
 * @param <D> the data type
 */
public class ChangeEvent<D> {
    enum Type {
        UNCHANGED,
        ADDED,
        REMOVED,
        REPLACED
    }

    private Type type = Type.UNCHANGED;
    private D oldData;
    private D newData;

    public ChangeEvent() {
    }

    void found(D data) {
        this.oldData = data;
    }

    public D getOldData() {
        return oldData;
    }

    public @Nullable D getNewData() {
        return newData;
    }

    public @NonNull D getOldDataNonNull() {
        return Objects.requireNonNull(oldData);
    }

    public @NonNull D getNewDataNonNull() {
        return Objects.requireNonNull(newData);
    }

    /**
     * Call this method to indicate that a data object has been
     * replaced.
     *
     * @param oldData the data object that was removed
     * @param newData the data object that was added
     */
    void setReplaced(D oldData, D newData) {
        this.oldData = oldData;
        this.newData = newData;
        this.type = Type.REPLACED;
    }

    /**
     * Call this method to indicate that a data object has been removed.
     *
     * @param oldData the removed data object
     */
    void setRemoved(D oldData) {
        this.oldData = oldData;
        this.type = Type.REMOVED;
    }

    /**
     * Call this method to indicate that an element has been added.
     */
    void setAdded(D newData) {
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
     * Returns true if the value of an element has been replaced.
     */
    public boolean isReplaced() {
        return type == Type.REPLACED;
    }
}
