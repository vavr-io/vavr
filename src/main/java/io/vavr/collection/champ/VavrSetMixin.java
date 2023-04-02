package io.vavr.collection.champ;

import io.vavr.PartialFunction;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Collections;
import io.vavr.collection.HashSet;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Tree;
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
 * This mixin-interface defines a {@link #create} method and a {@link #createFromElements}
 * method, and provides default implementations for methods defined in the
 * {@link Set} interface.
 *
 * @param <T> the element type of the set
 */
@SuppressWarnings("unchecked")
interface VavrSetMixin<T, SELF extends VavrSetMixin<T, SELF>> extends Set<T> {
    long serialVersionUID = 0L;

    /**
     * Creates an empty set of the specified element type.
     *
     * @param <R> the element type
     * @return a new empty set.
     */
    <R> Set<R> create();

    /**
     * Creates an empty set of the specified element type, and adds all
     * the specified elements.
     *
     * @param elements the elements
     * @param <R>      the element type
     * @return a new set that contains the specified elements.
     */
    <R> Set<R> createFromElements(Iterable<? extends R> elements);

    @Override
    default <R> Set<R> collect(PartialFunction<? super T, ? extends R> partialFunction) {
        return createFromElements(iterator().<R>collect(partialFunction));
    }

    @Override
    default SELF diff(Set<? extends T> that) {
        return removeAll(that);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF distinct() {
        return (SELF) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF distinctBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return (SELF) createFromElements(iterator().distinctBy(comparator));
    }

    @SuppressWarnings("unchecked")
    @Override
    default <U> SELF distinctBy(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor, "keyExtractor is null");
        return (SELF) createFromElements(iterator().distinctBy(keyExtractor));
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF drop(int n) {
        if (n <= 0) {
            return (SELF) this;
        }
        return (SELF) createFromElements(iterator().drop(n));
    }


    @Override
    default SELF dropUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return dropWhile(predicate.negate());
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF dropWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final SELF dropped = (SELF) createFromElements(iterator().dropWhile(predicate));
        return dropped.length() == length() ? (SELF) this : dropped;
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final SELF filtered = (SELF) createFromElements(iterator().filter(predicate));

        if (filtered.isEmpty()) {
            return (SELF) create();
        } else if (filtered.length() == length()) {
            return (SELF) this;
        } else {
            return filtered;
        }
    }

    @Override
    default SELF tail() {
        // XXX Traversable.tail() specifies that we must throw
        //     UnsupportedOperationException instead of
        //     NoSuchElementException.
        if (isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return (SELF) remove(iterator().next());
    }

    @Override
    default <U> Set<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        Set<U> flatMapped = this.create();
        for (T t : this) {
            for (U u : mapper.apply(t)) {
                flatMapped = flatMapped.add(u);
            }
        }
        return flatMapped;
    }

    @Override
    default <U> Set<U> map(Function<? super T, ? extends U> mapper) {
        Set<U> mapped = this.create();
        for (T t : this) {
            mapped = mapped.add(mapper.apply(t));
        }
        return mapped;
    }

    @Override
    default SELF filterNot(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return filter(predicate.negate());
    }


    @Override
    default <C> Map<C, ? extends Set<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return Collections.groupBy(this, classifier, this::createFromElements);
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

    @SuppressWarnings("unchecked")
    @Override
    default SELF intersect(Set<? extends T> elements) {
        Objects.requireNonNull(elements, "elements is null");
        if (isEmpty() || elements.isEmpty()) {
            return (SELF) create();
        } else {
            final int size = size();
            if (size <= elements.size()) {
                return retainAll(elements);
            } else {
                final SELF results = (SELF) this.<T>createFromElements(elements).retainAll(this);
                return (size == results.size()) ? (SELF) this : results;
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

    @SuppressWarnings("unchecked")
    @Override
    default SELF orElse(Iterable<? extends T> other) {
        return isEmpty() ? (SELF) createFromElements(other) : (SELF) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF orElse(Supplier<? extends Iterable<? extends T>> supplier) {
        return isEmpty() ? (SELF) createFromElements(supplier.get()) : (SELF) this;
    }

    @Override
    default Tuple2<? extends Set<T>, ? extends Set<T>> partition(Predicate<? super T> predicate) {
        return Collections.partition(this, this::createFromElements, predicate);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF peek(Consumer<? super T> action) {
        Objects.requireNonNull(action, "action is null");
        if (!isEmpty()) {
            action.accept(iterator().head());
        }
        return (SELF) this;
    }

    @Override
    default SELF removeAll(Iterable<? extends T> elements) {
        return (SELF) Collections.removeAll(this, elements);
    }

    @SuppressWarnings("unchecked")
    @Override
    default SELF replace(T currentElement, T newElement) {
        if (contains(currentElement)) {
            return (SELF) remove(currentElement).add(newElement);
        } else {
            return (SELF) this;
        }
    }

    @Override
    default SELF replaceAll(T currentElement, T newElement) {
        return replace(currentElement, newElement);
    }

    @Override
    default SELF retainAll(Iterable<? extends T> elements) {
        return (SELF) Collections.retainAll(this, elements);
    }

    @Override
    default SELF scan(T zero, BiFunction<? super T, ? super T, ? extends T> operation) {
        return (SELF) scanLeft(zero, operation);
    }

    @Override
    default <U> Set<U> scanLeft(U zero, BiFunction<? super U, ? super T, ? extends U> operation) {
        return Collections.scanLeft(this, zero, operation, this::createFromElements);
    }

    @Override
    default <U> Set<U> scanRight(U zero, BiFunction<? super T, ? super U, ? extends U> operation) {
        return Collections.scanRight(this, zero, operation, this::createFromElements);
    }

    @Override
    default Iterator<? extends Set<T>> slideBy(Function<? super T, ?> classifier) {
        return iterator().slideBy(classifier).map(this::createFromElements);
    }

    @Override
    default Iterator<? extends Set<T>> sliding(int size) {
        return sliding(size, 1);
    }

    @Override
    default Iterator<? extends Set<T>> sliding(int size, int step) {
        return iterator().sliding(size, step).map(this::createFromElements);
    }

    @Override
    default Tuple2<? extends Set<T>, ? extends Set<T>> span(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Tuple2<Iterator<T>, Iterator<T>> t = iterator().span(predicate);
        return Tuple.of(HashSet.ofAll(t._1), createFromElements(t._2));
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
    default SELF take(int n) {
        if (n >= size() || isEmpty()) {
            return (SELF) this;
        } else if (n <= 0) {
            return (SELF) create();
        } else {
            return (SELF) createFromElements(() -> iterator().take(n));
        }
    }


    @Override
    default SELF takeUntil(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        return takeWhile(predicate.negate());
    }

    @Override
    default SELF takeWhile(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate, "predicate is null");
        final Set<T> taken = createFromElements(iterator().takeWhile(predicate));
        return taken.length() == length() ? (SELF) this : (SELF) taken;
    }

    @Override
    default java.util.Set<T> toJavaSet() {
        return toJavaSet(java.util.HashSet::new);
    }

    @Override
    default SELF union(Set<? extends T> that) {
        return (SELF) addAll(that);
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
        return createFromElements(iterator().zipAll(that, thisElem, thatElem));
    }

    @Override
    default <U, R> Set<R> zipWith(Iterable<? extends U> that, BiFunction<? super T, ? super U, ? extends R> mapper) {
        Objects.requireNonNull(that, "that is null");
        Objects.requireNonNull(mapper, "mapper is null");
        return createFromElements(iterator().zipWith(that, mapper));
    }

    @Override
    default Set<Tuple2<T, Integer>> zipWithIndex() {
        return zipWithIndex(Tuple::of);
    }

    @Override
    default <U> Set<U> zipWithIndex(BiFunction<? super T, ? super Integer, ? extends U> mapper) {
        Objects.requireNonNull(mapper, "mapper is null");
        return createFromElements(iterator().zipWithIndex(mapper));
    }

    @Override
    default SELF toSet() {
        return (SELF) this;
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
