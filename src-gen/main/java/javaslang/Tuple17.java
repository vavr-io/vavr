/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;

/**
 * Implementation of a pair, a tuple containing 17 elements.
 */
public class Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> implements Tuple {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;
    public final T7 _7;
    public final T8 _8;
    public final T9 _9;
    public final T10 _10;
    public final T11 _11;
    public final T12 _12;
    public final T13 _13;
    public final T14 _14;
    public final T15 _15;
    public final T16 _16;
    public final T17 _17;

    public Tuple17(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7, T8 t8, T9 t9, T10 t10, T11 t11, T12 t12, T13 t13, T14 t14, T15 t15, T16 t16, T17 t17) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
        this._7 = t7;
        this._8 = t8;
        this._9 = t9;
        this._10 = t10;
        this._11 = t11;
        this._12 = t12;
        this._13 = t13;
        this._14 = t14;
        this._15 = t15;
        this._16 = t16;
        this._17 = t17;
    }

    @Override
    public int arity() {
        return 17;
    }

    @Override
    public Tuple17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple17)) {
            return false;
        } else {
            final Tuple17 that = (Tuple17) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4)
                    && Objects.equals(this._5, that._5)
                    && Objects.equals(this._6, that._6)
                    && Objects.equals(this._7, that._7)
                    && Objects.equals(this._8, that._8)
                    && Objects.equals(this._9, that._9)
                    && Objects.equals(this._10, that._10)
                    && Objects.equals(this._11, that._11)
                    && Objects.equals(this._12, that._12)
                    && Objects.equals(this._13, that._13)
                    && Objects.equals(this._14, that._14)
                    && Objects.equals(this._15, that._15)
                    && Objects.equals(this._16, that._16)
                    && Objects.equals(this._17, that._17);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15, _16, _17);
    }
}