/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;

import java.util.Objects;

public class Test {

    void test() {

        Cons($(), $());

        Cons(1, $());

    }

    // `Cons($(), $())` results in `(x, xs) -> ...`
    static <__, T> void Cons(InversePattern<? extends T> p1, InversePattern<? extends List<T>> p2) {
    }

//    // Ambiguous to the above, `Cons($(), $())` does not compile any more
//    static <__, T> void Cons(T p1, InversePattern<? extends List<T>> p2) {
//    }

    // Solution 1: Add additional generic parameters with extends relation
    static <__, T, T2 extends T> void Cons(T p1, InversePattern<? extends List<T2>> p2) {
    }

//    // Solution 2: `Cons(1, $())` and `Cons("1", $())` do both compile. This is unsafe!
//    static <__, T> void Cons(Object p1, InversePattern<? extends List<T>> p2) {
//    }
//
//    // Solution 3: `Cons(Eq(1), $())` Syntax unsatisfying/too complicated.
//    static <__, T> void Cons(EqualsPattern<? extends T> p1, InversePattern<? extends List<T>> p2) {
//    }

//    static <__, T> void Cons(InversePattern<? extends T> p1, T p2) {
//    }

//    static <__, T> void Cons(T p1, List<T> p2) {
//    }

//    static <__, T> void Cons(InversePattern<? extends T> p1, List<T> p2) {
//    }

    static abstract class InversePattern<T> {
        public abstract Option<T> apply(T t);
    }

    static abstract class Pattern0 {
        public abstract Option<Void> apply(Object o);
    }

    static abstract class Pattern1<T, T1> {
        public abstract Option<T1> apply(Object o);
    }

    static abstract class EqualsPattern<T> {
        public abstract Option<T> apply(T t);
    }

    static Pattern0 $_ = new Pattern0() {
        @Override
        public Option<Void> apply(Object any) {
            return Option.nothing();
        }
    };

    static <T1> Pattern1<T1, T1> $(T1 prototype) {
        return new Pattern1<T1, T1>() {
            @SuppressWarnings("unchecked")
            @Override
            public Option<T1> apply(Object that) {
                // 'that' is of type T1 when injected by a type-safe pattern
                return Objects.equals(that, prototype) ? Option.some((T1) that) : Option.none();
            }
        };
    }

    static <T> InversePattern<T> $() {
        return new InversePattern<T>() {
            @Override
            public Option<T> apply(T t) {
                return Option.some(t);
            }
        };
    }
}
