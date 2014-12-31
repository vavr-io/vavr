/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;

/**
 * Definition of a Value Object missing in Java.
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
     *     } else if (!(o instanceof CurrentType)) {
     *         return false;
     *     } else {
     *         final CurrentType that = (CurrentType) o;
     *         return ...; // check if values of this and that are pairwise equal
     *     }
     *     </code>
     * </pre>
     *
     * @param o An object, may be null.
     * @return true, if o equals this, false otherwise.
     */
    @Override
    boolean equals(Object o);

    /**
     * Needs to be overridden because of equals.
     *
     * @return The hashCode of this object.
     */
    @Override
    int hashCode();

    /**
     * Returns a String representation of this object including type and state.
     *
     * @return A String representation of this object.
     */
    @Override
    String toString();

    // -- Clonable

    /**
     * Without loss of generality there is no need to clone immutable objects.
     * If an object is a singleton instance, it is not allowed to be cloned.
     *
     * @return nothing
     * @throws CloneNotSupportedException always
     */
    default Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Immutable objects and Singletons are not intended to be cloned.");
    }
}
