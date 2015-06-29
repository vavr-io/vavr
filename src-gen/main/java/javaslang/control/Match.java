/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javaslang.Function1;
import javaslang.Lazy;
import javaslang.collection.List;
import javaslang.collection.TraversableOnce;

/**
 * {@code Match} is a better switch for Java. Some characteristics of {@code Match} are:
 * <ul>
 * <li>it has a fluent API</li>
 * <li>it is a {@code Function<Object, R>}</li>
 * <li>it is able to match types, i.e. {@code Match.when((byte b) -> "a byte: " + b)}</li>
 * <li>it is able to match values, i.e. {@code Match.when(BigDecimal.ZERO, b -> "Zero: " + b)}</li>
 * </ul>
 *
 * Example of a Match as <a href="http://en.wikipedia.org/wiki/Partial_function"><strong>partial</strong> function</a>:
 *
 * <pre>
 * <code>
 * final Match&lt;Number&gt; toNumber = Match.as(Number.class)
 *     .when((Integer i) -&gt; i)
 *     .when((String s) -&gt; new BigDecimal(s));
 * final Number number = toNumber.apply(1.0d); // throws a MatchError
 * </code>
 * </pre>
 *
 * Example of a Match as <a href="http://en.wikipedia.org/wiki/Function_(mathematics)"><strong>total</strong> function</a>:
 *
 * <pre>
 * <code>
 * Match.as(Number.class)
 *     .when((Integer i) -&gt; i)
 *     .when((String s) -&gt; new BigDecimal(s))
 *     .otherwise(() -&gt; -1)
 *     .apply(1.0d); // result: -1
 * </code>
 * </pre>
 *
 * @param <R> The result type of the {@code Match}.
 * @since 1.0.0
 */
public interface Match<R> extends Function<Object, R> {

    /**
     * Applies this {@code Match} to an {@code Object}.
     *
     * @param o an {@code Object}
     * @throws MatchError if no {@code Case} matched
     */
    @Override
    R apply(Object o);

    /**
     * Creates a type-safe match by fixating the value to be matched.
     * 
     * @param value the value to be matched
     * @return a new type-safe match builder
     */
    static <T> SafeMatch.Of<T> of(T value) {
        return new SafeMatch.Of<>(value);
    }

    static SafeMatchBoolean.OfBoolean of(boolean value) {
        return new SafeMatchBoolean.OfBoolean(value);
    }

    static SafeMatchByte.OfByte of(byte value) {
        return new SafeMatchByte.OfByte(value);
    }

    static SafeMatchChar.OfChar of(char value) {
        return new SafeMatchChar.OfChar(value);
    }

    static SafeMatchDouble.OfDouble of(double value) {
        return new SafeMatchDouble.OfDouble(value);
    }

    static SafeMatchFloat.OfFloat of(float value) {
        return new SafeMatchFloat.OfFloat(value);
    }

    static SafeMatchInt.OfInt of(int value) {
        return new SafeMatchInt.OfInt(value);
    }

    static SafeMatchLong.OfLong of(long value) {
        return new SafeMatchLong.OfLong(value);
    }

    static SafeMatchShort.OfShort of(short value) {
        return new SafeMatchShort.OfShort(value);
    }

    /**
     * Specifies the type of the match expression. In many cases it is not necessary to call {@code as}. This
     * method is intended to be used for readability reasons when the upper bound of the cases cannot be inferred,
     * i.e. instead of
     *
     * <pre>
     * <code>
     * final Match&lt;Number&gt; toNumber = Match
     *         .&lt;Number&gt; when((Integer i) -&gt; i)
     *         .when((String s) -&gt; new BigDecimal(s))
     * </code>
     * </pre>
     *
     * we write
     *
     * <pre>
     * <code>
     * final Match&lt;Number&gt; toNumber = Match.as(Number.class)
     *         .when((Integer i) -&gt; i)
     *         .when((String s) -&gt; new BigDecimal(s))
     * </code>
     * </pre>
     *
     * @param type the hint of type {@code R}
     * @param <R>  the type of the {@code Match} expression
     * @return a new match builder
     */
    static <R> Typed<R> as(Class<R> type) {
        Objects.requireNonNull(type, "type is null");
        return new Typed<>();
    }

    /**
     * Creates a {@code Match.Case} by value.
     *
     * @param <T> type of the prototype value
     * @param <R> result type of the matched case
     * @param prototype A specific value to be matched
     * @param function A function which is applied to the value given a match
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    static <T, R> Case<R> when(T prototype, Function1<? super T, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(prototype, function);
    }

    /**
     * Creates a {@code Match.Case} by a collection of values.
     *
     * @param <T> type of the prototype values
     * @param <R> result type of the matched case
     * @param prototypes A specific set of values to be matched
     * @param function A function which is applied to the values given a match
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    static <T, R> Case<R> whenIn(T[] prototypes, Function1<? super T, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(prototypes, function);
    }

    /**
     * Creates a {@code Match.Case} by type.
     *
     * @param <R> result type of the matched case
     * @param function An {@code Object} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(Function1<?, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code boolean}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code boolean} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(BooleanFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code byte}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code byte} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(ByteFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code char}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code char} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(CharFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code double}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code double} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(DoubleFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code float}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code float} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(FloatFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code int}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code int} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(IntFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code long}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code long} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(LongFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * Creates a {@code Match.Case} by primitive type {@code short}.
     *
     * @param <R> result type of the matched case
     * @param function An {@code short} to {@code R} function
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    @SuppressWarnings("overloads")
    static <R> Case<R> when(ShortFunction<? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * The result of {@code Match.as(Class)}, which explicitly sets the {@code Match} result type.
     *
     * @param <R> the result type
     * @since 1.2.1
     */
    final class Typed<R> implements Expression.HasCases<R> {

        private Typed() {
        }

        @Override
        public <T> Case<R> when(T prototype, Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(prototype, function);
        }

        @Override
        public <T> Case<R> whenIn(T[] prototypes, Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(prototypes, function);
        }

        @Override
        public Case<R> when(Function1<?, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(BooleanFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(ByteFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(CharFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(DoubleFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(FloatFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(IntFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(LongFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> when(ShortFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }
    }

    /**
     * A {@code Match.Case} which matches an {@code Object} by <em>type</em> or by <em>value</em>.
     * <p>
     * Typically there is a chain of match cases. The first applicable match is applied to an object.
     * <p>
     * The {@code otherwise()} methods provide a default value which is returned if no case matches.
     *
     * @param <R> result type of the {@code Match.Case}
     * @since 1.0.0
     */
    final class Case<R> implements Match<R>, Expression.HasCases<R> {

        private final List<Function<Object, Option<R>>> cases;
        private final Lazy<Expression<R>> match;

        private Case(List<Function<Object, Option<R>>> cases) {
            this.cases = cases;
            this.match = Lazy.of(() -> new Expression<>(cases.reverse(), None.instance()));
        }

        private static <T, R> Case<R> of(T prototype, Function1<? super T, ? extends R> function) {
            return new Case<>(List.of(Case.when(new Some<>(prototype), function)));
        }

        private static <T, R> Case<R> of(T[] prototypes, Function1<? super T, ? extends R> function) {
            return new Case<>(List.of(prototypes).map(t -> Case.when(new Some<>(t), function)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(Function1<?, ? extends R> function) {
            return new Case<>(List.of(Case.when(None.instance(), function)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(BooleanFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Boolean, R>) function::apply, Boolean.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(ByteFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Byte, R>) function::apply, Byte.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(CharFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Character, R>) function::apply, Character.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(DoubleFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Double, R>) function::apply, Double.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(FloatFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Float, R>) function::apply, Float.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(IntFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Integer, R>) function::apply, Integer.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(LongFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Long, R>) function::apply, Long.class)));
        }

        @SuppressWarnings("overloads")
        private static <R> Case<R> of(ShortFunction<? extends R> function) {
          return new Case<>(List.of(Case.when(None.instance(), (Function1<Short, R>) function::apply, Short.class)));
        }

        @Override
        public R apply(Object o) {
            return match.get().apply(o);
        }

        @Override
        public <T> Case<R> when(T prototype, Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(new Some<>(prototype), function);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public <T> Case<R> whenIn(T[] prototypes, Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            List<Function<Object, Option<R>>> when = List.of(prototypes).map(p -> when(new Some<>(p), function));
            return new Case<>(cases.prependAll(when));
        }

        @Override
        public Case<R> when(Function1<?, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), function);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(BooleanFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Boolean, R>) function::apply, Boolean.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(ByteFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Byte, R>) function::apply, Byte.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(CharFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Character, R>) function::apply, Character.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(DoubleFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Double, R>) function::apply, Double.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(FloatFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Float, R>) function::apply, Float.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(IntFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Integer, R>) function::apply, Integer.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(LongFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Long, R>) function::apply, Long.class);
            return new Case<>(cases.prepend(when));
        }

        @Override
        public Case<R> when(ShortFunction<? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), (Function1<Short, R>) function::apply, Short.class);
            return new Case<>(cases.prepend(when));
        }

        /**
         * <p>Provides a default value which is returned if no case matches.</p>
         * <p>Note that this method takes the default by value which means that the input is
         * <em>eagerly evaluated</em> even if the {@code otherwise} clause of the expression is not executed.
         * Unless you already have a default value calculated or as a literal it might be better
         * to use the {@link Match.Case#otherwise(Supplier)} alternative to gain lazy evaluation.</p>
         *
         * @param defaultValue The default value.
         * @return a Match-expression
         */
        public Expression<R> otherwise(R defaultValue) {
            return new Expression<>(cases.reverse(), new Some<>(Lazy.of(() -> defaultValue)));
        }

        /**
         * <p>Provides a default value which is returned if no case matches.</p>
         * @param defaultSupplier A Supplier returning the default value.
         * @return a Match-expression
         */
        public Expression<R> otherwise(Supplier<R> defaultSupplier) {
            Objects.requireNonNull(defaultSupplier, "defaultSupplier is null");
            return new Expression<>(cases.reverse(), new Some<>(Lazy.of(defaultSupplier)));
        }

        private static <T, R> Function<Object, Option<R>> when(Option<T> prototype, Function1<T, ? extends R> function) {
            final Class<?> parameterType = function.getType().parameterType(0);
            return when(prototype, function, parameterType);
        }

        private static <T, R> Function<Object, Option<R>> when(Option<T> prototype, Function1<T, ? extends R> function, Class<?> parameterType) {
            final Predicate<Object> applicable = obj -> {
                final boolean isCompatible = obj == null || parameterType.isAssignableFrom(obj.getClass());
                return isCompatible
                        && prototype.map(val -> val == obj || (val != null && val.equals(obj))).orElse(obj != null);
            };
            return obj -> {
                if (applicable.test(obj)) {
                    @SuppressWarnings("unchecked")
                    final R result = ((Function1<Object, R>) function).apply(obj);
                    return new Some<>(result);
                } else {
                    return None.instance();
                }
            };
        }
    }

    /**
     * A final {@code Match} expression which may be applied to an {@code Object}.
     *
     * @param <R> result type of the {@code Match}
     * @since 1.0.0
     */
    final class Expression<R> implements Match<R> {

        private Iterable<Function<Object, Option<R>>> cases;
        private Option<Lazy<R>> otherwise;

        private Expression(Iterable<Function<Object, Option<R>>> cases, Option<Lazy<R>> otherwise) {
            this.cases = cases;
            this.otherwise = otherwise;
        }

        @Override
        public R apply(Object o) {
            for (Function<Object, Option<R>> when : cases) {
                final Option<R> result = when.apply(o);
                if (result.isDefined()) {
                    return result.get();
                }
            }
            return otherwise.orElseThrow(() -> new MatchError(o)).get();
        }

        // Note: placed this interface here, because interface Match cannot have private inner interfaces
        private interface HasCases<R> {

            /**
             * Creates a {@code Match.Case} by value.
             *
             * @param <T> type of the prototype value
             * @param prototype A specific value to be matched
             * @param function A function which is applied to the value given a match
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            <T> HasCases<R> when(T prototype, Function1<? super T, ? extends R> function);

            /**
             * Creates a {@code Match.Case} by values.
             *
             * @param <T> type of the prototype value
             * @param prototypes A specific value to be matched
             * @param function A function which is applied to the value given a match
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            <T> HasCases<R> whenIn(T[] prototypes, Function1<? super T, ? extends R> function);

            /**
             * Creates a {@code Match.Case} by type.
             *
             * @param function An {@code Object} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(Function1<?, ? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code boolean}.
             *
             * @param function An {@code boolean} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(BooleanFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code byte}.
             *
             * @param function An {@code byte} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(ByteFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code char}.
             *
             * @param function An {@code char} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(CharFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code double}.
             *
             * @param function An {@code double} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(DoubleFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code float}.
             *
             * @param function An {@code float} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(FloatFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code int}.
             *
             * @param function An {@code int} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(IntFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code long}.
             *
             * @param function An {@code long} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(LongFunction<? extends R> function);

            /**
             * Creates a {@code Match.Case} by primitive type {@code short}.
             *
             * @param function An {@code short} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(ShortFunction<? extends R> function);
        }
    }

    // intentionally not made Serializable
    // TODO: generate when() for primitive lambda types
    /**
     * @since 1.3.0
     */
    interface SafeMatch<T, R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        <U extends T> SafeMatch<T, R> when(Function<? super U, ? extends R> f);

        <U extends T> SafeMatch<T, R> when(U protoType, Function<? super U, ? extends R> f);

        // -- filter monadic operations

        // TODO: <U> SafeMatch<T, R> filter(Predicate<? super R> predicate);

        <U> SafeMatch<T, U> flatMap(Function<? super R, ? extends SafeMatch<T, U>> mapper);

        <U> SafeMatch<T, U> flatten(Function<? super R, ? extends SafeMatch<T, U>> f);

        <U> SafeMatch<T, U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatch<T, R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class Of<T> {

            private final T value;

            private Of(T value) {
                this.value = value;
            }

            public <R> Typed<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new Typed<>(value);
            }

            // -- when cases

            @SuppressWarnings("unchecked")

            public <U extends T, R> SafeMatch<T, R> when(Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = Function1.lift(f::apply).getType().parameterType(0);
                return Of.matches(value, paramType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            @SuppressWarnings("unchecked")

            public <U extends T, R> SafeMatch<T, R> when(U protoType, Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            // method declared here because Java 8 does not support private interface methods
            private static boolean matches(Object obj, Class<?> type) {
                return obj != null && type.isAssignableFrom(obj.getClass());
            }
        }

        /**
         * @since 1.3.0
         */
        final class Typed<T, R> {

            private final T value;

            private Typed(T value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("unchecked")

            public <U extends T> SafeMatch<T, R> when(Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = Function1.lift(f::apply).getType().parameterType(0);
                return Of.matches(value, paramType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            @SuppressWarnings("unchecked")

            public <U extends T> SafeMatch<T, R> when(U protoType, Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class Matched<T, R> implements SafeMatch<T, R> {

            private final R result;

            private Matched(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public <U extends T> Matched<T, R> when(Function<? super U, ? extends R> f) {
                // fast forward / no argument checks
                return this;
            }

            @Override
            public <U extends T> SafeMatch<T, R> when(U protoType, Function<? super U, ? extends R> f) {
                // fast forward / no argument checks
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatch<T, U> flatMap(Function<? super R, ? extends SafeMatch<T, U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatch<T, U> flatten(Function<? super R, ? extends SafeMatch<T, U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> Matched<T, U> map(Function<? super R, ? extends U> mapper) {
                return new Matched<T, U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.singleton(result).iterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class Unmatched<T, R> implements SafeMatch<T, R> {

            private final T value;

            private Unmatched(T value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("unchecked")
            @Override
            public <U extends T> SafeMatch<T, R> when(Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = Function1.lift(f::apply).getType().parameterType(0);
                return Of.matches(value, paramType) ? new Matched<>(f.apply((U) value)) : this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U extends T> SafeMatch<T, R> when(U protoType, Function<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatch<T, U> flatMap(Function<? super R, ? extends SafeMatch<T, U>> mapper) {
                return (SafeMatch<T, U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatch<T, U> flatten(Function<? super R, ? extends SafeMatch<T, U>> f) {
                return (SafeMatch<T, U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> Unmatched<T, U> map(Function<? super R, ? extends U> mapper) {
                return (Unmatched<T, U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchBoolean<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedBoolean<R> when(Function<? super Boolean, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedBoolean<R> when(BooleanFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchBoolean<R> when(Boolean protoType, Function<? super Boolean, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchBoolean<R> when(boolean protoType, BooleanFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchBoolean<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchBoolean<U> flatMap(Function<? super R, ? extends SafeMatchBoolean<U>> mapper);

        <U> SafeMatchBoolean<U> flatten(Function<? super R, ? extends SafeMatchBoolean<U>> f);

        <U> SafeMatchBoolean<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchBoolean<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfBoolean {

            private final boolean value;

            private OfBoolean(boolean value) {
                this.value = value;
            }

            public <R> TypedBoolean<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedBoolean<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedBoolean<R> when(Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedBoolean<R> when(BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchBoolean<R> when(Boolean protoType, Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedBoolean<>(f.apply(value)) : new UnmatchedBoolean<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchBoolean<R> when(boolean protoType, BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedBoolean<>(f.apply(value)) : new UnmatchedBoolean<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedBoolean<R> {

            private final boolean value;

            private TypedBoolean(boolean value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedBoolean<R> when(Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedBoolean<R> when(BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchBoolean<R> when(Boolean protoType, Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedBoolean<>(f.apply(value)) : new UnmatchedBoolean<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchBoolean<R> when(boolean protoType, BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedBoolean<>(f.apply(value)) : new UnmatchedBoolean<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedBoolean<R> implements SafeMatchBoolean<R> {

            private final R result;

            private MatchedBoolean(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedBoolean<R> when(Function<? super Boolean, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedBoolean<R> when(BooleanFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchBoolean<R> when(Boolean protoType, Function<? super Boolean, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchBoolean<R> when(boolean protoType, BooleanFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchBoolean<U> flatMap(Function<? super R, ? extends SafeMatchBoolean<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchBoolean<U> flatten(Function<? super R, ? extends SafeMatchBoolean<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedBoolean<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedBoolean<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedBoolean<R> implements SafeMatchBoolean<R> {

            private final boolean value;

            private UnmatchedBoolean(boolean value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedBoolean<R> when(Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedBoolean<R> when(BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedBoolean<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchBoolean<R> when(Boolean protoType, Function<? super Boolean, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedBoolean<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchBoolean<R> when(boolean protoType, BooleanFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedBoolean<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchBoolean<U> flatMap(Function<? super R, ? extends SafeMatchBoolean<U>> mapper) {
                return (SafeMatchBoolean<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchBoolean<U> flatten(Function<? super R, ? extends SafeMatchBoolean<U>> f) {
                return (SafeMatchBoolean<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedBoolean<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedBoolean<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchByte<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedByte<R> when(Function<? super Byte, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedByte<R> when(ByteFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchByte<R> when(Byte protoType, Function<? super Byte, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchByte<R> when(byte protoType, ByteFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchByte<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchByte<U> flatMap(Function<? super R, ? extends SafeMatchByte<U>> mapper);

        <U> SafeMatchByte<U> flatten(Function<? super R, ? extends SafeMatchByte<U>> f);

        <U> SafeMatchByte<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchByte<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfByte {

            private final byte value;

            private OfByte(byte value) {
                this.value = value;
            }

            public <R> TypedByte<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedByte<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedByte<R> when(Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedByte<R> when(ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchByte<R> when(Byte protoType, Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedByte<>(f.apply(value)) : new UnmatchedByte<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchByte<R> when(byte protoType, ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedByte<>(f.apply(value)) : new UnmatchedByte<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedByte<R> {

            private final byte value;

            private TypedByte(byte value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedByte<R> when(Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedByte<R> when(ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchByte<R> when(Byte protoType, Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedByte<>(f.apply(value)) : new UnmatchedByte<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchByte<R> when(byte protoType, ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedByte<>(f.apply(value)) : new UnmatchedByte<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedByte<R> implements SafeMatchByte<R> {

            private final R result;

            private MatchedByte(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedByte<R> when(Function<? super Byte, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedByte<R> when(ByteFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchByte<R> when(Byte protoType, Function<? super Byte, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchByte<R> when(byte protoType, ByteFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchByte<U> flatMap(Function<? super R, ? extends SafeMatchByte<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchByte<U> flatten(Function<? super R, ? extends SafeMatchByte<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedByte<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedByte<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedByte<R> implements SafeMatchByte<R> {

            private final byte value;

            private UnmatchedByte(byte value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedByte<R> when(Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedByte<R> when(ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedByte<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchByte<R> when(Byte protoType, Function<? super Byte, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedByte<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchByte<R> when(byte protoType, ByteFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedByte<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchByte<U> flatMap(Function<? super R, ? extends SafeMatchByte<U>> mapper) {
                return (SafeMatchByte<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchByte<U> flatten(Function<? super R, ? extends SafeMatchByte<U>> f) {
                return (SafeMatchByte<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedByte<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedByte<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchChar<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedChar<R> when(Function<? super Character, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedChar<R> when(CharFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchChar<R> when(Character protoType, Function<? super Character, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchChar<R> when(char protoType, CharFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchChar<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchChar<U> flatMap(Function<? super R, ? extends SafeMatchChar<U>> mapper);

        <U> SafeMatchChar<U> flatten(Function<? super R, ? extends SafeMatchChar<U>> f);

        <U> SafeMatchChar<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchChar<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfChar {

            private final char value;

            private OfChar(char value) {
                this.value = value;
            }

            public <R> TypedChar<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedChar<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedChar<R> when(Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedChar<R> when(CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchChar<R> when(Character protoType, Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedChar<>(f.apply(value)) : new UnmatchedChar<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchChar<R> when(char protoType, CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedChar<>(f.apply(value)) : new UnmatchedChar<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedChar<R> {

            private final char value;

            private TypedChar(char value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedChar<R> when(Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedChar<R> when(CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchChar<R> when(Character protoType, Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedChar<>(f.apply(value)) : new UnmatchedChar<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchChar<R> when(char protoType, CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedChar<>(f.apply(value)) : new UnmatchedChar<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedChar<R> implements SafeMatchChar<R> {

            private final R result;

            private MatchedChar(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedChar<R> when(Function<? super Character, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedChar<R> when(CharFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchChar<R> when(Character protoType, Function<? super Character, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchChar<R> when(char protoType, CharFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchChar<U> flatMap(Function<? super R, ? extends SafeMatchChar<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchChar<U> flatten(Function<? super R, ? extends SafeMatchChar<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedChar<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedChar<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedChar<R> implements SafeMatchChar<R> {

            private final char value;

            private UnmatchedChar(char value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedChar<R> when(Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedChar<R> when(CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedChar<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchChar<R> when(Character protoType, Function<? super Character, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedChar<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchChar<R> when(char protoType, CharFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedChar<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchChar<U> flatMap(Function<? super R, ? extends SafeMatchChar<U>> mapper) {
                return (SafeMatchChar<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchChar<U> flatten(Function<? super R, ? extends SafeMatchChar<U>> f) {
                return (SafeMatchChar<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedChar<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedChar<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchDouble<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedDouble<R> when(Function<? super Double, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedDouble<R> when(DoubleFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchDouble<R> when(Double protoType, Function<? super Double, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchDouble<R> when(double protoType, DoubleFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchDouble<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchDouble<U> flatMap(Function<? super R, ? extends SafeMatchDouble<U>> mapper);

        <U> SafeMatchDouble<U> flatten(Function<? super R, ? extends SafeMatchDouble<U>> f);

        <U> SafeMatchDouble<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchDouble<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfDouble {

            private final double value;

            private OfDouble(double value) {
                this.value = value;
            }

            public <R> TypedDouble<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedDouble<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedDouble<R> when(Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedDouble<R> when(DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchDouble<R> when(Double protoType, Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedDouble<>(f.apply(value)) : new UnmatchedDouble<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchDouble<R> when(double protoType, DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedDouble<>(f.apply(value)) : new UnmatchedDouble<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedDouble<R> {

            private final double value;

            private TypedDouble(double value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedDouble<R> when(Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedDouble<R> when(DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchDouble<R> when(Double protoType, Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedDouble<>(f.apply(value)) : new UnmatchedDouble<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchDouble<R> when(double protoType, DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedDouble<>(f.apply(value)) : new UnmatchedDouble<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedDouble<R> implements SafeMatchDouble<R> {

            private final R result;

            private MatchedDouble(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedDouble<R> when(Function<? super Double, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedDouble<R> when(DoubleFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchDouble<R> when(Double protoType, Function<? super Double, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchDouble<R> when(double protoType, DoubleFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchDouble<U> flatMap(Function<? super R, ? extends SafeMatchDouble<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchDouble<U> flatten(Function<? super R, ? extends SafeMatchDouble<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedDouble<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedDouble<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedDouble<R> implements SafeMatchDouble<R> {

            private final double value;

            private UnmatchedDouble(double value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedDouble<R> when(Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedDouble<R> when(DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedDouble<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchDouble<R> when(Double protoType, Function<? super Double, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedDouble<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchDouble<R> when(double protoType, DoubleFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedDouble<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchDouble<U> flatMap(Function<? super R, ? extends SafeMatchDouble<U>> mapper) {
                return (SafeMatchDouble<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchDouble<U> flatten(Function<? super R, ? extends SafeMatchDouble<U>> f) {
                return (SafeMatchDouble<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedDouble<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedDouble<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchFloat<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedFloat<R> when(Function<? super Float, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedFloat<R> when(FloatFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchFloat<R> when(Float protoType, Function<? super Float, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchFloat<R> when(float protoType, FloatFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchFloat<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchFloat<U> flatMap(Function<? super R, ? extends SafeMatchFloat<U>> mapper);

        <U> SafeMatchFloat<U> flatten(Function<? super R, ? extends SafeMatchFloat<U>> f);

        <U> SafeMatchFloat<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchFloat<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfFloat {

            private final float value;

            private OfFloat(float value) {
                this.value = value;
            }

            public <R> TypedFloat<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedFloat<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedFloat<R> when(Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedFloat<R> when(FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchFloat<R> when(Float protoType, Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedFloat<>(f.apply(value)) : new UnmatchedFloat<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchFloat<R> when(float protoType, FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedFloat<>(f.apply(value)) : new UnmatchedFloat<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedFloat<R> {

            private final float value;

            private TypedFloat(float value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedFloat<R> when(Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedFloat<R> when(FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchFloat<R> when(Float protoType, Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedFloat<>(f.apply(value)) : new UnmatchedFloat<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchFloat<R> when(float protoType, FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedFloat<>(f.apply(value)) : new UnmatchedFloat<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedFloat<R> implements SafeMatchFloat<R> {

            private final R result;

            private MatchedFloat(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedFloat<R> when(Function<? super Float, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedFloat<R> when(FloatFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchFloat<R> when(Float protoType, Function<? super Float, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchFloat<R> when(float protoType, FloatFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchFloat<U> flatMap(Function<? super R, ? extends SafeMatchFloat<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchFloat<U> flatten(Function<? super R, ? extends SafeMatchFloat<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedFloat<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedFloat<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedFloat<R> implements SafeMatchFloat<R> {

            private final float value;

            private UnmatchedFloat(float value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedFloat<R> when(Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedFloat<R> when(FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedFloat<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchFloat<R> when(Float protoType, Function<? super Float, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedFloat<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchFloat<R> when(float protoType, FloatFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedFloat<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchFloat<U> flatMap(Function<? super R, ? extends SafeMatchFloat<U>> mapper) {
                return (SafeMatchFloat<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchFloat<U> flatten(Function<? super R, ? extends SafeMatchFloat<U>> f) {
                return (SafeMatchFloat<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedFloat<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedFloat<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchInt<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedInt<R> when(Function<? super Integer, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedInt<R> when(IntFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchInt<R> when(Integer protoType, Function<? super Integer, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchInt<R> when(int protoType, IntFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchInt<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchInt<U> flatMap(Function<? super R, ? extends SafeMatchInt<U>> mapper);

        <U> SafeMatchInt<U> flatten(Function<? super R, ? extends SafeMatchInt<U>> f);

        <U> SafeMatchInt<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchInt<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfInt {

            private final int value;

            private OfInt(int value) {
                this.value = value;
            }

            public <R> TypedInt<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedInt<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedInt<R> when(Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedInt<R> when(IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchInt<R> when(Integer protoType, Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedInt<>(f.apply(value)) : new UnmatchedInt<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchInt<R> when(int protoType, IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedInt<>(f.apply(value)) : new UnmatchedInt<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedInt<R> {

            private final int value;

            private TypedInt(int value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedInt<R> when(Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedInt<R> when(IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchInt<R> when(Integer protoType, Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedInt<>(f.apply(value)) : new UnmatchedInt<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchInt<R> when(int protoType, IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedInt<>(f.apply(value)) : new UnmatchedInt<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedInt<R> implements SafeMatchInt<R> {

            private final R result;

            private MatchedInt(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedInt<R> when(Function<? super Integer, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedInt<R> when(IntFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchInt<R> when(Integer protoType, Function<? super Integer, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchInt<R> when(int protoType, IntFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchInt<U> flatMap(Function<? super R, ? extends SafeMatchInt<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchInt<U> flatten(Function<? super R, ? extends SafeMatchInt<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedInt<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedInt<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedInt<R> implements SafeMatchInt<R> {

            private final int value;

            private UnmatchedInt(int value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedInt<R> when(Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedInt<R> when(IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedInt<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchInt<R> when(Integer protoType, Function<? super Integer, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedInt<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchInt<R> when(int protoType, IntFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedInt<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchInt<U> flatMap(Function<? super R, ? extends SafeMatchInt<U>> mapper) {
                return (SafeMatchInt<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchInt<U> flatten(Function<? super R, ? extends SafeMatchInt<U>> f) {
                return (SafeMatchInt<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedInt<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedInt<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchLong<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedLong<R> when(Function<? super Long, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedLong<R> when(LongFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchLong<R> when(Long protoType, Function<? super Long, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchLong<R> when(long protoType, LongFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchLong<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchLong<U> flatMap(Function<? super R, ? extends SafeMatchLong<U>> mapper);

        <U> SafeMatchLong<U> flatten(Function<? super R, ? extends SafeMatchLong<U>> f);

        <U> SafeMatchLong<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchLong<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfLong {

            private final long value;

            private OfLong(long value) {
                this.value = value;
            }

            public <R> TypedLong<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedLong<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedLong<R> when(Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedLong<R> when(LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchLong<R> when(Long protoType, Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedLong<>(f.apply(value)) : new UnmatchedLong<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchLong<R> when(long protoType, LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedLong<>(f.apply(value)) : new UnmatchedLong<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedLong<R> {

            private final long value;

            private TypedLong(long value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedLong<R> when(Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedLong<R> when(LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchLong<R> when(Long protoType, Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedLong<>(f.apply(value)) : new UnmatchedLong<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchLong<R> when(long protoType, LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedLong<>(f.apply(value)) : new UnmatchedLong<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedLong<R> implements SafeMatchLong<R> {

            private final R result;

            private MatchedLong(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedLong<R> when(Function<? super Long, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedLong<R> when(LongFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchLong<R> when(Long protoType, Function<? super Long, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchLong<R> when(long protoType, LongFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchLong<U> flatMap(Function<? super R, ? extends SafeMatchLong<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchLong<U> flatten(Function<? super R, ? extends SafeMatchLong<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedLong<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedLong<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedLong<R> implements SafeMatchLong<R> {

            private final long value;

            private UnmatchedLong(long value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedLong<R> when(Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedLong<R> when(LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedLong<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchLong<R> when(Long protoType, Function<? super Long, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedLong<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchLong<R> when(long protoType, LongFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedLong<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchLong<U> flatMap(Function<? super R, ? extends SafeMatchLong<U>> mapper) {
                return (SafeMatchLong<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchLong<U> flatten(Function<? super R, ? extends SafeMatchLong<U>> f) {
                return (SafeMatchLong<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedLong<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedLong<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    /**
     * @since 1.3.0
     */
    interface SafeMatchShort<R> extends HasGetters<R>, TraversableOnce<R> {

        // -- when cases

        @SuppressWarnings("overloads")
        MatchedShort<R> when(Function<? super Short, ? extends R> f);

        @SuppressWarnings("overloads")
        MatchedShort<R> when(ShortFunction<? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchShort<R> when(Short protoType, Function<? super Short, ? extends R> f);

        @SuppressWarnings("overloads")
        SafeMatchShort<R> when(short protoType, ShortFunction<? extends R> f);

        // -- filter monadic operations

        // TODO: @Override <U> SafeMatchShort<R> filter(Predicate<? super R> predicate);

        <U> SafeMatchShort<U> flatMap(Function<? super R, ? extends SafeMatchShort<U>> mapper);

        <U> SafeMatchShort<U> flatten(Function<? super R, ? extends SafeMatchShort<U>> f);

        <U> SafeMatchShort<U> map(Function<? super R, ? extends U> mapper);

        // TODO: SafeMatchShort<R> peek(Consumer<? super R> action);

        /**
         * @since 1.3.0
         */
        final class OfShort {

            private final short value;

            private OfShort(short value) {
                this.value = value;
            }

            public <R> TypedShort<R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new TypedShort<>(value);
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public <R> MatchedShort<R> when(Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> MatchedShort<R> when(ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchShort<R> when(Short protoType, Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedShort<>(f.apply(value)) : new UnmatchedShort<>(value);
            }

            @SuppressWarnings("overloads")
            public <R> SafeMatchShort<R> when(short protoType, ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedShort<>(f.apply(value)) : new UnmatchedShort<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class TypedShort<R> {

            private final short value;

            private TypedShort(short value) {
                this.value = value;
            }

            // -- when cases

            @SuppressWarnings("overloads")
            public  MatchedShort<R> when(Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  MatchedShort<R> when(ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")
            public  SafeMatchShort<R> when(Short protoType, Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedShort<>(f.apply(value)) : new UnmatchedShort<>(value);
            }

            @SuppressWarnings("overloads")
            public  SafeMatchShort<R> when(short protoType, ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedShort<>(f.apply(value)) : new UnmatchedShort<>(value);
            }
        }

        /**
         * @since 1.3.0
         */
        final class MatchedShort<R> implements SafeMatchShort<R> {

            private final R result;

            private MatchedShort(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public R get() {
                return result;
            }

            @Override
            public R orElse(R other) {
                return result;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            @Override
            public Option<R> toOption() {
                return new Some<>(result);
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            // -- when cases

            @Override
            public MatchedShort<R> when(Function<? super Short, ? extends R> f) {
                return this;
            }

            @Override
            public MatchedShort<R> when(ShortFunction<? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchShort<R> when(Short protoType, Function<? super Short, ? extends R> f) {
                return this;
            }

            @Override
            public SafeMatchShort<R> when(short protoType, ShortFunction<? extends R> f) {
                return this;
            }

            // -- filter monadic operations

            @Override
            public <U> SafeMatchShort<U> flatMap(Function<? super R, ? extends SafeMatchShort<U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatchShort<U> flatten(Function<? super R, ? extends SafeMatchShort<U>> f) {
                return f.apply(result);
            }

            @Override
            public <U> MatchedShort<U> map(Function<? super R, ? extends U> mapper) {
                return new MatchedShort<U>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        /**
         * @since 1.3.0
         */
        final class UnmatchedShort<R> implements SafeMatchShort<R> {

            private final short value;

            private UnmatchedShort(short value) {
                this.value = value;
            }

            // -- getters

            @Override
            public R get() {
                throw new MatchError(value);
            }

            @Override
            public R orElse(R other) {
                return other;
            }

            @Override
            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            @Override
            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            @Override
            public Option<R> toOption() {
                return None.instance();
            }

            @Override
            public Optional <R> toJavaOptional() {
                return Optional.empty();
            }

            // -- when cases

            @SuppressWarnings("overloads")@Override
            public  MatchedShort<R> when(Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  MatchedShort<R> when(ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return new MatchedShort<>(f.apply(value));
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchShort<R> when(Short protoType, Function<? super Short, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new MatchedShort<>(f.apply(value)) : this;
            }

            @SuppressWarnings("overloads")@Override
            public  SafeMatchShort<R> when(short protoType, ShortFunction<? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                return (value == protoType) ? new MatchedShort<>(f.apply(value)) : this;
            }

            // -- filter monadic operations

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchShort<U> flatMap(Function<? super R, ? extends SafeMatchShort<U>> mapper) {
                return (SafeMatchShort<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatchShort<U> flatten(Function<? super R, ? extends SafeMatchShort<U>> f) {
                return (SafeMatchShort<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> UnmatchedShort<U> map(Function<? super R, ? extends U> mapper) {
                return (UnmatchedShort<U>) this;
            }

            // -- traversable once

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }
    }

    interface HasGetters<R> {

        R get();

        R orElse(R other);

        R orElseGet(Supplier<? extends R> other);

        <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X;

        Option<R> toOption();

        Optional<R> toJavaOptional();
    }

    /**
     * A function {@code f: boolean -> R} that takes a primitive {@code boolean} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface BooleanFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A boolean value
         * @return A new value of type R
         */
        R apply(boolean value);
    }

    /**
     * A function {@code f: byte -> R} that takes a primitive {@code byte} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface ByteFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A byte value
         * @return A new value of type R
         */
        R apply(byte value);
    }

    /**
     * A function {@code f: char -> R} that takes a primitive {@code char} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface CharFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A char value
         * @return A new value of type R
         */
        R apply(char value);
    }

    /**
     * A function {@code f: double -> R} that takes a primitive {@code double} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface DoubleFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A double value
         * @return A new value of type R
         */
        R apply(double value);
    }

    /**
     * A function {@code f: float -> R} that takes a primitive {@code float} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface FloatFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A float value
         * @return A new value of type R
         */
        R apply(float value);
    }

    /**
     * A function {@code f: int -> R} that takes a primitive {@code int} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface IntFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A int value
         * @return A new value of type R
         */
        R apply(int value);
    }

    /**
     * A function {@code f: long -> R} that takes a primitive {@code long} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface LongFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A long value
         * @return A new value of type R
         */
        R apply(long value);
    }

    /**
     * A function {@code f: short -> R} that takes a primitive {@code short} value and returns a value of type {@code R}.
     *
     * @param <R> return type of this function
     * @since 1.0.0
     */
    @FunctionalInterface
    interface ShortFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value A short value
         * @return A new value of type R
         */
        R apply(short value);
    }
}