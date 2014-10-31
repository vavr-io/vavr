/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.generator;

import static java.util.stream.Collectors.joining;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javaslang.Strings;

// TODO:
// - open(s) => depth + 1
// - close(s) => depth - 1
// - type("my.package.Class") => simple name and imports handling
// - new JavaGenerator(fullQualifiedName)
public abstract class _____________JavaGenerator {

	static final int INDENT = 4; // spaces

	final StringBuilder builder = new StringBuilder();

	// TODO:
	//	final String javaPackage;
	//	final String simpleName;
	//
	//	protected _____________JavaGenerator(String fullQualifiedName) {
	//		this.javaPackage = getJavaPackage(fullQualifiedName);
	//		this.simpleName = getSimpleName(fullQualifiedName);
	//	}

	public String generate() {
		gen();
		return builder.toString();
	}

	protected abstract void gen();

	protected void gen(String s, Object... args) {
		builder.append(String.format(s, args));
	}

	protected StringGen<Integer> range(int from, int to) {
		return new StringGen<>(IntStream.rangeClosed(from, to).boxed());
	}

	protected String indent(int i) {
		return Strings.repeat(' ', i * INDENT);
	}

	public static class StringGen<T> {

		final Stream<T> stream;

		StringGen(Stream<T> stream) {
			this.stream = stream;
		}

		public String join(Function<T, String> mapper, String delimiter) {
			return stream.map(t -> mapper.apply(t)).collect(joining(delimiter));
		}

		public String join(Function<T, String> mapper, String delimiter, String prefix, String suffix) {
			return stream.map(t -> mapper.apply(t)).collect(joining(delimiter, prefix, suffix));
		}

		public void forEach(Consumer<T> consumer) {
			stream.forEach(t -> consumer.accept(t));
		}
	}
}
