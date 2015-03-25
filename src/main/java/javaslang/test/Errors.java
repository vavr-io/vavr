/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

// internally used within Property.check
interface Errors {

    String ARBITRARY_ERROR = "Arbitrary %s of size %s: %s";
    String GEN_ERROR = "Gen %s of size %s: %s";
    String PREDICATE_ERROR = "Applying predicate: %s";

    /**
     *
     * @param position The position of the argument within the argument list of the property, starting with 1.
     * @param size The size hint passed to the {@linkplain Arbitrary} which caused the error.
     * @param cause The error which occured when the {@linkplain Arbitrary} tried to obtain the generator {@linkplain Gen}.
     * @return
     */
    static Error arbitraryError(int position, int size, Throwable cause) {
        return new Error(String.format(ARBITRARY_ERROR, position, size, cause.getMessage()), cause);
    }

    static Error genError(int position, int size, Throwable cause) {
        return new Error(String.format(GEN_ERROR, position, size, cause.getMessage()), cause);
    }

    static Error predicateError(Throwable cause) {
        return new Error(String.format(PREDICATE_ERROR, cause.getMessage()), cause);
    }
}
