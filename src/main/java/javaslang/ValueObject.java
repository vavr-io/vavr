/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.io.Serializable;

/**
 * Definition of the category of Value Objects, i.e. immutable, decomposable objects with an identity based on their
 * type and encapsulated values.
 * <p/>
 * This interface should be implemented by concrete classes only rather than extending it by other interfaces due to
 * its Serializable nature.
 * <p/>
 * By default a Value Object is not cloneable because of the following conclusion:
 * <blockquote>
 * "[...] , it doesn’t make sense for immutable classes to support object copying, because copies would be virtually indistinguishable from the original."
 * </blockquote>
 * <em>(see Effective Java, 2nd ed., p. 61)</em>.
 * <p/>
 * Other methods than {@link #unapply()} do not define new API rather than specifying the behavior of standard Java API.
 * <p/>
 * In particular the identity of a Value Object should be defined by its content by implementing
 * {@linkplain Object#equals(Object)}, {@linkplain Object#hashCode()} and {@linkplain Object#toString()}
 * appropriately.
 * <p/>
 * The {@link #unapply()} method decomposes an Object by unwrapping it. This comes handy when using the
 * {@link javaslang.match.Match} API.
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
        throw new CloneNotSupportedException("Immutable objects and singletons are not intended to be cloned.");
    }
}
