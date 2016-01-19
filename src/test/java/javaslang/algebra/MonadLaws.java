/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.Kind1;
import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

import java.util.function.Function;

@SuppressWarnings("Convert2MethodRef")
public interface MonadLaws<M extends Kind1<M, ?>> extends FunctorLaws {

    void shouldSatisfyMonadLeftIdentity();

    void shouldSatisfyMonadRightIdentity();

    void shouldSatisfyMonadAssociativity();

    // unit(t).flatMap(f) ≡ f.apply(t)
    default <T, U> CheckResult checkMonadLeftIdentity(Function<? super T, ? extends Monad<M, T>> unit,
                                                      Arbitrary<T> ts,
                                                      Arbitrary<Function<? super T, ? extends Monad<M, U>>> fs) {
        return Property.def("monad.left_identity")
                .forAll(ts, fs)
                .suchThat((t, f) -> unit.apply(t).flatMap(f).equals(f.apply(t)))
                .check();
    }

    // m.flatMap(unit) ≡ m
    default <T> CheckResult checkMonadRightIdentity(Function<? super T, ? extends Monad<M, T>> unit,
                                                    Arbitrary<? extends Monad<M, T>> ms) {
        return Property.def("monad.right_identity")
                .forAll(ms)
                .suchThat(m -> m.flatMap(unit).equals(m))
                .check();
    }

    // m.flatMap(f).flatMap(g) ≡ m.flatMap(t -> f.apply(t).flatMap(g))
    default <T, U, V> CheckResult checkMonadAssociativity(Arbitrary<? extends Monad<M, T>> ms,
                                                          Arbitrary<Function<? super T, ? extends Monad<M, U>>> fs,
                                                          Arbitrary<Function<? super U, ? extends Monad<M, V>>> gs) {
        return Property.def("monad.associativity")
                .forAll(ms, fs, gs)
                .suchThat((m, f, g) -> m.flatMap(f).flatMap(g).equals(m.flatMap(t -> f.apply(t).flatMap(g))))
                .check();
    }
}
