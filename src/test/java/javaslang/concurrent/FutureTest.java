/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.collection.List;
import javaslang.collection.Seq;
import javaslang.collection.Stream;
import javaslang.control.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.concurrent.CancellationException;

import static javaslang.concurrent.Concurrent.waitUntil;
import static javaslang.concurrent.Concurrent.zZz;
import static org.assertj.core.api.Assertions.assertThat;

public class FutureTest {

    /* TODO: extends AbstractValueTest {

    @Override
    protected <T> Future<T> empty() {
        return Future.failed(TrivialExecutorService.instance(), new Error());
    }

    @Override
    protected <T> Future<T> of(T element) {
        return Future.of(TrivialExecutorService.instance(), () -> element);
    }

    @Override
    protected <T> Seq<T> of(T... elements) {
        return List.of(elements);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }
    */

    // -- static failed()

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
        final Future<Integer> future = Future.of(TrivialExecutorService.instance(), () -> {
            throw new Error();
        });
        assertFailed(future, Error.class);
    }

    // -- static find()

    @Test
    public void shouldFindFirstValueThatSatisfiesAPredicateUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(() -> i)).take(20);
        final Future<Option<Integer>> testee = Future.find(futures, i -> i == 13);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, new Some<>(13));
    }

    @Test
    public void shouldFailFindingFirstValueBecauseNoResultSatisfiesTheGivenPredicateUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(() -> i)).take(20);
        final Future<Option<Integer>> testee = Future.find(futures, i -> false);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, None.instance());
    }

    @Test
    public void shouldFindOneSucceedingFutureWhenAllOthersFailUsingForkJoinPool() {
        final Seq<Future<Integer>> futures = Stream.from(1)
                .map(i -> Future.<Integer> of(() -> {
                    throw new Error();
                }))
                .take(12)
                .append(Future.of(() -> 13));
        final Future<Option<Integer>> testee = Future.find(futures, i -> i == 13);
        waitUntil(testee::isCompleted);
        assertCompleted(testee, new Some<>(13));
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
        assertCompleted(testee, None.instance());
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
        final Future<?> testee = Future.firstCompletedOf(futures);
        waitUntil(testee::isCompleted);
        assertThat(testee.getValue().get().isSuccess()).isTrue();
    }

    // -- fold()

    @Test
    public void shouldFoldEmptyIterable() {
        final Seq<Future<Integer>> futures = Stream.empty();
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertThat(testee.get()).isEqualTo(0);
    }

    @Test
    public void shouldFoldNonEmptyIterableOfSucceedingFutures() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.of(zZz(i))).take(5);
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertThat(testee.get()).isEqualTo(15);
    }

    @Test
    public void shouldFoldNonEmptyIterableOfFailingFutures() {
        final Seq<Future<Integer>> futures = Stream.from(1).map(i -> Future.<Integer> of(zZz(new Error()))).take(5);
        final Future<Integer> testee = Future.fold(futures, 0, (a, b) -> a + b);
        waitUntil(testee::isCompleted);
        assertFailed(testee, Error.class);
    }

    // -- static fromTry()

    // TODO

    // -- static of()

    @Test
    public void shouldCreateAndCompleteAFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(TrivialExecutorService.instance(), () -> 1);
        assertCompleted(future, 1);
    }

    @Test
    public void shouldNotCancelCompletedFutureUsingTrivialExecutorService() {
        final Future<Integer> future = Future.of(TrivialExecutorService.instance(), () -> 1);
        assertThat(future.cancel()).isFalse();
        assertCompleted(future, 1);
    }

    // -- static reduce()

    // TODO

    // -- static run()

    // TODO

    // -- static sequence()

    @Test
    public void shoudCompleteWithSeqOfValueIfSequenceOfFuturesContainsNoError() {
        final Future<Seq<Integer>> sequence = Future.sequence(
                List.of(Future.of(zZz(1)), Future.of(zZz(2)))
        );
        waitUntil(sequence::isCompleted);
        assertThat(sequence.getValue().get()).isEqualTo(new Success<>(Stream.of(1, 2)));
    }

    @Test
    public void shoudCompleteWithErrorIfSequenceOfFuturesContainsOneError() {
        final Future<Seq<Integer>> sequence = Future.sequence(
                List.of(Future.of(zZz(13)), Future.of(zZz(new Error())))
        );
        waitUntil(sequence::isCompleted);
        assertFailed(sequence, Error.class);
    }

    // -- static successful()

    // TODO

    // -- static traverse()

    // TODO

    // -- cancel()

    @Test
    public void shouldInterruptLockedFuture() {
        final Future<?> future = Future.of(() -> {
            while (true) {
                Try.run(() -> Thread.sleep(100));
            }
        });
        future.onComplete(r -> Assertions.fail("future should lock forever"));
        future.cancel();
        assertCancelled(future);
    }

    // -- executorService()

    // TODO

    // -- getValue()

    // TODO

    // -- isCompleted()

    // TODO

    // -- isSuccess()

    // TODO

    // -- isFailure()

    // TODO

    // -- onComplete()

    @Test
    public void shouldRegisterCallbackBeforeFutureCompletes() {

        // instead of delaying we wait/notify
        final Object lock = new Object();
        final int[] actual = new int[] { -1 };
        final int expected = 1;

        // create a future and put it to sleep
        final Future<Integer> future = Future.of(() -> {
            synchronized (lock) {
                lock.wait();
            }
            return expected;
        });

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
        final Future<Integer> future = Future.of(TrivialExecutorService.instance(), () -> 1);
        assertCompleted(future, 1);
        assertThat(actual[0]).isEqualTo(-1);
        future.onComplete(result -> actual[0] = result.get());
        assertThat(actual[0]).isEqualTo(1);
    }

    // -- onFailure()

    // TODO

    // -- onSuccess()

    // TODO

    // -- Value implementation

    // TODO: filter, flatten, flatMap, get, isEmpty, iterator, map, peek

    // -- (helpers)

    // checks the invariant for cancelled state
    static void assertCancelled(Future<?> future) {
        assertFailed(future, CancellationException.class);
    }

    static void assertFailed(Future<?> future, Class<? extends Throwable> exception) {
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.getValue().get().failed().get()).isExactlyInstanceOf(exception);
    }

    // checks the invariant for cancelled state
    static <T> void assertCompleted(Future<?> future, T value) {
        assertThat(future.isCompleted()).isTrue();
        assertThat(future.getValue()).isEqualTo(new Some<>(new Success<>(value)));
    }
}
