/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.match.Matcher;

/**
 * Replacement for {@link java.util.Optional}.
 * <p>
 * Option is a <a href="http://stackoverflow.com/questions/13454347/monads-with-java-8">monadic</a>
 * container type which represents an optional value. Instances of Option are either an instance of
 * {@link javaslang.option.Some} or the singleton {@link javaslang.option.None}.
 * <p>
 * Most of the API is taken from {@link java.util.Optional}. A similar type can be found in <a
 * href="http://hackage.haskell.org/package/base-4.6.0.1/docs/Data-Maybe.html">Haskell</a> and <a
 * href="http://www.scala-lang.org/api/current/#scala.Option">Scala</a>.
 * 
 * @param <T> The type of the optional value.
 */
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
	
	void forEach(Consumer<? super T> action);
	
	default <S> S match(Matcher<S> matcher) {
		Objects.requireNonNull(matcher);
		return matcher.apply(this);
	}

}
