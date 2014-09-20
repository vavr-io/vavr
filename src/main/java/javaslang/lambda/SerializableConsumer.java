/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.lambda;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * A consumer which implements Serializable in order to obtain runtime type information about the parameter type via
 * {@link javaslang.lambda.Lambdas#getLambdaSignature(Serializable)}.
 *
 * @param <T> The parameter type of the consumer.
 */
@FunctionalInterface
public interface SerializableConsumer<T> extends Consumer<T>, Serializable {
}
