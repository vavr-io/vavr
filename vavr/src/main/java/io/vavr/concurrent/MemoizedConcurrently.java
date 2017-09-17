package io.vavr.concurrent;

import io.vavr.Function1;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Marker interface for concurrently memoized functions.
 */
public interface MemoizedConcurrently {

    /**
     * Delegates to {@link #of(Function1, ConcurrentMap)}} using a <tt>ConcurrentHashMap</tt> as cache implementation.
     */
    static <T, R> Function1<T, Future<R>> of(Function1<T, Future<R>> function) {
        if (function instanceof MemoizedConcurrently) {
            return function; // make this method idempotent
        }
        return MemoizedConcurrently.of(function, new ConcurrentHashMap<>());
    }

    /**
     * Lift the given function to a thread-safe, concurrently memoizing version. The returned function computes the
     * return value for a given argument only once. On subsequent calls given the same argument the memoized value
     * is returned. The returned function has a few interesting properties:
     * <ul>
     * <li>The function does not permit {@code null} values.
     * <li>Different threads will always wind up using the same value instances, so the function may compute values
     * that are supposed to be singletons.
     * <li>Concurrent callers won't block each other.
     * </ul>
     * This method is idempotent, i. e. applying it to an already concurrently memoized function will return the
     * function unchanged.
     * @param function a possibly recursive (asynchronous) function
     * @param cache a structure that holds the memoized values
     * @return the memoizing equivalent
     */
    static <T, R> Function1<T, Future<R>> of(Function1<T, Future<R>> function, ConcurrentMap<T, Promise<R>> cache) {
        if (function instanceof MemoizedConcurrently) {
            return function; // make this method idempotent
        }
        ConcurrentTrampoliningMemoizer<T, R> memoizer = new ConcurrentTrampoliningMemoizer<>(cache);
        Function1<T, Future<R>> memoized = memoizer.memoize(function);
        return (Function1<T, Future<R>> & MemoizedConcurrently) memoized::apply; // mark as memoized using intersection type
    }
}
