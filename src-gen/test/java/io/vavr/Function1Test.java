/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2020 Vavr, http://vavr.io
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.vavr.control.Try;
import java.lang.CharSequence;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class Function1Test {

    @Test
    public void shouldCreateFromMethodReference() {
        class Type {
            Object methodReference(Object o1) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function1.of(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldLiftPartialFunction() {
        assertThat(Function1.lift((o1) -> { while(true); })).isNotNull();
    }

    @Test
    public void shouldCreateIdentityFunction() {
        final Function1<String, String> identity = Function1.identity();
        final String s = "test";
        assertThat(identity.apply(s)).isEqualTo(s);
    }

    @Test
    public void shouldGetArity() {
        final Function1<Object, Object> f = (o1) -> null;
        assertThat(f.arity()).isEqualTo(1);
    }

    @Test
    public void shouldConstant() {
        final Function1<Object, Object> f = Function1.constant(6);
        assertThat(f.apply(1)).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final Function1<Object, Object> f = (o1) -> null;
        final Function1<Object, Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function1<Object, Object> f = (o1) -> null;
        final Function1<Tuple1<Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function1<Object, Object> f = (o1) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() {
        final AtomicInteger integer = new AtomicInteger();
        final Function1<Integer, Integer> f = (i1) -> i1 + integer.getAndIncrement();
        final Function1<Integer, Integer> memo = f.memoized();
        // should apply f on first apply()
        final int expected = memo.apply(1);
        // should return memoized value of second apply()
        assertThat(memo.apply(1)).isEqualTo(expected);
        // should calculate new values when called subsequently with different parameters
        assertThat(memo.apply(2 )).isEqualTo(2  + 1);
        // should return memoized value of second apply() (for new value)
        assertThat(memo.apply(2 )).isEqualTo(2  + 1);
    }

    @Test
    public void shouldNotMemoizeAlreadyMemoizedFunction() {
        final Function1<Integer, Integer> f = (i1) -> null;
        final Function1<Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() {
        final Function1<Integer, Integer> f = (i1) -> null;
        final Function1<Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final Function1<Integer, Integer> f = (i1) -> null;
        final Function1<Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    @Test
    public void shouldThrowOnPartialWithNullPredicate() {
        final Function1<Integer, String> f = String::valueOf;
        assertThatThrownBy(() -> f.partial(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("isDefinedAt is null");
    }

    @Test
    public void shouldCreatePartialFunction() {
        final Function1<Integer, String> f = String::valueOf;
        final PartialFunction<Integer, String> pf = f.partial(i -> i % 2 == 0);
        assertThat(pf.isDefinedAt(0)).isTrue();
        assertThat(pf.isDefinedAt(1)).isFalse();
        assertThat(pf.apply(0)).isEqualTo("0");
        assertThat(pf.apply(1)).isEqualTo("1"); // it is valid to return a value, even if isDefinedAt returns false
    }

    @Test
    public void shouldLiftTryPartialFunction() {
        AtomicInteger integer = new AtomicInteger();
        Function1<Integer, Integer> divByZero = (i1) -> 10 / integer.get();
        Function1<Integer, Try<Integer>> divByZeroTry = Function1.liftTry(divByZero);

        Try<Integer> res = divByZeroTry.apply(0);
        assertThat(res.isFailure()).isTrue();
        assertThat(res.getCause()).isNotNull();
        assertThat(res.getCause().getMessage()).isEqualToIgnoringCase("/ by zero");

        integer.incrementAndGet();
        res = divByZeroTry.apply(1);
        assertThat(res.isSuccess()).isTrue();
        assertThat(res.get()).isEqualTo(10);
    }

    private static final Function1<Integer, Integer> recurrent1 = (i1) -> i1 <= 0 ? i1 : Function1Test.recurrent2.apply(i1 - 1) + 1;
    private static final Function1<Integer, Integer> recurrent2 = Function1Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() {
        assertThat(recurrent1.apply(11)).isEqualTo(11);
        assertThat(recurrent1.apply(22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function1<Object, Object> f = (o1) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function1<Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldComposeWithCompose() {
        final Function1<Object, Object> f = (o1) -> null;
        final Function1<Object, Object> before = o -> null;
        final Function1<Object, Object> composed = f.compose(before);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldNarrow(){
        final Function1<Number, String> wideFunction = (o1) -> String.format("Numbers are: %s", o1);
        final Function1<Integer, CharSequence> narrowFunction = Function1.narrow(wideFunction);

        assertThat(narrowFunction.apply(1)).isEqualTo("Numbers are: 1");
    }
}