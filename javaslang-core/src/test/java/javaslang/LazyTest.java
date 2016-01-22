/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.control.Try;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.assertj.core.api.Assertions.assertThat;

public class LazyTest {

    @Test
    public void shouldBeSingletonType() {
        assertThat(Lazy.of(() -> 1).isSingleValued()).isTrue();
    }

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

    // -- iterator

    @Test
    public void shouldReturnIterator() {
        assertThat(Lazy.undefined().iterator().hasNext()).isFalse();
        assertThat(Lazy.of(() -> 1).iterator().hasNext()).isTrue();
        assertThat(Lazy.of(() -> 1).iterator().next()).isEqualTo(1);
    }

    // -- isEmpty()

    @Test
    public void shouldEnsureThatNonEmptyLazyIsNotEmpty() {
        assertThat(Lazy.of(() -> null).isEmpty()).isFalse();
    }

    @Test
    public void shouldEnsureThatEmptyLazyIsEmpty() {
        assertThat(Lazy.undefined().isEmpty()).isTrue();
    }

    // -- isEvaluated()

    @Test(expected = UnsupportedOperationException.class)
    public void shouldFailIsEvaluatedOfUndefined() {
        Lazy.undefined().isEvaluated();
    }

    @Test
    public void shouldBeAwareOfEvaluated() {
        final Lazy<Void> lazy = Lazy.of(() -> null);
        assertThat(lazy.isEvaluated()).isFalse();
        assertThat(lazy.isEvaluated()).isFalse(); // remains not evaluated
        lazy.get();
        assertThat(lazy.isEvaluated()).isTrue();
    }

    // -- get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowGetEmpty() {
        Lazy.undefined().get();
    }

    // -- orElse

    @Test
    public void shouldReturnSelfOnOrElseIfValueIsPresent() {
        Lazy<Integer> lazy = Lazy.of(() -> 42);
        assertThat(lazy.orElse(Lazy.of(() -> (0)))).isSameAs(lazy);
    }

    @Test
    public void shouldReturnSelfOnOrElseSupplierIfValueIsPresent() {
        Lazy<Integer> lazy = Lazy.of(() -> 42);
        assertThat(lazy.orElse(() -> Lazy.of(() -> 0))).isSameAs(lazy);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseIfValueIsNotPresent() {
        Lazy<Integer> lazy = Lazy.of(() -> 42);
        assertThat(Lazy.undefined().orElse(lazy)).isSameAs(lazy);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseSupplierIfValueIsNotPresent() {
        Lazy<Integer> lazy = Lazy.of(() -> 42);
        assertThat(Lazy.undefined().orElse(() -> lazy)).isSameAs(lazy);
    }

    // -- filter

    @Test
    public void shouldThrowFilterEmptyLazy() {
        final Lazy<Integer> testee = Lazy.undefined();
        final Lazy<Integer> actual = testee.filter(i -> true);
        assertThat(actual == testee).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowEmptyFilterNonEmptyLazy() {
        Lazy.of(() -> 1).filter(i -> false).get();
    }

    @Test
    public void shouldFilterNonEmptyLazy() {
        final Lazy<Integer> testee = Lazy.of(() -> 1);
        final Lazy<Integer> actual = testee.filter(i -> true);
        assertThat(actual == testee).isTrue();
    }

    // -- filterNot

    @Test
    public void shouldThrowFilterNotEmptyLazy() {
        final Lazy<Integer> testee = Lazy.undefined();
        final Lazy<Integer> actual = testee.filterNot(i -> false);
        assertThat(actual == testee).isTrue();
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowEmptyFilterNotNonEmptyLazy() {
        Lazy.of(() -> 1).filterNot(i -> true).get();
    }

    @Test
    public void shouldFilterNotNonEmptyLazy() {
        final Lazy<Integer> testee = Lazy.of(() -> 1);
        final Lazy<Integer> actual = testee.filterNot(i -> false);
        assertThat(actual == testee).isTrue();
    }

    // -- flatMap

    @Test(expected = NoSuchElementException.class)
    public void shouldFlatMapEmptyLazy() {
        Lazy.undefined().flatMap(v -> Lazy.of(() -> v)).get();
    }

    @Test
    public void shouldFlatMapNonEmptyLazy() {
        assertThat(Lazy.of(() -> 1).flatMap(v -> Lazy.of(() -> v))).isEqualTo(Lazy.of(() -> 1));
        assertThat(Lazy.of(() -> 1).flatMap(ignored -> Lazy.undefined())).isSameAs(Lazy.undefined());
    }

    // -- map

    @Test
    public void shouldMapEmpty() {
        assertThat(Lazy.undefined().map(Function.identity())).isEqualTo(Lazy.undefined());
    }

    @Test
    public void shouldMapNonEmpty() {
        assertThat(Lazy.of(() -> 1).map(i -> i + 1)).isEqualTo(Lazy.of(() -> 2));
    }

    // -- peek

    @Test
    public void shouldPeekEmpty() {
        final Lazy<?> testee = Lazy.undefined();
        final Lazy<?> actual = testee.peek(t -> {});
        assertThat(actual == testee).isTrue();
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final int[] effect = { 0 };
        final Lazy<Integer> testee = Lazy.of(() -> 1);
        final Lazy<Integer> actual = testee.peek(i -> effect[0] = i);
        assertThat(actual == testee).isTrue();
        assertThat(effect[0]).isEqualTo(1);
    }

    // -- transform

    @Test
    public void shouldTransform() {
        assertThat((Integer) Lazy.of(() -> 1).transform(l -> l.get())).isEqualTo(1);
    }

    // -- serializable

    @Test
    public void shouldPreserveSingletonInstanceOnDeserialization() {
        final boolean actual = deserialize(serialize(Lazy.undefined())) == Lazy.undefined();
        assertThat(actual).isTrue();
    }

    @Test
    public void shouldSerializeDeserializeNonNil() {
        final Object actual = deserialize(serialize(Lazy.of(() -> 1)));
        final Object expected = Lazy.of(() -> 1);
        assertThat(actual).isEqualTo(expected);
    }

    // -- concurrency

    @Test
    public void shouldSupportMultithreading() {
        final boolean[] lock = new boolean[] { true };
        final Lazy<Integer> lazy = Lazy.of(() -> {
            while (lock[0]) {
                Try.run(() -> Thread.sleep(300));
            }
            return 1;
        });
        new Thread(() -> {
            Try.run(() -> Thread.sleep(100));
            new Thread(() -> {
                Try.run(() -> Thread.sleep(100));
                lock[0] = false;
            }).start();
            assertThat(lazy.isEvaluated()).isFalse();
            lazy.get();
        }).start();
        assertThat(lazy.get()).isEqualTo(1);
    }

    // -- equals

    @Test
    public void shouldDetectEqualObject() {
        assertThat(Lazy.undefined().equals(Lazy.undefined())).isTrue();
        assertThat(Lazy.undefined().equals(Lazy.of(() -> 1))).isFalse();
        assertThat(Lazy.of(() -> 1).equals("")).isFalse();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 1))).isTrue();
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 2))).isFalse();
        Lazy<Integer> same = Lazy.of(() -> 1);
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
        assertThat(Lazy.undefined().hashCode()).isEqualTo(Lazy.undefined().hashCode());
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

    @Test
    public void shouldConvertUndefinedToString() {
        assertThat(Lazy.undefined().toString()).isEqualTo("Lazy()");
    }
}
