/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2024 Vavr, https://vavr.io
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

import io.vavr.CheckedFunction2;
import java.lang.IllegalArgumentException;
import org.junit.jupiter.api.Test;

public class PropertyCheck2Test {

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity2() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity2() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> true;
        final Property.Property2<Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty2() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty2() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty2() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> predicate = (o1, o2) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty2ImplicationWithTruePrecondition() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> p1 = (o1, o2) -> true;
        final CheckedFunction2<Object, Object, Boolean> p2 = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty2ImplicationWithFalsePrecondition() {
        final Property.ForAll2<Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS);
        final CheckedFunction2<Object, Object, Boolean> p1 = (o1, o2) -> false;
        final CheckedFunction2<Object, Object, Boolean> p2 = (o1, o2) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test
    public void shouldThrowOnProperty2CheckGivenNegativeTries() {
        assertThrows(IllegalArgumentException.class, () -> Property.def("test")
          .forAll(OBJECTS, OBJECTS)
          .suchThat((o1, o2) -> true)
          .check(Checkable.RNG.get(), 0, -1));
    }

    @Test
    public void shouldReturnErroneousProperty2CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test")
            .forAll(failingGen, OBJECTS)
            .suchThat((o1, o2) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty2CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test")
            .forAll(failingArbitrary, OBJECTS)
            .suchThat((o1, o2) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}
