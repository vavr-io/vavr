/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Random;
import javaslang.*;
import javaslang.collection.List;
import org.junit.Test;

public class PropertyTest {

    static <T> CheckedFunction1<T, Boolean> tautology() {
        return any -> true;
    }

    static <T> CheckedFunction1<T, Boolean> falsum() {
        return any -> false;
    }

    static Arbitrary<Object> objects = Gen.of(null).arbitrary();

    // -- Property.check methods

    @Test
    public void shouldCheckUsingDefaultConfiguration() {
        final CheckResult result = new Property("test").forAll(objects).suchThat(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckGivenSizeAndTries() {
        final CheckResult result = new Property("test").forAll(objects).suchThat(tautology()).check(0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCheckGivenNegativeTries() {
        new Property("test").forAll(objects).suchThat(tautology()).check(0, -1);
    }

    @Test
    public void shouldCheckGivenRandomAndSizeAndTries() {
        final CheckResult result = new Property("test").forAll(objects).suchThat(tautology()).check(new Random(), 0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- satisfaction

    @Test
    public void shouldCheckPythagoras() {

        final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

        // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
        final Checkable property = new Property("test").forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
        final CheckResult result = property.check();

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
        final CheckResult result = new Property("test")
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
        final CheckResult result = new Property("test").forAll(objects).suchThat(falsum()).implies(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- falsification

    @Test
    public void shouldFalsifyFalseProperty() {
        final Arbitrary<Integer> ones = n -> random -> 1;
        final CheckResult result = new Property("test").forAll(ones).suchThat(one -> one == 2).check();
        assertThat(result.isFalsified()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
    }

    // -- error detection

    @Test
    public void shouldRecognizeArbitraryError() {
        final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(0);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeGenError() {
        final Arbitrary<?> arbitrary = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizePropertyError() {
        final Arbitrary<Integer> a1 = n -> random -> 1;
        final Arbitrary<Integer> a2 = n -> random -> 2;
        final CheckResult result = new Property("test").forAll(a1, a2).suchThat((a, b) -> {
            throw new RuntimeException("woops");
        }).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isDefined()).isTrue();
        assertThat(result.sample().get()).isEqualTo(Tuple.of(1, 2));
    }

    // -- Property checks

    @Test
    public void shouldApplyForAllOfArity1() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity1() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> true;
        final Property.Property1<Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty1() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty1() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty1() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty1ImplicationWithTruePrecondition() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> p1 = (o1) -> true;
        final CheckedFunction1<Object, Boolean> p2 = (o1) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty1ImplicationWithFalsePrecondition() {
        final Property.ForAll1<Object> forAll = new Property("test").forAll(objects);
        final CheckedFunction1<Object, Boolean> p1 = (o1) -> false;
        final CheckedFunction1<Object, Boolean> p2 = (o1) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty1CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects)
            .suchThat((o1) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty1CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen)
            .suchThat((o1) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty1CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary)
            .suchThat((o1) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity2() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity2() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> true;
        final Property.Property2<Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty2() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty2() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty2() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty2ImplicationWithTruePrecondition() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> p1 = (o1, o2) -> true;
        final CheckedFunction2<Object, Object, Boolean> p2 = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty2ImplicationWithFalsePrecondition() {
        final Property.ForAll2<Object, Object> forAll = new Property("test").forAll(objects, objects);
        final CheckedFunction2<Object, Object, Boolean> p1 = (o1, o2) -> false;
        final CheckedFunction2<Object, Object, Boolean> p2 = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty2CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects)
            .suchThat((o1, o2) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty2CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects)
            .suchThat((o1, o2) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty2CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects)
            .suchThat((o1, o2) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity3() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity3() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> true;
        final Property.Property3<Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty3ImplicationWithTruePrecondition() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> p1 = (o1, o2, o3) -> true;
        final CheckedFunction3<Object, Object, Object, Boolean> p2 = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty3ImplicationWithFalsePrecondition() {
        final Property.ForAll3<Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects);
        final CheckedFunction3<Object, Object, Object, Boolean> p1 = (o1, o2, o3) -> false;
        final CheckedFunction3<Object, Object, Object, Boolean> p2 = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty3CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects)
            .suchThat((o1, o2, o3) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty3CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects)
            .suchThat((o1, o2, o3) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty3CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects)
            .suchThat((o1, o2, o3) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> true;
        final Property.Property4<Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty4ImplicationWithTruePrecondition() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4) -> true;
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty4ImplicationWithFalsePrecondition() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4) -> false;
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty4CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty4CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects)
            .suchThat((o1, o2, o3, o4) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty4CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects)
            .suchThat((o1, o2, o3, o4) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> true;
        final Property.Property5<Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty5ImplicationWithTruePrecondition() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5) -> true;
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty5ImplicationWithFalsePrecondition() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5) -> false;
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty5CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty5CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty5CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity6() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity6() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6) -> true;
        final Property.Property6<Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty6() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty6() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty6() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty6ImplicationWithTruePrecondition() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6) -> true;
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty6ImplicationWithFalsePrecondition() {
        final Property.ForAll6<Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects);
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6) -> false;
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty6CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty6CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty6CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity7() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity7() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7) -> true;
        final Property.Property7<Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty7() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty7() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty7() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty7ImplicationWithTruePrecondition() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7) -> true;
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty7ImplicationWithFalsePrecondition() {
        final Property.ForAll7<Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7) -> false;
        final CheckedFunction7<Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty7CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty7CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty7CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity8() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity8() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8) -> true;
        final Property.Property8<Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty8() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty8() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty8() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty8ImplicationWithTruePrecondition() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8) -> true;
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty8ImplicationWithFalsePrecondition() {
        final Property.ForAll8<Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8) -> false;
        final CheckedFunction8<Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty8CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty8CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty8CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity9() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity9() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true;
        final Property.Property9<Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty9() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty9() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty9() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty9ImplicationWithTruePrecondition() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true;
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty9ImplicationWithFalsePrecondition() {
        final Property.ForAll9<Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> false;
        final CheckedFunction9<Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty9CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty9CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty9CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final Property.Property10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty10ImplicationWithTruePrecondition() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty10ImplicationWithFalsePrecondition() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> false;
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty10CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty10CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty10CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity11() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity11() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true;
        final Property.Property11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty11() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty11() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty11() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty11ImplicationWithTruePrecondition() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true;
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty11ImplicationWithFalsePrecondition() {
        final Property.ForAll11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> false;
        final CheckedFunction11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty11CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty11CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty11CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity12() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity12() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true;
        final Property.Property12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty12() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty12() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty12() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty12ImplicationWithTruePrecondition() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true;
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty12ImplicationWithFalsePrecondition() {
        final Property.ForAll12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> false;
        final CheckedFunction12<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty12CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty12CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty12CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity13() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity13() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true;
        final Property.Property13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty13() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty13() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty13() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty13ImplicationWithTruePrecondition() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true;
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty13ImplicationWithFalsePrecondition() {
        final Property.ForAll13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> false;
        final CheckedFunction13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty13CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty13CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty13CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity14() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity14() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true;
        final Property.Property14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty14() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty14() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty14() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty14ImplicationWithTruePrecondition() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true;
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty14ImplicationWithFalsePrecondition() {
        final Property.ForAll14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> false;
        final CheckedFunction14<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty14CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty14CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty14CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity15() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity15() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true;
        final Property.Property15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty15() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty15() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty15() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty15ImplicationWithTruePrecondition() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true;
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty15ImplicationWithFalsePrecondition() {
        final Property.ForAll15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> false;
        final CheckedFunction15<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty15CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty15CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty15CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity16() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity16() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true;
        final Property.Property16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty16() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty16() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty16() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty16ImplicationWithTruePrecondition() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true;
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty16ImplicationWithFalsePrecondition() {
        final Property.ForAll16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> false;
        final CheckedFunction16<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty16CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty16CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty16CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity17() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity17() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true;
        final Property.Property17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty17() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty17() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty17() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty17ImplicationWithTruePrecondition() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true;
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty17ImplicationWithFalsePrecondition() {
        final Property.ForAll17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> false;
        final CheckedFunction17<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty17CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty17CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty17CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity18() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity18() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true;
        final Property.Property18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty18() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty18() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty18() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty18ImplicationWithTruePrecondition() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true;
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty18ImplicationWithFalsePrecondition() {
        final Property.ForAll18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> false;
        final CheckedFunction18<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty18CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty18CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty18CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity19() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity19() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true;
        final Property.Property19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty19() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty19() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty19() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty19ImplicationWithTruePrecondition() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true;
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty19ImplicationWithFalsePrecondition() {
        final Property.ForAll19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> false;
        final CheckedFunction19<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty19CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty19CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty19CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity20() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity20() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true;
        final Property.Property20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty20() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty20() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty20() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty20ImplicationWithTruePrecondition() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true;
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty20ImplicationWithFalsePrecondition() {
        final Property.ForAll20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> false;
        final CheckedFunction20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty20CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty20CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty20CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity21() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity21() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true;
        final Property.Property21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty21() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty21() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty21() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty21ImplicationWithTruePrecondition() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true;
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty21ImplicationWithFalsePrecondition() {
        final Property.ForAll21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> false;
        final CheckedFunction21<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty21CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty21CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty21CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity22() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity22() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true;
        final Property.Property22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty22() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty22() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty22() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty22ImplicationWithTruePrecondition() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true;
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty22ImplicationWithFalsePrecondition() {
        final Property.ForAll22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> false;
        final CheckedFunction22<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty22CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty22CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty22CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity23() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity23() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true;
        final Property.Property23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty23() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty23() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty23() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty23ImplicationWithTruePrecondition() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true;
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty23ImplicationWithFalsePrecondition() {
        final Property.ForAll23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> false;
        final CheckedFunction23<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty23CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty23CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty23CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity24() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity24() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true;
        final Property.Property24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty24() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty24() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty24() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty24ImplicationWithTruePrecondition() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true;
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty24ImplicationWithFalsePrecondition() {
        final Property.ForAll24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> false;
        final CheckedFunction24<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty24CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty24CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty24CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity25() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity25() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true;
        final Property.Property25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty25() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty25() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty25() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty25ImplicationWithTruePrecondition() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true;
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty25ImplicationWithFalsePrecondition() {
        final Property.ForAll25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> false;
        final CheckedFunction25<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty25CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty25CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty25CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldApplyForAllOfArity26() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity26() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true;
        final Property.Property26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty26() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty26() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty26() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> { throw new RuntimeException("woops"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty26ImplicationWithTruePrecondition() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true;
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty26ImplicationWithFalsePrecondition() {
        final Property.ForAll26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = new Property("test").forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects);
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> false;
        final CheckedFunction26<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty26CheckGivenNegativeTries() {
        new Property("test")
            .forAll(objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty26CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("woops").arbitrary();
        final CheckResult result = new Property("test")
            .forAll(failingGen, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty26CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("woops"); };
        final CheckResult result = new Property("test")
            .forAll(failingArbitrary, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects, objects)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20, o21, o22, o23, o24, o25, o26) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    // -- Property.and tests

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(tautology());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(tautology());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(tautology());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(falsum());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(falsum());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(tautology());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(falsum());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(falsum());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    // -- Property.or tests

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(tautology());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(tautology());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(tautology());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(falsum());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(falsum());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(tautology());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable p1 = new Property("test").forAll(objects).suchThat(falsum());
        final Checkable p2 = new Property("test").forAll(objects).suchThat(falsum());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }
}