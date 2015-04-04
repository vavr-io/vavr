/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import javaslang.algebra.HigherKinded9;
import javaslang.algebra.Monad9;

/**
 * A tuple of 9 elements which can be seen as cartesian product of 9 components.
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
 */
public class Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> implements Tuple, Monad9<T1, T2, T3, T4, T5, T6, T7, T8, T9, Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>> {

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
     * The 7th element of this tuple.
     */
    public final T7 _7;

    /**
     * The 8th element of this tuple.
     */
    public final T8 _8;

    /**
     * The 9th element of this tuple.
     */
    public final T9 _9;

    /**
     * Constructs a tuple of 9 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     * @param t9 the 9th element
     */
    public Tuple9(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
        this._9 = t9;
    }

    @Override
    public int arity() {
        return 9;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5, U6, U7, U8, U9, TUPLE extends HigherKinded9<U1, U2, U3, U4, U5, U6, U7, U8, U9, Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>>> Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> flatMap(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, TUPLE> f) {
        return (Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9>) f.apply(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5, U6, U7, U8, U9> Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> map(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, Tuple9<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9>) f.apply(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @Override
    public <U1, U2, U3, U4, U5, U6, U7, U8, U9> Tuple9<U1, U2, U3, U4, U5, U6, U7, U8, U9> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9)));
    }

    @Override
    public Tuple9<T1, T2, T3, T4, T5, T6, T7, T8, T9> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple9)) {
            return false;
        } else {
            final Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple9<?, ?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4)
                    && Objects.equals(this._5, that._5)
                    && Objects.equals(this._6, that._6)
                    && Objects.equals(this._7, that._7)
                    && Objects.equals(this._8, that._8)
                    && Objects.equals(this._9, that._9);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9);
    }
}