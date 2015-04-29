/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.Serializables;
import javaslang.Tuple;
import javaslang.algebra.CheckedMonad;
import javaslang.algebra.CheckedMonadLaws;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.CheckResultAssertions;
import javaslang.test.Gen;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

public class TryTest implements CheckedMonadLaws<Try<?>> {

    private static final String OK = "ok";
    private static final String FAILURE = "failure";

    // -- flatten

    @Test
    public void shouldThrowWhenFlatteningUnnestedSuccess() {
        assertThat(Try.of(() -> 1).flatten().isFailure()).isTrue();
    }

    @Test
    public void shouldFlattenSuccessOfSuccess() {
        assertThat(new Success<>(new Success<>(1)).flatten()).isEqualTo(new Success<>(1));
    }

    @Test
    public void shouldFlattenSuccessOfFailure() {
        assertThat(new Success<>(failure()).flatten()).isEqualTo(failure());
    }

    @Test
    public void shouldFlattenFailure() {
        assertThat(failure().flatten()).isEqualTo(failure());
    }

    // -- flatten(Function)

    static final Match<Try<Integer>> MATCH = Match
        .caze((Try<Integer> o) -> o)
        .caze((Integer i) -> new Success<>(i));

    @Test
    public void shouldFlattenUnnestedSuccessWithFunction() {
        assertThat(new Success<>(1)).isEqualTo(new Success<>(1));
    }

    @Test
    public void shouldFlattenSuccessOfSuccessWithFunction() {
        assertThat(new Success<>(new Success<>(1)).flatten(MATCH::apply)).isEqualTo(new Success<>(1));
    }

    @Test
    public void shouldFlattenSuccessOfFailureWithFunction() {
        assertThat(new Success<>(failure()).flatten(MATCH::apply)).isEqualTo(failure());
    }

    @Test
    public void shouldFlattenFailureWithFunction() {
        assertThat(failure().flatten(MATCH::apply)).isEqualTo(failure());
    }

    // -- treeMap

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCallingTreeMapWithNullMapperOnFailure() {
        failure().treeMap(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowWhenCallingTreeMapWithNullMapperOnSuccess() {
        success().treeMap(null);
    }

    @Test
    public void shouldTreeMapFailure() {
        assertThat(failure().treeMap(Try.CheckedFunction.identity())).isEqualTo(failure());
    }

    @Test
    public void shouldTreeMapSuccess() {
        // Success([Failure, Success("1")])
        final Try<javaslang.collection.List<Try<String>>> actual = Try.of(() -> javaslang.collection.List.of(failure(), new Success<>("1"))).treeMap(String::length);
        final Try<javaslang.collection.List<Try<Integer>>> expected = Try.of(() -> javaslang.collection.List.of(failure(), new Success<>(1)));
        assertThat(actual).isEqualTo(expected);
    }

    // -- exists

    @Test
    public void shouldBeAwareOfPropertyThatHoldsExistsOfSuccess() {
        assertThat(new Success<>(1).exists(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsExistsOfSuccess() {
        assertThat(new Success<>(1).exists(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsOfFailure() {
        assertThat(failure().exists(e -> true)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyExistsWhenPredicateThrows() {
        assertThat(new Success<>(1).exists(e -> { throw new Error("error"); })).isFalse();
    }

    // -- forall

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAllOfSuccess() {
        assertThat(new Success<>(1).forAll(i -> i == 1)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAllOfSuccess() {
        assertThat(new Success<>(1).forAll(i -> i == 2)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyForAllOfFailure() {
        assertThat(failure().forAll(e -> true)).isFalse();
    }

    @Test
    public void shouldNotHoldPropertyForAllWhenPredicateThrows() {
        assertThat(new Success<>(1).forAll(e -> { throw new Error("error"); })).isFalse();
    }

    // -- iterator

    @Test
    public void shouldReturnIteratorOfSuccess() {
        assertThat(new Success<>(1).iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfFailure() {
        assertThat(failure().iterator()).isNotNull();
    }

    // -- Try.of

    @Test
    public void shouldCreateFailureWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> 1) instanceof Success).isTrue();
    }

    @Test
    public void shouldCreateSuccessWhenCallingTryOfSupplier() {
        assertThat(Try.of(() -> {
            throw new Error("error");
        }) instanceof Failure).isTrue();
    }

    // -- Try.run

    @Test
    public void shouldCreateFailureWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
        }) instanceof Success).isTrue();
    }

    @Test
    public void shouldCreateSuccessWhenCallingTryRunRunnable() {
        assertThat(Try.run(() -> {
            throw new Error("error");
        }) instanceof Failure).isTrue();
    }

    // -- Failure.Cause

    @Test
    public void shouldDetectFatalException() throws Exception {
        final Failure.Cause cause = Failure.Cause.of(new OutOfMemoryError());
        assertThat(cause.isFatal()).isTrue();
    }

    @Test
    public void shouldDetectNonFatalException() throws Exception {
        final Failure.Cause cause = Failure.Cause.of(new StackOverflowError());
        assertThat(cause.isFatal()).isFalse();
    }

    @Test
    public void shouldUnapplyCause() {
        final Exception exception = new Exception();
        assertThat(Failure.Cause.of(exception).unapply()).isEqualTo(Tuple.of(exception));
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
        } catch (Throwable x) {
            Assertions.assertThat(x.getCause().getMessage()).isEqualTo("\uD83D\uDCA9");
        }
    }

    // -- Failure

    @Test
    public void shouldDetectFailureOfRunnable() {
        assertThat(Try.of(() -> {
            throw new RuntimeException();
        }).isFailure()).isTrue();
    }

    @Test(expected = Failure.Fatal.class)
    public void shouldPassThroughFatalException() {
        Try.of(() -> {
            throw new UnknownError();
        });
    }

    @Test
    public void shouldDetectFailureOnNonFatalException() {
        assertThat(failure().isFailure()).isTrue();
    }

    @Test
    public void shouldDetectNonSuccessOnFailure() {
        assertThat(failure().isSuccess()).isFalse();
    }

    @Test(expected = Failure.NonFatal.class)
    public void shouldThrowWhenGetOnFailure() {
        failure().get();
    }

    @Test
    public void shouldReturnElseWhenOrElseOnFailure() {
        assertThat(failure().orElse(OK)).isEqualTo(OK);
    }

    @Test
    public void shouldReturnElseWhenOrElseGetOnFailure() {
        assertThat(failure().orElseGet(x -> OK)).isEqualTo(OK);
    }

    @Test
    public void shouldRunElseWhenOrElseRunOnFailure() {
        final String[] result = new String[1];
        failure().orElseRun(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowOtherWhenOrElseThrowOnFailure() {
        failure().orElseThrow(x -> new IllegalStateException(OK));
    }

    @Test
    public void shouldRecoverOnFailure() {
        assertThat(failure().recover(x -> OK).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithOnFailure() {
        assertThat(TryTest.<String> failure().recoverWith(x -> success()).get()).isEqualTo(OK);
    }

    @Test
    public void shouldRecoverWithThrowingOnFailure() {
        final RuntimeException error = error();
        assertThat(failure().recoverWith(x -> {
            throw error;
        })).isEqualTo(new Failure<>(error));
    }

    @Test
    public void shouldConsumeThrowableWhenCallingOnFailureGivenFailure() {
        final String[] result = new String[]{FAILURE};
        failure().onFailure(x -> result[0] = OK);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldReturnNewFailureWhenCallingOnFailureAndThrowingGivenFailure() {
        final Try<Throwable> actual = failure().onFailure(x -> {
            throw new Error(OK);
        }).failed();
        assertThat(actual.get().getMessage()).isEqualTo(OK);
    }

    @Test
    public void shouldConvertFailureToOption() {
        assertThat(failure().toOption().isDefined()).isFalse();
    }

    @Test
    public void shouldConvertFailureToEither() {
        assertThat(failure().toEither().isLeft()).isTrue();
    }

    @Test
    public void shouldConvertFailureToJavaOptional() {
        assertThat(failure().toJavaOptional().isPresent()).isFalse();
    }

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
    public void shouldFlatMapOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(s -> Try.of(() -> s + "!"))).isEqualTo(actual);
    }

    @Test
    public void shouldFlatMapWithExceptionOnFailure() {
        final Try<String> actual = failure();
        assertThat(actual.flatMap(this::flatMap)).isEqualTo(actual);
    }

    @Test
    public void shouldForEachOnFailure() {
        final List<String> actual = new ArrayList<>();
        TryTest.<String> failure().forEach(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

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
    public void shouldCreateFailureOnNonFatalException() {
        assertThat(failure().failed().get().getClass().getName()).isEqualTo(RuntimeException.class.getName());
    }

    @Test
    public void shouldComposeFailureWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
            throw new Error("err1");
        }).andThen(() -> {
            throw new Error("err2");
        });
        final Try<Void> expected = new Failure<>(new Error("err1"));
        assertThat(actual).isEqualTo(expected);
    }

    // peek

    @Test
    public void shouldPeekFailure() {
        final List<Object> list = new ArrayList<>();
        assertThat(failure().peek(list::add)).isEqualTo(failure());
        assertThat(list.isEmpty()).isTrue();
    }

    // unapply

    @Test
    public void shouldUnapplyFailure() {
        final Try<?> failure = failure();
        assertThat(failure.unapply()).isEqualTo(Tuple.of(failure.failed().get()));
    }

    // equals

    @Test
    public void shouldEqualFailureIfObjectIsSame() {
        final Failure<?> success = new Failure<>(error());
        assertThat(success).isEqualTo(success);
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsNull() {
        assertThat(new Failure<>(error())).isNotNull();
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsOfDifferentType() {
        assertThat(new Failure<>(error()).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualFailure() {
        assertThat(new Failure<>(error())).isEqualTo(new Failure<>(error()));
    }

    // hashCode

    @Test
    public void shouldHashFailure() {
        final Throwable error = error();
        assertThat(new Failure<>(error).hashCode()).isEqualTo(Objects.hashCode(error));
    }

    // toString

    @Test
    public void shouldConvertFailureToString() {
        assertThat(new Failure<>(error()).toString()).isEqualTo("Failure(java.lang.RuntimeException: error)");
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeFailure() {
        final Object actual = Serializables.deserialize(Serializables.serialize(new Failure<>(error())));
        assertThat(actual.toString()).isEqualTo(new Failure<>(error()).toString());
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
    public void shouldOrElseOnSuccess() {
        assertThat(success().orElse(null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseGetOnSuccess() {
        assertThat(success().orElseGet(x -> null)).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseRunOnSuccess() {
        final String[] result = new String[]{OK};
        success().orElseRun(x -> result[0] = FAILURE);
        assertThat(result[0]).isEqualTo(OK);
    }

    @Test
    public void shouldOrElseThrowOnSuccess() {
        assertThat(success().orElseThrow(x -> null)).isEqualTo(OK);
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
        final String[] result = new String[]{OK};
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

    @Test(expected = Failure.NonFatal.class)
    public void shouldFilterNonMatchingPredicateOnSuccess() {
        success().filter(s -> false).get();
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

    @Test(expected = Failure.NonFatal.class)
    public void shouldMapWithExceptionOnSuccess() {
        success().map(s -> {
            throw new RuntimeException("xxx");
        }).get();
    }

    @Test(expected = Failure.NonFatal.class)
    public void shouldThrowWhenCallingFailedOnSuccess() {
        success().failed().get();
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenFailing() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
            throw new Error("failure");
        });
        final Try<Void> expected = new Failure<>(new Error("failure"));
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldComposeSuccessWithAndThenWhenSucceeding() {
        final Try<Void> actual = Try.run(() -> {
        }).andThen(() -> {
        });
        final Try<Void> expected = new Success<>(null);
        assertThat(actual).isEqualTo(expected);
    }

    // peek

    @Test
    public void shouldPeekSuccess() {
        final List<Object> list = new ArrayList<>();
        assertThat(success().peek(list::add)).isEqualTo(success());
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    public void shouldPeekSuccessAndThrow() {
        assertThat(success().peek(t -> { failure().get(); })).isEqualTo(failure());
    }

    // unapply

    @Test
    public void shouldUnapplySuccess() {
        assertThat(success().unapply()).isEqualTo(Tuple.of(success().get()));
    }

    // equals

    @Test
    public void shouldEqualSuccessIfObjectIsSame() {
        final Success<?> success = new Success<>(1);
        assertThat(success).isEqualTo(success);
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsNull() {
        assertThat(new Success<>(1)).isNotNull();
    }

    @Test
    public void shouldNotEqualSuccessIfObjectIsOfDifferentType() {
        assertThat(new Success<>(1).equals(new Object())).isFalse();
    }

    @Test
    public void shouldEqualSuccess() {
        assertThat(new Success<>(1)).isEqualTo(new Success<>(1));
    }

    // hashCode

    @Test
    public void shouldHashSuccess() {
        assertThat(new Success<>(1).hashCode()).isEqualTo(Objects.hashCode(1));
    }

    // toString

    @Test
    public void shouldConvertSuccessToString() {
        assertThat(new Success<>(1).toString()).isEqualTo("Success(1)");
    }

    // serialization

    @Test
    public void shouldSerializeDeserializeSuccess() {
        final Object actual = Serializables.deserialize(Serializables.serialize(new Success<>(1)));
        assertThat(actual).isEqualTo(new Success<>(1));
    }

    // -- helpers

    private RuntimeException error() {
        return new RuntimeException("error");
    }

    private static <T> Try<T> failure() {
        return Try.of(() -> {
            throw new RuntimeException();
        });
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

    // -- CheckedFunctor1Laws

    static final Arbitrary<Try<Integer>> TRIES = size -> random -> Gen.frequency(
            Tuple.of(1, Gen.<Try<Integer>>of(new Failure<>(new Error("test")))),
            Tuple.of(4, Gen.choose(-size, size).<Try<Integer>>map(Success::new))
    ).apply(random);

    @Test
    @Override
    public void shouldSatisfyCheckedFunctorIdentity() {
        final CheckResult result = checkCheckedFunctorIdentity(TRIES);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyCheckedFunctorComposition() {
        final Arbitrary<Try.CheckedFunction<? super Integer, ? extends Double>> before =
                size -> random -> Double::valueOf;
        final Arbitrary<Try.CheckedFunction<? super Double, ? extends String>> after =
                size -> random -> String::valueOf;
        final CheckResult result = checkCheckedFunctorComposition(TRIES, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    // -- CheckedMonad1Laws

    static final Arbitrary<Integer> INTEGERS = size -> random -> Gen.frequency(
            Tuple.of(1, Gen.of(null)),
            Tuple.of(4, Gen.choose(-size, size))
    ).apply(random);

    static <T> Try.CheckedFunction<? super T, ? extends CheckedMonad<T, Try<?>>> unit() {
        return i -> Try.of(() -> {
            if (i == null) {
                throw new Error("test");
            } else {
                return i;
            }
        });
    }

    static <T, R> CheckedMonad<R, Try<?>> mapTry(T t, Try.CheckedFunction<? super T, R> mapper) throws Throwable {
        return TryTest.<T>unit().apply(t).map(mapper::apply);
    }

    @Test
    @Override
    public void shouldSatisfyCheckedMonadLeftIdentity() {
        final Arbitrary<Try.CheckedFunction<? super Integer, ? extends CheckedMonad<String, Try<?>>>> mappers =
                size -> random -> i -> TryTest.mapTry(i, String::valueOf);
        final CheckResult result = checkCheckedMonadLeftIdentity(TryTest.<Integer>unit(), INTEGERS, mappers);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyCheckedMonadRightIdentity() {
        final CheckResult result = checkCheckedMonadRightIdentity(TryTest.<Integer>unit(), TRIES);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }

    @Test
    @Override
    public void shouldSatisfyCheckedMonadAssociativity() {
        final Arbitrary<Try.CheckedFunction<? super Integer, ? extends CheckedMonad<Double, Try<?>>>> before =
                size -> random -> i -> TryTest.mapTry(i, Double::valueOf);
        final Arbitrary<Try.CheckedFunction<? super Double, ? extends CheckedMonad<String, Try<?>>>> after =
                size -> random -> d -> TryTest.mapTry(d, String::valueOf);
        final CheckResult result = checkCheckedMonadAssociativity(TRIES, before, after);
        CheckResultAssertions.assertThat(result).isSatisfiedWithExhaustion(false);
    }
}
