/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.option;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * None is a singleton representation of the undefined {@link javaslang.option.Option}. The instance
 * is obtained by calling {@link #instance()}.
 *
 * @param <T> The type of the optional value.
 */
public final class None<T> implements Option<T> {

	/**
	 * The singleton instance of None.
	 */
	private static final None<?> NONE = new None<>();

	/**
	 * Hidden constructor.
	 */
	private None() {
	}

	/**
	 * Returns the singleton instance of None as {@code None<T>} in the
	 * context of a type {@code <T>}, e.g.
	 * 
	 * <pre>
	 * <code>final Option&lt;Integer&gt; o = None.instance(); // o is of type None&lt;Integer&gt;</code>
	 * </pre>
	 * 
	 * @param <T> The type of the optional value.
	 * @return None
	 */
	public static <T> None<T> instance() {
		@SuppressWarnings("unchecked")
		final None<T> none = (None<T>) NONE;
		return none;
	}

	@Override
	public T get() {
		throw new NoSuchElementException("No value present");
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
	public boolean isPresent() {
		return false;
	}

	@Override
	public void ifPresent(Consumer<? super T> consumer) {
		// nothing to do
	}

	@Override
	public Option<T> filter(Predicate<? super T> predicate) {
		// semantically correct but structurally the same that <code>return this;</code>
		return None.instance();
	}

	@Override
	public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
		return None.instance();
	}

	@Override
	public <U> Option<U> flatMap(Function<? super T, Option<U>> mapper) {
		return None.instance();
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		// nothing to do
	}

	// super.equals and super.hashCode are fine because this is a singleton

	@Override
	public String toString() {
		return "None";
	}

}
