package io.vavr.concurrent;

import io.vavr.Function1;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

/**
 * A utility class that creates a memoizing version of an arbitrary concurrent function. Recursive calls are trampolined
 * so as to avoid stack overflows.
 * @see "http://sebastian-millies.blogspot.de/2016/05/concurrent-recursive-function.html"
 */
class ConcurrentTrampoliningMemoizer<T, R> {
    private final ConcurrentMap<T, Promise<R>> memo;

    ConcurrentTrampoliningMemoizer(ConcurrentMap<T, Promise<R>> cache) {
        this.memo = cache;
    }

    Function1<T, Future<R>> memoize(Function1<T, Future<R>> f) {
        return t -> {
            Promise<R> r = memo.get(t);
            if (r == null) {
                // value not yet memoized: put a container in the map that will come to hold the value
                final Promise<R> compute = Promise.make();
                r = memo.putIfAbsent(t, compute);
                if (r == null) {
                    // only the thread that first has a cache miss calls the underlying function.
                    // recursive asynchronous calls are bounced off the task queue inside the default executor, avoiding stack overflows.
                    // the computed value is placed in the container.
                    Future<R> futureValue = Future.ofSupplier(() -> f.apply(t)).flatMap(Function1.identity());  // unwrap nested Future
                    r = compute.completeWith(futureValue);
                }
            }
            return r.future();
        };
    }
}
