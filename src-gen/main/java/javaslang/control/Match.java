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
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javaslang.Function1;
import javaslang.Lazy;
import javaslang.collection.List;

/**
 * TODO
 *
 * @param <R> The result type of the Match expression.
 * @since 1.0.0
 */
public interface Match<R> extends Function<Object, R> {

    /**
     * Specifies the type of the match expression. In many cases it is not necessary to call {@code ofType}. This
     * method is intended to be used for readability reasons when the upper bound of the cases cannot be inferred,
     * i.e. instead of
     *
     * <pre>
     * <code>
     * final Match&lt;Number&gt; toNumber = Match
     *         .&lt;Number&gt;caze((Integer i) -&gt; i)
     *         .caze((String s) -&gt; new BigDecimal(s))
     * </code>
     * </pre>
     *
     * we write
     *
     * <pre>
     * <code>
     * final Match&lt;Number&gt; toNumber = ofType(Number.class)
     *         .caze((Integer i) -&gt; i)
     *         .caze((String s) -&gt; new BigDecimal(s))
     * </code>
     * </pre>
     *
     * @param type the hint of type {@code R}
     * @param <R>  the type of the {@code Match} expression
     * @return a new match builder
     */
    static <R> Typed<R> ofType(Class<R> type) {
        Objects.requireNonNull(type, "type is null");
        return new Typed<>();
    }

    static <R> Case<R> caze(Function1<?, R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <T, R> Case<R> caze(T prototype, Function1<T, R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(prototype, function);
    }

    static <R> Case<R> caze(BooleanFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(ByteFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(CharFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(DoubleFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(FloatFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(IntFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(LongFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    static <R> Case<R> caze(ShortFunction<R> function) {
        Objects.requireNonNull(function, "function is null");
        return Case.of(function);
    }

    /**
     * TODO
     *
     * @param <R>
     */
    class Typed<R> implements Expression.HasCases<R> {

        @Override
        public Case<R> caze(Function1<?, R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public <T> Case<R> caze(T prototype, Function1<T, R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(prototype, function);
        }

        @Override
        public Case<R> caze(BooleanFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(ByteFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(CharFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(DoubleFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(FloatFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(IntFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(LongFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }

        @Override
        public Case<R> caze(ShortFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            return Case.of(function);
        }
    }

    /**
     * TODO
     *
     * @param <R>
     */
    class Case<R> implements Match<R>, Expression.HasCases<R> {

        private final List<Function<Object, Option<R>>> cases;
        private final Lazy<Expression<R>> match;

        private Case(List<Function<Object, Option<R>>> cases) {
            this.cases = cases;
            this.match = Lazy.of(() -> new Expression<>(cases.reverse(), None.instance()));
        }

        private static <R> Case<R> of(Function1<?, R> function) {
            return new Case<>(List.of(Case.caze(None.instance(), function)));
        }

        private static <T, R> Case<R> of(T prototype, Function1<?, R> function) {
            return new Case<>(List.of(Case.caze(new Some<>(prototype), function)));
        }

        private static <R> Case<R> of(BooleanFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Boolean, R>) function::apply, Boolean.class)));
        }

        private static <R> Case<R> of(ByteFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Byte, R>) function::apply, Byte.class)));
        }

        private static <R> Case<R> of(CharFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Character, R>) function::apply, Character.class)));
        }

        private static <R> Case<R> of(DoubleFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Double, R>) function::apply, Double.class)));
        }

        private static <R> Case<R> of(FloatFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Float, R>) function::apply, Float.class)));
        }

        private static <R> Case<R> of(IntFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Integer, R>) function::apply, Integer.class)));
        }

        private static <R> Case<R> of(LongFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Long, R>) function::apply, Long.class)));
        }

        private static <R> Case<R> of(ShortFunction<R> function) {
          return new Case<>(List.of(Case.caze(None.instance(), (Function1<Short, R>) function::apply, Short.class)));
        }

        @Override
        public R apply(Object o) {
            return match.get().apply(o);
        }

        @Override
        public Case<R> caze(Function1<?, R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), function);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public <T> Case<R> caze(T prototype, Function1<T, R> function) {
            final Function<Object, Option<R>> caze = caze(new Some<>(prototype), function);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(BooleanFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Boolean, R>) function::apply, Boolean.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(ByteFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Byte, R>) function::apply, Byte.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(CharFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Character, R>) function::apply, Character.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(DoubleFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Double, R>) function::apply, Double.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(FloatFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Float, R>) function::apply, Float.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(IntFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Integer, R>) function::apply, Integer.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(LongFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Long, R>) function::apply, Long.class);
            return new Case<>(cases.prepend(caze));
        }

        @Override
        public Case<R> caze(ShortFunction<R> function) {
            final Function<Object, Option<R>> caze = caze(None.instance(), (Function1<Short, R>) function::apply, Short.class);
            return new Case<>(cases.prepend(caze));
        }

        public Expression<R> orElse(R defaultValue) {
            return new Expression<>(cases.reverse(), new Some<>(Lazy.of(() -> defaultValue)));
        }

        public Expression<R> orElse(Supplier<R> defaultSupplier) {
            return new Expression<>(cases.reverse(), new Some<>(Lazy.of(defaultSupplier)));
        }

        private static <R> Function<Object, Option<R>> caze(Option<?> prototype, Function1<?, R> function) {
            final MethodType type = function.getType();
            // the compiler may add additional parameters to the lambda, our parameter is the last one
            final Class<?> parameterType = type.parameterType(type.parameterCount() - 1);
            return caze(prototype, function, parameterType);
        }

        private static <R> Function<Object, Option<R>> caze(Option<?> prototype, Function1<?, R> function, Class<?> parameterType) {
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
     * TODO
     *
     * @param <R>
     */
    class Expression<R> implements Match<R> {

        private Iterable<Function<Object, Option<R>>> cases;
        private Option<Lazy<R>> orElse;

        private Expression(Iterable<Function<Object, Option<R>>> cases, Option<Lazy<R>> orElse) {
            this.cases = cases;
            this.orElse = orElse;
        }

        @Override
        public R apply(Object o) {
            for (Function<Object, Option<R>> caze : cases) {
                final Option<R> result = caze.apply(o);
                if (result.isDefined()) {
                    return result.get();
                }
            }
            return orElse.orElseThrow(() -> new MatchError(o)).get();
        }

        // Note: placed this interface here, because interface Match cannot have private inner interfaces
        private interface HasCases<R> {

            HasCases<R> caze(Function1<?, R> function);

            <T> HasCases<R> caze(T prototype, Function1<T, R> function);

            HasCases<R> caze(BooleanFunction<R> function);

            HasCases<R> caze(ByteFunction<R> function);

            HasCases<R> caze(CharFunction<R> function);

            HasCases<R> caze(DoubleFunction<R> function);

            HasCases<R> caze(FloatFunction<R> function);

            HasCases<R> caze(IntFunction<R> function);

            HasCases<R> caze(LongFunction<R> function);

            HasCases<R> caze(ShortFunction<R> function);
        }
    }

    /**
     * A function {@code f: boolean -&gt; R} that takes a primitive boolean value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: byte -&gt; R} that takes a primitive byte value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: char -&gt; R} that takes a primitive char value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: double -&gt; R} that takes a primitive double value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: float -&gt; R} that takes a primitive float value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: int -&gt; R} that takes a primitive int value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: long -&gt; R} that takes a primitive long value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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
     * A function {@code f: short -&gt; R} that takes a primitive short value and returns a value of type R.
     *
     * @param <R> Return type of the function.
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