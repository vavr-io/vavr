/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.API;
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

public class IntMultimap<T> implements Traversable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final Multimap<Integer, T> original;

    public static <T> IntMultimap<T> of(Multimap<Integer, T> original) {
        return new IntMultimap<>(original);
    }

    private IntMultimap(Multimap<Integer, T> original) {
        this.original = original;
    }

    @Override
    public int hashCode() {
        return original.values().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof IntMultimap) {
            final IntMultimap<?> that = (IntMultimap<?>) o;
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
    public IntMultimap<T> distinct() {
        return IntMultimap.of(original.distinct());
    }

    @Override
    public IntMultimap<T> distinctBy(Comparator<? super T> comparator) {
        return IntMultimap.of(original.distinctBy((o1, o2) -> comparator.compare(o1._2, o2._2)));
    }

    @Override
    public <U> IntMultimap<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return IntMultimap.of(original.distinctBy(f -> keyExtractor.apply(f._2)));
    }

    @Override
    public IntMultimap<T> drop(long n) {
        final Multimap<Integer, T> dropped = original.drop(n);
        return dropped == original ? this : IntMultimap.of(dropped);
    }

    @Override
    public IntMultimap<T> dropRight(long n) {
        final Multimap<Integer, T> dropped = original.dropRight(n);
        return dropped == original ? this : IntMultimap.of(dropped);
    }

    @Override
    public IntMultimap<T> dropUntil(Predicate<? super T> predicate) {
        return IntMultimap.of(original.dropUntil(p -> predicate.test(p._2)));
    }

    @Override
    public IntMultimap<T> dropWhile(Predicate<? super T> predicate) {
        return IntMultimap.of(original.dropWhile(p -> predicate.test(p._2)));
    }

    @Override
    public IntMultimap<T> filter(Predicate<? super T> predicate) {
        return IntMultimap.of(original.filter(p -> predicate.test(p._2)));
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
    public <C> Map<C, ? extends IntMultimap<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return original.groupBy(e -> classifier.apply(e._2)).map((k, v) -> Tuple.of(k, IntMultimap.of(v)));
    }

    @Override
    public Iterator<IntMultimap<T>> grouped(long size) {
        return original.grouped(size).map(IntMultimap::of);
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
    public IntMultimap<T> init() {
        return IntMultimap.of(original.init());
    }

    @Override
    public Option<? extends IntMultimap<T>> initOption() {
        return original.initOption().map(IntMultimap::of);
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
    public Tuple2<IntMultimap<T>, IntMultimap<T>> partition(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return original.partition(p -> predicate.test(p._2)).map(IntMultimap::of, IntMultimap::of);
    }

    @Override
    public IntMultimap<T> peek(Consumer<? super T> action) {
        return IntMultimap.of(original.peek(e -> action.accept(e._2)));
    }

    @Override
    public IntMultimap<T> replace(T currentElement, T newElement) {
        final Option<Tuple2<Integer, T>> currentEntryOpt = original.find(e -> e._2.equals(currentElement));
        if (currentEntryOpt.isDefined()) {
            final Tuple2<Integer, T> currentEntry = currentEntryOpt.get();
            return IntMultimap.of(original.replace(currentEntry, Tuple.of(original.size() + 1, newElement)));
        } else {
            return this;
        }
    }

    @Override
    public IntMultimap<T> replaceAll(T currentElement, T newElement) {
        Multimap<Integer, T> result = original;
        for (Tuple2<Integer, T> entry : original.filter(e -> e._2.equals(currentElement))) {
            result = result.replaceAll(entry, Tuple.of(entry._1, newElement));
        }
        return IntMultimap.of(result);
    }

    @Override
    public IntMultimap<T> retainAll(Iterable<? extends T> elements) {
        final Set<T> elementsSet = HashSet.ofAll(elements);
        return IntMultimap.of(original.retainAll(original.filter(e -> elementsSet.contains(e._2))));
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
    public Iterator<IntMultimap<T>> sliding(long size) {
        return original.sliding(size).map(IntMultimap::of);
    }

    @Override
    public Iterator<IntMultimap<T>> sliding(long size, long step) {
        return original.sliding(size, step).map(IntMultimap::of);
    }

    @Override
    public Tuple2<? extends IntMultimap<T>, ? extends IntMultimap<T>> span(Predicate<? super T> predicate) {
        return original.span(p -> predicate.test(p._2)).map(IntMultimap::of, IntMultimap::of);
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
    public IntMultimap<T> tail() {
        return IntMultimap.of(original.tail());
    }

    @Override
    public Option<IntMultimap<T>> tailOption() {
        return original.tailOption().map(IntMultimap::of);
    }

    @Override
    public IntMultimap<T> take(long n) {
        return IntMultimap.of(original.take(n));
    }

    @Override
    public IntMultimap<T> takeRight(long n) {
        return IntMultimap.of(original.takeRight(n));
    }

    @Override
    public Traversable<T> takeUntil(Predicate<? super T> predicate) {
        return IntMultimap.of(original.takeUntil(p -> predicate.test(p._2)));
    }

    @Override
    public IntMultimap<T> takeWhile(Predicate<? super T> predicate) {
        return IntMultimap.of(original.takeWhile(p -> predicate.test(p._2)));
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
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zip(that));
    }

    @Override
    public <U> Seq<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return Stream.ofAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    public Seq<Tuple2<T, Long>> zipWithIndex() {
        return Stream.ofAll(iterator().zipWithIndex());
    }
}
