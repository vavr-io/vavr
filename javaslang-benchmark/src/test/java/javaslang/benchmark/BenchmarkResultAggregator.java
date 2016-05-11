package javaslang.benchmark;

import javaslang.*;
import javaslang.collection.*;
import javaslang.collection.List;
import javaslang.collection.Map;
import org.intellij.lang.annotations.RegExp;
import org.openjdk.jmh.results.*;

import java.text.DecimalFormat;
import java.util.*;

public class BenchmarkResultAggregator {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    public static void displayRatios(Collection<RunResult> runResultsCollection, @RegExp String resultFilter) {
        final Seq<RunResult> runResults = List.ofAll(runResultsCollection);
        final Map<String, ? extends Seq<RunResult>> groups = getGroupsBasedOnBenchmarkPrefix(runResults);

        for (Tuple2<String, ? extends Seq<RunResult>> group : groups) {
            final Multimap<String, Tuple2<String, Double>> containerSizeGroups = groupContainerSizes(group._2);
            final Seq<String> containerSizes = containerSizeGroups.keySet().toList().sorted();
            final Multimap<Tuple2<String, String>, Double> ratios = allRatios(containerSizes, containerSizeGroups);
            final Seq<Seq<String>> resultingLines = resultingLines(ratios, resultFilter);
            final Seq<Seq<CharSeq>> linesWithHeader = resultingLines.prepend(List.of(String.format("Group '%s'", group._1)).appendAll(containerSizes)).map(l -> l.map(CharSeq::of));

            final Seq<Integer> columnWidths = List.range(0, linesWithHeader.head().size()).map(column -> linesWithHeader.map(row -> row.get(column).size()).max().get());

            final Seq<String> stringLines = linesWithHeader.map(line ->
                    line.zipWithIndex().foldLeft("", (string, textAndPaddingIndex) -> {
                        final CharSeq text = textAndPaddingIndex._1;
                        final int index = textAndPaddingIndex._2.intValue();
                        final int padding = columnWidths.get(index);
                        if (index == 0) {
                            return string + text.padTo(padding, ' ') + ": ";
                        } else {
                            return string + text.leftPadTo(padding, ' ') + " | ";
                        }
                    })
            );

            System.out.println("\n" + stringLines.head());
            for (String line : stringLines.tail()) {
                System.out.println(line);
            }
        }
    }

    private static Map<String, ? extends Seq<RunResult>> getGroupsBasedOnBenchmarkPrefix(Seq<RunResult> runResults) {
        return runResults.groupBy(r -> {
            final String[] parts = r.getParams().getBenchmark().split("\\.");
            final String enclosingClassName = parts[parts.length - 2];
            return enclosingClassName;
        });
    }

    private static Multimap<String, Tuple2<String, Double>> groupContainerSizes(Seq<RunResult> runResults) {
        Multimap<String, Tuple2<String, Double>> results = HashMultimap.withSeq().empty();
        for (RunResult runResult : runResults) {
            final BenchmarkResult benchmarkResult = runResult.getAggregatedResult();
            results = results.put(getContainerSize(benchmarkResult), getPrimaryResult(benchmarkResult));
        }
        return results;
    }

    private static String getContainerSize(BenchmarkResult benchmarkResult) {
        return benchmarkResult.getParams().getParam("CONTAINER_SIZE");
    }

    private static Tuple2<String, Double> getPrimaryResult(BenchmarkResult benchmarkResult) {
        final Result primaryResult = benchmarkResult.getPrimaryResult();
        return Tuple.of(primaryResult.getLabel(), primaryResult.getScore());
    }

    private static Multimap<Tuple2<String, String>, Double> allRatios(Seq<String> containerSizes, Multimap<String, Tuple2<String, Double>> results) {
        Multimap<Tuple2<String, String>, Double> ratios = HashMultimap.withSeq().empty();
        for (String size : containerSizes) {
            for (Seq<Tuple2<String, Double>> pairs : results.get(size).get().toList().combinations(2)) {
                final Tuple2<String, Double> first = pairs.get(0), second = pairs.get(1);
                ratios = ratios.put(Tuple.of(first._1, second._1), (first._2 / second._2));
                ratios = ratios.put(Tuple.of(second._1, first._1), (second._2 / first._2));
            }
        }
        return ratios;
    }

    private static Seq<Seq<String>> resultingLines(Multimap<Tuple2<String, String>, Double> ratiosMap, String resultFilter) {
        final Seq<Tuple2<String, String>> sortedNames = ratiosMap.keySet().toSortedSet(Comparator.<Tuple2<String, String>, String> comparing(Tuple2::_1).thenComparing(Tuple2::_2)).toList();
        return sortedNames.<Seq<String>> map(name -> {
            final Traversable<Double> ratios = ratiosMap.get(name).get();
            final Traversable<String> formattedRatios = ratios.map(r -> DECIMAL_FORMAT.format(r) + "x");
            return List.of(name._1 + " / " + name._2).appendAll(formattedRatios);
        }).filter(l -> l.head().matches(resultFilter));
    }
}