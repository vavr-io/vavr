/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import java.util.function.Function;

class TraversableTest {

}

@SuppressWarnings({"unused", "ConstantConditions", "TypeParameterExplicitlyExtendsObject"})
final class ShouldJustCompile {

    private ShouldJustCompile() {
        throw new Error("Should just compile.");
    }

    private <T> LinearSeq<T> toLinearSeq(Iterable<? extends T> iterable) {
        return null;
    }

    void shouldConvertIndexedSeqToIterable(IndexedSeq<String> indexedSeq) {
        { // Traversable
            Traversable<String> traversable1 = indexedSeq.to(this::toLinearSeq);
            Traversable<CharSequence> traversable2 = indexedSeq.to(this::toLinearSeq);
        }
        { // Seq
            Seq<String> seq1 = indexedSeq.to(this::toLinearSeq);
            Seq<CharSequence> seq2 = indexedSeq.to(this::toLinearSeq);
        }
        { // LinearSeq
            LinearSeq<String> linearSeq = indexedSeq.to(this::toLinearSeq);
            LinearSeq<CharSequence> seq = indexedSeq.to(this::toLinearSeq);
        }
    }

    void shouldConvertToInThePresenceOfVariance() {

        class A {
        }

        class B extends A {
        }

        class C extends B {
        }

        final Traversable<Number> traversable = null;

        // -- Number

        {
            final Function<Iterable<Number>, A> toA = ignored -> null;
            final Function<Iterable<Number>, B> toB = ignored -> null;
            final Function<Iterable<Number>, C> toC = ignored -> null;

            final A a1 = traversable.to(toA);
            final A a2 = traversable.to(toB);
            final A a3 = traversable.to(toC);
        }

        {
            final Function<Iterable<? extends Number>, A> toA = ignored -> null;
            final Function<Iterable<? extends Number>, B> toB = ignored -> null;
            final Function<Iterable<? extends Number>, C> toC = ignored -> null;

            final A a1 = traversable.to(toA);
            final A a2 = traversable.to(toB);
            final A a3 = traversable.to(toC);
        }

        {
            final Function<? super Iterable<? super Number>, ? extends A> toA = ignored -> null;
            final Function<? super Iterable<Number>, ? extends B> toB = ignored -> null;
            final Function<Iterable<? super Number>, ? extends C> toC = ignored -> null;

            final A a1 = traversable.to(toA);
            final A a2 = traversable.to(toB);
            final A a3 = traversable.to(toC);
        }

        // -- Object

        {
            final Function<Iterable<Object>, A> toA = ignored -> null;
            final Function<Iterable<Object>, B> toB = ignored -> null;
            final Function<Iterable<Object>, C> toC = ignored -> null;

            // final A a1 = traversable.to(toA);
            // final A a2 = traversable.to(toB);
            // final A a3 = traversable.to(toC);
        }

        {
            final Function<Iterable<? extends Object>, A> toA = ignored -> null;
            final Function<Iterable<? extends Object>, B> toB = ignored -> null;
            final Function<Iterable<? extends Object>, C> toC = ignored -> null;

            final A a1 = traversable.to(toA);
            final A a2 = traversable.to(toB);
            final A a3 = traversable.to(toC);
        }

        {
            final Function<? super Iterable<? super Object>, ? extends A> toA = ignored -> null;
            final Function<? super Iterable<Object>, ? extends B> toB = ignored -> null;
            final Function<Iterable<? super Object>, ? extends C> toC = ignored -> null;

            // final A a1 = traversable.to(toA);
            // final A a2 = traversable.to(toB);
            // final A a3 = traversable.to(toC);
        }

        // -- Integer

        {
            final Function<Iterable<Integer>, A> toA = ignored -> null;
            final Function<Iterable<Integer>, B> toB = ignored -> null;
            final Function<Iterable<Integer>, C> toC = ignored -> null;

            // final A a1 = traversable.to(toA);
            // final A a2 = traversable.to(toB);
            // final A a3 = traversable.to(toC);
        }

        {
            final Function<Iterable<? extends Integer>, A> toA = ignored -> null;
            final Function<Iterable<? extends Integer>, B> toB = ignored -> null;
            final Function<Iterable<? extends Integer>, C> toC = ignored -> null;

            // final A a1 = traversable.to(toA);
            // final A a2 = traversable.to(toB);
            // final A a3 = traversable.to(toC);
        }

        {
            final Function<? super Iterable<? super Integer>, ? extends A> toA = ignored -> null;
            final Function<? super Iterable<Integer>, ? extends B> toB = ignored -> null;
            final Function<Iterable<? super Integer>, ? extends C> toC = ignored -> null;

            final A a1 = traversable.to(toA);
            // final A a2 = traversable.to(toB);
            final A a3 = traversable.to(toC);
        }
    }
}
