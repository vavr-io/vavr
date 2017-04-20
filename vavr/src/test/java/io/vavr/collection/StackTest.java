/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection;

import io.vavr.Tuple2;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("deprecation")
public class StackTest {

    // -- static narrow

    @Test
    public void shouldNarrowStack() {
        final Stack<Double> doubles = List.of(1.0d);
        Stack<Number> numbers = Stack.narrow(doubles);
        numbers = numbers.push(new BigDecimal("2.0"));
        int sum = 0;
        while (!numbers.isEmpty()) {
            final Tuple2<Number, ? extends Stack<Number>> t = numbers.pop2();
            sum += t._1.intValue();
            numbers = t._2;
        }
        assertThat(sum).isEqualTo(3);
    }

}
