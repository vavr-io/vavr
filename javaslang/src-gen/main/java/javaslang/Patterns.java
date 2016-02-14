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

    public static final Pattern0 None = Pattern0.create(javaslang.control.Option.None.class);

    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(T p1, javaslang.collection.List<T> p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> Pattern0.equals(t2, p2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(T p1, Pattern0 p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> Pattern0.equals(t1, p1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, T> Cons(InversePattern<? extends T> p1, javaslang.collection.List<T> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern1<javaslang.collection.List.Cons<T>, T> Cons(InversePattern<? extends T> p1, Pattern0 p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> InversePattern.narrow(p1).apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(Pattern0 p1, javaslang.collection.List<T> p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> Pattern0.equals(t2, p2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T> Pattern0 Cons(Pattern0 p1, Pattern0 p2) {
        return Pattern0.<javaslang.collection.List.Cons<T>>create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(_1 -> p2.apply(t2))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(Pattern1<? extends T, T1> p1, javaslang.collection.List<T> p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1> Pattern1<javaslang.collection.List.Cons<T>, T1> Cons(Pattern1<? extends T, T1> p1, Pattern0 p2) {
        return Pattern1.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern2<? extends T, T1, T2> p1, javaslang.collection.List<T> p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2> Pattern2<javaslang.collection.List.Cons<T>, T1, T2> Cons(Pattern2<? extends T, T1, T2> p1, Pattern0 p2) {
        return Pattern2.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern3<? extends T, T1, T2, T3> p1, javaslang.collection.List<T> p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3> Pattern3<javaslang.collection.List.Cons<T>, T1, T2, T3> Cons(Pattern3<? extends T, T1, T2, T3> p1, Pattern0 p2) {
        return Pattern3.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, javaslang.collection.List<T> p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4> Pattern4<javaslang.collection.List.Cons<T>, T1, T2, T3, T4> Cons(Pattern4<? extends T, T1, T2, T3, T4> p1, Pattern0 p2) {
        return Pattern4.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, javaslang.collection.List<T> p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5> Pattern5<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5> Cons(Pattern5<? extends T, T1, T2, T3, T4, T5> p1, Pattern0 p2) {
        return Pattern5.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, javaslang.collection.List<T> p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6> Pattern6<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6> Cons(Pattern6<? extends T, T1, T2, T3, T4, T5, T6> p1, Pattern0 p2) {
        return Pattern6.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, javaslang.collection.List<T> p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7> Pattern7<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7> Cons(Pattern7<? extends T, T1, T2, T3, T4, T5, T6, T7> p1, Pattern0 p2) {
        return Pattern7.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern8<? extends T, T1, T2, T3, T4, T5, T6, T7, T8> p1, javaslang.collection.List<T> p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> Pattern0.equals(t2, p2).map(_1 -> v1))));
    }
    public static <__ extends javaslang.collection.List.Cons<T>, T, T1, T2, T3, T4, T5, T6, T7, T8> Pattern8<javaslang.collection.List.Cons<T>, T1, T2, T3, T4, T5, T6, T7, T8> Cons(Pattern8<? extends T, T1, T2, T3, T4, T5, T6, T7, T8> p1, Pattern0 p2) {
        return Pattern8.create(javaslang.collection.List.Cons.class, t -> $.Cons(t).transform((t1, t2) -> p1.apply(t1).flatMap(v1 -> p2.apply(t2).map(_1 -> v1))));
    }

    public static final Pattern0 Nil = Pattern0.create(javaslang.collection.List.Nil.class);

}
