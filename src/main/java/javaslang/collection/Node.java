/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.io.Serializable;

import javaslang.collection.Tree.TreeTransformer;
import javaslang.collection.TreeLikeStructure.AbstractTreeLikeStructure;

/**
 * TODO
 * 
 * @param <T> value type of this tree
 */
public class Node<T> extends AbstractTreeLikeStructure<T, Node<T>> implements Serializable /* TODO:extends Iterable<T> */{

	private static final long serialVersionUID = 8021802992954631100L;

	private final T value;
	private final List<Node<T>> children;

	// -- constructors + factory methods

	public Node(T value) {
		this(value, List.of());
	}

	public Node(T value, Iterable<Node<T>> children) {
		requireNonNull(children, "children is null");
		this.value = value;
		this.children = List.of(children);
	}

	/**
	 * Factory method for easy tree building.
	 * <p>
	 * <strong>Example:</strong>
	 * <p>
	 * {@code node("A", node("B"), node("C", node("D"), node("E", node("F", node("G")))))}
	 * <p>
	 * results in:
	 * 
	 * <pre>
	 * <code>
	 *   A
	 *  / \
	 * B   C
	 *    / \
	 *   D   E
	 *       |
	 *       F
	 *       |
	 *       G
	 * </code>
	 * </pre>
	 * 
	 * @param <T> value type of the result Node
	 * @param value The value of the Node.
	 * @param children The children of the Node.
	 * @return A new Node instance based on the arguments.
	 */
	@SafeVarargs
	public static <T> Node<T> node(T value, Node<T>... children) {
		return new Node<>(value, List.of(children));
	}

	// -- core

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Node<T> setValue(T value) {
		return new Node<>(value, children);
	}

	@Override
	public List<Node<T>> getChildren() {
		return children;
	}

	@Override
	public Node<T> setChildren(Iterable<Node<T>> children) {
		return new Node<>(value, children);
	}

	// -- operations

	@Override
	public Node<T> attach(Iterable<Node<T>> nodes) {
		return new Node<>(value, List.of(nodes).prependAll(children));
	}

	@Override
	public Node<T> detach(Iterable<Node<T>> trees) {
		return new Node<>(value, children.removeAll(trees));
	}

	@Override
	public Node<T> subtree() {
		return this;
	}

	// -- conversion

	public Tree<T> asTree() {
		final List<Tree<T>> treeChildren = children.stream().map(child -> asTree(child)).collect(List.collector());
		return new Tree<>(null, value, treeChildren, TreeTransformer::identity, TreeTransformer::updateChildren);
	}

	// DEV-NOTE: omits updating parent and child refs / builds Tree in O(n)
	private Tree<T> asTree(Node<T> node) {
		final List<Tree<T>> treeChildren = node.children.stream().map(child -> asTree(child)).collect(List.collector());
		return new Tree<>(null, node.value, treeChildren, TreeTransformer::identity, TreeTransformer::identity);
	}
}
