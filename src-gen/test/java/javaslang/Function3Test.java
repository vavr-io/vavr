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

public class Function3Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(Function3.lift(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith1Arguments() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith2Arguments() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.apply(null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.arity()).isEqualTo(3);
    }

    @Test
    public void shouldCurry() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final Function1<Object, Function1<Object, Function1<Object, Object>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final Function1<Tuple3<Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function3<Object, Object, Object, Object> f = (o1, o2, o3) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function3<Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}