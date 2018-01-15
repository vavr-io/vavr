/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
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
package io.vavr;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.fail;

public class CheckedConsumerTest {

    @Test
    public void shouldApplyNonThrowingCheckedConsumer() {
        final CheckedConsumer<?> f = t -> {};
        try {
            f.accept(null);
        } catch(Throwable x) {
            fail("should not have thrown", x);
        }
    }

    @Test
    public void shouldApplyThrowingCheckedConsumer() {
        final CheckedConsumer<?> f = t -> { throw new Error(); };
        try {
            f.accept(null);
            fail("should have thrown");
        } catch(Throwable x) {
            // ok
        }
    }

    @Test
    public void shouldThrowWhenComposingCheckedConsumerUsingAndThenWithNullParameter() {
        final CheckedConsumer<?> f = t -> {};
        assertThatThrownBy(() -> f.andThen(null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldComposeCheckedConsumerUsingAndThenWhenFirstOneSucceeds() {
        final AtomicBoolean result = new AtomicBoolean(false);
        final CheckedConsumer<?> f = t -> {};
        try {
            f.andThen(ignored -> result.set(true)).accept(null);
            assertThat(result.get()).isTrue();
        } catch(Throwable x) {
            fail("should not have thrown", x);
        }
    }

    @Test
    public void shouldComposeCheckedConsumerUsingAndThenWhenFirstOneFails() {
        final AtomicBoolean result = new AtomicBoolean(false);
        final CheckedConsumer<?> f = t -> { throw new Error(); };
        try {
            f.andThen(ignored -> result.set(true)).accept(null);
            fail("should have thrown");
        } catch(Throwable x) {
            assertThat(result.get()).isFalse();
        }
    }
}
