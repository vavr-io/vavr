/* ____  ______________  ________________________  __________
 * \   \/   /      \   \/   /   __/   /      \   \/   /      \
 *  \______/___/\___\______/___/_____/___/\___\______/___/\___\
 *
 * The MIT License (MIT)
 *
 * Copyright 2024 Vavr, https://vavr.io
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.vavr.collection.euler;

import io.vavr.API;
import io.vavr.Function1;
import io.vavr.Tuple;
import io.vavr.collection.Iterator;
import io.vavr.collection.Stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Scanner;

import static io.vavr.API.$;

final class Utils {

    private Utils() {
    }

    static final Function1<Integer, BigInteger> MEMOIZED_FACTORIAL = Function1.of(Utils::factorial).memoized();

    static final Function1<Long, Boolean> MEMOIZED_IS_PRIME = Function1.of(Utils::isPrime).memoized();

    static Stream<BigInteger> fibonacci() {
        return Stream.of(BigInteger.ZERO, BigInteger.ONE).appendSelf(self -> self.zip(self.tail()).map(t -> t._1.add(t._2)));
    }

    static BigInteger factorial(int n) {
        return Stream.rangeClosed(1, n).map(BigInteger::valueOf).fold(BigInteger.ONE, BigInteger::multiply);
    }

    static Stream<Long> factors(long number) {
        return Stream.rangeClosed(1, (long) Math.sqrt(number))
                .filter(d -> number % d == 0)
                .flatMap(d -> Stream.of(d, number / d))
                .distinct();
    }

    static Stream<Long> divisors(long l) {
        return factors(l).filter((d) -> d < l);
    }

    static boolean isPrime(long val) {
        return API.Match(val).of(
                API.Case($(n -> n < 2L), false),
                API.Case($(2L), true),
                API.Case($(), n -> {
                    final double upperLimitToCheck = Math.sqrt(n);
                    return !PrimeNumbers.primes().takeWhile(d -> d <= upperLimitToCheck).exists(d -> n % d == 0);
                })
        );
    }

    static Stream<String> readLines(File file) {
        try {
            return Stream.ofAll(new Iterator<String>() {

                final Scanner scanner = new Scanner(file);

                @Override
                public boolean hasNext() {
                    final boolean hasNext = scanner.hasNextLine();
                    if (!hasNext) {
                        scanner.close();
                    }
                    return hasNext;
                }

                @Override
                public String next() {
                    return scanner.nextLine();
                }
            });
        } catch (FileNotFoundException e) {
            return Stream.empty();
        }
    }

    static File file(String fileName) {
        final URL resource = Utils.class.getResource(fileName);
        if (resource == null) {
            throw new RuntimeException("resource not found");
        }
        return new File(resource.getFile());
    }

    static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static boolean isPalindrome(String val) {
        return val.equals(reverse(val));
    }

    static boolean isPalindrome(int val) {
        return isPalindrome(Long.toString(val));
    }

    static Stream<Long> pentagonal() {
        return Stream.of(Tuple.of(1L, 1)).appendSelf(self ->
                self.map(t ->
                        Tuple.of((t._2 + 1) * (3L * (t._2 + 1) - 1) / 2, t._2 + 1)))
                .map(t -> t._1);
    }

    static boolean isPentagonal(long number) {
        return ((1 + Math.sqrt(1 + 24 * number)) / 6) % 1 == 0;
    }
}
