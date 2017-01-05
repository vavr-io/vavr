/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.*;

public class ArbitraryTuple {
    /**
     * Generates an arbitrary tuple of 1 given variables
     *
     * @param a1 1st variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @return A new generator
     */
    public static <T1> Arbitrary<Tuple1<T1>> of(Arbitrary<T1> a1) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 2 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2> Arbitrary<Tuple2<T1, T2>> of(Arbitrary<T1> a1, Arbitrary<T2> a2) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 3 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3> Arbitrary<Tuple3<T1, T2, T3>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 4 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param a4 4th variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @param <T4> 4th variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4> Arbitrary<Tuple4<T1, T2, T3, T4>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random),
                a4.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 5 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param a4 4th variable of this tuple
     * @param a5 5th variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @param <T4> 4th variable type of this tuple
     * @param <T5> 5th variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5> Arbitrary<Tuple5<T1, T2, T3, T4, T5>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random),
                a4.apply(size).apply(random),
                a5.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 6 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param a4 4th variable of this tuple
     * @param a5 5th variable of this tuple
     * @param a6 6th variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @param <T4> 4th variable type of this tuple
     * @param <T5> 5th variable type of this tuple
     * @param <T6> 6th variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6> Arbitrary<Tuple6<T1, T2, T3, T4, T5, T6>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random),
                a4.apply(size).apply(random),
                a5.apply(size).apply(random),
                a6.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 7 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param a4 4th variable of this tuple
     * @param a5 5th variable of this tuple
     * @param a6 6th variable of this tuple
     * @param a7 7th variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @param <T4> 4th variable type of this tuple
     * @param <T5> 5th variable type of this tuple
     * @param <T6> 6th variable type of this tuple
     * @param <T7> 7th variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Arbitrary<Tuple7<T1, T2, T3, T4, T5, T6, T7>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random),
                a4.apply(size).apply(random),
                a5.apply(size).apply(random),
                a6.apply(size).apply(random),
                a7.apply(size).apply(random));
    }

    /**
     * Generates an arbitrary tuple of 8 given variables
     *
     * @param a1 1st variable of this tuple
     * @param a2 2nd variable of this tuple
     * @param a3 3rd variable of this tuple
     * @param a4 4th variable of this tuple
     * @param a5 5th variable of this tuple
     * @param a6 6th variable of this tuple
     * @param a7 7th variable of this tuple
     * @param a8 8th variable of this tuple
     * @param <T1> 1st variable type of this tuple
     * @param <T2> 2nd variable type of this tuple
     * @param <T3> 3rd variable type of this tuple
     * @param <T4> 4th variable type of this tuple
     * @param <T5> 5th variable type of this tuple
     * @param <T6> 6th variable type of this tuple
     * @param <T7> 7th variable type of this tuple
     * @param <T8> 8th variable type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Arbitrary<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> of(Arbitrary<T1> a1, Arbitrary<T2> a2, Arbitrary<T3> a3, Arbitrary<T4> a4, Arbitrary<T5> a5, Arbitrary<T6> a6, Arbitrary<T7> a7, Arbitrary<T8> a8) {
        return size -> random -> Tuple.of(
                a1.apply(size).apply(random),
                a2.apply(size).apply(random),
                a3.apply(size).apply(random),
                a4.apply(size).apply(random),
                a5.apply(size).apply(random),
                a6.apply(size).apply(random),
                a7.apply(size).apply(random),
                a8.apply(size).apply(random));
    }
}