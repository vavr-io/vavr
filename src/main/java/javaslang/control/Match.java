/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import javaslang.Function2;
import javaslang.Lazy;
import javaslang.Value;
import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.collection.TraversableOnce;

import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * {@code Match} is a Java switch on steroids (without the negative 'side effects'). Some characteristics of
 * {@code Match} are:
 * <ul>
 * <li>it has a fluent API</li>
 * <li>it can be {@code Function}</li>
 * <li>it can be {@code Value}</li>
 * <li>it is able to match <em>values</em>, i.e. {@code when(Object)} and {@code whenIn(Object...)}</li>
 * <li>it is able to match <em>types</em>, i.e. {@code whenType(Class)} and {@code whenTypeIn(Class...)}</li>
 * <li>it is able to match <em>conditions</em>, i.e. {@code whenTrue(Function1)}</li>
 * <li>it is able to match <em>function applicability</em>, i.e. {@code whenApplicable(Function1)}</li>
 * <li>results may be specified <em>eagerly</em>, i.e. {@code then(value)}</li>
 * <li>results may be obtained <em>lazily</em>, i.e. {@code then(() -> value)}</li>
 * <li>results may be derived from the <em>context</em>, i.e. {@code then(object -> f(object)}</li>
 * </ul>
 * The Match API comes in two flavors, the {@code MatchMonad} and the {@code MatchFunction}.
 * <p>
 * {@code MatchMonad} is a {@linkplain javaslang.Value}, obtained by {@code Match.of(someValue)}. In this case a Match
 * is terminated {@code get()}, {@code orElse()}, etc.
 * <pre><code>Match.of(1)
 *      .whenType(String.class).then(s -&gt; "String " + s)
 *      .whenType(Number.class).then(n -&gt; "Number " + n)
 *      .whenType(Integer.class).then(i -&gt; "int " + i)
 *      .orElse("unknown");
 * </code></pre>
 * {@code MatchFunction} is a {@linkplain java.util.function.Function}, obtained by one of {@code Match.whenXxx(...)}.
 * In this case a Match is terminated by applying it to an object, e.g.
 * <pre><code>Match.when(...).then(...).otherwise(...).apply(o);</code></pre>
 * A {@code MatchFunction} is a reusable Match, i.e. it may be applied to different objects.
 * <p>
 * Example of a Match as <a href="http://en.wikipedia.org/wiki/Partial_function"><strong>partial</strong> function</a>:
 * <pre><code>final Match&lt;Number&gt; toNumber = Match.as(Number.class)
 *     .whenType(Integer.class).then(i -&gt; i)
 *     .whenType(String.class).then(s -&gt; new BigDecimal(s));
 * final Number number = toNumber.apply(1.0d); // throws a MatchError
 * </code></pre>
 * Example of a Match as <a href="http://en.wikipedia.org/wiki/Function_(mathematics)"><strong>total</strong> function</a>:
 * <pre><code>final Match&lt;Number&gt; toNumber = Match.as(Number.class)
 *     .whenType(Integer.class).then(i -&gt; i)
 *     .whenType(String.class).then(s -&gt; new BigDecimal(s));
 *     .otherwise(-1)
 *     .apply(1.0d); // result: -1
 * </code></pre>
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
     * @param <T>   type of the value to be matched
     * @param value the value to be matched
     * @return a new type-safe match builder
     */
    static <T> MatchMonad.Of<T> of(T value) {
        return new MatchMonad.Of<>(value);
    }

    /**
     * Specifies the type of the match expression. In many cases it is not necessary to call {@code as}.
     *
     * @param type the hint of type {@code R}
     * @param <R>  the type of the {@code Match} expression
     * @return a new match builder
     */
    static <R> MatchFunction.When.Then<R> as(Class<R> type) {
        Objects.requireNonNull(type, "type is null");
        return new MatchFunction.When.Then<>(List.empty());
    }

    static <T> MatchFunction.WhenUntyped<T> when(Function1<? super T, Boolean> predicate) {
        return new MatchFunction.WhenUntyped<>(MatchFunction.When.of(predicate));
    }

    static <T> MatchFunction.WhenUntyped<T> whenIs(T prototype) {
        return new MatchFunction.WhenUntyped<>(MatchFunction.When.is(prototype));
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    static <T> MatchFunction.WhenUntyped<T> whenIsIn(T... prototypes) {
        Objects.requireNonNull(prototypes, "prototypes is null");
        return new MatchFunction.WhenUntyped<>(MatchFunction.When.isIn(prototypes));
    }

    static <T> MatchFunction.WhenUntyped<T> whenType(Class<T> type) {
        Objects.requireNonNull(type, "type is null");
        return new MatchFunction.WhenUntyped<>(MatchFunction.When.type(type));
    }

    static <T, R> MatchFunction.WhenApplicable<T, R> whenApplicable(Function1<? super T, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return new MatchFunction.WhenApplicable<>(function, List.empty());
    }

    static <R> MatchFunction.Otherwise<R> otherwise(R that) {
        return new MatchFunction.Otherwise<>(ignored -> that, List.empty());
    }

    static <R> MatchFunction.Otherwise<R> otherwise(Function<? super Object, ? extends R> function) {
        Objects.requireNonNull(function, "function is null");
        return new MatchFunction.Otherwise<>(function, List.empty());
    }

    static <R> MatchFunction.Otherwise<R> otherwise(Supplier<? extends R> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new MatchFunction.Otherwise<>(ignored -> supplier.get(), List.empty());
    }

    static <R> MatchFunction.Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return new MatchFunction.Otherwise<>(ignored -> {
            throw supplier.get();
        }, List.empty());
    }

    /**
     * Match as Function
     */
    interface MatchFunction {

        /**
         * {@code WhenUntyped} is needed, when the return type of the MatchFunction is still unknown,
         * i.e. before the first call of {@code then()} or {@link Match#as(Class)}.
         *
         * @param <T> superset of the domain of the predicate
         */
        final class WhenUntyped<T> {

            private final Predicate<? super T> predicate;

            private WhenUntyped(Predicate<? super T> predicate) {
                this.predicate = predicate;
            }

            public <R> When.Then<R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                final Case<R> caze = new Case<>(predicate, function);
                return new When.Then<>(List.of(caze));
            }

            public <R> When.Then<R> then(R that) {
                return then(ignored -> that);
            }

            public <R> When.Then<R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public <R> When.Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }
        }

        final class When<T, R> {

            private final Predicate<? super T> predicate;
            private final List<Case<R>> cases;

            private When(Predicate<? super T> predicate, List<Case<R>> cases) {
                this.predicate = predicate;
                this.cases = cases;
            }

            public Then<R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Then<>(cases.prepend(new Case<>(predicate, function)));
            }

            public Then<R> then(R that) {
                return then(ignored -> that);
            }

            public Then<R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }

            private static <T> Predicate<? super T> of(Function1<? super T, Boolean> predicate) {
                final Class<?> type = predicate.getType().parameterType(0);
                return value -> (value == null || type.isAssignableFrom(value.getClass())) && predicate.apply(value);
            }

            private static <T> Predicate<? super T> is(T prototype) {
                return value -> value == prototype || (value != null && value.equals(prototype));
            }

            @SuppressWarnings({ "unchecked", "varargs" })
            @SafeVarargs
            private static <T> Predicate<? super T> isIn(T... prototypes) {
                return value -> Stream.of(prototypes).findFirst(prototype -> is(prototype).test(value)).isDefined();
            }

            private static <T> Predicate<? super T> type(Class<T> type) {
                return value -> value != null && type.isAssignableFrom(value.getClass());
            }

            public static final class Then<R> implements Match<R> {

                private final List<Case<R>> cases;

                private Then(List<Case<R>> cases) {
                    this.cases = cases;
                }

                @Override
                public R apply(Object o) {
                    return cases
                            .reverse()
                            .findFirst(caze -> caze.isApplicable(o))
                            .map(caze -> caze.apply(o))
                            .orElseThrow(() -> new MatchError(o));
                }

                public <T> When<T, R> when(Function1<? super T, Boolean> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    return new When<>(When.of(predicate), cases);
                }

                public <T> When<T, R> whenIs(T prototype) {
                    return new When<>(When.is(prototype), cases);
                }

                @SuppressWarnings({ "unchecked", "varargs" })
                public <T> When<T, R> whenIsIn(T... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    return new When<>(When.isIn(prototypes), cases);
                }

                public <T> When<T, R> whenType(Class<T> type) {
                    Objects.requireNonNull(type, "type is null");
                    return new When<>(When.type(type), cases);
                }

                public <T> WhenApplicable<T, R> whenApplicable(Function1<? super T, ? extends R> function) {
                    Objects.requireNonNull(function, "function is null");
                    return new WhenApplicable<>(function, cases);
                }

                public Otherwise<R> otherwise(R that) {
                    return new Otherwise<>(ignored -> that, cases);
                }

                public Otherwise<R> otherwise(Function<? super Object, ? extends R> function) {
                    Objects.requireNonNull(function, "function is null");
                    return new Otherwise<>(function, cases);
                }

                public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise<>(ignored -> supplier.get(), cases);
                }

                public Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise<>(ignored -> {
                        throw supplier.get();
                    }, cases);
                }
            }
        }

        final class Otherwise<R> implements Match<R> {

            private final Function<? super Object, ? extends R> function;
            private final List<Case<R>> cases;

            private Otherwise(Function<? super Object, ? extends R> function, List<Case<R>> cases) {
                this.function = function;
                this.cases = cases;
            }

            @Override
            public R apply(Object o) {
                return cases
                        .reverse()
                        .findFirst(caze -> caze.isApplicable(o))
                        .map(caze -> caze.apply(o))
                        .orElseGet(() -> function.apply(o));
            }
        }

        final class WhenApplicable<T, R> {

            private final Predicate<? super Object> predicate;
            private final Function1<? super T, ? extends R> function;
            private final List<Case<R>> cases;

            @SuppressWarnings("unchecked")
            private WhenApplicable(Function1<? super T, ? extends R> function, List<Case<R>> cases) {
                final Class<Object> type = (Class<Object>) function.getType().parameterType(0);
                this.predicate = When.type(type);
                this.function = function;
                this.cases = cases;
            }

            public When.Then<R> thenApply() {
                return new When.Then<>(cases.prepend(new Case<>(predicate, function)));
            }

            public When.Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new When.Then<>(cases.prepend(new Case<>(predicate, ignored -> {
                    throw supplier.get();
                })));
            }
        }

        final class Case<R> {

            private final Predicate<? super Object> predicate;
            private final Function<? super Object, ? extends R> function;

            @SuppressWarnings("unchecked")
            private <T> Case(Predicate<? super T> predicate, Function<? super T, ? extends R> function) {
                this.predicate = (Predicate<? super Object>) predicate;
                this.function = (Function<? super Object, ? extends R>) function;
            }

            private boolean isApplicable(Object object) {
                return predicate.test(object);
            }

            private R apply(Object object) {
                return function.apply(object);
            }
        }
    }

    /**
     * @since 2.0.0
     */
    interface MatchMonad<R> extends TraversableOnce<R>, Value<R> {

        interface MatchMonadWithWhen<T, R> extends MatchMonad<R>, WithWhen<T, R> {

            Otherwise<R> otherwise(R that);

            Otherwise<R> otherwise(Supplier<? extends R> supplier);

            Otherwise<R> otherwise(Function<? super T, ? extends R> function);
        }

        interface WithWhenUntyped<T> {

            <U> WhenUntyped<T, U> when(U prototype);

            @SuppressWarnings("unchecked")
            <U> WhenUntyped<T, U> whenIn(U... prototypes);

            <U> WhenUntyped<T, U> whenTrue(Function1<? super U, ? extends Boolean> predicate);

            <U> WhenUntyped<T, U> whenType(Class<U> type);

            @SuppressWarnings("unchecked")
            <U> WhenUntyped<T, U> whenTypeIn(Class<? extends U>... type);

            <U, R> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function);

            interface WhenUntyped<T, U> {

                <R> MatchMonadWithWhen<T, R> then(Function<? super U, ? extends R> function);

                default <R> MatchMonadWithWhen<T, R> then(R that) {
                    return then(ignored -> that);
                }

                default <R> MatchMonadWithWhen<T, R> then(Supplier<? extends R> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return then(ignored -> supplier.get());
                }
            }
        }

        interface WithWhen<T, R> {

            <U> When<T, U, R> when(U prototype);

            @SuppressWarnings("unchecked")
            <U> When<T, U, R> whenIn(U... prototypes);

            <U> When<T, U, R> whenTrue(Function1<? super U, ? extends Boolean> predicate);

            <U> When<T, U, R> whenType(Class<U> type);

            @SuppressWarnings("unchecked")
            <U> When<T, U, R> whenTypeIn(Class<? extends U>... type);

            <U> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function);

            interface When<T, U, R> {

                MatchMonadWithWhen<T, R> then(Function<? super U, ? extends R> function);

                default MatchMonadWithWhen<T, R> then(R that) {
                    return then(ignored -> that);
                }

                default MatchMonadWithWhen<T, R> then(Supplier<? extends R> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return then(ignored -> supplier.get());
                }

                final class WhenUnmatched<T, U, R> implements When<T, U, R> {

                    private final Object value;
                    private final boolean isMatching;

                    private WhenUnmatched(Object value, boolean isMatching) {
                        this.value = value;
                        this.isMatching = isMatching;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public MatchMonadWithWhen<T, R> then(Function<? super U, ? extends R> function) {
                        Objects.requireNonNull(function, "function is null");
                        return isMatching ? new Matched<>(function.apply((U) value)) : new Unmatched<>((T) value);
                    }
                }

                final class WhenMatched<T, U, R> implements When<T, U, R> {

                    private final Matched<T, R> matched;

                    private WhenMatched(Matched<T, R> matched) {
                        this.matched = matched;
                    }

                    @Override
                    public Matched<T, R> then(Function<? super U, ? extends R> function) {
                        Objects.requireNonNull(function, "function is null");
                        return matched;
                    }
                }

                final class WhenApplicableMatched<T, U, R> implements WhenApplicable<T, U, R> {

                    private final MatchMonad.Matched<T, R> matched;

                    private WhenApplicableMatched(MatchMonad.Matched<T, R> matched) {
                        this.matched = matched;
                    }

                    @Override
                    public MatchMonad.Matched<T, R> thenApply() {
                        return matched;
                    }
                }
            }
        }

        interface WhenApplicable<T, U, R> {

            MatchMonadWithWhen<T, R> thenApply();

            static <T, U, R> WhenApplicable<T, U, R> of(Function1<? super U, ? extends R> function, T value) {
                final boolean isMatching = function.isApplicableTo(value);
                if (isMatching) {
                    @SuppressWarnings("unchecked")
                    final R result = ((Function<T, R>) function).apply(value);
                    return new WithResult<>(result);
                } else {
                    return new WithValue<>(value);
                }
            }

            final class WithValue<T, U, R> implements WhenApplicable<T, U, R> {

                private final T value;

                private WithValue(T value) {
                    this.value = value;
                }

                @Override
                public MatchMonadWithWhen<T, R> thenApply() {
                    return new MatchMonad.Unmatched<>(value);
                }
            }

            final class WithResult<T, U, R> implements WhenApplicable<T, U, R> {

                private final R result;

                private WithResult(R result) {
                    this.result = result;
                }

                @Override
                public MatchMonadWithWhen<T, R> thenApply() {
                    return new MatchMonad.Matched<>(result);
                }
            }
        }

        // -- TODO: extract filter monadic operations to FilterMonadic interface

        // TODO: MatchMonad<R> filter(Predicate<? super R> predicate);

        <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper);

        <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f);

        <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper);

        // TODO: MatchMonad<R> peek(Consumer<? super R> action);

        final class Of<T> implements WithWhenUntyped<T> {

            private final T value;

            private Of(T value) {
                this.value = value;
            }

            public <R> Typed<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new Typed<>(value);
            }

            @Override
            public <U> WhenUnmatchedUntyped<T, U> when(U prototype) {
                final boolean isMatching = _internal.MATCH_BY_VALUE.apply(prototype, value);
                return new WhenUnmatchedUntyped<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> WhenUnmatchedUntyped<T, U> whenIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = List.of(prototypes).findFirst(p -> _internal.MATCH_BY_VALUE.apply(p, value)).isDefined();
                return new WhenUnmatchedUntyped<>(value, isMatching);
            }

            @Override
            public <U> WhenUnmatchedUntyped<T, U> whenTrue(Function1<? super U, ? extends Boolean> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = _internal.MATCH_BY_PREDICATE.apply(predicate, value);
                return new WhenUnmatchedUntyped<>(value, isMatching);
            }

            @Override
            public <U> WhenUnmatchedUntyped<T, U> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = _internal.MATCH_BY_TYPE.apply(type, value);
                return new WhenUnmatchedUntyped<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> WhenUnmatchedUntyped<T, U> whenTypeIn(Class<? extends U>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = List.of(types).findFirst(type -> _internal.MATCH_BY_TYPE.apply(type, value)).isDefined();
                return new WhenUnmatchedUntyped<>(value, isMatching);
            }

            @Override
            public <U, R> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return WhenApplicable.of(function, value);
            }
        }

        final class Typed<T, R> implements WithWhen<T, R> {

            private final T value;

            private Typed(T value) {
                this.value = value;
            }

            @Override
            public <U> When.WhenUnmatched<T, U, R> when(U prototype) {
                final boolean isMatching = _internal.MATCH_BY_VALUE.apply(prototype, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When.WhenUnmatched<T, U, R> whenIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = List.of(prototypes).findFirst(p -> _internal.MATCH_BY_VALUE.apply(p, value)).isDefined();
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> When.WhenUnmatched<T, U, R> whenTrue(Function1<? super U, ? extends Boolean> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = _internal.MATCH_BY_PREDICATE.apply(predicate, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> When.WhenUnmatched<T, U, R> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = _internal.MATCH_BY_TYPE.apply(type, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When.WhenUnmatched<T, U, R> whenTypeIn(Class<? extends U>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = List.of(types).findFirst(type -> _internal.MATCH_BY_TYPE.apply(type, value)).isDefined();
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return WhenApplicable.of(function, value);
            }
        }

        final class WhenUnmatchedUntyped<T, U> implements WithWhenUntyped.WhenUntyped<T, U> {

            private final Object value;
            private final boolean isMatching;

            private WhenUnmatchedUntyped(Object value, boolean isMatching) {
                this.value = value;
                this.isMatching = isMatching;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <R> MatchMonadWithWhen<T, R> then(Function<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return isMatching ? new Matched<>(function.apply((U) value)) : new Unmatched<>((T) value);
            }
        }

        final class Matched<T, R> implements MatchMonadWithWhen<T, R> {

            private final R result;
            private final Lazy<When.WhenMatched<T, ?, R>> when;
            private final Lazy<When.WhenApplicableMatched<T, ?, R>> whenApplicable;

            private Matched(R result) {
                this.result = result;
                this.when = Lazy.of(() -> new When.WhenMatched<>(this));
                this.whenApplicable = Lazy.of(() -> new When.WhenApplicableMatched<>(this));
            }

            // -- getters

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public R get() {
                return result;
            }

            // -- when cases

            @SuppressWarnings("unchecked")
            @Override
            public <U> When<T, U, R> when(U prototype) {
                return (When<T, U, R>) when.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When<T, U, R> whenIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                return (When<T, U, R>) when.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When<T, U, R> whenTrue(Function1<? super U, ? extends Boolean> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                return (When<T, U, R>) when.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When<T, U, R> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                return (When<T, U, R>) when.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When<T, U, R> whenTypeIn(Class<? extends U>... types) {
                Objects.requireNonNull(types, "types is null");
                return (When<T, U, R>) when.get();
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return (WhenApplicable<T, U, R>) whenApplicable.get();
            }

            @Override
            public Otherwise<R> otherwise(R that) {
                return new Otherwise<>(result);
            }

            @Override
            public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(result);
            }

            @Override
            public Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Otherwise<>(result);
            }

            // -- TODO: extract filter monadic operations to FilterMonadic interface

            @Override
            public <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return mapper.apply(result);
            }

            @Override
            public <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f) {
                Objects.requireNonNull(f, "f is null");
                return f.apply(result);
            }

            @Override
            public <U> Matched<T, U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return new Matched<>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public Iterator<R> iterator() {
                return Collections.singleton(result).iterator();
            }
        }

        final class Unmatched<T, R> implements MatchMonadWithWhen<T, R> {

            private final T value;

            private Unmatched(T value) {
                this.value = value;
            }

            // -- getters

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public R get() {
                throw new MatchError(value);
            }

            // -- when cases

            @Override
            public <U> When.WhenUnmatched<T, U, R> when(U prototype) {
                final boolean isMatching = _internal.MATCH_BY_VALUE.apply(prototype, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When.WhenUnmatched<T, U, R> whenIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = List.of(prototypes).findFirst(p -> _internal.MATCH_BY_VALUE.apply(p, value)).isDefined();
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> When.WhenUnmatched<T, U, R> whenTrue(Function1<? super U, ? extends Boolean> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = _internal.MATCH_BY_PREDICATE.apply(predicate, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> When.WhenUnmatched<T, U, R> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = _internal.MATCH_BY_TYPE.apply(type, value);
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> When.WhenUnmatched<T, U, R> whenTypeIn(Class<? extends U>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = List.of(types).findFirst(type -> _internal.MATCH_BY_TYPE.apply(type, value)).isDefined();
                return new When.WhenUnmatched<>(value, isMatching);
            }

            @Override
            public <U> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return WhenApplicable.of(function, value);
            }

            @Override
            public Otherwise<R> otherwise(R that) {
                return new Otherwise<>(that);
            }

            @Override
            public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(supplier.get());
            }

            @Override
            public Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Otherwise<>(function.apply(value));
            }

            // -- TODO: extract filter monadic operations to FilterMonadic interface

            @SuppressWarnings("unchecked")
            @Override
            public <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return (MatchMonad<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f) {
                Objects.requireNonNull(f, "f is null");
                return (MatchMonad<U>) this;
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> Unmatched<T, U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return (Unmatched<T, U>) this;
            }

            // -- traversable once

            @Override
            public Iterator<R> iterator() {
                return Collections.emptyIterator();
            }
        }

        final class Otherwise<R> implements MatchMonad<R> {

            private final R result;

            private Otherwise(R result) {
                this.result = result;
            }

            // -- getters

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public R get() {
                return result;
            }

            // -- TODO: extract filter monadic operations to FilterMonadic interface

            @Override
            public <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return mapper.apply(result);
            }

            @Override
            public <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f) {
                Objects.requireNonNull(f, "f is null");
                return f.apply(result);
            }

            @Override
            public <U> Otherwise<U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return new Otherwise<>(mapper.apply(result));
            }

            // -- traversable once

            @Override
            public Iterator<R> iterator() {
                return Collections.singleton(result).iterator();
            }
        }
    }

    /**
     * Wrapper for internal API that will be private in a future version.
     */
    interface _internal {

        Function2<Object, Object, Boolean> MATCH_BY_VALUE = (v, o) ->
                (v == o) || (v != null && v.equals(o));

        Function2<Class<?>, Object, Boolean> MATCH_BY_TYPE = (t, o) ->
                o != null && t.isAssignableFrom(o.getClass());

        @SuppressWarnings("unchecked")
        Function2<Function1<?, ? extends Boolean>, Object, Boolean> MATCH_BY_PREDICATE = (p, o) ->
                p.isApplicableTo(o) && ((Function1<Object, Boolean>) p).apply(o);
    }
}
