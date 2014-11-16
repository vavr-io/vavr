/**
 * <strong>Either</strong>
 * <p>
 * The monad package contains an implementation of the Either monad which is either Left or Right.
 * A given Either is projected to a Left or a Right.
 * Both cases can be further processed with monad operations map, flatMap, filter.
 * If a Right is projected to a Left, the Left monad operations have no effect on the Right value.
 * If a Left is projected to a Right, the Right monad operations have no effect on the Left value.
 * </p>
 * 
 * <strong>Option</strong>
 * <p>
 * The Option monad is a replacement for {@linkplain java.util.Optional}. An Option is either Some(value) or None.
 * In contrast to Optional, Option supports null values, i.e. it is possible to call <code>new Some(null)</code>.
 * However, Option.of(null) results in None. See also <a href="http://blog.rocketscience.io/your-codebase-looks-like-this/">3 ways to deal with null</a>.
 * </p>
 * 
 * <strong>Try</strong>
 * <p>
 * Exceptions are handled with the Try monad which is either a Success, containing a result, or a Failure, containing an Exception.
 * Try internally handles exceptions by wrapping exceptions in a Cause.
 * A Cause is unchecked, i.e. a RuntimeException, and is Fatal or NonFatal.
 * Fatal exceptions cannot be handled and are thrown without further processing.
 * NonFatal exceptions are wrapped in a Failure.
 * </p>
 */
package javaslang.monad;