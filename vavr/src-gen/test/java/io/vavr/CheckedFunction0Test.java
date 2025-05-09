/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
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
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.vavr.control.Try;
import java.lang.CharSequence;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class CheckedFunction0Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference() {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction0.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction0.lift(() -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction0<Object> f = () -> null;
        assertThat(f.arity()).isEqualTo(0);
    }

    @Test
    public void shouldConstant() throws Throwable {
        final CheckedFunction0<Object> f = CheckedFunction0.constant(6);
        assertThat(f.apply()).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction0<Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction1<Tuple0, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction0<Object> f = () -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction0<Integer> f = () -> integer.getAndIncrement();
        final CheckedFunction0<Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply();
        // should return memoized value of second apply()
        assertThat(memo.apply()).isEqualTo(expected);

    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction0<Integer> f = () -> null;
        final CheckedFunction0<Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction0<Integer> f = () -> null;
        final CheckedFunction0<Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldRecover() {
        final AtomicInteger integer = new AtomicInteger();
        CheckedFunction0<MessageDigest> digest = () -> MessageDigest.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
        Function0<MessageDigest> recover = digest.recover(throwable -> () -> null);
        MessageDigest md5 = recover.apply();
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        integer.incrementAndGet();
        assertThat(recover.apply()).isNull();
    }

    @Test
    public void shouldRecoverNonNull() {
        final AtomicInteger integer = new AtomicInteger();
        CheckedFunction0<MessageDigest> digest = () -> MessageDigest.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
        Function0<MessageDigest> recover = digest.recover(throwable -> null);

        MessageDigest md5 = recover.apply();
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);

        integer.incrementAndGet();
        Try<MessageDigest> unknown = Function0.liftTry(recover).apply();
        assertThat(unknown).isNotNull();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
    }

    @Test
    public void shouldUncheckedWork() {
        CheckedFunction0<MessageDigest> digest = () -> MessageDigest.getInstance("MD5");
        Function0<MessageDigest> unchecked = digest.unchecked();
        MessageDigest md5 = unchecked.apply();
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
    }

    @Test
    public void shouldThrowCheckedExceptionWhenUnchecked() {
        assertThrows(NoSuchAlgorithmException.class, () -> {
            CheckedFunction0<MessageDigest> digest = () -> MessageDigest.getInstance("Unknown");
            Function0<MessageDigest> unchecked = digest.unchecked();
            unchecked.apply(); // Look ma, we throw an undeclared checked exception!
        });
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        final AtomicInteger integer = new AtomicInteger();
        CheckedFunction0<MessageDigest> digest = () -> MessageDigest.getInstance(integer.get() == 0 ? "MD5" : "Unknown");
        Function0<Try<MessageDigest>> liftTry = CheckedFunction0.liftTry(digest);
        Try<MessageDigest> md5 = liftTry.apply();
        assertThat(md5.isSuccess()).isTrue();
        assertThat(md5.get()).isNotNull();
        assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.get().getDigestLength()).isEqualTo(16);

        integer.incrementAndGet();
        Try<MessageDigest> unknown = liftTry.apply();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull();
        assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
    }

    private static final CheckedFunction0<Integer> recurrent1 = () -> 11;

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply()).isEqualTo(11);

    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction0<Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Nested
    class ComposeTests {

    }

    @Test
    public void shouldNarrow() throws Throwable{
        final CheckedFunction0<String> wideFunction = () -> "Zero args";
        final CheckedFunction0<CharSequence> narrowFunction = CheckedFunction0.narrow(wideFunction);

        assertThat(narrowFunction.apply()).isEqualTo("Zero args");
    }

}