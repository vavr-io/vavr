/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package outside_of_javaslang;

import javaslang.collection.Array;
import javaslang.collection.BitSet;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import org.junit.Test;

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
