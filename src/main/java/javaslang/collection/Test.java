/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

public class Test {

    public static void main(String[] args) {

//        printNumbers(0, 0, 0); // error
//
//        printNumbers(0, 0, 1); // = (0)
//
//        printNumbers(0, 0, -1); // = (0)
//
//        printNumbers(1, 3, 1); // = (1, 2, 3)
//
//        printNumbers(3, 1, -1); // = (3, 2, 1)
//
//        printNumbers(1, 3, 2); // = (1, 3)
//
//        printNumbers(4, 1, 2); // = ()
//
//        printNumbers(4, 1, -2); // = (4, 2)
//
//        printNumbers(1, -4, -2); // = (1, -1, -3)
//
//        printNumbers(1, -5, -2); // = (1, -1, -3, -5)
//        range(1, 3, 1);
//
//        printNumbers(1, 2, 1);

        range(0, 0, 1);
    }

    static void range(int from, int toExclusive, int step) {
        if (step == 0) {
            throw new IllegalArgumentException("step cannot be 0.");
        } else if (from == toExclusive || step * (from - toExclusive) > 0) {
            System.out.printf("(%s until %s by %s) = ()\n", from, toExclusive, step);
        } else {
            final int one = (from < toExclusive) ? 1 : -1;
            printNumbers(from, toExclusive - one, step);
        }
    }

    static void printNumbers(int from, int to, int step) {
        if (step == 0) {
            System.out.println("step cannot be 0.");
        } else if (from == to) {
            System.out.printf("(%s to %s by %s) = (%s)\n", from, to, step, from);
        } else if (step * (from - to) > 0) {
            System.out.printf("(%s to %s by %s) = ()\n", from, to, step);
        } else {
            final int gap = (from - to) % step;
            final int signum = (from < to) ? -1 : 1;
            final int bound = from * signum;
            System.out.printf("(%s to %s by %s) = ", from, to, step);
            int count = 0;
            for (int i = to + gap; i * signum <= bound; i -= step) {
                System.out.printf("%s ", i);
                if (count++ > 10) {
                    break;
                }
            }
            System.out.println();
        }
    }
}
