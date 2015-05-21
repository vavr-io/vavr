/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.control.Match;

import java.util.*;
import java.util.function.Function;

/**
 * <p>A general Tree interface.</p>
 *
 * @param <T> component type of this Tree
 * @since 1.1.0
 */
public interface Tree<T> extends Iterable<T> {

    /**
     * Gets the value of this tree.
     *
     * @return The value of this tree.
     * @throws java.lang.UnsupportedOperationException if this tree is empty
     */
    T getValue();

    /**
     * Checks if this tree is the empty tree.
     *
     * @return true, if this tree is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Checks if this Tree is a leaf. A tree is a leaf if it is a Node with no children.
     * Because the empty tree is no Node, it is not a leaf by definition.
     *
     * @return true if this tree is a leaf, false otherwise.
     */
    boolean isLeaf();

    /**
     * Checks if this Tree is a branch. A Tree is a branch if it is a Node which has children.
     * Because the empty tree is not a Node, it is not a branch by definition.
     *
     * @return true if this tree is a branch, false otherwise.
     */
    default boolean isBranch() {
        return !(isEmpty() || isLeaf());
    }

    /**
     * Returns the List of children of this tree.
     *
     * @return The empty list, if this is the empty tree or a leaf, otherwise the non-empty list of children.
     */
    List<? extends Tree<T>> getChildren();

    /**
     * Counts the number of branches of this tree. The empty tree and a leaf have no branches.
     *
     * @return The number of branches of this tree.
     */
    default int branchCount() {
        if (isEmpty() || isLeaf()) {
            return 0;
        } else {
            return getChildren().foldLeft(1, (count, child) -> count + child.branchCount());
        }
    }

    /**
     * Counts the number of leaves of this tree. The empty tree has no leaves.
     *
     * @return The number of leaves of this tree.
     */
    default int leafCount() {
        if (isEmpty()) {
            return 0;
        } else if (isLeaf()) {
            return 1;
        } else {
            return getChildren().foldLeft(0, (count, child) -> count + child.leafCount());
        }
    }

    /**
     * Counts the number of nodes (i.e. branches and leaves) of this tree. The empty tree has no nodes.
     *
     * @return The number of nodes of this tree.
     */
    default int nodeCount() {
        if (isEmpty()) {
            return 0;
        } else {
            return 1 + getChildren().foldLeft(0, (count, child) -> count + child.nodeCount());
        }
    }

    /**
     * Checks whether the given element occurs in this tree.
     *
     * @param element An element.
     * @return true, if this tree contains
     */
    default boolean contains(T element) {
        if (isEmpty()) {
            return false;
        } else if (Objects.equals(getValue(), element)) {
            return true;
        } else {
            for (Tree<T> child : getChildren()) {
                if (child.contains(element)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Iterates over the elements of this tree in pre-order.
     *
     * @return An iterator of this tree's node values.
     */
    @Override
    default Iterator<T> iterator() {
        return flatten().iterator();
    }

    /**
     * Flattens the Tree to a List, traversing the tree in pre-order.
     *
     * @return A List containing all elements of this tree, which is List.Nil if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default List<T> flatten() {
        return flatten(Order.PRE_ORDER);
    }

    /**
     * Flattens the Tree to a List, traversing the tree in a specific order.
     *
     * @param order the tree traversal order
     * @return A List containing all elements of this tree, which is List.Nil if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default List<T> flatten(Order order) {
        Objects.requireNonNull(order, "order is null");
        class Flatten {
            List<T> preOrder(Tree<T> tree) {
                return tree.getChildren()
                        .foldLeft(List.of(tree.getValue()), (acc, child) -> acc.appendAll(preOrder(child)));
            }

            List<T> inOrder(Tree<T> tree) {
                if (tree.isLeaf()) {
                    return List.of(tree.getValue());
                } else {
                    final List<? extends Tree<T>> children = tree.getChildren();
                    return children.tail().foldLeft(List.<T>nil(), (acc, child) -> acc.appendAll(inOrder(child)))
                            .prepend(tree.getValue())
                            .prependAll(inOrder(children.head()));
                }
            }

            List<T> postOrder(Tree<T> tree) {
                return tree.getChildren()
                        .foldLeft(List.<T>nil(), (acc, child) -> acc.appendAll(postOrder(child)))
                        .append(tree.getValue());
            }

            List<T> levelOrder(Tree<T> tree) {
                List<T> result = List.nil();
                final Queue<Tree<T>> queue = new LinkedList<>();
                queue.add(tree);
                while (!queue.isEmpty()) {
                    final Tree<T> next = queue.remove();
                    result = result.prepend(next.getValue());
                    queue.addAll(next.getChildren().toJavaList());
                }
                return result.reverse();
            }
        }
        if (isEmpty()) {
            return List.nil();
        } else {
            final Flatten flatten = new Flatten();
            return Match
                    .when(Order.PRE_ORDER, (Order o) -> flatten.preOrder(this))
                    .when(Order.IN_ORDER, (Order o) -> flatten.inOrder(this))
                    .when(Order.POST_ORDER, (Order o) -> flatten.postOrder(this))
                    .when(Order.LEVEL_ORDER, (Order o) -> flatten.levelOrder(this))
                    .apply(order);
        }
    }

    <U> Tree<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Returns a list string representation of this tree.
     *
     * @return A new string
     */
    default String toLispString() {
        class Local {
            String toString(Tree<T> tree) {
                if (tree.isEmpty()) {
                    return "()";
                } else {
                    final String value = String.valueOf(tree.getValue()).replaceAll("\\s+", " ").trim();
                    if (tree.isLeaf()) {
                        return value;
                    } else {
                        final String children = tree.getChildren()
                                .map(Local.this::toString)
                                .join(" ");
                        return "(" + value + " " + children + ")";
                    }
                }
            }
        }
        final String string = new Local().toString(this);
        return isLeaf() ? "(" + string + ")" : string;
    }

    /**
     * Returns an indented multiline string representation of this tree.
     *
     * @return A new string
     */
    default String toIndentedString() {
        class Local {
            String toString(Tree<T> tree, int depth) {
                if (tree.isEmpty()) {
                    return "";
                } else {
                    final String indent = repeat(' ', depth * 2);
                    final String value = String.valueOf(tree.getValue()).replaceAll("\\s+", " ").trim();
                    if (tree.isLeaf()) {
                        return "\n" + indent + value;
                    } else {
                        final String children = tree.getChildren()
                                .map(child -> toString(child, depth + 1))
                                .join();
                        return "\n" + indent + value + children;
                    }
                }
            }

            String repeat(char c, int n) {
                final char[] buf = new char[n];
                Arrays.fill(buf, c);
                return String.valueOf(buf);
            }
        }
        return new Local().toString(this, 0);
    }

    /**
     * <p>Tree traversal order.</p>
     *
     * Example tree:
     * <pre>
     * <code>
     *         1
     *        / \
     *       /   \
     *      /     \
     *     2       3
     *    / \     /
     *   4   5   6
     *  /       / \
     * 7       8   9
     * </code>
     * </pre>
     *
     * See also
     * <ul>
     * <li><a href="http://en.wikipedia.org/wiki/Tree_traversal">Tree traversal</a> (wikipedia)</li>
     * <li>See <a href="http://rosettacode.org/wiki/Tree_traversal">Tree traversal</a> (rosetta code)</li>
     * </ul>
     */
    // see http://programmers.stackexchange.com/questions/138766/in-order-traversal-of-m-way-trees
    enum Order {

        /**
         * 1 2 4 7 5 3 6 8 9 (= depth-first)
         */
        PRE_ORDER,

        /**
         * 7 4 2 5 1 8 6 9 3
         */
        IN_ORDER,

        /**
         * 7 4 5 2 8 9 6 3 1
         */
        POST_ORDER,

        /**
         * 1 2 3 4 5 6 7 8 9 (= breadth-first)
         */
        LEVEL_ORDER
    }
}
