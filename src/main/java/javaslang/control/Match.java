/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;
import javaslang.Lazy;
import javaslang.Value;
import javaslang.collection.Iterator;
import javaslang.collection.List;

import java.io.Serializable;
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
 * @author Daniel Dietrich
 * @since 1.0.0
 */
public interface Match<R> extends Function1<Object, R> {

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

    static MatchFunction.OtherwiseRun otherwiseRun(Consumer<? super Object> action) {
        Objects.requireNonNull(action, "action is null");
        return new MatchFunction.OtherwiseRun(value -> {
            action.accept(value);
            return null;
        }, List.empty());
    }

    static MatchFunction.OtherwiseRun otherwiseRun(Runnable action) {
        Objects.requireNonNull(action, "action is null");
        return new MatchFunction.OtherwiseRun(value -> {
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
                return new When.Then<>(List.of(new Case(predicate, function)));
            }

            public <R> When.Then<R> then(R that) {
                return then(ignored -> that);
            }

            public <R> When.Then<R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public WhenRun.ThenRun thenRun(Consumer<? super T> action) {
                Objects.requireNonNull(action, "action is null");
                return new WhenRun.ThenRun(List.of(new Case(predicate, value -> {
                    action.accept(value);
                    return null;
                })));
            }

            public WhenRun.ThenRun thenRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                return new WhenRun.ThenRun(List.of(new Case(predicate, value -> {
                    action.run();
                    return null;
                })));
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

            public static final class Then<R> implements Match<R> {

                private final static long serialVersionUID = 1L;

                private final List<Case> cases;

                private Then(List<Case> cases) {
                    this.cases = cases;
                }

                @SuppressWarnings("unchecked")
                @Override
                public R apply(Object o) {
                    return (R) cases.reverse()
                            .findFirst(caze -> caze.isApplicable(o))
                            .map(caze -> caze.apply(o))
                            .orElseThrow(() -> new MatchError(o));
                }

                public <T> When<T, R> when(Function1<? super T, ? extends Boolean> predicate) {
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
        }

        final class WhenRun<T> {

            private final Predicate<? super T> predicate;
            private final List<Case> cases;

            private WhenRun(Predicate<? super T> predicate, List<Case> cases) {
                this.predicate = predicate;
                this.cases = cases;
            }

            public ThenRun thenRun(Consumer<? super T> action) {
                Objects.requireNonNull(action, "action is null");
                return new ThenRun(cases.prepend(new Case(predicate, value -> {
                    action.accept(value);
                    return null;
                })));
            }

            public ThenRun thenRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                return new ThenRun(cases.prepend(new Case(predicate, ignored -> {
                    action.run();
                    return null;
                })));
            }

            public ThenRun thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new ThenRun(cases.prepend(new Case(predicate, ignored -> {
                    throw supplier.get();
                })));
            }

            public static final class ThenRun implements Match<Void> {

                private final static long serialVersionUID = 1L;

                private final List<Case> cases;

                private ThenRun(List<Case> cases) {
                    this.cases = cases;
                }

                @Override
                public Void apply(Object o) {
                    cases.reverse()
                            .findFirst(caze -> caze.isApplicable(o))
                            .map(caze -> caze.apply(o))
                            .orElseThrow(() -> new MatchError(o));
                    return null;
                }

                public <T> WhenRun<T> when(Function1<? super T, ? extends Boolean> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    return new WhenRun<>(isTrue(predicate), cases);
                }

                public <T> WhenRun<T> whenIs(T prototype) {
                    return new WhenRun<>(is(prototype), cases);
                }

                @SuppressWarnings("unchecked")
                public <T> WhenRun<T> whenIsIn(T... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    return new WhenRun<>(isIn(prototypes), cases);
                }

                public <T> WhenRun<T> whenType(Class<T> type) {
                    Objects.requireNonNull(type, "type is null");
                    return new WhenRun<>(isType(type), cases);
                }

                public WhenRun<Object> whenTypeIn(Class<?>... types) {
                    Objects.requireNonNull(types, "types is null");
                    return new WhenRun<>(isTypeIn(types), cases);
                }

                public <T> WhenRunnable<T> whenRunnable(SerializableConsumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new WhenRunnable<>(action, cases);
                }

                public OtherwiseRun otherwiseRun(Consumer<? super Object> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseRun(value -> {
                        action.accept(value);
                        return null;
                    }, cases);
                }

                public OtherwiseRun otherwiseRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseRun(ignored -> {
                        action.run();
                        return null;
                    }, cases);
                }

                public OtherwiseRun otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new OtherwiseRun(ignored -> {
                        throw supplier.get();
                    }, cases);
                }
            }
        }

        final class WhenApplicable<T, R> {

            private final Predicate<? super Object> predicate;
            private final Function1<? super T, ? extends R> function;
            private final List<Case> cases;

            private WhenApplicable(Function1<? super T, ? extends R> function, List<Case> cases) {
                this.predicate = function::isApplicableTo;
                this.function = function;
                this.cases = cases;
            }

            public When.Then<R> thenApply() {
                return new When.Then<>(cases.prepend(new Case(predicate, function)));
            }

            public When.Then<R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new When.Then<>(cases.prepend(new Case(predicate, ignored -> {
                    throw supplier.get();
                })));
            }
        }

        final class WhenRunnable<T> {

            private final Predicate<? super Object> predicate;
            private final Function1<? super T, ? extends Void> function;
            private final List<Case> cases;

            private WhenRunnable(SerializableConsumer<? super T> action, List<Case> cases) {
                this.function = Function1.of((T t) -> {
                    action.accept(t);
                    return null;
                });
                this.predicate = function::isApplicableTo;
                this.cases = cases;
            }

            public WhenRun.ThenRun thenRun() {
                return new WhenRun.ThenRun(cases.prepend(new Case(predicate, function)));
            }

            public WhenRun.ThenRun thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new WhenRun.ThenRun(cases.prepend(new Case(predicate, ignored -> {
                    throw supplier.get();
                })));
            }
        }

        final class Otherwise<R> implements Match<R> {

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
                        .findFirst(caze -> caze.isApplicable(o))
                        .map(caze -> caze.apply(o))
                        .orElseGet(() -> function.apply(o));
            }
        }

        final class OtherwiseRun implements Match<Void> {

            private final static long serialVersionUID = 1L;

            private final Function<? super Object, ? extends Void> function;
            private final List<Case> cases;

            private OtherwiseRun(Function<? super Object, ? extends Void> function, List<Case> cases) {
                this.function = function;
                this.cases = cases;
            }

            @Override
            public Void apply(Object o) {
                cases.reverse()
                        .findFirst(caze -> caze.isApplicable(o))
                        .map(caze -> caze.apply(o))
                        .orElseGet(() -> function.apply(o));
                return null;
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

        // TODO: move this into the MatchModule (i.e. need to share it between Match states)?
        @FunctionalInterface
        interface SerializableConsumer<T> extends Consumer<T>, Serializable {
        }
    }

    interface MatchMonad<R> extends Value<R>, Supplier<R> {

        @Override
        MatchMonad<R> filter(Predicate<? super R> predicate);

        @Override
        default MatchMonad<R> filterNot(Predicate<? super R> predicate) {
            Objects.requireNonNull(predicate, "predicate is null");
            return filter(predicate.negate());
        }

        @Override
        <U> MatchMonad<U> flatMap(Function<? super R, ? extends Iterable<? extends U>> mapper);

        /**
         * A match is a singleton type.
         *
         * @return {@code true}
         */
        @Override
        default boolean isSingletonType() {
            return true;
        }

        @Override
        <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper);

        @Override
        MatchMonad<R> peek(Consumer<? super R> action);

        @Override
        default String stringPrefix() {
            return "Match";
        }

        /**
         * Transforms this {@code MatchMonad}.
         *
         * @param f   A transformation
         * @param <U> Type of transformation result
         * @return An instance of type {@code U}
         * @throws NullPointerException if {@code f} is null
         */
        default <U> U transform(Function<? super MatchMonad<? super R>, ? extends U> f) {
            Objects.requireNonNull(f, "f is null");
            return f.apply(this);
        }

        final class Of<T> {

            private final T value;

            private Of(T value) {
                this.value = value;
            }

            public <R> When.Then<T, R> as(Class<R> resultType) {
                Objects.requireNonNull(resultType, "resultType is null");
                return new When.Then<>(value, Option.none());
            }

            public <U> WhenUntyped<T, U> when(Function1<? super U, ? extends Boolean> predicate) {
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

            // DEV-NOTE: return WhenUntyped<U = T> because lower bound of class types Class<? super U> cannot be calculated (this is the best we can do)
            public <U> WhenUntyped<T, Object> whenTypeIn(Class<?>... types) {
                Objects.requireNonNull(types, "types is null");
                final boolean isMatching = isTypeIn(types).test(value);
                return new WhenUntyped<>(value, isMatching);
            }

            public <U, R> WhenApplicable<T, U, R> whenApplicable(Function1<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new WhenApplicable<>(value, Option.none(), function);
            }

            public <R> Otherwise<R> otherwise(R that) {
                return new Otherwise<>(() -> that);
            }

            public <R> Otherwise<R> otherwise(Function<? super Object, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                return new Otherwise<>(() -> function.apply(value));
            }

            public <R> Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(supplier);
            }

            public Otherwise<Void> otherwiseRun(Consumer<? super T> action) {
                Objects.requireNonNull(action, "action is null");
                return new Otherwise<>(() -> {
                    action.accept(value);
                    return null;
                });
            }

            public Otherwise<Void> otherwiseRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                return new Otherwise<>(() -> {
                    action.run();
                    return null;
                });
            }

            public <R> Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return new Otherwise<>(() -> {
                    throw supplier.get();
                });
            }
        }

        final class WhenUntyped<T, U> {

            private final T value;
            private final boolean isMatching;

            private WhenUntyped(T value, boolean isMatching) {
                this.value = value;
                this.isMatching = isMatching;
            }

            public <R> When.Then<T, R> then(Function<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                final Option<Supplier<? extends R>> result = When.computeResult(value, Option.none(), isMatching,
                        function);
                return new When.Then<>(value, result);
            }

            public <R> When.Then<T, R> then(R that) {
                return then(ignored -> that);
            }

            public <R> When.Then<T, R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public When.Then<T, Void> thenRun(Consumer<? super U> action) {
                return then(param -> {
                    action.accept(param);
                    return null;
                });
            }

            public When.Then<T, Void> thenRun(Runnable action) {
                return then(ignored -> {
                    action.run();
                    return null;
                });
            }

            public <R> When.Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }
        }

        final class When<T, U, R> {

            private final T value;
            private final Option<Supplier<? extends R>> result;
            private final boolean isMatching;

            private When(T value, Option<Supplier<? extends R>> result, boolean isMatching) {
                this.value = value;
                this.result = result;
                this.isMatching = isMatching;
            }

            public Then<T, R> then(Function<? super U, ? extends R> function) {
                Objects.requireNonNull(function, "function is null");
                final Option<Supplier<? extends R>> updatedResult = When.computeResult(value, result, isMatching, function);
                return new Then<>(value, updatedResult);
            }

            public Then<T, R> then(R that) {
                return then(ignored -> that);
            }

            public Then<T, R> then(Supplier<? extends R> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> supplier.get());
            }

            public When.Then<T, Void> thenRun(Consumer<? super U> action) {
                Objects.requireNonNull(action, "action is null");
                final Function<? super U, ? extends Void> function = value -> {
                    action.accept(value);
                    return null;
                };
                final Option<Supplier<? extends Void>> updatedResult = When.computeResult(value, result, isMatching, function);
                return new Then<>(value, updatedResult);
            }

            public When.Then<T, Void> thenRun(Runnable action) {
                Objects.requireNonNull(action, "action is null");
                final Function<? super U, ? extends Void> function = ignored -> {
                    action.run();
                    return null;
                };
                final Option<Supplier<? extends Void>> updatedResult = When.computeResult(value, null, isMatching, function);
                return new Then<>(value, updatedResult);
            }

            public Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                return then(ignored -> {
                    throw supplier.get();
                });
            }

            // DEV-NOTE: should move to MatchMonad interface (staying private) with Java 9+

            @SuppressWarnings("unchecked")
            private static <T, R> Option<Supplier<? extends R>> computeResult(Object value,
                                                                              Option<Supplier<? extends R>> result, boolean isMatching,
                                                                              Function<? super T, ? extends R> function) {
                if (result.isEmpty() && isMatching) {
                    final Function<? super Object, ? extends R> f = (Function<? super Object, ? extends R>) function;
                    return Option.of(() -> f.apply(value));
                } else {
                    return result;
                }
            }

            public static final class Then<T, R> implements MatchMonad<R> {

                private final T value;
                private final Option<Supplier<? extends R>> result;

                private Then(T value, Option<Supplier<? extends R>> result) {
                    this.value = value;
                    this.result = result;
                }

                public <U> When<T, U, R> when(Function1<? super U, ? extends Boolean> predicate) {
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

                // U = T is the best we can do
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
                    return new Otherwise<>(() -> result.orElse(() -> that).get());
                }

                public Otherwise<R> otherwise(Function<? super T, ? extends R> function) {
                    Objects.requireNonNull(function, "function is null");
                    return new Otherwise<>(() -> result.orElse(() -> function.apply(value)).get());
                }

                public Otherwise<R> otherwise(Supplier<? extends R> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise<>(() -> result.orElse(supplier).get());
                }

                public OtherwiseVoid otherwiseRun(Consumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseVoid(() -> {
                        result.orElse(() -> {
                            action.accept(value);
                            return null;
                        }).get();
                        return null;
                    });
                }

                public OtherwiseVoid otherwiseRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseVoid(() -> {
                        result.orElse(() -> {
                            action.run();
                            return null;
                        }).get();
                        return null;
                    });
                }

                public Otherwise<R> otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise<>(() -> result.orElseThrow(supplier).get());
                }

                @Override
                public MatchMonad<R> filter(Predicate<? super R> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    return result.map(supplier -> {
                        final R resultValue = supplier.get();
                        if (predicate.test(resultValue)) {
                            return this;
                        } else {
                            return new Then<>(value, Option.<Supplier<? extends R>> none());
                        }
                    }).orElse(this);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <U> MatchMonad<U> flatMap(
                        Function<? super R, ? extends Iterable<? extends U>> mapper) {
                    Objects.requireNonNull(mapper, "mapper is null");
                    return result.map(supplier -> {
                        final Option<Supplier<? extends U>> some = Option.some(() -> Value.get(mapper.apply(supplier.get())));
                        return (MatchMonad<U>) new Then<>(value, some);
                    }).orElse((MatchMonad<U>) this);
                }

                @SuppressWarnings("unchecked")
                @Override
                public <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper) {
                    Objects.requireNonNull(mapper, "mapper is null");
                    return result
                            .map(supplier -> new Then<T, U>(value, Option.some(() -> mapper.apply(supplier.get()))))
                            .orElseGet(() -> (Then<T, U>) this);
                }

                @Override
                public MatchMonad<R> peek(Consumer<? super R> action) {
                    Objects.requireNonNull(action, "action is null");
                    result.peek(supplier -> action.accept(supplier.get()));
                    return this;
                }

                @Override
                public R get() {
                    return result.orElseThrow(() -> new MatchError(value)).get();
                }

                @Override
                public boolean isEmpty() {
                    return result.isEmpty();
                }

                @Override
                public Iterator<R> iterator() {
                    return result.isEmpty() ? Iterator.empty() : Iterator.of(get());
                }

                private boolean isMatching(Supplier<Predicate<? super Object>> predicate) {
                    return result.isEmpty() && predicate.get().test(value);
                }
            }
        }

        final class WhenVoid<T, U> {

            ...

            public static final class ThenVoid<T> {

                private final T value;

                private ThenVoid(T value) {
                    this.value = value;
                }

                public <U> WhenVoid<T, U> when(Function1<? super U, ? extends Boolean> predicate) {
                    Objects.requireNonNull(predicate, "predicate is null");
                    final boolean isMatching = isMatching(() -> isTrue(predicate));
                    return new WhenVoid<>(value, isMatching);
                }

                public <U> WhenVoid<T, U> whenIs(U prototype) {
                    final boolean isMatching = isMatching(() -> is(prototype));
                    return new WhenVoid<>(value, isMatching);
                }

                @SuppressWarnings("unchecked")
                public <U> WhenVoid<T, U> whenIsIn(U... prototypes) {
                    Objects.requireNonNull(prototypes, "prototypes is null");
                    final boolean isMatching = isMatching(() -> isIn(prototypes));
                    return new WhenVoid<>(value, isMatching);
                }

                public <U> WhenVoid<T, U> whenType(Class<U> type) {
                    Objects.requireNonNull(type, "type is null");
                    final boolean isMatching = isMatching(() -> isType(type));
                    return new WhenVoid<>(value, isMatching);
                }

                // U = T is the best we can do
                public WhenVoid<T, T> whenTypeIn(Class<?>... types) {
                    Objects.requireNonNull(types, "types is null");
                    final boolean isMatching = isMatching(() -> isTypeIn(types));
                    return new WhenVoid<>(value, isMatching);
                }

                // we widen function result type because Void is of type Object
                public <U> WhenApplicableVoid<T, U> whenApplicable(Function1<? super U, ? extends Object> function) {
                    Objects.requireNonNull(function, "function is null");
                    return new WhenApplicable<>(value, function);
                }

                public OtherwiseVoid otherwiseRun(Consumer<? super T> action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseVoid(() -> {
                        result.orElse(() -> {
                            action.accept(value);
                            return null;
                        }).get();
                        return null;
                    });
                }

                public OtherwiseVoid otherwiseRun(Runnable action) {
                    Objects.requireNonNull(action, "action is null");
                    return new OtherwiseVoid(() -> {
                        result.orElse(() -> {
                            action.run();
                            return null;
                        }).get();
                        return null;
                    });
                }

                public OtherwiseVoid otherwiseThrow(Supplier<? extends RuntimeException> supplier) {
                    Objects.requireNonNull(supplier, "supplier is null");
                    return new Otherwise<>(() -> result.orElseThrow(supplier).get());
                }

                private boolean isMatching(Supplier<Predicate<? super Object>> predicate) {
                    return result.isEmpty() && predicate.get().test(value);
                }
            }
        }

        final class WhenApplicable<T, U, R> {

            private final T value;
            private final Option<Supplier<? extends R>> result;
            private final boolean isMatching;
            private final Function1<? super U, ? extends R> function;

            public WhenApplicable(T value, Option<Supplier<? extends R>> result,
                                  Function1<? super U, ? extends R> function) {
                this.value = value;
                this.result = result;
                this.isMatching = result.isEmpty() && function.isApplicableTo(value);
                this.function = function;
            }

            public When.Then<T, R> thenApply() {
                final Option<Supplier<? extends R>> updatedResult = MatchMonad.When.computeResult(value, result,
                        isMatching, function);
                return new When.Then<>(value, updatedResult);
            }

            public When.Then<T, R> thenThrow(Supplier<? extends RuntimeException> supplier) {
                Objects.requireNonNull(supplier, "supplier is null");
                final Option<Supplier<? extends R>> updatedResult = MatchMonad.When.computeResult(value, result,
                        isMatching, ignored -> {
                            throw supplier.get();
                        });
                return new When.Then<T, R>(value, updatedResult);
            }
        }

        final class WhenApplicableVoid<T, U> {
            ...
        }

        final class Otherwise<R> implements MatchMonad<R> {

            // we need to ensure referential transparency of Otherwise.get()
            private final Lazy<R> result;

            private Otherwise(Supplier<? extends R> supplier) {
                this.result = Lazy.of(supplier);
            }

            @Override
            public MatchMonad<R> filter(Predicate<? super R> predicate) {
                Objects.requireNonNull(predicate, "predicate is null");
                return new Otherwise<>(() -> result.filter(predicate).get());
            }

            @Override
            public <U> MatchMonad<U> flatMap(Function<? super R, ? extends Iterable<? extends U>> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return new Otherwise<>(() -> Value.get(mapper.apply(result.get())));
            }

            @Override
            public <U> MatchMonad<U> map(Function<? super R, ? extends U> mapper) {
                Objects.requireNonNull(mapper, "mapper is null");
                return new Otherwise<>(() -> mapper.apply(result.get()));
            }

            @Override
            public MatchMonad<R> peek(Consumer<? super R> action) {
                Objects.requireNonNull(action, "action is null");
                action.accept(result.get());
                return this;
            }

            @Override
            public R get() {
                return result.get();
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public Iterator<R> iterator() {
                return Iterator.of(get());
            }
        }

        final class OtherwiseVoid {

            // we need to ensure referential transparency of Otherwise.get()
            private final Lazy<Void> result;

            private OtherwiseVoid(Supplier<Void> supplier) {
                this.result = Lazy.of(supplier);
            }

            public void run() {
                result.get();
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
        return value -> Iterator.of(prototypes).findFirst(prototype -> is(prototype).test(value)).isDefined();
    }

    @SuppressWarnings("unchecked")
    static <T> Predicate<? super Object> isTrue(Function1<? super T, ? extends Boolean> predicate) {
        final Class<? super T> type = predicate.getType().parameterType1();
        return value -> (value == null || type.isAssignableFrom(value.getClass()))
                && ((Function1<? super Object, ? extends Boolean>) predicate).apply(value);
    }

    static <T> Predicate<? super Object> isType(Class<T> type) {
        return value -> value != null && type.isAssignableFrom(value.getClass());
    }

    static <T> Predicate<? super Object> isTypeIn(Class<?>... types) {
        return value -> Iterator.of(types).findFirst(type -> isType(type).test(value)).isDefined();
    }
}
