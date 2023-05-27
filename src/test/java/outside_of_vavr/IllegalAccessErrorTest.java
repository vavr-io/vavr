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
package outside_of_vavr;

import io.vavr.collection.Array;
import io.vavr.collection.BitSet;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;

public class IllegalAccessErrorTest {

    @Test
    public void shouldNotThrowIllegalAccessErrorWhenUsingHashMapMergeMethodReference() {
        final BiFunction<HashMap<String, String>, HashMap<String, String>, HashMap<String, String>> merge = HashMap::merge;
        final HashMap<String, String> reduced = Array.of("a", "b", "c")
                .map(t -> HashMap.of(t, t))
                .reduce(merge);
        assertThat(reduced).isEqualTo(HashMap.of("a", "a", "b", "b", "c", "c"));
    }

    @Test
    public void shouldNotThrowIllegalAccessErrorWhenUsingBitSetAddAllMethodReference() {
        final BiFunction<BitSet<Integer>, BitSet<Integer>, BitSet<Integer>> union = BitSet::union;
        final BitSet<Integer> reduced = List.of(BitSet.of(1, 2, 3), BitSet.of(2, 3, 4)).reduce(union);
        assertThat(reduced).isEqualTo(BitSet.of(1, 2, 3, 4));
    }
}
