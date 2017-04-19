/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * Represents a partial function T -&gt; R that is not necessarily defined for all input values of type T.
 * The caller is responsible for calling the method isDefinedAt() before this function is applied to the value.
 * <p>
 * If the function <em>is not defined</em> for a specific value, apply() may produce an arbitrary result.
 * More specifically it is not guaranteed that the function will throw an exception.
 * <p>
 * If the function <em>is defined</em> for a specific value, apply() may still throw an exception.
 *
 * @param <T> type of the function input, called <em>domain</em> of the function
 * @param <R> type of the function output, called <em>codomain</em> of the function
 * @author Daniel Dietrich
 * @since 2.1.0
 */
public interface PartialFunction<T, R> {

    /**
     * Applies this function to the given argument and returns the result.
     *
     * @param t the argument
     * @return the result of function application
     *
     */
    R apply(T t);

    /**
     * Tests if a value is contained in the function's domain.
     *
     * @param value a potential function argument
     * @return true, if the given value is contained in the function's domain, false otherwise
     */
    boolean isDefinedAt(T value);

}
