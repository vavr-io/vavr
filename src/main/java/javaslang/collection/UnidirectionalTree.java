/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

import javaslang.option.Option;

public class UnidirectionalTree<T> implements Tree<T, UnidirectionalTree<T>>, Serializable {

	private static final String UNIDIRECTIONAL_TREE_HAS_NO_PARENT = "unidirectional tree has no parent";

	private static final long serialVersionUID = 317802359716550715L;

	private final T value;
	private final List<UnidirectionalTree<T>> children;

	private UnidirectionalTree(T value, List<UnidirectionalTree<T>> children) {
		this.value = value;
		this.children = children;
	}

	@Override
	public Option<UnidirectionalTree<T>> getParent() {
		throw new UnsupportedOperationException(UNIDIRECTIONAL_TREE_HAS_NO_PARENT);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public List<UnidirectionalTree<T>> getChildren() {
		return children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public UnidirectionalTree<T> attach(UnidirectionalTree<T> tree1, UnidirectionalTree<T>... trees) {
		return new UnidirectionalTree<>(value, List.of(tree1, trees).prependAll(children));
	}

	@Override
	@SuppressWarnings("unchecked")
	public UnidirectionalTree<T> detach(UnidirectionalTree<T> tree1, UnidirectionalTree<T>... trees) {
		return new UnidirectionalTree<>(value, children.removeAll(List.of(tree1, trees)));
	}

	@Override
	public UnidirectionalTree<T> setChildren(List<UnidirectionalTree<T>> children) {
		return new UnidirectionalTree<>(value, children);
	}

	@Override
	public UnidirectionalTree<T> setParent(UnidirectionalTree<T> parent) {
		throw new UnsupportedOperationException(UNIDIRECTIONAL_TREE_HAS_NO_PARENT);
	}

	@Override
	public UnidirectionalTree<T> setValue(T value) {
		return new UnidirectionalTree<>(value, children);
	}

	@Override
	public UnidirectionalTree<T> subtree() {
		return this;
	}

}
