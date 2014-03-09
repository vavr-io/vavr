package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
	 * Use {@link Matcher#of(Class)} or one of {@link Matchers#caze} to create a Matcher.
	 */
	Matcher() {
	}

	/**
	 * If the type of the object applied to this Matcher (see {@link #apply(Object)}) is assignable
	 * to S, the given object is applied to function.<br>
	 * <br>
	 * Use this method to match a type.
	 * 
	 * @param function A function.
	 * @return this, the current instance of Matcher.
	 */
	public <S> Matcher<T> caze(Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(None.instance(), function));
		return this;
	}

	/**
	 * If the given prototype value is equal to the object applied to this Matcher (see
	 * {@link #apply(Object)}), the object is applied to function.<br>
	 * <br>
	 * Use this method to match a value and map it.
	 * 
	 * @param prototype
	 * @param function
	 * @return this, the current instance of Matcher.
	 */
	public <S> Matcher<T> caze(S prototype, Function<S, T> function) {
		require(function != null, "function is null");
		cases.add(new Case<>(new Some<>(prototype), function));
		return this;
	}

	/**
	 * Creates a Matcher. See also {@link Matchers}.
	 * 
	 * @param type Makes the compiler aware of the generic Matcher type T.
	 * @return A Matcher instance of type <code>Matcher&lt;T&gt;</code>.
	 */
	public static <T> Matcher<T> of(Class<T> type) {
		return new Matcher<>();
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
