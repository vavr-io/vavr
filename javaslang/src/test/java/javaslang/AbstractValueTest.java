/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.*;
import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.collection.LinkedHashMap;
import javaslang.collection.LinkedHashSet;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.PriorityQueue;
import javaslang.collection.Queue;
import javaslang.collection.Set;
import javaslang.collection.SortedMap;
import javaslang.collection.SortedSet;
import javaslang.collection.Stack;
import javaslang.collection.TreeMap;
import javaslang.collection.TreeSet;
import javaslang.collection.Vector;
import javaslang.control.Either;
import javaslang.control.Option;
import javaslang.control.Try;
import org.assertj.core.api.*;
import org.junit.Test;

import java.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.function.Function;

import static java.lang.Integer.bitCount;
import static javaslang.Serializables.deserialize;
import static javaslang.Serializables.serialize;
import static org.junit.Assert.fail;

public abstract class AbstractValueTest {

    protected Random getRandom(int seed) {
        if (seed >= 0) {
            return new Random(seed);
        } else {
            final Random random = new Random();
            seed = random.nextInt();
            System.out.println("using seed: " + seed);
            random.setSeed(seed);
            return random;
        }
    }

    protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
        return new IterableAssert<T>(actual) {};
    }

    protected <T> ObjectAssert<T> assertThat(T actual) {
        return new ObjectAssert<T>(actual) {};
    }

    protected <T> ObjectArrayAssert<T> assertThat(T[] actual) {
        return new ObjectArrayAssert<T>(actual) {};
    }

    protected BooleanAssert assertThat(Boolean actual) {
        return new BooleanAssert(actual) {};
    }

    protected DoubleAssert assertThat(Double actual) {
        return new DoubleAssert(actual) {};
    }

    protected IntegerAssert assertThat(Integer actual) {
        return new IntegerAssert(actual) {};
    }

    protected LongAssert assertThat(Long actual) {
        return new LongAssert(actual) {};
    }

    protected StringAssert assertThat(String actual) {
        return new StringAssert(actual) {};
    }

    abstract protected <T> Value<T> empty();

    abstract protected <T> Value<T> of(T element);

    @SuppressWarnings("unchecked")
    abstract protected <T> Value<T> of(T... elements);

    abstract protected boolean useIsEqualToInsteadOfIsSameAs();

    // returns the peek result of the specific Traversable implementation
    abstract protected int getPeekNonNilPerformingAnAction();

    // -- get()

    @Test(expected = NoSuchElementException.class)
    public void shouldGetEmpty() {
        empty().get();
    }

    @Test
    public void shouldGetNonEmpty() {
        assertThat(of(1).get()).isEqualTo(1);
    }

    // -- getOption()

    @Test
    public void shouldGetOptionEmpty() {
        assertThat(empty().getOption()).isEqualTo(Option.none());
    }

    @Test
    public void shouldGetOptionNonEmpty() {
        assertThat(of(1).getOption()).isEqualTo(Option.of(1));
    }

    // -- getOrElse(T)

    @Test
    public void shouldCalculateGetOrElse() {
        assertThat(empty().getOrElse(1)).isEqualTo(1);
        assertThat(of(1).getOrElse(2)).isEqualTo(1);
    }

    // -- getOrElse(Supplier)

    @Test
    public void shouldCalculateGetOrElseSupplier() {
        assertThat(empty().getOrElse(() -> 1)).isEqualTo(1);
        assertThat(of(1).getOrElse(() -> 2)).isEqualTo(1);
    }

    // -- getOrElseThrow

    @Test(expected = ArithmeticException.class)
    public void shouldThrowOnGetOrElseThrowIfEmpty() {
        empty().getOrElseThrow(ArithmeticException::new);
    }

    @Test
    public void shouldNotThrowOnGetOrElseThrowIfNonEmpty() {
        assertThat(of(1).getOrElseThrow(ArithmeticException::new)).isEqualTo(1);
    }

    // -- getOrElseTry

    @Test
    public void shouldReturnUnderlyingValueWhenCallingGetOrElseTryOnNonEmptyValue() {
        assertThat(of(1).getOrElseTry(() -> 2)).isEqualTo(1);
    }

    @Test
    public void shouldReturnAlternateValueWhenCallingGetOrElseTryOnEmptyValue() {
        assertThat(empty().getOrElseTry(() -> 2)).isEqualTo(2);
    }

    @Test(expected = Try.NonFatalException.class)
    public void shouldThrowWhenCallingGetOrElseTryOnEmptyValueAndTryIsAFailure() {
        empty().getOrElseTry(() -> {
            throw new Error();
        });
    }

    // -- forEach

    @Test
    public void shouldPerformsActionOnEachElement() {
        final int[] consumer = new int[1];
        final Value<Integer> value = of(1, 2, 3);
        value.forEach(i -> consumer[0] += i);
        assertThat(consumer[0]).isEqualTo(value.isSingleValued() ? 1 : 6);
    }

    // -- isEmpty

    @Test
    public void shouldCalculateIsEmpty() {
        assertThat(empty().isEmpty()).isTrue();
        assertThat(of(1).isEmpty()).isFalse();
    }

    // -- peek

    @Test
    public void shouldPeekNil() {
        assertThat(empty().peek(t -> {})).isEqualTo(empty());
    }

    @Test
    public void shouldPeekNonNilPerformingNoAction() {
        assertThat(of(1).peek(t -> {})).isEqualTo(of(1));
    }

    @Test
    public void shouldPeekSingleValuePerformingAnAction() {
        final int[] effect = { 0 };
        final Value<Integer> actual = of(1).peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of(1));
        assertThat(effect[0]).isEqualTo(1);
    }

    @Test
    public void shouldPeekNonNilPerformingAnAction() {
        final int[] effect = { 0 };
        final Value<Integer> actual = of(1, 2, 3).peek(i -> effect[0] = i);
        assertThat(actual).isEqualTo(of(1, 2, 3)); // traverses all elements in the lazy case
        assertThat(effect[0]).isEqualTo(getPeekNonNilPerformingAnAction());
    }

    // -- Conversions toXxx()

    /**
     * States whether the Value's elements are ordered by a Comparator.
     * <p>
     * Test classes override this method to return {@code true} if needed.
     *
     * @return false (by default), if the Value's elements are ordered, true otherwise
     */
    protected boolean isOrdered() {
        return false;
    }

    @Test
    public void shouldConvertToArray() {
        final Value<Integer> value = of(1, 2, 3);
        final Array<Integer> array = value.toArray();
        if (value.isSingleValued()) {
            assertThat(array).isEqualTo(Array.of(1));
        } else {
            assertThat(array).isEqualTo(Array.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToCharSeq() {
        final Value<Integer> value = of(1, 2, 3);
        final CharSeq charSeq = value.toCharSeq();
        assertThat(charSeq).isEqualTo(CharSeq.of(value.toString()));
    }

    @Test
    public void shouldConvertToList() {
        final Value<Integer> value = of(1, 2, 3);
        final List<Integer> list = value.toList();
        if (value.isSingleValued()) {
            assertThat(list).isEqualTo(List.of(1));
        } else {
            assertThat(list).isEqualTo(List.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToHashMap() {
        final Value<Integer> value = of(9, 5, 1);
        final Map<Integer, Integer> map = value.toMap(i -> Tuple.of(i, i));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(HashMap.of(9, 9));
        } else {
            assertThat(map).isEqualTo(HashMap.of(1, 1, 5, 5, 9, 9));
        }
    }

    @Test
    public void shouldConvertToHashMapTwoFunctions() {
        final Value<Integer> value = of(9, 5, 1);
        final Map<Integer, Integer> map = value.toMap(Function.identity(), Function.identity());
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(HashMap.of(9, 9));
        } else {
            assertThat(map).isEqualTo(HashMap.of(1, 1, 5, 5, 9, 9));
        }
    }

    @Test
    public void shouldConvertToLinkedMap() {
        final Value<Integer> value = of(9, 5, 1);
        final Map<Integer, Integer> map = value.toLinkedMap(i -> Tuple.of(i, i));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(LinkedHashMap.of(9, 9));
        } else if (isOrdered()) {
            assertThat(map).isEqualTo(LinkedHashMap.of(1, 1, 5, 5, 9, 9));
        } else {
            assertThat(map).isEqualTo(LinkedHashMap.of(9, 9, 5, 5, 1, 1));
        }
    }

    @Test
    public void shouldConvertToLinkedMapTwoFunctions() {
        final Value<Integer> value = of(9, 5, 1);
        final Map<Integer, Integer> map = value.toLinkedMap(Function.identity(), Function.identity());
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(LinkedHashMap.of(9, 9));
        } else if (isOrdered()) {
            assertThat(map).isEqualTo(LinkedHashMap.of(1, 1, 5, 5, 9, 9));
        } else {
            assertThat(map).isEqualTo(LinkedHashMap.of(9, 9, 5, 5, 1, 1));
        }
    }

    @Test
    public void shouldConvertToSortedMap() {
        final Value<Integer> value = of(9, 5, 1);
        final SortedMap<Integer, Integer> map = value.toSortedMap(i -> Tuple.of(i, i));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(TreeMap.of(9, 9));
        } else {
            assertThat(map).isEqualTo(TreeMap.of(1, 1, 5, 5, 9, 9));
        }
    }

    @Test
    public void shouldConvertToSortedMapTwoFunctions() {
        final Value<Integer> value = of(9, 5, 1);
        final SortedMap<Integer, Integer> map = value.toSortedMap(Function.identity(), Function.identity());
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(TreeMap.of(9, 9));
        } else {
            assertThat(map).isEqualTo(TreeMap.of(1, 1, 5, 5, 9, 9));
        }
    }

    @Test
    public void shouldConvertToSortedMapWithComparator() {
        final Value<Integer> value = of(9, 5, 1);
        final Comparator<Integer> comparator = ((Comparator<Integer>) Integer::compareTo).reversed();
        final SortedMap<Integer, Integer> map = value.toSortedMap(comparator, i -> Tuple.of(i, i));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(TreeMap.of(comparator, 9, 9));
        } else {
            assertThat(map).isEqualTo(TreeMap.of(comparator, 9, 9, 5, 5, 1, 1));
        }
    }

    @Test
    public void shouldConvertToSortedMapTwoFunctionsWithComparator() {
        final Value<Integer> value = of(9, 5, 1);
        final Comparator<Integer> comparator = ((Comparator<Integer>) Integer::compareTo).reversed();
        final SortedMap<Integer, Integer> map = value.toSortedMap(comparator, Function.identity(), Function.identity());
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(TreeMap.of(comparator, 9, 9));
        } else {
            assertThat(map).isEqualTo(TreeMap.of(comparator, 9, 9, 5, 5, 1, 1));
        }
    }

    @Test
    public void shouldConvertToOption() {
        assertThat(empty().toOption()).isSameAs(Option.none());
        assertThat(of(1).toOption()).isEqualTo(Option.of(1));
    }

    @Test
    public void shouldConvertToQueue() {
        final Value<Integer> value = of(1, 2, 3);
        final Queue<Integer> queue = value.toQueue();
        if (value.isSingleValued()) {
            assertThat(queue).isEqualTo(Queue.of(1));
        } else {
            assertThat(queue).isEqualTo(Queue.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToPriorityQueueUsingImplicitComparator() {
        final Value<Integer> value = of(1, 3, 2);
        final PriorityQueue<Integer> queue = value.toPriorityQueue();
        if (value.isSingleValued()) {
            assertThat(queue).isEqualTo(PriorityQueue.of(1));
        } else {
            assertThat(queue).isEqualTo(PriorityQueue.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToPriorityQueueUsingExplicitComparator() {
        final Comparator<Integer> comparator = Comparator.naturalOrder();
        final Value<Integer> value = of(1, 3, 2);
        final PriorityQueue<Integer> queue = value.toPriorityQueue(comparator);
        if (value.isSingleValued()) {
            assertThat(queue).isEqualTo(PriorityQueue.of(comparator, 1));
        } else {
            assertThat(queue).isEqualTo(PriorityQueue.of(comparator, 1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToPriorityQueueUsingSerializableComparator() {
        final Value<Integer> value = of(1, 3, 2);
        final PriorityQueue<Integer> queue = value.toPriorityQueue();
        final PriorityQueue<Integer> actual = deserialize(serialize(queue));
        assertThat(actual).isEqualTo(queue);
    }

    @Test
    public void shouldConvertToSet() {
        final Value<Integer> value = of(1, 2, 3);
        final Set<Integer> set = value.toSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(HashSet.of(1));
        } else {
            assertThat(set).isEqualTo(HashSet.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToLinkedSet() {
        final Value<Integer> value = of(3, 7, 1, 15, 0);
        final Set<Integer> set = value.toLinkedSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(LinkedHashSet.of(3));
        } else {
            final List<Integer> itemsInOrder;
            if (value instanceof Traversable && !((Traversable) value).isTraversableAgain()) {
                itemsInOrder = List.of(3, 7, 1, 15, 0);
            } else {
                itemsInOrder = value.toList();
            }
            assertThat(set).isEqualTo(itemsInOrder.foldLeft(LinkedHashSet.empty(), LinkedHashSet::add));
        }
    }

    @Test
    public void shouldConvertToSortedSetWithoutComparatorOnComparable() {
        final Value<Integer> value = of(3, 7, 1, 15, 0);
        final SortedSet<Integer> set = value.toSortedSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(TreeSet.of(3));
        } else {
            assertThat(set).isEqualTo(TreeSet.of(0, 1, 3, 7, 15));
        }
    }

    @Test(expected = ClassCastException.class)
    public void shouldThrowOnConvertToSortedSetWithoutComparatorOnNonComparable() {
        final Value<StringBuilder> value = of(new StringBuilder("3"), new StringBuilder("7"), new StringBuilder("1"), new StringBuilder("15"), new StringBuilder("0"));
        final SortedSet<StringBuilder> set = value.toSortedSet();
        if (value.isSingleValued()) {
            //Comparator is not used yet
            set.add(new StringBuilder("7"));
        }
    }

    @Test
    public void shouldConvertToSortedSet() {
        final Value<Integer> value = of(3, 7, 1, 15, 0);
        final Comparator<Integer> comparator = (o1, o2) -> Integer.compare(bitCount(o1), bitCount(o2));
        final SortedSet<Integer> set = value.toSortedSet(comparator.reversed());
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(TreeSet.of(3));
        } else {
            assertThat(set).isEqualTo(TreeSet.of(comparator.reversed(), 0, 1, 3, 7, 15));
        }
    }

    @Test
    public void shouldConvertToSortedSetUsingSerializableComparator() {
        final Value<Integer> value = of(1, 3, 2);
        final SortedSet<Integer> set = value.toSortedSet();
        final SortedSet<Integer> actual = deserialize(serialize(set));
        assertThat(actual).isEqualTo(set);
    }

    @Test
    public void shouldConvertToStack() {
        final Value<Integer> value = of(1, 2, 3);
        final Stack<Integer> stack = value.toStack();
        if (value.isSingleValued()) {
            assertThat(stack).isEqualTo(List.of(1));
        } else {
            assertThat(stack).isEqualTo(List.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToStream() {
        final Value<Integer> value = of(1, 2, 3);
        final Stream<Integer> stream = value.toStream();
        if (value.isSingleValued()) {
            assertThat(stream).isEqualTo(Stream.of(1));
        } else {
            assertThat(stream).isEqualTo(Stream.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertEmptyToTree() {
        final Value<Integer> value = empty();
        final Tree<Integer> tree = value.toTree();
        assertThat(tree).isEqualTo(Tree.empty());
    }

    @Test
    public void shouldConvertNonEmptyToTree() {
        final Value<Integer> value = of(1, 2, 3);
        final Tree<Integer> tree = value.toTree();
        if (value.isSingleValued()) {
            assertThat(tree).isEqualTo(Tree.of(1));
        } else {
            assertThat(tree).isEqualTo(Tree.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertEmptyToTreeUsingComparator() {
        fail("not implemented");
    }

    @Test
    public void shouldConvertNonEmptyToProperTreeUsingComparator() {
        final String actual = List.of(
                "/",
                "  home",
                "    daniel",
                "  var")
                .toTree(s -> CharSeq.of(s).indexWhere(c -> c != ' '))
                .map(String::trim).draw();
        final String expected = String.join("\n",
                                            "/",
                                            "├──home",
                                            "│  └──daniel",
                                            "└──var");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonEmptyToTreeAddingRootUsingComparator() {
        final String actual = List.of(
                "<dependency>",
                "  <groupId>io.javaslang</groupId>",
                "  <artifactId>javaslang</artifactId>",
                "  <version>BLEEDING-EDGE</version>",
                "</dependency>")
                .toTree(s -> CharSeq.of(s).indexWhere(c -> c != ' '))
                .map(s -> (s == null) ? "" : s.trim())
                .draw();
        final String expected = String.join("\n",
                                            "",
                                            "├──<dependency>",
                                            "│  ├──<groupId>io.javaslang</groupId>",
                                            "│  ├──<artifactId>javaslang</artifactId>",
                                            "│  └──<version>BLEEDING-EDGE</version>",
                                            "└──</dependency>");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonEmptyNonToExpandedTreeUsingComparator() {
        final String actual = List.of(
                "   a",
                "  b",
                "c",
                "  d",
                "    e")
                .toTree(s -> CharSeq.of(s).indexWhere(c -> c != ' '))
                .map(s -> (s == null) ? "" : s.trim())
                .draw();
        final String expected = String.join("\n",
                                            "",
                                            "├──",
                                            "│  ├──",
                                            "│  │  └──a",
                                            "│  └──b",
                                            "└──c",
                                            "   └──d",
                                            "      └──e");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldConvertNonEmptyToTry() {
        assertThat(of(1, 2, 3).toTry()).isEqualTo(Try.of(() -> 1));
    }

    @Test
    public void shouldConvertEmptyToTry() {
        final Try<?> actual = empty().toTry();
        assertThat(actual.isFailure()).isTrue();
        assertThat(actual.getCause()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    public void shouldConvertNonEmptyToTryUsingExceptionSupplier() {
        final Exception x = new Exception("test");
        assertThat(of(1, 2, 3).toTry(() -> x)).isEqualTo(Try.of(() -> 1));
    }

    @Test
    public void shouldConvertEmptyToTryUsingExceptionSupplier() {
        final Exception x = new Exception("test");
        assertThat(empty().toTry(() -> x)).isEqualTo(Try.failure(x));
    }

    @Test
    public void shouldConvertToVector() {
        final Value<Integer> value = of(1, 2, 3);
        final Vector<Integer> vector = value.toVector();
        if (value.isSingleValued()) {
            assertThat(vector).isEqualTo(Vector.of(1));
        } else {
            assertThat(vector).isEqualTo(Vector.of(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaArray() {
        final Value<Integer> value = of(1, 2, 3);
        final Object[] ints = value.toJavaArray();
        if (value.isSingleValued()) {
            assertThat(ints).isEqualTo(new int[] { 1 });
        } else {
            assertThat(ints).isEqualTo(new int[] { 1, 2, 3 });
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHint() {
        final Value<Integer> value = of(1, 2, 3);
        final Integer[] ints = value.toJavaArray(Integer.class);
        if (value.isSingleValued()) {
            assertThat(ints).containsOnly(1);
        } else {
            assertThat(ints).containsOnly(1, 2, 3);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveBoolean() {
        final Value<Boolean> value = of(true, false);
        final Boolean[] array = value.toJavaArray(boolean.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly(true);
        } else {
            assertThat(array).containsOnly(true, false);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveByte() {
        final Value<Byte> value = of((byte) 1, (byte) 2);
        final Byte[] array = value.toJavaArray(byte.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly((byte) 1);
        } else {
            assertThat(array).containsOnly((byte) 1, (byte) 2);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveChar() {
        final Value<Character> value = of('a', 'b');
        final Character[] array = value.toJavaArray(char.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly('a');
        } else {
            assertThat(array).containsOnly('a', 'b');
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveDouble() {
        final Value<Double> value = of(.1, .2);
        final Double[] array = value.toJavaArray(double.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly(.1);
        } else {
            assertThat(array).containsOnly(.1, .2);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveFloat() {
        final Value<Float> value = of(.1f, .2f);
        final Float[] array = value.toJavaArray(float.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly(.1f);
        } else {
            assertThat(array).containsOnly(.1f, .2f);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveInt() {
        final Value<Integer> value = of(1, 2);
        final Integer[] array = value.toJavaArray(int.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly(1);
        } else {
            assertThat(array).containsOnly(1, 2);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveLong() {
        final Value<Long> value = of(1L, 2L);
        final Long[] array = value.toJavaArray(long.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly(1L);
        } else {
            assertThat(array).containsOnly(1L, 2L);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveShort() {
        final Value<Short> value = of((short) 1, (short) 2);
        final Short[] array = value.toJavaArray(short.class);
        if (value.isSingleValued()) {
            assertThat(array).containsOnly((short) 1);
        } else {
            assertThat(array).containsOnly((short) 1, (short) 2);
        }
    }

    @Test
    public void shouldConvertToJavaArrayWithTypeHintPrimitiveVoid() {
        final Value<Void> value = of((Void) null);
        final Void[] array = value.toJavaArray(void.class);
        assertThat(array).containsOnly((Void) null);
    }

    @Test
    public void shouldConvertToJavaCollectionUsingSupplier() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.List<Integer> ints = value.toJavaCollection(ArrayList::new);
        if (value.isSingleValued()) {
            assertThat(ints).isEqualTo(Collections.singletonList(1));
        } else {
            assertThat(ints).isEqualTo(Arrays.asList(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaList() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.List<Integer> list = value.toJavaList();
        if (value.isSingleValued()) {
            assertThat(list).isEqualTo(Collections.singletonList(1));
        } else {
            assertThat(list).isEqualTo(Arrays.asList(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaListUsingSupplier() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.List<Integer> ints = value.toJavaList(ArrayList::new);
        if (value.isSingleValued()) {
            assertThat(ints).isEqualTo(Collections.singletonList(1));
        } else {
            assertThat(ints).isEqualTo(Arrays.asList(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaMapUsingFunction() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Map<Integer, Integer> map = value.toJavaMap(v -> Tuple.of(v, v));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1));
        } else {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1, 2, 2, 3, 3));
        }
    }

    @Test
    public void shouldConvertToJavaMapUsingSupplierAndFunction() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Map<Integer, Integer> map = value.toJavaMap(java.util.HashMap::new, i -> Tuple.of(i, i));
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1));
        } else {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, 1, 2, 2, 3, 3));
        }
    }

    @Test
    public void shouldConvertToJavaMapUsingSupplierAndTwoFunction() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Map<Integer, String> map = value.toJavaMap(java.util.HashMap::new, Function.identity(), String::valueOf);
        if (value.isSingleValued()) {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, "1"));
        } else {
            assertThat(map).isEqualTo(JavaCollections.javaMap(1, "1", 2, "2", 3, "3"));
        }
    }

    @Test
    public void shouldConvertToJavaOptional() {
        assertThat(of(1, 2, 3).toJavaOptional()).isEqualTo(Optional.of(1));
    }

    @Test
    public void shouldConvertToJavaSet() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Set<Integer> set = value.toJavaSet();
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1));
        } else {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaSetUsingSupplier() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.Set<Integer> set = value.toJavaSet(java.util.HashSet::new);
        if (value.isSingleValued()) {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1));
        } else {
            assertThat(set).isEqualTo(JavaCollections.javaSet(1, 2, 3));
        }
    }

    @Test
    public void shouldConvertToJavaStream() {
        final Value<Integer> value = of(1, 2, 3);
        final java.util.stream.Stream<Integer> s1 = value.toJavaStream();
        if (value.isSingleValued()) {
            final java.util.stream.Stream<Integer> s2 = java.util.stream.Stream.of(1);
            assertThat(List.ofAll(s1::iterator)).isEqualTo(List.ofAll(s2::iterator));
        } else {
            final java.util.stream.Stream<Integer> s2 = java.util.stream.Stream.of(1, 2, 3);
            assertThat(List.ofAll(s1::iterator)).isEqualTo(List.ofAll(s2::iterator));
        }
    }

    @Test
    public void shouldConvertToEitherLeftFromValueSupplier() {
        Either<Integer, String> either = of(0).toLeft(() -> "fallback");
        assertThat(either.isLeft()).isTrue();
        assertThat(either.getLeft()).isEqualTo(0);

        Either<Object, String> either2 = empty().toLeft(() -> "fallback");
        assertThat(either2.isRight()).isTrue();
        assertThat(either2.get()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherLeftFromValue() {
        Either<Integer, String> either = of(0).toLeft("fallback");
        assertThat(either.isLeft()).isTrue();
        assertThat(either.getLeft()).isEqualTo(0);

        Either<Object, String> either2 = empty().toLeft("fallback");
        assertThat(either2.isRight()).isTrue();
        assertThat(either2.get()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherRightFromValueSupplier() {
        Either<String, Integer> either = of(0).toRight(() -> "fallback");
        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(0);

        Either<String, Object> either2 = empty().toRight(() -> "fallback");
        assertThat(either2.isLeft()).isTrue();
        assertThat(either2.getLeft()).isEqualTo("fallback");
    }

    @Test
    public void shouldConvertToEitherRightFromValue() {
        Either<String, Integer> either = of(0).toRight("fallback");
        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(0);

        Either<String, Object> either2 = empty().toRight("fallback");
        assertThat(either2.isLeft()).isTrue();
        assertThat(either2.getLeft()).isEqualTo("fallback");
    }

    // -- exists

    @Test
    public void shouldBeAwareOfExistingElement() {
        final Value<Integer> value = of(1, 2);
        if (value.isSingleValued()) {
            assertThat(value.exists(i -> i == 1)).isTrue();
        } else {
            assertThat(value.exists(i -> i == 2)).isTrue();
        }

    }

    @Test
    public void shouldBeAwareOfNonExistingElement() {
        assertThat(this.<Integer> empty().exists(i -> i == 1)).isFalse();
    }

    // -- forAll

    @Test
    public void shouldBeAwareOfPropertyThatHoldsForAll() {
        assertThat(of(2, 4).forAll(i -> i % 2 == 0)).isTrue();
    }

    @Test
    public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
        assertThat(of(1, 2).forAll(i -> i % 2 == 0)).isFalse();
    }

    // ### ValueModule.Iterable ###

    // -- corresponds

    @Test
    public void shouldntCorrespondsNilNil() {
        assertThat(empty().corresponds(empty(), (o1, o2) -> true)).isTrue();
    }

    @Test
    public void shouldntCorrespondsNilNonNil() {
        assertThat(empty().corresponds(of(1), (o1, i2) -> true)).isFalse();
    }

    @Test
    public void shouldntCorrespondsNonNilNil() {
        assertThat(of(1).corresponds(empty(), (i1, o2) -> true)).isFalse();
    }

    @Test
    public void shouldntCorrespondsDifferentLengths() {
        if (!empty().isSingleValued()) {
            assertThat(of(1, 2, 3).corresponds(of(1, 2), (i1, i2) -> true)).isFalse();
            assertThat(of(1, 2).corresponds(of(1, 2, 3), (i1, i2) -> true)).isFalse();
        }
    }

    @Test
    public void shouldCorresponds() {
        assertThat(of(1, 2, 3).corresponds(of(3, 4, 5), (i1, i2) -> i1 == i2 - 2)).isTrue();
        assertThat(of(1, 2, 3).corresponds(of(1, 2, 3), (i1, i2) -> i1 == i2 + 1)).isFalse();
    }

    @Test
    public void shouldHaveAReasonableToString() {
        final Value<Integer> value = of(1, 2);
        value.toList(); // evaluate all elements (e.g. for Stream)
        final String actual = value.toString();

        if (value.isSingleValued()) {
            assertThat(actual).contains("1");
        } else {
            assertThat(actual).contains("1", "2");
        }
    }

    // -- Serialization

    /**
     * States whether the specific Value implementation is Serializable.
     * <p>
     * Test classes override this method to return false if needed.
     *
     * @return true (by default), if the Value is Serializable, false otherwise
     */
    protected boolean isSerializable() {
        return true;
    }

    @Test
    public void shouldSerializeDeserializeEmpty() {
        if (isSerializable()) {
            final Value<?> testee = empty();
            final Value<?> actual = deserialize(serialize(testee));
            assertThat(actual).isEqualTo(testee);
        }
    }

    @Test
    public void shouldSerializeDeserializeSingleValued() {
        if (isSerializable()) {
            final Value<?> testee = of(1);
            final Value<?> actual = deserialize(serialize(testee));
            assertThat(actual).isEqualTo(testee);
        }
    }

    @Test
    public void shouldSerializeDeserializeMultiValued() {
        if (isSerializable()) {
            final Value<?> testee = of(1, 2, 3);
            final Value<?> actual = deserialize(serialize(testee));
            assertThat(actual).isEqualTo(testee);
        }
    }
}
