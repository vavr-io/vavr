/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.AbstractValueTest;
import javaslang.Serializables;
import javaslang.collection.Seq;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.fail;

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
    public void shouldCreateSuccessWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> 1) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
    }

    // -- Try.run

    @Test
    public void shouldCreateSuccessWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
        }) instanceof Try.Success).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
            throw new Error("error");
        }) instanceof Try.Failure).isTrue();
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
        assertThat(Try.NonFatalException.of(cause) instanceof Try.NonFatalException).isTrue();
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

    @Test(expected = Try.NonFatalException.class)
    public void shouldFilterNonMatchingPredicateOnSuccess() {
        success().filter(s -> false).get();
    }

    @Test
    public void shouldFilterNonMatchingPredicateAndDefaultThrowableSupplierOnSuccess()
    {
        assertThat(success().filter(s -> false).getCause())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldFilterNonMatchingPredicateAndCustomThrowableSupplierOnSuccess()
    {
        assertThat(success().filter(s -> false, () -> new IllegalArgumentException()).getCause())
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

    // -- helpers

    private RuntimeException error() {
        return new RuntimeException("error");
    }

    private static <T> Try<T> failure() {
        return Try.failure(new RuntimeException());
    }

    private static <T, X extends Throwable> Try<T> failure(Class<X> exceptionType) {
        try {
            final X exception = exceptionType.newInstance();
            return Try.failure(exception);
        } catch (InstantiationException | IllegalAccessException x) {
            throw new IllegalStateException("Error instantiating " + exceptionType);
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
}
