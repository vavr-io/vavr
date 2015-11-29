/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.collection.Iterator;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public interface MonadLaws extends FunctorLaws {

    void shouldSatisfyMonadLeftIdentity();

    void shouldSatisfyMonadRightIdentity();

    void shouldSatisfyMonadAssociativity();

    @SuppressWarnings("unchecked")
    // unit(a).flatMap(f) ≡ f.apply(a)
    default <T, U> CheckResult checkMonadLeftIdentity(Function<? super T, ? extends Monad<T>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<Function<? super T, ? extends Iterable<U>>> fs) {
        return Property.def("monad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> {
                    final Iterable<U> term1 = unit.apply(t).flatMap((T tt) -> f.apply(tt));
                    final Iterable<U> term2 = f.apply(t);
                    //check  structural equality
                    return Iterator.ofAll(term1).eq(term2);
                })
                .check();
    }

    @SuppressWarnings("unchecked")
    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkMonadRightIdentity(Function<? super T, ? extends Monad<T>> unit,
                                                    Arbitrary<? extends Monad<T>> ms) {
        return Property.def("monad.right_identity")
                .forAll(ms)
                .suchThat(m -> {
                    final Monad<T> term = m.flatMap((T t) -> unit.apply(t));
                    return term.equals(m);
                })
                .check();
    }

    @SuppressWarnings("unchecked")
    // m.flatMap(f).flatMap(g) ≡ m.flatMap(x -> f.apply(x).flatMap(g))
    default <T, U, V> CheckResult checkMonadAssociativity(Arbitrary<? extends Monad<T>> ms,
                                                          Arbitrary<Function<? super T, ? extends Iterable<U>>> fs,
                                                          Arbitrary<Function<? super U, ? extends Iterable<V>>> gs) {
        return Property.def("monad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> {
                    final Monad<V> term1 = m.flatMap((T t) -> f.apply(t)).flatMap((U u) -> g.apply(u));
                    final Monad<V> term2 = m.flatMap((T t) -> ((Monad<U>) f.apply(t)).flatMap((U u) -> g.apply(u)));
                    return term1.equals(term2);
                })
                .check();
    }
}
