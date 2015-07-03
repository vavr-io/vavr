/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

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

    static <T> WhenUntyped<T> whenNull() {
        return new WhenUntyped<>(null);
    }

    /**
     * Creates a {@code Match.Case} by value.
     *
     * @param <T> type of the prototype value
     * @param prototype A specific value to be matched
     * @return a new {@code Case}
     * @throws NullPointerException if {@code function} is null
     */
    static <T> WhenUntyped<T> when(T prototype) {
        return new WhenUntyped<>(prototype);
    }

    @SuppressWarnings("unchecked")
    static <T> WhenInUntyped<T> whenIn(T... prototypes) {
        Objects.requireNonNull(prototypes, "prototypes is null");
        return new WhenInUntyped<>(prototypes);
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
     * The result of {@code Match.as(Class)}, which explicitly sets the {@code Match} result type.
     *
     * @param <R> the result type
     * @since 1.2.1
     */
    final class Typed<R> implements Expression.HasCases<R> {

        private Typed() {
        }

        @Override
        public <T> When<T, R> whenNull() {
            return new When<>(null, List.nil());
        }

        @Override
        public <T> When<T, R> when(T prototype) {
            return new When<>(prototype, List.nil());
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> WhenIn<T, R> whenIn(T... prototypes) {
            Objects.requireNonNull(prototypes, "prototypes is null");
            return new WhenIn<>(prototypes, List.nil());
        }

        @Override
        public Case<R> when(Function1<?, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }
    }

    interface WithThenUntyped<T> {

        <R> Case<R> then(Function1<? super T, ? extends R> function);

        default <R> Case<R> then(Supplier<? extends R> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return then(ignored -> supplier.get());
        }
    }

    interface WithThen<T, R> {

        Case<R> then(Function1<? super T, ? extends R> function);

        default Case<R> then(Supplier<? extends R> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            return then(ignored -> supplier.get());
        }
    }

    final class WhenUntyped<T> implements WithThenUntyped<T> {

        private final T prototype;

        private WhenUntyped(T prototype) {
            this.prototype = prototype;
        }

        @Override
        public <R> Case<R> then(Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(prototype, function);
        }
    }

    final class WhenInUntyped<T> implements WithThenUntyped<T> {

        private final T[] prototypes;

        private WhenInUntyped(T[] prototypes) {
            this.prototypes = prototypes;
        }

        @Override
        public <R> Case<R> then(Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            return new Case<>(List.of(prototypes).map(t -> Case.when(new Some<>(t), function)));
        }
    }

    final class When<T, R> implements WithThen<T, R> {

        private final T prototype;
        private final List<Function<Object, Option<R>>> cases;

        private When(T prototype, List<Function<Object, Option<R>>> cases) {
            this.prototype = prototype;
            this.cases = cases;
        }

        @Override
        public Case<R> then(Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = Case.when(new Some<>(prototype), function);
            return new Case<>(cases.prepend(when));
        }
    }

    final class WhenIn<T, R> implements WithThen<T, R> {

        private final T[] prototypes;
        private final List<Function<Object, Option<R>>> cases;

        private WhenIn(T[] prototypes, List<Function<Object, Option<R>>> cases) {
            this.prototypes = prototypes;
            this.cases = cases;
        }

        @Override
        public Case<R> then(Function1<? super T, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final List<Function<Object, Option<R>>> list = List.of(prototypes).map(t -> Case.when(new Some<>(t), function));
            return new Case<>(list.foldLeft(cases, List::prepend));
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

        @Override
        public R apply(Object o) {
            return match.get().apply(o);
        }

        @Override
        public <T> When<T, R> whenNull() {
            return new When<>(null, cases);
        }

        @Override
        public <T> When<T, R> when(T prototype) {
            return new When<>(prototype, cases);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> WhenIn<T, R> whenIn(T... prototypes) {
            Objects.requireNonNull(prototypes, "prototypes is null");
            return new WhenIn<>(prototypes, cases);
        }

        @Override
        public Case<R> when(Function1<?, ? extends R> function) {
            Objects.requireNonNull(function, "function is null");
            final Function<Object, Option<R>> when = when(None.instance(), function);
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

            <T> When<T, R> whenNull();

            /**
             * Creates a {@code Match.When} by value.
             *
             * @param <T> type of the prototype value
             * @param prototype A specific value to be matched
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            <T> When<T, R> when(T prototype);

            @SuppressWarnings("unchecked")
            <T> WhenIn<T, R> whenIn(T... prototypes);

            /**
             * Creates a {@code Match.Case} by type.
             *
             * @param function An {@code Object} to {@code R} function
             * @return a new {@code Case}
             * @throws NullPointerException if {@code function} is null
             */
            @SuppressWarnings("overloads")
            HasCases<R> when(Function1<?, ? extends R> function);
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

    interface HasGetters<R> {

        R get();

        R orElse(R other);

        R orElseGet(Supplier<? extends R> other);

        <X extends Throwable> R orElseThrow(Supplier<X> exceptionSupplier) throws X;

        Option<R> toOption();

        Optional<R> toJavaOptional();
    }
}