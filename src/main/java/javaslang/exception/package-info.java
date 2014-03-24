/**
 * Exception handling with the Try monad.
 * <p>
 * Try is either Success, containing a result, or Failure, containing an Exception.
 * <p>
 * Try internally handles exceptions by wrapping exceptions in a Cause.
 * A Cause is unchecked, i.e. a RuntimeException, and is Fatal or NonFatal.
 * Fatal exceptions cannot be handled and are thrown without further processing.
 * NonFatal exceptions are wrapped in a Failure.
 * 
 * TODO
 */
package javaslang.exception;
