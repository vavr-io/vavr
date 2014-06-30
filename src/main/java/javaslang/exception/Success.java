/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.exception;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.option.Option;
import javaslang.option.Some;

public class Success<T> implements Try<T> {

	private T value;

	public Success(T value) {
		this.value = value;
	}

	@Override
	public boolean isFailure() {
		return false;
	}

	@Override
	public boolean isSuccess() {
		return true;
	}

	@Override
	public T get() {
		return value;
	}
	
	@Override
	public T orElse(T other) {
		return value;
	}

	@Override
	public T orElseGet(Function<Throwable, ? extends T> other) {
		return value;
	}

	@Override
	public <X extends Throwable> T orElseThrow(Function<Throwable, ? extends X> exceptionSupplier) throws X {
		return value;
	}
	
	@Override
	public Try<T> recover(Function<? super Throwable, ? extends T> f) {
		return this;
	}

	@Override
	public Try<T> recoverWith(Function<? super Throwable, Try<T>> f) {
		return this;
	}

	@Override
	public Option<T> toOption() {
		return new Some<>(value);
	}
	
	@Override
	public Try<T> filter(Predicate<? super T> predicate) {
		try {
			if (predicate.test(value)) {
				return this;
			} else {
				return new Failure<T>(new NoSuchElementException("Predicate does not hold for " + value));
			}
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	@Override
	public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
		try {
			return mapper.apply(value);
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(value);
	}

	@Override
	public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
		try {
			return new Success<>(mapper.apply(value));
		} catch (Throwable t) {
			return new Failure<>(t);
		}
	}

	@Override
	public Try<Throwable> failed() {
		return new Failure<>(new UnsupportedOperationException("Success.failed()"));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Success)) {
			return false;
		}
		final Success<?> success = (Success<?>) obj;
		return Objects.equals(value, success.value);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}
	
	@Override
	public String toString() {
		return String.format("Success[%s]", value);
	}
	
}
