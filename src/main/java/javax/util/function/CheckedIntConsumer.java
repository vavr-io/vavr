/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import java.io.Serializable;
import java.util.Objects;

/**
 * Checked version of java.util.function.IntConsumer.
 * Essentially the same as {@code CheckedFunction1<Integer, Void>}.
 */
@FunctionalInterface
public interface CheckedIntConsumer extends Serializable {

    static final long serialVersionUID = 1L;

    void accept(int value) throws Throwable;

    default CheckedIntConsumer andThen(CheckedIntConsumer after) {
        Objects.requireNonNull(after);
        return (int t) -> { accept(t); after.accept(t); };
    }
}
