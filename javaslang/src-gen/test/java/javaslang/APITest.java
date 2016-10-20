/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static javaslang.API.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import javaslang.control.Try;
import org.junit.Test;

public class APITest {

    @Test
    public void shouldNotBeInstantiable() {
        AssertionsExtensions.assertThat(API.class).isNotInstantiable();
    }

    // -- shortcuts

    @Test
    public void shouldCompileTODOAndThrowDefaultMessageAtRuntime() {
        try {
            final String s = TODO();
            fail("TODO() should throw. s: " + s);
        } catch(NotImplementedError err) {
            assertThat(err.getMessage()).isEqualTo("An implementation is missing.");
        }
    }

    @Test
    public void shouldCompileTODOAndThrowGivenMessageAtRuntime() {
        final String msg = "Don't try this in production!";
        try {
            final String s = TODO(msg);
            fail("TODO(String) should throw. s: " + s);
        } catch(NotImplementedError err) {
            assertThat(err.getMessage()).isEqualTo(msg);
        }
    }

    @Test
    public void shouldCallprint_Object() {
        print("ok");
    }

    @Test
    public void shouldCallprintf() {
        printf("%s", "ok");
    }

    @Test
    public void shouldCallprintln_Object() {
        println("ok");
    }

    @Test
    public void shouldCallprintln() {
        println();
    }

    //
    // Alias should return not null.
    // More specific test for each aliased class implemented in separate test class
    //

    @Test
    public void shouldFunction0ReturnNotNull() {
        assertThat(Function0(() -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction0ReturnNotNull() {
        assertThat(CheckedFunction0(() -> null)).isNotNull();
    }

    @Test
    public void shouldFunction1ReturnNotNull() {
        assertThat(Function1((v1) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction1ReturnNotNull() {
        assertThat(CheckedFunction1((v1) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction2ReturnNotNull() {
        assertThat(Function2((v1, v2) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction2ReturnNotNull() {
        assertThat(CheckedFunction2((v1, v2) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction3ReturnNotNull() {
        assertThat(Function3((v1, v2, v3) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction3ReturnNotNull() {
        assertThat(CheckedFunction3((v1, v2, v3) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction4ReturnNotNull() {
        assertThat(Function4((v1, v2, v3, v4) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction4ReturnNotNull() {
        assertThat(CheckedFunction4((v1, v2, v3, v4) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction5ReturnNotNull() {
        assertThat(Function5((v1, v2, v3, v4, v5) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction5ReturnNotNull() {
        assertThat(CheckedFunction5((v1, v2, v3, v4, v5) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction6ReturnNotNull() {
        assertThat(Function6((v1, v2, v3, v4, v5, v6) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction6ReturnNotNull() {
        assertThat(CheckedFunction6((v1, v2, v3, v4, v5, v6) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction7ReturnNotNull() {
        assertThat(Function7((v1, v2, v3, v4, v5, v6, v7) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction7ReturnNotNull() {
        assertThat(CheckedFunction7((v1, v2, v3, v4, v5, v6, v7) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction8ReturnNotNull() {
        assertThat(Function8((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction8ReturnNotNull() {
        assertThat(CheckedFunction8((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isNotNull();
    }

    @Test
    public void shouldUnchecked0ReturnNonCheckedFunction() {
        assertThat(Unchecked(() -> null)).isInstanceOf(Function0.class);
    }

    @Test
    public void shouldUnchecked1ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1) -> null)).isInstanceOf(Function1.class);
    }

    @Test
    public void shouldUnchecked2ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2) -> null)).isInstanceOf(Function2.class);
    }

    @Test
    public void shouldUnchecked3ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3) -> null)).isInstanceOf(Function3.class);
    }

    @Test
    public void shouldUnchecked4ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3, v4) -> null)).isInstanceOf(Function4.class);
    }

    @Test
    public void shouldUnchecked5ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3, v4, v5) -> null)).isInstanceOf(Function5.class);
    }

    @Test
    public void shouldUnchecked6ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3, v4, v5, v6) -> null)).isInstanceOf(Function6.class);
    }

    @Test
    public void shouldUnchecked7ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3, v4, v5, v6, v7) -> null)).isInstanceOf(Function7.class);
    }

    @Test
    public void shouldUnchecked8ReturnNonCheckedFunction() {
        assertThat(Unchecked((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isInstanceOf(Function8.class);
    }

    @Test
    public void shouldTuple0ReturnNotNull() {
        assertThat(Tuple()).isNotNull();
    }

    @Test
    public void shouldTuple1ReturnNotNull() {
        assertThat(Tuple(1)).isNotNull();
    }

    @Test
    public void shouldTuple2ReturnNotNull() {
        assertThat(Tuple(1, 2)).isNotNull();
    }

    @Test
    public void shouldTuple3ReturnNotNull() {
        assertThat(Tuple(1, 2, 3)).isNotNull();
    }

    @Test
    public void shouldTuple4ReturnNotNull() {
        assertThat(Tuple(1, 2, 3, 4)).isNotNull();
    }

    @Test
    public void shouldTuple5ReturnNotNull() {
        assertThat(Tuple(1, 2, 3, 4, 5)).isNotNull();
    }

    @Test
    public void shouldTuple6ReturnNotNull() {
        assertThat(Tuple(1, 2, 3, 4, 5, 6)).isNotNull();
    }

    @Test
    public void shouldTuple7ReturnNotNull() {
        assertThat(Tuple(1, 2, 3, 4, 5, 6, 7)).isNotNull();
    }

    @Test
    public void shouldTuple8ReturnNotNull() {
        assertThat(Tuple(1, 2, 3, 4, 5, 6, 7, 8)).isNotNull();
    }

    @Test
    public void shouldRightReturnNotNull() {
        assertThat(Right(null)).isNotNull();
    }

    @Test
    public void shouldLeftReturnNotNull() {
        assertThat(Left(null)).isNotNull();
    }

    @Test
    public void shouldFutureWithSupplierReturnNotNull() {
        final Future<?> future = Future(() -> 1).await();
        assertThat(future).isNotNull();
        assertThat(future.isSuccess()).isTrue();
    }

    @Test
    public void shouldFutureWithinExecutorWithSupplierReturnNotNull() {
        final Future<?> future = Future(Executors.newSingleThreadExecutor(), () -> 1).await();
        assertThat(future).isNotNull();
        assertThat(future.isSuccess()).isTrue();
    }

    @Test
    public void shouldFutureWithValueReturnNotNull() {
        final Future<?> future = Future(1).await();
        assertThat(future).isNotNull();
        assertThat(future.isSuccess()).isTrue();
    }

    @Test
    public void shouldFutureWithinExecutorWithValueReturnNotNull() {
        final Future<?> future = Future(Executors.newSingleThreadExecutor(), 1).await();
        assertThat(future).isNotNull();
        assertThat(future.isSuccess()).isTrue();
    }

    @Test
    public void shouldFutureWithErrorReturnNotNull() {
        final Future<?> future = Future(new Error()).await();
        assertThat(future).isNotNull();
        assertThat(future.isFailure()).isTrue();
    }

    @Test
    public void shouldFutureWithinExecutorWithErrorReturnNotNull() {
        final Future<?> future = Future(Executors.newSingleThreadExecutor(), new Error()).await();
        assertThat(future).isNotNull();
        assertThat(future.isFailure()).isTrue();
    }

    @Test
    public void shouldLazyReturnNotNull() {
        assertThat(Lazy(() -> 1)).isNotNull();
    }

    @Test
    public void shouldOptionReturnNotNull() {
        assertThat(Option(1)).isNotNull();
    }

    @Test
    public void shouldSomeReturnNotNull() {
        assertThat(Some(1)).isNotNull();
    }

    @Test
    public void shouldNoneReturnNotNull() {
        assertThat(None()).isNotNull();
    }

    @Test
    public void shouldTryReturnNotNull() {
        final Try<?> t = Try(() -> 1);
        assertThat(t).isNotNull();
        assertThat(t.isSuccess()).isTrue();
    }

    @Test
    public void shouldSuccessReturnNotNull() {
        final Try<?> t = Success(1);
        assertThat(t).isNotNull();
        assertThat(t.isSuccess()).isTrue();
    }

    @Test
    public void shouldFailureReturnNotNull() {
        final Try<?> t = Failure(new Error());
        assertThat(t).isNotNull();
        assertThat(t.isFailure()).isTrue();
    }

    @Test
    public void shouldValidReturnNotNull() {
        assertThat(Valid(1)).isNotNull();
    }

    @Test
    public void shouldInvalidReturnNotNull() {
        assertThat(Invalid(new Error())).isNotNull();
    }

    @Test
    public void shouldCharReturnNotNull() {
        assertThat((Iterable<Character>) CharSeq('1')).isNotNull();
    }

    @Test
    public void shouldCharArrayReturnNotNull() {
        assertThat((Iterable<Character>) CharSeq('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldCharSeqReturnNotNull() {
        assertThat((Iterable<Character>) CharSeq("123")).isNotNull();
    }

    @Test
    public void shouldEmptyArrayReturnNotNull() {
        assertThat(Array()).isNotNull();
    }

    @Test
    public void shouldArrayWithSingleReturnNotNull() {
        assertThat(Array('1')).isNotNull();
    }

    @Test
    public void shouldArrayWithVarArgReturnNotNull() {
        assertThat(Array('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldArrayWithIterableReturnNotNull() {
        assertThat(Array(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldArrayWithStreamReturnNotNull() {
        assertThat(Array(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyVectorReturnNotNull() {
        assertThat(Vector()).isNotNull();
    }

    @Test
    public void shouldVectorWithSingleReturnNotNull() {
        assertThat(Vector('1')).isNotNull();
    }

    @Test
    public void shouldVectorWithVarArgReturnNotNull() {
        assertThat(Vector('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldVectorWithIterableReturnNotNull() {
        assertThat(Vector(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldVectorWithStreamReturnNotNull() {
        assertThat(Vector(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyListReturnNotNull() {
        assertThat(List()).isNotNull();
    }

    @Test
    public void shouldListWithSingleReturnNotNull() {
        assertThat(List('1')).isNotNull();
    }

    @Test
    public void shouldListWithVarArgReturnNotNull() {
        assertThat(List('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldListWithIterableReturnNotNull() {
        assertThat(List(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldListWithStreamReturnNotNull() {
        assertThat(List(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyStreamReturnNotNull() {
        assertThat(Stream()).isNotNull();
    }

    @Test
    public void shouldStreamWithSingleReturnNotNull() {
        assertThat(Stream('1')).isNotNull();
    }

    @Test
    public void shouldStreamWithVarArgReturnNotNull() {
        assertThat(Stream('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldStreamWithIterableReturnNotNull() {
        assertThat(Stream(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldStreamWithStreamReturnNotNull() {
        assertThat(Stream(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyQueueReturnNotNull() {
        assertThat(Queue()).isNotNull();
    }

    @Test
    public void shouldQueueWithSingleReturnNotNull() {
        assertThat(Queue('1')).isNotNull();
    }

    @Test
    public void shouldQueueWithVarArgReturnNotNull() {
        assertThat(Queue('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldQueueWithIterableReturnNotNull() {
        assertThat(Queue(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldQueueWithStreamReturnNotNull() {
        assertThat(Queue(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyLinkedSetReturnNotNull() {
        assertThat(LinkedSet()).isNotNull();
    }

    @Test
    public void shouldLinkedSetWithSingleReturnNotNull() {
        assertThat(LinkedSet('1')).isNotNull();
    }

    @Test
    public void shouldLinkedSetWithVarArgReturnNotNull() {
        assertThat(LinkedSet('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldLinkedSetWithIterableReturnNotNull() {
        assertThat(LinkedSet(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldLinkedSetWithStreamReturnNotNull() {
        assertThat(LinkedSet(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptySetReturnNotNull() {
        assertThat(Set()).isNotNull();
    }

    @Test
    public void shouldSetWithSingleReturnNotNull() {
        assertThat(Set('1')).isNotNull();
    }

    @Test
    public void shouldSetWithVarArgReturnNotNull() {
        assertThat(Set('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldSetWithIterableReturnNotNull() {
        assertThat(Set(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldSetWithStreamReturnNotNull() {
        assertThat(Set(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptySeqReturnNotNull() {
        assertThat(Seq()).isNotNull();
    }

    @Test
    public void shouldSeqWithSingleReturnNotNull() {
        assertThat(Seq('1')).isNotNull();
    }

    @Test
    public void shouldSeqWithVarArgReturnNotNull() {
        assertThat(Seq('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldSeqWithIterableReturnNotNull() {
        assertThat(Seq(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldSeqWithStreamReturnNotNull() {
        assertThat(Seq(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyIndexedSeqReturnNotNull() {
        assertThat(IndexedSeq()).isNotNull();
    }

    @Test
    public void shouldIndexedSeqWithSingleReturnNotNull() {
        assertThat(IndexedSeq('1')).isNotNull();
    }

    @Test
    public void shouldIndexedSeqWithVarArgReturnNotNull() {
        assertThat(IndexedSeq('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldIndexedSeqWithIterableReturnNotNull() {
        assertThat(IndexedSeq(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldIndexedSeqWithStreamReturnNotNull() {
        assertThat(IndexedSeq(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptySortedSetReturnNotNull() {
        assertThat(SortedSet()).isNotNull();
    }

    @Test
    public void shouldEmptySortedSetWithComparatorReturnNotNull() {
        assertThat(SortedSet((Comparator<Character>) Character::compareTo)).isNotNull();
    }

    @Test
    public void shouldSortedSetWithSingleReturnNotNull() {
        assertThat(SortedSet('1')).isNotNull();
    }

    @Test
    public void shouldSortedSetWithSingleAndComparatorReturnNotNull() {
        assertThat(SortedSet(Character::compareTo, '1')).isNotNull();
    }

    @Test
    public void shouldSortedSetWithVarArgReturnNotNull() {
        assertThat(SortedSet('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldSortedSetWithVarArgAndComparatorReturnNotNull() {
        assertThat(SortedSet((Comparator<Character>) Character::compareTo, '1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldSortedSetWithIterableReturnNotNull() {
        assertThat(SortedSet(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldSortedSetWithIterableAndComparatorReturnNotNull() {
        assertThat(SortedSet(Character::compareTo, CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldSortedSetWithStreamReturnNotNull() {
        assertThat(SortedSet(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldSortedSetWithStreamAndComparatorReturnNotNull() {
        assertThat(SortedSet(Character::compareTo, Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyPriorityQueueReturnNotNull() {
        assertThat(PriorityQueue()).isNotNull();
    }

    @Test
    public void shouldEmptyPriorityQueueWithComparatorReturnNotNull() {
        assertThat(PriorityQueue((Comparator<Character>) Character::compareTo)).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithSingleReturnNotNull() {
        assertThat(PriorityQueue('1')).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithSingleAndComparatorReturnNotNull() {
        assertThat(PriorityQueue(Character::compareTo, '1')).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithVarArgReturnNotNull() {
        assertThat(PriorityQueue('1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithVarArgAndComparatorReturnNotNull() {
        assertThat(PriorityQueue((Comparator<Character>) Character::compareTo, '1', '2', '3')).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithIterableReturnNotNull() {
        assertThat(PriorityQueue(CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithIterableAndComparatorReturnNotNull() {
        assertThat(PriorityQueue(Character::compareTo, CharSeq('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithStreamReturnNotNull() {
        assertThat(PriorityQueue(Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldPriorityQueueWithStreamAndComparatorReturnNotNull() {
        assertThat(PriorityQueue(Character::compareTo, Stream.of('1', '2', '3'))).isNotNull();
    }

    @Test
    public void shouldEmptyLinkedMapReturnNotNull() {
        assertThat(LinkedMap()).isNotNull();
    }

    @Test
    public void shouldLinkedMapFromSingleReturnNotNull() {
        assertThat(LinkedMap(1, '1')).isNotNull();
    }

    @Test
    public void shouldLinkedMapFromTuplesReturnNotNull() {
        assertThat(LinkedMap(Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3'))).isNotNull();
    }

    @Test
    public void shouldLinkedMapFromMapReturnNotNull() {
        assertThat(LinkedMap(Collections.singletonMap(1, '1'))).isNotNull();
    }

    @Test
    public void shouldLinkedMapFromPairsReturnNotNull() {
        assertThat(LinkedMap(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldEmptyMapReturnNotNull() {
        assertThat(Map()).isNotNull();
    }

    @Test
    public void shouldMapFromSingleReturnNotNull() {
        assertThat(Map(1, '1')).isNotNull();
    }

    @Test
    public void shouldMapFromTuplesReturnNotNull() {
        assertThat(Map(Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3'))).isNotNull();
    }

    @Test
    public void shouldMapFromMapReturnNotNull() {
        assertThat(Map(Collections.singletonMap(1, '1'))).isNotNull();
    }

    @Test
    public void shouldMapFromPairsReturnNotNull() {
        assertThat(Map(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldEmptySortedMapReturnNotNull() {
        assertThat(SortedMap()).isNotNull();
    }

    @Test
    public void shouldSortedMapFromSingleReturnNotNull() {
        assertThat(SortedMap(1, '1')).isNotNull();
    }

    @Test
    public void shouldSortedMapFromTuplesReturnNotNull() {
        assertThat(SortedMap(Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3'))).isNotNull();
    }

    @Test
    public void shouldSortedMapFromMapReturnNotNull() {
        assertThat(SortedMap(Collections.singletonMap(1, '1'))).isNotNull();
    }

    @Test
    public void shouldSortedMapFromPairsReturnNotNull() {
        assertThat(SortedMap(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldEmptySortedMapFromComparatorReturnNotNull() {
        assertThat(SortedMap(Integer::compareTo)).isNotNull();
    }

    @Test
    public void shouldSortedMapFromSingleAndComparatorReturnNotNull() {
        assertThat(SortedMap((Comparator<Integer>)Integer::compareTo, 1, '1')).isNotNull();
    }

    @Test
    public void shouldSortedMapFromTuplesAndComparatorReturnNotNull() {
        assertThat(SortedMap((Comparator<Integer>)Integer::compareTo, Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3'))).isNotNull();
    }

    // -- run

    @Test
    public void shouldRunUnitAndReturnVoid() {
        int[] i = { 0 };
        @SuppressWarnings("unused")
        Void nothing = run(() -> i[0]++);
        assertThat(i[0]).isEqualTo(1);
    }

    // -- For

    @Test
    public void shouldIterateFor1UsingSimpleYield() {
        final List<Integer> list = List.of(1, 2, 3);
        final List<Integer> actual = For(list).yield().toList();
        assertThat(actual).isEqualTo(list);
    }

    @Test
    public void shouldIterateFor1() {
        final List<Integer> result = For(
            List.of(1, 2, 3)
        ).yield(i1 -> i1).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 1));
        assertThat(result.head()).isEqualTo(1);
        assertThat(result.last()).isEqualTo(3 * 1);
    }

    @Test
    public void shouldIterateFor2() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2) -> i1 + i2).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 2));
        assertThat(result.head()).isEqualTo(2);
        assertThat(result.last()).isEqualTo(3 * 2);
    }

    @Test
    public void shouldIterateFor3() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 3));
        assertThat(result.head()).isEqualTo(3);
        assertThat(result.last()).isEqualTo(3 * 3);
    }

    @Test
    public void shouldIterateFor4() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 4));
        assertThat(result.head()).isEqualTo(4);
        assertThat(result.last()).isEqualTo(3 * 4);
    }

    @Test
    public void shouldIterateFor5() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 5));
        assertThat(result.head()).isEqualTo(5);
        assertThat(result.last()).isEqualTo(3 * 5);
    }

    @Test
    public void shouldIterateFor6() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 6));
        assertThat(result.head()).isEqualTo(6);
        assertThat(result.last()).isEqualTo(3 * 6);
    }

    @Test
    public void shouldIterateFor7() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 7));
        assertThat(result.head()).isEqualTo(7);
        assertThat(result.last()).isEqualTo(3 * 7);
    }

    @Test
    public void shouldIterateFor8() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 8));
        assertThat(result.head()).isEqualTo(8);
        assertThat(result.last()).isEqualTo(3 * 8);
    }

    @Test
    public void shouldIterateNestedFor() {
        final List<String> result =
                For(Arrays.asList(1, 2), i ->
                        For(CharSeq.of('a', 'b')).yield(c -> i + ":" + c)).toList();
        assertThat(result).isEqualTo(List.of("1:a", "1:b", "2:a", "2:b"));
    }

    // -- Match

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndSupplier() {
        assertThat(Case(ignored -> true, ignored -> 1).apply(null)).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndSupplier() {
        assertThat(Case(ignored -> false, ignored -> 1).apply(null)).isEqualTo(Option.none());
    }

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndValue() {
        assertThat(Case(ignored -> true, 1).apply(null)).isEqualTo(Option.some(1));
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndValue() {
        assertThat(Case(ignored -> false, 1).apply(null)).isEqualTo(Option.none());
    }

    // -- Match patterns

    static class ClzMatch {}
    static class ClzMatch1 extends ClzMatch {}
    static class ClzMatch2 extends ClzMatch {}

    @Test
    public void shouldMatchPattern1() {
        final Tuple1<Integer> tuple = Tuple.of(1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), (m1) -> "fail"),
                Case(Patterns.Tuple1($()), (m1) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), () -> "fail"),
                Case(Patterns.Tuple1($()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple1($(0)), "fail"),
                Case(Patterns.Tuple1($()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern1.of(ClzMatch1.class, $(), t -> Tuple.of(null)), "fail"),
                Case(API.Match.Pattern1.of(ClzMatch2.class, $(), t -> Tuple.of(null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern2() {
        final Tuple2<Integer, Integer> tuple = Tuple.of(1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), (m1, m2) -> "fail"),
                Case(Patterns.Tuple2($(), $()), (m1, m2) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), () -> "fail"),
                Case(Patterns.Tuple2($(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple2($(0), $()), "fail"),
                Case(Patterns.Tuple2($(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern2.of(ClzMatch1.class, $(), $(), t -> Tuple.of(null, null)), "fail"),
                Case(API.Match.Pattern2.of(ClzMatch2.class, $(), $(), t -> Tuple.of(null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern3() {
        final Tuple3<Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), (m1, m2, m3) -> "fail"),
                Case(Patterns.Tuple3($(), $(), $()), (m1, m2, m3) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), () -> "fail"),
                Case(Patterns.Tuple3($(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple3($(0), $(), $()), "fail"),
                Case(Patterns.Tuple3($(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern3.of(ClzMatch1.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "fail"),
                Case(API.Match.Pattern3.of(ClzMatch2.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern4() {
        final Tuple4<Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), (m1, m2, m3, m4) -> "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), (m1, m2, m3, m4) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple4($(0), $(), $(), $()), "fail"),
                Case(Patterns.Tuple4($(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern4.of(ClzMatch1.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "fail"),
                Case(API.Match.Pattern4.of(ClzMatch2.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern5() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple5($(0), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple5($(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern5.of(ClzMatch1.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern5.of(ClzMatch2.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern6() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple6($(0), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple6($(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern6.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern6.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern7() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple7($(0), $(), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple7($(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern7.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern7.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern8() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case(Patterns.Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), "fail"),
                Case(Patterns.Tuple8($(), $(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(API.Match.Pattern8.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "fail"),
                Case(API.Match.Pattern8.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }
}