/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.*;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

import static javaslang.API.TODO;

/**
 * THIS CLASS IS INTENDED TO BE USED INTERNALLY ONLY!
 * <p>
 * This helper class provides methods that return views on Java collections.
 * The view creation and back conversion take O(1).
 *
 * @author Daniel Dietrich
 * @since 2.1.0
 */
class JavaConverters {

    private JavaConverters() {
    }

    static <T, C extends Seq<T>> ListView<T, C> asJava(C seq, ChangePolicy changePolicy) {
        return new ListView<>(seq, changePolicy.isMutable());
    }

    static <T, C extends Set<T>> SetView<T> asJava(C set, ChangePolicy changePolicy) {
        return TODO("new SetView<>(set, changePolicy.isMutable());");
    }

    static <T, C extends SortedSet<T>> NavigableSetView<T> asJava(C sortedSet, ChangePolicy changePolicy) {
        return TODO("new NavigableSetView<>(set, changePolicy.isMutable());");
    }

    static <K, V, C extends Map<K, V>> MapView<K, V> asJava(C map, ChangePolicy changePolicy) {
        return TODO("new MapView<>(map, changePolicy.isMutable());");
    }

    static <K, V, C extends SortedMap<K, V>> NavigableMapView<K, V> asJava(C sortedMap, ChangePolicy changePolicy) {
        return TODO("new NavigableMapView<>(map, changePolicy.isMutable());");
    }

    /*
    static <K, V, C extends Multimap<K, V>> MapView<K, java.util.Collection<V>> asJava(C multimap, ChangePolicy changePolicy) {
        return TODO("new MapView<>(map, changePolicy.isMutable());");
    }

    TODO: create interface javaslang.collection.SortedMultimap
    static <K, V, C extends SortedMultimap<K, V>> NavigableMapView<K, java.util.Collection<V>> asJava(C sortedMultimap, ChangePolicy changePolicy) {
        return TODO("new NavigableMapView<>(map, changePolicy.isMutable());");
    }
    */

    enum ChangePolicy {

        IMMUTABLE, MUTABLE;

        boolean isMutable() {
            return this == MUTABLE;
        }
    }

    // -- private view implementations

    /**
     * Encapsulates the access to delegate and performs mutability checks.
     *
     * @param <C> The Javaslang collection type
     */
    private static abstract class HasDelegate<C extends Traversable<?>> implements Serializable {

        private static final long serialVersionUID = 1L;

        private C delegate;
        private final boolean mutable;

        HasDelegate(C delegate, boolean mutable) {
            this.delegate = delegate;
            this.mutable = mutable;
        }

        protected boolean isMutable() {
            return mutable;
        }

        C getDelegate() {
            return delegate;
        }

        protected boolean setDelegateAndCheckChanged(Supplier<C> delegate) {
            ensureMutable();
            final C previousDelegate = this.delegate;
            final C newDelegate = delegate.get();
            final boolean changed = newDelegate != previousDelegate;
            if (changed) {
                this.delegate = newDelegate;
            }
            return changed;
        }

        protected void setDelegate(Supplier<C> newDelegate) {
            ensureMutable();
            this.delegate = newDelegate.get();
        }

        protected void ensureMutable() {
            if (!mutable) {
                throw new UnsupportedOperationException();
            }
        }
    }

    static class ListView<T, C extends Seq<T>> extends HasDelegate<C> implements java.util.List<T> {

        private static final long serialVersionUID = 1L;

        ListView(C delegate, boolean mutable) {
            super(delegate, mutable);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean add(T element) {
            return setDelegateAndCheckChanged(() -> (C) getDelegate().append(element));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void add(int index, T element) {
            setDelegate(() -> (C) getDelegate().insert(index, element));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean addAll(Collection<? extends T> collection) {
            Objects.requireNonNull(collection, "collection is null");
            return setDelegateAndCheckChanged(() -> (C) getDelegate().appendAll(collection));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean addAll(int index, Collection<? extends T> collection) {
            Objects.requireNonNull(collection, "collection is null");
            return setDelegateAndCheckChanged(() -> (C) getDelegate().insertAll(index, collection));
        }

        @SuppressWarnings("unchecked")
        @Override
        public void clear() {
            setDelegate(() -> (C) getDelegate().take(0));
        }

        @Override
        public boolean contains(Object obj) {
            @SuppressWarnings("unchecked") final T that = (T) obj;
            return getDelegate().contains(that);
        }

        @Override
        public boolean containsAll(Collection<?> collection) {
            Objects.requireNonNull(collection, "collection is null");
            @SuppressWarnings("unchecked") final Collection<T> that = (Collection<T>) collection;
            return getDelegate().containsAll(that);
        }

        @Override
        public T get(int index) {
            return getDelegate().get(index);
        }

        @Override
        public int indexOf(Object obj) {
            @SuppressWarnings("unchecked") final T that = (T) obj;
            return getDelegate().indexOf(that);
        }

        @Override
        public boolean isEmpty() {
            return getDelegate().isEmpty();
        }

        @Override
        public Iterator<T> iterator() {
            return getDelegate().iterator();
        }

        @Override
        public int lastIndexOf(Object obj) {
            @SuppressWarnings("unchecked") final T that = (T) obj;
            return getDelegate().lastIndexOf(that);
        }

        @Override
        public java.util.ListIterator<T> listIterator() {
            return new ListIterator<>(this, 0);
        }

        @Override
        public java.util.ListIterator<T> listIterator(int index) {
            return new ListIterator<>(this, index);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T remove(int index) {
            return setDelegateAndGetPreviousElement(index, () -> (C) getDelegate().removeAt(index));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean remove(Object obj) {
            @SuppressWarnings("unchecked") final T that = (T) obj;
            return setDelegateAndCheckChanged(() -> (C) getDelegate().remove(that));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean removeAll(Collection<?> collection) {
            Objects.requireNonNull(collection, "collection is null");
            @SuppressWarnings("unchecked") final Collection<T> that = (Collection<T>) collection;
            return setDelegateAndCheckChanged(() -> (C) getDelegate().removeAll(that));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean retainAll(Collection<?> collection) {
            Objects.requireNonNull(collection, "collection is null");
            @SuppressWarnings("unchecked") final Collection<T> that = (Collection<T>) collection;
            return setDelegateAndCheckChanged(() -> (C) getDelegate().retainAll(that));
        }

        @SuppressWarnings("unchecked")
        @Override
        public T set(int index, T element) {
            return setDelegateAndGetPreviousElement(index, () -> (C) getDelegate().update(index, element));
        }

        @Override
        public int size() {
            return getDelegate().size();
        }

        @Override
        public List<T> subList(int fromIndex, int toIndex) {
            return new ListView<>(getDelegate().subSequence(fromIndex, toIndex), isMutable());
        }

        @Override
        public Object[] toArray() {
            return getDelegate().toJavaArray();
        }

        @GwtIncompatible("reflection is not supported")
        @SuppressWarnings("unchecked")
        @Override
        public <U> U[] toArray(U[] array) {
            Objects.requireNonNull(array, "array is null");
            final U[] target;
            final int length = getDelegate().length();
            if (array.length < length) {
                final Class<? extends Object[]> newType = array.getClass();
                target = (newType == Object[].class)
                         ? (U[]) new Object[length]
                         : (U[]) java.lang.reflect.Array.newInstance(newType.getComponentType(), length);
            } else {
                if (array.length > length) {
                    array[length] = null;
                }
                target = array;
            }
            final Iterator<T> iter = iterator();
            for (int i = 0; i < length; i++) {
                target[i] = (U) iter.next();
            }
            return target;
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof java.util.List) {
                return Collections.areEqual(getDelegate(), (java.util.List<?>) o);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            // DEV-NOTE: Ensures that hashCode calculation is stable, regardless of delegate.hashCode()
            return Collections.hash(getDelegate());
        }

        @Override
        public String toString() {
            return getDelegate().mkString("[", ", ", "]");
        }

        // -- private helpers

        private T setDelegateAndGetPreviousElement(int index, Supplier<C> delegate) {
            ensureMutable();
            final T previousElement = getDelegate().get(index);
            setDelegate(delegate);
            return previousElement;
        }

        // DEV-NOTE: ListIterator is intentionally not Serializable
        private static class ListIterator<T, C extends Seq<T>> implements java.util.ListIterator<T> {

            private ListView<T, C> list;
            private int index;
            private boolean dirty = true;

            ListIterator(ListView<T, C> list, int index) {
                if (index < 0 || index > list.size()) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + list.size());
                }
                this.list = list;
                this.index = index;
            }

            @Override
            public boolean hasNext() {
                return index < list.size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                dirty = false;
                return list.get(index++);
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
                if (!hasPrevious()) {
                    throw new NoSuchElementException();
                }
                dirty = false;
                return list.get(--index);
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                checkDirty();
                list.remove(index);
                dirty = true;
            }

            @Override
            public void set(T value) {
                checkDirty();
                list.set(index, value);
            }

            @Override
            public void add(T value) {
                /* TODO:
                 * The new element is inserted before the implicit
                 * cursor: a subsequent call to {@code next} would be unaffected, and a
                 * subsequent call to {@code previous} would return the new element.
                 * (This call increases by one the value that would be returned by a
                 * call to {@code nextIndex} or {@code previousIndex}.)
                 */
                // may throw a ClassCastException accordingly to j.u.ListIterator.add(T)
                list.add(index++, value);
                dirty = true;
            }

            private void checkDirty() {
                if (dirty) {
                    throw new IllegalStateException();
                }
            }
        }
    }

    static abstract class SetView<T> extends HasDelegate<Set<T>> implements java.util.Set<T> {

        private static final long serialVersionUID = 1L;

        SetView(Set<T> delegate, boolean mutable) {
            super(delegate, mutable);
        }

        // TODO
    }

    static abstract class NavigableSetView<T> extends HasDelegate<SortedSet<T>> implements java.util.NavigableSet<T> {

        private static final long serialVersionUID = 1L;

        NavigableSetView(SortedSet<T> delegate, boolean mutable) {
            super(delegate, mutable);
        }

        // TODO
    }

    static abstract class MapView<K, V> extends HasDelegate<Map<K, V>> implements java.util.Map<K, V> {

        private static final long serialVersionUID = 1L;

        MapView(Map<K, V> delegate, boolean mutable) {
            super(delegate, mutable);
        }

        // TODO
    }

    static abstract class NavigableMapView<K, V> extends HasDelegate<SortedMap<K, V>> implements java.util.NavigableMap<K, V> {

        private static final long serialVersionUID = 1L;

        NavigableMapView(SortedMap<K, V> delegate, boolean mutable) {
            super(delegate, mutable);
        }

        // TODO
    }
}
