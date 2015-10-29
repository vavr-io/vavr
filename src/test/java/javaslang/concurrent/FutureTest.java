/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import javaslang.control.None;
import javaslang.control.Try;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class FutureTest {

    @Test
    public void shouldInterruptLockedFuture() {

        final Future<?> future = Future.of(() -> {
            final Object lock = new Object();
            synchronized (lock) {
                lock.wait();
            }
            return null;
        });

        future.onComplete(r -> Assertions.fail("future should lock forever"));

        int count = 0;
        while (!future.isCompleted() && !future.isCancelled()) {
            Try.run(() -> Thread.sleep(100));
            if (++count > 3) {
                future.cancel();
            }
        }

        assertCancelled(future);
    }

    // checks the invariant for cancelled state
    void assertCancelled(Future<?> future) {
        assertThat(future.isCancelled()).isTrue();
        assertThat(future.isCompleted()).isFalse();
        assertThat(future.getValue()).isEqualTo(None.instance());
    }
}
