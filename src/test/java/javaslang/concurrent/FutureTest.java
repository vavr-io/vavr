/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;


import javaslang.collection.List;
import javaslang.control.Try;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class FutureTest {


    @Test
    public void basicMapTest() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("Starting FutureTest.basicMapTest");

        Future<Integer> first = new Future<>(() -> {
            System.out.println("Starting First calc");
            Thread.sleep(1000);
            System.out.println("Finishing First calc");
            return 6;
        });

        Future<Integer> second = first.map(f -> {System.out.println("Starting Second calc"); return f * 2;});

        Future<Integer> third = second.map(s -> {System.out.println("Starting Third calc"); return s / 3;});


        third.onCompleted((Integer thirdResult) -> {
            System.out.println("Making callBack");
            int firstResult = first.value().get().get();
            int secondResult = second.value().get().get();

            assertEquals(6, firstResult);
            assertEquals(12, secondResult);
            assertEquals(4, thirdResult.intValue());

        }, (t) -> Assert.fail());

        third.await(1100, TimeUnit.MILLISECONDS);
    }

    @Test
    public void concurentMapTest() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("Starting FutureTest.concurentMapTest");

        Future<Integer> first = new Future<>(() -> {
            Thread.sleep(1000);
            System.out.println("Finishing First calc");
            return 6;
        });

        Future<Integer> second = first.map(f -> {
            Try.run(() -> Thread.sleep(1500)); System.out.println("Finishing Second calc"); return f * 2;});

        Future<Integer> third = first.map(s -> {Try.run(() -> Thread.sleep(1000)); System.out.println("Finishing Third calc");return s / 3;});
        Future<Integer> fourth = first.map(s -> {Try.run(() -> Thread.sleep(500)); System.out.println("Finishing Fourth calc");return s * 3;});
        Future<Integer> fifth = first.map(s -> {System.out.println("Finishing Fifth calc"); return s + 3;});

        second.await(3000, TimeUnit.MILLISECONDS);
    }


    @Test
    public void andThenTest() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("Starting FutureTest.andThenTest");

        Future<Integer> first = new Future<>(() -> {
            Thread.sleep(1000);
            System.out.println("Finishing First calc");
            return 6;
        }).andThen(f -> {
            Try.run(() -> Thread.sleep(1500));
            System.out.println("Finishing Second calc");
        }).andThen(s -> {
            Try.run(() -> Thread.sleep(1000));
            System.out.println("Finishing Third calc");
        }).andThen(s -> {
            Try.run(() -> Thread.sleep(500));
            System.out.println("Finishing Fourth calc");
        }).andThen(s -> System.out.println("Finishing Fifth calc"));

        first.await(10000, TimeUnit.MILLISECONDS);


    }

    @Test
    @Ignore
    public void massiveTest() throws InterruptedException, ExecutionException, TimeoutException {
        for(int i = 0; i < 100; i++) {
            traverseTest();
        }
    }

    @Test
    public void traverseTest() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("Starting FutureTest.traverseTest");

        Random rand = new Random();
        List<Integer> source = List.range(0, 10);

        Future<List<Integer>> total = Future.traverse(source, i -> new Future<>(() -> {
            int pauseTime = rand.nextInt(1000);
            System.out.println("Pausing for " + pauseTime + " while calculating value: " + i);
            Thread.sleep(pauseTime);
            System.out.println("Finished calculating value: " + i);
            return i;
        }));

        total.onCompleted(list -> {
            System.out.println("Final result: " + list.toString());
            assertEquals(list, source);
        }, e -> Assert.fail());

        total.await(20_000, TimeUnit.MILLISECONDS);
        Thread.sleep(1000);
        System.out.println("Finished FutureTest.traverseTest");

    }







}
