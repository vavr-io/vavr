package io.vavr.collection;

import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This mixin-interface defines a {@link #clear} method and a {@link #setAll}
 * method, and provides default implementations for methods defined in the
 * {@link Set} interface.
 *
 * @param <T> the element type of the set
 */
interface SetMixin<T> extends Set<T> {
    long serialVersionUID = 0L;

    /**
     * Creates an empty set of the specified element type.
     *
     * @param <R> the element type
     * @return a new empty set.
     */
    <R> Set<R> clear();

    /**
     * Creates an empty set of the specified element type, and adds all
     * the specified elements.
     *
     * @param elements the elements
     * @param <R>      the element type
     * @return a new instance that contains the specified elements.
     */
    <R> Set<R> setAll(Iterable<? extends R> elements);

    @Override
    default <R> Set<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
        return setAll(iterator().<R>collect(partialFunction));
    }

    @Override
    default Set<T> diff(Set<? extends T> that) {
        return removeAll(that);
    }

    @Override
    default Set<T> distinct() {
        return this;
    }

    @Override
    default Set<T> distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return setAll(iterator().distinctBy(comparator));
    }

    @Override
    default <U> Set<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return setAll(iterator().distinctBy(keyExtractor));
    }

    @Override
    default Set<T> drop(int n) {
        if (n <= 0) {
            return this;
        }
        return setAll(iterator().drop(n));
    }


    @Override
    default Set<T> dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @Override
    default Set<T> dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Set<T> dropped = setAll(iterator().dropWhile(predicate));
        return dropped.length() == length() ? this : dropped;
    }

    @Override
    default Set<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Set<T> filtered = setAll(iterator().filter(predicate));

        if (filtered.isEmpty()) {
            return clear();
        } else if (filtered.length() == length()) {
            return this;
        } else {
            return filtered;
        }
    }

    @Override
    default Set<T> tail() {
        // XXX Traversable.tail() specifies that we must throw
        //     UnsupportedOperationException instead of
        //     NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return remove(iterator().next());
    }

    @Override
    default <U> Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Set<U> flatMapped = this.clear();
        for (T t : this) {
            for (U u : mapper.apply(t)) {
                flatMapped = flatMapped.add(u);
            }
        }
        return flatMapped;
    }

    @Override
    default <U> Set<U> map(Function<? super T, ? extends U> mapper) {
        Set<U> mapped = this.clear();
        for (T t : this) {
            mapped = mapped.add(mapper.apply(t));
        }
        return mapped;
    }

    @Override
    default Set<T> filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }


    @Override
    default <C> Map<C, ? extends Set<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, this::setAll);
    }

    @Override
    default Iterator<? extends Set<T>> grouped(int size) {
        return sliding(size, size);
    }

    @Override
    default boolean hasDefiniteSize() {
        return true;
    }

    @Override
    default T head() {
        return iterator().next();
    }


    @Override
    default Option<? extends Set<T>> initOption() {
        return tailOption();
    }

    @Override
    default Set<T> intersect(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return clear();
        } else {
            final int size = size();
            if (size <= elements.size()) {
                return retainAll(elements);
            } else {
                final Set<T> results = this.<T>setAll(elements).retainAll(this);
                return (size == results.size()) ? this : results;
            }
        }
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
    default boolean isTraversableAgain() {
        return true;
    }

    @Override
    default T last() {
        return Collections.last(this);
    }

    @Override
    default Set<T> orElse(Iterable<? extends T> other) {
        return isEmpty() ? setAll(other) : this;
    }

    @Override
    default Set<T> orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? setAll(supplier.get()) : this;
    }

    @Override
    default Tuple2<? extends Set<T>, ? extends Set<T>> partition(Predicate<? super T> predicate) {
        return Collections.partition(this, this::setAll, predicate);
    }

    @Override
    default Set<T> peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(iterator().head());
        }
        return this;
    }

    @Override
    default Set<T> removeAll(Iterable<? extends T> elements) {
        return Collections.removeAll(this, elements);
    }

    @Override
    default Set<T> replace(T currentElement, T newElement) {
        if (contains(currentElement)) {
            return remove(currentElement).add(newElement);
        } else {
            return this;
        }
    }

    @Override
    default Set<T> replaceAll(T currentElement, T newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    default Set<T> retainAll(Iterable<? extends T> elements) {
        return Collections.retainAll(this, elements);
    }

    @Override
    default Set<T> scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return scanLeft(zero, operation);
    }

    @Override
    default <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, this::setAll);
    }

    @Override
    default <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, this::setAll);
    }

    @Override
    default Iterator<? extends Set<T>> slideBy(Function<? super T, ?> classifier) {
        return iterator().slideBy(classifier).map(this::setAll);
    }

    @Override
    default Iterator<? extends Set<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Iterator<? extends Set<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(this::setAll);
    }

    @Override
    default Tuple2<? extends Set<T>, ? extends Set<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> t = iterator().span(predicate);
        return Tuple.of(HashSet.ofAll(t._1), setAll(t._2));
    }

    @Override
    default String stringPrefix() {
        return getClass().getSimpleName();
    }

    @Override
    default Option<? extends Set<T>> tailOption() {
        if (isEmpty()) {
            return Option.none();
        } else {
            return Option.some(tail());
        }
    }

    @Override
    default Set<T> take(int n) {
        if (n >= size() || isEmpty()) {
            return this;
        } else if (n <= 0) {
            return clear();
        } else {
            return setAll(() -> iterator().take(n));
        }
    }


    @Override
    default Set<T> takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    default Set<T> takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Set<T> taken = setAll(iterator().takeWhile(predicate));
        return taken.length() == length() ? this : taken;
    }

    @Override
    default java.util.Set<T> toJavaSet() {
        return toJavaSet(java.util.HashSet::new);
    }

    @Override
    default Set<T> union(Set<? extends T> that) {
        return addAll(that);
    }

    @Override
    default <U> Set<Tuple2<T, U>> zip(Iterable<? extends U> that) {
        return zipWith(that, Tuple::of);
    }

    /**
     * Transforms this {@code Set}.
     *
     * @param f   A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    default <U> U transform(Function<? super Set<T>, ? extends U> f) {
        Objects.requireNonNull(f, "f is null");
        return f.apply(this);
    }

    @Override
    default T get() {
        // XXX LinkedChampSetTest.shouldThrowWhenInitOfNil wants us to throw
        //     UnsupportedOperationException instead of NoSuchElementException
        //     when this set is empty.
        // XXX LinkedChampSetTest.shouldConvertEmptyToTry wants us to throw
        //     NoSuchElementException when this set is empty.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return head();
    }

    @Override
    default <U> Set<Tuple2<T, U>> zipAll(Iterable<? extends U> that, T thisElem, U thatElem) {
        Objects.requireNonNull(that, "that is null");
        return setAll(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default <U, R> Set<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return setAll(iterator().zipWith(that, mapper));
    }

    @Override
    default Set<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    default <U> Set<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return setAll(iterator().zipWithIndex(mapper));
    }

    @Override
    default Set<T> toSet() {
        return this;
    }

    @Override
    default <ID> List<Tree.Node<T>> toTree(Function<? super T, ? extends ID> idMapper, Function<? super T, ? extends ID> parentMapper) {
        // XXX AbstractTraversableTest.shouldConvertToTree() wants us to
        //     sort the elements by hash code.
        java.util.List<T> list = new ArrayList<T>(this.size());
        for (T t : this) {
            list.add(t);
        }
        list.sort(Comparator.comparing(Objects::hashCode));
        return Tree.build(list, idMapper, parentMapper);
    }
}
