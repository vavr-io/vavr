/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.List;
import javaslang.collection.Stream;
import org.junit.Test;

import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class GenTest {

    static final Random RANDOM = Property.RNG.get();
    static final int TRIES = 1000;

    // -- random number generator (rng)

    @Test
    public void shouldUseCustomRandomNumberGenerator() {
        @SuppressWarnings("SerializableInnerClassWithNonSerializableOuterClass")
        final Random rng = new Random() {
            private static final long serialVersionUID = 1L;
            public int nextInt(int bound) {
                return 0;
            }
        };
        final Gen<Integer> gen = Gen.choose(1, 2);
        final int actual = Stream.gen(() -> gen.apply(rng)).take(10).sum();
        assertThat(actual).isEqualTo(10);
    }

    // -- choose(int, int)

    @Test
    public void shouldChooseIntBetweenMinMax() {
        repeat(() -> {
            final int actual = Gen.choose(-1, 1).apply(RANDOM);
            assertThat(actual >= -1).isTrue();
            assertThat(actual <= 1).isTrue();
        });
    }

    @Test
    public void shouldChooseIntWhenMinEqualsMax() {
        repeat(() -> {
            final int actual = Gen.choose(0, 0).apply(RANDOM);
            assertThat(actual).isEqualTo(0);
        });
    }

    // -- choose(long, long)

    @Test
    public void shouldChooseLongBetweenMinMax() {
        repeat(() -> {
            final long actual = Gen.choose(-1L, 1L).apply(RANDOM);
            assertThat(actual >= -1L).isTrue();
            assertThat(actual <= 1L).isTrue();
        });
    }

    @Test
    public void shouldChooseLongWhenMinEqualsMax() {
        repeat(() -> {
            final long actual = Gen.choose(0L, 0L).apply(RANDOM);
            assertThat(actual).isEqualTo(0L);
        });
    }

    // -- choose(double, double)

    @Test
    public void shouldChooseDoubleBetweenMinMax() {
        repeat(() -> {
            final double actual = Gen.choose(-1.0d, 1.0d).apply(RANDOM);
            assertThat(actual >= -1.0d).isTrue();
            assertThat(actual <= 1.0d).isTrue();
        });
    }

    @Test
    public void shouldChooseDoubleWhenMinEqualsMax() {
        repeat(() -> {
            final double actual = Gen.choose(0.0d, 0.0d).apply(RANDOM);
            assertThat(actual).isEqualTo(0.0d);
        });
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMinIsNegativeInfinite() {
        Gen.choose(Double.NEGATIVE_INFINITY, 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMinIsPositiveInfinite() {
        Gen.choose(Double.POSITIVE_INFINITY, 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMinIsNotANumber() {
        Gen.choose(Double.NaN, 0.0d);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMaxIsNegativeInfinite() {
        Gen.choose(0.0d, Double.NEGATIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMaxIsPositiveInfinite() {
        Gen.choose(0.0d, Double.POSITIVE_INFINITY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenChooseDoubleAndMaxIsNotANumber() {
        Gen.choose(0.0d, Double.NaN);
    }

    // -- choose(char, char)

    @Test
    public void shouldChooseCharBetweenMinMax() {
        repeat(() -> {
            final char actual = Gen.choose('A', 'Z').apply(RANDOM);
            assertThat(actual >= 'A').isTrue();
            assertThat(actual <= 'Z').isTrue();
        });
    }

    @Test
    public void shouldChooseCharWhenMinEqualsMax() {
        repeat(() -> {
            final char actual = Gen.choose('λ', 'λ').apply(RANDOM);
            assertThat(actual).isEqualTo('λ');
        });
    }

    // -- fail

    @Test(expected = RuntimeException.class)
    public void shouldFailAlwaysWhenCallingFailingGen() {
        Gen.fail().apply(RANDOM);
    }

    // -- frequency(VarArgs)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCallingFrequencyOfVarArgsAndArgIsNull() {
        Gen.frequency((Tuple2<Integer, Gen<Object>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCallingFrequencyOfVarArgsAndArgIsEmpty() {
        @SuppressWarnings("unchecked")
        final Tuple2<Integer, Gen<Object>>[] actual = (Tuple2<Integer, Gen<Object>>[]) new Tuple2<?, ?>[0];
        Gen.frequency(actual);
    }

    @Test
    public void shouldGenerateElementsAccordingToFrequencyGivenVarArgs() {
        final Gen<Integer> gen = Gen.frequency(Tuple.of(0, Gen.of(-1)), Tuple.of(1, Gen.of(1)));
        repeat(() -> gen.apply(RANDOM), i -> i != -1);
    }

    // -- frequency(Iterable)

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCallingFrequencyOfIterableAndArgIsNull() {
        Gen.frequency((Iterable<Tuple2<Integer, Gen<Object>>>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCallingFrequencyOfIterableAndArgIsEmpty() {
        Gen.frequency(List.nil());
    }

    @Test
    public void shouldGenerateElementsAccordingToFrequencyGivenAnIterable() {
        final Gen<Integer> gen = Gen.frequency(List.of(Tuple.of(0, Gen.of(-1)), Tuple.of(1, Gen.of(1))));
        repeat(() -> gen.apply(RANDOM), i -> i != -1);
    }

    // -- oneOf(VarArgs)

    // TODO: GenTest.oneOf(VarArgs)

    // -- oneOf(Iterable)

    // TODO: GenTest.oneOf(Iterable)

    // -- arbitrary

    // TODO: GenTest.arbitrary

    // -- map

    // TODO: GenTest.map

    // -- flatMap

    // TODO: GenTest.flatMap

    // -- filter

    @Test
    public void shouldFilterGenerator() {
        final Gen<Integer> gen = Gen.choose(1, 2).filter(i -> i % 2 == 0);
        repeat(() -> gen.apply(RANDOM), i -> i == 2);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldDetectEmptyFilter() {
        Gen.of(1).filter(ignored -> false).apply(RANDOM);
    }

    // helpers

    void repeat(Runnable unit) {
        for (int i = 0; i < TRIES; i++) {
            unit.run();
        }
    }

    <T> void repeat(Supplier<T> supplier, Predicate<T> predicate) {
        for (int i = 0; i < TRIES; i++) {
            final T element = supplier.get();
            if (!predicate.test(element)) {
                throw new AssertionError("predicate did not hold for " + element);
            }
        }
    }
}
