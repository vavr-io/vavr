package io.rocketscience.java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Try<T> {
	
	static <T> Try<T> of(Supplier<T> supplier) {
		try {
			return new Success<>(supplier.get());
		} catch(Throwable x) {
			return new Failure<>(x);
		}
	}

	boolean isFailure();

	boolean isSuccess();

	T get() throws Throwable;

	Try<T> filter(Predicate<? super T> predicate);

	<U> Try<U> flatMap(Function<? super T, Try<U>> mapper);

	void forEach(Consumer<? super T> action);

	<U> Try<U> map(Function<? super T, ? extends U> mapper);

	Try<T> recoverWith(Function<? super Throwable, Try<T>> f);

	Try<T> recover(Function<? super Throwable, ? extends T> f);

	Option<T> toOption();
	
	Try<Throwable> failed();

}
