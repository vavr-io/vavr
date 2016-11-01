/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * This exception is temporarily used during development in order to indicate that an implementation is missing.
 * <p>
 * The idiomatic way is to use one of {@link API#TODO()} and {@link API#TODO(String)}.
 */
public class NotImplementedError extends Error {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a {@code NotImplementedError} containing the message "an implementation is missing".
     */
    public NotImplementedError() {
        super("An implementation is missing.");
    }

    /**
     * Creates a {@code NotImplementedError} containing the given {@code message}.
     *
     * @param message A text that describes the error
     */
    public NotImplementedError(String message) {
        super(message);
    }
}
