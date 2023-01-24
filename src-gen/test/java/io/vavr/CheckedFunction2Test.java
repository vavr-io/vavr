/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import io.vavr.control.Try;
import java.lang.CharSequence;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CheckedFunction2Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction2.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(CheckedFunction2.lift((o1, o2) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.arity()).isEqualTo(2);
    }

    @Test
    public void shouldConstant() throws Throwable {
        final CheckedFunction2<Object, Object, Object> f = CheckedFunction2.constant(6);
        assertThat(f.apply(1, 2)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final Function1<Object, CheckedFunction1<Object, Object>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final CheckedFunction1<Tuple2<Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction2<Integer, Integer, Integer> f = (i1, i2) -> i1 + i2 + integer.getAndIncrement();
        final CheckedFunction2<Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 )).isEqualTo(2  + 3  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 )).isEqualTo(2  + 3  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final CheckedFunction2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final CheckedFunction2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final CheckedFunction2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static final CheckedFunction2<String, String, MessageDigest> digest = (s1, s2) -> MessageDigest.getInstance(s1 + s2);

    @Test
    public void shouldRecover() {
        final Function2<String, String, MessageDigest> recover = digest.recover(throwable -> (s1, s2) -> null);
        final MessageDigest md5 = recover.apply("M", "D5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("U", "nknown")).isNull();
    }

    @Test
    public void shouldRecoverNonNull() {
        final Function2<String, String, MessageDigest> recover = digest.recover(throwable -> null);
        final MessageDigest md5 = recover.apply("M", "D5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        final Try<MessageDigest> unknown = Function2.liftTry(recover).apply("U", "nknown");
        assertThat(unknown).isNotNull();
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull().isInstanceOf(NullPointerException.class);
        assertThat(unknown.getCause().getMessage()).isNotEmpty().isEqualToIgnoringCase("recover return null for class java.security.NoSuchAlgorithmException: Unknown MessageDigest not available");
    }

    @Test
    public void shouldUncheckedWork() {
        final Function2<String, String, MessageDigest> unchecked = digest.unchecked();
        final MessageDigest md5 = unchecked.apply("M", "D5");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
    }

    @Test(expected = NoSuchAlgorithmException.class)
    public void shouldUncheckedThrowIllegalState() {
        final Function2<String, String, MessageDigest> unchecked = digest.unchecked();
        unchecked.apply("U", "nknown"); // Look ma, we throw an undeclared checked exception!
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        final Function2<String, String, Try<MessageDigest>> liftTry = CheckedFunction2.liftTry(digest);
        final Try<MessageDigest> md5 = liftTry.apply("M", "D5");
        assertThat(md5.isSuccess()).isTrue();
        assertThat(md5.get()).isNotNull();
        assertThat(md5.get().getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.get().getDigestLength()).isEqualTo(16);
        final Try<MessageDigest> unknown = liftTry.apply("U", "nknown");
        assertThat(unknown.isFailure()).isTrue();
        assertThat(unknown.getCause()).isNotNull();
        assertThat(unknown.getCause().getMessage()).isEqualToIgnoringCase("Unknown MessageDigest not available");
    }

    private static final CheckedFunction2<Integer, Integer, Integer> recurrent1 = (i1, i2) -> i1 <= 0 ? i1 : CheckedFunction2Test.recurrent2.apply(i1 - 1, i2) + 1;
    private static final CheckedFunction2<Integer, Integer, Integer> recurrent2 = CheckedFunction2Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction2<Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow() throws Throwable{
        final CheckedFunction2<Number, Number, String> wideFunction = (o1, o2) -> String.format("Numbers are: %s, %s", o1, o2);
        final CheckedFunction2<Integer, Integer, CharSequence> narrowFunction = CheckedFunction2.narrow(wideFunction);

        assertThat(narrowFunction.apply(1, 2)).isEqualTo("Numbers are: 1, 2");
    }
}