package javaslang.collection.euler;

import javaslang.collection.Stream;

public enum Sieve {

    INSTANCE(2_000_000);

    private final long[] primes;

    Sieve(int limit) {
        primes = calculatePrimes(limit);
    }

    public long[] array() {
        return primes;
    }

    public Stream<Long> stream() {
        return Stream.ofAll(primes);
    }

    private static long[] calculatePrimes(int limit) {

        // init sieve
        final boolean[] sieve = new boolean[limit];
        for (int z = 0; z < limit; z++) {
            sieve[z] = false;
        }
        final int root = (int) Math.ceil(Math.sqrt(limit));
        for (int x = 1; x <= root; x++) {
            for (int y = 1; y <= root; y++) {
                int n = (4 * x * x) + (y * y);
                if (n <= limit && (n % 12 == 1 || n % 12 == 5)) {
                    sieve[n] ^= true;
                }
                n = (3 * x * x) + (y * y);
                if (n <= limit && n % 12 == 7) {
                    sieve[n] ^= true;
                }
                n = (3 * x * x) - (y * y);
                if (x > y && n <= limit && n % 12 == 11) {
                    sieve[n] ^= true;
                }
            }
        }
        for (int r = 5; r <= root; r++) {
            if (sieve[r]) {
                int step = r * r;
                for (int i = step; i < limit; i += step) {
                    sieve[i] = false;
                }
            }
        }

        // calculate primes
        final long[] primes = new long[limit];
        primes[0] = 2;
        primes[1] = 3;
        int index = 2;
        for (int p = 5; p < limit; p++) {
            if (sieve[p]) {
                primes[index++] = p;
            }
        }

        return primes;
    }
}
