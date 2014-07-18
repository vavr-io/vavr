/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import static javaslang.Lang.require;
import static javaslang.Lang.requireNonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Supplier;

import javaslang.Lambdas;
import javaslang.exception.NonFatal;
import javaslang.option.None;
import javaslang.option.Option;
import javaslang.option.Some;

// TODO: new match builder ensuring: 1) no case after orElse, 2) orElse not twice, 3) apply after case or orElse

/**
 * A better switch for Java. A Match...
 * <ul>
 * <li>is an expression, i.e. the call of {@link #apply(Object)} results in a value. In fact it is a
 * {@code Function<Object, R>}.</li>
 * <li>is able to match types</li>
 * <li>is able to match values</li>
 * <li>lazily processes an object in the case of a match</li>
 * </ul>
 * 
 * See {@link Matchs} for convenience methods creating a matcher.
 * <p>
 * TODO: remove these methods? it's not nice to pass an 'unfinished' object as arg in favor of a nice api
 * Match is a first class member of the monads provided with javaslang. See
 * {@link javaslang.option.Option#match(Match.Builder)}, {@link javaslang.exception.Try#match(Match.Builder)},
 * {@link javaslang.either.Either.LeftProjection#match(Match.Builder)} and
 * {@link javaslang.either.Either.RightProjection#match(Match.Builder)}.
 *
 * @param <R> The result type of the Match expression.
 */
public class Match<R> implements Function<Object, R> {

	private final List<Case<R>> cases;
	private final Option<Supplier<R>> defaultOption;
	
	private Match(List<Case<R>> cases, Option<Supplier<R>> defaultOption) {
		this.cases = cases;
		this.defaultOption = defaultOption;
	}

	/**
	 * Applies an object to this matcher. This is the implementation of the {@link Function}
	 * interface.
	 * 
	 * @param obj An object.
	 * @return The result when applying the given obj to the first matching case. If the case has a
	 *         consumer, the result is null, otherwise the result of the underlying function or
	 *         supplier.
	 * @throws MatchError if no Match case matches the given object and no default is defined via orElse().
	 * @throws NonFatal if an error occurs executing the matched case.
	 */
	@Override
	public R apply(Object obj) {
		require(!cases.isEmpty() || defaultOption.isPresent(), "empty match");
		for (Case<R> caze : cases) {
			if (caze.isApplicable(obj)) {
				return caze.apply(obj);
			}
		}
		return defaultOption.orElseThrow(() -> new MatchError(obj)).get();
	}

	/**
	 * Internal representation of a Match case.
	 * 
	 * @param <R> The same type as the return type of the Match a case belongs to.
	 */
	static class Case<R> {
		final Option<?> prototype;
		final Function<?, R> function;
		final Class<?> parameterType;

		/**
		 * Constructs a Case, used for functions having an object parameter type.
		 * 
		 * @param prototype A prototype object.
		 * @param function A serializable function.
		 */
		Case(Option<?> prototype, SerializableFunction<?, R> function) {
			this.prototype = prototype;
			this.function = function;
			this.parameterType = Lambdas.getLambdaSignature(function).getParameterTypes()[0];
		}

		/**
		 * Constructs a Case, used for functions having a primitive parameter type.
		 * 
		 * @param prototype A prototype object.
		 * @param boxedFunction A function with boxed argument.
		 * @param parameterType The type of the unboxed function argument.
		 */
		Case(Option<?> prototype, Function<?, R> boxedFunction, Class<?> parameterType) {
			this.prototype = prototype;
			this.function = boxedFunction;
			this.parameterType = parameterType;
		}

		/**
		 * Checks if the Match case represented by this Case can be applied to the given object. The
		 * null value is applicable, if the prototype is null. If no prototype is specified, a null
		 * obj is not applicable because the first occuring function would match otherwise, which
		 * wouldn't be correct in general.
		 * 
		 * @param obj An object, may be null.
		 * @return true, if prototype is None or prototype is Some(value) and value equals obj,
		 *         false otherwise.
		 */
		boolean isApplicable(Object obj) {
			final boolean isCompatible = obj == null
					|| parameterType.isAssignableFrom(obj.getClass());
			return isCompatible
					&& prototype.map(val -> val == obj || (val != null && val.equals(obj))).orElse(
							obj != null);
		}

		/**
		 * Apply the function of this Case to the given object.
		 * 
		 * @param obj An object.
		 * @return The result of function.apply(obj).
		 */
		@SuppressWarnings("unchecked")
		R apply(Object obj) {
			return ((Function<Object, R>) function).apply(obj);
		}
	}
	
	// -- case lambda args

	/**
	 * A function which implements Serializable in order to obtain runtime type information about
	 * the lambda via {@link javaslang.Lambdas#getLambdaSignature(Serializable)}.
	 *
	 * @param <T> The parameter type of the function.
	 * @param <R> The return type of the function.
	 */
	@FunctionalInterface
	public static interface SerializableFunction<T, R> extends Function<T, R>, Serializable {
	}

	/**
	 * Represents a function that accepts a boolean-valued argument and produces a result. This is
	 * the {@code boolean}-consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface BooleanFunction<R> {
		R apply(boolean b);
	}

	/**
	 * Represents a function that accepts a byte-valued argument and produces a result. This is the
	 * {@code byte}-consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface ByteFunction<R> {
		R apply(byte b);
	}

	/**
	 * Represents a function that accepts a char-valued argument and produces a result. This is the
	 * {@code char}-consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface CharFunction<R> {
		R apply(char c);
	}

	/**
	 * Represents a function that accepts a float-valued argument and produces a result. This is the
	 * {@code float}-consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface FloatFunction<R> {
		R apply(float f);
	}

	/**
	 * Represents a function that accepts a short-valued argument and produces a result. This is the
	 * {@code short}-consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface ShortFunction<R> {
		R apply(short s);
	}
	
	// -- builder
	
	public static class Builder<R> extends OrElseBuilder<R> {
		
		private final List<Case<R>> cases = new ArrayList<>();
		private Option<Supplier<R>> defaultOption = Option.empty();
		
		/**
		 * Use this method to match by object type T. An object o matches this case, if
		 * {@code o != null && T isAssignableFrom o.getClass()}.
		 * 
		 * @param <T> (super-)type of the object to be matched
		 * @param function A SerializableFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public <T> Builder<R> caze(SerializableFunction<T, R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), function));
			return this;
		}

		/**
		 * Use this method to match by prototype value of object type T. An object o matches this case,
		 * if {@code prototype == o || (prototype != null && prototype.equals(o))}.
		 * 
		 * @param <T> (super-)type of the object to be matched
		 * @param prototype An object to be matched by equality as defined above.
		 * @param function A SerializableFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		// DEV NOTE: the compiler cannot distinguish between primitive and Object types, e.g.
		// public Match<R> caze(int prototype, IntFunction<R> function)
		// Autoboxing does not work here.
		public <T> Builder<R> caze(T prototype, SerializableFunction<T, R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(new Some<>(prototype), function));
			return this;
		}

		/**
		 * Use this method to match by boolean. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Boolean.class}.
		 * 
		 * @param function A BooleanFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(BooleanFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Boolean b) -> function.apply(b), Boolean.class));
			return this;
		}

		/**
		 * Use this method to match by byte. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Byte.class}.
		 * 
		 * @param function A ByteFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(ByteFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Byte b) -> function.apply(b), Byte.class));
			return this;
		}

		/**
		 * Use this method to match by char. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Character.class}.
		 * 
		 * @param function A CharFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(CharFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Character c) -> function.apply(c), Character.class));
			return this;
		}

		/**
		 * Use this method to match by double. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Double.class}.
		 * 
		 * @param function A DoubleFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(DoubleFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Double d) -> function.apply(d), Double.class));
			return this;
		}

		/**
		 * Use this method to match by float. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Float.class}.
		 * 
		 * @param function A FloatFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(FloatFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Float f) -> function.apply(f), Float.class));
			return this;
		}

		/**
		 * Use this method to match by int. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Integer.class}.
		 * 
		 * @param function A IntFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(IntFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Integer i) -> function.apply(i), Integer.class));
			return this;
		}

		/**
		 * Use this method to match by long. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Long.class}.
		 * 
		 * @param function A LongFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(LongFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Long l) -> function.apply(l), Long.class));
			return this;
		}

		/**
		 * Use this method to match by short. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Short.class}.
		 * 
		 * @param function A ShortFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws IllegalStateException if function is null.
		 */
		public Builder<R> caze(ShortFunction<R> function) {
			requireNonNull(function, "function is null");
			cases.add(new Case<>(None.instance(), (Short s) -> function.apply(s), Short.class));
			return this;
		}

		/* (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#getCases()
		 */
		@Override
		protected List<Case<R>> getCases() {
			return cases;
		}

		/* (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#getDefault()
		 */
		@Override
		protected Option<Supplier<R>> getDefault() {
			return defaultOption;
		}

		/* (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#setDefault(javaslang.option.Option)
		 */
		@Override
		protected void setDefault(Option<Supplier<R>> defaultOption) {
			this.defaultOption = defaultOption;
		}
	}
	
	public static abstract class OrElseBuilder<R> extends MatchBuilder<R> {
		
		/**
		 * Defines the default return value.
		 * 
		 * @param <T> (super-)type of the object to be matched
		 * @param defaultSupplier Supplier of the default return value of this Match.
		 * @return this, the current instance of Match.
		 */
		public <T> MatchBuilder<R> orElse(Supplier<R> defaultSupplier) {
			requireNonNull(defaultSupplier, "defaultSupplier is null");
			setDefault(Option.of(defaultSupplier));
			return this;
		}		
	}

	public static abstract class MatchBuilder<R> {
		
		public Match<R> build() {
			return new Match<>(getCases(), getDefault());
		}
		
		protected abstract List<Case<R>> getCases();
		
		protected abstract Option<Supplier<R>> getDefault();
		
		protected abstract void setDefault(Option<Supplier<R>> defaultOption);
		
	}

}
