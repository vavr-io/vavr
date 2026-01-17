/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.control;

import io.vavr.*;
import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
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
import org.jspecify.annotations.NonNull;

import static io.vavr.API.Match;
import static io.vavr.control.TryModule.isFatal;
import static io.vavr.control.TryModule.sneakyThrow;

/**
 * A control structure that allows writing safe code without explicitly managing try-catch blocks for exceptions.
 * <p>
 * The following exceptions are considered fatal or non-recoverable:
 * <ul>
 *     <li>{@linkplain InterruptedException}</li>
 *     <li>{@linkplain LinkageError}</li>
 *     <li>{@linkplain ThreadDeath}</li>
 *     <li>{@linkplain VirtualMachineError}, including {@linkplain OutOfMemoryError} and {@linkplain StackOverflowError}</li>
 * </ul>
 * <p>
 * <strong>Note:</strong> Methods such as {@code get()} may re-throw exceptions without declaring them. When used
 * within a {@link java.lang.reflect.InvocationHandler} of a dynamic proxy, such exceptions will be wrapped in
 * {@link java.lang.reflect.UndeclaredThrowableException}. See 
 * <a href="https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html">
 * Dynamic Proxy Classes</a> for more details.
 *
 * @param <T> the type of the value in case of success
 * @author Daniel
 */
public interface Try<T> extends Value<T>, Serializable {

    /**
     * The serial version UID for serialization.
     */
    long serialVersionUID = 1L;

    /**
     * Creates a {@link Try} instance from a {@link CheckedFunction0}.
     * <p>
     * If the supplier executes without throwing an exception, a {@link Success} containing the result is returned.
     * If an exception occurs during execution, a {@link Failure} wrapping the thrown exception is returned.
     *
     * @param supplier the checked supplier to execute
     * @param <T>      the type of the value returned by the supplier
     * @return a {@link Success} with the supplier's result, or a {@link Failure} if an exception is thrown
     * @throws NullPointerException if {@code supplier} is {@code null}
     */
    static <T> Try<T> of(@NonNull CheckedFunction0<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        try {
            return new Success<>(supplier.apply());
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }


    /**
     * Creates a {@link Try} instance from a {@link Supplier}.
     * <p>
     * If the supplier executes without throwing an exception, a {@link Success} containing the result is returned.
     * If an exception occurs during execution, a {@link Failure} wrapping the thrown exception is returned.
     *
     * @param supplier the supplier to execute
     * @param <T>      the type of the value returned by the supplier
     * @return a {@link Success} with the supplier's result, or a {@link Failure} if an exception is thrown
     * @throws NullPointerException if {@code supplier} is {@code null}
     */
    static <T> Try<T> ofSupplier(@NonNull Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return of(supplier::get);
    }

    /**
     * Creates a {@link Try} instance from a {@link Callable}.
     * <p>
     * If the callable executes without throwing an exception, a {@link Success} containing the result is returned.
     * If an exception occurs during execution, a {@link Failure} wrapping the thrown exception is returned.
     *
     * @param callable the callable to execute
     * @param <T>      the type of the value returned by the callable
     * @return a {@link Success} with the callable's result, or a {@link Failure} if an exception is thrown
     * @throws NullPointerException if {@code callable} is {@code null}
     */
    static <T> Try<T> ofCallable(@NonNull Callable<? extends T> callable) {
        Objects.requireNonNull(callable, "callable is null");
        return of(callable::call);
    }

    /**
     * Creates a {@link Try} instance from a {@link CheckedRunnable}.
     * <p>
     * If the runnable executes without throwing an exception, a {@link Success} containing {@code null} (representing
     * the absence of a value) is returned. If an exception occurs during execution, a {@link Failure} wrapping the
     * thrown exception is returned.
     *
     * @param runnable the checked runnable to execute
     * @return a {@link Success} with {@code null} if the runnable completes successfully, or a {@link Failure} if an exception is thrown
     * @throws NullPointerException if {@code runnable} is {@code null}
     */
    static Try<Void> run(@NonNull CheckedRunnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        try {
            runnable.run();
            return new Success<>(null); // null represents the absence of an value, i.e. Void
        } catch (Throwable t) {
            return new Failure<>(t);
        }
    }

    /**
     * Creates a {@link Try} instance from a {@link Runnable}.
     * <p>
     * If the runnable executes without throwing an exception, a {@link Success} containing {@code null} (representing
     * the absence of a value) is returned. If an exception occurs during execution, a {@link Failure} wrapping the
     * thrown exception is returned.
     *
     * @param runnable the runnable to execute
     * @return a {@link Success} with {@code null} if the runnable completes successfully, or a {@link Failure} if an exception is thrown
     * @throws NullPointerException if {@code runnable} is {@code null}
     */
    static Try<Void> runRunnable(@NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return run(runnable::run);
    }


    /**
     * Transforms an {@link Iterable} of {@link Try} instances into a single {@link Try} containing a {@link Seq}
     * of all successful results. 
     * <p>
     * If any element in the input iterable is a {@link Try.Failure}, the resulting {@link Try} will also be a
     * {@link Try.Failure}, containing the first encountered failure's cause.
     *
     * @param values an {@link Iterable} of {@code Try} instances
     * @param <T>    the type of values contained in the {@code Try}s
     * @return a {@link Try} containing a {@link Seq} of all successful results, or a {@link Try.Failure} if any input is a failure
     * @throws NullPointerException if {@code values} is {@code null}
     */
    static <T> Try<Seq<T>> sequence(@NonNull Iterable<? extends Try<? extends T>> values) {
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
     * Transforms an {@link Iterable} of values into a single {@link Try} containing a {@link Seq} of mapped results.
     * <p>
     * Each value in the input iterable is mapped using the provided {@code mapper} function, which produces a
     * {@link Try}. If all mappings succeed, a {@link Success} containing a {@link Seq} of results is returned.
     * If any mapping results in a {@link Try.Failure}, the first encountered failure is returned.
     *
     * @param values an {@link Iterable} of input values
     * @param mapper a function mapping each input value to a {@link Try} of the mapped result
     * @param <T>    the type of the input values
     * @param <U>    the type of the mapped results
     * @return a {@link Try} containing a {@link Seq} of all mapped results, or a {@link Try.Failure} if any mapping fails
     * @throws NullPointerException if {@code values} or {@code mapper} is {@code null}
     */
    static <T, U> Try<Seq<U>> traverse(@NonNull Iterable<? extends T> values, @NonNull Function<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(values, "values is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return sequence(Iterator.ofAll(values).map(mapper));
    }

    /**
     * Creates a {@link Success} containing the given {@code value}.
     * <p>
     * This is a convenience method equivalent to {@code new Success<>(value)}.
     *
     * @param value the value to wrap in a {@link Success}
     * @param <T>   the type of the value
     * @return a new {@link Success} containing {@code value}
     */
    static <T> Try<T> success(T value) {
        return new Success<>(value);
    }

    /**
     * Creates a {@link Failure} containing the given {@code exception}.
     * <p>
     * This is a convenience method equivalent to {@code new Failure<>(exception)}.
     *
     * @param exception the exception to wrap in a {@link Failure}
     * @param <T>       the component type of the {@code Try}
     * @return a new {@link Failure} containing {@code exception}
     */
    static <T> Try<T> failure(Throwable exception) {
        return new Failure<>(exception);
    }

    /**
     * Narrows a {@code Try<? extends T>} to {@code Try<T>} using a type-safe cast.
     * <p>
     * This is safe because {@code Try} is immutable and its contents are read-only, allowing covariance.
     *
     * @param t   the {@code Try} instance to narrow
     * @param <T> the component type of the {@code Try}
     * @return the given {@code Try} instance as {@code Try<T>}
     */
    @SuppressWarnings("unchecked")
    static <T> Try<T> narrow(Try<? extends T> t) {
        return (Try<T>) t;
    }

    /**
     * Performs the given {@link Consumer} on the value of this {@code Try} if it is a {@link Success}.
     * <p>
     * This is a shortcut for {@code andThenTry(consumer::accept)}. If this {@code Try} is a {@link Failure}, 
     * it is returned unchanged. If the consumer throws an exception, a {@link Failure} is returned.
     *
     * @param consumer the consumer to execute on the value
     * @return this {@code Try} if it is a {@link Failure} or the consumer succeeds, otherwise a {@link Failure} of the consumer
     * @throws NullPointerException if {@code consumer} is {@code null}
     * @see #andThenTry(CheckedConsumer)
     */
    default Try<T> andThen(@NonNull Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        return andThenTry(consumer::accept);
    }


    /**
     * Passes the result of this {@code Try} to the given {@link CheckedConsumer} if this is a {@link Success}.
     * <p>
     * This allows chaining of operations that may throw checked exceptions. If this {@code Try} is a {@link Failure}, 
     * it is returned unchanged. If the consumer throws an exception, a {@link Failure} containing that exception is returned.
     * <p>
     * Example usage:
     * <pre>{@code
     * Try.of(() -> 100)
     *    .andThenTry(i -> System.out.println(i));
     * }</pre>
     *
     * @param consumer the checked consumer to execute on the value
     * @return this {@code Try} if it is a {@link Failure} or the consumer succeeds, otherwise a {@link Failure} of the consumer
     * @throws NullPointerException if {@code consumer} is {@code null}
     */
    default Try<T> andThenTry(@NonNull CheckedConsumer<? super T> consumer) {
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
     * Performs the given {@link Runnable} if this {@code Try} is a {@link Success}.
     * <p>
     * This is a shortcut for {@code andThenTry(runnable::run)}. If this {@code Try} is a {@link Failure}, it is returned unchanged.
     * If the runnable throws an exception, a {@link Failure} containing that exception is returned.
     *
     * @param runnable the runnable to execute
     * @return this {@code Try} if it is a {@link Failure} or the runnable succeeds, otherwise a {@link Failure} of the runnable
     * @throws NullPointerException if {@code runnable} is {@code null}
     * @see #andThenTry(CheckedRunnable)
     */
    default Try<T> andThen(@NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andThenTry(runnable::run);
    }


    /**
     * Executes the given {@link CheckedRunnable} if this {@code Try} is a {@link Success}; 
     * otherwise, returns this {@code Failure}.
     * <p>
     * This allows chaining of runnables that may throw checked exceptions. If the runnable throws an exception,
     * a {@link Failure} containing that exception is returned.
     * <p>
     * Example usage with method references:
     * <pre>{@code
     * Try.run(A::methodRef)
     *    .andThen(B::methodRef)
     *    .andThen(C::methodRef);
     * }</pre>
     *
     * The following two forms are semantically equivalent:
     * <pre>{@code
     * Try.run(this::doStuff)
     *    .andThen(this::doMoreStuff)
     *    .andThen(this::doEvenMoreStuff);
     *
     * Try.run(() -> {
     *     doStuff();
     *     doMoreStuff();
     *     doEvenMoreStuff();
     * });
     * }</pre>
     *
     * @param runnable the checked runnable to execute
     * @return this {@code Try} if it is a {@link Failure} or the runnable succeeds, otherwise a {@link Failure} of the runnable
     * @throws NullPointerException if {@code runnable} is {@code null}
     */
    default Try<T> andThenTry(@NonNull CheckedRunnable runnable) {
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
     * Transforms the value of this {@code Try} using the given {@link PartialFunction} if it is defined at the value.
     * <p>
     * The {@code partialFunction} is first tested with {@link PartialFunction#isDefinedAt(Object)}. If it returns
     * {@code true}, the value is mapped using {@link PartialFunction#apply(Object)} and wrapped in a new {@code Try}.
     * If the function is not defined at the value or this {@code Try} is a {@link Failure}, the result is a {@link Failure}.
     * <p>
     * Example:
     * <pre>{@code
     * PartialFunction<Integer, String> pf = ...;
     * Try.of(() -> 42)
     *    .collect(pf); // maps the value if pf.isDefinedAt(42) is true
     * }</pre>
     *
     * @param partialFunction a function that may not be defined for all input values
     * @param <R>             the type of the mapped result
     * @return a new {@code Try} containing the mapped value if defined, or a {@link Failure}
     * @throws NullPointerException if {@code partialFunction} is {@code null}
     */
    @SuppressWarnings("unchecked")
    default <R> Try<R> collect(@NonNull PartialFunction<? super T, ? extends R> partialFunction){
        Objects.requireNonNull(partialFunction, "partialFunction is null");
        return filter(partialFunction::isDefinedAt).map(partialFunction::apply);
    }

    /**
     * Returns a {@code Success} containing the throwable if this {@code Try} is a {@link Failure}.
     * <p>
     * If this {@code Try} is a {@link Success}, a {@code Failure} containing a {@link NoSuchElementException} is returned.
     *
     * @return a {@code Try<Throwable>} representing the throwable of this {@link Failure}, or a {@link Failure} if this is a {@link Success}
     */
    default Try<Throwable> failed() {
        if (isFailure()) {
            return new Success<>(getCause());
        } else {
            return new Failure<>(new NoSuchElementException("Success.failed()"));
        }
    }

    /**
     * Returns a {@code Try} if the given {@link Predicate} evaluates to {@code true}, otherwise returns a {@link Failure}
     * created by the given {@link Supplier} of {@link Throwable}.
     * <p>
     * This is a shortcut for {@link #filterTry(CheckedPredicate, Supplier)}.
     *
     * @param predicate         the predicate to test the value
     * @param throwableSupplier a supplier providing a throwable if the predicate fails
     * @return this {@code Try} if the predicate passes, otherwise a {@link Failure} from the throwable supplier
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is {@code null}
     */
    default Try<T> filter(@NonNull Predicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(throwableSupplier, "throwableSupplier is null");
        return filterTry(predicate::test, throwableSupplier);
    }

    /**
     * Returns a {@code Try} if the given {@link Predicate} evaluates to {@code true}, otherwise returns a {@link Failure}
     * created by applying the given function to the value.
     * <p>
     * This is a shortcut for {@link #filterTry(CheckedPredicate, CheckedFunction1)}.
     *
     * @param predicate     the predicate to test the value
     * @param errorProvider a function providing a throwable if the predicate fails
     * @return this {@code Try} if the predicate passes, otherwise a {@link Failure} from the error provider
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is {@code null}
     */
    default Try<T> filter(@NonNull Predicate<? super T> predicate, Function<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return filterTry(predicate::test, errorProvider::apply);
    }

    /**
     * Returns a {@code Try} if the given {@link Predicate} evaluates to {@code true}, otherwise returns a {@link Failure}.
     * <p>
     * This is a shortcut for {@link #filterTry(CheckedPredicate)}.
     *
     * @param predicate the predicate to test the value
     * @return this {@code Try} if the predicate passes, otherwise a {@link Failure}
     * @throws NullPointerException if {@code predicate} is {@code null}
     */
    default Try<T> filter(@NonNull Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate::test);
    }

    /**
     * Returns {@code this} if this {@code Try} is a {@link Failure} or if it is a {@link Success} and the value
     * satisfies the given checked predicate.
     * <p>
     * If this is a {@link Success} and the predicate returns {@code false}, or if evaluating the predicate
     * throws an exception, a new {@link Failure} is returned. The returned failure wraps a {@link Throwable}
     * provided by the given {@code throwableSupplier}.
     *
     * @param predicate         the checked predicate to test the value
     * @param throwableSupplier a supplier providing the {@link Throwable} for the failure if the predicate does not hold
     * @return this {@code Try} if it is a {@link Failure} or the predicate passes, otherwise a {@link Failure} from the supplier
     * @throws NullPointerException if {@code predicate} or {@code throwableSupplier} is {@code null}
     */
    default Try<T> filterTry(@NonNull CheckedPredicate<? super T> predicate, Supplier<? extends Throwable> throwableSupplier) {
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
     * Returns {@code this} if this {@code Try} is a {@link Failure} or if it is a {@link Success} and the value
     * satisfies the given checked predicate.
     * <p>
     * If the predicate does not hold or throws an exception, a new {@link Failure} is returned. The returned
     * failure wraps a {@link Throwable} provided by the given {@code errorProvider} function.
     *
     * @param predicate     the checked predicate to test the value
     * @param errorProvider a function that provides a {@link Throwable} if the predicate fails
     * @return this {@code Try} if it is a {@link Failure} or the predicate passes, otherwise a {@link Failure} from the error provider
     * @throws NullPointerException if {@code predicate} or {@code errorProvider} is {@code null}
     */
    default Try<T> filterTry(@NonNull CheckedPredicate<? super T> predicate, CheckedFunction1<? super T, ? extends Throwable> errorProvider) {
        Objects.requireNonNull(predicate, "predicate is null");
        Objects.requireNonNull(errorProvider, "errorProvider is null");
        return flatMapTry(t -> predicate.test(t) ? this : failure(errorProvider.apply(t)));
    }

    /**
     * Returns {@code this} if this {@code Try} is a {@link Failure} or if it is a {@link Success} and the value
     * satisfies the given checked predicate.
     * <p>
     * If the predicate does not hold or throws an exception, a new {@link Failure} wrapping a
     * {@link NoSuchElementException} is returned.
     *
     * @param predicate the checked predicate to test the value
     * @return this {@code Try} if it is a {@link Failure} or the predicate passes, otherwise a {@link Failure} with a {@link NoSuchElementException}
     * @throws NullPointerException if {@code predicate} is {@code null}
     */
    default Try<T> filterTry(@NonNull CheckedPredicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterTry(predicate, () -> new NoSuchElementException("Predicate does not hold for " + get()));
    }

    /**
     * Transforms the value of this {@code Try} using the given {@link Function} if it is a {@link Success},
     * or returns this {@link Failure}.
     * <p>
     * This is a shortcut for {@link #flatMapTry(CheckedFunction1)}.
     *
     * @param mapper a function mapping the value to another {@code Try}
     * @param <U>    the type of the resulting {@code Try}
     * @return a new {@code Try} resulting from applying the mapper, or this {@code Failure} if this is a failure
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    default <U> Try<U> flatMap(@NonNull Function<? super T, ? extends Try<? extends U>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return flatMapTry((CheckedFunction1<T, Try<? extends U>>) mapper::apply);
    }

    /**
     * Transforms the value of this {@code Try} using the given {@link CheckedFunction1} if it is a {@link Success},
     * or returns this {@link Failure}.
     * <p>
     * If applying the mapper throws an exception, a {@link Failure} containing the exception is returned.
     *
     * @param mapper a checked function mapping the value to another {@code Try}
     * @param <U>    the type of the resulting {@code Try}
     * @return a new {@code Try} resulting from applying the mapper, or this {@code Failure} if this is a failure
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> flatMapTry(@NonNull CheckedFunction1<? super T, ? extends Try<? extends U>> mapper) {
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
     * Returns the value of this {@code Try} if it is a {@link Success}, or throws the underlying exception if it is a {@link Failure}.
     * <p>
     * <strong>Important:</strong> If this {@code Try} is a {@link Failure}, the exception thrown is exactly the
     * {@link #getCause()} of this {@code Failure}.
     *
     * @return the value contained in this {@code Success}
     * throws Throwable the underlying cause sneakily if this is a {@link Failure}
     */
    @Override
    T get();

    /**
     * Returns the cause of failure if this {@code Try} is a {@link Failure}.
     *
     * @return the throwable cause of this {@link Failure}
     * @throws UnsupportedOperationException if this {@code Try} is a {@link Success}
     */
    Throwable getCause();

    /**
     * Indicates whether this {@code Try} is computed asynchronously.
     *
     * @return {@code false} for a regular {@code Try}, since the value is computed synchronously
     */
    @Override
    default boolean isAsync() {
        return false;
    }

    /**
     * Checks whether this {@code Try} contains no value, i.e., it is a {@link Failure}.
     *
     * @return {@code true} if this is a {@link Failure}, {@code false} if this is a {@link Success}
     */
    @Override
    boolean isEmpty();

    /**
     * Checks whether this {@code Try} is a {@link Failure}.
     *
     * @return {@code true} if this is a {@link Failure}, {@code false} if this is a {@link Success}
     */
    boolean isFailure();

    /**
     * Indicates whether this {@code Try} is evaluated lazily.
     *
     * @return {@code false} for a standard {@code Try}, as its value is computed eagerly
     */
    @Override
    default boolean isLazy() {
        return false;
    }

    /**
     * Indicates whether this {@code Try} represents a single value.
     *
     * @return {@code true}, since a {@code Try} always contains at most one value
     */
    @Override
    default boolean isSingleValued() {
        return true;
    }

    /**
     * Checks whether this {@code Try} is a {@link Success}.
     *
     * @return {@code true} if this is a {@link Success}, {@code false} if this is a {@link Failure}
     */
    boolean isSuccess();

    @Override
    default @NonNull Iterator<T> iterator() {
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
    default <U> Try<U> map(@NonNull Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return mapTry(mapper::apply);
    }

    @Override
    default <U> Try<U> mapTo(U value) {
        return map(ignored -> value);
    }

    @Override
    default Try<Void> mapToVoid() {
        return map(ignored -> null);
    }

    /**
     * Transforms the cause of this {@link Failure} using the given sequence of match cases.
     * <p>
     * If this {@code Try} is a {@link Success}, it is returned unchanged. If this is a {@link Failure}, the
     * cause is matched against the provided {@code cases}. If a match is found, a new {@link Failure} containing
     * the mapped exception is returned. If none of the cases match, the original {@link Failure} is returned.
     *
     * @param cases a possibly non-exhaustive sequence of match cases to handle the cause
     * @return a new {@code Try} with a mapped cause if a match is found, otherwise this {@code Try}
     */
    @GwtIncompatible
    @SuppressWarnings({ "unchecked", "varargs" })
    default Try<T> mapFailure(Match.@NonNull Case<? extends Throwable, ? extends Throwable> @NonNull ... cases) {
        if (isSuccess()) {
            return this;
        } else {
            final Option<Throwable> x = Match(getCause()).option(cases);
            return x.isEmpty() ? this : failure(x.get());
        }
    }

    /**
     * Applies the given checked function to the value of this {@link Success}, or returns this {@link Failure} unchanged.
     * <p>
     * If the function throws an exception, a new {@link Failure} containing the exception is returned.
     * This allows chaining of computations that may throw checked exceptions.
     * <p>
     * Example:
     * <pre>{@code
     * Try.of(() -> 0)
     *    .mapTry(x -> 1 / x); // division by zero will result in a Failure
     * }</pre>
     *
     * @param <U>    the type of the result
     * @param mapper a checked function to apply to the value
     * @return a new {@code Try} containing the mapped value if this is a {@link Success}, otherwise this {@link Failure}
     * @throws NullPointerException if {@code mapper} is {@code null}
     */
    @SuppressWarnings("unchecked")
    default <U> Try<U> mapTry(@NonNull CheckedFunction1<? super T, ? extends U> mapper) {
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
     * Performs the given action if this {@link Try} is a {@link Failure}.
     * <p>
     * Example:
     * <pre>{@code
     * // does not print anything
     * Try.success(1).onFailure(System.out::println);
     *
     * // prints "java.lang.Error"
     * Try.failure(new Error()).onFailure(System.out::println);
     * }</pre>
     *
     * @param action a consumer of the throwable cause
     * @return this {@code Try} instance
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onFailure(@NonNull Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
        return this;
    }

    /**
     * Performs the given action if this {@link Try} is a {@link Failure} and the cause is an instance of the specified type.
     * <p>
     * Example:
     * <pre>{@code
     * // does not print anything
     * Try.success(1).onFailure(Error.class, System.out::println);
     *
     * // prints "Error"
     * Try.failure(new Error())
     *    .onFailure(RuntimeException.class, x -> System.out.println("Runtime exception"))
     *    .onFailure(Error.class, x -> System.out.println("Error"));
     * }</pre>
     *
     * @param exceptionType the type of exception to handle
     * @param action        a consumer for the exception
     * @param <X>           the type of exception that should be handled
     * @return this {@code Try} instance
     * @throws NullPointerException if {@code exceptionType} or {@code action} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> onFailure(@NonNull Class<X> exceptionType, @NonNull Consumer<? super X> action) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(action, "action is null");
        if (isFailure() && exceptionType.isAssignableFrom(getCause().getClass())) {
            action.accept((X) getCause());
        }
        return this;
    }

    /**
     * Performs the given action if this {@link Try} is a {@link Success}.
     * <p>
     * Example:
     * <pre>{@code
     * // prints "1"
     * Try.success(1).onSuccess(System.out::println);
     *
     * // does not print anything
     * Try.failure(new Error()).onSuccess(System.out::println);
     * }</pre>
     *
     * @param action a consumer of the value
     * @return this {@code Try} instance
     * @throws NullPointerException if {@code action} is null
     */
    default Try<T> onSuccess(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }

    /**
     * Returns this {@code Try} if it is a {@link Success}, or the given alternative {@code Try} if this is a {@link Failure}.
     *
     * @param other the alternative {@code Try} to return if this is a {@link Failure}
     * @return this {@code Try} if success, otherwise {@code other}
     * @throws NullPointerException if {@code other} is null
     */
    @SuppressWarnings("unchecked")
    default Try<T> orElse(@NonNull Try<? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        return isSuccess() ? this : (Try<T>) other;
    }

    /**
     * Returns this {@code Try} if it is a {@link Success}, or a {@code Try} supplied by the given {@link Supplier} if this is a {@link Failure}.
     * <p>
     * The supplier is only invoked if this {@code Try} is a {@link Failure}.
     *
     * @param supplier a supplier of an alternative {@code Try}
     * @return this {@code Try} if success, otherwise the {@code Try} returned by {@code supplier}
     * @throws NullPointerException if {@code supplier} is null
     */
    @SuppressWarnings("unchecked")
    default Try<T> orElse(@NonNull Supplier<? extends Try<? extends T>> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return isSuccess() ? this : (Try<T>) supplier.get();
    }

    /**
     * Returns the value of this {@code Success}, or applies the given function to the cause if this is a {@link Failure}.
     *
     * @param other a function mapping the throwable cause to a replacement value
     * @return the value of this {@link Success}, or the result of applying {@code other} to the failure cause
     * @throws NullPointerException if {@code other} is null
     */
    default T getOrElseGet(@NonNull Function<? super Throwable, ? extends T> other) {
        Objects.requireNonNull(other, "other is null");
        if (isFailure()) {
            return other.apply(getCause());
        } else {
            return get();
        }
    }

    /**
     * Executes the given action if this {@code Try} is a {@link Failure}.
     * <p>
     * This method is typically used for side-effecting handling of failure cases.
     *
     * @param action a consumer of the throwable cause
     * @throws NullPointerException if {@code action} is null
     */
    default void orElseRun(@NonNull Consumer<? super Throwable> action) {
        Objects.requireNonNull(action, "action is null");
        if (isFailure()) {
            action.accept(getCause());
        }
    }


    /**
     * Returns the value of this {@link Success}, or throws a provided exception if this is a {@link Failure}.
     * <p>
     * The exception to throw is created by applying the given {@code exceptionProvider} function to the cause of the failure.
     *
     * @param <X>               the type of the exception to throw
     * @param exceptionProvider a function mapping the throwable cause to an exception to be thrown
     * @return the value of this {@link Success}
     * @throws X                     the exception provided by {@code exceptionProvider} if this is a {@link Failure}
     * @throws NullPointerException  if {@code exceptionProvider} is null
     */
    default <X extends Throwable> T getOrElseThrow(@NonNull Function<? super Throwable, X> exceptionProvider) throws X {
        Objects.requireNonNull(exceptionProvider, "exceptionProvider is null");
        if (isFailure()) {
            throw exceptionProvider.apply(getCause());
        } else {
            return get();
        }
    }

    /**
     * Folds this {@code Try} into a single value by applying one of two functions:
     * one for the failure case and one for the success case.
     * <p>
     * If this is a {@link Failure}, the {@code ifFail} function is applied to the cause.
     * If this is a {@link Success}, the {@code f} function is applied to the value.
     *
     * @param ifFail maps the throwable cause if this is a {@link Failure}
     * @param f      maps the value if this is a {@link Success}
     * @param <X>    the type of the result
     * @return the result of applying the corresponding function
     */
    default <X> X fold(@NonNull Function<? super Throwable, ? extends X> ifFail, @NonNull Function<? super T, ? extends X> f) {
        if (isFailure()) {
            return ifFail.apply(getCause());
        } else {
            return f.apply(get());
        }
    }

    /**
     * Performs the given action if this {@code Try} is a {@link Success}, otherwise does nothing.
     * <p>
     * This method is useful for side-effecting operations without transforming the value.
     *
     * @param action a consumer of the value
     * @return this {@code Try} instance
     * @throws NullPointerException if {@code action} is null
     */
    @Override
    default Try<T> peek(@NonNull Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (isSuccess()) {
            action.accept(get());
        }
        return this;
    }


    /**
     * Attempts to recover from a failure if the cause is an instance of the specified exception type.
     * <p>
     * If this {@code Try} is a {@link Success}, it is returned unchanged. If this is a {@link Failure} and the cause
     * is assignable from {@code exceptionType}, the recovery function {@code f} is applied to the cause inside a new {@code Try}.
     * Otherwise, the original {@code Failure} is returned.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recover(ArithmeticException.class, x -> Integer.MAX_VALUE);
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0)
     *    .recover(Error.class, x -> -1)
     *    .recover(ArithmeticException.class, x -> Integer.MAX_VALUE);
     *
     * // = Failure(java.lang.ArithmeticException: / by zero)
     * Try.of(() -> 1/0).recover(Error.class, x -> Integer.MAX_VALUE);
     * }</pre>
     *
     * @param <X>           the type of exception to handle
     * @param exceptionType the specific exception type that should be recovered
     * @param f             a recovery function taking an exception of type {@code X} and producing a value
     * @return a {@code Success} with the recovered value if the exception matches, otherwise this {@code Try}
     * @throws NullPointerException if {@code exceptionType} or {@code f} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recover(@NonNull Class<X> exceptionType, @NonNull Function<? super X, ? extends T> f) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            final Throwable cause = getCause();
            if (exceptionType.isAssignableFrom(cause.getClass())) {
                return Try.of(() -> f.apply((X) cause));
            }
        }
        return this;
    }


    /**
     * Attempts to recover from a failure by applying the given recovery function if the cause matches the specified exception type.
     * <p>
     * If this {@code Try} is a {@link Success}, it is returned unchanged. If this is a {@link Failure} and the cause
     * is assignable from {@code exceptionType}, the recovery function {@code f} is applied to the cause and returns a new {@code Try}.
     * If the returned {@code Try} is a {@link Try#isFailure()}, the recovery is considered unsuccessful.
     * Otherwise, the original {@code Failure} is returned.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recoverWith(ArithmeticException.class, x -> Try.success(Integer.MAX_VALUE));
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0)
     *    .recoverWith(Error.class, x -> Try.success(-1))
     *    .recoverWith(ArithmeticException.class, x -> Try.success(Integer.MAX_VALUE));
     *
     * // = Failure(java.lang.ArithmeticException: / by zero)
     * Try.of(() -> 1/0).recoverWith(Error.class, x -> Try.success(Integer.MAX_VALUE));
     * }</pre>
     *
     * @param <X>           the type of exception to handle
     * @param exceptionType the specific exception type that should trigger recovery
     * @param f             a recovery function that takes an exception of type {@code X} and returns a new {@code Try} instance
     * @return a {@code Try} representing the recovered value if the exception matches, otherwise this {@code Try}
     * @throws NullPointerException if {@code exceptionType} or {@code f} is null
     */
    @GwtIncompatible
    @SuppressWarnings("unchecked")
    default <X extends Throwable> Try<T> recoverWith(@NonNull Class<X> exceptionType, @NonNull Function<? super X, Try<? extends T>> f) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            final Throwable cause = getCause();
            if (exceptionType.isAssignableFrom(cause.getClass())) {
                try {
                    return narrow(f.apply((X) cause));
                } catch (Throwable t) {
                    return new Failure<>(t);
                }
            }
        }
        return this;
    }


    /**
     * Recovers this {@code Try} with the given {@code recovered} value if this is a {@link Try.Failure}
     * and the underlying cause is assignable to the specified {@code exceptionType}.
     * <p>
     * If this {@code Try} is a {@link Success}, or if the cause does not match {@code exceptionType},
     * the original {@code Try} is returned unchanged.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recoverWith(ArithmeticException.class, Try.success(Integer.MAX_VALUE));
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0)
     *    .recoverWith(Error.class, Try.success(-1))
     *    .recoverWith(ArithmeticException.class, Try.success(Integer.MAX_VALUE));
     *
     * // = Failure(java.lang.ArithmeticException: / by zero)
     * Try.of(() -> 1/0).recoverWith(Error.class, Try.success(Integer.MAX_VALUE));
     * }</pre>
     *
     * @param <X>           the type of exception to handle
     * @param exceptionType the exception type that triggers recovery
     * @param recovered     a {@code Try} instance to return if the cause matches {@code exceptionType}
     * @return the given {@code recovered} if the exception matches, otherwise this {@code Try}
     * @throws NullPointerException if {@code exceptionType} or {@code recovered} is null
     */
    @GwtIncompatible
    default <X extends Throwable> Try<T> recoverWith(@NonNull Class<X> exceptionType, @NonNull Try<? extends T> recovered) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(recovered, "recovered is null");
        return (isFailure() && exceptionType.isAssignableFrom(getCause().getClass()))
          ? narrow(recovered)
          : this;
    }


    /**
     * Recovers this {@code Try} with the given {@code value} if this is a {@link Try.Failure}
     * and the underlying cause matches the specified {@code exceptionType}.
     * <p>
     * If this {@code Try} is a {@link Success}, or if the cause is not assignable from {@code exceptionType},
     * the original {@code Try} is returned unchanged.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recover(ArithmeticException.class, 13);
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0)
     *    .recover(Error.class, -1)
     *    .recover(ArithmeticException.class, Integer.MAX_VALUE);
     *
     * // = Failure(java.lang.ArithmeticException: / by zero)
     * Try.of(() -> 1/0).recover(Error.class, Integer.MAX_VALUE);
     * }</pre>
     *
     * @param <X>           the type of exception to handle
     * @param exceptionType the exception type that triggers recovery
     * @param value         the value to return in a {@link Try.Success} if the cause matches
     * @return a {@code Try} containing the recovery value if the exception matches, otherwise this {@code Try}
     * @throws NullPointerException if {@code exceptionType} is null
     */
    @GwtIncompatible
    default <X extends Throwable> Try<T> recover(@NonNull Class<X> exceptionType, T value) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        return (isFailure() && exceptionType.isAssignableFrom(getCause().getClass()))
          ? Try.success(value)
          : this;
    }


    /**
     * Recovers this {@code Try} if it is a {@link Try.Failure} by applying the given recovery function {@code f}
     * to the underlying exception. The result of the function is wrapped in a {@link Try.Success}.
     * <p>
     * If this {@code Try} is already a {@link Success}, it is returned unchanged.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recover(x -> Integer.MAX_VALUE);
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0).recover(x -> Integer.MAX_VALUE);
     * }</pre>
     *
     * @param f A recovery function that takes the underlying exception and returns a value
     * @return a {@code Try} containing either the original success value or the recovered value
     * @throws NullPointerException if {@code f} is null
     */
    default Try<T> recover(@NonNull Function<? super Throwable, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        if (isFailure()) {
            return Try.of(() -> f.apply(getCause()));
        } else {
            return this;
        }
    }


    /**
     * Recovers this {@code Try} if it is a {@link Try.Failure} by applying the given recovery function {@code f}
     * to the underlying exception. The recovery function returns a new {@code Try} instance. 
     * <p>
     * If this {@code Try} is already a {@link Success}, it is returned unchanged. 
     * If an exception occurs while executing the recovery function, a new {@link Try.Failure} is returned 
     * wrapping that exception.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(13)
     * Try.of(() -> 27/2).recoverWith(x -> Try.success(Integer.MAX_VALUE));
     *
     * // = Success(2147483647)
     * Try.of(() -> 1/0).recoverWith(x -> Try.success(Integer.MAX_VALUE));
     * }</pre>
     *
     * @param f A recovery function that takes the underlying exception and returns a new {@code Try}
     * @return a {@code Try} containing either the original success value or the recovered {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    @SuppressWarnings("unchecked")
    default Try<T> recoverWith(@NonNull Function<? super Throwable, ? extends Try<? extends T>> f) {
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
     * Recovers from any failure by evaluating the given {@code recoveryAttempt} if this {@code Try} is a {@link Try.Failure}.
     * <p>
     * If this {@code Try} is already a {@link Success}, it is returned unchanged. The {@code recoveryAttempt} is
     * evaluated using {@link Try#of(CheckedFunction0)}, and its result is wrapped in a new {@code Try}.
     * <p>
     * Example:
     * <pre>{@code
     * // = Success(5)
     * Try.of(() -> 5)
     *    .recoverAllAndTry(() -> 10);
     *
     * // = Success(10)
     * Try.of(() -> 1/0)
     *    .recoverAllAndTry(() -> 10);
     * }</pre>
     *
     * @param recoveryAttempt A checked supplier providing a fallback value in case of failure
     * @return a {@code Try} containing either the original success value or the result of {@code recoveryAttempt}
     * @throws NullPointerException if {@code recoveryAttempt} is null
     */
    default Try<T> recoverAllAndTry(@NonNull CheckedFunction0<? extends T> recoveryAttempt) {
        Objects.requireNonNull(recoveryAttempt, "recoveryAttempt is null");
        return isFailure() ? of(recoveryAttempt) : this;
    }


    /**
     * Returns {@code this} if it is a {@link Try.Success}, or attempts to recover from a failure when the
     * underlying cause is assignable to the specified {@code exceptionType} by evaluating the given
     * {@code recoveryAttempt} (via {@link Try#of(CheckedFunction0)}).
     * <p>
     * Example usage:
     * <pre>{@code
     * // = Success(5)
     * Try.of(() -> 5)
     *    .recoverAndTry(ArithmeticException.class, () -> 10);
     *
     * // = Success(10)
     * Try.of(() -> 1/0)
     *    .recoverAndTry(ArithmeticException.class, () -> 10);
     *
     * // = Failure(java.lang.ArithmeticException: / by zero)
     * Try.of(() -> 1/0)
     *    .recoverAndTry(NullPointerException.class, () -> 10);
     * }</pre>
     *
     * @param <X>             The type of the exception that may be recovered
     * @param exceptionType   The specific exception type that triggers the recovery
     * @param recoveryAttempt A checked function providing a fallback value if the exception matches
     * @return a {@code Try} containing either the original success value or the result of {@code recoveryAttempt}
     * @throws NullPointerException if {@code exceptionType} or {@code recoveryAttempt} is null
     */
    default <X extends Throwable> Try<T> recoverAndTry(@NonNull Class<X> exceptionType, @NonNull CheckedFunction0<? extends T> recoveryAttempt) {
        Objects.requireNonNull(exceptionType, "exceptionType is null");
        Objects.requireNonNull(recoveryAttempt, "recoveryAttempt is null");
        return isFailure() && exceptionType.isAssignableFrom(getCause().getClass())
          ? of(recoveryAttempt)
          : this;
    }


    /**
     * Converts this {@code Try} to an {@link Either}.
     * <p>
     * If this is a {@link Try.Success}, the value is wrapped as a {@link Either#right(Object)}.
     * If this is a {@link Try.Failure}, the cause is wrapped as a {@link Either#left(Object)}.
     *
     * @return a new {@code Either} representing this {@code Try}
     */
    default Either<Throwable, T> toEither() {
        if (isFailure()) {
            return Either.left(getCause());
        } else {
            return Either.right(get());
        }
    }

    /**
     * Converts this {@code Try} to an {@link Either}, mapping the failure cause to a left value using
     * the provided {@link Function}.
     *
     * @param <L>             the left type of the resulting {@code Either}
     * @param throwableMapper a function to convert the failure {@link Throwable} to type {@code L}
     * @return a new {@code Either} representing this {@code Try}
     * @throws NullPointerException if {@code throwableMapper} is null
     */
    default <L> Either<L, T> toEither(@NonNull Function<? super Throwable, ? extends L> throwableMapper) {
        Objects.requireNonNull(throwableMapper, "throwableMapper is null");
        if (isFailure()) {
            return Either.left(throwableMapper.apply(getCause()));
        } else {
            return Either.right(get());
        }
    }

    /**
     * Converts this {@code Try} to a {@link Validation}.
     * <p>
     * A {@link Try.Success} is converted to a {@link Validation#valid(Object)}, while
     * a {@link Try.Failure} is converted to a {@link Validation#invalid(Object)} containing
     * the cause.
     *
     * @return a new {@code Validation} representing this {@code Try}
     */
    default Validation<Throwable, T> toValidation() {
        return toValidation(Function.identity());
    }

    /**
     * Converts this {@code Try} to a {@link Validation}, mapping the failure cause to an invalid value
     * using the provided {@link Function}.
     *
     * <pre>{@code
     * Validation<String, Integer> validation = Try.of(() -> 1/0).toValidation(Throwable::getMessage);
     * }</pre>
     *
     * @param <U>             the type of the invalid value
     * @param throwableMapper a function to convert the failure {@link Throwable} to type {@code U}
     * @return a {@code Validation} representing this {@code Try}
     * @throws NullPointerException if {@code throwableMapper} is null
     */
    default <U> Validation<U, T> toValidation(@NonNull Function<? super Throwable, ? extends U> throwableMapper) {
        Objects.requireNonNull(throwableMapper, "throwableMapper is null");
        if (isFailure()) {
            return Validation.invalid(throwableMapper.apply(getCause()));
        } else {
            return Validation.valid(get());
        }
    }

    /**
     * Transforms this {@code Try} into a value of another type using the provided function.
     *
     * @param f   a transformation function
     * @param <U> the result type of the transformation
     * @return the result of applying {@code f} to this {@code Try}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(@NonNull Function<? super Try<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    /**
     * Executes a given {@link Runnable} after this {@code Try}, regardless of whether it is a
     * {@link Try.Success} or {@link Try.Failure}.
     *
     * @param runnable a final action to perform
     * @return this {@code Try} unchanged if the runnable succeeds, or a new {@link Try.Failure} if it throws
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinally(@NonNull Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable is null");
        return andFinallyTry(runnable::run);
    }

    /**
     * Executes a given {@link CheckedRunnable} after this {@code Try}, regardless of whether it is a
     * {@link Try.Success} or {@link Try.Failure}.
     *
     * @param runnable a checked final action to perform
     * @return this {@code Try} unchanged if the runnable succeeds, or a new {@link Try.Failure} if it throws
     * @throws NullPointerException if {@code runnable} is null
     */
    default Try<T> andFinallyTry(@NonNull CheckedRunnable runnable) {
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
     * Represents a successful {@link Try} containing a value.
     * <p>
     * Instances of this class indicate that the computation completed successfully
     * and hold the resulting value of type {@code T}.
     *
     * <pre>{@code
     * Try<Integer> success = new Try.Success<>(42);
     * success.isSuccess(); // true
     * success.get();       // 42
     * }</pre>
     *
     * @param <T> the type of the contained value
     * @author Daniel Dietrich
     */
    final class Success<T> implements Try<T>, Serializable {

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("serial") // Conditionally serializable
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
     * Represents a failed {@link Try} containing a {@link Throwable} as the cause.
     * <p>
     * Instances of this class indicate that the computation threw an exception
     * and do not contain a successful value.
     *
     * <pre>{@code
     * Try<Integer> failure = new Try.Failure<>(new RuntimeException("error"));
     * failure.isFailure();  // true
     * failure.getCause();   // RuntimeException: error
     * }</pre>
     *
     * @param <T> the type of the value that would have been contained if successful
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
         * throws Throwable             sneakily, if the given {@code cause} is fatal, i.e. non-recoverable
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
    static <T1 extends AutoCloseable> WithResources1<T1> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable> WithResources2<T1, T2> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable> WithResources3<T1, T2, T3> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable> WithResources4<T1, T2, T3, T4> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable> WithResources5<T1, T2, T3, T4, T5> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable> WithResources6<T1, T2, T3, T4, T5, T6> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, @NonNull CheckedFunction0<? extends T6> t6Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable> WithResources7<T1, T2, T3, T4, T5, T6, T7> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, @NonNull CheckedFunction0<? extends T6> t6Supplier, @NonNull CheckedFunction0<? extends T7> t7Supplier) {
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
    static <T1 extends AutoCloseable, T2 extends AutoCloseable, T3 extends AutoCloseable, T4 extends AutoCloseable, T5 extends AutoCloseable, T6 extends AutoCloseable, T7 extends AutoCloseable, T8 extends AutoCloseable> WithResources8<T1, T2, T3, T4, T5, T6, T7, T8> withResources(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, @NonNull CheckedFunction0<? extends T6> t6Supplier, @NonNull CheckedFunction0<? extends T7> t7Supplier, @NonNull CheckedFunction0<? extends T8> t8Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction1<? super T1, ? extends R> f) {
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

        private WithResources2(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction2<? super T1, ? super T2, ? extends R> f) {
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

        private WithResources3(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction3<? super T1, ? super T2, ? super T3, ? extends R> f) {
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

        private WithResources4(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction4<? super T1, ? super T2, ? super T3, ? super T4, ? extends R> f) {
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

        private WithResources5(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? extends R> f) {
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

        private WithResources6(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, CheckedFunction0<? extends T6> t6Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? extends R> f) {
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

        private WithResources7(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, @NonNull CheckedFunction0<? extends T6> t6Supplier, @NonNull CheckedFunction0<? extends T7> t7Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? extends R> f) {
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

        private WithResources8(@NonNull CheckedFunction0<? extends T1> t1Supplier, @NonNull CheckedFunction0<? extends T2> t2Supplier, @NonNull CheckedFunction0<? extends T3> t3Supplier, @NonNull CheckedFunction0<? extends T4> t4Supplier, @NonNull CheckedFunction0<? extends T5> t5Supplier, @NonNull CheckedFunction0<? extends T6> t6Supplier, @NonNull CheckedFunction0<? extends T7> t7Supplier, @NonNull CheckedFunction0<? extends T8> t8Supplier) {
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
        public <R> Try<R> of(@NonNull CheckedFunction8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? extends R> f) {
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
                || ThreadDeathResolver.isThreadDeath(throwable)
                || throwable instanceof VirtualMachineError;
    }

    // DEV-NOTE: we do not plan to expose this as public API
    @SuppressWarnings("unchecked")
    static <T extends Throwable, R> R sneakyThrow(Throwable t) throws T {
        throw (T) t;
    }

    class ThreadDeathResolver {
        static final Class<?> THREAD_DEATH_CLASS = resolve();

        static boolean isThreadDeath(Throwable throwable) {
            return THREAD_DEATH_CLASS != null && THREAD_DEATH_CLASS.isInstance(throwable);
        }

        private static Class<?> resolve() {
            try {
                return Class.forName("java.lang.ThreadDeath");
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }
}
