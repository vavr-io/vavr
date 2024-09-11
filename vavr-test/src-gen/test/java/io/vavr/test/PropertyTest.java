/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vavr.CheckedFunction1;
import io.vavr.Tuple;
import io.vavr.collection.List;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class PropertyTest {

    static <T> CheckedFunction1<T, Boolean> tautology() {
        return any -> true;
    }

    static <T> CheckedFunction1<T, Boolean> falsum() {
        return any -> false;
    }

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldThrowWhenPropertyNameIsNull() {
        assertThrows(NullPointerException.class, () -> Property.def(null));
    }

    @Test
    public void shouldThrowWhenPropertyNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> Property.def(""));
    }

    // -- Property.check methods

    @Test
    public void shouldCheckUsingDefaultConfiguration() {
        final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckGivenSizeAndTries() {
        final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test
    public void shouldThrowOnCheckGivenNegativeTries() {
        assertThrows(IllegalArgumentException.class, () -> Property.def("test")
          .forAll(OBJECTS)
          .suchThat(tautology())
          .check(0, -1));
    }

    @Test
    public void shouldCheckGivenRandomAndSizeAndTries() {
        final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(tautology()).check(new Random(), 0, 0);
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- satisfaction

    @Test
    public void shouldCheckPythagoras() {

        final Arbitrary<Double> real = n -> Gen.choose(0, (double) n).filter(d -> d > .0d);

        // (∀a,b ∈ ℝ+ ∃c ∈ ℝ+ : a²+b²=c²) ≡ (∀a,b ∈ ℝ+ : √(a²+b²) ∈ ℝ+)
        final Checkable property = Property.def("test").forAll(real, real).suchThat((a, b) -> Math.sqrt(a * a + b * b) > .0d);
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
        final CheckResult result = Property.def("test")
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
        final CheckResult result = Property.def("test").forAll(OBJECTS).suchThat(falsum()).implies(tautology()).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    // -- falsification

    @Test
    public void shouldFalsifyFalseProperty() {
        final Arbitrary<Integer> ones = n -> random -> 1;
        final CheckResult result = Property.def("test").forAll(ones).suchThat(one -> one == 2).check();
        assertThat(result.isFalsified()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
    }

    // -- error detection

    @Test
    public void shouldRecognizeArbitraryError() {
        final Arbitrary<?> arbitrary = n -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(0);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizeGenError() {
        final Arbitrary<?> arbitrary = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test").forAll(arbitrary).suchThat(tautology()).check();
        assertThat(result.isErroneous()).isTrue();
        assertThat(result.isExhausted()).isFalse();
        assertThat(result.count()).isEqualTo(1);
        assertThat(result.sample().isEmpty()).isTrue();
    }

    @Test
    public void shouldRecognizePropertyError() {
        final Arbitrary<Integer> a1 = n -> random -> 1;
        final Arbitrary<Integer> a2 = n -> random -> 2;
        final CheckResult result = Property.def("test").forAll(a1, a2).suchThat((a, b) -> {
            throw new RuntimeException("yay! (this is a negative test)");
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
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    @Test
    public void shouldCheckAndCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult result = p1.and(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }

    // -- Property.or tests

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsTrue() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsTrueAndSecondPropertyIsFalse() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsTrue() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(tautology());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isTrue();
    }

    @Test
    public void shouldCheckOrCombinationWhereFirstPropertyIsFalseAndSecondPropertyIsFalse() {
        final Checkable p1 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final Checkable p2 = Property.def("test").forAll(OBJECTS).suchThat(falsum());
        final CheckResult result = p1.or(p2).check();
        assertThat(result.isSatisfied()).isFalse();
    }
}