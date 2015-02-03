/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.*;
import javaslang.control.*;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@FunctionalInterface
public interface Property {

    /**
     * A thread-safe, equally distributed random number generator.
     */
    Supplier<Random> RNG = ThreadLocalRandom::current;

    /**
     * Default size hint for generators.
     */
    int DEFAULT_SIZE = 100;

    /**
     * Default tries to check a property.
     */
    int DEFAULT_TRIES = 1000;

    CheckResult check(Random randomNumberGenerator, int size, int tries);

    default CheckResult check(int size, int tries) {
        return check(RNG.get(), size, tries);
    }

    default CheckResult check() {
        return check(RNG.get(), DEFAULT_SIZE, DEFAULT_TRIES);
    }

    default Property and(Property property) {
        return (rng, size, tries) -> {
            final CheckResult result = check(rng, size, tries);
            if (result.isSatisfied()) {
                return property.check(rng, size, tries);
            } else {
                return result;
            }
        };
    }

    default Property or(Property property) {
        return (rng, size, tries) -> {
            final CheckResult result = check(rng, size, tries);
            if (result.isSatisfied()) {
                return result;
            } else {
                return property.check(rng, size, tries);
            }
        };
    }

    static <T1> ForAll1<T1> forAll(Arbitrary<T1> a1) {
        return new ForAll1<>(a1);
    }

    static <T1, T2> ForAll2<T1, T2> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return new ForAll2<>(a1, a2);
    }

    static <T1, T2, T3> ForAll3<T1, T2, T3> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3) {
        return new ForAll3<>(a1, a2, a3);
    }

    static <T1, T2, T3, T4> ForAll4<T1, T2, T3, T4> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
        return new ForAll4<>(a1, a2, a3, a4);
    }

    static <T1, T2, T3, T4, T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
        return new ForAll5<>(a1, a2, a3, a4, a5);
    }

    static <T1, T2, T3, T4, T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
        return new ForAll6<>(a1, a2, a3, a4, a5, a6);
    }

    static <T1, T2, T3, T4, T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
        return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
        return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
        return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
        return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
        return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
        return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
        return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
        return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
        return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
        return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
        return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
        return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
        return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
        return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
        return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
        return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
        return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
        return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
        return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
        return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
    }

    static class ForAll1<T1> {

        private final Arbitrary<T1> a1;

        ForAll1(Arbitrary<T1> a1) {
            this.a1 = a1;
        }

        public Property1<T1> suchThat(CheckedFunction1<T1, Boolean> predicate) {
            final CheckedFunction1<T1, Condition> proposition = (t1) -> new Condition(true, predicate.apply(t1));
            return new Property1<>(a1, proposition);
        }
    }

    static class ForAll2<T1, T2> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;

        ForAll2(Arbitrary<T1> a1, Arbitrary<T2> a2) {
            this.a1 = a1;
            this.a2 = a2;
        }

        public Property2<T1, T2> suchThat(CheckedFunction2<T1, T2, Boolean> predicate) {
            final CheckedFunction2<T1, T2, Condition> proposition = (t1, t2) -> new Condition(true, predicate.apply(t1, t2));
            return new Property2<>(a1, a2, proposition);
        }
    }

    static class ForAll3<T1, T2, T3> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;

        ForAll3(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
        }

        public Property3<T1, T2, T3> suchThat(CheckedFunction3<T1, T2, T3, Boolean> predicate) {
            final CheckedFunction3<T1, T2, T3, Condition> proposition = (t1, t2, t3) -> new Condition(true, predicate.apply(t1, t2, t3));
            return new Property3<>(a1, a2, a3, proposition);
        }
    }

    static class ForAll4<T1, T2, T3, T4> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;

        ForAll4(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
        }

        public Property4<T1, T2, T3, T4> suchThat(CheckedFunction4<T1, T2, T3, T4, Boolean> predicate) {
            final CheckedFunction4<T1, T2, T3, T4, Condition> proposition = (t1, t2, t3, t4) -> new Condition(true, predicate.apply(t1, t2, t3, t4));
            return new Property4<>(a1, a2, a3, a4, proposition);
        }
    }

    static class ForAll5<T1, T2, T3, T4, T5> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;

        ForAll5(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
        }

        public Property5<T1, T2, T3, T4, T5> suchThat(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> predicate) {
            final CheckedFunction5<T1, T2, T3, T4, T5, Condition> proposition = (t1, t2, t3, t4, t5) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5));
            return new Property5<>(a1, a2, a3, a4, a5, proposition);
        }
    }

    static class ForAll6<T1, T2, T3, T4, T5, T6> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;

        ForAll6(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
        }

        public Property6<T1, T2, T3, T4, T5, T6> suchThat(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> proposition = (t1, t2, t3, t4, t5, t6) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6));
            return new Property6<>(a1, a2, a3, a4, a5, a6, proposition);
        }
    }

    static class ForAll7<T1, T2, T3, T4, T5, T6, T7> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;

        ForAll7(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
        }

        public Property7<T1, T2, T3, T4, T5, T6, T7> suchThat(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7));
            return new Property7<>(a1, a2, a3, a4, a5, a6, a7, proposition);
        }
    }

    static class ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;

        ForAll8(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
        }

        public Property8<T1, T2, T3, T4, T5, T6, T7, T8> suchThat(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
            final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8));
            return new Property8<>(a1, a2, a3, a4, a5, a6, a7, a8, proposition);
        }
    }

    static class ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;

        ForAll9(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
        }

        public Property9<T1, T2, T3, T4, T5, T6, T7, T8, T9> suchThat(CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
            final CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
            return new Property9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, proposition);
        }
    }

    static class ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;

        ForAll10(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
        }

        public Property10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> suchThat(CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
            final CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
            return new Property10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, proposition);
        }
    }

    static class ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;

        ForAll11(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
        }

        public Property11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> suchThat(CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
            final CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
            return new Property11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, proposition);
        }
    }

    static class ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;

        ForAll12(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
        }

        public Property12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> suchThat(CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
            final CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
            return new Property12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, proposition);
        }
    }

    static class ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;

        ForAll13(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
        }

        public Property13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> suchThat(CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
            final CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
            return new Property13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, proposition);
        }
    }

    static class ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;

        ForAll14(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
        }

        public Property14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> suchThat(CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate) {
            final CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
            return new Property14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, proposition);
        }
    }

    static class ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;

        ForAll15(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
        }

        public Property15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> suchThat(CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate) {
            final CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
            return new Property15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, proposition);
        }
    }

    static class ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;

        ForAll16(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
        }

        public Property16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> suchThat(CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate) {
            final CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
            return new Property16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, proposition);
        }
    }

    static class ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;

        ForAll17(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
        }

        public Property17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> suchThat(CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate) {
            final CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
            return new Property17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, proposition);
        }
    }

    static class ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;

        ForAll18(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
        }

        public Property18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> suchThat(CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate) {
            final CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
            return new Property18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, proposition);
        }
    }

    static class ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;

        ForAll19(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
        }

        public Property19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> suchThat(CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate) {
            final CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
            return new Property19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, proposition);
        }
    }

    static class ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;

        ForAll20(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
        }

        public Property20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> suchThat(CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate) {
            final CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
            return new Property20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, proposition);
        }
    }

    static class ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;

        ForAll21(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
        }

        public Property21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> suchThat(CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate) {
            final CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
            return new Property21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, proposition);
        }
    }

    static class ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;

        ForAll22(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
        }

        public Property22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> suchThat(CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate) {
            final CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
            return new Property22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, proposition);
        }
    }

    static class ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;

        ForAll23(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
        }

        public Property23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> suchThat(CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate) {
            final CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23));
            return new Property23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, proposition);
        }
    }

    static class ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;

        ForAll24(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
        }

        public Property24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> suchThat(CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate) {
            final CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24));
            return new Property24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, proposition);
        }
    }

    static class ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;
        private final Arbitrary<T25> a25;

        ForAll25(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
            this.a25 = a25;
        }

        public Property25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> suchThat(CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate) {
            final CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25));
            return new Property25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, proposition);
        }
    }

    static class ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;
        private final Arbitrary<T25> a25;
        private final Arbitrary<T26> a26;

        ForAll26(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
            this.a25 = a25;
            this.a26 = a26;
        }

        public Property26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> suchThat(CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate) {
            final CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26));
            return new Property26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, proposition);
        }
    }

    static class Property1<T1> implements Property {

        private final Arbitrary<T1> a1;
        final CheckedFunction1<T1, Condition> predicate;

        Property1(Arbitrary<T1> a1, CheckedFunction1<T1, Condition> predicate) {
            this.a1 = a1;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction1<T1, Boolean> postcondition) {
            final CheckedFunction1<T1, Condition> implication = (t1) -> {
                final Condition precondition = predicate.apply(t1);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1));
                }
            };
            return new Property1<>(a1, implication);
        }

        @Override
        public CheckResult<Tuple1<T1>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property2<T1, T2> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        final CheckedFunction2<T1, T2, Condition> predicate;

        Property2(Arbitrary<T1> a1, Arbitrary<T2> a2, CheckedFunction2<T1, T2, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction2<T1, T2, Boolean> postcondition) {
            final CheckedFunction2<T1, T2, Condition> implication = (t1, t2) -> {
                final Condition precondition = predicate.apply(t1, t2);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2));
                }
            };
            return new Property2<>(a1, a2, implication);
        }

        @Override
        public CheckResult<Tuple2<T1, T2>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property3<T1, T2, T3> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        final CheckedFunction3<T1, T2, T3, Condition> predicate;

        Property3(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, CheckedFunction3<T1, T2, T3, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction3<T1, T2, T3, Boolean> postcondition) {
            final CheckedFunction3<T1, T2, T3, Condition> implication = (t1, t2, t3) -> {
                final Condition precondition = predicate.apply(t1, t2, t3);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3));
                }
            };
            return new Property3<>(a1, a2, a3, implication);
        }

        @Override
        public CheckResult<Tuple3<T1, T2, T3>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property4<T1, T2, T3, T4> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        final CheckedFunction4<T1, T2, T3, T4, Condition> predicate;

        Property4(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, CheckedFunction4<T1, T2, T3, T4, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction4<T1, T2, T3, T4, Boolean> postcondition) {
            final CheckedFunction4<T1, T2, T3, T4, Condition> implication = (t1, t2, t3, t4) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4));
                }
            };
            return new Property4<>(a1, a2, a3, a4, implication);
        }

        @Override
        public CheckResult<Tuple4<T1, T2, T3, T4>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property5<T1, T2, T3, T4, T5> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        final CheckedFunction5<T1, T2, T3, T4, T5, Condition> predicate;

        Property5(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, CheckedFunction5<T1, T2, T3, T4, T5, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> postcondition) {
            final CheckedFunction5<T1, T2, T3, T4, T5, Condition> implication = (t1, t2, t3, t4, t5) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5));
                }
            };
            return new Property5<>(a1, a2, a3, a4, a5, implication);
        }

        @Override
        public CheckResult<Tuple5<T1, T2, T3, T4, T5>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property6<T1, T2, T3, T4, T5, T6> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> predicate;

        Property6(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> postcondition) {
            final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> implication = (t1, t2, t3, t4, t5, t6) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6));
                }
            };
            return new Property6<>(a1, a2, a3, a4, a5, a6, implication);
        }

        @Override
        public CheckResult<Tuple6<T1, T2, T3, T4, T5, T6>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property7<T1, T2, T3, T4, T5, T6, T7> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> predicate;

        Property7(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> postcondition) {
            final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> implication = (t1, t2, t3, t4, t5, t6, t7) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7));
                }
            };
            return new Property7<>(a1, a2, a3, a4, a5, a6, a7, implication);
        }

        @Override
        public CheckResult<Tuple7<T1, T2, T3, T4, T5, T6, T7>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property8<T1, T2, T3, T4, T5, T6, T7, T8> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> predicate;

        Property8(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> postcondition) {
            final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8));
                }
            };
            return new Property8<>(a1, a2, a3, a4, a5, a6, a7, a8, implication);
        }

        @Override
        public CheckResult<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        final CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Condition> predicate;

        Property9(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> postcondition) {
            final CheckedFunction9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9));
                }
            };
            return new Property9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, implication);
        }

        @Override
        public CheckResult<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        final CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Condition> predicate;

        Property10(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> postcondition) {
            final CheckedFunction10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10));
                }
            };
            return new Property10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, implication);
        }

        @Override
        public CheckResult<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        final CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Condition> predicate;

        Property11(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> postcondition) {
            final CheckedFunction11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11));
                }
            };
            return new Property11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, implication);
        }

        @Override
        public CheckResult<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        final CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Condition> predicate;

        Property12(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> postcondition) {
            final CheckedFunction12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12));
                }
            };
            return new Property12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, implication);
        }

        @Override
        public CheckResult<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        final CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Condition> predicate;

        Property13(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> postcondition) {
            final CheckedFunction13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13));
                }
            };
            return new Property13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, implication);
        }

        @Override
        public CheckResult<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        final CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Condition> predicate;

        Property14(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> postcondition) {
            final CheckedFunction14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14));
                }
            };
            return new Property14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, implication);
        }

        @Override
        public CheckResult<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        final CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Condition> predicate;

        Property15(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> postcondition) {
            final CheckedFunction15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15));
                }
            };
            return new Property15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, implication);
        }

        @Override
        public CheckResult<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        final CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Condition> predicate;

        Property16(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> postcondition) {
            final CheckedFunction16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16));
                }
            };
            return new Property16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, implication);
        }

        @Override
        public CheckResult<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        final CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Condition> predicate;

        Property17(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> postcondition) {
            final CheckedFunction17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17));
                }
            };
            return new Property17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, implication);
        }

        @Override
        public CheckResult<Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        final CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Condition> predicate;

        Property18(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> postcondition) {
            final CheckedFunction18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18));
                }
            };
            return new Property18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, implication);
        }

        @Override
        public CheckResult<Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        final CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Condition> predicate;

        Property19(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> postcondition) {
            final CheckedFunction19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19));
                }
            };
            return new Property19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, implication);
        }

        @Override
        public CheckResult<Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        final CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Condition> predicate;

        Property20(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> postcondition) {
            final CheckedFunction20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20));
                }
            };
            return new Property20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, implication);
        }

        @Override
        public CheckResult<Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        final CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Condition> predicate;

        Property21(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> postcondition) {
            final CheckedFunction21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21));
                }
            };
            return new Property21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, implication);
        }

        @Override
        public CheckResult<Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        final CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Condition> predicate;

        Property22(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> postcondition) {
            final CheckedFunction22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22));
                }
            };
            return new Property22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, implication);
        }

        @Override
        public CheckResult<Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                final Gen<T22> gen22 = Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        final T22 val22 = Try.of(() -> gen22.apply(random)).recover(x -> { throw Errors.genError(22, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        final CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Condition> predicate;

        Property23(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> postcondition) {
            final CheckedFunction23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23));
                }
            };
            return new Property23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, implication);
        }

        @Override
        public CheckResult<Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                final Gen<T22> gen22 = Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).get();
                final Gen<T23> gen23 = Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        final T22 val22 = Try.of(() -> gen22.apply(random)).recover(x -> { throw Errors.genError(22, size, x); }).get();
                        final T23 val23 = Try.of(() -> gen23.apply(random)).recover(x -> { throw Errors.genError(23, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;
        final CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Condition> predicate;

        Property24(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> postcondition) {
            final CheckedFunction24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24));
                }
            };
            return new Property24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, implication);
        }

        @Override
        public CheckResult<Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                final Gen<T22> gen22 = Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).get();
                final Gen<T23> gen23 = Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).get();
                final Gen<T24> gen24 = Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        final T22 val22 = Try.of(() -> gen22.apply(random)).recover(x -> { throw Errors.genError(22, size, x); }).get();
                        final T23 val23 = Try.of(() -> gen23.apply(random)).recover(x -> { throw Errors.genError(23, size, x); }).get();
                        final T24 val24 = Try.of(() -> gen24.apply(random)).recover(x -> { throw Errors.genError(24, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;
        private final Arbitrary<T25> a25;
        final CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Condition> predicate;

        Property25(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
            this.a25 = a25;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> postcondition) {
            final CheckedFunction25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25));
                }
            };
            return new Property25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, implication);
        }

        @Override
        public CheckResult<Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                final Gen<T22> gen22 = Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).get();
                final Gen<T23> gen23 = Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).get();
                final Gen<T24> gen24 = Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).get();
                final Gen<T25> gen25 = Try.of(() -> a25.apply(size)).recover(x -> { throw Errors.arbitraryError(25, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        final T22 val22 = Try.of(() -> gen22.apply(random)).recover(x -> { throw Errors.genError(22, size, x); }).get();
                        final T23 val23 = Try.of(() -> gen23.apply(random)).recover(x -> { throw Errors.genError(23, size, x); }).get();
                        final T24 val24 = Try.of(() -> gen24.apply(random)).recover(x -> { throw Errors.genError(24, size, x); }).get();
                        final T25 val25 = Try.of(() -> gen25.apply(random)).recover(x -> { throw Errors.genError(25, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Property26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        private final Arbitrary<T10> a10;
        private final Arbitrary<T11> a11;
        private final Arbitrary<T12> a12;
        private final Arbitrary<T13> a13;
        private final Arbitrary<T14> a14;
        private final Arbitrary<T15> a15;
        private final Arbitrary<T16> a16;
        private final Arbitrary<T17> a17;
        private final Arbitrary<T18> a18;
        private final Arbitrary<T19> a19;
        private final Arbitrary<T20> a20;
        private final Arbitrary<T21> a21;
        private final Arbitrary<T22> a22;
        private final Arbitrary<T23> a23;
        private final Arbitrary<T24> a24;
        private final Arbitrary<T25> a25;
        private final Arbitrary<T26> a26;
        final CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Condition> predicate;

        Property26(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26, CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Condition> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
            this.a9 = a9;
            this.a10 = a10;
            this.a11 = a11;
            this.a12 = a12;
            this.a13 = a13;
            this.a14 = a14;
            this.a15 = a15;
            this.a16 = a16;
            this.a17 = a17;
            this.a18 = a18;
            this.a19 = a19;
            this.a20 = a20;
            this.a21 = a21;
            this.a22 = a22;
            this.a23 = a23;
            this.a24 = a24;
            this.a25 = a25;
            this.a26 = a26;
            this.predicate = predicate;
        }

        public Property implies(CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> postcondition) {
            final CheckedFunction26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) -> {
                final Condition precondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26);
                if (precondition.isFalse()) {
                    // ex falso quodlibet
                    return new Condition(false, true);
                } else {
                    return new Condition(true, postcondition.apply(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26));
                }
            };
            return new Property26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, implication);
        }

        @Override
        public CheckResult<Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>> check(Random random, int size, int tries) {
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).get();
                final Gen<T9> gen9 = Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).get();
                final Gen<T10> gen10 = Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).get();
                final Gen<T11> gen11 = Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).get();
                final Gen<T12> gen12 = Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).get();
                final Gen<T13> gen13 = Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).get();
                final Gen<T14> gen14 = Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).get();
                final Gen<T15> gen15 = Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).get();
                final Gen<T16> gen16 = Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).get();
                final Gen<T17> gen17 = Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).get();
                final Gen<T18> gen18 = Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).get();
                final Gen<T19> gen19 = Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).get();
                final Gen<T20> gen20 = Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).get();
                final Gen<T21> gen21 = Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).get();
                final Gen<T22> gen22 = Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).get();
                final Gen<T23> gen23 = Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).get();
                final Gen<T24> gen24 = Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).get();
                final Gen<T25> gen25 = Try.of(() -> a25.apply(size)).recover(x -> { throw Errors.arbitraryError(25, size, x); }).get();
                final Gen<T26> gen26 = Try.of(() -> a26.apply(size)).recover(x -> { throw Errors.arbitraryError(26, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw Errors.genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw Errors.genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw Errors.genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw Errors.genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw Errors.genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw Errors.genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw Errors.genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw Errors.genError(8, size, x); }).get();
                        final T9 val9 = Try.of(() -> gen9.apply(random)).recover(x -> { throw Errors.genError(9, size, x); }).get();
                        final T10 val10 = Try.of(() -> gen10.apply(random)).recover(x -> { throw Errors.genError(10, size, x); }).get();
                        final T11 val11 = Try.of(() -> gen11.apply(random)).recover(x -> { throw Errors.genError(11, size, x); }).get();
                        final T12 val12 = Try.of(() -> gen12.apply(random)).recover(x -> { throw Errors.genError(12, size, x); }).get();
                        final T13 val13 = Try.of(() -> gen13.apply(random)).recover(x -> { throw Errors.genError(13, size, x); }).get();
                        final T14 val14 = Try.of(() -> gen14.apply(random)).recover(x -> { throw Errors.genError(14, size, x); }).get();
                        final T15 val15 = Try.of(() -> gen15.apply(random)).recover(x -> { throw Errors.genError(15, size, x); }).get();
                        final T16 val16 = Try.of(() -> gen16.apply(random)).recover(x -> { throw Errors.genError(16, size, x); }).get();
                        final T17 val17 = Try.of(() -> gen17.apply(random)).recover(x -> { throw Errors.genError(17, size, x); }).get();
                        final T18 val18 = Try.of(() -> gen18.apply(random)).recover(x -> { throw Errors.genError(18, size, x); }).get();
                        final T19 val19 = Try.of(() -> gen19.apply(random)).recover(x -> { throw Errors.genError(19, size, x); }).get();
                        final T20 val20 = Try.of(() -> gen20.apply(random)).recover(x -> { throw Errors.genError(20, size, x); }).get();
                        final T21 val21 = Try.of(() -> gen21.apply(random)).recover(x -> { throw Errors.genError(21, size, x); }).get();
                        final T22 val22 = Try.of(() -> gen22.apply(random)).recover(x -> { throw Errors.genError(22, size, x); }).get();
                        final T23 val23 = Try.of(() -> gen23.apply(random)).recover(x -> { throw Errors.genError(23, size, x); }).get();
                        final T24 val24 = Try.of(() -> gen24.apply(random)).recover(x -> { throw Errors.genError(24, size, x); }).get();
                        final T25 val25 = Try.of(() -> gen25.apply(random)).recover(x -> { throw Errors.genError(25, size, x); }).get();
                        final T26 val26 = Try.of(() -> gen26.apply(random)).recover(x -> { throw Errors.genError(26, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26)).recover(x -> { throw Errors.predicateError(x); }).get();
                            if (condition.precondition) {
                                exhausted = false;
                                if (!condition.postcondition) {
                                    return CheckResult.falsified(i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26));
                                }
                            }
                        } catch(Failure.NonFatal nonFatal) {
                            return CheckResult.erroneous(i, (Error) nonFatal.getCause(), new Some<>(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26)));
                        }
                    } catch(Failure.NonFatal nonFatal) {
                        return CheckResult.erroneous(i, (Error) nonFatal.getCause(), None.instance());
                    }
                }
                return CheckResult.satisfied(tries, exhausted);
            } catch(Failure.NonFatal nonFatal) {
                return CheckResult.erroneous(0, (Error) nonFatal.getCause(), None.instance());
            }
        }
    }

    static class Condition {

        final boolean precondition;
        final boolean postcondition;

        Condition(boolean precondition, boolean postcondition) {
            this.precondition = precondition;
            this.postcondition = postcondition;
        }

        // ¬(p => q) ≡ ¬(¬p ∨ q) ≡ p ∧ ¬q
        boolean isFalse() {
            return precondition && !postcondition;
        }
    }
}