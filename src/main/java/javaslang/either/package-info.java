/**
 * Provides an implementation of the Either monad which is either Left or Right.
 * A given Either is projected to a Left or a Right.
 * Both cases can be further processed with monad operations map, flatMap, filter.
 * If a Right is projected to a Left, the Left monad operations have no effect on the Right value.
 * If a Left is projected to a Right, the Right monad operations have no effect on the Left value.
 */
package javaslang.either;
