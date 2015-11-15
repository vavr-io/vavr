/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

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
    public void shouldRecognizeApplicabilityOfNonNull() {
        final Function1<Integer, Integer> f = (i1) -> null;
        assertThat(f.isApplicableTo(1)).isTrue();
    }

    @Test
    public void shouldRecognizeApplicabilityToTypes() {
        final Function1<Integer, Integer> f = (i1) -> null;
        assertThat(f.isApplicableToTypes(Integer.class)).isTrue();
    }

    @Test
    public void shouldRecognizeNonApplicabilityToType1() {
        final Function1<Number, Number> f = (i1) -> null;
        assertThat(f.isApplicableToTypes(String.class)).isFalse();
    }

    @Test
    public void shouldGetArity() {
        final Function1<Object, Object> f = (o1) -> null;
        assertThat(f.arity()).isEqualTo(1);
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
    public void shouldGetType() {
        final Function1<Integer, Integer> f = (i1) -> null;
        final Function1.Type<Integer, Integer> type = f.getType();
        assertThat(type.parameterType1()).isEqualTo(Integer.class);
        assertThat(type.toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }

    @Test
    public void shouldGetReturnType() {
        final Function1<Integer, Integer> f = (i1) -> null;
        assertThat(f.getType().returnType()).isEqualTo(Integer.class);
    }

    @Test
    public void testTypesEquals() {
        final Function1<Integer, Integer> f1 = (i1) -> null;
        final Function1<Integer, Integer> f2 = (i1) -> null;
        final Function1<Integer, String> f3 = (i1) -> null;
        final Function1<String, Integer> f4 = (i1) -> null;
        final Function1.Type<Integer, Integer> t1 = f1.getType();
        assertThat(t1).isEqualTo(t1);
        assertThat(t1).isNotEqualTo(11);
        assertThat(f1.getType()).isEqualTo(f2.getType());
        assertThat(f1.getType()).isNotEqualTo(f3.getType());
        assertThat(f1.getType()).isNotEqualTo(f4.getType());
    }
}