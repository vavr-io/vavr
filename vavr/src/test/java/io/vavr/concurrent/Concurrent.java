/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
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
            System.out.println("ForkJoinPool.commonPool() is quiecent");
        } else {
            throw new TimeoutException("Timeout while waiting for running threads in ForkJoinPool.commonPool() to finish.");
        }
    }
}
