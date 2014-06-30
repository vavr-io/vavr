/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Some represents a defined {@link javaslang.option.Option}. It contains a value which may be null.
 * However, to create an Option containing null, {@code new Some(null)} has to be called. In all
 * other cases {@link Option#of(Object)} is sufficient.
 *
 * @param <T> The type of the optional value.
 */
public class Some<T> implements Option<T> {

	private final T value;

	public Some(T value) {
		this.value = value;
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
	public T orElseGet(Supplier<? extends T> other) {
		return value;
	}

	@Override
	public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
		return value;
	}

	@Override
	public boolean isPresent() {
		return true;
	}

	@Override
	public void ifPresent(Consumer<? super T> consumer) {
		consumer.accept(value);
	}

	@Override
	public Option<T> filter(Predicate<? super T> predicate) {
		if (predicate.test(value)) {
			return this;
		} else {
			return None.instance();
		}
	}

	@Override
	public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
		return new Some<>(mapper.apply(value));
	}

	@Override
	public <U> Option<U> flatMap(Function<? super T, Option<U>> mapper) {
		return mapper.apply(value);
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		action.accept(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Some)) {
			return false;
		}
		final Some<?> other = (Some<?>) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return String.format("Some[%s]", value);
	}

}
