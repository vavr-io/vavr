package javaslang.util;

import static javaslang.lang.Lang.require;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
	 * If the type of the object applied to this Matcher (see {@link #apply(Object)}) is assignable
	 * to S, the given object is applied to consumer.<br>
	 * <br>
	 * Use this method to match a type.
	 * 
	 * @param consumer A consumer.
	 * @return this, the current instance of Matcher.
	 */
	public <S> Matcher<T> caze(Consumer<S> consumer) {
		require(consumer != null, "consumer is null");
		cases.add(new Case<>(None.instance(), (S o) -> {
			consumer.accept(o);
			return null;
		}));
		return this;
	}

	/**
	 * If the type of the object applied to this Matcher (see {@link #apply(Object)}) is assignable
	 * to S, the given object is applied to supplier.<br>
	 * <br>
	 * Use this method to match a type.
	 * 
	 * @param supplier A supplier.
	 * @return this, the current instance of Matcher.
	 */
	public Matcher<T> caze(Supplier<T> supplier) {
		require(supplier != null, "supplier is null");
		cases.add(new Case<>(None.instance(), o -> supplier.get()));
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
	 * If the given prototype value is equal to the object applied to this Matcher (see
	 * {@link #apply(Object)}), the object is applied to consumer.<br>
	 * <br>
	 * Use this method to match a value and consume it, i.e. produce side-effect(s).
	 * 
	 * @param prototype
	 * @param consumer
	 * @return this, the current instance of Matcher.
	 */
	public <S> Matcher<T> caze(S prototype, Consumer<S> consumer) {
		require(consumer != null, "consumer is null");
		cases.add(new Case<>(new Some<>(prototype), (S o) -> {
			consumer.accept(o);
			return null;
		}));
		return this;
	}

	/**
	 * If the given prototype value is equal to the object applied to this Matcher (see
	 * {@link #apply(Object)}), the object is applied to supplier.<br>
	 * <br>
	 * Use this method to match a value and return a result orthogonal (read <em>independent</em>)
	 * to the value.
	 * 
	 * @param prototype
	 * @param supplier
	 * @return this, the current instance of Matcher.
	 */
	public <S> Matcher<T> caze(S prototype, Supplier<T> supplier) {
		require(supplier != null, "supplier is null");
		cases.add(new Case<>(new Some<>(prototype), o -> supplier.get()));
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
			try {
				if (caze.isApplicableTo(obj)) {
					return caze.apply(obj);
				}
			} catch (ClassCastException x) {
				// TODO: may also occur within f.apply if o has correct type
			} catch (NullPointerException x) {
				// TODO: may also occur within f.apply if o has correct type
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
		 * Checks if a given object is applicable to the Matcher case represented by this Case.
		 * 
		 * @param obj An object, may be null.
		 * @return true, if prototype is None or prototype is Some(value) and value equals obj,
		 *         false otherwise.
		 */
		boolean isApplicableTo(Object obj) {
			return prototype.map(
					val -> val == obj || (val != null && val.equals(obj)))
					.orElse(true);
		}

		/**
		 * Apply the function of this Case to the given object.
		 * 
		 * @param obj An object.
		 * @return The result of function.apply(obj).
		 */
		@SuppressWarnings("unchecked")
		T apply(Object obj) {
			return ((Function<Object, T>) function).apply(obj);
		}
	}
}
