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

import java.util.Random;
import org.openjdk.jol.info.GraphLayout;

/**
 * Reports the retained heap footprint of the classic HAMT vs the experimental CHAMP at several
 * sizes, using JOL. Both tries are built from the <em>same</em> boxed key/value instances, so the
 * shared {@code Integer} objects are counted once per graph and the difference between the totals
 * reflects node/structural overhead rather than payload.
 *
 * <p>Run with {@code -Djol.magicFieldOffset=true} (and ideally {@code --add-opens} for JOL) via the
 * {@code champ-footprint} profile.
 */
public final class ChampMemoryFootprint {

    private ChampMemoryFootprint() {
    }

    public static void main(String[] args) {
        final int[] sizes = {1_000, 10_000, 100_000};
        System.out.printf("%-8s %14s %14s %10s %10s %8s%n",
                "size", "HAMT bytes", "CHAMP bytes", "HAMT/ent", "CHAMP/ent", "CHAMP%");
        for (int size : sizes) {
            final Integer[] keys = distinctKeys(size);

            HashArrayMappedTrie<Integer, Integer> hamt = HashArrayMappedTrie.empty();
            HashArrayMappedTrie<Integer, Integer> champ = CompressedHashArrayMappedPrefixTrie.empty();
            for (Integer k : keys) {
                hamt = hamt.put(k, k);
                champ = champ.put(k, k);
            }

            final long hamtBytes = GraphLayout.parseInstance(hamt).totalSize();
            final long champBytes = GraphLayout.parseInstance(champ).totalSize();
            System.out.printf("%-8d %14d %14d %10.1f %10.1f %7.1f%%%n",
                    size, hamtBytes, champBytes,
                    (double) hamtBytes / size, (double) champBytes / size,
                    100.0 * champBytes / hamtBytes);
        }
    }

    private static Integer[] distinctKeys(int size) {
        final Random r = new Random(0xC0FFEE);
        final java.util.LinkedHashSet<Integer> set = new java.util.LinkedHashSet<>();
        while (set.size() < size) {
            set.add(r.nextInt(size * 4));
        }
        return set.toArray(new Integer[0]);
    }
}
