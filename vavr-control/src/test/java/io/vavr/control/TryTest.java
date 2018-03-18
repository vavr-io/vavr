/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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

import org.junit.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TryTest {

    // -- Testees

    private static final String SUCCESS_VALUE = "success";
    private static final Try<String> SUCCESS = Try.success(SUCCESS_VALUE);

    private static final Exception FAILURE_CAUSE = new IllegalStateException("failure");
    private static final Try<String> FAILURE = Try.failure(FAILURE_CAUSE);

    // ---- alternate errors, e.g. when chaining a failure

    private static final Error ERROR = new Error();

    // ---- unexpected behavior like running unexpected code

    private static final AssertionError ASSERTION_ERROR = new AssertionError("unexpected");

    // ---- rethrown fatal errors

    private static final LinkageError LINKAGE_ERROR = new LinkageError();

    private static final ThreadDeath THREAD_DEATH = new ThreadDeath();

    private static final VirtualMachineError VM_ERROR = new VirtualMachineError() {
        private static final long serialVersionUID = 1L;
    };

    // -- static .of(Callable)

    @Test
    public void shouldCreateSuccessWhenCallingTryOfWithNullValue() {
        assertThat(Try.of(() -> null)).isNotNull();
    }

    @Test
    public void shouldCreateSuccessWhenCallingTryOfCallable() {
        assertThat(Try.of(() -> SUCCESS_VALUE).isSuccess()).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryOfCallable() {
        assertThat(Try.of(() -> { throw FAILURE_CAUSE; }).isFailure()).isTrue();
    }

    @Test
    public void shouldThrowNPEWhenCallingTryOfCallable() {
        assertThatThrownBy(() -> Try.of(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("callable is null");
    }

    @Test
    public void shouldRethrowLinkageErrorWhenCallingTryOfCallable() {
        assertThatThrownBy(() -> Try.of(() -> { throw LINKAGE_ERROR; })).isSameAs(LINKAGE_ERROR);
    }

    @Test
    public void shouldRethrowThreadDeathWhenCallingTryOfCallable() {
        assertThatThrownBy(() -> Try.of(() -> { throw THREAD_DEATH; })).isSameAs(THREAD_DEATH);
    }

    @Test
    public void shouldRethrowVirtualMachoneErrorWhenCallingTryOfCallable() {
        assertThatThrownBy(() -> Try.of(() -> { throw VM_ERROR; })).isSameAs(VM_ERROR);
    }

    @Test
    public void shouldBeIndistinguishableWhenCreatingFailureWithOfFactoryOrWithFailureFactory() {
        final Try<?> failure1 = Try.of(() -> { throw FAILURE_CAUSE; });
        final Try<?> failure2 = Try.failure(FAILURE_CAUSE);
        assertThatThrownBy(failure1::get)
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Failure.get()")
                .hasCause(FAILURE_CAUSE);
        assertThatThrownBy(failure2::get)
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Failure.get()")
                .hasCause(FAILURE_CAUSE);
        assertThat(failure1.getCause()).isSameAs(failure2.getCause());
        assertThat(failure1.isFailure()).isEqualTo(failure2.isFailure());
        assertThat(failure1.isSuccess()).isEqualTo(failure2.isSuccess());
        assertThat(failure1.equals(failure2)).isTrue();
        assertThat(failure1.hashCode()).isEqualTo(failure2.hashCode());
        assertThat(failure1.toString()).isEqualTo(failure2.toString());
    }

    @Test
    public void shouldBeIndistinguishableWhenCreatingSuccessWithOfFactoryOrWithSuccessFactory() {
        final Try<?> success1 = Try.of(() -> SUCCESS_VALUE);
        final Try<?> success2 = Try.success(SUCCESS_VALUE);
        assertThat(success1.get()).isSameAs(success2.get());
        assertThatThrownBy(success1::getCause).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(success2::getCause).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThat(success1.isFailure()).isEqualTo(success2.isFailure());
        assertThat(success1.isSuccess()).isEqualTo(success2.isSuccess());
        assertThat(success1.equals(success2)).isTrue();
        assertThat(success1.hashCode()).isEqualTo(success2.hashCode());
        assertThat(success1.toString()).isEqualTo(success2.toString());
    }

    // -- static .run(CheckedRunnable)

    @Test
    public void shouldCreateSuccessWhenCallingTryRunCheckedRunnable() {
        assertThat(Try.run(() -> {}).isSuccess()).isTrue();
    }

    @Test
    public void shouldCreateFailureWhenCallingTryRunCheckedRunnable() {
        assertThat(Try.run(() -> { throw ERROR; }).isFailure()).isTrue();
    }

    @Test
    public void shouldThrowNPEWhenCallingTryRunCheckedRunnable() {
        assertThatThrownBy(() -> Try.run(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("runnable is null");
    }

    @Test
    public void shouldRethrowLinkageErrorWhenCallingTryRunCheckedRunnable() {
        assertThatThrownBy(() -> Try.run(() -> { throw LINKAGE_ERROR; })).isSameAs(LINKAGE_ERROR);
    }

    @Test
    public void shouldRethrowThreadDeathWhenCallingTryRunCheckedRunnable() {
        assertThatThrownBy(() -> Try.run(() -> { throw THREAD_DEATH; })).isSameAs(THREAD_DEATH);
    }

    @Test
    public void shouldRethrowVirtualMachineErrorWhenCallingTryRunCheckedRunnable() {
        assertThatThrownBy(() -> Try.run(() -> { throw VM_ERROR; })).isSameAs(VM_ERROR);
    }

    // -- static .success(Object)

    @Test
    public void shouldCreateSuccessWithNullValue() {
        assertThat(Try.success(null)).isNotNull();
    }

    @Test
    public void shouldCreateSuccess() {
        assertThat(Try.success(SUCCESS_VALUE)).isNotNull();
    }

    @Test
    public void shouldVerifyBasicSuccessProperties() {
        final Try<?> success = Try.success(SUCCESS_VALUE);
        assertThat(success.get()).isSameAs(SUCCESS_VALUE);
        assertThatThrownBy(success::getCause).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThat(success.isFailure()).isFalse();
        assertThat(success.isSuccess()).isTrue();
        assertThat(success.equals(SUCCESS)).isTrue();
        assertThat(success.hashCode()).isEqualTo(Objects.hashCode(SUCCESS_VALUE));
        assertThat(success.toString()).isEqualTo("Success(" + SUCCESS_VALUE + ")");
    }

    // -- static .failure(Throwable)

    @Test
    public void shouldCreateFailureWithNullValue() {
        assertThat(Try.failure(null)).isNotNull();
    }

    @Test
    public void shouldCreateFailure() {
        assertThat(Try.failure(FAILURE_CAUSE)).isNotNull();
    }

    @Test
    public void shouldVerifyBasicFailureProperties() {
        final Try<?> failure = Try.failure(FAILURE_CAUSE);
        assertThatThrownBy(failure::get).hasCause(FAILURE_CAUSE);
        assertThat(failure.getCause()).isSameAs(FAILURE_CAUSE);
        assertThat(failure.isSuccess()).isFalse();
        assertThat(failure.isFailure()).isTrue();
        assertThat(failure.equals(FAILURE)).isTrue();
        assertThat(failure.hashCode()).isEqualTo(Objects.hashCode(FAILURE_CAUSE));
        assertThat(failure.toString()).isEqualTo("Failure(" + FAILURE_CAUSE + ")");
    }

    @Test
    public void shouldRethrowLinkageErrorWhenCallingTryFailure() {
        assertThatThrownBy(() -> Try.failure(LINKAGE_ERROR)).isSameAs(LINKAGE_ERROR);
    }

    @Test
    public void shouldRethrowThreadDeathWhenCallingTryFailure() {
        assertThatThrownBy(() -> Try.failure(THREAD_DEATH)).isSameAs(THREAD_DEATH);
    }

    @Test
    public void shouldRethrowVirtualMachineErrorWhenCallingTryFailure() {
        assertThatThrownBy(() -> Try.failure(VM_ERROR)).isSameAs(VM_ERROR);
    }

    // -- .failed()

    @Test
    public void shouldInvertSuccessByCallingFailed() {
        final Try<?> testee = SUCCESS.failed();
        // we check it manually because exceptions generally don't override equals()
        assertThat(testee.isFailure()).isTrue();
        assertThat(testee.getCause()).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThat(testee.getCause()).hasMessage("Success.failed()");
    }

    @Test
    public void shouldInvertSuccessWithNullValueByCallingFailed() {
        assertThat(Try.success(null).failed()).isNotNull();
    }

    @Test
    public void shouldInvertFailureByCallingFailed() {
        assertThat(FAILURE.failed()).isEqualTo(Try.success(FAILURE_CAUSE));
    }

    @Test
    public void shouldInvertFailureWithNullCauseByCallingFailed() {
        assertThat(Try.failure(null).failed()).isNotNull();
    }

    // -- .filter(CheckedPredicate)

    @Test
    public void shouldFilterMatchingPredicateOnFailure() {
        assertThat(FAILURE.filter(s -> true)).isSameAs(FAILURE);
    }

    @Test
    public void shouldFilterNonMatchingPredicateOnFailure() {
        assertThat(FAILURE.filter(s -> false)).isSameAs(FAILURE);
    }

    @Test
    public void shouldFilterWithExceptionOnFailure() {
        assertThat(FAILURE.filter(t -> { throw ERROR; })).isSameAs(FAILURE);
    }

    @Test
    public void shouldFilterMatchingPredicateOnSuccess() {
        assertThat(SUCCESS.filter(s -> true)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldFilterNonMatchingPredicateOnSuccess() {
        final String message = "Predicate does not hold for " + SUCCESS_VALUE;
        final Try<String> testee = SUCCESS.filter(s -> false);
        assertThat(testee.isFailure()).isTrue();
        assertThat(testee.getCause())
                .isExactlyInstanceOf(NoSuchElementException.class)
                .hasMessage(message);
    }

    @Test
    public void shouldFilterWithExceptionOnSuccess() {
        final Try<String> testee = SUCCESS.filter(t -> { throw ERROR; });
        assertThat(testee.isFailure()).isTrue();
        assertThat(testee.getCause()).isSameAs(ERROR);
    }

    @Test
    public void shouldThrowNPEWhenFilteringFailureWithNullPredicate() {
        assertThatThrownBy(() -> FAILURE.filter(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("predicate is null");
    }

    @Test
    public void shouldThrowNPEWhenFilteringSuccessWithNullPredicate() {
        assertThatThrownBy(() -> SUCCESS.filter(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("predicate is null");
    }

    @Test
    public void shouldFilterFailureWithNullCause() {
        assertThat(Try.failure(null).filter(x -> true)).isNotNull();
    }

    @Test
    public void shouldFilterSuccessWithNullValue() {
        assertThat(Try.success(null).filter(x -> true)).isNotNull();
    }

    // -- .flatMap(CheckedFunction)

    @Test
    public void shouldFlatMapSuccessToNull() {
        assertThat(SUCCESS.flatMap(ignored -> null)).isNull();
    }

    @Test
    public void shouldFlatMapToSuccessOnSuccess() {
        assertThat(SUCCESS.flatMap(ignored -> SUCCESS)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldFlatMapToFailureOnSuccess() {
        assertThat(SUCCESS.flatMap(ignored -> FAILURE)).isSameAs(FAILURE);
    }

    @Test
    public void shouldFlatMapOnFailure() {
        assertThat(FAILURE.flatMap(ignored -> SUCCESS)).isSameAs(FAILURE);
    }

    @Test
    public void shouldCaptureExceptionWhenFlatMappingSuccess() {
        assertThat(SUCCESS.flatMap(ignored -> { throw ERROR; })).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldIgnoreExceptionWhenFlatMappingFailure() {
        assertThat(FAILURE.flatMap(ignored -> { throw ERROR; })).isSameAs(FAILURE);
    }

    @Test
    public void shouldThrowNPEWhenFlatMappingFailureWithNullParam() {
        assertThatThrownBy(() -> FAILURE.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    @Test
    public void shouldThrowNPEWhenFlatMappingSuccessWithNullParam() {
        assertThatThrownBy(() -> SUCCESS.flatMap(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    @Test
    public void shouldFlatMapFailureWithNullCause() {
        assertThat(Try.failure(null).flatMap(x -> null)).isNotNull();
    }

    @Test
    public void shouldFlatMapSuccessWithNullValue() {
        assertThat(Try.success(null).flatMap(s -> SUCCESS)).isSameAs(SUCCESS);
    }

    // -- .fold(Function, Function)

    @Test
    public void shouldFoldFailureWhenCauseIsNull() {
        assertThat(Try.failure(null).<Integer> fold(x -> 0, s -> 1)).isEqualTo(0);
    }

    @Test
    public void shouldFoldSuccessWhenValueIsNull() {
        assertThat(Try.success(null).<Integer> fold(x -> 0, s -> 1)).isEqualTo(1);
    }

    @Test
    public void shouldFoldFailureToNull() {
        assertThat(FAILURE.<Object> fold(x -> null, s -> "")).isSameAs(null);
    }

    @Test
    public void shouldFoldSuccessToNull() {
        assertThat(SUCCESS.<Object> fold(x -> "", s -> null)).isSameAs(null);
    }

    @Test
    public void shouldFoldAndReturnValueIfSuccess() {
        final int folded = SUCCESS.fold(x -> { throw ASSERTION_ERROR; }, String::length);
        assertThat(folded).isEqualTo(SUCCESS_VALUE.length());
    }

    @Test
    public void shouldFoldAndReturnAlternateValueIfFailure() {
        final String folded = FAILURE.fold(x -> SUCCESS_VALUE, a -> { throw ASSERTION_ERROR; });
        assertThat(folded).isEqualTo(SUCCESS_VALUE);
    }

    @Test
    public void shouldFoldAndThrowNPEOnWhenOnFailureFunctionIsNullIfSuccess() {
        assertThatThrownBy(() -> SUCCESS.fold(null, Function.identity()))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onFailure is null");
    }

    @Test
    public void shouldFoldAndThrowNPEOnWhenOnFailureFunctionIsNullIfFailure() {
        assertThatThrownBy(() -> FAILURE.fold(null, Function.identity()))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onFailure is null");
    }

    @Test
    public void shouldFoldAndThrowNPEOnWhenOnSuccessFunctionIsNullIfSuccess() {
        assertThatThrownBy(() -> SUCCESS.fold(Function.identity(), null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onSuccess is null");
    }

    @Test
    public void shouldFoldAndThrowNPEOnWhenOnSuccessFunctionIsNullIfFailure() {
        assertThatThrownBy(() -> FAILURE.fold(Function.identity(), null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onSuccess is null");
    }

    // -- .get()

    @Test
    public void shouldGetOnSuccessWhenValueIsNull() {
        assertThat(Try.success(null).get()).isSameAs(null);
    }

    @Test
    public void shouldThrowCauseWrappedInRuntimeExceptionWhenGetOnFailure() {
        assertThatThrownBy(FAILURE::get)
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Failure.get()")
                .hasCause(FAILURE_CAUSE);
    }

    @Test
    public void shouldThrowNullCauseWrappedInRuntimeExceptionWhenGetOnFailure() {
        assertThatThrownBy(() -> Try.failure(null).get())
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessage("Failure.get()")
                .hasCause(null);
    }

    @Test
    public void shouldGetOnSuccess() {
        assertThat(SUCCESS.get()).isEqualTo(SUCCESS_VALUE);
    }

    // -- .getCause()

    @Test
    public void shouldGetCauseOnFailureWhenCauseIsNull() {
        assertThat(Try.failure(null).getCause()).isSameAs(null);
    }
    
    @Test
    public void shouldGetCauseOnFailure() {
        assertThat(FAILURE.getCause()).isSameAs(FAILURE_CAUSE);
    }

    @Test
    public void shouldThrowWhenCallingGetCauseOnSuccess() {
        assertThatThrownBy(SUCCESS::getCause)
                .isExactlyInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Success.getCause()");
    }

    // -- .getOrElse(Object)

    @Test
    public void shouldReturnElseWhenOrElseOnFailure() {
        assertThat(FAILURE.getOrElse(SUCCESS_VALUE)).isEqualTo(SUCCESS_VALUE);
    }

    @Test
    public void shouldGetOrElseOnSuccess() {
        assertThat(SUCCESS.getOrElse(null)).isEqualTo(SUCCESS_VALUE);
    }

    // -- .getOrElseGet(Supplier)

    @Test
    public void shouldReturnElseWhenOrElseGetOnFailure() {
        assertThat(FAILURE.getOrElseGet(() -> SUCCESS_VALUE)).isEqualTo(SUCCESS_VALUE);
    }

    @Test
    public void shouldOrElseGetOnSuccess() {
        assertThat(SUCCESS.getOrElseGet(() -> null)).isEqualTo(SUCCESS_VALUE);
    }

    // -- .getOrElseThrow(Function)

    @Test
    public void shouldThrowOtherWhenGetOrElseThrowOnFailure() {
        assertThatThrownBy(() -> FAILURE.getOrElseThrow(x -> ERROR)).isSameAs(ERROR);
    }

    @Test
    public void shouldOrElseThrowOnSuccess() {
        assertThat(SUCCESS.getOrElseThrow(x -> null)).isEqualTo(SUCCESS_VALUE);
    }

    // -- .isFailure()

    @Test
    public void shouldDetectFailureIfFailure() {
        assertThat(FAILURE.isFailure()).isTrue();
    }

    @Test
    public void shouldDetectNonFailureIfSuccess() {
        assertThat(SUCCESS.isFailure()).isFalse();
    }

    // -- .isSuccess()

    @Test
    public void shouldDetectSuccessIfSuccess() {
        assertThat(SUCCESS.isSuccess()).isTrue();
    }

    @Test
    public void shouldDetectNonSuccessIfSuccess() {
        assertThat(FAILURE.isSuccess()).isFalse();
    }

    // -- .iterator()

    @Test
    public void shouldReturnIteratorOfSuccess() {
        assertThat(SUCCESS.iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorOfFailure() {
        assertThat(FAILURE.iterator()).isNotNull();
    }

    // -- .map(CheckedFunction)

    @Test
    public void shouldMapOnFailure() {
        assertThat(FAILURE.map(s -> s + "!")).isSameAs(FAILURE);
    }

    @Test
    public void shouldMapWithExceptionOnFailure() {
        assertThat(FAILURE.map(ignored -> { throw ERROR; })).isSameAs(FAILURE);
    }

    @Test
    public void shouldMapOnSuccess() {
        assertThat(SUCCESS.map(s -> s + "!")).isEqualTo(Try.success(SUCCESS_VALUE + "!"));
    }

    @Test
    public void shouldMapOnSuccessWhenValueIsNull() {
        assertThat(Try.success(null).map(s -> s + "!")).isEqualTo(Try.success("null!"));
    }

    @Test
    public void shouldMapWithExceptionOnSuccess() {
        assertThat(SUCCESS.map(ignored -> { throw ERROR; })).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldThrowNPEWhenMappingFailureAndParamIsNull() {
        assertThatThrownBy(() -> FAILURE.map(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    @Test
    public void shouldThrowNPEWhenMappingSuccessAndParamIsNull() {
        assertThatThrownBy(() -> SUCCESS.map(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    // -- .mapFailure(CheckedFunction)

    @Test
    public void shouldMapFailureOnFailure() {
        assertThat(FAILURE.mapFailure(x -> ERROR)).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldMapFailureOnFailureWhenCauseIsNull() {
        assertThat(Try.failure(null).mapFailure(x -> ERROR)).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldMapFailureWithExceptionOnFailure() {
        assertThat(FAILURE.mapFailure(x -> { throw ERROR; })).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldMapFailureOnSuccess() {
        assertThat(SUCCESS.mapFailure(x -> ERROR)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldMapFailureWithExceptionOnSuccess() {
        assertThat(SUCCESS.mapFailure(x -> { throw ERROR; })).isSameAs(SUCCESS);
    }

    @Test
    public void shouldThrowNPEWhenCallingMapFailureOnFailureAndParamIsNull() {
        assertThatThrownBy(() -> FAILURE.mapFailure(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    @Test
    public void shouldThrowNPEWhenCallingMapFailureOnSuccessAndParamIsNull() {
        assertThatThrownBy(() -> SUCCESS.mapFailure(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("mapper is null");
    }

    // -- .onFailure(Consumer)

    @Test
    public void shouldConsumeThrowableWhenCallingOnFailureGivenFailure() {
        final List<Throwable> sideEffect = new ArrayList<>();
        FAILURE.onFailure(sideEffect::add);
        assertThat(sideEffect).isEqualTo(Collections.singletonList(FAILURE_CAUSE));
    }

    @Test
    public void shouldNotHandleUnexpectedExceptionWhenCallingOnFailureGivenFailure() {
        assertThatThrownBy(() -> FAILURE.onFailure(ignored -> { throw ERROR; })).isSameAs(ERROR);
    }

    @Test
    public void shouldDoNothingWhenCallingOnFailureGivenSuccess() {
        assertThat(SUCCESS.onFailure(x -> { throw ERROR; })).isSameAs(SUCCESS);
    }

    @Test
    public void shouldThrowNPEWhenCallingOnFailureWithNullParamOnFailure() {
        assertThatThrownBy(() -> FAILURE.onFailure(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("action is null");
    }
    
    @Test
    public void shouldThrowNPEWhenCallingOnFailureWithNullParamOnSuccess() {
        assertThatThrownBy(() -> SUCCESS.onFailure(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("action is null");
    }

    // -- .onSuccess(Consumer)

    @Test
    public void shouldConsumeValueWhenCallingOnSuccessGivenSuccess() {
        final List<String> sideEffect = new ArrayList<>();
        SUCCESS.onSuccess(sideEffect::add);
        assertThat(sideEffect).isEqualTo(Collections.singletonList(SUCCESS_VALUE));
    }

    @Test
    public void shouldNotHandleUnexpectedExceptionWhenCallingOnSuccessGivenSuccess() {
        assertThatThrownBy(() -> SUCCESS.onSuccess(ignored -> { throw ERROR; })).isSameAs(ERROR);
    }

    @Test
    public void shouldDoNothingWhenCallingOnSuccessGivenFailure() {
        assertThat(FAILURE.onSuccess(x -> { throw ERROR; })).isSameAs(FAILURE);
    }

    @Test
    public void shouldThrowNPEWhenCallingOnSuccessWithNullParamOnFailure() {
        assertThatThrownBy(() -> FAILURE.onSuccess(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("action is null");
    }

    @Test
    public void shouldThrowNPEWhenCallingOnSuccessWithNullParamOnSuccess() {
        assertThatThrownBy(() -> SUCCESS.onSuccess(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("action is null");
    }

    // -- .orElse(Callable)

    @Test
    public void shouldReturnSelfOnOrElseIfSuccess() {
        assertThat(SUCCESS.orElse(() -> null)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldReturnAlternativeOnOrElseIfFailure() {
        assertThat(FAILURE.orElse(() -> SUCCESS)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldCaptureErrorOnOrElseIfFailure() {
        assertThat(FAILURE.orElse(() -> { throw ERROR; }).getCause()).isSameAs(ERROR);
    }

    @Test
    public void shouldThrowNPEOnOrElseWithNullParameterIfSuccess() {
        assertThatThrownBy(() -> SUCCESS.orElse(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("supplier is null");
    }

    @Test
    public void shouldThrowNPEOnOrElseWithNullParameterIfFailure() {
        assertThatThrownBy(() -> FAILURE.orElse(null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("supplier is null");
    }

    // -- .recover(Class, CheckedFunction)

    @Test
    public void shouldRecoverWhenFailureMatchesExactly() {
        assertThat(FAILURE.recover(FAILURE_CAUSE.getClass(), x -> SUCCESS_VALUE)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldRecoverWhenFailureIsAssignableFrom() {
        assertThat(FAILURE.recover(Throwable.class, x -> SUCCESS_VALUE)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldNotRecoverWhenFailureIsNotAssignableFrom() {
        assertThat(FAILURE.recover(VirtualMachineError.class, x -> SUCCESS_VALUE)).isSameAs(FAILURE);
    }

    @Test
    public void shouldRecoverWhenSuccess() {
        assertThat(SUCCESS.recover(Throwable.class, x -> null)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldThrowNPEOnRecoverFailureWhenExceptionTypeIsNull() {
        assertThatThrownBy(() -> FAILURE.recover(null, x -> null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("exceptionType is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverFailureWhenRecoveryFunctionIsNull() {
        assertThatThrownBy(() -> FAILURE.recover(Error.class, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("recoveryFunction is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverSuccessWhenExceptionTypeIsNull() {
        assertThatThrownBy(() -> SUCCESS.recover(null, x -> null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("exceptionType is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverSuccessWhenRecoveryFunctionIsNull() {
        assertThatThrownBy(() -> SUCCESS.recover(Error.class, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("recoveryFunction is null");
    }

    // -- .recoverWith(Class, CheckedFunction)

    @Test
    public void shouldRecoverWithWhenFailureMatchesExactly() {
        assertThat(FAILURE.recoverWith(FAILURE_CAUSE.getClass(), x -> SUCCESS)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldRecoverWithSuccessWhenFailureIsAssignableFrom() {
        assertThat(FAILURE.recoverWith(Throwable.class, x -> SUCCESS)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldRecoverWithFailureWhenFailureIsAssignableFrom() {
        final Try<String> failure = Try.failure(ERROR);
        assertThat(FAILURE.recoverWith(Throwable.class, x -> failure)).isEqualTo(failure);
    }

    @Test
    public void shouldNotRecoverWithWhenFailureIsNotAssignableFrom() {
        assertThat(FAILURE.recoverWith(VirtualMachineError.class, x -> SUCCESS)).isSameAs(FAILURE);
    }

    @Test
    public void shouldRecoverWithWhenSuccess() {
        assertThat(SUCCESS.recoverWith(Throwable.class, x -> null)).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldThrowNPEOnRecoverWithFailureWhenExceptionTypeIsNull() {
        assertThatThrownBy(() -> FAILURE.recoverWith(null, x -> null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("exceptionType is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverWithFailureWhenRecoveryFunctionIsNull() {
        assertThatThrownBy(() -> FAILURE.recoverWith(Error.class, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("recoveryFunction is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverWithSuccessWhenExceptionTypeIsNull() {
        assertThatThrownBy(() -> SUCCESS.recoverWith(null, x -> null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("exceptionType is null");
    }

    @Test
    public void shouldThrowNPEOnRecoverWithSuccessWhenRecoveryFunctionIsNull() {
        assertThatThrownBy(() -> SUCCESS.recoverWith(Error.class, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("recoveryFunction is null");
    }

    // -- .toOptional()

    @Test
    public void shouldConvertFailureToOptional() {
        assertThat(FAILURE.toOptional()).isEqualTo(Optional.empty());
    }


    @Test
    public void shouldConvertSuccessOfNonNullToOptional() {
        assertThat(SUCCESS.toOptional()).isEqualTo(Optional.of(SUCCESS_VALUE));
    }

    @Test
    public void shouldConvertSuccessOfNullToOptional() {
        assertThat(Try.success(null).toOptional()).isEqualTo(Optional.empty());
    }

    // -- .transform(CheckedFunction, CheckedFunction)

    @Test
    public void shouldTransformFailureWhenCauseIsNull() {
        assertThat(Try.failure(null).transform(x -> SUCCESS, s -> SUCCESS)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldTransformSuccessWhenValueIsNull() {
        assertThat(Try.success(null).transform(x -> SUCCESS, s -> SUCCESS)).isSameAs(SUCCESS);
    }

    @Test
    public void shouldTransformFailureToNull() {
        assertThat(FAILURE.transform(x -> null, s -> SUCCESS)).isSameAs(null);
    }

    @Test
    public void shouldTransformSuccessToNull() {
        assertThat(SUCCESS.transform(x -> FAILURE, s -> null)).isSameAs(null);
    }

    @Test
    public void shouldTransformAndReturnValueIfSuccess() {
        final Try<Integer> transformed = SUCCESS.transform(x -> { throw ASSERTION_ERROR; }, s -> Try.success(s.length()));
        assertThat(transformed).isEqualTo(Try.success(SUCCESS_VALUE.length()));
    }

    @Test
    public void shouldTransformAndReturnAlternateValueIfFailure() {
        final Try<String> transformed = FAILURE.transform(x -> SUCCESS, a -> { throw ASSERTION_ERROR; });
        assertThat(transformed).isSameAs(SUCCESS);
    }

    @Test
    public void shouldTransformAndThrowNPEOnWhenOnFailureFunctionIsNullIfSuccess() {
        assertThatThrownBy(() -> SUCCESS.transform(null, s -> SUCCESS))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onFailure is null");
    }

    @Test
    public void shouldTransformAndThrowNPEOnWhenOnFailureFunctionIsNullIfFailure() {
        assertThatThrownBy(() -> FAILURE.transform(null, s -> SUCCESS))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onFailure is null");
    }

    @Test
    public void shouldTransformAndThrowNPEOnWhenOnSuccessFunctionIsNullIfSuccess() {
        assertThatThrownBy(() -> SUCCESS.transform(x -> FAILURE, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onSuccess is null");
    }

    @Test
    public void shouldTransformAndThrowNPEOnWhenOnSuccessFunctionIsNullIfFailure() {
        assertThatThrownBy(() -> FAILURE.transform(x -> FAILURE, null))
                .isExactlyInstanceOf(NullPointerException.class)
                .hasMessage("onSuccess is null");
    }

    @Test
    public void shouldTransformFailureAndCaptureException() {
        final Try<String> transformed = FAILURE.transform(x -> { throw ERROR; }, s -> SUCCESS);
        assertThat(transformed).isEqualTo(Try.failure(ERROR));
    }

    @Test
    public void shouldTransformSuccessAndCaptureException() {
        final Try<String> transformed = SUCCESS.transform(x -> FAILURE, s -> { throw ERROR; });
        assertThat(transformed).isEqualTo(Try.failure(ERROR));
    }

    // -- Object.equals(Object)

    @Test
    public void shouldEqualFailureIfObjectIsSame() {
        assertThat(FAILURE).isEqualTo(FAILURE);
    }

    @Test
    public void shouldNotEqualFailureIfObjectIsNotSame() {
        assertThat(Try.failure(new Error())).isNotEqualTo(Try.failure(new Error()));
    }

    @Test
    public void shouldEqualSuccessIfObjectIsSame() {
        assertThat(SUCCESS).isEqualTo(SUCCESS);
    }

    @Test
    public void shouldEqualSuccessIfObjectIsNotSame() {
        assertThat(Try.success(1)).isEqualTo(Try.success(1));
    }

    @Test
    public void shouldNotEqualSuccessIfValueTypesDiffer() {
        assertThat(Try.success(1)).isNotEqualTo(Try.success("1"));
    }

    // -- Object.hashCode()

    @Test
    public void shouldHashFailure() {
        assertThat(FAILURE.hashCode()).isEqualTo(Objects.hashCode(FAILURE_CAUSE));
    }

    @Test
    public void shouldHashFailureWithNullCause() {
        assertThat(Try.failure(null).hashCode()).isEqualTo(Objects.hashCode(null));
    }

    @Test
    public void shouldHashSuccess() {
        assertThat(SUCCESS.hashCode()).isEqualTo(Objects.hashCode(SUCCESS_VALUE));
    }

    @Test
    public void shouldHashSuccessWithNullValue() {
        assertThat(Try.success(null).hashCode()).isEqualTo(Objects.hashCode(null));
    }

    // -- Object.toString()

    @Test
    public void shouldConvertFailureToString() {
        assertThat(FAILURE.toString()).isEqualTo("Failure(" + FAILURE_CAUSE + ")");
    }

    @Test
    public void shouldConvertFailureWithNullCauseToString() {
        assertThat(Try.failure(null).toString()).isEqualTo("Failure(null)");
    }

    @Test
    public void shouldConvertSuccessToString() {
        assertThat(SUCCESS.toString()).isEqualTo("Success(" + SUCCESS_VALUE + ")");
    }

    @Test
    public void shouldConvertSuccessWithNullValueToString() {
        assertThat(Try.success(null).toString()).isEqualTo("Success(null)");
    }
}
