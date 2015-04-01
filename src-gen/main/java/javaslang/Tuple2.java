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
import javaslang.algebra.HigherKinded2;
import javaslang.algebra.Monad2;

/**
 * Implementation of a pair, a tuple containing 2 elements.
 */
public class Tuple2<T1, T2> implements Tuple, Monad2<T1, T2, Tuple2<?, ?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;

    public Tuple2(T1 t1, T2 t2) {
        this._1 = t1;
        this._2 = t2;
    }

    @Override
    public int arity() {
        return 2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, TUPLE extends HigherKinded2<U1, U2, Tuple2<?, ?>>> Tuple2<U1, U2> flatMap(Function2<? super T1, ? super T2, TUPLE> f) {
        return (Tuple2<U1, U2>) f.apply(_1, _2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2> Tuple2<U1, U2> map(Function2<? super T1, ? super T2, Tuple2<? extends U1, ? extends U2>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple2<U1, U2>) f.apply(_1, _2);
    }

    @Override
    public Tuple2<T1, T2> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple2)) {
            return false;
        } else {
            final Tuple2<?, ?> that = (Tuple2<?, ?>) o;
            return Objects.equals(this._1, that._1)
                    && Objects.equals(this._2, that._2);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1, _2);
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", _1, _2);
    }
}