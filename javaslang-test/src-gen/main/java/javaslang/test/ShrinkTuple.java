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
import javaslang.collection.Stream;

public class ShrinkTuple {
    /**
     * Generates a shrink for tuple of 1 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @return A new generator
     */
    public static <T1> Shrink<Tuple1<T1>> of(Shrink<T1> s1) {
        return t -> t.apply((t1) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a)))));
    }

    /**
     * Generates a shrink for tuple of 2 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @return A new generator
     */
    public static <T1, T2> Shrink<Tuple2<T1, T2>> of(Shrink<T1> s1, Shrink<T2> s2) {
        return t -> t.apply((t1, t2) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2)),
                s2.apply(t2).map(a -> Tuple.of(t1, a)))));
    }

    /**
     * Generates a shrink for tuple of 3 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3> Shrink<Tuple3<T1, T2, T3>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3) {
        return t -> t.apply((t1, t2, t3) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a)))));
    }

    /**
     * Generates a shrink for tuple of 4 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param s4 4th shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @param <T4> 4th type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4> Shrink<Tuple4<T1, T2, T3, T4>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4) {
        return t -> t.apply((t1, t2, t3, t4) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3, t4)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3, t4)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a, t4)),
                s4.apply(t4).map(a -> Tuple.of(t1, t2, t3, a)))));
    }

    /**
     * Generates a shrink for tuple of 5 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param s4 4th shrink of this tuple
     * @param s5 5th shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @param <T4> 4th type of this tuple
     * @param <T5> 5th type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5> Shrink<Tuple5<T1, T2, T3, T4, T5>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5) {
        return t -> t.apply((t1, t2, t3, t4, t5) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3, t4, t5)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3, t4, t5)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a, t4, t5)),
                s4.apply(t4).map(a -> Tuple.of(t1, t2, t3, a, t5)),
                s5.apply(t5).map(a -> Tuple.of(t1, t2, t3, t4, a)))));
    }

    /**
     * Generates a shrink for tuple of 6 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param s4 4th shrink of this tuple
     * @param s5 5th shrink of this tuple
     * @param s6 6th shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @param <T4> 4th type of this tuple
     * @param <T5> 5th type of this tuple
     * @param <T6> 6th type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6> Shrink<Tuple6<T1, T2, T3, T4, T5, T6>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6) {
        return t -> t.apply((t1, t2, t3, t4, t5, t6) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3, t4, t5, t6)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3, t4, t5, t6)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a, t4, t5, t6)),
                s4.apply(t4).map(a -> Tuple.of(t1, t2, t3, a, t5, t6)),
                s5.apply(t5).map(a -> Tuple.of(t1, t2, t3, t4, a, t6)),
                s6.apply(t6).map(a -> Tuple.of(t1, t2, t3, t4, t5, a)))));
    }

    /**
     * Generates a shrink for tuple of 7 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param s4 4th shrink of this tuple
     * @param s5 5th shrink of this tuple
     * @param s6 6th shrink of this tuple
     * @param s7 7th shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @param <T4> 4th type of this tuple
     * @param <T5> 5th type of this tuple
     * @param <T6> 6th type of this tuple
     * @param <T7> 7th type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6, T7> Shrink<Tuple7<T1, T2, T3, T4, T5, T6, T7>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6, Shrink<T7> s7) {
        return t -> t.apply((t1, t2, t3, t4, t5, t6, t7) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3, t4, t5, t6, t7)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3, t4, t5, t6, t7)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a, t4, t5, t6, t7)),
                s4.apply(t4).map(a -> Tuple.of(t1, t2, t3, a, t5, t6, t7)),
                s5.apply(t5).map(a -> Tuple.of(t1, t2, t3, t4, a, t6, t7)),
                s6.apply(t6).map(a -> Tuple.of(t1, t2, t3, t4, t5, a, t7)),
                s7.apply(t7).map(a -> Tuple.of(t1, t2, t3, t4, t5, t6, a)))));
    }

    /**
     * Generates a shrink for tuple of 8 given shrinks
     *
     * @param s1 1st shrink of this tuple
     * @param s2 2nd shrink of this tuple
     * @param s3 3rd shrink of this tuple
     * @param s4 4th shrink of this tuple
     * @param s5 5th shrink of this tuple
     * @param s6 6th shrink of this tuple
     * @param s7 7th shrink of this tuple
     * @param s8 8th shrink of this tuple
     * @param <T1> 1st type of this tuple
     * @param <T2> 2nd type of this tuple
     * @param <T3> 3rd type of this tuple
     * @param <T4> 4th type of this tuple
     * @param <T5> 5th type of this tuple
     * @param <T6> 6th type of this tuple
     * @param <T7> 7th type of this tuple
     * @param <T8> 8th type of this tuple
     * @return A new generator
     */
    public static <T1, T2, T3, T4, T5, T6, T7, T8> Shrink<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> of(Shrink<T1> s1, Shrink<T2> s2, Shrink<T3> s3, Shrink<T4> s4, Shrink<T5> s5, Shrink<T6> s6, Shrink<T7> s7, Shrink<T8> s8) {
        return t -> t.apply((t1, t2, t3, t4, t5, t6, t7, t8) -> concat(Stream.of(
                s1.apply(t1).map(a -> Tuple.of(a, t2, t3, t4, t5, t6, t7, t8)),
                s2.apply(t2).map(a -> Tuple.of(t1, a, t3, t4, t5, t6, t7, t8)),
                s3.apply(t3).map(a -> Tuple.of(t1, t2, a, t4, t5, t6, t7, t8)),
                s4.apply(t4).map(a -> Tuple.of(t1, t2, t3, a, t5, t6, t7, t8)),
                s5.apply(t5).map(a -> Tuple.of(t1, t2, t3, t4, a, t6, t7, t8)),
                s6.apply(t6).map(a -> Tuple.of(t1, t2, t3, t4, t5, a, t7, t8)),
                s7.apply(t7).map(a -> Tuple.of(t1, t2, t3, t4, t5, t6, a, t8)),
                s8.apply(t8).map(a -> Tuple.of(t1, t2, t3, t4, t5, t6, t7, a)))));
    }

    private static <T> Stream<T> concat(Stream<Stream<T>> streams) {
        return streams.foldLeft(Stream.empty(), Stream::appendAll);
    }
}