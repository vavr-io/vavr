/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2025 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import java.util.*;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContext;
import org.junit.jupiter.api.extension.TestTemplateInvocationContextProvider;

import static io.vavr.collection.JavaConvertersTest.ChangePolicy.IMMUTABLE;
import static io.vavr.collection.JavaConvertersTest.ChangePolicy.MUTABLE;
import static io.vavr.collection.JavaConvertersTest.ElementNullability.NON_NULLABLE;
import static io.vavr.collection.JavaConvertersTest.ElementNullability.NULLABLE;
import static io.vavr.collection.JavaConvertersTest.ElementType.FIXED;
import static io.vavr.collection.JavaConvertersTest.ElementType.GENERIC;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(JavaConvertersTest.TestTemplateProvider.class)
public class JavaConvertersTest {

    @SuppressWarnings("unchecked")
    public static java.util.List<Data> data() {
        return asList(
          // -- immutable classes
          new Data("java.util.Arrays$ArrayList", new ListFactory(java.util.Arrays::asList), IMMUTABLE, GENERIC, NULLABLE),
          new Data(Array.class.getName(), new ListFactory(ts -> Array.of(ts).asJava()), IMMUTABLE, GENERIC, NULLABLE),
          new Data(CharSeq.class.getName(), new ListFactory(ts -> (java.util.List<Object>) (Object) CharSeq.ofAll((List<Character>) (Object) (List.of(ts))).asJava()), IMMUTABLE, FIXED, NON_NULLABLE),
          new Data(List.class.getName(), new ListFactory(ts -> List.of(ts).asJava()), IMMUTABLE, GENERIC, NULLABLE),
          new Data(Queue.class.getName(), new ListFactory(ts -> Queue.of(ts).asJava()), IMMUTABLE, GENERIC, NULLABLE),
          new Data(Stream.class.getName(), new ListFactory(ts -> Stream.of(ts).asJava()), IMMUTABLE, GENERIC, NULLABLE),
          new Data(Vector.class.getName(), new ListFactory(ts -> Vector.of(ts).asJava()), IMMUTABLE, GENERIC, NULLABLE),

          // -- mutable classes
          new Data(java.util.ArrayList.class.getName(), new ListFactory(ts -> {
              final java.util.List<Object> list = new java.util.ArrayList<>();
              java.util.Collections.addAll(list, ts);
              return list;
          }), MUTABLE, GENERIC, NULLABLE),
          new Data(Array.class.getName(), new ListFactory(ts -> Array.of(ts).asJavaMutable()), MUTABLE, GENERIC, NULLABLE),
          new Data(CharSeq.class.getName(), new ListFactory(ts -> (java.util.List<Object>) (Object) CharSeq.ofAll((List<Character>) (Object) (List.of(ts))).asJavaMutable()), MUTABLE, FIXED, NON_NULLABLE),
          new Data(List.class.getName(), new ListFactory(ts -> List.of(ts).asJavaMutable()), MUTABLE, GENERIC, NULLABLE),
          new Data(Queue.class.getName(), new ListFactory(ts -> Queue.of(ts).asJavaMutable()), MUTABLE, GENERIC, NULLABLE),
          new Data(Stream.class.getName(), new ListFactory(ts -> Stream.of(ts).asJavaMutable()), MUTABLE, GENERIC, NULLABLE),
          new Data(Vector.class.getName(), new ListFactory(ts -> Vector.of(ts).asJavaMutable()), MUTABLE, GENERIC, NULLABLE)
        );
    }

    private ListFactory listFactory;
    private ChangePolicy changePolicy;
    private ElementType elementType;
    private ElementNullability elementNullability;

    static class TestTemplateProvider implements TestTemplateInvocationContextProvider {

        @Override
        public boolean supportsTestTemplate(ExtensionContext context) {
            return true;
        }

        @Override
        public java.util.stream.Stream<TestTemplateInvocationContext> provideTestTemplateInvocationContexts(ExtensionContext extensionContext) {
            return data().stream().map(data -> new TestTemplateInvocationContext() {
                @Override
                public String getDisplayName(int invocationIndex) {
                    return "[" + data.name + "]";
                }

                @Override
                public java.util.List<Extension> getAdditionalExtensions() {
                    return java.util.Collections.singletonList((BeforeTestExecutionCallback) context -> {
                        JavaConvertersTest instance = (JavaConvertersTest) context.getRequiredTestInstance();
                        instance.listFactory = data.listFactory;
                        instance.changePolicy = data.changePolicy;
                        instance.elementType = data.elementType;
                        instance.elementNullability = data.elementNullability;
                    });
                }
            });
        }
    }

    private static class Data {
        private final String name;
        private final ListFactory listFactory;
        private final ChangePolicy changePolicy;
        private final ElementType elementType;
        private final ElementNullability elementNullability;

        private Data(String name, ListFactory listFactory, ChangePolicy changePolicy, ElementType elementType, ElementNullability elementNullability) {
            this.name = name;
            this.listFactory = listFactory;
            this.changePolicy = changePolicy;
            this.elementType = elementType;
            this.elementNullability = elementNullability;
        }
    }

    @SuppressWarnings("unchecked")
    private <T> java.util.List<T> empty() {
        return listFactory.of();
    }

    @SuppressWarnings("unchecked")
    private <T> java.util.List<T> ofNull() {
        return listFactory.of((T) null);
    }

    @SuppressWarnings("unchecked")
    private <T> java.util.List<T> of(T t) {
        return listFactory.of(t);
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    private final <T> java.util.List<T> of(T... ts) {
        return listFactory.of(ts);
    }

    // -- add(T)

    @TestTemplate
    public void shouldAddElementToEmptyListView() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            assertThat(list.add('1')).isTrue();
            assertThat(list).isEqualTo(asList('1'));
        });
    }

    @TestTemplate
    public void shouldAddElementToEndOfNonEmptyListView() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.add('2')).isTrue();
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    @TestTemplate
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
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<A> list = of(new B());
                assertThat(list.add(new C())).isTrue();
                assertThat(list).isEqualTo(asList(new B(), new C()));
            });
        }
    }

    @TestTemplate
    public void shouldAddNull() {
        if (elementNullability == NULLABLE) {
            ifSupported(() -> {
                final java.util.List<Character> list = empty();
                assertThat(list.add(null)).isTrue();
                assertThat(list).isEqualTo(asList((Object) null));
            });
        }
    }

    @TestTemplate
    public void shouldAddSelf() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Object> list = empty();
                assertThat(list.add(list)).isTrue();
                assertThat(list).isEqualTo(list);
            });
        }
    }

    // -- add(int, T)

    @TestTemplate
    public void shouldThrowWhenAddingElementAtNegativeIndexToEmpty() {
        ifSupported(() -> empty().add(-1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingElementAtNonExistingIndexToEmpty() {
        ifSupported(() -> empty().add(1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingElementAtNegativeIndexToNonEmpty() {
        ifSupported(() -> of('1').add(-1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingElementAtNonExistingIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            // should throw for eagerly evaluated collections
            list.add(2, null);
            // afterburner for lazy persistent collections
            list.size();
        }, IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldAddNullToEmptyAtIndex0() {
        if (elementNullability == NULLABLE) {
            ifSupported(() -> {
                final java.util.List<Character> list = empty();
                list.add(0, null);
                assertThat(list).isEqualTo(asList((Object) null));
            });
        }
    }

    @TestTemplate
    public void shouldAddNonNullToEmptyAtIndex0() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            list.add(0, '1');
            assertThat(list).isEqualTo(asList('1'));
        });
    }

    @TestTemplate
    public void shouldAddElementAtSizeIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            list.add(1, '2');
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    // -- addAll(Collection)

    @TestTemplate
    public void shouldThrowNPEWhenAddingAllNullCollectionToEmpty() {
        assertThrows(NullPointerException.class, () -> {
            empty().addAll(null);
        });
    }

    @TestTemplate
    public void shouldThrowNPEWhenAddingAllNullCollectionToNonEmpty() {
        assertThrows(NullPointerException.class, () -> {
            of('1').addAll(null);
        });
    }

    @TestTemplate
    public void shouldReturnFalseIfAddAllEmptyToEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            final java.util.List<Character> javaList = asList();
            assertThat(list.addAll(javaList)).isFalse();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @TestTemplate
    public void shouldReturnFalseIfAddAllEmptyToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.addAll(asList())).isFalse();
            assertThat(list).isEqualTo(of('1'));
        });
    }

    @TestTemplate
    public void shouldReturnTrueIfAddAllNonEmptyToEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            final java.util.List<Character> javaList = asList('1');
            assertThat(list.addAll(javaList)).isTrue();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @TestTemplate
    public void shouldReturnTrueIfAddAllNonEmptyToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.addAll(asList('1', '2', '3'))).isTrue();
            assertThat(list).isEqualTo(asList('1', '2', '3', '1', '2', '3'));
        });
    }

    // -- addAll(int, Collection)

    @TestTemplate
    public void shouldThrowNPEWhenAddingAllNullCollectionAtFirstIndexToEmpty() {
        assertThatThrownBy(() -> empty().addAll(0, null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowNPEWhenAddingAllNullCollectionAtFirstIndexToNonEmpty() {
        assertThatThrownBy(() -> of('1').addAll(0, null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingAllCollectionElementsAtNegativeIndexToEmpty() {
        ifSupported(() -> empty().addAll(-1, asList('1', '2', '3')), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingAllCollectionElementsAtNonExistingIndexToEmpty() {
        ifSupported(() -> empty().addAll(1, asList('1', '2', '3')), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingAllCollectionElementsAtNegativeIndexToNonEmpty() {
        ifSupported(() -> of('1').addAll(-1, asList('1', '2', '3')), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingAllCollectionElementsAtNonExistingIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            // should throw for eagerly evaluated collections
            list.addAll(2, asList('1', '2', '3'));
            // afterburner for lazy persistent collections
            list.size();
        }, IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldAddAllCollectionElementsAtFirstIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('4');
            assertThat(list.addAll(0, asList('1', '2', '3'))).isTrue();
            assertThat(list).isEqualTo(asList('1', '2', '3', '4'));
        });
    }

    @TestTemplate
    public void shouldAddAllCollectionElementsAtSizeIndexToEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            final java.util.List<Character> javaList = asList('1', '2', '3');
            assertThat(list.addAll(0, javaList)).isTrue();
            assertThat(list).isEqualTo(javaList);
        });
    }

    @TestTemplate
    public void shouldAddAllCollectionElementsAtSizeIndexToNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.addAll(1, asList('2', '3'))).isTrue();
            assertThat(list).isEqualTo(of('1', '2', '3'));
        });
    }

    // -- clear()

    @TestTemplate
    public void shouldThrowWhenCallingClearOnEmpty() {
        final java.util.List<Character> empty = empty();
        empty.clear();
        assertThat(empty).isEqualTo(asList());
    }

    @TestTemplate
    public void shouldThrowWhenCallingClearOnNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            list.clear();
            assertThat(list).isEqualTo(asList());
        });
    }

    // -- contains(Object)

    @TestTemplate
    public void shouldRecognizeThatEmptyListViewDoesNotContainElement() {
        assertThat(empty().contains('1')).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeThatNonEmptyListViewDoesNotContainElement() {
        assertThat(of('1').contains('2')).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeThatNWhenEmptyListViewContainElement() {
        assertThat(of('1').contains('1')).isTrue();
    }

    @TestTemplate
    public void shouldEnsureThatEmptyListViewContainsPermitsNullElements() {
        assertThat(empty().contains(null)).isFalse();
    }

    @TestTemplate
    public void shouldEnsureThatNonEmptyListViewContainsPermitsNullElements() {
        if (elementNullability == NULLABLE) {
            assertThat(ofNull().contains(null)).isTrue();
        }
    }

    @TestTemplate
    public void shouldBehaveLikeStandardJavaWhenCallingContainsOfIncompatibleTypeOnEmpty() {
        assertThat(empty().contains("")).isFalse();
    }

    @TestTemplate
    public void shouldBehaveLikeStandardJavaWhenCallingContainsOfIncompatibleTypeWhenNotEmpty() {
        assertThat(of('1').contains("")).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeListContainsSelf() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Object> list = empty();
                list.add(list);
                assertThat(list.contains(list)).isTrue();
            });
        }
    }

    // -- containsAll(Collection)

    @TestTemplate
    public void shouldThrowNPEWhenCallingContainsAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().containsAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowNPEWhenCallingContainsAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').containsAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyContainsAllEmpty() {
        assertThat(of('1').containsAll(asList())).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyContainsAllNonEmpty() {
        assertThat(of('1').containsAll(asList('1'))).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyNotContainsAllNonEmpty() {
        assertThat(of('1', '2').containsAll(asList('1', '3'))).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeEmptyContainsAllEmpty() {
        assertThat(empty().containsAll(asList())).isTrue();
    }

    @TestTemplate
    public void shouldThrowOnEmptyContainsAllGivenNull() {
        assertThrows(NullPointerException.class, () -> {
            empty().containsAll(null);
        });
    }

    @TestTemplate
    public void shouldRecognizeListContainsAllSelf() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Object> list = empty();
                list.add(list);
                assertThat(list.containsAll(list)).isTrue();
            });
        }
    }

    // -- equals(Object)

    @TestTemplate
    public void shouldRecognizeEqualsSame() {
        final java.util.List<Character> list = of('1');
        assertThat(list.equals(list)).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeEmptyEqualsEmpty() {
        assertThat(empty().equals(empty())).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyEqualsNonEmpty() {
        assertThat(of('1').equals(of('1'))).isTrue();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyNotEqualsNonEmpty() {
        assertThat(of('1', '2').equals(of('1', '3'))).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeEmptyNotEqualsNonEmpty() {
        assertThat(empty().equals(of('1'))).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyNotEqualsEmpty() {
        assertThat(of('1').equals(empty())).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeEmptyNotEqualsNull() {
        assertThat(empty().equals(null)).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonEmptyNotEqualsNull() {
        assertThat(of('1').equals(null)).isFalse();
    }

    @TestTemplate
    public void shouldRecognizeNonEqualityOfSameValuesOfDifferentType() {
        if (elementType == GENERIC) {
            assertThat(of(1).equals(of(1.0d))).isFalse();
        }
    }

    @TestTemplate
    public void shouldRecognizeSelfEqualityOfListThatContainsItself() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Object> list = empty();
                list.add(list);
                assertThat(list.equals(list)).isTrue();
            });
        }
    }

    // -- get(int)

    @TestTemplate
    public void shouldThrowWhenEmptyGetWithNegativeIndex() {
        assertThatThrownBy(() -> empty().get(-1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenEmptyGetWithIndexEqualsSize() {
        assertThatThrownBy(() -> empty().get(0))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenNonEmptyGetWithNegativeIndex() {
        assertThatThrownBy(() -> of('1').get(-1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenNonEmptyGetWithIndexEqualsSize() {
        assertThatThrownBy(() -> of('1').get(1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldGetAtFirstIndex() {
        assertThat(of('1', '2', '3').get(0)).isEqualTo('1');
    }

    @TestTemplate
    public void shouldGetAtLastIndex() {
        assertThat(of('1', '2', '3').get(2)).isEqualTo('3');
    }

    // -- hashCode()

    @TestTemplate
    public void shouldCalculateHashCodeOfEmptyLikeJava() {
        assertThat(empty().hashCode()).isEqualTo(asList().hashCode());
    }

    @TestTemplate
    public void shouldCalculateHashCodeOfNonEmptyLikeJava() {
        assertThat(of('1', '2', '3').hashCode()).isEqualTo(asList('1', '2', '3').hashCode());
    }

    @TestTemplate
    public void shouldThrowInsteadOfLoopingInfinitelyWhenComputingHashCodeOfListThatContainsItself() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Object> list = empty();
                list.add(list);
                list.hashCode();
            }, StackOverflowError.class);
        }
    }

    // -- indexOf(Object)

    @TestTemplate
    public void shouldReturnIndexOfNonExistingElementWhenEmpty() {
        assertThat(empty().indexOf('0')).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnIndexOfNonExistingElementWhenNonEmpty() {
        assertThat(of('1', '2', '3').indexOf('0')).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnIndexOfFirstOccurrenceWhenNonEmpty() {
        assertThat(of('1', '2', '3', '2').indexOf('2')).isEqualTo(1);
    }

    @TestTemplate
    public void shouldReturnIndexOfNullWhenNonEmpty() {
        if (elementNullability == NULLABLE) {
            assertThat(of('1', null, '2', null).indexOf(null)).isEqualTo(1);
        }
    }

    @TestTemplate
    public void shouldReturnIndexOfWrongTypedElementWhenNonEmpty() {
        if (elementType == GENERIC) {
            assertThat(of('1', '2').indexOf("a")).isEqualTo(-1);
        }
    }

    // -- isEmpty()

    @TestTemplate
    public void shouldReturnTrueWhenCallingIsEmptyWhenEmpty() {
        assertThat(empty().isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldReturnFalseWhenCallingIsEmptyWhenNotEmpty() {
        assertThat(of('1').isEmpty()).isFalse();
    }

    // -- iterator()

    @TestTemplate
    public void shouldReturnIteratorWhenEmpty() {
        assertThat(empty().iterator()).isNotNull();
    }

    @TestTemplate
    public void shouldReturnIteratorWhenNotEmpty() {
        assertThat(of('1').iterator()).isNotNull();
    }

    @TestTemplate
    public void shouldReturnEmptyIteratorWhenEmpty() {
        assertThat(empty().iterator().hasNext()).isFalse();
    }

    @TestTemplate
    public void shouldReturnNonEmptyIteratorWhenNotEmpty() {
        assertThat(of('1').iterator().hasNext()).isTrue();
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextOnIteratorWhenEmpty() {
        assertThatThrownBy(() -> empty().iterator().next())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldNotThrowWhenCallingNextOnIteratorWhenNotEmpty() {
        assertThat(of('1').iterator().next()).isEqualTo('1');
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextTooOftenOnIteratorWhenNotEmpty() {
        final java.util.Iterator<Character> iterator = of('1').iterator();
        iterator.next();
        assertThatThrownBy(iterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldIterateAsExpectedWhenCallingIteratorWhenNotEmpty() {
        final java.util.Iterator<Character> iterator = of('1', '2', '3').iterator();
        assertThat(iterator.next()).isEqualTo('1');
        assertThat(iterator.next()).isEqualTo('2');
        assertThat(iterator.next()).isEqualTo('3');
    }

    @TestTemplate
    public void shouldNotHaveNextWhenAllIteratorElementsWereConsumedByNext() {
        final java.util.Iterator<Character> iterator = of('1').iterator();
        iterator.next();
        assertThat(iterator.hasNext()).isFalse();
    }

    // -- iterator().forEachRemaining()

    @TestTemplate
    public void shouldPerformNoSideEffectForEachRemainingOfEmpty() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        this.<Character> empty().<Character> iterator().forEachRemaining(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldPerformNoSideEffectsForEachRemainingOfNonEmptyButAllIterated() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        final java.util.Iterator<Character> iterator = of('1').iterator();
        iterator.next();
        iterator.forEachRemaining(actual::add);
        assertThat(actual.isEmpty()).isTrue();
    }

    @TestTemplate
    public void shouldPerformSideEffectsForEachRemainingOfNonEmpty() {
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        final java.util.Iterator<Character> iterator = of('1', '2').iterator();
        iterator.next();
        iterator.forEachRemaining(actual::add);
        assertThat(actual).isEqualTo(asList('2'));
    }

    @TestTemplate
    public void shouldThrowWhenRemovingElementFromListWhileIteratingForEachRemaining() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final java.util.Iterator<Character> iterator = list.iterator();
            iterator.forEachRemaining(list::remove);
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldThrowWhenAddingElementFromListWhileIteratingForEachRemaining() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final java.util.Iterator<Character> iterator = list.iterator();
            iterator.forEachRemaining(list::add);
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldThrowWhenRemovingElementFromIteratorWhileIteratingForEachRemaining() {
        ifSupported(() -> {
            final java.util.Iterator<Character> iterator = of('1', '2').iterator();
            iterator.forEachRemaining(e -> iterator.remove());
        }, IllegalStateException.class);
    }

    // -- iterator().remove()

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnEmptyIterator() {
        ifSupported(() -> empty().iterator().remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnNonEmptyIteratorWithoutHavingCalledNext() {
        ifSupported(() -> of('1').iterator().remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveTwiceOnNonEmptyIteratorAfterHavingCalledNext() {
        ifSupported(() -> {
            final java.util.Iterator<Character> iter = of('1', '2', '3').iterator();
            iter.next();
            iter.remove();
            iter.remove(); // should fail
        }, IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenModifyingListWhileIteratingAndRemoving() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final java.util.Iterator<Character> iter = list.iterator();
            iter.next();
            list.add('4');
            iter.remove();
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldRemoveFirstListElementWhenIterating() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final java.util.Iterator<Character> iter = list.iterator();
            iter.next();
            iter.remove();
            assertThat(list).isEqualTo(asList('2', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveInnerListElementWhenIterating() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final java.util.Iterator<Character> iter = list.iterator();
            iter.next();
            iter.next();
            iter.remove();
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveLastListElementWhenIterating() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final java.util.Iterator<Character> iter = list.iterator();
            iter.next();
            iter.next();
            iter.next();
            iter.remove();
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    // -- lastIndexOf()

    @TestTemplate
    public void shouldReturnLastIndexOfNonExistingElementWhenEmpty() {
        assertThat(empty().lastIndexOf('0')).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnLastIndexOfNonExistingElementWhenNonEmpty() {
        assertThat(of('1', '2', '3').lastIndexOf('0')).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnLastIndexOfFirstOccurrenceWhenNonEmpty() {
        assertThat(of('1', '2', '3', '2').lastIndexOf('2')).isEqualTo(3);
    }

    @TestTemplate
    public void shouldReturnLastIndexOfNullWhenNonEmpty() {
        if (elementNullability == NULLABLE) {
            assertThat(of('1', null, '2', null).lastIndexOf(null)).isEqualTo(3);
        }
    }

    @TestTemplate
    public void shouldReturnLastIndexOfWrongTypedElementWhenNonEmpty() {
        if (elementType == GENERIC) {
            assertThat(of('1', null, '2').lastIndexOf("a")).isEqualTo(-1);
        }
    }

    // -- listIterator()

    @TestTemplate
    public void shouldReturnListIteratorWhenEmpty() {
        assertThat(empty().listIterator()).isNotNull();
    }

    @TestTemplate
    public void shouldReturnListIteratorWhenNotEmpty() {
        assertThat(of('1').listIterator()).isNotNull();
    }

    // -- listIterator().add()

    @TestTemplate
    public void shouldUseListIteratorToAddElementToEmptyList() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            list.listIterator().add('1');
            assertThat(list).isEqualTo(asList('1'));
        });
    }

    @TestTemplate
    public void shouldAddElementToListIteratorStart() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            list.listIterator().add('2');
            assertThat(list).isEqualTo(asList('2', '1'));
        });
    }

    @TestTemplate
    public void shouldAddingElementToListIteratorEnd() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.add('2');
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    @TestTemplate
    public void shouldAddElementHavingWrongTypeToListIterator() {
        if (elementType == GENERIC) {
            ifSupported(() -> {
                final java.util.List<Character> list = of('1');
                @SuppressWarnings("unchecked") final ListIterator<Object> listIterator = (ListIterator<Object>) (Object) list.listIterator();
                listIterator.add("x");
                assertThat(list).isEqualTo(asList("x", '1'));
            });
        }
    }

    @TestTemplate
    public void shouldReturnUnaffectedNextWhenCallingAddOnListIterator() {
        ifSupported(() -> {
            final ListIterator<Character> listIterator = of('1').listIterator();
            listIterator.add('2');
            assertThat(listIterator.next()).isEqualTo('1');
        });
    }

    @TestTemplate
    public void shouldReturnNewElementWhenCallingAddAndThenPreviousOnListIterator() {
        ifSupported(() -> {
            final ListIterator<Character> listIterator = of('1').listIterator();
            listIterator.next();
            listIterator.add('2');
            assertThat(listIterator.previous()).isEqualTo('2');
        });
    }

    @TestTemplate
    public void shouldIncreaseNextIndexByOneWhenCallingAddOnListIterator() {
        ifSupported(() -> {
            final ListIterator<Character> listIterator = of('1').listIterator();
            assertThat(listIterator.nextIndex()).isEqualTo(0);
            listIterator.add('2');
            assertThat(listIterator.nextIndex()).isEqualTo(1);
        });
    }

    @TestTemplate
    public void shouldIncreasePreviousIndexByOneWhenCallingAddOnListIterator() {
        ifSupported(() -> {
            final ListIterator<Character> listIterator = of('1').listIterator();
            assertThat(listIterator.previousIndex()).isEqualTo(-1);
            listIterator.add('2');
            assertThat(listIterator.previousIndex()).isEqualTo(0);
        });
    }

    // -- listIterator().hasNext()

    @TestTemplate
    public void shouldNotHaveNextWhenEmptyListIterator() {
        assertThat(empty().listIterator().hasNext()).isFalse();
    }

    @TestTemplate
    public void shouldHaveNextWhenNonEmptyListIterator() {
        assertThat(of('1').listIterator().hasNext()).isTrue();
    }

    @TestTemplate
    public void shouldNotHaveNextWhenAllListIteratorElementsWereConsumedByNext() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        assertThat(listIterator.hasNext()).isTrue();
        listIterator.next();
        assertThat(listIterator.hasNext()).isFalse();
    }

    // -- listIterator().next()

    @TestTemplate
    public void shouldThrowWhenCallingNextOnListIteratorWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator().next())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldNotThrowWhenCallingNextOnListIteratorWhenNotEmpty() {
        assertThat(of('1').listIterator().next()).isEqualTo('1');
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextTooOftenOnListIteratorWhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        listIterator.next();
        assertThatThrownBy(listIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldIterateAsExpectedWhenCallingListIteratorWhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1', '2', '3').listIterator();
        assertThat(listIterator.next()).isEqualTo('1');
        assertThat(listIterator.next()).isEqualTo('2');
        assertThat(listIterator.next()).isEqualTo('3');
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorNextAndListElementWasRemoved() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            assertThat(listIterator.hasNext()).isTrue();
            list.remove(0);
            listIterator.next();
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorNextAndListElementWasAdded() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            assertThat(listIterator.hasNext()).isTrue();
            list.add('2');
            listIterator.next();
        }, ConcurrentModificationException.class);
    }

    // -- listIterator().nextIndex()

    @TestTemplate
    public void shouldReturnNextIndexOfEmptyListIterator() {
        assertThat(empty().listIterator().nextIndex()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldReturnNextIndexOfNonEmptyListIterator() {
        assertThat(of('1').listIterator().nextIndex()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldReturnCorrectNextIndexOfListIteratorAfterIteratingAllElements() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        listIterator.next();
        assertThat(listIterator.nextIndex()).isEqualTo(1);
    }

    // -- listIterator().hasPrevious()

    @TestTemplate
    public void shouldNotHavePreviousWhenEmptyListIterator() {
        assertThat(empty().listIterator().hasPrevious()).isFalse();
    }

    @TestTemplate
    public void shouldNotHavePreviousWhenNonEmptyListIterator() {
        assertThat(of('1').listIterator().hasPrevious()).isFalse();
    }

    @TestTemplate
    public void shouldNotHavePreviousWhenAllListIteratorElementsWereConsumedByPrevious() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        listIterator.next();
        assertThat(listIterator.hasPrevious()).isTrue();
        listIterator.previous();
        assertThat(listIterator.hasPrevious()).isFalse();
    }

    // -- listIterator().previous()

    @TestTemplate
    public void shouldThrowWhenCallingPreviousOnListIteratorWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator().previous())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingPreviousOnListIteratorWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').listIterator().previous())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldRepeatedlyReturnTheSameElementWhenAlternatingNextAndPrevious() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        actual.add(listIterator.next());
        actual.add(listIterator.previous());
        actual.add(listIterator.next());
        actual.add(listIterator.previous());
        assertThat(actual).isEqualTo(asList('1', '1', '1', '1'));
    }

    @TestTemplate
    public void shouldIterateListIteratorBackwards() {
        final ListIterator<Character> listIterator = of('1', '2', '3', '4').listIterator();
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        while (listIterator.hasNext()) {
            listIterator.next();
        }
        while (listIterator.hasPrevious()) {
            actual.add(listIterator.previous());
        }
        assertThat(actual).isEqualTo(asList('4', '3', '2', '1'));
    }

    @TestTemplate
    public void shouldThrowWhenCallingPreviousTooOftenOnListIteratorWhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        listIterator.next();
        listIterator.previous();
        assertThatThrownBy(listIterator::previous).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorPreviousAndListElementWasRemoved() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            assertThat(listIterator.hasPrevious()).isFalse();
            listIterator.next();
            assertThat(listIterator.hasPrevious()).isTrue();
            list.remove(0);
            listIterator.previous();
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorPreviousAndListElementWasAdded() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            assertThat(listIterator.hasPrevious()).isFalse();
            listIterator.next();
            assertThat(listIterator.hasPrevious()).isTrue();
            list.add('2');
            listIterator.previous();
        }, ConcurrentModificationException.class);
    }

    // -- listIterator.previousIndex()

    @TestTemplate
    public void shouldReturnPreviousIndexOfEmptyListIterator() {
        assertThat(empty().listIterator().previousIndex()).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnPreviousIndexOfNonEmptyListIterator() {
        assertThat(of('1').listIterator().previousIndex()).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnCorrectPreviousIndexOfListIteratorAfterIteratingAllElements() {
        final ListIterator<Character> listIterator = of('1').listIterator();
        listIterator.next();
        assertThat(listIterator.previousIndex()).isEqualTo(0);
    }

    // -- listIterator().remove()

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnEmptyListIterator() {
        ifSupported(() -> empty().listIterator().remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnNonEmptyListIterator() {
        ifSupported(() -> of('1').listIterator().remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterRemoveHasBeenCalledAfterTheLastCallOfNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            listIterator.remove();
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterRemoveHasBeenCalledAfterTheLastCallOfPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            listIterator.previous();
            listIterator.remove();
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterAddHasBeenCalledAfterTheLastCallOfNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.add('4');
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterAddHasBeenCalledAfterTheLastCallOfPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.previous();
            listIterator.add('4');
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldRemoveLastElementOfListIteratorThatWasReturnedByNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            assertThat(listIterator.next()).isEqualTo('2');
            listIterator.remove();
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveLastElementOfListIteratorThatWasReturnedByPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            assertThat(listIterator.previous()).isEqualTo('2');
            listIterator.remove();
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnListIteratorAfterListWasChanged() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            list.add('2');
            assertThatThrownBy(listIterator::remove).isInstanceOf(ConcurrentModificationException.class);
        });
    }

    // -- listIterator().set()

    @TestTemplate
    public void shouldThrowWhenCallingSetOnEmptyListIterator() {
        ifSupported(() -> empty().listIterator().set('0'), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetOnNonEmptyListIterator() {
        ifSupported(() -> of('1').listIterator().set('0'), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterRemoveHasBeenCalledAfterTheLastCallOfNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            listIterator.remove();
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterRemoveHasBeenCalledAfterTheLastCallOfPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            listIterator.previous();
            listIterator.remove();
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterAddHasBeenCalledAfterTheLastCallOfNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.add('4');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterAddHasBeenCalledAfterTheLastCallOfPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.previous();
            listIterator.add('4');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldSetLastElementOfListIteratorThatWasReturnedByNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            assertThat(listIterator.next()).isEqualTo('2');
            listIterator.set('0');
            assertThat(list).isEqualTo(asList('1', '0', '3'));
        });
    }

    @TestTemplate
    public void shouldSetLastElementOfListIteratorThatWasReturnedByPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            listIterator.next();
            assertThat(listIterator.previous()).isEqualTo('2');
            listIterator.set('0');
            assertThat(list).isEqualTo(asList('1', '0', '3'));
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetOnListIteratorAfterListWasChanged() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator();
            listIterator.next();
            list.add('2');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(ConcurrentModificationException.class);
        });
    }

    // -- listIterator(int)

    @TestTemplate
    public void shouldThrowWhenListIteratorAtNegativeIndexWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(-1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenListIteratorAtNegativeIndexWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').listIterator(-1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldNotThrowWhenListIteratorAtSizeIndexWhenEmpty() {
        assertThat(empty().listIterator(0)).isNotNull();
    }

    @TestTemplate
    public void shouldNotThrowWhenListIteratorAtSizeIndexWhenNotEmpty() {
        assertThat(of('1').listIterator(1)).isNotNull();
    }

    @TestTemplate
    public void shouldThrowWhenListIteratorAtIndexGreaterSizeWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(1))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenListIteratorAtIndexGreaterSizeWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').listIterator(2))
          .isInstanceOf(IndexOutOfBoundsException.class);
    }

    // -- listIterator(int).hasNext()

    @TestTemplate
    public void shouldReturnEmptyListIteratorAtFirstIndexWhenEmpty() {
        assertThat(empty().listIterator(0).hasNext()).isFalse();
    }

    @TestTemplate
    public void shouldReturnNonEmptyListIteratorAtFirstIndexWhenNotEmpty() {
        assertThat(of('1').listIterator(0).hasNext()).isTrue();
    }

    @TestTemplate
    public void shouldNotHaveNextWhenAllListIteratorElementsAtFirstIndexWereConsumedByNext() {
        final ListIterator<Character> listIterator = of('1').listIterator(0);
        listIterator.next();
        assertThat(listIterator.hasNext()).isFalse();
    }

    // -- listIterator(int).next()

    @TestTemplate
    public void shouldThrowWhenCallingNextOnListIteratorAtFirstIndexWhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(0).next())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldNotThrowWhenCallingNextOnListIteratorAtFirstIndexWhenNotEmpty() {
        assertThat(of('1').listIterator(0).next()).isEqualTo('1');
    }

    @TestTemplate
    public void shouldThrowWhenCallingNextTooOftenOnListIteratorAtFirstIndexWhenNotEmpty() {
        final java.util.Iterator<Character> listIterator = of('1').listIterator(0);
        listIterator.next();
        assertThatThrownBy(listIterator::next).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldIterateAsExpectedWhenCallingListIteratorAtFirstIndexWhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1', '2', '3').listIterator(0);
        assertThat(listIterator.next()).isEqualTo('1');
        assertThat(listIterator.next()).isEqualTo('2');
        assertThat(listIterator.next()).isEqualTo('3');
    }

    @TestTemplate
    public void shouldIterateAsExpectedWhenCallingListIteratorAtNonFirstIndexWhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1', '2', '3').listIterator(1);
        assertThat(listIterator.next()).isEqualTo('2');
        assertThat(listIterator.next()).isEqualTo('3');
    }

    // -- listIterator().nextIndex()

    @TestTemplate
    public void shouldReturnNextIndexOfEmptyListIteratorAtFirstIndex() {
        assertThat(empty().listIterator(0).nextIndex()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldReturnNextIndexOfNonEmptyListIteratorAtIndex1() {
        assertThat(of('1').listIterator(1).nextIndex()).isEqualTo(1);
    }

    @TestTemplate
    public void shouldReturnCorrectNextIndexOfListIteratorAtIndex1AfterIteratingAllElements() {
        final ListIterator<Character> listIterator = of('1', '2').listIterator(1);
        listIterator.next();
        assertThat(listIterator.nextIndex()).isEqualTo(2);
    }

    // -- listIterator().hasPrevious()

    @TestTemplate
    public void shouldNotHavePreviousWhenEmptyListIteratorAtIndex0() {
        assertThat(empty().listIterator(0).hasPrevious()).isFalse();
    }

    @TestTemplate
    public void shouldHavePreviousWhenNonEmptyListIteratorAtIndex1() {
        assertThat(of('1').listIterator(1).hasPrevious()).isTrue();
    }

    @TestTemplate
    public void shouldNotHavePreviousWhenAllListIteratorAtIndex0ElementsWereConsumedByPrevious() {
        final ListIterator<Character> listIterator = of('1').listIterator(1);
        assertThat(listIterator.hasPrevious()).isTrue();
        listIterator.previous();
        assertThat(listIterator.hasPrevious()).isFalse();
    }

    // -- listIterator().previous()

    @TestTemplate
    public void shouldThrowWhenCallingPreviousOnListIteratorAtIndex0WhenEmpty() {
        assertThatThrownBy(() -> empty().listIterator(0).previous())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingPreviousOnListIteratorAtIndex0WhenNotEmpty() {
        assertThatThrownBy(() -> of('1').listIterator(0).previous())
          .isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldRepeatedlyReturnTheSameElementWhenAlternatingNextAndPreviousAtIndex1() {
        final ListIterator<Character> listIterator = of('1').listIterator(1);
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        actual.add(listIterator.previous());
        actual.add(listIterator.next());
        actual.add(listIterator.previous());
        actual.add(listIterator.next());
        assertThat(actual).isEqualTo(asList('1', '1', '1', '1'));
    }

    @TestTemplate
    public void shouldIterateListIteratorAtLastIndexBackwards() {
        final ListIterator<Character> listIterator = of('1', '2', '3', '4').listIterator(4);
        final java.util.List<Character> actual = new java.util.ArrayList<>();
        while (listIterator.hasPrevious()) {
            actual.add(listIterator.previous());
        }
        assertThat(actual).isEqualTo(asList('4', '3', '2', '1'));
    }

    @TestTemplate
    public void shouldThrowWhenCallingPreviousTooOftenOnListIteratorAtIndex1WhenNotEmpty() {
        final ListIterator<Character> listIterator = of('1').listIterator(1);
        listIterator.previous();
        assertThatThrownBy(listIterator::previous).isInstanceOf(NoSuchElementException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorAtIndex1PreviousAndListElementWasRemoved() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator(1);
            assertThat(listIterator.hasPrevious()).isTrue();
            list.remove(0);
            listIterator.previous();
        }, ConcurrentModificationException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingListIteratorAtIndex1PreviousAndListElementWasAdded() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            final ListIterator<Character> listIterator = list.listIterator(1);
            assertThat(listIterator.hasPrevious()).isTrue();
            list.add('2');
            listIterator.previous();
        }, ConcurrentModificationException.class);
    }

    // -- listIterator.previousIndex()

    @TestTemplate
    public void shouldReturnPreviousIndexOfEmptyListIteratorAtIndex0() {
        assertThat(empty().listIterator(0).previousIndex()).isEqualTo(-1);
    }

    @TestTemplate
    public void shouldReturnPreviousIndexOfNonEmptyListIteratorAtIndex1() {
        assertThat(of('1').listIterator(1).previousIndex()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldReturnCorrectPreviousIndexOfListIteratorAtIndex1AfterIteratingAllElements() {
        final ListIterator<Character> listIterator = of('1', '2').listIterator(1);
        listIterator.next();
        assertThat(listIterator.previousIndex()).isEqualTo(1);
    }

    // -- listIterator().remove()

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnEmptyListIteratorAtIndex0() {
        ifSupported(() -> empty().listIterator(0).remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnNonEmptyListIteratorAtIndex1() {
        ifSupported(() -> of('1').listIterator(1).remove(), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterRemoveHasBeenCalledAfterTheLastCallOfNextAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.remove();
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterRemoveHasBeenCalledAfterTheLastCallOfPreviousAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.previous();
            listIterator.remove();
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterAddHasBeenCalledAfterTheLastCallOfNextAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.add('4');
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveAfterAddHasBeenCalledAfterTheLastCallOfPreviousAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.previous();
            listIterator.add('4');
            assertThatThrownBy(listIterator::remove).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldRemoveLastElementOfListIteratorAtIndex1ThatWasReturnedByNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            assertThat(listIterator.next()).isEqualTo('2');
            listIterator.remove();
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveLastElementOfListIteratorAtIndex1ThatWasReturnedByPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            assertThat(listIterator.previous()).isEqualTo('2');
            listIterator.remove();
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingRemoveOnListIteratorAtIndex1AfterListWasChanged() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            list.add('2');
            assertThatThrownBy(listIterator::remove).isInstanceOf(ConcurrentModificationException.class);
        });
    }

    // -- listIterator().set()

    @TestTemplate
    public void shouldThrowWhenCallingSetOnEmptyListIteratorAtIndex0() {
        ifSupported(() -> empty().listIterator(0).set('0'), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetOnNonEmptyListIteratorAtIndex1() {
        ifSupported(() -> of('1', '2').listIterator(1).set('0'), IllegalStateException.class);
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterRemoveHasBeenCalledAfterTheLastCallOfNextAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.remove();
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterRemoveHasBeenCalledAfterTheLastCallOfPreviousAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.previous();
            listIterator.remove();
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterAddHasBeenCalledAfterTheLastCallOfNextAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.add('4');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetAfterAddHasBeenCalledAfterTheLastCallOfPreviousAtIndex1() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            listIterator.previous();
            listIterator.add('4');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(IllegalStateException.class);
        });
    }

    @TestTemplate
    public void shouldSetLastElementOfListIteratorAtIndex1ThatWasReturnedByNext() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            assertThat(listIterator.next()).isEqualTo('3');
            listIterator.set('0');
            assertThat(list).isEqualTo(asList('1', '2', '0'));
        });
    }

    @TestTemplate
    public void shouldSetLastElementOfListIteratorAtIndex1ThatWasReturnedByPrevious() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            assertThat(listIterator.previous()).isEqualTo('2');
            listIterator.set('0');
            assertThat(list).isEqualTo(asList('1', '0', '3'));
        });
    }

    @TestTemplate
    public void shouldThrowWhenCallingSetOnListIteratorAtIndex1AfterListWasChanged() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            final ListIterator<Character> listIterator = list.listIterator(1);
            listIterator.next();
            list.add('3');
            assertThatThrownBy(() -> listIterator.set('0')).isInstanceOf(ConcurrentModificationException.class);
        });
    }

    // -- remove(int)

    @TestTemplate
    public void shouldThrowWhenRemovingElementAtNegativeIndexWhenEmpty() {
        ifSupported(() -> empty().remove(-1), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenRemovingElementAtNegativeIndexWhenNotEmpty() {
        ifSupported(() -> of('1').remove(-1), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenRemovingElementAtSizeIndexWhenEmpty() {
        ifSupported(() -> empty().remove(0), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenRemovingElementAtSizeIndexWhenNotEmpty() {
        ifSupported(() -> of('1').remove(1), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldRemoveTheElementAtFirstIndex() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.remove(0)).isEqualTo('1');
            assertThat(list).isEqualTo(asList('2', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveTheElementAtInnerIndex() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.remove(1)).isEqualTo('2');
            assertThat(list).isEqualTo(asList('1', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveTheElementAtLastIndex() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.remove(2)).isEqualTo('3');
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    @TestTemplate
    public void shouldRemoveAllUsingFirstIndex() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            list.remove(0);
            list.remove(0);
            list.remove(0);
            assertThat(list).isEmpty();
        });
    }

    @TestTemplate
    public void shouldRemoveAllUsingDescendingIndices() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            list.remove(2);
            list.remove(1);
            list.remove(0);
            assertThat(list).isEmpty();
        });
    }

    @TestTemplate
    public void shouldThrowWhenTryingToRemoveMoreElementsThanPresent() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            list.remove(0);
            list.remove(0);
            assertThatThrownBy(() -> list.remove(0)).isInstanceOf(IndexOutOfBoundsException.class);
        });
    }

    // -- remove(Object)

    @TestTemplate
    public void shouldRemoveElementFromEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            assertThat(list.remove((Object) '1')).isFalse();
            assertThat(list).isEqualTo(empty());
        });
    }

    @TestTemplate
    public void shouldRemoveNonExistingElementFromNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            assertThat(list.remove((Object) '3')).isFalse();
            assertThat(list).isEqualTo(of('1', '2'));
        });
    }

    @TestTemplate
    public void shouldRemoveExistingElementFromNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.remove((Object) '2')).isTrue();
            assertThat(list).isEqualTo(of('1', '3'));
        });
    }

    @TestTemplate
    public void shouldRemoveNull() {
        if (elementNullability == NULLABLE) {
            ifSupported(() -> {
                final java.util.List<Character> list = of('1', null, '2');
                assertThat(list.remove(null)).isTrue();
                assertThat(list).isEqualTo(of('1', '2'));
            });
        }
    }

    // -- removeAll(Collection)

    @TestTemplate
    public void shouldThrowNPEWhenCallingRemoveAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().removeAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowNPEWhenCallingRemoveAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').removeAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldRemoveAllWhenCollectionsAreDistinct() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.removeAll(asList('2'))).isFalse();

        });
    }

    @TestTemplate
    public void shouldRemoveAllWhenCollectionsAreNotDistinct() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            assertThat(list.removeAll(asList('2', '3'))).isTrue();
            assertThat(list).isEqualTo(asList('1'));
        });
    }

    @TestTemplate
    public void shouldRemoveAllWhenCollectionsAreEqual() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            assertThat(list.removeAll(asList('1', '2'))).isTrue();
            assertThat(list).isEmpty();
        });
    }

    @TestTemplate
    public void shouldRemoveAllEmptyFromEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of();
            assertThat(list.removeAll(asList())).isFalse();
            assertThat(list).isEmpty();
        });
    }

    @TestTemplate
    public void shouldRemoveAllEmptyFromNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.removeAll(asList())).isFalse();
            assertThat(list).isEqualTo(asList('1'));
        });
    }

    @TestTemplate
    public void shouldRemoveAllNonEmptyFromEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            assertThat(list.removeAll(asList('1'))).isFalse();
            assertThat(list).isEmpty();
        });
    }

    // -- replaceAll(UnaryOperator)

    @TestTemplate
    public void shouldThrowWhenEmptyReplaceAllGivenNullUnaryOperator() {
        assertThatThrownBy(() -> this.<Character> empty().replaceAll(null)).isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowWhenNonEmptyReplaceAllGivenNullUnaryOperator() {
        assertThatThrownBy(() ->of('1').replaceAll(null)).isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldNotThrowWhenReplacingAllOfEmpty() {
        empty().replaceAll(UnaryOperator.identity());
    }

    @TestTemplate
    public void shouldReplaceAllOfNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            list.replaceAll(c -> (char) (c + 1));
            assertThat(list).isEqualTo(asList('2', '3'));
        });
    }

    // -- retainAll(Collection)

    @TestTemplate
    public void shouldThrowNPEWhenCallingRetainAllNullWhenEmpty() {
        assertThatThrownBy(() -> empty().retainAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowNPEWhenCallingRetainAllNullWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').retainAll(null))
          .isInstanceOf(NullPointerException.class);
    }

    // -- retainAll(Collection) tests

    @TestTemplate
    public void shouldThrowWhenRetainAllNull() {
        assertThatThrownBy(() -> empty().retainAll(null)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> of('1').retainAll(null)).isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldRetainAllEmptyOfEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            assertThat(list.retainAll(asList())).isFalse();
            assertThat(list).isEmpty();
        });
    }

    @TestTemplate
    public void shouldRetainAllEmptyOfNonEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.retainAll(asList())).isTrue();
            assertThat(list).isEqualTo(asList());
        });
    }

    @TestTemplate
    public void shouldRetainAllNonEmptyOfEmpty() {
        ifSupported(() -> {
            final java.util.List<Character> list = empty();
            assertThat(list.retainAll(asList('1'))).isFalse();
            assertThat(list).isEqualTo(asList());
        });

    }

    @TestTemplate
    public void shouldRetainAllWhenDisjoint() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.retainAll(asList('2'))).isTrue();
            assertThat(list).isEqualTo(asList());
        });
    }

    @TestTemplate
    public void shouldRetainAllWhenIntersecting() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            assertThat(list.retainAll(asList('2', '3'))).isTrue();
            assertThat(list).isEqualTo(asList('2'));
        });
    }

    @TestTemplate
    public void shouldRetainAllWhenEqual() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2');
            assertThat(list.retainAll(asList('1', '2'))).isFalse();
            assertThat(list).isEqualTo(asList('1', '2'));
        });
    }

    // -- set(int, T)

    @TestTemplate
    public void shouldThrowWhenSettingElementAtNegativeIndexWhenEmpty() {
        ifSupported(() -> empty().set(-1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenSettingElementAtNegativeIndexWhenNotEmpty() {
        ifSupported(() -> of('1').set(-1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenSettingElementAtSizeIndexWhenEmpty() {
        ifSupported(() -> empty().set(0, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldThrowWhenSettingElementAtSizeIndexWhenNotEmpty() {
        ifSupported(() -> of('1').set(1, null), IndexOutOfBoundsException.class);
    }

    @TestTemplate
    public void shouldSetElementAtFirstIndexWhenListWithSingleElement() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1');
            assertThat(list.set(0, 'a')).isEqualTo('1');
            assertThat(list).isEqualTo(asList('a'));
        });
    }

    @TestTemplate
    public void shouldSetElementAtFirstIndexWhenListWithThreeElements() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.set(0, 'a')).isEqualTo('1');
            assertThat(list).isEqualTo(asList('a', '2', '3'));
        });
    }

    @TestTemplate
    public void shouldSetElementAtLastIndexWhenListWithThreeElements() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.set(2, 'a')).isEqualTo('3');
            assertThat(list).isEqualTo(asList('1', '2', 'a'));
        });
    }

    @TestTemplate
    public void shouldSetElementAtMiddleIndexWhenListWithThreeElements() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('1', '2', '3');
            assertThat(list.set(1, 'a')).isEqualTo('2');
            assertThat(list).isEqualTo(asList('1', 'a', '3'));
        });
    }

    // -- size()

    @TestTemplate
    public void shouldReturnSizeOfEmpty() {
        assertThat(empty().size()).isEqualTo(0);
    }

    @TestTemplate
    public void shouldReturnSizeOfNonEmpty() {
        assertThat(of('1', '2', '3').size()).isEqualTo(3);
    }

    // -- sort(Comparator)

    @TestTemplate
    public void shouldSortEmptyList() {
        final java.util.List<Character> list = empty();
        list.sort(Comparator.naturalOrder());
        assertThat(list).isEmpty();
    }

    @TestTemplate
    public void shouldSortNonEmptyList() {
        ifSupported(() -> {
            final java.util.List<Character> list = of('3', '1', '2');
            list.sort(Comparator.naturalOrder());
            assertThat(list).isEqualTo(asList('1', '2', '3'));
        });
    }

    // -- spliterator()

    @TestTemplate
    public void shouldReturnNonNullSpliteratorWhenEmpty() {
        final Spliterator<Object> spliterator = empty().spliterator();
        assertThat(spliterator).isNotNull();
        assertThat(spliterator.tryAdvance(e -> {
            throw new AssertionError("spliterator reports element for empty collection: " + e);
        })).isFalse();
    }

    @TestTemplate
    public void shouldReturnNonNullSpliteratorWhenNotEmpty() {
        final Spliterator<Character> spliterator = of('1').spliterator();
        assertThat(spliterator).isNotNull();
        assertThat(spliterator.tryAdvance(e -> assertThat(e).isEqualTo('1'))).isTrue();
        assertThat(spliterator.tryAdvance(e -> {
            throw new AssertionError("spliterator reports element for empty collection: " + e);
        })).isFalse();
    }

    @TestTemplate
    public void shouldHaveSpliteratorOrderedCharacteristicsWhenEmpty() {
        final Spliterator<Object> spliterator = empty().spliterator();
        assertThat(spliterator.characteristics() & Spliterator.ORDERED).isEqualTo(Spliterator.ORDERED);
    }

    @TestTemplate
    public void shouldHaveSpliteratorOrderedCharacteristicsWhenNotEmpty() {
        final Spliterator<Character> spliterator = of('1').spliterator();
        assertThat(spliterator.characteristics() & Spliterator.ORDERED).isEqualTo(Spliterator.ORDERED);
    }

    // -- subList(int, int)

    @TestTemplate
    public void shouldReturnEmptyWhenSubListFrom0To0OnEmpty() {
        assertThat(empty().subList(0, 0)).isEmpty();
    }

    @TestTemplate
    public void shouldReturnEmptyWhenSubListFrom0To0OnNonEmpty() {
        assertThat(of('1').subList(0, 0)).isEmpty();
    }

    @TestTemplate
    public void shouldReturnListWithFirstElementWhenSubListFrom0To1OnNonEmpty() {
        assertThat(of('1').subList(0, 1)).isEqualTo(asList('1'));
    }

    @TestTemplate
    public void shouldReturnEmptyWhenSubListFrom1To1OnNonEmpty() {
        assertThat(of('1').subList(1, 1)).isEmpty();
    }

    @TestTemplate
    public void shouldReturnSubListWhenIndicesAreWithinRange() {
        assertThat(of('1', '2', '3').subList(1, 3)).isEqualTo(asList('2', '3'));
    }

    @TestTemplate
    public void shouldReturnEmptyWhenSubListIndicesBothAreUpperBound() {
        assertThat(of('1', '2', '3').subList(3, 3)).isEmpty();
    }

    @TestTemplate
    public void shouldThrowOnSubListOnNonEmptyWhenBeginIndexIsGreaterThanEndIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            of('1', '2', '3').subList(1, 0);
        });
    }

    @TestTemplate
    public void shouldThrowOnSubListOnEmptyWhenBeginIndexIsGreaterThanEndIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            empty().subList(1, 0);
        });
    }

    @TestTemplate
    public void shouldThrowOnSubListOnNonEmptyWhenBeginIndexExceedsLowerBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            of('1', '2', '3').subList(-1, 2);
        });
    }

    @TestTemplate
    public void shouldThrowOnSubListOnEmptyWhenBeginIndexExceedsLowerBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            empty().subList(-1, 2);
        });
    }

    @TestTemplate
    public void shouldThrowWhenSubList2OnEmpty() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            empty().subList(0, 1);
        });
    }

    @TestTemplate
    public void shouldThrowOnSubListWhenEndIndexExceedsUpperBound() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            of('1', '2', '3').subList(1, 4).size(); // force computation of last element, e.g. for lazy delegate
        });
    }

    @TestTemplate
    public void shouldThrowOnSubListWhenBeginIndexIsGreaterThanEndIndex() {
        assertThrows(IllegalArgumentException.class, () -> {
            of('1', '2', '3').subList(2, 1).size(); // force computation of last element, e.g. for lazy delegate
        });
    }

    @TestTemplate
    public void shouldReturnEqualInstanceIfSubListStartsAtZeroAndEndsAtLastElement() {
        assertThat(of('1', '2', '3').subList(0, 3)).isEqualTo(asList('1', '2', '3'));
    }

    // -- toArray()

    @TestTemplate
    public void shouldConvertEmptyToArray() {
        assertThat(empty().toArray()).isEqualTo(new Object[0]);
    }

    @TestTemplate
    public void shouldConvertNonEmptyToArray() {
        assertThat(of('1').toArray()).isEqualTo(new Object[] { '1' });
    }

    // -- toArray(T[])

    @TestTemplate
    public void shouldThrowNPEWhenCallingToArrayNullWhenEmpty() {
        assertThatThrownBy(() -> empty().toArray((Object[]) null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldThrowNPEWhenCallingToArrayNullWhenNotEmpty() {
        assertThatThrownBy(() -> of('1').toArray((Object[]) null))
          .isInstanceOf(NullPointerException.class);
    }

    @TestTemplate
    public void shouldConvertEmptyToArrayPassingArrayOfCorrectSize() {
        assertThat(this.<Character> empty().toArray(new Character[0])).isEqualTo(new Character[0]);
    }

    @TestTemplate
    public void shouldConvertEmptyToArrayPassingArrayOfGreaterSize() {
        assertThat(this.<Character> empty().toArray(new Character[] { 'x' })).isEqualTo(new Character[] { null });
    }

    @TestTemplate
    public void shouldConvertNonEmptyToArrayPassingArrayOfCorrectSize() {
        assertThat(of('1', '2').toArray(new Character[2])).isEqualTo(new Character[] { '1', '2' });
    }

    @TestTemplate
    public void shouldConvertNonEmptyToArrayPassingArrayOfGreaterSize() {
        assertThat(of('1', '2').toArray(new Character[] { 'a', 'b', 'c', 'd' })).isEqualTo(new Character[] { '1', '2', null, 'd' });
    }

    @TestTemplate
    public void shouldConvertNonEmptyToArrayPassingArrayOfSmallerSize() {
        assertThat(of('1', '2').toArray(new Character[1])).isEqualTo(new Character[] { '1', '2' });
    }

    // --- helpers

    @SuppressWarnings("varargs")
    @SafeVarargs
    private final void ifSupported(Runnable test, Class<? extends Throwable>... expectedExceptionTypes) {
        try {
            test.run();
            if (changePolicy == IMMUTABLE) {
                Assertions.fail("Operation should throw " + UnsupportedOperationException.class.getName());
            }
            if (expectedExceptionTypes.length > 0) {
                Assertions.fail("Expected one of " + List.of(expectedExceptionTypes).map(Class::getName).mkString("[", ", ", "]"));
            }
        } catch (Throwable x) {
            if (changePolicy == IMMUTABLE) {
                if (!(x instanceof UnsupportedOperationException)) {
                    final boolean isJavaCollection = empty().getClass().getName().startsWith("java.util.");
                    // DEV-NOTE: Java's collections throw UnsupportedOperationException inconsistently
                    if (!isJavaCollection) {
                        Assertions.fail("Operation should throw " + UnsupportedOperationException.class.getName() + " but found " + x.getClass().getName() + ":\n" + x.getMessage());
                    }
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

    enum ChangePolicy {
        IMMUTABLE, MUTABLE;
    }

    enum ElementType {
        FIXED, GENERIC;
    }

    enum ElementNullability {
        NULLABLE, NON_NULLABLE;
    }
}
