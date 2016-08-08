/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.collection.Seq;

/**
 * A tuple of no elements which can be seen as cartesian product of no components.
 *
 * @author Daniel Dietrich
 * @since 1.1.0
 */
public final class Tuple0 implements Tuple, Comparable<Tuple0>, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * The singleton instance of Tuple0.
     */
    private static final Tuple0 INSTANCE = new Tuple0 ();

    /**
     * The singleton Tuple0 comparator.
     */
    private static final Comparator<Tuple0> COMPARATOR = (Comparator<Tuple0> & Serializable) (t1, t2) -> 0;

    // hidden constructor, internally called
    private Tuple0 () {
    }

    /**
     * Returns the singleton instance of Tuple0.
     *
     * @return The singleton instance of Tuple0.
     */
    public static Tuple0 instance() {
        return INSTANCE;
    }

    public static  Comparator<Tuple0> comparator() {
        return COMPARATOR;
    }

    @Override
    public int arity() {
        return 0;
    }

    @Override
    public int compareTo(Tuple0 that) {
        return 0;
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U apply(Supplier<? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.get();
    }

    /**
     * Transforms this tuple to an object of type U.
     *
     * @deprecated Use {@link #apply(Supplier)} instead, will be removed in 3.0.0
     * @param f Transformation which creates a new object of type U based on this tuple's contents.
     * @param <U> type of the transformation result
     * @return An object of type U
     * @throws NullPointerException if {@code f} is null
     */
    @Deprecated(/* Use apply instead, will be removed in 3.0.0 */)
    public <U> U transform(Supplier<? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.get();
    }

    @Override
    public Seq<?> toSeq() {
        return List.empty();
    }

    // -- Object

    @Override
    public boolean equals(Object o) {
        return o == this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "()";
    }

    // -- Serializable implementation

    /**
     * Instance control for object serialization.
     *
     * @return The singleton instance of Tuple0.
     * @see java.io.Serializable
     */
    private Object readResolve() {
        return INSTANCE;
    }

}