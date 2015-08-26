/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.*;
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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> appendAll(java.lang.Iterable<? extends T> elements) {
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<Tuple2<T, U>> crossProduct(java.lang.Iterable<? extends U> that) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<? extends Array<T>> combinations() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<? extends Array<T>> combinations(int k) {
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> distinctBy(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<T> distinctBy(Function<? super T, ? extends U> keyExtractor) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> drop(int n) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> dropRight(int n) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> dropWhile(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> filter(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<U> flatMap(Function<? super T, ? extends java.lang.Iterable<? extends U>> mapper) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<U> flatMapVal(Function<? super T, ? extends Value<? extends U>> mapper) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<Object> flatten() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <C> Map<C, ? extends Array<T>> groupBy(Function<? super T, ? extends C> classifier) {
        throw new UnsupportedOperationException("TODO");
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
    public int indexOf(T element, int from) {
        return 0;
    }

    @Override
    public Array<T> init() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Option<? extends Array<T>> initOption() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public boolean isEmpty() {
        return binaryList.isEmpty();
    }

    @Override
    public Array<T> insert(int index, T element) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> insertAll(int index, java.lang.Iterable<? extends T> elements) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> intersperse(T element) {
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Tuple2<? extends Array<T>, ? extends Array<T>> partition(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> peek(Consumer<? super T> action) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<? extends Array<T>> permutations() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> prepend(T element) {
        return new Array<>(binaryList.prepend(element));
    }

    @Override
    public Array<T> prependAll(java.lang.Iterable<? extends T> elements) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> remove(T element) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> removeFirst(Predicate<T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> removeLast(Predicate<T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> removeAt(int indx) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> removeAll(T element) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> removeAll(java.lang.Iterable<? extends T> elements) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> replace(T currentElement, T newElement) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> replaceAll(T currentElement, T newElement) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> replaceAll(UnaryOperator<T> operator) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> retainAll(java.lang.Iterable<? extends T> elements) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> reverse() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> set(int index, T element) {
        return new Array<>(binaryList.set(index, element));
    }

    @Override
    public Array<T> sort() {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> sort(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(int n) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAt(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Tuple2<? extends Seq<T>, ? extends Seq<T>> splitAtInclusive(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Tuple2<? extends Array<T>, ? extends Array<T>> span(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> subSequence(int beginIndex) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> subSequence(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException("TODO");
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
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> takeRight(int n) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<T> takeWhile(Predicate<? super T> predicate) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<U> unit(java.lang.Iterable<? extends U> iterable) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <T1, T2> Tuple2<? extends Array<T1>, ? extends Array<T2>> unzip(Function<? super T, Tuple2<? extends T1, ? extends T2>> unzipper) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<Tuple2<T, U>> zip(java.lang.Iterable<U> that) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public <U> Array<Tuple2<T, U>> zipAll(java.lang.Iterable<U> that, T thisElem, U thatElem) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public Array<Tuple2<T, Integer>> zipWithIndex() {
        throw new UnsupportedOperationException("TODO");
    }
}
