/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
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

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.*;
import org.jspecify.annotations.NonNull;

import static io.vavr.API.Tuple;

/**
 * An abstract base implementation of the {@link Multimap} interface that provides
 * common functionality for concrete Multimap implementations. This class is not
 * intended to be public and serves as a foundation for specialized Multimap types.
 *
 * @param <K> Key type
 * @param <V> Value type
 * @param <M> Concrete Multimap type extending this class
 * @author Ruslan Sennov
 */
abstract class AbstractMultimap<K, V, M extends Multimap<K, V>> implements Multimap<K, V> {

    private static final long serialVersionUID = 1L;

    protected final Map<K, Traversable<V>> back;
    protected final SerializableSupplier<Traversable<?>> emptyContainer;
    private final ContainerType containerType;

    /**
     * Creates a new AbstractMultimap with the specified backing map, container type, and empty container supplier.
     *
     * @param back           The backing map that stores the key-value pairs
     * @param containerType  The type of container used to store multiple values for a key
     * @param emptyContainer A supplier that creates empty containers for new key entries
     */
    AbstractMultimap(Map<K, Traversable<V>> back, ContainerType containerType, SerializableSupplier<Traversable<?>> emptyContainer) {
        this.back = back;
        this.containerType = containerType;
        this.emptyContainer = emptyContainer;
    }

    /**
     * Returns an empty Map instance specific to the implementing class.
     *
     * @param <K2> Key type of the empty map
     * @param <V2> Value type of the empty map
     * @return An empty Map instance
     */
    protected abstract <K2, V2> Map<K2, V2> emptyMapSupplier();

    /**
     * Returns an empty Multimap instance specific to the implementing class.
     *
     * @param <K2> Key type of the empty multimap
     * @param <V2> Value type of the empty multimap
     * @return An empty Multimap instance
     */
    protected abstract <K2, V2> Multimap<K2, V2> emptyInstance();

    /**
     * Creates a new Multimap instance from the given backing map.
     *
     * @param <K2> Key type of the new multimap
     * @param <V2> Value type of the new multimap
     * @param back The backing map to create the multimap from
     * @return A new Multimap instance containing the entries from the backing map
     */
    protected abstract <K2, V2> Multimap<K2, V2> createFromMap(Map<K2, Traversable<V2>> back);

    /**
     * Creates a new Multimap from the given entries by grouping values by their keys.
     * For each key, a new container is created using the emptyContainer supplier,
     * and values are added using the containerType's add operation.
     *
     * @param <K2>    Key type of the new multimap
     * @param <V2>    Value type of the new multimap
     * @param entries The entries to create the multimap from
     * @return A new Multimap containing all the entries with values grouped by keys
     */
    @SuppressWarnings("unchecked")
    private <K2, V2> Multimap<K2, V2> createFromEntries(Iterable<? extends Tuple2<? extends K2, ? extends V2>> entries) {
        Map<K2, Traversable<V2>> back = emptyMapSupplier();
        for (Tuple2<? extends K2, ? extends V2> entry : entries) {
            if (back.containsKey(entry._1)) {
                back = back.put(entry._1, containerType.add(back.get(entry._1).get(), entry._2));
            } else {
                back = back.put(entry._1, containerType.add(emptyContainer.get(), entry._2));
            }
        }
        return createFromMap(back);
    }

    @Override
    public Map<K, Traversable<V>> asMap() {
        return back;
    }

    @Override
    public <K2, V2> Multimap<K2, V2> bimap(@NonNull Function<? super K, ? extends K2> keyMapper, @NonNull Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(keyMapper, "keyMapper is null");
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        final Iterator<Tuple2<K2, V2>> entries = iterator().map(entry -> Tuple.of(keyMapper.apply(entry._1), valueMapper.apply(entry._2)));
        return createFromEntries(entries);
    }

    @Override
    public boolean containsKey(K key) {
        return back.containsKey(key);
    }

    @Override
    public ContainerType getContainerType() {
        return containerType;
    }

    @Override
    public <K2, V2> Multimap<K2, V2> flatMap(@NonNull BiFunction<? super K, ? super V, ? extends Iterable<Tuple2<K2, V2>>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.emptyInstance(), (acc, entry) -> {
            for (Tuple2<? extends K2, ? extends V2> mappedEntry : mapper.apply(entry._1, entry._2)) {
                acc = acc.put(mappedEntry);
            }
            return acc;
        });
    }

    @Override
    public Option<Traversable<V>> get(K key) {
        return back.get(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Traversable<V> getOrElse(K key, Traversable<? extends V> defaultValue) {
        return back.getOrElse(key, (Traversable<V>) defaultValue);
    }

    @Override
    public Set<K> keySet() {
        return back.keySet();
    }

    @Override
    public <K2, V2> Multimap<K2, V2> map(@NonNull BiFunction<? super K, ? super V, Tuple2<K2, V2>> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return foldLeft(this.emptyInstance(), (acc, entry) -> acc.put(mapper.apply(entry._1, entry._2)));
    }

    @Override
    public <V2> Multimap<K, V2> mapValues(@NonNull Function<? super V, ? extends V2> valueMapper) {
        Objects.requireNonNull(valueMapper, "valueMapper is null");
        return map((k, v) -> Tuple.of(k, valueMapper.apply(v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M put(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse((Traversable<V>) emptyContainer.get());
        final Traversable<V> newValues = containerType.add(values, value);
        return (M) (newValues == values ? this : createFromMap(back.put(key, newValues)));
    }

    @Override
    public M put(@NonNull Tuple2<? extends K, ? extends V> entry) {
        Objects.requireNonNull(entry, "entry is null");
        return put(entry._1, entry._2);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key) {
        return (M) (back.containsKey(key) ? createFromMap(back.remove(key)) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M remove(K key, V value) {
        final Traversable<V> values = back.get(key).getOrElse((Traversable<V>) emptyContainer.get());
        final Traversable<V> newValues = containerType.remove(values, value);
        if (newValues == values) {
            return (M) this;
        } else if (newValues.isEmpty()) {
            return (M) createFromMap(back.remove(key));
        } else {
            return (M) createFromMap(back.put(key, newValues));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public M removeAll(@NonNull Iterable<? extends K> keys) {
        final Map<K, Traversable<V>> result = back.removeAll(keys);
        return (M) (result == back ? this : createFromMap(result));
    }

    @Override
    public int size() {
        return back.foldLeft(0, (s, t) -> s + t._2.size());
    }

    @Override
    public Traversable<V> values() {
        return Iterator.concat(back.values()).toStream();
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinct() {
        return (M) (containerType == ContainerType.SEQ ? createFromEntries(iterator().distinct()) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M distinctBy(@NonNull Comparator<? super Tuple2<K, V>> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return (M) (isEmpty() ? this : createFromEntries(iterator().distinctBy(comparator)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> M distinctBy(@NonNull Function<? super Tuple2<K, V>, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return (M) (isEmpty() ? this : createFromEntries(iterator().distinctBy(keyExtractor)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public M drop(int n) {
        if (n <= 0 || isEmpty()) {
            return (M) this;
        } else if (n >= length()) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().drop(n));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public M dropRight(int n) {
        if (n <= 0 || isEmpty()) {
            return (M) this;
        } else if (n >= length()) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().dropRight(n));
        }
    }

    @Override
    public M dropUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M dropWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return (M) (isEmpty() ? this : createFromEntries(iterator().dropWhile(predicate)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M filter(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        if (isEmpty()) {
            return (M) this;
        } else {
            return (M) createFromEntries(iterator().filter(predicate));
        }
    }

    @Override
    public M reject(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }

    @Override
    public M filter(@NonNull BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._1, t._2));
    }

    @Override
    public M reject(@NonNull BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return reject(t -> predicate.test(t._1, t._2));
    }

    @Override
    public M filterKeys(@NonNull Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._1));
    }

    @Override
    public M rejectKeys(@NonNull Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return reject(t -> predicate.test(t._1));
    }

    @Override
    public M filterValues(@NonNull Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(t -> predicate.test(t._2));
    }

    @Override
    public M rejectValues(@NonNull Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return reject(t -> predicate.test(t._2));
    }

    @Override
    @Deprecated
    public M removeAll(@NonNull BiPredicate<? super K, ? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return reject(predicate);
    }

    @Override
    @Deprecated
    public M removeKeys(@NonNull Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return rejectKeys(predicate);
    }

    @Override
    @Deprecated
    public M removeValues(@NonNull Predicate<? super V> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return rejectValues(predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> Map<C, M> groupBy(@NonNull Function<? super Tuple2<K, V>, ? extends C> classifier) {
        return (Map<C, M>) Collections.groupBy(this, classifier, this::createFromEntries);
    }

    @Override
    public Iterator<M> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    public M init() {
        if (back.isEmpty()) {
            throw new UnsupportedOperationException("init of empty HashMap");
        } else {
            final Tuple2<K, V> last = last();
            return remove(last._1, last._2);
        }
    }

    @Override
    public Tuple2<K, V> head() {
        final Tuple2<K, Traversable<V>> head = back.head();
        return Tuple.of(head._1, head._2.head());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Option<M> initOption() {
        return isEmpty() ? Option.none() : Option.some(init());
    }

    @Override
    public boolean isAsync() {
        return back.isAsync();
    }

    @Override
    public boolean isEmpty() {
        return back.isEmpty();
    }

    @Override
    public boolean isLazy() {
        return back.isLazy();
    }

    @Override
    public @NonNull Iterator<Tuple2<K, V>> iterator() {
        if (containerType == ContainerType.SORTED_SET) {
            return back.iterator().flatMap(t -> t._2.iterator().map(v -> Tuple.of(t._1, v)));
        } else {
            return back.iterator().flatMap(t -> t._2.map(v -> Tuple.of(t._1, v)));
        }
    }

    @Override
    public Tuple2<K, V> last() {
        final Tuple2<K, Traversable<V>> last = back.last();
        return Tuple.of(last._1, last._2.last());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M merge(@NonNull Multimap<? extends K, ? extends V> that) {
        Objects.requireNonNull(that, "that is null");
        if (isEmpty()) {
            return (M) createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            return that.foldLeft((M) this, (map, entry) -> (M) map.put(entry));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K2 extends K, V2 extends V> M merge(@NonNull Multimap<K2, V2> that, @NonNull BiFunction<Traversable<V>, Traversable<V2>, Traversable<V>> collisionResolution) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(collisionResolution, "collisionResolution is null");
        if (isEmpty()) {
            return (M) createFromEntries(that);
        } else if (that.isEmpty()) {
            return (M) this;
        } else {
            final Map<K, Traversable<V>> result = that.keySet().foldLeft(this.back, (map, key) -> {
                final Traversable<V> thisValues = map.get(key).getOrElse((Traversable<V>) emptyContainer.get());
                final Traversable<V2> thatValues = that.get(key).get();
                final Traversable<V> newValues = collisionResolution.apply(thisValues, thatValues);
                return map.put(key, newValues);
            });
            return (M) createFromMap(result);
        }
    }

    /**
     * Returns this {@code Multimap} if it is nonempty,
     * otherwise {@code Multimap} created from iterable, using existing multimap properties.
     *
     * @param other An alternative {@code Traversable}
     * @return this {@code Multimap} if it is nonempty,
     * otherwise {@code Multimap} created from iterable, using existing multimap properties.
     */
    @SuppressWarnings("unchecked")
    @Override
    public M orElse(Iterable<? extends Tuple2<K, V>> other) {
        return isEmpty() ? (M) createFromEntries(other) : (M) this;
    }

    /**
     * Returns this {@code Multimap} if it is nonempty,
     * otherwise {@code Multimap} created from result of evaluating supplier, using existing multimap properties.
     *
     * @param supplier An alternative {@code Traversable}
     * @return this {@code Multimap} if it is nonempty,
     * otherwise {@code Multimap} created from result of evaluating supplier, using existing multimap properties.
     */
    @SuppressWarnings("unchecked")
    @Override
    public M orElse(@NonNull Supplier<? extends Iterable<? extends Tuple2<K, V>>> supplier) {
        return isEmpty() ? (M) createFromEntries(supplier.get()) : (M) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<M, M> partition(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final java.util.List<Tuple2<K, V>> left = new java.util.ArrayList<>();
        final java.util.List<Tuple2<K, V>> right = new java.util.ArrayList<>();
        for (Tuple2<K, V> entry : this) {
            (predicate.test(entry) ? left : right).add(entry);
        }
        return Tuple.of((M) createFromEntries(left), (M) createFromEntries(right));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M peek(@NonNull Consumer<? super Tuple2<K, V>> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(head());
        }
        return (M) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replace(@NonNull Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        Objects.requireNonNull(currentElement, "currentElement is null");
        Objects.requireNonNull(newElement, "newElement is null");
        return (M) (containsKey(currentElement._1) ? remove(currentElement._1, currentElement._2).put(newElement) : this);
    }

    @Override
    public M replaceAll(@NonNull Tuple2<K, V> currentElement, Tuple2<K, V> newElement) {
        return replace(currentElement, newElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replaceValue(K key, V value) {
        return (M) (containsKey(key) ? remove(key).put(key, value) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replace(K key, V oldValue, V newValue) {
        return (M) (contains(Tuple(key, oldValue)) ? remove(key, oldValue).put(key, newValue) : this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public M replaceAll(@NonNull BiFunction<? super K, ? super V, ? extends V> function) {
        return (M) map((k, v) -> Tuple(k, function.apply(k, v)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M retainAll(@NonNull Iterable<? extends Tuple2<K, V>> elements) {
        Objects.requireNonNull(elements, "elements is null");
        return (M) createFromEntries(back.flatMap(t -> t._2.map(v -> Tuple.of(t._1, v))).retainAll(elements));
    }

    @SuppressWarnings("unchecked")
    @Override
    public M scan(Tuple2<K, V> zero, @NonNull BiFunction<? super Tuple2<K, V>, ? super Tuple2<K, V>, ? extends Tuple2<K, V>> operation) {
        return (M) Collections.scanLeft(this, zero, operation, this::createFromEntries);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<M> slideBy(@NonNull Function<? super Tuple2<K, V>, ?> classifier) {
        return (Iterator<M>) iterator().slideBy(classifier).map(this::createFromEntries);
    }

    @Override
    public Iterator<M> sliding(int size) {
        return sliding(size, 1);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<M> sliding(int size, int step) {
        return (Iterator<M>) iterator().sliding(size, step).map(this::createFromEntries);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple2<M, M> span(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<Tuple2<K, V>>, Iterator<Tuple2<K, V>>> t = iterator().span(predicate);
        return Tuple.of((M) createFromEntries(t._1), (M) createFromEntries(t._2));
    }

    @Override
    public M tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail of empty Multimap");
        } else {
            final Tuple2<K, V> head = head();
            return remove(head._1, head._2);
        }
    }

    @Override
    public Option<M> tailOption() {
        return isEmpty() ? Option.none() : Option.some(tail());
    }

    @Override
    @SuppressWarnings("unchecked")
    public M take(int n) {
        if (isEmpty() || n >= length()) {
            return (M) this;
        } else if (n <= 0) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().take(n));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public M takeRight(int n) {
        if (isEmpty() || n >= length()) {
            return (M) this;
        } else if (n <= 0) {
            return (M) this.emptyInstance();
        } else {
            return (M) createFromEntries(iterator().takeRight(n));
        }
    }

    @Override
    public M takeUntil(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    public M takeWhile(@NonNull Predicate<? super Tuple2<K, V>> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Multimap<K, V> taken = createFromEntries(iterator().takeWhile(predicate));
        return (M) (taken.length() == length() ? this : taken);
    }

    @Override
    public boolean equals(Object o) {
        return Collections.equals(this, o);
    }

    @Override
    public int hashCode() {
        return back.hashCode();
    }

    @Override
    public String stringPrefix() {
        return getClass().getSimpleName() + "[" + emptyContainer.get().stringPrefix() + "]";
    }

    @Override
    public String toString() {
        return mkString(stringPrefix() + "(", ", ", ")");
    }

    @Override
    public java.util.Map<K, Collection<V>> toJavaMap() {
        return toJavaMap(new java.util.HashMap<>());
    }

    protected <JM extends java.util.Map<K, Collection<V>>> JM toJavaMap(JM javaMap) {
        for (Tuple2<K, V> t : this) {
            javaMap.computeIfAbsent(t._1, k -> containerType.instantiate()).add(t._2);
        }
        return javaMap;
    }


    interface SerializableSupplier<T> extends Supplier<T>, Serializable {
    }
}
