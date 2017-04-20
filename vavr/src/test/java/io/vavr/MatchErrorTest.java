/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package io.vavr;

import org.junit.Test;

import static io.vavr.API.$;
import static io.vavr.API.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class MatchErrorTest {

    @Test
    public void shouldReturnCorrectObject() {
        final Object obj = new Object();
        try {
            Match(obj).of(
                    Case($(0), 0)
            );
            failBecauseExceptionWasNotThrown(MatchError.class);
        } catch (MatchError matchError) {
            assertThat(matchError.getObject()).isEqualTo(obj);
        }
    }
}
