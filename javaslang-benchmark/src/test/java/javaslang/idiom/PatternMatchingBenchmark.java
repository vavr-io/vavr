package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;

import static javaslang.API.*;

/**
 * Benchmark for nested loops vs javaslang's For().yield comprehensions.
 *
 * @see For2
 */
public class PatternMatchingBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            PatternMatching.class
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
        int INSTANCES = 1000;
        int CASES = 5;
        int[] VALUES;

        @Setup
        public void setup() {
            VALUES = Array.range(0, INSTANCES).map(i -> new Random(0).nextInt(CASES)).toJavaStream().mapToInt(i -> i).toArray();
        }
    }

    public static class PatternMatching extends Base {
        @Benchmark
        public void java_switch(Blackhole bh) {
            for (int i : VALUES) {
                final String result;
                switch (i) {
                    case 0:
                        result = "0";
                        break;
                    case 1:
                        result = "1";
                        break;
                    case 2:
                        result = "2";
                        break;
                    case 3:
                        result = "3";
                        break;
                    default:
                        result = "4";
                        break;
                }

                assert String.valueOf(i).equals(result);
                bh.consume(result);
            }
        }

        @Benchmark
        public void slang_match(Blackhole bh) {
            for (int i : VALUES) {
                final String result = Match(i).of(
                        Case($(0), "0"),
                        Case($(1), "1"),
                        Case($(2), "2"),
                        Case($(3), "3"),
                        Case($(), "4")
                );
                assert String.valueOf(i).equals(result);
                bh.consume(result);
            }
        }
    }
}