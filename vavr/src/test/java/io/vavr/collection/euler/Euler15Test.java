/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.collection.euler;

import org.junit.Test;

import java.math.BigInteger;

import static io.vavr.collection.euler.Utils.factorial;
import static org.assertj.core.api.Assertions.assertThat;

public class Euler15Test {

    /**
     * <strong>Problem 15: Lattice paths</strong>
     * <p>
     * Starting in the top left corner of a 2x2 grid, and only being able to move to the right and down,
     * there are exactly 6 routes to the bottom right corner.
     * <p>
     * How many such routes are there through a 20×20 grid?
     * <p>
     * See also <a href="https://projecteuler.net/problem=15">projecteuler.net problem 15</a>.
     */
    @Test
    public void shouldSolveProblem15() {
        assertThat(solve(2)).isEqualTo(6);
        assertThat(solve(20)).isEqualTo(137_846_528_820L);
    }

    private static long solve(int n) {
        final BigInteger f = factorial(n);
        return factorial(2 * n).divide(f).divide(f).longValue();
    }
}
