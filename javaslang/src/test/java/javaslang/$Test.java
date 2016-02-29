/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.collection.List;
import javaslang.control.Option;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class $Test {

    @Test
    public void testNone() {
        assertThat($.None((Option.None <?>) Option.none())).isEqualTo(Tuple.empty());
    }

    @Test
    public void testNil() throws Exception {
        assertThat($.Nil(List.Nil.instance())).isEqualTo(Tuple.empty());
    }
}