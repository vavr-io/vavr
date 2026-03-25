/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2014-2026 Vavr, https://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.control;

import io.vavr.AbstractValueTest;
import io.vavr.Serializables;
import io.vavr.Value;
import io.vavr.collection.Seq;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ResultTest extends AbstractValueTest {

    @Override
    protected <T> Value<T> empty() {
        return Result.error("error");
    }

    @Override
    protected <T> Value<T> of(T element) {
        return Result.value(element);
    }

    @SafeVarargs
    @Override
    protected final <T> Value<T> of(T... elements) {
        return Result.value(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    @Nested
    class ValueTests {

        @Test
        public void shouldCreateValue() {
            assertThat(Result.value(42).get()).isEqualTo(42);
        }

        @Test
        public void shouldCreateValueWithNull() {
            assertThat(Result.value(null).get()).isEqualTo(null);
        }
    }

    @Nested
    class ErrorTests {

        @Test
        public void shouldCreateError() {
            assertThat(Result.error("err").getError()).isEqualTo("err");
        }

        @Test
        public void shouldThrowOnNullError() {
            assertThrows(NullPointerException.class, () -> Result.error(null));
        }
    }

    @Nested
    class NarrowTests {

        @Test
        public void shouldNarrowValue() {
            Result<Integer, String> result = Result.value(42);
            Result<Number, String> narrowed = Result.narrow(result);
            assertThat(narrowed.get()).isEqualTo(42);
        }

        @Test
        public void shouldNarrowError() {
            Result<Integer, String> result = Result.error("err");
            Result<Integer, CharSequence> narrowed = Result.narrow(result);
            assertThat(narrowed.getError()).isEqualTo("err");
        }
    }

    @Nested
    class SequenceTests {

        @Test
        public void shouldSequenceAllValues() {
            List<Result<Integer, String>> results = Arrays.asList(
              Result.value(1), Result.value(2), Result.value(3));
            Result<Seq<Integer>, String> seq = Result.sequence(results);
            assertThat(seq.isValue()).isTrue();
            assertThat(seq.get().size()).isEqualTo(3);
            assertThat(seq.get().mkString(",")).isEqualTo("1,2,3");
        }

        @Test
        public void shouldSequenceShortCircuitOnFirstError() {
            List<Result<Integer, String>> results = Arrays.asList(
              Result.value(1), Result.error("bad"), Result.value(3));
            Result<Seq<Integer>, String> seq = Result.sequence(results);
            assertThat(seq.isError()).isTrue();
            assertThat(seq.getError()).isEqualTo("bad");
        }

        @Test
        public void shouldSequenceAllErrors() {
            List<Result<Integer, String>> results = Arrays.asList(
              Result.error("first"), Result.error("second"));
            Result<Seq<Integer>, String> seq = Result.sequence(results);
            assertThat(seq.isError()).isTrue();
            assertThat(seq.getError()).isEqualTo("first");
        }

        @Test
        public void shouldSequenceEmptyIterable() {
            Result<Seq<Integer>, String> seq = Result.sequence(List.of());
            assertThat(seq.isValue()).isTrue();
            assertThat(seq.get().isEmpty()).isTrue();
        }

        @Test
        public void shouldThrowOnNullSequence() {
            assertThrows(NullPointerException.class, () -> Result.sequence(null));
        }
    }

    @Nested
    class TraverseTests {

        @Test
        public void shouldTraverseAllToValues() {
            List<String> items = Arrays.asList("1", "2", "3");
            Result<Seq<Integer>, String> result = Result.traverse(items, s -> Result.value(Integer.parseInt(s)));
            assertThat(result.isValue()).isTrue();
            assertThat(result.get().mkString(",")).isEqualTo("1,2,3");
        }

        @Test
        public void shouldTraverseShortCircuitOnFirstError() {
            List<String> items = Arrays.asList("1", "bad", "3");
            Result<Seq<Integer>, String> result = Result.traverse(items,
              s -> s.equals("bad") ? Result.error("invalid: " + s) : Result.value(Integer.parseInt(s)));
            assertThat(result.isError()).isTrue();
            assertThat(result.getError()).isEqualTo("invalid: bad");
        }

        @Test
        public void shouldThrowOnNullTraverseItems() {
            assertThrows(NullPointerException.class, () -> Result.traverse(null, Result::value));
        }

        @Test
        public void shouldThrowOnNullTraverseMapper() {
            assertThrows(NullPointerException.class, () -> Result.traverse(List.of(), null));
        }
    }

    @Nested
    class IsValueTests {

        @Test
        public void shouldBeValueOnValue() {
            assertThat(Result.value(1).isValue()).isTrue();
        }

        @Test
        public void shouldNotBeValueOnError() {
            assertThat(Result.error("err").isValue()).isFalse();
        }
    }

    @Nested
    class IsErrorTests {

        @Test
        public void shouldBeErrorOnError() {
            assertThat(Result.error("err").isError()).isTrue();
        }

        @Test
        public void shouldNotBeErrorOnValue() {
            assertThat(Result.value(1).isError()).isFalse();
        }
    }

    @Nested
    class IsEmptyTests {

        @Test
        public void shouldBeEmptyOnError() {
            assertThat(Result.error("err").isEmpty()).isTrue();
        }

        @Test
        public void shouldNotBeEmptyOnValue() {
            assertThat(Result.value(1).isEmpty()).isFalse();
        }
    }

    @Nested
    class GetTests {

        @Test
        public void shouldGetOnValue() {
            assertThat(Result.value(42).get()).isEqualTo(42);
        }

        @Test
        public void shouldThrowOnGetOfError() {
            assertThrows(NoSuchElementException.class, () -> Result.error("err").get());
        }
    }

    @Nested
    class GetErrorTests {

        @Test
        public void shouldGetErrorOnError() {
            assertThat(Result.error("err").getError()).isEqualTo("err");
        }

        @Test
        public void shouldThrowOnGetErrorOfValue() {
            assertThrows(NoSuchElementException.class, () -> Result.value(1).getError());
        }
    }

    @Nested
    class MapTests {

        @Test
        public void shouldMapValue() {
            assertThat(Result.value(1).map(String::valueOf)).isEqualTo(Result.value("1"));
        }

        @Test
        public void shouldNotMapError() {
            Result<String, String> result = Result.<Integer, String>error("err").map(String::valueOf);
            assertThat(result).isEqualTo(Result.error("err"));
        }

        @Test
        public void shouldThrowOnNullMapFunction() {
            assertThrows(NullPointerException.class, () -> Result.value(1).map(null));
        }

        @Test
        public void shouldThrowOnNullMapFunctionForError() {
            assertThrows(NullPointerException.class, () -> Result.error("err").map(null));
        }
    }

    @Nested
    class FlatMapTests {

        @Test
        public void shouldFlatMapValue() {
            assertThat(Result.value(1).flatMap(i -> Result.value(String.valueOf(i)))).isEqualTo(Result.value("1"));
        }

        @Test
        public void shouldFlatMapValueToError() {
            assertThat(Result.value(1).flatMap(i -> Result.error("bad"))).isEqualTo(Result.error("bad"));
        }

        @Test
        public void shouldNotFlatMapError() {
            Result<String, String> result = Result.<Integer, String>error("err")
              .flatMap(i -> Result.value(String.valueOf(i)));
            assertThat(result).isEqualTo(Result.error("err"));
        }

        @Test
        public void shouldThrowOnNullFlatMapFunction() {
            assertThrows(NullPointerException.class, () -> Result.value(1).flatMap(null));
        }

        @Test
        public void shouldThrowOnNullFlatMapFunctionForError() {
            assertThrows(NullPointerException.class, () -> Result.error("err").flatMap(null));
        }
    }

    @Nested
    class MapErrorTests {

        @Test
        public void shouldMapError() {
            assertThat(Result.error("err").mapError(String::length)).isEqualTo(Result.error(3));
        }

        @Test
        public void shouldNotMapErrorOnValue() {
            Result<Integer, Integer> result = Result.<Integer, String>value(1).mapError(String::length);
            assertThat(result).isEqualTo(Result.value(1));
        }

        @Test
        public void shouldThrowOnNullMapErrorFunction() {
            assertThrows(NullPointerException.class, () -> Result.error("err").mapError(null));
        }

        @Test
        public void shouldThrowOnNullMapErrorFunctionForValue() {
            assertThrows(NullPointerException.class, () -> Result.value(1).mapError(null));
        }
    }

    @Nested
    class BimapTests {

        @Test
        public void shouldBimapValue() {
            Result<String, Integer> result = Result.<Integer, String>value(1)
              .bimap(String::length, String::valueOf);
            assertThat(result).isEqualTo(Result.value("1"));
        }

        @Test
        public void shouldBimapError() {
            Result<String, Integer> result = Result.<Integer, String>error("err")
              .bimap(String::length, String::valueOf);
            assertThat(result).isEqualTo(Result.error(3));
        }

        @Test
        public void shouldThrowOnNullBimapErrorMapper() {
            assertThrows(NullPointerException.class, () -> Result.value(1).bimap(null, x -> x));
        }

        @Test
        public void shouldThrowOnNullBimapValueMapper() {
            assertThrows(NullPointerException.class, () -> Result.value(1).bimap(x -> x, null));
        }
    }

    @Nested
    class FoldTests {

        @Test
        public void shouldFoldValue() {
            String result = Result.<Integer, String>value(1).fold(e -> "error:" + e, v -> "value:" + v);
            assertThat(result).isEqualTo("value:1");
        }

        @Test
        public void shouldFoldError() {
            String result = Result.<Integer, String>error("err").fold(e -> "error:" + e, v -> "value:" + v);
            assertThat(result).isEqualTo("error:err");
        }

        @Test
        public void shouldThrowOnNullFoldErrorMapper() {
            assertThrows(NullPointerException.class, () -> Result.value(1).fold(null, v -> v));
        }

        @Test
        public void shouldThrowOnNullFoldValueMapper() {
            assertThrows(NullPointerException.class, () -> Result.value(1).fold(e -> e, null));
        }
    }

    @Nested
    class GetOrElseTests {

        @Test
        public void shouldGetValueOnGetOrElseWhenValue() {
            assertThat(Result.value(1).getOrElse(0)).isEqualTo(1);
        }

        @Test
        public void shouldGetAlternativeOnGetOrElseWhenError() {
            assertThat(Result.<Integer, String>error("err").getOrElse(0)).isEqualTo(0);
        }

        @Test
        public void shouldGetValueOnGetOrElseSupplierWhenValue() {
            assertThat(Result.value(1).getOrElse(() -> 0)).isEqualTo(1);
        }

        @Test
        public void shouldGetAlternativeOnGetOrElseSupplierWhenError() {
            assertThat(Result.<Integer, String>error("err").getOrElse(() -> 0)).isEqualTo(0);
        }

        @Test
        public void shouldThrowOnNullGetOrElseSupplier() {
            assertThrows(NullPointerException.class, () -> Result.value(1)
              .getOrElse((java.util.function.Supplier<Integer>) null));
        }
    }

    @Nested
    class GetOrElseThrowTests {

        @Test
        public void shouldGetValueOnGetOrElseThrowWhenValue() throws Exception {
            assertThat(Result.<Integer, String>value(1).getOrElseThrow(e -> new RuntimeException(e))).isEqualTo(1);
        }

        @Test
        public void shouldThrowOnGetOrElseThrowWhenError() {
            assertThrows(RuntimeException.class, () ->
              Result.<Integer, String>error("err").getOrElseThrow((String e) -> new RuntimeException(e)));
        }

        @Test
        public void shouldThrowOnNullGetOrElseThrowMapper() {
            assertThrows(NullPointerException.class, () ->
              Result.<Integer, String>value(1)
                .getOrElseThrow((java.util.function.Function<String, RuntimeException>) null));
        }
    }

    @Nested
    class OrElseTests {

        @Test
        public void shouldReturnSelfOnOrElseWhenValue() {
            Result<Integer, String> val = Result.value(1);
            assertThat(val.orElse(() -> Result.value(0))).isSameAs(val);
        }

        @Test
        public void shouldReturnAlternativeOnOrElseWhenError() {
            Result<Integer, String> alternative = Result.value(0);
            assertThat(Result.<Integer, String>error("err").orElse(() -> alternative)).isSameAs(alternative);
        }

        @Test
        public void shouldThrowOnNullOrElseSupplier() {
            assertThrows(NullPointerException.class, () -> Result.value(1).orElse(null));
        }
    }

    @Nested
    class RecoverTests {

        @Test
        public void shouldRecoverFromError() {
            assertThat(Result.<Integer, String>error("err").recover(e -> -1)).isEqualTo(Result.value(-1));
        }

        @Test
        public void shouldNotRecoverFromValue() {
            Result<Integer, String> val = Result.value(1);
            assertThat(val.recover(e -> -1)).isSameAs(val);
        }

        @Test
        public void shouldThrowOnNullRecoverFunction() {
            assertThrows(NullPointerException.class, () -> Result.error("err").recover(null));
        }
    }

    @Nested
    class RecoverWithTests {

        @Test
        public void shouldRecoverWithFromError() {
            assertThat(Result.<Integer, String>error("err").recoverWith(e -> Result.value(-1)))
              .isEqualTo(Result.value(-1));
        }

        @Test
        public void shouldRecoverWithChainedError() {
            assertThat(Result.<Integer, String>error("err").recoverWith(e -> Result.error("new:" + e)))
              .isEqualTo(Result.error("new:err"));
        }

        @Test
        public void shouldNotRecoverWithFromValue() {
            Result<Integer, String> val = Result.value(1);
            assertThat(val.recoverWith(e -> Result.value(-1))).isSameAs(val);
        }

        @Test
        public void shouldThrowOnNullRecoverWithFunction() {
            assertThrows(NullPointerException.class, () -> Result.error("err").recoverWith(null));
        }
    }

    @Nested
    class PeekTests {

        @Test
        public void shouldPeekOnValue() {
            final int[] actual = {-1};
            Result<Integer, String> result = Result.<Integer, String>value(1).peek(i -> actual[0] = i);
            assertThat(actual[0]).isEqualTo(1);
            assertThat(result).isEqualTo(Result.value(1));
        }

        @Test
        public void shouldNotPeekOnError() {
            final int[] actual = {-1};
            Result<Integer, String> result = Result.<Integer, String>error("err").peek(i -> actual[0] = i);
            assertThat(actual[0]).isEqualTo(-1);
            assertThat(result).isEqualTo(Result.error("err"));
        }

        @Test
        public void shouldThrowOnNullPeekAction() {
            assertThrows(NullPointerException.class, () -> Result.value(1).peek(null));
        }

        @Test
        public void shouldThrowOnNullPeekActionForError() {
            assertThrows(NullPointerException.class, () -> Result.error("err").peek(null));
        }
    }

    @Nested
    class PeekErrorTests {

        @Test
        public void shouldPeekErrorOnError() {
            final String[] actual = {null};
            Result<Integer, String> result = Result.<Integer, String>error("err").peekError(e -> actual[0] = e);
            assertThat(actual[0]).isEqualTo("err");
            assertThat(result).isEqualTo(Result.error("err"));
        }

        @Test
        public void shouldNotPeekErrorOnValue() {
            final AtomicBoolean called = new AtomicBoolean(false);
            Result<Integer, String> result = Result.<Integer, String>value(1).peekError(e -> called.set(true));
            assertThat(called.get()).isFalse();
            assertThat(result).isEqualTo(Result.value(1));
        }

        @Test
        public void shouldThrowOnNullPeekErrorAction() {
            assertThrows(NullPointerException.class, () -> Result.error("err").peekError(null));
        }
    }

    @Nested
    class FilterTests {

        @Test
        public void shouldReturnValueWhenFilterMatchesOnValue() {
            assertThat(Result.value(1).filter(i -> i == 1, () -> "bad")).isEqualTo(Result.value(1));
        }

        @Test
        public void shouldReturnErrorWhenFilterDoesNotMatchOnValue() {
            assertThat(Result.value(1).filter(i -> i == 2, () -> "bad")).isEqualTo(Result.error("bad"));
        }

        @Test
        public void shouldReturnErrorWhenFilterCalledOnError() {
            assertThat(Result.<Integer, String>error("original").filter(i -> i == 1, () -> "bad"))
              .isEqualTo(Result.error("original"));
        }

        @Test
        public void shouldThrowOnNullFilterPredicate() {
            assertThrows(NullPointerException.class, () -> Result.value(1).filter(null, () -> "bad"));
        }

        @Test
        public void shouldThrowOnNullFilterErrorSupplier() {
            assertThrows(NullPointerException.class, () -> Result.value(1).filter(i -> true, null));
        }
    }

    @Nested
    class SwapTests {

        @Test
        public void shouldSwapValue() {
            assertThat(Result.value(1).swap()).isEqualTo(Result.error(1));
        }

        @Test
        public void shouldSwapError() {
            assertThat(Result.<Integer, String>error("err").swap()).isEqualTo(Result.value("err"));
        }
    }

    @Nested
    class TransformTests {

        @Test
        public void shouldTransformValue() {
            String result = Result.value(1).transform(r -> r.isValue() ? "value:" + r.get() : "error");
            assertThat(result).isEqualTo("value:1");
        }

        @Test
        public void shouldTransformError() {
            String result = Result.<Integer, String>error("err")
              .transform(r -> r.isError() ? "error:" + r.getError() : "value");
            assertThat(result).isEqualTo("error:err");
        }

        @Test
        public void shouldThrowOnNullTransformFunction() {
            assertThrows(NullPointerException.class, () -> Result.value(1).transform(null));
        }
    }

    @Nested
    class ToEitherTests {

        @Test
        public void shouldConvertValueToEitherRight() {
            assertThat(Result.value(1).toEither()).isEqualTo(Either.right(1));
        }

        @Test
        public void shouldConvertErrorToEitherLeft() {
            assertThat(Result.<Integer, String>error("err").toEither()).isEqualTo(Either.left("err"));
        }
    }

    @Nested
    class IteratorTests {

        @Test
        public void shouldReturnNonEmptyIteratorForValue() {
            assertThat(Result.value(1).iterator().hasNext()).isTrue();
        }

        @Test
        public void shouldReturnEmptyIteratorForError() {
            assertThat(Result.error("err").iterator().hasNext()).isFalse();
        }
    }

    @Nested
    class EqualsTests {

        @Test
        public void shouldEqualValueIfSameInstance() {
            Result<Integer, String> val = Result.value(1);
            assertThat(val).isEqualTo(val);
        }

        @Test
        public void shouldEqualErrorIfSameInstance() {
            Result<Integer, String> err = Result.error("err");
            assertThat(err).isEqualTo(err);
        }

        @Test
        public void shouldEqualValuesWithSameContent() {
            assertThat(Result.value(1)).isEqualTo(Result.value(1));
        }

        @Test
        public void shouldNotEqualValuesWithDifferentContent() {
            assertThat(Result.value(1)).isNotEqualTo(Result.value(2));
        }

        @Test
        public void shouldEqualErrorsWithSameContent() {
            assertThat(Result.error("err")).isEqualTo(Result.error("err"));
        }

        @Test
        public void shouldNotEqualErrorsWithDifferentContent() {
            assertThat(Result.error("err1")).isNotEqualTo(Result.error("err2"));
        }

        @Test
        public void shouldNotEqualValueAndError() {
            assertThat(Result.value(1)).isNotEqualTo(Result.error(1));
        }

        @Test
        public void shouldNotEqualValueIfObjectIsNull() {
            assertThat(Result.value(1)).isNotNull();
        }

        @Test
        public void shouldNotEqualErrorIfObjectIsNull() {
            assertThat(Result.error("err")).isNotNull();
        }

        @Test
        public void shouldNotEqualValueIfObjectIsOfDifferentType() {
            assertThat(Result.value(1).equals(new Object())).isFalse();
        }

        @Test
        public void shouldNotEqualErrorIfObjectIsOfDifferentType() {
            assertThat(Result.error("err").equals(new Object())).isFalse();
        }
    }

    @Nested
    class HashCodeTests {

        @Test
        public void shouldHashValue() {
            assertThat(Result.value(1).hashCode()).isEqualTo(Objects.hashCode(1));
        }

        @Test
        public void shouldHashError() {
            assertThat(Result.error("err").hashCode()).isEqualTo(Objects.hashCode("err"));
        }

        @Test
        public void shouldHaveSameHashCodeForEqualValues() {
            assertThat(Result.value(1).hashCode()).isEqualTo(Result.value(1).hashCode());
        }

        @Test
        public void shouldHaveSameHashCodeForEqualErrors() {
            assertThat(Result.error("err").hashCode()).isEqualTo(Result.error("err").hashCode());
        }
    }

    @Nested
    class ToStringTests {

        @Test
        public void shouldConvertValueToString() {
            assertThat(Result.value(1).toString()).isEqualTo("Value(1)");
        }

        @Test
        public void shouldConvertErrorToString() {
            assertThat(Result.error("err").toString()).isEqualTo("Error(err)");
        }

        @Test
        public void shouldConvertValueNullToString() {
            assertThat(Result.value(null).toString()).isEqualTo("Value(null)");
        }
    }

    @Nested
    class SerializationTests {

        @Test
        public void shouldSerializeValue() {
            final Result<Integer, String> value = Result.value(1);
            final Result<?, ?> deserialized = Serializables.deserialize(Serializables.serialize(value));
            assertThat(deserialized).isEqualTo(value);
        }

        @Test
        public void shouldSerializeError() {
            final Result<Integer, String> error = Result.error("err");
            final Result<?, ?> deserialized = Serializables.deserialize(Serializables.serialize(error));
            assertThat(deserialized).isEqualTo(error);
        }
    }

    @Nested
    class ChainingTests {

        @Test
        public void shouldChainMapAndFlatMap() {
            Result<String, String> result = Result.<Integer, String>value(1)
              .map(i -> i * 2)
              .flatMap(i -> Result.value(String.valueOf(i)));
            assertThat(result).isEqualTo(Result.value("2"));
        }

        @Test
        public void shouldPropagateErrorThroughChain() {
            AtomicInteger mapCount = new AtomicInteger(0);
            Result<String, String> result = Result.<Integer, String>error("fail")
              .map(i -> {
                  mapCount.incrementAndGet();
                  return i * 2;
              })
              .flatMap(i -> {
                  mapCount.incrementAndGet();
                  return Result.value(String.valueOf(i));
              });
            assertThat(result).isEqualTo(Result.error("fail"));
            assertThat(mapCount.get()).isEqualTo(0);
        }
    }
}
