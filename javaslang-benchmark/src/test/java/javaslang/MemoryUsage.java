package javaslang;

import javaslang.collection.*;

import java.text.DecimalFormat;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.carrotsearch.sizeof.RamUsageEstimator.humanReadableUnits;
import static com.carrotsearch.sizeof.RamUsageEstimator.sizeOf;
import static java.lang.Math.max;

public class MemoryUsage {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0");
    private static Map<Integer, LinkedHashSet<Seq<CharSeq>>> memoryUsages = TreeMap.empty(); // if forked, this will be reset every time

    /** Calculate the occupied memory of different internals */
    static void printAndReset() {
        for (Tuple2<Integer, LinkedHashSet<Seq<CharSeq>>> entry : memoryUsages) {
            final Seq<Integer> columnSizes = columnSizes(entry._1);
            System.out.println(String.format("\nfor `%d` elements", entry._1));
            for (Seq<CharSeq> stats : entry._2) {
                final String format = String.format("  %s → %s bytes - %s",
                        stats.get(0).padTo(columnSizes.get(0), ' '),
                        stats.get(1).leftPadTo(columnSizes.get(1), ' '),
                        stats.get(2).leftPadTo(columnSizes.get(2), ' ')
                );
                System.out.println(format);
            }
        }

        memoryUsages = memoryUsages.take(0); // reset
    }
    private static Seq<Integer> columnSizes(int size) {
        return memoryUsages.get(size)
                .map(rows -> rows.map(row -> row.map(CharSeq::length))).get()
                .reduce((row1, row2) -> row1.zip(row2).map(ts -> max(ts._1, ts._2)));
    }

    static void storeMemoryUsages(int elementCount, Object target) {
        memoryUsages = memoryUsages.put(elementCount, memoryUsages.get(elementCount).getOrElse(LinkedHashSet.empty()).add(Array.of(
                toHumanReadableName(target),
                FORMAT.format(sizeOf(target)),
                humanReadableUnits(sizeOf(target))
        ).map(CharSeq::of)));
    }

    private static HashMap<Predicate<String>, String> names = HashMap.ofEntries(
            Tuple.of("^java\\.", "Java mutable @ "),
            Tuple.of("^fj\\.", "Functional Java persistent @ "),
            Tuple.of("^org\\.pcollections", "PCollections persistent @ "),
            Tuple.of("^org\\.eclipse\\.collections", "Eclipse Collections persistent @ "),
            Tuple.of("^clojure\\.", "Clojure persistent @ "),
            Tuple.of("^scalaz\\.Heap", "Scalaz persistent @ "),
            Tuple.of("^scala\\.collection.immutable", "Scala persistent @ "),
            Tuple.of("^scala\\.collection.mutable", "Scala mutable @ "),
            Tuple.of("^javaslang\\.", "Javaslang persistent @ ")
    ).mapKeys(r -> Pattern.compile(r).asPredicate());
    private static String toHumanReadableName(Object target) {
        final Class<?> type = target.getClass();
        return prefix(type) + type.getSimpleName();
    }
    private static String prefix(Class<?> type) { return names.find(p -> p._1.test(type.getName())).get()._2; }
}