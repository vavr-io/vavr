/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2022 Vavr, https://vavr.io
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

/**
 * A tuple of 6 elements which can be seen as cartesian product of 6 components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 */
public final class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * The 2nd element of this tuple.
     */
    public final T2 _2;

    /**
     * The 3rd element of this tuple.
     */
    public final T3 _3;

    /**
     * The 4th element of this tuple.
     */
    public final T4 _4;

    /**
     * The 5th element of this tuple.
     */
    public final T5 _5;

    /**
     * The 6th element of this tuple.
     */
    public final T6 _6;

    /**
     * Constructs a tuple of 6 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     */
    public Tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
    }

    public static <T1, T2, T3, T4, T5, T6> Comparator<Tuple6<T1, T2, T3, T4, T5, T6>> comparator(Comparator<? super T1> t1Comp, Comparator<? super T2> t2Comp, Comparator<? super T3> t3Comp, Comparator<? super T4> t4Comp, Comparator<? super T5> t5Comp, Comparator<? super T6> t6Comp) {
        return (Comparator<Tuple6<T1, T2, T3, T4, T5, T6>> & Serializable) (t1, t2) -> {
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

            final int check4 = t4Comp.compare(t1._4, t2._4);
            if (check4 != 0) {
                return check4;
            }

            final int check5 = t5Comp.compare(t1._5, t2._5);
            if (check5 != 0) {
                return check5;
            }

            final int check6 = t6Comp.compare(t1._6, t2._6);
            if (check6 != 0) {
                return check6;
            }

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>, U2 extends Comparable<? super U2>, U3 extends Comparable<? super U3>, U4 extends Comparable<? super U4>, U5 extends Comparable<? super U5>, U6 extends Comparable<? super U6>> int compareTo(Tuple6<?, ?, ?, ?, ?, ?> o1, Tuple6<?, ?, ?, ?, ?, ?> o2) {
        final Tuple6<U1, U2, U3, U4, U5, U6> t1 = (Tuple6<U1, U2, U3, U4, U5, U6>) o1;
        final Tuple6<U1, U2, U3, U4, U5, U6> t2 = (Tuple6<U1, U2, U3, U4, U5, U6>) o2;

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

        final int check4 = t1._4.compareTo(t2._4);
        if (check4 != 0) {
            return check4;
        }

        final int check5 = t1._5.compareTo(t2._5);
        if (check5 != 0) {
            return check5;
        }

        final int check6 = t1._6.compareTo(t2._6);
        if (check6 != 0) {
            return check6;
        }

        // all components are equal
        return 0;
    }

    @Override
    public int arity() {
        return 6;
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
    public Tuple6<T1, T2, T3, T4, T5, T6> update1(T1 value) {
        return new Tuple6<>(value, _2, _3, _4, _5, _6);
    }

    /**
     * Remove the 1st value from this tuple.
     *
     * @return a copy of this tuple with the 1st value element removed.
     */
    public Tuple5<T2, T3, T4, T5, T6> remove1() {
        return Tuple.of(_2, _3, _4, _5, _6);
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
    public Tuple6<T1, T2, T3, T4, T5, T6> update2(T2 value) {
        return new Tuple6<>(_1, value, _3, _4, _5, _6);
    }

    /**
     * Remove the 2nd value from this tuple.
     *
     * @return a copy of this tuple with the 2nd value element removed.
     */
    public Tuple5<T1, T3, T4, T5, T6> remove2() {
        return Tuple.of(_1, _3, _4, _5, _6);
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
    public Tuple6<T1, T2, T3, T4, T5, T6> update3(T3 value) {
        return new Tuple6<>(_1, _2, value, _4, _5, _6);
    }

    /**
     * Remove the 3rd value from this tuple.
     *
     * @return a copy of this tuple with the 3rd value element removed.
     */
    public Tuple5<T1, T2, T4, T5, T6> remove3() {
        return Tuple.of(_1, _2, _4, _5, _6);
    }

    /**
     * Getter of the 4th element of this tuple.
     *
     * @return the 4th element of this Tuple.
     */
    public T4 _4() {
        return _4;
    }

    /**
     * Sets the 4th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 4th element of this Tuple.
     */
    public Tuple6<T1, T2, T3, T4, T5, T6> update4(T4 value) {
        return new Tuple6<>(_1, _2, _3, value, _5, _6);
    }

    /**
     * Remove the 4th value from this tuple.
     *
     * @return a copy of this tuple with the 4th value element removed.
     */
    public Tuple5<T1, T2, T3, T5, T6> remove4() {
        return Tuple.of(_1, _2, _3, _5, _6);
    }

    /**
     * Getter of the 5th element of this tuple.
     *
     * @return the 5th element of this Tuple.
     */
    public T5 _5() {
        return _5;
    }

    /**
     * Sets the 5th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 5th element of this Tuple.
     */
    public Tuple6<T1, T2, T3, T4, T5, T6> update5(T5 value) {
        return new Tuple6<>(_1, _2, _3, _4, value, _6);
    }

    /**
     * Remove the 5th value from this tuple.
     *
     * @return a copy of this tuple with the 5th value element removed.
     */
    public Tuple5<T1, T2, T3, T4, T6> remove5() {
        return Tuple.of(_1, _2, _3, _4, _6);
    }

    /**
     * Getter of the 6th element of this tuple.
     *
     * @return the 6th element of this Tuple.
     */
    public T6 _6() {
        return _6;
    }

    /**
     * Sets the 6th element of this tuple to the given {@code value}.
     *
     * @param value the new value
     * @return a copy of this tuple with a new value for the 6th element of this Tuple.
     */
    public Tuple6<T1, T2, T3, T4, T5, T6> update6(T6 value) {
        return new Tuple6<>(_1, _2, _3, _4, _5, value);
    }

    /**
     * Remove the 6th value from this tuple.
     *
     * @return a copy of this tuple with the 6th value element removed.
     */
    public Tuple5<T1, T2, T3, T4, T5> remove6() {
        return Tuple.of(_1, _2, _3, _4, _5);
    }

    /**
     * Maps the components of this tuple using a mapper function.
     *
     * @param mapper the mapper function
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @param <U5> new type of the 5th component
     * @param <U6> new type of the 6th component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<U1, U2, U3, U4, U5, U6>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapper.apply(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Maps the components of this tuple using a mapper function for each component.
     *
     * @param f1 the mapper function of the 1st component
     * @param f2 the mapper function of the 2nd component
     * @param f3 the mapper function of the 3rd component
     * @param f4 the mapper function of the 4th component
     * @param f5 the mapper function of the 5th component
     * @param f6 the mapper function of the 6th component
     * @param <U1> new type of the 1st component
     * @param <U2> new type of the 2nd component
     * @param <U3> new type of the 3rd component
     * @param <U4> new type of the 4th component
     * @param <U5> new type of the 5th component
     * @param <U6> new type of the 6th component
     * @return A new Tuple of same arity.
     * @throws NullPointerException if one of the arguments is null
     */
    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function<? super T1, ? extends U1> f1, Function<? super T2, ? extends U2> f2, Function<? super T3, ? extends U3> f3, Function<? super T4, ? extends U4> f4, Function<? super T5, ? extends U5> f5, Function<? super T6, ? extends U6> f6) {
        Objects.requireNonNull(f1, "f1 is null");
        Objects.requireNonNull(f2, "f2 is null");
        Objects.requireNonNull(f3, "f3 is null");
        Objects.requireNonNull(f4, "f4 is null");
        Objects.requireNonNull(f5, "f5 is null");
        Objects.requireNonNull(f6, "f6 is null");
        return Tuple.of(f1.apply(_1), f2.apply(_2), f3.apply(_3), f4.apply(_4), f5.apply(_5), f6.apply(_6));
    }

    /**
     * Maps the 1st component of this tuple to a new value.
     *
     * @param <U> new type of the 1st component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 1st component
     */
    public <U> Tuple6<U, T2, T3, T4, T5, T6> map1(Function<? super T1, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_1);
        return Tuple.of(u, _2, _3, _4, _5, _6);
    }

    /**
     * Maps the 2nd component of this tuple to a new value.
     *
     * @param <U> new type of the 2nd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 2nd component
     */
    public <U> Tuple6<T1, U, T3, T4, T5, T6> map2(Function<? super T2, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_2);
        return Tuple.of(_1, u, _3, _4, _5, _6);
    }

    /**
     * Maps the 3rd component of this tuple to a new value.
     *
     * @param <U> new type of the 3rd component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 3rd component
     */
    public <U> Tuple6<T1, T2, U, T4, T5, T6> map3(Function<? super T3, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_3);
        return Tuple.of(_1, _2, u, _4, _5, _6);
    }

    /**
     * Maps the 4th component of this tuple to a new value.
     *
     * @param <U> new type of the 4th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 4th component
     */
    public <U> Tuple6<T1, T2, T3, U, T5, T6> map4(Function<? super T4, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_4);
        return Tuple.of(_1, _2, _3, u, _5, _6);
    }

    /**
     * Maps the 5th component of this tuple to a new value.
     *
     * @param <U> new type of the 5th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 5th component
     */
    public <U> Tuple6<T1, T2, T3, T4, U, T6> map5(Function<? super T5, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_5);
        return Tuple.of(_1, _2, _3, _4, u, _6);
    }

    /**
     * Maps the 6th component of this tuple to a new value.
     *
     * @param <U> new type of the 6th component
     * @param mapper A mapping function
     * @return a new tuple based on this tuple and substituted 6th component
     */
    public <U> Tuple6<T1, T2, T3, T4, T5, U> map6(Function<? super T6, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        final U u = mapper.apply(_6);
        return Tuple.of(_1, _2, _3, _4, _5, u);
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(_1, _2, _3, _4, _5, _6);
    }

    @Override
    public Seq<?> toSeq() {
        return List.of(_1, _2, _3, _4, _5, _6);
    }

    /**
     * Prepend a value to this tuple.
     *
     * @param <T> type of the value to prepend
     * @param t the value to prepend
     * @return a new Tuple with the value prepended
     */
    public <T> Tuple7<T, T1, T2, T3, T4, T5, T6> prepend(T t) {
        return Tuple.of(t, _1, _2, _3, _4, _5, _6);
    }

    /**
     * Append a value to this tuple.
     *
     * @param <T> type of the value to append
     * @param t the value to append
     * @return a new Tuple with the value appended
     */
    public <T> Tuple7<T1, T2, T3, T4, T5, T6, T> append(T t) {
        return Tuple.of(_1, _2, _3, _4, _5, _6, t);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T7> the type of the 7th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> concat(Tuple1<T7> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, _4, _5, _6, tuple._1);
    }

    /**
     * Concat a tuple's values to this tuple.
     *
     * @param <T7> the type of the 7th value in the tuple
     * @param <T8> the type of the 8th value in the tuple
     * @param tuple the tuple to concat
     * @return a new Tuple with the tuple values appended
     * @throws NullPointerException if {@code tuple} is null
     */
    public <T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> concat(Tuple2<T7, T8> tuple) {
        Objects.requireNonNull(tuple, "tuple is null");
        return Tuple.of(_1, _2, _3, _4, _5, _6, tuple._1, tuple._2);
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple6)) {
            return false;
        } else {
            final Tuple6<?, ?, ?, ?, ?, ?> that = (Tuple6<?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4)
                    && Objects.equals(this._5, that._5)
                    && Objects.equals(this._6, that._6);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6);
    }

    @Override
    public String toString() {
        return "(" + _1 + ", " + _2 + ", " + _3 + ", " + _4 + ", " + _5 + ", " + _6 + ")";
    }

}