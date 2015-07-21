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

public class CheckedFunction0Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference() {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction0.lift(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction0<Object> f = () -> null;
        assertThat(f.arity()).isEqualTo(0);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction0<Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction1<Tuple0, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction0<Object> f = () -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldMemoize() throws Throwable {
        final AtomicInteger integer = new AtomicInteger();
        final CheckedFunction0<Integer> f = () -> integer.getAndIncrement();
        final CheckedFunction0<Integer> memo = f.memoized();
        final int expected = memo.apply();
        assertThat(memo.apply()).isEqualTo(expected);
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction0<Object> f = () -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction0<Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}