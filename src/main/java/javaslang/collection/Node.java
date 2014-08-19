/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

/**
 * According to <a href="http://en.m.wikipedia.org/wiki/Tree_data_structure">wikipedia</a> "..., looking at a tree as a
 * whole, one can talk about 'the parent node' of a given node, but in general as a data structure a given node only
 * contains the list of its children, but does not contain a reference to its parent (if any)."
 *
 * @param <T>
 */
public class Node<T> implements TreeLikeStructure<T, Node<T>>, Serializable /* TODO:extends Iterable<T> */{

	private static final long serialVersionUID = 8021802992954631100L;

	private final T value;
	private final List<Node<T>> children;

	// -- constructors

	public Node(T value) {
		this(value, List.empty());
	}

	public Node(T value, List<Node<T>> children) {
		this.value = value;
		this.children = children;
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
	@SuppressWarnings("unchecked")
	public Node<T> attach(Node<T> tree1, Node<T>... trees) {
		return new Node<>(value, List.of(tree1, trees).prependAll(children));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Node<T> detach(Node<T> tree1, Node<T>... trees) {
		return new Node<>(value, children.removeAll(List.of(tree1, trees)));
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
}
