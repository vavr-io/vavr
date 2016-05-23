/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.benchmark;

import javaslang.Function2;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.TreeMap;
import javaslang.collection.Vector;
import javaslang.control.Option;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.util.ListStatistics;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BenchmarkPerformanceReporter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.000");
    private static final DecimalFormat PERFORMANCE_FORMAT = new DecimalFormat("#0.00");
    private static final DecimalFormat PCT_FORMAT = new DecimalFormat("0.00%");
    private final Collection<RunResult> runResults;
    private final String targetImplementation;
    private final double outlierLowPct;
    private final double outlierHighPct;

    public static BenchmarkPerformanceReporter of(Collection<RunResult> runResults) {
        return of(runResults, "slang", 0.3, 0.1);
    }

    public static BenchmarkPerformanceReporter of(Collection<RunResult> runResults, String targetImplementation, double outlierLowPct, double outlierHighPct) {
        return new BenchmarkPerformanceReporter(runResults, targetImplementation, outlierLowPct, outlierHighPct);
    }

    /**
     * This class prints performance reports about the execution of individual tests, comparing their performance
     * against other implementations as required.
     *
     * @param runResults           The results
     * @param targetImplementation The target implementation we want to focus on in the Ratio report.
     *                             It is case insensitive. If we enter "slang", it will match "JavaSlang" and "java_slang".
     * @param outlierLowPct        The percentage of samples on the lower end that will be ignored from the statistics
     * @param outlierHighPct       The percentage of samples on the higher end that will be ignored from the statistics
     */
    private BenchmarkPerformanceReporter(Collection<RunResult> runResults, String targetImplementation, double outlierLowPct, double outlierHighPct) {
        this.runResults = runResults;
        this.targetImplementation = targetImplementation;
        this.outlierLowPct = outlierLowPct;
        this.outlierHighPct = outlierHighPct;
    }

    /**
     * Prints all performance reports
     */
    public void print() {
        printDetailedPerformanceReport();
        printRatioPerformanceReport();
    }

    /**
     * Prints the detail performance report for each individual test.
     * <br>
     * For each test it prints out:
     * <ul>
     *     <li>Group</li>
     *     <li>Test Name</li>
     *     <li>Implementation - tests can have different implementations, e.g. Scala, Java, JavaSlang</li>
     *     <li>Parameters</li>
     *     <li>Score</li>
     *     <li>Error - 99% confidence interval expressed in % of the Score</li>
     *     <li>Unit - units for the Score</li>
     *     <li>Alternative implementations - compares performance of this test against alternative implementations</li>
     * </ul>
     *
     */
    public void printDetailedPerformanceReport() {
        final List<TestExecution> results = mapToTestExecutions(runResults);
        if (results.isEmpty()) {
            return;
        }
        new DetailedPerformanceReport(results).print();
    }

    /**
     * Prints the performance ratio report for each test, and compares the performance against different implementations
     * of the same operation.
     * <br>
     * For each test it prints out:
     * <ul>
     *     <li>Group</li>
     *     <li>Test Name</li>
     *     <li>Ratio - A/B means implementation A is compared against base implementation B</li>
     *     <li>Results - How many times faster implementation A is compared with B</li>
     * </ul>
     */
    public void printRatioPerformanceReport() {
        final List<TestExecution> results = mapToTestExecutions(runResults);
        if (results.isEmpty()) {
            return;
        }
        new RatioPerformanceReport(results, targetImplementation).print();
    }

    private List<TestExecution> mapToTestExecutions(Collection<RunResult> runResults) {
        List<TestExecution> executions = List.empty();
        for (RunResult runResult : runResults) {
            executions = executions.push(TestExecution.of(runResult.getAggregatedResult(), outlierLowPct, outlierHighPct));
        }
        return executions;
    }

    private String padLeft(String str, int size) {
        return str + CharSeq.repeat(' ', size - str.length());
    }

    private String padRight(String str, int size) {
        return CharSeq.repeat(' ', size - str.length()) + str;
    }

    private class DetailedPerformanceReport {
        private final List<TestExecution> results;
        private final Map<String, List<TestExecution>> resultsByKey;
        private final int paramKeySize;
        private final int groupSize;
        private final int nameSize;
        private final int implSize;
        private final int countSize;
        private final int scoreSize;
        private final int errorSize;
        private final int unitSize;
        private final List<String> alternativeImplementations;

        public DetailedPerformanceReport(List<TestExecution> results) {
            this.results = results;
            resultsByKey = results.groupBy(TestExecution::getTestNameParamKey);
            paramKeySize = Math.max(results.map(r -> r.getParamKey().length()).max().get(), 10);
            groupSize = Math.max(results.map(r -> r.getTarget().length()).max().get(), 10);
            nameSize = Math.max(results.map(r -> r.getOperation().length()).max().get(), 10);
            implSize = Math.max(results.map(r -> r.getImplementation().length()).max().get(), 10);
            countSize = Math.max(results.map(r -> Long.toString(r.getSampleCount()).length()).max().get(), 5);
            scoreSize = Math.max(results.map(r -> r.getScoreFormatted().length()).max().get(), 15);
            errorSize = Math.max(results.map(r -> r.getScoreErrorPct().length()).max().get(), 10);
            unitSize = Math.max(results.map(r -> r.getUnit().length()).max().get(), 7);

            alternativeImplementations = results.map(TestExecution::getImplementation).distinct().sorted();
        }

        public void print() {
            printHeader();
            printDetails();
        }

        private void printHeader() {
            final String alternativeImplHeader = alternativeImplementations.map(altImpl -> padRight(altImpl, altImplColSize(altImpl))).mkString("  ");
            final String header = String.format("%s  %s  %s  %s  %s  %s  ±%s %s  %s",
                    padLeft("Target", groupSize),
                    padLeft("Operation", nameSize),
                    padLeft("Impl", implSize),
                    padRight("Params", paramKeySize),
                    padRight("Count", countSize),
                    padRight("Score", scoreSize),
                    padRight("Error", errorSize),
                    padRight("Unit", unitSize),
                    alternativeImplHeader
            );

            System.out.println("\n\n\n");
            System.out.println("Detailed Performance Execution Report");
            System.out.println(CharSeq.of("=").repeat(header.length()));
            System.out.println("  (Error: ±99% confidence interval, expressed as % of Score)");
            if (outlierLowPct > 0.0 && outlierHighPct > 0.0) {
                System.out.println(String.format("  (Outliers removed: %s low end, %s high end)", PCT_FORMAT.format(outlierLowPct), PCT_FORMAT.format(outlierHighPct)));
            }
            if (!alternativeImplementations.isEmpty()) {
                System.out.println(String.format("  (%s: read as current row implementation is x times faster than alternative implementation)", alternativeImplementations.mkString(", ")));
            }
            System.out.println();
            System.out.println(header);
        }

        private void printDetails() {
            for (TestExecution result : results) {
                System.out.println(String.format("%s  %s  %s  %s  %s  %s  ±%s %s %s",
                        padLeft(result.getTarget(), groupSize),
                        padLeft(result.getOperation(), nameSize),
                        padLeft(result.getImplementation(), implSize),
                        padRight(result.getParamKey(), paramKeySize),
                        padRight(Long.toString(result.getSampleCount()), countSize),
                        padRight(result.getScoreFormatted(), scoreSize),
                        padRight(result.getScoreErrorPct(), errorSize),
                        padRight(result.getUnit(), unitSize),
                        calculatePerformanceStr(result, alternativeImplementations, resultsByKey)
                ));
            }
            System.out.println("\n");
        }

        private int altImplColSize(String name) {
            return Math.max(5, name.length());
        }

        private String calculatePerformanceStr(TestExecution result, List<String> alternativeImplementations, Map<String, List<TestExecution>> resultsByKey) {
            final String aggregateKey = result.getTestNameParamKey();
            final List<TestExecution> alternativeResults = resultsByKey.get(aggregateKey).getOrElse(List::empty);

            return alternativeImplementations.map(altImpl -> Tuple.of(altImpl, alternativeResults.find(r -> altImpl.equals(r.getImplementation()))))
                    .map(alt -> Tuple.of(alt._1, calculateRatioStr(result, alt._2)))
                    .map(alt -> padRight(alt._2, altImplColSize(alt._1)))
                    .mkString("  ");
        }

        private String calculateRatioStr(TestExecution baseResult, Option<TestExecution> alternativeResult) {
            if (!alternativeResult.isDefined()) {
                return "";
            }
            final double alternativeScore = alternativeResult.get().getScore();
            if (alternativeScore == 0.0) {
                return "";
            }
            final double ratio = baseResult.getScore() / alternativeScore;
            return ratio == 1.0 ? "" : PERFORMANCE_FORMAT.format(ratio) + "x";
        }
    }

    private class RatioPerformanceReport {
        private final TreeMap<String, List<TestExecution>> resultsByKey;
        private final int groupSize;
        private final int nameSize;
        private final List<String> paramKeys;
        private final int paramKeySize;
        private final List<String> alternativeImplementations;
        private final int alternativeImplSize;
        private final int ratioSize;
        private final List<String> targetImplementations;
        private final String targetImplementation;

        public RatioPerformanceReport(List<TestExecution> results, String targetImplementation) {
            this.targetImplementation = targetImplementation;

            resultsByKey = TreeMap.ofEntries(results.groupBy(TestExecution::getTestNameKey));
            groupSize = Math.max(results.map(r -> r.getTarget().length()).max().get(), 10);
            nameSize = Math.max(results.map(r -> r.getOperation().length()).max().get(), 10);

            paramKeys = results.map(TestExecution::getParamKey).distinct().sorted();
            paramKeySize = Math.max(results.map(r -> r.getParamKey().length()).max().get(), 10);

            alternativeImplementations = results.map(TestExecution::getImplementation).distinct().sorted();
            targetImplementations = alternativeImplementations.filter(i -> i.toLowerCase().contains(targetImplementation.toLowerCase()));
            alternativeImplSize = Math.max(alternativeImplementations.map(String::length).max().get(), 10);
            ratioSize = Math.max(alternativeImplSize * 2 + 1, 10);
        }

        public void print() {
            printHeader();
            printReport();
        }

        private void printHeader() {
            System.out.println("\n\n");
            System.out.println("Performance Ratios");
            System.out.println(CharSeq.of("=").repeat(ratioHeaderNumerator().length()));
            if (outlierLowPct > 0.0 && outlierHighPct > 0.0) {
                System.out.println(String.format("  (Outliers removed: %s low end, %s high end)", PCT_FORMAT.format(outlierLowPct), PCT_FORMAT.format(outlierHighPct)));
            }
        }

        private String ratioHeaderNumerator() {
            final String paramKeyHeader = paramKeys.map(type -> padRight(type, paramKeySize)).mkString(" ");
            return String.format("%s  %s  %s  %s ",
                    padLeft("Target", groupSize),
                    padLeft("Operation", nameSize),
                    padLeft("Ratio", ratioSize),
                    paramKeyHeader
            );
        }

        private String ratioHeaderDenominator() {
            final String paramKeyHeader = paramKeys.map(type -> padRight(type, paramKeySize)).mkString(" ");
            return String.format("%s  %s  %s  %s ",
                    padLeft("Target", groupSize),
                    padLeft("Operation", nameSize),
                    padRight("Ratio", ratioSize),
                    paramKeyHeader
            );
        }

        private void printReport() {
            if (alternativeImplementations.size() < 2) {
                System.out.println("(nothing to report, you need at least two different implementation)");
                return;
            }

            printTargetInNumerator();
            printTargetInDenominator();

            System.out.println("\n");
        }

        private void printTargetInNumerator() {
            System.out.println(String.format("\nRatios %s / <alternative_impl>", targetImplementation));
            System.out.println(ratioHeaderNumerator());
            for (String targetImpl : targetImplementations) {
                for (Tuple2<String, List<TestExecution>> execution : resultsByKey) {
                    printRatioForBaseType(targetImpl, execution._2,
                                                (baseImpl, alternativeImpl) -> padLeft(String.format("%s/%s", baseImpl, alternativeImpl), ratioSize),
                                                (baseExec, alternativeExec) -> calculateRatios(baseExec, alternativeExec));
                }
            }
        }

        private void printTargetInDenominator() {
            System.out.println(String.format("\nRatios <alternative_impl> / %s", targetImplementation));
            System.out.println(ratioHeaderDenominator());
            for (String targetImpl : targetImplementations) {
                for (Tuple2<String, List<TestExecution>> execution : resultsByKey) {
                    printRatioForBaseType(targetImpl, execution._2,
                            (baseImpl, alternativeImpl) -> padRight(String.format("%s/%s", alternativeImpl, baseImpl), ratioSize),
                            (baseExec, alternativeExec) -> calculateRatios(alternativeExec, baseExec));
                }
            }
        }

        private void printRatioForBaseType(String baseType, List<TestExecution> testExecutions,
                                           Function2<String, String, String> ratioNamePrinter,
                                           Function2<List<TestExecution>, List<TestExecution>, String> ratioCalculator) {
            final List<TestExecution> baseImplExecutions = testExecutions.filter(e -> e.getImplementation().equals(baseType));
            if (baseImplExecutions.isEmpty()) {
                return;
            }
            final TestExecution baseTypeExecution = baseImplExecutions.head();

            for (String alternativeImpl : alternativeImplementations) {
                if (alternativeImpl.equals(baseType)) {
                    continue;
                }
                final List<TestExecution> alternativeExecutions = testExecutions.filter(e -> e.getImplementation().equals(alternativeImpl));
                if (alternativeExecutions.isEmpty()) {
                    continue;
                }
                System.out.println(String.format("%s  %s  %s  %s ",
                        padLeft(baseTypeExecution.getTarget(), groupSize),
                        padLeft(baseTypeExecution.getOperation(), nameSize),
                        ratioNamePrinter.apply(baseType, alternativeImpl),
                        ratioCalculator.apply(baseImplExecutions, alternativeExecutions)));
            }
        }

        private String calculateRatios(List<TestExecution> alternativeExecutions, List<TestExecution> baseImplExecutions) {
            Array<String> ratioStrs = Array.empty();
            for (String paramKey : paramKeys) {
                final Option<TestExecution> alternativeExecution = alternativeExecutions.find(e -> e.getParamKey().equals(paramKey));
                final Option<TestExecution> baseExecution = baseImplExecutions.find(e -> e.getParamKey().equals(paramKey));
                final String paramRatio = alternativeExecution.isEmpty() || baseExecution.isEmpty() || baseExecution.get().getScore() == 0.0
                        ? ""
                        : PERFORMANCE_FORMAT.format(alternativeExecution.get().getScore() / baseExecution.get().getScore()) + "x";
                ratioStrs = ratioStrs.append(padRight(paramRatio, paramKeySize));
            }
            return ratioStrs.mkString(" ");
        }
    }

    static class TestExecution implements Comparable<TestExecution> {
        private static double outlierLowPct;
        private static double outlierHighPct;
        private final String paramKey;
        private final String fullName;
        private final String target;
        private final String operation;
        private final String implementation;
        private final long sampleCount;
        private final double score;
        private final double scoreError;
        private final String unit;

        public static TestExecution of(BenchmarkResult benchmarkResult, double outlierLowPct, double outlierHighPct) {
            TestExecution.outlierLowPct = outlierLowPct;
            TestExecution.outlierHighPct = outlierHighPct;
            return new TestExecution(benchmarkResult);
        }

        public TestExecution(BenchmarkResult benchmark) {
            final Result<?> primaryResult = benchmark.getPrimaryResult();
            fullName = benchmark.getParams().getBenchmark();
            target = extractPart(fullName, 2);
            operation = extractPart(fullName, 1);
            implementation = extractPart(fullName, 0);
            paramKey = getParameterKey(benchmark);

            final ListStatistics statistics = createStatisticsWithoutOutliers(benchmark, outlierLowPct, outlierHighPct);
            sampleCount = statistics.getN();
            score = statistics.getMean();
            scoreError = statistics.getMeanErrorAt(0.999);
            unit = primaryResult.getScoreUnit();
        }

        private ListStatistics createStatisticsWithoutOutliers(BenchmarkResult benchmark, double outlierLowPct, double outlierHighPct) {
            Vector<Double> results = benchmark.getIterationResults().stream()
                    .map(r -> r.getPrimaryResult().getScore())
                    .sorted()
                    .collect(Vector.collector());
            final int size = results.size();
            final int outliersLow = (int) (size * outlierLowPct);
            final int outliersHigh = (int) (size * outlierHighPct);
            results = results.drop(outliersLow).dropRight(outliersHigh);
            return new ListStatistics(results.toJavaList().stream().mapToDouble(r -> r).toArray());
        }

        private String getParameterKey(BenchmarkResult benchmarkResult) {
            final BenchmarkParams params = benchmarkResult.getParams();
            return params.getParamsKeys().stream().map(params::getParam).collect(Collectors.joining(","));
        }

        public String getTestNameParamKey() {
            return target + ":" + operation + ":" + unit + ":" + paramKey;
        }

        public String getTestNameKey() {
            return target + ":" + operation + ":" + unit;
        }

        private String extractPart(String fullyQualifiedName, int indexFromLast) {
            final String[] parts = fullyQualifiedName.split("\\.");
            return parts.length > indexFromLast ? parts[parts.length - indexFromLast - 1] : "";
        }

        public String getParamKey() {
            return paramKey;
        }

        public String getTarget() {
            return target;
        }

        public String getOperation() {
            return operation;
        }

        public String getImplementation() {
            return implementation;
        }

        public long getSampleCount() {
            return sampleCount;
        }

        public double getScore() {
            return score;
        }

        public String getScoreFormatted() {
            return DECIMAL_FORMAT.format(score);
        }

        public String getScoreErrorPct() {
            return PCT_FORMAT.format(score == 0 ? 0 : scoreError / score);
        }

        public String getUnit() {
            return unit;
        }

        @Override
        public String toString() {
            return String.format("%s %s %s %s -> %s (± %s)", paramKey, target, operation, implementation, getScoreFormatted(), getScoreErrorPct());
        }

        Comparator<TestExecution> comparator = Comparator
                .comparing(TestExecution::getUnit)
                .thenComparing(TestExecution::getTarget)
                .thenComparing(TestExecution::getParamKey)
                .thenComparing(TestExecution::getOperation)
                .thenComparing(TestExecution::getImplementation);
        @Override
        public int compareTo(TestExecution o) {
            return comparator.compare(this, o);
        }
    }
}