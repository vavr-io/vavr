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

import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import java.util.Map;
import java.util.Objects;

/**
 * The base interface of all tuples.
 *
 * @author Daniel Dietrich
 */
public interface Tuple {

    /**
     * The maximum arity of an Tuple.
     * <p>
     * Note: This value might be changed in a future version of Vavr.
     * So it is recommended to use this constant instead of hardcoding the current maximum arity.
     */
    int MAX_ARITY = 8;

    /**
     * Returns the number of elements of this tuple.
     *
     * @return the number of elements.
     */
    int arity();

    /**
     * Converts this tuple to a sequence.
     *
     * @return A new {@code Seq}.
     */
    Seq<?> toSeq();

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
     * Creates a {@code Tuple2} from a {@link Map.Entry}.
     *
     * @param <T1> Type of first component (entry key)
     * @param <T2> Type of second component (entry value)
     * @param      entry A {@link java.util.Map.Entry}
     * @return a new {@code Tuple2} containing key and value of the given {@code entry}
     */
    static <T1, T2> Tuple2<T1, T2> fromEntry(Map.Entry<? extends T1, ? extends T2> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return new Tuple2<>(entry.getKey(), entry.getValue());
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
     * Return the order-dependent hash of the one given value.
     *
     * @param o1 the 1st value to hash
     * @return the same result as {@link Objects#hashCode(Object)}
     */
    static int hash(Object o1) {
        return Objects.hashCode(o1);
    }

    /**
     * Return the order-dependent hash of the two given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        return result;
    }

    /**
     * Return the order-dependent hash of the three given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        return result;
    }

    /**
     * Return the order-dependent hash of the 4 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3, Object o4) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        result = 31 * result + hash(o4);
        return result;
    }

    /**
     * Return the order-dependent hash of the 5 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @param o5 the 5th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3, Object o4, Object o5) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        result = 31 * result + hash(o4);
        result = 31 * result + hash(o5);
        return result;
    }

    /**
     * Return the order-dependent hash of the 6 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @param o5 the 5th value to hash
     * @param o6 the 6th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        result = 31 * result + hash(o4);
        result = 31 * result + hash(o5);
        result = 31 * result + hash(o6);
        return result;
    }

    /**
     * Return the order-dependent hash of the 7 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @param o5 the 5th value to hash
     * @param o6 the 6th value to hash
     * @param o7 the 7th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        result = 31 * result + hash(o4);
        result = 31 * result + hash(o5);
        result = 31 * result + hash(o6);
        result = 31 * result + hash(o7);
        return result;
    }

    /**
     * Return the order-dependent hash of the 8 given values.
     *
     * @param o1 the 1st value to hash
     * @param o2 the 2nd value to hash
     * @param o3 the 3rd value to hash
     * @param o4 the 4th value to hash
     * @param o5 the 5th value to hash
     * @param o6 the 6th value to hash
     * @param o7 the 7th value to hash
     * @param o8 the 8th value to hash
     * @return the same result as {@link Objects#hash(Object...)}
     */
    static int hash(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6, Object o7, Object o8) {
        int result = 1;
        result = 31 * result + hash(o1);
        result = 31 * result + hash(o2);
        result = 31 * result + hash(o3);
        result = 31 * result + hash(o4);
        result = 31 * result + hash(o5);
        result = 31 * result + hash(o6);
        result = 31 * result + hash(o7);
        result = 31 * result + hash(o8);
        return result;
    }

    /**
     * Narrows a widened {@code Tuple1<? extends T1>} to {@code Tuple1<T1>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple1}.
     * @param <T1> the 1st component type
     * @return the given {@code t} instance as narrowed type {@code Tuple1<T1>}.
     */
    @SuppressWarnings("unchecked")
    static <T1> Tuple1<T1> narrow(Tuple1<? extends T1> t) {
        return (Tuple1<T1>) t;
    }

    /**
     * Narrows a widened {@code Tuple2<? extends T1, ? extends T2>} to {@code Tuple2<T1, T2>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple2}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @return the given {@code t} instance as narrowed type {@code Tuple2<T1, T2>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2> Tuple2<T1, T2> narrow(Tuple2<? extends T1, ? extends T2> t) {
        return (Tuple2<T1, T2>) t;
    }

    /**
     * Narrows a widened {@code Tuple3<? extends T1, ? extends T2, ? extends T3>} to {@code Tuple3<T1, T2, T3>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple3}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @return the given {@code t} instance as narrowed type {@code Tuple3<T1, T2, T3>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3> Tuple3<T1, T2, T3> narrow(Tuple3<? extends T1, ? extends T2, ? extends T3> t) {
        return (Tuple3<T1, T2, T3>) t;
    }

    /**
     * Narrows a widened {@code Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>} to {@code Tuple4<T1, T2, T3, T4>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple4}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple4<T1, T2, T3, T4>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> narrow(Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4> t) {
        return (Tuple4<T1, T2, T3, T4>) t;
    }

    /**
     * Narrows a widened {@code Tuple5<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5>} to {@code Tuple5<T1, T2, T3, T4, T5>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple5}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @param <T5> the 5th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple5<T1, T2, T3, T4, T5>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> narrow(Tuple5<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5> t) {
        return (Tuple5<T1, T2, T3, T4, T5>) t;
    }

    /**
     * Narrows a widened {@code Tuple6<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6>} to {@code Tuple6<T1, T2, T3, T4, T5, T6>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple6}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @param <T5> the 5th component type
     * @param <T6> the 6th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple6<T1, T2, T3, T4, T5, T6>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> narrow(Tuple6<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6> t) {
        return (Tuple6<T1, T2, T3, T4, T5, T6>) t;
    }

    /**
     * Narrows a widened {@code Tuple7<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7>} to {@code Tuple7<T1, T2, T3, T4, T5, T6, T7>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple7}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @param <T5> the 5th component type
     * @param <T6> the 6th component type
     * @param <T7> the 7th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple7<T1, T2, T3, T4, T5, T6, T7>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> narrow(Tuple7<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7> t) {
        return (Tuple7<T1, T2, T3, T4, T5, T6, T7>) t;
    }

    /**
     * Narrows a widened {@code Tuple8<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7, ? extends T8>} to {@code Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>}.
     * This is eligible because immutable/read-only tuples are covariant.
     * @param t A {@code Tuple8}.
     * @param <T1> the 1st component type
     * @param <T2> the 2nd component type
     * @param <T3> the 3rd component type
     * @param <T4> the 4th component type
     * @param <T5> the 5th component type
     * @param <T6> the 6th component type
     * @param <T7> the 7th component type
     * @param <T8> the 8th component type
     * @return the given {@code t} instance as narrowed type {@code Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>}.
     */
    @SuppressWarnings("unchecked")
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> narrow(Tuple8<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7, ? extends T8> t) {
        return (Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>) t;
    }

    /**
     * Turns a sequence of {@code Tuple1} into a Tuple1 of {@code Seq}.
     *
     * @param <T1> 1st component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of one {@link Seq}.
     */
    static <T1> Tuple1<Seq<T1>> sequence1(Iterable<? extends Tuple1<? extends T1>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple1<? extends T1>> s = Stream.ofAll(tuples);
        return new Tuple1<>(s.map(Tuple1::_1));
    }

    /**
     * Turns a sequence of {@code Tuple2} into a Tuple2 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of two {@link Seq}s.
     */
    static <T1, T2> Tuple2<Seq<T1>, Seq<T2>> sequence2(Iterable<? extends Tuple2<? extends T1, ? extends T2>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple2<? extends T1, ? extends T2>> s = Stream.ofAll(tuples);
        return new Tuple2<>(s.map(Tuple2::_1), s.map(Tuple2::_2));
    }

    /**
     * Turns a sequence of {@code Tuple3} into a Tuple3 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of three {@link Seq}s.
     */
    static <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> sequence3(Iterable<? extends Tuple3<? extends T1, ? extends T2, ? extends T3>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple3<? extends T1, ? extends T2, ? extends T3>> s = Stream.ofAll(tuples);
        return new Tuple3<>(s.map(Tuple3::_1), s.map(Tuple3::_2), s.map(Tuple3::_3));
    }

    /**
     * Turns a sequence of {@code Tuple4} into a Tuple4 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 4 {@link Seq}s.
     */
    static <T1, T2, T3, T4> Tuple4<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>> sequence4(Iterable<? extends Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple4<? extends T1, ? extends T2, ? extends T3, ? extends T4>> s = Stream.ofAll(tuples);
        return new Tuple4<>(s.map(Tuple4::_1), s.map(Tuple4::_2), s.map(Tuple4::_3), s.map(Tuple4::_4));
    }

    /**
     * Turns a sequence of {@code Tuple5} into a Tuple5 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param <T5> 5th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 5 {@link Seq}s.
     */
    static <T1, T2, T3, T4, T5> Tuple5<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>, Seq<T5>> sequence5(Iterable<? extends Tuple5<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple5<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5>> s = Stream.ofAll(tuples);
        return new Tuple5<>(s.map(Tuple5::_1), s.map(Tuple5::_2), s.map(Tuple5::_3), s.map(Tuple5::_4), s.map(Tuple5::_5));
    }

    /**
     * Turns a sequence of {@code Tuple6} into a Tuple6 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param <T5> 5th component type
     * @param <T6> 6th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 6 {@link Seq}s.
     */
    static <T1, T2, T3, T4, T5, T6> Tuple6<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>, Seq<T5>, Seq<T6>> sequence6(Iterable<? extends Tuple6<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple6<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6>> s = Stream.ofAll(tuples);
        return new Tuple6<>(s.map(Tuple6::_1), s.map(Tuple6::_2), s.map(Tuple6::_3), s.map(Tuple6::_4), s.map(Tuple6::_5), s.map(Tuple6::_6));
    }

    /**
     * Turns a sequence of {@code Tuple7} into a Tuple7 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param <T5> 5th component type
     * @param <T6> 6th component type
     * @param <T7> 7th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 7 {@link Seq}s.
     */
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>, Seq<T5>, Seq<T6>, Seq<T7>> sequence7(Iterable<? extends Tuple7<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple7<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7>> s = Stream.ofAll(tuples);
        return new Tuple7<>(s.map(Tuple7::_1), s.map(Tuple7::_2), s.map(Tuple7::_3), s.map(Tuple7::_4), s.map(Tuple7::_5), s.map(Tuple7::_6), s.map(Tuple7::_7));
    }

    /**
     * Turns a sequence of {@code Tuple8} into a Tuple8 of {@code Seq}s.
     *
     * @param <T1> 1st component type
     * @param <T2> 2nd component type
     * @param <T3> 3rd component type
     * @param <T4> 4th component type
     * @param <T5> 5th component type
     * @param <T6> 6th component type
     * @param <T7> 7th component type
     * @param <T8> 8th component type
     * @param tuples an {@code Iterable} of tuples
     * @return a tuple of 8 {@link Seq}s.
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<Seq<T1>, Seq<T2>, Seq<T3>, Seq<T4>, Seq<T5>, Seq<T6>, Seq<T7>, Seq<T8>> sequence8(Iterable<? extends Tuple8<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7, ? extends T8>> tuples) {
        Objects.requireNonNull(tuples, "tuples is null");
        final Stream<Tuple8<? extends T1, ? extends T2, ? extends T3, ? extends T4, ? extends T5, ? extends T6, ? extends T7, ? extends T8>> s = Stream.ofAll(tuples);
        return new Tuple8<>(s.map(Tuple8::_1), s.map(Tuple8::_2), s.map(Tuple8::_3), s.map(Tuple8::_4), s.map(Tuple8::_5), s.map(Tuple8::_6), s.map(Tuple8::_7), s.map(Tuple8::_8));
    }

}