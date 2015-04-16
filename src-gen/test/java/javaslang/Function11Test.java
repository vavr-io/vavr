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

public class Function11Test {

    @Test
    public void shouldPartiallyApplyWith1Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null) instanceof Function10).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith2Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null) instanceof Function9).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith3Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null) instanceof Function8).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith4Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null) instanceof Function7).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith5Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null) instanceof Function6).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith6Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null, null) instanceof Function5).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith7Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null, null, null) instanceof Function4).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith8Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null, null, null, null) instanceof Function3).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith9Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null, null, null, null, null) instanceof Function2).isTrue();
    }

    @Test
    public void shouldPartiallyApplyWith10Arguments() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.apply(null, null, null, null, null, null, null, null, null, null) instanceof Function1).isTrue();
    }

    @Test
    public void shouldGetArity() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.arity()).isEqualTo(11);
    }

    @Test
    public void shouldCurry() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        final Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Function1<Object, Object>>>>>>>>>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        final Function1<Tuple11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>, Object> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> f = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11) -> null;
        final Function1<Object, Object> after = o -> null;
        final Function11<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

}