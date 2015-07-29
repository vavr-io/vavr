/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.junit.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class LazyTest {

    // -- of(Supplier)

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

    // -- of(Supplier, Class) -- Proxy

    @Test
    public void shouldCreateLazyProxy() {

        final String[] evaluated = new String[] { null };

        final CharSequence chars = Lazy.asVal(() -> {
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
        Lazy.asVal(null, CharSequence.class);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCreatingLazyProxyAndTypeIsNull() {
        Lazy.asVal(() -> "", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenCreatingLazyProxyOfObjectType() {
        Lazy.asVal(() -> "", String.class);
    }

    @Test
    public void shouldBehaveLikeValueWhenCreatingProxy() {
        final CharSequence chars = Lazy.asVal(() -> "Yay!", CharSequence.class);
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
