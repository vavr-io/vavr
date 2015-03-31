/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Function1;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AlgebraTest {

    @Test
    public void shouldCombineMonoids() {
        final Monoid<Function1<Integer, Integer>> endo = Monoid.endoMonoid();
        final Function1<Integer, Integer> after = i -> i + 1;
        final Function1<Integer, Integer> before = i -> i * 2;
        assertThat(endo.combine(after, before).apply(2)).isEqualTo(5);
    }
}
