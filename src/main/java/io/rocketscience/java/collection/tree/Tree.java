package io.rocketscience.java.collection.tree;

import static io.rocketscience.java.lang.Lang.require;
import io.rocketscience.java.util.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Tree<T> {

	public final String id; // identifier, not necessarily unique
	public Tree<T> parent = null;
	private final T value;
	private final List<Tree<T>> children = new ArrayList<>();

	public Tree(String id) {
		this(id, null);
	}
	
	public Tree(String id, T value) {
		require(id != null, "id cannot be null");
		this.id = id;
		this.value = value;
	}
	
	public List<Tree<T>> getChildLeafs() {
		return children.stream().filter(child -> child.isLeaf()).collect(Collectors.<Tree<T>>toList());
	}
	
	public List<Tree<T>> getChildNodes() {
		return children.stream().filter(child -> child.isNode()).collect(Collectors.<Tree<T>>toList());
	}
	
	public List<Tree<T>> children() {
		return children;
	}
	
	public void add(Tree<T> child) {
		require(child.parent == null, "Tree " + child + " has already a parent.");
		children().add(child);
		child.parent = this;
	}
	
	public T value() {
		return value;
	}

	public boolean isNode() {
		return children().size() > 0;
	}

	public boolean isLeaf() {
		return children().size() == 0;
	}

	public boolean isRoot() {
		return parent == null;
	}

	public Tree<T> root() {
		return isRoot() ? this : parent.root();
	}

	/**
	 * Traverses a Tree top down, testing the given predicate against each tree node. If predicate.test() returns true,
	 * descend children, else go on with neighbors.
	 */
	public void traverse(Predicate<Tree<T>> predicate) {
		if (predicate.test(this)) {
			children().forEach(child -> child.traverse(predicate));
		}
	}

	/**
	 * Traverses a Tree top down, applying the given predicate to each tree node. If predicate.test() returns true, the
	 * tree node is part of the result list.
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
		children().forEach(child -> child.collect(predicate, result));
	}

	@Override
	public String toString() {
		return toString(0);
	}

	protected String toString(int depth) {
		final String indent = Strings.space(depth);
		final String inner = children.stream()
				.map(child -> child.toString(depth+1)) // create child strings
				.reduce((l,r) -> l + ",\n" + r) // concatenate child strings
				.map(s -> "\n" + s + "\n" + indent) // apply if concatenation is not empty
				.orElse("");
		final String content = (value == null) ? "" : value.toString().replaceAll("\\s+", " ").trim();
		return indent + id + "(" + content + inner + ")";
	}
	
}
