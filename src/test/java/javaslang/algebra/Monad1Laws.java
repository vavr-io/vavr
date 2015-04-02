/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Function1;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

@SuppressWarnings("Convert2MethodRef")
public interface Monad1Laws<M extends Monad1<?, M>> extends Functor1Laws {

    void shouldSatisfyMonadLeftIdentity();

    void shouldSatisfyMonadRightIdentity();

    void shouldSatisfyMonadAssociativity();

    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkMonadLeftIdentity(Function1<? super T, ? extends Monad1<T, M>> unit,
                                                 Arbitrary<T> ts,
                                                 Arbitrary<Function1<? super T, ? extends Monad1<U, M>>> fs) {
        return new Property("monad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final Monad1<U, M> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final Monad1<U, M> term2 = f.apply(t);
                    return term1.equals(term2);
                })
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkMonadRightIdentity(Function1<? super T, ? extends Monad1<T, M>> unit,
                                               Arbitrary<? extends Monad1<T, M>> ms) {
        return new Property("monad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final Monad1<T, M> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkMonadAssociativity(Arbitrary<? extends Monad1<T, M>> ms,
                                                     Arbitrary<Function1<? super T, ? extends Monad1<U, M>>> fs,
                                                     Arbitrary<Function1<? super U, ? extends Monad1<V, M>>> gs) {
        return new Property("monad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final Monad1<V, M> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final Monad1<V, M> term2 = m.flatMap((T t) -> f.apply(t).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
