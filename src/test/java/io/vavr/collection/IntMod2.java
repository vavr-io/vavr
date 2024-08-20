/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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

/**
 * An Int wrapper that implements equality by comparing int values modulo 2.
 * <br>
 * Examples:
 * <ul>
 * <li>IntMod2(0) equals IntMod2(2) equals IntMod2(4) ...</li>
 * <li>IntMod2(1) equals IntMod2(3) equals IntMod2(5) ...</li>
 * <li>IntMod2(0) &lt; IntMod2(1)</li>
 * <li>IntMod2(_even_int_) &lt; IntMod2(_odd_int_)</li>
 * </ul>
 */
final class IntMod2 implements Comparable<IntMod2> {

    private final int val;

    IntMod2(int val) {
        this.val = val;
    }

    @Override
    public int compareTo(IntMod2 that) {
        return this.hashCode() - that.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        return that == this || (that instanceof IntMod2 && this.hashCode() == that.hashCode());
    }

    @Override
    public int hashCode() {
        return val % 2;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }

}
