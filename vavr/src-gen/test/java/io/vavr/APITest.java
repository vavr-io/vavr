/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
package io.vavr;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static io.vavr.API.*;
import static io.vavr.OutputTester.captureStdOut;
import static io.vavr.Patterns.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Stream;
import io.vavr.concurrent.Future;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.Executors;
import org.junit.Test;

@SuppressWarnings("deprecation")
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
        assertThat(captureStdOut(()->print("ok"))).isEqualTo("ok");
    }

    @Test
    public void shouldCallprintf() {
        assertThat(captureStdOut(()->printf("%s", "ok"))).isEqualTo("ok");
    }

    @Test
    public void shouldCallprintln_Object() {
        assertThat(captureStdOut(()->println("ok"))).isEqualTo("ok\n");
    }

    @Test
    public void shouldCallprintln() {
        assertThat(captureStdOut(()->println())).isEqualTo("\n");
    }

    @Test
     public void shouldCallprintlnWithArguments() {
        assertThat(captureStdOut(() -> println("this", "and", "that"))).isEqualTo("this and that\n");
     }

    //
    // Alias should return not null.
    // More specific test for each aliased class implemented in separate test class
    //

    @Test
    public void shouldFunction0ReturnNotNull() {
        assertThat(Function(() -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction0ReturnNotNull() {
        assertThat(CheckedFunction(() -> null)).isNotNull();
    }

    @Test
    public void shouldFunction1ReturnNotNull() {
        assertThat(Function((v1) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction1ReturnNotNull() {
        assertThat(CheckedFunction((v1) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction2ReturnNotNull() {
        assertThat(Function((v1, v2) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction2ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction3ReturnNotNull() {
        assertThat(Function((v1, v2, v3) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction3ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction4ReturnNotNull() {
        assertThat(Function((v1, v2, v3, v4) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction4ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3, v4) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction5ReturnNotNull() {
        assertThat(Function((v1, v2, v3, v4, v5) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction5ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3, v4, v5) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction6ReturnNotNull() {
        assertThat(Function((v1, v2, v3, v4, v5, v6) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction6ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3, v4, v5, v6) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction7ReturnNotNull() {
        assertThat(Function((v1, v2, v3, v4, v5, v6, v7) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction7ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3, v4, v5, v6, v7) -> null)).isNotNull();
    }

    @Test
    public void shouldFunction8ReturnNotNull() {
        assertThat(Function((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isNotNull();
    }

    @Test
    public void shouldCheckedFunction8ReturnNotNull() {
        assertThat(CheckedFunction((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isNotNull();
    }

    @Test
    public void shouldUnchecked0ReturnNonCheckedFunction() {
        assertThat(unchecked(() -> null)).isInstanceOf(Function0.class);
    }

    @Test
    public void shouldUnchecked1ReturnNonCheckedFunction() {
        assertThat(unchecked((v1) -> null)).isInstanceOf(Function1.class);
    }

    @Test
    public void shouldUnchecked2ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2) -> null)).isInstanceOf(Function2.class);
    }

    @Test
    public void shouldUnchecked3ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3) -> null)).isInstanceOf(Function3.class);
    }

    @Test
    public void shouldUnchecked4ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3, v4) -> null)).isInstanceOf(Function4.class);
    }

    @Test
    public void shouldUnchecked5ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3, v4, v5) -> null)).isInstanceOf(Function5.class);
    }

    @Test
    public void shouldUnchecked6ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3, v4, v5, v6) -> null)).isInstanceOf(Function6.class);
    }

    @Test
    public void shouldUnchecked7ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3, v4, v5, v6, v7) -> null)).isInstanceOf(Function7.class);
    }

    @Test
    public void shouldUnchecked8ReturnNonCheckedFunction() {
        assertThat(unchecked((v1, v2, v3, v4, v5, v6, v7, v8) -> null)).isInstanceOf(Function8.class);
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
    public void shouldLinkedMapFromPairsReturnNotNull() {
        assertThat(LinkedMap(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldCreateLinkedMapFrom1Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2);
      assertThat(map.apply(1)).isEqualTo(2);
    }

    @Test
    public void shouldCreateLinkedMapFrom2Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
    }

    @Test
    public void shouldCreateLinkedMapFrom3Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
    }

    @Test
    public void shouldCreateLinkedMapFrom4Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
    }

    @Test
    public void shouldCreateLinkedMapFrom5Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
    }

    @Test
    public void shouldCreateLinkedMapFrom6Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
    }

    @Test
    public void shouldCreateLinkedMapFrom7Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
    }

    @Test
    public void shouldCreateLinkedMapFrom8Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
    }

    @Test
    public void shouldCreateLinkedMapFrom9Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
    }

    @Test
    public void shouldCreateLinkedMapFrom10Pairs() {
      Map<Integer, Integer> map = LinkedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18, 10, 20);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
      assertThat(map.apply(10)).isEqualTo(20);
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
    public void shouldMapFromPairsReturnNotNull() {
        assertThat(Map(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldCreateMapFrom1Pairs() {
      Map<Integer, Integer> map = Map(1, 2);
      assertThat(map.apply(1)).isEqualTo(2);
    }

    @Test
    public void shouldCreateMapFrom2Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
    }

    @Test
    public void shouldCreateMapFrom3Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
    }

    @Test
    public void shouldCreateMapFrom4Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
    }

    @Test
    public void shouldCreateMapFrom5Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
    }

    @Test
    public void shouldCreateMapFrom6Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
    }

    @Test
    public void shouldCreateMapFrom7Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
    }

    @Test
    public void shouldCreateMapFrom8Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
    }

    @Test
    public void shouldCreateMapFrom9Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
    }

    @Test
    public void shouldCreateMapFrom10Pairs() {
      Map<Integer, Integer> map = Map(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18, 10, 20);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
      assertThat(map.apply(10)).isEqualTo(20);
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
    public void shouldSortedMapFromPairsReturnNotNull() {
        assertThat(SortedMap(1, '1', 2, '2', 3, '3')).isNotNull();
    }

    @Test
    public void shouldCreateSortedMapFrom1Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2);
      assertThat(map.apply(1)).isEqualTo(2);
    }

    @Test
    public void shouldCreateSortedMapFrom2Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
    }

    @Test
    public void shouldCreateSortedMapFrom3Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
    }

    @Test
    public void shouldCreateSortedMapFrom4Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
    }

    @Test
    public void shouldCreateSortedMapFrom5Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
    }

    @Test
    public void shouldCreateSortedMapFrom6Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
    }

    @Test
    public void shouldCreateSortedMapFrom7Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
    }

    @Test
    public void shouldCreateSortedMapFrom8Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
    }

    @Test
    public void shouldCreateSortedMapFrom9Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
    }

    @Test
    public void shouldCreateSortedMapFrom10Pairs() {
      Map<Integer, Integer> map = SortedMap(1, 2, 2, 4, 3, 6, 4, 8, 5, 10, 6, 12, 7, 14, 8, 16, 9, 18, 10, 20);
      assertThat(map.apply(1)).isEqualTo(2);
      assertThat(map.apply(2)).isEqualTo(4);
      assertThat(map.apply(3)).isEqualTo(6);
      assertThat(map.apply(4)).isEqualTo(8);
      assertThat(map.apply(5)).isEqualTo(10);
      assertThat(map.apply(6)).isEqualTo(12);
      assertThat(map.apply(7)).isEqualTo(14);
      assertThat(map.apply(8)).isEqualTo(16);
      assertThat(map.apply(9)).isEqualTo(18);
      assertThat(map.apply(10)).isEqualTo(20);
    }

    @Test
    public void shouldEmptySortedMapFromComparatorReturnNotNull() {
        assertThat(SortedMap(Integer::compareTo)).isNotNull();
    }

    @Test
    public void shouldSortedMapFromSingleAndComparatorReturnNotNull() {
        assertThat(SortedMap(Integer::compareTo, 1, '1')).isNotNull();
    }

    @Test
    public void shouldSortedMapFromTuplesAndComparatorReturnNotNull() {
        assertThat(SortedMap((Comparator<Integer>)Integer::compareTo, Tuple(1, '1'), Tuple(2, '2'), Tuple(3, '3'))).isNotNull();
    }

    // -- run

    @Test
    public void shouldRunUnitAndReturnVoid() {
        int[] i = { 0 };
        Void nothing = run(() -> i[0]++);
        assertThat(nothing).isNull();
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
    public void shouldIterateForList1() {
        final List<Integer> result = For(
            List.of(1, 2, 3)
        ).yield(i1 -> i1).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 1));
        assertThat(result.head()).isEqualTo(1);
        assertThat(result.last()).isEqualTo(3 * 1);
    }

    @Test
    public void shouldIterateForList2() {
        final List<Integer> result = For(
            List.of(1, 2, 3),
            List.of(1, 2, 3)
        ).yield((i1, i2) -> i1 + i2).toList();
        assertThat(result.length()).isEqualTo((int) Math.pow(3, 2));
        assertThat(result.head()).isEqualTo(2);
        assertThat(result.last()).isEqualTo(3 * 2);
    }

    @Test
    public void shouldIterateForList3() {
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
    public void shouldIterateForList4() {
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
    public void shouldIterateForList5() {
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
    public void shouldIterateForList6() {
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
    public void shouldIterateForList7() {
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
    public void shouldIterateForList8() {
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
    public void shouldIterateForOption1() {
        final Option<Integer> result = For(
            Option.of(1)
        ).yield(i1 -> i1);
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void shouldIterateForOption2() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2)
        ).yield((i1, i2) -> i1 + i2);
        assertThat(result.get()).isEqualTo(3);
    }

    @Test
    public void shouldIterateForOption3() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3);
        assertThat(result.get()).isEqualTo(6);
    }

    @Test
    public void shouldIterateForOption4() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3),
            Option.of(4)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4);
        assertThat(result.get()).isEqualTo(10);
    }

    @Test
    public void shouldIterateForOption5() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3),
            Option.of(4),
            Option.of(5)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5);
        assertThat(result.get()).isEqualTo(15);
    }

    @Test
    public void shouldIterateForOption6() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3),
            Option.of(4),
            Option.of(5),
            Option.of(6)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6);
        assertThat(result.get()).isEqualTo(21);
    }

    @Test
    public void shouldIterateForOption7() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3),
            Option.of(4),
            Option.of(5),
            Option.of(6),
            Option.of(7)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7);
        assertThat(result.get()).isEqualTo(28);
    }

    @Test
    public void shouldIterateForOption8() {
        final Option<Integer> result = For(
            Option.of(1),
            Option.of(2),
            Option.of(3),
            Option.of(4),
            Option.of(5),
            Option.of(6),
            Option.of(7),
            Option.of(8)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8);
        assertThat(result.get()).isEqualTo(36);
    }

    @Test
    public void shouldIterateForFuture1() {
        final Future<Integer> result = For(
            Future.of(() -> 1)
        ).yield(i1 -> i1);
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void shouldIterateForFuture2() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2)
        ).yield((i1, i2) -> i1 + i2);
        assertThat(result.get()).isEqualTo(3);
    }

    @Test
    public void shouldIterateForFuture3() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3);
        assertThat(result.get()).isEqualTo(6);
    }

    @Test
    public void shouldIterateForFuture4() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3),
            Future.of(() -> 4)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4);
        assertThat(result.get()).isEqualTo(10);
    }

    @Test
    public void shouldIterateForFuture5() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3),
            Future.of(() -> 4),
            Future.of(() -> 5)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5);
        assertThat(result.get()).isEqualTo(15);
    }

    @Test
    public void shouldIterateForFuture6() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3),
            Future.of(() -> 4),
            Future.of(() -> 5),
            Future.of(() -> 6)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6);
        assertThat(result.get()).isEqualTo(21);
    }

    @Test
    public void shouldIterateForFuture7() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3),
            Future.of(() -> 4),
            Future.of(() -> 5),
            Future.of(() -> 6),
            Future.of(() -> 7)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7);
        assertThat(result.get()).isEqualTo(28);
    }

    @Test
    public void shouldIterateForFuture8() {
        final Future<Integer> result = For(
            Future.of(() -> 1),
            Future.of(() -> 2),
            Future.of(() -> 3),
            Future.of(() -> 4),
            Future.of(() -> 5),
            Future.of(() -> 6),
            Future.of(() -> 7),
            Future.of(() -> 8)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8);
        assertThat(result.get()).isEqualTo(36);
    }

    @Test
    public void shouldIterateForEither1() {
        final Either<Object, Integer> result = For(
                Either.right(1)
        ).yield((i1) -> i1);
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void shouldIterateForEither2() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2)
        ).yield((i1, i2) -> i1 + i2);
        assertThat(result.get()).isEqualTo(3);
    }

    @Test
    public void shouldIterateForEither3() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3);
        assertThat(result.get()).isEqualTo(6);
    }

    @Test
    public void shouldIterateForEither4() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3),
                Either.right(4)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4);
        assertThat(result.get()).isEqualTo(10);
    }

    @Test
    public void shouldIterateForEither5() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3),
                Either.right(4),
                Either.right(5)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5);
        assertThat(result.get()).isEqualTo(15);
    }

    @Test
    public void shouldIterateForEither6() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3),
                Either.right(4),
                Either.right(5),
                Either.right(6)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6);
        assertThat(result.get()).isEqualTo(21);
    }

    @Test
    public void shouldIterateForEither7() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3),
                Either.right(4),
                Either.right(5),
                Either.right(6),
                Either.right(7)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7);
        assertThat(result.get()).isEqualTo(28);
    }

    @Test
    public void shouldIterateForEither8() {
        final Either<Object, Integer> result = For(
                Either.right(1),
                Either.right(2),
                Either.right(3),
                Either.right(4),
                Either.right(5),
                Either.right(6),
                Either.right(7),
                Either.right(8)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8);
        assertThat(result.get()).isEqualTo(36);
    }

    @Test
    public void shouldIterateForTry1() {
        final Try<Integer> result = For(
            Try.of(() -> 1)
        ).yield(i1 -> i1);
        assertThat(result.get()).isEqualTo(1);
    }

    @Test
    public void shouldIterateForTry2() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2)
        ).yield((i1, i2) -> i1 + i2);
        assertThat(result.get()).isEqualTo(3);
    }

    @Test
    public void shouldIterateForTry3() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3)
        ).yield((i1, i2, i3) -> i1 + i2 + i3);
        assertThat(result.get()).isEqualTo(6);
    }

    @Test
    public void shouldIterateForTry4() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3),
            Try.of(() -> 4)
        ).yield((i1, i2, i3, i4) -> i1 + i2 + i3 + i4);
        assertThat(result.get()).isEqualTo(10);
    }

    @Test
    public void shouldIterateForTry5() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3),
            Try.of(() -> 4),
            Try.of(() -> 5)
        ).yield((i1, i2, i3, i4, i5) -> i1 + i2 + i3 + i4 + i5);
        assertThat(result.get()).isEqualTo(15);
    }

    @Test
    public void shouldIterateForTry6() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3),
            Try.of(() -> 4),
            Try.of(() -> 5),
            Try.of(() -> 6)
        ).yield((i1, i2, i3, i4, i5, i6) -> i1 + i2 + i3 + i4 + i5 + i6);
        assertThat(result.get()).isEqualTo(21);
    }

    @Test
    public void shouldIterateForTry7() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3),
            Try.of(() -> 4),
            Try.of(() -> 5),
            Try.of(() -> 6),
            Try.of(() -> 7)
        ).yield((i1, i2, i3, i4, i5, i6, i7) -> i1 + i2 + i3 + i4 + i5 + i6 + i7);
        assertThat(result.get()).isEqualTo(28);
    }

    @Test
    public void shouldIterateForTry8() {
        final Try<Integer> result = For(
            Try.of(() -> 1),
            Try.of(() -> 2),
            Try.of(() -> 3),
            Try.of(() -> 4),
            Try.of(() -> 5),
            Try.of(() -> 6),
            Try.of(() -> 7),
            Try.of(() -> 8)
        ).yield((i1, i2, i3, i4, i5, i6, i7, i8) -> i1 + i2 + i3 + i4 + i5 + i6 + i7 + i8);
        assertThat(result.get()).isEqualTo(36);
    }

    @Test
    public void shouldIterateNestedFor() {
        final List<String> result =
                For(Arrays.asList(1, 2), i ->
                        For(List.of('a', 'b')).yield(c -> i + ":" + c)).toList();
        assertThat(result).isEqualTo(List.of("1:a", "1:b", "2:a", "2:b"));
    }

    // -- Match

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndSupplier() {
        final Match.Case<Object, Integer> _case = Case($(ignored -> true), ignored -> 1);
        assertThat(_case.isDefinedAt(null)).isTrue();
        assertThat(_case.apply(null)).isEqualTo(1);
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndSupplier() {
        assertThat(Case($(ignored -> false), ignored -> 1).isDefinedAt(null)).isFalse();
    }

    @Test
    public void shouldReturnSomeWhenApplyingCaseGivenPredicateAndValue() {
        final Match.Case<Object, Integer> _case = Case($(ignored -> true), 1);
        assertThat(_case.isDefinedAt(null)).isTrue();
        assertThat(_case.apply(null)).isEqualTo(1);
    }

    @Test
    public void shouldReturnNoneWhenApplyingCaseGivenPredicateAndValue() {
        assertThat(Case($(ignored -> false), 1).isDefinedAt(null)).isFalse();
    }

    @Test
    public void shouldPassIssue2401() {
        final Seq<String> empty = Stream.empty();
        try {
            Match(empty).of(
                    Case($(List.empty()), ignored -> "list")
            );
            fail("expected MatchError");
        } catch (MatchError err) {
            // ok!
        }
    }

    @Test
    public void shouldCatchClassCastExceptionWhenPredicateHasDifferentType() {
        try {
            final Object o = "";
            Match(o).of(
                    Case($((Integer i) -> true), "never")
            );
            fail("expected MatchError");
        } catch (MatchError err) {
            // ok!
        }
    }

    // -- Match patterns

    static class ClzMatch {}
    static class ClzMatch1 extends ClzMatch {}
    static class ClzMatch2 extends ClzMatch {}

    @Test
    public void shouldMatchPattern1() {
        final Tuple1<Integer> tuple = Tuple.of(1);
        final String func = Match(tuple).of(
                Case($Tuple1($(0)), (m1) -> "fail"),
                Case($Tuple1($()), (m1) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple1($(0)), () -> "fail"),
                Case($Tuple1($()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple1($(0)), "fail"),
                Case($Tuple1($()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern1.of(ClzMatch1.class, $(), t -> Tuple.of(null)), "fail"),
                Case(Match.Pattern1.of(ClzMatch2.class, $(), t -> Tuple.of(null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern2() {
        final Tuple2<Integer, Integer> tuple = Tuple.of(1, 1);
        final String func = Match(tuple).of(
                Case($Tuple2($(0), $()), (m1, m2) -> "fail"),
                Case($Tuple2($(), $()), (m1, m2) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple2($(0), $()), () -> "fail"),
                Case($Tuple2($(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple2($(0), $()), "fail"),
                Case($Tuple2($(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern2.of(ClzMatch1.class, $(), $(), t -> Tuple.of(null, null)), "fail"),
                Case(Match.Pattern2.of(ClzMatch2.class, $(), $(), t -> Tuple.of(null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern3() {
        final Tuple3<Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple3($(0), $(), $()), (m1, m2, m3) -> "fail"),
                Case($Tuple3($(), $(), $()), (m1, m2, m3) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple3($(0), $(), $()), () -> "fail"),
                Case($Tuple3($(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple3($(0), $(), $()), "fail"),
                Case($Tuple3($(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern3.of(ClzMatch1.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "fail"),
                Case(Match.Pattern3.of(ClzMatch2.class, $(), $(), $(), t -> Tuple.of(null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern4() {
        final Tuple4<Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple4($(0), $(), $(), $()), (m1, m2, m3, m4) -> "fail"),
                Case($Tuple4($(), $(), $(), $()), (m1, m2, m3, m4) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple4($(0), $(), $(), $()), () -> "fail"),
                Case($Tuple4($(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple4($(0), $(), $(), $()), "fail"),
                Case($Tuple4($(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern4.of(ClzMatch1.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "fail"),
                Case(Match.Pattern4.of(ClzMatch2.class, $(), $(), $(), $(), t -> Tuple.of(null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern5() {
        final Tuple5<Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple5($(0), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "fail"),
                Case($Tuple5($(), $(), $(), $(), $()), (m1, m2, m3, m4, m5) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple5($(0), $(), $(), $(), $()), () -> "fail"),
                Case($Tuple5($(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple5($(0), $(), $(), $(), $()), "fail"),
                Case($Tuple5($(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern5.of(ClzMatch1.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "fail"),
                Case(Match.Pattern5.of(ClzMatch2.class, $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern6() {
        final Tuple6<Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple6($(0), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "fail"),
                Case($Tuple6($(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple6($(0), $(), $(), $(), $(), $()), () -> "fail"),
                Case($Tuple6($(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple6($(0), $(), $(), $(), $(), $()), "fail"),
                Case($Tuple6($(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern6.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "fail"),
                Case(Match.Pattern6.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern7() {
        final Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple7($(0), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "fail"),
                Case($Tuple7($(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple7($(0), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case($Tuple7($(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple7($(0), $(), $(), $(), $(), $(), $()), "fail"),
                Case($Tuple7($(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern7.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "fail"),
                Case(Match.Pattern7.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }

    @Test
    public void shouldMatchPattern8() {
        final Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer> tuple = Tuple.of(1, 1, 1, 1, 1, 1, 1, 1);
        final String func = Match(tuple).of(
                Case($Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "fail"),
                Case($Tuple8($(), $(), $(), $(), $(), $(), $(), $()), (m1, m2, m3, m4, m5, m6, m7, m8) -> "okFunc")
        );
        assertThat(func).isEqualTo("okFunc");
        final String supp = Match(tuple).of(
                Case($Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), () -> "fail"),
                Case($Tuple8($(), $(), $(), $(), $(), $(), $(), $()), () -> "okSupp")
        );
        assertThat(supp).isEqualTo("okSupp");
        final String val = Match(tuple).of(
                Case($Tuple8($(0), $(), $(), $(), $(), $(), $(), $()), "fail"),
                Case($Tuple8($(), $(), $(), $(), $(), $(), $(), $()), "okVal")
        );
        assertThat(val).isEqualTo("okVal");

        final ClzMatch c = new ClzMatch2();
        final String match = Match(c).of(
                Case(Match.Pattern8.of(ClzMatch1.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "fail"),
                Case(Match.Pattern8.of(ClzMatch2.class, $(), $(), $(), $(), $(), $(), $(), $(), t -> Tuple.of(null, null, null, null, null, null, null, null)), "okMatch")
        );
        assertThat(match).isEqualTo("okMatch");
    }
}