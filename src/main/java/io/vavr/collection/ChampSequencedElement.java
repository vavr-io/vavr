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
 * A {@code SequencedElement} stores an element of a set and a sequence number.
 * <p>
 * {@code hashCode} and {@code equals} are based on the element - the sequence
 * number is not included.
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
 class ChampSequencedElement<E> implements ChampSequencedData {

    private final  E element;
    private final int sequenceNumber;

     ChampSequencedElement(E element) {
        this.element = element;
        this.sequenceNumber = NO_SEQUENCE_NUMBER;
    }

     ChampSequencedElement(E element, int sequenceNumber) {
        this.element = element;
        this.sequenceNumber = sequenceNumber;
    }
    public static int keyHash( Object a) {
        return Objects.hashCode(a);
    }


     static <E> ChampSequencedElement<E> forceUpdate( ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return newK;
    }

     static <E> ChampSequencedElement<E> update(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return oldK;
    }

    
     static <E> ChampSequencedElement<E> updateAndMoveToFirst(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return oldK.getSequenceNumber() == newK.getSequenceNumber() + 1 ? oldK : newK;
    }

    
     static <E> ChampSequencedElement<E> updateAndMoveToLast(ChampSequencedElement<E> oldK, ChampSequencedElement<E> newK) {
        return oldK.getSequenceNumber() == newK.getSequenceNumber() - 1 ? oldK : newK;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChampSequencedElement<?> that = (ChampSequencedElement<?>) o;
        return Objects.equals(element, that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(element);
    }

      E getElement() {
        return element;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        return "{" +
                "" + element +
                ", seq=" + sequenceNumber +
                '}';
    }
}
