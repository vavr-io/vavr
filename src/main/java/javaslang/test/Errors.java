/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

/**
 * Error messages used internally within {@code Property.check}.
 *
 * @since 1.2.0
 */
interface Errors {

    /**
     * Creates an Error caused by an exception when obtaining a generator.
     *
     * @param position The position of the argument within the argument list of the property, starting with 1.
     * @param size     The size hint passed to the {@linkplain Arbitrary} which caused the error.
     * @param cause    The error which occured when the {@linkplain Arbitrary} tried to obtain the generator {@linkplain Gen}.
     * @return a new Error instance.
     */
    static Error arbitraryError(int position, int size, Throwable cause) {
        return new Error(String.format("Arbitrary %s of size %s: %s", position, size, cause.getMessage()), cause);
    }

    /**
     * Creates an Error caused by an exception when generating a value.
     *
     * @param position The position of the argument within the argument list of the property, starting with 1.
     * @param size     The size hint of the arbitrary which called the generator {@linkplain Gen} which caused the error.
     * @param cause    The error which occured when the {@linkplain Gen} tried to generate a random value.
     * @return a new Error instance.
     */
    static Error genError(int position, int size, Throwable cause) {
        return new Error(String.format("Gen %s of size %s: %s", position, size, cause.getMessage()), cause);
    }

    /**
     * Creates an Error caused by an exception when testing a Predicate.
     *
     * @param cause The error which occured when applying the {@linkplain java.util.function.Predicate}.
     * @return a new Error instance.
     */
    static Error predicateError(Throwable cause) {
        return new Error("Applying predicate: " + cause.getMessage(), cause);
    }
}
