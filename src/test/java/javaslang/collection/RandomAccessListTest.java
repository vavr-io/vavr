/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomAccessListTest {

    @Test
    public void shouldCreateEmptyList() {
        assertThat(RandomAccessList.empty().isEmpty()).isTrue();
    }

    @Test
    public void should() {

        RandomAccessList<Integer> list = RandomAccessList.<Integer> empty().prepend(1).prepend(2).prepend(3);
        assertThat(list.size()).isEqualTo(3);

        assertThat(list.get(0)).isEqualTo(3);
        assertThat(list.get(1)).isEqualTo(2);
        assertThat(list.get(2)).isEqualTo(1);

        list = list.set(0, 13);
        assertThat(list.size()).isEqualTo(3);

        assertThat(list.get(0)).isEqualTo(13);
        assertThat(list.get(1)).isEqualTo(2);
        assertThat(list.get(2)).isEqualTo(1);

        assertThat(list.prepend(4).prepend(5).prepend(6).prepend(7).tail().size()).isEqualTo(6);

//        RandomAccessList<Integer> list2 = RandomAccessList.empty();
//        for (int i = 0; i < 1_000_000; i++) {
//            list2 = list2.prepend(i);
//            if (i > 0) list2.get(i - 1);
//        }
//        assertThat(list2.size()).isEqualTo(1_000_000);

    }
}
