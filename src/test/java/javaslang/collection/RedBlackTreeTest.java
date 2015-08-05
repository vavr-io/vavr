/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import org.junit.Ignore;
import org.junit.Test;
import scala.Char;

import java.util.Comparator;

import static javaslang.collection.RedBlackTree.TreeNode;
import static javaslang.collection.RedBlackTree.EmptyNode;

import static javaslang.collection.RedBlackTree.Color.BLACK;
import static javaslang.collection.RedBlackTree.Color.RED;

import static org.assertj.core.api.Assertions.assertThat;

public class RedBlackTreeTest {

    @Test
    public void shouldCreateEmptyTree() {
        final RedBlackTree<Integer> tree = RedBlackTree.empty();
        assertThat(tree.isEmpty()).isTrue();
    }

    // -- [START] Tests from "Purely Functional Data Structures" (Okasaki 2003, p. 27)

    private static final Comparator<Character> COMP = (c1, c2) -> c1 - c2;

    private static final EmptyNode<Character> EMPTY = new EmptyNode<>(COMP);

    private static TreeNode<Character> red(char value) {
        return red(EMPTY, value, EMPTY);
    }

    private static TreeNode<Character> black(char value) {
        return black(EMPTY, value, EMPTY);
    }

    private static TreeNode<Character> red(RedBlackTree<Character> left, char value, RedBlackTree<Character> right) {
        return new TreeNode<>(RED, left, value, right, COMP);
    }

    private static TreeNode<Character> black(RedBlackTree<Character> left, char value, RedBlackTree<Character> right) {
        return new TreeNode<>(BLACK, left, value, right, COMP);
    }

    // All of the four following cases have the same result:
    //
    //     (Ry)
    //     /   \
    //  (Bx)   (Bz)
    //  /  \   /  \
    // a    b c    d
    //
    // = (R:y (B:x R:a R:b) (B:z R:c R:d))
    static final TreeNode<Character> EXAMPLE = red(black(red('a'), 'x', red('b')), 'y', black(red('c'), 'z', red('d')));

    /**
     * Case 0:
     * <pre><code>
     *        (Bz)
     *        /  \
     *     (Ry)   d
     *     /  \
     *  (Rx)   c
     *  /  \
     * a    b
     * </code></pre>
     */
    @Test
    public void shouldBalanceCase0() {
        final TreeNode<Character> testee = black(red(red(red('a'), 'x', red('b')),'y', red('c')), 'z', red('d'));
        final TreeNode<Character> actual = balance(testee);
        assertThat(actual.equals(testee)).isFalse();
        assertThat(actual.equals(EXAMPLE)).isTrue();
    }

    /**
     * Case 1:
     * <pre><code>
     *     (Bz)
     *     /  \
     *   (Rx)  d
     *  /   \
     * a    (Ry)
     *      /  \
     *     b    c
     * </code></pre>
     */
    @Test
    public void shouldBalanceCase1() {
        final TreeNode<Character> testee = black(red(red('a'), 'x', red(red('b'), 'y', red('c'))), 'z', red('d'));
        final TreeNode<Character> actual = balance(testee);
        // TODO:
        System.out.println(testee);
        System.out.println(actual);
        System.out.println(EXAMPLE);
        assertThat(actual.equals(testee)).isFalse();
        assertThat(actual.equals(EXAMPLE)).isTrue();
    }

    /**
     * Case 2:
     * <pre><code>
     *  (Bx)
     *  /  \
     * a   (Rz)
     *     /  \
     *   (Ry)  d
     *   /  \
     *  c    d
     * </code></pre>
     */
    @Test
    public void shouldBalanceCase2() {
        final TreeNode<Character> testee = black(red('a'), 'x', red(red(red('c'), 'y', red('d')), 'z', red('d')));
        final TreeNode<Character> actual = balance(testee);
        // TODO:
        System.out.println(testee);
        System.out.println(actual);
        System.out.println(EXAMPLE);
        assertThat(actual.equals(testee)).isFalse();
        assertThat(actual.equals(EXAMPLE)).isTrue();
    }

    /**
     * <strong>Case 3:</strong>
     *
     * <pre><code>
     *  (Bx)
     *  /  \
     * a   (Ry)
     *     /  \
     *    b   (Rz)
     *        /  \
     *       c    d
     * </code></pre>
     */
    @Test
    public void shouldBalanceCase3() {
        final TreeNode<Character> testee = black(red('a'), 'x', red(red('b'), 'y', red(red('c'), 'z', red('d'))));
        final TreeNode<Character> actual = balance(testee);
        // TODO:
        System.out.println(testee);
        System.out.println(actual);
        System.out.println(EXAMPLE);
        assertThat(actual.equals(testee)).isFalse();
        assertThat(actual.equals(EXAMPLE)).isTrue();
    }

    private static <T> TreeNode<T> balance(TreeNode<T> tree) {
        return tree.balance(tree.color, tree.left, tree.value, tree.right, tree.comparator);
    }

    // -- [END] Tests from "Purely Functional Data Structures" (Okasaki 2003, p. 27)

    // Example: http://www.csee.umbc.edu/courses/undergraduate/341/spring04/hood/notes/red_black/
    @Ignore
    @Test
    public void shouldInsert_2_1_4_5_9_3_6_7() {
        RedBlackTree<Integer> tree = RedBlackTree.empty();

        // insert 2
        tree = tree.add(2);
        assertThat(toLispString(tree)).isEqualTo("(b2)");

        // insert 1
        tree = tree.add(1);
        assertThat(toLispString(tree)).isEqualTo("(b2 r1)");

        // insert 4
        tree = tree.add(4);
        assertThat(toLispString(tree)).isEqualTo("(b2 r1 r4)");

        // insert 5
        tree = tree.add(5);
        assertThat(toLispString(tree)).isEqualTo("(b2 b1 (b4 r5))"); // currently (b2 r1 (r4 r5))

        // insert 9
        tree = tree.add(9);
        assertThat(toLispString(tree)).isEqualTo("(b2 b1 (b5 r4 r9))");

        // insert 3
        tree = tree.add(3);
        assertThat(toLispString(tree)).isEqualTo("(b2 b1 (r5 (b4 r3) b9))");

        // insert 6
        tree = tree.add(6);
        assertThat(toLispString(tree)).isEqualTo("(b2 b1 (b5 (b4 r3) (b9 r6)))");

        // insert 7
        tree = tree.add(7);
        assertThat(toLispString(tree)).isEqualTo("(b2 b1 (r5 (b4 r3) (b7 r6 r9)))");
    }

    private static <T> String toLispString(RedBlackTree<T> tree) {
        class Local {
            String toString(RedBlackTree<T> tree) {
                if (tree.isEmpty()) {
                    return "";
                } else {
                    final RedBlackTree.TreeNode<T> node = (RedBlackTree.TreeNode<T>) tree;
                    final String value = node.color.toString().toLowerCase().charAt(0) + String.valueOf(node.value).replaceAll("\\s+", " ").trim();
                    final boolean isLeaf = node.left.isEmpty() && node.right.isEmpty();
                    if (isLeaf) {
                        return value;
                    } else {
                        final String children = List.of(node.left, node.right)
                                .map(Local.this::toString)
                                .join(" ").trim();
                        return "(" + value + (children.isEmpty() ? "" : " " + children) + ")";
                    }
                }
            }
        }
        final String string = new Local().toString(tree);
        return !string.startsWith("(") ? "(" + string + ")" : string;
    }
}
