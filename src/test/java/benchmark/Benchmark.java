/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package benchmark;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class Benchmark {

	private Benchmark() {
	}

	public static <T> void bench(String name, int count, int warmup, T seed, BiFunction<Integer, T, T> calculation) {
		System.out.printf("%s bench(%s) took %s sec.\n", name, count, run(count, warmup, seed, calculation) / 1000.0d);
	}

	public static <T> void bench(String name, int count, int warmup, Runnable unit) {
		bench(name, count, warmup, null, (i, ignored) -> {
			unit.run();
			return null;
		});
	}

	public static <T> void bench(String name, int count, int warmup, Consumer<Integer> consumer) {
		bench(name, count, warmup, null, (i, ignored) -> {
			consumer.accept(i);
			return null;
		});
	}

	private static <T> long run(int count, int warmup, T seed, BiFunction<Integer, T, T> calculation) {
		T value = seed;
		for (int i = 0; i < warmup; i++) {
			value = calculation.apply(i, value);
		}
		final long start = System.currentTimeMillis();
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
