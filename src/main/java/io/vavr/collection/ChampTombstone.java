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
 * A tombstone is used by {@code VectorSet} to mark a deleted slot in its Vector.
 * <p>
 * A tombstone stores the minimal number of neighbors 'before' and 'after' it in the
 * Vector.
 * <p>
 * When we insert a new tombstone, we update 'before' and 'after' values only on
 * the first and last tombstone of a sequence of tombstones. Therefore, a delete
 * operation requires reading of up to 3 neighboring elements in the vector, and
 * updates of up to 3 elements.
 * <p>
 * There are no tombstones at the first and last element of the vector. When we
 * remove the first or last element of the vector, we remove the tombstones.
 * <p>
 * Example: Tombstones are shown as <i>before</i>.<i>after</i>.
 * <pre>
 *
 *
 *                              Indices:  0   1   2   3   4   5   6   7   8   9
 * Initial situation:           Values:  'a' 'b' 'c' 'd' 'e' 'f' 'g' 'h' 'i' 'j'
 *
 * Deletion of element 5:
 * - read elements at indices 4, 5, 6                    'e' 'f' 'g'
 * - notice that none of them are tombstones
 * - put tombstone 0.0 at index 5                            0.0
 *
 * After deletion of element 5:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 'h' 'i' 'j'
 *
 * After deletion of element 7:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 0.0 'i' 'j'
 *
 * Deletion of element 8:
 * - read elements at indices 7, 8, 9                                0.0 'i' 'j'
 * - notice that 7 is a tombstone 0.0
 * - put tombstones 0.1, 1.0 at indices 7 and 8
 *
 * After deletion of element 8:          'a' 'b' 'c' 'd' 'e' 0.0 'g' 0.1 1.0 'j'
 *
 * Deletion of element 6:
 * - read elements at indices 5, 6, 7                        0.0 'g' 0.1
 * - notice that two of them are tombstones
 * - put tombstones 0.3, 0.0, 3.0 at indices 5, 6 and 8
 *
 * After deletion of element 6:          'a' 'b' 'c' 'd' 'e' 0.3 0.0 0.1 3.0 'j'
 *
 * Deletion of the last element 9:
 * - read elements at index 8                                            3.0
 * - notice that it is a tombstone
 * - remove the last element and the neighboring tombstone sequence
 *
 * After deletion of element 9:          'a' 'b' 'c' 'd' 'e'
 * </pre>
 * References:
 * <p>
 * The code in this class has been derived from JHotDraw 8.
 * <p>
 * The design of this class is inspired by 'VectorMap.scala'.
 * <dl>
 *     <dt>JHotDraw 8. Copyright © 2023 The authors and contributors of JHotDraw.
 *     <a href="https://github.com/wrandelshofer/jhotdraw8/blob/8c1a98b70bc23a0c63f1886334d5b568ada36944/LICENSE">MIT License</a>.</dt>
 *     <dd><a href="https://github.com/wrandelshofer/jhotdraw8">github.com</a>
 *     </dd>
 *     <dt>VectorMap.scala
 *     <br>The Scala library. Copyright EPFL and Lightbend, Inc. Apache License 2.0.</dt>
 *     <dd><a href="https://github.com/scala/scala/blob/28eef15f3cc46f6d3dd1884e94329d7601dc20ee/src/library/scala/collection/immutable/VectorMap.scala">github.com</a>
 *     </dd>
 * </dl>
 */
final class ChampTombstone {
    private final int before;
    private final int after;

    /**
     * @param before minimal number of neighboring tombstones before this one
     * @param after  minimal number of neighboring tombstones after this one
     */
    ChampTombstone(int before, int after) {
        this.before = before;
        this.after = after;
    }

    public int before() {
        return before;
    }

    public int after() {
        return after;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        ChampTombstone that = (ChampTombstone) obj;
        return this.before == that.before &&
                this.after == that.after;
    }

    @Override
    public int hashCode() {
        return Objects.hash(before, after);
    }

    @Override
    public String toString() {
        return "ChampTombstone[" +
                "before=" + before + ", " +
                "after=" + after + ']';
    }


}
