/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.function.BiFunction;

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
 * @author Pap Lőrinc, Daniel Dietrich
 * @since 2.1.0
 */
final class JavaConverters {
    private static final boolean SHOULD_MUTATE = true;
    private JavaConverters() {}

    static <T> java.util.List<T> asJava(LinearSeq<T> seq) {
        return new SeqAsJavaList<>(seq, LinearSeqListIterator::new);
    }

    static <T> java.util.List<T> asJava(IndexedSeq<T> seq) {
        return new SeqAsJavaList<>(seq, IndexedSeqListIterator::new);
    }

    static <T> java.util.Set<T> asJava(Set<T> set) {
        return new SetAsJavaSet<>(set);
    }

    static <K, V> java.util.Map<K, V> asJava(Map<K, V> map) {
        return new MapAsJavaMap<>(map);
    }

    static <K, V> java.util.Map<K, java.util.Collection<V>> asJava(Multimap<K, V> map) {
        return new MultimapAsJavaMap<>(map);
    }

    // -- private view implementations

    static class SeqAsJavaList<T, S extends Seq<T>> implements java.util.List<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private final ListIteratorFactory<T, S> listIteratorFactory;
        private Seq<T> delegate;

        SeqAsJavaList(S delegate, ListIteratorFactory<T, S> listIteratorFactory) {
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
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.get(int)
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
            // may throw IndexOutOfBoundsException accordingly to j.u.List.subList(int, int)
            return delegate.subSequence(fromIndex, toIndex).asJava();
        }

        @Override
        public Object[] toArray() {
            return delegate.toJavaArray();
        }

        @Override
        public <U> U[] toArray(U[] array) {
            return delegate.toJavaList().toArray(array);
        }

        @Override
        public boolean add(T element) {
            setDelegate(delegate.append(element));
            return true;
        }

        @Override
        public void add(int index, T element) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.add(int, T)
            setDelegate(delegate.insert(index, element));
        }

        @Override
        public boolean addAll(Collection<? extends T> collection) {
            setDelegate(delegate.appendAll(collection));
            return true;
        }

        @Override
        public boolean addAll(int index, Collection<? extends T> collection) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.addAll(int, Collection)
            setDelegate(delegate.insertAll(index, collection));
            return true;
        }

        @Override
        public void clear() {
            setDelegate(delegate.take(0));
        }

        @Override
        public T remove(int index) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.remove(int)
            return setDelegateAndGetPreviousElement(index, delegate.removeAt(index));
        }

        public Seq<Object> narrowed() {
            return Seq.narrow(delegate);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(Object obj) {
            return setDelegateAndCheckChanged(narrowed().remove(obj));
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return setDelegateAndCheckChanged(narrowed().removeAll(c));
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return setDelegateAndCheckChanged(narrowed().retainAll(c));
        }

        @Override
        public T set(int index, T element) {
            // may throw an IndexOutOfBoundsException accordingly to the j.u.List.set(int, T)
            return setDelegateAndGetPreviousElement(index, delegate.update(index, element));
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return (o == this) || ((o instanceof java.util.List) && areEqual(delegate, (java.util.List<?>) o));
        }

        @Override
        public int hashCode() {
            return Collections.hash(delegate);
        }

        @Override
        public String toString() {
            return delegate.toJavaList().toString();
        }

        // -- private helpers

        private void setDelegate(Seq<T> delegate) {
            if (!SHOULD_MUTATE) throw new UnsupportedOperationException();
            else this.delegate = delegate;
        }

        private T setDelegateAndGetPreviousElement(int index, Seq<T> newDelegate) {
            final T previousElement = delegate.get(index);
            setDelegate(newDelegate);
            return previousElement;
        }

        @SuppressWarnings("unchecked")
        private boolean setDelegateAndCheckChanged(Seq<?> newDelegate) {
            final boolean changed = delegate.size() != newDelegate.size();
            setDelegate((Seq<T>) newDelegate);
            return changed;
        }

        // -- Javaslang converters

        Vector<T> asVector() {
            return delegate.toVector();
        }

        List<T> asList() {
            return delegate.toList();
        }

        Array<T> asArray() {
            return delegate.toArray();
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

    private static class IndexedSeqListIterator<T> implements java.util.ListIterator<T> {
        private IndexedSeq<T> delegate;
        private int index;

        IndexedSeqListIterator(IndexedSeq<T> delegate, int index) {
            this.delegate = delegate;
            this.index = index;
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

        @Override
        public void remove() {
            setDelegate(delegate.removeAt(index));
        }

        @Override
        public void set(T value) {
            setDelegate(delegate.update(index, value));
        }

        @Override
        public void add(T value) {
            setDelegate(delegate.insert(index, value));
        }

        private void setDelegate(IndexedSeq<T> delegate) {
            if (!SHOULD_MUTATE) throw new UnsupportedOperationException();
            else this.delegate = delegate;
        }
    }

    private static class LinearSeqListIterator<T> extends IndexedSeqListIterator<T> {
        LinearSeqListIterator(Seq<T> delegate, int index) {
            super(delegate.toVector(), index);
        }
    }
}
