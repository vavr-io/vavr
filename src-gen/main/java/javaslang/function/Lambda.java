/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
// @@ GENERATED FILE - DO NOT MODIFY @@
package javaslang.function;

import javaslang.control.Try;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * <p>
 * This is a general definition of a checked function of unknown parameters and a return value of type R.
 * A checked function may throw an exception. The exception type is not a generic type parameter because
 * when composing functions, we cannot say anything else about the resulting type of exception than that it is
 * a Throwable.
 * </p>
 * <p>
 * This class is intended to be used internally.
 * </p>
 *
 * @param <R> Return type of the checked function.
 */
public interface Lambda<R> extends Serializable {

    /**
     * Serializes a lambda and returns the corresponding {@link java.lang.invoke.SerializedLambda}.
     *
     * @param lambda A serializable lambda
     * @return The serialized lambda wrapped in a {@link javaslang.control.Try.Success}, or a {@link javaslang.control.Try.Failure}
     * if an exception occurred.
     * @see <a
     * href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
     * debug info on errors with java 8 lambda expressions</a>
     * @see <a href="http://www.slideshare.net/hendersk/method-handles-in-java">Method Handles in Java</a>
     */
    static SerializedLambda getSerializedLambda(Serializable lambda) {
        return Try.of(() -> {
            final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return (SerializedLambda) method.invoke(lambda);
        }).get();
    }

    /**
     * <p>
     * Gets the runtime method signature of the given lambda instance. Especially this function is handy when the
     * functional interface is generic and the parameter and/or return types cannot be determined directly.
     * </p>
     * <p>
     * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by parsing the JVM field
     * types of the method signature. The result is a {@link java.lang.invoke.MethodType} which contains the return type
     * and the parameter types of the given lambda.
     * </p>
     *
     * @param lambda A serializable lambda.
     * @return The signature of the lambda as {@linkplain java.lang.invoke.MethodType}.
     */
    static MethodType getLambdaSignature(Serializable lambda) {
        final String signature = getSerializedLambda(lambda).getImplMethodSignature();
        return MethodType.fromMethodDescriptorString(signature, lambda.getClass().getClassLoader());
    }

    /**
     * @return the numper of function arguments.
     * @see <a href="http://en.wikipedia.org/wiki/Arity">Arity</a>
     */
    int arity();

    /**
     * Returns a curried version of this function.
     *
     * @return A curried function equivalent to this.
     */
    Lambda curried();

    /**
     * Returns a tupled version of this function.
     *
     * @return A tupled function equivalent to this.
     */
    Lambda<R> tupled();

    /**
     * Returns a reversed version of this function.
     *
     * @return A reversed function equivalent to this.
     */
    Lambda<R> reversed();

    /**
     * There can be nothing said about the type of exception (in Java), if the Function arg is also a checked function.
     * In an ideal world we could denote the appropriate bound of both exception types (this and after).
     * This is the reason why CheckedFunction throws a Throwable instead of a concrete exception.
     *
     * @param after Functions applied after this
     * @param <V> Return value of after
     * @return A Function composed of this and after
     */
    <V> Lambda<V> andThen(Function<? super R, ? extends V> after);

    default MethodType getType() {
        return Lambda.getLambdaSignature(this);
    }
}