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
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class Function0Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference() {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function0.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldMemoize() {
        final AtomicInteger integer = new AtomicInteger();
        final Function0<Integer> f = () -> integer.getAndIncrement();
        final Function0<Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply();
        // should return memoized value of second apply()
        assertThat(memo.apply()).isEqualTo(expected);

    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() {
        final Function0<Integer> f = () -> null;
        final Function0<Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final Function0<Integer> f = () -> null;
        final Function0<Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldGetValue() {
        final String s = "test";
        final Function0<String> supplier = () -> s;
        assertThat(supplier.get()).isEqualTo(s);
    }

    @Test
    public void shouldCurry() {
        final Function0<Object> f = () -> null;
        final Function0<Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function0<Object> f = () -> null;
        final Function1<Tuple0, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function0<Object> f = () -> null;
        assertThat(f.reversed()).isNotNull();
    }

    private static final Function0<Integer> recurrent1 = () -> 11;

    @Test
    public void shouldCalculatedRecursively() {
        assertThat(recurrent1.apply()).isEqualTo(11);

    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function0<Object> f = () -> null;
        final Function1<Object, Object> after = o -> null;
        final Function0<Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow(){
        final Function0<String> wideFunction = () -> "Zero args";
        final Function0<CharSequence> narrowFunction = Function0.narrow(wideFunction);

        assertThat(narrowFunction.apply()).isEqualTo("Zero args");
    }

}