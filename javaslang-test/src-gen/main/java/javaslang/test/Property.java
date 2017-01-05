/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import javaslang.*;

/**
 * A property builder which provides a fluent API to build checkable properties.
 *
 * @author Daniel Dietrich
 * @since 1.2.0
 */
public class Property {

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

    /**
     * Returns a logical for all quantor of 1 given variables.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param a1 1st variable of this for all quantor
     * @return a new {@code ForAll1} instance of 1 variables
     */
    public <T1> ForAll1<T1> forAll(Arbitrary<T1> a1) {
        return new ForAll1<>(name, ArbitraryTuple.of(a1));
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
        return new ForAll2<>(name, ArbitraryTuple.of(a1, a2));
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
        return new ForAll3<>(name, ArbitraryTuple.of(a1, a2, a3));
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
        return new ForAll4<>(name, ArbitraryTuple.of(a1, a2, a3, a4));
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
        return new ForAll5<>(name, ArbitraryTuple.of(a1, a2, a3, a4, a5));
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
        return new ForAll6<>(name, ArbitraryTuple.of(a1, a2, a3, a4, a5, a6));
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
        return new ForAll7<>(name, ArbitraryTuple.of(a1, a2, a3, a4, a5, a6, a7));
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
        return new ForAll8<>(name, ArbitraryTuple.of(a1, a2, a3, a4, a5, a6, a7, a8));
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class ForAll1<T1> {

        private final String name;
        private final Arbitrary<Tuple1<T1>> arbitrary;

        ForAll1(String name, Arbitrary<Tuple1<T1>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 1 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 1-ary predicate
         * @return a new {@code Property1} of 1 variables.
         */
        public Property1<T1> suchThat(CheckedFunction1<T1, Boolean> predicate) {
            final CheckedFunction1<Tuple1<T1>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property1<>(name, proposition, arbitrary, Shrink.empty());
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class ForAll2<T1, T2> {

        private final String name;
        private final Arbitrary<Tuple2<T1, T2>> arbitrary;

        ForAll2(String name, Arbitrary<Tuple2<T1, T2>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 2 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 2-ary predicate
         * @return a new {@code Property2} of 2 variables.
         */
        public Property2<T1, T2> suchThat(CheckedFunction2<T1, T2, Boolean> predicate) {
            final CheckedFunction1<Tuple2<T1, T2>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property2<>(name, proposition, arbitrary, Shrink.empty());
        }
    }

    /**
     * Represents a logical for all quantor.
     *
     * @param <T1> 1st variable type of this for all quantor
     * @param <T2> 2nd variable type of this for all quantor
     * @param <T3> 3rd variable type of this for all quantor
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class ForAll3<T1, T2, T3> {

        private final String name;
        private final Arbitrary<Tuple3<T1, T2, T3>> arbitrary;

        ForAll3(String name, Arbitrary<Tuple3<T1, T2, T3>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 3 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 3-ary predicate
         * @return a new {@code Property3} of 3 variables.
         */
        public Property3<T1, T2, T3> suchThat(CheckedFunction3<T1, T2, T3, Boolean> predicate) {
            final CheckedFunction1<Tuple3<T1, T2, T3>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property3<>(name, proposition, arbitrary, Shrink.empty());
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
     * @since 1.2.0
     */
    public static class ForAll4<T1, T2, T3, T4> {

        private final String name;
        private final Arbitrary<Tuple4<T1, T2, T3, T4>> arbitrary;

        ForAll4(String name, Arbitrary<Tuple4<T1, T2, T3, T4>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 4 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 4-ary predicate
         * @return a new {@code Property4} of 4 variables.
         */
        public Property4<T1, T2, T3, T4> suchThat(CheckedFunction4<T1, T2, T3, T4, Boolean> predicate) {
            final CheckedFunction1<Tuple4<T1, T2, T3, T4>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property4<>(name, proposition, arbitrary, Shrink.empty());
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
     * @since 1.2.0
     */
    public static class ForAll5<T1, T2, T3, T4, T5> {

        private final String name;
        private final Arbitrary<Tuple5<T1, T2, T3, T4, T5>> arbitrary;

        ForAll5(String name, Arbitrary<Tuple5<T1, T2, T3, T4, T5>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 5 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 5-ary predicate
         * @return a new {@code Property5} of 5 variables.
         */
        public Property5<T1, T2, T3, T4, T5> suchThat(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> predicate) {
            final CheckedFunction1<Tuple5<T1, T2, T3, T4, T5>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property5<>(name, proposition, arbitrary, Shrink.empty());
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
     * @since 1.2.0
     */
    public static class ForAll6<T1, T2, T3, T4, T5, T6> {

        private final String name;
        private final Arbitrary<Tuple6<T1, T2, T3, T4, T5, T6>> arbitrary;

        ForAll6(String name, Arbitrary<Tuple6<T1, T2, T3, T4, T5, T6>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 6 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 6-ary predicate
         * @return a new {@code Property6} of 6 variables.
         */
        public Property6<T1, T2, T3, T4, T5, T6> suchThat(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> predicate) {
            final CheckedFunction1<Tuple6<T1, T2, T3, T4, T5, T6>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property6<>(name, proposition, arbitrary, Shrink.empty());
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
     * @since 1.2.0
     */
    public static class ForAll7<T1, T2, T3, T4, T5, T6, T7> {

        private final String name;
        private final Arbitrary<Tuple7<T1, T2, T3, T4, T5, T6, T7>> arbitrary;

        ForAll7(String name, Arbitrary<Tuple7<T1, T2, T3, T4, T5, T6, T7>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 7 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 7-ary predicate
         * @return a new {@code Property7} of 7 variables.
         */
        public Property7<T1, T2, T3, T4, T5, T6, T7> suchThat(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> predicate) {
            final CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property7<>(name, proposition, arbitrary, Shrink.empty());
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
     * @since 1.2.0
     */
    public static class ForAll8<T1, T2, T3, T4, T5, T6, T7, T8> {

        private final String name;
        private final Arbitrary<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> arbitrary;

        ForAll8(String name, Arbitrary<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> arbitrary) {
            this.name = name;
            this.arbitrary = arbitrary;
        }

        /**
         * Returns a checkable property that checks values of the 8 variables of this {@code ForAll} quantor.
         *
         * @param predicate A 8-ary predicate
         * @return a new {@code Property8} of 8 variables.
         */
        public Property8<T1, T2, T3, T4, T5, T6, T7, T8> suchThat(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> predicate) {
            final CheckedFunction1<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, Condition> proposition = (t) -> new Condition(true, predicate.tupled().apply(t));
            return new Property8<>(name, proposition, arbitrary, Shrink.empty());
        }
    }

    /**
     * Represents a 1-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property1<T1> extends PropertyCheck<Tuple1<T1>> {

        Property1(String name, CheckedFunction1<Tuple1<T1>, Condition> predicate, Arbitrary<Tuple1<T1>> arbitrary, Shrink<Tuple1<T1>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple1<T1>> implies(CheckedFunction1<T1, Boolean> postcondition) {
            final CheckedFunction1<Tuple1<T1>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property1<>(name, implication, arbitrary, shrink);
        }

        public Property1<T1> shrinking(Shrink<T1> s1) {
            return new Property1<>(name, predicate, arbitrary, ShrinkTuple.of(s1));
        }
    }

    /**
     * Represents a 2-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property2<T1, T2> extends PropertyCheck<Tuple2<T1, T2>> {

        Property2(String name, CheckedFunction1<Tuple2<T1, T2>, Condition> predicate, Arbitrary<Tuple2<T1, T2>> arbitrary, Shrink<Tuple2<T1, T2>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple2<T1, T2>> implies(CheckedFunction2<T1, T2, Boolean> postcondition) {
            final CheckedFunction1<Tuple2<T1, T2>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property2<>(name, implication, arbitrary, shrink);
        }

        public Property2<T1, T2> shrinking(Shrink<T1> s1, Shrink<T2> s2) {
            return new Property2<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2));
        }
    }

    /**
     * Represents a 3-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property3<T1, T2, T3> extends PropertyCheck<Tuple3<T1, T2, T3>> {

        Property3(String name, CheckedFunction1<Tuple3<T1, T2, T3>, Condition> predicate, Arbitrary<Tuple3<T1, T2, T3>> arbitrary, Shrink<Tuple3<T1, T2, T3>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple3<T1, T2, T3>> implies(CheckedFunction3<T1, T2, T3, Boolean> postcondition) {
            final CheckedFunction1<Tuple3<T1, T2, T3>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property3<>(name, implication, arbitrary, shrink);
        }

        public Property3<T1, T2, T3> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3) {
            return new Property3<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3));
        }
    }

    /**
     * Represents a 4-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property4<T1, T2, T3, T4> extends PropertyCheck<Tuple4<T1, T2, T3, T4>> {

        Property4(String name, CheckedFunction1<Tuple4<T1, T2, T3, T4>, Condition> predicate, Arbitrary<Tuple4<T1, T2, T3, T4>> arbitrary, Shrink<Tuple4<T1, T2, T3, T4>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple4<T1, T2, T3, T4>> implies(CheckedFunction4<T1, T2, T3, T4, Boolean> postcondition) {
            final CheckedFunction1<Tuple4<T1, T2, T3, T4>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property4<>(name, implication, arbitrary, shrink);
        }

        public Property4<T1, T2, T3, T4> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4) {
            return new Property4<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3, s4));
        }
    }

    /**
     * Represents a 5-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property5<T1, T2, T3, T4, T5> extends PropertyCheck<Tuple5<T1, T2, T3, T4, T5>> {

        Property5(String name, CheckedFunction1<Tuple5<T1, T2, T3, T4, T5>, Condition> predicate, Arbitrary<Tuple5<T1, T2, T3, T4, T5>> arbitrary, Shrink<Tuple5<T1, T2, T3, T4, T5>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple5<T1, T2, T3, T4, T5>> implies(CheckedFunction5<T1, T2, T3, T4, T5, Boolean> postcondition) {
            final CheckedFunction1<Tuple5<T1, T2, T3, T4, T5>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property5<>(name, implication, arbitrary, shrink);
        }

        public Property5<T1, T2, T3, T4, T5> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5) {
            return new Property5<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3, s4, s5));
        }
    }

    /**
     * Represents a 6-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property6<T1, T2, T3, T4, T5, T6> extends PropertyCheck<Tuple6<T1, T2, T3, T4, T5, T6>> {

        Property6(String name, CheckedFunction1<Tuple6<T1, T2, T3, T4, T5, T6>, Condition> predicate, Arbitrary<Tuple6<T1, T2, T3, T4, T5, T6>> arbitrary, Shrink<Tuple6<T1, T2, T3, T4, T5, T6>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple6<T1, T2, T3, T4, T5, T6>> implies(CheckedFunction6<T1, T2, T3, T4, T5, T6, Boolean> postcondition) {
            final CheckedFunction1<Tuple6<T1, T2, T3, T4, T5, T6>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property6<>(name, implication, arbitrary, shrink);
        }

        public Property6<T1, T2, T3, T4, T5, T6> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6) {
            return new Property6<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3, s4, s5, s6));
        }
    }

    /**
     * Represents a 7-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property7<T1, T2, T3, T4, T5, T6, T7> extends PropertyCheck<Tuple7<T1, T2, T3, T4, T5, T6, T7>> {

        Property7(String name, CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, Condition> predicate, Arbitrary<Tuple7<T1, T2, T3, T4, T5, T6, T7>> arbitrary, Shrink<Tuple7<T1, T2, T3, T4, T5, T6, T7>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple7<T1, T2, T3, T4, T5, T6, T7>> implies(CheckedFunction7<T1, T2, T3, T4, T5, T6, T7, Boolean> postcondition) {
            final CheckedFunction1<Tuple7<T1, T2, T3, T4, T5, T6, T7>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property7<>(name, implication, arbitrary, shrink);
        }

        public Property7<T1, T2, T3, T4, T5, T6, T7> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6, Shrink<T7> s7) {
            return new Property7<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3, s4, s5, s6, s7));
        }
    }

    /**
     * Represents a 8-ary checkable property.
     *
     * @author Daniel Dietrich
     * @since 1.2.0
     */
    public static class Property8<T1, T2, T3, T4, T5, T6, T7, T8> extends PropertyCheck<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> {

        Property8(String name, CheckedFunction1<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, Condition> predicate, Arbitrary<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> arbitrary, Shrink<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> shrink) {
            super(name, arbitrary, predicate, shrink);
        }

        /**
         * Returns an implication which composes this Property as pre-condition and a given post-condition.
         *
         * @param postcondition The postcondition of this implication
         * @return A new Checkable implication
         */
        public Checkable<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> implies(CheckedFunction8<T1, T2, T3, T4, T5, T6, T7, T8, Boolean> postcondition) {
            final CheckedFunction1<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, Condition> implication = (t) -> {
                final Condition precondition = predicate.apply(t);
                if (precondition.isFalse()) {
                    return Condition.EX_FALSO_QUODLIBET;
                } else {
                    return new Condition(true, postcondition.tupled().apply(t));
                }
            };
            return new Property8<>(name, implication, arbitrary, shrink);
        }

        public Property8<T1, T2, T3, T4, T5, T6, T7, T8> shrinking(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6, Shrink<T7> s7, Shrink<T8> s8) {
            return new Property8<>(name, predicate, arbitrary, ShrinkTuple.of(s1, s2, s3, s4, s5, s6, s7, s8));
        }
    }

    /**
     * Internally used to model conditions composed of pre- and post-condition.
     */
    static class Condition {

        static final Condition EX_FALSO_QUODLIBET = new Condition(false, true);

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