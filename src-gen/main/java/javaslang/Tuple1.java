/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;

import java.util.Objects;

/**
 * Implementation of a pair, a tuple containing 1 elements.
 */
public class Tuple1<T1> implements Tuple, Monad1<T1, Tuple1<?>> {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    @Override
    public int arity() {
        return 1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U1, MONAD extends HigherKinded1<U1, Tuple1<?>>> Tuple1<U1> flatMap(java.util.function.Function<? super T1, MONAD> f) {
        return (Tuple1<U1>) f.apply(_1);
    }

    @Override
    public <U1> Tuple1<U1> map(java.util.function.Function<? super T1, ? extends U1> f) {
        return new Tuple1<>(f.apply(_1));
    }

    @Override
    public Tuple1<T1> unapply() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple1)) {
            return false;
        } else {
            final Tuple1 that = (Tuple1) o;
            return Objects.equals(this._1, that._1);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(_1);
    }

    @Override
    public String toString() {
        return String.format("(%s)", _1);
    }
}