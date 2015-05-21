/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.control.Try;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

@SuppressWarnings("Convert2MethodRef")
public interface CheckedMonadLaws<M extends CheckedMonad<M, ?>> extends CheckedFunctorLaws {

    void shouldSatisfyCheckedMonadLeftIdentity();

    void shouldSatisfyCheckedMonadRightIdentity();

    void shouldSatisfyCheckedMonadAssociativity();

    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkCheckedMonadLeftIdentity(Try.CheckedFunction<? super T, ? extends CheckedMonad<M, T>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<Try.CheckedFunction<? super T, ? extends CheckedMonad<M, U>>> fs) {
        return new Property("checkedMonad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final CheckedMonad<M, U> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final CheckedMonad<M, U> term2 = f.apply(t);
                    return term1.equals(term2);
                })
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkCheckedMonadRightIdentity(Try.CheckedFunction<? super T, ? extends CheckedMonad<M, T>> unit,
                                                    Arbitrary<? extends CheckedMonad<M, T>> ms) {
        return new Property("checkedMonad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final CheckedMonad<M, T> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkCheckedMonadAssociativity(Arbitrary<? extends CheckedMonad<M, T>> ms,
                                                          Arbitrary<Try.CheckedFunction<? super T, ? extends CheckedMonad<M, U>>> fs,
                                                          Arbitrary<Try.CheckedFunction<? super U, ? extends CheckedMonad<M, V>>> gs) {
        return new Property("checkedMonad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final CheckedMonad<M, V> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final CheckedMonad<M, V> term2 = m.flatMap((T t) -> f.apply(t).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
