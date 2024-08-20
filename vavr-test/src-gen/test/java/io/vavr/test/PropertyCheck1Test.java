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

import io.vavr.CheckedFunction1;
import org.junit.Test;

public class PropertyCheck1Test {

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity1() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity1() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> true;
        final Property.Property1<Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty1() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty1() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty1() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> predicate = (o1) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty1ImplicationWithTruePrecondition() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> p1 = (o1) -> true;
        final CheckedFunction1<Object, Boolean> p2 = (o1) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty1ImplicationWithFalsePrecondition() {
        final Property.ForAll1<Object> forAll = Property.def("test").forAll(OBJECTS);
        final CheckedFunction1<Object, Boolean> p1 = (o1) -> false;
        final CheckedFunction1<Object, Boolean> p2 = (o1) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty1CheckGivenNegativeTries() {
        Property.def("test")
            .forAll(OBJECTS)
            .suchThat((o1) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty1CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test")
            .forAll(failingGen)
            .suchThat((o1) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty1CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test")
            .forAll(failingArbitrary)
            .suchThat((o1) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}