/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.collection.Stream;
import javaslang.concurrent.Future;
import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.control.Try;
import javaslang.control.Validation;
import javaslang.match.annotation.Unapply;
import javaslang.match.annotation.Patterns;

/**
 * <strong>INTERNAL TYPE - turned to javaslang.Patterns by javaslang-match annotation processor.</strong>
 */
@Patterns class $ {

    // -- javaslang

    // Tuple0-N
    @Unapply static Tuple0 Tuple0(Tuple0 tuple0) { return tuple0; }
    @Unapply static <T1> Tuple1<T1> Tuple1(Tuple1<T1> tuple1) { return tuple1; }
    @Unapply static <T1, T2> Tuple2<T1, T2> Tuple2(Tuple2<T1, T2> tuple2) { return tuple2; }
    @Unapply static <T1, T2, T3> Tuple3<T1, T2, T3> Tuple3(Tuple3<T1, T2, T3> tuple3) { return tuple3; }
    @Unapply static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> Tuple4(Tuple4<T1, T2, T3, T4> tuple4) { return tuple4; }
    @Unapply static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> Tuple5(Tuple5<T1, T2, T3, T4, T5> tuple5) { return tuple5; }
    @Unapply static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> Tuple6(Tuple6<T1, T2, T3, T4, T5, T6> tuple6) { return tuple6; }
    @Unapply static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> Tuple7(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple7) { return tuple7; }
    @Unapply static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> Tuple8(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple8) { return tuple8; }

    // -- javaslang.collection

    // List
    @Unapply static <T> Tuple2<T, List<T>> List(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }
    @Unapply static <T> Tuple0 List(List.Nil<T> nil) { return Tuple.empty(); }

    // Stream
    @Unapply static <T> Tuple2<T, Stream<T>> Stream(Stream.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }
    @Unapply static <T> Tuple0 Stream(Stream.Empty<T> empty) { return Tuple.empty(); }

    // -- javaslang.concurrent

    @Unapply static <T> Tuple1<Option<Try<T>>> Future(Future<T> future) { return Tuple.of(future.getValue()); }

    // -- javaslang.control

    // Either
    @Unapply static <L, R> Tuple1<R> Right(Either.Right<L, R> right) { return Tuple.of(right.get()); }
    @Unapply static <L, R> Tuple1<L> Left(Either.Left<L, R> left) { return Tuple.of(left.getLeft()); }

    // Option
    @Unapply static <T> Tuple1<T> Some(Option.Some<T> some) { return Tuple.of(some.get()); }
    @Unapply static <T> Tuple0 None(Option.None<T> none) { return Tuple.empty(); }

    // Try
    @Unapply static <T> Tuple1<T> Success(Try.Success<T> success) { return Tuple.of(success.get()); }
    @Unapply static <T> Tuple1<Throwable> Failure(Try.Failure<T> failure) { return Tuple.of(failure.getCause()); }

    // Validation
    @Unapply static <E, T> Tuple1<T> Valid(Validation.Valid<E, T> valid) { return Tuple.of(valid.get()); }
    @Unapply static <E, T> Tuple1<E> Invalid(Validation.Invalid<E, T> invalid) { return Tuple.of(invalid.getError()); }

}
