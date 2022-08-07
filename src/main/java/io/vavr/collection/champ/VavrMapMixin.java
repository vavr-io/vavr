package io.vavr.collection.champ;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.Iterator;
import io.vavr.collection.Map;
import io.vavr.collection.Maps;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This mixin-interface defines a {@link #create} method and a {@link #createFromEntries}
 * method, and provides default implementations for methods defined in the
 * {@link Set} interface.
 *
 * @param <K> the key type of the map
 * @param <V> the value type of the map
 */
interface VavrMapMixin<K, V> extends Map<K, V> {
    long serialVersionUID = 1L;

    /**
     * Creates an empty map of the specified key and value types.
     *
     * @param <L> the key type of the map
     * @param <U> the value type of the map
     * @return a new empty map.
     */
    <L, U> Map<L, U> create();

    /**
     * Creates an empty map of the specified key and value types,
     * and adds all the specified entries.
     *
     * @param entries the entries
     * @param <L>     the key type of the map
     * @param <U>     the value type of the map
     * @return a new map contains the specified entries.
     */
    <L, U> Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries);

    @Override
    default <K2, V2> Map<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return createFromEntries(entries);
    }

    @Override
    default Tuple2<V, ? extends Map<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Maps.<K, V, Map<K, V>>computeIfAbsent(this, key, mappingFunction);
    }

    @Override
    default Tuple2<Option<V>, ? extends Map<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Maps.<K, V, Map<K, V>>computeIfPresent(this, key, remappingFunction);
    }


    @Override
    default Map<K, V> filter(BiPredicate<? super K, ? super V> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filter(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterNot(BiPredicate<? super K, ? super V> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterNot(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterKeys(Predicate<? super K> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterKeys(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterNotKeys(Predicate<? super K> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterNotKeys(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterValues(Predicate<? super V> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterValues(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterNotValues(Predicate<? super V> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterNotValues(this, this::createFromEntries, predicate);
    }

    @Override
    default <K2, V2> Map<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(create(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    default V getOrElse(K key, V defaultValue) {
        return get(key).getOrElse(defaultValue);
    }

    @Override
    default Tuple2<K, V> last() {
        return Collections.last(this);
    }

    @Override
    default <K2, V2> Map<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(create(), (acc, entry) -> acc.put(entry.map(mapper)));

    }

    @Override
    default <K2> Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return map((k, v) -> Tuple.of(keyMapper.apply(k), v));
    }

    @Override
    default <K2> Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
        return Collections.mapKeys(this, create(), keyMapper, valueMerge);
    }

    @Override
    default <V2> Map<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    default Map<K, V> merge(Map<? extends K, ? extends V> that) {
        if (that.isEmpty()) {
            return this;
        }
        if (isEmpty()) {
            return (Map<K, V>) that;
        }
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>merge(this, this::createFromEntries, that);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U extends V> Map<K, V> merge(Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        if (that.isEmpty()) {
            return this;
        }
        if (isEmpty()) {
            return (Map<K, V>) that;
        }
        // Type parameters are needed by javac!
        return Maps.<K, V, U, Map<K, V>>merge(this, this::createFromEntries, that, collisionResolution);
    }


    @Override
    default Map<K, V> put(Tuple2<? extends K, ? extends V> entry) {
        return put(entry._1, entry._2);
    }

    @Override
    default <U extends V> Map<K, V> put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, key, value, merge);
    }

    @Override
    default <U extends V> Map<K, V> put(Tuple2<? extends K, U> entry, BiFunction<? super V, ? super U, ? extends V> merge) {
        return Maps.put(this, entry, merge);
    }


    @Override
    default Map<K, V> distinct() {
        return Maps.<K, V, Map<K, V>>distinct(this);
    }

    @Override
    default Map<K, V> distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>distinctBy(this, this::createFromEntries, comparator);
    }

    @Override
    default <U> Map<K, V> distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        // Type parameters are needed by javac!
        return Maps.<K, V, U, Map<K, V>>distinctBy(this, this::createFromEntries, keyExtractor);
    }

    @Override
    default Map<K, V> drop(int n) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>drop(this, this::createFromEntries, this::create, n);
    }

    @Override
    default Map<K, V> dropRight(int n) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>dropRight(this, this::createFromEntries, this::create, n);
    }

    @Override
    default Map<K, V> dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>dropUntil(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>dropWhile(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filter(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filter(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> filterNot(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>filterNot(this, this::createFromEntries, predicate);
    }

    @Override
    default <C> Map<C, ? extends Map<K, V>> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        // Type parameters are needed by javac!
        return Maps.<K, V, C, Map<K, V>>groupBy(this, this::createFromEntries, classifier);
    }

    @Override
    default Iterator<? extends Map<K, V>> grouped(int size) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>grouped(this, this::createFromEntries, size);
    }

    @Override
    default Tuple2<K, V> head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head of empty HashMap");
        } else {
            return iterator().next();
        }
    }

    @Override
    default Map<K, V> init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty HashMap");
        } else {
            return remove(last()._1);
        }
    }

    @Override
    default Option<? extends Map<K, V>> initOption() {
        return Maps.<K, V, Map<K, V>>initOption(this);
    }

    @Override
    default Map<K, V> orElse(Iterable<? extends Tuple2<K, V>> other) {
        return isEmpty() ? createFromEntries(other) : this;
    }

    @Override
    default Map<K, V> orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
        return isEmpty() ? createFromEntries(supplier.get()) : this;
    }

    @Override
    default Tuple2<? extends Map<K, V>, ? extends Map<K, V>> partition(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return Maps.<K, V, Map<K, V>>partition(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> peek(Consumer<? super Tuple2<K, V>> action) {
        return Maps.<K, V, Map<K, V>>peek(this, action);
    }

    @Override
    default Map<K, V> replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return Maps.<K, V, Map<K, V>>replace(this, currentElement, newElement);
    }

    @Override
    default Map<K, V> replaceValue(K key, V value) {
        return Maps.<K, V, Map<K, V>>replaceValue(this, key, value);
    }

    @Override
    default Map<K, V> replace(K key, V oldValue, V newValue) {
        return Maps.<K, V, Map<K, V>>replace(this, key, oldValue, newValue);
    }

    @Override
    default Map<K, V> replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        return Maps.<K, V, Map<K, V>>replaceAll(this, function);
    }

    @Override
    default Map<K, V> replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return Maps.<K, V, Map<K, V>>replaceAll(this, currentElement, newElement);
    }


    @Override
    default Map<K, V> scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        return Maps.<K, V, Map<K, V>>scan(this, zero, operation, this::createFromEntries);
    }

    @Override
    default Iterator<? extends Map<K, V>> slideBy(Function<? super Tuple2<K, V>, ?> classifier) {
        return Maps.<K, V, Map<K, V>>slideBy(this, this::createFromEntries, classifier);
    }

    @Override
    default Iterator<? extends Map<K, V>> sliding(int size) {
        return Maps.<K, V, Map<K, V>>sliding(this, this::createFromEntries, size);
    }

    @Override
    default Iterator<? extends Map<K, V>> sliding(int size, int step) {
        return Maps.<K, V, Map<K, V>>sliding(this, this::createFromEntries, size, step);
    }

    @Override
    default Tuple2<? extends Map<K, V>, ? extends Map<K, V>> span(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.<K, V, Map<K, V>>span(this, this::createFromEntries, predicate);
    }

    @Override
    default Option<? extends Map<K, V>> tailOption() {
        return Maps.<K, V, Map<K, V>>tailOption(this);
    }

    @Override
    default Map<K, V> take(int n) {
        return Maps.<K, V, Map<K, V>>take(this, this::createFromEntries, n);
    }

    @Override
    default Map<K, V> takeRight(int n) {
        return Maps.<K, V, Map<K, V>>takeRight(this, this::createFromEntries, n);
    }

    @Override
    default Map<K, V> takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.<K, V, Map<K, V>>takeUntil(this, this::createFromEntries, predicate);
    }

    @Override
    default Map<K, V> takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return Maps.<K, V, Map<K, V>>takeWhile(this, this::createFromEntries, predicate);
    }

    @Override
    default boolean isAsync() {
        return false;
    }

    @Override
    default boolean isLazy() {
        return false;
    }

    @Override
    default String stringPrefix() {
        return getClass().getSimpleName();
    }
}
