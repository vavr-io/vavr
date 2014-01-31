package io.rocketscience.java.util.function;

/**
 * Consider using <code>io.rocketscience.java.util.Either&lt;T,Exception&gt;</code> instead.
 *
 * @param <T>
 */
@FunctionalInterface
public interface CheckedSupplier<T> {
	T get() throws Exception;
}
