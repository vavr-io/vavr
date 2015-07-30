/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
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

    static <T> MatchFunction.WhenUntyped<T> when(Function1<? super T, ? extends Boolean> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
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

    // TODO: whenTypeIn

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

            // TODO: these private methods should move to the outer Monad interface with Java 9 because they are used by MatchFunction and MatchMonad
            @SuppressWarnings("unchecked")
            private static <T> Predicate<? super Object> of(Function1<? super T, ? extends Boolean> predicate) {
                final Class<?> type = predicate.getType().parameterType(0);
                return value -> (value == null || type.isAssignableFrom(value.getClass()))
                        && ((Function1<? super Object, ? extends Boolean>) predicate).apply(value);
            }

            private static <T> Predicate<? super Object> is(T prototype) {
                return value -> value == prototype || (value != null && value.equals(prototype));
            }

            @SuppressWarnings({ "unchecked", "varargs" })
            @SafeVarargs
            private static <T> Predicate<? super Object> isIn(T... prototypes) {
                return value -> Stream.of(prototypes).findFirst(prototype -> is(prototype).test(value)).isDefined();
            }

            private static <T> Predicate<? super Object> type(Class<T> type) {
                return value -> value != null && type.isAssignableFrom(value.getClass());
            }

            @SuppressWarnings({ "unchecked", "varargs" })
            @SafeVarargs
            private static <T> Predicate<? super Object> typeIn(Class<?>... types) {
                return value -> Stream.of(types).findFirst(type -> type(type).test(value)).isDefined();
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

                public <T> When<T, R> when(Function1<? super T, ? extends Boolean> predicate) {
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

                // TODO: whenTypeIn

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

        final class WhenApplicable<T, R> {

            private final Predicate<? super Object> predicate;
            private final Function1<? super T, ? extends R> function;
            private final List<Case<R>> cases;

            @SuppressWarnings("unchecked")
            private WhenApplicable(Function1<? super T, ? extends R> function, List<Case<R>> cases) {
                final Class<?> type = function.getType().parameterType(0);
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

        // -- TODO: extract filter monadic operations to FilterMonadic interface

        // TODO: MatchMonad<R> filter(Predicate<? super R> predicate);

        <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper);

        <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f);

        <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper);

        // TODO: MatchMonad<R> peek(Consumer<? super R> action);

        final class Of<T> {

            private final Object value;

            private Of(T value) {
                this.value = value;
            }

            public <R> When.Then<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new When.Then<>(value, None.instance());
            }

            public <U> WhenUntyped<U> when(Function1<? super U, ? extends Boolean> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = MatchFunction.When.of(predicate).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U> WhenUntyped<U> whenIs(U prototype) {
                final boolean isMatching = MatchFunction.When.is(prototype).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            @SuppressWarnings({ "unchecked", "varargs" })
            public <U> WhenUntyped<U> whenIsIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = MatchFunction.When.isIn(prototypes).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U> WhenUntyped<U> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = MatchFunction.When.type(type).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            // DEV-NOTE: return WhenUntyped<U = T> because lower bound of class types Class<? super U> cannot be calculated (this is the best we can do)
            public <U> WhenUntyped<T> whenTypeIn(Class<?>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = MatchFunction.When.typeIn(types).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <T, R> WhenApplicable<T, R> whenApplicable(Function1<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return null; // TODO
            }

            public <R> Otherwise<R> otherwise(R that) {
                return null; // TODO
            }

            public <R> Otherwise<R> otherwise(Function<? super Object, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return null; // TODO
            }

            public <R> Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return null; // TODO
            }

            public <R> Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return null; // TODO
            }
        }

        final class WhenUntyped<T> {

            private final Object value;
            private final boolean isMatching;

            private WhenUntyped(Object value, boolean isMatching) {
                this.value = value;
                this.isMatching = isMatching;
            }

            public <R> When.Then<T, R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return null; // TODO
            }

            public <R> When.Then<T, R> then(R that) {
                return then(ignored -> that);
            }

            public <R> When.Then<T, R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public <R> When.Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }
        }

        final class When<T, R> {

            private final Object value;
            private final Option<R> result;
            private final boolean isMatching;

            private When(Object value, Option<R> result, boolean isMatching) {
                this.value = value;
                this.result = result;
                this.isMatching = isMatching;
            }

            public Then<T, R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Then<>(value, computeResult(function));
            }

            public Then<T, R> then(R that) {
                return then(ignored -> that);
            }

            public Then<T, R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }

            @SuppressWarnings("unchecked")
            private Option<R> computeResult(Function<? super T, ? extends R> function) {
                if (result.isEmpty() && isMatching) {
                    final Function<? super Object, ? extends R> f = (Function<? super Object, ? extends R>) function;
                    return Option.of(f.apply(value));
                } else {
                    return result;
                }
            }

            public static final class Then<T, R> implements MatchMonad<R> {

                private final Object value;
                private final Option<R> result;

                private Then(Object value, Option<R> result) {
                    this.value = value;
                    this.result = result;
                }

                public <U> When<U, R> when(Function1<? super U, ? extends Boolean> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    final boolean isMatching = isMatching(() -> MatchFunction.When.of(predicate));
                    return new When<>(value, result, isMatching);
                }

                public <U> When<U, R> whenIs(U prototype) {
                    final boolean isMatching = isMatching(() -> MatchFunction.When.is(prototype));
                    return new When<>(value, result, isMatching);
                }

                @SuppressWarnings({ "unchecked", "varargs" })
                public <U> When<U, R> whenIsIn(U... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    final boolean isMatching = isMatching(() -> MatchFunction.When.isIn(prototypes));
                    return new When<>(value, result, isMatching);
                }

                public <U> When<U, R> whenType(Class<U> type) {
                    Objects.requireNonNull(type, "type is null");
                    final boolean isMatching = isMatching(() -> MatchFunction.When.type(type));
                    return new When<>(value, result, isMatching);
                }

                @SuppressWarnings({ "unchecked", "varargs" })
                public When<T, R> whenTypeIn(Class<?>... types) {
                    Objects.requireNonNull(types, "types is null");
                    final boolean isMatching = isMatching(() -> MatchFunction.When.typeIn(types));
                    return new When<>(value, result, isMatching);
                }

                public <U> WhenApplicable<U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                    Objects.requireNonNull(function, "function is null");
                    return null; // TODO
                }

                public Otherwise<R> otherwise(R that) {
                    return null; // TODO
                }

                public Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                    Objects.requireNonNull(function, "function is null");
                    return null; // TODO
                }

                public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return null; // TODO
                }

                public Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return null; // TODO
                }

                @SuppressWarnings("unchecked")
                @Override
                public <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper) {
                    Objects.requireNonNull(mapper, "mapper is null");
                    return result
                            .map((Function<? super R, MatchMonad<U>>) mapper::apply)
                            .orElseGet(() -> (MatchMonad<U>) this);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f) {
                    Objects.requireNonNull(f, "f is null");
                    return result
                            .map((Function<? super R, MatchMonad<U>>) f::apply)
                            .orElseGet(() -> (MatchMonad<U>) this);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper) {
                    Objects.requireNonNull(mapper, "mapper is null");
                    return result
                            .map(r -> new Then<T, U>(value, new Some<>((U) mapper.apply(r))))
                            .orElseGet(() -> (Then<T, U>) this);
                }

                @Override
                public R get() {
                    return result.get();
                }

                @Override
                public boolean isEmpty() {
                    return result.isEmpty();
                }

                @Override
                public Iterator<R> iterator() {
                    return isEmpty() ? Collections.emptyIterator() : Collections.singleton(get()).iterator();
                }

                private boolean isMatching(Supplier<Predicate<? super Object>> predicate) {
                    return result.isEmpty() && predicate.get().test(value);
                }
            }
        }

        final class WhenApplicable<T, R> {

            public When.Then<T, R> thenApply() {
                return null; // TODO
            }

            public When.Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return null; // TODO
            }
        }

        final class Otherwise<R> implements MatchMonad<R> {

            // TODO

            @Override
            public <U> MatchMonad<U> flatMap(Function<? super R, ? extends MatchMonad<U>> mapper) {
                return null;
            }

            @Override
            public <U> MatchMonad<U> flatten(Function<? super R, ? extends MatchMonad<U>> f) {
                return null;
            }

            @Override
            public <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper) {
                return null;
            }

            @Override
            public R get() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Iterator<R> iterator() {
                return null;
            }
        }
    }
}
