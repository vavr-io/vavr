/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MapEntryTest {

    @Test
    public void testFlatMap() {
        assertThat(Map.Entry.of(1, 2).flatMap((c1, c2) -> Map.Entry.of(c1 + 1, c2 + 1))).isEqualTo(Map.Entry.of(2, 3));
    }

    @Test
    public void testMap() {
        assertThat(Map.Entry.of(1, 2).map(c -> c + 1, c -> c + 2)).isEqualTo(Map.Entry.of(2, 4));
    }

    @Test
    public void testEqualsSame() {
        Map.Entry<Integer, Integer> e = Map.Entry.of(1, 2);
        assertThat(e).isEqualTo(e);
    }

    @Test
    public void testEquals() {
        assertThat(Map.Entry.of(1, 2)).isEqualTo(Map.Entry.of(1, 2));
    }

    @Test
    public void testNonEqualsEntry() {
        assertThat(Map.Entry.of(1, 2)).isNotEqualTo(Map.Entry.of(3, 2));
    }

    @Test
    public void testNonEqualsNonEntry() {
        assertThat(Map.Entry.of(1, 2)).isNotEqualTo(1);
    }

    @Test
    public void testToString() {
        assertThat(Map.Entry.of(1, 2).toString()).isEqualTo("1 -> 2");
    }
}
