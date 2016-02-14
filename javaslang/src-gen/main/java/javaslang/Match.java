/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javaslang.control.Option;

/**
 * Scala-like structural pattern matching for Java.
 *
 * <pre><code>
 * // Match API
 * import static javaslang.Match.*;
 *
 * // Match Patterns for Javaslang types
 * import static javaslang.match.Patterns.*;
 *
 * // Example
 * Match(list).of(
 *         Case(List($(), $()), (x, xs) -&gt; "head: " + x + ", tail: " + xs),
 *         Case($_, -&gt; "Nil")
 * );
 *
 * // Syntactic sugar
 * list.match(
 *         Case(List($(), $()), (x, xs) -&gt; "head: " + x + ", tail: " + xs),
 *         Case($_, -&gt; "Nil")
 * );
 * </code></pre>
 */
public final class Match<T> {

    private final T value;

    private Match(T value) {
        this.value = value;
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public final <SUP extends R, R> R of(Case<? extends T, ? extends R>... cases) {
        return safe(cases).getOrElseThrow(() -> new MatchError(value));
    }

    @SuppressWarnings({ "unchecked", "varargs" })
    @SafeVarargs
    public final <SUP extends R, R> Option<R> safe(Case<? extends T, ? extends R>... cases) {
        Objects.requireNonNull(cases, "cases is null");
        for (Case<? extends T, ? extends R> _case : cases) {
            final Option<R> it = ((Case<T, R>) _case).apply(value);
            if (it.isDefined()) {
                return it;
            }
        }
        return Option.none();
    }

    // -- static Match API

    /**
     * Entry point of the match API.
     *
     * @param value a value to be matched
     * @param <T> type of the value
     * @return a new {@code Match} instance
     */
    @SuppressWarnings("MethodNameSameAsClassName")
    public static <T> Match<T> Match(T value) {
        return new Match<>(value);
    }

    // -- Cases

    public static <T, R> Case<T, R> Case(T value, R retVal) {
        return new Case0<>(new Pattern0() {
            @Override
            public Option<Void> apply(Object o) {
                return Pattern0.equals(o, value);
            }
        }, () -> retVal);
    }

    public static <T, R> Case<T, R> Case(InversePattern<T> pattern, Function<? super T, ? extends R> f) {
        return new Case1<>(new Pattern1<T, T>() {
            @SuppressWarnings("unchecked")
            @Override
            public Option<T> apply(Object o) {
                return pattern.apply((T) o);
            }
        }, f);
    }

    public static <T, R> Case<T, R> Case(Pattern0 pattern, Supplier<? extends R> f) {
        return new Case0<>(pattern, f);
    }

    // syntactic sugar
    public static <T, R> Case<T, R> Case(Pattern0 pattern, R retVal) {
        return new Case0<>(pattern, () -> retVal);
    }

    public static <T, T1, R> Case<T, R> Case(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case1<>(pattern, f);
    }

    public static <T, T1, T2, R> Case<T, R> Case(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case2<>(pattern, f);
    }

    public static <T, T1, T2, T3, R> Case<T, R> Case(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case3<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, R> Case<T, R> Case(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case4<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, R> Case<T, R> Case(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case5<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, R> Case<T, R> Case(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case6<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, R> Case<T, R> Case(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case7<>(pattern, f);
    }

    public static <T, T1, T2, T3, T4, T5, T6, T7, T8, R> Case<T, R> Case(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
        Objects.requireNonNull(pattern, "pattern is null");
        return new Case8<>(pattern, f);
    }

    // -- Atomic matchers $_, $(), $(val)

    /**
     * Wildcard pattern.
     * <p>
     * Matches any value but does not extract it.
     */
    public static final Pattern0 $_ = new Pattern0() {
        @Override
        public Option<Void> apply(Object any) {
            return Option.nothing();
        }
    };

    /**
     * Wildcard extractor.
     * <p>
     * Matches any value and extracts it as named lambda parameter.
     *
     * @param <T> injected type of the underlying value
     * @return a new {@code InversePattern} instance
     */
    public static <T> InversePattern<T> $() {
        return new InversePattern<T>() {
            @Override
            public Option<T> apply(T t) {
                return Option.some(t);
            }
        };
    }

    /**
     * Value extractor.
     * <p>
     * Matches a specific value and extracts it as named lambda parameter.
     *
     * @param <T1>      type of the prototype
     * @param prototype the value that should be equal to the underlying object
     * @return a new {@code Pattern1} instance
     */
    public static <T1> Pattern1<T1, T1> $(T1 prototype) {
        return new Pattern1<T1, T1>() {
            @SuppressWarnings("unchecked")
            @Override
            public Option<T1> apply(Object that) {
                // 'that' is of type T1 when injected by a type-safe pattern
                return Objects.equals(that, prototype) ? Option.some((T1) that) : Option.none();
            }
        };
    }

    // -- Match Cases

    public interface Case<T, R> extends Function<Object, Option<R>> {
    }

    public static final class Case0<T, R> implements Case<T, R> {

        private final Pattern0 pattern;
        private final Supplier<? extends R> f;

        private Case0(Pattern0 pattern, Supplier<? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(ignored -> f.get());
        }
    }

    public static final class Case1<T, T1, R> implements Case<T, R> {

        private final Pattern1<T, T1> pattern;
        private final Function<? super T1, ? extends R> f;

        private Case1(Pattern1<T, T1> pattern, Function<? super T1, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(f::apply);
        }
    }

    public static final class Case2<T, T1, T2, R> implements Case<T, R> {

        private final Pattern2<T, T1, T2> pattern;
        private final BiFunction<? super T1, ? super T2, ? extends R> f;

        private Case2(Pattern2<T, T1, T2> pattern, BiFunction<? super T1, ? super T2, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2));
        }
    }

    public static final class Case3<T, T1, T2, T3, R> implements Case<T, R> {

        private final Pattern3<T, T1, T2, T3> pattern;
        private final Function3<? super T1, ? super T2, ? super T3, ? extends R> f;

        private Case3(Pattern3<T, T1, T2, T3> pattern, Function3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3));
        }
    }

    public static final class Case4<T, T1, T2, T3, T4, R> implements Case<T, R> {

        private final Pattern4<T, T1, T2, T3, T4> pattern;
        private final Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f;

        private Case4(Pattern4<T, T1, T2, T3, T4> pattern, Function4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3, t._4));
        }
    }

    public static final class Case5<T, T1, T2, T3, T4, T5, R> implements Case<T, R> {

        private final Pattern5<T, T1, T2, T3, T4, T5> pattern;
        private final Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f;

        private Case5(Pattern5<T, T1, T2, T3, T4, T5> pattern, Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5));
        }
    }

    public static final class Case6<T, T1, T2, T3, T4, T5, T6, R> implements Case<T, R> {

        private final Pattern6<T, T1, T2, T3, T4, T5, T6> pattern;
        private final Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f;

        private Case6(Pattern6<T, T1, T2, T3, T4, T5, T6> pattern, Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6));
        }
    }

    public static final class Case7<T, T1, T2, T3, T4, T5, T6, T7, R> implements Case<T, R> {

        private final Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern;
        private final Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f;

        private Case7(Pattern7<T, T1, T2, T3, T4, T5, T6, T7> pattern, Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7));
        }
    }

    public static final class Case8<T, T1, T2, T3, T4, T5, T6, T7, T8, R> implements Case<T, R> {

        private final Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern;
        private final Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f;

        private Case8(Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> pattern, Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            this.pattern = pattern;
            this.f = f;
        }

        @Override
        public Option<R> apply(Object o) {
            return pattern.apply(o).map(t -> f.apply(t._1, t._2, t._3, t._4, t._5, t._6, t._7, t._8));
        }
    }

    // -- Match Patterns
    //    These can't be @FunctionalInterfaces because of ambiguities.
    //    For benchmarks lambda vs. abstract class see http://www.oracle.com/technetwork/java/jvmls2013kuksen-2014088.pdf

    // used by any-match $() to inject a type into the pattern
    public static abstract class InversePattern<T> {

        public abstract Option<T> apply(T t);

        @SuppressWarnings("unchecked")
        public static <T> InversePattern<T> narrow(InversePattern<? extends T> p) {
            return (InversePattern<T>) p;
        }
    }

    // no type forwarding via T here, type ignored
    public static abstract class Pattern0 {

        public abstract Option<Void> apply(Object o);

        // for @Unapply result type Tuple0
        public static <T> Pattern0 create(Class<? super T> c) {
            return new Pattern0() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Void> apply(Object o) {
                    return (o != null && c.isAssignableFrom(o.getClass())) ? Option.nothing() : Option.none();
                }
            };
        }

        // should have been Class<T> instead of Class<? super T> but it does not work for complex generic types
        public static <T> Pattern0 create(Class<? super T> c, Function<T, Option<Void>> unapply) {
            return new Pattern0() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Void> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }

        public static Option<Void> equals(Object o1, Object o2) {
            return Objects.equals(o1, o2) ? Option.nothing() : Option.none();
        }
    }

    public static abstract class Pattern1<T, T1> {

        public abstract Option<T1> apply(Object o);

        public static <T, T1> Pattern1<T, T1> create(Class<? super T> c, Function<T, Option<T1>> unapply) {
            return new Pattern1<T, T1>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<T1> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern2<T, T1, T2> {

        public abstract Option<Tuple2<T1, T2>> apply(Object o);

        public static <T, T1, T2> Pattern2<T, T1, T2> create(Class<? super T> c, Function<T, Option<Tuple2<T1, T2>>> unapply) {
            return new Pattern2<T, T1, T2>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple2<T1, T2>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern3<T, T1, T2, T3> {

        public abstract Option<Tuple3<T1, T2, T3>> apply(Object o);

        public static <T, T1, T2, T3> Pattern3<T, T1, T2, T3> create(Class<? super T> c, Function<T, Option<Tuple3<T1, T2, T3>>> unapply) {
            return new Pattern3<T, T1, T2, T3>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple3<T1, T2, T3>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern4<T, T1, T2, T3, T4> {

        public abstract Option<Tuple4<T1, T2, T3, T4>> apply(Object o);

        public static <T, T1, T2, T3, T4> Pattern4<T, T1, T2, T3, T4> create(Class<? super T> c, Function<T, Option<Tuple4<T1, T2, T3, T4>>> unapply) {
            return new Pattern4<T, T1, T2, T3, T4>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple4<T1, T2, T3, T4>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern5<T, T1, T2, T3, T4, T5> {

        public abstract Option<Tuple5<T1, T2, T3, T4, T5>> apply(Object o);

        public static <T, T1, T2, T3, T4, T5> Pattern5<T, T1, T2, T3, T4, T5> create(Class<? super T> c, Function<T, Option<Tuple5<T1, T2, T3, T4, T5>>> unapply) {
            return new Pattern5<T, T1, T2, T3, T4, T5>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple5<T1, T2, T3, T4, T5>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern6<T, T1, T2, T3, T4, T5, T6> {

        public abstract Option<Tuple6<T1, T2, T3, T4, T5, T6>> apply(Object o);

        public static <T, T1, T2, T3, T4, T5, T6> Pattern6<T, T1, T2, T3, T4, T5, T6> create(Class<? super T> c, Function<T, Option<Tuple6<T1, T2, T3, T4, T5, T6>>> unapply) {
            return new Pattern6<T, T1, T2, T3, T4, T5, T6>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple6<T1, T2, T3, T4, T5, T6>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern7<T, T1, T2, T3, T4, T5, T6, T7> {

        public abstract Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> apply(Object o);

        public static <T, T1, T2, T3, T4, T5, T6, T7> Pattern7<T, T1, T2, T3, T4, T5, T6, T7> create(Class<? super T> c, Function<T, Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>>> unapply) {
            return new Pattern7<T, T1, T2, T3, T4, T5, T6, T7>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple7<T1, T2, T3, T4, T5, T6, T7>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }

    public static abstract class Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> {

        public abstract Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> apply(Object o);

        public static <T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8> create(Class<? super T> c, Function<T, Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>>> unapply) {
            return new Pattern8<T, T1, T2, T3, T4, T5, T6, T7, T8>() {
                @SuppressWarnings("unchecked")
                @Override
                public Option<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>> apply(Object o) {
                    if (o != null && c.isAssignableFrom(o.getClass())) {
                        return unapply.apply(((T) o));
                    } else {
                        return Option.none();
                    }
                }
            };
        }
    }
}