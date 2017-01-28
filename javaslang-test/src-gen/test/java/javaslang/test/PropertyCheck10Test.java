/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.CheckedFunction10;
import org.junit.Test;

public class PropertyCheck10Test {

    static final Arbitrary<Object> OBJECTS = Gen.of(null).arbitrary();

    @Test
    public void shouldApplyForAllOfArity10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(null, null, null, null, null, null, null, null, null, null);
        assertThat(forAll).isNotNull();
    }

    @Test
    public void shouldApplySuchThatOfArity10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final Property.Property10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> suchThat = forAll.suchThat(predicate);
        assertThat(suchThat).isNotNull();
    }

    @Test
    public void shouldCheckTrueProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckFalseProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> false;
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isFalsified()).isTrue();
    }

    @Test
    public void shouldCheckErroneousProperty10() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> predicate = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = forAll.suchThat(predicate).check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldCheckProperty10ImplicationWithTruePrecondition() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isFalse();
    }

    @Test
    public void shouldCheckProperty10ImplicationWithFalsePrecondition() {
        final Property.ForAll10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> forAll = Property.def("test").forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS);
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p1 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> false;
        final CheckedFunction10<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Boolean> p2 = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true;
        final CheckResult result = forAll.suchThat(p1).implies(p2).check();
        assertThat(result.isSatisfied()).isTrue();
        assertThat(result.isExhausted()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnProperty10CheckGivenNegativeTries() {
        Property.def("test")
            .forAll(OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check(Checkable.RNG.get(), 0, -1);
    }

    @Test
    public void shouldReturnErroneousProperty10CheckResultIfGenFails() {
        final Arbitrary<Object> failingGen = Gen.fail("yay! (this is a negative test)").arbitrary();
        final CheckResult result = Property.def("test")
            .forAll(failingGen, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }

    @Test
    public void shouldReturnErroneousProperty10CheckResultIfArbitraryFails() {
        final Arbitrary<Object> failingArbitrary = size -> { throw new RuntimeException("yay! (this is a negative test)"); };
        final CheckResult result = Property.def("test")
            .forAll(failingArbitrary, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS, OBJECTS)
            .suchThat((o1, o2, o3, o4, o5, o6, o7, o8, o9, o10) -> true)
            .check();
        assertThat(result.isErroneous()).isTrue();
    }
}