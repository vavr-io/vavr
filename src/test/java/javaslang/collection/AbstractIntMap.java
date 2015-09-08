package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.*;

public class AbstractIntMap<T> implements Traversable<T> {

    private final Map<Integer, T> original;

    public static <T> AbstractIntMap<T> of(Map<Integer, T> original) {
        return new AbstractIntMap<>(original);
    }

    private AbstractIntMap(Map<Integer, T> original) {
        this.original = original;
    }

    @Override
    public int hashCode() {
        return original.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof AbstractIntMap) {
            final AbstractIntMap<?> that = (AbstractIntMap<?>) o;
            return original.values().equals(that.original.values());
        } else if (o instanceof Iterable) {
            final Iterable<?> that = (Iterable<?>) o;
            return original.values().equals(that);
        } else {
            return false;
        }
    }

    @Override
    public AbstractIntMap<T> clear() {
        return AbstractIntMap.of(original.clear());
    }

    @Override
    public AbstractIntMap<T> distinct() {
        return AbstractIntMap.of(original.distinct());
    }

    @Override
    public AbstractIntMap<T> distinctBy(Comparator<? super T> comparator) {
        return AbstractIntMap.of(original.distinctBy((o1, o2) -> comparator.compare(o1.value, o2.value)));
    }

    @Override
    public <U> AbstractIntMap<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return AbstractIntMap.of(original.distinctBy(f -> keyExtractor.apply(f.value)));
    }

    @Override
    public AbstractIntMap<T> drop(int n) {
        return AbstractIntMap.of(original.drop(n));
    }

    @Override
    public AbstractIntMap<T> dropRight(int n) {
        return AbstractIntMap.of(original.dropRight(n));
    }

    @Override
    public AbstractIntMap<T> dropWhile(Predicate<? super T> predicate) {
        return AbstractIntMap.of(original.dropWhile(p -> predicate.test(p.value)));
    }

    @Override
    public AbstractIntMap<T> filter(Predicate<? super T> predicate) {
        return AbstractIntMap.of(original.filter(p -> predicate.test(p.value)));
    }

    @Override
    public Option<T> findLast(Predicate<? super T> predicate) {
        return original.findLast(p -> predicate.test(p.value)).map(o -> o.value);
    }

    @Override
    public <U> Traversable<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return original.flatMap(e -> mapper.apply(e.value));
    }

    @Override
    public Traversable<Object> flatten() {
        return original.flatten();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super T, ? super U, ? extends U> f) {
        return original.foldRight(zero, (e, u) -> f.apply(e.value, u));
    }

    @Override
    public <C> Map<C, ? extends AbstractIntMap<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return original.groupBy(e -> classifier.apply(e.value)).map((k, v) -> Map.Entry.of(k, AbstractIntMap.of(v)));
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
    public AbstractIntMap<T> init() {
        return AbstractIntMap.of(original.init());
    }

    @Override
    public Option<? extends AbstractIntMap<T>> initOption() {
        return original.initOption().map(AbstractIntMap::of);
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
    public Tuple2<AbstractIntMap<T>, AbstractIntMap<T>> partition(Predicate<? super T> predicate) {
        return original.partition(p -> predicate.test(p.value)).map(AbstractIntMap::of, AbstractIntMap::of);
    }

    @Override
    public AbstractIntMap<T> peek(Consumer<? super T> action) {
        return AbstractIntMap.of(original.peek(e -> action.accept(e.value)));
    }

    @Override
    public T reduceRight(BiFunction<? super T, ? super T, ? extends T> op) {
        // TODO
        return null;
    }

    @Override
    public AbstractIntMap<T> replace(T currentElement, T newElement) {
        final Option<Map.Entry<Integer, T>> currentEntryOpt = original.findFirst(e -> e.value.equals(currentElement));
        if (currentEntryOpt.isDefined()) {
            final Map.Entry<Integer, T> currentEntry = currentEntryOpt.get();
            return AbstractIntMap.of(original.replace(currentEntry, Map.Entry.of(currentEntry.key, newElement)));
        } else {
            return this;
        }
    }

    @Override
    public AbstractIntMap<T> replaceAll(T currentElement, T newElement) {
        // TODO
        return null;
    }

    @Override
    public AbstractIntMap<T> replaceAll(UnaryOperator<T> operator) {
        return AbstractIntMap.of(original.replaceAll(o -> Map.Entry.of(o.key, operator.apply(o.value))));
    }

    @Override
    public AbstractIntMap<T> retainAll(Iterable<? extends T> elements) {
        // TODO
        return null;
    }

    @Override
    public Tuple2<? extends AbstractIntMap<T>, ? extends AbstractIntMap<T>> span(Predicate<? super T> predicate) {
        return original.span(p -> predicate.test(p.value)).map(AbstractIntMap::of, AbstractIntMap::of);
    }

    @Override
    public AbstractIntMap<T> tail() {
        return AbstractIntMap.of(original.tail());
    }

    @Override
    public Option<? extends AbstractIntMap<T>> tailOption() {
        return original.tailOption().map(AbstractIntMap::of);
    }

    @Override
    public AbstractIntMap<T> take(int n) {
        return AbstractIntMap.of(original.take(n));
    }

    @Override
    public AbstractIntMap<T> takeRight(int n) {
        return AbstractIntMap.of(original.takeRight(n));
    }

    @Override
    public AbstractIntMap<T> takeWhile(Predicate<? super T> predicate) {
        return AbstractIntMap.of(original.takeWhile(p -> predicate.test(p.value)));
    }
}
