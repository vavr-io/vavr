/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */

package javaslang.test;

import javaslang.function.*;

// TODO: THIS FILE WILL BE GENERATED
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

    static class ForAll1<T1> {

        final Arbitrary<T1> a1;

        ForAll1(Arbitrary<T1> a1) {
            this.a1 = a1;
        }

        public <T2> ForAll2<T1, T2> forAll(Arbitrary<T2> a2) {
            return new ForAll2<>(a1, a2);
        }

        public SuchThat1<T1> suchThat(Lambda1<T1, Boolean> predicate) {
            return new SuchThat1<>(a1, predicate);
        }
    }

    static class SuchThat1<T1> implements Property {

        final Arbitrary<T1> a1;
        final Lambda1<T1, Boolean> predicate;

        public SuchThat1(Arbitrary<T1> a1, Lambda1<T1, Boolean> predicate) {
            this.a1 = a1;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            return predicate.apply(gen1.get());
        }
    }

    static class ForAll2<T1, T2> {
        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;

        ForAll2(Arbitrary<T1> a1, Arbitrary<T2> a2) {
            this.a1 = a1;
            this.a2 = a2;
        }

//        public <T3> ForAll3<T1, T2, T3> forAll(Arbitrary<T3> a3) {
//            return new ForAll3<>(a1, a2, a3);
//        }

        public SuchThat2<T1, T2> suchThat(Lambda2<T1, T2, Boolean> predicate) {
            return new SuchThat2<>(a1, a2, predicate);
        }
    }

    static class SuchThat2<T1, T2> implements Property {

        final Arbitrary<T1> a1;
        final Arbitrary<T2> a2;
        final Lambda2<T1, T2, Boolean> predicate;

        public SuchThat2(Arbitrary<T1> a1, Arbitrary<T2> a2, Lambda2<T1, T2, Boolean> predicate) {
            this.a1 = a1;
            this.a2 = a2;
            this.predicate = predicate;
        }

        @Override
        public boolean test(int n) {
            final Gen<T1> gen1 = a1.apply(n);
            final Gen<T2> gen2 = a2.apply(n);
            return predicate.apply(gen1.get(), gen2.get());
        }
    }
}
