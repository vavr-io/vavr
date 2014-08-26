/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.io.Serializable;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javaslang.collection.TreeLikeStructure.AbstractTreeLikeStructure;
import javaslang.option.Option;

/**
 * Because this is an immutable, bidirectional tree implementation, tree operations on any node result in creating new
 * instances of all tree node. In particular, the original tree nodes stay uneffected.
 * <p>
 * <strong>Hint:</strong> Please take a look how to build a Tree with {@link Node#node(Object, Node...)} and
 * {@link Node#asTree()} in O(n).
 *
 * @param <T> value type of this tree
 */
public class Tree<T> extends AbstractTreeLikeStructure<T, Tree<T>> implements Serializable /* TODO:extends Iterable<T> */{

	private static final long serialVersionUID = -7482343083286183794L;

	private final Tree<T> parent;
	private final T value;
	private final List<Tree<T>> children;

	// -- constructors + factory methods

	// Shortcut for {@code Tree<>(null, value, List.empty())}.
	public Tree(T value) {
		this(null, value, List.empty());
	}

	// Shortcut for {@code Tree<>(parent, value, List.empty())}.
	public Tree(Tree<T> parent, T value) {
		this(parent, value, List.empty());
	}

	// Shortcut for {@code Tree<>(null, value, List.empty())}.
	public Tree(T value, Iterable<Tree<T>> children) {
		this(null, value, children);
	}

	public Tree(Tree<T> parent, T value, Iterable<Tree<T>> children) {
		this(parent, value, children, TreeTransformer::updateParent, TreeTransformer::updateChildren);
	}

	Tree(Tree<T> parent, T value, Iterable<Tree<T>> children, TreeTransformer<T> updateParent,
			TreeTransformer<T> updateChildren) {
		requireNonNull(children, "children is null");
		this.value = value;
		this.parent = (updateParent == null) ? parent : Option.of(parent).map(updateParent.apply(this)).orElse(null);
		this.children = (updateChildren == null) ? List.of(children) : List.of(children).replaceAll(
				updateChildren.apply(this));
	}

	// -- core

	public Option<Tree<T>> getParent() {
		return Option.of(parent);
	}

	public Tree<T> setParent(Tree<T> parent) {
		return new Tree<>(parent, value, children, TreeTransformer::updateParent, TreeTransformer::updateChildren);
	}

	public Tree<T> getRoot() {
		Tree<T> tree = this;
		while (tree.parent != null) {
			tree = tree.parent;
		}
		return tree;
	}

	public boolean isRoot() {
		return parent == null;
	}

	@Override
	public T getValue() {
		return value;
	}

	@Override
	public Tree<T> setValue(T value) {
		return new Tree<>(parent, value, children);
	}

	@Override
	public List<Tree<T>> getChildren() {
		return children;
	}

	@Override
	public Tree<T> setChildren(Iterable<Tree<T>> children) {
		return new Tree<>(parent, value, children);
	}

	// -- operations

	@Override
	public Tree<T> attach(Iterable<Tree<T>> trees) {
		return new Tree<>(parent, value, List.of(trees).prependAll(children));
	}

	@Override
	public Tree<T> detach(Iterable<Tree<T>> trees) {
		return new Tree<>(parent, value, children.removeAll(trees));
	}

	@Override
	public Tree<T> subtree() {
		return new Tree<>(null, value, children);
	}

	// -- conversion

	public Node<T> asNode() {
		return new Node<T>(value, children.stream().map(child -> child.asNode()).collect(List.collector()));
	}

	// -- transformation

	/**
	 * Manifest-type for and holder of Tree transformations.
	 * 
	 * @param <T>
	 */
	private static interface TreeTransformer<T> extends Function<Tree<T>, UnaryOperator<Tree<T>>> {

		// use-case: tree tells parent how to re-create its parent 
		static <T> UnaryOperator<Tree<T>> updateParent(Tree<T> self) {
			return parent -> new Tree<>(parent.parent, parent.value, parent.children, TreeTransformer::updateParent,
					TreeTransformer.substitutePreviousChild(parent));
		}

		// use-case: existing tree instructs its parent to replace it and re-create the rest of the children
		static <T> TreeTransformer<T> substitutePreviousChild(Tree<T> prevChild) {
			return self -> child -> (child == prevChild) ? self : new Tree<>(self, child.value, child.children,
					TreeTransformer::keepParent, TreeTransformer::updateChildren);
		}

		// use-case: tree passes itself as parent to its children
		static <T> UnaryOperator<Tree<T>> keepParent(Tree<T> self) {
			return parent -> self;
		}

		// use-case: tree tells its children to re-create all their children without re-creating their parents
		static <T> UnaryOperator<Tree<T>> updateChildren(Tree<T> self) {
			return child -> new Tree<>(self, child.value, child.children, TreeTransformer::keepParent,
					TreeTransformer::updateChildren);
		}
	}
}
