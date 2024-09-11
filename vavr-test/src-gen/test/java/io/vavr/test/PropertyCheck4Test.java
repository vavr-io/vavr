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

import io.vavr.CheckedFunction4;
import java.lang.IllegalArgumentException;
import org.junit.jupiter.api.Test;

public class PropertyCheck4Test {

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> true;
        final Property.Property4<Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty4() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty4ImplicationWithTruePrecondition() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4) -> true;
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty4ImplicationWithFalsePrecondition() {
        final Property.ForAll4<Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4) -> false;
        final CheckedFunction4<Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test
    public void shouldThrowOnProperty4CheckGivenNegativeTries() {
        assertThrows(IllegalArgumentException.class, () -> Property.def("test")
          .forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS)
          .suchThat((o1, o2, o3, o4) -> true)
          .check(Checkable.RNG.get(), 0, -1));
    }

    @Test
    public void shouldReturnErroneousProperty4CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test")
            .forAll(failingGen, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty4CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test")
            .forAll(failingArbitrary, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}