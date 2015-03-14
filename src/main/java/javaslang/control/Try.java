/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.ValueObject;
import javaslang.algebra.HigherKinded1;
import javaslang.algebra.Monad1;
import javaslang.control.Valences.Bivalent;

import javax.lang.CheckedRunnable;
import javax.util.function.CheckedSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An implementation similar to Scala's Try control.
 *
 * @param <T> Value type in the case of success.
 */
public interface Try<T> extends Monad1<T, Try<?>>, ValueObject, Bivalent<T, Throwable> {

    static final long serialVersionUID = 1L;

	static <T> Try<T> of(CheckedSupplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	static <T> Try<Void> run(CheckedRunnable runnable) {
		try {
			runnable.run();
			return new Success<>(null); // null represents the absence of an value, i.e. Void
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	Try<T> recover(Function<Throwable, ? extends T> f);

	Try<T> recoverWith(Function<Throwable, Try<T>> f);

	Try<Throwable> failed();

    Try<T> onFailure(Consumer<Throwable> f);

	Try<T> filter(Predicate<? super T> predicate);

	void forEach(Consumer<? super T> action);

	@Override
	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	@Override
	<U, TRY extends HigherKinded1<U, Try<?>>> Try<U> flatMap(Function<? super T, TRY> mapper);

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();
}
