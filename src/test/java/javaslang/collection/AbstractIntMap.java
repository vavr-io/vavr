package javaslang.collection;

import javaslang.Tuple2;
import javaslang.control.Option;

import java.util.Comparator;
import java.util.function.*;

public class AbstractIntMap<T> implements Traversable<Map.Entry<Integer, T>> {

    private final Map<Integer, T> original;

    public static <T> AbstractIntMap<T> of(Map<Integer, T> original) {
        return new AbstractIntMap<>(original);
    }

    private AbstractIntMap(Map<Integer, T> original) {
        this.original = original;
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> clear() {
        return original.clear();
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> distinct() {
        return original.distinct();
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> distinctBy(Comparator<? super Map.Entry<Integer, T>> comparator) {
        return original.distinctBy(comparator);
    }

    @Override
    public <U> Traversable<Map.Entry<Integer, T>> distinctBy(Function<? super Map.Entry<Integer, T>, ? extends U> keyExtractor) {
        return original.distinctBy(keyExtractor);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> drop(int n) {
        return original.drop(n);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> dropRight(int n) {
        return original.dropRight(n);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> dropWhile(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.dropWhile(predicate);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> filter(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.filter(predicate);
    }

    @Override
    public Option<Map.Entry<Integer, T>> findLast(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.findLast(predicate);
    }

    @Override
    public <U> Traversable<U> flatMap(Function<? super Map.Entry<Integer, T>, ? extends Iterable<? extends U>> mapper) {
        return original.flatMap(mapper);
    }

    @Override
    public Traversable<Object> flatten() {
        return original.flatten();
    }

    @Override
    public <U> U foldRight(U zero, BiFunction<? super Map.Entry<Integer, T>, ? super U, ? extends U> f) {
        return original.foldRight(zero, f);
    }

    @Override
    public <C> Map<C, ? extends Traversable<Map.Entry<Integer, T>>> groupBy(Function<? super Map.Entry<Integer, T>, ? extends C> classifier) {
        return original.groupBy(classifier);
    }

    @Override
    public boolean hasDefiniteSize() {
        return original.hasDefiniteSize();
    }

    @Override
    public Map.Entry<Integer, T> head() {
        return original.head();
    }

    @Override
    public Option<Map.Entry<Integer, T>> headOption() {
        return original.headOption();
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> init() {
        return original.init();
    }

    @Override
    public Option<? extends Traversable<Map.Entry<Integer, T>>> initOption() {
        return original.initOption();
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
    public <U> Traversable<U> map(Function<? super Map.Entry<Integer, T>, ? extends U> mapper) {
        return original.map(mapper);
    }

    @Override
    public Tuple2<? extends Traversable<Map.Entry<Integer, T>>, ? extends Traversable<Map.Entry<Integer, T>>> partition(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.partition(predicate);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> peek(Consumer<? super Map.Entry<Integer, T>> action) {
        return original.peek(action);
    }

    @Override
    public Map.Entry<Integer, T> reduceRight(BiFunction<? super Map.Entry<Integer, T>, ? super Map.Entry<Integer, T>, ? extends Map.Entry<Integer, T>> op) {
        return original.reduceRight(op);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> replace(Map.Entry<Integer, T> currentElement, Map.Entry<Integer, T> newElement) {
        return original.replace(currentElement, newElement);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> replaceAll(Map.Entry<Integer, T> currentElement, Map.Entry<Integer, T> newElement) {
        return original.replaceAll(currentElement, newElement);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> replaceAll(UnaryOperator<Map.Entry<Integer, T>> operator) {
        return original.replaceAll(operator);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> retainAll(Iterable<? extends Map.Entry<Integer, T>> elements) {
        return original.retainAll(elements);
    }

    @Override
    public Tuple2<? extends Traversable<Map.Entry<Integer, T>>, ? extends Traversable<Map.Entry<Integer, T>>> span(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.span(predicate);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> tail() {
        return original.tail();
    }

    @Override
    public Option<? extends Traversable<Map.Entry<Integer, T>>> tailOption() {
        return original.tailOption();
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> take(int n) {
        return original.take(n);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> takeRight(int n) {
        return original.takeRight(n);
    }

    @Override
    public Traversable<Map.Entry<Integer, T>> takeWhile(Predicate<? super Map.Entry<Integer, T>> predicate) {
        return original.takeWhile(predicate);
    }
}
