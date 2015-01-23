/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Represents an arbitrary object of type T.
 *
 * @param <T> The type of the arbitrary object.
 */
@FunctionalInterface
public interface Arbitrary<T> extends IntFunction<Gen<T>>, Function<Integer, Gen<T>> {

    /**
     * <p>
     * Returns an arbitrary object of
     * Use {@link Gen} to generate arbitrary objects.
     * </p>
     * Example:
     * <pre>
     * <code>
     * // represents arbitrary binary trees of a certain depth n
     * final class ArbitraryTree implements Arbitrary&lt;BinaryTree&lt;Integer&gt;&gt; {
     *     @Override
     *     public Gen&lt;BinaryTree&lt;Integer&gt;&gt; apply(int n) {
     *         return Gen.choose(-1000, 1000).flatMap(value -> {
     *                  if (n == 0) {
     *                      return Gen.of(BinaryTree.leaf(value));
     *                  } else {
     *                      return Gen.frequency(
     *                              Tuple.of(1, Gen.of(BinaryTree.leaf(value))),
     *                              Tuple.of(4, Gen.of(BinaryTree.branch(apply(n / 2).get(), value, apply(n / 2).get())))
     *                      );
     *                  }
     *         });
     *     }
     * }
     *
     * // tree generator with a size hint of 10
     * final Gen&lt;BinaryTree&lt;Integer&gt;&gt; treeGen = new ArbitraryTree().apply(10);
     *
     * // stream sum of tree node values to console for 100 arbitrary trees
     * Stream.of(treeGen).map(Tree::sum).take(100).stdout();
     * </code>
     * </pre>
     *
     * @param size A size parameter which may be interpreted idividually and is constant for all arbitrary objects regarding one property check.
     * @return A generator for objects of type T.
     */
    @Override
    Gen<T> apply(int size);

    /**
     * Calls {@link #apply(int)}.
     * @param size A size hint.
     * @return The result of {@link #apply(int)}.
     */
    default Gen<T> apply(Integer size) {
        Objects.requireNonNull(size, "size is null");
        return apply((int) size);
    }
}
