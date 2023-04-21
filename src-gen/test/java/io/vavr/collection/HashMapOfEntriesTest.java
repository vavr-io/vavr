/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2023 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class HashMapOfEntriesTest {
  @Test
  public void shouldConstructFrom1Entries() {
    final HashMap<Integer, String> map =
      HashMap
      .of(1, "1");
    assertThat(map.size()).isEqualTo(1);
    assertThat(map.get(1).get()).isEqualTo("1");
  }

  @Test
  public void shouldConstructFrom2Entries() {
    final HashMap<Integer, String> map =
      HashMap
      .of(1, "1", 2, "2");
    assertThat(map.size()).isEqualTo(2);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
  }

  @Test
  public void shouldConstructFrom3Entries() {
    final HashMap<Integer, String> map =
      HashMap
      .of(1, "1", 2, "2", 3, "3");
    assertThat(map.size()).isEqualTo(3);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
  }

  @Test
  public void shouldConstructFrom4Entries() {
    final HashMap<Integer, String> map =
      HashMap
      .of(1, "1", 2, "2", 3, "3", 4, "4");
    assertThat(map.size()).isEqualTo(4);
    assertThat(map.get(1).get()).isEqualTo("1");
    assertThat(map.get(2).get()).isEqualTo("2");
    assertThat(map.get(3).get()).isEqualTo("3");
    assertThat(map.get(4).get()).isEqualTo("4");
  }

  @Test
  public void shouldConstructFrom5Entries() {
    final HashMap<Integer, String> map =
      HashMap
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
    final HashMap<Integer, String> map =
      HashMap
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
    final HashMap<Integer, String> map =
      HashMap
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
    final HashMap<Integer, String> map =
      HashMap
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
    final HashMap<Integer, String> map =
      HashMap
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
    final HashMap<Integer, String> map =
      HashMap
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