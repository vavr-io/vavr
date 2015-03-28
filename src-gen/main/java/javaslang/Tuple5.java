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
import javaslang.algebra.HigherKinded5;
import javaslang.algebra.Monad5;

/**
 * Implementation of a pair, a tuple containing 5 elements.
 */
public class Tuple5<T1, T2, T3, T4, T5> implements Tuple, Monad5<T1, T2, T3, T4, T5, Tuple5<?, ?, ?, ?, ?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;

    public Tuple5(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
    }

    @Override
    public int arity() {
        return 5;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5, TUPLE extends HigherKinded5<U1, U2, U3, U4, U5, Tuple5<?, ?, ?, ?, ?>>> Tuple5<U1, U2, U3, U4, U5> flatMap(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, TUPLE> f) {
        return (Tuple5<U1, U2, U3, U4, U5>) f.apply(_1, _2, _3, _4, _5);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5> Tuple5<U1, U2, U3, U4, U5> map(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, Tuple5<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple5<U1, U2, U3, U4, U5>) f.apply(_1, _2, _3, _4, _5);
    }

    @Override
    public Tuple5<T1, T2, T3, T4, T5> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple5)) {
            return false;
        } else {
            final Tuple5<?, ?, ?, ?, ?> that = (Tuple5<?, ?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4)
                    && Objects.equals(this._5, that._5);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4, _5);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s, %s)", _1, _2, _3, _4, _5);
    }
}