package io.vavr.collection.champ;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.Maps;
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
 * {@link io.vavr.collection.Set} interface.
 *
 * @param <K> the key type of the map
 * @param <V> the value type of the map
 */
public interface VavrMapMixin<K, V, SELF extends VavrMapMixin<K, V, SELF>> extends io.vavr.collection.Map<K, V> {
    long serialVersionUID = 1L;

    /**
     * Creates an empty map of the specified key and value types.
     *
     * @param <L> the key type of the map
     * @param <U> the value type of the map
     * @return a new empty map.
     */
    <L, U> io.vavr.collection.Map<L, U> create();

    /**
     * Creates an empty map of the specified key and value types,
     * and adds all the specified entries.
     *
     * @param entries the entries
     * @param <L>     the key type of the map
     * @param <U>     the value type of the map
     * @return a new map contains the specified entries.
     */
    <L, U> io.vavr.collection.Map<L, U> createFromEntries(Iterable<? extends Tuple2<? extends L, ? extends U>> entries);

    @Override
    default <K2, V2> io.vavr.collection.Map<K2, V2> bimap(Function<? super K, ? extends K2> keyMapper, Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final io.vavr.collection.Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return createFromEntries(entries);
    }

    @Override
    default Tuple2<V, ? extends io.vavr.collection.Map<K, V>> computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return Maps.<K, V, io.vavr.collection.Map<K, V>>computeIfAbsent(this, key, mappingFunction);
    }

    @Override
    default Tuple2<Option<V>, ? extends io.vavr.collection.Map<K, V>> computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return Maps.<K, V, io.vavr.collection.Map<K, V>>computeIfPresent(this, key, remappingFunction);
    }


    @SuppressWarnings("unchecked")
    @Override
    default SELF filter(BiPredicate<? super K, ? super V> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filter(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterNot(BiPredicate<? super K, ? super V> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterNot(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterKeys(Predicate<? super K> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterKeys(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterNotKeys(Predicate<? super K> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterNotKeys(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterValues(Predicate<? super V> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterValues(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterNotValues(Predicate<? super V> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterNotValues(this, this::createFromEntries, predicate);
    }

    @Override
    default <K2, V2> io.vavr.collection.Map<K2, V2> flatMap(BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
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
    default <K2, V2> io.vavr.collection.Map<K2, V2> map(BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(create(), (acc, entry) -> acc.put(entry.map(mapper)));

    }

    @Override
    default <K2> io.vavr.collection.Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        return map((k, v) -> Tuple.of(keyMapper.apply(k), v));
    }

    @Override
    default <K2> io.vavr.collection.Map<K2, V> mapKeys(Function<? super K, ? extends K2> keyMapper, BiFunction<? super V, ? super V, ? extends V> valueMerge) {
        return Collections.mapKeys(this, create(), keyMapper, valueMerge);
    }

    @Override
    default <V2> io.vavr.collection.Map<K, V2> mapValues(Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF merge(io.vavr.collection.Map<? extends K, ? extends V> that) {
        if (that.isEmpty()) {
            return (SELF) this;
        }
        if (isEmpty()) {
            return that.getClass() == this.getClass() ? (SELF) that : (SELF) createFromEntries(that);
        }
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>merge(this, this::createFromEntries, that);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U extends V> SELF merge(io.vavr.collection.Map<? extends K, U> that, BiFunction<? super V, ? super U, ? extends V> collisionResolution) {
        if (that.isEmpty()) {
            return (SELF) this;
        }
        if (isEmpty()) {
            return that.getClass() == this.getClass() ? (SELF) that : (SELF) createFromEntries(that);
        }
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, U, io.vavr.collection.Map<K, V>>merge(this, this::createFromEntries, that, collisionResolution);
    }


    @SuppressWarnings("unchecked")
    @Override
    default SELF put(Tuple2<? extends K, ? extends V> entry) {
        return (SELF) put(entry._1, entry._2);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U extends V> SELF put(K key, U value, BiFunction<? super V, ? super U, ? extends V> merge) {
        return (SELF) Maps.put(this, key, value, merge);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U extends V> SELF put(Tuple2<? extends K, U> entry, BiFunction<? super V, ? super U, ? extends V> merge) {
        return (SELF) Maps.put(this, entry, merge);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF distinct() {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>distinct(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF distinctBy(Comparator<? super Tuple2<K, V>> comparator) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>distinctBy(this, this::createFromEntries, comparator);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U> SELF distinctBy(Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, U, io.vavr.collection.Map<K, V>>distinctBy(this, this::createFromEntries, keyExtractor);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF drop(int n) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>drop(this, this::createFromEntries, this::create, n);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF dropRight(int n) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>dropRight(this, this::createFromEntries, this::create, n);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF dropUntil(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>dropUntil(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF dropWhile(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>dropWhile(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filter(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filter(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filterNot(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>filterNot(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default <C> io.vavr.collection.Map<C, ? extends SELF> groupBy(Function<? super Tuple2<K, V>, ? extends C> classifier) {
        // Type parameters are needed by javac!
        return (io.vavr.collection.Map<C, ? extends SELF>) (io.vavr.collection.Map<C, ?>) Maps.<K, V, C, io.vavr.collection.Map<K, V>>groupBy(this, this::createFromEntries, classifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    default io.vavr.collection.Iterator<? extends SELF> grouped(int size) {
        // Type parameters are needed by javac!
        return (io.vavr.collection.Iterator<? extends SELF>) (io.vavr.collection.Iterator<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>grouped(this, this::createFromEntries, size);
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
    default SELF init() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("init of empty HashMap");
        } else {
            return remove(last()._1);
        }
    }

    @Override
    SELF remove(K key);

    @SuppressWarnings("unchecked")
    @Override
    default Option<? extends SELF> initOption() {
        return (Option<? extends SELF>) (Option<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>initOption(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF orElse(Iterable<? extends Tuple2<K, V>> other) {
        return isEmpty() ? (SELF) createFromEntries(other) : (SELF) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF orElse(Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
        return isEmpty() ? (SELF) createFromEntries(supplier.get()) : (SELF) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    default Tuple2<? extends SELF, ? extends SELF> partition(Predicate<? super Tuple2<K, V>> predicate) {
        // Type parameters are needed by javac!
        return (Tuple2<? extends SELF, ? extends SELF>) (Tuple2<?, ?>) Maps.<K, V, io.vavr.collection.Map<K, V>>partition(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF peek(Consumer<? super Tuple2<K, V>> action) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>peek(this, action);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replace(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>replace(this, currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replaceValue(K key, V value) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>replaceValue(this, key, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replace(K key, V oldValue, V newValue) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>replace(this, key, oldValue, newValue);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>replaceAll(this, function);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replaceAll(Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>replaceAll(this, currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF scan(Tuple2<K, V> zero, BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>scan(this, zero, operation, this::createFromEntries);
    }

    @SuppressWarnings("unchecked")
    @Override
    default io.vavr.collection.Iterator<? extends SELF> slideBy(Function<? super Tuple2<K, V>, ?> classifier) {
        return (io.vavr.collection.Iterator<? extends SELF>) (io.vavr.collection.Iterator<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>slideBy(this, this::createFromEntries, classifier);
    }

    @SuppressWarnings("unchecked")
    @Override
    default io.vavr.collection.Iterator<? extends SELF> sliding(int size) {
        return (io.vavr.collection.Iterator<? extends SELF>) (io.vavr.collection.Iterator<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>sliding(this, this::createFromEntries, size);
    }

    @SuppressWarnings("unchecked")
    @Override
    default io.vavr.collection.Iterator<? extends SELF> sliding(int size, int step) {
        return (io.vavr.collection.Iterator<? extends SELF>) (io.vavr.collection.Iterator<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>sliding(this, this::createFromEntries, size, step);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Tuple2<? extends SELF, ? extends SELF> span(Predicate<? super Tuple2<K, V>> predicate) {
        return (Tuple2<? extends SELF, ? extends SELF>) (Tuple2<?, ?>) Maps.<K, V, io.vavr.collection.Map<K, V>>span(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default Option<? extends SELF> tailOption() {
        return (Option<? extends SELF>) (Option<?>) Maps.<K, V, io.vavr.collection.Map<K, V>>tailOption(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF take(int n) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>take(this, this::createFromEntries, n);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF takeRight(int n) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>takeRight(this, this::createFromEntries, n);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF takeUntil(Predicate<? super Tuple2<K, V>> predicate) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>takeUntil(this, this::createFromEntries, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF takeWhile(Predicate<? super Tuple2<K, V>> predicate) {
        return (SELF) Maps.<K, V, io.vavr.collection.Map<K, V>>takeWhile(this, this::createFromEntries, predicate);
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
