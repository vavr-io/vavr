package javaslang.lambda;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

import javaslang.monad.Try;

/**
 * Adds reflective capabilities to lambda expressions.
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
 *      debug info on errors with java 8 lambda expressions</a>
 * @see <a href="http://www.slideshare.net/hendersk/method-handles-in-java">Method Handles in Java</a>
 */
public interface Reflective extends Serializable {

	/**
	 * Serializes this lambda and returns the corresponding {@link java.lang.invoke.SerializedLambda}.
	 * 
	 * @return The serialized lambda wrapped in a {@link javaslang.monad.Success}, or a {@link javaslang.monad.Failure}
	 *         if an exception occurred.
	 */
	// TODO: Memoization / caching
	default SerializedLambda getSerializedLambda() {
		return Try.of(() -> {
			final Method method = getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			return (SerializedLambda) method.invoke(this);
		}).get();
	}

	/**
	 * Gets the runtime method signature of the given lambda instance. Especially this function is handy when the
	 * functional interface is generic and the parameter and/or return types cannot be determined directly.
	 * <p>
	 * Uses internally the {@link java.lang.invoke.SerializedLambda#getImplMethodSignature()} by parsing the JVM field
	 * types of the method signature. The result is a {@link java.lang.invoke.MethodType} which contains the return type
	 * and the parameter types of the given lambda.
	 * 
	 * @param lambda A serializable lambda.
	 * @return The signature of the lambda wrapped in a {@link javaslang.monad.Success}, or a
	 *         {@link javaslang.monad.Failure} if an exception occurred.
	 */
	// TODO: Memoization / caching
	default MethodType getLambdaSignature() {
		final String signature = getSerializedLambda().getImplMethodSignature();
		return MethodType.fromMethodDescriptorString(signature, getClass().getClassLoader());
	}
}
