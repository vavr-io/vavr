/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class MonoidTest {

    @Test
    public void shouldCombineMonoids() {
        final Monoid<Function<Integer, Integer>> endo = Monoid.endoMonoid();
        final Function<Integer, Integer> after = i -> i + 1;
        final Function<Integer, Integer> before = i -> i * 2;
        assertThat(endo.combine(after, before).apply(2)).isEqualTo(5);
    }
}
