package javaslang;

import com.carrotsearch.sizeof.RamUsageEstimator;
import javaslang.collection.*;

import java.lang.reflect.Field;

public class Memory {
    private static Map<Integer, Array<String>> memoryUsages = TreeMap.empty();

    /** Calculate the occupied memory of different internals */
    static void printMemoryUsages() {
        for (Tuple2<Integer, Array<String>> memoryUsage : memoryUsages) {
            System.out.println(String.format("\nfor %d elements", memoryUsage._1));
            for (String usages : memoryUsage._2) {
                System.out.println("\t" + usages);
            }
        }
    }

    public static void storeMemoryUsages(Object[] source, Object parentInstance, Object... targets) {
        final int key = source.length;
        if (!memoryUsages.containsKey(key)) {
            Array<String> usages = Array.empty();
            for (Object target : targets) {
                final long overhead = RamUsageEstimator.sizeOf(target) - RamUsageEstimator.sizeOf(source);
                final long overheadPerElement = overhead / source.length;
                final String usage = String.format("%s uses %s (%s overhead, %s overhead per element)",
                        getFieldName(parentInstance, target),
                        RamUsageEstimator.humanSizeOf(target),
                        RamUsageEstimator.humanReadableUnits(overhead),
                        RamUsageEstimator.humanReadableUnits(overheadPerElement));
                usages = usages.append(usage);
            }
            memoryUsages = memoryUsages.put(key, usages);
        }
    }

    private static String getFieldName(Object parentInstance, Object fieldInstance) {
        try {
            for (Field field : parentInstance.getClass().getFields()) {
                if (fieldInstance == field.get(parentInstance)) {
                    return field.getName();
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        throw new IllegalStateException("Not found: " + fieldInstance + " in " + parentInstance);
    }
}