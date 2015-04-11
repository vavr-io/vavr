/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Function1;

import java.io.Serializable;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A better switch for Java. A Match...
 * <ul>
 * <li>has a fluent API and is an expression</li>
 * <li>is able to match types, i.e. {@code Match.caze((byte b) -> "a byte: " + b)}</li>
 * <li>is able to match values, i.e. {@code Match.caze(BigDecimal.ZERO, b -> "Zero: " + b)}</li>
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
 * <code>Match
 *     .&lt;Number&gt; caze((Integer i) -&gt; i)
 *     .caze((String s) -&gt; new BigDecimal(s))
 *     .orElse(() -&gt; -1)
 *     .apply(1.0d); // result: -1</code>
 * </pre>
 * <p>
 * The following calls are equivalent:
 * <ul>
 * <li>{@code new Match.Builder<R>.caze(...).build().apply(obj)}</li>
 * <li>{@code new Match.Builder<R>.caze(...).apply(obj)}</li>
 * <li>{@code Match.caze(...).apply(obj)}</li>
 * </ul>
 *
 * @param <R> The result type of the Match expression.
 * @since 1.0.0
 */
public final class Match<R> {

    private final List<Function1<Object, Option<R>>> cases;
    private final Option<Supplier<R>> defaultOption;

    private Match(List<Function1<Object, Option<R>>> cases, Option<Supplier<R>> defaultOption) {
        this.cases = cases;
        this.defaultOption = defaultOption;
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param <R>      return type of the matcher function
     * @param function A function which is applied to a matched object.
     * @return A Match of type T
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(Function1<?, R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(prototype, function)}.
     *
     * @param <T>       type of the prototype object
     * @param <R>       return type of the matcher function
     * @param prototype An object which matches by equality.
     * @param function  A function which is applied to a matched object.
     * @return A Match of type T
     */
    public static <T, R> Match.Builder<R> caze(T prototype, Function1<T, R> function) {
        return new Match.Builder<R>().caze(prototype, function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A boolean function, i.e. {@code boolean -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type boolean
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(BooleanFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A byte function, i.e. {@code byte -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type byte
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(ByteFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A char function, i.e. {@code char -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type char
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(CharFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A double function, i.e. {@code double -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type double
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(DoubleFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A float function, i.e. {@code float -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type float
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(FloatFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A int function, i.e. {@code int -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type int
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(IntFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A long function, i.e. {@code long -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type long
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(LongFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Shortcut for {@code new Match.Builder<R>().caze(function)}.
     *
     * @param function A short function, i.e. {@code short -> R}.
     * @param <R>      return type of the matcher function
     * @return A Match of type short
     */
    @SuppressWarnings("overloads")
    public static <R> Match.Builder<R> caze(ShortFunction<R> function) {
        return new Match.Builder<R>().caze(function);
    }

    /**
     * Applies an object to this matcher. This is the implementation of the {@link Function1} interface.
     *
     * @param obj An object.
     * @return The result when applying the given obj to the first matching case. If the case has a consumer, the result
     * is null, otherwise the result of the underlying function or supplier.
     * @throws MatchError                         if no Match case matches the given object and no default is defined via orElse().
     * @throws javaslang.control.Failure.NonFatal if an error occurs executing the matched case.
     */
    public R apply(Object obj) {
        for (Function1<Object, Option<R>> caze : cases) {
            final Option<R> result = caze.apply(obj);
            if (result.isDefined()) {
                return result.get();
            }
        }
        return defaultOption.orElseThrow(() -> new MatchError(obj)).get();
    }

    /**
     * A Match builder, providing fluent API to specify Match cases.
     *
     * @param <R> Return type of the Match
     * @since 1.0.0
     */
    public static class Builder<R> extends OrElseBuilder<R> {

        private final List<Function1<Object, Option<R>>> cases = new ArrayList<>();
        private Option<Supplier<R>> defaultOption = Option.none();

        /**
         * Use this method to match by object type T. An object o matches this case, if
         * {@code o != null && T isAssignableFrom o.getClass()}.
         *
         * @param function A Function which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(Function1<?, R> function) {
            Objects.requireNonNull(function, "function is null");
            cases.add(caze(None.instance(), function));
            return this;
        }

        /**
         * Use this method to match by prototype value of object type T. An object o matches this case, if
         * {@code prototype == o || (prototype != null && prototype.equals(o))}.
         *
         * @param <T>       type of the object to be matched
         * @param prototype An object to be matched by equality as defined above.
         * @param function  A Function which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        // DEV NOTE: the compiler cannot distinguish between primitive and Object types, e.g.
        // public Match<R> caze(int prototype, IntFunction<R> function)
        // Autoboxing does not work here.
        public <T> Builder<R> caze(T prototype, Function1<T, R> function) {
            Objects.requireNonNull(function, "function is null");
            cases.add(caze(new Some<>(prototype), function));
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
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(BooleanFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Boolean b) -> function.apply(b), Boolean.class));
            return this;
        }

        /**
         * Use this method to match by byte. An object o matches this case, if {@code o != null &&
         * o.getClass() == Byte.class}.
         *
         * @param function A ByteFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(ByteFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Byte b) -> function.apply(b), Byte.class));
            return this;
        }

        /**
         * Use this method to match by char. An object o matches this case, if {@code o != null &&
         * o.getClass() == Character.class}.
         *
         * @param function A CharFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(CharFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Character c) -> function.apply(c), Character.class));
            return this;
        }

        /**
         * Use this method to match by double. An object o matches this case, if {@code o != null &&
         * o.getClass() == Double.class}.
         *
         * @param function A DoubleFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(DoubleFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Double d) -> function.apply(d), Double.class));
            return this;
        }

        /**
         * Use this method to match by float. An object o matches this case, if {@code o != null &&
         * o.getClass() == Float.class}.
         *
         * @param function A FloatFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(FloatFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Float f) -> function.apply(f), Float.class));
            return this;
        }

        /**
         * Use this method to match by int. An object o matches this case, if {@code o != null &&
         * o.getClass() == Integer.class}.
         *
         * @param function A IntFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(IntFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Integer i) -> function.apply(i), Integer.class));
            return this;
        }

        /**
         * Use this method to match by long. An object o matches this case, if {@code o != null &&
         * o.getClass() == Long.class}.
         *
         * @param function A LongFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(LongFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Long l) -> function.apply(l), Long.class));
            return this;
        }

        /**
         * Use this method to match by short. An object o matches this case, if {@code o != null &&
         * o.getClass() == Short.class}.
         *
         * @param function A ShortFunction which is applied to a matched object.
         * @return this, the current instance of Match.
         * @throws NullPointerException if function is null.
         */
        @SuppressWarnings("overloads")
        public Builder<R> caze(ShortFunction<R> function) {
            Objects.requireNonNull(function, "function is null");
            //noinspection Convert2MethodRef
            cases.add(caze(None.instance(), (Short s) -> function.apply(s), Short.class));
            return this;
        }

        @Override
        protected List<Function1<Object, Option<R>>> getCases() {
            return cases;
        }

        @Override
        protected Option<Supplier<R>> getDefault() {
            return defaultOption;
        }

        @Override
        protected void setDefault(Option<Supplier<R>> defaultOption) {
            this.defaultOption = defaultOption;
        }

        private Function1<Object, Option<R>> caze(Option<?> prototype, Function1<?, R> function) {
            final MethodType type = function.getType();
            // the compiler may add additional parameters to the lambda, our parameter is the last one
            final Class<?> parameterType = type.parameterType(type.parameterCount() - 1);
            return caze(prototype, function, parameterType);
        }

        /**
         * Constructs a Case, used for functions having a primitive parameter type.
         *
         * @param prototype     A prototype object.
         * @param function      A function with boxed argument.
         * @param parameterType The type of the unboxed function argument.
         */
        // TODO: split prototype and non-prototype cases to increase performance
        private Function1<Object, Option<R>> caze(Option<?> prototype, Function1<?, R> function, Class<?> parameterType) {
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
     * An abstract Match builder providing the orElse case.
     *
     * @param <R> Return type of the Match
     * @since 1.0.0
     */
    public static abstract class OrElseBuilder<R> extends MatchBuilder<R> {

        /**
         * Defines the default return value.
         *
         * @param defaultValue The default return value of this Match.
         * @return this, the current instance of Match.
         */
        public MatchBuilder<R> orElse(R defaultValue) {
            setDefault(Option.of(() -> defaultValue));
            return this;
        }

        /**
         * Defines the supplier of the default return value.
         *
         * @param defaultSupplier Supplier of the default return value of this Match.
         * @return this, the current instance of Match.
         */
        public MatchBuilder<R> orElse(Supplier<R> defaultSupplier) {
            Objects.requireNonNull(defaultSupplier, "defaultSupplier is null");
            setDefault(Option.of(defaultSupplier));
            return this;
        }
    }

    /**
     * An abstract Match builder providing base methods for the internal Match builder DSL.
     *
     * @param <R> Return type of the Match
     * @since 1.0.0
     */
    public static abstract class MatchBuilder<R> {

        /**
         * Finally used by a Match builder to build the Match instance.
         *
         * @return a new Match consisting of the cases specified before.
         */
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

        protected abstract List<Function1<Object, Option<R>>> getCases();

        protected abstract Option<Supplier<R>> getDefault();

        protected abstract void setDefault(Option<Supplier<R>> defaultOption);
    }

    /**
     * A function {@code f: boolean -&gt; R} that takes a primitive boolean value and returns a value of type R.
     *
     * @param <R> Return type of the function.
     * @since 1.0.0
     */
    @FunctionalInterface
    public interface BooleanFunction<R> extends Serializable {

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
    public interface ByteFunction<R> extends Serializable {

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
    public interface CharFunction<R> extends Serializable {

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
    public interface DoubleFunction<R> extends Serializable {

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
    public interface FloatFunction<R> extends Serializable {

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
    public interface IntFunction<R> extends Serializable {

        /**
         * The <a href="https://docs.oracle.com/javase/8/docs/api/index.html">serial version uid</a>.
         */
        long serialVersionUID = 1L;

        /**
         * Applies this function to the given value.
         *
         * @param value An int value
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
    public interface LongFunction<R> extends Serializable {

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
    public interface ShortFunction<R> extends Serializable {

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
