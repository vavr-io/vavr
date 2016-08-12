/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorPropertyTest {
    @Before
    public void setUp() { Vector.BRANCHING_BASE = 2; }

    @Test
    public void shouldCreateAndGet() {
        for (int i = 0; i < 2000; i++) {
            final Seq<Integer> expected = Array.range(0, i);
            final Vector<Integer> actual = Vector.ofAll(expected);
            assertAreEqual(expected, actual);

            final Vector<Integer> actualInt = Vector.ofAll((int[]) Arrays2.toPrimitiveArray(int.class, expected.toJavaArray()));
            assert actualInt.leading instanceof int[];
            assertAreEqual(expected, actual);
        }
    }

    private static void assertAreEqual(Seq<Integer> expected, Seq<Integer> actual) {
        final List<Integer> actualList = actual.toJavaList();
        final List<Integer> expectedList = expected.toJavaList();
        assertThat(actualList).isEqualTo(expectedList); // a lot faster than `hasSameElementsAs`
    }

    private static int getMaxSizeForDepth(int depth) {
        final int max = Vector.branchingFactor() + (int) Math.pow(Vector.branchingFactor(), depth) + Vector.branchingFactor();
        return Math.min(max, 10_000);
    }
}
