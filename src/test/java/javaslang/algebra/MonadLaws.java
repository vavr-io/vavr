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
public interface MonadLaws<M extends Monad<?, M>> extends FunctorLaws {

    void shouldSatisfyMonadLeftIdentity();

    void shouldSatisfyMonadRightIdentity();

    void shouldSatisfyMonadAssociativity();

    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkMonadLeftIdentity(Function<? super T, ? extends Monad<T, M>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<Function<? super T, ? extends Monad<U, M>>> fs) {
        return new Property("monad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final Monad<U, M> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final Monad<U, M> term2 = f.apply(t);
                    return term1.equals(term2);
                })
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkMonadRightIdentity(Function<? super T, ? extends Monad<T, M>> unit,
                                                    Arbitrary<? extends Monad<T, M>> ms) {
        return new Property("monad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final Monad<T, M> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkMonadAssociativity(Arbitrary<? extends Monad<T, M>> ms,
                                                          Arbitrary<Function<? super T, ? extends Monad<U, M>>> fs,
                                                          Arbitrary<Function<? super U, ? extends Monad<V, M>>> gs) {
        return new Property("monad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final Monad<V, M> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final Monad<V, M> term2 = m.flatMap((T t) -> f.apply(t).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
