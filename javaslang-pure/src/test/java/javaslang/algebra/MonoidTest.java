/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.collection.List;
import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class MonoidTest {

    // -- combine

    @Test
    public void shouldCombineMonoids() {
        final Monoid<Function<Integer, Integer>> endo = Monoid.endoMonoid();
        final Function<Integer, Integer> after = i -> i + 1;
        final Function<Integer, Integer> before = i -> i * 2;
        assertThat(endo.combine(after, before).apply(2)).isEqualTo(5);
    }
    
    // -- fold

    @Test
    public void shouldFoldMultipleElements() {
        // DEV note:  generic is required for Windows compiler (1.8.0_45)
        assertThat(Monoid.fold(Monoid.<Integer>of(0, (a, b) -> a + b), List.of(1, 2, 3))).isEqualTo(6);
    }

    // -- foldLeft

    @Test
    public void shouldFoldLeftNil() {
        assertThat(Monoid.foldLeft(Monoid.of("", (xs, x) -> xs + x), List.<String> empty())).isEqualTo("");
    }

    @Test
    public void shouldFoldLeftNonNil() {
        assertThat(Monoid.foldLeft(Monoid.of("", (xs, x) -> xs + x), List.of("a", "b", "c"))).isEqualTo("abc");
    }

    // -- foldRight

    @Test
    public void shouldFoldRightNil() {
        assertThat(Monoid.foldRight(Monoid.of("", (x, xs) -> x + xs), List.<String> empty())).isEqualTo("");
    }

    @Test
    public void shouldFoldRightNonNil() {
        assertThat(Monoid.foldRight(Monoid.of("", (x, xs) -> x + xs), List.of("a", "b", "c"))).isEqualTo("abc");
    }

    // -- foldMap

    @Test
    public void shouldFoldMapNil() {
        assertThat(Monoid.foldMap(Monoid.of("", (x, xs) -> x + xs), List.empty(), String::valueOf)).isEqualTo("");
    }

    @Test(expected = NullPointerException.class)
    public void shouldFoldMapFailNullMapper() {
        Monoid.foldMap(Monoid.of("", (x, xs) -> x + xs), List.empty(), null);
    }

    @Test
    public void shouldFoldMapNonNil() {
        assertThat(Monoid.foldMap(Monoid.of("", (x, xs) -> x + xs), List.of(1, 2, 3), String::valueOf)).isEqualTo("123");
    }
    
}
