/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;

import javaslang.option.Option;

/**
 * TODO
 *
 */
public class BidirectionalTree<T> extends Tree.AbstractTree<T, BidirectionalTree<T>> implements Serializable {

	private static final long serialVersionUID = -5010567338163511359L;

	private final BidirectionalTree<T> parent;
	private final List<BidirectionalTree<T>> children;

	static enum UpdateMode {
		PARENT, CHILDREN, BOTH;
	}

	BidirectionalTree(BidirectionalTree<T> copyOf, BidirectionalTree<T> parent, T value,
			List<BidirectionalTree<T>> children, UpdateMode mode) {
		super(value);
		if (mode == UpdateMode.CHILDREN || parent == null || copyOf == null) {
			this.parent = parent;
		} else {
			final List<BidirectionalTree<T>> childrenOfParent = parent.children.replace(copyOf, this);
			this.parent = new BidirectionalTree<>(parent, parent.parent, parent.value, childrenOfParent,
					UpdateMode.PARENT);
		}
		if (mode == UpdateMode.PARENT) {
			this.children = children;
		} else {
			this.children = children.replaceAll(child -> new BidirectionalTree<>(child, this, child.value,
					child.children, UpdateMode.CHILDREN));
		}
	}

	@Override
	public Option<BidirectionalTree<T>> getParent() {
		return Option.of(parent);
	}

	@Override
	public List<BidirectionalTree<T>> getChildren() {
		return children;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> attach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(this, parent, value, List.of(tree1, trees).prependAll(children), UpdateMode.BOTH);
	}

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> detach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(this, parent, value, children.removeAll(List.of(tree1, trees)), UpdateMode.BOTH);
	}

	@Override
	public BidirectionalTree<T> setChildren(List<BidirectionalTree<T>> children) {
		return new BidirectionalTree<>(this, parent, value, children, UpdateMode.BOTH);
	}

	@Override
	public BidirectionalTree<T> setParent(BidirectionalTree<T> parent) {
		return new BidirectionalTree<>(this, parent, value, children, UpdateMode.BOTH);
	}

	@Override
	public BidirectionalTree<T> setValue(T value) {
		return new BidirectionalTree<>(this, parent, value, children, UpdateMode.BOTH);
	}

	@Override
	public BidirectionalTree<T> subtree() {
		return new BidirectionalTree<>(this, null, value, children, UpdateMode.BOTH);
	}

}
