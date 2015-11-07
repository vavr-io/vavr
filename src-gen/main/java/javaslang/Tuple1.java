/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import javaslang.collection.List;
import javaslang.collection.Seq;

/**
 * A tuple of one element which can be seen as cartesian product of one component.
 *
 * @param <T1> type of the 1st element
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public final class Tuple1<T1> implements Tuple, Comparable<Tuple1<T1>>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The 1st element of this tuple.
     */
    public final T1 _1;

    /**
     * Constructs a tuple of one element.
     *
     * @param t1 the 1st element
     */
    public Tuple1(T1 t1) {
        this._1 = t1;
    }

    public static <T1> Comparator<Tuple1<T1>> comparator(Comparator<? super T1> t1Comp) {
        return (Comparator<Tuple1<T1>> & Serializable) (t1, t2) -> {
            final int check1 = t1Comp.compare(t1._1, t2._1);
            if (check1 != 0) {
                return check1;
            }

            // all components are equal
            return 0;
        };
    }

    @SuppressWarnings("unchecked")
    private static <U1 extends Comparable<? super U1>> int compareTo(Tuple1<?> o1, Tuple1<?> o2) {
        final Tuple1<U1> t1 = (Tuple1<U1>) o1;
        final Tuple1<U1> t2 = (Tuple1<U1>) o2;

        final int check1 = t1._1.compareTo(t2._1);
        if (check1 != 0) {
            return check1;
        }

        // all components are equal
        return 0;
    }

    @Override
    public int arity() {
        return 1;
    }

    @Override
    public int compareTo(Tuple1<T1> that) {
        return Tuple1.compareTo(this, that);
    }

    /**
     * Getter of the 1st element of this tuple.
     *
     * @return the 1st element of this Tuple.
     */
    public T1 _1() {
        return _1;
    }

    public <U1> Tuple1<U1> flatMap(Function1<? super T1, ? extends U1> f) {
        return new Tuple1<>(f.apply(_1));
    }

    /**
     * Transforms this tuple to an arbitrary object (which may be also a tuple of same or different arity).
     *
     * @param f Transformation which takes this tuple and return a new tuple of type U
     * @param <U> New type
     * @return An object of type U
     */
    public <U> U transform(Function<? super Tuple1<T1>, U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    public Seq<?> toSeq() {
        return List.of(_1);
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Tuple1)) {
            return false;
        } else {
            final Tuple1<?> that = (Tuple1<?>) o;
            return Objects.equals(this._1, that._1);
        }
    }

    // if _1 == null, hashCode() returns Objects.hash(new T1[] { null }) = 31 instead of 0 = Objects.hash(null)
    @Override
    public int hashCode() {
        return Objects.hash(_1);
    }

    @Override
    public String toString() {
        return String.format("(%s)", _1);
    }

}