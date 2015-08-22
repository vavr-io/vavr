/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Kind;
import javaslang.Tuple2;
import javaslang.control.None;
import javaslang.control.Option;
import javaslang.control.Some;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class Array<T> implements IndexedSeq<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private static final Array<?> EMPTY = new Array<>(RandomAccessList.empty());

    private final RandomAccessList<T> binaryList;

    private Array(RandomAccessList<T> binaryList) {
        this.binaryList = binaryList;
    }

    @SuppressWarnings("unchecked")
    public static <T> Array<T> empty() {
        return (Array<T>) EMPTY;
    }
    
    @Override
    public Array<T> append(T element) {
        return null;
    }

    @Override
    public Array<T> appendAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    public boolean hasDefiniteSize() {
        return true;
    }

    @Override
    public boolean isTraversableAgain() {
        return true;
    }

    @Override
    public Array<T> clear() {
        return empty();
    }

    @Override
    public Array<Tuple2<T, T>> crossProduct() {
        return null;
    }

    @Override
    public <U> Array<Tuple2<T, U>> crossProduct(Iterable<? extends U> that) {
        return null;
    }

    @Override
    public Array<? extends Array<T>> combinations() {
        return null;
    }

    @Override
    public Array<? extends Array<T>> combinations(int k) {
        return null;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("get(" + index + ")");
        }
        return binaryList.get(index);
    }

    @Override
    public Array<T> distinct() {
        return null;
    }

    @Override
    public Array<T> distinctBy(Comparator<? super T> comparator) {
        return null;
    }

    @Override
    public <U> Array<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        return null;
    }

    @Override
    public Array<T> drop(int n) {
        return null;
    }

    @Override
    public Array<T> dropRight(int n) {
        return null;
    }

    @Override
    public Array<T> dropWhile(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Array<T> filter(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Array<T> findAll(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public <U> Array<U> flatMap(Function<? super T, ? extends Iterable<? extends U>> mapper) {
        return null;
    }

    @Override
    public <U> Array<U> flatMapM(Function<? super T, ? extends Kind<? extends IterableKind<?>, ? extends U>> mapper) {
        return null;
    }

    @Override
    public Array<Object> flatten() {
        return null;
    }

    @Override
    public <C> Map<C, ? extends Array<T>> groupBy(Function<? super T, ? extends C> classifier) {
        return null;
    }

    @Override
    public T head() {
        if (isEmpty()) {
            throw new NoSuchElementException("head() on empty Array");
        }
        return binaryList.head();
    }

    @Override
    public Option<T> headOption() {
        return isEmpty() ? None.instance() : new Some<>(head());
    }

    @Override
    public Array<? extends Array<T>> grouped(int size) {
        return null;
    }

    @Override
    public int indexOf(T element, int from) {
        return 0;
    }

    @Override
    public Array<T> init() {
        return null;
    }

    @Override
    public Option<? extends Array<T>> initOption() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return binaryList.isEmpty();
    }

    @Override
    public Array<T> insert(int index, T element) {
        return null;
    }

    @Override
    public Array<T> insertAll(int index, Iterable<? extends T> elements) {
        return null;
    }

    @Override
    public Array<T> intersperse(T element) {
        return null;
    }

    @Override
    public int lastIndexOf(T element, int end) {
        return 0;
    }

    @Override
    public int length() {
        return binaryList.size();
    }

    @Override
    public <U> Array<U> map(Function<? super T, ? extends U> mapper) {
        return null;
    }

    @Override
    public Tuple2<? extends Array<T>, ? extends Array<T>> partition(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Array<T> peek(Consumer<? super T> action) {
        return null;
    }

    @Override
    public Array<? extends Array<T>> permutations() {
        return null;
    }

    @Override
    public Array<T> prepend(T element) {
        return new Array<>(binaryList.prepend(element));
    }

    @Override
    public Array<T> prependAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    public Array<T> remove(T element) {
        return null;
    }

    @Override
    public Array<T> removeFirst(Predicate<T> predicate) {
        return null;
    }

    @Override
    public Array<T> removeLast(Predicate<T> predicate) {
        return null;
    }

    @Override
    public Array<T> removeAt(int indx) {
        return null;
    }

    @Override
    public Array<T> removeAll(T element) {
        return null;
    }

    @Override
    public Array<T> removeAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    public Array<T> replace(T currentElement, T newElement) {
        return null;
    }

    @Override
    public Array<T> replaceAll(T currentElement, T newElement) {
        return null;
    }

    @Override
    public Array<T> replaceAll(UnaryOperator<T> operator) {
        return null;
    }

    @Override
    public Array<T> retainAll(Iterable<? extends T> elements) {
        return null;
    }

    @Override
    public Array<T> reverse() {
        return null;
    }

    @Override
    public Array<T> set(int index, T element) {
        return new Array<>(binaryList.set(index, element));
    }

    @Override
    public Array<? extends Array<T>> sliding(int size) {
        return null;
    }

    @Override
    public Array<? extends Array<T>> sliding(int size, int step) {
        return null;
    }

    @Override
    public Array<T> sort() {
        return null;
    }

    @Override
    public Array<T> sort(Comparator<? super T> comparator) {
        return null;
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n) {
        return null;
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAtInclusive(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Tuple2<? extends Array<T>, ? extends Array<T>> span(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public Array<T> subSequence(int beginIndex) {
        return null;
    }

    @Override
    public Array<T> subSequence(int beginIndex, int endIndex) {
        return null;
    }

    @Override
    public Array<T> tail() {
        if (isEmpty()) {
            throw new UnsupportedOperationException("tail() on empty Array");
        }
        return new Array<>(binaryList.tail());
    }

    @Override
    public Option<? extends Array<T>> tailOption() {
        return isEmpty() ? None.instance() : new Some<>(tail());
    }

    @Override
    public Array<T> take(int n) {
        return null;
    }

    @Override
    public Array<T> takeRight(int n) {
        return null;
    }

    @Override
    public Array<T> takeWhile(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public <U> Array<U> unit(Iterable<? extends U> iterable) {
        return null;
    }

    @Override
    public <T1, T2> Tuple2<? extends Array<T1>, ? extends Array<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        return null;
    }

    @Override
    public <U> Array<Tuple2<T, U>> zip(Iterable<U> that) {
        return null;
    }

    @Override
    public <U> Array<Tuple2<T, U>> zipAll(Iterable<U> that, T thisElem, U thatElem) {
        return null;
    }

    @Override
    public Array<Tuple2<T, Integer>> zipWithIndex() {
        return null;
    }
}
