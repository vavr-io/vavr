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
package io.vavr.collection;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static io.vavr.collection.Comparators.naturalComparator;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class TreeMapOfEntriesTest {
  @Test
  public void shouldConstructFrom1EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
    assertThat(map.get(9).get()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10EntriesWithKeyComparator() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(naturalComparator(), 1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
    assertThat(map.get(9).get()).isEqualTo("9");
    assertThat(map.get(10).get()).isEqualTo("10");
  }

  @Test
  public void shouldConstructFrom1Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5");
    assertThat(map.size()).isEqualTo(5);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
  }

  @Test
  public void shouldConstructFrom6Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6");
    assertThat(map.size()).isEqualTo(6);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
  }

  @Test
  public void shouldConstructFrom7Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7");
    assertThat(map.size()).isEqualTo(7);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
  }

  @Test
  public void shouldConstructFrom8Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8");
    assertThat(map.size()).isEqualTo(8);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
  }

  @Test
  public void shouldConstructFrom9Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9");
    assertThat(map.size()).isEqualTo(9);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
    assertThat(map.get(9).get()).isEqualTo("9");
  }

  @Test
  public void shouldConstructFrom10Entries() {
    final TreeMap<Integer, String> map =
      TreeMap
      .of(1, "1", 2, "2", 3, "3", 4, "4", 5, "5", 6, "6", 7, "7", 8, "8", 9, "9", 10, "10");
    assertThat(map.size()).isEqualTo(10);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
    assertThat(map.get(5).get()).isEqualTo("5");
    assertThat(map.get(6).get()).isEqualTo("6");
    assertThat(map.get(7).get()).isEqualTo("7");
    assertThat(map.get(8).get()).isEqualTo("8");
    assertThat(map.get(9).get()).isEqualTo("9");
    assertThat(map.get(10).get()).isEqualTo("10");
  }
}