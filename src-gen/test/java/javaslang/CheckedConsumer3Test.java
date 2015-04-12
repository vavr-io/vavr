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

public class CheckedConsumer3Test {

    @Test
    public void shouldGetArity() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        assertThat(f.arity()).isEqualTo(3);
    }

    @Test
    public void shouldCurry() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        final CheckedFunction1<Object, CheckedFunction1<Object, CheckedConsumer1<Object>>> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        final CheckedConsumer1<Tuple3<Object, Object, Object>> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        final CheckedConsumer3<Object, Object, Object> after = (o1, o2, o3) -> {};
        final CheckedConsumer3<Object, Object, Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldComposeWithCompose() {
        final CheckedConsumer3<Object, Object, Object> f = (o1, o2, o3) -> {};
        final CheckedConsumer3<Object, Object, Object> before = (o1, o2, o3) -> {};
        final CheckedConsumer3<Object, Object, Object> composed = f.compose(before);
        assertThat(composed).isNotNull();
    }
}