/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import org.junit.Test;

import java.math.BigDecimal;

public abstract class AbstractLinearSeqTest extends AbstractSeqTest {

    @Override
    abstract protected <T> LinearSeq<T> of(T element);

    // -- static narrow

    @Test
    public void shouldNarrowIndexedSeq() {
        final LinearSeq<Double> doubles = of(1.0d);
        final LinearSeq<Number> numbers = LinearSeq.narrow(doubles);
        final int actual = numbers.append(new BigDecimal("2.0")).sum().intValue();
        assertThat(actual).isEqualTo(3);
    }
}
