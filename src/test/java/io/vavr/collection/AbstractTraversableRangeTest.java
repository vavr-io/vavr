/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
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

import org.junit.Test;

public abstract class AbstractTraversableRangeTest extends AbstractTraversableTest {

    abstract protected Traversable<Character> range(char from, char toExclusive);

    abstract protected Traversable<Character> rangeBy(char from, char toExclusive, int step);

    abstract protected Traversable<Double> rangeBy(double from, double toExclusive, double step);

    abstract protected Traversable<Integer> range(int from, int toExclusive);

    abstract protected Traversable<Integer> rangeBy(int from, int toExclusive, int step);

    abstract protected Traversable<Long> range(long from, long toExclusive);

    abstract protected Traversable<Long> rangeBy(long from, long toExclusive, long step);

    abstract protected Traversable<Character> rangeClosed(char from, char toInclusive);

    abstract protected Traversable<Character> rangeClosedBy(char from, char toInclusive, int step);

    abstract protected Traversable<Double> rangeClosedBy(double from, double toInclusive, double step);

    abstract protected Traversable<Integer> rangeClosed(int from, int toInclusive);

    abstract protected Traversable<Integer> rangeClosedBy(int from, int toInclusive, int step);

    abstract protected Traversable<Long> rangeClosed(long from, long toInclusive);

    abstract protected Traversable<Long> rangeClosedBy(long from, long toInclusive, long step);

    // ------------------------------------------------------------------------
    // static range, rangeBy, rangeClosed, rangeCloseBy tests
    //
    // Basically there are the following tests scenarios:
    //
    // * step == 0
    // * from == to, step < 0
    // * from == to, step > 0
    // * from < to, step < 0
    // * from < to, step > 0
    // * from > to, step < 0
    // * from > to, step > 0
    //
    // Additionally we have to test these special cases
    // (where MIN/MAX may also be NEGATIVE_INFINITY, POSITIVE_INFINITY):
    //
    // * from = MAX, to = MAX, step < 0
    // * from = MAX, to = MAX, step > 0
    // * from = MIN, to = MIN, step < 0
    // * from = MIN, to = MIN, step > 0
    // * from = MAX - x, to = MAX, step > 0, 0 < x < step
    // * from = MAX - step, to = MAX, step > 0
    // * from = MAX - x, to = MAX, step > 0, x > step
    // * from = MIN, to = MIN - x, step < 0, 0 > x > step
    // * from = MIN, to = MIN - step, step < 0
    // * from = MIN, to = MIN - x, step < 0, x < step
    //
    // All of these scenarios are multiplied with
    //
    // * the inclusive and exclusive case
    // * the with and without step case
    //
    // ------------------------------------------------------------------------

    // -- static rangeClosed()

    @Test
    public void shouldCreateRangeClosedWhereFromIsGreaterThanTo() {
        assertThat(rangeClosed('b', 'a')).isEmpty();
        assertThat(rangeClosed(1, 0)).isEmpty();
        assertThat(rangeClosed(1L, 0L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedWhereFromEqualsTo() {
        assertThat(rangeClosed('a', 'a')).isEqualTo(of('a'));
        assertThat(rangeClosed(0, 0)).isEqualTo(of(0));
        assertThat(rangeClosed(0L, 0L)).isEqualTo(of(0L));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromIsLessThanTo() {
        assertThat(rangeClosed('a', 'c')).isEqualTo(of('a', 'b', 'c'));
        assertThat(rangeClosed(1, 3)).isEqualTo(of(1, 2, 3));
        assertThat(rangeClosed(1L, 3L)).isEqualTo(of(1L, 2L, 3L));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMIN_VALUE() {
        assertThat(rangeClosed(Character.MIN_VALUE, Character.MIN_VALUE)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosed(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosed(Long.MIN_VALUE, Long.MIN_VALUE)).isEqualTo(of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedWhereFromAndToEqualMAX_VALUE() {
        assertThat(rangeClosed(Character.MAX_VALUE, Character.MAX_VALUE)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosed(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosed(Long.MAX_VALUE, Long.MAX_VALUE)).isEqualTo(of(Long.MAX_VALUE));
    }

    // -- static rangeClosedBy()

    @Test
    public void shouldCreateRangeClosedByWhereFromIsGreaterThanToAndStepWrongDirection() {

        // char
        assertThat(rangeClosedBy('b', 'a', 1)).isEmpty();
        assertThat(rangeClosedBy('b', 'a', 3)).isEmpty();
        assertThat(rangeClosedBy('a', 'b', -1)).isEmpty();
        assertThat(rangeClosedBy('a', 'b', -3)).isEmpty();

        // double
        assertThat(rangeClosedBy(1.0, 0.0, 1.0)).isEmpty();
        assertThat(rangeClosedBy(1.0, 0.0, 3.0)).isEmpty();
        assertThat(rangeClosedBy(0.0, 1.0, -1.0)).isEmpty();
        assertThat(rangeClosedBy(0.0, 1.0, -3.0)).isEmpty();

        // int
        assertThat(rangeClosedBy(1, 0, 1)).isEmpty();
        assertThat(rangeClosedBy(1, 0, 3)).isEmpty();
        assertThat(rangeClosedBy(0, 1, -1)).isEmpty();
        assertThat(rangeClosedBy(0, 1, -3)).isEmpty();

        // long
        assertThat(rangeClosedBy(1L, 0L, 1L)).isEmpty();
        assertThat(rangeClosedBy(1L, 0L, 3L)).isEmpty();
        assertThat(rangeClosedBy(0L, 1L, -1L)).isEmpty();
        assertThat(rangeClosedBy(0L, 1L, -3L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromEqualsTo() {

        // char
        assertThat(rangeClosedBy('a', 'a', 1)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', 3)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', -1)).isEqualTo(of('a'));
        assertThat(rangeClosedBy('a', 'a', -3)).isEqualTo(of('a'));

        // double
        assertThat(rangeClosedBy(0.0, 0.0, 1.0)).isEqualTo(of(0.0));
        assertThat(rangeClosedBy(0.0, 0.0, 3.0)).isEqualTo(of(0.0));
        assertThat(rangeClosedBy(0.0, 0.0, -1.0)).isEqualTo(of(0.0));
        assertThat(rangeClosedBy(0.0, 0.0, -3.0)).isEqualTo(of(0.0));

        // int
        assertThat(rangeClosedBy(0, 0, 1)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, 3)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, -1)).isEqualTo(of(0));
        assertThat(rangeClosedBy(0, 0, -3)).isEqualTo(of(0));

        // long
        assertThat(rangeClosedBy(0L, 0L, 1L)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, 3L)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, -1L)).isEqualTo(of(0L));
        assertThat(rangeClosedBy(0L, 0L, -3L)).isEqualTo(of(0L));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromIsLessThanToAndStepCorrectDirection() {

        // char
        assertThat(rangeClosedBy('a', 'c', 1)).isEqualTo(of('a', 'b', 'c'));
        assertThat(rangeClosedBy('a', 'e', 2)).isEqualTo(of('a', 'c', 'e'));
        assertThat(rangeClosedBy('a', 'f', 2)).isEqualTo(of('a', 'c', 'e'));
        assertThat(rangeClosedBy((char) (Character.MAX_VALUE - 2), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 2)));
        assertThat(rangeClosedBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE));
        assertThat(rangeClosedBy('c', 'a', -1)).isEqualTo(of('c', 'b', 'a'));
        assertThat(rangeClosedBy('e', 'a', -2)).isEqualTo(of('e', 'c', 'a'));
        assertThat(rangeClosedBy('e', (char) ('a' - 1), -2)).isEqualTo(of('e', 'c', 'a'));
        assertThat(rangeClosedBy((char) (Character.MIN_VALUE + 2), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 2)));
        assertThat(rangeClosedBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE));

        // double
        assertThat(rangeClosedBy(1.0, 3.0, 1.0)).isEqualTo(of(1.0, 2.0, 3.0));
        assertThat(rangeClosedBy(1.0, 5.0, 2.0)).isEqualTo(of(1.0, 3.0, 5.0));
        assertThat(rangeClosedBy(1.0, 6.0, 2.0)).isEqualTo(of(1.0, 3.0, 5.0));
        assertThat(rangeClosedBy(Double.MAX_VALUE - 2.0E307, Double.MAX_VALUE, 3.0E307)).isEqualTo(of(Double.MAX_VALUE - 2.0E307));
        assertThat(rangeClosedBy(3.0, 1.0, -1.0)).isEqualTo(of(3.0, 2.0, 1.0));
        assertThat(rangeClosedBy(5.0, 1.0, -2.0)).isEqualTo(of(5.0, 3.0, 1.0));
        assertThat(rangeClosedBy(5.0, 0.0, -2.0)).isEqualTo(of(5.0, 3.0, 1.0));
        assertThat(rangeClosedBy(-Double.MAX_VALUE + 2.0E307, -Double.MAX_VALUE, -3.0E307)).isEqualTo(of(-Double.MAX_VALUE + 2.0E307));

        // int
        assertThat(rangeClosedBy(1, 3, 1)).isEqualTo(of(1, 2, 3));
        assertThat(rangeClosedBy(1, 5, 2)).isEqualTo(of(1, 3, 5));
        assertThat(rangeClosedBy(1, 6, 2)).isEqualTo(of(1, 3, 5));
        assertThat(rangeClosedBy(3, 1, -1)).isEqualTo(of(3, 2, 1));
        assertThat(rangeClosedBy(5, 1, -2)).isEqualTo(of(5, 3, 1));
        assertThat(rangeClosedBy(5, 0, -2)).isEqualTo(of(5, 3, 1));

        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE - 2, -1)).isEqualTo(of(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE - 2, -3)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE - 2, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Integer.MAX_VALUE - 3, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 3, Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, -5, Integer.MAX_VALUE)).isEmpty();
        assertThat(rangeClosedBy(Integer.MAX_VALUE, -5, -Integer.MAX_VALUE)).isEqualTo(of(Integer.MAX_VALUE, 0));

        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE + 2, 1)).isEqualTo(of(Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE + 2, 3)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE + 2, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Integer.MIN_VALUE + 3, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 3, Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, 5, Integer.MAX_VALUE)).isEqualTo(of(Integer.MIN_VALUE, -1));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, 5, -Integer.MAX_VALUE)).isEmpty();

        // long
        assertThat(rangeClosedBy(1L, 3L, 1)).isEqualTo(of(1L, 2L, 3L));
        assertThat(rangeClosedBy(1L, 5L, 2)).isEqualTo(of(1L, 3L, 5L));
        assertThat(rangeClosedBy(1L, 6L, 2)).isEqualTo(of(1L, 3L, 5L));
        assertThat(rangeClosedBy(3L, 1L, -1)).isEqualTo(of(3L, 2L, 1L));
        assertThat(rangeClosedBy(5L, 1L, -2)).isEqualTo(of(5L, 3L, 1L));
        assertThat(rangeClosedBy(5L, 0L, -2)).isEqualTo(of(5L, 3L, 1L));

        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE - 2, -1)).isEqualTo(of(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE - 2, -3)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE - 2, Long.MAX_VALUE, 3L)).isEqualTo(of(Long.MAX_VALUE - 2));
        assertThat(rangeClosedBy(Long.MAX_VALUE - 3, Long.MAX_VALUE, 3L)).isEqualTo(of(Long.MAX_VALUE - 3, Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, -5, Long.MAX_VALUE)).isEmpty();
        assertThat(rangeClosedBy(Long.MAX_VALUE, -5, -Long.MAX_VALUE)).isEqualTo(of(Long.MAX_VALUE, 0L));

        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE + 2, 1L)).isEqualTo(of(Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE + 2, 3L)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE + 2, Long.MIN_VALUE, -3L)).isEqualTo(of(Long.MIN_VALUE + 2));
        assertThat(rangeClosedBy(Long.MIN_VALUE + 3, Long.MIN_VALUE, -3L)).isEqualTo(of(Long.MIN_VALUE + 3, Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, 5L, Long.MAX_VALUE)).isEqualTo(of(Long.MIN_VALUE, -1L));
        assertThat(rangeClosedBy(Long.MIN_VALUE, 5L, -Long.MAX_VALUE)).isEmpty();
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMIN_VALUE() {

        // char
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEqualTo(of(Character.MIN_VALUE));
        assertThat(rangeClosedBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEqualTo(of(Character.MIN_VALUE));

        // double
        assertThat(rangeClosedBy(-Double.MAX_VALUE, -Double.MAX_VALUE, 1)).isEqualTo(of(-Double.MAX_VALUE));
        assertThat(rangeClosedBy(-Double.MAX_VALUE, -Double.MAX_VALUE, 3)).isEqualTo(of(-Double.MAX_VALUE));
        assertThat(rangeClosedBy(-Double.MAX_VALUE, -Double.MAX_VALUE, -1)).isEqualTo(of(-Double.MAX_VALUE));
        assertThat(rangeClosedBy(-Double.MAX_VALUE, -Double.MAX_VALUE, -3)).isEqualTo(of(-Double.MAX_VALUE));

        // int
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeClosedBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE));

        // long
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 1)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, 3)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -1)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeClosedBy(Long.MIN_VALUE, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE));
    }

    @Test
    public void shouldCreateRangeClosedByWhereFromAndToEqualMAX_VALUE() {

        // char
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEqualTo(of(Character.MAX_VALUE));
        assertThat(rangeClosedBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEqualTo(of(Character.MAX_VALUE));

        // double
        assertThat(rangeClosedBy(Double.MAX_VALUE, Double.MAX_VALUE, 1)).isEqualTo(of(Double.MAX_VALUE));
        assertThat(rangeClosedBy(Double.MAX_VALUE, Double.MAX_VALUE, 3)).isEqualTo(of(Double.MAX_VALUE));
        assertThat(rangeClosedBy(Double.MAX_VALUE, Double.MAX_VALUE, -1)).isEqualTo(of(Double.MAX_VALUE));
        assertThat(rangeClosedBy(Double.MAX_VALUE, Double.MAX_VALUE, -3)).isEqualTo(of(Double.MAX_VALUE));

        // int
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeClosedBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEqualTo(of(Integer.MAX_VALUE));

        // long
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 1)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -1)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeClosedBy(Long.MAX_VALUE, Long.MAX_VALUE, -3)).isEqualTo(of(Long.MAX_VALUE));
    }

    // -- static range()

    @Test
    public void shouldCreateRangeWhereFromIsGreaterThanTo() {
        assertThat(range('b', 'a').isEmpty());
        assertThat(range(1, 0)).isEmpty();
        assertThat(range(1L, 0L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromEqualsTo() {
        assertThat(range('a', 'a')).isEmpty();
        assertThat(range(0, 0)).isEmpty();
        assertThat(range(0L, 0L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromIsLessThanTo() {
        assertThat(range('a', 'c')).isEqualTo(of('a', 'b'));
        assertThat(range(1, 3)).isEqualTo(of(1, 2));
        assertThat(range(1L, 3L)).isEqualTo(of(1L, 2L));
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMIN_VALUE() {
        assertThat(range(Character.MIN_VALUE, Character.MIN_VALUE)).isEmpty();
        assertThat(range(Integer.MIN_VALUE, Integer.MIN_VALUE)).isEmpty();
        assertThat(range(Long.MIN_VALUE, Long.MIN_VALUE)).isEmpty();
    }

    @Test
    public void shouldCreateRangeWhereFromAndToEqualMAX_VALUE() {
        assertThat(range(Character.MAX_VALUE, Character.MAX_VALUE)).isEmpty();
        assertThat(range(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEmpty();
        assertThat(range(Long.MAX_VALUE, Long.MAX_VALUE)).isEmpty();
    }

    // -- static rangeBy()

    @Test
    public void shouldCreateRangeByWhereFromIsGreaterThanToAndStepWrongDirection() {

        // char
        assertThat(rangeBy('b', 'a', 1)).isEmpty();
        assertThat(rangeBy('b', 'a', 3)).isEmpty();
        assertThat(rangeBy('a', 'b', -1)).isEmpty();
        assertThat(rangeBy('a', 'b', -3)).isEmpty();

        // double
        assertThat(rangeBy(1.0, 0.0, 1.0)).isEmpty();
        assertThat(rangeBy(1.0, 0.0, 3.0)).isEmpty();
        assertThat(rangeBy(0.0, 1.0, -1.0)).isEmpty();
        assertThat(rangeBy(0.0, 1.0, -3.0)).isEmpty();

        // int
        assertThat(rangeBy(1, 0, 1)).isEmpty();
        assertThat(rangeBy(1, 0, 3)).isEmpty();
        assertThat(rangeBy(0, 1, -1)).isEmpty();
        assertThat(rangeBy(0, 1, -3)).isEmpty();

        // long
        assertThat(rangeBy(1L, 0L, 1L)).isEmpty();
        assertThat(rangeBy(1L, 0L, 3L)).isEmpty();
        assertThat(rangeBy(0L, 1L, -1L)).isEmpty();
        assertThat(rangeBy(0L, 1L, -3L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWithBigStep() {
        // step * (from - toExclusive) < 0 because of overflow
        // int
        assertThat(rangeBy(3_721, 2_000_000, 3_721)).isNotEmpty();
        // long
        assertThat(rangeBy(3_221_000L, 200_000_000_000L, 154_221_000L)).isNotEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromEqualsTo() {

        // char
        assertThat(rangeBy('a', 'a', 1)).isEmpty();
        assertThat(rangeBy('a', 'a', 3)).isEmpty();
        assertThat(rangeBy('a', 'a', -1)).isEmpty();
        assertThat(rangeBy('a', 'a', -3)).isEmpty();

        // double
        assertThat(rangeBy(0.0, 0.0, 1.0)).isEmpty();
        assertThat(rangeBy(0.0, 0.0, 3.0)).isEmpty();
        assertThat(rangeBy(0.0, 0.0, -1.0)).isEmpty();
        assertThat(rangeBy(0.0, 0.0, -3.0)).isEmpty();

        // int
        assertThat(rangeBy(0, 0, 1)).isEmpty();
        assertThat(rangeBy(0, 0, 3)).isEmpty();
        assertThat(rangeBy(0, 0, -1)).isEmpty();
        assertThat(rangeBy(0, 0, -3)).isEmpty();

        // long
        assertThat(rangeBy(0L, 0L, 1L)).isEmpty();
        assertThat(rangeBy(0L, 0L, 3L)).isEmpty();
        assertThat(rangeBy(0L, 0L, -1L)).isEmpty();
        assertThat(rangeBy(0L, 0L, -3L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromIsLessThanToAndStepCorrectDirection() {

        // char
        assertThat(rangeBy('a', 'c', 1)).isEqualTo(of('a', 'b'));
        assertThat(rangeBy('a', 'd', 2)).isEqualTo(of('a', 'c'));
        assertThat(rangeBy('c', 'a', -1)).isEqualTo(of('c', 'b'));
        assertThat(rangeBy('d', 'a', -2)).isEqualTo(of('d', 'b'));
        assertThat(rangeBy((char) (Character.MAX_VALUE - 3), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 3)));
        assertThat(rangeBy((char) (Character.MAX_VALUE - 4), Character.MAX_VALUE, 3)).isEqualTo(of((char) (Character.MAX_VALUE - 4), (char) (Character.MAX_VALUE - 1)));
        assertThat(rangeBy((char) (Character.MIN_VALUE + 3), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 3)));
        assertThat(rangeBy((char) (Character.MIN_VALUE + 4), Character.MIN_VALUE, -3)).isEqualTo(of((char) (Character.MIN_VALUE + 4), (char) (Character.MIN_VALUE + 1)));

        // double
        assertThat(rangeBy(1.0, 3.0, 1.0)).isEqualTo(of(1.0, 2.0));
        assertThat(rangeBy(1.0, 4.0, 2.0)).isEqualTo(of(1.0, 3.0));
        assertThat(rangeBy(3.0, 1.0, -1.0)).isEqualTo(of(3.0, 2.0));
        assertThat(rangeBy(4.0, 1.0, -2.0)).isEqualTo(of(4.0, 2.0));
        assertThat(rangeBy(Double.MAX_VALUE - 3.0E307, Double.MAX_VALUE, 3.0E307)).isEqualTo(of(Double.MAX_VALUE - 3.0E307));
        assertThat(rangeBy(-Double.MAX_VALUE + 3.0E307, -Double.MAX_VALUE, -3.0E307)).isEqualTo(of(-Double.MAX_VALUE + 3.0E307));

        // int
        assertThat(rangeBy(1, 3, 1)).isEqualTo(of(1, 2));
        assertThat(rangeBy(1, 4, 2)).isEqualTo(of(1, 3));
        assertThat(rangeBy(3, 1, -1)).isEqualTo(of(3, 2));
        assertThat(rangeBy(4, 1, -2)).isEqualTo(of(4, 2));

        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE - 3, -1)).isEqualTo(of(Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2));
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE - 3, -3)).isEqualTo(of(Integer.MAX_VALUE));
        assertThat(rangeBy(Integer.MAX_VALUE - 3, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 3));
        assertThat(rangeBy(Integer.MAX_VALUE - 4, Integer.MAX_VALUE, 3)).isEqualTo(of(Integer.MAX_VALUE - 4, Integer.MAX_VALUE - 1));
        assertThat(rangeBy(Integer.MAX_VALUE, -5, Integer.MAX_VALUE)).isEmpty();
        assertThat(rangeBy(Integer.MAX_VALUE, -5, -Integer.MAX_VALUE)).isEqualTo(of(Integer.MAX_VALUE, 0));

        assertThat(rangeBy(Integer.MIN_VALUE + 3, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 3));
        assertThat(rangeBy(Integer.MIN_VALUE + 4, Integer.MIN_VALUE, -3)).isEqualTo(of(Integer.MIN_VALUE + 4, Integer.MIN_VALUE + 1));
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE + 3, 1)).isEqualTo(of(Integer.MIN_VALUE, Integer.MIN_VALUE + 1, Integer.MIN_VALUE + 2));
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE + 3, 3)).isEqualTo(of(Integer.MIN_VALUE));
        assertThat(rangeBy(Integer.MIN_VALUE, 5, Integer.MAX_VALUE)).isEqualTo(of(Integer.MIN_VALUE, -1));
        assertThat(rangeBy(Integer.MIN_VALUE, 5, -Integer.MAX_VALUE)).isEmpty();

        // long
        assertThat(rangeBy(1L, 3L, 1L)).isEqualTo(of(1L, 2L));
        assertThat(rangeBy(1L, 4L, 2L)).isEqualTo(of(1L, 3L));
        assertThat(rangeBy(3L, 1L, -1L)).isEqualTo(of(3L, 2L));
        assertThat(rangeBy(4L, 1L, -2L)).isEqualTo(of(4L, 2L));
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE - 3, -1)).isEqualTo(of(Long.MAX_VALUE, Long.MAX_VALUE - 1, Long.MAX_VALUE - 2));
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE - 3, -3)).isEqualTo(of(Long.MAX_VALUE));
        assertThat(rangeBy(Long.MAX_VALUE - 3, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE - 3));
        assertThat(rangeBy(Long.MAX_VALUE - 4, Long.MAX_VALUE, 3)).isEqualTo(of(Long.MAX_VALUE - 4, Long.MAX_VALUE - 1));
        assertThat(rangeBy(Long.MAX_VALUE, -5, Long.MAX_VALUE)).isEmpty();
        assertThat(rangeBy(Long.MAX_VALUE, -5, -Long.MAX_VALUE)).isEqualTo(of(Long.MAX_VALUE, 0L));

        assertThat(rangeBy(Long.MIN_VALUE + 3, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE + 3));
        assertThat(rangeBy(Long.MIN_VALUE + 4, Long.MIN_VALUE, -3)).isEqualTo(of(Long.MIN_VALUE + 4, Long.MIN_VALUE + 1));
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE + 3, 1)).isEqualTo(of(Long.MIN_VALUE, Long.MIN_VALUE + 1, Long.MIN_VALUE + 2));
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE + 3, 3)).isEqualTo(of(Long.MIN_VALUE));
        assertThat(rangeBy(Long.MIN_VALUE, 5, Long.MAX_VALUE)).isEqualTo(of(Long.MIN_VALUE, -1L));
        assertThat(rangeBy(Long.MIN_VALUE, 5, -Long.MAX_VALUE)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMIN_VALUE() {

        // char
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Character.MIN_VALUE, Character.MIN_VALUE, -3)).isEmpty();

        // double
        assertThat(rangeBy(-Double.MAX_VALUE, -Double.MAX_VALUE, 1.0)).isEmpty();
        assertThat(rangeBy(-Double.MAX_VALUE, -Double.MAX_VALUE, 3.0)).isEmpty();
        assertThat(rangeBy(-Double.MAX_VALUE, -Double.MAX_VALUE, -1.0)).isEmpty();
        assertThat(rangeBy(-Double.MAX_VALUE, -Double.MAX_VALUE, -3.0)).isEmpty();

        // int
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Integer.MIN_VALUE, Integer.MIN_VALUE, -3)).isEmpty();

        // long
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 1L)).isEmpty();
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, 3L)).isEmpty();
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -1L)).isEmpty();
        assertThat(rangeBy(Long.MIN_VALUE, Long.MIN_VALUE, -3L)).isEmpty();
    }

    @Test
    public void shouldCreateRangeByWhereFromAndToEqualMAX_VALUE() {

        // char
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Character.MAX_VALUE, Character.MAX_VALUE, -3)).isEmpty();

        // double
        assertThat(rangeBy(Double.MAX_VALUE, Double.MAX_VALUE, 1.0)).isEmpty();
        assertThat(rangeBy(Double.MAX_VALUE, Double.MAX_VALUE, 3.0)).isEmpty();
        assertThat(rangeBy(Double.MAX_VALUE, Double.MAX_VALUE, -1.0)).isEmpty();
        assertThat(rangeBy(Double.MAX_VALUE, Double.MAX_VALUE, -3.0)).isEmpty();

        // int
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 1)).isEmpty();
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, 3)).isEmpty();
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -1)).isEmpty();
        assertThat(rangeBy(Integer.MAX_VALUE, Integer.MAX_VALUE, -3)).isEmpty();

        // long
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 1L)).isEmpty();
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, 3L)).isEmpty();
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -1L)).isEmpty();
        assertThat(rangeBy(Long.MAX_VALUE, Long.MAX_VALUE, -3L)).isEmpty();
    }

    // step == 0

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitCharRangeByStepZero() {
        rangeBy('a', 'b', 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeByStepZero() {
        rangeBy(0.0, 1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitIntRangeByStepZero() {
        rangeBy(0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitLongRangeByStepZero() {
        rangeBy(0L, 1L, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitCharRangeClosedByStepZero() {
        rangeClosedBy('a', 'b', 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeClosedByStepZero() {
        rangeClosedBy(0.0, 1.0, 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitIntRangeClosedByStepZero() {
        rangeClosedBy(0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitLongRangeClosedByStepZero() {
        rangeClosedBy(0L, 1L, 0L);
    }

    // double special cases

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeClosedByToEqualsNaN() {
        rangeClosedBy(0.0, Double.NaN, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeClosedByFromEqualNaN() {
        rangeClosedBy(Double.NaN, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeClosedByStepEqualNaN() {
        rangeClosedBy(0.0, 10.0, Double.NaN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeByToEqualsNaN() {
        rangeBy(0.0, Double.NaN, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeByFromEqualNaN() {
        rangeBy(Double.NaN, 0.0, 1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldProhibitDoubleRangeByStepEqualNaN() {
        rangeBy(0.0, 10.0, Double.NaN);
    }
}
