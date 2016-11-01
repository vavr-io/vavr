/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function2;
import javaslang.Tuple;
import javaslang.Tuple2;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.Predicate;

import static javaslang.collection.BitMappedTrie.BRANCHING_FACTOR;
import static org.assertj.core.api.Assertions.assertThat;

public class VectorPropertyTest {
    @Test
    public void shouldCreateAndGet() {
        for (int i = 0; i < 500; i++) {
            final Seq<Integer> expected = Array.range(0, i);
            final Vector<Integer> actual = Vector.ofAll(expected);
            for (int j = 0; j < actual.size(); j++) {
                assertThat(expected.get(j)).isEqualTo(actual.get(j));
            }

            /* boolean */
            final Seq<Boolean> expectedBoolean = expected.map(v -> v > 0);
            final Vector<Boolean> actualBoolean = Vector.ofAll(ArrayType.<boolean[]> asPrimitives(boolean.class, expectedBoolean));
            assert (i == 0) || (actualBoolean.trie.type.type() == boolean.class);
            assertAreEqual(expectedBoolean, actualBoolean);

            /* byte */
            final Seq<Byte> expectedByte = expected.map(Integer::byteValue);
            final Vector<Byte> actualByte = Vector.ofAll(ArrayType.<byte[]> asPrimitives(byte.class, expectedByte));
            assert (i == 0) || (actualByte.trie.type.type() == byte.class);
            assertAreEqual(expectedByte, actualByte);

            /* char */
            final Seq<Character> expectedChar = expected.map(v -> (char) v.intValue());
            final Vector<Character> actualChar = Vector.ofAll(ArrayType.<char[]> asPrimitives(char.class, expectedChar));
            assert (i == 0) || (actualChar.trie.type.type() == char.class);
            assertAreEqual(expectedChar, actualChar);

            /* double */
            final Seq<Double> expectedDouble = expected.map(Integer::doubleValue);
            final Vector<Double> actualDouble = Vector.ofAll(ArrayType.<double[]> asPrimitives(double.class, expectedDouble));
            assert (i == 0) || (actualDouble.trie.type.type() == double.class);
            assertAreEqual(expectedDouble, actualDouble);

            /* float */
            final Seq<Float> expectedFloat = expected.map(Integer::floatValue);
            final Vector<Float> actualFloat = Vector.ofAll(ArrayType.<float[]> asPrimitives(float.class, expectedFloat));
            assert (i == 0) || (actualFloat.trie.type.type() == float.class);
            assertAreEqual(expectedFloat, actualFloat);

            /* int */
            final Vector<Integer> actualInt = Vector.ofAll(ArrayType.<int[]> asPrimitives(int.class, expected));
            assert (i == 0) || (actualInt.trie.type.type() == int.class);
            assertAreEqual(expected, actualInt);

            /* long */
            final Seq<Long> expectedLong = expected.map(Integer::longValue);
            final Vector<Long> actualLog = Vector.ofAll(ArrayType.<long[]> asPrimitives(long.class, expectedLong));
            assert (i == 0) || (actualLog.trie.type.type() == long.class);
            assertAreEqual(expectedLong, actualLog);

            /* short */
            final Seq<Short> expectedShort = expected.map(Integer::shortValue);
            final Vector<Short> actualShort = Vector.ofAll(ArrayType.<short[]> asPrimitives(short.class, expectedShort));
            assert (i == 0) || (actualShort.trie.type.type() == short.class);
            assertAreEqual(expectedShort, actualShort);
        }
    }

    @Test
    public void shouldIterate() {
        for (byte depth = 0; depth <= 2; depth++) {
            for (int i = 0; i < 5000; i++) {
                final Seq<Integer> expected = Array.range(0, i);
                final Vector<Integer> actual = Vector.ofAll(expected);
                assertAreEqual(actual, expected);
            }
        }

        Seq<Integer> expected = Array.range(0, 1000);
        Vector<Integer> actual = Vector.ofAll(ArrayType.<int[]> asPrimitives(int.class, expected));
        for (int drop = 0; drop <= (BRANCHING_FACTOR + 1); drop++) {
            final Iterator<Integer> expectedIterator = expected.iterator();
            actual.trie.<int[]> visit((index, leaf, start, end) -> {
                for (int i = start; i < end; i++) {
                    assertThat(leaf[i]).isEqualTo(expectedIterator.next());
                }
                return -1;
            });

            expected = expected.tail().init();
            actual = actual.tail().init();
        }
    }

    @Test
    public void shouldPrepend() {
        Seq<Integer> expected = Array.empty();
        Vector<Integer> actual = Vector.empty();

        for (int drop = 0; drop <= (BRANCHING_FACTOR + 1); drop++) {
            for (Integer value : Iterator.range(0, 1000)) {
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

        for (int drop = 0; drop <= (BRANCHING_FACTOR + 1); drop++) {
            for (Integer value : Iterator.range(0, 500)) {
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
            final int length = 10_000;

            for (int drop = 0; drop <= (BRANCHING_FACTOR + 1); drop++) {
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
        }
    }

    @Test
    public void shouldDrop() {
        final Seq<Integer> expected = Array.range(0, 2_000);
        final Vector<Integer> actual = Vector.ofAll(expected);

        Vector<Integer> actualSingleDrop = actual;
        for (int i = 0; i <= expected.length(); i++) {
            final Seq<Integer> expectedDrop = expected.drop(i);

            assertAreEqual(actual, i, Vector::drop, expectedDrop);
            assertAreEqual(actualSingleDrop, null, (a, p) -> a, expectedDrop);

            actualSingleDrop = actualSingleDrop.drop(1);
        }
    }

    @Test
    public void shouldDropRight() {
        final Seq<Integer> expected = Array.range(0, 2_000);
        final Vector<Integer> actual = Vector.ofAll(expected);

        Vector<Integer> actualSingleDrop = actual;
        for (int i = 0; i <= expected.length(); i++) {
            final Seq<Integer> expectedDrop = expected.dropRight(i);

            assertAreEqual(actual, i, Vector::dropRight, expectedDrop);
            assertAreEqual(actualSingleDrop, null, (a, p) -> a, expectedDrop);

            actualSingleDrop = actualSingleDrop.dropRight(1);
        }
    }

    @Test
    public void shouldSlice() {
        for (int length = 1, end = 500; length <= end; length++) {
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
        final int seed = ThreadLocalRandom.current().nextInt();
        System.out.println("using seed " + seed);
        final Random random = new Random(seed);

        for (int i = 1; i < 10; i++) {
            Seq<Object> expected = Array.empty();
            Vector<Object> actual = Vector.empty();
            for (int j = 0; j < 20_000; j++) {
                Seq<Tuple2<Seq<Object>, Vector<Object>>> history = Array.empty();

                if (percent(random) < 20) {
                    final ArrayList<Object> values = new ArrayList<>();
                    for (int k = 0; k < random.nextInt(j + 1); k++) {
                        values.add(random.nextInt());
                    }
                    expected = Array.ofAll(values);
                    final boolean isPrimitive = percent(random) < 30;
                    actual = isPrimitive ? Vector.narrow(Vector.ofAll(ArrayType.<int[]> asPrimitives(int.class, values))) : Vector.ofAll(values);
                    assertAreEqual(expected, actual);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 50) {
                    final Object value = randomValue(random);
                    expected = expected.append(value);
                    actual = assertAreEqual(actual, value, Vector::append, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 50) {
                    final Object value = randomValue(random);
                    expected = expected.prepend(value);
                    actual = assertAreEqual(actual, value, Vector::prepend, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 30) {
                    final int n = random.nextInt(expected.size() + 1);
                    expected = expected.drop(n);
                    actual = assertAreEqual(actual, n, Vector::drop, expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 30) {
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

                if (percent(random) < 50) {
                    if (!expected.isEmpty()) {
                        final int index = random.nextInt(expected.size());
                        final Object value = randomValue(random);
                        expected = expected.update(index, value);
                        actual = assertAreEqual(actual, null, (a, p) -> a.update(index, value), expected);
                        history = history.append(Tuple.of(expected, actual));
                    }
                }

                if (percent(random) < 20) {
                    final Function<Object, Object> mapper = val -> (val instanceof Integer) ? ((Integer) val + 1) : val;
                    expected = expected.map(mapper);
                    actual = assertAreEqual(actual, null, (a, p) -> a.map(mapper), expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 30) {
                    final Predicate<Object> filter = val -> (String.valueOf(val).length() % 10) == 0;
                    expected = expected.filter(filter);
                    actual = assertAreEqual(actual, null, (a, p) -> a.filter(filter), expected);
                    history = history.append(Tuple.of(expected, actual));
                }

                if (percent(random) < 30) {
                    for (int k = 0; k < 2; k++) {
                        if (!expected.isEmpty()) {
                            final int to = random.nextInt(expected.size());
                            final int from = random.nextInt(to + 1);
                            expected = expected.slice(from, to);
                            actual = assertAreEqual(actual, null, (a, p) -> a.slice(from, to), expected);
                            history = history.append(Tuple.of(expected, actual));
                        }
                    }
                }

                history.forEach(t -> assertAreEqual(t._1, t._2)); // test that the modifications are persistent
            }
        }
    }

    private int percent(Random random) { return random.nextInt(100); }

    private Object randomValue(Random random) {
        final int percent = percent(random);
        if (percent < 5) {
            return null;
        } else if (percent < 10) {
            return "String";
        } else {
            return random.nextInt();
        }
    }

    private static <T extends Seq<?>, P> T assertAreEqual(T previousActual, P param, Function2<T, P, T> actualProvider, Seq<?> expected) {
        final T actual = actualProvider.apply(previousActual, param);
        assertAreEqual(expected, actual);
        return actual; // makes debugging a lot easier, as the frame can be dropped and rerun on AssertError
    }

    private static void assertAreEqual(Seq<?> expected, Seq<?> actual) {
        final List<?> actualList = actual.toJavaList();
        final List<?> expectedList = expected.toJavaList();
        assertThat(actualList).isEqualTo(expectedList); // a lot faster than `hasSameElementsAs`
    }
}
