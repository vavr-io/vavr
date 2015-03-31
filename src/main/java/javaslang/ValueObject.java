/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;

/**
 * <p>
 * Value Objects are immutable, decomposable objects with an identity based on their type and encapsulated values.
 * </p>
 * <p>
 * In particular the identity of a Value Object should be defined by its content by implementing
 * {@linkplain Object#equals(Object)}, {@linkplain Object#hashCode()} and {@linkplain Object#toString()}
 * appropriately.
 * </p>
 * <p>
 * The {@link #unapply()} method decomposes an Object by unwrapping it. This comes handy when using the
 * {@link javaslang.control.Match} API.
 * </p>
 * Please note that a Value Object is not cloneable because of the following conclusion:
 * <blockquote>
 * "[...] , it doesn’t make sense for immutable classes to support object copying, because copies would be virtually indistinguishable from the original."
 * </blockquote>
 * <em>(see Effective Java, 2nd ed., p. 61)</em>.
 * @since 1.1.0
 */
public interface ValueObject extends Serializable {

    /**
     * Decomposes this object into its parts.
     *
     * @return A Tuple of parts of the construction of this object.
     */
    Tuple unapply();

    // -- Object.*

    /**
     * Checks if o equals this.
     *
     * <pre>
     *     <code>
     *     if (o == this) {
     *         return true;
     *     } else if (o instanceof CurrentType) {
     *         final CurrentType that = (CurrentType) o;
     *         return ...; // check if values of this and that are pairwise equal
     *     } else {
     *         return false;
     *     }
     *     </code>
     * </pre>
     *
     * Please note that double and float values 0.0 and -0.0 are not equal, just in case.
     *
     * @param o An object, may be null.
     * @return true, if o equals this, false otherwise.
     * @see java.util.Objects#equals(Object, Object)
     */
    @Override
    boolean equals(Object o);

    /**
     * Needs to be overridden because of equals.
     *
     * @return The hashCode of this object.
     * @see java.util.Objects#hash(Object...)
     */
    @Override
    int hashCode();

    /**
     * Returns a String representation of this object including type and state.
     *
     * @return A String representation of this object.
     * @see java.lang.String#valueOf(Object)
     */
    @Override
    String toString();
}
