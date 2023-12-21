/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.concurrent;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static java.util.concurrent.ForkJoinPool.commonPool;
import static org.assertj.core.api.Assertions.fail;

final class Concurrent {

    private static final Random RND = new Random();

    // Max sleep time to delay computation
    private static final int SLEEP_MAX_MILLIS = 150;

    private Concurrent() {
    }

    static void waitUntil(Supplier<Boolean> condition) {
        long millis = 1;
        boolean interrupted = false;
        while (!interrupted && !condition.get()) {
            if (millis > 4096) {
                fail("Condition not met.");
            } else {
                try {
                    Thread.sleep(millis);
                    millis = millis << 1;
                } catch(InterruptedException x) {
                    interrupted = true;
                }
            }
        }
    }

    /**
     * Block current thread a random time between 0 and {@link #SLEEP_MAX_MILLIS} ms.
     */
    static void zZz() {
        Try.run(() -> Thread.sleep(RND.nextInt(SLEEP_MAX_MILLIS)));
    }

    static <T> CheckedFunction0<T> zZz(T value) {
        return () -> {
            zZz();
            return value;
        };
    }

    static <T, X extends Throwable> CheckedFunction0<T> zZz(X exception) {
        return () -> {
            zZz();
            throw exception;
        };
    }

    static void gracefullyFinishThreads() throws TimeoutException {
        final boolean isQuiescent = ForkJoinPool.commonPool().awaitQuiescence(1L, TimeUnit.MINUTES);
        if (isQuiescent) {
            System.out.println("ForkJoinPool.commonPool() is quiescent");
        } else {
            throw new TimeoutException("Timeout while waiting for running threads in ForkJoinPool.commonPool() to finish.");
        }
    }
}
