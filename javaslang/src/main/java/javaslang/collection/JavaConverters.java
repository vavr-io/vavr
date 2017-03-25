/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static javaslang.API.TODO;
import static javaslang.collection.Collections.areEqual;

/**
 * THIS CLASS IS INTENDED TO BE USED INTERNALLY ONLY!
 * <p>
 * This helper class is similar to scala.collection.JavaConverters.
 * It provides methods that return views on Java collections.
 * <p>
 * These views are tightly coupled to collections of same characteristics, e.g.
 * <p>
 * <ul>
 * <li>javaslang.collection.Seq has a java.util.List view</li>
 * <li>javaslang.collection.Set has a java.util.Set view</li>
 * <li>javaslang.collection.Map has a java.util.Map view</li>
 * <li>javaslang.collection.Multimap has a java.util.Map view</li>
 * </ul>
 * <p>
 * Subtypes of the Javaslang types mentioned above can have special views that make use of optimized implementations.
 *
 * @author Pap Lőrinc, Daniel Dietrich, Sean Flanigan
 * @since 2.1.0
 */
final class JavaConverters {
    private JavaConverters() {}

    static <T> java.util.List<T> asImmutableJava(LinearSeq<T> seq) {
        return new SeqAsImmutableJavaList<>(seq, LinearSeqImmutableListIterator::new);
    }

    static <T> java.util.List<T> asImmutableJava(IndexedSeq<T> seq) {
        return new SeqAsImmutableJavaList<>(seq, IndexedSeqImmutableListIterator::new);
    }

    static <T> java.util.List<T> asMutableJava(LinearSeq<T> seq) {
        return new SeqAsMutableJavaList<>(seq, LinearSeqMutableListIterator::new);
    }

    static <T> java.util.List<T> asMutableJava(IndexedSeq<T> seq) {
        return new SeqAsMutableJavaList<>(seq, IndexedSeqMutableListIterator::new);
    }

    static <T> java.util.Set<T> asImmutableJava(Set<T> set) {
        return new SetAsJavaSet<>(set);
    }

    static <K, V> java.util.Map<K, V> asImmutableJava(Map<K, V> map) {
        return new MapAsJavaMap<>(map);
    }

    static <K, V> java.util.Map<K, java.util.Collection<V>> asImmutableJava(Multimap<K, V> map) {
        return new MultimapAsJavaMap<>(map);
    }

    // -- private view implementations
    // TODO these should be split out into multiple files

    static abstract class AbstractSeqAsJavaList<T, S extends Seq<T>> extends java.util.AbstractList<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final ListIteratorFactory<T, S> listIteratorFactory;
        private Seq<T> delegate;

        AbstractSeqAsJavaList(S delegate, ListIteratorFactory<T, S> listIteratorFactory) {
            this.delegate = delegate;
            this.listIteratorFactory = listIteratorFactory;
        }

        @Override
        public boolean contains(Object obj) {
            return narrowed().contains(obj);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            return narrowed().containsAll(collection);
        }

        @Override
        public T get(int index) {
            // may throw IndexOutOfBoundsException according to j.u.List.get(int)
            return delegate.get(index);
        }

        @Override
        public int indexOf(Object obj) {
            return narrowed().indexOf(obj);
        }

        @Override
        public boolean isEmpty() {
            return delegate.isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return delegate.iterator();
        }

        @Override
        public int lastIndexOf(Object obj) {
            return narrowed().lastIndexOf(obj);
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListIterator<T> listIterator() {
            return listIteratorFactory.apply((S) delegate, 0);
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListIterator<T> listIterator(int index) {
            return listIteratorFactory.apply((S) delegate, index);
        }

        @Override
        public int size() {
            return delegate.size();
        }

        @Override
        public java.util.List<T> subList(int fromIndex, int toIndex) {
            // may throw IndexOutOfBoundsException according to j.u.List.subList(int, int)
            return delegate.subSequence(fromIndex, toIndex).asImmutableJavaList();
        }

        @Override
        public Object[] toArray() {
            return delegate.toJavaArray();
        }

        @Override
        public <U> U[] toArray(U[] array) {
            return delegate.toJavaList().toArray(array);
        }

        public Seq<Object> narrowed() {
            return Seq.narrow(delegate);
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return (o == this) || ((o instanceof java.util.List) && areEqual(delegate, (java.util.List<?>) o));
        }

        @Override
        public String toString() {
            return delegate.mkString("[", ", ", "]");
        }

        // -- private helpers

        Seq<T> getDelegate() {
            return delegate;
        }

        void setDelegate(Seq<T> delegate) {
            this.delegate = delegate;
        }

        // -- Javaslang converters

        Array<T> asArray() {
            return delegate.toArray();
        }

        List<T> asList() {
            return delegate.toList();
        }

        Queue<T> asQueue() {
            return delegate.toQueue();
        }

        Stream<T> asStream() {
            return delegate.toStream();
        }

        Vector<T> asVector() {
            return delegate.toVector();
        }


    }

    static class SeqAsImmutableJavaList<T, S extends Seq<T>> extends AbstractSeqAsJavaList<T, S> {
        private static final long serialVersionUID = 1L;

        SeqAsImmutableJavaList(S delegate, ListIteratorFactory<T, S> listIteratorFactory) {
            super(delegate, listIteratorFactory);
        }

        @Override
        void setDelegate(Seq<T> delegate) {
            // this should never be called
            throw new IllegalStateException();
        }

        @Override
        public boolean add(T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object obj) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> collection) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void replaceAll(UnaryOperator<T> operator) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void sort(Comparator<? super T> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T set(int index, T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(int index, T element) {
            throw new UnsupportedOperationException();
        }

        @Override
        public T remove(int index) {
            throw new UnsupportedOperationException();
        }
    }

    static class SeqAsMutableJavaList<T, S extends Seq<T>> extends AbstractSeqAsJavaList<T, S> {
        private static final long serialVersionUID = 1L;

        SeqAsMutableJavaList(S delegate, ListIteratorFactory<T, S> listIteratorFactory) {
            super(delegate, listIteratorFactory);
        }

        T setDelegateAndGetPreviousElement(int index, Seq<T> newDelegate) {
            final T previousElement = getDelegate().get(index);
            setDelegate(newDelegate);
            return previousElement;
        }

        /**
         * Points at a new delegate, but only if it has a different size
         * @param newDelegate the new delegate to use if it has a different size
         * @return whether the delegate was changed
         */
        @SuppressWarnings("unchecked")
        boolean setDelegateIfSizeChanged(Seq<?> newDelegate) {
            final boolean changed = getDelegate().size() != newDelegate.size();
            if (changed) setDelegate((Seq<T>) newDelegate);
            return changed;
        }

        @Override
        public boolean add(T element) {
            setDelegate(getDelegate().append(element));
            return true;
        }

        @Override
        public void add(int index, T element) {
            // may throw IndexOutOfBoundsException according to j.u.List.add(int, T)
            setDelegate(getDelegate().insert(index, element));
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            return setDelegateIfSizeChanged(getDelegate().appendAll(collection));
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> collection) {
            // may throw IndexOutOfBoundsException according to j.u.List.addAll(int, Collection)
            return setDelegateIfSizeChanged(getDelegate().insertAll(index, collection));
        }

        @Override
        public void clear() {
            setDelegate(getDelegate().take(0));
        }

        @Override
        public T remove(int index) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.remove(int)
            return setDelegateAndGetPreviousElement(index, getDelegate().removeAt(index));
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object obj) {
            return setDelegateIfSizeChanged(narrowed().remove(obj));
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return setDelegateIfSizeChanged(narrowed().removeAll(c));
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return setDelegateIfSizeChanged(narrowed().retainAll(c));
        }

        @Override
        public T set(int index, T element) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.set(int, T)
            return setDelegateAndGetPreviousElement(index, getDelegate().update(index, element));
        }

    }


    @FunctionalInterface
    private interface ListIteratorFactory<T, S extends Seq<T>> extends BiFunction<S, Integer, ListIterator<T>>, Serializable {
    }

    static class SetAsJavaSet<T> implements java.util.Set<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private Set<T> delegate;

        SetAsJavaSet(Set<T> delegate) {
            this.delegate = delegate;
        }
        @Override
        public int size() {
            return TODO();
        }

        @Override
        public boolean isEmpty() {
            return TODO();
        }

        @Override
        public boolean contains(Object o) {
            return TODO();
        }

        @Override
        public Iterator<T> iterator() {
            return TODO();
        }

        @Override
        public Object[] toArray() {
            return TODO();
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return TODO();
        }

        @Override
        public boolean add(T t) {
            return TODO();
        }

        @Override
        public boolean remove(Object o) {
            return TODO();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return TODO();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return TODO();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return TODO();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return TODO();
        }

        @Override
        public void clear() {
            TODO();
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return TODO();
        }

        @Override
        public int hashCode() {
            return TODO();
        }

        @Override
        public String toString() {
            return delegate.toJavaSet().toString();
        }
    }

    static class MapAsJavaMap<K, V> implements java.util.Map<K, V>, Serializable {
        private static final long serialVersionUID = 1L;
        private Map<K, V> delegate;

        MapAsJavaMap(Map<K, V> delegate) {
            this.delegate = delegate;
        }

        @Override
        public int size() {
            return TODO();
        }

        @Override
        public boolean isEmpty() {
            return TODO();
        }

        @Override
        public boolean containsKey(Object key) {
            return TODO();
        }

        @Override
        public boolean containsValue(Object value) {
            return TODO();
        }

        @Override
        public V get(Object key) {
            return TODO();
        }

        @Override
        public V put(K key, V value) {
            return TODO();
        }

        @Override
        public V remove(Object key) {
            return TODO();
        }

        @Override
        public void putAll(java.util.Map<? extends K, ? extends V> m) {
            TODO();
        }

        @Override
        public void clear() {
            TODO();
        }

        @Override
        public java.util.Set<K> keySet() {
            return TODO();
        }

        @Override
        public Collection<V> values() {
            return TODO();
        }

        @Override
        public java.util.Set<Entry<K, V>> entrySet() {
            return TODO();
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return TODO();
        }

        @Override
        public int hashCode() {
            return TODO();
        }

        @Override
        public String toString() {
            return delegate.toJavaMap().toString();
        }
    }

    static class MultimapAsJavaMap<K, V> implements java.util.Map<K, java.util.Collection<V>>, Serializable {
        private static final long serialVersionUID = 1L;
        private Multimap<K, V> delegate;

        MultimapAsJavaMap(Multimap<K, V> delegate) {
            this.delegate = delegate;
        }

        @Override
        public int size() {
            return TODO();
        }

        @Override
        public boolean isEmpty() {
            return TODO();
        }

        @Override
        public boolean containsKey(Object key) {
            return TODO();
        }

        @Override
        public boolean containsValue(Object value) {
            return TODO();
        }

        @Override
        public Collection<V> get(Object key) {
            return TODO();
        }

        @Override
        public Collection<V> put(K key, Collection<V> value) {
            return TODO();
        }

        @Override
        public Collection<V> remove(Object key) {
            return TODO();
        }

        @Override
        public void putAll(java.util.Map<? extends K, ? extends Collection<V>> m) {
            TODO();
        }

        @Override
        public void clear() {
            TODO();
        }

        @Override
        public java.util.Set<K> keySet() {
            return TODO();
        }

        @Override
        public Collection<Collection<V>> values() {
            return TODO();
        }

        @Override
        public java.util.Set<Entry<K, Collection<V>>> entrySet() {
            return TODO();
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return TODO();
        }

        @Override
        public int hashCode() {
            return TODO();
        }

        @Override
        public String toString() {
            return delegate.toJavaMap().toString();
        }
    }

    private static abstract class AbstractIndexedSeqListIterator<T> implements java.util.ListIterator<T> {
        private IndexedSeq<T> delegate;
        private int index;

        AbstractIndexedSeqListIterator(IndexedSeq<T> delegate, int index) {
            this.delegate = delegate;
            this.index = index;
        }

        int getIndex() {
            return index;
        }

        @Override
        public boolean hasNext() {
            return index < delegate.length();
        }

        @Override
        public T next() {
            return delegate.get(index++);
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public T previous() {
            return delegate.get(--index);
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        IndexedSeq<T> getDelegate() {
            return delegate;
        }

        void setDelegate(IndexedSeq<T> delegate) {
            this.delegate = delegate;
        }
    }

    private static class IndexedSeqImmutableListIterator<T> extends AbstractIndexedSeqListIterator<T> {
        IndexedSeqImmutableListIterator(IndexedSeq<T> delegate, int index) {
            super(delegate, index);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T value) {
            throw new UnsupportedOperationException();
        }

        void setDelegate(IndexedSeq<T> delegate) {
            // this should never be called
            throw new IllegalStateException();
        }

    }

    private static class IndexedSeqMutableListIterator<T> extends AbstractIndexedSeqListIterator<T> {
        IndexedSeqMutableListIterator(IndexedSeq<T> delegate, int index) {
            super(delegate, index);
        }

        @Override
        public void remove() {
            setDelegate(getDelegate().removeAt(getIndex()));
        }

        @Override
        public void set(T value) {
            setDelegate(getDelegate().update(getIndex(), value));
        }

        @Override
        public void add(T value) {
            setDelegate(getDelegate().insert(getIndex(), value));
        }

    }

    private static class LinearSeqImmutableListIterator<T> extends IndexedSeqImmutableListIterator<T> {
        LinearSeqImmutableListIterator(Seq<T> delegate, int index) {
            super(delegate.toVector(), index);
        }
    }

    private static class LinearSeqMutableListIterator<T> extends IndexedSeqMutableListIterator<T> {
        LinearSeqMutableListIterator(Seq<T> delegate, int index) {
            super(delegate.toVector(), index);
        }
    }
}
