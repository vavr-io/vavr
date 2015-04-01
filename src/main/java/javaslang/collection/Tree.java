/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Function1;
import javaslang.ValueObject;
import javaslang.algebra.Functor1;
import javaslang.control.Match;

import java.util.*;

/**
 * A general Tree interface.
 *
 * @param <T> component type of this Tree
 * @since 1.1.0
 */
public interface Tree<T> extends Functor1<T>, ValueObject {

    long serialVersionUID = 1L;

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
            // need cast because of jdk 1.8.0_25 compiler error
            //noinspection RedundantCast
            return (int) getChildren().foldLeft(1, (count, child) -> count + child.branchCount());
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
            // need cast because of jdk 1.8.0_25 compiler error
            //noinspection RedundantCast
            return (int) getChildren().foldLeft(0, (count, child) -> count + child.leafCount());
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
            // need cast because of jdk 1.8.0_25 compiler error
            //noinspection RedundantCast
            return 1 + getChildren().foldLeft(0, (count, child) -> count + child.nodeCount());
        }
    }

    /**
     * Checks, if the given element occurs in this tree.
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

    default Iterator<T> iterator() {
        if (isEmpty()) {
            return List.<T>nil().iterator();
        } else {
            class Local {
                Stream<T> preOrder(Tree<T> tree) {
                    return new Stream.Cons<>(tree.getValue(), () -> Stream.of(tree.getChildren()).flatMap(Local.this::preOrder));
                }
            }
            return new Local().preOrder(this).iterator();
        }
    }

    /**
     * Flattens the Tree to a List, traversing the tree in preorder.
     *
     * @return A List containing all elements of this tree, which is List.Nil if this tree is empty.
     * @throws java.lang.NullPointerException if order is null
     */
    default List<T> flatten() {
        return flatten(Order.PRE_ORDER);
    }

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
                    .caze(Order.PRE_ORDER, (Order o) -> flatten.preOrder(this))
                    .caze(Order.IN_ORDER, (Order o) -> flatten.inOrder(this))
                    .caze(Order.POST_ORDER, (Order o) -> flatten.postOrder(this))
                    .caze(Order.LEVEL_ORDER, (Order o) -> flatten.levelOrder(this))
                    .apply(order);
        }
    }

    @Override
    <U> Tree<U> map(Function1<? super T, ? extends U> mapper);

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
                        return String.format("(%s %s)", value, children);
                    }
                }
            }
        }
        final String string = new Local().toString(this);
        return isLeaf() ? "(" + string + ")" : string;
    }

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
                        return String.format("\n%s%s%s", indent, value, children);
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

    /*
     * See http://en.wikipedia.org/wiki/Tree_traversal, http://rosettacode.org/wiki/Tree_traversal,
     * http://programmers.stackexchange.com/questions/138766/in-order-traversal-of-m-way-trees.
     *
     *         1
     *        / \
     *       /   \
     *      /     \
     *     2       3
     *    / \     /
     *   4   5   6
     *  /       / \
     * 7       8   9
     *
     */
    enum Order {

        // 1 2 4 7 5 3 6 8 9 (= depth-first)
        PRE_ORDER,

        // 7 4 2 5 1 8 6 9 3
        IN_ORDER,

        // 7 4 5 2 8 9 6 3 1
        POST_ORDER,

        // 1 2 3 4 5 6 7 8 9 (= breadth-first)
        LEVEL_ORDER
    }
}
