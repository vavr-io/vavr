package io.rocketscience.java.util;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
	public Try<T> filter(Predicate<? super T> predicate) {
		try {
			if (predicate.test(value)) {
				return this;
			} else {
				return new Failure<T>(new NoSuchElementException("Predicate does not hold for " + value));
			}
		} catch(Exception x) {
			return new Failure<T>(x);
		}
	}

	@Override
	public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
		return mapper.apply(value);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(value);
	}

	@Override
	public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
		return new Success<>(mapper.apply(value));
	}
	
	@Override
	public Try<T> recoverWith(Function<? super Throwable, Try<T>> f) {
		return this;
	}

	@Override
	public Try<T> recover(Function<? super Throwable, ? extends T> f) {
		return this;
	}

	@Override
	public Option<T> toOption() {
		return new Some<>(value);
	}
	
	@Override
	public Try<Throwable> failed() {
		return new Failure<> (new UnsupportedOperationException("Success.failed"));
	}
	
}
