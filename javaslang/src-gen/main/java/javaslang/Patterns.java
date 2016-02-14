package javaslang;

import static javaslang.Match.*;

// GENERATED <<>> JAVASLANG
// derived from javaslang.$

public final class Patterns {

    private Patterns() {
    }

    public static <__ extends javaslang.control.Option.Some<T>, T> Pattern0 Some(T p1) {
        return Pattern0.<javaslang.control.Option.Some<T>>create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> Pattern0.equals(t1, p1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T> Pattern1<javaslang.control.Option.Some<T>, T> Some(InversePattern<? extends T> p1) {
        return Pattern1.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> InversePattern.narrow(p1).apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T> Pattern0 Some(Pattern0 p1) {
        return Pattern0.<javaslang.control.Option.Some<T>>create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1> Pattern1<javaslang.control.Option.Some<T>, T1> Some(Pattern1<? extends T, T1> p1) {
        return Pattern1.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2> Pattern2<javaslang.control.Option.Some<T>, T1, T2> Some(Pattern2<? extends T, T1, T2> p1) {
        return Pattern2.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3> Pattern3<javaslang.control.Option.Some<T>, T1, T2, T3> Some(Pattern3<? extends T, T1, T2, T3> p1) {
        return Pattern3.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3, T4> Pattern4<javaslang.control.Option.Some<T>, T1, T2, T3, T4> Some(Pattern4<? extends T, T1, T2, T3, T4> p1) {
        return Pattern4.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.control.Option.Some<T>, T1, T2, T3, T4, T5> Some(Pattern5<? extends T, T1, T2, T3, T4, T5> p1) {
        return Pattern5.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.control.Option.Some<T>, T1, T2, T3, T4, T5, T6> Some(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1) {
        return Pattern6.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.control.Option.Some<T>, T1, T2, T3, T4, T5, T6, T7> Some(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1) {
        return Pattern7.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }
    public static <__ extends javaslang.control.Option.Some<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.control.Option.Some<T>, T1, T2, T3, T4, T5, T6, T7, T8> Some(Pattern8<? extends T, T1, T2, T3, T4, T5, T6, T7, T8> p1) {
        return Pattern8.create(javaslang.control.Option.Some.class, t -> $.Some(t).transform(t1 -> p1.apply(t1)));
    }

    public static Pattern0 None = Pattern0.create(javaslang.control.Option.None.class);

    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(T p1, javaslang.collection.List<T> p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> Pattern0.equals(t2, p2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, javaslang.collection.List<T>> Cons(T p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> InversePattern.narrow(p2).apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(T p1, Pattern0 p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(T p1, Pattern1<? extends javaslang.collection.List<T>, T1> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(T p1, Pattern2<? extends javaslang.collection.List<T>, T1, T2> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(T p1, Pattern3<? extends javaslang.collection.List<T>, T1, T2, T3> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(T p1, Pattern4<? extends javaslang.collection.List<T>, T1, T2, T3, T4> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(T p1, Pattern5<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(T p1, Pattern6<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(T p1, Pattern7<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(T p1, Pattern8<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, T> Cons(InversePattern<? extends T> p1, javaslang.collection.List<T> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern2<javaslang.collection.List.Cons<T>, T, javaslang.collection.List<T>> Cons(InversePattern<? extends T> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, T> Cons(InversePattern<? extends T> p1, Pattern0 p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern2<javaslang.collection.List.Cons<T>, T, T1> Cons(InversePattern<? extends T> p1, Pattern1<? extends javaslang.collection.List<T>, T1> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern3<javaslang.collection.List.Cons<T>, T, T1, T2> Cons(InversePattern<? extends T> p1, Pattern2<? extends javaslang.collection.List<T>, T1, T2> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern4<javaslang.collection.List.Cons<T>, T, T1, T2, T3> Cons(InversePattern<? extends T> p1, Pattern3<? extends javaslang.collection.List<T>, T1, T2, T3> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern5<javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Cons(InversePattern<? extends T> p1, Pattern4<? extends javaslang.collection.List<T>, T1, T2, T3, T4> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern6<javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Cons(InversePattern<? extends T> p1, Pattern5<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern7<javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Cons(InversePattern<? extends T> p1, Pattern6<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5, v2._6)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern8<javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Cons(InversePattern<? extends T> p1, Pattern7<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6, T7> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5, v2._6, v2._7)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(Pattern0 p1, javaslang.collection.List<T> p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> Pattern0.equals(t2, p2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, javaslang.collection.List<T>> Cons(Pattern0 p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> InversePattern.narrow(p2).apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(Pattern0 p1, Pattern0 p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(Pattern0 p1, Pattern1<? extends javaslang.collection.List<T>, T1> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern0 p1, Pattern2<? extends javaslang.collection.List<T>, T1, T2> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern0 p1, Pattern3<? extends javaslang.collection.List<T>, T1, T2, T3> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern0 p1, Pattern4<? extends javaslang.collection.List<T>, T1, T2, T3, T4> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern0 p1, Pattern5<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern0 p1, Pattern6<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern0 p1, Pattern7<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern0 p1, Pattern8<? extends javaslang.collection.List<T>, T1, T2, T3, T4, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(Pattern1<? extends T, T1> p1, javaslang.collection.List<T> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern2<javaslang.collection.List.Cons<T>, T1, javaslang.collection.List<T>> Cons(Pattern1<? extends T, T1> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(Pattern1<? extends T, T1> p1, Pattern0 p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern1<? extends T, T1> p1, Pattern1<? extends javaslang.collection.List<T>, T2> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern1<? extends T, T1> p1, Pattern2<? extends javaslang.collection.List<T>, T2, T3> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern1<? extends T, T1> p1, Pattern3<? extends javaslang.collection.List<T>, T2, T3, T4> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern1<? extends T, T1> p1, Pattern4<? extends javaslang.collection.List<T>, T2, T3, T4, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern1<? extends T, T1> p1, Pattern5<? extends javaslang.collection.List<T>, T2, T3, T4, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern1<? extends T, T1> p1, Pattern6<? extends javaslang.collection.List<T>, T2, T3, T4, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5, v2._6)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern1<? extends T, T1> p1, Pattern7<? extends javaslang.collection.List<T>, T2, T3, T4, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1, v2._1, v2._2, v2._3, v2._4, v2._5, v2._6, v2._7)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern2<? extends T, T1, T2> p1, javaslang.collection.List<T> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, javaslang.collection.List<T>> Cons(Pattern2<? extends T, T1, T2> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern2<? extends T, T1, T2> p1, Pattern0 p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern2<? extends T, T1, T2> p1, Pattern1<? extends javaslang.collection.List<T>, T3> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern2<? extends T, T1, T2> p1, Pattern2<? extends javaslang.collection.List<T>, T3, T4> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern2<? extends T, T1, T2> p1, Pattern3<? extends javaslang.collection.List<T>, T3, T4, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern2<? extends T, T1, T2> p1, Pattern4<? extends javaslang.collection.List<T>, T3, T4, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2._1, v2._2, v2._3, v2._4)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern2<? extends T, T1, T2> p1, Pattern5<? extends javaslang.collection.List<T>, T3, T4, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2._1, v2._2, v2._3, v2._4, v2._5)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern2<? extends T, T1, T2> p1, Pattern6<? extends javaslang.collection.List<T>, T3, T4, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v2._1, v2._2, v2._3, v2._4, v2._5, v2._6)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern3<? extends T, T1, T2, T3> p1, javaslang.collection.List<T> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, javaslang.collection.List<T>> Cons(Pattern3<? extends T, T1, T2, T3> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern0 p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern1<? extends javaslang.collection.List<T>, T4> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern2<? extends javaslang.collection.List<T>, T4, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern3<? extends javaslang.collection.List<T>, T4, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern4<? extends javaslang.collection.List<T>, T4, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2._1, v2._2, v2._3, v2._4)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern5<? extends javaslang.collection.List<T>, T4, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v2._1, v2._2, v2._3, v2._4, v2._5)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, javaslang.collection.List<T> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, javaslang.collection.List<T>> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern0 p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern1<? extends javaslang.collection.List<T>, T5> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern2<? extends javaslang.collection.List<T>, T5, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern3<? extends javaslang.collection.List<T>, T5, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern4<? extends javaslang.collection.List<T>, T5, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v2._1, v2._2, v2._3, v2._4)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, javaslang.collection.List<T> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, javaslang.collection.List<T>> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, Pattern0 p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, Pattern1<? extends javaslang.collection.List<T>, T6> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, Pattern2<? extends javaslang.collection.List<T>, T6, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, Pattern3<? extends javaslang.collection.List<T>, T6, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v2._1, v2._2, v2._3)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, javaslang.collection.List<T> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, javaslang.collection.List<T>> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v1._6, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, Pattern0 p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, Pattern1<? extends javaslang.collection.List<T>, T7> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v1._6, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, Pattern2<? extends javaslang.collection.List<T>, T7, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v1._6, v2._1, v2._2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, javaslang.collection.List<T> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, javaslang.collection.List<T>> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, InversePattern<? extends javaslang.collection.List<T>> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> InversePattern.narrow(p2).apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v1._6, v1._7, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, Pattern0 p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, Pattern1<? extends javaslang.collection.List<T>, T8> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(v2 -> javaslang.Tuple.of(v1._1, v1._2, v1._3, v1._4, v1._5, v1._6, v1._7, v2)))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern8<? extends T, T1, T2, T3, T4, T5, T6, T7, T8> p1, javaslang.collection.List<T> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern8<? extends T, T1, T2, T3, T4, T5, T6, T7, T8> p1, Pattern0 p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }

    public static Pattern0 Nil = Pattern0.create(javaslang.collection.List.Nil.class);

}
