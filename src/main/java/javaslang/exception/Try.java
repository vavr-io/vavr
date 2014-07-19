/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.option.Option;

/**
 * An implementation similar to Scala's Try monad.
 *
 * @param <T> Value type in the case of success.
 */
public interface Try<T> {

	static <T> Try<T> of(Try.CheckedSupplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	T get() throws NonFatal;

	T orElse(T other);

	T orElseGet(Function<Throwable, ? extends T> other);

	<X extends Throwable> T orElseThrow(Function<Throwable, ? extends X> exceptionProvider)
			throws X;

	Try<T> recover(Function<? super Throwable, ? extends T> f);

	Try<T> recoverWith(Function<? super Throwable, Try<T>> f);

	Option<T> toOption();

	Try<T> filter(Predicate<? super T> predicate);

	<U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

	void forEach(Consumer<? super T> action);

	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	Try<Throwable> failed();

	/**
	 * Used to initialize a Try calling {@link Try#of(Try.CheckedSupplier)}.
	 *
	 * @param <T> Type of supplied object.
	 */
	@FunctionalInterface
	static interface CheckedSupplier<T> {

		/**
		 * Gets a result or throws a Throwable.
		 *
		 * @return a result
		 * @throws java.lang.Throwable if an error occurs.
		 */
		T get() throws Throwable;

	}

}
