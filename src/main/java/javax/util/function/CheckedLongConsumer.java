/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.LongConsumer.
 * Essentially the same as {@code CheckedFunction1<Long, Void>}, or short {@code X1<Long, Void>}.
 */
@FunctionalInterface
public interface CheckedLongConsumer extends Serializable {

    void accept(long value) throws Throwable;

    default CheckedLongConsumer andThen(CheckedLongConsumer after) {
        Objects.requireNonNull(after);
        return (long t) -> { accept(t); after.accept(t); };
    }
}
