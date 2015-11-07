/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Some;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class LazyTest {

    // -- ofAll(Supplier)

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

    // -- ofAll(Supplier, Class) -- Proxy

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

    // -- isEmpty()

    @Test
    public void shouldNotBeEmpty() {
        assertThat(Lazy.of(() -> null).isEmpty()).isEqualTo(false);
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

    // -- get

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowGetEmpty() {
        Lazy.empty().get();
    }

    // -- filter

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowFilterEmptyLazy() {
        Lazy.empty().filter(ignored -> true).get();
    }

    @Test
    public void shouldFilterNonEmptyLazy() {
        assertThat(Lazy.of(() -> 1).filter(i -> true)).isEqualTo(new Some<>(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowEmptyFilterNonEmptyLazy() {
        Lazy.of(() -> 1).filter(i -> false).get();
    }

    @Test
    public void shouldNonEmptyFilterNonEmptyLazy() {
        assertThat(Lazy.of(() -> 1).filter(i -> true)).isEqualTo(new Some<>(1));
    }

    // -- flatten()

    @Test(expected = NoSuchElementException.class)
    public void shouldFlattenEmptyLazy() {
        Lazy.empty().flatten().get();
    }

    @Test
    public void shouldFlattenLazyOfLazy() {
        assertThat(Lazy.of(() -> Lazy.of(() -> 1)).flatten()).isEqualTo(Lazy.of(() -> 1));
    }

    // -- flatMap

    @Test(expected = NoSuchElementException.class)
    public void shouldFlatMapEmptyLazy() {
        Lazy.empty().flatMap(List::of).get();
    }

    @Test
    public void shouldFlatMapNonEmptyLazy() {
        assertThat(Lazy.of(() -> 1).flatMap(List::of)).isEqualTo(Lazy.of(() -> 1));
    }

    // -- map

    @Test(expected = NoSuchElementException.class)
    public void shouldMapEmpty() {
        Lazy.empty().map(Function.identity()).get();
    }

    @Test
    public void shouldMapNonEmpty() {
        assertThat(Lazy.of(() -> 1).map(i -> i + 1)).isEqualTo(Lazy.of(() -> 2));
    }

    // -- peek

    @Test(expected = NoSuchElementException.class)
    public void shouldPeekEmpty() {
        Lazy.empty().peek(t -> {
        });
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final int[] effect = { 0 };
        assertThat(Lazy.of(() -> 1).peek(i -> effect[0] = i)).isEqualTo(Lazy.of(() -> 1));
        assertThat(effect[0]).isEqualTo(1);
    }

    // -- equals

    @Test
    public void shouldDetectEqualObject() {
        assertThat(Lazy.of(() -> 1).equals(Lazy.of(() -> 1))).isTrue();
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
