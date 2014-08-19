/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.collection;

/**
 * According to <a href="http://en.m.wikipedia.org/wiki/Tree_data_structure">wikipedia</a> "..., looking at a tree as a
 * whole, one can talk about 'the parent node' of a given node, but in general as a data structure a given node only
 * contains the list of its children, but does not contain a reference to its parent (if any)."
 *
 * @param <T>
 * @param <TREE>
 */
interface TreeLikeStructure<T, TREE extends TreeLikeStructure<T, ?>> {

	// -- core

	T getValue();

	TREE setValue(T value);

	List<TREE> getChildren();

	TREE setChildren(List<TREE> children);

	default boolean isLeaf() {
		return getChildren().isEmpty();
	}

	// -- operations

	@SuppressWarnings("unchecked")
	TREE attach(TREE tree1, TREE... trees);

	@SuppressWarnings("unchecked")
	TREE detach(TREE tree1, TREE... trees);

	TREE subtree();

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
	//	// -- traveral
	//
	//	// TODO: see http://rosettacode.org/wiki/Tree_traversal
	//
	//	// TODO: stream(), parallelStream(), ...

}
