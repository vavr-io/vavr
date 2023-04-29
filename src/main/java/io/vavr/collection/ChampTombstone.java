/*
 * @(#)Tombstone.java
 * Copyright © 2023 The authors and contributors of JHotDraw. MIT License.
 */

package io.vavr.collection;

/**
 * A tombstone is used by {@link VectorSet} to mark a deleted slot in its Vector.
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
 *
 * @param before minimal number of neighboring tombstones before this one
 * @param after  minimal number of neighboring tombstones after this one
 */
record ChampTombstone(int before, int after) {

}
