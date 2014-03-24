/**                       ___ __          ,                   ___                                
 *  __ ___ _____  _______/  /  / ______  / \_   ______ ______/__/_____  ______  _______ _____    
 * /  '__/'  _  \/   ___/      \/   "__\/  _/__/ ____/'  ___/  /   "__\/   ,  \/   ___/'  "__\   
 * \__/  \______/\______\__/___/\______/\___/\_____/ \______\_/\______/\__/___/\______\______/.io
 * Licensed under the Apache License, Version 2.0. Copyright 2014 Daniel Dietrich.
 */
package javaslang.match;

import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;
import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

/**
 * A better switch for Java. A Matcher is
 * <ul>
 * <li>an expression, i.e. the call of {@link #apply(Object)} results in a value. In fact it is a
 * Function.</li>
 * <li>able to match types</li>
 * <li>able to match values</li>
 * <li>lazily processes an object in the case of a match</li>
 * </ul>
 * 
 * See {@link Matchers} for convenience methods creating a matcher.
 *
 * @param <T> The result type of the Matcher expression.
 */
public final class Matcher<T> implements Function<Object, T> {

	private List<Case<T>> cases = new ArrayList<>();

	/**
	 * Creates a Matcher.
	 * @see Matchers#caze
	 */
	public Matcher() {
	}

	/**
	 * Use this method to match by type S. Implementations of this method apply the given function
	 * to an object, if the object is of type S.
	 * 
	 * @param <S> type of the object to be matched
	 * @param function A function which is applied to a matched object.
	 * @return this, the current instance of Matcher.
	 * @throws IllegalStateException if function is null.
	 */
	public <S> Matcher<T> caze(Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(None.instance(), function));
		return this;
	}

	/**
	 * Use this method to match by value. Implementations of this method apply the given function
	 * to an object, if the object equals a prototype of type S.
	 * 
	 * @param <S> type of the prototype object
	 * @param prototype An object to be matched by equality.
	 * @param function A function which is applied to a matched object.
	 * @return this, the current instance of Matcher.
	 * @throws IllegalStateException if function is null.
	 */
	public <S> Matcher<T> caze(S prototype, Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(new Some<>(prototype), function));
		return this;
	}

	/**
	 * Applies an object to this matcher.
	 * 
	 * @param obj An object.
	 * @return The result when applying the given obj to the first matching case. If the case has a
	 *         consumer, the result is null, otherwise the result of the underlying function or
	 *         supplier.
	 * @throws MatchError if no Matcher case matches the given object.
	 */
	@Override
	public T apply(Object obj) throws MatchError {
		for (Case<T> caze : cases) {
			if (caze.isApplicable(obj)) {
				final Option<Try<T>> value = caze.tryToApply(obj);
				if (value.isPresent()) {
					return value.get().get();
				}
			}
		}
		throw new MatchError(obj);
	}

	/**
	 * Internal representation of a Matcher case.
	 * 
	 * @param <T> The same type as the return type of the Matcher a case belongs to.
	 */
	static class Case<T> {
		final Option<?> prototype;
		final Function<?, T> function;

		/**
		 * Constructs a Case.
		 * 
		 * @param prototype
		 * @param function
		 */
		Case(Option<?> prototype, Function<?, T> function) {
			this.prototype = prototype;
			this.function = function;
		}

		/**
		 * Checks if the Matcher case represented by this Case can be applied to the given object.
		 * 
		 * @param obj An object, may be null.
		 * @return true, if prototype is None or prototype is Some(value) and value equals obj,
		 *         false otherwise.
		 */
		boolean isApplicable(Object obj) {
			return prototype.map(
					val -> val == obj || (val != null && val.equals(obj)))
					.orElse(obj != null);
		}

		/**
		 * Apply the function of this Case to the given object.
		 * 
		 * @param obj An object.
		 * @return The result of function.apply(obj).
		 */
		Option<Try<T>> tryToApply(Object obj) {
			try {
				@SuppressWarnings("unchecked")
				final T value = ((Function<Object, T>) function).apply(obj);
				return new Some<>(new Success<>(value));
			} catch (ClassCastException x) {
				return None.instance();
			} catch (Throwable x) {
				return new Some<>(new Failure<>(x));
			}
		}
	}
}
