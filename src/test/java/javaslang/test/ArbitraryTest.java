/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.BinaryTree;
import javaslang.collection.Stream;

import java.util.Random;

public class ArbitraryTest {

    // TODO: unit tests

    public static void main(String[] args) {

        // represents arbitrary binary trees of a certain depth n
        final class ArbitraryTree implements Arbitrary<BinaryTree<Integer>> {
            @Override
            public Gen<BinaryTree<Integer>> apply(int n) {
                return random -> Gen.choose(-1000, 1000).flatMap(value -> {
                            if (n == 0) {
                                return Gen.of(BinaryTree.leaf(value));
                            } else {
                                return Gen.frequency(
                                        Tuple.of(1, Gen.of(BinaryTree.leaf(value))),
                                        Tuple.of(4, Gen.of(BinaryTree.branch(apply(n / 2).apply(random), value, apply(n / 2).apply(random))))
                                );
                            }
                        }
                ).apply(random);
            }
        }

        // tree generator with a size hint of 10
        final Gen<BinaryTree<Integer>> treeGen = new ArbitraryTree().apply(10);

        // stream sum of tree node values to console for 100 arbitrary trees
        final Random rng = new Random();
        Stream.gen(() -> treeGen.apply(rng))/*TODO:.map(Tree::sum)*/.take(100).stdout();
    }
}
