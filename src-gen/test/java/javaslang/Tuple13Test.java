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

public class Tuple13Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(13);
    }

    @Test
    public void shouldFlatMap() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Function13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>> mapper = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> tuple;
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.flatMap(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMap() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Function13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Tuple13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> mapper = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13) -> tuple;
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Function1<Object, Object> f3 = Function1.identity();
      final Function1<Object, Object> f4 = Function1.identity();
      final Function1<Object, Object> f5 = Function1.identity();
      final Function1<Object, Object> f6 = Function1.identity();
      final Function1<Object, Object> f7 = Function1.identity();
      final Function1<Object, Object> f8 = Function1.identity();
      final Function1<Object, Object> f9 = Function1.identity();
      final Function1<Object, Object> f10 = Function1.identity();
      final Function1<Object, Object> f11 = Function1.identity();
      final Function1<Object, Object> f12 = Function1.identity();
      final Function1<Object, Object> f13 = Function1.identity();
      final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldUnapply() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.unapply()).isEqualTo(tuple);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple2 = createTuple();
        assertThat(tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Object other = new Object();
        assertThat(tuple1).isNotEqualTo(other);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null, null, null, null, null, null, null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Tuple13<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> createTuple() {
        return new Tuple13<>(null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}