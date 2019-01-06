/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr;

import io.vavr.collection.Array;
import io.vavr.collection.CharSeq;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.util.ListStatistics;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BenchmarkPerformanceReporter {
    private static final Comparator<String> TO_STRING_COMPARATOR = Comparator.comparing(String::length).thenComparing(Function.identity());
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00");
    private static final DecimalFormat PERFORMANCE_FORMAT = new DecimalFormat("#0.00");
    private static final DecimalFormat PCT_FORMAT = new DecimalFormat("0.00%");

    private final Array<String> includeNames;
    private final Array<String> benchmarkClasses;
    private final Array<RunResult> runResults;
    private final String targetImplementation;
    private final double outlierLowPct;
    private final double outlierHighPct;

    public static BenchmarkPerformanceReporter of(Array<String> includeNames, Array<String> benchmarkClasses, Array<RunResult> runResults) {
        return of(includeNames, benchmarkClasses, runResults, "vavr", 0.3, 0.05);
    }

    public static BenchmarkPerformanceReporter of(Array<String> includeNames, Array<String> benchmarkClasses, Array<RunResult> runResults, String targetImplementation, double outlierLowPct, double outlierHighPct) {
        return new BenchmarkPerformanceReporter(includeNames, benchmarkClasses, runResults, targetImplementation, outlierLowPct, outlierHighPct);
    }

    /**
     * This class prints performance reports about the execution of individual tests, comparing their performance
     * against other implementations as required.
     *
     * @param benchmarkClasses     The benchmarked source class names
     * @param runResults           The results
     * @param targetImplementation The target implementation we want to focus on in the Ratio report.
     *                             It is case insensitive. If we enter "vavr", it will match "VaVr" and "va_vr".
     * @param outlierLowPct        The percentage of samples on the lower end that will be ignored from the statistics
     * @param outlierHighPct       The percentage of samples on the higher end that will be ignored from the statistics
     */
    private BenchmarkPerformanceReporter(Array<String> includeNames, Array<String> benchmarkClasses, Array<RunResult> runResults, String targetImplementation, double outlierLowPct, double outlierHighPct) {
        this.includeNames = includeNames;
        this.benchmarkClasses = benchmarkClasses;
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
     * <li>Group</li>
     * <li>Test Name</li>
     * <li>Implementation - tests can have different implementations, e.g. Scala, Java, Vavr</li>
     * <li>Parameters</li>
     * <li>Score</li>
     * <li>Error - 99% confidence interval expressed in % of the Score</li>
     * <li>Unit - units for the Score</li>
     * <li>Alternative implementations - compares performance of this test against alternative implementations</li>
     * </ul>
     */
    public void printDetailedPerformanceReport() {
        final Array<TestExecution> results = mapToTestExecutions();
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
     * <li>Group</li>
     * <li>Test Name</li>
     * <li>Ratio - A/B means implementation A is compared against base implementation B</li>
     * <li>Results - How many times faster implementation A is compared with B</li>
     * </ul>
     */
    public void printRatioPerformanceReport() {
        final Array<TestExecution> results = mapToTestExecutions();
        if (results.isEmpty()) {
            return;
        }
        new RatioPerformanceReport(results, targetImplementation).print();
    }

    private Array<TestExecution> mapToTestExecutions() {
        Array<TestExecution> executions = Array.empty();
        for (RunResult runResult : runResults) {
            executions = executions.append(TestExecution.of(runResult.getAggregatedResult(), outlierLowPct, outlierHighPct));
        }
        return sort(executions, includeNames);
    }

    private Array<TestExecution> sort(Array<TestExecution> results, Array<String> includeNames) {
        final Comparator<TestExecution> comparator = Comparator
                .<TestExecution, Integer> comparing(t -> benchmarkClasses.indexWhere(c -> c.endsWith(t.getOperation())))
                .thenComparing(t -> includeNames.indexWhere(i -> t.getImplementation().startsWith(i)));
        return results.sorted(comparator);
    }

    private String padLeft(String str, int size) {
        return str + CharSeq.repeat(' ', size - str.length());
    }

    private String padRight(String str, int size) {
        return CharSeq.repeat(' ', size - str.length()) + str;
    }

    private class DetailedPerformanceReport {
        private final Array<TestExecution> results;
        private final Map<String, Array<TestExecution>> resultsByKey;
        private final int paramKeySize;
        private final int groupSize;
        private final int nameSize;
        private final int implSize;
        private final int countSize;
        private final int scoreSize;
        private final int errorSize;
        private final int unitSize;
        private final Array<String> alternativeImplementations;

        public DetailedPerformanceReport(Array<TestExecution> results) {
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

            alternativeImplementations = results.map(TestExecution::getImplementation).distinct();
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

        private String calculatePerformanceStr(TestExecution result, Array<String> alternativeImplementations, Map<String, Array<TestExecution>> resultsByKey) {
            final String aggregateKey = result.getTestNameParamKey();
            final Array<TestExecution> alternativeResults = resultsByKey.get(aggregateKey).getOrElse(Array::empty);

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
            return ratio == 1.0 ? "" : PERFORMANCE_FORMAT.format(ratio) + "×";
        }
    }

    private class RatioPerformanceReport {
        private final Map<String, Array<TestExecution>> resultsByKey;
        private final int groupSize;
        private final int nameSize;
        private final Array<String> paramKeys;
        private final int paramKeySize;
        private final Array<String> alternativeImplementations;
        private final int alternativeImplSize;
        private final int ratioSize;
        private final Array<String> targetImplementations;
        private final String targetImplementation;

        public RatioPerformanceReport(Array<TestExecution> results, String targetImplementation) {
            this.targetImplementation = targetImplementation;

            resultsByKey = results.groupBy(TestExecution::getTestNameKey);
            groupSize = Math.max(results.map(r -> r.getTarget().length()).max().get(), 10);
            nameSize = Math.max(results.map(r -> r.getOperation().length()).max().get(), 9);

            paramKeys = results.map(TestExecution::getParamKey).distinct().sorted(TO_STRING_COMPARATOR);
            paramKeySize = Math.max(results.map(r -> r.getParamKey().length()).max().get(), 8);

            alternativeImplementations = results.map(TestExecution::getImplementation).distinct();
            targetImplementations = alternativeImplementations.filter(i -> i.toLowerCase().contains(targetImplementation.toLowerCase()));
            alternativeImplSize = Math.max(alternativeImplementations.map(String::length).max().getOrElse(0), 10);

            final int targetImplSize = Math.max(targetImplementations.map(String::length).max().getOrElse(0), 10);
            ratioSize = Math.max(targetImplSize + 1 + alternativeImplSize, 10);
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

            printTargetInDenominator();
            printTargetInNumerator();

            System.out.println("\n");
        }

        @SuppressWarnings("Convert2MethodRef")
        private void printTargetInNumerator() {
            System.out.println(String.format("\nRatios %s / <alternative_impl>", targetImplementation));
            System.out.println(ratioHeaderNumerator());
            for (String targetImpl : targetImplementations) {
                for (Tuple2<String, Array<TestExecution>> execution : resultsByKey) {
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
                for (Tuple2<String, Array<TestExecution>> execution : resultsByKey) {
                    printRatioForBaseType(targetImpl, execution._2,
                            (baseImpl, alternativeImpl) -> padRight(String.format("%s/%s", alternativeImpl, baseImpl), ratioSize),
                            (baseExec, alternativeExec) -> calculateRatios(alternativeExec, baseExec));
                }
            }
        }

        private void printRatioForBaseType(String baseType, Array<TestExecution> testExecutions,
                BiFunction<String, String, String> ratioNamePrinter,
                BiFunction<Array<TestExecution>, Array<TestExecution>, String> ratioCalculator) {
            final Array<TestExecution> baseImplExecutions = testExecutions.filter(e -> e.getImplementation().equals(baseType));
            if (baseImplExecutions.isEmpty()) {
                return;
            }
            final TestExecution baseTypeExecution = baseImplExecutions.head();

            for (String alternativeImpl : alternativeImplementations) {
                if (alternativeImpl.equals(baseType)) {
                    continue;
                }
                final Array<TestExecution> alternativeExecutions = testExecutions.filter(e -> e.getImplementation().equals(alternativeImpl));
                if (alternativeExecutions.isEmpty()) {
                    continue;
                }
                System.out.println(String.format("%s  %s  %s  %s",
                        padLeft(baseTypeExecution.getTarget(), groupSize),
                        padLeft(baseTypeExecution.getOperation(), nameSize),
                        ratioNamePrinter.apply(baseType, alternativeImpl),
                        ratioCalculator.apply(baseImplExecutions, alternativeExecutions)));
            }
            System.out.println();
        }

        private String calculateRatios(Array<TestExecution> alternativeExecutions, Array<TestExecution> baseImplExecutions) {
            Array<String> ratioStings = Array.empty();
            for (String paramKey : paramKeys) {
                final Option<TestExecution> alternativeExecution = alternativeExecutions.find(e -> e.getParamKey().equals(paramKey));
                final Option<TestExecution> baseExecution = baseImplExecutions.find(e -> e.getParamKey().equals(paramKey));
                final String paramRatio = alternativeExecution.isEmpty() || baseExecution.isEmpty() || baseExecution.get().getScore() == 0.0
                                          ? ""
                                          : PERFORMANCE_FORMAT.format(alternativeExecution.get().getScore() / baseExecution.get().getScore()) + "×";
                ratioStings = ratioStings.append(padRight(paramRatio, paramKeySize));
            }
            return ratioStings.mkString(" ");
        }
    }

    public static class TestExecution implements Comparable<TestExecution> {
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
            Array<Double> results = benchmark.getIterationResults().stream()
                    .map(r -> r.getPrimaryResult().getScore())
                    .collect(Array.collector());
            final int size = results.size();
            final int outliersLow = (int) (size * outlierLowPct);
            final int outliersHigh = (int) (size * outlierHighPct);
            results = results.drop(outliersLow).dropRight(outliersHigh);
            return new ListStatistics(results.toJavaList().stream().mapToDouble(r -> r).toArray());
        }

        private String getParameterKey(BenchmarkResult benchmarkResult) {
            final BenchmarkParams params = benchmarkResult.getParams();
            return params.getParamsKeys().stream().map(params::getParam).collect(Collectors.joining(";"));
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
