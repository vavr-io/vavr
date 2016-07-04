package javaslang;

import javaslang.collection.*;

import java.text.DecimalFormat;
import java.util.Comparator;

import static com.carrotsearch.sizeof.RamUsageEstimator.*;

public class MemoryUsage {
    private static final DecimalFormat RATIO_FORMAT = new DecimalFormat("#0.0");
    private static TreeMultimap<Integer, String> memoryUsages = TreeMultimap.withSeq().empty(Comparator.reverseOrder());

    /** Calculate the occupied memory of different internals */
    static void printAndReset() {
        for (int size : memoryUsages.keySet()) {
            System.out.println(String.format("\nfor %d elements", size));
            for (String usages : memoryUsages.get(size).get()) {
                System.out.println("\t" + usages);
            }
        }

        memoryUsages = memoryUsages.take(0); // reset
    }

    public static void storeMemoryUsages(Object source, int elementCount, Object target) {
        final long overhead = sizeOf(target) - sizeOf(source);
        final double overheadPerElement = overhead / (double) elementCount;
        final String usage = String.format("`%s` uses `%s` (`%s` overhead, `%s` bytes overhead per element)",
                                           target.getClass().getName(),
                                           humanSizeOf(target),
                                           humanReadableUnits(overhead),
                                           RATIO_FORMAT.format(overheadPerElement));
        if (!memoryUsages.get(elementCount).getOrElse(List::empty).contains(usage)) {
            memoryUsages = memoryUsages.put(elementCount, usage);
        }
    }
}