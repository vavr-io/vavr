/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.monad;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.Algebra.Monad;
import javaslang.Manifest;

/**
 * Replacement for {@link java.util.Optional}.
 * <p>
 * Option is a <a href="http://stackoverflow.com/questions/13454347/monads-with-java-8">monadic</a> container type which
 * represents an optional value. Instances of Option are either an instance of {@link javaslang.monad.Option.Some} or the
 * singleton {@link javaslang.monad.Option.None}.
 * <p>
 * Most of the API is taken from {@link java.util.Optional}. A similar type can be found in <a
 * href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and <a
 * href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 * 
 * @param <T> The type of the optional value.
 */
public interface Option<T> extends Monad<T, Option<?>> {

	static <T> Option<T> of(T value) {
		return (value == null) ? None.instance() : new Some<>(value);
	}

	static <T> Option<T> none() {
		return None.instance();
	}

	T get();

	T orElse(T other);

	T orElseGet(Supplier<? extends T> other);

	<X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

	boolean isPresent();

	boolean isNotPresent();

	void ifPresent(Consumer<? super T> consumer);

	Option<T> filter(Predicate<? super T> predicate);

	void forEach(Consumer<? super T> action);

	@Override
	<U> Option<U> map(Function<? super T, ? extends U> mapper);

	@Override
	<U, OPTION extends Manifest<U, Option<?>>> Option<U> flatMap(Function<? super T, OPTION> mapper);

	@Override
	<U> Option<U> unit(U u);

	@Override
	boolean equals(Object o);

	@Override
	int hashCode();

	@Override
	String toString();

	/**
	 * Some represents a defined {@link javaslang.monad.Option}. It contains a value which may be null. However, to
	 * create an Option containing null, {@code new Some(null)} has to be called. In all other cases
	 * {@link Option#of(Object)} is sufficient.
	 *
	 * @param <T> The type of the optional value.
	 */
	static final class Some<T> implements Option<T>, Serializable {

		private static final long serialVersionUID = 8703728987837576700L;

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
		public boolean isNotPresent() {
			return false;
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
		public void forEach(Consumer<? super T> action) {
			action.accept(value);
		}

		@Override
		public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
			return new Some<>(mapper.apply(value));
		}

		@SuppressWarnings("unchecked")
		@Override
		public <U, OPTION extends Manifest<U, Option<?>>> Option<U> flatMap(Function<? super T, OPTION> mapper) {
			return (Option<U>) mapper.apply(value);
		}

		@Override
		public <U> Option<U> unit(U u) {
			return Option.of(u);
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
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
			return String.format("Some(%s)", value);
		}
	}

	/**
	 * None is a singleton representation of the undefined {@link javaslang.monad.Option}. The instance is obtained by
	 * calling {@link #instance()}.
	 *
	 * @param <T> The type of the optional value.
	 */
	static final class None<T> implements Option<T>, Serializable {

		private static final long serialVersionUID = -7265680402159660165L;

		/**
		 * The singleton instance of None.
		 */
		private static final None<?> INSTANCE = new None<>();

		/**
		 * Hidden constructor.
		 */
		private None() {
		}

		/**
		 * Returns the singleton instance of None as {@code None<T>} in the context of a type {@code <T>}, e.g.
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
			final None<T> none = (None<T>) INSTANCE;
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
		public boolean isNotPresent() {
			return true;
		}

		@Override
		public void ifPresent(Consumer<? super T> consumer) {
			// nothing to do
		}

		@Override
		public Option<T> filter(Predicate<? super T> predicate) {
			// semantically correct but structurally the same as <code>return this;</code>
			return None.instance();
		}

		@Override
		public void forEach(Consumer<? super T> action) {
			// nothing to do
		}

		@Override
		public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
			return None.instance();
		}

		@Override
		public <U, OPTION extends Manifest<U, Option<?>>> Option<U> flatMap(Function<? super T, OPTION> mapper) {
			return None.instance();
		}

		/**
		 * {@code None<A>.unit(B)} returns {@code None<B>} to be consistet with {@code Monad<A>.map(F<A,B>)} which is by
		 * default {@code flatMap(a -> unit((B) f.apply(a)))}.
		 * 
		 * @param u a value of type U
		 * @return A new instance of Option&lt;U&gt;
		 *
		 * @param <U> The type of the new Option's value, if present.
		 */
		@Override
		public <U> Option<U> unit(U u) {
			return None.instance();
		}

		@Override
		public boolean equals(Object o) {
			return o == this;
		}

		@Override
		public int hashCode() {
			return Objects.hash();
		}

		@Override
		public String toString() {
			return "None";
		}

		// -- Serializable implementation

		/**
		 * Instance control for object serialization.
		 * 
		 * @return The singleton instance of None.
		 * @see java.io.Serializable
		 */
		private Object readResolve() {
			return INSTANCE;
		}
	}
}
