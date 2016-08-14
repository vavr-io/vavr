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
        for (int i = 0; i < 2000; i++) {
            final Seq<Integer> expected = Array.range(0, i);
            final Vector<Integer> actual = Vector.ofAll(expected);
            assertAreEqual(expected, actual);

            final Vector<Byte> actualByte = Vector.ofAll((byte[]) Arrays2.toPrimitiveArray(byte.class, expected.map(Integer::byteValue).toJavaArray()));
            assert actualByte.leading instanceof byte[];
            assertAreEqual(expected, actual);

            final Vector<Boolean> actualBoolean = Vector.ofAll((boolean[]) Arrays2.toPrimitiveArray(boolean.class, expected.map(v -> (v % 2) == 0).toJavaArray()));
            assert actualBoolean.leading instanceof boolean[];
            assertAreEqual(expected, actual);

            final Vector<Character> actualChar = Vector.ofAll((char[]) Arrays2.toPrimitiveArray(char.class, expected.map(v -> (char) v.intValue()).toJavaArray()));
            assert actualChar.leading instanceof char[];
            assertAreEqual(expected, actual);

            final Vector<Double> actualDouble = Vector.ofAll((double[]) Arrays2.toPrimitiveArray(double.class, expected.map(Integer::doubleValue).toJavaArray()));
            assert actualDouble.leading instanceof double[];
            assertAreEqual(expected, actual);

            final Vector<Integer> actualInt = Vector.ofAll((int[]) Arrays2.toPrimitiveArray(int.class, expected.toJavaArray()));
            assert actualInt.leading instanceof int[];
            assertAreEqual(expected, actual);

            final Vector<Long> actualLong = Vector.ofAll((long[]) Arrays2.toPrimitiveArray(long.class, expected.map(Integer::longValue).toJavaArray()));
            assert actualLong.leading instanceof long[];
            assertAreEqual(expected, actual);
        }
    }

    @Test
    public void shouldIterate() {
        final Seq<Integer> expected = Array.range(0, 10000);
        final Vector<Integer> actual = Vector.ofAll((int[]) Arrays2.toPrimitiveArray(int.class, expected.toJavaArray()));

        Iterator<Integer> expectedIterator = expected.iterator();
        for (int i = 0; i < actual.length(); ) {
            for (int value : (int[]) actual.getLeafUnsafe(i)) {
                assertThat(value).isEqualTo(expectedIterator.next());
                i++;
            }
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
