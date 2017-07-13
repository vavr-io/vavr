/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.control;

import io.vavr.*;
import io.vavr.collection.Seq;
import io.vavr.collection.Iterator;
import io.vavr.collection.Vector;

import java.io.Serializable;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.vavr.API.Match;
import static io.vavr.control.TryModule.isFatal;
import static io.vavr.control.TryModule.sneakyThrow;

/**
 * The Try control gives us the ability write safe code without focusing on try-catch blocks in the presence of exceptions.
 * <p>
 * The following exceptions are considered to be fatal/non-recoverable:
 * <ul>
 * <li>{@linkplain InterruptedException}</li>
 * <li>{@linkplain LinkageError}</li>
 * <li>{@linkplain ThreadDeath}</li>
 * <li>{@linkplain VirtualMachineError} (i.e. {@linkplain OutOfMemoryError} or {@linkplain StackOverflowError})</li>
 * </ul>
 * <p>
 * <strong>Important note:</strong> Try may re-throw (undeclared) exceptions, e.g. on {@code get()}. From within a
 * dynamic proxy {@link java.lang.reflect.InvocationHandler} this will lead to an
 * {@link java.lang.reflect.UndeclaredThrowableException}. For more information, please read
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html">Dynamic Proxy Classes</a>.
 *
 * @param <T> Value type in the case of success.
 * @author Daniel Dietrich
 */
public interface Try<T> extends Value<T>, Serializable {

    long serialVersionUID = 1L;

    /**
     * Creates a Try of a CheckedFunction0.
     *
     * @param supplier A checked supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.apply())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.apply()}.
     */
    static <T> Try<T> of(CheckedFunction0<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Success<>(supplier.apply());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }


    /**
     * Creates a Try of a Supplier.
     *
     * @param supplier A supplier
     * @param <T>      Component type
     * @return {@code Success(supplier.get())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code supplier.get()}.
     */
    static <T> Try<T> ofSupplier(Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return of(supplier::get);
    }

    /**
     * Creates a Try of a Callable.
     *
     * @param callable A callable
     * @param <T>      Component type
     * @return {@code Success(callable.call())} if no exception occurs, otherwise {@code Failure(throwable)} if an
     * exception occurs calling {@code callable.call()}.
     */
    static <T> Try<T> ofCallable(Callable<? extends T> callable) {
        Objects.requireNonNull(callable, "callable is null");
        return of(callable::call);
    }

    /**
     * Creates a Try of a CheckedRunnable.
     *
     * @param runnable A checked runnable
     * @return {@code Success(null)} if no exception occurs, otherwise {@code Failure(throwable)} if an exception occurs
     * calling {@code runnable.run()}.
     */
    static Try<Void> run(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return new Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    /**
     * Creates a Try of a Runnable.
     *
     * @param runnable A runnable
     * @return {@code Success(null)} if no exception occurs, otherwise {@code Failure(throwable)} if an exception occurs
     * calling {@code runnable.run()}.
     */
    static Try<Void> runRunnable(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return run(runnable::run);
    }

    /**
     * Reduces many {@code Try}s into a single {@code Try} by transforming an
     * {@code Iterable<Try<? extends T>>} into a {@code Try<Seq<T>>}. If any of
     * the {@code Try}s are {@link Try.Failure}, then this returns a {@link Try.Failure}.
     *
     * @param values An {@link Iterable} of {@code Try}s
     * @param <T>    type of the Trys
     * @return A {@code Try} of a {@link Seq} of results
     * @throws NullPointerException if {@code values} is null
     */
    static <T> Try<Seq<T>> sequence(Iterable<? extends Try<? extends T>> values) {
        Objects.requireNonNull(values, "values is null");
        Vector<T> vector = Vector.empty();
        for (Try<? extends T> value : values) {
            if (value.isFailure()) {
                return Try.failure(value.getCause());
            }
            vector = vector.append(value.get());
        }
        return Try.success(vector);
    }

    /**
     * Creates a {@link Success} that contains the given {@code value}. Shortcut for {@code new Success<>(value)}.
     *
     * @param value A value.
     * @param <T>   Type of the given {@code value}.
     * @return A new {@code Success}.
     */
    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a {@link Failure} that contains the given {@code exception}. Shortcut for {@code new Failure<>(exception)}.
     *
     * @param exception An exception.
     * @param <T>       Component type of the {@code Try}.
     * @return A new {@code Failure}.
     */
    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    /**
     * Narrows a widened {@code Try<? extends T>} to {@code Try<T>}
     * by performing a type-safe cast. This is eligible because immutable/read-only
     * collections are covariant.
     *
     * @param t   A {@code Try}.
     * @param <T> Component type of the {@code Try}.
     * @return the given {@code t} instance as narrowed type {@code Try<T>}.
     */
    @SuppressWarnings("unchecked")
    static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    /**
     * Shortcut for {@code andThenTry(consumer::accept)}, see {@link #andThenTry(CheckedConsumer)}.
     *
     * @param consumer A consumer
     * @return this {@code Try} if this is a {@code Failure} or the consumer succeeded, otherwise the
     * {@code Failure} of the consumption.
     * @throws NullPointerException if {@code consumer} is null
     */
    default Try<T> andThen(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        return andThenTry(consumer::accept);
    }

    /**
     * Passes the result to the given {@code consumer} if this is a {@code Success}.
     * <p>
     * The main use case is chaining checked functions using method references:
     *
     * <pre>
     * <code>
     * Try.of(() -&gt; 100)
     *    .andThen(i -&gt; System.out.println(i));
     *
     * </code>
     * </pre>
     *
     * @param consumer A checked consumer
     * @return this {@code Try} if this is a {@code Failure} or the consumer succeeded, otherwise the
     * {@code Failure} of the consumption.
     * @throws NullPointerException if {@code consumer} is null
     */
    default Try<T> andThenTry(CheckedConsumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                consumer.accept(get());
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Shortcut for {@code andThenTry(runnable::run)}, see {@link #andThenTry(CheckedRunnable)}.
     *
     * @param runnable A runnable
     * @return this {@code Try} if this is a {@code Failure} or the runnable succeeded, otherwise the
     * {@code Failure} of the run.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andThen(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andThenTry(runnable::run);
    }

    /**
     * Runs the given runnable if this is a {@code Success}, otherwise returns this {@code Failure}.
     * <p>
     * The main use case is chaining runnables using method references:
     *
     * <pre>
     * <code>
     * Try.run(A::methodRef).andThen(B::methodRef).andThen(C::methodRef);
     * </code>
     * </pre>
     *
     * Please note that these lines are semantically the same:
     *
     * <pre>
     * <code>
     * Try.run(this::doStuff)
     *    .andThen(this::doMoreStuff)
     *    .andThen(this::doEvenMoreStuff);
     *
     * Try.run(() -&gt; {
     *     doStuff();
     *     doMoreStuff();
     *     doEvenMoreStuff();
     * });
     * </code>
     * </pre>
     *
     * @param runnable A checked runnable
     * @return this {@code Try} if this is a {@code Failure} or the runnable succeeded, otherwise the
     * {@code Failure} of the run.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andThenTry(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        if (isFailure()) {
            return this;
        } else {
            try {
                runnable.run();
                return this;
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Collects value that is in the domain of the given {@code partialFunction} by mapping the value to type {@code R}.
     *
     * <pre>{@code
     * partialFunction.isDefinedAt(value)
     * }</pre>
     *
     * If the element makes it through that filter, the mapped instance is wrapped in {@code Try}
     *
     * <pre>{@code
     * R newValue = partialFunction.apply(value)
     * }</pre>
     *
     *
     * @param partialFunction A function that is not necessarily defined on value of this try.
     * @param <R> The new value type
     * @return A new {@code Try} instance containing value of type {@code R}
     * @throws NullPointerException if {@code partialFunction} is null
     */
    @SuppressWarnings("unchecked")
    default <R> Try<R> collect(PartialFunction<? super T, ? extends R> partialFunction){
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return filter(partialFunction::isDefinedAt).map(partialFunction::apply);
    }

    /**
     * Returns {@code Success(throwable)} if this is a {@code Failure(throwable)}, otherwise
     * a {@code Failure(new NoSuchElementException("Success.failed()"))} if this is a Success.
     *
     * @return a new Try
     */
    default Try<Throwable> failed() {
        if (isFailure()) {
            return new Success<>(getCause());
        } else {
            return new Failure<>(new NoSuchElementException("Success.failed()"));
        }
    }

    /**
     * Shortcut for {@code filterTry(predicate::test, throwableSupplier)}, see
     * {@link #filterTry(CheckedPredicate, Supplier)}}.
     *
     * @param predicate         A predicate
     * @param throwableSupplier A supplier of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is null
     */
    default Try<T> filter(Predicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(throwableSupplier, "throwableSupplier is null");
        return filterTry(predicate::test, throwableSupplier);
    }

    /**
     * Shortcut for {@code filterTry(predicate::test, errorProvider::apply)}, see
     * {@link #filterTry(CheckedPredicate, CheckedFunction1)}}.
     *
     * @param predicate A predicate
     * @param errorProvider A function that provides some kind of Throwable for T
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is null
     */
    default Try<T> filter(Predicate<? super T> predicate, Function<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return filterTry(predicate::test, errorProvider::apply);
    }

    /**
     * Shortcut for {@code filterTry(predicate::test)}, see {@link #filterTry(CheckedPredicate)}}.
     *
     * @param predicate A predicate
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    default Try<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate::test);
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a Throwable instance provided by the given
     * {@code throwableSupplier}.
     *
     * @param predicate         A checked predicate
     * @param throwableSupplier A supplier of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(throwableSupplier, "throwableSupplier is null");

        if (isFailure()) {
            return this;
        } else {
            try {
                if (predicate.test(get())) {
                    return this;
                } else {
                    return new Failure<>(throwableSupplier.get());
                }
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a Throwable instance provided by the given
     * {@code errorProvider}.
     *
     * @param predicate         A checked predicate
     * @param errorProvider     A provider of a throwable
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate, CheckedFunction1<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return flatMapTry(t -> predicate.test(t) ? this : failure(errorProvider.apply(t)));
    }

    /**
     * Returns {@code this} if this is a Failure or this is a Success and the value satisfies the predicate.
     * <p>
     * Returns a new Failure, if this is a Success and the value does not satisfy the Predicate or an exception
     * occurs testing the predicate. The returned Failure wraps a {@link NoSuchElementException} instance.
     *
     * @param predicate A checked predicate
     * @return a {@code Try} instance
     * @throws NullPointerException if {@code predicate} is null
     */
    default Try<T> filterTry(CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate, () -> new NoSuchElementException("Predicate does not hold for " + get()));
    }

    /**
     * Shortcut for {@code flatMapTry(mapper::apply)}, see {@link #flatMapTry(CheckedFunction1)}.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    default <U> Try<U> flatMap(Function<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return flatMapTry((CheckedFunction1<T, Try<? extends U>>) mapper::apply);
    }

    /**
     * FlatMaps the value of a Success or returns a Failure.
     *
     * @param mapper A mapper
     * @param <U>    The new component type
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> flatMapTry(CheckedFunction1<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            try {
                return (Try<U>) mapper.apply(get());
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Gets the result of this Try if this is a {@code Success} or throws if this is a {@code Failure}.
     * <p>
     * <strong>IMPORTANT! If this is a {@link Failure}, the underlying {@code cause} of type {@link Throwable} is thrown.</strong>
     * <p>
     * The thrown exception is exactly the same as the result of {@link #getCause()}.
     *
     * @return The result of this {@code Try}.
     */
    @Override
    T get();

    /**
     * Gets the cause if this is a Failure or throws if this is a Success.
     *
     * @return The cause if this is a Failure
     * @throws UnsupportedOperationException if this is a Success
     */
    Throwable getCause();

    /**
     * A {@code Try}'s value is computed synchronously.
     *
     * @return false
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * Checks whether this Try has no result, i.e. is a Failure.
     *
     * @return true if this is a Failure, returns false if this is a Success.
     */
    @Override
    boolean isEmpty();

    /**
     * Checks if this is a Failure.
     *
     * @return true, if this is a Failure, otherwise false, if this is a Success
     */
    boolean isFailure();

    /**
     * A {@code Try}'s value is computed eagerly.
     *
     * @return false
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * A {@code Try} is a single-valued.
     *
     * @return {@code true}
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    /**
     * Checks if this is a Success.
     *
     * @return true, if this is a Success, otherwise false, if this is a Failure
     */
    boolean isSuccess();

    @Override
    default Iterator<T> iterator() {
        return isSuccess() ? Iterator.of(get()) : Iterator.empty();
    }

    /**
     * Shortcut for {@code mapTry(mapper::apply)}, see {@link #mapTry(CheckedFunction1)}.
     *
     * @param <U>    The new component type
     * @param mapper A checked function
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @Override
    default <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapTry(mapper::apply);
    }

    /**
     * Maps the cause to a new exception if this is a {@code Failure} or returns this instance if this is a {@code Success}.
     * <p>
     * If none of the given cases matches the cause, the same {@code Failure} is returned.
     *
     * @param cases A not necessarily exhaustive sequence of cases that will be matched against a cause.
     * @return A new {@code Try} if this is a {@code Failure}, otherwise this.
     */
    @GwtIncompatible
    @SuppressWarnings({ "unchecked", "varargs" })
    default Try<T> mapFailure(Match.Case<? extends Throwable, ? extends Throwable>... cases) {
        if (isSuccess()) {
            return this;
        } else {
            final Option<Throwable> x = Match(getCause()).option(cases);
            return x.isEmpty() ? this : failure(x.get());
        }
    }

    /**
     * Runs the given checked function if this is a {@code Success},
     * passing the result of the current expression to it.
     * If this expression is a {@code Failure} then it'll return a new
     * {@code Failure} of type R with the original exception.
     * <p>
     * The main use case is chaining checked functions using method references:
     *
     * <pre>
     * <code>
     * Try.of(() -&gt; 0)
     *    .map(x -&gt; 1 / x); // division by zero
     * </code>
     * </pre>
     *
     * @param <U>    The new component type
     * @param mapper A checked function
     * @return a {@code Try}
     * @throws NullPointerException if {@code mapper} is null
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> mapTry(CheckedFunction1<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        if (isFailure()) {
            return (Failure<U>) this;
        } else {
            try {
                return new Success<>(mapper.apply(get()));
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        }
    }

    /**
     * Consumes the throwable if this is a Failure.
     *
     * @param action An exception consumer
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onFailure(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
        return this;
    }

    /**
     * Consumes the value if this is a Success.
     *
     * @param action A value consumer
     * @return this
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onSuccess(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    default Try<T> orElse(Try<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return isSuccess() ? this : (Try<T>) other;
    }

    @SuppressWarnings("unchecked")
    default Try<T> orElse(Supplier<? extends Try<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isSuccess() ? this : (Try<T>) supplier.get();
    }

    default T getOrElseGet(Function<? super Throwable, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isFailure()) {
            return other.apply(getCause());
        } else {
            return get();
        }
    }

    default void orElseRun(Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
    }

    default <X extends Throwable> T getOrElseThrow(Function<? super Throwable, X> exceptionProvider) throws X {
        Objects.requireNonNull(exceptionProvider, "exceptionProvider is null");
        if (isFailure()) {
            throw exceptionProvider.apply(getCause());
        } else {
            return get();
        }
    }

    /**
     * Folds either the {@code Failure} or the {@code Success} side of the Try value.
     *
     * @param ifFail  maps the left value if this is a {@code Failure}
     * @param f maps the value if this is a {@code Success}
     * @param <X>         type of the folded value
     * @return A value of type X
     */
    default <X> X fold(Function<? super Throwable, ? extends X> ifFail, Function<? super T, ? extends X> f) {
        if (isFailure()) {
            return ifFail.apply(getCause());
        } else {
            return f.apply(get());
        }
    }

    /**
     * Applies the action to the value of a Success or does nothing in the case of a Failure.
     *
     * @param action A Consumer
     * @return this {@code Try}
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default Try<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code Try.of(() -> f.apply((X) getCause())}.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param f         A recovery function taking an exception of type {@code X}
     * @return a {@code Try}
     * @throws NullPointerException if {@code exception} is null or {@code f} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recover(Class<X> exception, Function<? super X, ? extends T> f) {
        Objects.requireNonNull(exception, "exception is null");
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            final Throwable cause = getCause();
            if (exception.isAssignableFrom(cause.getClass())) {
                return Try.of(() -> f.apply((X) cause));
            }
        }
        return this;
    }

    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise tries to recover the exception of the failure with {@code f} <b>which returns Try</b>.
     * If {@link Try#isFailure()} returned by {@code f} function is <code>true</code> it means that recovery cannot take place due to some circumstances.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param f         A recovery function taking an exception of type {@code X} and returning Try as a result of recovery.
     *                  If Try is {@link Try#isSuccess()} then recovery ends up successfully. Otherwise the function was not able to recover.
     * @return a {@code Try}
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recoverWith(Class<X> exception, Function<? super X, Try<? extends T>> f){
        Objects.requireNonNull(exception, "exception is null");
        Objects.requireNonNull(f, "f is null");
        if(isFailure()){
            final Throwable cause = getCause();
            if (exception.isAssignableFrom(cause.getClass())) {
                try {
                    return narrow(f.apply((X) cause));
                } catch (Throwable t) {
                    return new Failure<>(t);
                }
            }
        }
        return this;
    }

    @GwtIncompatible
    default <X extends Throwable> Try<T> recoverWith(Class<X> exception,  Try<? extends T> recovered){
        return (isFailure() && exception.isAssignableFrom(getCause().getClass()))
                ? narrow(recovered)
                : this;
    }


    /**
     * Returns {@code this}, if this is a {@code Success} or this is a {@code Failure} and the cause is not assignable
     * from {@code cause.getClass()}. Otherwise returns a {@code Success} containing the given {@code value}.
     *
     * @param <X>       Exception type
     * @param exception The specific exception type that should be handled
     * @param value     A value that is used in case of a recovery
     * @return a {@code Try}
     * @throws NullPointerException if {@code exception} is null
     */
    @GwtIncompatible
    default <X extends Throwable> Try<T> recover(Class<X> exception, T value) {
        Objects.requireNonNull(exception, "exception is null");
        return (isFailure() && exception.isAssignableFrom(getCause().getClass()))
               ? Try.success(value)
               : this;
    }



    /**
     * Returns {@code this}, if this is a {@code Success}, otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code Try.of(() -> f.apply(throwable))}.
     *
     * @param f A recovery function taking a Throwable
     * @return a {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    default Try<T> recover(Function<? super Throwable, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            return Try.of(() -> f.apply(getCause()));
        } else {
            return this;
        }
    }

    /**
     * Returns {@code this}, if this is a Success, otherwise tries to recover the exception of the failure with {@code f},
     * i.e. calling {@code f.apply(cause.getCause())}. If an error occurs recovering a Failure, then the new Failure is
     * returned.
     *
     * @param f A recovery function taking a Throwable
     * @return a {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    default Try<T> recoverWith(Function<? super Throwable, ? extends Try<? extends T>> f) {
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            try {
                return (Try<T>) f.apply(getCause());
            } catch (Throwable t) {
                return new Failure<>(t);
            }
        } else {
            return this;
        }
    }

    /**
     * Converts this {@code Try} to an {@link Either}.
     *
     * @return A new {@code Either}
     */
    default Either<Throwable, T> toEither() {
        if (isFailure()) {
            return Either.left(getCause());
        } else {
            return Either.right(get());
        }
    }

    /**
     * Transforms this {@code Try}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Try<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Provides try's finally behavior no matter what the result of the operation is.
     *
     * @param runnable A runnable
     * @return this {@code Try}.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinally(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andFinallyTry(runnable::run);
    }

    /**
     * Provides try's finally behavior no matter what the result of the operation is.
     *
     * @param runnable A runnable
     * @return this {@code Try}.
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinallyTry(CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return this;
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    /**
     * A succeeded Try.
     *
     * @param <T> component type of this Success
     * @author Daniel Dietrich
     */
    final class Success<T> implements Try<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final T value;

        /**
         * Constructs a Success.
         *
         * @param value The value of this Success.
         */
        private Success(T value) {
            this.value = value;
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public Throwable getCause() {
            throw new UnsupportedOperationException("getCause on Success");
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Success && Objects.equals(value, ((Success<?>) obj).value));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(value);
        }

        @Override
        public String stringPrefix() {
            return "Success";
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + value + ")";
        }
    }

    /**
     * A failed Try.
     *
     * @param <T> component type of this Failure
     * @author Daniel Dietrich
     */
    final class Failure<T> implements Try<T>, Serializable {

        private static final long serialVersionUID = 1L;

        private final Throwable cause;

        /**
         * Constructs a Failure.
         *
         * @param cause A cause of type Throwable, may not be null.
         * @throws NullPointerException if {@code cause} is null
         * @throws Throwable            if the given {@code cause} is fatal, i.e. non-recoverable
         */
        private Failure(Throwable cause) {
            Objects.requireNonNull(cause, "cause is null");
            if (isFatal(cause)) {
                sneakyThrow(cause);
            }
            this.cause = cause;
        }

        @Override
        public T get() {
            return sneakyThrow(cause);
        }

        @Override
        public Throwable getCause() {
            return cause;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj == this) || (obj instanceof Failure && Arrays.deepEquals(cause.getStackTrace(), ((Failure<?>) obj).cause.getStackTrace()));
        }

        @Override
        public String stringPrefix() {
            return "Failure";
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cause.getStackTrace());
        }

        @Override
        public String toString() {
            return stringPrefix() + "(" + cause + ")";
        }

    }

    // -- try with resources

    /**
     * Creates a {@code Try}-with-resources builder that operates on one {@link AutoCloseable} resource.
     *
     * @param t1Supplier The supplier of the first resource.
     * @param <T1> Type of the 1st resource.
     * @return a new {@link WithResources1} instance.
     */
    static <T1 extends AutoCloseable> WithResources1<T1> withResources(CheckedFunction0<? extends T1> t1Supplier) {
        return new WithResources1<>(t1Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on two {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @return a new {@link WithResources2} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable> WithResources2<T1, T2> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier) {
        return new WithResources2<>(t1Supplier, t2Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on three {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @return a new {@link WithResources3} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable> WithResources3<T1, T2, T3> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier) {
        return new WithResources3<>(t1Supplier, t2Supplier, t3Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on four {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @return a new {@link WithResources4} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable> WithResources4<T1, T2, T3, T4> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier) {
        return new WithResources4<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on five {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @return a new {@link WithResources5} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable> WithResources5<T1, T2, T3, T4, T5> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier) {
        return new WithResources5<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on six {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @return a new {@link WithResources6} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable> WithResources6<T1, T2, T3, T4, T5, T6> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier) {
        return new WithResources6<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on seven {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param t7Supplier The supplier of the 7th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @return a new {@link WithResources7} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable> WithResources7<T1, T2, T3, T4, T5, T6, T7> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier, CheckedFunction0<? extends T7> t7Supplier) {
        return new WithResources7<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier, t7Supplier);
    }

    /**
     * Creates a {@code Try}-with-resources builder that operates on eight {@link AutoCloseable} resources.
     *
     * @param t1Supplier The supplier of the 1st resource.
     * @param t2Supplier The supplier of the 2nd resource.
     * @param t3Supplier The supplier of the 3rd resource.
     * @param t4Supplier The supplier of the 4th resource.
     * @param t5Supplier The supplier of the 5th resource.
     * @param t6Supplier The supplier of the 6th resource.
     * @param t7Supplier The supplier of the 7th resource.
     * @param t8Supplier The supplier of the 8th resource.
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @param <T8> Type of the 8th resource.
     * @return a new {@link WithResources8} instance.
     */
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable, T8 extends AutoCloseable> WithResources8<T1, T2, T3, T4, T5, T6, T7, T8> withResources(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier, CheckedFunction0<? extends T7> t7Supplier, CheckedFunction0<? extends T8> t8Supplier) {
        return new WithResources8<>(t1Supplier, t2Supplier, t3Supplier, t4Supplier, t5Supplier, t6Supplier, t7Supplier, t8Supplier);
    }

    /**
     * A {@code Try}-with-resources builder that operates on one {@link AutoCloseable} resource.
     *
     * @param <T1> Type of the 1st resource.
     */
    final class WithResources1<T1 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;

        private WithResources1(CheckedFunction0<? extends T1> t1Supplier) {
            this.t1Supplier = t1Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes one {@code AutoClosable} resource.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction1<? super T1, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply()) {
                    return f.apply(t1);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on two {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     */
    final class WithResources2<T1 extends AutoCloseable, T2 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;

        private WithResources2(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes two {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction2<? super T1, ? super T2, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply()) {
                    return f.apply(t1, t2);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on three {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     */
    final class WithResources3<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;

        private WithResources3(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes three {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction3<? super T1, ? super T2, ? super T3, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply()) {
                    return f.apply(t1, t2, t3);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on four {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     */
    final class WithResources4<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;
        private final CheckedFunction0<? extends T4> t4Supplier;

        private WithResources4(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes four {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply(); T4 t4 = t4Supplier.apply()) {
                    return f.apply(t1, t2, t3, t4);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on five {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     */
    final class WithResources5<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;
        private final CheckedFunction0<? extends T4> t4Supplier;
        private final CheckedFunction0<? extends T5> t5Supplier;

        private WithResources5(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes five {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply(); T4 t4 = t4Supplier.apply(); T5 t5 = t5Supplier.apply()) {
                    return f.apply(t1, t2, t3, t4, t5);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on six {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     */
    final class WithResources6<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;
        private final CheckedFunction0<? extends T4> t4Supplier;
        private final CheckedFunction0<? extends T5> t5Supplier;
        private final CheckedFunction0<? extends T6> t6Supplier;

        private WithResources6(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes six {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply(); T4 t4 = t4Supplier.apply(); T5 t5 = t5Supplier.apply(); T6 t6 = t6Supplier.apply()) {
                    return f.apply(t1, t2, t3, t4, t5, t6);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on seven {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     */
    final class WithResources7<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;
        private final CheckedFunction0<? extends T4> t4Supplier;
        private final CheckedFunction0<? extends T5> t5Supplier;
        private final CheckedFunction0<? extends T6> t6Supplier;
        private final CheckedFunction0<? extends T7> t7Supplier;

        private WithResources7(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier, CheckedFunction0<? extends T7> t7Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
            this.t7Supplier = t7Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes seven {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
        public <R> Try<R> of(CheckedFunction7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply(); T4 t4 = t4Supplier.apply(); T5 t5 = t5Supplier.apply(); T6 t6 = t6Supplier.apply(); T7 t7 = t7Supplier.apply()) {
                    return f.apply(t1, t2, t3, t4, t5, t6, t7);
                }
            });
        }
    }

    /**
     * A {@code Try}-with-resources builder that operates on eight {@link AutoCloseable} resources.
     *
     * @param <T1> Type of the 1st resource.
     * @param <T2> Type of the 2nd resource.
     * @param <T3> Type of the 3rd resource.
     * @param <T4> Type of the 4th resource.
     * @param <T5> Type of the 5th resource.
     * @param <T6> Type of the 6th resource.
     * @param <T7> Type of the 7th resource.
     * @param <T8> Type of the 8th resource.
     */
    final class WithResources8<T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable, T8 extends AutoCloseable> {

        private final CheckedFunction0<? extends T1> t1Supplier;
        private final CheckedFunction0<? extends T2> t2Supplier;
        private final CheckedFunction0<? extends T3> t3Supplier;
        private final CheckedFunction0<? extends T4> t4Supplier;
        private final CheckedFunction0<? extends T5> t5Supplier;
        private final CheckedFunction0<? extends T6> t6Supplier;
        private final CheckedFunction0<? extends T7> t7Supplier;
        private final CheckedFunction0<? extends T8> t8Supplier;

        private WithResources8(CheckedFunction0<? extends T1> t1Supplier, CheckedFunction0<? extends T2> t2Supplier, CheckedFunction0<? extends T3> t3Supplier, CheckedFunction0<? extends T4> t4Supplier, CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier, CheckedFunction0<? extends T7> t7Supplier, CheckedFunction0<? extends T8> t8Supplier) {
            this.t1Supplier = t1Supplier;
            this.t2Supplier = t2Supplier;
            this.t3Supplier = t3Supplier;
            this.t4Supplier = t4Supplier;
            this.t5Supplier = t5Supplier;
            this.t6Supplier = t6Supplier;
            this.t7Supplier = t7Supplier;
            this.t8Supplier = t8Supplier;
        }

        /**
         * Wraps the result of a computation that may fail in a {@code Try}.
         *
         * @param f A computation that takes eight {@code AutoClosable} resources.
         * @param <R> Result type of the computation.
         * @return A new {@code Try} instance.
         */
        @SuppressWarnings("try"/* https://bugs.openjdk.java.net/browse/JDK-8155591 */)
        public <R> Try<R> of(CheckedFunction8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
            return Try.of(() -> {
                try (T1 t1 = t1Supplier.apply(); T2 t2 = t2Supplier.apply(); T3 t3 = t3Supplier.apply(); T4 t4 = t4Supplier.apply(); T5 t5 = t5Supplier.apply(); T6 t6 = t6Supplier.apply(); T7 t7 = t7Supplier.apply(); T8 t8 = t8Supplier.apply()) {
                    return f.apply(t1, t2, t3, t4, t5, t6, t7, t8);
                }
            });
        }
    }
}

interface TryModule {

    static boolean isFatal(Throwable throwable) {
        return throwable instanceof InterruptedException
                || throwable instanceof LinkageError
                || throwable instanceof ThreadDeath
                || throwable instanceof VirtualMachineError;
    }

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

}
