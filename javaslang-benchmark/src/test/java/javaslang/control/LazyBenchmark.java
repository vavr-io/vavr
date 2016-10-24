package javaslang.control;

import javaslang.JmhRunner;
import javaslang.Lazy;
import javaslang.collection.Array;
import javaslang.collection.Iterator;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import static javaslang.API.Array;
import static javaslang.JmhRunner.Includes.JAVA;
import static javaslang.JmhRunner.Includes.JAVASLANG;

public class LazyBenchmark {
    static final Array<Class<?>> CLASSES = Array(
            Get.class
    );

    @Test
    public void testAsserts() { JmhRunner.runDebugWithAsserts(CLASSES); }

    public static void main(String... args) {
        JmhRunner.runDebugWithAsserts(CLASSES, JAVA, JAVASLANG);
        JmhRunner.runSlowNoAsserts(CLASSES, JAVA, JAVASLANG);
    }

    @State(Scope.Benchmark)
    public static class Base {
        final int SIZE = 10;

        Integer[] EAGERS;
        javaslang.Lazy<Integer>[] INITED_LAZIES;

        @Setup
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public void setup() {
            EAGERS = Iterator.range(0, SIZE).toJavaArray(Integer.class);
            INITED_LAZIES = Iterator.of(EAGERS).map(i -> {
                final Lazy<Integer> lazy = Lazy.of(() -> i);
                lazy.get();
                return lazy;
            }).toJavaList().toArray(new Lazy[0]);
        }
    }

    @Threads(4)
    @SuppressWarnings({ "WeakerAccess", "rawtypes" })
    public static class Get extends Base {
        @State(Scope.Thread)
        public static class Initialized {
            javaslang.Lazy<Integer>[] LAZIES;

            @Setup(Level.Invocation)
            @SuppressWarnings("unchecked")
            public void initializeMutable(Base state) {
                LAZIES = Iterator.of(state.EAGERS).map(i -> Lazy.of(() -> i)).toJavaList().toArray(new Lazy[0]);
            }
        }

        @Benchmark
        public void java_eager(Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                bh.consume(EAGERS[i]);
            }
        }

        @Benchmark
        public void slang_inited_lazy(Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                assert INITED_LAZIES[i].isEvaluated();
                bh.consume(INITED_LAZIES[i].get());
            }
        }

        @Benchmark
        public void slang_lazy(Initialized state, Blackhole bh) {
            for (int i = 0; i < SIZE; i++) {
                assert !state.LAZIES[i].isEvaluated();
                bh.consume(state.LAZIES[i].get());
            }
        }
    }
}