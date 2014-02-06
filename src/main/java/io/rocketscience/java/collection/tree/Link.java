package io.rocketscience.java.collection.tree;

import io.rocketscience.java.util.Strings;

import java.util.List;

public class Link<T> extends Tree<T> {

	private final Tree<T> ref;

	public Link(Tree<T> ref) {
		super("Link->" + ref.id);
		this.ref = ref;
	}
	
	@Override
	public List<Tree<T>> getChildLeafs() {
		return ref.getChildLeafs();
	}
	
	@Override
	public List<Tree<T>> getChildNodes() {
		return ref.getChildNodes();
	}

	@Override
	public List<Tree<T>> children() {
		return ref.children();
	}

	@Override
	public void add(Tree<T> child) {
		ref.add(child);
	}
	
	@Override
	public T value() {
		return ref.value();
	}
	
	@Override
	protected String toString(int depth) {
		return Strings.space(depth) + id;
	}

}
