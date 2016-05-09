package javaslang.benchmark;

import javaslang.*;
import javaslang.collection.*;
import org.openjdk.jmh.results.*;

import java.text.DecimalFormat;
import java.util.Collection;

public class BenchmarkResultAggregator {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    public static void displayRatios(Collection<RunResult> runResultsCollection) {
        final Array<RunResult> runResults = Array.ofAll(runResultsCollection);

        if (runResults.size() > 1) {
            printHeader(runResults);
            for (Tuple2<String, Array<RunResult>> group : getGroupsBasedOnBenchmarkPrefix(runResults)) {
                final Multimap<Integer, Tuple2<String, Double>> results = aggregate(group._2);
                final Multimap<String, Double> ratios = getRatios(results);
                if (ratios.isNotEmpty()) {
                    System.out.println("Group '" + group._1 + "':");

                    final Seq<String> output = getFormattedRatios(ratios);
                    output.forEach(System.out::println);
                }
            }
        }
    }

    private static void printHeader(Array<RunResult> list) {
        if (list.isNotEmpty()) {
            final Array<Integer> containerSizes = list.map(r -> getContainerSize(r.getAggregatedResult())).distinct().sorted();
            final String header = formatList(containerSizes);
            System.out.println("\nRatios for: " + header);
        }
    }

    private static Map<String, Array<RunResult>> getGroupsBasedOnBenchmarkPrefix(Array<RunResult> runResults) {
        return runResults.groupBy(r -> {
            final String[] parts = r.getParams().getBenchmark().split("\\.");
            final String enclosingClassName = parts[parts.length - 2];
            return enclosingClassName;
        });
    }

    private static Multimap<Integer, Tuple2<String, Double>> aggregate(Array<RunResult> runResults) {
        Multimap<Integer, Tuple2<String, Double>> results = HashMultimap.withSeq().empty();
        for (RunResult runResult : runResults) {
            final BenchmarkResult benchmarkResult = runResult.getAggregatedResult();
            results = results.put(getContainerSize(benchmarkResult), getPrimaryResult(benchmarkResult));
        }
        return results;
    }

    private static Integer getContainerSize(BenchmarkResult benchmarkResult) {
        return Integer.parseInt(benchmarkResult.getParams().getParam("CONTAINER_SIZE"));
    }

    private static Tuple2<String, Double> getPrimaryResult(BenchmarkResult benchmarkResult) {
        final Result primaryResult = benchmarkResult.getPrimaryResult();
        return Tuple.of(primaryResult.getLabel(), primaryResult.getScore());
    }

    private static Multimap<String, Double> getRatios(Multimap<Integer, Tuple2<String, Double>> results) {
        Multimap<String, Double> ratios = HashMultimap.withSeq().empty();
        for (Integer size : results.keySet().toList().sorted()) {
            for (Array<Tuple2<String, Double>> pairs : results.get(size).get().toArray().combinations(2)) {
                final Tuple2<String, Double> first = pairs.get(0), second = pairs.get(1);
                ratios = ratios.put(formatBenchmarks(first, second), formatRatios(first, second));
            }
        }
        return ratios;
    }

    private static Array<String> getFormattedRatios(Multimap<String, Double> ratios) {
        final Array<String> sortedNames = ratios.keySet().toArray().sorted();
        final int padLength = sortedNames.map(String::length).max().get();
        final Array<String> output = sortedNames.map(
                name -> {
                    final CharSeq benchmarks = CharSeq.of(name).padTo(padLength, ' ');
                    final String benchmarkSpeedRatios = formatList(ratios.get(name).get().map(DECIMAL_FORMAT::format));
                    return benchmarks + ": " + benchmarkSpeedRatios;
                }
        );
        return output;
    }

    private static String formatBenchmarks(Tuple2<String, Double> first, Tuple2<String, Double> second) {
        return first._1 + "/" + second._1;
    }

    private static double formatRatios(Tuple2<String, Double> first, Tuple2<String, Double> second) {
        return first._2 / second._2;
    }

    private static String formatList(Traversable<?> elements) {
        return elements.mkString("[", ", ", "]");
    }
}