/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class IntMod2Test {

    private static final IntMod2 _1 = new IntMod2(1);
    private static final IntMod2 _2 = new IntMod2(2);
    private static final IntMod2 _3 = new IntMod2(3);
    private static final IntMod2 _4 = new IntMod2(4);

    @Test
    public void shouldBeEqualIfEven() {
        assertThat(_2.equals(_4)).isTrue();
        assertThat(_2.compareTo(_4)).isEqualTo(0);
    }

    @Test
    public void shouldBeEqualIfOdd() {
        assertThat(_1.equals(_3)).isTrue();
        assertThat(_1.compareTo(_3)).isEqualTo(0);
    }

    @Test
    public void shouldNotBeEqualIfEvenAndOdd() {
        assertThat(_1.equals(_2)).isFalse();
        assertThat(_1.compareTo(_2)).isEqualTo(1);
        assertThat(_2.compareTo(_3)).isEqualTo(-1);
    }

}
