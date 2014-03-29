package javaslang.lang;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Extension methods for java.lang.invoke.*
 * 
 * @see <a
 *      href="http://stackoverflow.com/questions/21860875/printing-debug-info-on-errors-with-java-8-lambda-expressions">printing
 *      debug info on errors with java 8 lambda expressions</a>
 */
public final class Invocations {

	/**
	 * This class is not intendet to be instantiated.
	 */
	private Invocations() {
		throw new AssertionError(Invocations.class.getName() + " cannot be instantiated.");
	}

	public static SerializedLambda getSerializedLambda(Serializable lambda) {
		try {
			final Method method = lambda.getClass().getDeclaredMethod("writeReplace");
			method.setAccessible(true);
			return (SerializedLambda) method.invoke(lambda);
		} catch (Throwable x) {
			throw new IllegalStateException("Error serializing lamda expression.", x);
		}
	}

	public static LambdaSignature getLambdaSignature(Serializable lambda) {
		try {
		final String signature = getSerializedLambda(lambda).getImplMethodSignature();
		// TODO: parse signature to get returnType and parameterTypes
		// TODO: http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html (Table 4.2)
		final Class<?> returnType = null;
		final Class<?>[] parameterTypes = new Class[] { Class.forName(signature.substring(2,
				signature.indexOf(";)")).replaceAll("/", ".")) };
		return new LambdaSignature(returnType, parameterTypes);
		} catch(Exception x) {
			// TODO:
			throw new IllegalStateException(x);
		}
	}

	/**
	 * Represents a Lambda signature having a return type and parameter types, similar to a
	 * {@link java.lang.reflect.Method}.
	 */
	public static class LambdaSignature {
		private final Class<?> returnType;
		private final Class<?>[] parameterTypes;

		public LambdaSignature(Class<?> returnType, Class<?>[] parameterTypes) {
			this.returnType = returnType;
			this.parameterTypes = parameterTypes;
		}

		public Class<?> getReturnType() {
			return returnType;
		}

		public Class<?>[] getParameterTypes() {
			return parameterTypes;
		}
	}

}
