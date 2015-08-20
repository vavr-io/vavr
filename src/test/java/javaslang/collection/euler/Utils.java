/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection.euler;

import javaslang.collection.Iterator;
import javaslang.collection.Stream;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Scanner;

public final class Utils {

    private Utils() {
    }

    public static Stream<Integer> fibonacci() {
        return Stream.of(1, 1).appendSelf(self -> self.zip(self.tail()).map(t -> t._1 + t._2));
    }

    public static BigInteger factorial(int n) {
        return Stream.rangeClosed(1, n).map(BigInteger::valueOf).fold(BigInteger.ONE, BigInteger::multiply);
    }

    public static Stream<String> readLines(File file) {
        final Scanner scanner;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            return Stream.empty();
        }
        return Stream.ofAll(new Iterator<String>() {
            @Override
            public boolean hasNext() {
                return scanner.hasNextLine();
            }

            @Override
            public String next() {
                return scanner.nextLine();
            }
        });
    }

    public static File file(String fileName) {
        final URL resource = Utils.class.getClassLoader().getResource(fileName);
        if(resource == null) {
            throw new RuntimeException("resource not found");
        }
        return new File(resource.getFile());
    }

    public static String reverse(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public static boolean isPalindrome(String val) {
        return val.equals(reverse(val));
    }

    public static boolean isPalindrome(int val) {
        return isPalindrome(Long.toString(val));
    }
}
