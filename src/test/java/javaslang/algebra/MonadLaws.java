/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public interface MonadLaws<M extends Monad<M, ?>> extends FunctorLaws {

    void shouldSatisfyMonadLeftIdentity();

    void shouldSatisfyMonadRightIdentity();

    void shouldSatisfyMonadAssociativity();

    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkMonadLeftIdentity(Function<? super T, ? extends Monad<M, T>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<Function<? super T, ? extends Monad<M, U>>> fs) {
        return new Property("monad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final Monad<M, U> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final Monad<M, U> term2 = f.apply(t);
                    return term1.equals(term2);
                })
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkMonadRightIdentity(Function<? super T, ? extends Monad<M, T>> unit,
                                                    Arbitrary<? extends Monad<M, T>> ms) {
        return new Property("monad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final Monad<M, T> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkMonadAssociativity(Arbitrary<? extends Monad<M, T>> ms,
                                                          Arbitrary<Function<? super T, ? extends Monad<M, U>>> fs,
                                                          Arbitrary<Function<? super U, ? extends Monad<M, V>>> gs) {
        return new Property("monad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final Monad<M, V> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final Monad<M, V> term2 = m.flatMap((T t) -> f.apply(t).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
