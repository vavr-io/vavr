/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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

import io.vavr.CheckedFunction5;
import org.junit.Test;

public class PropertyCheck5Test {

    private static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> true;
        final Property.Property5<Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> true;
        final CheckResult<?> result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> false;
        final CheckResult<?> result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty5() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult<?> result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty5ImplicationWithTruePrecondition() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5) -> true;
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5) -> true;
        final CheckResult<?> result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty5ImplicationWithFalsePrecondition() {
        final Property.ForAll5<Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5) -> false;
        final CheckedFunction5<Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5) -> true;
        final CheckResult<?> result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty5CheckGivenNegativeTries() {
        Property.def("test")
            .forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty5CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult<?> result = Property.def("test")
            .forAll(failingGen, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty5CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult<?> result = Property.def("test")
            .forAll(failingArbitrary, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}