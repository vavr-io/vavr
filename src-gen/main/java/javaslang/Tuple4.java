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
import javaslang.algebra.HigherKinded4;
import javaslang.algebra.Monad4;

/**
 * Implementation of a pair, a tuple containing 4 elements.
 */
public class Tuple4<T1, T2, T3, T4> implements Tuple, Monad4<T1, T2, T3, T4, Tuple4<?, ?, ?, ?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;

    public Tuple4(T1 t1, T2 t2, T3 t3, T4 t4) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
    }

    @Override
    public int arity() {
        return 4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, TUPLE extends HigherKinded4<U1, U2, U3, U4, Tuple4<?, ?, ?, ?>>> Tuple4<U1, U2, U3, U4> flatMap(Function4<? super T1, ? super T2, ? super T3, ? super T4, TUPLE> f) {
        return (Tuple4<U1, U2, U3, U4>) f.apply(_1, _2, _3, _4);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4> Tuple4<U1, U2, U3, U4> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple4<U1, U2, U3, U4>) f.apply(_1, _2, _3, _4);
    }

    @Override
    public <U1, U2, U3, U4> Tuple4<U1, U2, U3, U4> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4) {
        return map((t1, t2, t3, t4) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4)));
    }

    @Override
    public Tuple4<T1, T2, T3, T4> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple4)) {
            return false;
        } else {
            final Tuple4<?, ?, ?, ?> that = (Tuple4<?, ?, ?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2)
                    && Objects.equals(this._3, that._3)
                    && Objects.equals(this._4, that._4);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3, _4);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s, %s)", _1, _2, _3, _4);
    }
}