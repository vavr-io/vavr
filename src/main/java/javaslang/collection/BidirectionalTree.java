/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javaslang.collection.Tree.TreeWithParent;
import javaslang.option.Option;

public class BidirectionalTree<T> implements TreeWithParent<T, BidirectionalTree<T>>, Serializable {

	private static final long serialVersionUID = -5010567338163511359L;

	private final BidirectionalTree<T> parent;
	private final T value;
	private final List<BidirectionalTree<T>> children;

	// DEV-NOTE: should be used internally by UnidirectionalTree.bidirectional() only!
	BidirectionalTree(T value, List<BidirectionalTree<T>> children) {
		this.parent = null;
		this.value = value;
		this.children = children;
	}

	// Shortcut for {@code BidirectionalTree<>(parent, value, children, TreeTransformer::updateParent, TreeTransformer::updateChildren)}.
	BidirectionalTree(BidirectionalTree<T> parent, T value, List<BidirectionalTree<T>> children) {
		this(parent, value, children, TreeTransformer::updateParent, TreeTransformer::updateChildren);
	}

	private BidirectionalTree(BidirectionalTree<T> parent, T value, List<BidirectionalTree<T>> children,
			TreeTransformer<T> updateParent, TreeTransformer<T> updateChildren) {
		this.value = value;
		this.parent = Option.of(parent).map(updateParent.apply(this)).orElse(null);
		this.children = children.replaceAll(updateChildren.apply(this));
	}

	// -- core

	@Override
	public Option<BidirectionalTree<T>> getParent() {
		return Option.of(parent);
	}

	@Override
	public BidirectionalTree<T> setParent(BidirectionalTree<T> parent) {
		return new BidirectionalTree<>(parent, value, children, TreeTransformer::updateParent,
				TreeTransformer::updateChildren);
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public BidirectionalTree<T> setValue(T value) {
		return new BidirectionalTree<>(parent, value, children);
	}

	@Override
	public List<BidirectionalTree<T>> getChildren() {
		return children;
	}

	@Override
	public BidirectionalTree<T> setChildren(List<BidirectionalTree<T>> children) {
		return new BidirectionalTree<>(parent, value, children);
	}

	// -- operations

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> attach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(parent, value, List.of(tree1, trees).prependAll(children));
	}

	@Override
	@SuppressWarnings("unchecked")
	public BidirectionalTree<T> detach(BidirectionalTree<T> tree1, BidirectionalTree<T>... trees) {
		return new BidirectionalTree<>(parent, value, children.removeAll(List.of(tree1, trees)));
	}

	@Override
	public BidirectionalTree<T> subtree() {
		return new BidirectionalTree<>(null, value, children);
	}

	// -- conversion

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

	// -- transformation

	/**
	 * Manifest-type for and holder of Tree transformations.
	 * 
	 * @param <T>
	 */
	private static interface TreeTransformer<T> extends
			Function<BidirectionalTree<T>, UnaryOperator<BidirectionalTree<T>>> {

		// use-case: tree tells parent how to re-create its parent 
		static <T> UnaryOperator<BidirectionalTree<T>> updateParent(BidirectionalTree<T> self) {
			return parent -> new BidirectionalTree<>(parent.parent, parent.value, parent.children,
					TreeTransformer::updateParent, TreeTransformer.substitutePreviousChild(parent));
		}

		// use-case: existing tree instructs its parent to replace it and re-create the rest of the children
		static <T> TreeTransformer<T> substitutePreviousChild(BidirectionalTree<T> prevChild) {
			return self -> child -> (child == prevChild) ? self : new BidirectionalTree<>(self, child.value,
					child.children, TreeTransformer::keepParent, TreeTransformer::updateChildren);
		}

		// use-case: tree passes itself as parent to its children
		static <T> UnaryOperator<BidirectionalTree<T>> keepParent(BidirectionalTree<T> self) {
			return parent -> self;
		}

		// use-case: tree tells its children to re-create all their children without re-creating their parents
		static <T> UnaryOperator<BidirectionalTree<T>> updateChildren(BidirectionalTree<T> self) {
			return child -> new BidirectionalTree<>(self, child.value, child.children, TreeTransformer::keepParent,
					TreeTransformer::updateChildren);
		}
	}
}
