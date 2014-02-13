package io.rocketscience.java.util;

import io.rocketscience.java.lang.Lang;
import io.rocketscience.java.lang.NonFatal;
import io.rocketscience.java.lang.Thrown;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Failure<T> implements Try<T> {
	
	private final NonFatal throwable;
	
	public Failure(Throwable t) {
		Lang.require(t != null, "Throwable is null");
		final Thrown thrown = Thrown.of(t);
		if (thrown.isFatal()) {
			throw thrown;
		} else {
			this.throwable = (NonFatal) thrown;
		}
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
	public T get() throws NonFatal {
		throw throwable;
	}
	
	@Override
	public T orElse(T other) {
		return other;
	}

	@Override
	public T orElseGet(Supplier<? extends T> other) {
		return other.get();
	}

	@Override
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		throw exceptionSupplier.get();
	}
	
	@Override
	public Try<T> recover(Function<? super Throwable, ? extends T> f) {
		return Try.of(() -> f.apply(throwable.get()));
	}

	@Override
	public Try<T> recoverWith(Function<? super Throwable, Try<T>> f) {
		try {
			return f.apply(throwable.get());
		} catch(Throwable t) {
			return new Failure<>(t);
		}
	}

	@Override
	public Option<T> toOption() {
		return None.instance();
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
	public Try<Throwable> failed() {
		return new Success<>(throwable.get());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Failure)) {
			return false;
		}
		final Failure<?> failure = (Failure<?>) obj;
		return Objects.equals(throwable.get(), failure.throwable.get());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(throwable.get());
	}
	
	@Override
	public String toString() {
		return String.format("Failure[%s]", throwable.get());
	}
	
}
