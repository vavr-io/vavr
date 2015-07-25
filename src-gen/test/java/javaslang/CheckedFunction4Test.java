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

public class CheckedFunction4Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction4.lift(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith1Arguments() throws Throwable {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith2Arguments() throws Throwable {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.apply(null, null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith3Arguments() throws Throwable {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.apply(null, null, null)).isNotNull();
    }

    @Test
      public void shouldRecognizeApplicabilityOfNull() {
          final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
          assertThat(f.isApplicableTo(null, null, null, null)).isTrue();
      }

      @Test
      public void shouldRecognizeApplicabilityOfNonNull() {
          final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
          assertThat(f.isApplicableTo(1, 2, 3, 4)).isTrue();
      }

      @Test
      public void shouldRecognizeApplicabilityToTypes() {
          final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> null;
          assertThat(f.isApplicableToTypes(Integer.class, Integer.class, Integer.class, Integer.class)).isTrue();
      }

    @Test
    public void shouldGetArity() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        assertThat(f.arity()).isEqualTo(4);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>>> curried = f.curried();
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

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> f = (i1, i2, i3, i4) -> i1 + i2 + i3 + i4 + integer.getAndIncrement();
        final CheckedFunction4<Integer, Integer, Integer, Integer, Integer> memo = f.memoized();
        final int expected = memo.apply(1, 2, 3, 4);
        assertThat(memo.apply(1, 2, 3, 4)).isEqualTo(expected);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction4<Object, Object, Object, Object, Object> f = (o1, o2, o3, o4) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction4<Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}