/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*\
   G E N E R A T O R   C R A F T E D
\*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

import static org.assertj.core.api.Assertions.assertThat;

import javaslang.*;
import javaslang.collection.List;
import org.junit.Test;

public class MonadTest {

    @Test
    public void testList1() {
        List<Integer> list = List.of(1, 2);
        List<Tuple1<Integer>> crossProductPower = (List<Tuple1<Integer>>) Monad
                .lift((Function1<Integer, Tuple1<Integer>>) Tuple::of)
                .apply(list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 1);
    }

    @Test
    public void testList2() {
        List<Integer> list = List.of(1, 2);
        List<Tuple2<Integer, Integer>> crossProductPower = (List<Tuple2<Integer, Integer>>) Monad
                .lift((Function2<Integer, Integer, Tuple2<Integer, Integer>>) Tuple::of)
                .apply(list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 2);
    }

    @Test
    public void testList3() {
        List<Integer> list = List.of(1, 2);
        List<Tuple3<Integer, Integer, Integer>> crossProductPower = (List<Tuple3<Integer, Integer, Integer>>) Monad
                .lift((Function3<Integer, Integer, Integer, Tuple3<Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 3);
    }

    @Test
    public void testList4() {
        List<Integer> list = List.of(1, 2);
        List<Tuple4<Integer, Integer, Integer, Integer>> crossProductPower = (List<Tuple4<Integer, Integer, Integer, Integer>>) Monad
                .lift((Function4<Integer, Integer, Integer, Integer, Tuple4<Integer, Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 4);
    }

    @Test
    public void testList5() {
        List<Integer> list = List.of(1, 2);
        List<Tuple5<Integer, Integer, Integer, Integer, Integer>> crossProductPower = (List<Tuple5<Integer, Integer, Integer, Integer, Integer>>) Monad
                .lift((Function5<Integer, Integer, Integer, Integer, Integer, Tuple5<Integer, Integer, Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 5);
    }

    @Test
    public void testList6() {
        List<Integer> list = List.of(1, 2);
        List<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>> crossProductPower = (List<Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>>) Monad
                .lift((Function6<Integer, Integer, Integer, Integer, Integer, Integer, Tuple6<Integer, Integer, Integer, Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list, list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 6);
    }

    @Test
    public void testList7() {
        List<Integer> list = List.of(1, 2);
        List<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>> crossProductPower = (List<Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>>) Monad
                .lift((Function7<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Tuple7<Integer, Integer, Integer, Integer, Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list, list, list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 7);
    }

    @Test
    public void testList8() {
        List<Integer> list = List.of(1, 2);
        List<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>> crossProductPower = (List<Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>>) Monad
                .lift((Function8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer, Tuple8<Integer, Integer, Integer, Integer, Integer, Integer, Integer, Integer>>) Tuple::of)
                .apply(list, list, list, list, list, list, list, list);
        assertThat(crossProductPower.size()).isEqualTo(1 << 8);
    }

}