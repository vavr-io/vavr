/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.io.Serializable;

import javaslang.option.Option;

/**
 * TODO
 *
 */
public class BidirectionalTree<T> implements Tree<T, BidirectionalTree<T>>, Serializable {

	private static final long serialVersionUID = -5010567338163511359L;

	static final int UPDATE_NONE = 0;
	static final int UPDATE_PARENT = 1;
	static final int UPDATE_CHILDREN = 2;
	static final int UPDATE_BOTH = UPDATE_PARENT | UPDATE_CHILDREN;

	private final BidirectionalTree<T> parent;
	private final T value;
	private final List<BidirectionalTree<T>> children;

	// DEV-NOTE: caution, because 'this' leaves constructor
	BidirectionalTree(BidirectionalTree<T> copyOf, BidirectionalTree<T> parent, T value,
			List<BidirectionalTree<T>> children, int mode) {
		requireNonNull(children, "children is null");

		this.value = value;

		final boolean updateParent = (mode & UPDATE_PARENT) == UPDATE_PARENT && parent != null && copyOf != null;
		if (updateParent) {
			final List<BidirectionalTree<T>> childrenOfParent = parent.children.replace(copyOf, this);
			this.parent = new BidirectionalTree<>(parent, parent.parent, parent.value, childrenOfParent, UPDATE_PARENT);
		} else {
			this.parent = parent;
		}

		final boolean updateChildren = (mode & UPDATE_CHILDREN) == UPDATE_CHILDREN && !children.isEmpty();
		if (updateChildren) {
			this.children = children.replaceAll(child -> new BidirectionalTree<>(child, this, child.value,
					child.children, UPDATE_CHILDREN));
		} else {
			this.children = children;
		}
	}

	@Override
	public Option<BidirectionalTree<T>> getParent() {
		return Option.of(parent);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public List<BidirectionalTree<T>> getChildren() {
		return children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> attach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(this, parent, value, List.of(tree1, trees).prependAll(children), UPDATE_BOTH);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> detach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(this, parent, value, children.removeAll(List.of(tree1, trees)), UPDATE_BOTH);
	}

	@Override
	public BidirectionalTree<T> setChildren(List<BidirectionalTree<T>> children) {
		return new BidirectionalTree<>(this, parent, value, children, UPDATE_BOTH);
	}

	@Override
	public BidirectionalTree<T> setParent(BidirectionalTree<T> parent) {
		return new BidirectionalTree<>(this, parent, value, children, UPDATE_BOTH);
	}

	@Override
	public BidirectionalTree<T> setValue(T value) {
		return new BidirectionalTree<>(this, parent, value, children, UPDATE_BOTH);
	}

	@Override
	public BidirectionalTree<T> subtree() {
		return new BidirectionalTree<>(this, null, value, children, UPDATE_BOTH);
	}

	@Override
	public BidirectionalTree<T> bidirectional() {
		return this;
	}

	@Override
	public UnidirectionalTree<T> unidirectional() {
		return new UnidirectionalTree<T>(value, children
				.stream()
				.map(child -> child.unidirectional())
				.collect(List.collector()));
	}
}
