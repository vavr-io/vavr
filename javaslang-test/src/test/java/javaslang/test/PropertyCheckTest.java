/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.CheckedFunction1;
import javaslang.Tuple;
import javaslang.Tuple1;
import javaslang.collection.List;
import org.junit.Test;

import java.util.Random;

import static javaslang.API.Some;
import static javaslang.API.Tuple;
import static org.assertj.core.api.Assertions.assertThat;

public class PropertyCheckTest {

    private static final String WOOPS = "yay! (this is a negative test)";
    private static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();
    private static <T> CheckedFunction1<T, Boolean> tautology() {
        return any -> true;
    }
    private static <T> CheckedFunction1<T, Boolean> falsum() {
        return any -> false;
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenPropertyNameIsNull() {
        Property.def(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowWhenPropertyNameIsEmpty() {
        Property.def("");
    }

    // -- Property.check methods

    @Test
    public void shouldCheckUsingDefaultConfiguration() {
        final CheckResult<?> result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckGivenSizeAndTries() {
        final CheckResult<?> result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCheckGivenNegativeTries() {
        Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(0, -1);
    }

    @Test
    public void shouldCheckGivenRandomAndSizeAndTries() {
        final CheckResult<?> result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(new Random(), 0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- satisfaction

    @Test
    public void shouldCheckPythagoras() {

        final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

        // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
        final Checkable<?> property = Property.def("test").forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
        final CheckResult<?> result = property.check();

        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckZipAndThenUnzipIsIdempotentForListsOfSameLength() {
        // ∀is,ss: length(is) = length(ss) → unzip(zip(is, ss)) = (is, ss)
        final Arbitrary<List<Integer>> ints = Arbitrary.list(size -> Gen.choose(0, size));
        final Arbitrary<List<String>> strings = Arbitrary.list(
                Arbitrary.string(
                        Gen.frequency(
                                Tuple.of(1, Gen.choose('A', 'Z')),
                                Tuple.of(1, Gen.choose('a', 'z')),
                                Tuple.of(1, Gen.choose('0', '9'))
                        )));
        final CheckResult<?> result = Property.def("test")
                                              .forAll(ints, strings)
                                              .suchThat((is, ss) -> is.length() == ss.length())
                                              .implies((is, ss) -> is.zip(ss).unzip(t -> t).equals(Tuple.of(is, ss)))
                                              .check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    // -- exhausting

    @Test
    public void shouldRecognizeExhaustedParameters() {
        final CheckResult<?> result = Property.def("test").forAll(OBJECTS).suchThat(falsum()).implies(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- falsification

    @Test
    public void shouldFalsifyFalseProperty() {
        final Arbitrary<Integer> ones = n -> random -> 1;
        final CheckResult<?> result = Property.def("test").forAll(ones).suchThat(one -> one == 2).check();
        assertThat(result.isFalsified()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
    }

    // -- error detection

    @Test
    public void shouldRecognizeArbitraryError() {
        final Arbitrary<?> arbitrary = n -> { throw new RuntimeException(WOOPS); };
        final CheckResult<?> result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeGenError() {
        final Arbitrary<?> arbitrary = Gen.fail(WOOPS).arbitrary();
        final CheckResult<?> result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizePropertyError() {
        final Arbitrary<Integer> a1 = n -> random -> 1;
        final Arbitrary<Integer> a2 = n -> random -> 2;
        final CheckResult<?> result = Property.def("test").forAll(a1, a2).suchThat((a, b) -> {
            throw new RuntimeException(WOOPS);
        }).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isDefined()).isTrue();
        assertThat(result.sample().get()).isEqualTo(Tuple.of(1, 2));
    }

    // -- Property.and tests

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult<?> result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult<?> result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult<?> result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult<?> result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    // -- Property.or tests

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult<?> result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult<?> result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult<?> result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable<Tuple1<Object>> p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable<Tuple1<Object>> p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult<?> result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    // -- Shrink

    @Test
    public void shouldShrinkErroneous() throws Exception {
        final CheckResult<Tuple1<Integer>> check =
                Property.def("test")
                        .forAll(Arbitrary.of(Integer.MAX_VALUE))
                        .suchThat((i) -> {throw new RuntimeException(WOOPS);})
                        .shrinking(Shrink.integer())
                        .check();

        check.assertIsErroneous();
        assertThat(check.shrinks()).isGreaterThan(0);
        assertThat(check.sample()).isEqualTo(Some(Tuple(0)));
    }

    @Test
    public void shouldShrinkFalsified() throws Exception {
        final CheckResult<?> check = Property.def("test")
                                             .forAll(Arbitrary.of(Integer.MAX_VALUE))
                                             .suchThat(i -> i < 1)//to stop shrinking
                                             .shrinking(Shrink.integer())
                                             .check();

        check.assertIsFalsified();
        assertThat(check.shrinks()).isGreaterThan(0);
        assertThat(check.sample()).isEqualTo(Some(Tuple(1)));
    }

    @Test
    public void shouldNotShrinkSatisfied() throws Exception {
        final CheckResult<?> check = Property.def("test")
                                             .forAll(Arbitrary.of(Integer.MAX_VALUE))
                                             .suchThat(tautology())
                                             .shrinking(Shrink.integer())
                                             .check();

        check.assertIsSatisfied();
        assertThat(check.shrinks()).isEqualTo(0);
    }
}