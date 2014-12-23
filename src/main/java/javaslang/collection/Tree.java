/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.Strings;

import java.util.Objects;

public interface Tree<T, SELF extends Tree<T, SELF>> {

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

    List<SELF> children();

    // TODO: flatten, levels

    // -- traveral

    // TODO: traverse PREORDER, INORDER, POSTORDER, LEVEL_ORDER (see http://rosettacode.org/wiki/Tree_traversal)

    // -- toString

    default String toLispString() {
        class Local {
            String toString(Tree<T, SELF> tree) {
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
            String toString(Tree<T, SELF> tree, int depth) {
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

    // -- RTree API shared by implementations Node and Nil

    static abstract class AbstractTree<T, SELF extends Tree<T, SELF>> implements Tree<T, SELF> {

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o == null || getClass().isAssignableFrom(o.getClass())) {
                return false;
            } else {
                final Tree<?, ?> that = (Tree<?, ?>) o;
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
}
