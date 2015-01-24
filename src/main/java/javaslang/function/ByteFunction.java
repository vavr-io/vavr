/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.function;

import java.io.Serializable;

/**
 * Unchecked byte to R function.
 * Essentially the same as {@code Function1<Byte, R>}, or short {@code λ1<Byte, R>}.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface ByteFunction<R> extends Serializable {

    R apply(byte b);
}
