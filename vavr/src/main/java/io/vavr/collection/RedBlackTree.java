/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2017 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.collection;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.collection.RedBlackTreeModule.Empty;
import io.vavr.collection.RedBlackTreeModule.Node;
import io.vavr.control.Option;

import java.io.Serializable;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Objects;

import static io.vavr.collection.RedBlackTree.Color.BLACK;
import static io.vavr.collection.RedBlackTree.Color.RED;

/**
 * Purely functional Red/Black Tree, inspired by <a href="https://github.com/kazu-yamamoto/llrbtree/blob/master/Data/Set/RBTree.hs">Kazu Yamamoto's Haskell implementation</a>.
 * <p>
 * Based on
 * <ul>
 * <li><a href="http://www.eecs.usma.edu/webs/people/okasaki/pubs.html#jfp99">Chris Okasaki, "Red-Black Trees in a Functional Setting", Journal of Functional Programming, 9(4), pp 471-477, July 1999</a></li>
 * <li>Stefan Kahrs, "Red-black trees with types", Journal of functional programming, 11(04), pp 425-432, July 2001</li>
 * </ul>
 *
 * @param <T> Component type
 * @author Daniel Dietrich
 */
interface RedBlackTree<T> extends Iterable<T> {

    static <T> RedBlackTree<T> empty(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator, "comparator is null");
        return new Empty<>(comparator);
    }

    static <T> RedBlackTree<T> of(Comparator<? super T> comparator, T value) {
        Objects.requireNonNull(comparator, "comparator is null");
        final Empty<T> empty = new Empty<>(comparator);
        return new Node<>(BLACK, 1, empty, value, empty, empty);
    }

    @SafeVarargs
    static <T> RedBlackTree<T> of(Comparator<? super T> comparator, T... values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        RedBlackTree<T> tree = empty(comparator);
        for (T value : values) {
            tree = tree.insert(value);
        }
        return tree;
    }

    @SuppressWarnings("unchecked")
    static <T> RedBlackTree<T> ofAll(Comparator<? super T> comparator, Iterable<? extends T> values) {
        Objects.requireNonNull(comparator, "comparator is null");
        Objects.requireNonNull(values, "values is null");
        // function equality is not computable => same object check
        if (values instanceof RedBlackTree && ((RedBlackTree<T>) values).comparator() == comparator) {
            return (RedBlackTree<T>) values;
        } else {
            RedBlackTree<T> tree = empty(comparator);
            for (T value : values) {
                tree = tree.insert(value);
            }
            return tree;
        }
    }

    /**
     * Inserts a new value into this tree.
     *
     * @param value A value.
     * @return A new tree if this tree does not contain the given value, otherwise the same tree instance.
     */
    default RedBlackTree<T> insert(T value) {
        return Node.insert(this, value).color(BLACK);
    }

    /**
     * Return the {@link Color} of this Red/Black Tree node.
     * <p>
     * An empty node is {@code BLACK} by definition.
     *
     * @return Either {@code RED} or {@code BLACK}.
     */
    Color color();

    /**
     * Returns the underlying {@link java.util.Comparator} of this RedBlackTree.
     *
     * @return The comparator.
     */
    Comparator<T> comparator();

    /**
     * Checks, if this {@code RedBlackTree} contains the given {@code value}.
     *
     * @param value A value.
     * @return true, if this tree contains the value, false otherwise.
     */
    boolean contains(T value);

    /**
     * Deletes a value from this RedBlackTree.
     *
     * @param value A value
     * @return A new RedBlackTree if the value is present, otherwise this.
     */
    default RedBlackTree<T> delete(T value) {
        final RedBlackTree<T> tree = Node.delete(this, value)._1;
        return Node.color(tree, BLACK);
    }

    default RedBlackTree<T> difference(RedBlackTree<T> tree) {
        Objects.requireNonNull(tree, "tree is null");
        if (isEmpty() || tree.isEmpty()) {
            return this;
        } else {
            final Node<T> that = (Node<T>) tree;
            final Tuple2<RedBlackTree<T>, RedBlackTree<T>> split = Node.split(this, that.value);
            return Node.merge(split._1.difference(that.left), split._2.difference(that.right));
        }
    }

    /**
     * Returns the empty instance of this RedBlackTree.
     *
     * @return An empty ReadBlackTree
     */
    RedBlackTree<T> emptyInstance();

    /**
     * Finds the value stored in this tree, if exists, by applying the underlying comparator to the tree elements and
     * the given element.
     * <p>
     * Especially the value returned may differ from the given value, even if the underlying comparator states that
     * both are equal.
     *
     * @param value A value
     * @return Some value, if this tree contains a value equal to the given value according to the underlying comparator. Otherwise None.
     */
    Option<T> find(T value);

    default RedBlackTree<T> intersection(RedBlackTree<T> tree) {
        Objects.requireNonNull(tree, "tree is null");
        if (isEmpty()) {
            return this;
        } else if (tree.isEmpty()) {
            return tree;
        } else {
            final Node<T> that = (Node<T>) tree;
            final Tuple2<RedBlackTree<T>, RedBlackTree<T>> split = Node.split(this, that.value);
            if (contains(that.value)) {
                return Node.join(split._1.intersection(that.left), that.value, split._2.intersection(that.right));
            } else {
                return Node.merge(split._1.intersection(that.left), split._2.intersection(that.right));
            }
        }
    }

    /**
     * Checks if this {@code RedBlackTree} is empty, i.e. an instance of {@code Leaf}.
     *
     * @return true, if it is empty, false otherwise.
     */
    boolean isEmpty();

    /**
     * Returns the left child if this is a non-empty node, otherwise throws.
     *
     * @return The left child.
     * @throws UnsupportedOperationException if this RedBlackTree is empty
     */
    RedBlackTree<T> left();

    /**
     * Returns the maximum element of this tree according to the underlying comparator.
     *
     * @return Some element, if this is not empty, otherwise None
     */
    default Option<T> max() {
        return isEmpty() ? Option.none() : Option.some(Node.maximum((Node<T>) this));
    }

    /**
     * Returns the minimum element of this tree according to the underlying comparator.
     *
     * @return Some element, if this is not empty, otherwise None
     */
    default Option<T> min() {
        return isEmpty() ? Option.none() : Option.some(Node.minimum((Node<T>) this));
    }

    /**
     * Returns the right child if this is a non-empty node, otherwise throws.
     *
     * @return The right child.
     * @throws UnsupportedOperationException if this RedBlackTree is empty
     */
    RedBlackTree<T> right();

    /**
     * Returns the size of this tree.
     *
     * @return the number of nodes of this tree and 0 if this is the empty tree
     */
    int size();

    /**
     * Adds all of the elements of the given {@code tree} to this tree, if not already present.
     *
     * @param tree The RedBlackTree to form the union with.
     * @return A new RedBlackTree that contains all distinct elements of this and the given {@code tree}.
     */
    default RedBlackTree<T> union(RedBlackTree<T> tree) {
        Objects.requireNonNull(tree, "tree is null");
        if (tree.isEmpty()) {
            return this;
        } else {
            final Node<T> that = (Node<T>) tree;
            if (isEmpty()) {
                return that.color(BLACK);
            } else {
                final Tuple2<RedBlackTree<T>, RedBlackTree<T>> split = Node.split(this, that.value);
                return Node.join(split._1.union(that.left), that.value, split._2.union(that.right));
            }
        }
    }

    /**
     * Returns the value of the current tree node or throws if this is empty.
     *
     * @return The value.
     * @throws NoSuchElementException if this is the empty node.
     */
    T value();

    /**
     * Returns an Iterator that iterates elements in the order induced by the underlying Comparator.
     * <p>
     * Internally an in-order traversal of the RedBlackTree is performed.
     * <p>
     * Example:
     *
     * <pre><code>
     *       4
     *      / \
     *     2   6
     *    / \ / \
     *   1  3 5  7
     * </code></pre>
     *
     * Iteration order: 1, 2, 3, 4, 5, 6, 7
     * <p>
     * See also <a href="http://n00tc0d3r.blogspot.de/2013/08/implement-iterator-for-binarytree-i-in.html">Implement Iterator for BinaryTree I (In-order)</a>.
     */
    @Override
    default Iterator<T> iterator() {
        if (isEmpty()) {
            return Iterator.empty();
        } else {
            final Node<T> that = (Node<T>) this;
            return new AbstractIterator<T>() {

                List<Node<T>> stack = pushLeftChildren(List.empty(), that);

                @Override
                public boolean hasNext() {
                    return !stack.isEmpty();
                }

                @Override
                public T getNext() {
                    final Tuple2<Node<T>, ? extends List<Node<T>>> result = stack.pop2();
                    final Node<T> node = result._1;
                    stack = node.right.isEmpty() ? result._2 : pushLeftChildren(result._2, (Node<T>) node.right);
                    return result._1.value;
                }

                private List<Node<T>> pushLeftChildren(List<Node<T>> initialStack, Node<T> that) {
                    List<Node<T>> stack = initialStack;
                    RedBlackTree<T> tree = that;
                    while (!tree.isEmpty()) {
                        final Node<T> node = (Node<T>) tree;
                        stack = stack.push(node);
                        tree = node.left;
                    }
                    return stack;
                }
            };
        }
    }

    /**
     * Returns a Lisp like representation of this tree.
     *
     * @return This Tree as Lisp like String.
     */
    @Override
    String toString();

    enum Color {

        RED, BLACK;

        @Override
        public String toString() {
            return (this == RED) ? "R" : "B";
        }
    }
}

interface RedBlackTreeModule {

    /**
     * A non-empty tree node.
     *
     * @param <T> Component type
     */
    final class Node<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Color color;
        final int blackHeight;
        final RedBlackTree<T> left;
        final T value;
        final RedBlackTree<T> right;
        final Empty<T> empty;
        final int size;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        Node(Color color, int blackHeight, RedBlackTree<T> left, T value, RedBlackTree<T> right, Empty<T> empty) {
            this.color = color;
            this.blackHeight = blackHeight;
            this.left = left;
            this.value = value;
            this.right = right;
            this.empty = empty;
            this.size = left.size() + right.size() + 1;
        }

        @Override
        public Color color() {
            return color;
        }

        @Override
        public Comparator<T> comparator() {
            return empty.comparator;
        }

        @Override
        public boolean contains(T value) {
            final int result = empty.comparator.compare(value, this.value);
            if (result < 0) {
                return left.contains(value);
            } else if (result > 0) {
                return right.contains(value);
            } else {
                return true;
            }
        }

        @Override
        public Empty<T> emptyInstance() {
            return empty;
        }

        @Override
        public Option<T> find(T value) {
            final int result = empty.comparator.compare(value, this.value);
            if (result < 0) {
                return left.find(value);
            } else if (result > 0) {
                return right.find(value);
            } else {
                return Option.some(this.value);
            }
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public RedBlackTree<T> left() {
            return left;
        }

        @Override
        public RedBlackTree<T> right() {
            return right;
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public String toString() {
            return isLeaf() ? "(" + color + ":" + value + ")" : toLispString(this);
        }

        private static String toLispString(RedBlackTree<?> tree) {
            if (tree.isEmpty()) {
                return "";
            } else {
                final Node<?> node = (Node<?>) tree;
                final String value = node.color + ":" + node.value;
                if (node.isLeaf()) {
                    return value;
                } else {
                    final String left = node.left.isEmpty() ? "" : " " + toLispString(node.left);
                    final String right = node.right.isEmpty() ? "" : " " + toLispString(node.right);
                    return "(" + value + left + right + ")";
                }
            }
        }

        private boolean isLeaf() {
            return left.isEmpty() && right.isEmpty();
        }

        Node<T> color(Color color) {
            return (this.color == color) ? this : new Node<>(color, blackHeight, left, value, right, empty);
        }

        static <T> RedBlackTree<T> color(RedBlackTree<T> tree, Color color) {
            return tree.isEmpty() ? tree : ((Node<T>) tree).color(color);
        }

        private static <T> Node<T> balanceLeft(Color color, int blackHeight, RedBlackTree<T> left, T value,
                RedBlackTree<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!left.isEmpty()) {
                    final Node<T> ln = (Node<T>) left;
                    if (ln.color == RED) {
                        if (!ln.left.isEmpty()) {
                            final Node<T> lln = (Node<T>) ln.left;
                            if (lln.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, lln.left, lln.value, lln.right,
                                        empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, ln.right, value, right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, ln.value, newRight, empty);
                            }
                        }
                        if (!ln.right.isEmpty()) {
                            final Node<T> lrn = (Node<T>) ln.right;
                            if (lrn.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, ln.left, ln.value, lrn.left,
                                        empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, lrn.right, value, right, empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, lrn.value, newRight, empty);
                            }
                        }
                    }
                }
            }
            return new Node<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Node<T> balanceRight(Color color, int blackHeight, RedBlackTree<T> left, T value,
                RedBlackTree<T> right, Empty<T> empty) {
            if (color == BLACK) {
                if (!right.isEmpty()) {
                    final Node<T> rn = (Node<T>) right;
                    if (rn.color == RED) {
                        if (!rn.right.isEmpty()) {
                            final Node<T> rrn = (Node<T>) rn.right;
                            if (rrn.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, left, value, rn.left, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, rrn.left, rrn.value, rrn.right,
                                        empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, rn.value, newRight, empty);
                            }
                        }
                        if (!rn.left.isEmpty()) {
                            final Node<T> rln = (Node<T>) rn.left;
                            if (rln.color == RED) {
                                final Node<T> newLeft = new Node<>(BLACK, blackHeight, left, value, rln.left, empty);
                                final Node<T> newRight = new Node<>(BLACK, blackHeight, rln.right, rn.value, rn.right,
                                        empty);
                                return new Node<>(RED, blackHeight + 1, newLeft, rln.value, newRight, empty);
                            }
                        }
                    }
                }
            }
            return new Node<>(color, blackHeight, left, value, right, empty);
        }

        private static <T> Tuple2<? extends RedBlackTree<T>, Boolean> blackify(RedBlackTree<T> tree) {
            if (tree instanceof Node) {
                final Node<T> node = (Node<T>) tree;
                if (node.color == RED) {
                    return Tuple.of(node.color(BLACK), false);
                }
            }
            return Tuple.of(tree, true);
        }

        static <T> Tuple2<? extends RedBlackTree<T>, Boolean> delete(RedBlackTree<T> tree, T value) {
            if (tree.isEmpty()) {
                return Tuple.of(tree, false);
            } else {
                final Node<T> node = (Node<T>) tree;
                final int comparison = node.comparator().compare(value, node.value);
                if (comparison < 0) {
                    final Tuple2<? extends RedBlackTree<T>, Boolean> deleted = delete(node.left, value);
                    final RedBlackTree<T> l = deleted._1;
                    final boolean d = deleted._2;
                    if (d) {
                        return Node.unbalancedRight(node.color, node.blackHeight - 1, l, node.value, node.right,
                                node.empty);
                    } else {
                        final Node<T> newNode = new Node<>(node.color, node.blackHeight, l, node.value, node.right,
                                node.empty);
                        return Tuple.of(newNode, false);
                    }
                } else if (comparison > 0) {
                    final Tuple2<? extends RedBlackTree<T>, Boolean> deleted = delete(node.right, value);
                    final RedBlackTree<T> r = deleted._1;
                    final boolean d = deleted._2;
                    if (d) {
                        return Node.unbalancedLeft(node.color, node.blackHeight - 1, node.left, node.value, r,
                                node.empty);
                    } else {
                        final Node<T> newNode = new Node<>(node.color, node.blackHeight, node.left, node.value, r,
                                node.empty);
                        return Tuple.of(newNode, false);
                    }
                } else {
                    if (node.right.isEmpty()) {
                        if (node.color == BLACK) {
                            return blackify(node.left);
                        } else {
                            return Tuple.of(node.left, false);
                        }
                    } else {
                        final Node<T> nodeRight = (Node<T>) node.right;
                        final Tuple3<? extends RedBlackTree<T>, Boolean, T> newRight = deleteMin(nodeRight);
                        final RedBlackTree<T> r = newRight._1;
                        final boolean d = newRight._2;
                        final T m = newRight._3;
                        if (d) {
                            return Node.unbalancedLeft(node.color, node.blackHeight - 1, node.left, m, r, node.empty);
                        } else {
                            final RedBlackTree<T> newNode = new Node<>(node.color, node.blackHeight, node.left, m, r,
                                    node.empty);
                            return Tuple.of(newNode, false);
                        }
                    }
                }
            }
        }

        private static <T> Tuple3<? extends RedBlackTree<T>, Boolean, T> deleteMin(Node<T> node) {
            if (node.color() == BLACK && node.left().isEmpty() && node.right.isEmpty()){
                return Tuple.of(node.empty, true, node.value());
            } else if (node.color() == BLACK && node.left().isEmpty() && node.right().color() == RED){
                return Tuple.of(((Node<T>)node.right()).color(BLACK), false, node.value());
            } else if (node.color() == RED && node.left().isEmpty()){
                return Tuple.of(node.right(), false, node.value());
            } else{
                final Node<T> nodeLeft = (Node<T>) node.left;
                final Tuple3<? extends RedBlackTree<T>, Boolean, T> newNode = deleteMin(nodeLeft);
                final RedBlackTree<T> l = newNode._1;
                final boolean deleted = newNode._2;
                final T m = newNode._3;
                if (deleted) {
                    final Tuple2<Node<T>, Boolean> tD = Node.unbalancedRight(node.color, node.blackHeight - 1, l,
                            node.value, node.right, node.empty);
                    return Tuple.of(tD._1, tD._2, m);
                } else {
                    final Node<T> tD = new Node<>(node.color, node.blackHeight, l, node.value, node.right, node.empty);
                    return Tuple.of(tD, false, m);
                }
            }
        }

        static <T> Node<T> insert(RedBlackTree<T> tree, T value) {
            if (tree.isEmpty()) {
                final Empty<T> empty = (Empty<T>) tree;
                return new Node<>(RED, 1, empty, value, empty, empty);
            } else {
                final Node<T> node = (Node<T>) tree;
                final int comparison = node.comparator().compare(value, node.value);
                if (comparison < 0) {
                    final Node<T> newLeft = insert(node.left, value);
                    return (newLeft == node.left)
                           ? node
                           : Node.balanceLeft(node.color, node.blackHeight, newLeft, node.value, node.right,
                            node.empty);
                } else if (comparison > 0) {
                    final Node<T> newRight = insert(node.right, value);
                    return (newRight == node.right)
                           ? node
                           : Node.balanceRight(node.color, node.blackHeight, node.left, node.value, newRight,
                            node.empty);
                } else {
                    // DEV-NOTE: Even if there is no _comparison_ difference, the object may not be _equal_.
                    //           To save an equals() call, which may be expensive, we return a new instance.
                    return new Node<>(node.color, node.blackHeight, node.left, value, node.right, node.empty);
                }
            }
        }

        private static boolean isRed(RedBlackTree<?> tree) {
            return !tree.isEmpty() && ((Node<?>) tree).color == RED;
        }

        static <T> RedBlackTree<T> join(RedBlackTree<T> t1, T value, RedBlackTree<T> t2) {
            if (t1.isEmpty()) {
                return t2.insert(value);
            } else if (t2.isEmpty()) {
                return t1.insert(value);
            } else {
                final Node<T> n1 = (Node<T>) t1;
                final Node<T> n2 = (Node<T>) t2;
                final int comparison = n1.blackHeight - n2.blackHeight;
                if (comparison < 0) {
                    return Node.joinLT(n1, value, n2, n1.blackHeight).color(BLACK);
                } else if (comparison > 0) {
                    return Node.joinGT(n1, value, n2, n2.blackHeight).color(BLACK);
                } else {
                    return new Node<>(BLACK, n1.blackHeight + 1, n1, value, n2, n1.empty);
                }
            }
        }

        private static <T> Node<T> joinGT(Node<T> n1, T value, Node<T> n2, int h2) {
            if (n1.blackHeight == h2) {
                return new Node<>(RED, h2 + 1, n1, value, n2, n1.empty);
            } else {
                final Node<T> node = joinGT((Node<T>) n1.right, value, n2, h2);
                return Node.balanceRight(n1.color, n1.blackHeight, n1.left, n1.value, node, n2.empty);
            }
        }

        private static <T> Node<T> joinLT(Node<T> n1, T value, Node<T> n2, int h1) {
            if (n2.blackHeight == h1) {
                return new Node<>(RED, h1 + 1, n1, value, n2, n1.empty);
            } else {
                final Node<T> node = joinLT(n1, value, (Node<T>) n2.left, h1);
                return Node.balanceLeft(n2.color, n2.blackHeight, node, n2.value, n2.right, n2.empty);
            }
        }

        static <T> RedBlackTree<T> merge(RedBlackTree<T> t1, RedBlackTree<T> t2) {
            if (t1.isEmpty()) {
                return t2;
            } else if (t2.isEmpty()) {
                return t1;
            } else {
                final Node<T> n1 = (Node<T>) t1;
                final Node<T> n2 = (Node<T>) t2;
                final int comparison = n1.blackHeight - n2.blackHeight;
                if (comparison < 0) {
                    final Node<T> node = Node.mergeLT(n1, n2, n1.blackHeight);
                    return Node.color(node, BLACK);
                } else if (comparison > 0) {
                    final Node<T> node = Node.mergeGT(n1, n2, n2.blackHeight);
                    return Node.color(node, BLACK);
                } else {
                    final Node<T> node = Node.mergeEQ(n1, n2);
                    return Node.color(node, BLACK);
                }
            }
        }

        private static <T> Node<T> mergeEQ(Node<T> n1, Node<T> n2) {
            final T m = Node.minimum(n2);
            final RedBlackTree<T> t2 = Node.deleteMin(n2)._1;
            final int h2 = t2.isEmpty() ? 0 : ((Node<T>) t2).blackHeight;
            if (n1.blackHeight == h2) {
                return new Node<>(RED, n1.blackHeight + 1, n1, m, t2, n1.empty);
            } else if (isRed(n1.left)) {
                final Node<T> node = new Node<>(BLACK, n1.blackHeight, n1.right, m, t2, n1.empty);
                return new Node<>(RED, n1.blackHeight + 1, Node.color(n1.left, BLACK), n1.value, node, n1.empty);
            } else if (isRed(n1.right)) {
                final RedBlackTree<T> rl = ((Node<T>) n1.right).left;
                final T rx = ((Node<T>) n1.right).value;
                final RedBlackTree<T> rr = ((Node<T>) n1.right).right;
                final Node<T> left = new Node<>(RED, n1.blackHeight, n1.left, n1.value, rl, n1.empty);
                final Node<T> right = new Node<>(RED, n1.blackHeight, rr, m, t2, n1.empty);
                return new Node<>(BLACK, n1.blackHeight, left, rx, right, n1.empty);
            } else {
                return new Node<>(BLACK, n1.blackHeight, n1.color(RED), m, t2, n1.empty);
            }
        }

        private static <T> Node<T> mergeGT(Node<T> n1, Node<T> n2, int h2) {
            if (n1.blackHeight == h2) {
                return Node.mergeEQ(n1, n2);
            } else {
                final Node<T> node = Node.mergeGT((Node<T>) n1.right, n2, h2);
                return Node.balanceRight(n1.color, n1.blackHeight, n1.left, n1.value, node, n1.empty);
            }
        }

        private static <T> Node<T> mergeLT(Node<T> n1, Node<T> n2, int h1) {
            if (n2.blackHeight == h1) {
                return Node.mergeEQ(n1, n2);
            } else {
                final Node<T> node = Node.mergeLT(n1, (Node<T>) n2.left, h1);
                return Node.balanceLeft(n2.color, n2.blackHeight, node, n2.value, n2.right, n2.empty);
            }
        }

        static <T> T maximum(Node<T> node) {
            Node<T> curr = node;
            while (!curr.right.isEmpty()) {
                curr = (Node<T>) curr.right;
            }
            return curr.value;
        }

        static <T> T minimum(Node<T> node) {
            Node<T> curr = node;
            while (!curr.left.isEmpty()) {
                curr = (Node<T>) curr.left;
            }
            return curr.value;
        }

        static <T> Tuple2<RedBlackTree<T>, RedBlackTree<T>> split(RedBlackTree<T> tree, T value) {
            if (tree.isEmpty()) {
                return Tuple.of(tree, tree);
            } else {
                final Node<T> node = (Node<T>) tree;
                final int comparison = node.comparator().compare(value, node.value);
                if (comparison < 0) {
                    final Tuple2<RedBlackTree<T>, RedBlackTree<T>> split = Node.split(node.left, value);
                    return Tuple.of(split._1, Node.join(split._2, node.value, Node.color(node.right, BLACK)));
                } else if (comparison > 0) {
                    final Tuple2<RedBlackTree<T>, RedBlackTree<T>> split = Node.split(node.right, value);
                    return Tuple.of(Node.join(Node.color(node.left, BLACK), node.value, split._1), split._2);
                } else {
                    return Tuple.of(Node.color(node.left, BLACK), Node.color(node.right, BLACK));
                }
            }
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedLeft(Color color, int blackHeight, RedBlackTree<T> left,
                T value, RedBlackTree<T> right, Empty<T> empty) {
            if (!left.isEmpty()) {
                final Node<T> ln = (Node<T>) left;
                if (ln.color == BLACK) {
                    final Node<T> newNode = Node.balanceLeft(BLACK, blackHeight, ln.color(RED), value, right, empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !ln.right.isEmpty()) {
                    final Node<T> lrn = (Node<T>) ln.right;
                    if (lrn.color == BLACK) {
                        final Node<T> newRightNode = Node.balanceLeft(BLACK, blackHeight, lrn.color(RED), value, right,
                                empty);
                        final Node<T> newNode = new Node<>(BLACK, ln.blackHeight, ln.left, ln.value, newRightNode,
                                empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException("unbalancedLeft(" + color + ", " + blackHeight + ", " + left + ", " + value + ", " + right + ")");
        }

        private static <T> Tuple2<Node<T>, Boolean> unbalancedRight(Color color, int blackHeight, RedBlackTree<T> left,
                T value, RedBlackTree<T> right, Empty<T> empty) {
            if (!right.isEmpty()) {
                final Node<T> rn = (Node<T>) right;
                if (rn.color == BLACK) {
                    final Node<T> newNode = Node.balanceRight(BLACK, blackHeight, left, value, rn.color(RED), empty);
                    return Tuple.of(newNode, color == BLACK);
                } else if (color == BLACK && !rn.left.isEmpty()) {
                    final Node<T> rln = (Node<T>) rn.left;
                    if (rln.color == BLACK) {
                        final Node<T> newLeftNode = Node.balanceRight(BLACK, blackHeight, left, value, rln.color(RED),
                                empty);
                        final Node<T> newNode = new Node<>(BLACK, rn.blackHeight, newLeftNode, rn.value, rn.right,
                                empty);
                        return Tuple.of(newNode, false);
                    }
                }
            }
            throw new IllegalStateException("unbalancedRight(" + color + ", " + blackHeight + ", " + left + ", " + value + ", " + right + ")");
        }
    }

    /**
     * The empty tree node. It can't be a singleton because it depends on a {@link Comparator}.
     *
     * @param <T> Component type
     */
    final class Empty<T> implements RedBlackTree<T>, Serializable {

        private static final long serialVersionUID = 1L;

        final Comparator<T> comparator;

        // This is no public API! The RedBlackTree takes care of passing the correct Comparator.
        @SuppressWarnings("unchecked")
        Empty(Comparator<? super T> comparator) {
            this.comparator = (Comparator<T>) comparator;
        }

        @Override
        public Color color() {
            return BLACK;
        }

        @Override
        public Comparator<T> comparator() {
            return comparator;
        }

        @Override
        public boolean contains(T value) {
            return false;
        }

        @Override
        public Empty<T> emptyInstance() {
            return this;
        }

        @Override
        public Option<T> find(T value) {
            return Option.none();
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public RedBlackTree<T> left() {
            throw new UnsupportedOperationException("left on empty");
        }

        @Override
        public RedBlackTree<T> right() {
            throw new UnsupportedOperationException("right on empty");
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public T value() {
            throw new NoSuchElementException("value on empty");
        }

        @Override
        public String toString() {
            return "()";
        }
    }
}
