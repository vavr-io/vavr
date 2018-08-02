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
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A tuple of no elements which can be seen as cartesian product of no components.
 *
 * @author Daniel Dietrich
 */
public final class Tuple0 implements Comparable<Tuple0>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of Tuple0.
     */
    private static final Tuple0 INSTANCE = new Tuple0 ();

    /**
     * The singleton Tuple0 comparator.
     */
    private static final Comparator<Tuple0> COMPARATOR = (Comparator<Tuple0> & Serializable) (t1, t2) -> 0;

    // hidden constructor, internally called
    private Tuple0 () {
    }

    /**
     * Returns the singleton instance of Tuple0.
     *
     * @return The singleton instance of Tuple0.
     */
    public static Tuple0 instance() {
        return INSTANCE;
    }

    public static  Comparator<Tuple0> comparator() {
        return COMPARATOR;
    }

    @Override
    public int compareTo(Tuple0 that) {
        return 0;
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(Supplier<? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.get();
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T1> type of the value to append
     * @param t1 the value to append
     * @return a new Tuple with the value appended
     */
    public <T1> Tuple1<T1> append(T1 t1) {
        return Tuple.of(t1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1> Tuple1<T1> concat(Tuple1<T1> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2> Tuple2<T1, T2> concat(Tuple2<T1, T2> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3> Tuple3<T1, T2, T3> concat(Tuple3<T1, T2, T3> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> concat(Tuple4<T1, T2, T3, T4> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(Tuple5<T1, T2, T3, T4, T5> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param <T7> the type of the 7th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6, tuple._7);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T1> the type of the 1st value in the tuple
     * @param <T2> the type of the 2nd value in the tuple
     * @param <T3> the type of the 3rd value in the tuple
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param <T7> the type of the 7th value in the tuple
     * @param <T8> the type of the 8th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6, tuple._7, tuple._8);
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "()";
    }

    // -- Serializable implementation

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Tuple0.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }

}