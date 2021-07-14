/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2021 Vavr, http://vavr.io
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
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class Function5Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4, Object o5) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function5.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(Function5.lift((o1, o2, o3, o4, o5) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.apply(null)).isNotNull();
        assertThat(f.apply(null, null)).isNotNull();
        assertThat(f.apply(null, null, null)).isNotNull();
        assertThat(f.apply(null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.arity()).isEqualTo(5);
    }

    @Test
    public void shouldConstant() {
        final Function5<Object, Object, Object, Object, Object, Object> f = Function5.constant(6);
        assertThat(f.apply(1, 2, 3, 4, 5)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Object>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Tuple5<Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() {
        final AtomicInteger integer = new AtomicInteger();
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5 + integer.getAndIncrement();
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1, 2, 3, 4, 5);
        // should return memoized value of second apply()
        assertThat(memo.apply(1, 2, 3, 4, 5)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 , 3 , 4 , 5 , 6 )).isEqualTo(2  + 3  + 4  + 5  + 6  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null, null, null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4, i5) -> null;
        final Function5<Integer, Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        AtomicInteger integer = new AtomicInteger();
        Function5<Integer, Integer, Integer, Integer, Integer, Integer> divByZero = (i1, i2, i3, i4, i5) -> 10 / integer.get();
        Function5<Integer, Integer, Integer, Integer, Integer, Try<Integer>> divByZeroTry = Function5.liftTry(divByZero);

        Try<Integer> res = divByZeroTry.apply(0, 0, 0, 0, 0);
        assertThat(res.isFailure()).isTrue();
        assertThat(res.getCause()).isNotNull();
        assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

        integer.incrementAndGet();
        res = divByZeroTry.apply(1, 2, 3, 4, 5);
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.get()).isEqualTo(10);
    }

    private static final Function5<Integer, Integer, Integer, Integer, Integer, Integer> recurrent1 = (i1, i2, i3, i4, i5) -> i1 <= 0 ? i1 : Function5Test.recurrent2.apply(i1 - 1, i2, i3, i4, i5) + 1;
    private static final Function5<Integer, Integer, Integer, Integer, Integer, Integer> recurrent2 = Function5Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() {
        assertThat(recurrent1.apply(11, 11, 11, 11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22, 22, 22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function5<Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function5<Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow(){
        final Function5<Number, Number, Number, Number, Number, String> wideFunction = (o1, o2, o3, o4, o5) -> String.format("Numbers are: %s, %s, %s, %s, %s", o1, o2, o3, o4, o5);
        final Function5<Integer, Integer, Integer, Integer, Integer, CharSequence> narrowFunction = Function5.narrow(wideFunction);

        assertThat(narrowFunction.apply(1, 2, 3, 4, 5)).isEqualTo("Numbers are: 1, 2, 3, 4, 5");
    }
}