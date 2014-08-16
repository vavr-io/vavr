/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.require;

import java.io.Serializable;
import java.util.function.UnaryOperator;

import javaslang.option.Option;

public class Tree<T> implements Serializable {

	private static final long serialVersionUID = 3366047180513233623L;

	private static final String UNIDIRECTIONAL_ERROR = "unidirectional tree has no parent ref";

	private final boolean bidirectional;
	private final Tree<T> parent;
	private final T value;
	private final List<Tree<T>> children;

	public Tree(T value) {
		this(value, false);
	}

	public Tree(T value, boolean bidirectional) {
		this.bidirectional = bidirectional;
		this.parent = null;
		this.value = value;
		this.children = List.empty();
	}

	protected Tree(Tree<T> parent, T value, List<Tree<T>> children, boolean bidirectional) {
		this.bidirectional = bidirectional;
		this.parent = parent; // TODO: (bidirectional && parent != null) ? parent.updateChild(oldChild, newChild) : parent;
		this.value = value;
		if (bidirectional) {
			final UnaryOperator<Tree<T>> f = child -> new Tree<>(this, child.value, child.children, bidirectional);
			this.children = children.replaceAll(f);
		} else {
			this.children = children;
		}
	}

	// -- accessors

	public boolean isBidirectional() {
		return bidirectional;
	}

	public Option<Tree<T>> getParent() {
		require(bidirectional, UNIDIRECTIONAL_ERROR);
		return Option.of(parent);
	}

	public T getValue() {
		return value;
	}

	public List<Tree<T>> getChildren() {
		return children;
	}

	public boolean isRoot() {
		require(bidirectional, UNIDIRECTIONAL_ERROR);
		return parent == null;
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	// -- mutators

	public Tree<T> attach(Tree<T> tree1, @SuppressWarnings("unchecked") Tree<T>... trees) {
		return new Tree<>(parent, value, List.of(tree1, trees).prependAll(children), bidirectional);
	}

	public Tree<T> detach(Tree<T> tree1, @SuppressWarnings("unchecked") Tree<T>... trees) {
		return new Tree<>(parent, value, children.removeAll(List.of(tree1, trees)), bidirectional);
	}

	public Tree<T> setChildren(List<Tree<T>> children) {
		return new Tree<>(parent, value, children, bidirectional);
	}

	public Tree<T> subtree() {
		final boolean hasParentRef = bidirectional && parent != null;
		if (hasParentRef) {
			return new Tree<>(null, value, children, bidirectional);
		} else {
			return this;
		}
	}

	public Tree<T> bidirectional() {
		return bidirectional ? this : new Tree<>(null, value, children, true);
	}

	public Tree<T> unidirectional() {
		return bidirectional ? new Tree<>(null, value, children, false) : this;
	}

}
