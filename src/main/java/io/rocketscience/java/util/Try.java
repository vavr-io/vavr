package io.rocketscience.java.util;

import static io.rocketscience.java.lang.Lang.isFatal;
import io.rocketscience.java.lang.FatalError;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Try<T> {
	
	static <T> Try<T> of(Supplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch (Throwable t) {
			return handle(t);
		}
	}
	
	static <T> Try<T> handle(Throwable t) {
		if (isFatal(t)) {
			throw new FatalError(t);
		} else {
			return new Failure<>(t);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	T get() throws Throwable;
	
    T orElse(T other);

    T orElseGet(Supplier<? extends T> other);

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

	Try<T> recover(Function<? super Throwable, ? extends T> f);

	Try<T> recoverWith(Function<? super Throwable, Try<T>> f);

	Option<T> toOption();

	Try<T> filter(Predicate<? super T> predicate);

	<U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

	void forEach(Consumer<? super T> action);

	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	Try<Throwable> failed();

}
