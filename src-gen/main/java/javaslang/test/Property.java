/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

//
// *-- GENERATED FILE - DO NOT MODIFY --*
//

import javaslang.function.*;

public interface Property {

    boolean test(int n);

    default boolean test() {
        return test(100);
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

        final Arbitrary<T1> a1;

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

        public Property suchThat(Lambda1<T1, Boolean> predicate) {
            return new SuchThat1<>(a1, predicate);
        }
    }

    static class ForAll2<T1, T2> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;

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

        public Property suchThat(Lambda2<T1, T2, Boolean> predicate) {
            return new SuchThat2<>(a1, a2, predicate);
        }
    }

    static class ForAll3<T1, T2, T3> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;

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

        public Property suchThat(Lambda3<T1, T2, T3, Boolean> predicate) {
            return new SuchThat3<>(a1, a2, a3, predicate);
        }
    }

    static class ForAll4<T1, T2, T3, T4> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;

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

        public Property suchThat(Lambda4<T1, T2, T3, T4, Boolean> predicate) {
            return new SuchThat4<>(a1, a2, a3, a4, predicate);
        }
    }

    static class ForAll5<T1, T2, T3, T4, T5> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;

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

        public Property suchThat(Lambda5<T1, T2, T3, T4, T5, Boolean> predicate) {
            return new SuchThat5<>(a1, a2, a3, a4, a5, predicate);
        }
    }

    static class ForAll6<T1, T2, T3, T4, T5, T6> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;

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

        public Property suchThat(Lambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            return new SuchThat6<>(a1, a2, a3, a4, a5, a6, predicate);
        }
    }

    static class ForAll7<T1, T2, T3, T4, T5, T6, T7> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;

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

        public Property suchThat(Lambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            return new SuchThat7<>(a1, a2, a3, a4, a5, a6, a7, predicate);
        }
    }

    static class ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;

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

        public Property suchThat(Lambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
            return new SuchThat8<>(a1, a2, a3, a4, a5, a6, a7, a8, predicate);
        }
    }

    static class ForAll9<T1, T2, T3, T4, T5, T6, T7, T8, T9> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;

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

        public Property suchThat(Lambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
            return new SuchThat9<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, predicate);
        }
    }

    static class ForAll10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;

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

        public Property suchThat(Lambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
            return new SuchThat10<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, predicate);
        }
    }

    static class ForAll11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;

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

        public Property suchThat(Lambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
            return new SuchThat11<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, predicate);
        }
    }

    static class ForAll12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;

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

        public Property suchThat(Lambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
            return new SuchThat12<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, predicate);
        }
    }

    static class ForAll13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;

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

        public Property suchThat(Lambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
            return new SuchThat13<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, predicate);
        }
    }

    static class ForAll14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;

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

        public Property suchThat(Lambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate) {
            return new SuchThat14<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, predicate);
        }
    }

    static class ForAll15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;

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

        public Property suchThat(Lambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate) {
            return new SuchThat15<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, predicate);
        }
    }

    static class ForAll16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;

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

        public Property suchThat(Lambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate) {
            return new SuchThat16<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, predicate);
        }
    }

    static class ForAll17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;

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

        public Property suchThat(Lambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate) {
            return new SuchThat17<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, predicate);
        }
    }

    static class ForAll18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;

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

        public Property suchThat(Lambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate) {
            return new SuchThat18<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, predicate);
        }
    }

    static class ForAll19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;

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

        public Property suchThat(Lambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate) {
            return new SuchThat19<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, predicate);
        }
    }

    static class ForAll20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;

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

        public Property suchThat(Lambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate) {
            return new SuchThat20<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, predicate);
        }
    }

    static class ForAll21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;

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

        public Property suchThat(Lambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate) {
            return new SuchThat21<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, predicate);
        }
    }

    static class ForAll22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;

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

        public Property suchThat(Lambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate) {
            return new SuchThat22<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, predicate);
        }
    }

    static class ForAll23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;

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

        public Property suchThat(Lambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate) {
            return new SuchThat23<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, predicate);
        }
    }

    static class ForAll24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;

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

        public Property suchThat(Lambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate) {
            return new SuchThat24<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, predicate);
        }
    }

    static class ForAll25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;
        final Arbitrary<T25> a25;

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

        public Property suchThat(Lambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate) {
            return new SuchThat25<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, predicate);
        }
    }

    static class ForAll26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;
        final Arbitrary<T25> a25;
        final Arbitrary<T26> a26;

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

        public Property suchThat(Lambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate) {
            return new SuchThat26<>(a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11, a12, a13, a14, a15, a16, a17, a18, a19, a20, a21, a22, a23, a24, a25, a26, predicate);
        }
    }

    static class SuchThat1<T1> implements Property {

        final Arbitrary<T1> a1;
        final Lambda1<T1, Boolean> predicate;

        SuchThat1(Arbitrary<T1> a1, Lambda1<T1, Boolean> predicate) {
            this.a1 = a1;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get());
        }
    }

    static class SuchThat2<T1, T2> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Lambda2<T1, T2, Boolean> predicate;

        SuchThat2(Arbitrary<T1> a1, Arbitrary<T2> a2, Lambda2<T1, T2, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get());
        }
    }

    static class SuchThat3<T1, T2, T3> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Lambda3<T1, T2, T3, Boolean> predicate;

        SuchThat3(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Lambda3<T1, T2, T3, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get());
        }
    }

    static class SuchThat4<T1, T2, T3, T4> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Lambda4<T1, T2, T3, T4, Boolean> predicate;

        SuchThat4(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Lambda4<T1, T2, T3, T4, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get());
        }
    }

    static class SuchThat5<T1, T2, T3, T4, T5> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Lambda5<T1, T2, T3, T4, T5, Boolean> predicate;

        SuchThat5(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Lambda5<T1, T2, T3, T4, T5, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get());
        }
    }

    static class SuchThat6<T1, T2, T3, T4, T5, T6> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Lambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate;

        SuchThat6(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Lambda6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get());
        }
    }

    static class SuchThat7<T1, T2, T3, T4, T5, T6, T7> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Lambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate;

        SuchThat7(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Lambda7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get());
        }
    }

    static class SuchThat8<T1, T2, T3, T4, T5, T6, T7, T8> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Lambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate;

        SuchThat8(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Lambda8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get());
        }
    }

    static class SuchThat9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Lambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate;

        SuchThat9(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Lambda9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get());
        }
    }

    static class SuchThat10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Lambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate;

        SuchThat10(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Lambda10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get());
        }
    }

    static class SuchThat11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Lambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate;

        SuchThat11(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Lambda11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get());
        }
    }

    static class SuchThat12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Lambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate;

        SuchThat12(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Lambda12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get());
        }
    }

    static class SuchThat13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Lambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate;

        SuchThat13(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Lambda13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get());
        }
    }

    static class SuchThat14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Lambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate;

        SuchThat14(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Lambda14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get());
        }
    }

    static class SuchThat15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Lambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate;

        SuchThat15(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Lambda15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get());
        }
    }

    static class SuchThat16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Lambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate;

        SuchThat16(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Lambda16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get());
        }
    }

    static class SuchThat17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Lambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate;

        SuchThat17(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Lambda17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get());
        }
    }

    static class SuchThat18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Lambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate;

        SuchThat18(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Lambda18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get());
        }
    }

    static class SuchThat19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Lambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate;

        SuchThat19(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Lambda19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get());
        }
    }

    static class SuchThat20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Lambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate;

        SuchThat20(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Lambda20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get());
        }
    }

    static class SuchThat21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Lambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate;

        SuchThat21(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Lambda21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get());
        }
    }

    static class SuchThat22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Lambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate;

        SuchThat22(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Lambda22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            final Gen<T22> gen22 = a22.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get(), gen22.get());
        }
    }

    static class SuchThat23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Lambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate;

        SuchThat23(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Lambda23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            final Gen<T22> gen22 = a22.apply(n);
            final Gen<T23> gen23 = a23.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get(), gen22.get(), gen23.get());
        }
    }

    static class SuchThat24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;
        final Lambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate;

        SuchThat24(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Lambda24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            final Gen<T22> gen22 = a22.apply(n);
            final Gen<T23> gen23 = a23.apply(n);
            final Gen<T24> gen24 = a24.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get(), gen22.get(), gen23.get(), gen24.get());
        }
    }

    static class SuchThat25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;
        final Arbitrary<T25> a25;
        final Lambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate;

        SuchThat25(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Lambda25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            final Gen<T22> gen22 = a22.apply(n);
            final Gen<T23> gen23 = a23.apply(n);
            final Gen<T24> gen24 = a24.apply(n);
            final Gen<T25> gen25 = a25.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get(), gen22.get(), gen23.get(), gen24.get(), gen25.get());
        }
    }

    static class SuchThat26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Arbitrary<T3> a3;
        final Arbitrary<T4> a4;
        final Arbitrary<T5> a5;
        final Arbitrary<T6> a6;
        final Arbitrary<T7> a7;
        final Arbitrary<T8> a8;
        final Arbitrary<T9> a9;
        final Arbitrary<T10> a10;
        final Arbitrary<T11> a11;
        final Arbitrary<T12> a12;
        final Arbitrary<T13> a13;
        final Arbitrary<T14> a14;
        final Arbitrary<T15> a15;
        final Arbitrary<T16> a16;
        final Arbitrary<T17> a17;
        final Arbitrary<T18> a18;
        final Arbitrary<T19> a19;
        final Arbitrary<T20> a20;
        final Arbitrary<T21> a21;
        final Arbitrary<T22> a22;
        final Arbitrary<T23> a23;
        final Arbitrary<T24> a24;
        final Arbitrary<T25> a25;
        final Arbitrary<T26> a26;
        final Lambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate;

        SuchThat26(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, Arbitrary<T9> a9, Arbitrary<T10> a10, Arbitrary<T11> a11, Arbitrary<T12> a12, Arbitrary<T13> a13, Arbitrary<T14> a14, Arbitrary<T15> a15, Arbitrary<T16> a16, Arbitrary<T17> a17, Arbitrary<T18> a18, Arbitrary<T19> a19, Arbitrary<T20> a20, Arbitrary<T21> a21, Arbitrary<T22> a22, Arbitrary<T23> a23, Arbitrary<T24> a24, Arbitrary<T25> a25, Arbitrary<T26> a26, Lambda26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, Boolean> predicate) {
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
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            final Gen<T3> gen3 = a3.apply(n);
            final Gen<T4> gen4 = a4.apply(n);
            final Gen<T5> gen5 = a5.apply(n);
            final Gen<T6> gen6 = a6.apply(n);
            final Gen<T7> gen7 = a7.apply(n);
            final Gen<T8> gen8 = a8.apply(n);
            final Gen<T9> gen9 = a9.apply(n);
            final Gen<T10> gen10 = a10.apply(n);
            final Gen<T11> gen11 = a11.apply(n);
            final Gen<T12> gen12 = a12.apply(n);
            final Gen<T13> gen13 = a13.apply(n);
            final Gen<T14> gen14 = a14.apply(n);
            final Gen<T15> gen15 = a15.apply(n);
            final Gen<T16> gen16 = a16.apply(n);
            final Gen<T17> gen17 = a17.apply(n);
            final Gen<T18> gen18 = a18.apply(n);
            final Gen<T19> gen19 = a19.apply(n);
            final Gen<T20> gen20 = a20.apply(n);
            final Gen<T21> gen21 = a21.apply(n);
            final Gen<T22> gen22 = a22.apply(n);
            final Gen<T23> gen23 = a23.apply(n);
            final Gen<T24> gen24 = a24.apply(n);
            final Gen<T25> gen25 = a25.apply(n);
            final Gen<T26> gen26 = a26.apply(n);
            // TODO: loop this m times (default: 1000) and return a CheckResult containing detailed informations
            return predicate.apply(gen1.get(), gen2.get(), gen3.get(), gen4.get(), gen5.get(), gen6.get(), gen7.get(), gen8.get(), gen9.get(), gen10.get(), gen11.get(), gen12.get(), gen13.get(), gen14.get(), gen15.get(), gen16.get(), gen17.get(), gen18.get(), gen19.get(), gen20.get(), gen21.get(), gen22.get(), gen23.get(), gen24.get(), gen25.get(), gen26.get());
        }
    }
}