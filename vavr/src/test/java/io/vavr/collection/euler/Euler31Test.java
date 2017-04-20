/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection.euler;

import io.vavr.collection.List;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <strong>Problem 31: Coin sums</strong>
 * <p>In England the currency is made up of pound, £, and pence, p, and there are eight coins in general circulation:</p>
 * <blockquote>1p, 2p, 5p, 10p, 20p, 50p, £1 (100p) and £2 (200p).</blockquote>
 * <p>It is possible to make £2 in the following way:</p>
 * <blockquote>1×£1 + 1×50p + 2×20p + 1×5p + 1×2p + 3×1p</blockquote>
 * <p>How many different ways can £2 be made using any number of coins?</p>
 * See also <a href="https://projecteuler.net/problem=31">projecteuler.net problem 31</a>.
 */
public class Euler31Test {

    @Test
    public void shouldSolveProblem31() {
        final List<Integer> coins = List.of(1, 2, 5, 10, 20, 50, 100, 200);
        assertThat(coinSums(200, coins)).isEqualTo(73682);
    }

    private static int coinSums(int n, List<Integer> coins) {
        return (n == 0) ? 1 :
               (n < 0 || coins.isEmpty()) ? 0 :
               coinSums(n, coins.tail()) + coinSums(n - coins.head(), coins);
    }

}
