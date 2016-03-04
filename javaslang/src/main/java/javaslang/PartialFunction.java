/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.util.function.Function;

/**
 * A partial function represents a function that is not total, i.e. a function that might be not defined for all
 * inputs.
 *
 * @param <T> Input type
 * @param <R> Output type
 * @author Daniel Dietrich
 * @since 2.0.0
 */
// DEV-NOTE: there is currently no use case for having PartialFunction0-N
public interface PartialFunction<T, R> extends Function<T, R> {
    boolean isApplicable(T t);
}
