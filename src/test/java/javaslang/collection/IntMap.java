/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class IntMap<T> implements Traversable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final IntMap<?> EMPTY = IntMap.of(HashMap.empty());

    private final Map<Integer, T> original;

    public static <T> IntMap<T> of(Map<Integer, T> original) {
        return new IntMap<>(original);
    }

    private IntMap(Map<Integer, T> original) {
        this.original = original;
    }

    Map<Integer, T> original() {
        return original;
    }

    @Override
    public int hashCode() {
        return original.values().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof IntMap) {
            final IntMap<?> that = (IntMap<?>) o;
            return original.values().equals(that.original.values());
        } else if (o instanceof Iterable) {
            final Iterable<?> that = (Iterable<?>) o;
            return original.values().equals(that);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return original.mkString(", ", "IntMap(", ")");
    }

    private Object readResolve() {
        return original.isEmpty() ? EMPTY : this;
    }

    @Override
    public IntMap<T> clear() {
        return IntMap.of(original.clear());
    }

    @Override
    public IntMap<T> distinct() {
        return IntMap.of(original.distinct());
    }

    @Override
    public IntMap<T> distinctBy(Comparator<? super T> comparator) {
        return IntMap.of(original.distinctBy((o1, o2) -> comparator.compare(o1.value, o2.value)));
    }

    @Override
    public <U> IntMap<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return IntMap.of(original.distinctBy(f -> keyExtractor.apply(f.value)));
    }

    @Override
    public IntMap<T> drop(int n) {
        final Map<Integer, T> dropped = original.drop(n);
        return dropped == original ? this : IntMap.of(dropped);
    }

    @Override
    public IntMap<T> dropRight(int n) {
        final Map<Integer, T> dropped = original.dropRight(n);
        return dropped == original ? this : IntMap.of(dropped);
    }

    @Override
    public IntMap<T> dropWhile(Predicate<? super T> predicate) {
        return IntMap.of(original.dropWhile(p -> predicate.test(p.value)));
    }

    @Override
    public IntMap<T> filter(Predicate<? super T> predicate) {
        return IntMap.of(original.filter(p -> predicate.test(p.value)));
    }

    @Override
    public Option<T> findLast(Predicate<? super T> predicate) {
        return original.findLast(p -> predicate.test(p.value)).map(o -> o.value);
    }

    @Override
    public <U> Seq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return original.flatMap(e -> mapper.apply(e.value));
    }

    @Override
    public <U> Seq<U> flatten() {
        return original.flatten();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return original.foldRight(zero, (e, u) -> f.apply(e.value, u));
    }

    @Override
    public <C> Map<C, ? extends IntMap<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return original.groupBy(e -> classifier.apply(e.value)).map((k, v) -> Map.Entry.of(k, IntMap.of(v)));
    }

    @Override
    public boolean hasDefiniteSize() {
        return original.hasDefiniteSize();
    }

    @Override
    public T head() {
        return original.head().value;
    }

    @Override
    public Option<T> headOption() {
        return original.headOption().map(o -> o.value);
    }

    @Override
    public IntMap<T> init() {
        return IntMap.of(original.init());
    }

    @Override
    public Option<? extends IntMap<T>> initOption() {
        return original.initOption().map(IntMap::of);
    }

    @Override
    public boolean isEmpty() {
        return original.isEmpty();
    }

    @Override
    public boolean isTraversableAgain() {
        return original.isTraversableAgain();
    }

    @Override
    public int length() {
        return original.length();
    }

    @Override
    public <U> Traversable<U> map(Function<? super T, ? extends U> mapper) {
        return original.map(e -> mapper.apply(e.value));
    }

    @Override
    public Tuple2<IntMap<T>, IntMap<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return original.partition(p -> predicate.test(p.value)).map(IntMap::of, IntMap::of);
    }

    @Override
    public IntMap<T> peek(Consumer<? super T> action) {
        return IntMap.of(original.peek(e -> action.accept(e.value)));
    }

    @Override
    public T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        Objects.requireNonNull(op, "op is null");
        return original.reduceRight((t1, t2) -> Map.Entry.of(t2.key, op.apply(t1.value, t2.value))).value;
    }

    @Override
    public IntMap<T> replace(T currentElement, T newElement) {
        final Option<Map.Entry<Integer, T>> currentEntryOpt = original.findFirst(e -> e.value.equals(currentElement));
        if (currentEntryOpt.isDefined()) {
            final Map.Entry<Integer, T> currentEntry = currentEntryOpt.get();
            return IntMap.of(original.replace(currentEntry, Map.Entry.of(currentEntry.key, newElement)));
        } else {
            return this;
        }
    }

    @Override
    public IntMap<T> replaceAll(T currentElement, T newElement) {
        Map<Integer, T> result = original;
        for (Map.Entry<Integer, T> entry : original.filter(e -> e.value.equals(currentElement))) {
            result = result.replaceAll(entry, Map.Entry.of(entry.key, newElement));
        }
        return IntMap.of(result);
    }

    @Override
    public IntMap<T> retainAll(Iterable<? extends T> elements) {
        final Set<T> elementsSet = HashSet.ofAll(elements);
        return IntMap.of(original.retainAll(original.filter(e -> elementsSet.contains(e.value))));
    }

    @Override
    public Tuple2<? extends IntMap<T>, ? extends IntMap<T>> span(Predicate<? super T> predicate) {
        return original.span(p -> predicate.test(p.value)).map(IntMap::of, IntMap::of);
    }

    public Spliterator<T> spliterator() {
        class SpliteratorProxy implements Spliterator<T> {
            private final Spliterator<Map.Entry<Integer, T>> spliterator;

            SpliteratorProxy(Spliterator<Map.Entry<Integer, T>> spliterator) {
                this.spliterator = spliterator;
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return spliterator.tryAdvance(a -> action.accept(a.value));
            }

            @Override
            public Spliterator<T> trySplit() {
                return new SpliteratorProxy(spliterator.trySplit());
            }

            @Override
            public long estimateSize() {
                return spliterator.estimateSize();
            }

            @Override
            public int characteristics() {
                return spliterator.characteristics();
            }
        }
        return new SpliteratorProxy(original.spliterator());
    }

    @Override
    public IntMap<T> tail() {
        return IntMap.of(original.tail());
    }

    @Override
    public Option<IntMap<T>> tailOption() {
        return original.tailOption().map(IntMap::of);
    }

    @Override
    public IntMap<T> take(int n) {
        return IntMap.of(original.take(n));
    }

    @Override
    public IntMap<T> takeRight(int n) {
        return IntMap.of(original.takeRight(n));
    }

    @Override
    public Traversable<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    public IntMap<T> takeWhile(Predicate<? super T> predicate) {
        return IntMap.of(original.takeWhile(p -> predicate.test(p.value)));
    }

    @Override
    public <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    @Override
    public <U> Seq<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zip(that));
    }

    @Override
    public <U> Seq<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Seq<Tuple2<T, Integer>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }
}
