/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static javaslang.API.List;
import static javaslang.API.Tuple;

/**
 * Internal class, containing helpers.
 *
 * @author Daniel Dietrich
 * @since 2.0.0
 */
final class Collections {

    // checks, if the *elements* of the given iterables are equal
    static boolean areEqual(Iterable<?> iterable1, Iterable<?> iterable2) {
        final java.util.Iterator<?> iter1 = iterable1.iterator();
        final java.util.Iterator<?> iter2 = iterable2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!Objects.equals(iter1.next(), iter2.next())) {
                return false;
            }
        }
        return iter1.hasNext() == iter2.hasNext();
    }

    @SuppressWarnings("unchecked")
    static <T, S extends Seq<T>> Iterator<S> crossProduct(S empty, S seq, int power) {
        if (power < 0) {
            return Iterator.empty();
        } else {
            return Iterator.range(0, power)
                           .foldLeft(Iterator.of(empty), (product, ignored) -> product.flatMap(el -> seq.map(t -> (S) el.append(t))));
        }
    }

    static <T> Iterator<T> fill(int n, Supplier<? extends T> supplier) {
        Objects.requireNonNull(supplier, "supplier is null");
        return tabulate(n, ignored -> supplier.get());
    }

    static <C extends Traversable<T>, T> C fill(int n, Supplier<? extends T> s, C empty, Function<T[], C> of) {
        Objects.requireNonNull(s, "s is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        return tabulate(n, anything -> s.get(), empty, of);
    }

    static <T, C, R extends Iterable<T>> Map<C, R> groupBy(Traversable<T> source, Function<? super T, ? extends C> classifier, Function<? super Iterable<T>, R> mapper) {
        Objects.requireNonNull(classifier, "classifier is null");
        Objects.requireNonNull(mapper, "mapper is null");
        final java.util.Map<C, Collection<T>> mutableResults = new java.util.LinkedHashMap<>();
        for (T value : source) {
            final C key = classifier.apply(value);
            mutableResults.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
        Map<C, R> results = LinkedHashMap.empty();
        for (java.util.Map.Entry<C, Collection<T>> entry : mutableResults.entrySet()) {
            results = results.put(entry.getKey(), mapper.apply(entry.getValue()));
        }
        return results;
    }

    // hashes the elements of an iterable
    static int hash(Iterable<?> iterable) {
        int hashCode = 1;
        for (Object o : iterable) {
            hashCode = 31 * hashCode + Objects.hashCode(o);
        }
        return hashCode;
    }

    static Option<Integer> indexOption(int index) {
        return Option.when(index >= 0, index);
    }

    static <T> boolean isTraversableAgain(Iterable<? extends T> iterable) {
        return (iterable instanceof Collection) ||
               (iterable instanceof Traversable && ((Traversable<?>) iterable).isTraversableAgain());
    }

    @SuppressWarnings("unchecked")
    static <K, V, K2, U extends Map<K2, V>> U mapKeys(Map<K, V> source, U zero, Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
        Objects.requireNonNull(zero, "zero is null");
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMerge, "valueMerge is null");
        return source.foldLeft(zero, (acc, entry) -> {
            final K2 k2 = keyMapper.apply(entry._1);
            final V v2 = entry._2;
            final Option<V> v1 = acc.get(k2);
            final V v = v1.isDefined() ? valueMerge.apply(v1.get(), v2) : v2;
            return (U) acc.put(k2, v);
        });
    }

    @SuppressWarnings("unchecked")
    static <C extends Traversable<T>, T> C removeAll(C source, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Set<T> removed = HashSet.ofAll(elements);
        return (C) source.filter(e -> !removed.contains(e));
    }

    @SuppressWarnings("unchecked")
    static <C extends Traversable<T>, T> C removeAll(C source, Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (C) source.filter(predicate.negate());
    }

    static <C extends Traversable<T>, T> C removeAll(C source, T element) {
        Objects.requireNonNull(element, "element is null");
        return removeAll(source, List.of(element));
    }

    @SuppressWarnings("unchecked")
    static <C extends Traversable<T>, T> C retainAll(C source, Iterable<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        final Set<T> removed = HashSet.ofAll(elements);
        return (C) source.filter(removed::contains);
    }

    static <T> Iterator<T> reverseIterator(Iterable<T> iterable) {
        if (iterable instanceof java.util.List) {
            final java.util.List<T> list = (java.util.List<T>) iterable;
            return new Iterator<T>() {
                final java.util.ListIterator<T> delegate = list.listIterator(list.size());

                @Override
                public boolean hasNext() { return delegate.hasPrevious(); }

                @Override
                public T next() { return delegate.previous(); }
            };
        } else if (iterable instanceof Seq) {
            return ((Seq<T>) iterable).reverseIterator();
        } else {
            return List.<T> empty().pushAll(iterable).iterator();
        }
    }

    static <T, U, R extends Traversable<U>> R scanLeft(Traversable<? extends T> source,
            U zero, BiFunction<? super U, ? super T, ? extends U> operation, Function<Iterator<U>, R> finisher) {
        Objects.requireNonNull(operation, "operation is null");
        final Iterator<U> iterator = source.iterator().scanLeft(zero, operation);
        return finisher.apply(iterator);
    }

    static <T, U, R extends Traversable<U>> R scanRight(Traversable<? extends T> source,
            U zero, BiFunction<? super T, ? super U, ? extends U> operation, Function<Iterator<U>, R> finisher) {
        Objects.requireNonNull(operation, "operation is null");
        final Iterator<? extends T> reversedElements = reverseIterator(source);
        return scanLeft(reversedElements, zero, (u, t) -> operation.apply(t, u), us -> finisher.apply(reverseIterator(us)));
    }

    static <T, S extends Seq<T>> S shuffle(S source, Function<? super Iterable<T>, S> ofAll) {
        if (source.length() <= 1) {
            return source;
        }

        final java.util.List<T> list = source.toJavaList();
        java.util.Collections.shuffle(list);
        return ofAll.apply(list);
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

    static <C extends Traversable<T>, T> C tabulate(int n, Function<? super Integer, ? extends T> f, C empty, Function<T[], C> of) {
        Objects.requireNonNull(f, "f is null");
        Objects.requireNonNull(empty, "empty is null");
        Objects.requireNonNull(of, "of is null");
        if (n <= 0) {
            return empty;
        } else {
            @SuppressWarnings("unchecked")
            final T[] elements = (T[]) new Object[n];
            for (int i = 0; i < n; i++) {
                elements[i] = f.apply(i);
            }
            return of.apply(elements);
        }
    }

    static <T> Tuple2<Iterable<? extends T>, Integer> withSize(Iterable<? extends T> iterable) {
        if (iterable instanceof Collection) {
            return Tuple(iterable, ((Collection<?>) iterable).size());
        } else if (iterable instanceof Traversable && isTraversableAgain(iterable)) {
            return Tuple(iterable, ((Traversable<?>) iterable).size());
        } else {
            return withSize(List(iterable));
        }
    }
}
