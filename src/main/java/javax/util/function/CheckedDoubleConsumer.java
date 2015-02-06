/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.DoubleConsumer.
 * Essentially the same as {@code CheckedFunction1<Double, Void>}, or short {@code X1<Double, Void>}.
 */
@FunctionalInterface
public interface CheckedDoubleConsumer extends Serializable {

    static final long serialVersionUID = 1L;

    void accept(double value) throws Throwable;

    default CheckedDoubleConsumer andThen(CheckedDoubleConsumer after) {
        Objects.requireNonNull(after);
        return (double t) -> { accept(t); after.accept(t); };
    }
}
