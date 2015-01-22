/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.test;

import javaslang.Tuple;
import javaslang.collection.BinaryTree;
import javaslang.collection.List;

public class ArbitraryTest {

    public static void main(String[] args) {

        final class ArbitraryTree {
            Gen<BinaryTree<Integer>> gen(int n) {
                return Gen.choose(0, 1000).flatMap(i -> {
                            if (n == 0) {
                                return Gen.of(BinaryTree.leaf(i));
                            } else {
                                return Gen.frequency(
                                        Tuple.of(1, Gen.of(BinaryTree.leaf(i))),
                                        Tuple.of(4, Gen.of(BinaryTree.branch(gen(n/2).get(), i, gen(n/2).get())))
                                );
                            }
                        }
                );
            }
        }

        final Arbitrary<BinaryTree<Integer>> tree = n -> new ArbitraryTree().gen(n);

        // TODO: DELME
        List.range(0, 10).forEach(i ->  System.out.println(tree.arbitrary(i).get()));

    }
}
