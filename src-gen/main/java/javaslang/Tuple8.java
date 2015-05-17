/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Objects;

/**
 * A tuple of 8 elements which can be seen as cartesian product of 8 components.
 *
 * @param <T1> type of the 1st element
 * @param <T2> type of the 2nd element
 * @param <T3> type of the 3rd element
 * @param <T4> type of the 4th element
 * @param <T5> type of the 5th element
 * @param <T6> type of the 6th element
 * @param <T7> type of the 7th element
 * @param <T8> type of the 8th element
 * @since 1.1.0
 */
public final class Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> implements Tuple, Serializable {

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
     * Constructs a tuple of 8 elements.
     *
     * @param t1 the 1st element
     * @param t2 the 2nd element
     * @param t3 the 3rd element
     * @param t4 the 4th element
     * @param t5 the 5th element
     * @param t6 the 6th element
     * @param t7 the 7th element
     * @param t8 the 8th element
     */
    public Tuple8(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
    }

    @Override
    public int arity() {
        return 8;
    }

    public <U1, U2, U3, U4, U5, U6, U7, U8> Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> map(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, Tuple8<U1, U2, U3, U4, U5, U6, U7, U8>> f) {
        return f.apply(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    public <U1, U2, U3, U4, U5, U6, U7, U8> Tuple8<U1, U2, U3, U4, U5, U6, U7, U8> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8)));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple8)) {
            return false;
        } else {
            final Tuple8<?, ?, ?, ?, ?, ?, ?, ?> that = (Tuple8<?, ?, ?, ?, ?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4)
                    && Objects.equals(this._5, that._5)
                    && Objects.equals(this._6, that._6)
                    && Objects.equals(this._7, that._7)
                    && Objects.equals(this._8, that._8);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8);
    }
}