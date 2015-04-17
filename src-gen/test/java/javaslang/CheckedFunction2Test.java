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

import org.junit.Test;

public class CheckedFunction2Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference(Object o1, Object o2) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction2.lift(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith1Arguments() throws Throwable {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.arity()).isEqualTo(2);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, Object>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final CheckedFunction1<Tuple2<Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction2<Object, Object, Object> f = (o1, o2) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction2<Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}