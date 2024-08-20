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

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.function.IntFunction;

import static java.lang.Integer.signum;
import static io.vavr.API.List;
import static org.assertj.core.api.Assertions.assertThat;

public class QuickSortTest {
    @Test
    public void shouldQuickSort() {
        final List<Integer> values = List(3, 1, 4, 1, 5, 9, 2, 6, 5, 3, 5, 8, 9, 7, 9, 3, 2, 3, 8, 4, 6, 2, 6);
        Assertions.assertThat(sort(values.shuffle())).isEqualTo(values.sorted());
        assertThat(sort2(values.shuffle())).isEqualTo(values.sorted());
    }

    /** Note: this example is only meant to show off, not to be used in reality: it can have quadratic performance and cause stack overflow */
    private static Seq<Integer> sort(Seq<Integer> values) {
        if (values.size() <= 1) return values;
        return values.tail().partition(v -> v <= values.head())
                     .apply((less, more) -> sort(less).append(values.head()).appendAll(sort(more)));
    }
    private static <T extends Comparable<T>> List<T> sort2(List<T> values) {
        if (values.size() <= 1) return values;
        final Map<Integer, List<T>> map = values.groupBy(v -> signum(v.compareTo(values.head())));
        final IntFunction<List<T>> parts =  signum -> map.get(signum).getOrElse(List());
        return sort2(parts.apply(-1)).appendAll(parts.apply(0)).appendAll(sort2(parts.apply(1)));
    }
}
