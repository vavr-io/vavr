/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.test.Property.Condition;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConditionTest {

    /**
     * Def: A 'Condition' is the result of {@code p => q} where {@code p} is a pre-condition and {@code q} is a post-condition.
     * <p>
     * The following holds: {@code p => q ≡ ¬p ∨ q}
     */
    @Test
    public void should() {
        assertThat(cond(false, false)).isTrue();
        assertThat(cond(false, true)).isTrue();
        assertThat(cond(true, false)).isFalse();
        assertThat(cond(true, true)).isTrue();
    }

    private boolean cond(boolean p, boolean q) {
        return !new Condition(p, q).isFalse();
    }
}
