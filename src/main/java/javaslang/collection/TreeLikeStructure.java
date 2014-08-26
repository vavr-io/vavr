/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import static java.util.stream.Collectors.joining;

import java.util.Objects;
import java.util.function.Consumer;

import javaslang.Strings;

/**
 * A list based n-ary tree.
 * <p>
 * According to <a href="http://en.m.wikipedia.org/wiki/Tree_data_structure">wikipedia</a> "..., looking at a tree as a
 * whole, one can talk about 'the parent node' of a given node, but in general as a data structure a given node only
 * contains the list of its children, but does not contain a reference to its parent (if any)."
 *
 * @param <T> value type of this tree structure
 * @param <TREE> type of the tree structure implementation
 */
interface TreeLikeStructure<T, TREE extends TreeLikeStructure<T, ?>> {

	// -- core tree API

	T getValue();

	TREE setValue(T value);

	List<TREE> getChildren();

	default int getChildCount() {
		return getChildren().size();
	}

	TREE setChildren(List<TREE> children);

	default boolean isLeaf() {
		return getChildren().isEmpty();
	}

	// -- tree operations

	default TREE attach(TREE tree) {
		return attach(List.of(tree));
	}

	TREE attach(List<TREE> trees);

	default TREE detach(TREE tree) {
		return detach(List.of(tree));
	}

	TREE detach(List<TREE> trees);

	TREE subtree();

	// TODO: stream(), iterator() : Enumerating all the items
	// TODO: subtree() : Enumerating a section of a tree
	// TODO: find() : Searching for an item
	// TODO: Adding a new item at a certain position on the tree
	// TODO: Deleting an item
	// TODO: Pruning: Removing a whole section of a tree
	// TODO: Grafting: Adding a whole section to a tree
	// TODO: getRoot() Finding the root for any node

	//	Tree<Tuple2<T, Integer>> zipWithIndex();
	//
	//	/**
	//	 * TODO: (element, depth, index)
	//	 * <p>
	//	 * <strong>Using Breadth-First Search (BFS):</strong>
	//	 * 
	//	 * <pre>
	//	 * <code>
	//	 *                 (e1,0,0)
	//	 *                /        \
	//	 *        (e2,1,0)          (e3,1,1)
	//	 *        /      \          /      \
	//	 *    (e4,2,0) (e5,2,1) (e6,2,2) (e7,2,3)
	//	 * </code>
	//	 * </pre>
	//	 * 
	//	 * @return
	//	 */
	//	Tree<Tuple3<T, Integer, Integer>> zipWithCoordinates();
	//

	// -- traveral

	// TODO: see http://rosettacode.org/wiki/Tree_traversal

	default void traverse(Consumer<TREE> consumer) {
		// TODO
	}

	default void traverse(Traversal traversal, Consumer<TREE> consumer) {
		// TODO
	}

	static enum Traversal {

		PREORDER, INORDER, POSTORDER, LEVEL_ORDER;
	}

	// -- streaming 

	// TODO: stream(), parallelStream(), ...

	/**
	 * This class is needed because the interface {@link TreeLikeStructure} cannot use default methods to override
	 * Object's non-final methods equals, hashCode and toString.
	 * <p>
	 * See <a href="http://mail.openjdk.java.net/pipermail/lambda-dev/2013-March/008435.html">Allow default methods to
	 * override Object's methods</a>.
	 * 
	 * @param <T> value type of this tree structure
	 * @param <TREE> type of the tree structure implementation
	 */
	static abstract class AbstractTreeLikeStructure<T, TREE extends AbstractTreeLikeStructure<T, ?>> implements
			TreeLikeStructure<T, TREE> {

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (o == null || !(getClass().isAssignableFrom(o.getClass()))) {
				return false;
			} else {
				final Tree<?> that = (Tree<?>) o;
				return Objects.equals(getValue(), that.getValue()) && Objects.equals(getChildren(), that.getChildren());
			}
		}

		@Override
		public int hashCode() {
			final T value = getValue();
			return (value == null ? 0 : value.hashCode() * 31) + getChildren().hashCode();
		}

		@Override
		public String toString() {
			return toLispString();
		}

		/**
		 * Prints the complete tree in LISP format {@code (root child1 .. childN)}. Prints just the value if node is a
		 * leaf.
		 * 
		 * @return The string representation of this tree structure in LISP format.
		 */
		public String toLispString() {
			final String value = Strings.toString(getValue());
			if (isLeaf()) {
				return value;
			} else {
				final String children = getChildren()
						.stream()
						.map(AbstractTreeLikeStructure::toString)
						.collect(joining(" "));
				return String.format("(%s %s)", value, children);
			}
		}

		public String toTreeString() {
			return toTreeString(0);
		}

		protected String toTreeString(int depth) {
			final String indent = Strings.repeat(' ', depth * 2);
			final String value = Strings.toString(getValue()).replaceAll("\\s+", " ").trim();
			if (isLeaf()) {
				return indent + value;
			} else {
				final String children = getChildren()
						.stream()
						.map(child -> child.toTreeString(depth + 1))
						.reduce((l, r) -> l + ",\n" + r)
						.map(s -> "\n" + s + "\n" + indent)
						.orElse("");
				return String.format("%s(%s %s)", indent, value, children);
			}
		}
	}
}
