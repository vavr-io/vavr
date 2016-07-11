/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.Tuple3;
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

    private final Map<Integer, T> original;

    private static final IntMap<?> EMPTY = new IntMap<>(HashMap.empty());

    @SuppressWarnings("unchecked")
    public static <T> IntMap<T> of(Map<Integer, T> original) {
        return original.isEmpty() ? (IntMap<T>) EMPTY
                : new IntMap<>(original);
    }

    private IntMap(Map<Integer, T> original) {
        this.original = original;
    }

    @Override
    public int hashCode() {
        return original.values().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntMap) {
            final IntMap<?> that = (IntMap<?>) o;
            return original.equals(that.original) || original.values().equals(that.original.values());
        } else if (o instanceof Iterable) {
            final Iterable<?> that = (Iterable<?>) o;
            return original.values().equals(that);
        } else {
            return false;
        }
    }

    @Override
    public String stringPrefix() {
        return "IntMap";
    }

    @Override
    public String toString() {
        return original.mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public IntMap<T> distinct() {
        return IntMap.of(original.distinct());
    }

    @Override
    public IntMap<T> distinctBy(Comparator<? super T> comparator) {
        return IntMap.of(original.distinctBy((o1, o2) -> comparator.compare(o1._2, o2._2)));
    }

    @Override
    public <U> IntMap<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return IntMap.of(original.distinctBy(f -> keyExtractor.apply(f._2)));
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
    public IntMap<T> dropUntil(Predicate<? super T> predicate) {
        return IntMap.of(original.dropUntil(p -> predicate.test(p._2)));
    }

    @Override
    public IntMap<T> dropWhile(Predicate<? super T> predicate) {
        return IntMap.of(original.dropWhile(p -> predicate.test(p._2)));
    }

    @Override
    public IntMap<T> filter(Predicate<? super T> predicate) {
        return IntMap.of(original.filter(p -> predicate.test(p._2)));
    }

    @Override
    public <U> Seq<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return original.flatMap(e -> mapper.apply(e._2));
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return original.foldRight(zero, (e, u) -> f.apply(e._2, u));
    }

    @Override
    public <C> Map<C, ? extends IntMap<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return original.groupBy(e -> classifier.apply(e._2)).map((k, v) -> Tuple.of(k, IntMap.of(v)));
    }

    @Override
    public Iterator<IntMap<T>> grouped(int size) {
        return original.grouped(size).map(IntMap::of);
    }

    @Override
    public boolean hasDefiniteSize() {
        return original.hasDefiniteSize();
    }

    @Override
    public T head() {
        return original.head()._2;
    }

    @Override
    public Option<T> headOption() {
        return original.headOption().map(o -> o._2);
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
    public <U> Seq<U> map(Function<? super T, ? extends U> mapper) {
        return original.map(e -> mapper.apply(e._2));
    }

    @Override
    public Tuple2<IntMap<T>, IntMap<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return original.partition(p -> predicate.test(p._2)).map(IntMap::of, IntMap::of);
    }

    @Override
    public IntMap<T> peek(Consumer<? super T> action) {
        return IntMap.of(original.peek(e -> action.accept(e._2)));
    }

    @Override
    public IntMap<T> replace(T currentElement, T newElement) {
        final Option<Tuple2<Integer, T>> currentEntryOpt = original.find(e -> e._2.equals(currentElement));
        if (currentEntryOpt.isDefined()) {
            final Tuple2<Integer, T> currentEntry = currentEntryOpt.get();
            return IntMap.of(original.replace(currentEntry, Tuple.of(original.size() + 1, newElement)));
        } else {
            return this;
        }
    }

    @Override
    public IntMap<T> replaceAll(T currentElement, T newElement) {
        Map<Integer, T> result = original;
        for (Tuple2<Integer, T> entry : original.filter(e -> e._2.equals(currentElement))) {
            result = result.replaceAll(entry, Tuple.of(entry._1, newElement));
        }
        return IntMap.of(result);
    }

    @Override
    public IntMap<T> retainAll(Iterable<? extends T> elements) {
        final Set<T> elementsSet = HashSet.ofAll(elements);
        return IntMap.of(original.retainAll(original.filter(e -> elementsSet.contains(e._2))));
    }

    @Override
    public Traversable<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        final int[] index = new int[] { 0 };
        return original.scan(Tuple.of(-1, zero), (i, t) -> Tuple.of(index[0]++, operation.apply(i._2, t._2))).values();
    }

    @Override
    public <U> Traversable<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return original.scanLeft(zero, (i, t) -> operation.apply(i, t._2));
    }

    @Override
    public <U> Traversable<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return original.scanRight(zero, (t, i) -> operation.apply(t._2, i));
    }

    @Override
    public Iterator<IntMap<T>> sliding(int size) {
        return original.sliding(size).map(IntMap::of);
    }

    @Override
    public Iterator<IntMap<T>> sliding(int size, int step) {
        return original.sliding(size, step).map(IntMap::of);
    }

    @Override
    public Tuple2<? extends IntMap<T>, ? extends IntMap<T>> span(Predicate<? super T> predicate) {
        return original.span(p -> predicate.test(p._2)).map(IntMap::of, IntMap::of);
    }

    public Spliterator<T> spliterator() {
        class SpliteratorProxy implements Spliterator<T> {
            private final Spliterator<Tuple2<Integer, T>> spliterator;

            SpliteratorProxy(Spliterator<Tuple2<Integer, T>> spliterator) {
                this.spliterator = spliterator;
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return spliterator.tryAdvance(a -> action.accept(a._2));
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
        return IntMap.of(original.takeUntil(p -> predicate.test(p._2)));
    }

    @Override
    public IntMap<T> takeWhile(Predicate<? super T> predicate) {
        return IntMap.of(original.takeWhile(p -> predicate.test(p._2)));
    }

    @Override
    public <T1, T2> Tuple2<Seq<T1>, Seq<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip(unzipper).map(Stream::ofAll, Stream::ofAll);
    }

    @Override
    public <T1, T2, T3> Tuple3<Seq<T1>, Seq<T2>, Seq<T3>> unzip3(Function<? super T, Tuple3<? extends T1, ? extends T2, ? extends T3>> unzipper) {
        Objects.requireNonNull(unzipper, "unzipper is null");
        return iterator().unzip3(unzipper).map(Stream::ofAll, Stream::ofAll, Stream::ofAll);
    }

    @Override
    public <U> Seq<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    @Override
    public <U, R> Seq<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return Stream.ofAll(iterator().zipWith(that, mapper));
    }

    @Override
    public <U> Seq<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Seq<Tuple2<T, Integer>> zipWithIndex() {
        return  zipWithIndex(Tuple::of);
    }

    @Override
    public <U> Seq<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return Stream.ofAll(iterator().zipWithIndex(mapper));
    }
}
