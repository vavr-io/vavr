/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.parser;

import static javaslang.Lang.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import javaslang.Strings;

/**
 * A tree representation, Inspired by <a
 * href="http://zef.me/4030/avoiding-javascript-pitfalls-through-tree-hugging">treehugger.js</a>.
 *
 * @param <T> Type of tree node values.
 */
// TODO: discouraged impl, goal: create functional javaslang.collection.{List,Set,Map,Tree,...}
class Tree<T> {

	final String id; // identifier, not necessarily unique
	final List<Tree<T>> children = new ArrayList<>();

	// need to be accessible interally for attaching/detaching children
	T value;
	Tree<T> parent = null;

	Tree(String id) {
		this(id, null);
	}

	Tree(String id, T value) {
		requireNonNull(id, "id cannot be null");
		this.id = id;
		this.value = value;
	}

	public List<Tree<T>> getChildren() {
		return Collections.unmodifiableList(children);
	}

	public Tree<T> getParent() {
		return parent;
	}

	/**
	 * Attaches a node to this tree. Detaches the child from its current parent, if present.
	 * 
	 * @param child A Tree.
	 * @return true, if the child was attached, false if it was already attached to this tree.
	 */
	public boolean attach(Tree<T> child) {
		if (children.contains(child)) {
			return false;
		} else {
			child.parent = this;
			return children.add(child); // true
		}
	}

	/**
	 * Detaches a node from this tree.
	 * 
	 * @param child A Tree.
	 * @return true, if the child was successfully detached, false if it is not a child of this
	 *         tree.
	 */
	public boolean detach(Tree<T> child) {
		if (children.remove(child)) {
			child.parent = null;
			return true;
		} else {
			return false;
		}
	}

	public boolean isLeaf() {
		return children.size() == 0;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public Tree<T> getRoot() {
		return (parent == null) ? this : parent.getRoot();
	}

	/**
	 * Convenience method for {@code collect(node -> true)}.
	 * 
	 * @return A list presentation of this tree, collection the children top down.
	 */
	public List<Tree<T>> toList() {
		return collect(node -> true);
	}

	/**
	 * Traverses this tree top down, testing the given predicate against each tree node. If
	 * predicate.test() returns true, descend children, else go on with neighbors.
	 * 
	 * @param predicate Predicate to be tested.
	 */
	public void traverse(Predicate<Tree<T>> predicate) {
		if (predicate.test(this)) {
			children.forEach(child -> child.traverse(predicate));
		}
	}

	/**
	 * Traverses this tree top down, applying the given predicate to each tree node. If
	 * predicate.test() returns true, the tree node is part of the result list.
	 * 
	 * @param predicate Predicate to be tested.
	 * @return A list of tree nodes matching the given predicate.
	 */
	public List<Tree<T>> collect(Predicate<Tree<T>> predicate) {
		final List<Tree<T>> result = new ArrayList<>();
		collect(predicate, result);
		return result;
	}

	private void collect(Predicate<Tree<T>> predicate, List<Tree<T>> result) {
		if (predicate.test(this)) {
			result.add(this);
		}
		children.forEach(child -> child.collect(predicate, result));
	}

	@Override
	public String toString() {
		return toString(0);
	}

	protected String toString(int depth) {
		final String indent = Strings.repeat(' ', depth * 2);
		final String inner = children.stream()
		// create child strings
				.map(child -> child.toString(depth + 1))
				// combine strings
				.reduce((l, r) -> l + ",\n" + r)
				// concatenate child strings
				.map(s -> "\n" + s + "\n" + indent)
				// apply if concatenation is not empty
				.orElse("");
		final String content = (value == null) ? "" : value
				.toString()
				.replaceAll("\\s+", " ")
				.trim();
		return indent + id + "(" + content + inner + ")";
	}

}
