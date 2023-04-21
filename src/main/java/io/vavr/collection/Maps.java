/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.vavr.API.Tuple;

/**
 * INTERNAL: Common {@code Map} functions (not intended to be public).
 * <p>
 * XXX The javadoc states that this class is internal, however when this class
 * is not public, then vavr is not open for extension. Ideally, it should
 * be possible to add new collection implementations to vavr without
 * having to change existing code and packages of vavr.
 */
public final class Maps {

    private Maps() {
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> Tuple2<V, M> computeIfAbsent(M map, K key, Function<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction, "mappingFunction is null");
        final Option<V> value = map.get(key);
        if (value.isDefined()) {
            return Tuple.of(value.get(), map);
        } else {
            final V newValue = mappingFunction.apply(key);
            final M newMap = (M) map.put(key, newValue);
            return Tuple.of(newValue, newMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> Tuple2<Option<V>, M> computeIfPresent(M map, K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        final Option<V> value = map.get(key);
        if (value.isDefined()) {
            final V newValue = remappingFunction.apply(key, value.get());
            final M newMap = (M) map.put(key, newValue);
            return Tuple.of(Option.of(newValue), newMap);
        } else {
            return Tuple.of(Option.none(), map);
        }
    }

    public static <K, V, M extends Map<K, V>> M distinct(M map) {
        return map;
    }

    public static <K, V, M extends Map<K, V>> M distinctBy(M map, OfEntries<K, V, M> ofEntries,
                                                           Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return ofEntries.apply(map.iterator().distinctBy(comparator));
    }

    public static <K, V, U, M extends Map<K, V>> M distinctBy(
            M map, OfEntries<K, V, M> ofEntries, Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return ofEntries.apply(map.iterator().distinctBy(keyExtractor));
    }

    public static <K, V, M extends Map<K, V>> M drop(M map, OfEntries<K, V, M> ofEntries, Supplier<M> emptySupplier, int n) {
        if (n <= 0) {
            return map;
        } else if (n >= map.size()) {
            return emptySupplier.get();
        } else {
            return ofEntries.apply(map.iterator().drop(n));
        }
    }

    public static <K, V, M extends Map<K, V>> M dropRight(M map, OfEntries<K, V, M> ofEntries, Supplier<M> emptySupplier,
                                                          int n) {
        if (n <= 0) {
            return map;
        } else if (n >= map.size()) {
            return emptySupplier.get();
        } else {
            return ofEntries.apply(map.iterator().dropRight(n));
        }
    }

    public static <K, V, M extends Map<K, V>> M dropUntil(M map, OfEntries<K, V, M> ofEntries,
                                                          Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(map, ofEntries, predicate.negate());
    }

    public static <K, V, M extends Map<K, V>> M dropWhile(M map, OfEntries<K, V, M> ofEntries,
                                                          Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return ofEntries.apply(map.iterator().dropWhile(predicate));
    }

    public static <K, V, M extends Map<K, V>> M filter(M map, OfEntries<K, V, M> ofEntries,
                                                       BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(map, ofEntries, t -> predicate.test(t._1, t._2));
    }

    public static <K, V, M extends Map<K, V>> M filter(M map, OfEntries<K, V, M> ofEntries,
                                                       Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return ofEntries.apply(map.iterator().filter(predicate));
    }

    public static <K, V, M extends Map<K, V>> M filterKeys(M map, OfEntries<K, V, M> ofEntries,
                                                           Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(map, ofEntries, t -> predicate.test(t._1));
    }

    public static <K, V, M extends Map<K, V>> M filterValues(M map, OfEntries<K, V, M> ofEntries,
                                                             Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(map, ofEntries, t -> predicate.test(t._2));
    }

    public static <K, V, C, M extends Map<K, V>> Map<C, M> groupBy(M map, OfEntries<K, V, M> ofEntries,
                                                                   Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return Collections.groupBy(map, classifier, ofEntries);
    }

    public static <K, V, M extends Map<K, V>> Iterator<M> grouped(M map, OfEntries<K, V, M> ofEntries, int size) {
        return sliding(map, ofEntries, size, size);
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> Option<M> initOption(M map) {
        return map.isEmpty() ? Option.none() : Option.some((M) map.init());
    }

    public static <K, V, M extends Map<K, V>> M merge(M map, OfEntries<K, V, M> ofEntries,
                                                      Map<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (map.isEmpty()) {
            return ofEntries.apply(Map.narrow(that));
        } else if (that.isEmpty()) {
            return map;
        } else {
            return that.foldLeft(map, (result, entry) -> !result.containsKey(entry._1) ? put(result, entry) : result);
        }
    }

    @SuppressWarnings("unchecked")
    public static <K, V, U extends V, M extends Map<K, V>> M merge(
            M map, OfEntries<K, V, M> ofEntries,
            Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (map.isEmpty()) {
            return ofEntries.apply(Map.narrow(that));
        } else if (that.isEmpty()) {
            return map;
        } else {
            return that.foldLeft(map, (result, entry) -> {
                final K key = entry._1;
                final U value = entry._2;
                final V newValue = result.get(key).map(v -> (V) collisionResolution.apply(v, value)).getOrElse(value);
                return (M) result.put(key, newValue);
            });
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, K, V, M extends Map<K, V>> M ofStream(M map, java.util.stream.Stream<? extends T> stream,
                                                            Function<? super T, ? extends K> keyMapper,
                                                            Function<? super T, ? extends V> valueMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return Stream.ofAll(stream).foldLeft(map, (m, el) -> (M) m.put(keyMapper.apply(el), valueMapper.apply(el)));
    }

    @SuppressWarnings("unchecked")
    public static <T, K, V, M extends Map<K, V>> M ofStream(M map, java.util.stream.Stream<? extends T> stream,
                                                            Function<? super T, Tuple2<? extends K, ? extends V>> entryMapper) {
        Objects.requireNonNull(stream, "stream is null");
        Objects.requireNonNull(entryMapper, "entryMapper is null");
        return Stream.ofAll(stream).foldLeft(map, (m, el) -> (M) m.put(entryMapper.apply(el)));
    }

    public static <K, V, M extends Map<K, V>> Tuple2<M, M> partition(M map, OfEntries<K, V, M> ofEntries,
                                                                     Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<Tuple2<K, V>> left = new java.util.ArrayList<>();
        final java.util.List<Tuple2<K, V>> right = new java.util.ArrayList<>();
        for (Tuple2<K, V> entry : map) {
            (predicate.test(entry) ? left : right).add(entry);
        }
        return Tuple.of(ofEntries.apply(left), ofEntries.apply(right));
    }

    public static <K, V, M extends Map<K, V>> M peek(M map, Consumer<? super Tuple2<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!map.isEmpty()) {
            action.accept(map.head());
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V, U extends V, M extends Map<K, V>> M put(M map, K key, U value,
                                                                 BiFunction<? super V, ? super U, ? extends V> merge) {
        Objects.requireNonNull(merge, "the merge function is null");
        final Option<V> currentValue = map.get(key);
        if (currentValue.isEmpty()) {
            return (M) map.put(key, value);
        } else {
            return (M) map.put(key, merge.apply(currentValue.get(), value));
        }
    }

    @SuppressWarnings("unchecked")
    static <K, V, M extends Map<K, V>> M put(M map, Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return (M) map.put(entry._1, entry._2);
    }

    public static <K, V, U extends V, M extends Map<K, V>> M put(M map, Tuple2<? extends K, U> entry,
                                                                 BiFunction<? super V, ? super U, ? extends V> merge) {
        Objects.requireNonNull(merge, "the merge function is null");
        final Option<V> currentValue = map.get(entry._1);
        if (currentValue.isEmpty()) {
            return put(map, entry);
        } else {
            return put(map, entry.map2(value -> merge.apply(currentValue.get(), value)));
        }
    }

    public static <K, V, M extends Map<K, V>> M filterNot(M map, OfEntries<K, V, M> ofEntries,
                                                          Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(map, ofEntries, predicate.negate());
    }

    public static <K, V, M extends Map<K, V>> M filterNot(M map, OfEntries<K, V, M> ofEntries,
                                                          BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(map, ofEntries, predicate.negate());
    }

    public static <K, V, M extends Map<K, V>> M filterNotKeys(M map, OfEntries<K, V, M> ofEntries,
                                                              Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterKeys(map, ofEntries, predicate.negate());
    }

    public static <K, V, M extends Map<K, V>> M filterNotValues(M map, OfEntries<K, V, M> ofEntries,
                                                                Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filterValues(map, ofEntries, predicate.negate());
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M replace(M map, K key, V oldValue, V newValue) {
        return map.contains(Tuple(key, oldValue)) ? (M) map.put(key, newValue) : map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M replace(M map, Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return (M) (map.containsKey(currentElement._1) ? map.remove(currentElement._1).put(newElement) : map);
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M replaceAll(M map, BiFunction<? super K, ? super V, ? extends V> function) {
        return (M) map.map((k, v) -> Tuple(k, function.apply(k, v)));
    }

    public static <K, V, M extends Map<K, V>> M replaceAll(M map, Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(map, currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M replaceValue(M map, K key, V value) {
        return map.containsKey(key) ? (M) map.put(key, value) : map;
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M scan(M map, Tuple2<K, V> zero,
                                                     BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation,
                                                     Function<Iterator<Tuple2<K, V>>, Traversable<Tuple2<K, V>>> finisher) {
        return (M) Collections.scanLeft(map, zero, operation, finisher);
    }

    public static <K, V, M extends Map<K, V>> Iterator<M> slideBy(M map, OfEntries<K, V, M> ofEntries,
                                                                  Function<? super Tuple2<K, V>, ?> classifier) {
        return map.iterator().slideBy(classifier).map(ofEntries);
    }

    public static <K, V, M extends Map<K, V>> Iterator<M> sliding(M map, OfEntries<K, V, M> ofEntries, int size) {
        return sliding(map, ofEntries, size, 1);
    }

    public static <K, V, M extends Map<K, V>> Iterator<M> sliding(M map, OfEntries<K, V, M> ofEntries, int size, int step) {
        return map.iterator().sliding(size, step).map(ofEntries);
    }

    public static <K, V, M extends Map<K, V>> Tuple2<M, M> span(M map, OfEntries<K, V, M> ofEntries,
                                                                Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = map.iterator().span(predicate);
        return Tuple.of(ofEntries.apply(t._1), ofEntries.apply(t._2));
    }

    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> Option<M> tailOption(M map) {
        return map.isEmpty() ? Option.none() : Option.some((M) map.tail());
    }

    public static <K, V, M extends Map<K, V>> M take(M map, OfEntries<K, V, M> ofEntries, int n) {
        if (n >= map.size()) {
            return map;
        } else {
            return ofEntries.apply(map.iterator().take(n));
        }
    }

    public static <K, V, M extends Map<K, V>> M takeRight(M map, OfEntries<K, V, M> ofEntries, int n) {
        if (n >= map.size()) {
            return map;
        } else {
            return ofEntries.apply(map.iterator().takeRight(n));
        }
    }

    public static <K, V, M extends Map<K, V>> M takeUntil(M map, OfEntries<K, V, M> ofEntries,
                                                          Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(map, ofEntries, predicate.negate());
    }

    public static <K, V, M extends Map<K, V>> M takeWhile(M map, OfEntries<K, V, M> ofEntries,
                                                          Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final M taken = ofEntries.apply(map.iterator().takeWhile(predicate));
        return taken.size() == map.size() ? map : taken;
    }

    @FunctionalInterface
    public interface OfEntries<K, V, M extends Map<K, V>> extends Function<Iterable<Tuple2<K, V>>, M> {
    }
}
