/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

// TODO:
// - open(s) => depth + 1
// - close(s) => depth - 1
// - type("my.package.Class") => simple name and imports handling
abstract class JavaGenerator {

	private static final int INDENT = 4; // spaces

	protected final String className;
	protected final String packageName;

	private final StringBuilder builder = new StringBuilder();

	protected JavaGenerator(String fullQualifiedName) {
		if (fullQualifiedName == null) {
			throw new IllegalArgumentException("fullQualifiedName is null");
		}
		final int index = fullQualifiedName.lastIndexOf('.');
		className = fullQualifiedName.substring(index + 1);
		packageName = (index == -1) ? "" : fullQualifiedName.substring(0, index);
	}

	public void generate(String target) {
		System.out.println(String.format("Generating %s.%s", packageName, className));
		if (target == null) {
			throw new IllegalArgumentException("target is null");
		}
		gen("/**    / \\____  _    ______   _____ / \\____   ____  _____\n");
		gen(" *    /  \\__  \\/ \\  / \\__  \\ /  __//  \\__  \\ /    \\/ __  \\   Javaslang\n");
		gen(" *  _/  // _\\  \\  \\/  / _\\  \\\\_  \\/  // _\\  \\  /\\  \\__/  /   Copyright 2014 Daniel Dietrich\n");
		gen(" * /___/ \\_____/\\____/\\_____/____/\\___\\_____/_/  \\_/____/    Licensed under the Apache License, Version 2.0\n");
		gen(" */\n");
		gen();
		try(BufferedWriter writer = Files.newBufferedWriter(
				Files.createDirectories(Paths.get(target, getDirName())).resolve(getFileName()))) {
			writer.write(builder.toString());
		} catch (IOException x) {
			throw new IllegalStateException(String.format("Error generating %s.%s", packageName, className), x);
		}
	}

	/**
	 * Generator implementation, calls #gen, #range, #format etc.
	 */
	protected abstract void gen();

	protected void gen(String s, Object... args) {
		builder.append(String.format(s, args));
	}

	protected StringGen<Integer> range(int from, int to) {
		return new StringGen<>(IntStream.rangeClosed(from, to).boxed());
	}

	// Shortcut for String#format
	protected String format(String format, Object... args) {
		return String.format(format, args);
	}

	protected String indent(int i) {
		if (i <= 0) {
			return "";
		} else {
			final char[] buf = new char[i * INDENT];
			Arrays.fill(buf, ' ');
			return String.valueOf(buf);
		}
	}

	private String getDirName() {
		return packageName.replaceAll("\\.", File.separator);
	}

	private String getFileName() {
		return className + ".java";
	}

	public static class StringGen<T> {

		final Stream<T> stream;

		StringGen(Stream<T> stream) {
			this.stream = stream;
		}

		public String join(Function<T, String> mapper, String delimiter) {
			return stream.map(mapper::apply).collect(joining(delimiter));
		}

		public String join(Function<T, String> mapper, String delimiter, String prefix, String suffix) {
			return stream.map(mapper::apply).collect(joining(delimiter, prefix, suffix));
		}

		public void forEach(Consumer<T> consumer) {
			stream.forEach(consumer::accept);
		}
	}
}
