/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.Try;
import javaslang.control.Try.CheckedSupplier;

import java.util.Random;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.fail;

interface Concurrent {

    Random RND = new Random();

    // Max wait time for results = WAIT_MILLIS * WAIT_COUNT (however, most probably it will take only WAIT_MILLIS * 1)
    long WAIT_MILLIS = 50;
    int WAIT_COUNT = 100;

    // Max sleep time to delay computation
    int SLEEP_MAX_MILLIS = 150;

    /**
     * Frequently checking if something happened by testing a condition.
     * If after {@link #WAIT_COUNT} * {@link #WAIT_MILLIS} ms nothing happened, an {@code AssertionError} is thrown.
     *
     * @param condition A condition.
     */
    static void waitUntil(Supplier<Boolean> condition) {
        int count = 0;
        while (!condition.get()) {
            if (++count > WAIT_COUNT) {
                fail("Condition not met.");
            } else {
                Try.run(() -> Thread.sleep(WAIT_MILLIS));
            }
        }
    }

    /**
     * Block current thread a random time between 0 and {@link #SLEEP_MAX_MILLIS} ms.
     */
    static void zZz() {
        Try.run(() -> Thread.sleep(RND.nextInt(SLEEP_MAX_MILLIS)));
    }

    static <T> CheckedSupplier<T> zZz(T value) {
        return () -> {
            zZz();
            return value;
        };
    }

    static <T, X extends Throwable> CheckedSupplier<T> zZz(X exception) {
        return () -> {
            zZz();
            throw exception;
        };
    }

    static Void waitForever() {
        while (true) {
            Try.run(() -> Thread.sleep(WAIT_MILLIS));
        }
    }
}
