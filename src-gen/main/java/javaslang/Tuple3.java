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
import javaslang.algebra.HigherKinded3;
import javaslang.algebra.Monad3;

/**
 * Implementation of a pair, a tuple containing 3 elements.
 */
public class Tuple3<T1, T2, T3> implements Tuple, Monad3<T1, T2, T3, Tuple3<?, ?, ?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;

    public Tuple3(T1 t1, T2 t2, T3 t3) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
    }

    @Override
    public int arity() {
        return 3;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, MONAD extends HigherKinded3<U1, U2, U3, Tuple3<?, ?, ?>>> Tuple3<U1, U2, U3> flatMap(Function3<? super T1, ? super T2, ? super T3, MONAD> f) {
        return (Tuple3<U1, U2, U3>) f.apply(_1, _2, _3);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3> Tuple3<U1, U2, U3> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<? extends U1, ? extends U2, ? extends U3>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple3<U1, U2, U3>) f.apply(_1, _2, _3);
    }

    @Override
    public Tuple3<T1, T2, T3> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple3)) {
            return false;
        } else {
            final Tuple3 that = (Tuple3) o;
            return Objects.equals(this._1, that._1)
                  && Objects.equals(this._2, that._2)
                  && Objects.equals(this._3, that._3);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2, _3);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", _1, _2, _3);
    }
}