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

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.CharSequence;
import java.security.MessageDigest;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CheckedFunction4Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction4.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> i1 + i2 + i3 + i4 + integer.getAndIncrement();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 )).isEqualTo(2  + 3  + 4  + 5  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 )).isEqualTo(2  + 3  + 4  + 5  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldPartiallyApply() throws Throwable {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final Function1<Object, Function1<Object, Function1<Object, CheckedFunction1<Object, Object>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Tuple4<Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    private static final CheckedFunction4<String, String, String, String, MessageDigest> digest = (s1, s2, s3, s4) -> MessageDigest.getInstance(s1 + s2 + s3 + s4);

    @Test
    public void shouldRecover() {
        final Function4<String, String, String, String, MessageDigest> recover = digest.recover(throwable -> (s1, s2, s3, s4) -> null);
        final MessageDigest md5 = recover.apply("M", "D", "5", "");
        assertThat(md5).isNotNull();
        assertThat(md5.getAlgorithm()).isEqualToIgnoringCase("MD5");
        assertThat(md5.getDigestLength()).isEqualTo(16);
        assertThat(recover.apply("U", "n", "k", "nown")).isNull();
    }

    private static final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4) -> i1 <= 0 ? i1 : CheckedFunction4Test.recurrent2.apply(i1 - 1, i2, i3, i4) + 1;
    private static final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> recurrent2 = CheckedFunction4Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction4<Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow() throws Throwable{
        final CheckedFunction4<Number, Number, Number, Number, String> wideFunction = (o1, o2, o3, o4) -> String.format("Numbers are: %s, %s, %s, %s", o1, o2, o3, o4);
        final CheckedFunction4<Integer, Integer, Integer, Integer, CharSequence> narrowFunction = CheckedFunction4.narrow(wideFunction);

        assertThat(narrowFunction.apply(1, 2, 3, 4)).isEqualTo("Numbers are: 1, 2, 3, 4");
    }
}