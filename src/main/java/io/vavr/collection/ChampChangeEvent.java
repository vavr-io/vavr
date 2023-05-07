/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.vavr.collection;

import java.util.Objects;

/**
 * This class is used to report a change (or no changes) of data in a CHAMP trie.
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
 *
 * @param <D> the data type
 */
class ChampChangeEvent<D> {
    private  Type type = Type.UNCHANGED;
    private  D oldData;
    private  D newData;

    public ChampChangeEvent() {
    }

    public boolean isUnchanged() {
        return type == Type.UNCHANGED;
    }

    public boolean isAdded() {
        return type==Type.ADDED;
    }

    /**
     * Call this method to indicate that a data element has been added.
     */
    void setAdded( D newData) {
        this.newData = newData;
        this.type = Type.ADDED;
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

    void reset() {
        type = Type.UNCHANGED;
        oldData = null;
        newData = null;
    }

    enum Type {
        UNCHANGED,
        ADDED,
        REMOVED,
        REPLACED
    }
}
