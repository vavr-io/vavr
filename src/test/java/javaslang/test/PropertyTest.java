/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PropertyTest {

    @Test
    public void shouldCheckPythagoras() {

        final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

        // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
        final Property property = Property.forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
        final CheckResult result = property.check();

        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldRecognizeExhaustedParameters() {
        final Arbitrary<?> x = n -> random -> null;
        final CheckResult result = Property.forAll(x).suchThat(ignored -> false).implies(ignored -> true).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test
    public void shouldFalsifyFalseProperty() {
        final Arbitrary<Integer> ones = n -> random -> 1;
        final CheckResult result = Property.forAll(ones).suchThat(one -> one == 2).check();
        assertThat(result.isFalsified()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
    }

    @Test
    public void shouldRecognizeArbitraryError() {
        final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("woops"); };
        final CheckResult result = Property.forAll(arbitrary).suchThat(ignored -> true).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(0);
        assertThat(result.sample().isNotPresent()).isTrue();
    }

    @Test
    public void shouldRecognizeGenError() {
        final Arbitrary<?> arbitrary = Gen.fail("woops").arbitrary();
        final CheckResult result = Property.forAll(arbitrary).suchThat(ignored -> true).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isNotPresent()).isTrue();
    }

    @Test
    public void shouldRecognizePropertyError() {
        final Arbitrary<Integer> a1 = n -> random -> 1;
        final Arbitrary<Integer> a2 = n -> random -> 2;
        final CheckResult result = Property.forAll(a1, a2).suchThat((a, b) -> {
            throw new RuntimeException("woops");
        }).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isPresent()).isTrue();
        assertThat(result.sample().get()).isEqualTo(Tuple.of(1, 2));
    }

    @Test
    public void shouldXxx() {
        // ∀is,ss: length(is) = length(ss) → unzip(zip(is, ss)) = (is, ss)
        final Arbitrary<List<Integer>> ints = null; // TODO
        final Arbitrary<List<String>> strings = null; // TODO
        final CheckResult result = Property
                .forAll(ints, strings)
                .suchThat((is, ss) -> is.length() == ss.length())
                .implies((is, ss) -> is.zip(ss).unzip(t -> t).equals(Tuple.of(is, ss)))
                .check();
        System.out.println(result.error().get()); // TODO: CheckResult.toString
    }
}
