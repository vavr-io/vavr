/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;


import javaslang.Function1;
import javaslang.collection.List;
import javaslang.control.Failure;
import javaslang.control.Success;
import javaslang.control.Try;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class FutureTest {

//	private Consumer<Throwable> failOnError(String message){
//		return (t) -> fail(message + t.getMessage());
//	}
//
//	private static class Checker {
//
//		private final AtomicReference<List<Integer>> holder;
//
//		public Checker(int max) {
//			holder = new AtomicReference<>(List.range(0, max + 1));
//		}
//
//		public void empty() {
//			assertEquals("Checker was supposed to be empty, but was not. ", 0, holder.get().length());
//		}
//
//		public void next(Integer expected) {
//			holder.getAndUpdate(actual -> {
//				assertFalse("Checker is empty and should not be. ", actual.isEmpty());
//				assertEquals("Expected index was out of order. ", expected, actual.head());
//
//				return actual.tail();
//			});
//		}
//	}
//
//	private Checker checker(int max) {
//		return new Checker(max);
//	}
//
//    @Test
//    public void basicMapTest() throws InterruptedException, ExecutionException, TimeoutException {
//	    Checker order = checker(3);
//
//        Future<Integer> first = Future.of(() -> {
//            Thread.sleep(100);
//	        order.next(0);
//            return 6;
//        });
//
//        Future<Integer> second = first.map(f -> {
//	        order.next(1);
//	        return f * 2;
//        });
//
//        Future<Integer> third = second.map(s -> {
//	        order.next(2);
//	        return s / 3;
//        });
//
//
//        third.onSuccess((Integer thirdResult) -> {
//	        int firstResult = first.value().get().get();
//	        int secondResult = second.value().get().get();
//
//	        order.next(3);
//	        assertEquals(6, firstResult);
//	        assertEquals(12, secondResult);
//	        assertEquals(4, thirdResult.intValue());
//
//        });
//
//	    third.onFailure(failOnError("Caught exception: "));
//
//        third.await(200, TimeUnit.MILLISECONDS);
//	    order.empty();
//    }
//
//    @Test
//    public void concurentMapTest() throws InterruptedException, ExecutionException, TimeoutException {
//	    Checker order = checker(4);
//
//        Future<Integer> first = new Future<>(() -> {
//            Thread.sleep(100);
//	        order.next(0);
//            return 6;
//        });
//
//        Future<Integer> second = first.map(f -> {
//			        Try.run(() -> Thread.sleep(150));
//					order.next(4);
//	                return f * 2;
//		        }
//        );
//
//        Future<Integer> third = first.map(s -> {
//	                Try.run(() -> Thread.sleep(100));
//					order.next(3);
//	                return s / 3;
//		        }
//        );
//        Future<Integer> fourth = first.map(s -> {
//	                Try.run(() -> Thread.sleep(50));
//					order.next(2);
//	                return s * 3;
//		        }
//        );
//        Future<Integer> fifth = first.map(s -> {
//			        order.next(1);
//	                return s + 3;
//		        }
//        );
//
//	    second.onSuccess(i -> assertEquals(12, i.intValue()));
//	    second.onFailure(failOnError("Second calculation failed with exception: "));
//
//	    third.onSuccess(i -> assertEquals(2, i.intValue()));
//	    third.onFailure(failOnError("Third calculation failed with exception: "));
//
//	    fourth.onSuccess(i -> assertEquals(18, i.intValue()));
//	    fourth.onFailure(failOnError("Fourth calculation failed with exception: "));
//
//	    fifth.onSuccess(i -> assertEquals(9, i.intValue()));
//	    fifth.onFailure(failOnError("Fifth calculation failed with exception: "));
//
//        second.await(500, TimeUnit.MILLISECONDS);
//	    order.empty();
//    }
//
//
//    @Test
//    public void andThenTest() throws InterruptedException, ExecutionException, TimeoutException {
//	    Checker order = checker(4);
//
//
//        Future<Integer> first = new Future<>(() -> {
//            Thread.sleep(100);
//	        order.next(0);
//            return 6;
//        }).andThen(f -> {
//            Try.run(() -> Thread.sleep(150));
//	        order.next(1);
//        }).andThen(s -> {
//            Try.run(() -> Thread.sleep(100));
//	        order.next(2);
//        }).andThen(s -> {
//            Try.run(() -> Thread.sleep(50));
//	        order.next(3);
//        }).andThen(s -> {
//	        order.next(4);
//        });
//
//        first.await(10000, TimeUnit.MILLISECONDS);
//	    order.empty();
//
//    }
//
//    @Test
//    public void traverseTest() throws InterruptedException, ExecutionException, TimeoutException {
//	    Checker order = checker(0);
//
//        List<Integer> source = List.range(0, 10);
//
//        Future<List<Integer>> total = Future.traverse(source, i -> new Future<>(() -> {
//            int pauseTime = 100 - i * 10;
//            Thread.sleep(pauseTime);
//            return i;
//        }));
//
//        total.onCompleted(list -> {
//	        order.next(0);
//            assertEquals(list, source);
//        }, failOnError("Traversal failed with error: "));
//
//        total.await(20_000, TimeUnit.MILLISECONDS);
//        Thread.sleep(10);
//	    order.empty();
//    }
//
//    @Test
//    public void callBackTest() throws InterruptedException, TimeoutException {
//	    Checker order = checker(0);
//
//        List<Integer> source = List.range(0, 10);
//
//        List<Future<Integer>> list = source.map(i -> new Future<Integer>(() -> {
//            int pauseTime = 100 - i * 10;
//            Thread.sleep(pauseTime);
//            return i;
//        })).map(f -> {
//            source.forEach(j ->
//                            f.onCompletedTry(t -> assertTrue(t.isSuccess()))
//            );
//            return f;
//        });
//
//        Future<List<Integer>> result = Future.sequence(list).map(l -> {order.next(0); return l;});
//	    result.await(2_000, TimeUnit.MILLISECONDS);
//	    assertEquals(source, result.value().get().get());
//	    order.empty();
//    }
//
//	static class TestException extends RuntimeException {
//		private static final long serialVersionUID = 1L;
//	}
//
//	@Test
//	public void failureTest() throws InterruptedException, ExecutionException, TimeoutException {
//
//
//		Checker order = checker(2);
//
//		Future<Integer> first = Future.of(() -> {
//			order.next(0);
//			throw new TestException();
//		});
//
//		Future<Integer> second = first.map(i -> i);
//
//
//		first.onCompleted(i -> fail("This test should have failed! "), t -> {
//			order.next(1);
//			assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
//		});
//
//		second.onCompleted(i -> fail("This test should have failed! "), t -> {
//			order.next(2);
//			assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
//		});
//
//		second.await(2_000, TimeUnit.MILLISECONDS);
//
//		Thread.sleep(10);
//		order.empty();
//	}
//
//	final Function1<Try<Integer>, Try<Integer>> mapper = t -> {
//		if(t.isSuccess())
//			return t.flatMap(i -> new Failure<>(new TestException()));
//		else
//			return t.recoverWith(e -> new Success<>(0));
//	};
//
//	@Test
//	public void mapTryFailureTest() throws TimeoutException, InterruptedException {
//		Checker order = checker(1);
//
//
//		Future<Integer> first = Future.of(() -> {
//			order.next(0);
//			throw new TestException();
//		});
//
//		Future<Integer> second = first.mapTry(mapper);
//
//		Future<Integer> third = second.andThen(t ->
//			t.map(i -> {
//				order.next(1);
//				assertEquals(0, i.intValue());
//				return i;
//			}));
//
//		third.await(200, TimeUnit.MILLISECONDS);
//		order.empty();
//	}
//
//	@Test
//	public void mapTryPassTest() throws TimeoutException, InterruptedException {
//		Checker order = checker(1);
//
//
//		Future<Integer> first = Future.of(() -> {
//			order.next(0);
//			return 5;
//		});
//
//		Future<Integer> second = first.mapTry(mapper);
//
//		Future<Integer> third = second.recover(t -> {
//			order.next(1);
//			assertTrue("Test failed, but was not the expected error: ", t instanceof TestException);
//			return 0;
//		});
//
//		third.await(200, TimeUnit.MILLISECONDS);
//		order.empty();
//	}
//
//	@Test
//	public void failedProjectionTest() throws TimeoutException, InterruptedException {
//		Checker order = checker(1);
//
//
//		Future<Integer> first = Future.of(() -> {
//			order.next(0);
//			throw new TestException();
//		});
//
//		Future<Throwable> second = first.failed();
//
//		Future<Throwable> third = second.andThen(t ->
//				t.map(i -> {
//					order.next(1);
//					assertTrue("Test failed, but was not the expected error: ", i instanceof TestException);
//					return i;
//				}));
//
//		third.await(200, TimeUnit.MILLISECONDS);
//		order.empty();
//	}
}
