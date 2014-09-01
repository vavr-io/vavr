/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static javaslang.Requirements.requireNonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javaslang.collection.TreeLikeStructure.AbstractTreeLikeStructure;
import javaslang.monad.Option;

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

	public Tree(T value) {
		this(null, value, List.of(), TreeTransformer::identity, TreeTransformer::identity);
	}

	public Tree(T value, Iterable<Tree<T>> children) {
		this(null, value, children, TreeTransformer::identity, TreeTransformer::updateChildren);
	}

	public Tree(Tree<T> parent, T value) {
		this(parent, value, List.of(), TreeTransformer::addThisToParent, TreeTransformer::identity);
	}

	public Tree(Tree<T> parent, T value, Iterable<Tree<T>> children) {
		this(parent, value, children, TreeTransformer::addThisToParent, TreeTransformer::updateChildren);
	}

	// DEV-NOTE: beware of NPEs because this is leaving the constructor
	Tree(Tree<T> parent, T value, Iterable<Tree<T>> children, TreeTransformer<T> updateParent,
			TreeTransformer<T> updateChildren) {
		requireNonNull(children, "children is null");
		requireNonNull(updateParent, "updateParent is null");
		requireNonNull(updateChildren, "updateChildren is null");
		this.value = value;
		this.children = List.of(children).replaceAll(updateChildren.apply(this));
		this.parent = Option.of(parent).map(updateParent.apply(this)).orElse(null);
	}

	/**
	 * Factory method for easy tree building.
	 * <p>
	 * <strong>Example:</strong>
	 * <p>
	 * {@code tree("A", tree("B"), tree("C", tree("D"), tree("E", tree("F", tree("G")))))}
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
	 * @param <T> value type of the result Tree
	 * @param value The value of the Tree.
	 * @param children The children of the Tree.
	 * @return A new Tree instance based on the arguments.
	 */
	@SafeVarargs
	public static <T> Tree<T> tree(T value, Tree<T>... children) {
		final List<Tree<T>> childList = List.of(children);
		return new Tree<>(null, value, childList, TreeTransformer::identity, TreeTransformer::updateChildren);
	}

	// -- core

	public Option<Tree<T>> getParent() {
		return Option.of(parent);
	}

	public Tree<T> setParent(Tree<T> parent) {
		if (Objects.equals(this.parent, parent)) {
			return this;
		} else {
			return new Tree<>(parent, value, children, TreeTransformer::addThisToParent,
					TreeTransformer::updateChildren);
		}
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
		return new Tree<>(parent, value, children, TreeTransformer.updateParent(this), TreeTransformer::updateChildren);
	}

	@Override
	public List<Tree<T>> getChildren() {
		return children;
	}

	@Override
	public Tree<T> setChildren(Iterable<Tree<T>> children) {
		return new Tree<>(parent, value, children, TreeTransformer.updateParent(this), TreeTransformer::updateChildren);
	}

	// -- operations

	@Override
	public Tree<T> attach(Iterable<Tree<T>> trees) {
		return new Tree<>(parent, value, List.of(trees).prependAll(children), TreeTransformer.updateParent(this),
				TreeTransformer::updateChildren);
	}

	@Override
	public Tree<T> detach(Iterable<Tree<T>> trees) {
		return new Tree<>(parent, value, children.removeAll(trees), TreeTransformer.updateParent(this),
				TreeTransformer::updateChildren);
	}

	@Override
	public Tree<T> subtree() {
		return new Tree<>(null, value, children, TreeTransformer::identity, TreeTransformer::updateChildren);
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
	static interface TreeTransformer<T> extends Function<Tree<T>, UnaryOperator<Tree<T>>> {

		/**
		 * Use-case: tree tells its children to re-create all their children without re-creating their parents.
		 * <p>
		 * Example: (1 2 3).detach(3)<br>
		 * 
		 * <pre>
		 * <code>
		 *    1
		 *   / \
		 *  2   3
		 * 
		 * new1 = Tree(prev1.parent, prev1.value, updateChildren(prev1.children.remove(3)))
		 * updateChildren(prev2) = new2 = Tree(new1, prev2.value, ())
		 * 
		 *    1
		 *    |
		 *    2
		 * </code>
		 * </pre>
		 * 
		 * @param self
		 * @return
		 */
		static <T> UnaryOperator<Tree<T>> updateChildren(Tree<T> self) {
			return child -> new Tree<>(self, child.value, child.children, TreeTransformer::identity,
					TreeTransformer::updateChildren);
		}

		// Use-case 1: tree passes itself as parent to its children
		// Use-case 2: tree defers decending children, see Node.asTree()
		// TODO: return None instead of identity() to reduce O(n) to O(1)
		static <T> UnaryOperator<Tree<T>> identity(Tree<T> self) {
			return parent -> parent;
		}

		// Use-case: update the whole parent structure but substitute parent.oldChild with this new child
		static <T> TreeTransformer<T> updateParent(Tree<T> prevChild) {
			return self -> parent -> new Tree<>(parent.parent, parent.value, parent.children,
					TreeTransformer.updateParent(parent), TreeTransformer.substitutePreviousChild(prevChild, self));
		}

		// Use-case: existing tree instructs its parent to replace it and re-create the rest of the children
		static <T> TreeTransformer<T> substitutePreviousChild(Tree<T> prevChild, Tree<T> newChild) {
			return self -> child -> (child.equals(prevChild)) ? newChild : new Tree<>(self, child.value,
					child.children, TreeTransformer::identity, TreeTransformer::updateChildren);
		}

		// Use-case: creating new Tree node with a specific parent
		static <T> UnaryOperator<Tree<T>> addThisToParent(Tree<T> self) {
			return parent -> new Tree<>(parent.parent, parent.value, parent.children.append(self),
					TreeTransformer.updateParent(parent), TreeTransformer::updateChildren);
		}
	}
}
