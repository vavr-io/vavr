/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

import javaslang.test.Arbitrary;
import javaslang.test.CheckResult;
import javaslang.test.Property;

import java.util.function.Function;

public interface FunctorLaws {

    void shouldSatisfyFunctorIdentity();

    void shouldSatisfyFunctorComposition();

    // m.map(id) ≡ id
    default <T> CheckResult checkFunctorIdentity(Arbitrary<? extends Functor<T>> functors) {
        return Property.def("functor.identity")
                .forAll(functors)
                .suchThat(functor -> functor.map(t -> t).equals(functor))
                .check();
    }

    // m.map(f).map(g) ≡ m.map(x -> g.apply(f.apply(x)))
    default <T, U, V> CheckResult checkFunctorComposition(Arbitrary<? extends Functor<T>> functors,
                                                          Arbitrary<Function<? super T, ? extends U>> fs,
                                                          Arbitrary<Function<? super U, ? extends V>> gs) {
        return Property.def("functor.composition")
                .forAll(functors, fs, gs)
                .suchThat((functor, f, g) -> functor.map(f).map(g).equals(functor.map(t -> g.apply(f.apply(t)))))
                .check();
    }
}
