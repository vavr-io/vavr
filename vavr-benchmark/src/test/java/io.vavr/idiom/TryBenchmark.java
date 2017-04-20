/*                        __    __  __  __    __  ___
 *                       \  \  /  /    \  \  /  /  __/
 *                        \  \/  /  /\  \  \/  /  /
 *                         \____/__/  \__\____/__/.ɪᴏ
 * ᶜᵒᵖʸʳᶦᵍʰᵗ ᵇʸ ᵛᵃᵛʳ ⁻ ˡᶦᶜᵉⁿˢᵉᵈ ᵘⁿᵈᵉʳ ᵗʰᵉ ᵃᵖᵃᶜʰᵉ ˡᶦᶜᵉⁿˢᵉ ᵛᵉʳˢᶦᵒⁿ ᵗʷᵒ ᵈᵒᵗ ᶻᵉʳᵒ
 */
package io.vavr.idiom;

import io.vavr.JmhRunner;
import io.vavr.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import static io.vavr.API.Array;
import static io.vavr.API.Try;

/**
 * Benchmark for Try vs try/catch.
 */
public class TryBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            Try.class
    );

    @Test
    public void testAsserts() {
        JmhRunner.runDebugWithAsserts(CLASSES);
    }

    public static void main(String... args) {
        JmhRunner.runNormalNoAsserts(CLASSES);
    }

    @State(Scope.Benchmark)
    public static class Base {
        int inverse(int divisor) throws ArithmeticException { return 1 / divisor; }
    }

    public static class Try extends Base {
        @Benchmark
        public void java_try(Blackhole bh) {
            for (int i = 0; i <= 1; i++) {
                int result;
                try {
                    result = inverse(i);
                } catch (ArithmeticException e) {
                    result = 0;
                }
                assert result == i;
                bh.consume(result);
            }
        }

        @Benchmark
        public void slang_try(Blackhole bh) {
            for (int i = 0; i <= 1; i++) {
                int i2 = i;
                final int result = Try(() -> inverse(i2))
                        .recover(ArithmeticException.class, 0)
                        .get();

                assert result == i;
                bh.consume(result);
            }
        }
    }
}
