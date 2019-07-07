// @formatter:off
// CHECKSTYLE:OFF
package io.vavr;

import io.vavr.API.Match.Pattern;
import io.vavr.API.Match.Pattern0;
import io.vavr.API.Match.Pattern1;
import io.vavr.API.Match.Pattern2;
import io.vavr.API.Match.Pattern3;
import io.vavr.API.Match.Pattern4;
import io.vavr.API.Match.Pattern5;
import io.vavr.API.Match.Pattern6;
import io.vavr.API.Match.Pattern7;
import io.vavr.API.Match.Pattern8;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;

/**
 * @deprecated Will be removed in the next major version, along with VAVR's pattern matching, in favor of Java's native pattern matching.
 */
@Deprecated
public final class Patterns {

    private Patterns() {
    }

    public static final Pattern0<Tuple0> $Tuple0 = Pattern0.of(Tuple0.class);

    public static <T1, _1 extends T1> Pattern1<Tuple1<T1>, _1> $Tuple1(Pattern<_1, ?> p1) {
        return Pattern1.of(Tuple1.class, p1, io.vavr.$::Tuple1);
    }

    public static <T1, T2, _1 extends T1, _2 extends T2> Pattern2<Tuple2<T1, T2>, _1, _2> $Tuple2(Pattern<_1, ?> p1, Pattern<_2, ?> p2) {
        return Pattern2.of(Tuple2.class, p1, p2, io.vavr.$::Tuple2);
    }

    public static <T1, T2, T3, _1 extends T1, _2 extends T2, _3 extends T3> Pattern3<Tuple3<T1, T2, T3>, _1, _2, _3> $Tuple3(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3) {
        return Pattern3.of(Tuple3.class, p1, p2, p3, io.vavr.$::Tuple3);
    }

    public static <T1, T2, T3, T4, _1 extends T1, _2 extends T2, _3 extends T3, _4 extends T4> Pattern4<Tuple4<T1, T2, T3, T4>, _1, _2, _3, _4> $Tuple4(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3, Pattern<_4, ?> p4) {
        return Pattern4.of(Tuple4.class, p1, p2, p3, p4, io.vavr.$::Tuple4);
    }

    public static <T1, T2, T3, T4, T5, _1 extends T1, _2 extends T2, _3 extends T3, _4 extends T4, _5 extends T5> Pattern5<Tuple5<T1, T2, T3, T4, T5>, _1, _2, _3, _4, _5> $Tuple5(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3, Pattern<_4, ?> p4, Pattern<_5, ?> p5) {
        return Pattern5.of(Tuple5.class, p1, p2, p3, p4, p5, io.vavr.$::Tuple5);
    }

    public static <T1, T2, T3, T4, T5, T6, _1 extends T1, _2 extends T2, _3 extends T3, _4 extends T4, _5 extends T5, _6 extends T6> Pattern6<Tuple6<T1, T2, T3, T4, T5, T6>, _1, _2, _3, _4, _5, _6> $Tuple6(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3, Pattern<_4, ?> p4, Pattern<_5, ?> p5, Pattern<_6, ?> p6) {
        return Pattern6.of(Tuple6.class, p1, p2, p3, p4, p5, p6, io.vavr.$::Tuple6);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, _1 extends T1, _2 extends T2, _3 extends T3, _4 extends T4, _5 extends T5, _6 extends T6, _7 extends T7> Pattern7<Tuple7<T1, T2, T3, T4, T5, T6, T7>, _1, _2, _3, _4, _5, _6, _7> $Tuple7(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3, Pattern<_4, ?> p4, Pattern<_5, ?> p5, Pattern<_6, ?> p6, Pattern<_7, ?> p7) {
        return Pattern7.of(Tuple7.class, p1, p2, p3, p4, p5, p6, p7, io.vavr.$::Tuple7);
    }

    public static <T1, T2, T3, T4, T5, T6, T7, T8, _1 extends T1, _2 extends T2, _3 extends T3, _4 extends T4, _5 extends T5, _6 extends T6, _7 extends T7, _8 extends T8> Pattern8<Tuple8<T1, T2, T3, T4, T5, T6, T7, T8>, _1, _2, _3, _4, _5, _6, _7, _8> $Tuple8(Pattern<_1, ?> p1, Pattern<_2, ?> p2, Pattern<_3, ?> p3, Pattern<_4, ?> p4, Pattern<_5, ?> p5, Pattern<_6, ?> p6, Pattern<_7, ?> p7, Pattern<_8, ?> p8) {
        return Pattern8.of(Tuple8.class, p1, p2, p3, p4, p5, p6, p7, p8, io.vavr.$::Tuple8);
    }

    public static <T, _1 extends T, _2 extends List<T>> Pattern2<List.Cons<T>, _1, _2> $Cons(Pattern<_1, ?> p1, Pattern<_2, ?> p2) {
        return Pattern2.of(List.Cons.class, p1, p2, io.vavr.$::Cons);
    }

    public static <T> Pattern0<List.Nil<T>> $Nil() {
        return Pattern0.of(List.Nil.class);
    }

    public static <T, _1 extends Option<Try<T>>> Pattern1<Future<T>, _1> $Future(Pattern<_1, ?> p1) {
        return Pattern1.of(Future.class, p1, io.vavr.$::Future);
    }

    public static <L, R, _1 extends R> Pattern1<Either.Right<L, R>, _1> $Right(Pattern<_1, ?> p1) {
        return Pattern1.of(Either.Right.class, p1, io.vavr.$::Right);
    }

    public static <L, R, _1 extends L> Pattern1<Either.Left<L, R>, _1> $Left(Pattern<_1, ?> p1) {
        return Pattern1.of(Either.Left.class, p1, io.vavr.$::Left);
    }

    public static <T, _1 extends T> Pattern1<Option.Some<T>, _1> $Some(Pattern<_1, ?> p1) {
        return Pattern1.of(Option.Some.class, p1, io.vavr.$::Some);
    }

    public static <T> Pattern0<Option.None<T>> $None() {
        return Pattern0.of(Option.None.class);
    }

    public static <T, _1 extends T> Pattern1<Try.Success<T>, _1> $Success(Pattern<_1, ?> p1) {
        return Pattern1.of(Try.Success.class, p1, io.vavr.$::Success);
    }

    public static <T, _1 extends Throwable> Pattern1<Try.Failure<T>, _1> $Failure(Pattern<_1, ?> p1) {
        return Pattern1.of(Try.Failure.class, p1, io.vavr.$::Failure);
    }

    public static <E, T, _1 extends T> Pattern1<Validation.Valid<E, T>, _1> $Valid(Pattern<_1, ?> p1) {
        return Pattern1.of(Validation.Valid.class, p1, io.vavr.$::Valid);
    }

    public static <E, T, _1 extends E> Pattern1<Validation.Invalid<E, T>, _1> $Invalid(Pattern<_1, ?> p1) {
        return Pattern1.of(Validation.Invalid.class, p1, io.vavr.$::Invalid);
    }

}
// CHECKSTYLE:ON
// @formatter:on