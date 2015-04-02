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

import java.util.Objects;
import org.junit.Test;

public class Tuple4Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple4<Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple4<Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(4);
    }

    @Test
    public void shouldFlatMap() {
        final Tuple4<Object, Object, Object, Object> tuple = createTuple();
        final Function4<Object, Object, Object, Object, Tuple4<Object, Object, Object, Object>> mapper = (o1, o2, o3, o4) -> tuple;
        final Tuple4<Object, Object, Object, Object> actual = tuple.flatMap(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMap() {
        final Tuple4<Object, Object, Object, Object> tuple = createTuple();
        final Function4<Object, Object, Object, Object, Tuple4<?, ?, ?, ?>> mapper = (o1, o2, o3, o4) -> tuple;
        final Tuple4<Object, Object, Object, Object> actual = tuple.map(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldUnapply() {
        final Tuple4<Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.unapply()).isEqualTo(tuple);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple4<Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple4<Object, Object, Object, Object> tuple2 = createTuple();
        assertThat(tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple4<Object, Object, Object, Object> tuple1 = createTuple();
        final Object other = new Object();
        assertThat(tuple1).isNotEqualTo(other);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Tuple4<Object, Object, Object, Object> createTuple() {
        return new Tuple4<>(null, null, null, null);
    }
}