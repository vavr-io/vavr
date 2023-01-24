/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Option;

/**
 * <strong>INTERNAL TYPE - turned to io.vavr.Patterns by vavr-match annotation processor.</strong>
 *
 * @deprecated Will be removed in the next major version, along with VAVR's pattern matching, in favor of Java's native pattern matching.
 */
@Deprecated
class $ {

    // -- io.vavr

    // Tuple0-N
    static Tuple0 Tuple0(Tuple0 tuple0) { return tuple0; }
    static <T1> Tuple1<T1> Tuple1(Tuple1<T1> tuple1) { return tuple1; }
    static <T1, T2> Tuple2<T1, T2> Tuple2(Tuple2<T1, T2> tuple2) { return tuple2; }
    static <T1, T2, T3> Tuple3<T1, T2, T3> Tuple3(Tuple3<T1, T2, T3> tuple3) { return tuple3; }
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> Tuple4(Tuple4<T1, T2, T3, T4> tuple4) { return tuple4; }
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> Tuple5(Tuple5<T1, T2, T3, T4, T5> tuple5) { return tuple5; }
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> Tuple6(Tuple6<T1, T2, T3, T4, T5, T6> tuple6) { return tuple6; }
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> Tuple7(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple7) { return tuple7; }
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> Tuple8(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple8) { return tuple8; }

    // -- io.vavr.collection

    // List
    static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }
    static <T> Tuple0 Nil(List.Nil<T> nil) { return Tuple.empty(); }

    // -- io.vavr.concurrent

    static <T> Tuple1<Option<Try<T>>> Future(Future<T> future) { return Tuple.of(future.getValue()); }

    // -- io.vavr.control

    // Either
    static <L, R> Tuple1<R> Right(Either.Right<L, R> right) { return Tuple.of(right.get()); }
    static <L, R> Tuple1<L> Left(Either.Left<L, R> left) { return Tuple.of(left.getLeft()); }

    // Option
    static <T> Tuple1<T> Some(Option.Some<T> some) { return Tuple.of(some.get()); }
    static <T> Tuple0 None(Option.None<T> none) { return Tuple.empty(); }

    // Try
    static <T> Tuple1<T> Success(Try.Success<T> success) { return Tuple.of(success.get()); }
    static <T> Tuple1<Throwable> Failure(Try.Failure<T> failure) { return Tuple.of(failure.getCause()); }

    // Validation
    static <E, T> Tuple1<T> Valid(Validation.Valid<E, T> valid) { return Tuple.of(valid.get()); }
    static <E, T> Tuple1<E> Invalid(Validation.Invalid<E, T> invalid) { return Tuple.of(invalid.getError()); }

}
