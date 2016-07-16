/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function2;
import javaslang.Tuple;
import javaslang.Tuple2;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static javaslang.Function2.constant;
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

    @Test
    public void shouldIterate() {
        for (byte depth = 1; depth <= 6; depth++) {
            for (int i = 0; i < getMaxSizeForDepth(depth); i++) {
                final Seq<Integer> expected = Array.range(0, i);
                final Vector<Integer> actual = Vector.ofAll(expected);

                assertAreEqual(actual, null, constant(actual), expected);
            }

            System.out.println("Depth " + depth + " ok!");
        }
    }

    @Test
    public void shouldPrepend() {
        Seq<Integer> expected = Array.empty();
        Vector<Integer> actual = Vector.empty();

        for (int drop = 0; drop <= (Vector.branchingFactor() + 1); drop += 2) {
            for (Integer value : Iterator.range(0, getMaxSizeForDepth(3) + 1)) {
                expected = expected.drop(drop);
                actual = assertAreEqual(actual, drop, Vector::drop, expected);

                expected = expected.prepend(value);
                actual = assertAreEqual(actual, value, Vector::prepend, expected);
            }
        }
    }

    @Test
    public void shouldAppend() {
        Seq<Integer> expected = Array.empty();
        Vector<Integer> actual = Vector.empty();

        for (int drop = 0; drop <= (Vector.branchingFactor() + 1); drop += 2) {
            for (Integer value : Iterator.range(0, getMaxSizeForDepth(2) + 1)) {
                expected = expected.drop(drop);
                actual = assertAreEqual(actual, drop, Vector::drop, expected);

                expected = expected.append(value);
                actual = assertAreEqual(actual, value, Vector::append, expected);
            }
        }
    }

    @Test
    public void shouldUpdate() {
        final Function<Integer, Integer> mapper = i -> i + 1;

        for (byte depth = 0; depth <= 2; depth++) {
            final int length = getMaxSizeForDepth(depth) + 1;

            for (int drop = 0; drop <= (Vector.branchingFactor() + 1); drop += 2) {
                Seq<Integer> expected = Array.range(0, length);
                Vector<Integer> actual = Vector.ofAll(expected);

                expected = expected.drop(drop); // test the `trailing` drops and the internal tree offset
                actual = assertAreEqual(actual, drop, Vector::drop, expected);

                for (int i = 0; i < actual.length(); i++) {
                    final Integer newValue = mapper.apply(actual.get(i));
                    actual = actual.update(i, newValue);
                }

                assertAreEqual(actual, 0, (a, p) -> a, expected.map(mapper));
            }
            System.out.println("Depth " + depth + " ok!");
        }
    }

    @Test
    public void shouldDrop() {
        final int length = getMaxSizeForDepth(6) + 1;

        final Seq<Integer> expected = Array.range(0, length);
        final Vector<Integer> actual = Vector.ofAll(expected);

        Vector<Integer> actualSingleDrop = actual;
        for (int i = 0; i <= length; i++) {
            final Seq<Integer> expectedDrop = expected.drop(i);

            assertAreEqual(actual, i, Vector::drop, expectedDrop);
            assertAreEqual(actualSingleDrop, null, (a, p) -> a, expectedDrop);

            actualSingleDrop = actualSingleDrop.drop(1);
        }
    }

    @Test
    public void shouldDropRight() {
        final int length = getMaxSizeForDepth(5) + 1;

        final Seq<Integer> expected = Array.range(0, length);
        final Vector<Integer> actual = Vector.ofAll(expected);

        Vector<Integer> actualSingleDrop = actual;
        for (int i = 0; i <= length; i++) {
            final Seq<Integer> expectedDrop = expected.dropRight(i);

            assertAreEqual(actual, i, Vector::dropRight, expectedDrop);
            assertAreEqual(actualSingleDrop, null, (a, p) -> a, expectedDrop);

            actualSingleDrop = actualSingleDrop.dropRight(1);
        }
    }

    @Test
    public void shouldSlice() {
        for (int length = 1, end = getMaxSizeForDepth(2) + 1; length <= end; length++) {
            Seq<Integer> expected = Array.range(0, length);
            Vector<Integer> actual = Vector.ofAll(expected);

            for (int i = 0; i <= expected.length(); i++) {
                expected = expected.slice(1, expected.size() - 1);
                actual = assertAreEqual(actual, i, (a, p) -> a.slice(1, a.size() - 1), expected);
            }
        }
    }

    @Test
    public void shouldBehaveLikeArray() {
        Random random = new Random();
        final int seed = random.nextInt();
        System.out.println("using seed " + seed);
        random = new Random(seed);

        for (int i = 0; i < 10; i++) {
            Seq<Integer> expected = Array.empty();
            Vector<Integer> actual = Vector.empty();
            for (int j = 0; j < 100_000; j++) {
                Seq<Tuple2<Seq<Integer>, Vector<Integer>>> history = Array.empty();

                if (random.nextInt(100) < 10) {
                    final ArrayList<Integer> values = new ArrayList<>();
                    for (int k = 0; k < random.nextInt(j + 1); k++) {
                        values.add(random.nextInt());
                    }
                    expected = Array.ofAll(values);
                    actual = assertAreEqual(values, null, (v, p) -> Vector.ofAll(v), expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (random.nextInt(100) < 50) {
                    final int value = random.nextInt();
                    expected = expected.append(value);
                    actual = assertAreEqual(actual, value, Vector::append, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (random.nextInt(100) < 50) {
                    final int value = random.nextInt();
                    expected = expected.prepend(value);
                    actual = assertAreEqual(actual, value, Vector::prepend, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (random.nextInt(100) < 30) {
                    final int n = random.nextInt(expected.size() + 1);
                    expected = expected.drop(n);
                    actual = assertAreEqual(actual, n, Vector::drop, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (random.nextInt(100) < 30) {
                    final int n = random.nextInt(expected.size() + 1);
                    expected = expected.take(n);
                    actual = assertAreEqual(actual, n, Vector::take, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (!expected.isEmpty()) {
                    assertThat(actual.head()).isEqualTo(expected.head());
                    assertThat(actual.tail().toJavaList()).isEqualTo(expected.tail().toJavaList());
                    history = history.append(Tuple.of(expected, actual));
                }

                if (!expected.isEmpty()) {
                    final int index = random.nextInt(expected.size());
                    assertThat(actual.get(index)).isEqualTo(expected.get(index));
                    history = history.append(Tuple.of(expected, actual));
                }

                if (random.nextInt(100) < 50) {
                    if (!expected.isEmpty()) {
                        final int index = random.nextInt(expected.size());
                        final int value = random.nextInt();
                        expected = expected.update(index, value);
                        actual = assertAreEqual(actual, null, (a, p) -> a.update(index, value), expected);
                        history = history.append(Tuple.of(expected, actual));
                    }
                }

                for (int k = 0; k < 10; k++) {
                    if (!expected.isEmpty()) {
                        final int from = random.nextInt(expected.size());
                        final int to = random.nextInt(expected.size());
                        expected = expected.slice(from, to);
                        actual = assertAreEqual(actual, null, (a, p) -> a.slice(from, to), expected);
                        history = history.append(Tuple.of(expected, actual));
                    }
                }

                history.forEach(t -> assertAreEqual(t._1, t._2)); // test that the modifications are persistent
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
