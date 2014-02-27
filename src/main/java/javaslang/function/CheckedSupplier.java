package javaslang.function;

/**
 * @see java.function.Supplier
 */
@FunctionalInterface
public interface CheckedSupplier<T> {

    /**
     * Gets a result or throws an exception.
     *
     * @return a result
     * @throws An exception if an error occurs.
     */
	T get() throws Exception;
	
}
