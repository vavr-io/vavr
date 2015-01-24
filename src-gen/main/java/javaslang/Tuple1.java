/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang;

import java.util.Objects;

/**
 * Implementation of a pair, a tuple containing 1 elements.
 */
public class Tuple1<T1> implements Tuple {

    private static final long serialVersionUID = 1L;

    public final T1 _1;

    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    @Override
    public int arity() {
        return 1;
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