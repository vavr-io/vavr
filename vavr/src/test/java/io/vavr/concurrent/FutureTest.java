/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
package io.vavr.concurrent;

import io.vavr.*;
import io.vavr.collection.Seq;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.assertj.core.api.IterableAssert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static io.vavr.concurrent.Concurrent.waitUntil;
import static io.vavr.concurrent.Concurrent.zZz;
import static io.vavr.concurrent.ExecutorServices.rejectingExecutorService;
import static io.vavr.concurrent.ExecutorServices.trivialExecutorService;
import static org.assertj.core.api.Assertions.fail;

public class FutureTest extends AbstractValueTest {

    @Override
    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {
            @SuppressWarnings("unchecked")
            @Override
            public IterableAssert<T> isEqualTo(Object expected) {
                if (actual instanceof Future && expected instanceof Future) {
                    assertThat(((Future<T>) actual).getValue()).isEqualTo(((Future<T>) expected).getValue());
                    return this;
                } else {
                    return super.isEqualTo(expected);
                }
            }
        };
    }

    @Override
    protected <T> Future<T> empty() {
        return Future.failed(trivialExecutorService(), new NoSuchElementException());
    }

    @Override
    protected <T> Future<T> of(T element) {
        return Future.of(trivialExecutorService(), () -> element);
    }

    @SafeVarargs
    @Override
    protected final <T> Future<T> of(T... elements) {
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

    // -- static failed()

    @Test
    public void shouldCreateFailureThatFailsWithRuntimeException() {
        final Future<Void> failed = Future.failed(new RuntimeException("ooops"));
        waitUntil(failed::isCompleted);
        assertThat(failed.isFailure()).isTrue();
        final Throwable t = failed.getValue().get().getCause();
        assertThat(t.getClass()).isEqualTo(RuntimeException.class);
        assertThat(t.getMessage()).isEqualTo("ooops");
    }

    @Test
    public void shouldCreateFailureThatFailsWithError() {
        final Future<Void> failed = Future.failed(new Error("ooops"));
        waitUntil(failed::isCompleted);
        assertThat(failed.isFailure()).isTrue();
        final Throwable t = failed.getValue().get().getCause();
        assertThat(t.getClass()).isEqualTo(Error.class);
        assertThat(t.getMessage()).isEqualTo("ooops");
    }

    @Test
    public void shouldCreateAndFailAFutureUsingForkJoinPool() {
        final Future<Integer> future = Future.of(() -> {
            throw new Error();
        });
        waitUntil(future::isCompleted);
        assertFailed(future, Error.class);
    }

    @Test
    public void shouldCreateAndFailAFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(trivialExecutorService(), () -> {
            throw new Error();
        });
        assertFailed(future, Error.class);
    }

    // -- static fromJavaFuture()

    @Test
    public void shouldCreateFutureFromJavaFuture() {
        // Create slow-resolving Java future to show that the wrapping doesn't block
        final java.util.concurrent.Future<Integer> jFuture = generateJavaFuture(1, 3000);
        final Future<Integer> future = Future.fromJavaFuture(jFuture);
        waitUntil(future::isCompleted);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldCreateFutureFromJavaFutureUsingTrivialExecutorService() {
        // Create slow-resolving Java future to show that the wrapping doesn't block
        final java.util.concurrent.Future<String> jFuture = generateJavaFuture("Result", 3000);
        final Future<String> future = Future.fromJavaFuture(trivialExecutorService(), jFuture);
        waitUntil(future::isCompleted);
        assertCompleted(future, "Result");
    }

    // -- static fromCompletableFuture()

    @Test
    public void shouldCreateFutureFromCompletedJavaCompletableFuture() {
        final CompletableFuture<Integer> jFuture = CompletableFuture.completedFuture(1);
        final Future<Integer> future = Future.fromCompletableFuture(jFuture);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldCreateFutureFromFailedJavaCompletableFuture() {
        final CompletableFuture<Integer> jFuture = new CompletableFuture<>();
        jFuture.completeExceptionally(new RuntimeException("some"));
        final Future<Integer> future = Future.fromCompletableFuture(jFuture);
        assertFailed(future, RuntimeException.class);
    }

    @Test
    public void shouldCreateFutureFromJavaCompletableFuture() {
        // Create slow-resolving Java future to show that the wrapping doesn't block
        final CompletableFuture<Integer> jFuture = generateJavaCompletableFuture(1, 1000);
        final Future<Integer> future = Future.fromCompletableFuture(jFuture);
        waitUntil(future::isCompleted);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldCreateFutureFromLateFailingJavaCompletableFuture() {
        final CompletableFuture<Integer> jFuture = Future.<Integer> of(zZz(new RuntimeException())).toCompletableFuture();
        final Future<Integer> future = Future.fromCompletableFuture(jFuture);
        waitUntil(future::isCompleted);
        assertFailed(future, RuntimeException.class);
    }

    @Test
    public void shouldCreateFutureFromJavaCompletableFutureUsingTrivialExecutorService() {
        // Create slow-resolving Java future to show that the wrapping doesn't block
        final java.util.concurrent.Future<String> jFuture = generateJavaCompletableFuture("Result", 1000);
        final Future<String> future = Future.fromJavaFuture(trivialExecutorService(), jFuture);
        waitUntil(future::isCompleted);
        assertCompleted(future, "Result");
    }

    // -- static find()

    @Test
    public void shouldFindNoneWhenEmptySeq() {
        final Future<Option<Object>> testee = Future.find(List.empty(), t -> true);
        assertCompleted(testee, Option.none());
    }

    @Test
    public void shouldFindFirstValueThatSatisfiesAPredicateUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(() -> i)).take(20);
        final Future<Option<Integer>> testee = Future.find(futures, i -> i == 13);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, Option.some(13));
    }

    @Test
    public void shouldFailFindingFirstValueBecauseNoResultSatisfiesTheGivenPredicateUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(() -> i)).take(20);
        final Future<Option<Integer>> testee = Future.find(futures, i -> false);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, Option.none());
    }

    @Test
    public void shouldFindOneSucceedingFutureWhenAllOthersFailUsingDefaultExecutorService() {
        final Seq<Future<Integer>> futures = Stream.from(1)
                .map(i -> Future.<Integer> of(() -> {
                    throw new Error();
                }))
                .take(12)
                .append(Future.of(() -> 13));
        final Future<Option<Integer>> testee = Future.find(futures, i -> i == 13);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, Option.some(13));
    }

    @Test
    public void shouldFindNoneWhenAllFuturesFailUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1)
                .map(i -> Future.<Integer> of(() -> {
                    throw new Error(String.valueOf(i));
                }))
                .take(20);
        final Future<Option<Integer>> testee = Future.find(futures, i -> i == 13);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, Option.none());
    }

    // -- static firstCompletedOf()

    @Test
    public void shouldGetFirstCompletedOfFailuresUsingForkJoinPool() {
        final Seq<Future<Object>> futures = Stream.from(1).map(i -> Future.of(zZz(new Error()))).take(3);
        final Future<?> testee = Future.firstCompletedOf(futures);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get().isFailure()).isTrue();
    }

    @Test
    public void shouldGetFirstCompletedOfSucceedingFuturesUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(zZz(i))).take(3);
        final Future<?> testee = Future.firstCompletedOf(futures).await();
        assertThat(testee.getValue().get().isSuccess()).isTrue();
    }

    // -- static fromTry()

    @Test
    public void shouldCreateFailFutureFromTry() {
        final Future<Integer> future = Future.fromTry(Try.of(() -> { throw new Error(); }));
        waitUntil(future::isCompleted);
        assertThat(future.isFailure()).isTrue();
    }

    @Test
    public void shouldCreateSuccessFutureFromTry() {
        final Future<Integer> future = Future.fromTry(Try.of(() -> 42));
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(42);
    }

    // -- static of()

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(trivialExecutorService(), () -> 1);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldNotCancelCompletedFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(trivialExecutorService(), () -> 1);
        assertThat(future.cancel()).isFalse();
        assertCompleted(future, 1);
    }

    @Test
    public void shouldCompleteWithFailureWhenExecutorServiceThrowsRejectedExecutionException() {
        final Future<Integer> future = Future.of(rejectingExecutorService(), () -> 1);
        assertFailed(future, RejectedExecutionException.class);
    }

    // TODO: Re-enable this test when solving #1530
    @Ignore
    @Test
    public void shouldCompleteOneFuturesUsingAThreadPoolExecutorLimitedToOneThread() {
        final ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS, new SynchronousQueue<>());
        final Future<Integer> future = Future.of(service, () -> expensiveOperation(1)).await();
        assertCompleted(future, 1);
        service.shutdown();
    }

    // TODO: Re-enable this test when solving #1530
    @Ignore
    @Test
    public void shouldCompleteThreeFuturesUsingAThreadPoolExecutorLimitedToOneThread() {
        final ExecutorService service = new ThreadPoolExecutor(1, 1, 0L, MILLISECONDS, new SynchronousQueue<>());
        final Stream<Future<Integer>> futures = Stream
                .rangeClosed(1, 3)
                .map(value -> Future.of(service, () -> expensiveOperation(value)));
        futures.forEach(Future::await);
        assertThat(futures.flatMap(Function.identity()).toList().sorted()).isEqualTo(List.of(1, 2, 3));
        service.shutdown();
    }

    private static <T> T expensiveOperation(T value) throws InterruptedException {
        Thread.sleep(500);
        return value;
    }

    // -- static ofSupplier()

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorServiceAndSupplier() {
        final Future<Integer> future = Future.ofSupplier(trivialExecutorService(), () -> 1);
        assertThat(future.executorService()).isSameAs(trivialExecutorService());
        assertCompleted(future, 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenExecutorIsNullUsingSupplier() {
        Future.ofSupplier(null, () -> 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenSupplierIsNull() {
        Future.ofSupplier(trivialExecutorService(), null);
    }

    @Test
    public void shouldCreateAndCompleteAFutureUsingDefaultExecutorServiceAndSupplier() {
        final Future<Integer> future = Future.ofSupplier(() -> 1);
        assertThat(future.executorService()).isSameAs(Future.DEFAULT_EXECUTOR_SERVICE);
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEUsingDefaultExecutorWhenSupplierIsNull() {
        Future.ofSupplier(null);
    }

    // -- static ofCallable()

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorServiceAndCallable() {
        final Future<Integer> future = Future.ofCallable(trivialExecutorService(), () -> 1);
        assertThat(future.executorService()).isSameAs(trivialExecutorService());
        assertCompleted(future, 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenExecutorIsNullUsingCallable() {
        Future.ofCallable(null, () -> 1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenCallableIsNull() {
        Future.ofCallable(trivialExecutorService(), null);
    }

    @Test
    public void shouldCreateAndCompleteAFutureUsingDefaultExecutorServiceAndCallable() {
        final Future<Integer> future = Future.ofCallable(() -> 1);
        assertThat(future.executorService()).isSameAs(Future.DEFAULT_EXECUTOR_SERVICE);
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(1);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEUsingDefaultExecutorWhenCallableIsNull() {
        Future.ofCallable(null);
    }

    // -- static runRunnable()

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorServiceAndRunnable() {
        final int[] sideEffect = new int[] { 0 };
        final Future<Void> future = Future.runRunnable(trivialExecutorService(), () -> sideEffect[0] = 42);
        assertThat(future.executorService()).isSameAs(trivialExecutorService());
        waitUntil(future::isCompleted);
        assertThat(sideEffect[0]).isEqualTo(42);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenExecutorIsNullUsingRunnable() {
        Future.runRunnable(null, () -> {});
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenRunnableIsNull() {
        Future.runRunnable(trivialExecutorService(), null);
    }

    @Test
    public void shouldCreateAndCompleteAFutureUsingDefaultExecutorServiceAndRunnable() {
        final int[] sideEffect = new int[] { 0 };
        final Future<Void> future = Future.runRunnable(() -> sideEffect[0] = 42);
        assertThat(future.executorService()).isSameAs(Future.DEFAULT_EXECUTOR_SERVICE);
        waitUntil(future::isCompleted);
        assertThat(sideEffect[0]).isEqualTo(42);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEUsingDefaultExecutorWhenRunnableIsNull() {
        Future.runRunnable(null);
    }

    // -- static reduce()

    @Test(expected = NoSuchElementException.class)
    public void shouldFailReduceEmptySequence() {
        Future.<Integer> reduce(List.empty(), (i1, i2) -> i1 + i2);
    }

    @Test
    public void shouldReduceSequenceOfFutures() {
        final Future<String> future = Future.reduce(
                List.of(Future.of(zZz("Va")), Future.of(zZz("vr"))),
                (i1, i2) -> i1 + i2
        );
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo("Vavr");
    }

    @Test
    public void shouldReduceWithErrorIfSequenceOfFuturesContainsOneError() {
        final Future<Integer> future = Future.reduce(
                List.of(Future.of(zZz(13)), Future.of(zZz(new Error()))),
                (i1, i2) -> i1 + i2
        );
        waitUntil(future::isCompleted);
        assertFailed(future, Error.class);
    }

    // -- static run()

    @Test
    public void shouldCompleteRunnable() {
        final int[] sideEffect = new int[] { 0 };
        final Future<Void> future = Future.run(() -> sideEffect[0] = 42);
        waitUntil(future::isCompleted);
        assertThat(sideEffect[0]).isEqualTo(42);
    }

    // -- static sequence()

    @Test
    public void shouldCompleteWithSeqOfValueIfSequenceOfFuturesContainsNoError() {
        final Future<Seq<Integer>> sequence = Future.sequence(
                List.of(Future.of(zZz(1)), Future.of(zZz(2)))
        );
        waitUntil(sequence::isCompleted);
        assertThat(sequence.getValue().get()).isEqualTo(Try.success(Stream.of(1, 2)));
    }

    @Test
    public void shouldCompleteWithErrorIfSequenceOfFuturesContainsOneError() {
        final Future<Seq<Integer>> sequence = Future.sequence(
                List.of(Future.of(zZz(13)), Future.of(zZz(new Error())))
        );
        waitUntil(sequence::isCompleted);
        assertFailed(sequence, Error.class);
    }

    // -- static successful()

    @Test
    public void shouldCreateSuccessful() {
        final Future<Integer> succ = Future.successful(42);
        assertThat(succ.isCompleted()).isTrue();
        assertThat(succ.isSuccess()).isTrue();
        assertThat(succ.get()).isEqualTo(42);
    }

    // -- static traverse()

    @Test
    public void shouldCompleteTraverse() {
        final Future<Seq<Integer>> future = Future.traverse(List.of(1, 2, 3), i -> Future.of(zZz(i)));
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(Stream.of(1, 2, 3));
    }

    // -- andThen

    @Test
    public void shouldCompleteWithErrorIfFailAndThenFail() {
        final Future<Integer> future = Future.<Integer> of(zZz(new Error("fail!")))
                .andThen(t -> zZz(new Error("and then fail!")));
        waitUntil(future::isCompleted);
        assertFailed(future, Error.class);
    }

    @Test
    public void shouldCompleteWithSuccessIfSuccessAndThenFail() {
        final Future<Integer> future = Future.of(zZz(42))
                .andThen(t -> zZz(new Error("and then fail!")));
        waitUntil(future::isCompleted);
        assertThat(future.getValue().get()).isEqualTo(Try.success(42));
    }

    @Test
    public void shouldCompleteWithSpecificOrderIfSuccessAndThenSuccess() {
        final boolean[] lock = new boolean[] { true };
        final int[] sideEffect = new int[] { 0 };
        final Future<Void> future = Future.<Void> of(() -> {
            waitUntil(() -> !lock[0]);
            return null;
        }).andThen(t -> sideEffect[0] = 42);
        assertThat(future.isCompleted()).isFalse();
        assertThat(sideEffect[0]).isEqualTo(0);
        lock[0] = false;
        waitUntil(future::isCompleted);
        assertThat(sideEffect[0]).isEqualTo(42);
    }

    // -- orElse

    @Test
    public void shouldReturnSelfResultOnOrElseIfSuccess() {
        final Future<String> f1 = Future.of(() -> "f1");
        final Future<String> f2 = f1.orElse(Future.of(() -> "f2"));
        waitUntil(f2::isCompleted);
        assertThat(f2.get()).isEqualTo("f1");
    }

    @Test
    public void shouldReturnSelfResultOnOrElseSupplierIfSuccess() {
        final Future<String> f1 = Future.of(() -> "f1");
        final Future<String> f2 = f1.orElse(() -> Future.of(() -> "f2"));
        waitUntil(f2::isCompleted);
        assertThat(f2.get()).isEqualTo("f1");
    }

    @Test
    public void shouldReturnOtherResultOnOrElseIfFailure() {
        final Future<String> f1 = Future.failed(new RuntimeException());
        final Future<String> f2 = f1.orElse(Future.of(() -> "f2"));
        waitUntil(f2::isCompleted);
        assertThat(f2.get()).isEqualTo("f2");
    }

    @Test
    public void shouldReturnOtherResultOnOrElseSupplierIfFailure() {
        final Future<String> f1 = Future.failed(new RuntimeException());
        final Future<String> f2 = f1.orElse(() -> Future.of(() -> "f2"));
        waitUntil(f2::isCompleted);
        assertThat(f2.get()).isEqualTo("f2");
    }

    // -- await

    @Test
    public void shouldAwaitOnGet() {
        final Future<Integer> future = Future.of(() -> {
            Try.run(() -> Thread.sleep(250L));
            return 1;
        });
        assertThat(future.get()).isEqualTo(1);
    }

    // -- failed

    @Test
    public void shouldConvertToFailedFromFail() {
        final Future<Throwable> future = Future.of(zZz(new Error())).failed();
        waitUntil(future::isCompleted);
        assertThat(future.isSuccess()).isTrue();
        assertThat(future.get().getClass()).isEqualTo(Error.class);
    }

    @Test
    public void shouldConvertToFailedFromSuccess() {
        final Future<Throwable> future = Future.of(zZz(42)).failed();
        waitUntil(future::isCompleted);
        assertThat(future.isFailure()).isTrue();
        assertThat(future.getValue().get().getCause().getClass()).isEqualTo(NoSuchElementException.class);
    }

    // -- fallbackTo

    @Test
    public void shouldFallbackToThisResult() {
        final Future<Integer> future = Future.of(() -> 1);
        final Future<Integer> that = Future.of(() -> {
            throw new Error();
        });
        final Future<Integer> testee = future.fallbackTo(that);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get()).isEqualTo(Try.success(1));
    }

    @Test
    public void shouldFallbackToThatResult() {
        final Future<Integer> future = Future.of(() -> {
            throw new Error();
        });
        final Future<Integer> that = Future.of(() -> 1);
        final Future<Integer> testee = future.fallbackTo(that);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get()).isEqualTo(Try.success(1));
    }

    @Test
    public void shouldFallbackToThisFailure() {
        final Future<Integer> future = Future.of(() -> {
            throw new Error("ok");
        });
        final Future<Integer> that = Future.of(() -> {
            throw new Error();
        });
        final Future<Integer> testee = future.fallbackTo(that);
        waitUntil(testee::isCompleted);
        final Try<Integer> result = testee.getValue().get();
        assertThat(result.isFailure()).isTrue();
        assertThat(result.getCause().getMessage()).isEqualTo("ok");
    }

    // -- fold()

    @Test
    public void shouldFoldEmptyIterable() {
        final Seq<Future<Integer>> futures = Stream.empty();
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get()).isEqualTo(Try.success(0));
    }

    @Test
    public void shouldFoldNonEmptyIterableOfSucceedingFutures() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(zZz(i))).take(5);
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get()).isEqualTo(Try.success(15));
    }

    @Test
    public void shouldFoldNonEmptyIterableOfFailingFutures() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.<Integer> of(zZz(new Error()))).take(5);
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertFailed(testee, Error.class);
    }

    // -- cancel()

    @SuppressWarnings("InfiniteLoopStatement")
    @Test
    public void shouldInterruptLockedFuture() {
        final Future<?> future = Future.of(() -> {
            while (true) {
                Try.run(() -> Thread.sleep(100));
            }
        });
        future.cancel();
        waitUntil(future::isCompleted);
        assertCancelled(future);
    }

    @Test(expected = CancellationException.class)
    public void shouldThrowOnGetAfterCancellation() {
        final Future<?> future = Future.of(Concurrent::waitForever);
        final boolean isCancelled = future.cancel();
        assertThat(isCancelled).isTrue();
        future.get();
        fail("Future was expected to throw on get() after cancellation!");
    }

    // -- collect()

    @Test
    public void shouldCollectDefinedValueUsingPartialFunction() {
        final PartialFunction<Integer, String> pf = Function1.<Integer, String> of(String::valueOf).partial(i -> i % 2 == 1);
        final Future<String> future = Future.of(zZz(3)).collect(pf);
        waitUntil(future::isCompleted);
        assertThat(future.getValue().get()).isEqualTo(Try.success("3"));
    }

    @Test
    public void shouldFilterNotDefinedValueUsingPartialFunction() {
        final PartialFunction<Integer, String> pf = Function1.<Integer, String> of(String::valueOf).partial(i -> i % 2 == 1);
        final Future<String> future = Future.of(zZz(2)).collect(pf);
        waitUntil(future::isCompleted);
        assertThat(future.getValue().get().isFailure()).isTrue();
    }

    @Test
    public void shouldCollectEmptyFutureUsingPartialFunction() {
        final PartialFunction<Integer, String> pf = Function1.<Integer, String> of(String::valueOf).partial(i -> i % 2 == 1);
        final Future<String> future = Future.<Integer> of(zZz(new Error())).collect(pf);
        waitUntil(future::isCompleted);
        assertThat(future.getValue().get().isFailure()).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionOnNullCollectPartialFunction() {
        final PartialFunction<Integer, String> pf = null;
        Future.of(zZz(3)).collect(pf);
    }

    // -- executorService()

    @Test
    public void shouldReturnExecutorService() {
        final Future<Integer> f1 = Future.of(() -> 42);
        assertThat(f1.executorService()).isSameAs(Future.DEFAULT_EXECUTOR_SERVICE);
        final ExecutorService customExecutorService = Executors.newCachedThreadPool();
        final Future<Integer> f2 = Future.of(customExecutorService, () -> 42);
        assertThat(f2.executorService()).isSameAs(customExecutorService);
    }

    // -- getCause()

    @Test
    public void shouldGetCauseOfUncompletedFuture() {
        final Future<?> future = Future.of(Concurrent::waitForever);
        assertThat(future.getCause()).isEqualTo(Option.none());
    }

    @Test
    public void shouldGetCauseOfFailedFuture() {
        final Error error = new Error();
        assertThat(Future.failed(error).getCause()).isEqualTo(Option.some(error));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowWhenGettingCauseOfSucceededFuture() {
        Future.successful("ok").getCause();
    }

    // -- getValue()

    @Test
    public void shouldGetValueOfUncompletedFuture() {
        final Future<?> future = Future.of(Concurrent::waitForever);
        assertThat(future.getValue()).isEqualTo(Option.none());
    }

    @Test
    public void shouldGetValueOfSucceededFuture() {
        assertThat(Future.successful("ok").getValue()).isEqualTo(Option.some(Try.success("ok")));
    }

    @Test
    public void shouldThrowWhenGettingValueOfFailedFuture() {
        final Error error = new Error();
        assertThat(Future.failed(error).getValue()).isEqualTo(Option.some(Try.failure(error)));
    }

    // -- isAsync

    @Override
    @Test
    public void shouldVerifyAsyncProperty() {
        assertThat(empty().isAsync()).isTrue();
        assertThat(of(1).isAsync()).isTrue();
    }

    // -- isCompleted()

    @Test
    public void shouldBeCompletedWhenSuccessful() {
        assertThat(Future.successful(null).isCompleted()).isTrue();
    }

    @Test
    public void shouldBeCompletedWhenFailed() {
        assertThat(Future.failed(new Exception()).isCompleted()).isTrue();
    }

    @Test
    public void shouldBeCompletedWhenResultIsPresent() {
        final Future<Integer> future = Future.of(() -> null);
        waitUntil(future::isCompleted);
        assertThat(future.isCompleted()).isTrue();
    }

    // -- isSuccess()

    @Test
    public void shouldBeSuccessful() {
        assertThat(Future.successful(null).isSuccess()).isTrue();
    }

    // -- isFailure()

    @Test
    public void shouldBeFailed() {
        assertThat(Future.failed(new Exception()).isFailure()).isTrue();
    }
    
    // -- onComplete()

    @Test
    public void shouldExecuteOnCompleteOnCompletedFuture() {
        final int[] actual = new int[] { 0 };
        Future.successful(1).onComplete(t -> {
            actual[0] = t.get();
        });
        waitUntil(() -> actual[0] == 1);
    }

    @Test
    public void shouldRegisterCallbackBeforeFutureCompletes() {

        // instead of delaying we wait/notify
        final Object lock = new Object();
        final int[] actual = new int[] { -1 };
        final boolean[] futureWaiting = new boolean[] { false };
        final int expected = 1;

        // create a future and put it to sleep
        final Future<Integer> future = Future.of(() -> {
            synchronized (lock) {
                futureWaiting[0] = true;
                lock.wait();
            }
            return expected;
        });

        // give the future thread some time to sleep
        waitUntil(() -> futureWaiting[0]);

        // the future now is on hold and we have time to register a callback
        future.onComplete(result -> actual[0] = result.get());
        assertThat(future.isCompleted()).isFalse();
        assertThat(actual[0]).isEqualTo(-1);

        // now awake the future
        synchronized (lock) {
            lock.notify();
        }

        // give the future thread some time to complete
        waitUntil(future::isCompleted);

        // the callback is also executed on its own thread - we have to wait for it to complete.
        waitUntil(() -> actual[0] == expected);
    }

    @Test
    public void shouldPerformActionAfterFutureCompleted() {
        final int[] actual = new int[] { -1 };
        final Future<Integer> future = Future.of(trivialExecutorService(), () -> 1);
        assertCompleted(future, 1);
        assertThat(actual[0]).isEqualTo(-1);
        future.onComplete(result -> actual[0] = result.get());
        assertThat(actual[0]).isEqualTo(1);
    }

    // -- onFailure()

    @Test
    public void shouldDoActionOnFailure() {
        final Future<?> future = Future.of(zZz(new Error()));
        final Throwable[] holder = new Throwable[] { null };
        future.onFailure(t -> holder[0] = t);
        waitUntil(() -> holder[0] != null);
        assertThat(holder[0].getClass()).isEqualTo(Error.class);
    }

    // -- onSuccess()

    @Test
    public void shouldDoActionOnSuccess() {
        final Future<Integer> future = Future.of(zZz(42));
        final int[] holder = new int[] { 0 };
        future.onSuccess(i -> holder[0] = i);
        waitUntil(() -> holder[0] > 0);
        assertThat(holder[0]).isEqualTo(42);
    }

    // -- recover()

    @Test
    public void shouldRecoverFailedFuture() {
        final Future<Integer> recovered = Future.<Integer> of(zZz(new Error())).recover(t -> 42);
        waitUntil(recovered::isCompleted);
        assertThat(recovered.isSuccess()).isTrue();
        assertThat(recovered.get()).isEqualTo(42);
    }

    @Test
    public void shouldNotCrashWhenRecoverFails() {
        final Future<Integer> recovered = Future.<Integer> of(zZz(new Error())).recover(t -> {
            throw new ArithmeticException();
        });
        waitUntil(recovered::isCompleted);
        waitUntil(recovered::isFailure);
        assertThat(recovered.getCause().get().getClass()).isEqualTo(ArithmeticException.class);
    }

    // -- recoverWith()

    @Test
    public void shouldRecoverFailedFutureWithFuture() {
        final Future<String> recovered = Future.<String> of(() -> { throw new Error("oh!"); })
                .recoverWith(x -> Future.of(x::getMessage));
        waitUntil(recovered::isCompleted);
        assertThat(recovered.isSuccess()).isTrue();
        assertThat(recovered.get()).isEqualTo("oh!");
    }

    @Test
    public void shouldRecoverSuccessFutureWithFuture() {
        final Future<String> recovered = Future.of(() -> "oh!")
                .recoverWith(ignored -> Future.of(() -> "ignored"));
        waitUntil(recovered::isCompleted);
        assertThat(recovered.isSuccess()).isTrue();
        assertThat(recovered.get()).isEqualTo("oh!");
    }

    // -- toCompletableFuture

    @Test
    public void shouldConvertCompletedFutureToCompletableFuture() {
        final CompletableFuture<Integer> completableFuture = Future.of(() -> 42).toCompletableFuture();
        assertThat(completableFuture.isDone());
        assertThat(Try.of(completableFuture::get).get()).isEqualTo(42);
    }

    @Test
    public void shouldConvertFailedFutureToCompletableFuture() {
        final CompletableFuture<Integer> completableFuture = Future.<Integer> failed(new RuntimeException()).toCompletableFuture();
        assertThat(completableFuture.isCompletedExceptionally());
        assertThat(Try.of(completableFuture::get).getCause()).isExactlyInstanceOf(ExecutionException.class);
    }

    @Test
    public void shouldConvertLateFutureToCompletableFuture() {
        final CompletableFuture<Integer> completableFuture = Future.of(zZz(42)).toCompletableFuture();
        assertThat(Try.of(completableFuture::get).get()).isEqualTo(42);
    }

    // -- toTry()

    @Test
    public void shouldConvertSuccessfulFutureToTry() {
        assertThat(Future.successful(1).toTry()).isEqualTo(Try.success(1));
    }

    @Test
    public void shouldConvertFailedFutureToTry() {
        assertThat(Future.failed(new Error("!")).toTry()).isEqualTo(Try.failure(new Error("!")));
    }

    // -- transform()

    @Test
    public void shouldTransform() {
        final String transformed = Future.of(() -> 42).transform(f -> String.valueOf(f.get()));
        assertThat(transformed).isEqualTo("42");
    }

    // -- transformValue()

    @Test
    public void shouldTransformResultFromSuccessToSuccess() {
        final Future<String> future = Future.of(zZz(42)).transformValue(t -> Try.of(() -> "forty two"));
        waitUntil(future::isCompleted);
        waitUntil(future::isSuccess);
        assertThat(future.get()).isEqualTo("forty two");
    }

    @Test
    public void shouldTransformResultFromSuccessToFailure() {
        final Future<String> future = Future.of(zZz(42)).transformValue(t -> Try.failure(new Error()));
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(Error.class);
    }

    @Test
    public void shouldTransformResultFromSuccessToFailureThroughError() {
        final Future<String> future = Future.of(zZz(42)).transformValue(t -> Try.of(() -> {
            throw new ArithmeticException();
        }));
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(ArithmeticException.class);
    }

    @Test
    public void shouldTransformResultFromFailureToSuccess() {
        final Future<String> future = Future.of(zZz(new Error())).transformValue(t -> Try.of(() -> "forty two"));
        waitUntil(future::isCompleted);
        waitUntil(future::isSuccess);
        assertThat(future.get()).isEqualTo("forty two");
    }

    @Test
    public void shouldTransformResultFromFailureToFailure() {
        final Future<String> future = Future.of(() -> {
            throw new ArithmeticException();
        }).transformValue(t -> Try.failure(new Error()));
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(Error.class);
    }

    @Test
    public void shouldTransformResultFromFailureToFailureThroughError() {
        final Future<String> future = Future.of(zZz(new Error())).transformValue(t -> Try.of(() -> {
            throw new ArithmeticException();
        }));
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(ArithmeticException.class);
    }

    // -- zip()

    @Test
    public void shouldZipSuccess() {
        final Future<Tuple2<Integer, Integer>> future = Future.of(zZz(1)).zip(Future.of(zZz(2)));
        waitUntil(future::isCompleted);
        waitUntil(future::isSuccess);
        assertThat(future.get()).isEqualTo(Tuple.of(1, 2));
    }

    @Test
    public void shouldZipFailure() {
        final Future<Tuple2<Integer, Integer>> future = Future.<Integer> of(zZz(new Error())).zip(Future.of(zZz(2)));
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(Error.class);
    }

    // -- zipWith()

    @Test
    public void shouldZipWithSuccess() {
        final Future<List<Integer>> future = Future.of(zZz(1)).zipWith(Future.of(zZz(2)), List::of);
        waitUntil(future::isCompleted);
        waitUntil(future::isSuccess);
        assertThat(future.get()).isEqualTo(List.of(1, 2));
    }

    @Test
    public void shouldZipWithFailure() {
        final Future<List<Integer>> future = Future.<Integer> of(zZz(new Error())).zipWith(Future.of(zZz(2)), List::of);
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(Error.class);
    }

    @Test
    public void shouldZipWithCombinatorFailure() {
        final Future<List<Integer>> future = Future.of(zZz(3)).zipWith(Future.of(zZz(2)), (t, u) -> {
            throw new RuntimeException();
        });
        waitUntil(future::isCompleted);
        waitUntil(future::isFailure);
        assertThat(future.getCause().get().getClass()).isEqualTo(RuntimeException.class);
    }

    // -- Value implementation

    @Test
    public void shouldFilterFuture() {
        final Future<Integer> future = Future.successful(42);
        assertThat(future.filter(i -> i == 42).get()).isEqualTo(42);
        assertThat(future.filter(i -> i == 43).isEmpty()).isTrue();
    }

    @Test
    public void shouldFlatMapFuture() {
        final Future<Integer> future = Future.of(zZz(42)).flatMap(i -> Future.of(zZz(i * 2)));
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(84);
    }

    @Test
    public void shouldMapFuture() {
        final Future<Integer> future = Future.of(zZz(42)).map(i -> i * 2);
        waitUntil(future::isCompleted);
        assertThat(future.get()).isEqualTo(84);
    }

    @Test
    public void shouldPeekFuture() {
        final int[] consumer = new int[] { -1 };
        Future.of(zZz(42)).peek(i -> consumer[0] = i);
        waitUntil(() -> consumer[0] > 0);
        assertThat(consumer[0]).isEqualTo(42);
    }

    @Test
    public void shouldReturnIterator() {
        final Iterator<Integer> it = Future.successful(42).iterator();
        assertThat(it.hasNext()).isTrue();
        assertThat(it.next()).isEqualTo(42);
        assertThat(it.hasNext()).isFalse();
    }

    // TODO: also test what happens an exception occurs within one of these
    // TODO: { filter, flatten, flatMap, get, isEmpty, iterator, map, peek }
    // TODO: method calls and compare it with Scala

    // -- map()

    @Test
    public void shouldMapTheHappyPath() {
        final Future<String> testee = Future.of(zZz(1)).map(Object::toString);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, "1");
    }

    @Test
    public void shouldMapWhenCrashingDuringFutureComputation() {
        final Future<String> testee = Future.<Integer> of(zZz(new Error())).map(Object::toString);
        waitUntil(testee::isCompleted);
        assertFailed(testee, Error.class);
    }

    @Test
    public void shouldMapWhenCrashingDuringMapping() {
        final Future<String> testee = Future.of(zZz(1)).map(i -> {
            throw new IllegalStateException();
        });
        waitUntil(testee::isCompleted);
        assertFailed(testee, IllegalStateException.class);
    }

    // -- mapTry()

    @Test
    public void shouldMapTryTheHappyPath() {
        final Future<String> testee = Future.of(zZz(1)).mapTry(Object::toString);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, "1");
    }

    @Test
    public void shouldMapTryWhenCrashingDuringFutureComputation() {
        final Future<String> testee = Future.<Integer> of(zZz(new Error())).mapTry(Object::toString);
        waitUntil(testee::isCompleted);
        assertFailed(testee, Error.class);
    }

    @Test
    public void shouldMapTryWhenCrashingDuringMapping() {
        final Future<String> testee = Future.of(zZz(1)).mapTry(i -> {
            throw new IOException();
        });
        waitUntil(testee::isCompleted);
        assertFailed(testee, IOException.class);
    }

    // -- equals

    @Override
    @Test
    public void shouldRecognizeEqualObjects() {
        // Future equality undefined
    }

    @Override
    @Test
    public void shouldRecognizeUnequalObjects() {
        // Future equality undefined
    }

    // -- (helpers)

    // checks the invariant for cancelled state
    private void assertCancelled(Future<?> future) {
        assertFailed(future, CancellationException.class);
    }

    private void assertFailed(Future<?> future, Class<? extends Throwable> exception) {
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.getValue().get().failed().get()).isExactlyInstanceOf(exception);
    }

    // checks the invariant for cancelled state
    private <T> void assertCompleted(Future<?> future, T value) {
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.getValue()).isEqualTo(Option.some(Try.success(value)));
    }

    private <T> java.util.concurrent.Future<T> generateJavaFuture(T value, int waitPeriod) {
        return generateJavaCompletableFuture(value, waitPeriod);
    }

    private <T> java.util.concurrent.CompletableFuture<T> generateJavaCompletableFuture(T value, int waitPeriod) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(waitPeriod);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return value;
        });
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
