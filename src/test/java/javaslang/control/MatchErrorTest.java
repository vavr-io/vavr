/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MatchErrorTest {

    @Test
    public void shouldReturnCorrectObjectWhenMatchingByMonad() {
        final Object obj = new Object();
        try {
            Match.of(obj).whenIs(0).then(0).get();
        } catch (MatchError matchError) {
            assertThat(matchError.getObject()).isEqualTo(obj);
        }
    }

    @Test
    public void shouldReturnCorrectObjectWhenMatchingByFunction() {
        final Object obj = new Object();
        try {
            Match.whenIs(0).then(0).apply(obj);
        } catch (MatchError matchError) {
            assertThat(matchError.getObject()).isEqualTo(obj);
        }
    }
}
