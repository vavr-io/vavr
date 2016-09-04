package javaslang;

import javaslang.collection.CharSeq;
import javaslang.collection.LinkedHashMultimap;
import javaslang.collection.List;
import javaslang.collection.Multimap;

import static com.carrotsearch.sizeof.RamUsageEstimator.humanReadableUnits;
import static com.carrotsearch.sizeof.RamUsageEstimator.sizeOf;
import static javaslang.BenchmarkPerformanceReporter.DECIMAL_FORMAT;

public class MemoryUsage {
    private static Multimap<Integer, String> memoryUsages = LinkedHashMultimap.withSeq().empty(); // if forked, this will be reset every time

    /** Calculate the occupied memory of different internals */
    static void printAndReset() {
        for (int size : memoryUsages.keySet()) {
            System.out.println(String.format("\nfor `%d` elements", size));
            for (String usages : memoryUsages.get(size).get()) {
                System.out.println(usages);
            }
        }

        memoryUsages = memoryUsages.take(0); // reset
    }

    public static void storeMemoryUsages(int elementCount, Object target) {
        final CharSeq name = CharSeq.of(target.getClass().getPackage().getName()).splitSeq("\\.").map(CharSeq::head).mkCharSeq("", ".", "." + target.getClass().getSimpleName());
        final String usage = String.format("%s  → %s bytes (%s)",
                name.padTo(32, ' '),
                CharSeq.of(DECIMAL_FORMAT.format(sizeOf(target))).leftPadTo(15, ' '),
                humanReadableUnits(sizeOf(target)));
        if (!memoryUsages.get(elementCount).getOrElse(List::empty).contains(usage)) {
            memoryUsages = memoryUsages.put(elementCount, usage);
        }
    }
}