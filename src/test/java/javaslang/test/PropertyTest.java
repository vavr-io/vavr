/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Ignore;
import org.junit.Test;

public class PropertyTest {

    @Test
    public void shouldCheckPythagoras() {

        final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

        // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : a²+b² ∈ ℝ+)
        final Property property = Property.forAll(real, real).suchThat((a, b) -> a * a + b * b > .0d);
        final CheckResult checkResult = property.check();

        assertThat(checkResult.isSatisfied()).isTrue();
    }
}
