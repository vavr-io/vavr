/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import static javaslang.API.Case;
import static javaslang.API.Failure;
import static javaslang.API.Success;
import static javaslang.Predicates.instanceOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

import javaslang.API;
import javaslang.AbstractValueTest;
import javaslang.Predicates;
import javaslang.Serializables;
import javaslang.collection.Seq;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class TryTest extends AbstractValueTest {

    private static final String OK = "ok";
    private static final String FAILURE = "failure";

    // -- AbstractValueTest

    @Override
    protected <T> Try<T> empty() {
        return Try.failure(new NoSuchElementException());
    }

    @Override
    protected <T> Try<T> of(T element) {
        return Try.success(element);
    }

    @SafeVarargs
    @Override
    protected final <T> Try<T> of(T... elements) {
        return of(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Override
    @Test(expected = Try.NonFatalException.class)
    public void shouldGetEmpty() {
        empty().get();
    }

    // -- Try

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfSuccess() {
        assertThat(Try.success(1).exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfSuccess() {
        assertThat(Try.success(1).exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfFailure() {
        assertThat(failure().exists(e -> true)).isFalse();
    }

    @Test(expected = Error.class)
    public void shouldNotHoldPropertyExistsWhenPredicateThrows() {
        Try.success(1).exists(e -> {
            throw new Error("error");
        });
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfSuccess() {
        assertThat(Try.success(1).forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfSuccess() {
        assertThat(Try.success(1).forAll(i -> i == 2)).isFalse();
    }

    @Test // a property holds for all elements of no elements
    public void shouldNotHoldPropertyForAllOfFailure() {
        assertThat(failure().forAll(e -> true)).isTrue();
    }

    @Test(expected = Error.class)
    public void shouldNotHoldPropertyForAllWhenPredicateThrows() {
        Try.success(1).forAll(e -> {
            throw new Error("error");
        });
    }

    // -- orElse

    @Test
    public void shouldReturnSelfOnOrElseIfSuccess() {
        Try<Integer> success = Try.success(42);
        assertThat(success.orElse(Try.success(0))).isSameAs(success);
    }

    @Test
    public void shouldReturnSelfOnOrElseSupplierIfSuccess() {
        Try<Integer> success = Try.success(42);
        assertThat(success.orElse(() -> Try.success(0))).isSameAs(success);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseIfFailure() {
        Try<Integer> success = Try.success(42);
        assertThat(Try.failure(new RuntimeException()).orElse(success)).isSameAs(success);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseSupplierIfFailure() {
        Try<Integer> success = Try.success(42);
        assertThat(Try.failure(new RuntimeException()).orElse(() -> success)).isSameAs(success);
    }

    // -- iterator

    @Test
    public void shouldReturnIteratorOfSuccess() {
        assertThat((Iterator<Integer>) Try.success(1).iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfFailure() {
        assertThat((Iterator<Object>) failure().iterator()).isNotNull();
    }

    // -- Try.of

    @Test
    public void shouldCreateSuccessWhenCallingTryOfCheckedSupplier() {
        assertThat(Try.of(() -> 1) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfCheckedSupplier() {
        assertThat(Try.of(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCallingTryOfCheckedSupplier() {
        assertThatThrownBy(() -> Try.of(null)).isInstanceOf(NullPointerException.class).hasMessage("supplier is null");
    }

    // -- Try.ofSupplier

    @Test
    public void shouldCreateSuccessWhenCallingTryOfSupplier() {
        assertThat(Try.ofSupplier(() -> 1) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCallingTryOfSupplier() {
        assertThatThrownBy(() -> Try.ofSupplier(null)).isInstanceOf(NullPointerException.class).hasMessage("supplier is null");
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfSupplier() {
        assertThat(Try.ofSupplier(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    // -- Try.ofCallable

    @Test
    public void shouldCreateSuccessWhenCallingTryOfCallable() {
        assertThat(Try.ofCallable(() -> 1) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfCallable() {
        assertThat(Try.ofCallable(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCallingTryOfCallable() {
        assertThatThrownBy(() -> Try.ofCallable(null)).isInstanceOf(NullPointerException.class).hasMessage("callable is null");
    }

    // -- Try.run

    @Test
    public void shouldCreateSuccessWhenCallingTryRunCheckedRunnable() {
        assertThat(Try.run(() -> {
        }) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryRunCheckedRunnable() {
        assertThat(Try.run(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCallingTryRunCheckedRunnable() {
        assertThatThrownBy(() -> Try.run(null)).isInstanceOf(NullPointerException.class).hasMessage("runnable is null");
    }

    // -- Try.runRunnable

    @Test
    public void shouldCreateSuccessWhenCallingTryRunRunnable() {
        assertThat(Try.runRunnable(() -> {
        }) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryRunRunnable() {
        assertThat(Try.runRunnable(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    @Test
    public void shouldThrowNullPointerExceptionWhenCallingTryRunRunnable() {
        assertThatThrownBy(() -> Try.runRunnable(null)).isInstanceOf(NullPointerException.class).hasMessage("runnable is null");
    }

    // -- Try.withResources

    @SuppressWarnings("try")/* https://bugs.openjdk.java.net/browse/JDK-8155591 */
    static class Resource<T> implements AutoCloseable {

        final T value;
        boolean isClosed = false;

        Resource(T value) {
            this.value = value;
        }

        @Override
        public void close() throws Exception {
            isClosed = true;
        }
    }

    private static <T> Resource<T> resource(T value) {
        return new Resource<>(value);
    }

    @Test
    public void shouldCreateSuccessTryWithResources1() {
        final Resource<Integer> resource1 = resource(1);
        final Try<String> actual = Try.withResources(() -> resource1).of(i1 -> "" + i1.value);
        assertThat(actual).isEqualTo(Success("1"));
        assertThat(resource1.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources1() {
        final Resource<Integer> resource1 = resource(1);
        final Try<?> actual = Try.withResources(() -> resource1).of(i -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources2() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2).of((i1, i2) -> "" + i1.value + i2.value);
        assertThat(actual).isEqualTo(Success("12"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources2() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2).of((i1, i2) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources3() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3).of((i1, i2, i3) -> "" + i1.value + i2.value + i3.value);
        assertThat(actual).isEqualTo(Success("123"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources3() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3).of((i1, i2, i3) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources4() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4).of((i1, i2, i3, i4) -> "" + i1.value + i2.value + i3.value + i4.value);
        assertThat(actual).isEqualTo(Success("1234"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources4() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4).of((i1, i2, i3, i4) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources5() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5).of((i1, i2, i3, i4, i5) -> "" + i1.value + i2.value + i3.value + i4.value + i5.value);
        assertThat(actual).isEqualTo(Success("12345"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources5() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5).of((i1, i2, i3, i4, i5) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources6() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6).of((i1, i2, i3, i4, i5, i6) -> "" + i1.value + i2.value + i3.value + i4.value + i5.value + i6.value);
        assertThat(actual).isEqualTo(Success("123456"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources6() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6).of((i1, i2, i3, i4, i5, i6) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources7() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Resource<Integer> resource7 = resource(7);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6, () -> resource7).of((i1, i2, i3, i4, i5, i6, i7) -> "" + i1.value + i2.value + i3.value + i4.value + i5.value + i6.value + i7.value);
        assertThat(actual).isEqualTo(Success("1234567"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
        assertThat(resource7.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources7() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Resource<Integer> resource7 = resource(7);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6, () -> resource7).of((i1, i2, i3, i4, i5, i6, i7) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
        assertThat(resource7.isClosed).isTrue();
    }

    @Test
    public void shouldCreateSuccessTryWithResources8() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Resource<Integer> resource7 = resource(7);
        final Resource<Integer> resource8 = resource(8);
        final Try<String> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6, () -> resource7, () -> resource8).of((i1, i2, i3, i4, i5, i6, i7, i8) -> "" + i1.value + i2.value + i3.value + i4.value + i5.value + i6.value + i7.value + i8.value);
        assertThat(actual).isEqualTo(Success("12345678"));
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
        assertThat(resource7.isClosed).isTrue();
        assertThat(resource8.isClosed).isTrue();
    }

    @Test
    public void shouldCreateFailureTryWithResources8() {
        final Resource<Integer> resource1 = resource(1);
        final Resource<Integer> resource2 = resource(2);
        final Resource<Integer> resource3 = resource(3);
        final Resource<Integer> resource4 = resource(4);
        final Resource<Integer> resource5 = resource(5);
        final Resource<Integer> resource6 = resource(6);
        final Resource<Integer> resource7 = resource(7);
        final Resource<Integer> resource8 = resource(8);
        final Try<?> actual = Try.withResources(() -> resource1, () -> resource2, () -> resource3, () -> resource4, () -> resource5, () -> resource6, () -> resource7, () -> resource8).of((i1, i2, i3, i4, i5, i6, i7, i8) -> { throw new Error(); });
        assertThat(actual.isFailure()).isTrue();
        assertThat(resource1.isClosed).isTrue();
        assertThat(resource2.isClosed).isTrue();
        assertThat(resource3.isClosed).isTrue();
        assertThat(resource4.isClosed).isTrue();
        assertThat(resource5.isClosed).isTrue();
        assertThat(resource6.isClosed).isTrue();
        assertThat(resource7.isClosed).isTrue();
        assertThat(resource8.isClosed).isTrue();
    }

    // -- Failure.Cause

    @Test(expected = Try.FatalException.class)
    public void shouldDetectFatalException() throws Exception {
        Try.NonFatalException.of(new OutOfMemoryError());
    }

    @Test
    public void shouldDetectNonFatalException() throws Exception {
        final Try.NonFatalException cause = Try.NonFatalException.of(new Exception());
        assertThat(cause).isNotNull();
    }

    @Test
    public void shouldSubsequentlyHandOverCause() {
        final Supplier<?> inner = () -> {
            throw new UnknownError("\uD83D\uDCA9");
        };
        final Supplier<?> outer = () -> Try.of(inner::get).get();
        try {
            Try.of(outer::get).get();
            Assertions.fail("Exception expected");
        } catch (Try.FatalException x) {
            Assertions.assertThat(x.getCause().getMessage()).isEqualTo("\uD83D\uDCA9");
        } catch (Throwable x) {
            Assertions.fail("Unexpected exception type: " + x.getClass().getName());
        }
    }

    @Test
    public void shouldCreateFailureOnNonFatalException() {
        assertThat(failure().failed().get().getClass().getName()).isEqualTo(RuntimeException.class.getName());
    }

    // -- Failure.NonFatal

    @Test
    public void shouldReturnAndNotThrowOnNonFatal() {
        final Try.NonFatalException cause = Try.NonFatalException.of(new Exception());
        assertThat(Try.NonFatalException.of(cause)).isNotNull();
    }

    @Test
    public void shouldReturnToStringOnNonFatal() {
        final Exception exception = new java.lang.Exception();
        final Try.NonFatalException cause = Try.NonFatalException.of(exception);
        assertThat(cause.toString()).isEqualTo("NonFatal(" + exception.toString() + ")");
    }

    @Test
    public void shouldReturnHasCodeOnNonFatal() {
        final Exception exception = new java.lang.Exception();
        final Try.NonFatalException cause = Try.NonFatalException.of(exception);
        assertThat(cause.hashCode()).isEqualTo(Objects.hashCode(exception));
    }

    // -- Failure.Fatal

    @Test
    public void shouldReturnToStringOnFatal() {
        try {
            Try.of(() -> {
                throw new UnknownError();
            });
            fail("Exception Expected");
        } catch (Try.FatalException x) {
            assertThat(x.toString()).isEqualTo("Fatal(java.lang.UnknownError)");
        }
    }

    @Test
    public void shouldReturnHashCodeOnFatal() {
        UnknownError error = new UnknownError();
        try {
            Try.of(() -> {
                throw error;
            });
            fail("Exception Expected");
        } catch (Try.FatalException x) {
            assertThat(x.hashCode()).isEqualTo(Objects.hashCode(error));
        }
    }

    @Test
    public void shouldReturnEqualsOnFatal() {
        UnknownError error = new UnknownError();
        try {
            Try.of(() -> {
                throw error;
            });
            fail("Exception Expected");
        } catch (Try.FatalException x) {
            try {
                Try.of(() -> {
                    throw error;
                });
                fail("Exception Expected");
            } catch (Try.FatalException fatal) {
                assertThat(x.equals(fatal)).isEqualTo(true);
            }
        }
    }

    // -- Failure

    @Test
    public void shouldDetectFailureOfRunnable() {
        assertThat(Try.of(() -> {
            throw new RuntimeException();
        }).isFailure()).isTrue();
    }

    @Test(expected = Try.FatalException.class)
    public void shouldPassThroughFatalException() {
        Try.of(() -> {
            throw new UnknownError();
        });
    }

    // -- isFailure

    @Test
    public void shouldDetectFailureOnNonFatalException() {
        assertThat(failure().isFailure()).isTrue();
    }

    // -- isSuccess

    @Test
    public void shouldDetectNonSuccessOnFailure() {
        assertThat(failure().isSuccess()).isFalse();
    }

    // -- get

    @Test(expected = Try.NonFatalException.class)
    public void shouldThrowWhenGetOnFailure() {
        failure().get();
    }

    // -- getOrElse

    @Test
    public void shouldReturnElseWhenOrElseOnFailure() {
        assertThat(failure().getOrElse(OK)).isEqualTo(OK);
    }

    // -- getOrElseGet

    @Test
    public void shouldReturnElseWhenOrElseGetOnFailure() {
        assertThat(failure().getOrElseGet(x -> OK)).isEqualTo(OK);
    }

    // -- getOrElseThrow

    @Test(expected = IllegalStateException.class)
    public void shouldThrowOtherWhenGetOrElseThrowOnFailure() {
        failure().getOrElseThrow(x -> new IllegalStateException(OK));
    }

    // -- orElseRun

    @Test
    public void shouldRunElseWhenOrElseRunOnFailure() {
        final String[] result = new String[1];
        failure().orElseRun(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    // -- recover(Class, Function)

    @Test
    public void shouldRecoverWhenFailureMatchesExactly() {
        final Try<String> testee = failure(RuntimeException.class);
        assertThat(testee.recover(RuntimeException.class, x -> OK).isSuccess()).isTrue();
    }

    @Test
    public void shouldRecoverWhenFailureIsAssignableFrom() {
        final Try<String> testee = failure(UnsupportedOperationException.class);
        assertThat(testee.recover(RuntimeException.class, x -> OK).isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnThisWhenRecoverDifferentTypeOfFailure() {
        final Try<String> testee = failure(RuntimeException.class);
        assertThat(testee.recover(NullPointerException.class, x -> OK)).isSameAs(testee);
    }

    @Test
    public void shouldReturnThisWhenRecoverSpecificFailureOnSuccess() {
        final Try<String> testee = success();
        assertThat(testee.recover(RuntimeException.class, x -> OK)).isSameAs(testee);
    }

    // -- recover(Class, Object)

    @Test
    public void shouldRecoverWithSuccessWhenFailureMatchesExactly() {
        final Try<String> testee = failure(RuntimeException.class);
        assertThat(testee.recover(RuntimeException.class, OK).isSuccess()).isTrue();
    }

    @Test
    public void shouldRecoverWithSuccessWhenFailureIsAssignableFrom() {
        final Try<String> testee = failure(UnsupportedOperationException.class);
        assertThat(testee.recover(RuntimeException.class, OK).isSuccess()).isTrue();
    }

    @Test
    public void shouldReturnThisWhenRecoverWithSuccessDifferentTypeOfFailure() {
        final Try<String> testee = failure(RuntimeException.class);
        assertThat(testee.recover(NullPointerException.class, OK)).isSameAs(testee);
    }

    @Test
    public void shouldReturnThisWhenRecoverWithSuccessSpecificFailureOnSuccess() {
        final Try<String> testee = success();
        assertThat(testee.recover(RuntimeException.class, OK)).isSameAs(testee);
    }

    // -- recover(Function)

    @Test
    public void shouldRecoverOnFailure() {
        assertThat(failure().recover(x -> OK).get()).isEqualTo(OK);
    }

    @Test
    public void shouldReturnThisWhenRecoverOnSuccess() {
        final Try<String> testee = success();
        assertThat(testee.recover(x -> OK)).isSameAs(testee);
    }

    // -- recoverWith

    @Test
    public void shouldRecoverWithOnFailure() {
        assertThat(TryTest.<String> failure().recoverWith(x -> success()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithThrowingOnFailure() {
        final RuntimeException error = error();
        assertThat(failure().recoverWith(x -> {
            throw error;
        })).isEqualTo(Try.failure(error));
    }

    // -- onFailure

    @Test
    public void shouldConsumeThrowableWhenCallingOnFailureGivenFailure() {
        final String[] result = new String[] { FAILURE };
        failure().onFailure(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    // -- toOption

    @Test
    public void shouldConvertFailureToOption() {
        assertThat(failure().toOption().isDefined()).isFalse();
    }

    // -- toEither

    @Test
    public void shouldConvertFailureToEither() {
        assertThat(failure().toEither().isLeft()).isTrue();
    }

    @Test
    public void shouldConvertFailureToEitherLeft() {
        assertThat(failure().toEither("test").isLeft()).isTrue();
    }

    @Test
    public void shouldConvertFailureToEitherLeftSupplier() {
        assertThat(failure().toEither(() -> "test").isLeft()).isTrue();
    }

    // -- toCompletableFuture

    @Test
    public void shouldConvertSuccessToCompletableFuture() {
        CompletableFuture<String> future = success().toCompletableFuture();
        assertThat(future.isDone());
        assertThat(Try.of(future::get).get()).isEqualTo(success().get());
    }

    @Test
    public void shouldConvertFailureToFailedCompletableFuture() {

        final CompletableFuture<Object> future = failure().toCompletableFuture();
        assertThat(future.isDone());
        assertThat(future.isCompletedExceptionally());
        assertThatThrownBy(future::get)
                .isExactlyInstanceOf(ExecutionException.class)
                .hasCauseExactlyInstanceOf(RuntimeException.class);
    }

    // -- toValidation

    @Test
    public void shouldConvertFailureToValidationLeft() {
        assertThat(failure().toValidation("test").isInvalid()).isTrue();
    }

    @Test
    public void shouldConvertFailureToValidationLeftSupplier() {
        assertThat(failure().toValidation(() -> "test").isInvalid()).isTrue();
    }

    // -- toJavaOptional

    @Test
    public void shouldConvertFailureToJavaOptional() {
        assertThat(failure().toJavaOptional().isPresent()).isFalse();
    }

    // -- filter

    @Test
    public void shouldFilterMatchingPredicateOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(s -> true)).isEqualTo(actual);
    }

    @Test
    public void shouldFilterNonMatchingPredicateOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(s -> false)).isEqualTo(actual);
    }

    @Test
    public void shouldFilterWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.filter(this::filter)).isEqualTo(actual);
    }

    @Test
    public void shouldReturnIdentityWhenFilterOnFailure() {
        final Try<String> identity = failure();
        assertThat(identity.filter(s -> true)).isEqualTo(identity);
    }

    @Test
    public void shouldReturnIdentityWhenFilterWithErrorProviderOnFailure() throws Exception {
        final Try<String> identity = failure();
        assertThat(identity.filter(s -> false, ignored -> new IllegalArgumentException())).isEqualTo(identity);
    }

    // -- flatMap

    @Test
    public void shouldFlatMapOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(s -> Try.of(() -> s + "!"))).isEqualTo(actual);
    }

    @Test
    public void shouldFlatMapWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(this::flatMap)).isEqualTo(actual);
    }

    // -- isEmpty

    @Test
    public void shouldForEachOnFailure() {
        final List<String> actual = new ArrayList<>();
        TryTest.<String> failure().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    // -- map

    @Test
    public void shouldMapOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.map(s -> s + "!")).isEqualTo(actual);
    }

    @Test
    public void shouldMapWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.map(this::map)).isEqualTo(actual);
    }

    @Test
    public void shouldChainSuccessWithMap() {
        final Try<Integer> actual = Try.of(() -> 100)
                .map(x -> x + 100)
                .map(x -> x + 50);

        final Try<Integer> expected = Try.success(250);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldChainFailureWithMap() {
        final Try<Integer> actual = Try.of(() -> 100)
                .map(x -> x + 100)
                .map(x -> Integer.parseInt("aaa") + x)   //Throws exception.
                .map(x -> x / 2);
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.NumberFormatException: For input string: \"aaa\")");
    }
    
    // -- andThen

    @Test
    public void shouldComposeFailureWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
            throw new Error("err1");
        }).andThen(() -> {
            throw new Error("err2");
        });
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.Error: err1)");
    }

    @Test
    public void shouldChainConsumableSuccessWithAndThen() {
        final Try<Integer> actual = Try.of(() -> new ArrayList<Integer>())
                .andThen(arr -> arr.add(10))
                .andThen(arr -> arr.add(30))
                .andThen(arr -> arr.add(20))
                .map(arr -> arr.get(1));

        final Try<Integer> expected = Try.success(30);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldChainConsumableFailureWithAndThen() {
        final Try<Integer> actual = Try.of(() -> new ArrayList<Integer>())
                .andThen(arr -> arr.add(10))
                .andThen(arr -> arr.add(Integer.parseInt("aaa"))) //Throws exception.
                .andThen(arr -> arr.add(20))
                .map(arr -> arr.get(1));
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.NumberFormatException: For input string: \"aaa\")");
    }

    // peek

    @Test
    public void shouldPeekFailure() {
        final List<Object> list = new ArrayList<>();
        assertThat(failure().peek(list::add)).isEqualTo(failure());
        assertThat(list.isEmpty()).isTrue();
    }

    // equals

    @Test
    public void shouldEqualFailureIfObjectIsSame() {
        final Try<?> failure = Try.failure(error());
        assertThat(failure).isEqualTo(failure);
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsNull() {
        assertThat(Try.failure(error())).isNotNull();
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsOfDifferentType() {
        assertThat(Try.failure(error()).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualFailure() {
        assertThat(Try.failure(error())).isEqualTo(Try.failure(error()));
    }

    // hashCode

    @Test
    public void shouldHashFailure() {
        final Throwable error = error();
        assertThat(Try.failure(error).hashCode()).isEqualTo(Objects.hashCode(error));
    }

    // toString

    @Test
    public void shouldConvertFailureToString() {
        assertThat(Try.failure(error()).toString()).isEqualTo("Failure(java.lang.RuntimeException: error)");
    }

    // -- sequence

    @Test
    public void shouldConvertListOfSuccessToTryOfList() {
        List<Try<String>> tries = Arrays.asList(Try.success("a"), Try.success("b"), Try.success("c"));
        Try<Seq<String>> reducedTry = Try.sequence(tries);
        assertThat(reducedTry instanceof Try.Success).isTrue();
        assertThat(reducedTry.get().size()).isEqualTo(3);
        assertThat(reducedTry.get().mkString()).isEqualTo("abc");
    }

    @Test
    public void shouldConvertListOfFailureToTryOfList() {
        Throwable t = new RuntimeException("failure");
        List<Try<String>> tries = Arrays.asList(Try.failure(t), Try.failure(t), Try.failure(t));
        Try<Seq<String>> reducedTry = Try.sequence(tries);
        assertThat(reducedTry instanceof Try.Failure).isTrue();
    }

    @Test
    public void shouldConvertListOfMixedTryToTryOfList() {
        Throwable t = new RuntimeException("failure");
        List<Try<String>> tries = Arrays.asList(Try.success("a"), Try.failure(t), Try.success("c"));
        Try<Seq<String>> reducedTry = Try.sequence(tries);
        assertThat(reducedTry instanceof Try.Failure).isTrue();
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeFailure() {
        final Object actual = Serializables.deserialize(Serializables.serialize(Try.failure(error())));
        assertThat(actual.toString()).isEqualTo(Try.failure(error()).toString());
    }

    // -- Success

    @Test
    public void shouldDetectSuccessOfRunnable() {
        assertThat(Try.run(() -> System.out.println("side-effect")).isSuccess()).isTrue();
    }

    @Test
    public void shouldDetectSuccess() {
        assertThat(success().isSuccess()).isTrue();
    }

    @Test
    public void shouldDetectNonFailureOnSuccess() {
        assertThat(success().isFailure()).isFalse();
    }

    @Test
    public void shouldGetOnSuccess() {
        assertThat(success().get()).isEqualTo(OK);
    }

    @Test
    public void shouldGetOrElseOnSuccess() {
        assertThat(success().getOrElse((String) null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseGetOnSuccess() {
        assertThat(success().getOrElseGet(x -> null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseRunOnSuccess() {
        final String[] result = new String[] { OK };
        success().orElseRun(x -> result[0] = FAILURE);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseThrowOnSuccess() {
        assertThat(success().getOrElseThrow(x -> null)).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverOnSuccess() {
        assertThat(success().recover(x -> null).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithOnSuccess() {
        assertThat(success().recoverWith(x -> null).get()).isEqualTo(OK);
    }

    @Test
    public void shouldNotConsumeThrowableWhenCallingOnFailureGivenSuccess() {
        final String[] result = new String[] { OK };
        success().onFailure(x -> result[0] = FAILURE);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldConvertSuccessToOption() {
        assertThat(success().toOption().get()).isEqualTo(OK);
    }

    @Test
    public void shouldConvertSuccessToEither() {
        assertThat(success().toEither().isRight()).isTrue();
    }

    @Test
    public void shouldConvertSuccessToJavaOptional() {
        assertThat(success().toJavaOptional().get()).isEqualTo(OK);
    }

    @Test
    public void shouldFilterMatchingPredicateOnSuccess() {
        assertThat(success().filter(s -> true).get()).isEqualTo(OK);
    }

    @Test
    public void shouldFilterMatchingPredicateWithErrorProviderOnSuccess() {
        assertThat(success().filter(s -> true, s -> new IllegalArgumentException(s)).get()).isEqualTo(OK);
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldFilterNonMatchingPredicateOnSuccess() {
        success().filter(s -> false).get();
    }

    @Test
    public void shouldFilterNonMatchingPredicateAndDefaultThrowableSupplierOnSuccess() {
        assertThat(success().filter(s -> false).getCause())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldFilterNonMatchingPredicateAndCustomThrowableSupplierOnSuccess() {
        assertThat(success().filter(s -> false, () -> new IllegalArgumentException()).getCause())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldUseErrorProviderWhenFilterNonMatchingPredicateOnSuccess() throws Exception {
        assertThat(success().filter(s -> false, str -> new IllegalArgumentException(str)).getCause())
                .isInstanceOf(IllegalArgumentException.class);
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldFilterWithExceptionOnSuccess() {
        success().filter(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test
    public void shouldFlatMapOnSuccess() {
        assertThat(success().flatMap(s -> Try.of(() -> s + "!")).get()).isEqualTo(OK + "!");
    }

    @Test
    public void shouldFlatMapOnIterable() {
        final Try<Integer> success = Try.success(1);
        assertThat(success().flatMap(ignored -> success)).isEqualTo(success);
    }

    @Test
    public void shouldFlatMapOnEmptyIterable() {
        final Try<Integer> failure = Try.failure(new Error());
        assertThat(success().flatMap(ignored -> failure)).isEqualTo(failure);
    }

    @Test(expected = RuntimeException.class)
    public void shouldFlatMapWithExceptionOnSuccess() {
        success().flatMap(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test
    public void shouldForEachOnSuccess() {
        final List<String> actual = new ArrayList<>();
        success().forEach(actual::add);
        assertThat(actual).isEqualTo(Collections.singletonList(OK));
    }

    @Test
    public void shouldMapOnSuccess() {
        assertThat(success().map(s -> s + "!").get()).isEqualTo(OK + "!");
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldMapWithExceptionOnSuccess() {
        success().map(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldThrowWhenCallingFailedOnSuccess() {
        success().failed().get();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenCallingGetCauseOnSuccess() {
        success().getCause();
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
            throw new Error("failure");
        });
        assertThat(actual.toString()).isEqualTo("Failure(java.lang.Error: failure)");
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenSucceeding() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
        });
        final Try<Void> expected = Try.success(null);
        assertThat(actual).isEqualTo(expected);
    }

    // peek

    @Test
    public void shouldPeekSuccess() {
        final List<Object> list = new ArrayList<>();
        assertThat(success().peek(list::add)).isEqualTo(success());
        assertThat(list.isEmpty()).isFalse();
    }

    @Test(expected = RuntimeException.class)
    public void shouldPeekSuccessAndThrow() {
        success().peek(t -> failure().get());
    }

    // equals

    @Test
    public void shouldEqualSuccessIfObjectIsSame() {
        final Try<?> success = Try.success(1);
        assertThat(success).isEqualTo(success);
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsNull() {
        assertThat(Try.success(1)).isNotNull();
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsOfDifferentType() {
        assertThat(Try.success(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualSuccess() {
        assertThat(Try.success(1)).isEqualTo(Try.success(1));
    }

    // hashCode

    @Test
    public void shouldHashSuccess() {
        assertThat(Try.success(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertSuccessToString() {
        assertThat(Try.success(1).toString()).isEqualTo("Success(1)");
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeSuccess() {
        final Object actual = Serializables.deserialize(Serializables.serialize(Try.success(1)));
        assertThat(actual).isEqualTo(Try.success(1));
    }

    // -- Checked Functions

    @Test
    public void shouldCreateIdentityCheckedFunction() {
        assertThat(Function.identity()).isNotNull();
    }

    @Test
    public void shouldEnsureThatIdentityCheckedFunctionReturnsIdentity() throws Throwable {
        assertThat(Function.identity().apply(1)).isEqualTo(1);
    }

    @Test
    public void shouldNotTryToRecoverWhenItIsNotNeeded(){
        assertThat(Try.of(() -> OK).recoverWith(RuntimeException.class, (ex) -> failure()).get()).isEqualTo(OK);
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldReturnExceptionWhenRecoveryWasNotSuccess(){
        Try.of(() -> {throw error();}).recoverWith(IOException.class, (ex) -> failure()).get();
    }

    @Test
    public void shouldReturnRecoveredValue(){
        assertThat(Try.of(() -> {throw error();}).recoverWith(RuntimeException.class, (ex) -> success()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldHandleErrorDuringRecovering(){
        Try<?> t = Try.of(() -> {throw new IllegalArgumentException(OK);}).recoverWith(IOException.class, (ex) -> { throw new IllegalStateException(FAILURE);});
         assertThatThrownBy(t::get).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void shouldNotReturnRecoveredValueOnSuccess(){
        assertThat(Try.of(() -> OK).recoverWith(IOException.class, failure()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldReturnRecoveredValueOnFailure(){
        assertThat(Try.of(() -> {throw new IllegalStateException(FAILURE);}).recoverWith(IllegalStateException.class, success()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldExecuteAndFinallyOnSuccess(){
        MutableInteger count = new MutableInteger(0);

        Try.run(() -> count.setValue(0)).andFinally(() -> count.setValue(1));

        assertThat(count.getValue()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteAndFinallyTryOnSuccess(){
        MutableInteger count = new MutableInteger(0);

        Try.run(() -> count.setValue(0)).andFinallyTry(() -> count.setValue(1));

        assertThat(count.getValue()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteAndFinallyOnFailure(){
        MutableInteger count = new MutableInteger(0);

        Try.run(() -> {throw new IllegalStateException(FAILURE);})
            .andFinally(() -> count.setValue(1));

        assertThat(count.getValue()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteAndFinallyTryOnFailure(){
        MutableInteger count = new MutableInteger(0);

        Try.run(() -> {throw new IllegalStateException(FAILURE);})
            .andFinallyTry(() -> count.setValue(1));

        assertThat(count.getValue()).isEqualTo(1);
    }

    @Test
    public void shouldExecuteAndFinallyTryOnFailureWithFailure(){
        Try<Object> result = Try.of(() -> {throw new IllegalStateException(FAILURE);})
            .andFinallyTry(() -> {throw new IllegalStateException(FAILURE);});

        assertThat(result.isFailure());
    }

    // -- helpers

    private RuntimeException error() {
        return new RuntimeException("error");
    }

    private static <T> Try<T> failure() {
        return Try.failure(new RuntimeException());
    }

    private static <T, X extends Throwable> Try<T> failure(Class<X> exceptionType) {
        try {
            final X exception = exceptionType.getConstructor().newInstance();
            return Try.failure(exception);
        } catch (Throwable e) {
            throw new IllegalStateException("Error instantiating " + exceptionType, e);
        }
    }

    private <T> boolean filter(T t) {
        throw new RuntimeException("xxx");
    }

    private <T> Try<T> flatMap(T t) {
        throw new RuntimeException("xxx");
    }

    private <T> T map(T t) {
        throw new RuntimeException("xxx");
    }

    private Try<String> success() {
        return Try.of(() -> "ok");
    }

    private class MutableInteger {
        private int value;

        public MutableInteger(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    // -- spliterator

    @Test
    public void shouldHaveSizedSpliterator() {
        assertThat(of(1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED)).isTrue();
    }

    @Test
    public void shouldHaveOrderedSpliterator() {
        assertThat(of(1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
    }

    @Test
    public void shouldReturnSizeWhenSpliterator() {
        assertThat(of(1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
    }
}
