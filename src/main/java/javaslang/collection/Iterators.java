/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.monad.None;
import javaslang.monad.Option;
import javaslang.monad.Some;

public final class Iterators {

	private Iterators() {
		throw new AssertionError(Iterators.class.getName() + " is not intended to be instantiated.");
	}

	public static <T> Iterator<T> of(BooleanSupplier hasNext, Supplier<T> next) {
		return new Iterator<T>() {

			@Override
			public boolean hasNext() {
				return hasNext.getAsBoolean();
			}

			@Override
			public T next() {
				return next.get();
			}
		};
	}

	public static <T> Iterator<T> of(Iterator<T> iterator, Predicate<? super T> whileCondition) {
		return new Iterator<T>() {

			Option<T> next = testNext();

			@Override
			public boolean hasNext() {
				return next.isPresent();
			}

			@Override
			public T next() {
				final T result = next.orElseThrow(() -> new NoSuchElementException("no more elements"));
				next = testNext();
				return result;
			}

			Option<T> testNext() {
				return (iterator.hasNext() ? new Some<>(iterator.next()) : None.<T> instance()).filter(whileCondition);
			}
		};
	}
}
