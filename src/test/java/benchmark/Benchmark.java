/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package benchmark;

import java.util.function.BiFunction;

public final class Benchmark {

    private Benchmark() {
    }

    public static <T> void bench(String name, int count, int warmup, T seed, BiFunction<Integer, T, T> calculation) {
        run(warmup, seed, calculation);
        System.out.printf("%s bench took %s sec.\n", name, run(count, seed, calculation) / 1000.0d);
    }

    private static <T> long run(int count, T seed, BiFunction<Integer, T, T> calculation) {
        final long start = System.currentTimeMillis();
        T value = seed;
        for (int i = 0; i < count; i++) {
            value = calculation.apply(i, value);
        }
        final long time = System.currentTimeMillis() - start;
        gc();
        return time;
    }

    private static void gc() {
        try {
            for (int i = 0; i < 2; i++) {
                System.gc();
                Thread.sleep(50);
            }
        } catch (InterruptedException x) {
            // nothin' to do
        }
    }
}
