/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.test;

import io.vavr.test.Property.Condition;
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
