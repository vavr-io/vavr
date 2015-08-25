/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;
import org.junit.Test;

public class CheckedFunction1Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference(Object o1) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction1.lift(type::methodReference)).isNotNull();
    }

    @Test
      public void shouldRecognizeApplicabilityOfNull() {
          final CheckedFunction1<Object, Object> f = (o1) -> null;
          assertThat(f.isApplicableTo(null)).isTrue();
      }

      @Test
      public void shouldRecognizeApplicabilityOfNonNull() {
          final CheckedFunction1<Integer, Integer> f = (i1) -> null;
          assertThat(f.isApplicableTo(1)).isTrue();
      }

      @Test
      public void shouldRecognizeApplicabilityToType() {
          final CheckedFunction1<Integer, Integer> f = (i1) -> null;
          assertThat(f.isApplicableToType(Integer.class)).isTrue();
      }

    @Test
    public void shouldGetArity() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        assertThat(f.arity()).isEqualTo(1);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Tuple1<Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction1<Integer, Integer> f = (i1) -> i1 + integer.getAndIncrement();
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
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
    public void shouldNotMemoizeAlreadyMemoizedFunction() throws Throwable {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(memo.memoized() == memo).isTrue();
    }

    @Test
    public void shouldMemoizeValueGivenNullArguments() throws Throwable {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(memo.apply(null)).isNull();
    }

    @Test
    public void shouldRecognizeMemoizedFunctions() {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1<Integer, Integer> memo = f.memoized();
        assertThat(f.isMemoized()).isFalse();
        assertThat(memo.isMemoized()).isTrue();
    }

    private static CheckedFunction1<Integer, Integer> recurrent1 = (i1) -> i1 <= 0 ? i1 : CheckedFunction1Test.recurrent2.apply(i1 - 1) + 1;
    private static CheckedFunction1<Integer, Integer> recurrent2 = CheckedFunction1Test.recurrent1.memoized();

    @Test
    public void shouldCalculatedRecursively() throws Throwable {
        assertThat(recurrent1.apply(11)).isEqualTo(11);
        assertThat(recurrent1.apply(22)).isEqualTo(22);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction1<Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldComposeWithCompose() {
        final CheckedFunction1<Object, Object> f = (o1) -> null;
        final CheckedFunction1<Object, Object> before = o -> null;
        final CheckedFunction1<Object, Object> composed = f.compose(before);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldGetType() {
        final CheckedFunction1<Integer, Integer> f = (i1) -> null;
        final CheckedFunction1.Type<Integer, Integer> type = f.getType();
        assertThat(type.toString()).isEqualTo("(java.lang.Integer) -> java.lang.Integer");
    }
}