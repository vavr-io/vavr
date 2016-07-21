package javaslang.idiom;

import javaslang.JmhRunner;
import javaslang.collection.Array;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;

import java.util.Random;

import static javaslang.API.*;

/**
 * Benchmark for nested loops vs javaslang's For().yield comprehensions.
 *
 * @see javaslang.API.For2
 */
public class PatternMatchingBenchmark {
    static final Array<Class<?>> CLASSES = Array.of(
            PatternMatchingMatches.class
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
        @Param({ "2", "3", "4" })
        public int MATCH_CASES;

        int random;

        @Setup
        public void setup() {
            random = new Random(0).nextInt(MATCH_CASES);
        }
    }

    public static class PatternMatchingMatches extends Base {

        @Benchmark
        public Object slang_match_ok() {
            String res = Match(random).of(
                    Case($(0), "0"),
                    Case($(1), "1"),
                    Case($(2), "2"),
                    Case($(3), "3"),
                    Case($(), "error")
            );

            assert String.valueOf(random).equals(res);
            return res;
        }

        @Benchmark
        public Object java_switch() {
            String res;

            switch (random) {
                case 0:
                    res = "0";
                    break;
                case 1:
                    res = "1";
                    break;
                case 2:
                    res = "2";
                    break;
                case 3:
                    res = "3";
                    break;
                default:
                    res = "error";
            }

            assert String.valueOf(random).equals(res);
            return res;
        }
    }

}