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
    @Ignore
    public void shouldCheckPythagoras() {

        // Aa,b Ec : a^2 + b^2 == c^2

        final Arbitrary<Double> real = n -> Gen.choose((double) -n, (double) n);

        final boolean check = Property.forAll(real, real).exists(real).suchThat((a, b, c) -> a * a + b * b == c * c);

        assertThat(check).isTrue();
    }
}
