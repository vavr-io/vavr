/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
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
package io.vavr;

import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for lazy evaluation in For comprehensions.
 * These tests demonstrate that the nested For approach short-circuits
 * when encountering None/Left/Failure, avoiding unnecessary computations.
 * Uses counter-based verification to confirm that subsequent steps are not executed
 * when an early step fails (returns None/Left/Failure).
 */
public class LazyForComprehensionTest {

    @Test
    public void shouldShortCircuitOptionOnNone() {
        AtomicInteger counter = new AtomicInteger(0);

        Option<Integer> result = For(Option.none(), v1 ->
            For(Option.some(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.isEmpty()).isTrue();
        assertThat(counter.get()).isEqualTo(0); // Second computation should not execute
    }

    @Test
    public void shouldExecuteAllStepsForOptionWhenAllSome() {
        AtomicInteger counter = new AtomicInteger(0);

        Option<Integer> result = For(Option.some(1), v1 ->
            For(Option.some(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.get()).isEqualTo(2);
        assertThat(counter.get()).isEqualTo(1); // Second computation should execute
    }

    @Test
    public void shouldShortCircuitOptionOnNoneWithThreeLevels() {
        AtomicInteger counter = new AtomicInteger(0);

        Option<Integer> result = For(Option.some(1), v1 ->
            For(Option.none(), v2 ->
                For(Option.some(counter.incrementAndGet()))
                    .yield(v3 -> v1 + v2 + v3)
            )
        );

        assertThat(result.isEmpty()).isTrue();
        assertThat(counter.get()).isEqualTo(0); // Third computation should not execute
    }

    @Test
    public void shouldShortCircuitEitherOnLeft() {
        AtomicInteger counter = new AtomicInteger(0);

        Either<String, Integer> result = For(Either.left("error"), v1 ->
            For(Either.right(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo("error");
        assertThat(counter.get()).isEqualTo(0); // Second computation should not execute
    }

    @Test
    public void shouldExecuteAllStepsForEitherWhenAllRight() {
        AtomicInteger counter = new AtomicInteger(0);

        Either<String, Integer> result = For(Either.right(1), v1 ->
            For(Either.right(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo(2);
        assertThat(counter.get()).isEqualTo(1); // Second computation should execute
    }

    @Test
    public void shouldShortCircuitEitherOnLeftWithThreeLevels() {
        AtomicInteger counter1 = new AtomicInteger(0);
        AtomicInteger counter2 = new AtomicInteger(0);

        Either<String, Integer> result = For(Either.right(1), v1 ->
            For(Either.left("error"), v2 ->
                For(Either.right(counter1.incrementAndGet()))
                    .yield(v3 -> {
                        counter2.incrementAndGet();
                        return v1 + v2 + v3;
                    })
            )
        );

        assertThat(result.isLeft()).isTrue();
        assertThat(result.getLeft()).isEqualTo("error");
        assertThat(counter1.get()).isEqualTo(0); // Third computation should not execute
        assertThat(counter2.get()).isEqualTo(0); // Yield function should not execute
    }

    @Test
    public void shouldShortCircuitTryOnFailure() {
        AtomicInteger counter = new AtomicInteger(0);

        Try<Integer> result = For(Try.failure(new RuntimeException("error")), v1 ->
            For(Try.success(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.isFailure()).isTrue();
        assertThat(counter.get()).isEqualTo(0); // Second computation should not execute
    }

    @Test
    public void shouldExecuteAllStepsForTryWhenAllSuccess() {
        AtomicInteger counter = new AtomicInteger(0);

        Try<Integer> result = For(Try.success(1), v1 ->
            For(Try.success(counter.incrementAndGet()))
                .yield(v2 -> v1 + v2)
        );

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.get()).isEqualTo(2);
        assertThat(counter.get()).isEqualTo(1); // Second computation should execute
    }

    @Test
    public void shouldSupportComplexNestedForWithEither() {
        // Simulating a chain of operations where each depends on the previous
        Either<String, Integer> result = For(Either.right(10), value ->
            For(Either.right(value * 2), doubled ->
                For(Either.right(doubled + 5))
                    .yield(incremented -> incremented / 5)
            )
        );

        assertThat(result.isRight()).isTrue();
        assertThat(result.get()).isEqualTo(5); // (10 * 2 + 5) / 5 = 5
    }

    @Test
    public void shouldSupportComplexNestedForWithOption() {
        // Simulating a chain of operations where each depends on the previous
        Option<Integer> result = For(Option.some(10), value ->
            For(Option.some(value * 2), doubled ->
                For(Option.some(doubled + 5))
                    .yield(incremented -> incremented / 5)
            )
        );

        assertThat(result.isDefined()).isTrue();
        assertThat(result.get()).isEqualTo(5); // (10 * 2 + 5) / 5 = 5
    }
}
