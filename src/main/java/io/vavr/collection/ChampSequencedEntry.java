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



import java.io.Serial;
import java.util.AbstractMap;
import java.util.Objects;

/**
 * A {@code ChampSequencedEntry} stores an entry of a map and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the key and the value
 * of the entry - the sequence number is not included.
 * <p>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a></dd>
 * </dl>
 */
 class ChampSequencedEntry<K, V> extends AbstractMap.SimpleImmutableEntry<K, V>
        implements ChampSequencedData {
    @Serial
    private static final long serialVersionUID = 0L;
    private final int sequenceNumber;

     ChampSequencedEntry(K key) {
        super(key, null);
        sequenceNumber = NO_SEQUENCE_NUMBER;
    }

     ChampSequencedEntry(K key, V value) {
        super(key, value);
        sequenceNumber = NO_SEQUENCE_NUMBER;
    }
     ChampSequencedEntry(K key, V value, int sequenceNumber) {
        super(key, value);
        this.sequenceNumber = sequenceNumber;
    }

     static <K, V> ChampSequencedEntry<K, V> forceUpdate( ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return newK;
    }
     static <K, V> boolean keyEquals(ChampSequencedEntry<K, V> a, ChampSequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey());
    }

     static <K, V> boolean keyAndValueEquals(ChampSequencedEntry<K, V> a,  ChampSequencedEntry<K, V> b) {
        return Objects.equals(a.getKey(), b.getKey()) && Objects.equals(a.getValue(), b.getValue());
    }

     static <V, K> int keyHash( ChampSequencedEntry<K, V> a) {
        return Objects.hashCode(a.getKey());
    }

    
     static <K, V> ChampSequencedEntry<K, V> update(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue()) ? oldK :
                new ChampSequencedEntry<>(oldK.getKey(), newK.getValue(), oldK.getSequenceNumber());
    }

    
     static <K, V> ChampSequencedEntry<K, V> updateAndMoveToFirst(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    
     static <K, V> ChampSequencedEntry<K, V> updateAndMoveToLast(ChampSequencedEntry<K, V> oldK, ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }

    // FIXME This behavior is enforced by AbstractMapTest.shouldPutExistingKeyAndNonEqualValue().<br>
    //     This behavior replaces the existing key with the new one if it has not the same identity.<br>
    //     This behavior does not match the behavior of java.util.HashMap.put().
    //     This behavior violates the contract of the map: we do create a new instance of the map,
    //     although it is equal to the previous instance.
     static <K, V> ChampSequencedEntry<K, V> updateWithNewKey( ChampSequencedEntry<K, V> oldK,  ChampSequencedEntry<K, V> newK) {
        return Objects.equals(oldK.getValue(), newK.getValue())
                && oldK.getKey() == newK.getKey()
                ? oldK
                : new ChampSequencedEntry<>(newK.getKey(), newK.getValue(), oldK.getSequenceNumber());
    }
    public int getSequenceNumber() {
        return sequenceNumber;
    }
}
