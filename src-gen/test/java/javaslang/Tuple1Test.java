/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.Function;
import org.junit.Test;

public class Tuple1Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple1 tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple1 tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(1);
    }

    @Test
    public void shouldFlatMap() {
        final Tuple1 tuple = createTuple();
        final Function mapper = (t1) -> tuple;
        @SuppressWarnings("unchecked")
        final Tuple1 actual = tuple.flatMap(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMap() {
        final Tuple1 tuple = createTuple();
        final Function mapper = (t1) -> tuple;
        @SuppressWarnings("unchecked")
        final Tuple1 actual = tuple.flatMap(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldUnapply() {
        final Tuple1 tuple = createTuple();
        assertThat(tuple.unapply()).isEqualTo(tuple);
    }

    @Test
    public void shouldCompareViaEquals() {
        final Tuple1 tuple1 = createTuple();
        final Tuple1 tuple2 = createTuple();
        assertThat(tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(new Object[] { null });
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Tuple1 createTuple() {
        return new Tuple1<>(null);
    }
}