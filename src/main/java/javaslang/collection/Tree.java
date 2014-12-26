/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Algebra.Functor;
import javaslang.Manifest;
import javaslang.Strings;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Function;

public interface Tree<T, CLASS extends Tree<?, CLASS, ?>, SELF extends Tree<T, ?, SELF>> extends Functor<T>, Manifest<T, CLASS> {

    /**
     * Returns the name of the Tree implementation (e.g. BTree, RTree).
     */
    String getName();

    /**
     * Gets the value of this tree.
     *
     * @return The value of this tree.
     * @throws java.lang.UnsupportedOperationException if this tree is empty
     */
    T get();

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
    List<SELF> children();

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
            return (int) children().foldLeft(1, (count, child) -> count + child.branchCount());
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
            return (int) children().foldLeft(0, (count, child) -> count + child.leafCount());
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
        } else if (isLeaf()) {
            return 1;
        } else {
            // need cast because of jdk 1.8.0_25 compiler error
            //noinspection RedundantCast
            return (int) children().foldLeft(1, (count, child) -> count + child.nodeCount());
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
        } else if (Objects.equals(get(), element)) {
            return true;
        } else {
            for (Tree<T, ?, SELF> child : children()) {
                if (child.contains(element)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Flattens the Tree to a List, traversing the tree in preorder.
     * @return A List containing all elements of this tree, which is List.Nil if this tree is empty.
     */
    default List<T> flatten() {
        return flatten(Traversal.PRE_ORDER);
    }

    default List<T> flatten(Traversal traversal) {
        class Flatten {
            List<T> preOrder(Tree<T,?, SELF> tree) {
                return tree.children()
                        .foldLeft(List.of(tree.get()), (acc, child) -> acc.appendAll(preOrder(child)));
            }
            List<T> inOrder(Tree<T, ?, SELF> tree) {
                if (tree.isLeaf()) {
                    return List.of(tree.get());
                } else {
                    final List<SELF> children = tree.children();
                    return children.tail().foldLeft(List.<T>nil(), (acc, child) -> acc.appendAll(inOrder(child)))
                            .prepend(tree.get())
                            .prependAll(inOrder(children.head()));
                }
            }
            List<T> postOrder(Tree<T, ?, SELF> tree) {
                return tree.children()
                        .foldLeft(List.<T> nil(), (acc, child) -> acc.appendAll(postOrder(child)))
                        .append(tree.get());
            }
            List<T> levelOrder(Tree<T, ?, SELF> tree) {
                List<T> result = List.nil();
                final Queue<Tree<T, ?, SELF>> queue = new LinkedList<>();
                queue.add(tree);
                while (!queue.isEmpty()) {
                    final Tree<T, ?, SELF> next = queue.remove();
                    result = result.prepend(next.get());
                    queue.addAll(next.children().toArrayList());
                }
                return result.reverse();
            }
        }
        if (isEmpty()) {
            return List.nil();
        } else {
            final Flatten flatten = new Flatten();
            switch (traversal) {
                case PRE_ORDER : return flatten.preOrder(this);
                case IN_ORDER : return flatten.inOrder(this);
                case POST_ORDER : return flatten.postOrder(this);
                case LEVEL_ORDER : return flatten.levelOrder(this);
                default : throw new IllegalStateException("Unknown traversal: " + traversal.name());
            }
        }
    }

    // TODO: traverse(Consumer<T>), traverse(Consumer<T>, Traversal)
    // TODO: traverse(Predicate<T>)/*true = go on*/, traverse(Predicate<T>, Traversal)

    // -- toString

    default String toLispString() {
        class Local {
            String toString(Tree<T, ?, SELF> tree) {
                if (tree.isEmpty()) {
                    return "()";
                } else {
                    final String value = Strings.toString(tree.get()).replaceAll("\\s+", " ").trim();
                    if (tree.isLeaf()) {
                        return value;
                    } else {
                        final String children = tree.children()
                                .map(Local.this::toString)
                                .join(" ");
                        return String.format("(%s %s)", value, children);
                    }
                }
            }
        }
        final String string = new Local().toString(this);
        return getName() + (isLeaf() ? "(" + string + ")" : string);
    }

    default String toIndentedString() {
        class Local {
            String toString(Tree<T, ?, SELF> tree, int depth) {
                if (tree.isEmpty()) {
                    return "";
                } else {
                    final String indent = Strings.repeat(' ', depth * 2);
                    final String value = Strings.toString(tree.get()).replaceAll("\\s+", " ").trim();
                    if (tree.isLeaf()) {
                        return "\n" + indent + value;
                    } else {
                        final String children = tree.children()
                                .map(child -> toString(child, depth + 1))
                                .join();
                        return String.format("\n%s%s%s", indent, value, children);
                    }
                }
            }
        }
        return getName() + ":" + new Local().toString(this, 0);
    }

    // -- Object.*

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();

    // -- Functor implementation

    <U> Tree<U, CLASS, ?> unit(U value);

    <U, TREE extends Manifest<U, CLASS>> Tree<U, CLASS, ?> unit(U value, List<TREE> children);

    @Override
    default <U> Tree<U, CLASS, ?> map(Function<? super T, ? extends U> f) {
        if (isEmpty()) {
            //noinspection unchecked
            return (Tree<U, CLASS, ?>) this;
        } else if (isLeaf()) {
            //noinspection RedundantCast
            return (Tree<U, CLASS, ?>) unit(f.apply(get()));
        } else {
            final U value = f.apply(get());
            final List<Tree> children = children().map(tree -> tree.map(f::apply));
            //noinspection RedundantCast
            return (Tree<U, CLASS, ?>) unit(value, children);
        }
    }

    // -- Tree API shared by implementations

    static abstract class AbstractTree<T, CLASS extends Tree<?, CLASS, ?>, SELF extends Tree<T, ?, SELF>> implements Tree<T, CLASS, SELF> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || !getClass().isAssignableFrom(o.getClass())) {
                return false;
            } else {
                final Tree that = (Tree) o;
                return (this.isEmpty() && that.isEmpty()) || (!this.isEmpty() && !that.isEmpty()
                        && Objects.equals(this.get(), that.get())
                        && this.children().equals(that.children()));
            }
        }

        @Override
        public int hashCode() {
            if (isEmpty()) {
                return 1;
            } else {
                // need cast because of jdk 1.8.0_25 compiler error
                //noinspection RedundantCast
                return (int) children().map(Objects::hashCode).foldLeft(31 + Objects.hashCode(get()), (i,j) -> i * 31 + j);
            }
        }

        @Override
        public String toString() {
            return toLispString();
        }
    }

    // -- types

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
    static enum Traversal {

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
