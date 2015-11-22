/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Internal class, containing scan* implementations.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class Scanner {

    private Scanner() {
    }

    static <T, R extends Traversable<T>> R scan(
            Iterable<? extends T> elements,
            T zero, BiFunction<? super T, ? super T, ? extends T> operation,
            R result, BiFunction<R, T, R> resultAccumulator, Function<R, R> resultFinisher) {
        return scanLeft(elements, zero, operation, result, resultAccumulator, resultFinisher);
    }

    static <T, U, R extends Traversable<U>> R scanLeft(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super U, ? super T, ? extends U> operation,
            R result, BiFunction<R, U, R> resultAccumulator, Function<R, R> resultFinisher) {
        U acc = zero;
        result = resultAccumulator.apply(result, acc);
        for (T a : elements) {
            acc = operation.apply(acc, a);
            result = resultAccumulator.apply(result, acc);
        }
        return resultFinisher.apply(result);
    }

    static <T, U, R extends Traversable<U>> R scanRight(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super T, ? super U, ? extends U> operation,
            R result, BiFunction<R, U, R> resultAccumulator, Function<R, R> resultFinisher) {
        final Iterable<T> reversedElements = Seq.ofAll(elements).reverseIterator();
        return scanLeft(reversedElements, zero, (u, t) -> operation.apply(t, u), result, resultAccumulator, resultFinisher);
    }
}
