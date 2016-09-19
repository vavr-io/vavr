/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.control.Option;
import javaslang.control.Try;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.concurrent.CompletableFuture.runAsync;
import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static javaslang.collection.Iterator.range;
import static org.assertj.core.api.Assertions.assertThat;

public class LazyTest {

    // -- of(Supplier)

    @Test
    public void shouldNotChangeLazy() {
        final Lazy<Integer> expected = Lazy.of(() -> 1);
        assertThat(Lazy.of(expected)).isSameAs(expected);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullSupplier() {
        Lazy.of(null);
    }

    @Test
    public void shouldMemoizeValues() {
        final Lazy<Double> testee = Lazy.of(Math::random);
        final double expected = testee.get();
        for (int i = 0; i < 10; i++) {
            final double actual = testee.get();
            assertThat(actual).isEqualTo(expected);
        }
    }

    // -- sequence(Iterable)

    @Test
    public void shouldSequenceEmpty() {
        final List<Lazy<Integer>> testee = List.empty();
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.get()).isEqualTo(List.empty());
    }

    @Test
    public void shouldSequenceNonEmptyLazy() {
        final List<Lazy<Integer>> testee = List.of(1, 2, 3).map(i -> Lazy.of(() -> i));
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.get()).isEqualTo(List.of(1, 2, 3));
    }

    @Test
    public void shouldNotEvaluateEmptySequence() {
        final List<Lazy<Integer>> testee = List.empty();
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.isEvaluated()).isFalse();
    }

    @Test
    public void shouldNotEvaluateNonEmptySequence() {
        final List<Lazy<Integer>> testee = List.of(1, 2, 3).map(i -> Lazy.of(() -> i));
        final Lazy<Seq<Integer>> sequence = Lazy.sequence(testee);
        assertThat(sequence.isEvaluated()).isFalse();
    }

    @Test
    public void shouldMapOverLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Lazy<Integer> expected = Lazy.of(() -> 21);

        assertThat(testee.map(i -> i / 2)).isEqualTo(expected);
    }

    @Test
    public void shouldFilterOverLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Option<Integer> expectedPositive = Option.some(42);
        final Option<Integer> expectedNegative = Option.none();

        assertThat(testee.filter(i -> i % 2 == 0)).isEqualTo(expectedPositive);
        assertThat(testee.filter(i -> i % 2 != 0)).isEqualTo(expectedNegative);
    }

    @Test
    public void shouldTransformLazyValue() {
        final Lazy<Integer> testee = Lazy.of(() -> 42);
        final Integer expected = 21;

        final Integer actual = testee.transform(lazy -> lazy.get() / 2);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldNotBeEmpty() {
        assertThat(Lazy.of(Option::none).isEmpty()).isFalse();
    }

    @Test
    public void shouldContainASingleValue() {
        assertThat(Lazy.of(Option::none).isSingleValued()).isTrue();
    }

    // -- val(Supplier, Class) -- Proxy

    @Test
    public void shouldCreateLazyProxy() {

        final String[] evaluated = new String[] { null };

        final CharSequence chars = Lazy.val(() -> {
            final String value = "Yay!";
            evaluated[0] = value;
            return value;
        }, CharSequence.class);

        assertThat(evaluated[0]).isEqualTo(null);
        assertThat(chars).isEqualTo("Yay!");
        assertThat(evaluated[0]).isEqualTo("Yay!");
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCreatingLazyProxyAndSupplierIsNull() {
        Lazy.val(null, CharSequence.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCreatingLazyProxyAndTypeIsNull() {
        Lazy.val(() -> "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreatingLazyProxyOfObjectType() {
        Lazy.val(() -> "", String.class);
    }

    @Test
    public void shouldBehaveLikeValueWhenCreatingProxy() {
        final CharSequence chars = Lazy.val(() -> "Yay!", CharSequence.class);
        assertThat(chars.toString()).isEqualTo("Yay!");
    }

    // -- isEvaluated()

    @Test
    public void shouldBeAwareOfEvaluated() {
        final Lazy<Void> lazy = Lazy.of(() -> null);
        assertThat(lazy.isEvaluated()).isFalse();
        assertThat(lazy.isEvaluated()).isFalse(); // remains not evaluated
        lazy.get();
        assertThat(lazy.isEvaluated()).isTrue();
    }

    // -- Serialization

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(Lazy.of(() -> 1)));
        final Object expected = Lazy.of(() -> 1);
        assertThat(actual).isEqualTo(expected);
    }

    // -- concurrency

    @Test
    public void shouldSupportMultithreading() {
        final AtomicBoolean isEvaluated = new AtomicBoolean();
        final AtomicBoolean lock = new AtomicBoolean();
        final Lazy<Integer> lazy = Lazy.of(() -> {
            while (lock.get()) {
                Try.run(() -> Thread.sleep(300));
            }
            return 1;
        });
        new Thread(() -> {
            Try.run(() -> Thread.sleep(100));
            new Thread(() -> {
                Try.run(() -> Thread.sleep(100));
                lock.set(false);
            }).start();
            isEvaluated.compareAndSet(false, lazy.isEvaluated());
            lazy.get();
        }).start();
        assertThat(isEvaluated.get()).isFalse();
        assertThat(lazy.get()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings({ "StatementWithEmptyBody", "rawtypes" })
    public void shouldBeConsistentFromMultipleThreads() throws Exception {
        for (int i = 0; i < 100; i++) {
            final AtomicBoolean canProceed = new AtomicBoolean(false);
            final Vector<CompletableFuture<Void>> futures = Vector.range(0, 10).map(j -> {
                final AtomicBoolean isEvaluated = new AtomicBoolean(false);
                final Integer expected = ((j % 2) == 1) ? null : j;
                Lazy<Integer> lazy = Lazy.of(() -> {
                    assertThat(isEvaluated.getAndSet(true)).isFalse();
                    return expected;
                });
                return Tuple.of(lazy, expected);
            }).flatMap(t -> range(0, 5).map(j -> runAsync(() -> {
                        while (!canProceed.get()) { /* busy wait */ }
                        assertThat(t._1.get()).isEqualTo(t._2);
                    }))
            );

            final CompletableFuture all = CompletableFuture.allOf(futures.toJavaList().toArray(new CompletableFuture<?>[0]));
            canProceed.set(true);
            all.join();
        }
    }

    // -- equals

    @Test
    public void shouldDetectEqualObject() {
        assertThat(Lazy.of(() -> 1).equals("")).isFalse();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 1))).isTrue();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 2))).isFalse();
        final Lazy<Integer> same = Lazy.of(() -> 1);
        assertThat(same.equals(same)).isTrue();
    }

    @Test
    public void shouldDetectUnequalObject() {
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 2))).isFalse();
    }

    // -- hashCode

    @Test
    public void shouldComputeHashCode() {
        assertThat(Lazy.of(() -> 1).hashCode()).isEqualTo(Objects.hash(1));
    }

    // -- toString

    @Test
    public void shouldConvertNonEvaluatedValueToString() {
        final Lazy<Integer> lazy = Lazy.of(() -> 1);
        assertThat(lazy.toString()).isEqualTo("Lazy(?)");
    }

    @Test
    public void shouldConvertEvaluatedValueToString() {
        final Lazy<Integer> lazy = Lazy.of(() -> 1);
        lazy.get();
        assertThat(lazy.toString()).isEqualTo("Lazy(1)");
    }

}
