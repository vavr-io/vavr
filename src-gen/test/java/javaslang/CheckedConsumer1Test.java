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

public class CheckedConsumer1Test {

    @Test
    public void shouldGetArity() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        assertThat(f.arity()).isEqualTo(1);
    }

    @Test
    public void shouldCurry() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        final CheckedConsumer1<Object> curried = f.curried();
        assertThat(curried).isNotNull();
    }

    @Test
    public void shouldTuple() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        final CheckedConsumer1<Tuple1<Object>> tupled = f.tupled();
        assertThat(tupled).isNotNull();
    }

    @Test
    public void shouldReverse() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        assertThat(f.reversed()).isNotNull();
    }

    @Test
    public void shouldComposeWithAndThen() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        final CheckedConsumer1<Object> after = (o1) -> {};
        final CheckedConsumer1<Object> composed = f.andThen(after);
        assertThat(composed).isNotNull();
    }

    @Test
    public void shouldComposeWithCompose() {
        final CheckedConsumer1<Object> f = (o1) -> {};
        final CheckedConsumer1<Object> before = (o1) -> {};
        final CheckedConsumer1<Object> composed = f.compose(before);
        assertThat(composed).isNotNull();
    }
}