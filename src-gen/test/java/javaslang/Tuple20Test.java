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

public class Tuple20Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(20);
    }

    @Test
    public void shouldFlatMap() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Function20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object>> mapper = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> tuple;
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.flatMap(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMap() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        final Function20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Tuple20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>> mapper = (o1, o2, o3, o4, o5, o6, o7, o8, o9, o10, o11, o12, o13, o14, o15, o16, o17, o18, o19, o20) -> tuple;
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
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
      final Function1<Object, Object> f14 = Function1.identity();
      final Function1<Object, Object> f15 = Function1.identity();
      final Function1<Object, Object> f16 = Function1.identity();
      final Function1<Object, Object> f17 = Function1.identity();
      final Function1<Object, Object> f18 = Function1.identity();
      final Function1<Object, Object> f19 = Function1.identity();
      final Function1<Object, Object> f20 = Function1.identity();
      final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> actual = tuple.map(f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12, f13, f14, f15, f16, f17, f18, f19, f20);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldUnapply() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.unapply()).isEqualTo(tuple);
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple2 = createTuple();
        assertThat(tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Object other = new Object();
        assertThat(tuple1).isNotEqualTo(other);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Tuple20<Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object> createTuple() {
        return new Tuple20<>(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}