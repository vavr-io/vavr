/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;

import java.util.function.Function;
import javaslang.Algebra.*;

public class AlgebraTest {

    @Test
    public void shouldNotBeInstantiable() {
        AssertionsExtensions.assertThat(Algebra.class).isNotInstantiable();
    }

    @Test
    public void shouldCombineMonoids() {
        final Monoid<Function<Integer, Integer>> endo = Monoid.endoMonoid();
        final Functions.λ1<Integer, Integer> after = i -> i + 1;
        final Functions.λ1<Integer, Integer> before = i -> i * 2;
        assertThat(endo.combine(after, before).apply(2)).isEqualTo(5);
    }
}
