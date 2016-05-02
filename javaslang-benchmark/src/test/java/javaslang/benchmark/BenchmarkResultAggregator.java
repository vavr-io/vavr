package javaslang.benchmark;

import javaslang.*;
import javaslang.collection.*;
import org.openjdk.jmh.results.*;

import java.text.DecimalFormat;
import java.util.Collection;

public class BenchmarkResultAggregator {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    public static void displayRatios(Collection<RunResult> runResults) {
        final Multimap<Integer, Tuple2<String, Double>> results = aggregate(runResults);
        final Multimap<String, Double> ratios = getRatios(results);
        if (!ratios.isEmpty()) { // TODO ratios.isNotEmpty()
            final Seq<String> output = getFormattedRatios(results, ratios);

            output.forEach(System.out::println);
        }
    }

    private static Multimap<Integer, Tuple2<String, Double>> aggregate(Collection<RunResult> runResults) {
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
            for (List<Tuple2<String, Double>> pairs : results.get(size).get().toList().combinations(2)) {
                final Tuple2<String, Double> first = pairs.get(0), second = pairs.get(1);
                ratios = ratios.put(formatBenchmarks(first, second), formatRatios(first, second));
            }
        }
        return ratios;
    }

    private static List<String> getFormattedRatios(Multimap<Integer, Tuple2<String, Double>> results, Multimap<String, Double> ratios) {
        final List<String> sortedNames = ratios.keySet().toList().sorted();
        final int padLength = sortedNames.map(String::length).max().get();
        final List<String> output = sortedNames.map(
                name -> {
                    final CharSeq benchmarks = CharSeq.of(name).padTo(padLength, ' ');
                    final String benchmarkSpeedRatios = formatList(ratios.get(name).get().map(DECIMAL_FORMAT::format));
                    return benchmarks + ": " + benchmarkSpeedRatios;
                }
        );
        final String header = formatList(results.keySet().toList().sorted());
        return output.prepend("\nRatios for: " + header);
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