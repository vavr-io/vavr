package javaslang.control;

import javaslang.*;
import javaslang.collection.List;

import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * Created by LordBlackHole on 6/21/2015.
 */
public class Futures {

    public static <T> Future<List<Try<T>>> sequence(Iterable<Future<T>> source, Executor ex){
        Future<List<Try<T>>> start = Future.completed(List.<Try<T>>nil());

        return List.ofAll(source).foldLeft(start, (accumulator, next) -> {
            Promise<List<Try<T>>> result = new Promise<>(); //Each input needs to return a new promise. This is to ensure that the list returns in the same order as the input.

            accumulator.andThen(mainTry -> {
                mainTry.onFailure(result::failure);
                mainTry.forEach(list -> next.onCompletedTry(nextTry -> result.success(list.append(nextTry)), ex));
            }, ex);

            return result.future();
        });
    }

    public static <T> Future<List<Try<T>>> sequence(Iterable<Future<T>> source){
        return sequence(source, ForkJoinPool.commonPool());
    }

    public static <T> Future<List<T>> flatSequence(Iterable<Future<T>> source, Executor ex){
        return sequence(source, ex).map(list -> list.flatMap(Function1.identity()));
    }

    public static <T> Future<List<T>> flatSequence(Iterable<Future<T>> source){
        return flatSequence(source, ForkJoinPool.commonPool());
    }

    public static <T> Future<List<Try<T>>> traverse(Iterable<T> source, Function1<T, Future<T>> func, Executor ex){
        Objects.requireNonNull(func, "func is null");
        return sequence(List.ofAll(source).map(func), ex);
    }

    public static <T> Future<List<Try<T>>> traverse(Iterable<T> source, Function1<T, Future<T>> func){
        return traverse(source, func, ForkJoinPool.commonPool());
    }

    public static <T> Future<List<T>> flatTraverse(Iterable<T> source, Function1<T, Future<T>> func, Executor ex){
        return traverse(source, func, ex).map(list -> list.flatMap(Function1.identity()));
    }

    public static <T> Future<List<T>> flatTraverse(Iterable<T> source, Function1<T, Future<T>> func){
        return flatTraverse(source, func, ForkJoinPool.commonPool());
    }

    public static <T1> Future<Tuple1<T1>> sequence(Tuple1<Future<T1>> source){
        return source._1.map(v -> new Tuple1<>(v));
    }

    public static <T1, T2> Future<Tuple2<T1, T2>> sequence(Tuple2<Future<T1>, Future<T2>> source){
        return source._1.flatMap(v1 -> source._2.map(v2 -> new Tuple2<>(v1, v2)));
    }

    public static <T1, T2, T3> Future<Tuple3<T1, T2, T3>> sequence(Tuple3<Future<T1>, Future<T2>, Future<T3>> source){
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2)), source._3))
                .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._2));

    }

    public static <T1, T2, T3, T4> Future<Tuple4<T1, T2, T3, T4>> sequence(Tuple4<Future<T1>, Future<T2>, Future<T3>, Future<T4>> source){
        return sequence(Tuple.of(sequence(Tuple.of(source._1, source._2, source._3)), source._4))
                .map(nested -> Tuple.of(nested._1._1, nested._1._2, nested._1._3, nested._2));

    }
}
