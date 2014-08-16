/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.require;

import java.io.Serializable;

public final class Tree<T> implements Serializable {

	private static final long serialVersionUID = 3366047180513233623L;

	final Tree<T> parent;
	final T value;
	final List<Tree<T>> children;

	public Tree(T value) {
		this.parent = null;
		this.value = value;
		this.children = List.empty();
	}

	protected Tree(Tree<T> parent, T value, List<Tree<T>> children) {
		this.parent = parent;
		this.value = value;
		this.children = children.replaceAll(child -> setParent(child, this));
	}

	// -- accessors

	public Tree<T> getParent() {
		require(parent != null, "root has no parent");
		return parent;
	}

	public T getValue() {
		return value;
	}

	public List<Tree<T>> getChildren() {
		return children;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	// -- mutators

	@SafeVarargs
	public final Tree<T> attach(Tree<T> tree1, Tree<T>... trees) {
		return new Tree<T>(parent, value, List.of(tree1, trees).prependAll(children));
	}

	@SafeVarargs
	public final Tree<T> detach(Tree<T> tree1, Tree<T>... trees) {
		return new Tree<T>(parent, value, children.removeAll(List.of(tree1, trees)));
	}

	private static <T> Tree<T> setParent(Tree<T> tree, Tree<T> parent) {
		return (tree.parent == parent) ? tree : new Tree<>(parent, tree.value, tree.children);
	}

}
