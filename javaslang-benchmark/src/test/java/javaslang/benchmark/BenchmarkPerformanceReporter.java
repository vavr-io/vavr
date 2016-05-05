/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.benchmark;

import javaslang.Tuple2;
import javaslang.collection.Array;
import javaslang.collection.CharSeq;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.collection.TreeMap;
import javaslang.control.Option;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

public class BenchmarkPerformanceReporter {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.000");
    private static final DecimalFormat PERFORMANCE_FORMAT = new DecimalFormat("#0.00");
    private static final DecimalFormat PCT_FORMAT = new DecimalFormat("0.00%");
    private final Collection<RunResult> runResults;

    public static BenchmarkPerformanceReporter of(Collection<RunResult> runResults) {
        return new BenchmarkPerformanceReporter(runResults);
    }

    /**
     * This class prints performance reports about the execution of individual tests, comparing their performance
     * against other implementations as required.
     * <br>
     * A typical JMH test is configured as follows:
     * <pre>
     *    \@Benchmark
     *    \@Group("groupName")
     *    public Object testName_Slang() {
     *       ....
     *    }
     * </pre>
     * <br>
     * The method name is broken into two parts separated by an underscore:
     * <ul>
     *     <li>testName - the test name</li>
     *     <li>Implementation - the type of implementation</li>
     * </ul>
     * This class relies on this naming convention to identify different implementations of the same operation.
     *
     * @param runResults
     */
    private BenchmarkPerformanceReporter(Collection<RunResult> runResults) {
        this.runResults = runResults;
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
        printDetailedPerformanceExecutionReport(results);
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
    public void printPerformanceRatiosReport() {
        final List<TestExecution> results = mapToTestExecutions(runResults);
        if (results.isEmpty()) {
            return;
        }
        printPerformanceRatioReport(results);
    }

    private List<TestExecution> mapToTestExecutions(Collection<RunResult> runResults) {
        List<TestExecution> executions = List.empty();
        for (RunResult runResult : runResults) {
            executions = executions.pushAll(TestExecution.of(runResult.getAggregatedResult()));
        }
        return executions;
    }

    private void printDetailedPerformanceExecutionReport(List<TestExecution> results) {
        Map<String, List<TestExecution>> resultsByKey = results.groupBy(TestExecution::getTestNameParamKey);
        int paramKeySize = Math.max(results.map(r -> r.getParamKey().length()).max().get(), 10);
        int groupSize = Math.max(results.map(r -> r.getGroup().length()).max().get(), 10);
        int nameSize = Math.max(results.map(r -> r.getName().length()).max().get(), 10);
        int implSize = Math.max(results.map(r -> r.getImplementation().length()).max().get(), 10);
        int scoreSize = Math.max(results.map(r -> r.getScoreFormatted().length()).max().get(), 15);
        int errorSize = Math.max(results.map(r -> r.getScoreErrorPct().length()).max().get(), 10);
        int unitSize = Math.max(results.map(r -> r.getUnit().length()).max().get(), 7);

        List<String> alternativeImplementations = results.map(TestExecution::getImplementation).distinct().sorted();
        int alternativeImplSize = Math.max(alternativeImplementations.map(String::length).max().get(), 10);
        String alternativeImplHeader = alternativeImplementations.map(type -> padRight(type, alternativeImplSize)).mkString(" ");

        System.out.println("\n\n\nDetailed Performance Execution Report");
        System.out.println("  Error: ±99% confidence interval, expressed as % of Score");
        if (!alternativeImplementations.isEmpty()) {
            System.out.println(String.format("  %s: read current row implementation is x times faster than alternative implementation", alternativeImplementations.mkString(", ")));
        }
        System.out.println();
        System.out.println(String.format("%s  %s  %s  %s  %s  ±%s %s %s",
                padLeft("Group", groupSize),
                padLeft("Test Name", nameSize),
                padLeft("Impl", implSize),
                padRight("Params", paramKeySize),
                padRight("Score", scoreSize),
                padRight("Error", errorSize),
                padRight("Unit", unitSize),
                alternativeImplHeader
                ));
        for (TestExecution result : results) {

            System.out.println(String.format("%s  %s  %s  %s  %s  ±%s %s %s",
                    padLeft(result.getGroup(), groupSize),
                    padLeft(result.getName(), nameSize),
                    padLeft(result.getImplementation(), implSize),
                    padRight(result.getParamKey(), paramKeySize),
                    padRight(result.getScoreFormatted(), scoreSize),
                    padRight(result.getScoreErrorPct(), errorSize),
                    padRight(result.getUnit(), unitSize),
                    calculatePerformanceStr(result, alternativeImplementations, resultsByKey, alternativeImplSize)
                    ));
        }
        System.out.println("\n\n");
    }

    private String calculatePerformanceStr(TestExecution result, List<String> alternativeImplementations, Map<String, List<TestExecution>> resultsByKey, int alternativeImplSize) {
        final String aggregateKey = result.getTestNameParamKey();
        final List<TestExecution> alternativeResults = resultsByKey.get(aggregateKey).getOrElse(List::empty);
        List<Option<TestExecution>> map = alternativeImplementations.map(alternativeType -> alternativeResults.find(r -> alternativeType.equals(r.getImplementation())));
        map.removeAt(0);
        return alternativeImplementations.map(alternativeType -> alternativeResults.find(r -> alternativeType.equals(r.getImplementation())))
                .map(alternativeExecution -> alternativeExecution.isDefined() ? alternativeExecution.get().getScore() : 0.0)
                .map(alternativeScore -> alternativeScore == 0 ? 1.0 : (result.getScore() / alternativeScore))
                .map(performanceImprovement -> padRight(performanceImprovement == 1.0 ? "" : PERFORMANCE_FORMAT.format(performanceImprovement) + "x", alternativeImplSize))
                .mkString(" ");
    }

    private void printPerformanceRatioReport(List<TestExecution> results) {
        TreeMap<String, List<TestExecution>> resultsByKey = TreeMap.ofEntries(results.groupBy(TestExecution::getTestNameKey));
        int groupSize = Math.max(results.map(r -> r.getGroup().length()).max().get(), 10);
        int nameSize = Math.max(results.map(r -> r.getName().length()).max().get(), 10);

        List<String> paramKeys = results.map(TestExecution::getParamKey).distinct().sorted();
        int paramKeySize = Math.max(results.map(r -> r.getParamKey().length()).max().get(), 10);
        String paramKeyHeader = paramKeys.map(type -> padRight(type, paramKeySize)).mkString(" ");

        List<String> alternativeImplementations = results.map(TestExecution::getImplementation).distinct().sorted();
        int alternativeImplSize = Math.max(alternativeImplementations.map(String::length).max().get(), 10);
        int ratioSize = Math.max(alternativeImplSize * 2 + 1, 10);

        System.out.println("\n\n\nPerformance Ratios");
        if (alternativeImplementations.size() < 2) {
            System.out.println("(nothing to report, you need at least two types)");
            return;
        }

        for (String baseImpl : alternativeImplementations) {
            System.out.println(String.format("\nRatios AlternativeImpl/%s", baseImpl));
            System.out.println(String.format("  (read as AlternativeImpl implementation is times faster than %s", baseImpl));
            System.out.println(String.format("  (e.g. 2x means AlternativeImpl is twice as fast as %s)", baseImpl));
            System.out.println();
            System.out.println(String.format("%s  %s  %s  %s ",
                    padLeft("Group", groupSize),
                    padLeft("Test Name", nameSize),
                    padRight("Ratio", ratioSize),
                    paramKeyHeader
                    ));
            for (Tuple2<String, List<TestExecution>> execution : resultsByKey) {
                printRatioForBaseType(baseImpl, execution._2, alternativeImplementations, paramKeys, groupSize, nameSize, ratioSize, paramKeySize);
            }
        }
        System.out.println("\n\n");
    }

    private void printRatioForBaseType(String baseType, List<TestExecution> testExecutions, List<String> alternativeImplementations, List<String> paramKeys,
                                       int groupSize, int nameSize, int ratioSize, int paramKeySize) {
        List<TestExecution> baseImplExecutions = testExecutions.filter(e -> e.getImplementation().equals(baseType));
        if (baseImplExecutions.isEmpty()) {
            return;
        }
        TestExecution baseTypeExecution = baseImplExecutions.head();

        for (String alternativeImpl : alternativeImplementations) {
            if (alternativeImpl.equals(baseType)) {
                continue;
            }
            List<TestExecution> alternativeExecutions = testExecutions.filter(e -> e.getImplementation().equals(alternativeImpl));
            if (alternativeExecutions.isEmpty()) {
                continue;
            }
            System.out.println(String.format("%s  %s  %s  %s ",
                    padLeft(baseTypeExecution.getGroup(), groupSize),
                    padLeft(baseTypeExecution.getName(), nameSize),
                    padRight(String.format("%s/%s", alternativeImpl, baseType), ratioSize),
                    calculateRatios(baseImplExecutions, alternativeExecutions, paramKeys, paramKeySize)
                    ));
        }
    }

    private String calculateRatios(List<TestExecution> baseImplExecutions, List<TestExecution> alternativeExecutions, List<String> paramKeys, int paramKeySize) {
        Array<String> ratioStrs = Array.empty();
        for (String paramKey : paramKeys) {
            Option<TestExecution> alternativeExecution = alternativeExecutions.find(e -> e.getParamKey().equals(paramKey));
            Option<TestExecution> baseExecution = baseImplExecutions.find(e -> e.getParamKey().equals(paramKey));
            String paramRatio = alternativeExecution.isEmpty() || baseExecution.isEmpty() || baseExecution.get().getScore() == 0.0
                    ? ""
                    : PERFORMANCE_FORMAT.format(alternativeExecution.get().getScore() / baseExecution.get().getScore()) + "x";
            ratioStrs = ratioStrs.append(padRight(paramRatio, paramKeySize));
        }
        return ratioStrs.mkString(" ");
    }

    private String padLeft(String str, int size) {
        return str + CharSeq.repeat(' ', size - str.length());
    }

    private String padRight(String str, int size) {
        return CharSeq.repeat(' ', size - str.length()) + str;
    }

    static class TestExecution implements Comparable<TestExecution> {
        private final String paramKey;
        private final String fullName;
        private final String group;
        private final String name;
        private final String implementation;
        private final double score;
        private final double scoreError;
        private final String unit;

        public static List<TestExecution> of(BenchmarkResult benchmarkResult) {
            // As dump as it seems using keySet().stream() instead of entrySet().stream(),
            // I cannot use the latter as the -Werror compiler flag will raise compile error
            // due to the "missing type arguments for generic class" warning when using Result class
            // without a generic param
            return benchmarkResult.getSecondaryResults().keySet().stream()
                    .map(k -> new TestExecution(benchmarkResult, k, benchmarkResult.getSecondaryResults().get(k)))
                    .collect(List.collector())
                    .sorted();
        }

        public TestExecution(BenchmarkResult benchmark, String fullTestName, Result<?> result) {
            paramKey = getParameterKey(benchmark);
            group = extractGroupName(benchmark.getParams().getBenchmark());
            fullName = fullTestName;
            name = extractTestName(fullName);
            implementation = extractType(fullName);
            score = result.getScore();
            scoreError = result.getScoreError();
            unit = result.getScoreUnit();
        }

        private String getParameterKey(BenchmarkResult benchmarkResult) {
            BenchmarkParams params = benchmarkResult.getParams();
            return params.getParamsKeys().stream().map(params::getParam).collect(Collectors.joining(","));
        }

        public String getTestNameParamKey() {
            return group + ":" + name + ":" + unit + ":" + paramKey;
        }

        public String getTestNameKey() {
            return group + ":" + name + ":" + unit;
        }

        private String extractGroupName(String fullyQualifiedName) {
            return CharSeq.of(fullyQualifiedName).split("\\.").last().toString();
        }

        private String extractTestName(String fullName) {
            int separator = fullName.lastIndexOf('_');
            if (separator == -1) {
                return fullName;
            }
            return fullName.substring(0, separator);
        }

        private String extractType(String fullName) {
            int separator = fullName.lastIndexOf('_');
            if (separator == -1) {
                return "";
            }
            return fullName.substring(separator + 1);
        }

        public String getParamKey() {
            return paramKey;
        }

        public String getFullName() {
            return fullName;
        }

        public String getGroup() {
            return group;
        }

        public String getName() {
            return name;
        }

        public String getImplementation() {
            return implementation;
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
            return String.format("%s %s %s %s -> %s (± %s)", paramKey, group, name, implementation, getScoreFormatted(), getScoreErrorPct());
        }

        Comparator<TestExecution> comparator = Comparator
                .comparing(TestExecution::getUnit)
                .thenComparing(TestExecution::getGroup)
                .thenComparing(TestExecution::getParamKey)
                .thenComparing(TestExecution::getName)
                .thenComparing(TestExecution::getImplementation);
        @Override
        public int compareTo(TestExecution o) {
            return comparator.compare(this, o);
        }
    }
}