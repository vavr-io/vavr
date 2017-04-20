/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.collection.JavaConverters.ListView;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.NoSuchElementException;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(Enclosed.class)
public class JavaConvertersTest {

    @RunWith(Parameterized.class)
    public static class ListViewImmutableTest extends ListViewTest {

        public ListViewImmutableTest(String name, ListFactory listFactory) {
            super(listFactory);
        }

        @Parameterized.Parameters(name = "{index}: {0}")
        public static java.util.Collection<Object[]> data() {
            return java.util.Arrays.asList(new Object[][] {
                    { "java.util.Arrays$ArrayList", new ListFactory(java.util.Arrays::asList) },
                    { List.class.getName(), new ListFactory(ts -> List.of(ts).asJavaImmutable()) },
                    { Vector.class.getName(), new ListFactory(ts -> Vector.of(ts).asJavaImmutable()) }
            });
        }
    }

    @RunWith(Parameterized.class)
    public static class ListViewMutableTest extends ListViewTest {

        public ListViewMutableTest(String name, ListFactory listFactory) {
            super(listFactory);
        }

        @Parameterized.Parameters(name = "{index}: {0}")
        public static java.util.Collection<Object[]> data() {
            return java.util.Arrays.asList(new Object[][] {
                    { java.util.ArrayList.class.getName(), new ListFactory(ts -> {
                        final java.util.List<Object> list = new java.util.ArrayList<>();
                        java.util.Collections.addAll(list, ts);
                        return list;
                    }) },
                    { List.class.getName(), new ListFactory(ts -> List.of(ts).asJavaMutable()) },
                    { Vector.class.getName(), new ListFactory(ts -> Vector.of(ts).asJavaMutable()) }
            });
        }
    }
}

abstract class ListViewTest {

    private final ListFactory listFactory;

    protected ListViewTest(ListFactory listFactory) {
        this.listFactory = listFactory;
    }

    @SuppressWarnings("unchecked")
    protected final <T> java.util.List<T> empty() {
        return listFactory.of();
    }

    @SuppressWarnings("unchecked")
    protected final <T> java.util.List<T> ofNull() {
        return listFactory.of((T) null);
    }

    @SuppressWarnings("unchecked")
    protected final <T> java.util.List<T> of(T t) {
        return listFactory.of(t);
    }
    
    @SuppressWarnings("varargs")
    @SafeVarargs
    protected final <T> java.util.List<T> of(T... ts) {
        return listFactory.of(ts);
    }

    protected final <T> java.util.List<T> immutableList() {
        return java.util.Collections.emptyList();
    }

    protected final <T> java.util.List<T> immutableList(T t) {
        return java.util.Collections.singletonList(t);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    protected final <T> java.util.List<T> immutableList(T... ts) {
        return java.util.Arrays.asList(ts);
    }

    // -- add(T)

    @Test
    public void shouldAddElementToEmptyListView() {
        ifSupported(() -> {
            final java.util.List<Integer> list = empty();
            assertThat(list.add(1)).isTrue();
            assertThat(list).isEqualTo(immutableList(1));
        });
    }

    @Test
    public void shouldAddElementToEndOfNonEmptyListView() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1);
            assertThat(list.add(2)).isTrue();
            assertThat(list).isEqualTo(immutableList(1, 2));
        });
    }

    @Test
    public void shouldAddSubtypeToEndOfNonEmptyListView() {
        abstract class A {
            @Override
            public final int hashCode() {
                return 0;
            }
        }
        final class B extends A {
            @Override
            public boolean equals(Object o) {
                return o instanceof B;
            }
        }
        final class C extends A {
            @Override
            public boolean equals(Object o) {
                return o instanceof C;
            }
        }
        ifSupported(() -> {
            final java.util.List<A> list = of(new B());
            assertThat(list.add(new C())).isTrue();
            assertThat(list).isEqualTo(immutableList(new B(), new C()));
        });
    }

    @Test
    public void shouldAddNull() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            assertThat(list.add(null)).isTrue();
            assertThat(list).isEqualTo(immutableList((Object) null));
        });
    }

    @Test
    public void shouldAddSelf() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            assertThat(list.add(list)).isTrue();
            assertThat(list).isEqualTo(list);
        });
    }

    // -- add(int, T)

    @Test
    public void shouldThrowWhenAddingElementAtNegativeIndexToEmpty() {
        ifSupported(() -> empty().add(-1, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingElementAtNonExistingIndexToEmpty() {
        ifSupported(() -> empty().add(1, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingElementAtNegativeIndexToNonEmpty() {
        ifSupported(() -> of(1).add(-1, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingElementAtNonExistingIndexToNonEmpty() {
        ifSupported(() -> of(1).add(2, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldAddNullToEmptyAtIndex0() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            list.add(0, null);
            assertThat(list).isEqualTo(immutableList((Object) null));
        });
    }

    @Test
    public void shouldAddNonNullToEmptyAtIndex0() {
        ifSupported(() -> {
            final java.util.List<Integer> list = empty();
            list.add(0, 1);
            assertThat(list).isEqualTo(immutableList(1));
        });
    }

    @Test
    public void shouldAddElementAtSizeIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1);
            list.add(1, 2);
            assertThat(list).isEqualTo(immutableList(1, 2));
        });
    }

    // -- addAll(Collection)

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenAddingAllNullCollectionToEmpty() {
        empty().addAll(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEWhenAddingAllNullCollectionToNonEmpty() {
        of(1).addAll(null);
    }

    @Test
    public void shouldReturnFalseIfAddAllEmptyToEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = empty();
            final java.util.List<Integer> javaList = immutableList();
            assertThat(list.addAll(javaList)).isFalse();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @Test
    public void shouldReturnFalseIfAddAllEmptyToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1);
            assertThat(list.addAll(immutableList())).isFalse();
            assertThat(list).isEqualTo(of(1));
        });
    }

    @Test
    public void shouldReturnTrueIfAddAllNonEmptyToEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = empty();
            final java.util.List<Integer> javaList = immutableList(1);
            assertThat(list.addAll(javaList)).isTrue();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @Test
    public void shouldReturnTrueIfAddAllNonEmptyToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1, 2, 3);
            assertThat(list.addAll(immutableList(1, 2, 3))).isTrue();
            assertThat(list).isEqualTo(immutableList(1, 2, 3, 1, 2, 3));
        });
    }

    // -- addAll(int, Collection)

    @Test
    public void shouldThrowNPEWhenAddingAllNullCollectionAtFirstIndexToEmpty() {
        assertThatThrownBy(() -> empty().addAll(0, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenAddingAllNullCollectionAtFirstIndexToNonEmpty() {
        assertThatThrownBy(() -> of(1).addAll(0, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowWhenAddingAllCollectionElementsAtNegativeIndexToEmpty() {
        ifSupported(() -> empty().addAll(-1, immutableList(1, 2, 3)), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingAllCollectionElementsAtNonExistingIndexToEmpty() {
        ifSupported(() -> empty().addAll(1, immutableList(1, 2, 3)), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingAllCollectionElementsAtNegativeIndexToNonEmpty() {
        ifSupported(() -> of(1).addAll(-1, immutableList(1, 2, 3)), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenAddingAllCollectionElementsAtNonExistingIndexToNonEmpty() {
        ifSupported(() -> of(1).addAll(2, immutableList(1, 2, 3)), IndexOutOfBoundsException.class);
    }


    @Test
    public void shouldAddAllCollectionElementsAtFirstIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(4);
            assertThat(list.addAll(0, immutableList(1, 2, 3))).isTrue();
            assertThat(list).isEqualTo(immutableList(1, 2, 3, 4));
        });
    }

    @Test
    public void shouldAddAllCollectionElementsAtSizeIndexToEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = empty();
            final java.util.List<Integer> javaList = immutableList(1, 2, 3);
            assertThat(list.addAll(0, javaList)).isTrue();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @Test
    public void shouldAddAllCollectionElementsAtSizeIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1);
            assertThat(list.addAll(1, immutableList(2, 3))).isTrue();
            assertThat(list).isEqualTo(of(1, 2, 3));
        });
    }

    // -- clear()

    @Test
    public void shouldThrowWhenCallingClearOnEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> empty = empty();
            empty.clear();
            assertThat(empty).isEqualTo(immutableList());
        });
    }

    @Test
    public void shouldThrowWhenCallingClearOnNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Integer> list = of(1);
            list.clear();
            assertThat(list).isEqualTo(immutableList());
        });
    }

    // -- contains(Object)

    @Test
    public void shouldRecognizeThatEmptyListViewDoesNotContainElement() {
        assertThat(empty().contains(1)).isFalse();
    }

    @Test
    public void shouldRecognizeThatNonEmptyListViewDoesNotContainElement() {
        assertThat(of(1).contains(2)).isFalse();
    }

    @Test
    public void shouldRecognizeThatNWhenEmptyListViewContainElement() {
        assertThat(of(1).contains(1)).isTrue();
    }

    @Test
    public void shouldEnsureThatEmptyListViewContainsPermitsNullElements() {
        assertThat(empty().contains(null)).isFalse();
    }

    @Test
    public void shouldEnsureThatNonEmptyListViewContainsPermitsNullElements() {
        assertThat(ofNull().contains(null)).isTrue();
    }

    @Test
    public void shouldBehaveLikeStandardJavaWhenCallingContainsOfIncompatibleTypeOnEmpty() {
        assertThat(empty().contains("")).isFalse();
    }

    @Test
    public void shouldBehaveLikeStandardJavaWhenCallingContainsOfIncompatibleTypeWhenNotEmpty() {
        assertThat(of(1).contains("")).isFalse();
    }

    @Test
    public void shouldRecognizeListContainsSelf() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            list.add(list);
            assertThat(list.contains(list)).isTrue();
        });
    }

    // -- containsAll(Collection)

    @Test
    public void shouldThrowNPEWhenCallingContainsAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().containsAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenCallingContainsAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).containsAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldRecognizeNonEmptyContainsAllEmpty() {
        assertThat(of(1).containsAll(immutableList())).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyContainsAllNonEmpty() {
        assertThat(of(1).containsAll(immutableList(1))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyNotContainsAllNonEmpty() {
        assertThat(of(1, 2).containsAll(immutableList(1, 3))).isFalse();
    }

    @Test
    public void shouldRecognizeEmptyContainsAllEmpty() {
        assertThat(empty().containsAll(immutableList())).isTrue();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnEmptyContainsAllGivenNull() {
        empty().containsAll(null);
    }

    @Test
    public void shouldRecognizeListContainsAllSelf() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            list.add(list);
            assertThat(list.containsAll(list)).isTrue();
        });
    }

    // -- equals(Object)

    @Test
    public void shouldRecognizeEqualsSame() {
        final java.util.List<Integer> list = of(1);
        assertThat(list.equals(list)).isTrue();
    }

    @Test
    public void shouldRecognizeEmptyEqualsEmpty() {
        assertThat(empty().equals(empty())).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyEqualsNonEmpty() {
        assertThat(of(1).equals(of(1))).isTrue();
    }

    @Test
    public void shouldRecognizeNonEmptyNotEqualsNonEmpty() {
        assertThat(of(1, 2).equals(of(1, 3))).isFalse();
    }

    @Test
    public void shouldRecognizeEmptyNotEqualsNonEmpty() {
        assertThat(empty().equals(of(1))).isFalse();
    }

    @Test
    public void shouldRecognizeNonEmptyNotEqualsEmpty() {
        assertThat(of(1).equals(empty())).isFalse();
    }

    @Test
    public void shouldRecognizeEmptyNotEqualsNull() {
        assertThat(empty().equals(null)).isFalse();
    }

    @Test
    public void shouldRecognizeNonEmptyNotEqualsNull() {
        assertThat(of(1).equals(null)).isFalse();
    }

    @Test
    public void shouldRecognizeNonEqualityOfSameValuesOfDifferentType() {
        assertThat(of(1).equals(of(1.0d))).isFalse();
    }

    @Test
    public void shouldRecognizeSelfEqualityOfListThatContainsItself() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            list.add(list);
            assertThat(list.equals(list)).isTrue();
        });
    }

    // -- get(int)

    @Test
    public void shouldThrowWhenEmptyGetWithNegativeIndex() {
        assertThatThrownBy(() -> empty().get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenEmptyGetWithIndexEqualsSize() {
        assertThatThrownBy(() -> empty().get(0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenNonEmptyGetWithNegativeIndex() {
        assertThatThrownBy(() -> of(1).get(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenNonEmptyGetWithIndexEqualsSize() {
        assertThatThrownBy(() -> of(1).get(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldGetAtFirstIndex() {
        assertThat(of(1, 2, 3).get(0)).isEqualTo(1);
    }

    @Test
    public void shouldGetAtLastIndex() {
        assertThat(of(1, 2, 3).get(2)).isEqualTo(3);
    }

    // -- hashCode()

    @Test
    public void shouldCalculateHashCodeOfEmptyLikeJava() {
        assertThat(empty().hashCode()).isEqualTo(immutableList().hashCode());
    }

    @Test
    public void shouldCalculateHashCodeOfNonEmptyLikeJava() {
        assertThat(of(1, 2, 3).hashCode()).isEqualTo(immutableList(1, 2, 3).hashCode());
    }

    @Test
    public void shouldThrowInsteadOfLoopingInfinitelyWhenComputingHashCodeOfListThatContainsItself() {
        ifSupported(() -> {
            final java.util.List<Object> list = empty();
            list.add(list);
            list.hashCode();
        }, StackOverflowError.class);
    }

    // -- indexOf(Object)

    @Test
    public void shouldReturnIndexOfNonExistingElementWhenEmpty() {
        assertThat(empty().indexOf(0)).isEqualTo(-1);
    }

    @Test
    public void shouldReturnIndexOfNonExistingElementWhenNonEmpty() {
        assertThat(of(1, 2, 3).indexOf(0)).isEqualTo(-1);
    }

    @Test
    public void shouldReturnIndexOfFirstOccurrenceWhenNonEmpty() {
        assertThat(of(1, 2, 3, 2).indexOf(2)).isEqualTo(1);
    }

    @Test
    public void shouldReturnIndexOfNullWhenNonEmpty() {
        assertThat(of(1, null, 2, null).indexOf(null)).isEqualTo(1);
    }

    @Test
    public void shouldReturnIndexOfWrongTypedElementWhenNonEmpty() {
        assertThat(of(1, null, 2).indexOf("a")).isEqualTo(-1);
    }

    // -- isEmpty()

    @Test
    public void shouldReturnTrueWhenCallingIsEmptyWhenEmpty() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnFalseWhenCallingIsEmptyWhenNotEmpty() {
        assertThat(of(1).isEmpty()).isFalse();
    }

    // -- iterator()

    @Test
    public void shouldReturnIteratorWhenEmpty() {
        assertThat(empty().iterator()).isNotNull();
    }

    @Test
    public void shouldReturnIteratorWhenNotEmpty() {
        assertThat(of(1).iterator()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyIteratorWhenEmpty() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @Test
    public void shouldReturnNonEmptyIteratorWhenNotEmpty() {
        assertThat(of(1).iterator().hasNext()).isTrue();
    }

    @Test
    public void shouldThrowWhenCallingNextOnIteratorWhenEmpty() {
        assertThatThrownBy(() -> empty().iterator().next())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldNotThrowWhenCallingNextOnIteratorWhenNotEmpty() {
        assertThat(of(1).iterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldThrowWhenCallingNextTooOftenOnIteratorWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1).iterator();
        assertThatThrownBy(() -> {
            iterator.next();
            iterator.next();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldIteratorAsExpectedWhenCallingIteratorWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1, 2, 3).iterator();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(3);
    }

    @Test
    public void shouldNotHaveNextWhenAllIteratorElementsWereConsumedByNext() {
        final java.util.Iterator<Integer> iterator = of(1).iterator();
        iterator.next();
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- iterator().remove()

    @Test
    public void shouldThrowWhenCallingRemoveOnEmptyIterator() {
        ifSupported(() -> empty().iterator().remove(), IllegalStateException.class);
    }

    // -- TODO: more iterator().remove() tests

    // -- lastIndexOf()

    @Test
    public void shouldReturnLastIndexOfNonExistingElementWhenEmpty() {
        assertThat(empty().lastIndexOf(0)).isEqualTo(-1);
    }

    @Test
    public void shouldReturnLastIndexOfNonExistingElementWhenNonEmpty() {
        assertThat(of(1, 2, 3).lastIndexOf(0)).isEqualTo(-1);
    }

    @Test
    public void shouldReturnLastIndexOfFirstOccurrenceWhenNonEmpty() {
        assertThat(of(1, 2, 3, 2).lastIndexOf(2)).isEqualTo(3);
    }

    @Test
    public void shouldReturnLastIndexOfNullWhenNonEmpty() {
        assertThat(of(1, null, 2, null).lastIndexOf(null)).isEqualTo(3);
    }

    @Test
    public void shouldReturnLastIndexOfWrongTypedElementWhenNonEmpty() {
        assertThat(of(1, null, 2).lastIndexOf("a")).isEqualTo(-1);
    }

    // -- listIterator()

    @Test
    public void shouldReturnListIteratorWhenEmpty() {
        assertThat(empty().listIterator()).isNotNull();
    }

    @Test
    public void shouldReturnListIteratorWhenNotEmpty() {
        assertThat(of(1).listIterator()).isNotNull();
    }

    @Test
    public void shouldReturnEmptyListIteratorWhenEmpty() {
        assertThat(empty().listIterator().hasNext()).isFalse();
    }

    @Test
    public void shouldReturnNonEmptyListIteratorWhenNotEmpty() {
        assertThat(of(1).listIterator().hasNext()).isTrue();
    }

    @Test
    public void shouldThrowWhenCallingNextOnListIteratorWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator().next())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldNotThrowWhenCallingNextOnListIteratorWhenNotEmpty() {
        assertThat(of(1).listIterator().next()).isEqualTo(1);
    }

    @Test
    public void shouldThrowWhenCallingNextTooOftenOnListIteratorWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1).listIterator();
        assertThatThrownBy(() -> {
            iterator.next();
            iterator.next();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldIteratorAsExpectedWhenCallingListIteratorWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1, 2, 3).listIterator();
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(3);
    }

    @Test
    public void shouldNotHaveNextWhenAllListIteratorElementsWereConsumedByNext() {
        final java.util.Iterator<Integer> iterator = of(1).listIterator();
        iterator.next();
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- listIterator(int)

    @Test
    public void shouldThrowWhenListIteratorAtNegativeIndexWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenListIteratorAtNegativeIndexWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).listIterator(-1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldNotThrowWhenListIteratorAtSizeIndexWhenEmpty() {
        assertThat(empty().listIterator(0)).isNotNull();
    }

    @Test
    public void shouldNotThrowWhenListIteratorAtSizeIndexWhenNotEmpty() {
        assertThat(of(1).listIterator(1)).isNotNull();
    }

    @Test
    public void shouldThrowWhenListIteratorAtIndexGreaterSizeWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(1))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenListIteratorAtIndexGreaterSizeWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).listIterator(2))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldReturnEmptyListIteratorAtFirstIndexWhenEmpty() {
        assertThat(empty().listIterator(0).hasNext()).isFalse();
    }

    @Test
    public void shouldReturnNonEmptyListIteratorAtFirstIndexWhenNotEmpty() {
        assertThat(of(1).listIterator(0).hasNext()).isTrue();
    }

    @Test
    public void shouldThrowWhenCallingNextOnListIteratorAtFirstIndexWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(0).next())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldNotThrowWhenCallingNextOnListIteratorAtFirstIndexWhenNotEmpty() {
        assertThat(of(1).listIterator(0).next()).isEqualTo(1);
    }

    @Test
    public void shouldThrowWhenCallingNextTooOftenOnListIteratorAtFirstIndexWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1).listIterator(0);
        assertThatThrownBy(() -> {
            iterator.next();
            iterator.next();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldIteratorAsExpectedWhenCallingListIteratorAtFirstIndexWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1, 2, 3).listIterator(0);
        assertThat(iterator.next()).isEqualTo(1);
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(3);
    }

    @Test
    public void shouldIteratorAsExpectedWhenCallingListIteratorAtNonFirstIndexWhenNotEmpty() {
        final java.util.Iterator<Integer> iterator = of(1, 2, 3).listIterator(1);
        assertThat(iterator.next()).isEqualTo(2);
        assertThat(iterator.next()).isEqualTo(3);
    }

    @Test
    public void shouldNotHaveNextWhenAllListIteratorElementsAtFirstIndexWereConsumedByNext() {
        final java.util.Iterator<Integer> iterator = of(1).listIterator(0);
        iterator.next();
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- TODO: listIterator().add()/.remove()/.set()

    // -- TODO: listIterator(int).add()/.remove()/.set()

    // -- remove(int)

    @Test
    public void shouldThrowWhenRemovingElementAtNegativeIndexWhenEmpty() {
        ifSupported(() -> empty().remove(-1), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenRemovingElementAtNegativeIndexWhenNotEmpty() {
        ifSupported(() -> of(1).remove(-1), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenRemovingElementAtSizeIndexWhenEmpty() {
        ifSupported(() -> empty().remove(0), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenRemovingElementAtSizeIndexWhenNotEmpty() {
        ifSupported(() -> of(1).remove(1), IndexOutOfBoundsException.class);
    }

    // TODO: more remove(int) tests

    // -- TODO: remove(Object)

    // -- removeAll(Collection)

    @Test
    public void shouldThrowNPEWhenCallingRemoveAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().removeAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenCallingRemoveAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).removeAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    // TODO: more removeAll(Collection) tests

    // -- TODO: replaceAll(UnaryOperator)

    // -- retainAll(Collection)

    @Test
    public void shouldThrowNPEWhenCallingRetainAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().retainAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenCallingRetainAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).retainAll(null))
                .isInstanceOf(NullPointerException.class);
    }

    // -- TODO: more retainAll(Collection) tests

    // -- set(int, T)

    @Test
    public void shouldThrowWhenSettingElementAtNegativeIndexWhenEmpty() {
        ifSupported(() -> empty().set(-1, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenSettingElementAtNegativeIndexWhenNotEmpty() {
        ifSupported(() -> of(1).set(-1, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenSettingElementAtSizeIndexWhenEmpty() {
        ifSupported(() -> empty().set(0, null), IndexOutOfBoundsException.class);
    }

    @Test
    public void shouldThrowWhenSettingElementAtSizeIndexWhenNotEmpty() {
        ifSupported(() -> of(1).set(1, null), IndexOutOfBoundsException.class);
    }

    // TODO: more set(int, T) tests

    // -- size()

    @Test
    public void shouldReturnSizeOfEmpty() {
        assertThat(empty().size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnSizeOfNonEmpty() {
        assertThat(of(1, 2, 3).size()).isEqualTo(3);
    }

    // -- TODO: sort(Comparator)

    // -- TODO: spliterator()

    // -- TODO: subList(int, int)

    // -- TODO: toArray()

    // -- TODO: toArray(T[])

    @Test
    public void shouldThrowNPEWhenCallingToArrayNullWhenEmpty() {
        assertThatThrownBy(() -> empty().toArray(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldThrowNPEWhenCallingToArrayNullWhenNotEmpty() {
        assertThatThrownBy(() -> of(1).toArray(null))
                .isInstanceOf(NullPointerException.class);
    }

    // --- helpers

    @SafeVarargs
    private final void ifSupported(Runnable test, Class<? extends Throwable>... expectedExceptionTypes) {
        try {
            test.run();
            if (!isMutable()) {
                Assert.fail("Operation should throw " + UnsupportedOperationException.class.getName());
            }
        } catch (Throwable x) {
            if (!isMutable()) {
                if (!(x instanceof UnsupportedOperationException) && isJavaslang()) {
                    Assert.fail("Operation should throw " + UnsupportedOperationException.class.getName() + " but found " + x.getClass().getName());
                }
            } else {
                final Class<? extends Throwable> actualType = x.getClass();
                for (Class<? extends Throwable> expectedType : expectedExceptionTypes) {
                    if (expectedType.isAssignableFrom(actualType)) {
                        return;
                    }
                }
                throw x;
            }
        }
    }

    private boolean isJavaslang() {
        return listFactory.of().getClass().getName().startsWith("javaslang.collection.");
    }

    private boolean isMutable() {
        final java.util.List<?> list = listFactory.of();
        return isJavaslang() ? ((ListView<?, ?>) list).isMutable() : list instanceof java.util.ArrayList;
    }

    static final class ListFactory {

        private final Function<Object[], java.util.List<Object>> listFactory;

        ListFactory(Function<Object[], java.util.List<Object>> listFactory) {
            this.listFactory = listFactory;
        }

        @SuppressWarnings("unchecked")
        <T> java.util.List<T> of(T... elements) {
            return (java.util.List<T>) listFactory.apply(elements);
        }
    }
}
