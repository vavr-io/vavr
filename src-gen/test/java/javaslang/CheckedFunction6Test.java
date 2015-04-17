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

public class CheckedFunction6Test {

    @Test
    public void shouldLift() {
        class Type {
            Object methodReference(Object o1, Object o2, Object o3, Object o4, Object o5, Object o6) {
                return null;
            }
        }
        final Type type = new Type();
        assertThat(CheckedFunction6.lift(type::methodReference)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith1Arguments() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith2Arguments() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null, null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith3Arguments() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null, null, null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith4Arguments() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldPartiallyApplyWith5Arguments() throws Throwable {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.apply(null, null, null, null, null)).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.arity()).isEqualTo(6);
    }

    @Test
    public void shouldCurry() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, CheckedFunction1<Object, Object>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Tuple6<Object, Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6) -> null;
        final CheckedFunction1<Object, Object> after = o -> null;
        final CheckedFunction6<Object, Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}