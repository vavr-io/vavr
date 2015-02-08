/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javax.util.function;

import javaslang.CheckedFunction1;

/**
 * Checked version of CharFunction.
 *
 * @param <R> Return value type
 */
@FunctionalInterface
public interface CheckedCharFunction<R> extends CheckedFunction1<Character, R> {

    static final long serialVersionUID = 1L;

    R apply(char c) throws Throwable;

    @Override
    default R apply(Character c) throws Throwable {
        return apply(c.charValue());
    }
}
