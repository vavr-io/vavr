package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import javaslang.lang.Cause;
import javaslang.lang.NonFatal;

public class Failure<T> implements Try<T> {
	
	private final NonFatal cause;
	
	public Failure(Throwable t) {
		require(t != null, "Throwable is null");
		final Cause cause = Cause.of(t);
		if (cause.isFatal()) {
			throw cause;
		} else {
			this.cause = (NonFatal) cause;
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
		throw cause;
	}
	
	@Override
	public T orElse(T other) {
		return other;
	}

	@Override
	public T orElseGet(Function<Throwable, ? extends T> other) {
		return other.apply(cause.getCause());
	}

	@Override
	public <X extends Throwable> T orElseThrow(Function<Throwable, ? extends X> exceptionProvider) throws X {
		throw exceptionProvider.apply(cause.getCause());
	}
	
	@Override
	public Try<T> recover(Function<? super Throwable, ? extends T> f) {
		return Try.of(() -> f.apply(cause.getCause()));
	}

	@Override
	public Try<T> recoverWith(Function<? super Throwable, Try<T>> f) {
		try {
			return f.apply(cause.getCause());
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
		return new Success<>(cause.getCause());
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
		return Objects.equals(cause.getCause(), failure.cause.getCause());
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(cause.getCause());
	}
	
	@Override
	public String toString() {
		return String.format("Failure[%s]", cause.getCause());
	}
	
}
