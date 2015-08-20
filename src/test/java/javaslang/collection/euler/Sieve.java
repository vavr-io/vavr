package javaslang.collection.euler;

import javaslang.collection.Iterator;
import javaslang.collection.Stream;

public class Sieve {

    public static final Sieve INSTANCE = new Sieve(2_000_000);

    public int count() {
        return nPrimes;
    }

    public int[] array() {
        return primes;
    }

    public Stream<Integer> streamOfIntegers() {
        return Stream.ofAll(() -> new Iterator<Integer>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < nPrimes;
                    }

                    @Override
                    public Integer next() {
                        return primes[index++];
                    }
                }
        );
    }

    public Stream<Long> streamOfLongs() {
        return Stream.ofAll(() -> new Iterator<Long>() {
                    int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < nPrimes;
                    }

                    @Override
                    public Long next() {
                        return (long) primes[index++];
                    }
                }
        );
    }

    private final int[] primes;
    private final int nPrimes;

    private Sieve(int limit) {
        boolean[] sieve = new boolean[limit];
        primes = new int[limit];
        nPrimes = makePrimes(sieve, primes);
    }

    private static int makePrimes(boolean[] sieve, int[] primes) {
        int limit = sieve.length;
        int root = (int) Math.ceil(Math.sqrt(limit));
        int nPrimes = 2;
        primes[0] = 2;
        primes[1] = 3;
        for (int z = 0; z < limit; z++) {
            sieve[z] = false;
        }
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
        for (int p = 5; p < limit; p++) {
            if (sieve[p]) {
                primes[nPrimes++] = p;
            }
        }
        return nPrimes;
    }
}
