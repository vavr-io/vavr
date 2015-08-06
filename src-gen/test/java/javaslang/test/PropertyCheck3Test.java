/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.CheckedFunction3;
import org.junit.Test;

public class PropertyCheck3Test {

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity3() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity3() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> true;
        final Property.Property3<Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty3() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> predicate = (o1, o2, o3) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty3ImplicationWithTruePrecondition() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> p1 = (o1, o2, o3) -> true;
        final CheckedFunction3<Object, Object, Object, Boolean> p2 = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty3ImplicationWithFalsePrecondition() {
        final Property.ForAll3<Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction3<Object, Object, Object, Boolean> p1 = (o1, o2, o3) -> false;
        final CheckedFunction3<Object, Object, Object, Boolean> p2 = (o1, o2, o3) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty3CheckGivenNegativeTries() {
        Property.def("test")
            .forAll(OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty3CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test")
            .forAll(failingGen, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty3CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test")
            .forAll(failingArbitrary, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}