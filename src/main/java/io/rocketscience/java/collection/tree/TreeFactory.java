package io.rocketscience.java.collection.tree;

import static java.util.Arrays.asList;

public interface TreeFactory {

	/** Construct a Node. */
	@SafeVarargs
	static <T> Tree<T> node(String id, Tree<T>... children) {
		final Tree<T> node = new Tree<T>(id);
		asList(children).forEach(child -> node.add(child));
		return node;
	}

	/** Construct a Leaf. */
	static <T> Tree<T> leaf(String id, T value) {
		return new Tree<T>(id, value);
	}

	/** Construct a Link. */
	static <T, X extends Tree<T>> Tree<T> link(X ref) {
		return new Link<T>(ref);
	}

}
