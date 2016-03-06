/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.control.Try;
import org.junit.Test;

import static javaslang.API.*;
import static javaslang.Predicates.instanceOf;
import static org.assertj.core.api.Assertions.assertThat;

public class PredicatesTest {

    @Test
    public void shouldUsePredicateInCaseWithSuccess() {
        final Match.Case<? super Throwable, ? extends String> f = Case(instanceOf(Error.class), "fixed");
        final String actual = Try.of(() -> "ok")
                .recover(f)
                .get();
        assertThat(actual).isEqualTo("ok");
    }

    @Test
    public void shouldUsePredicateInCaseWithFailure() {
        final Object actual = Try.of(() -> { throw new Error("error"); })
                .recover(Case(instanceOf(Error.class), "fixed"))
                .get();
        assertThat(actual).isEqualTo("fixed");
    }

}
