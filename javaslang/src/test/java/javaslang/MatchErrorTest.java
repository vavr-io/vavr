/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.junit.Test;

import java.util.function.Function;

import static javaslang.Match.$;
import static javaslang.Match.*;
import static javaslang.Match.Match;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class MatchErrorTest {

    @Test
    public void shouldReturnCorrectObjectWhenMatchingByMonad() {

        final Object obj = new Object();
        try {

            Match(obj).of(
                    Case($(0), Function.identity())
            );

            failBecauseExceptionWasNotThrown(MatchError.class);

        } catch (MatchError matchError) {
            assertThat(matchError.getObject()).isEqualTo(obj);
        }
    }

    @Test
    public void shouldReturnCorrectObjectWhenMatchingByFunction() {
        final Object obj = new Object();
        try {
            Match.$(0).apply(obj);
        } catch (MatchError matchError) {
            assertThat(matchError.getObject()).isEqualTo(obj);
        }
    }
}
