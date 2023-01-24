/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * Copyright 2023 Vavr, https://vavr.io
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

import org.junit.Test;

public class LinkedHashMultimapOfEntriesTest {
  @Test
  public void shouldConstructFrom1EntriesWithSeq() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSeq() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Seq).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Seq).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSeq() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSeq()
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
  public void shouldConstructFrom1EntriesWithSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof Set).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof Set).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSet()
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
  public void shouldConstructFrom1EntriesWithBuilderComparatorWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithBuilderComparatorWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithBuilderComparatorWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet(naturalComparator())
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
  public void shouldConstructFrom1EntriesWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get() instanceof SortedSet).isTrue();
    assertThat(map.get(1).get().head()).isEqualTo("1");
    assertThat(map.get(2).get() instanceof SortedSet).isTrue();
    assertThat(map.get(2).get().head()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithSortedSet() {
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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
    final LinkedHashMultimap<Integer, String> map =
      LinkedHashMultimap.withSortedSet()
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