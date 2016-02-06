/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.control;

import javaslang.AbstractValueTest;
import javaslang.control.Match.MatchValue.Otherwise;
import org.junit.Test;

public class MatchValueOtherwiseTest extends AbstractValueTest {

    // -- AbstractValueTest

    @Override
    protected <R> Otherwise<R> empty() {
        return Match.of(null).otherwise((R) null).filter(ignored -> false);
    }

    @Override
    protected <R> Otherwise<R> of(R element) {
        return Match.of(null).otherwise(element);
    }

    @SafeVarargs
    @Override
    protected final <R> Otherwise<R> of(R... elements) {
        return of(elements[0]);
    }

    @Override
    protected boolean useIsEqualToInsteadOfIsSameAs() {
        return true;
    }

    @Override
    protected int getPeekNonNilPerformingAnAction() {
        return 1;
    }

    // -- MatchValue.Otherwise

    @Test
    public void shouldAcceptSupplier() {
        assertThat(Match.of(null).otherwise(() -> 42).get()).isEqualTo(42);
    }

    @Test
    public void shouldAcceptFunction() {
        assertThat(Match.of(null).otherwise(x -> 42).get()).isEqualTo(42);
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowOtherwise() {
        assertThat(Match.of(null).otherwiseThrow(RuntimeException::new).get()).isEqualTo(42);
    }

}
