/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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

public class Function2Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1, Object o2) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function2.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(Function2.lift((o1, o2) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldPartiallyApply() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.arity()).isEqualTo(2);
    }

    @Test
    public void shouldConstant() {
        final Function2<Object, Object, Object> f = Function2.constant(6);
        assertThat(f.apply(1, 2)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        final Function1<Object, Function1<Object, Object>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        final Function1<Tuple2<Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() {
        final AtomicInteger integer = new AtomicInteger();
        final Function2<Integer, Integer, Integer> f = (i1, i2) -> i1 + i2 + integer.getAndIncrement();
        final Function2<Integer, Integer, Integer> memo = f.memoized();
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
    public void shouldNotMemoizeAlreadyMemoizedFunction() {
        final Function2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final Function2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() {
        final Function2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final Function2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null, null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final Function2<Integer, Integer, Integer> f = (i1, i2) -> null;
        final Function2<Integer, Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        AtomicInteger integer = new AtomicInteger();
        Function2<Integer, Integer, Integer> divByZero = (i1, i2) -> 10 / integer.get();
        Function2<Integer, Integer, Try<Integer>> divByZeroTry = Function2.liftTry(divByZero);

        Try<Integer> res = divByZeroTry.apply(0, 0);
        assertThat(res.isFailure()).isTrue();
        assertThat(res.getCause()).isNotNull();
        assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

        integer.incrementAndGet();
        res = divByZeroTry.apply(1, 2);
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.get()).isEqualTo(10);
    }

    private static final Function2<Integer, Integer, Integer> recurrent1 = (i1, i2) -> i1 <= 0 ? i1 : Function2Test.recurrent2.apply(i1 - 1, i2) + 1;
    private static final Function2<Integer, Integer, Integer> recurrent2 = Function2Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() {
        assertThat(recurrent1.apply(11, 11)).isEqualTo(11);
        assertThat(recurrent1.apply(22, 22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function2<Object, Object, Object> f = (o1, o2) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function2<Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow(){
        final Function2<Number, Number, String> wideFunction = (o1, o2) -> String.format("Numbers are: %s, %s", o1, o2);
        final Function2<Integer, Integer, CharSequence> narrowFunction = Function2.narrow(wideFunction);

        assertThat(narrowFunction.apply(1, 2)).isEqualTo("Numbers are: 1, 2");
    }
}