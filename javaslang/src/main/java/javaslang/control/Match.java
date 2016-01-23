/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.*;
import javaslang.collection.Iterator;
import javaslang.collection.List;
import javaslang.control.Match.SerializablePredicate;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static javaslang.control.MatchModule.*;

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
 * The Match API comes in two flavors, the {@code MatchValue} and the {@code MatchFunction}.
 * <p>
 * {@code MatchValue} is a {@linkplain javaslang.Value}, obtained by {@code Match.of(someValue)}. In this case a Match
 * is terminated {@code get()}, {@code getOrElse()}, etc.
 * <pre><code>Match.of(1)
 *      .whenType(String.class).then(s -&gt; "String " + s)
 *      .whenType(Number.class).then(n -&gt; "Number " + n)
 *      .whenType(Integer.class).then(i -&gt; "int " + i)
 *      .getOrElse("unknown");
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
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Match {

    /**
     * Creates a type-safe match by fixating the value to be matched.
     *
     * @param <T>   type of the value to be matched
     * @param value the value to be matched
     * @return a new type-safe match builder
     */
    static <T> MatchValue.Of<T> of(T value) {
        return new MatchValue.Of<>(value);
    }

    /**
     * Specifies the type of the match expression. In many cases it is not necessary to call {@code as}.
     *
     * @param type the hint of type {@code R}
     * @param <R>  the type of the {@code Match} expression
     * @return a new match builder
     */
    static <R> MatchFunction.Then<R> as(Class<R> type) {
        Objects.requireNonNull(type, "type is null");
        return new MatchFunction.Then<>(List.empty());
    }

    static <T> MatchFunction.WhenUntyped<T> when(SerializablePredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return new MatchFunction.WhenUntyped<>(isTrue(predicate));
    }

    static <T> MatchFunction.WhenUntyped<T> whenIs(T prototype) {
        return new MatchFunction.WhenUntyped<>(is(prototype));
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> MatchFunction.WhenUntyped<T> whenIsIn(T... prototypes) {
        Objects.requireNonNull(prototypes, "prototypes is null");
        return new MatchFunction.WhenUntyped<>(isIn(prototypes));
    }

    static <T> MatchFunction.WhenUntyped<T> whenType(Class<T> type) {
        Objects.requireNonNull(type, "type is null");
        return new MatchFunction.WhenUntyped<>(isType(type));
    }

    // DEV-NOTE: setting T = Object is the best we can to because intersection of class types Class<? super U> cannot be calculated
    static MatchFunction.WhenUntyped<Object> whenTypeIn(Class<?>... types) {
        Objects.requireNonNull(types, "types is null");
        return new MatchFunction.WhenUntyped<>(isTypeIn(types));
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

    static MatchFunction.Effect.Otherwise otherwiseRun(Consumer<? super Object> action) {
        Objects.requireNonNull(action, "action is null");
        return new MatchFunction.Effect.Otherwise(value -> {
            action.accept(value);
            return null;
        }, List.empty());
    }

    static MatchFunction.Effect.Otherwise otherwiseRun(Runnable action) {
        Objects.requireNonNull(action, "action is null");
        return new MatchFunction.Effect.Otherwise(value -> {
            action.run();
            return null;
        }, List.empty());
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
    interface MatchFunction<R> extends Function1<Object, R> {

        long serialVersionUID = 1L;

        /**
         * Applies this {@code Match} to an {@code Object}.
         *
         * @param o an {@code Object}
         * @throws MatchError if no {@code Case} matched
         */
        @Override
        R apply(Object o);

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

            public <R> Then<R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Then<>(List.of(new Case(predicate, function)));
            }

            public <R> Then<R> then(R that) {
                return then(ignored -> that);
            }

            public <R> Then<R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public Effect.Then thenRun(Consumer<? super T> action) {
                Objects.requireNonNull(action, "action is null");
                final Function<? super T, ? extends Void> function = value -> {
                    action.accept(value);
                    return null;
                };
                return new Effect.Then(List.of(new Case(predicate, function)));
            }

            public Effect.Then thenRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                return new Effect.Then(List.of(new Case(predicate, value -> {
                    action.run();
                    return null;
                })));
            }

            public <R> Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }
        }

        final class When<T, R> {

            private final Predicate<? super T> predicate;
            private final List<Case> cases;

            private When(Predicate<? super T> predicate, List<Case> cases) {
                this.predicate = predicate;
                this.cases = cases;
            }

            public Then<R> then(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Then<>(cases.prepend(new Case(predicate, function)));
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
        }

        final class Then<R> implements MatchFunction<R> {

            private final static long serialVersionUID = 1L;

            private final List<Case> cases;

            private Then(List<Case> cases) {
                this.cases = cases;
            }

            @SuppressWarnings("unchecked")
            @Override
            public R apply(Object o) {
                return (R) cases.reverse()
                        .find(caze -> caze.isApplicable(o))
                        .map(caze -> caze.apply(o))
                        .getOrElseThrow(() -> new MatchError(o));
            }

            public <T> When<T, R> when(SerializablePredicate<? super T> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                return new When<>(isTrue(predicate), cases);
            }

            public <T> When<T, R> whenIs(T prototype) {
                return new When<>(is(prototype), cases);
            }

            @SuppressWarnings("unchecked")
            public <T> When<T, R> whenIsIn(T... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                return new When<>(isIn(prototypes), cases);
            }

            public <T> When<T, R> whenType(Class<T> type) {
                Objects.requireNonNull(type, "type is null");
                return new When<>(isType(type), cases);
            }

            // DEV-NOTE: setting T = Object is the best we can to because intersection of class types Class<? super U> cannot be calculated
            public When<Object, R> whenTypeIn(Class<?>... types) {
                Objects.requireNonNull(types, "types is null");
                return new When<>(isTypeIn(types), cases);
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

        final class WhenApplicable<T, R> {

            private final Function1<? super T, ? extends R> function;
            private final List<Case> cases;

            private WhenApplicable(Function1<? super T, ? extends R> function, List<Case> cases) {
                this.function = function;
                this.cases = cases;
            }

            public Then<R> thenApply() {
                return new Then<>(cases.prepend(new Case(function::isApplicableTo, function)));
            }

            public Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Then<>(cases.prepend(new Case(function::isApplicableTo, ignored -> {
                    throw supplier.get();
                })));
            }
        }

        final class Otherwise<R> implements MatchFunction<R> {

            private final static long serialVersionUID = 1L;

            private final Function<? super Object, ? extends R> function;
            private final List<Case> cases;

            private Otherwise(Function<? super Object, ? extends R> function, List<Case> cases) {
                this.function = function;
                this.cases = cases;
            }

            @SuppressWarnings("unchecked")
            @Override
            public R apply(Object o) {
                return (R) cases.reverse()
                        .find(caze -> caze.isApplicable(o))
                        .map(caze -> caze.apply(o))
                        .getOrElse(() -> function.apply(o));
            }
        }

        final class Case {

            private final Predicate<? super Object> predicate;
            private final Function<? super Object, ? extends Object> function;

            @SuppressWarnings("unchecked")
            private <T, R> Case(Predicate<? super T> predicate, Function<? super T, ? extends R> function) {
                this.predicate = (Predicate<? super Object>) predicate;
                this.function = (Function<? super Object, ? extends Object>) function;
            }

            private boolean isApplicable(Object object) {
                return predicate.test(object);
            }

            private Object apply(Object object) {
                return function.apply(object);
            }
        }

        interface Effect {

            final class When<T> {

                private final Predicate<? super T> predicate;
                private final List<Case> cases;

                private When(Predicate<? super T> predicate, List<Case> cases) {
                    this.predicate = predicate;
                    this.cases = cases;
                }

                public Then thenRun(Consumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    final Function<? super T, ? extends Void> function = value -> {
                        action.accept(value);
                        return null;
                    };
                    return new Then(cases.prepend(new Case(predicate, function)));
                }

                public Then thenRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return new Then(cases.prepend(new Case(predicate, ignored -> {
                        action.run();
                        return null;
                    })));
                }

                public Then thenThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Then(cases.prepend(new Case(predicate, ignored -> {
                        throw supplier.get();
                    })));
                }
            }

            final class Then implements Consumer<Object> {

                private final List<Case> cases;

                private Then(List<Case> cases) {
                    this.cases = cases;
                }

                @Override
                public void accept(Object o) {
                    cases.reverse()
                            .find(caze -> caze.isApplicable(o))
                            .map(caze -> caze.apply(o));
                }

                public <T> When<T> when(SerializablePredicate<? super T> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    return new When<>(isTrue(predicate), cases);
                }

                public <T> When<T> whenIs(T prototype) {
                    return new When<>(is(prototype), cases);
                }

                @SuppressWarnings("unchecked")
                public <T> When<T> whenIsIn(T... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    return new When<>(isIn(prototypes), cases);
                }

                public <T> When<T> whenType(Class<T> type) {
                    Objects.requireNonNull(type, "type is null");
                    return new When<>(isType(type), cases);
                }

                // DEV-NOTE: setting T = Object is the best we can to because intersection of class types Class<? super U> cannot be calculated
                public When<Object> whenTypeIn(Class<?>... types) {
                    Objects.requireNonNull(types, "types is null");
                    return new When<>(isTypeIn(types), cases);
                }

                public <T> WhenApplicable<T> whenApplicable(SerializableConsumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new WhenApplicable<>(action, cases);
                }

                public Otherwise otherwiseRun(Consumer<? super Object> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new Otherwise(value -> {
                        action.accept(value);
                        return null;
                    }, cases);
                }

                public Otherwise otherwiseRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return new Otherwise(ignored -> {
                        action.run();
                        return null;
                    }, cases);
                }

                public Otherwise otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise(ignored -> {
                        throw supplier.get();
                    }, cases);
                }
            }

            final class WhenApplicable<T> {

                private final SerializableConsumer<? super T> action;
                private final List<Case> cases;

                private WhenApplicable(SerializableConsumer<? super T> action, List<Case> cases) {
                    this.action = action;
                    this.cases = cases;
                }

                @SuppressWarnings("unchecked")
                public Then thenRun() {
                    return new Then(cases.prepend(new Case(action::isApplicableTo, o -> {
                        action.accept((T) o);
                        return null;
                    })));
                }

                public Then thenThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Then(cases.prepend(new Case(action::isApplicableTo, ignored -> {
                        throw supplier.get();
                    })));
                }
            }

            final class Otherwise implements Consumer<Object> {

                private final Function<? super Object, ? extends Void> action;
                private final List<Case> cases;

                private Otherwise(Function<? super Object, ? extends Void> action, List<Case> cases) {
                    this.action = action;
                    this.cases = cases;
                }

                @Override
                public void accept(Object o) {
                    cases.reverse()
                            .find(caze -> caze.isApplicable(o))
                            .map(caze -> caze.apply(o))
                            .getOrElse(() -> action.apply(o));
                }
            }
        }
    }

    // DEV-NOTE: No flatMap and orElse because this more like a Functor than a Monad.
    //           It represents a value rather than capturing a specific state.
    interface MatchValue<R> extends Value<R>, Supplier<R> {

        MatchValue<R> filter(Predicate<? super R> predicate);

        /**
         * A {@code MatchValue} is single-valued.
         *
         * @return {@code true}
         */
        @Override
        default boolean isSingleValued() {
            return true;
        }

        @Override
        <U> MatchValue<U> map(Function<? super R, ? extends U> mapper);

        @Override
        default MatchValue.Of<MatchValue<R>> match() {
            return Match.of(this);
        }

        @Override
        MatchValue<R> peek(Consumer<? super R> action);

        @Override
        default String stringPrefix() {
            return "Match";
        }

        /**
         * Transforms this {@code MatchValue}.
         *
         * @param f   A transformation
         * @param <U> Type of transformation result
         * @return An instance of type {@code U}
         * @throws NullPointerException if {@code f} is null
         */
        default <U> U transform(Function<? super MatchValue<? super R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        final class Of<T> {

            private final T value;

            private Of(T value) {
                this.value = value;
            }

            public <R> Then<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new Then<>(value, Option.none());
            }

            public <U> WhenUntyped<T, U> when(SerializablePredicate<? super U> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = isTrue(predicate).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U> WhenUntyped<T, U> whenIs(U prototype) {
                final boolean isMatching = is(prototype).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            @SuppressWarnings("unchecked")
            public <U> WhenUntyped<T, U> whenIsIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = isIn(prototypes).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U> WhenUntyped<T, U> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = isType(type).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            // DEV-NOTE: setting <U = T> is the best we can do because intersection of class types Class<?> cannot be calculated
            public WhenUntyped<T, T> whenTypeIn(Class<?>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = isTypeIn(types).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U, R> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new WhenApplicable<>(value, Option.none(), function);
            }

            public <R> Otherwise<R> otherwise(R that) {
                return new Otherwise<>(Option.some(that));
            }

            public <R> Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Otherwise<>(Option.some(function.apply(value)));
            }

            public <R> Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(Option.some(supplier.get()));
            }

            public void otherwiseRun(Consumer<? super T> action) {
                Objects.requireNonNull(action, "action is null");
                action.accept(value);
            }

            public void otherwiseRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                action.run();
            }

            public <R> Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                throw supplier.get();
            }
        }

        final class WhenUntyped<T, U> {

            private final T value;
            private final boolean isMatching;

            private WhenUntyped(T value, boolean isMatching) {
                this.value = value;
                this.isMatching = isMatching;
            }

            public <R> Then<T, R> then(Function<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                final Option<R> result = MatchModule.computeResult(value, Option.none(), isMatching, function);
                return new Then<>(value, result);
            }

            public <R> Then<T, R> then(R that) {
                return then(ignored -> that);
            }

            public <R> Then<T, R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            @SuppressWarnings("unchecked")
            public Effect.Then<T> thenRun(Consumer<? super U> action) {
                Objects.requireNonNull(action, "action is null");
                if (isMatching) {
                    action.accept((U) value);
                }
                return new Effect.Then<>(value, isMatching);
            }

            public Effect.Then<T> thenRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                return thenRun(ignored -> action.run());
            }

            public <R> Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }
        }

        final class When<T, U, R> {

            private final T value;
            private final Option<R> result;
            private final boolean isMatching;

            private When(T value, Option<R> result, boolean isMatching) {
                this.value = value;
                this.result = result;
                this.isMatching = isMatching;
            }

            public Then<T, R> then(Function<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                final Option<R> updatedResult = MatchModule.computeResult(value, result, isMatching, function);
                return new Then<>(value, updatedResult);
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
        }

        final class Then<T, R> implements MatchValue<R> {

            private final T value;
            private final Option<R> result;

            private Then(T value, Option<R> result) {
                this.value = value;
                this.result = result;
            }

            public <U> When<T, U, R> when(SerializablePredicate<? super U> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                final boolean isMatching = isMatching(() -> isTrue(predicate));
                return new When<>(value, result, isMatching);
            }

            public <U> When<T, U, R> whenIs(U prototype) {
                final boolean isMatching = isMatching(() -> is(prototype));
                return new When<>(value, result, isMatching);
            }

            @SuppressWarnings("unchecked")
            public <U> When<T, U, R> whenIsIn(U... prototypes) {
                Objects.requireNonNull(prototypes, "prototypes is null");
                final boolean isMatching = isMatching(() -> isIn(prototypes));
                return new When<>(value, result, isMatching);
            }

            public <U> When<T, U, R> whenType(Class<U> type) {
                Objects.requireNonNull(type, "type is null");
                final boolean isMatching = isMatching(() -> isType(type));
                return new When<>(value, result, isMatching);
            }

            // DEV-NOTE: setting <U = T> is the best we can do because intersection of class types Class<?> cannot be calculated
            public When<T, T, R> whenTypeIn(Class<?>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = isMatching(() -> isTypeIn(types));
                return new When<>(value, result, isMatching);
            }

            public <U> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new WhenApplicable<>(value, result, function);
            }

            public Otherwise<R> otherwise(R that) {
                return new Otherwise<>(result.orElse(() -> Option.some(that)));
            }

            public Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Otherwise<>(result.orElse(() -> Option.some(function.apply(value))));
            }

            public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(result.orElse(() -> Option.some(supplier.get())));
            }

            public Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                if (result.isEmpty()) {
                    throw supplier.get();
                } else {
                    return new Otherwise<>(result);
                }
            }

            @Override
            public Then<T, R> filter(Predicate<? super R> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                return new Then<>(value, result.filter(predicate));
            }

            @Override
            public <U> Then<T, U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return new Then<>(value, result.map(mapper));
            }

            @Override
            public Then<T, R> peek(Consumer<? super R> action) {
                Objects.requireNonNull(action, "action is null");
                result.peek(action);
                return this;
            }

            /**
             * Returns the match result.
             * <p>
             * <strong>Note:</strong> This implementation differs from the contract of {@link Value#get()} in the manner
             * that it throws a {@code MatchError} instead of a {@code NoSuchElementException}.
             *
             * @return the match result
             * @throws MatchError if the value don't matched
             */
            @Override
            public R get() {
                return result.getOrElseThrow(() -> new MatchError(value));
            }

            @Override
            public Option<R> getOption() {
                return result;
            }

            @Override
            public boolean isEmpty() {
                return result.isEmpty();
            }

            @Override
            public Iterator<R> iterator() {
                return result.isEmpty() ? Iterator.empty() : Iterator.of(get());
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                } else if (o instanceof MatchValue) {
                    final MatchValue<?> that = (MatchValue<?>) o;
                    return Objects.equals(result, that.getOption());
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return result.hashCode();
            }

            @Override
            public String toString() {
                return stringPrefix() + "(" + result.map(String::valueOf).getOrElse("") + ")";
            }

            private boolean isMatching(Supplier<Predicate<? super Object>> predicate) {
                return result.isEmpty() && predicate.get().test(value);
            }
        }

        final class WhenApplicable<T, U, R> {

            private final T value;
            private final Option<R> result;
            private final boolean isMatching;
            private final Function1<? super U, ? extends R> function;

            private WhenApplicable(T value, Option<R> result, Function1<? super U, ? extends R> function) {
                this.value = value;
                this.result = result;
                this.isMatching = result.isEmpty() && function.isApplicableTo(value);
                this.function = function;
            }

            public Then<T, R> thenApply() {
                final Option<R> updatedResult = MatchModule.computeResult(value, result, isMatching, function);
                return new Then<>(value, updatedResult);
            }

            public Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                final Option<R> updatedResult = MatchModule.computeResult(value, result, isMatching, ignored -> {
                    throw supplier.get();
                });
                return new Then<>(value, updatedResult);
            }
        }

        final class Otherwise<R> implements MatchValue<R> {

            private final Option<R> result;

            private Otherwise(Option<R> result) {
                this.result = result;
            }

            @Override
            public Otherwise<R> filter(Predicate<? super R> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                return new Otherwise<>(result.filter(predicate));
            }

            @SuppressWarnings("unchecked")
            @Override
            public <U> Otherwise<U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                if (result.isEmpty()) {
                    return (Otherwise<U>) this;
                } else {
                    return new Otherwise<>(result.map(mapper));
                }
            }

            @Override
            public Otherwise<R> peek(Consumer<? super R> action) {
                Objects.requireNonNull(action, "action is null");
                result.peek(action);
                return this;
            }

            /**
             * Returns the match result.
             * <p>
             * <strong>Note:</strong> The {@code otherwise} branch of Match should always return a result.
             * Here the result may only be empty when it was filtered with {@link #filter(Predicate)}.
             * Therefore it is no {@code MatchError} if the result is empty and we throw a
             * {@code NoSuchElementException} in such a case.
             *
             * @return the match result
             * @throws NoSuchElementException if there is no result
             */
            @Override
            public R get() {
                return result.get();
            }

            @Override
            public Option<R> getOption() {
                return result;
            }

            @Override
            public boolean isEmpty() {
                return result.isEmpty();
            }

            @Override
            public Iterator<R> iterator() {
                return result.isEmpty() ? Iterator.empty() : Iterator.of(get());
            }

            @Override
            public boolean equals(Object o) {
                if (o == this) {
                    return true;
                } else if (o instanceof MatchValue) {
                    final MatchValue<?> that = (MatchValue<?>) o;
                    return Objects.equals(result, that.getOption());
                } else {
                    return false;
                }
            }

            @Override
            public int hashCode() {
                return result.hashCode();
            }

            @Override
            public String toString() {
                return stringPrefix() + "(" + result.map(String::valueOf).getOrElse("") + ")";
            }
        }

        interface Effect {

            final class When<T, U> {

                private final T value;
                private final boolean isActionPerformed;
                private final boolean isMatching;

                private When(T value, boolean isActionPerformed, boolean isMatching) {
                    this.value = value;
                    this.isActionPerformed = isActionPerformed;
                    this.isMatching = isMatching;
                }

                @SuppressWarnings("unchecked")
                public Then<T> thenRun(Consumer<? super U> action) {
                    Objects.requireNonNull(action, "action is null");
                    final boolean run = !isActionPerformed && isMatching;
                    if (run) {
                        action.accept((U) value);
                    }
                    return new Then<>(value, isActionPerformed || run);
                }

                public Then<T> thenRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return thenRun(ignored -> action.run());
                }
            }

            final class Then<T> {

                private final T value;
                private final boolean isActionPerformed;

                private Then(T value, boolean isActionPerformed) {
                    this.value = value;
                    this.isActionPerformed = isActionPerformed;
                }

                public <U> When<T, U> when(SerializablePredicate<? super U> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    final boolean isMatching = isMatching(() -> isTrue(predicate));
                    return new When<>(value, isActionPerformed, isMatching);
                }

                public <U> When<T, U> whenIs(U prototype) {
                    final boolean isMatching = isMatching(() -> is(prototype));
                    return new When<>(value, isActionPerformed, isMatching);
                }

                @SuppressWarnings("unchecked")
                public <U> When<T, U> whenIsIn(U... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    final boolean isMatching = isMatching(() -> isIn(prototypes));
                    return new When<>(value, isActionPerformed, isMatching);
                }

                public <U> When<T, U> whenType(Class<U> type) {
                    Objects.requireNonNull(type, "type is null");
                    final boolean isMatching = isMatching(() -> isType(type));
                    return new When<>(value, isActionPerformed, isMatching);
                }

                // DEV-NOTE: setting <U = T> is the best we can do because intersection of class types Class<?> cannot be calculated
                public When<T, T> whenTypeIn(Class<?>... types) {
                    Objects.requireNonNull(types, "types is null");
                    final boolean isMatching = isMatching(() -> isTypeIn(types));
                    return new When<>(value, isActionPerformed, isMatching);
                }

                public <U> WhenApplicable<T, U> whenApplicable(SerializableConsumer<U> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new WhenApplicable<>(value, isActionPerformed, action);
                }

                public void otherwiseRun(Consumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    if (!isActionPerformed) {
                        action.accept(value);
                    }
                }

                public void otherwiseRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    if (!isActionPerformed) {
                        action.run();
                    }
                }

                public void otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    if (!isActionPerformed) {
                        throw supplier.get();
                    }
                }

                private boolean isMatching(Supplier<Predicate<? super Object>> predicate) {
                    return !isActionPerformed && predicate.get().test(value);
                }
            }

            final class WhenApplicable<T, U> {

                private final T value;
                private final boolean isActionPerformed;
                private final boolean isMatching;
                private final SerializableConsumer<? super U> action;

                private WhenApplicable(T value, boolean isActionPerformed, SerializableConsumer<? super U> action) {
                    this.value = value;
                    this.isActionPerformed = isActionPerformed;
                    this.isMatching = !isActionPerformed && action.isApplicableTo(value);
                    this.action = action;
                }

                @SuppressWarnings("unchecked")
                public Then<T> thenRun() {
                    if (isMatching) {
                        action.accept((U) value);
                    }
                    return new Then<>(value, isActionPerformed || isMatching);
                }

                public Then<T> thenThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    if (isMatching) {
                        throw supplier.get();
                    }
                    return new Then<>(value, isActionPerformed);
                }
            }
        }
    }

    @FunctionalInterface
    interface SerializableConsumer<T> extends λ<Void> {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Performs this action on the given argument.
         *
         * @param t a value of type T
         */
        void accept(T t);

        @Override
        default int arity() {
            return 1;
        }

        @Override
        default SerializableConsumer<T> curried() {
            return this;
        }

        @Override
        default SerializableConsumer<Tuple1<T>> tupled() {
            return t -> accept(t._1);
        }

        @Override
        default SerializableConsumer<T> reversed() {
            return this;
        }

        @Override
        default SerializableConsumer<T> memoized() {
            if (isMemoized()) {
                return this;
            } else {
                return (SerializableConsumer<T> & Memoized) t -> Lazy.of(() -> {
                    accept(t);
                    return null;
                });
            }
        }

        @Override
        default Type<T> getType() {
            return new Type<>(this);
        }

        final class Type<T> extends λ.Type<Void> {

            private static final long serialVersionUID = 1L;

            private Type(SerializableConsumer<T> λ) {
                super(λ);
            }

            @SuppressWarnings("unchecked")
            public Class<T> parameterType1() {
                return (Class<T>) parameterTypes()[0];
            }
        }
    }

    @FunctionalInterface
    interface SerializablePredicate<T> extends λ<Boolean> {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Checks if the given argument satisfies this predicate.
         *
         * @param t a value of type T
         * @return {@code true} if {@code t} satisfies this predicate, otherwise {@code false}
         */
        boolean test(T t);

        @Override
        default int arity() {
            return 1;
        }

        @Override
        default SerializablePredicate<T> curried() {
            return this;
        }

        @Override
        default SerializablePredicate<Tuple1<T>> tupled() {
            return t -> test(t._1);
        }

        @Override
        default SerializablePredicate<T> reversed() {
            return this;
        }

        @Override
        default SerializablePredicate<T> memoized() {
            if (isMemoized()) {
                return this;
            } else {
                return (SerializablePredicate<T> & Memoized) t -> Lazy.of(() -> test(t)).get();
            }
        }

        @Override
        default Type<T> getType() {
            return new Type<>(this);
        }

        final class Type<T> extends λ.Type<Boolean> {

            private static final long serialVersionUID = 1L;

            private Type(SerializablePredicate<T> λ) {
                super(λ);
            }

            @SuppressWarnings("unchecked")
            public Class<T> parameterType1() {
                return (Class<T>) parameterTypes()[0];
            }
        }
    }
}

interface MatchModule {

    static <T> Predicate<? super Object> is(T prototype) {
        return value -> value == prototype || (value != null && value.equals(prototype));
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    static <T> Predicate<? super Object> isIn(T... prototypes) {
        return value -> Iterator.of(prototypes).find(prototype -> is(prototype).test(value)).isDefined();
    }

    @SuppressWarnings("unchecked")
    static <T> Predicate<Object> isTrue(SerializablePredicate<T> predicate) {
        final Class<T> type = predicate.getType().parameterType1();
        return value -> (value == null || type.isAssignableFrom(value.getClass())) && ((SerializablePredicate<Object>) predicate).test(value);
    }

    static <T> Predicate<Object> isType(Class<T> type) {
        return value -> value != null && type.isAssignableFrom(value.getClass());
    }

    static <T> Predicate<Object> isTypeIn(Class<?>... types) {
        return value -> Iterator.of(types).find(type -> isType(type).test(value)).isDefined();
    }

    @SuppressWarnings("unchecked")
    static <T, U, R> Option<R> computeResult(T value,
                                             Option<R> result, boolean isMatching,
                                             Function<? super U, ? extends R> function) {
        return result.isEmpty() && isMatching ? Option.of(function.apply((U) value)) : result;
    }
}
