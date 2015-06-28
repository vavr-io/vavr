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
    interface SafeMatch<T, R> extends TraversableOnce<R> {

        <U extends T> SafeMatch<T, R> when(Function1<? super U, ? extends R> f);

        <U extends T> SafeMatch<T, R> when(U protoType, Function1<? super U, ? extends R> f);

        <U> SafeMatch<T, U> flatMap(Function<? super R, ? extends SafeMatch<T, U>> mapper);

        <U> SafeMatch<T, U> flatten(Function<? super R, ? extends SafeMatch<T, U>> f);

        SafeMatch<T, R> peek(Consumer<? super R> action);

        <U> SafeMatch<T, U> map(Function<? super R, ? extends U> mapper);

        final class Of<T> {

            private final T value;

            private Of(T value) {
                this.value = value;
            }

            public <R> Typed<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new Typed<>(value);
            }

            @SuppressWarnings("unchecked")
            public <U extends T, R> SafeMatch<T, R> when(Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = f.getType().parameterType(0);
                return matches(value, paramType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            @SuppressWarnings("unchecked")
            public <U extends T, R> SafeMatch<T, R> when(U protoType, Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(protoType, "protoType is null");
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            // method declared here because Java 8 does not support private interface methods
            private static boolean matches(Object obj, Class<?> type) {
                return obj != null && type.isAssignableFrom(obj.getClass());
            }
        }

        final class Typed<T, R> {

            private final T value;

            private Typed(T value) {
                this.value = value;
            }

            @SuppressWarnings("unchecked")
            public <U extends T> SafeMatch<T, R> when(Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = f.getType().parameterType(0);
                return Of.matches(value, paramType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }

            @SuppressWarnings("unchecked")
            public <U extends T> SafeMatch<T, R> when(U protoType, Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(protoType, "protoType is null");
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : new Unmatched<>(value);
            }
        }

        final class Matched<T, R> implements SafeMatch<T, R> {

            private final R result;

            private Matched(R result) {
                this.result = result;
            }

            @Override
            public <U extends T> Matched<T, R> when(Function1<? super U, ? extends R> f) {
                // fast forward / no argument checks
                return this;
            }

            @Override
            public <U extends T> SafeMatch<T, R> when(U protoType, Function1<? super U, ? extends R> f) {
                // fast forward / no argument checks
                return this;
            }

            public R get() {
                return result;
            }

            public R orElse(R other) {
                return result;
            }

            public R orElseGet(Supplier<? extends R> other) {
                return result;
            }

            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                return result;
            }

            public Option<R> toOption() {
                return new Some<>(result);
            }

            public Optional<R> toJavaOptional() {
                return Optional.ofNullable(result); // caution: may be empty if result is null
            }

            @Override
            public <U> SafeMatch<T, U> flatMap(Function<? super R, ? extends SafeMatch<T, U>> mapper) {
                return mapper.apply(result);
            }

            @Override
            public <U> SafeMatch<T, U> flatten(Function<? super R, ? extends SafeMatch<T, U>> f) {
                return f.apply(result);
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean exists(Predicate<? super R> predicate) {
                return predicate.test(result);
            }

            @Override
            public boolean forAll(Predicate<? super R> predicate) {
                return predicate.test(result);
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.singleton(result).iterator();
            }

            @Override
            public void forEach(Consumer<? super R> action) {
                action.accept(result);
            }

            @Override
            public SafeMatch<T, R> peek(Consumer<? super R> action) {
                action.accept(result);
                return this;
            }

            @Override
            public <U> SafeMatch<T, U> map(Function<? super R, ? extends U> mapper) {
                return new Matched<>(mapper.apply(result));
            }
        }

        final class Unmatched<T, R> implements SafeMatch<T, R> {

            private final T value;

            private Unmatched(T value) {
                this.value = value;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U extends T> SafeMatch<T, R> when(Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(f, "f is null");
                final Class<?> paramType = f.getType().parameterType(0);
                return Of.matches(value, paramType) ? new Matched<>(f.apply((U) value)) : this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U extends T> SafeMatch<T, R> when(U protoType, Function1<? super U, ? extends R> f) {
                Objects.requireNonNull(protoType, "protoType is null");
                Objects.requireNonNull(f, "f is null");
                return Objects.equals(value, protoType) ? new Matched<>(f.apply((U) value)) : this;
            }

            public R get() {
                throw new MatchError(value);
            }

            public R orElse(R other) {
                return other;
            }

            public R orElseGet(Supplier<? extends R> other) {
                return other.get();
            }

            public <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X {
                throw exceptionSupplier.get();
            }

            public Option<R> toOption() {
                return None.instance();
            }

            public Optional<R> toJavaOptional() {
                return Optional.empty();
            }

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

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public boolean exists(Predicate<? super R> predicate) {
                return false;
            }

            @Override
            public boolean forAll(Predicate<? super R> predicate) {
                return false;
            }

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }

            @Override
            public void forEach(Consumer<? super R> action) {
                // nothing to do
            }

            @Override
            public SafeMatch<T, R> peek(Consumer<? super R> action) {
                return this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> SafeMatch<T, U> map(Function<? super R, ? extends U> mapper) {
                return (SafeMatch<T, U>) this;
            }
        }
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