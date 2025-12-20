/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;

/**
 * A tuple of three elements which can be seen as cartesian product of three components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @author Daniel Dietrich
 */
public final class Tuple3<T1, T2, T3> implements Tuple, Comparable<Tuple3<T1, T2, T3>>, Serializable {

    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T2 _2;

    /**
     * The 3rd element of this tuple.
     */
    @SuppressWarnings("serial") // Conditionally serializable
    public final T3 _3;

    /**
     * Constructs a tuple of three elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     */
    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
    }

    public static <T1, T2, T3> Comparator<Tuple3<T1, T2, T3>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp, Comparator<? super T3> t3Comp) {
        return (Comparator<Tuple3<T1, T2, T3>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            final int check2 = t2Comp.compare(t1._2, t2._2);
            if (check2 != 0) {
                return check2;
            }

            final int check3 = t3Comp.compare(t1._3, t2._3);
            if (check3 != 0) {
                return check3;
            }

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>> int compareTo(Tuple3<?, ?, ?> o1, Tuple3<?, ?, ?> o2) {
        final Tuple3<U1, U2, U3> t1 = (Tuple3<U1, U2, U3>) o1;
        final Tuple3<U1, U2, U3> t2 = (Tuple3<U1, U2, U3>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        final int check2 = t1._2.compareTo(t2._2);
        if (check2 != 0) {
            return check2;
        }

        final int check3 = t1._3.compareTo(t2._3);
        if (check3 != 0) {
            return check3;
        }

        // all components are equal
        return 0;
    }

    @Override
    public int arity() {
        return 3;
    }

    @Override
    public int compareTo(Tuple3<T1, T2, T3> that) {
        return Tuple3.compareTo(this, that);
    }

    /**
     * Getter of the 1st element of this tuple.
     *
     * @return the 1st element of this Tuple.
     */
    public T1 _1() {
        return _1;
    }

    /**
     * Sets the 1st element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 1st element of this Tuple.
     */
    public Tuple3<T1, T2, T3> update1(T1 value) {
        return new Tuple3<>(value, _2, _3);
    }

    /**
     * Getter of the 2nd element of this tuple.
     *
     * @return the 2nd element of this Tuple.
     */
    public T2 _2() {
        return _2;
    }

    /**
     * Sets the 2nd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 2nd element of this Tuple.
     */
    public Tuple3<T1, T2, T3> update2(T2 value) {
        return new Tuple3<>(_1, value, _3);
    }

    /**
     * Getter of the 3rd element of this tuple.
     *
     * @return the 3rd element of this Tuple.
     */
    public T3 _3() {
        return _3;
    }

    /**
     * Sets the 3rd element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 3rd element of this Tuple.
     */
    public Tuple3<T1, T2, T3> update3(T3 value) {
        return new Tuple3<>(_1, _2, value);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U1, U2, U3> Tuple3<U1, U2, U3> map(@NonNull Function3<? super T1, ? super T2, ? super T3, Tuple3<U1, U2, U3>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1 the mapper function of the 1st component
     * @param f2 the mapper function of the 2nd component
     * @param f3 the mapper function of the 3rd component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2, U3> Tuple3<U1, U2, U3> map(@NonNull Function<? super T1, ? extends U1> f1, @NonNull Function<? super T2, ? extends U2> f2, @NonNull Function<? super T3, ? extends U3> f3) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U> new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> Tuple3<U, T2, T3> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U> new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> Tuple3<T1, U, T3> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U> new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> Tuple3<T1, T2, U> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u);
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(@NonNull Function3<? super T1, ? super T2, ? super T3, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3);
    }

    @Override
    public Seq<?> toSeq() {
        return List.of(_1, _2, _3);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T4> type of the value to append
     * @param t4 the value to append
     * @return a new Tuple with the value appended
     */
    public <T4> Tuple4<T1, T2, T3, T4> append(T4 t4) {
        return Tuple.of(_1, _2, _3, t4);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T4> the type of the 4th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T4> Tuple4<T1, T2, T3, T4> concat(@NonNull Tuple1<T4> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T4, T5> Tuple5<T1, T2, T3, T4, T5> concat(@NonNull Tuple2<T4, T5> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1, tuple._2);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> concat(@NonNull Tuple3<T4, T5, T6> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1, tuple._2, tuple._3);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param <T7> the type of the 7th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(@NonNull Tuple4<T4, T5, T6, T7> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1, tuple._2, tuple._3, tuple._4);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T4> the type of the 4th value in the tuple
     * @param <T5> the type of the 5th value in the tuple
     * @param <T6> the type of the 6th value in the tuple
     * @param <T7> the type of the 7th value in the tuple
     * @param <T8> the type of the 8th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(@NonNull Tuple5<T4, T5, T6, T7, T8> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, tuple._1, tuple._2, tuple._3, tuple._4, tuple._5);
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple3)) {
            return false;
        } else {
            final Tuple3<?, ?, ?> that = (Tuple3<?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                  && Objects.equals(this._2, that._2)
                  && Objects.equals(this._3, that._3);
        }
    }

    @Override
    public int hashCode() {
        return Tuple.hash(_1, _2, _3);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ")";
    }

}