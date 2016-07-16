/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function2;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorPropertyTest {
    @Before
    public void setUp() { Vector.BRANCHING_BASE = 2; }

    @Test
    public void shouldCreateAndGet() {
        for (byte depth = 0; depth <= 6; depth++) {
            final int length = getMaxSizeForDepth(depth);

            final Seq<Integer> expected = Array.range(0, length);
            final Vector<Integer> actual = Vector.ofAll(expected);

            int i = 0;
            for (Integer value : expected) {
                final Integer actualValue = actual.get(i++);
                assertThat(actualValue).isEqualTo(value);
            }

            System.out.println("Depth " + depth + " ok!");
        }
    }

    private static <T1, T2> Vector<Integer> assertAreEqual(T1 previousActual, T2 param, Function2<T1, T2, Vector<Integer>> actualProvider, Seq<Integer> expected) {
        final Vector<Integer> actual = actualProvider.apply(previousActual, param);
        assertAreEqual(expected, actual);
        return actual; // makes debugging a lot easier, as the frame can be dropped and rerun on AssertError
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
