package io.rocketscience.java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class Failure<T> implements Try<T> {
	
	private final Throwable exception;
	
	public Failure(Throwable exception) {
		this.exception = exception;
	}
	
	@Override
	public boolean isFailure() {
		return true;
	}

	@Override
	public boolean isSuccess() {
		return false;
	}

	@Override
	public T get() throws Throwable {
		throw exception;
	}

	@Override
	public Try<T> filter(Predicate<? super T> predicate) {
		return this;
	}

	@Override
	public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
		@SuppressWarnings("unchecked")
		final Try<U> result = (Try<U>) this;
		return result;
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		// nothing to do
	}

	@Override
	public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
		@SuppressWarnings("unchecked")
		final Try<U> result = (Try<U>) this;
		return result;
	}
	
	@Override
	public Try<T> recoverWith(Function<? super Throwable, Try<T>> f) {
		return f.apply(exception);
	}

	@Override
	public Try<T> recover(Function<? super Throwable, ? extends T> f) {
		return Try.of(() -> f.apply(exception));
	}

	@Override
	public Option<T> toOption() {
		return None.instance();
	}
	
	@Override
	public Try<Throwable> failed() {
		return new Success<Throwable>(exception);
	}
	
}
