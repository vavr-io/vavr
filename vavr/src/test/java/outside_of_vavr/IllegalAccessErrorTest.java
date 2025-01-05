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
