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

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.Test;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

public class PartialFunctionTest {

    @Test
    public void shouldReturnSome() {
        Option<String> oneToOne = HashMap.of(1, "One").lift().apply(1);
        assertThat(oneToOne).isEqualTo(Option.some("One"));
    }

    @Test
    public void shouldReturnNone() {
        Option<String> oneToOne = HashMap.<Integer, String>empty().lift().apply(1);
        assertThat(oneToOne).isEqualTo(Option.none());
    }

    @Test
    public void shouldUnliftTotalFunctionReturningAnOption() {
        final Predicate<Number> isEven = n -> n.intValue() % 2 == 0;
        final Function1<Number, Option<String>> totalFunction = n -> isEven.test(n) ? Option.some("even") : Option.none();

        final PartialFunction<Integer, CharSequence> partialFunction = PartialFunction.unlift(totalFunction);

        assertThat(partialFunction.isDefinedAt(1)).isFalse();
        assertThat(partialFunction.isDefinedAt(2)).isTrue();
        assertThat(partialFunction.apply(2)).isEqualTo("even");
    }

    @Test
    public void shouldNotBeDefinedAtLeft() {
        final Either<RuntimeException, Object> left = Either.left(new RuntimeException());

        assertThat(PartialFunction.getIfDefined().isDefinedAt(left)).isFalse();
    }

    @Test
    public void shouldBeDefinedAtRight() {
        Either<Object, Number> right = Either.right(42);

        PartialFunction<Either<Object, Number>, Number> ifDefined = PartialFunction.getIfDefined();

        assertThat(ifDefined.isDefinedAt(right)).isTrue();
        assertThat(ifDefined.apply(right)).isEqualTo(42);
    }

    @Test
    public void shouldCollectSomeValuesAndIgnoreNone() {
        final List<Integer> evenNumbers = List.range(0, 10)
          .map(n -> n % 2 == 0 ? Option.some(n) : Option.<Integer>none())
          .collect(PartialFunction.getIfDefined());

        assertThat(evenNumbers).containsExactly(0, 2, 4, 6, 8);
    }

}
