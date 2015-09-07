/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.StrictAssertions.assertThat;

public class HashMapTest extends AbstractMapTest {

    // -- map

    @Test
    public void shouldMapEmpty() {
        final Set<Integer> expected = HashSet.empty();

        final Set<Integer> actual = HashMap.<Integer,Integer>empty().map(entry -> entry.key);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldMapNonEmpty() {
        final Set<Integer> expected = HashSet.<Integer>of(1, 2);
        final Set<Integer> actual =
                HashMap.<Integer, String>of(
                        Map.Entry.of(1, "1"),
                        Map.Entry.of(2, "2"))
                .map(entry -> entry.key);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldReturnEmptySetWhenAskedForEntrySetOfAnEmptyHashMap() {
        assertThat(HashMap.empty().entrySet()).isEqualTo(HashSet.empty());
    }

    @Test
    public void shouldReturnEntrySetOfANonEmptyHashMap() {
        assertThat(HashMap.<Integer, String>of(
                    Map.Entry.of(1, "1"),
                    Map.Entry.of(2, "2"))
                .entrySet()).isEqualTo(
                HashSet.of(
                    Map.Entry.of(1, "1"),
                    Map.Entry.of(2, "2")));
    }

    // equality

    @Test
    public void shouldIgnoreOrderOfEntriesWhenComparingForEquality() {
        final Map<?, ?> map1 = HashMap.empty().put(1, 'a').put(2, 'b').put(3, 'c');
        final Map<?, ?> map2 = HashMap.empty().put(3, 'c').put(2, 'b').put(1, 'a').remove(2).put(2, 'b');
        assertThat(map1).isEqualTo(map2);
    }

    @Override
    protected <T> Map<Integer, T> emptyMap() {
        return HashMap.empty();
    }
}
