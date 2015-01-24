/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.function.*;
import javaslang.collection.Stream;

import java.util.function.Predicate;

// TODO: THIS FILE WILL BE GENERATED
public interface Property {

    static <T1> Property1<T1> exists(Arbitrary<T1> a1) {
        return new Property1<>(Test.exists(a1));
    }

    static <T1, T2> Property2<T1, T2> exists(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return new Property2<>(Test.exists(a1), Test.exists(a2));
    }

    static <T1> Property1<T1> existsUnique(Arbitrary<T1> a1) {
        return new Property1<>(Test.existsUnique(a1));
    }

    static <T1, T2> Property2<T1, T2> existsUnique(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return new Property2<>(Test.existsUnique(a1), Test.existsUnique(a2));
    }

    static <T1> Property1<T1> forAll(Arbitrary<T1> a1) {
        return new Property1<>(Test.forAll(a1));
    }

    static <T1, T2> Property2<T1, T2> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return new Property2<>(Test.forAll(a1), Test.forAll(a2));
    }

    @FunctionalInterface
    static interface Test<T> {
        static <T> Test<T> forAll(Arbitrary<T> arbitrary) {
            return (n, predicate) -> Stream.of(arbitrary.apply(n)).forAll(predicate);
        }

        static <T> Test<T> exists(Arbitrary<T> arbitrary) {
            return (n, predicate) -> Stream.of(arbitrary.apply(n)).exists(predicate);
        }

        static <T> Test<T> existsUnique(Arbitrary<T> arbitrary) {
            return (n, predicate) -> Stream.of(arbitrary.apply(n)).existsUnique(predicate);
        }

        boolean test(int n, Predicate<T> predicate);
    }

    static class Property1<T1> {

        final Test<T1> test1;

        Property1(Test<T1> test1) {
            this.test1 = test1;
        }

        public <T2> Property2<T1, T2> exists(Arbitrary<T2> a2) {
            return new Property2<>(test1, Test.exists(a2));
        }

        public <T2> Property2<T1, T2> existsUnique(Arbitrary<T2> a2) {
            return new Property2<>(test1, Test.existsUnique(a2));
        }

        public <T2> Property2<T1, T2> forAll(Arbitrary<T2> a2) {
            return new Property2<>(test1, Test.forAll(a2));
        }

        public boolean suchThat(Function1<T1, Boolean> predicate) {
            throw new UnsupportedOperationException("TODO");
        }
    }

    static class Property2<T1, T2> {
        final Test<T1> test1;
        final Test<T2> test2;

        Property2(Test<T1> test1, Test<T2> test2) {
            this.test1 = test1;
            this.test2 = test2;
        }

        public <T3> Property3<T1, T2, T3> exists(Arbitrary<T3> a3) {
            return new Property3<>(test1, test2, Test.exists(a3));
        }

        public <T3> Property3<T1, T2, T3> existsUnique(Arbitrary<T3> a3) {
            return new Property3<>(test1, test2, Test.existsUnique(a3));
        }

        public <T3> Property3<T1, T2, T3> forAll(Arbitrary<T3> a3) {
            return new Property3<>(test1, test2, Test.forAll(a3));
        }


        public boolean suchThat(Function2<T1, T2, Boolean> predicate) {
            throw new UnsupportedOperationException("TODO");
        }
    }

    static class Property3<T1, T2, T3> {
        final Test<T1> test1;
        final Test<T2> test2;
        final Test<T3> test3;

        Property3(Test<T1> test1, Test<T2> test2, Test<T3> test3) {
            this.test1 = test1;
            this.test2 = test2;
            this.test3 = test3;
        }

        // ...

        public boolean suchThat(λ3<T1, T2, T3, Boolean> predicate) {

            throw new UnsupportedOperationException("TODO");

            /*

            TODO:

            forAll(a1, ..., an) {
              testNext(a1, ..., an, b.get());
            }


            forAll1 : Function1<T1, Boolean> -> Boolean = f -> f.apply(a.get())

            exists2 : Function1<T2, Function1<T1, Boolean>> -> Boolean = f -> {
                while (!found) {
                   found = predecessor.apply(f.apply(arbitrary.get()));
                }
            }

            // final λ1<T3, λ1<T2, λ1<T1, Boolean>>> f = predicate.reversed().curried();
            suchThat2 : Function2<T1, T2, Boolean> -> Boolean = f2 -> property.apply(Functions.reverse(f2.curried()))

            exisits(a1, ..., an) {
              while(!found) {
                found = testNext(a1, ..., an, b.get());
              }
            }

            suchThat(a, b) {
               return a + b == 0
            }

            forAll(a) // Supplier<Boolean> // a.get()
            exists(b)
            suchThat((a, b) -> a + b = 0)

             */
        }
    }
}
