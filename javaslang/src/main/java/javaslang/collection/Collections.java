/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Internal class, containing helpers.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class Collections {

    // checks, if the *elements* of the given iterables are equal
    static boolean equals(Iterable<?> iterable1, Iterable<?> iterable2) {
        java.util.Iterator<?> iter1 = iterable1.iterator();
        java.util.Iterator<?> iter2 = iterable2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!Objects.equals(iter1.next(), iter2.next())) {
                return false;
            }
        }
        return iter1.hasNext() == iter2.hasNext();
    }

    // hashes the elements of an iterable
    static int hash(Iterable<?> iterable) {
        int hashCode = 1;
        for (Object o : iterable) {
            hashCode = 31 * hashCode + Objects.hashCode(o);
        }
        return hashCode;
    }

    static <T, U, C extends Iterable<U>, R extends Traversable<U>> R scanLeft(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super U, ? super T, ? extends U> operation,
            C cumulativeResult, BiFunction<C, U, C> combiner, Function<C, R> finisher) {
        U acc = zero;
        cumulativeResult = combiner.apply(cumulativeResult, acc);
        for (T a : elements) {
            acc = operation.apply(acc, a);
            cumulativeResult = combiner.apply(cumulativeResult, acc);
        }
        return finisher.apply(cumulativeResult);
    }

    static <T, U, C extends Iterable<U>, R extends Traversable<U>> R scanRight(
            Iterable<? extends T> elements,
            U zero, BiFunction<? super T, ? super U, ? extends U> operation,
            C cumulativeResult, BiFunction<C, U, C> combiner, Function<C, R> finisher) {
        final Iterator<? extends T> reversedElements = seq(elements).reverseIterator();
        return scanLeft(reversedElements, zero, (u, t) -> operation.apply(t, u), cumulativeResult, combiner, finisher);
    }

    @SuppressWarnings("unchecked")
    static <T, S extends Seq<T>> Iterator<S> crossProduct(S empty, S seq, int power) {
        if (power < 0) {
            throw new IllegalArgumentException("negative power");
        }
        return Iterator
                .range(0, power)
                .foldLeft(Iterator.of(empty), (product, ignored) -> product.flatMap(el -> seq.map(t -> (S) el.append(t))));
    }

    static <C extends Traversable<T>, T> C tabulate(int n, Function<? super Integer, ? extends T> f, C empty, Function<T[], C> of) {
        Objects.requireNonNull(f, "f is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        if (n <= 0) {
            return empty;
        } else {
            @SuppressWarnings("unchecked")
            T[] elements = (T[]) new Object[n];
            for (int i = 0; i < n; i++) {
                elements[i] = f.apply(i);
            }
            return of.apply(elements);
        }
    }

    static <C extends Traversable<T>, T> C fill(int n, Supplier<? extends T> s, C empty, Function<T[], C> of) {
        Objects.requireNonNull(s, "s is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        return tabulate(n, anything -> s.get(), empty, of);
    }

    static <T> Iterator<T> tabulate(int n, Function<? super Integer, ? extends T> f) {
        Objects.requireNonNull(f, "f is null");
        if (n <= 0) {
            return Iterator.empty();
        } else {
            return new AbstractIterator<T>() {

                int i = 0;

                @Override
                public boolean hasNext() {
                    return i < n;
                }

                @Override
                protected T getNext() {
                    return f.apply(i++);
                }
            };
        }
    }

    static <T> Iterator<T> fill(int n, Supplier<? extends T> s) {
        Objects.requireNonNull(s, "s is null");
        return tabulate(n, anything -> s.get());
    }

    @SuppressWarnings("unchecked")
    private static <T> Seq<T> seq(Iterable<? extends T> iterable) {
        if (iterable instanceof Seq) {
            return (Seq<T>) iterable;
        } else {
            return List.ofAll(iterable);
        }
    }
}
