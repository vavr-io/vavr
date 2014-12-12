/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.match;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javaslang.Require;
import javaslang.Lambda.λ1;
import javaslang.Lambda.λ2;
import javaslang.Require.UnsatisfiedRequirementException;
import javaslang.Tuple;
import javaslang.monad.Option;
import javaslang.monad.Option.None;
import javaslang.monad.Option.Some;

/**
 * A better switch for Java. A Match...
 * <ul>
 * <li>is lazy</li>
 * <li>is an expression, i.e. a {@code Function<Object, R>}</li>
 * <li>is able to match types, i.e. {@code Matchs.caze((byte b) -> "a byte: " + b)}</li>
 * <li>is able to match values, i.e. {@code Matchs.caze(BigDecimal.ZERO, b -> "Zero: " + b)}</li>
 * </ul>
 * 
 * Example of a Match as <strong>partial</strong> function:
 * 
 * <pre>
 * <code>final Match&lt;Number&gt; toNumber = new Match.Builder&lt;Number&gt;()
 *     .caze((Integer i) -&gt; i)
 *     .caze((String s) -&gt; new BigDecimal(s))
 *     .build();
 * final Number number = toNumber.apply(1.0d); // throws a MatchError</code>
 * </pre>
 * 
 * Example of a Match as <strong>total</strong> function:
 * 
 * <pre>
 * <code>final Match&lt;Number&gt; toNumber = new Match.Builder&lt;Number&gt;()
 *     .caze((Integer i) -&gt; i)
 *     .caze((String s) -&gt; new BigDecimal(s))
 *     .orElse(() -&gt; -1)
 *     .build();
 * final Number number = toNumber.apply(1.0d); // result: -1</code>
 * </pre>
 * <p>
 * The following calls are equivalent:
 * <ul>
 * <li>{@code new Match.Builder<R>.caze(...).build().apply(obj)}</li>
 * <li>{@code new Match.Builder<R>.caze(...).apply(obj)}</li>
 * <li>{@code Matchs.caze(...).apply(obj)}</li>
 * </ul>
 * 
 * @param <R> The result type of the Match expression.
 * 
 * @see javaslang.match.Matchs
 */
public final class Match<R> implements Function<Object, R> {

	private final List<Function<Object, Option<R>>> cases;
	private final Option<Supplier<R>> defaultOption;

	private Match(List<Function<Object, Option<R>>> cases, Option<Supplier<R>> defaultOption) {
		this.cases = cases;
		this.defaultOption = defaultOption;
	}

	/**
	 * Applies an object to this matcher. This is the implementation of the {@link Function} interface.
	 * 
	 * @param obj An object.
	 * @return The result when applying the given obj to the first matching case. If the case has a consumer, the result
	 *         is null, otherwise the result of the underlying function or supplier.
	 * @throws MatchError if no Match case matches the given object and no default is defined via orElse().
	 * @throws javaslang.monad.Try.Failure.NonFatal if an error occurs executing the matched case.
	 */
	@Override
	public R apply(Object obj) {
		for (Function<Object, Option<R>> caze : cases) {
			final Option<R> result = caze.apply(obj);
			if (result.isPresent()) {
				return result.get();
			}
		}
		return defaultOption.orElseThrow(() -> new MatchError(obj)).get();
	}

	// -- lambda types for cases

	/**
	 * Represents a function that accepts a boolean-valued argument and produces a result. This is the {@code boolean}
	 * -consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface BooleanFunction<R> {
		R apply(boolean b);
	}

	/**
	 * Represents a function that accepts a byte-valued argument and produces a result. This is the {@code byte}
	 * -consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface ByteFunction<R> {
		R apply(byte b);
	}

	/**
	 * Represents a function that accepts a char-valued argument and produces a result. This is the {@code char}
	 * -consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface CharFunction<R> {
		R apply(char c);
	}

	/**
	 * Represents a function that accepts a float-valued argument and produces a result. This is the {@code float}
	 * -consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface FloatFunction<R> {
		R apply(float f);
	}

	/**
	 * Represents a function that accepts a short-valued argument and produces a result. This is the {@code short}
	 * -consuming primitive specialization for {@link Function}.
	 *
	 * @param <R> the type of the result of the function
	 */
	@FunctionalInterface
	public static interface ShortFunction<R> {
		R apply(short s);
	}

	// -- builder

	public static class Builder<R> extends OrElseBuilder<R> {

		private final List<Function<Object, Option<R>>> cases = new ArrayList<>();
		private Option<Supplier<R>> defaultOption = Option.none();

		/**
		 * Use this method to match by object type T. An object o matches this case, if
		 * {@code o != null && T isAssignableFrom o.getClass()}.
		 * 
		 * @param function A Function which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(λ1<?, R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), function));
			return this;
		}

		/**
		 * Use this method to match by prototype value of object type T. An object o matches this case, if
		 * {@code prototype == o || (prototype != null && prototype.equals(o))}.
		 * 
		 * @param <T> type of the object to be matched
		 * @param prototype An object to be matched by equality as defined above.
		 * @param function A Function which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		// DEV NOTE: the compiler cannot distinguish between primitive and Object types, e.g.
		// public Match<R> caze(int prototype, IntFunction<R> function)
		// Autoboxing does not work here.
		public <T> Builder<R> caze(T prototype, λ1<T, R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(new Some<>(prototype), function));
			return this;
		}

		public <T, D extends Tuple> Builder<R> caze(Pattern<T, ?, D> pattern, λ2<T, D, R> function) {
			Require.nonNull(pattern, "pattern is null");
			Require.nonNull(function, "function is null");
			final Function<Object, Option<R>> mapping = obj -> {
				if (pattern.isApplicable(obj)) {
					return pattern.apply(obj).map(d -> function.apply(d._1, d._2));
				} else {
					return None.instance();
				}
			};
			cases.add(mapping);
			return this;
		}

		// TODO(Issue #36): Support Consumer / void return value
		//		public <T> Builder<R> caze(SerializableConsumer<T> consumer) {
		//			requireNonNull(consumer, "consumer is null");
		//			cases.add(caze(None.instance(), (T t) -> {
		//				consumer.accept(t);
		//			}, Void.class));
		//			return this;
		//		}

		/**
		 * Use this method to match by boolean. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Boolean.class}.
		 * 
		 * @param function A BooleanFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(BooleanFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Boolean b) -> function.apply(b), Boolean.class));
			return this;
		}

		/**
		 * Use this method to match by byte. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Byte.class}.
		 * 
		 * @param function A ByteFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(ByteFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Byte b) -> function.apply(b), Byte.class));
			return this;
		}

		/**
		 * Use this method to match by char. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Character.class}.
		 * 
		 * @param function A CharFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(CharFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Character c) -> function.apply(c), Character.class));
			return this;
		}

		/**
		 * Use this method to match by double. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Double.class}.
		 * 
		 * @param function A DoubleFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(DoubleFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Double d) -> function.apply(d), Double.class));
			return this;
		}

		/**
		 * Use this method to match by float. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Float.class}.
		 * 
		 * @param function A FloatFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(FloatFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Float f) -> function.apply(f), Float.class));
			return this;
		}

		/**
		 * Use this method to match by int. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Integer.class}.
		 * 
		 * @param function A IntFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(IntFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Integer i) -> function.apply(i), Integer.class));
			return this;
		}

		/**
		 * Use this method to match by long. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Long.class}.
		 * 
		 * @param function A LongFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(LongFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Long l) -> function.apply(l), Long.class));
			return this;
		}

		/**
		 * Use this method to match by short. An object o matches this case, if {@code o != null &&
		 * o.getClass() == Short.class}.
		 * 
		 * @param function A ShortFunction which is applied to a matched object.
		 * @return this, the current instance of Match.
		 * @throws UnsatisfiedRequirementException if function is null.
		 */
		public Builder<R> caze(ShortFunction<R> function) {
			Require.nonNull(function, "function is null");
			cases.add(caze(None.instance(), (Short s) -> function.apply(s), Short.class));
			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#getCases()
		 */
		@Override
		protected List<Function<Object, Option<R>>> getCases() {
			return cases;
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#getDefault()
		 */
		@Override
		protected Option<Supplier<R>> getDefault() {
			return defaultOption;
		}

		/*
		 * (non-Javadoc)
		 * @see javaslang.match.Match.MatchBuilder#setDefault(javaslang.option.Option)
		 */
		@Override
		protected void setDefault(Option<Supplier<R>> defaultOption) {
			this.defaultOption = defaultOption;
		}

		private Function<Object, Option<R>> caze(Option<?> prototype, λ1<?, R> function) {
			return caze(prototype, function, function.getType().parameterType(0));
		}

		/**
		 * Constructs a Case, used for functions having a primitive parameter type.
		 * 
		 * @param prototype A prototype object.
		 * @param function A function with boxed argument.
		 * @param parameterType The type of the unboxed function argument.
		 */
		// TODO: split prototype and non-prototype cases to increase performance
		private Function<Object, Option<R>> caze(Option<?> prototype, Function<?, R> function, Class<?> parameterType) {
			final Predicate<Object> applicable = obj -> {
				final boolean isCompatible = obj == null || parameterType.isAssignableFrom(obj.getClass());
				return isCompatible
						&& prototype.map(val -> val == obj || (val != null && val.equals(obj))).orElse(obj != null);
			};
			return obj -> {
				if (applicable.test(obj)) {
					@SuppressWarnings("unchecked")
					final R result = ((Function<Object, R>) function).apply(obj);
					return new Some<>(result);
				} else {
					return None.instance();
				}
			};
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
			Require.nonNull(defaultSupplier, "defaultSupplier is null");
			setDefault(Option.of(defaultSupplier));
			return this;
		}
	}

	public static abstract class MatchBuilder<R> {

		public Match<R> build() {
			return new Match<>(getCases(), getDefault());
		}

		/**
		 * Shortcut for {@code build().apply(obj)}.
		 * 
		 * @param obj An object.
		 * @return The match result.
		 */
		public R apply(Object obj) {
			return build().apply(obj);
		}

		protected abstract List<Function<Object, Option<R>>> getCases();

		protected abstract Option<Supplier<R>> getDefault();

		protected abstract void setDefault(Option<Supplier<R>> defaultOption);
	}
}
