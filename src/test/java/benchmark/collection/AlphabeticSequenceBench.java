/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package benchmark.collection;

import javaslang.collection.*;

import static benchmark.Benchmark.bench;

public class AlphabeticSequenceBench {

    private static final int COUNT = 4;
    private static final int WARMUP = 2;

    private static final Seq<Character> ALPHABET = CharSeq.rangeClosed('A', 'Z').toStream();

    public static void main(String[] args) {
        benchAlphabeticSequenceUsingCartesianPower();
        benchAlphabeticSequenceUsingAppendSelf();
    }

    static void benchAlphabeticSequenceUsingCartesianPower() {
        bench("alphabetic sequence using cartesian product", COUNT, WARMUP, AlphabeticSequenceBench::cartesianPower);
    }

    static void benchAlphabeticSequenceUsingAppendSelf() {
        bench("alphabetic sequence using append self", COUNT, WARMUP, AlphabeticSequenceBench::appendSelf);
    }

    private static List<String> cartesianPower(int n) {
        return Stream
                .from(1)
                .flatMap(ALPHABET::crossProduct)
                .map(IndexedSeq::mkString)
                .takeWhile(s -> s.length() <= n)
                .toList();
    }

    private static List<String> appendSelf(int n) {
        return ALPHABET
                .sliding(1)
                .toStream()
                .appendSelf(stream -> stream.flatMap(product -> ALPHABET.map(product::append)))
                .map(IndexedSeq::mkString)
                .takeWhile(s -> s.length() <= n)
                .toList();
    }
}
