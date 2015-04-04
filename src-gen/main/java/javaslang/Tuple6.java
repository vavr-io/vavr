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
import javaslang.algebra.HigherKinded6;
import javaslang.algebra.Monad6;

/**
 * Implementation of a pair, a tuple containing 6 elements.
 */
public class Tuple6<T1, T2, T3, T4, T5, T6> implements Tuple, Monad6<T1, T2, T3, T4, T5, T6, Tuple6<?, ?, ?, ?, ?, ?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;
    public final T2 _2;
    public final T3 _3;
    public final T4 _4;
    public final T5 _5;
    public final T6 _6;

    public Tuple6(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6) {
        this._1 = t1;
        this._2 = t2;
        this._3 = t3;
        this._4 = t4;
        this._5 = t5;
        this._6 = t6;
    }

    @Override
    public int arity() {
        return 6;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5, U6, TUPLE extends HigherKinded6<U1, U2, U3, U4, U5, U6, Tuple6<?, ?, ?, ?, ?, ?>>> Tuple6<U1, U2, U3, U4, U5, U6> flatMap(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, TUPLE> f) {
        return (Tuple6<U1, U2, U3, U4, U5, U6>) f.apply(_1, _2, _3, _4, _5, _6);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6>> f) {
        // normally the result of f would be mapped to the result type of map, but Tuple.map is a special case
        return (Tuple6<U1, U2, U3, U4, U5, U6>) f.apply(_1, _2, _3, _4, _5, _6);
    }

    @Override
    public <U1, U2, U3, U4, U5, U6> Tuple6<U1, U2, U3, U4, U5, U6> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6) {
        return map((t1, t2, t3, t4, t5, t6) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6)));
    }

    @Override
    public Tuple6<T1, T2, T3, T4, T5, T6> unapply() {
        return this;
    }

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
        return String.format("(%s, %s, %s, %s, %s, %s)", _1, _2, _3, _4, _5, _6);
    }
}