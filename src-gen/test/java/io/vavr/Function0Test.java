/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
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
    public void shouldLiftPartialFunction() {
        assertThat(Function0.lift(() -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldGetValue() {
        final String s = "test";
        final Function0<String> supplier = () -> s;
        assertThat(supplier.get()).isEqualTo(s);
    }

    @Test
    public void shouldGetArity() {
        final Function0<Object> f = () -> null;
        assertThat(f.arity()).isEqualTo(0);
    }

    @Test
    public void shouldConstant() {
        final Function0<Object> f = Function0.constant(6);
        assertThat(f.apply()).isEqualTo(6);
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
    public void shouldLiftTryPartialFunction() {
        AtomicInteger integer = new AtomicInteger();
        Function0<Integer> divByZero = () -> 10 / integer.get();
        Function0<Try<Integer>> divByZeroTry = Function0.liftTry(divByZero);

        Try<Integer> res = divByZeroTry.apply();
        assertThat(res.isFailure()).isTrue();
        assertThat(res.getCause()).isNotNull();
        assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

        integer.incrementAndGet();
        res = divByZeroTry.apply();
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.get()).isEqualTo(10);
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