/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
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

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static io.vavr.collection.Comparators.naturalComparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TreeMultimapOfEntriesTest {
  @Test
  public void shouldConstructFrom1EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Seq).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithKeyComparatorWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Seq).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof Seq).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Seq).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithSeq() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSeq()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Seq).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Seq).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Seq).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Seq).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Seq).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Seq).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Seq).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof Seq).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Set).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithKeyComparatorWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Set).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof Set).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Set).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof Set).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof Set).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof Set).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof Set).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof Set).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof Set).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof Set).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof Set).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithBuilderComparatorWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof SortedSet).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithBuilderComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof SortedSet).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithKeyComparatorWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof SortedSet).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithSortedSet() {
    final TreeMultimap<Integer, String> map =
      TreeMultimap.withSortedSet()
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
    assertThat(map.get(3).get() instanceof SortedSet).isTrue();
    assertThat(map.get(3).get().head()).isEqualTo("3");
    assertThat(map.get(4).get() instanceof SortedSet).isTrue();
    assertThat(map.get(4).get().head()).isEqualTo("4");
    assertThat(map.get(5).get() instanceof SortedSet).isTrue();
    assertThat(map.get(5).get().head()).isEqualTo("5");
    assertThat(map.get(6).get() instanceof SortedSet).isTrue();
    assertThat(map.get(6).get().head()).isEqualTo("6");
    assertThat(map.get(7).get() instanceof SortedSet).isTrue();
    assertThat(map.get(7).get().head()).isEqualTo("7");
    assertThat(map.get(8).get() instanceof SortedSet).isTrue();
    assertThat(map.get(8).get().head()).isEqualTo("8");
    assertThat(map.get(9).get() instanceof SortedSet).isTrue();
    assertThat(map.get(9).get().head()).isEqualTo("9");
    assertThat(map.get(10).get() instanceof SortedSet).isTrue();
    assertThat(map.get(10).get().head()).isEqualTo("10");
  }
}