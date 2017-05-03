/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.concurrent;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;

import java.util.Random;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.fail;

final class Concurrent {

    private static final Random RND = new Random();

    // Max wait time for results = WAIT_MILLIS * WAIT_COUNT (however, most probably it will take only WAIT_MILLIS * 1)
    private static final long WAIT_MILLIS = 50;
    private static final int WAIT_COUNT = 100;

    // Max sleep time to delay computation
    private static final int SLEEP_MAX_MILLIS = 150;

    private Concurrent() {
    }

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

    static Void waitForever() {
        while (true) {
            Try.run(() -> Thread.sleep(WAIT_MILLIS));
        }
    }
}
