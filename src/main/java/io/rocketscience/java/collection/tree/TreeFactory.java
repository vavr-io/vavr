package io.rocketscience.java.collection.tree;

import static java.util.Arrays.asList;

public abstract class TreeFactory {

	/** Construct a Node. */
	@SafeVarargs
	public static <T> Tree<T> Node(String id, Tree<T>... children) {
		final Tree<T> node = new Tree<T>(id);
		asList(children).forEach(child -> node.add(child));
		return node;
	}

	/** Construct a Leaf. */
	public static <T> Tree<T> Leaf(String id, T value) {
		return new Tree<T>(id, value);
	}

	/** Construct a Link. */
	public static <T, X extends Tree<T>> Tree<T> Link(X ref) {
		return new Link<T>(ref);
	}

}
