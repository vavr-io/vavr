/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

import javaslang.option.Option;

public interface Tree<T, TREE extends Tree<T, ?>> /* TODO:extends Iterable<T> */{

	// -- accessors

	Option<TREE> getParent();

	T getValue();

	List<TREE> getChildren();

	@SuppressWarnings("unchecked")
	default TREE getRoot() {
		return getParent().map(parent -> (TREE) parent.getRoot()).orElse((TREE) this);
	}

	default boolean isRoot() {
		return !getParent().isPresent();
	}

	default boolean isLeaf() {
		return getChildren().isEmpty();
	}

	// -- mutators

	@SuppressWarnings("unchecked")
	TREE attach(TREE tree1, TREE... trees);

	@SuppressWarnings("unchecked")
	TREE detach(TREE tree1, TREE... trees);

	TREE setChildren(List<TREE> children);

	TREE setParent(TREE parent);

	TREE setValue(T value);

	TREE subtree();

	// -- conversion

	BidirectionalTree<T> bidirectional();

	UnidirectionalTree<T> unidirectional();

	//		return bidirectional ? this : new Tree<>(this, null, value, children, true);
	//	}
	//
	//	public Tree<T> unidirectional() {
	//		return bidirectional ? new Tree<>(this, null, value, children, false) : this;
	//	}
	//
	//	// -- conversion and transformation
	//
	//	public Tree<T> bidirectional() {
	//		return bidirectional ? this : new Tree<>(this, null, value, children, true);
	//	}
	//
	//	public Tree<T> unidirectional() {
	//		return bidirectional ? new Tree<>(this, null, value, children, false) : this;
	//	}
	//
	//	/**
	//	 * TODO: (element, index)
	//	 * 
	//	 * @return
	//	 */
	//	public Tree<Tuple2<T, Integer>> zipWithIndex() {
	//		// TODO
	//		throw new UnsupportedOperationException();
	//	}
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
	//	public Tree<Tuple3<T, Integer, Integer>> zipWithCoordinates() {
	//		// TODO
	//		throw new UnsupportedOperationException();
	//	}
	//
	//	// -- traveral
	//
	//	// TODO: see http://rosettacode.org/wiki/Tree_traversal
	//
	//	@Override
	//	public Iterator<T> iterator() {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}
	//	// TODO: stream(), parallelStream(), ...

}
