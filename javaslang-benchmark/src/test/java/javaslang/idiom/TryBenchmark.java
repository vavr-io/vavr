package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.collection.Array;
import javaslang.control.Try;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

/**
 * Benchmark for Try vs try/catch.
 */
public class TryBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            TryThrowsCatchBenchmark.class,
            TryNoThrowsCatchBenchmark.class
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

        Integer throwsException() {
            throw new RuntimeException();
        }

        Integer doesntThrow() {
            return 0;
        }
    }

    public static class TryThrowsCatchBenchmark extends Base {

        @Benchmark
        public void slang_try_actual_exception(Blackhole bh) {
            Integer res = Try.of(this::throwsException)
                    .recover(RuntimeException.class, 1)
                    .getOrElse(0);

            assert res == 1;
            bh.consume(res);
        }

        @Benchmark
        public void java_try_actual_exception(Blackhole bh) {
            try {
                this.throwsException();
                assert false;
            } catch (RuntimeException e) {
                bh.consume(e);
            }
        }
    }

    public static class TryNoThrowsCatchBenchmark extends Base {

        @Benchmark
        public void slang_try_no_exception(Blackhole bh) {
            Integer res = Try.of(this::doesntThrow)
                    .recover(RuntimeException.class, 1)
                    .getOrElse(0);

            assert res == 0;
            bh.consume(res);
        }

        @Benchmark
        public void java_try_no_exception(Blackhole bh) {
            try {
                Integer res = this.doesntThrow();
                bh.consume(res);
            } catch (RuntimeException e) {
                assert false;
            }
        }
    }


}