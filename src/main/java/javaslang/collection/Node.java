/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

/**
 * TODO
 * 
 * @param <T> value type of this tree
 */
public class Node<T> implements TreeLikeStructure<T, Node<T>>, Serializable /* TODO:extends Iterable<T> */{

	private static final long serialVersionUID = 8021802992954631100L;

	private final T value;
	private final List<Node<T>> children;

	// -- constructors + factory methods

	public Node(T value) {
		this(value, List.empty());
	}

	public Node(T value, List<Node<T>> children) {
		this.value = value;
		this.children = children;
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
	 * @param The value of the Node.
	 * @param The children of the Node.
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
	public Node<T> setChildren(List<Node<T>> children) {
		return new Node<>(value, children);
	}

	// -- operations

	@Override
	public Node<T> attach(List<Node<T>> trees) {
		return new Node<>(value, trees.prependAll(children));
	}

	@Override
	public Node<T> detach(List<Node<T>> trees) {
		return new Node<>(value, children.removeAll(trees));
	}

	@Override
	public Node<T> subtree() {
		return this;
	}

	// -- conversion

	public Tree<T> asTree() {
		return new Tree<>(null, value, children.stream().map(child -> asTree(child)).collect(List.collector()));
	}

	// omits updating child refs multiple times when calling asTree()
	private Tree<T> asTree(Node<T> tree) {
		return new Tree<>(null, tree.value, tree.children
				.stream()
				.map(child -> asTree(child))
				.collect(List.collector()), null, null);
	}

	// -- java.lang.Object

	@Override
	public String toString() {
		return stringify();
	}
}
