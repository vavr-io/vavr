/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2025 Vavr, https://vavr.io
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

import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import static org.assertj.core.api.Assertions.assertThat;

// Just to reach 100%
public class StringTest {

    @Test
    public void charAt() {
        assertThat(CharSeq.of("123").charAt(0)).isEqualTo('1');
    }

    @Test
    public void codePointAt() {
        assertThat(CharSeq.of("123").codePointAt(0)).isEqualTo('1');
    }

    @Test
    public void codePointBefore() {
        assertThat(CharSeq.of("123").codePointBefore(1)).isEqualTo('1');
    }

    @Test
    public void codePointCount() {
        assertThat(CharSeq.of("123").codePointCount(0, 1)).isEqualTo(1);
    }

    @Test
    public void offsetByCodePoints() {
        assertThat(CharSeq.of("123").offsetByCodePoints(0, 0)).isEqualTo(0);
    }

    @Test
    public void getChars() {
        final char[] chars = new char[2];
        CharSeq.of("123").getChars(0, 2, chars, 0);
        assertThat(chars).isEqualTo(new char[] { '1', '2' });
    }

    @Test
    public void getBytesCharsetStr() throws UnsupportedEncodingException {
        assertThat(CharSeq.of("123").getBytes("UTF-8")).isEqualTo(new byte[] { 49, 50, 51 });
    }

    @Test
    public void getBytesCharset() {
        assertThat(CharSeq.of("123").getBytes(Charset.defaultCharset())).isEqualTo(new byte[] { 49, 50, 51 });
    }

    @Test
    public void getBytes() {
        assertThat(CharSeq.of("123").getBytes()).isEqualTo(new byte[] { 49, 50, 51 });
    }

    @Test
    public void contentEquals1() {
        assertThat(CharSeq.of("123").contentEquals("123")).isTrue();
    }

    @Test
    public void contentEquals2() {
        assertThat(CharSeq.of("123").contentEquals(new StringBuilder("123"))).isTrue();
    }

    @Test
    public void equalsIgnoreCase() {
        assertThat(CharSeq.of("Vavr").equalsIgnoreCase(CharSeq.of("vavr"))).isTrue();
    }

    @Test
    public void compareTo() {
        assertThat(CharSeq.of("123").compareTo(CharSeq.of("456"))).isNegative();
    }

    @Test
    public void compareToIgnoreCase() {
        assertThat(CharSeq.of("Vavr").compareToIgnoreCase(CharSeq.of("vavr"))).isEqualTo(0);
    }

    @Test
    public void regionMatches1() {
        assertThat(CharSeq.of("123").regionMatches(true, 0, CharSeq.of("12"), 0, 2)).isTrue();
    }

    @Test
    public void regionMatches2() {
        assertThat(CharSeq.of("123").regionMatches(0, CharSeq.of("12"), 0, 2)).isTrue();
    }

    @Test
    public void startsWithOffset() {
        assertThat(CharSeq.of("123").startsWith(CharSeq.of("1"), 0)).isTrue();
    }

    @Test
    public void startsWith() {
        assertThat(CharSeq.of("123").startsWith(CharSeq.of("1"))).isTrue();
    }

    @Test
    public void endsWith() {
        assertThat(CharSeq.of("123").endsWith(CharSeq.of("3"))).isTrue();
    }

    @Test
    public void indexOf() {
        assertThat(CharSeq.of("123").indexOf('2')).isEqualTo(1);

        assertThat(CharSeq.of("123").indexOfOption('2')).isEqualTo(Option.some(1));
    }

    @Test
    public void indexOfOffset() {
        assertThat(CharSeq.of("123").indexOf('2', 1)).isEqualTo(1);

        assertThat(CharSeq.of("123").indexOfOption('2', 1)).isEqualTo(Option.some(1));
    }

    @Test
    public void lastIndexOf() {
        assertThat(CharSeq.of("123").lastIndexOf('2')).isEqualTo(1);

        assertThat(CharSeq.of("123").lastIndexOfOption('2')).isEqualTo(Option.some(1));
    }

    @Test
    public void lastIndexOfOffset() {
        assertThat(CharSeq.of("123").lastIndexOf('2', 1)).isEqualTo(1);

        assertThat(CharSeq.of("123").lastIndexOfOption('2', 1)).isEqualTo(Option.some(1));
    }

    @Test
    public void indexOfSeq() {
        assertThat(CharSeq.of("123").indexOf(CharSeq.of("2"))).isEqualTo(1);

        assertThat(CharSeq.of("123").indexOfOption(CharSeq.of("2"))).isEqualTo(Option.some(1));
    }

    @Test
    public void indexOfSeqOffset() {
        assertThat(CharSeq.of("123").indexOf(CharSeq.of("2"), 1)).isEqualTo(1);

        assertThat(CharSeq.of("123").indexOfOption(CharSeq.of("2"), 1)).isEqualTo(Option.some(1));
    }

    @Test
    public void lastIndexOfSeq() {
        assertThat(CharSeq.of("123").lastIndexOf(CharSeq.of("2"))).isEqualTo(1);

        assertThat(CharSeq.of("123").lastIndexOfOption(CharSeq.of("2"))).isEqualTo(Option.some(1));
    }

    @Test
    public void lastIndexOfSeqOffset() {
        assertThat(CharSeq.of("123").lastIndexOf(CharSeq.of("2"), 1)).isEqualTo(1);

        assertThat(CharSeq.of("123").lastIndexOfOption(CharSeq.of("2"), 1)).isEqualTo(Option.some(1));
    }

    @Test
    public void substring() {
        assertThat((Iterable<Character>) CharSeq.of("123").substring(1)).isEqualTo(CharSeq.of("23"));
    }

    @Test
    public void substringBE() {
        assertThat((Iterable<Character>) CharSeq.of("123").substring(0, 2)).isEqualTo(CharSeq.of("12"));
    }

    @Test
    public void toStringTest() {
        assertThat(CharSeq.of("123").toString()).isEqualTo("123");
    }

    @Test
    public void concat() {
        assertThat((Iterable<Character>) CharSeq.of("123").concat(CharSeq.of("321"))).isEqualTo(CharSeq.of("123321"));
    }

    @Test
    public void matches() {
        assertThat(CharSeq.of("123").matches(".*2.*")).isTrue();
    }

    @Test
    public void contains() {
        assertThat(CharSeq.of("123").contains('1')).isTrue();
    }

    @Test
    public void replaceFirst() {
        assertThat((Iterable<Character>) CharSeq.of("123123").replaceFirst("1", "#")).isEqualTo(CharSeq.of("#23123"));
    }

    @Test
    public void replaceAll() {
        assertThat((Iterable<Character>) CharSeq.of("123123").replaceAll("1", "#")).isEqualTo(CharSeq.of("#23#23"));
    }

    @Test
    public void replace() {
        assertThat((Iterable<Character>) CharSeq.of("123").replace("1", "#")).isEqualTo(CharSeq.of("#23"));
    }

    @Test
    public void split() {
        assertThat(CharSeq.of("123").split("2")).isEqualTo(Array.of(CharSeq.of("1"), CharSeq.of("3")));
    }

    @Test
    public void splitLim() {
        assertThat(CharSeq.of("123").split("2", 2)).isEqualTo(Array.of(CharSeq.of("1"), CharSeq.of("3")));
    }

    @Test
    public void toLowerCase() {
        assertThat(CharSeq.of("Vavr").toLowerCase().toString()).isEqualTo("vavr");
    }

    @Test
    public void toUpperCase() {
        assertThat(CharSeq.of("Vavr").toUpperCase().toString()).isEqualTo("VAVR");
    }

    @Test
    public void trimTest() {
        assertThat(CharSeq.of("123").trim().toString()).isEqualTo("123");
    }

    @Test
    public void toCharArray() {
        assertThat(CharSeq.of("123").toCharArray()).isEqualTo(new char[] { 49, 50, 51 });
    }

}
