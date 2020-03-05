/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2020 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
