/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;


import javaslang.Function1;
import javaslang.Tuple;
import javaslang.Tuple8;
import javaslang.collection.List;
import javaslang.control.Failure;
import javaslang.control.Some;
import javaslang.control.Success;
import javaslang.control.Try;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class FutureTest {

    private Consumer<Throwable> failOnError(String message) {
        return (t) -> fail(message + t.getMessage());
    }

    private static class Checker {

        private final AtomicReference<List<Integer>> holder;

        public Checker(int max) {
            holder = new AtomicReference<>(List.range(0, max + 1));
        }

        public void empty() {
            assertEquals("Checker was supposed to be empty, but was not. ", 0, holder.get().length());
        }

        public void next(Integer expected) {
            holder.getAndUpdate(actual -> {
                assertFalse("Checker is empty and should not be. ", actual.isEmpty());
                assertEquals("Expected index was out of order. ", expected, actual.head());

                return actual.tail();
            });
        }
    }

    private Checker checker(int max) {
        return new Checker(max);
    }

    private <T> void wait(Future<T> future, int time, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<Try<T>> result = new CompletableFuture<>();

        future.onCompletedTry(result::complete);

        //await returns after result has finished, but NOT after callbacks have finished.
        result.get(time, unit);
    }

    @Test
    public void basicMapTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(3);

        Future<Integer> first = Future.of(() -> {
            Thread.sleep(100);
            order.next(0);
            return 6;
        });

        Future<Integer> second = first.map(f -> {
            order.next(1);
            return f * 2;
        });

        Future<Integer> third = second.map(s -> {
            order.next(2);
            return s / 3;
        });


        Future<Integer> last = third.andThen(t -> t.forEach((Integer thirdResult) -> {
            int firstResult = first.value().get().get();
            int secondResult = second.value().get().get();

            order.next(3);
            assertEquals(6, firstResult);
            assertEquals(12, secondResult);
            assertEquals(4, thirdResult.intValue());
        }));

        last.onFailure(failOnError("Caught exception: "));

        wait(last, 200, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void concurentMapTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(4);

        Future<Integer> first = new Future<>(() -> {
            Thread.sleep(100);
            order.next(0);
            return 6;
        });

        Future<Integer> second = first.map(f -> {
                    Try.run(() -> Thread.sleep(150));
                    order.next(4);
                    return f * 2;
                }
        );

        Future<Integer> third = first.map(s -> {
                    Try.run(() -> Thread.sleep(100));
                    order.next(3);
                    return s / 3;
                }
        );
        Future<Integer> fourth = first.map(s -> {
                    Try.run(() -> Thread.sleep(50));
                    order.next(2);
                    return s * 3;
                }
        );
        Future<Integer> fifth = first.map(s -> {
                    order.next(1);
                    return s + 3;
                }
        );

        second.onSuccess(i -> assertEquals(12, i.intValue()));
        second.onFailure(failOnError("Second calculation failed with exception: "));

        third.onSuccess(i -> assertEquals(2, i.intValue()));
        third.onFailure(failOnError("Third calculation failed with exception: "));

        fourth.onSuccess(i -> assertEquals(18, i.intValue()));
        fourth.onFailure(failOnError("Fourth calculation failed with exception: "));

        fifth.onSuccess(i -> assertEquals(9, i.intValue()));
        fifth.onFailure(failOnError("Fifth calculation failed with exception: "));

        wait(second, 500, TimeUnit.MILLISECONDS);
        order.empty();
    }


    @Test
    public void andThenTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(4);


        Future<Integer> first = new Future<>(() -> {
            Thread.sleep(100);
            order.next(0);
            return 6;
        }).andThen(f -> {
            Try.run(() -> Thread.sleep(150));
            order.next(1);
        }).andThen(s -> {
            Try.run(() -> Thread.sleep(100));
            order.next(2);
        }).andThen(s -> {
            Try.run(() -> Thread.sleep(50));
            order.next(3);
        }).andThen(s -> {
            order.next(4);
        });

        wait(first, 10000, TimeUnit.MILLISECONDS);
        order.empty();

    }

    @Test
    public void traverseTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        List<Integer> source = List.range(0, 10);

        Future<List<Integer>> total = Future.traverse(source, i -> new Future<Integer>(() -> {
            int pauseTime = 100 - i * 10;
            Thread.sleep(pauseTime);
            return i;
        }));

        total.onFailure(failOnError("Traversal failed with error: "));

        Future<List<Integer>> result = total.map(list -> {
            order.next(0);

            assertEquals(list, source);
            return list;
        });

        wait(result, 20_000, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void callBackTest() throws InterruptedException, TimeoutException, ExecutionException {
        Checker order = checker(0);

        List<Integer> source = List.range(0, 10);

        List<Future<Integer>> list = source.map(i -> new Future<Integer>(() -> {
            int pauseTime = 100 - i * 10;
            Thread.sleep(pauseTime);
            return i;
        })).map(f -> {
            source.forEach(j ->
                            f.onCompletedTry(t -> assertTrue(t.isSuccess()))
            );
            return f;
        });

        Future<List<Integer>> result = Future.sequence(list).map(l -> {
            order.next(0);
            return l;
        });

        wait(result, 2_000, TimeUnit.MILLISECONDS);
        assertEquals(source, result.value().get().get());
        order.empty();
    }

    static class TestException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }

    @Test
    public void failureTest() throws InterruptedException, ExecutionException, TimeoutException {


        Checker order = checker(2);

        Future<Integer> first = Future.of(() -> {
            order.next(0);
            throw new TestException();
        });

        Future<Integer> second = first.map(i -> i);


        first.onCompleted(i -> fail("This test should have failed! "), t -> {
            order.next(1);
            assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
        });

        second.onCompleted(i -> fail("This test should have failed! "), t -> {
            order.next(2);
            assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
        });

        wait(second, 2_000, TimeUnit.MILLISECONDS);
        order.empty();
    }

    final Function1<Try<Integer>, Try<Integer>> mapper = t -> {
        if (t.isSuccess())
            return t.flatMap(i -> new Failure<>(new TestException()));
        else
            return t.recoverWith(e -> new Success<>(0));
    };

    @Test
    public void mapTryFailureTest() throws TimeoutException, InterruptedException, ExecutionException {
        Checker order = checker(1);


        Future<Integer> first = Future.of(() -> {
            order.next(0);
            throw new TestException();
        });

        Future<Integer> second = first.mapTry(mapper);

        Future<Integer> third = second.andThen(t ->
                t.map(i -> {
                    order.next(1);
                    assertEquals(0, i.intValue());
                    return i;
                }));

        wait(third, 200, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void mapTryPassTest() throws TimeoutException, InterruptedException, ExecutionException {
        Checker order = checker(1);


        Future<Integer> first = Future.of(() -> {
            order.next(0);
            return 5;
        });

        Future<Integer> second = first.mapTry(mapper);

        Future<Integer> third = second.recover(t -> {
            order.next(1);
            assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
            return 0;
        });

        wait(third, 200, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void failedProjectionTest() throws TimeoutException, InterruptedException, ExecutionException {
        Checker order = checker(1);


        Future<Integer> first = Future.of(() -> {
            order.next(0);
            throw new TestException();
        });

        Future<Throwable> second = first.failed();

        Future<Throwable> third = second.andThen(t ->
                t.map(i -> {
                    order.next(1);
                    assertTrue("Test failed, but was not the expected error: ", i instanceof TestException);
                    return i;
                }));

        wait(third, 200, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void completableFutureConversionTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(1);

        CompletableFuture<Integer> first = new CompletableFuture<>();


        Future<Integer> second = Future.of(first);

        Future<Integer> third = second.andThen(t -> order.next(1));

        order.next(0);

        first.complete(0);

        wait(third, 40, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void completableFutureConversionHiddenTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(1);

        CompletableFuture<Integer> first = new CompletableFuture<>();

        //noinspection UnnecessaryLocalVariable
        java.util.concurrent.Future<Integer> alias = first;

        Future<Integer> second = Future.of(alias);

        Future<Integer> third = second.andThen(t -> order.next(1));

        order.next(0);

        first.complete(0);

        wait(third, 40, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void futureConverterTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        ExecutorService ex = Executors.newSingleThreadExecutor();

        Future<Integer> first = Future.of(ex.submit(() -> {
            order.next(0);
            return 0;
        }));

        wait(first, 20_000, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void completeBeforeCallback() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        Future<Integer> first = Future.success(1);

        first.andThen(t -> order.next(0));

        wait(first, 20, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void checkedSupplierOf() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        Try.CheckedSupplier<Integer> func = () -> {
            order.next(0);
            return 9;
        };

        Future<Integer> first = Future.of(func);

        wait(first, 20, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void completedTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        Future<Integer> first = Future.completed(new Success<>(0));

        Future<Integer> second = first.andThen(i -> order.next(0));

        wait(second, 20, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void failuredTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        Promise<Integer> p = new Promise<>();
        Future.failed(new TestException()).onFailure(t -> {
            order.next(0);
            p.success(0);
        });

        wait(p.future(), 20, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void completePromiseTwiceTest() {
        try {
            Promise<Integer> p = new Promise<>();

            assertFalse(p.isCompleted());

            p.success(0);

            assertTrue(p.isCompleted());

            assertEquals(new Some<>(new Success<>(0)), p.value());

            p.success(1);
            fail("Promise should have thrown exception when a second call to complete was made, but it didn't!");
        } catch (IllegalStateException e) {
            assertEquals("This Promise has already been completed!", e.getMessage());
        }

    }

    @Test
    public void completePromiseWithFutureTwiceTest() {
        try {
            Promise<Integer> p = new Promise<>();

            p.success(0);
            p.completeWith(Future.success(1));
            fail("Promise should have thrown exception when a second call to complete was made, but it didn't!");
        } catch (IllegalStateException e) {
            assertEquals("This Promise has already been completed!", e.getMessage());
        }

    }

    @Test
    public void completeFutureTwiceTest() {
        try {
            Future<Integer> p = new Future<>();

            assertFalse(p.isCompleted());

            p.complete(new Success<>(0));

            assertTrue(p.isSuccess());
            assertFalse(p.isFailure());

            p.complete(new Success<>(1));
            fail("Future should have thrown exception when a second call to complete was made, but it didn't!");
        } catch (IllegalStateException e) {
            assertEquals("This Future has already been completed!", e.getMessage());
        }

    }

    @Test
    public void flatMapTryTest() {
        Future<Integer> first = Future.success(0);

        first.flatMapTry(t -> Future.success(new Success<>(1)));
    }

    @Test
    public void tupleSequenceTest() throws InterruptedException, ExecutionException, TimeoutException {
        Checker order = checker(0);

        Future<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> first = Future.sequence(
                Tuple.of(Future.success(0), Future.success(1), Future.success(2), Future.success(3),
                        Future.success(4), Future.success(5), Future.success(6), Future.success(7)));

        Future<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> second =
                first.andThen(t -> t.forEach(bunch -> {
                    assertEquals(bunch, Tuple.of(0, 1, 2, 3, 4, 5, 6, 7));
                    order.next(0);
                }));


        wait(second, 20, TimeUnit.MILLISECONDS);
        order.empty();
    }

    @Test
    public void cancelPromiseTest() throws InterruptedException, ExecutionException, TimeoutException {
        Promise<Integer> p = new Promise<>();

        p.cancel();

        Future<Integer> second = p.future().andThen(t -> {
            t.onFailure(e -> {
                assertEquals("Promise was broken!", e.getMessage());
            });
            t.forEach(v -> {
                fail("Promise should have been cancled but was not!");
            });
        });

        wait(second, 20, TimeUnit.MILLISECONDS);
    }

    @Test
    public void feedPromiseOwnFutureTest() {
        Promise<Integer> p = new Promise<>();

        try {
            p.completeWith(p.future());
            fail("Promise should have thrown exception when completed with it's own future but it didn't!");
        } catch (IllegalArgumentException e) {
            assertEquals("Can't complete a Future with itself!", e.getMessage());
        }
    }

    @Test
    public void readyTest() throws TimeoutException, InterruptedException {
        Future.success(0).ready(1, TimeUnit.MILLISECONDS);
    }

    @Test
    public void notReadyTest() throws InterruptedException {
        try {
            Future.of(() -> {
                Thread.sleep(20);
                return 0;
            }).ready(1, TimeUnit.MILLISECONDS);

            fail("Future should have timed out while waiting but didn't!");
        } catch (TimeoutException e) {
            assertEquals("Future did not finish within given time limit.", e.getMessage());
        }
    }

    @Test
    public void notReadyYetTest() throws InterruptedException, TimeoutException {
        Future.of(() -> {
            Thread.sleep(5);
            return 0;
        }).ready(20, TimeUnit.MILLISECONDS);
    }

    @Test
    public void forEachTest() {
        Checker order = checker(0);

        Future.success(0).forEach(order::next);

        order.empty();
    }

    @Test
    public void filterTest() {
        assertEquals(1, Future.success(1).filter(i -> i > 0).value().get().get().intValue());
    }

    @Test
    public void failFilterTest() {
        assertEquals("Predicate does not hold for -1",
                Future.success(-1).filter(i -> i > 0).value().get().failed().get().getMessage());


    }

    @Test
    public void recoverWithTest() {
        assertEquals(0, Future.<Integer> failed(new TestException())
                .recoverWith(t -> Future.success(0))
                .value().get().get().intValue());
    }

    @Test
    public void tuple1SequenceTest() {
        assertEquals(0, Future.sequence(Tuple.of(Future.success(0))).value().get().get()._1.intValue());
    }

    @Test
    public void fallBackTest() {
        assertEquals(0, Future.<Integer> failed(new TestException())
                .fallbackTo(Future.success(0)).value().get().get().intValue());

    }
}
