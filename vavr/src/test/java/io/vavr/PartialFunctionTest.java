/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr;

import io.vavr.collection.HashMap;
import io.vavr.control.Option;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PartialFunctionTest {

    @Test
    public void shouldReturnSome() {
        Option<String> oneToOne = HashMap.of(1, "One").lift().apply(1);
        assertThat(oneToOne instanceof Option.Some);
    }

    @Test
    public void shouldReturnNone() {
        Option<String> none = HashMap.<Integer, String>empty().lift().apply(1);
        assertThat(none instanceof Option.None);
    }

}