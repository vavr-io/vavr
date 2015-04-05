/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

/**
 * The base interface of all tuples.
 * @since 1.1.0
 */
public interface Tuple extends ValueObject {

    /**
     * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
     */
    long serialVersionUID = 1L;

    /**
     * Returns the number of elements of this tuple.
     *
     * @return the number of elements.
     */
    int arity();

    // -- factory methods

    /**
     * Creates the empty tuple.
     *
     * @return the empty tuple.
     */
    static Tuple0 empty() {
        return Tuple0.instance();
    }

    /**
     * Creates a tuple of one element.
     *
     * @param <T1> type of the 1st element
     * @param t1 the 1st element
     * @return a tuple of one element.
     */
    static <T1> Tuple1<T1> of(T1 t1) {
        return new Tuple1<>(t1);
    }

    /**
     * Creates a tuple of two elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @return a tuple of two elements.
     */
    static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new Tuple2<>(t1, t2);
    }

    /**
     * Creates a tuple of three elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @return a tuple of three elements.
     */
    static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 t1, T2 t2, T3 t3) {
        return new Tuple3<>(t1, t2, t3);
    }

    /**
     * Creates a tuple of 4 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @return a tuple of 4 elements.
     */
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 t1, T2 t2, T3 t3, T4 t4) {
        return new Tuple4<>(t1, t2, t3, t4);
    }

    /**
     * Creates a tuple of 5 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @return a tuple of 5 elements.
     */
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        return new Tuple5<>(t1, t2, t3, t4, t5);
    }

    /**
     * Creates a tuple of 6 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @return a tuple of 6 elements.
     */
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        return new Tuple6<>(t1, t2, t3, t4, t5, t6);
    }

    /**
     * Creates a tuple of 7 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @return a tuple of 7 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7) {
        return new Tuple7<>(t1, t2, t3, t4, t5, t6, t7);
    }

    /**
     * Creates a tuple of 8 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @return a tuple of 8 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        return new Tuple8<>(t1, t2, t3, t4, t5, t6, t7, t8);
    }

    /**
     * Creates a tuple of 9 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @return a tuple of 9 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        return new Tuple9<>(t1, t2, t3, t4, t5, t6, t7, t8, t9);
    }

    /**
     * Creates a tuple of 10 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @return a tuple of 10 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10) {
        return new Tuple10<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10);
    }

    /**
     * Creates a tuple of 11 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @return a tuple of 11 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Tuple11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11) {
        return new Tuple11<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11);
    }

    /**
     * Creates a tuple of 12 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @return a tuple of 12 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Tuple12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12) {
        return new Tuple12<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12);
    }

    /**
     * Creates a tuple of 13 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @return a tuple of 13 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Tuple13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13) {
        return new Tuple13<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13);
    }

    /**
     * Creates a tuple of 14 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @return a tuple of 14 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Tuple14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14) {
        return new Tuple14<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14);
    }

    /**
     * Creates a tuple of 15 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @return a tuple of 15 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Tuple15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15) {
        return new Tuple15<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15);
    }

    /**
     * Creates a tuple of 16 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @return a tuple of 16 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Tuple16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16) {
        return new Tuple16<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16);
    }

    /**
     * Creates a tuple of 17 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @return a tuple of 17 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        return new Tuple17<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17);
    }

    /**
     * Creates a tuple of 18 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @return a tuple of 18 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> Tuple18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18) {
        return new Tuple18<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18);
    }

    /**
     * Creates a tuple of 19 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @return a tuple of 19 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> Tuple19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19) {
        return new Tuple19<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19);
    }

    /**
     * Creates a tuple of 20 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @return a tuple of 20 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> Tuple20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20) {
        return new Tuple20<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20);
    }

    /**
     * Creates a tuple of 21 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @return a tuple of 21 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> Tuple21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21) {
        return new Tuple21<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21);
    }

    /**
     * Creates a tuple of 22 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param <T22> type of the 2nd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @param t22 the 2nd element
     * @return a tuple of 22 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> Tuple22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22) {
        return new Tuple22<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22);
    }

    /**
     * Creates a tuple of 23 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param <T22> type of the 2nd element
     * @param <T23> type of the 3rd element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @param t22 the 2nd element
     * @param t23 the 3rd element
     * @return a tuple of 23 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> Tuple23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22, T23 t23) {
        return new Tuple23<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23);
    }

    /**
     * Creates a tuple of 24 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param <T22> type of the 2nd element
     * @param <T23> type of the 3rd element
     * @param <T24> type of the 24th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @param t22 the 2nd element
     * @param t23 the 3rd element
     * @param t24 the 24th element
     * @return a tuple of 24 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> Tuple24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22, T23 t23, T24 t24) {
        return new Tuple24<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24);
    }

    /**
     * Creates a tuple of 25 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param <T22> type of the 2nd element
     * @param <T23> type of the 3rd element
     * @param <T24> type of the 24th element
     * @param <T25> type of the 25th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @param t22 the 2nd element
     * @param t23 the 3rd element
     * @param t24 the 24th element
     * @param t25 the 25th element
     * @return a tuple of 25 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> Tuple25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22, T23 t23, T24 t24, T25 t25) {
        return new Tuple25<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25);
    }

    /**
     * Creates a tuple of 26 elements.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param <T9> type of the 9th element
     * @param <T10> type of the 10th element
     * @param <T11> type of the 11th element
     * @param <T12> type of the 12th element
     * @param <T13> type of the 13th element
     * @param <T14> type of the 14th element
     * @param <T15> type of the 15th element
     * @param <T16> type of the 16th element
     * @param <T17> type of the 17th element
     * @param <T18> type of the 18th element
     * @param <T19> type of the 19th element
     * @param <T20> type of the 20th element
     * @param <T21> type of the 1st element
     * @param <T22> type of the 2nd element
     * @param <T23> type of the 3rd element
     * @param <T24> type of the 24th element
     * @param <T25> type of the 25th element
     * @param <T26> type of the 26th element
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     * @param t10 the 10th element
     * @param t11 the 11th element
     * @param t12 the 12th element
     * @param t13 the 13th element
     * @param t14 the 14th element
     * @param t15 the 15th element
     * @param t16 the 16th element
     * @param t17 the 17th element
     * @param t18 the 18th element
     * @param t19 the 19th element
     * @param t20 the 20th element
     * @param t21 the 1st element
     * @param t22 the 2nd element
     * @param t23 the 3rd element
     * @param t24 the 24th element
     * @param t25 the 25th element
     * @param t26 the 26th element
     * @return a tuple of 26 elements.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> Tuple26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26> of(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17, T18 t18, T19 t19, T20 t20, T21 t21, T22 t22, T23 t23, T24 t24, T25 t25, T26 t26) {
        return new Tuple26<>(t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26);
    }
}