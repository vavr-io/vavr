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

import java.math.BigDecimal;
import java.util.Spliterator;
import org.junit.jupiter.api.Test;

public abstract class AbstractIndexedSeqTest extends AbstractSeqTest {

  @Override
  protected abstract <T> IndexedSeq<T> of(T element);

  // -- static narrow

  @Test
  public void shouldNarrowIndexedSeq() {
    final IndexedSeq<Double> doubles = of(1.0d);
    final IndexedSeq<Number> numbers = IndexedSeq.narrow(doubles);
    final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
    assertThat(actual).isEqualTo(3);
  }

  // -- spliterator

  @Test
  public void shouldHaveSizedSpliterator() {
    assertThat(
            of(1, 2, 3).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED))
        .isTrue();
  }

  @Test
  public void shouldReturnSizeWhenSpliterator() {
    assertThat(of(1, 2, 3).spliterator().getExactSizeIfKnown()).isEqualTo(3);
  }
}
