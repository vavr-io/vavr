/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Kind1;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/* TODO: we need a similar test mixin
 *       interface BiMonadTestMixings<M extends BiMonad<?, ?>> extends BiMonadLaws<M>, BiFunctorLaws
 */
public interface MonadTestMixin<M extends Kind1<M, ?>> extends MonadLaws<M>, FunctorLaws {

    <T> Monad<M, T> unit();

    <T> Monad<M, T> unit(T element);

    @SuppressWarnings("unchecked")
    <T> Monad<M, T> unit(T... elements);

    // -- flatMap

    @Test
    default void shouldFlatMapEmpty() {
        assertThat(unit().flatMap(this::unit)).isEqualTo(unit());
    }

    @Test
    default void shouldFlatMapNonEmpty() {
        assertThat(unit(1, 2, 3).flatMap(this::unit)).isEqualTo(unit(1, 2, 3));
    }

    @Test
    default void shouldFlatMapNonEmptyByExpandingElements() {
        assertThat(unit(1, 2, 3).flatMap(i -> {
            if (i == 1) {
                return unit(1, 2, 3);
            } else if (i == 2) {
                return unit(4, 5);
            } else {
                return unit(6);
            }
        })).isEqualTo(unit(1, 2, 3, 4, 5, 6));
    }

    @Test
    default void shouldFlatMapNonEmptyInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Monad<M, Integer> actualInts = unit(0, 1, 2)
                .flatMap(ignored -> unit(seq.getAndIncrement(), seq.getAndIncrement()));
        final Monad<M, Integer> expectedInts = unit(0, 1, 2, 3, 4, 5);
        assertThat(actualInts).isEqualTo(expectedInts);
    }

    // -- map

    @Test
    default void shouldMapNil() {
        assertThat(this.<Integer> unit().map(i -> i + 1)).isEqualTo(unit());
    }

    @Test
    default void shouldMapNonNil() {
        assertThat(unit(1, 2, 3).map(i -> i + 1)).isEqualTo(unit(2, 3, 4));
    }

    @Test
    default void shouldMapInTheRightOrder() {
        final AtomicInteger seq = new AtomicInteger(0);
        final Monad<M, Integer> expectedInts = unit(0, 1, 2, 3, 4);
        final Monad<M, Integer> actualInts = unit(0, 1, 2, 3, 4).map(ignored -> seq.getAndIncrement());
        assertThat(actualInts).isEqualTo(expectedInts);
    }
}
