/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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

import io.vavr.*;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Objects;
import java.util.Random;

/**
 * A property builder which provides a fluent API to build checkable properties.
 *
 * @author Daniel Dietrich
 */
public final class Property {

    private final String name;

    private Property(String name) {
        this.name = name;
    }

    /**
     * Defines a new Property.
     *
     * @param name property name
     * @return a new {@code Property}
     * @throws NullPointerException if name is null.
     * @throws IllegalArgumentException if name is empty or consists of whitespace only
     */
    public static Property def(String name) {
        Objects.requireNonNull(name, "name is null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }
        return new Property(name);
    }

    private static void logSatisfied(String name, int tries, long millis, boolean exhausted) {
        if (exhausted) {
            log(String.format("%s: Exhausted after %s tests in %s ms.", name, tries, millis));
        } else {
            log(String.format("%s: OK, passed %s tests in %s ms.", name, tries, millis));
        }
    }

    private static void logFalsified(String name, int currentTry, long millis) {
        log(String.format("%s: Falsified after %s passed tests in %s ms.", name, currentTry - 1, millis));
    }

    private static void logErroneous(String name, int currentTry, long millis, String errorMessage) {
        log(String.format("%s: Errored after %s passed tests in %s ms with message: %s", name, Math.max(0, currentTry - 1), millis, errorMessage));
    }

    private static void log(String msg) {
        System.out.println(msg);
    }

    /**
     * Creates a CheckError caused by an exception when obtaining a generator.
     *
     * @param position The position of the argument within the argument list of the property, starting with 1.
     * @param size     The size hint passed to the {@linkplain Arbitrary} which caused the error.
     * @param cause    The error which occurred when the {@linkplain Arbitrary} tried to obtain the generator {@linkplain Gen}.
     * @return a new CheckError instance.
     */
    private static CheckError arbitraryError(int position, int size, Throwable cause) {
        return new CheckError(String.format("Arbitrary %s of size %s: %s", position, size, cause.getMessage()), cause);
    }

    /**
     * Creates a CheckError caused by an exception when generating a value.
     *
     * @param position The position of the argument within the argument list of the property, starting with 1.
     * @param size     The size hint of the arbitrary which called the generator {@linkplain Gen} which caused the error.
     * @param cause    The error which occurred when the {@linkplain Gen} tried to generate a random value.
     * @return a new CheckError instance.
     */
    private static CheckError genError(int position, int size, Throwable cause) {
        return new CheckError(String.format("Gen %s of size %s: %s", position, size, cause.getMessage()), cause);
    }

    /**
     * Creates a CheckError caused by an exception when testing a Predicate.
     *
     * @param cause The error which occurred when applying the {@linkplain java.util.function.Predicate}.
     * @return a new CheckError instance.
     */
    private static CheckError predicateError(Throwable cause) {
        return new CheckError("Applying predicate: " + cause.getMessage(), cause);
    }

    /**
     * Returns a logical for all quantor of 1 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @return a new {@code ForAll1} instance of 1 variables
     */
    public <T1> ForAll1<T1> forAll(Arbitrary<T1> a1) {
        return new ForAll1<>(name, a1);
    }

    /**
     * Returns a logical for all quantor of 2 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @return a new {@code ForAll2} instance of 2 variables
     */
    public <T1, T2> ForAll2<T1, T2> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return new ForAll2<>(name, a1, a2);
    }

    /**
     * Returns a logical for all quantor of 3 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @return a new {@code ForAll3} instance of 3 variables
     */
    public <T1, T2, T3> ForAll3<T1, T2, T3> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3) {
        return new ForAll3<>(name, a1, a2, a3);
    }

    /**
     * Returns a logical for all quantor of 4 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @param a4 4th variable of this for all quantor
     * @return a new {@code ForAll4} instance of 4 variables
     */
    public <T1, T2, T3, T4> ForAll4<T1, T2, T3, T4> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
        return new ForAll4<>(name, a1, a2, a3, a4);
    }

    /**
     * Returns a logical for all quantor of 5 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @param a4 4th variable of this for all quantor
     * @param a5 5th variable of this for all quantor
     * @return a new {@code ForAll5} instance of 5 variables
     */
    public <T1, T2, T3, T4, T5> ForAll5<T1, T2, T3, T4, T5> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
        return new ForAll5<>(name, a1, a2, a3, a4, a5);
    }

    /**
     * Returns a logical for all quantor of 6 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @param a4 4th variable of this for all quantor
     * @param a5 5th variable of this for all quantor
     * @param a6 6th variable of this for all quantor
     * @return a new {@code ForAll6} instance of 6 variables
     */
    public <T1, T2, T3, T4, T5, T6> ForAll6<T1, T2, T3, T4, T5, T6> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
        return new ForAll6<>(name, a1, a2, a3, a4, a5, a6);
    }

    /**
     * Returns a logical for all quantor of 7 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @param <T7> 7th variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @param a4 4th variable of this for all quantor
     * @param a5 5th variable of this for all quantor
     * @param a6 6th variable of this for all quantor
     * @param a7 7th variable of this for all quantor
     * @return a new {@code ForAll7} instance of 7 variables
     */
    public <T1, T2, T3, T4, T5, T6, T7> ForAll7<T1, T2, T3, T4, T5, T6, T7> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
        return new ForAll7<>(name, a1, a2, a3, a4, a5, a6, a7);
    }

    /**
     * Returns a logical for all quantor of 8 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @param <T7> 7th variable type of this for all quantor
     * @param <T8> 8th variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @param a2 2nd variable of this for all quantor
     * @param a3 3rd variable of this for all quantor
     * @param a4 4th variable of this for all quantor
     * @param a5 5th variable of this for all quantor
     * @param a6 6th variable of this for all quantor
     * @param a7 7th variable of this for all quantor
     * @param a8 8th variable of this for all quantor
     * @return a new {@code ForAll8} instance of 8 variables
     */
    public <T1, T2, T3, T4, T5, T6, T7, T8> ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> forAll(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
        return new ForAll8<>(name, a1, a2, a3, a4, a5, a6, a7, a8);
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll1<T1> {

        private final String name;
        private final Arbitrary<T1> a1;

        ForAll1(String name, Arbitrary<T1> a1) {
            this.name = name;
            this.a1 = a1;
        }

        /**
         * Returns a checkable property that checks values of the 1 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 1-ary predicate
         * @return a new {@code Property1} of 1 variables.
         */
        public Property1<T1> suchThat(CheckedFunction1<T1, Boolean> predicate) {
            final CheckedFunction1<T1, Condition> proposition = (t1) -> new Condition(true, predicate.apply(t1));
            return new Property1<>(name, a1, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll2<T1, T2> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;

        ForAll2(String name, Arbitrary<T1> a1, Arbitrary<T2> a2) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
        }

        /**
         * Returns a checkable property that checks values of the 2 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 2-ary predicate
         * @return a new {@code Property2} of 2 variables.
         */
        public Property2<T1, T2> suchThat(CheckedFunction2<T1, T2, Boolean> predicate) {
            final CheckedFunction2<T1, T2, Condition> proposition = (t1, t2) -> new Condition(true, predicate.apply(t1, t2));
            return new Property2<>(name, a1, a2, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll3<T1, T2, T3> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;

        ForAll3(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
        }

        /**
         * Returns a checkable property that checks values of the 3 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 3-ary predicate
         * @return a new {@code Property3} of 3 variables.
         */
        public Property3<T1, T2, T3> suchThat(CheckedFunction3<T1, T2, T3, Boolean> predicate) {
            final CheckedFunction3<T1, T2, T3, Condition> proposition = (t1, t2, t3) -> new Condition(true, predicate.apply(t1, t2, t3));
            return new Property3<>(name, a1, a2, a3, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll4<T1, T2, T3, T4> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;

        ForAll4(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
        }

        /**
         * Returns a checkable property that checks values of the 4 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 4-ary predicate
         * @return a new {@code Property4} of 4 variables.
         */
        public Property4<T1, T2, T3, T4> suchThat(CheckedFunction4<T1, T2, T3, T4, Boolean> predicate) {
            final CheckedFunction4<T1, T2, T3, T4, Condition> proposition = (t1, t2, t3, t4) -> new Condition(true, predicate.apply(t1, t2, t3, t4));
            return new Property4<>(name, a1, a2, a3, a4, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll5<T1, T2, T3, T4, T5> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;

        ForAll5(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
        }

        /**
         * Returns a checkable property that checks values of the 5 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 5-ary predicate
         * @return a new {@code Property5} of 5 variables.
         */
        public Property5<T1, T2, T3, T4, T5> suchThat(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> predicate) {
            final CheckedFunction5<T1, T2, T3, T4, T5, Condition> proposition = (t1, t2, t3, t4, t5) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5));
            return new Property5<>(name, a1, a2, a3, a4, a5, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll6<T1, T2, T3, T4, T5, T6> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;

        ForAll6(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
        }

        /**
         * Returns a checkable property that checks values of the 6 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 6-ary predicate
         * @return a new {@code Property6} of 6 variables.
         */
        public Property6<T1, T2, T3, T4, T5, T6> suchThat(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> proposition = (t1, t2, t3, t4, t5, t6) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6));
            return new Property6<>(name, a1, a2, a3, a4, a5, a6, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @param <T7> 7th variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll7<T1, T2, T3, T4, T5, T6, T7> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;

        ForAll7(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
        }

        /**
         * Returns a checkable property that checks values of the 7 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 7-ary predicate
         * @return a new {@code Property7} of 7 variables.
         */
        public Property7<T1, T2, T3, T4, T5, T6, T7> suchThat(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7));
            return new Property7<>(name, a1, a2, a3, a4, a5, a6, a7, proposition);
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @param <T4> 4th variable type of this for all quantor
     * @param <T5> 5th variable type of this for all quantor
     * @param <T6> 6th variable type of this for all quantor
     * @param <T7> 7th variable type of this for all quantor
     * @param <T8> 8th variable type of this for all quantor
     * @author Daniel Dietrich
     */
    public static class ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;

        ForAll8(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.a8 = a8;
        }

        /**
         * Returns a checkable property that checks values of the 8 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 8-ary predicate
         * @return a new {@code Property8} of 8 variables.
         */
        public Property8<T1, T2, T3, T4, T5, T6, T7, T8> suchThat(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
            final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> proposition = (t1, t2, t3, t4, t5, t6, t7, t8) -> new Condition(true, predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8));
            return new Property8<>(name, a1, a2, a3, a4, a5, a6, a7, a8, proposition);
        }
    }

    /**
     * Represents a 1-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property1<T1> implements Checkable<Tuple1<T1>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final CheckedFunction1<T1, Condition> predicate;

        Property1(String name, Arbitrary<T1> a1, CheckedFunction1<T1, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property1} implication
         */
        public Property1<T1> implies(CheckedFunction1<T1, Boolean> postCondition) {
            final CheckedFunction1<T1, Condition> implication = (t1) -> {
                final Condition preCondition = predicate.apply(t1);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1));
                }
            };
            return new Property1<>(name, a1, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property1<T1>}
         */
        @Override
        public CheckResult<Tuple1<T1>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 2-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property2<T1, T2> implements Checkable<Tuple2<T1, T2>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final CheckedFunction2<T1, T2, Condition> predicate;

        Property2(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, CheckedFunction2<T1, T2, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property2} implication
         */
        public Property2<T1, T2> implies(CheckedFunction2<T1, T2, Boolean> postCondition) {
            final CheckedFunction2<T1, T2, Condition> implication = (t1, t2) -> {
                final Condition preCondition = predicate.apply(t1, t2);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2));
                }
            };
            return new Property2<>(name, a1, a2, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property2<T1, T2>}
         */
        @Override
        public CheckResult<Tuple2<T1, T2>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 3-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property3<T1, T2, T3> implements Checkable<Tuple3<T1, T2, T3>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final CheckedFunction3<T1, T2, T3, Condition> predicate;

        Property3(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, CheckedFunction3<T1, T2, T3, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property3} implication
         */
        public Property3<T1, T2, T3> implies(CheckedFunction3<T1, T2, T3, Boolean> postCondition) {
            final CheckedFunction3<T1, T2, T3, Condition> implication = (t1, t2, t3) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3));
                }
            };
            return new Property3<>(name, a1, a2, a3, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property3<T1, T2, T3>}
         */
        @Override
        public CheckResult<Tuple3<T1, T2, T3>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 4-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property4<T1, T2, T3, T4> implements Checkable<Tuple4<T1, T2, T3, T4>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final CheckedFunction4<T1, T2, T3, T4, Condition> predicate;

        Property4(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, CheckedFunction4<T1, T2, T3, T4, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property4} implication
         */
        public Property4<T1, T2, T3, T4> implies(CheckedFunction4<T1, T2, T3, T4, Boolean> postCondition) {
            final CheckedFunction4<T1, T2, T3, T4, Condition> implication = (t1, t2, t3, t4) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3, t4);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3, t4));
                }
            };
            return new Property4<>(name, a1, a2, a3, a4, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property4<T1, T2, T3, T4>}
         */
        @Override
        public CheckResult<Tuple4<T1, T2, T3, T4>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw arbitraryError(4, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw genError(4, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3, val4));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3, val4)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 5-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property5<T1, T2, T3, T4, T5> implements Checkable<Tuple5<T1, T2, T3, T4, T5>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final CheckedFunction5<T1, T2, T3, T4, T5, Condition> predicate;

        Property5(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, CheckedFunction5<T1, T2, T3, T4, T5, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property5} implication
         */
        public Property5<T1, T2, T3, T4, T5> implies(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> postCondition) {
            final CheckedFunction5<T1, T2, T3, T4, T5, Condition> implication = (t1, t2, t3, t4, t5) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3, t4, t5);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3, t4, t5));
                }
            };
            return new Property5<>(name, a1, a2, a3, a4, a5, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property5<T1, T2, T3, T4, T5>}
         */
        @Override
        public CheckResult<Tuple5<T1, T2, T3, T4, T5>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw arbitraryError(5, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw genError(5, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3, val4, val5));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3, val4, val5)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 6-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property6<T1, T2, T3, T4, T5, T6> implements Checkable<Tuple6<T1, T2, T3, T4, T5, T6>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> predicate;

        Property6(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property6} implication
         */
        public Property6<T1, T2, T3, T4, T5, T6> implies(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> postCondition) {
            final CheckedFunction6<T1, T2, T3, T4, T5, T6, Condition> implication = (t1, t2, t3, t4, t5, t6) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3, t4, t5, t6);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3, t4, t5, t6));
                }
            };
            return new Property6<>(name, a1, a2, a3, a4, a5, a6, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property6<T1, T2, T3, T4, T5, T6>}
         */
        @Override
        public CheckResult<Tuple6<T1, T2, T3, T4, T5, T6>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw arbitraryError(6, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw genError(6, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3, val4, val5, val6));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3, val4, val5, val6)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 7-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property7<T1, T2, T3, T4, T5, T6, T7> implements Checkable<Tuple7<T1, T2, T3, T4, T5, T6, T7>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> predicate;

        Property7(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> predicate) {
            this.name = name;
            this.a1 = a1;
            this.a2 = a2;
            this.a3 = a3;
            this.a4 = a4;
            this.a5 = a5;
            this.a6 = a6;
            this.a7 = a7;
            this.predicate = predicate;
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property7} implication
         */
        public Property7<T1, T2, T3, T4, T5, T6, T7> implies(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> postCondition) {
            final CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Condition> implication = (t1, t2, t3, t4, t5, t6, t7) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3, t4, t5, t6, t7));
                }
            };
            return new Property7<>(name, a1, a2, a3, a4, a5, a6, a7, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property7<T1, T2, T3, T4, T5, T6, T7>}
         */
        @Override
        public CheckResult<Tuple7<T1, T2, T3, T4, T5, T6, T7>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw arbitraryError(7, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw genError(7, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3, val4, val5, val6, val7));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3, val4, val5, val6, val7)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Represents a 8-ary checkable property.
     *
     * @author Daniel Dietrich
     */
    public static class Property8<T1, T2, T3, T4, T5, T6, T7, T8> implements Checkable<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

        private final String name;
        private final Arbitrary<T1> a1;
        private final Arbitrary<T2> a2;
        private final Arbitrary<T3> a3;
        private final Arbitrary<T4> a4;
        private final Arbitrary<T5> a5;
        private final Arbitrary<T6> a6;
        private final Arbitrary<T7> a7;
        private final Arbitrary<T8> a8;
        private final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> predicate;

        Property8(String name, Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8, CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> predicate) {
            this.name = name;
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

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postCondition The post-condition of this implication
         * @return A new {@code Property8} implication
         */
        public Property8<T1, T2, T3, T4, T5, T6, T7, T8> implies(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> postCondition) {
            final CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Condition> implication = (t1, t2, t3, t4, t5, t6, t7, t8) -> {
                final Condition preCondition = predicate.apply(t1, t2, t3, t4, t5, t6, t7, t8);
                if (preCondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postCondition.apply(t1, t2, t3, t4, t5, t6, t7, t8));
                }
            };
            return new Property8<>(name, a1, a2, a3, a4, a5, a6, a7, a8, implication);
        }

        /**
         * Checks this property.
         *
         * @param random An implementation of {@link java.util.Random}.
         * @param size   A (not necessarily positive) size hint.
         * @param tries  A non-negative number of tries to falsify the given property.
         * @return A new instance of {@code Property8<T1, T2, T3, T4, T5, T6, T7, T8>}
         */
        @Override
        public CheckResult<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> check(Random random, int size, int tries) {
            Objects.requireNonNull(random, "random is null");
            if (tries < 0) {
                throw new IllegalArgumentException("tries < 0");
            }
            final long startTime = System.currentTimeMillis();
            try {
                final Gen<T1> gen1 = Try.of(() -> a1.apply(size)).recover(x -> { throw arbitraryError(1, size, x); }).get();
                final Gen<T2> gen2 = Try.of(() -> a2.apply(size)).recover(x -> { throw arbitraryError(2, size, x); }).get();
                final Gen<T3> gen3 = Try.of(() -> a3.apply(size)).recover(x -> { throw arbitraryError(3, size, x); }).get();
                final Gen<T4> gen4 = Try.of(() -> a4.apply(size)).recover(x -> { throw arbitraryError(4, size, x); }).get();
                final Gen<T5> gen5 = Try.of(() -> a5.apply(size)).recover(x -> { throw arbitraryError(5, size, x); }).get();
                final Gen<T6> gen6 = Try.of(() -> a6.apply(size)).recover(x -> { throw arbitraryError(6, size, x); }).get();
                final Gen<T7> gen7 = Try.of(() -> a7.apply(size)).recover(x -> { throw arbitraryError(7, size, x); }).get();
                final Gen<T8> gen8 = Try.of(() -> a8.apply(size)).recover(x -> { throw arbitraryError(8, size, x); }).get();
                boolean exhausted = true;
                for (int i = 1; i <= tries; i++) {
                    try {
                        final T1 val1 = Try.of(() -> gen1.apply(random)).recover(x -> { throw genError(1, size, x); }).get();
                        final T2 val2 = Try.of(() -> gen2.apply(random)).recover(x -> { throw genError(2, size, x); }).get();
                        final T3 val3 = Try.of(() -> gen3.apply(random)).recover(x -> { throw genError(3, size, x); }).get();
                        final T4 val4 = Try.of(() -> gen4.apply(random)).recover(x -> { throw genError(4, size, x); }).get();
                        final T5 val5 = Try.of(() -> gen5.apply(random)).recover(x -> { throw genError(5, size, x); }).get();
                        final T6 val6 = Try.of(() -> gen6.apply(random)).recover(x -> { throw genError(6, size, x); }).get();
                        final T7 val7 = Try.of(() -> gen7.apply(random)).recover(x -> { throw genError(7, size, x); }).get();
                        final T8 val8 = Try.of(() -> gen8.apply(random)).recover(x -> { throw genError(8, size, x); }).get();
                        try {
                            final Condition condition = Try.of(() -> predicate.apply(val1, val2, val3, val4, val5, val6, val7, val8)).recover(x -> { throw predicateError(x); }).get();
                            if (condition.preCondition) {
                                exhausted = false;
                                if (!condition.postCondition) {
                                    logFalsified(name, i, System.currentTimeMillis() - startTime);
                                    return CheckResult.falsified(name, i, Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8));
                                }
                            }
                        } catch(Try.NonFatalThrowable x) {
                            final CheckError err = (CheckError) x.getCause();
                            logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                            return CheckResult.erroneous(name, i, err, Option.some(Tuple.of(val1, val2, val3, val4, val5, val6, val7, val8)));
                        }
                    } catch(Try.NonFatalThrowable x) {
                        final CheckError err = (CheckError) x.getCause();
                        logErroneous(name, i, System.currentTimeMillis() - startTime, err.getMessage());
                        return CheckResult.erroneous(name, i, err, Option.none());
                    }
                }
                logSatisfied(name, tries, System.currentTimeMillis() - startTime, exhausted);
                return CheckResult.satisfied(name, tries, exhausted);
            } catch(Try.NonFatalThrowable x) {
                final CheckError err = (CheckError) x.getCause();
                logErroneous(name, 0, System.currentTimeMillis() - startTime, err.getMessage());
                return CheckResult.erroneous(name, 0, err, Option.none());
            }
        }
    }

    /**
     * Internally used to model conditions composed of pre- and post-condition.
     */
    private static class Condition {

        static final Condition EX_FALSO_QUODLIBET = new Condition(false, true);

        final boolean preCondition;
        final boolean postCondition;

        Condition(boolean preCondition, boolean postCondition) {
            this.preCondition = preCondition;
            this.postCondition = postCondition;
        }

        // ¬(p => q) ≡ ¬(¬p ∨ q) ≡ p ∧ ¬q
        boolean isFalse() {
            return preCondition && !postCondition;
        }
    }
}