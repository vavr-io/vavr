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
import javaslang.function.*;

public interface Property {

    CheckResult check(int size, int tries);

    default CheckResult check() {
        return check(100, 1000);
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

        public <T2> ForAll2<T1, T2> forAll(Arbitrary<T2> a2) {
            return new ForAll2<>(a1, a2);
        }

        public <T2, T3> ForAll3<T1, T2, T3> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3) {
            return new ForAll3<>(a1, a2, a3);
        }

        public <T2, T3, T4> ForAll4<T1, T2, T3, T4> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
            return new ForAll4<>(a1, a2, a3, a4);
        }

        public <T2, T3, T4, T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
            return new ForAll5<>(a1, a2, a3, a4, a5);
        }

        public <T2, T3, T4, T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
            return new ForAll6<>(a1, a2, a3, a4, a5, a6);
        }

        public <T2, T3, T4, T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T2, T3, T4, T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda1<T1, Boolean> predicate) {
            return new SuchThat1<>(a1, predicate);
        }
    }

    static class ForAll2<T1, T2> {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;

        ForAll2(Arbitrary<T1> a1, Arbitrary<T2> a2) {
            this.a1 = a1;
            this.a2 = a2;
        }

        public <T3> ForAll3<T1, T2, T3> forAll(Arbitrary<T3> a3) {
            return new ForAll3<>(a1, a2, a3);
        }

        public <T3, T4> ForAll4<T1, T2, T3, T4> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4) {
            return new ForAll4<>(a1, a2, a3, a4);
        }

        public <T3, T4, T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
            return new ForAll5<>(a1, a2, a3, a4, a5);
        }

        public <T3, T4, T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
            return new ForAll6<>(a1, a2, a3, a4, a5, a6);
        }

        public <T3, T4, T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T3, T4, T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T3, T4, T5, T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda2<T1, T2, Boolean> predicate) {
            return new SuchThat2<>(a1, a2, predicate);
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

        public <T4> ForAll4<T1, T2, T3, T4> forAll(Arbitrary<T4> a4) {
            return new ForAll4<>(a1, a2, a3, a4);
        }

        public <T4, T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5) {
            return new ForAll5<>(a1, a2, a3, a4, a5);
        }

        public <T4, T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
            return new ForAll6<>(a1, a2, a3, a4, a5, a6);
        }

        public <T4, T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T4, T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T4, T5, T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T4, T5, T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda3<T1, T2, T3, Boolean> predicate) {
            return new SuchThat3<>(a1, a2, a3, predicate);
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

        public <T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T5> a5) {
            return new ForAll5<>(a1, a2, a3, a4, a5);
        }

        public <T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6) {
            return new ForAll6<>(a1, a2, a3, a4, a5, a6);
        }

        public <T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T5, T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T5, T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T5, T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda4<T1, T2, T3, T4, Boolean> predicate) {
            return new SuchThat4<>(a1, a2, a3, a4, predicate);
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

        public <T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T6> a6) {
            return new ForAll6<>(a1, a2, a3, a4, a5, a6);
        }

        public <T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T6, T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T6, T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T6, T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T6, T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda5<T1, T2, T3, T4, T5, Boolean> predicate) {
            return new SuchThat5<>(a1, a2, a3, a4, a5, predicate);
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

        public <T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T7> a7) {
            return new ForAll7<>(a1, a2, a3, a4, a5, a6, a7);
        }

        public <T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T7, T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T7, T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T7, T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T7, T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T7, T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            return new SuchThat6<>(a1, a2, a3, a4, a5, a6, predicate);
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

        public <T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T8> a8) {
            return new ForAll8<>(a1, a2, a3, a4, a5, a6, a7, a8);
        }

        public <T8, T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T8, T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T8, T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T8, T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T8, T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T8, T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            return new SuchThat7<>(a1, a2, a3, a4, a5, a6, a7, predicate);
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

        public <T9> ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> forAll(Arbitrary<T9> a9) {
            return new ForAll9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9);
        }

        public <T9, T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T9, T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T9, T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T9, T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T9, T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T9, T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
            return new SuchThat8<>(a1, a2, a3, a4, a5, a6, a7, a8, predicate);
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

        public <T10> ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> forAll(Arbitrary<T10> a10) {
            return new ForAll10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10);
        }

        public <T10, T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T10, T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T10, T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T10, T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T10, T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T10, T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
            return new SuchThat9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, predicate);
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

        public <T11> ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> forAll(Arbitrary<T11> a11) {
            return new ForAll11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11);
        }

        public <T11, T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T11, T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T11, T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T11, T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T11, T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T11, T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
            return new SuchThat10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, predicate);
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

        public <T12> ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> forAll(Arbitrary<T12> a12) {
            return new ForAll12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12);
        }

        public <T12, T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T12, T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T12, T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T12, T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T12, T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T12, T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
            return new SuchThat11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, predicate);
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

        public <T13> ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> forAll(Arbitrary<T13> a13) {
            return new ForAll13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13);
        }

        public <T13, T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T13, T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T13, T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T13, T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T13, T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T13, T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
            return new SuchThat12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, predicate);
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

        public <T14> ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> forAll(Arbitrary<T14> a14) {
            return new ForAll14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14);
        }

        public <T14, T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T14, T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T14, T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T14, T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T14, T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T14, T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
            return new SuchThat13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, predicate);
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

        public <T15> ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> forAll(Arbitrary<T15> a15) {
            return new ForAll15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15);
        }

        public <T15, T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T15, T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T15, T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T15, T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T15, T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T15, T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T15, T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T15, T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate) {
            return new SuchThat14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, predicate);
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

        public <T16> ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> forAll(Arbitrary<T16> a16) {
            return new ForAll16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16);
        }

        public <T16, T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T16, T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T16, T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T16, T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T16, T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T16, T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T16, T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T16, T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate) {
            return new SuchThat15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, predicate);
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

        public <T17> ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> forAll(Arbitrary<T17> a17) {
            return new ForAll17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17);
        }

        public <T17, T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T17, T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T17, T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T17, T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T17, T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T17, T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T17, T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T17, T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate) {
            return new SuchThat16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, predicate);
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

        public <T18> ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> forAll(Arbitrary<T18> a18) {
            return new ForAll18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18);
        }

        public <T18, T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T18, T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T18, T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T18, T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T18, T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T18, T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T18, T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T18, T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate) {
            return new SuchThat17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, predicate);
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

        public <T19> ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> forAll(Arbitrary<T19> a19) {
            return new ForAll19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19);
        }

        public <T19, T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T19, T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T19, T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T19, T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T19, T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T19, T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T19, T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate) {
            return new SuchThat18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, predicate);
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

        public <T20> ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> forAll(Arbitrary<T20> a20) {
            return new ForAll20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20);
        }

        public <T20, T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T20, T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T20, T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T20, T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T20, T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T20, T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate) {
            return new SuchThat19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, predicate);
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

        public <T21> ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> forAll(Arbitrary<T21> a21) {
            return new ForAll21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21);
        }

        public <T21, T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T21> a21, Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T21, T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T21, T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T21, T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T21, T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate) {
            return new SuchThat20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, predicate);
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

        public <T22> ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> forAll(Arbitrary<T22> a22) {
            return new ForAll22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22);
        }

        public <T22, T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T22> a22, Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T22, T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T22, T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T22, T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate) {
            return new SuchThat21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, predicate);
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

        public <T23> ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> forAll(Arbitrary<T23> a23) {
            return new ForAll23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23);
        }

        public <T23, T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T23> a23, Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T23, T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T23, T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate) {
            return new SuchThat22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, predicate);
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

        public <T24> ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> forAll(Arbitrary<T24> a24) {
            return new ForAll24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24);
        }

        public <T24, T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T24> a24, Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T24, T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate) {
            return new SuchThat23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, predicate);
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

        public <T25> ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> forAll(Arbitrary<T25> a25) {
            return new ForAll25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25);
        }

        public <T25, T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T25> a25, Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate) {
            return new SuchThat24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, predicate);
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

        public <T26> ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> forAll(Arbitrary<T26> a26) {
            return new ForAll26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26);
        }

        public Property suchThat(CheckedLambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate) {
            return new SuchThat25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, predicate);
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

        public Property suchThat(CheckedLambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate) {
            return new SuchThat26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, predicate);
        }
    }

    static class SuchThat1<T1> implements Property {

        private final Arbitrary<T1> a1;
        final CheckedLambda1<T1, Boolean> predicate;

        SuchThat1(Arbitrary<T1> a1, CheckedLambda1<T1, Boolean> predicate) {
            this.a1 = a1;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple1<T1>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple1<T1>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).map((Gen<T1> gen1) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple1<T1>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).map((T1 val1) -> {
                                try {
                                    final boolean test = predicate.apply(val1);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            });
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                });
            return overallCheckResult.recover(x -> CheckResult.<Tuple1<T1>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat2<T1, T2> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        final CheckedLambda2<T1, T2, Boolean> predicate;

        SuchThat2(Arbitrary<T1> a1, Arbitrary<T2> a2, CheckedLambda2<T1, T2, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple2<T1, T2>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple2<T1, T2>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).map((Gen<T2> gen2) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple2<T1, T2>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).map((T2 val2) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }));
            return overallCheckResult.recover(x -> CheckResult.<Tuple2<T1, T2>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat3<T1, T2, T3> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        final CheckedLambda3<T1, T2, T3, Boolean> predicate;

        SuchThat3(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, CheckedLambda3<T1, T2, T3, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple3<T1, T2, T3>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple3<T1, T2, T3>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).map((Gen<T3> gen3) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple3<T1, T2, T3>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).map((T3 val3) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })));
            return overallCheckResult.recover(x -> CheckResult.<Tuple3<T1, T2, T3>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat4<T1, T2, T3, T4> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        final CheckedLambda4<T1, T2, T3, T4, Boolean> predicate;

        SuchThat4(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, CheckedLambda4<T1, T2, T3, T4, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple4<T1, T2, T3, T4>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple4<T1, T2, T3, T4>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).map((Gen<T4> gen4) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple4<T1, T2, T3, T4>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).map((T4 val4) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple4<T1, T2, T3, T4>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat5<T1, T2, T3, T4, T5> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        final CheckedLambda5<T1, T2, T3, T4, T5, Boolean> predicate;

        SuchThat5(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, CheckedLambda5<T1, T2, T3, T4, T5, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple5<T1, T2, T3, T4, T5>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple5<T1, T2, T3, T4, T5>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).map((Gen<T5> gen5) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple5<T1, T2, T3, T4, T5>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).map((T5 val5) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple5<T1, T2, T3, T4, T5>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat6<T1, T2, T3, T4, T5, T6> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        final CheckedLambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate;

        SuchThat6(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, CheckedLambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple6<T1, T2, T3, T4, T5, T6>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple6<T1, T2, T3, T4, T5, T6>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).map((Gen<T6> gen6) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple6<T1, T2, T3, T4, T5, T6>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).map((T6 val6) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple6<T1, T2, T3, T4, T5, T6>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat7<T1, T2, T3, T4, T5, T6, T7> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        final CheckedLambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate;

        SuchThat7(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, CheckedLambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.predicate = predicate;
        }

        @Override
        public CheckResult<Tuple7<T1, T2, T3, T4, T5, T6, T7>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple7<T1, T2, T3, T4, T5, T6, T7>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).map((Gen<T7> gen7) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple7<T1, T2, T3, T4, T5, T6, T7>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).map((T7 val7) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple7<T1, T2, T3, T4, T5, T6, T7>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat8<T1, T2, T3, T4, T5, T6, T7, T8> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        final CheckedLambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate;

        SuchThat8(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, CheckedLambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).map((Gen<T8> gen8) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).map((T8 val8) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Property {

        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final Arbitrary<T9> a9;
        final CheckedLambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate;

        SuchThat9(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, CheckedLambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).map((Gen<T9> gen9) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).map((T9 val9) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Property {

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
        final CheckedLambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate;

        SuchThat10(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, CheckedLambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).map((Gen<T10> gen10) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).map((T10 val10) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> implements Property {

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
        final CheckedLambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate;

        SuchThat11(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, CheckedLambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).map((Gen<T11> gen11) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).map((T11 val11) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> implements Property {

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
        final CheckedLambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate;

        SuchThat12(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, CheckedLambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).map((Gen<T12> gen12) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).map((T12 val12) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> implements Property {

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
        final CheckedLambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate;

        SuchThat13(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, CheckedLambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).map((Gen<T13> gen13) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).map((T13 val13) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> implements Property {

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
        final CheckedLambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate;

        SuchThat14(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, CheckedLambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).map((Gen<T14> gen14) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).map((T14 val14) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> implements Property {

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
        final CheckedLambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate;

        SuchThat15(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, CheckedLambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).map((Gen<T15> gen15) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).map((T15 val15) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> implements Property {

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
        final CheckedLambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate;

        SuchThat16(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, CheckedLambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).map((Gen<T16> gen16) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).map((T16 val16) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> implements Property {

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
        final CheckedLambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate;

        SuchThat17(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, CheckedLambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).map((Gen<T17> gen17) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).map((T17 val17) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> implements Property {

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
        final CheckedLambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate;

        SuchThat18(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, CheckedLambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).map((Gen<T18> gen18) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).map((T18 val18) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> implements Property {

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
        final CheckedLambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate;

        SuchThat19(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, CheckedLambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).map((Gen<T19> gen19) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).map((T19 val19) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> implements Property {

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
        final CheckedLambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate;

        SuchThat20(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, CheckedLambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).map((Gen<T20> gen20) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).map((T20 val20) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> implements Property {

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
        final CheckedLambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate;

        SuchThat21(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, CheckedLambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).map((Gen<T21> gen21) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).map((T21 val21) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> implements Property {

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
        final CheckedLambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate;

        SuchThat22(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, CheckedLambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).flatMap((Gen<T21> gen21) ->
                Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).map((Gen<T22> gen22) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).flatMap((T21 val21) ->
                            Try.of(() -> gen22.get()).recover(x -> { throw Errors.genError(22, size, x); }).map((T22 val22) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> implements Property {

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
        final CheckedLambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate;

        SuchThat23(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, CheckedLambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).flatMap((Gen<T21> gen21) ->
                Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).flatMap((Gen<T22> gen22) ->
                Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).map((Gen<T23> gen23) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).flatMap((T21 val21) ->
                            Try.of(() -> gen22.get()).recover(x -> { throw Errors.genError(22, size, x); }).flatMap((T22 val22) ->
                            Try.of(() -> gen23.get()).recover(x -> { throw Errors.genError(23, size, x); }).map((T23 val23) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> implements Property {

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
        final CheckedLambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate;

        SuchThat24(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, CheckedLambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).flatMap((Gen<T21> gen21) ->
                Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).flatMap((Gen<T22> gen22) ->
                Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).flatMap((Gen<T23> gen23) ->
                Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).map((Gen<T24> gen24) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).flatMap((T21 val21) ->
                            Try.of(() -> gen22.get()).recover(x -> { throw Errors.genError(22, size, x); }).flatMap((T22 val22) ->
                            Try.of(() -> gen23.get()).recover(x -> { throw Errors.genError(23, size, x); }).flatMap((T23 val23) ->
                            Try.of(() -> gen24.get()).recover(x -> { throw Errors.genError(24, size, x); }).map((T24 val24) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> implements Property {

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
        final CheckedLambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate;

        SuchThat25(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, CheckedLambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).flatMap((Gen<T21> gen21) ->
                Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).flatMap((Gen<T22> gen22) ->
                Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).flatMap((Gen<T23> gen23) ->
                Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).flatMap((Gen<T24> gen24) ->
                Try.of(() -> a25.apply(size)).recover(x -> { throw Errors.arbitraryError(25, size, x); }).map((Gen<T25> gen25) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).flatMap((T21 val21) ->
                            Try.of(() -> gen22.get()).recover(x -> { throw Errors.genError(22, size, x); }).flatMap((T22 val22) ->
                            Try.of(() -> gen23.get()).recover(x -> { throw Errors.genError(23, size, x); }).flatMap((T23 val23) ->
                            Try.of(() -> gen24.get()).recover(x -> { throw Errors.genError(24, size, x); }).flatMap((T24 val24) ->
                            Try.of(() -> gen25.get()).recover(x -> { throw Errors.genError(25, size, x); }).map((T25 val25) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            })))))))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                })))))))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25>>erroneous(0, (Error) x)).get();
            */
        }
    }

    static class SuchThat26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> implements Property {

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
        final CheckedLambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate;

        SuchThat26(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26, CheckedLambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate) {
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

        @Override
        public CheckResult<Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>> check(int size, int tries) {
            return null; // TODO
            /*
            final Try<CheckResult<Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>>> overallCheckResult =
                Try.of(() -> a1.apply(size)).recover(x -> { throw Errors.arbitraryError(1, size, x); }).flatMap((Gen<T1> gen1) ->
                Try.of(() -> a2.apply(size)).recover(x -> { throw Errors.arbitraryError(2, size, x); }).flatMap((Gen<T2> gen2) ->
                Try.of(() -> a3.apply(size)).recover(x -> { throw Errors.arbitraryError(3, size, x); }).flatMap((Gen<T3> gen3) ->
                Try.of(() -> a4.apply(size)).recover(x -> { throw Errors.arbitraryError(4, size, x); }).flatMap((Gen<T4> gen4) ->
                Try.of(() -> a5.apply(size)).recover(x -> { throw Errors.arbitraryError(5, size, x); }).flatMap((Gen<T5> gen5) ->
                Try.of(() -> a6.apply(size)).recover(x -> { throw Errors.arbitraryError(6, size, x); }).flatMap((Gen<T6> gen6) ->
                Try.of(() -> a7.apply(size)).recover(x -> { throw Errors.arbitraryError(7, size, x); }).flatMap((Gen<T7> gen7) ->
                Try.of(() -> a8.apply(size)).recover(x -> { throw Errors.arbitraryError(8, size, x); }).flatMap((Gen<T8> gen8) ->
                Try.of(() -> a9.apply(size)).recover(x -> { throw Errors.arbitraryError(9, size, x); }).flatMap((Gen<T9> gen9) ->
                Try.of(() -> a10.apply(size)).recover(x -> { throw Errors.arbitraryError(10, size, x); }).flatMap((Gen<T10> gen10) ->
                Try.of(() -> a11.apply(size)).recover(x -> { throw Errors.arbitraryError(11, size, x); }).flatMap((Gen<T11> gen11) ->
                Try.of(() -> a12.apply(size)).recover(x -> { throw Errors.arbitraryError(12, size, x); }).flatMap((Gen<T12> gen12) ->
                Try.of(() -> a13.apply(size)).recover(x -> { throw Errors.arbitraryError(13, size, x); }).flatMap((Gen<T13> gen13) ->
                Try.of(() -> a14.apply(size)).recover(x -> { throw Errors.arbitraryError(14, size, x); }).flatMap((Gen<T14> gen14) ->
                Try.of(() -> a15.apply(size)).recover(x -> { throw Errors.arbitraryError(15, size, x); }).flatMap((Gen<T15> gen15) ->
                Try.of(() -> a16.apply(size)).recover(x -> { throw Errors.arbitraryError(16, size, x); }).flatMap((Gen<T16> gen16) ->
                Try.of(() -> a17.apply(size)).recover(x -> { throw Errors.arbitraryError(17, size, x); }).flatMap((Gen<T17> gen17) ->
                Try.of(() -> a18.apply(size)).recover(x -> { throw Errors.arbitraryError(18, size, x); }).flatMap((Gen<T18> gen18) ->
                Try.of(() -> a19.apply(size)).recover(x -> { throw Errors.arbitraryError(19, size, x); }).flatMap((Gen<T19> gen19) ->
                Try.of(() -> a20.apply(size)).recover(x -> { throw Errors.arbitraryError(20, size, x); }).flatMap((Gen<T20> gen20) ->
                Try.of(() -> a21.apply(size)).recover(x -> { throw Errors.arbitraryError(21, size, x); }).flatMap((Gen<T21> gen21) ->
                Try.of(() -> a22.apply(size)).recover(x -> { throw Errors.arbitraryError(22, size, x); }).flatMap((Gen<T22> gen22) ->
                Try.of(() -> a23.apply(size)).recover(x -> { throw Errors.arbitraryError(23, size, x); }).flatMap((Gen<T23> gen23) ->
                Try.of(() -> a24.apply(size)).recover(x -> { throw Errors.arbitraryError(24, size, x); }).flatMap((Gen<T24> gen24) ->
                Try.of(() -> a25.apply(size)).recover(x -> { throw Errors.arbitraryError(25, size, x); }).flatMap((Gen<T25> gen25) ->
                Try.of(() -> a26.apply(size)).recover(x -> { throw Errors.arbitraryError(26, size, x); }).map((Gen<T26> gen26) -> {
                    for (int i = 1; i < tries; i++) {
                        final int count = i;
                        final Try<CheckResult<Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>>> partialCheckResult =
                            Try.of(() -> gen1.get()).recover(x -> { throw Errors.genError(1, size, x); }).flatMap((T1 val1) ->
                            Try.of(() -> gen2.get()).recover(x -> { throw Errors.genError(2, size, x); }).flatMap((T2 val2) ->
                            Try.of(() -> gen3.get()).recover(x -> { throw Errors.genError(3, size, x); }).flatMap((T3 val3) ->
                            Try.of(() -> gen4.get()).recover(x -> { throw Errors.genError(4, size, x); }).flatMap((T4 val4) ->
                            Try.of(() -> gen5.get()).recover(x -> { throw Errors.genError(5, size, x); }).flatMap((T5 val5) ->
                            Try.of(() -> gen6.get()).recover(x -> { throw Errors.genError(6, size, x); }).flatMap((T6 val6) ->
                            Try.of(() -> gen7.get()).recover(x -> { throw Errors.genError(7, size, x); }).flatMap((T7 val7) ->
                            Try.of(() -> gen8.get()).recover(x -> { throw Errors.genError(8, size, x); }).flatMap((T8 val8) ->
                            Try.of(() -> gen9.get()).recover(x -> { throw Errors.genError(9, size, x); }).flatMap((T9 val9) ->
                            Try.of(() -> gen10.get()).recover(x -> { throw Errors.genError(10, size, x); }).flatMap((T10 val10) ->
                            Try.of(() -> gen11.get()).recover(x -> { throw Errors.genError(11, size, x); }).flatMap((T11 val11) ->
                            Try.of(() -> gen12.get()).recover(x -> { throw Errors.genError(12, size, x); }).flatMap((T12 val12) ->
                            Try.of(() -> gen13.get()).recover(x -> { throw Errors.genError(13, size, x); }).flatMap((T13 val13) ->
                            Try.of(() -> gen14.get()).recover(x -> { throw Errors.genError(14, size, x); }).flatMap((T14 val14) ->
                            Try.of(() -> gen15.get()).recover(x -> { throw Errors.genError(15, size, x); }).flatMap((T15 val15) ->
                            Try.of(() -> gen16.get()).recover(x -> { throw Errors.genError(16, size, x); }).flatMap((T16 val16) ->
                            Try.of(() -> gen17.get()).recover(x -> { throw Errors.genError(17, size, x); }).flatMap((T17 val17) ->
                            Try.of(() -> gen18.get()).recover(x -> { throw Errors.genError(18, size, x); }).flatMap((T18 val18) ->
                            Try.of(() -> gen19.get()).recover(x -> { throw Errors.genError(19, size, x); }).flatMap((T19 val19) ->
                            Try.of(() -> gen20.get()).recover(x -> { throw Errors.genError(20, size, x); }).flatMap((T20 val20) ->
                            Try.of(() -> gen21.get()).recover(x -> { throw Errors.genError(21, size, x); }).flatMap((T21 val21) ->
                            Try.of(() -> gen22.get()).recover(x -> { throw Errors.genError(22, size, x); }).flatMap((T22 val22) ->
                            Try.of(() -> gen23.get()).recover(x -> { throw Errors.genError(23, size, x); }).flatMap((T23 val23) ->
                            Try.of(() -> gen24.get()).recover(x -> { throw Errors.genError(24, size, x); }).flatMap((T24 val24) ->
                            Try.of(() -> gen25.get()).recover(x -> { throw Errors.genError(25, size, x); }).flatMap((T25 val25) ->
                            Try.of(() -> gen26.get()).recover(x -> { throw Errors.genError(26, size, x); }).map((T26 val26) -> {
                                try {
                                    final boolean test = predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26);
                                    if (test) {
                                        return CheckResult.satisfied(count);
                                    } else {
                                        return CheckResult.falsified(count, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8, val9, val10, val11, val12, val13, val14, val15, val16, val17, val18, val19, val20, val21, val22, val23, val24, val25, val26));
                                    }
                                } catch (Throwable x) {
                                    return CheckResult.erroneous(count, Errors.predicateError(x));
                                }
                            }))))))))))))))))))))))))));
                        if (!partialCheckResult.get().isSatisfied()) {
                            return partialCheckResult.get();
                        }
                    }
                    return CheckResult.satisfied(size);
                }))))))))))))))))))))))))));
            return overallCheckResult.recover(x -> CheckResult.<Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26>>erroneous(0, (Error) x)).get();
            */
        }
    }
}