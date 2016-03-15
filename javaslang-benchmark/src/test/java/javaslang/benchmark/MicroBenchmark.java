package javaslang.benchmark;

import javaslang.collection.List;
import javaslang.collection.Queue;
import javaslang.collection.Stream;
import javaslang.collection.Vector;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;


import java.util.Random;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
public class MicroBenchmark {


    @Test
    public void launchBenchmark() throws Exception {

        Options opt = new OptionsBuilder()
                // Specify which benchmarks to run.
                // You can be more specific if you'd like to run only one benchmark per test.
                .include(this.getClass().getName() + ".*")
                // Set the following options as needed
                .mode(Mode.Throughput)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(2))
                .warmupIterations(3)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(10)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                //.jvmArgs("-XX:+UnlockDiagnosticVMOptions", "-XX:+PrintInlining")
                //.addProfiler(WinPerfAsmProfiler.class)
                .build();

        new Runner(opt).run();
    }

    // The JMH samples are the best documentation for how to use it
    // http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
    @State(Scope.Thread)
    public static class BenchmarkState {
        @Param({"1", "100", "10000"})
        int containerSize;


        List<Integer> slangList = List.empty();
        Vector<Integer> slangVector = Vector.empty();
        Queue<Integer> slangQueue = Queue.empty();
        Stream<Integer> slangStream = Stream.empty();

        @Setup(Level.Trial)
        public void initialize() {
            Random rand = new Random();
            slangList = slangList.padTo(containerSize, 0);
            slangVector = slangVector.padTo(containerSize, 0);
            slangQueue = slangQueue.padTo(containerSize, 0);
            slangStream = slangStream.padTo(containerSize, 0);
        }
    }

    @Benchmark
    @Group("head")
    public Object slangListHead(BenchmarkState state) {
        return state.slangList.head();
    }

    @Benchmark
    @Group("head")
    public Object slangVectorHead(BenchmarkState state) {
        return state.slangVector.head();
    }

    @Benchmark
    @Group("head")
    public Object slangQueueHead(BenchmarkState state) {
        return state.slangQueue.head();
    }

    @Benchmark
    @Group("head")
    public Object slangStreamHead(BenchmarkState state) {
        return state.slangStream.head();
    }

    @Benchmark
    @Group("tail")
    public Object slangListTail(BenchmarkState state) {
        return state.slangList.tail();
    }

    @Benchmark
    @Group("tail")
    public Object slangVectorTail(BenchmarkState state) {
        return state.slangVector.tail();
    }

    @Benchmark
    @Group("tail")
    public Object slangQueueTail(BenchmarkState state) {
        return state.slangQueue.tail();
    }

    @Benchmark
    @Group("tail")
    public Object slangStreamTail(BenchmarkState state) {
        return state.slangStream.tail();
    }

    @Benchmark
    @Group("get")
    public Object slangListGet(BenchmarkState state) {
        return state.slangList.get(state.containerSize / 2);
    }

    @Benchmark
    @Group("get")
    public Object slangVectorGet(BenchmarkState state) {
        return state.slangVector.get(state.containerSize / 2);
    }

    @Benchmark
    @Group("get")
    public Object slangQueueGet(BenchmarkState state) {
        return state.slangQueue.get(state.containerSize / 2);
    }

    @Benchmark
    @Group("get")
    public Object slangStreamGet(BenchmarkState state) {
        return state.slangStream.get(state.containerSize / 2);
    }

    @Benchmark
    @Group("update")
    public Object slangListUpdate(BenchmarkState state) {
        return state.slangStream.update(state.containerSize / 2, 1);
    }

    @Benchmark
    @Group("update")
    public Object slangVectorUpdate(BenchmarkState state) {
        return state.slangVector.update(state.containerSize / 2, 1);
    }

    @Benchmark
    @Group("update")
    public Object slangQueueUpdate(BenchmarkState state) {
        return state.slangQueue.update(state.containerSize / 2, 1);
    }

    @Benchmark
    @Group("update")
    public Object slangStreamUpdate(BenchmarkState state) {
        return state.slangStream.update(state.containerSize / 2, 1);
    }


    @Benchmark
    @Group("prepend")
    public Object slangListPrepend(BenchmarkState state) {
        return state.slangList.prepend(1);
    }

    @Benchmark
    @Group("prepend")
    public Object slangVectorPrepend(BenchmarkState state) {
        return state.slangVector.prepend(1);
    }

    @Benchmark
    @Group("prepend")
    public Object slangQueuePrepend(BenchmarkState state) {
        return state.slangQueue.prepend(1);
    }

    @Benchmark
    @Group("prepend")
    public Object slangStreamPrepend(BenchmarkState state) {
        return state.slangStream.prepend(1);
    }

    @Benchmark
    @Group("append")
    public Object slangListAppend(BenchmarkState state) {
        return state.slangList.append(1);
    }

    @Benchmark
    @Group("append")
    public Object slangVectorAppend(BenchmarkState state) {
        return state.slangVector.append(1);
    }

    @Benchmark
    @Group("append")
    public Object slangQueueAppend(BenchmarkState state) {
        return state.slangQueue.append(1);
    }

    @Benchmark
    @Group("append")
    public Object slangStreamAppend(BenchmarkState state) {
        return state.slangStream.append(1);
    }
}