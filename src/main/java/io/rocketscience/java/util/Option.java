package io.rocketscience.java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public interface Option<T> {
	
	static <T> Option<T> of(T value) {
		return (value == null) ? None.instance() : new Some<>(value);
    }

	static <T> Option<T> empty() {
		return None.instance();
    }
	
    T get();
    
    T orElse(T other);

    T orElseGet(Supplier<? extends T> other);

    <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;
    
    boolean isPresent();
    
    void ifPresent(Consumer<? super T> consumer);
    
    Option<T> filter(Predicate<? super T> predicate);

    <U> Option<U> map(Function<? super T, ? extends U> mapper);

    <U> Option<U> flatMap(Function<? super T, Option<U>> mapper);

}
