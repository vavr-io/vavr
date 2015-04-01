/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import javaslang.*;

public interface UberMonad<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M extends HigherKinded1<?, M> & HigherKinded2<?, ?, M> & HigherKinded3<?, ?, ?, M> & HigherKinded4<?, ?, ?, ?, M> & HigherKinded5<?, ?, ?, ?, ?, M> & HigherKinded6<?, ?, ?, ?, ?, ?, M> & HigherKinded7<?, ?, ?, ?, ?, ?, ?, M> & HigherKinded8<?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded9<?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded10<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded11<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded12<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded13<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded14<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded15<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded16<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded17<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded18<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded19<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded20<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded21<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded22<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded23<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded24<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded25<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M> & HigherKinded26<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, M>> extends
    Monad1<T1, M>, HigherKinded1<T1, M>,
    Monad2<T1, T2, M>, HigherKinded2<T1, T2, M>,
    Monad3<T1, T2, T3, M>, HigherKinded3<T1, T2, T3, M>,
    Monad4<T1, T2, T3, T4, M>, HigherKinded4<T1, T2, T3, T4, M>,
    Monad5<T1, T2, T3, T4, T5, M>, HigherKinded5<T1, T2, T3, T4, T5, M>,
    Monad6<T1, T2, T3, T4, T5, T6, M>, HigherKinded6<T1, T2, T3, T4, T5, T6, M>,
    Monad7<T1, T2, T3, T4, T5, T6, T7, M>, HigherKinded7<T1, T2, T3, T4, T5, T6, T7, M>,
    Monad8<T1, T2, T3, T4, T5, T6, T7, T8, M>, HigherKinded8<T1, T2, T3, T4, T5, T6, T7, T8, M>,
    Monad9<T1, T2, T3, T4, T5, T6, T7, T8, T9, M>, HigherKinded9<T1, T2, T3, T4, T5, T6, T7, T8, T9, M>,
    Monad10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, M>, HigherKinded10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, M>,
    Monad11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, M>, HigherKinded11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, M>,
    Monad12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, M>, HigherKinded12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, M>,
    Monad13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, M>, HigherKinded13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, M>,
    Monad14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, M>, HigherKinded14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, M>,
    Monad15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, M>, HigherKinded15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, M>,
    Monad16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, M>, HigherKinded16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, M>,
    Monad17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, M>, HigherKinded17<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, M>,
    Monad18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, M>, HigherKinded18<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, M>,
    Monad19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, M>, HigherKinded19<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, M>,
    Monad20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, M>, HigherKinded20<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, M>,
    Monad21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, M>, HigherKinded21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, M>,
    Monad22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, M>, HigherKinded22<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, M>,
    Monad23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, M>, HigherKinded23<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, M>,
    Monad24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, M>, HigherKinded24<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, M>,
    Monad25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, M>, HigherKinded25<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, M>,
    Monad26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M>, HigherKinded26<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> {

    @Override
    <U1> UberMonad<U1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f);

    @Override
    <U1, MONAD extends HigherKinded1<U1, M>> UberMonad<U1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function1<? super T1, MONAD> f);

    @Override
    <U1, U2> UberMonad<U1, U2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function2<? super T1, ? super T2, Tuple2<? extends U1, ? extends U2>> f);

    @Override
    default <U1, U2> UberMonad<U1, U2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2) {
        return map((t1, t2) -> Tuple.of(f1.apply(t1), f2.apply(t2)));
    }

    @Override
    <U1, U2, MONAD extends HigherKinded2<U1, U2, M>> UberMonad<U1, U2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function2<? super T1, ? super T2, MONAD> f);

    @Override
    <U1, U2, U3> UberMonad<U1, U2, U3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function3<? super T1, ? super T2, ? super T3, Tuple3<? extends U1, ? extends U2, ? extends U3>> f);

    @Override
    default <U1, U2, U3> UberMonad<U1, U2, U3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3) {
        return map((t1, t2, t3) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3)));
    }

    @Override
    <U1, U2, U3, MONAD extends HigherKinded3<U1, U2, U3, M>> UberMonad<U1, U2, U3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function3<? super T1, ? super T2, ? super T3, MONAD> f);

    @Override
    <U1, U2, U3, U4> UberMonad<U1, U2, U3, U4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function4<? super T1, ? super T2, ? super T3, ? super T4, Tuple4<? extends U1, ? extends U2, ? extends U3, ? extends U4>> f);

    @Override
    default <U1, U2, U3, U4> UberMonad<U1, U2, U3, U4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4) {
        return map((t1, t2, t3, t4) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4)));
    }

    @Override
    <U1, U2, U3, U4, MONAD extends HigherKinded4<U1, U2, U3, U4, M>> UberMonad<U1, U2, U3, U4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function4<? super T1, ? super T2, ? super T3, ? super T4, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5> UberMonad<U1, U2, U3, U4, U5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, Tuple5<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5>> f);

    @Override
    default <U1, U2, U3, U4, U5> UberMonad<U1, U2, U3, U4, U5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5) {
        return map((t1, t2, t3, t4, t5) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5)));
    }

    @Override
    <U1, U2, U3, U4, U5, MONAD extends HigherKinded5<U1, U2, U3, U4, U5, M>> UberMonad<U1, U2, U3, U4, U5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function5<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6> UberMonad<U1, U2, U3, U4, U5, U6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, Tuple6<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6> UberMonad<U1, U2, U3, U4, U5, U6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6) {
        return map((t1, t2, t3, t4, t5, t6) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, MONAD extends HigherKinded6<U1, U2, U3, U4, U5, U6, M>> UberMonad<U1, U2, U3, U4, U5, U6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function6<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7> UberMonad<U1, U2, U3, U4, U5, U6, U7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, Tuple7<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7> UberMonad<U1, U2, U3, U4, U5, U6, U7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7) {
        return map((t1, t2, t3, t4, t5, t6, t7) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, MONAD extends HigherKinded7<U1, U2, U3, U4, U5, U6, U7, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function7<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, Tuple8<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, MONAD extends HigherKinded8<U1, U2, U3, U4, U5, U6, U7, U8, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function8<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, Tuple9<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, MONAD extends HigherKinded9<U1, U2, U3, U4, U5, U6, U7, U8, U9, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function9<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, Tuple10<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, MONAD extends HigherKinded10<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function10<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, Tuple11<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, MONAD extends HigherKinded11<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function11<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, Tuple12<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, MONAD extends HigherKinded12<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function12<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, Tuple13<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, MONAD extends HigherKinded13<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, T14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function13<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, Tuple14<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, MONAD extends HigherKinded14<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, T15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function14<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, Tuple15<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, MONAD extends HigherKinded15<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, T16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function15<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, Tuple16<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, MONAD extends HigherKinded16<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, T17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function16<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function17<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, Tuple17<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, MONAD extends HigherKinded17<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, T18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function17<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function18<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, Tuple18<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, T19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, MONAD extends HigherKinded18<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, T19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function18<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, T20, T21, T22, T23, T24, T25, T26, M> map(Function19<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, Tuple19<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, T20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, MONAD extends HigherKinded19<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, T20, T21, T22, T23, T24, T25, T26, M> flatMap(Function19<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, T21, T22, T23, T24, T25, T26, M> map(Function20<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, Tuple20<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, T21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, MONAD extends HigherKinded20<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, T21, T22, T23, T24, T25, T26, M> flatMap(Function20<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, T22, T23, T24, T25, T26, M> map(Function21<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, Tuple21<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, T22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, MONAD extends HigherKinded21<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, T22, T23, T24, T25, T26, M> flatMap(Function21<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, T23, T24, T25, T26, M> map(Function22<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, Tuple22<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, T23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21), f22.apply(t22)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, MONAD extends HigherKinded22<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, T23, T24, T25, T26, M> flatMap(Function22<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, T24, T25, T26, M> map(Function23<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, Tuple23<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22, ? extends U23>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, T24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22, Function1<? super T23, ? extends U23> f23) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21), f22.apply(t22), f23.apply(t23)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, MONAD extends HigherKinded23<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, T24, T25, T26, M> flatMap(Function23<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, T25, T26, M> map(Function24<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, Tuple24<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22, ? extends U23, ? extends U24>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, T25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22, Function1<? super T23, ? extends U23> f23, Function1<? super T24, ? extends U24> f24) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21), f22.apply(t22), f23.apply(t23), f24.apply(t24)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, MONAD extends HigherKinded24<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, T25, T26, M> flatMap(Function24<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, T26, M> map(Function25<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, ? super T25, Tuple25<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22, ? extends U23, ? extends U24, ? extends U25>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, T26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22, Function1<? super T23, ? extends U23> f23, Function1<? super T24, ? extends U24> f24, Function1<? super T25, ? extends U25> f25) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21), f22.apply(t22), f23.apply(t23), f24.apply(t24), f25.apply(t25)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, MONAD extends HigherKinded25<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, T26, M> flatMap(Function25<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, ? super T25, MONAD> f);

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26, M> map(Function26<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, ? super T25, ? super T26, Tuple26<? extends U1, ? extends U2, ? extends U3, ? extends U4, ? extends U5, ? extends U6, ? extends U7, ? extends U8, ? extends U9, ? extends U10, ? extends U11, ? extends U12, ? extends U13, ? extends U14, ? extends U15, ? extends U16, ? extends U17, ? extends U18, ? extends U19, ? extends U20, ? extends U21, ? extends U22, ? extends U23, ? extends U24, ? extends U25, ? extends U26>> f);

    @Override
    default <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26, M> map(Function1<? super T1, ? extends U1> f1, Function1<? super T2, ? extends U2> f2, Function1<? super T3, ? extends U3> f3, Function1<? super T4, ? extends U4> f4, Function1<? super T5, ? extends U5> f5, Function1<? super T6, ? extends U6> f6, Function1<? super T7, ? extends U7> f7, Function1<? super T8, ? extends U8> f8, Function1<? super T9, ? extends U9> f9, Function1<? super T10, ? extends U10> f10, Function1<? super T11, ? extends U11> f11, Function1<? super T12, ? extends U12> f12, Function1<? super T13, ? extends U13> f13, Function1<? super T14, ? extends U14> f14, Function1<? super T15, ? extends U15> f15, Function1<? super T16, ? extends U16> f16, Function1<? super T17, ? extends U17> f17, Function1<? super T18, ? extends U18> f18, Function1<? super T19, ? extends U19> f19, Function1<? super T20, ? extends U20> f20, Function1<? super T21, ? extends U21> f21, Function1<? super T22, ? extends U22> f22, Function1<? super T23, ? extends U23> f23, Function1<? super T24, ? extends U24> f24, Function1<? super T25, ? extends U25> f25, Function1<? super T26, ? extends U26> f26) {
        return map((t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, t17, t18, t19, t20, t21, t22, t23, t24, t25, t26) -> Tuple.of(f1.apply(t1), f2.apply(t2), f3.apply(t3), f4.apply(t4), f5.apply(t5), f6.apply(t6), f7.apply(t7), f8.apply(t8), f9.apply(t9), f10.apply(t10), f11.apply(t11), f12.apply(t12), f13.apply(t13), f14.apply(t14), f15.apply(t15), f16.apply(t16), f17.apply(t17), f18.apply(t18), f19.apply(t19), f20.apply(t20), f21.apply(t21), f22.apply(t22), f23.apply(t23), f24.apply(t24), f25.apply(t25), f26.apply(t26)));
    }

    @Override
    <U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26, MONAD extends HigherKinded26<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26, M>> UberMonad<U1, U2, U3, U4, U5, U6, U7, U8, U9, U10, U11, U12, U13, U14, U15, U16, U17, U18, U19, U20, U21, U22, U23, U24, U25, U26, M> flatMap(Function26<? super T1, ? super T2, ? super T3, ? super T4, ? super T5, ? super T6, ? super T7, ? super T8, ? super T9, ? super T10, ? super T11, ? super T12, ? super T13, ? super T14, ? super T15, ? super T16, ? super T17, ? super T18, ? super T19, ? super T20, ? super T21, ? super T22, ? super T23, ? super T24, ? super T25, ? super T26, MONAD> f);
}