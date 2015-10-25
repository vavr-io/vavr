/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Comparator;
import java.util.Objects;
import javaslang.collection.List;
import javaslang.collection.Seq;
import org.junit.Test;

public class Tuple5Test {

    @Test
    public void shouldCreateTuple() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple).isNotNull();
    }

    @Test
    public void shouldGetArity() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        assertThat(tuple.arity()).isEqualTo(5);
    }

    @Test
    public void shouldConvertToSeq() {
        final Seq<?> actual = createIntTuple(1, 0, 0, 0, 0).toSeq();
        assertThat(actual).isEqualTo(List.of(1, 0, 0, 0, 0));
    }

    @Test
    public void shouldCompareEqual() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        assertThat(t0.compareTo(t0)).isZero();
        assertThat(intTupleComparator.compare(t0, t0)).isZero();
    }

    @Test
    public void shouldCompare1thArg() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t1 = createIntTuple(1, 0, 0, 0, 0);
        assertThat(t0.compareTo(t1)).isNegative();
        assertThat(t1.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t1)).isNegative();
        assertThat(intTupleComparator.compare(t1, t0)).isPositive();
    }

    @Test
    public void shouldCompare2thArg() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t2 = createIntTuple(0, 1, 0, 0, 0);
        assertThat(t0.compareTo(t2)).isNegative();
        assertThat(t2.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t2)).isNegative();
        assertThat(intTupleComparator.compare(t2, t0)).isPositive();
    }

    @Test
    public void shouldCompare3thArg() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t3 = createIntTuple(0, 0, 1, 0, 0);
        assertThat(t0.compareTo(t3)).isNegative();
        assertThat(t3.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t3)).isNegative();
        assertThat(intTupleComparator.compare(t3, t0)).isPositive();
    }

    @Test
    public void shouldCompare4thArg() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t4 = createIntTuple(0, 0, 0, 1, 0);
        assertThat(t0.compareTo(t4)).isNegative();
        assertThat(t4.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t4)).isNegative();
        assertThat(intTupleComparator.compare(t4, t0)).isPositive();
    }

    @Test
    public void shouldCompare5thArg() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t0 = createIntTuple(0, 0, 0, 0, 0);
        final Tuple5<Integer, Integer, Integer, Integer, Integer> t5 = createIntTuple(0, 0, 0, 0, 1);
        assertThat(t0.compareTo(t5)).isNegative();
        assertThat(t5.compareTo(t0)).isPositive();
        assertThat(intTupleComparator.compare(t0, t5)).isNegative();
        assertThat(intTupleComparator.compare(t5, t0)).isPositive();
    }

    @Test
    public void shouldMap() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Function5<Object, Object, Object, Object, Object, Tuple5<Object, Object, Object, Object, Object>> mapper = (o1, o2, o3, o4, o5) -> tuple;
        final Tuple5<Object, Object, Object, Object, Object> actual = tuple.map(mapper);
        assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldMapComponents() {
      final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
      final Function1<Object, Object> f1 = Function1.identity();
      final Function1<Object, Object> f2 = Function1.identity();
      final Function1<Object, Object> f3 = Function1.identity();
      final Function1<Object, Object> f4 = Function1.identity();
      final Function1<Object, Object> f5 = Function1.identity();
      final Tuple5<Object, Object, Object, Object, Object> actual = tuple.map(f1, f2, f3, f4, f5);
      assertThat(actual).isEqualTo(tuple);
    }

    @Test
    public void shouldTransformTuple() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Tuple0 actual = tuple.transform(t -> Tuple0.instance());
        assertThat(actual).isEqualTo(Tuple0.instance());
    }

    @Test
    public void shouldRecognizeEquality() {
        final Tuple5<Object, Object, Object, Object, Object> tuple1 = createTuple();
        final Tuple5<Object, Object, Object, Object, Object> tuple2 = createTuple();
        assertThat((Object) tuple1).isEqualTo(tuple2);
    }

    @Test
    public void shouldRecognizeNonEquality() {
        final Tuple5<Object, Object, Object, Object, Object> tuple = createTuple();
        final Object other = new Object();
        assertThat(tuple).isNotEqualTo(other);
    }

    @Test
    public void shouldComputeCorrectHashCode() {
        final int actual = createTuple().hashCode();
        final int expected = Objects.hash(null, null, null, null, null);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldImplementToString() {
        final String actual = createTuple().toString();
        final String expected = "(null, null, null, null, null)";
        assertThat(actual).isEqualTo(expected);
    }

    private Comparator<Tuple5<Integer, Integer, Integer, Integer, Integer>> intTupleComparator = Tuple5.comparator(Integer::compare, Integer::compare, Integer::compare, Integer::compare, Integer::compare);

    private Tuple5<Object, Object, Object, Object, Object> createTuple() {
        return new Tuple5<>(null, null, null, null, null);
    }

    private Tuple5<Integer, Integer, Integer, Integer, Integer> createIntTuple(Integer i1, Integer i2, Integer i3, Integer i4, Integer i5) {
        return new Tuple5<>(i1, i2, i3, i4, i5);
    }
}