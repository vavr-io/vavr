/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.collection.List;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;

/**
 * <strong>INTERNAL TYPE - turned to io.vavr.Patterns by vavr-match annotation processor.</strong>
 * <p>
 * Provides pattern matching support for Vavr types.
 */
@Patterns
class $ {

    // -- io.vavr

    // Tuple0-N
    /**
     * Pattern matching support for {@link Tuple0}.
     *
     * @param tuple0 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static Tuple0 Tuple0(Tuple0 tuple0) { return tuple0; }

    /**
     * Pattern matching support for {@link Tuple1}.
     *
     * @param <T1> type of the 1st element
     * @param tuple1 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1> Tuple1<T1> Tuple1(Tuple1<T1> tuple1) { return tuple1; }

    /**
     * Pattern matching support for {@link Tuple2}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param tuple2 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2> Tuple2<T1, T2> Tuple2(Tuple2<T1, T2> tuple2) { return tuple2; }

    /**
     * Pattern matching support for {@link Tuple3}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param tuple3 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3> Tuple3<T1, T2, T3> Tuple3(Tuple3<T1, T2, T3> tuple3) { return tuple3; }

    /**
     * Pattern matching support for {@link Tuple4}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param tuple4 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> Tuple4(Tuple4<T1, T2, T3, T4> tuple4) { return tuple4; }

    /**
     * Pattern matching support for {@link Tuple5}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param tuple5 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> Tuple5(Tuple5<T1, T2, T3, T4, T5> tuple5) { return tuple5; }

    /**
     * Pattern matching support for {@link Tuple6}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param tuple6 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> Tuple6(Tuple6<T1, T2, T3, T4, T5, T6> tuple6) { return tuple6; }

    /**
     * Pattern matching support for {@link Tuple7}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param tuple7 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3, T4, T5, T6, T7> Tuple7<T1, T2, T3, T4, T5, T6, T7> Tuple7(Tuple7<T1, T2, T3, T4, T5, T6, T7> tuple7) { return tuple7; }

    /**
     * Pattern matching support for {@link Tuple8}.
     *
     * @param <T1> type of the 1st element
     * @param <T2> type of the 2nd element
     * @param <T3> type of the 3rd element
     * @param <T4> type of the 4th element
     * @param <T5> type of the 5th element
     * @param <T6> type of the 6th element
     * @param <T7> type of the 7th element
     * @param <T8> type of the 8th element
     * @param tuple8 the tuple to match
     * @return the tuple unchanged
     */
    @Unapply
    static <T1, T2, T3, T4, T5, T6, T7, T8> Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> Tuple8(Tuple8<T1, T2, T3, T4, T5, T6, T7, T8> tuple8) { return tuple8; }

    // -- io.vavr.collection

    // List
    /**
     * Pattern matching support for {@link io.vavr.collection.List.Cons}.
     *
     * @param <T> component type of the list
     * @param cons the non-empty list to match
     * @return a tuple of the head element and the tail list
     */
    @Unapply
    static <T> Tuple2<T, List<T>> Cons(List.Cons<T> cons) { return Tuple.of(cons.head(), cons.tail()); }

    /**
     * Pattern matching support for {@link io.vavr.collection.List.Nil}.
     *
     * @param <T> component type of the list
     * @param nil the empty list to match
     * @return an empty tuple
     */
    @Unapply
    static <T> Tuple0 Nil(List.Nil<T> nil) { return Tuple.empty(); }

    // -- io.vavr.concurrent

    /**
     * Pattern matching support for {@link Future}.
     *
     * @param <T> type of the Future value
     * @param future the future to match
     * @return a tuple containing the future's value as an Option of Try
     */
    @Unapply
    static <T> Tuple1<Option<Try<T>>> Future(Future<T> future) { return Tuple.of(future.getValue()); }

    // -- io.vavr.control

    // Either
    /**
     * Pattern matching support for {@link io.vavr.control.Either.Right}.
     *
     * @param <L> type of Left value
     * @param <R> type of Right value
     * @param right the right either to match
     * @return a tuple containing the right value
     */
    @Unapply
    static <L, R> Tuple1<R> Right(Either.Right<L, R> right) { return Tuple.of(right.get()); }

    /**
     * Pattern matching support for {@link io.vavr.control.Either.Left}.
     *
     * @param <L> type of Left value
     * @param <R> type of Right value
     * @param left the left either to match
     * @return a tuple containing the left value
     */
    @Unapply
    static <L, R> Tuple1<L> Left(Either.Left<L, R> left) { return Tuple.of(left.getLeft()); }

    // Option
    /**
     * Pattern matching support for {@link io.vavr.control.Option.Some}.
     *
     * @param <T> type of the Option value
     * @param some the present option to match
     * @return a tuple containing the option value
     */
    @Unapply
    static <T> Tuple1<T> Some(Option.Some<T> some) { return Tuple.of(some.get()); }

    /**
     * Pattern matching support for {@link io.vavr.control.Option.None}.
     *
     * @param <T> type of the Option value
     * @param none the absent option to match
     * @return an empty tuple
     */
    @Unapply
    static <T> Tuple0 None(Option.None<T> none) { return Tuple.empty(); }

    // Try
    /**
     * Pattern matching support for {@link io.vavr.control.Try.Success}.
     *
     * @param <T> type of the Try value
     * @param success the successful try to match
     * @return a tuple containing the success value
     */
    @Unapply
    static <T> Tuple1<T> Success(Try.Success<T> success) { return Tuple.of(success.get()); }

    /**
     * Pattern matching support for {@link io.vavr.control.Try.Failure}.
     *
     * @param <T> type of the Try value
     * @param failure the failed try to match
     * @return a tuple containing the failure cause
     */
    @Unapply
    static <T> Tuple1<Throwable> Failure(Try.Failure<T> failure) { return Tuple.of(failure.getCause()); }

    // Validation
    /**
     * Pattern matching support for {@link io.vavr.control.Validation.Valid}.
     *
     * @param <E> type of the error value
     * @param <T> type of the valid value
     * @param valid the valid validation to match
     * @return a tuple containing the valid value
     */
    @Unapply
    static <E, T> Tuple1<T> Valid(Validation.Valid<E, T> valid) { return Tuple.of(valid.get()); }

    /**
     * Pattern matching support for {@link io.vavr.control.Validation.Invalid}.
     *
     * @param <E> type of the error value
     * @param <T> type of the valid value
     * @param invalid the invalid validation to match
     * @return a tuple containing the error value
     */
    @Unapply
    static <E, T> Tuple1<E> Invalid(Validation.Invalid<E, T> invalid) { return Tuple.of(invalid.getError()); }

}
