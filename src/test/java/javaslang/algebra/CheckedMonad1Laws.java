/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.CheckedFunction1;
import javaslang.Function1;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

@SuppressWarnings("Convert2MethodRef")
public interface CheckedMonad1Laws<M extends CheckedMonad1<?, M>> extends CheckedFunctor1Laws {

    void shouldSatisfyCheckedMonadLeftIdentity();

    void shouldSatisfyCheckedMonadRightIdentity();

    void shouldSatisfyCheckedMonadAssociativity();

    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkCheckedMonadLeftIdentity(CheckedFunction1<? super T, ? extends CheckedMonad1<T, M>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<CheckedFunction1<? super T, ? extends CheckedMonad1<U, M>>> fs) {
        return new Property("checkedMonad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final CheckedMonad1<U, M> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final CheckedMonad1<U, M> term2 = f.apply(t);
                    return term1.equals(term2);
                })
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkCheckedMonadRightIdentity(CheckedFunction1<? super T, ? extends CheckedMonad1<T, M>> unit,
                                                    Arbitrary<? extends CheckedMonad1<T, M>> ms) {
        return new Property("checkedMonad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final CheckedMonad1<T, M> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkCheckedMonadAssociativity(Arbitrary<? extends CheckedMonad1<T, M>> ms,
                                                          Arbitrary<CheckedFunction1<? super T, ? extends CheckedMonad1<U, M>>> fs,
                                                          Arbitrary<CheckedFunction1<? super U, ? extends CheckedMonad1<V, M>>> gs) {
        return new Property("checkedMonad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final CheckedMonad1<V, M> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final CheckedMonad1<V, M> term2 = m.flatMap((T t) -> f.apply(t).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
