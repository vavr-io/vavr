/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

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

}
