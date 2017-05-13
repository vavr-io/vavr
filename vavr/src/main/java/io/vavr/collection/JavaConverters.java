/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * THIS CLASS IS INTENDED TO BE USED INTERNALLY ONLY!
 * <p>
 * This helper class provides methods that return views on Java collections.
 * The view creation and back conversion take O(1).
 *
 * @author Daniel Dietrich
 */
class JavaConverters {

    private JavaConverters() {
    }

    @GwtIncompatible
    static <T, C extends Seq<T>> ListView<T, C> asJava(C seq, ChangePolicy changePolicy) {
        return new ListView<>(seq, changePolicy.isMutable());
    }

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
     * @param <C> The Vavr collection type
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
            final boolean changed = newDelegate.size() != previousDelegate.size();
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

    @GwtIncompatible("reflection is not supported")
    static class ListView<T, C extends Seq<T>> extends HasDelegate<C> implements java.util.List<T> {

        private static final long serialVersionUID = 1L;

        ListView(C delegate, boolean mutable) {
            super(delegate, mutable);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean add(T element) {
            setDelegate(() -> (C) getDelegate().append(element));
            return true;
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
            // DEV-NOTE: acts like Java: works for empty immutable collections
            if (isEmpty()) {
                return;
            }
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
        public java.util.Iterator<T> iterator() {
            return new Iterator<>(this);
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
            final T that = (T) obj;
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

        @SuppressWarnings("unchecked")
        @Override
        public void sort(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator, "comparator is null");
            if (isEmpty()) {
                return;
            }
            setDelegate(() -> (C) getDelegate().sorted(comparator));
        }

        @Override
        public java.util.List<T> subList(int fromIndex, int toIndex) {
            return new ListView<>(getDelegate().subSequence(fromIndex, toIndex), isMutable());
        }

        @Override
        public Object[] toArray() {
            return getDelegate().toJavaArray();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <U> U[] toArray(U[] array) {
            Objects.requireNonNull(array, "array is null");
            final U[] target;
            final C delegate = getDelegate();
            final int length = delegate.length();
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
            final java.util.Iterator<T> iter = delegate.iterator();
            for (int i = 0; i < length; i++) {
                target[i] = (U) iter.next();
            }
            return target;
        }

        // -- Object.*

        @Override
        public boolean equals(Object o) {
            return o == this || o instanceof java.util.List && Collections.areEqual(getDelegate(), (java.util.List<?>) o);
        }

        @Override
        public int hashCode() {
            // DEV-NOTE: Ensures that hashCode calculation is stable, regardless of delegate.hashCode()
            return Collections.hashOrdered(getDelegate());
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

        // DEV-NOTE: Iterator is intentionally not Serializable
        private static class Iterator<T, C extends Seq<T>> implements java.util.Iterator<T> {

            ListView<T, C> list;
            int expectedSize;
            int nextIndex = 0;
            int lastIndex = -1;

            Iterator(ListView<T, C> list) {
                this.list = list;
                expectedSize = list.size();
            }

            @Override
            public boolean hasNext() {
                return nextIndex != list.size();
            }

            @Override
            public T next() {
                checkForComodification();
                if (nextIndex >= list.size()) {
                    throw new NoSuchElementException();
                }
                try {
                    return list.get(lastIndex = nextIndex++);
                } catch (IndexOutOfBoundsException x) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public void remove() {
                list.ensureMutable();
                if (lastIndex < 0) {
                    throw new IllegalStateException();
                }
                checkForComodification();
                try {
                    list.remove(nextIndex = lastIndex);
                    lastIndex = -1;
                    expectedSize = list.size();
                } catch (IndexOutOfBoundsException x) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public void forEachRemaining(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer, "consumer is  null");
                checkForComodification();
                if (nextIndex >= list.size()) {
                    return;
                }
                int index = nextIndex;
                // DEV-NOTE: intentionally not using hasNext() and next() in order not to modify internal state
                while (expectedSize == list.size() && index < expectedSize) {
                    consumer.accept(list.get(index++));
                }
                nextIndex = index;
                lastIndex = index - 1;
                checkForComodification();
            }

            final void checkForComodification() {
                if (expectedSize != list.size()) {
                    throw new ConcurrentModificationException();
                }
            }
        }

        // DEV-NOTE: ListIterator is intentionally not Serializable
        private static class ListIterator<T, C extends Seq<T>> extends ListView.Iterator<T, C> implements java.util.ListIterator<T> {

            ListIterator(ListView<T, C> list, int index) {
                super(list);
                if (index < 0 || index > list.size()) {
                    throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + list.size());
                }
                this.nextIndex = index;
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex != 0;
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex - 1;
            }

            @Override
            public T previous() {
                checkForComodification();
                final int index = nextIndex - 1;
                if (index < 0) {
                    throw new NoSuchElementException();
                }
                if (index >= list.size()) {
                    throw new ConcurrentModificationException();
                }
                try {
                    final T element = list.get(index);
                    // DEV-NOTE: intentionally updating indices _after_ reading the element. This makes a difference in case of a concurrent modification.
                    lastIndex = nextIndex = index;
                    return element;
                } catch (IndexOutOfBoundsException x) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public void set(T element) {
                list.ensureMutable();
                if (lastIndex < 0) {
                    throw new IllegalStateException();
                }
                checkForComodification();
                try {
                    list.set(lastIndex, element);
                } catch (IndexOutOfBoundsException x) {
                    throw new ConcurrentModificationException();
                }
            }

            @Override
            public void add(T element) {
                list.ensureMutable();
                checkForComodification();
                try {
                    final int index = nextIndex;
                    list.add(index, element);
                    // DEV-NOTE: intentionally increasing nextIndex _after_ adding the element. This makes a difference in case of a concurrent modification.
                    nextIndex = index + 1;
                    lastIndex = -1;
                    expectedSize = list.size();
                } catch (IndexOutOfBoundsException ex) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }
}
