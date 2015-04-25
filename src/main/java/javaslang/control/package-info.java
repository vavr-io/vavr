/**
 * <p>Control structures like {@linkplain javaslang.control.Match}, the disjoint union type {@linkplain javaslang.control.Either}, the optional value type
 * {@linkplain javaslang.control.Option} and {@linkplain javaslang.control.Try} for exception handling.
 * The {@linkplain javaslang.control.Match} provides pattern matching.</p>
 *
 * <strong>Match</strong>
 * <p>
 * The match control is a more mature switch expression with pattern matching capabilities.
 * </p>
 *
 * <strong>Either</strong>
 * <p>
 * The control package contains an implementation of the {@linkplain javaslang.control.Either} control which is either Left or Right.
 * A given Either is projected to a Left or a Right.
 * Both cases can be further processed with control operations map, flatMap, filter.
 * If a Right is projected to a Left, the Left control operations have no effect on the Right value.
 * If a Left is projected to a Right, the Right control operations have no effect on the Left value.
 * </p>
 *
 * <strong>Option</strong>
 * <p>
 * The Option control is a replacement for {@linkplain java.util.Optional}. An Option is either
 * {@linkplain javaslang.control.Some} value or {@linkplain javaslang.control.None}.
 * In contrast to Optional, Option supports null values, i.e. it is possible to call {@code new Some(null)}.
 * However, {@code Option.of(null)} results in None.
 * See also <a href="http://blog.rocketscience.io/your-codebase-looks-like-this/">3 ways to deal with null</a>.
 * </p>
 *
 * <strong>Try</strong>
 * <p>
 * Exceptions are handled with the {@linkplain javaslang.control.Try} control which is either a
 * {@linkplain javaslang.control.Success}, containing a result, or a {@linkplain javaslang.control.Failure},
 * containing an Exception.
 * Try internally handles exceptions by wrapping exceptions in a Cause.
 * A Cause is unchecked, i.e. a RuntimeException, and is Fatal or NonFatal.
 * Fatal exceptions cannot be handled and are thrown without further processing.
 * NonFatal exceptions are wrapped in a Failure.
 * </p>
 *
 * @since 1.0.0
 */
package javaslang.control;
