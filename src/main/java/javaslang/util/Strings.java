package javaslang.util;

import static java.lang.Math.max;
import static java.util.Arrays.asList;
import static java.util.Arrays.fill;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Strings {

	/**
	 * An end of line pattern (mac/unix/win)
	 */
	public static final Pattern EOL = compile("\\r\\n|\\n|\\r");

	private Strings() {
		throw new AssertionError(Strings.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Duplicate a string n times.
	 * 
	 * @param s A String
	 * @param n Duplication count, max be negative or zero.
	 * @return A string, s duplicated n times or null, if s is null. If n is negative or zero, the empty string is
	 *         returned.
	 */
	public static String repeat(String s, int n) {
		if (s == null) {
			return null;
		} else if (n <= 0) {
			return "";
		} else {
			final StringBuilder builder = new StringBuilder();
			for (int i = 0; i < n; i++) {
				builder.append(s);
			}
			return builder.toString();
		}
	}

	/**
	 * Returnes times * 2 spaces.
	 * 
	 * @param times A count of spaces.
	 * @return A string of spaces which has length times.
	 */
	public static String space(int times) {
		if (times <= 0) {
			return "";
		} else {
			final char[] buf = new char[times * 2];
			fill(buf, ' ');
			return String.valueOf(buf);
		}
	}

	/**
	 * Escape backslash '\' and double quote '"'.
	 * 
	 * @param s A string.
	 * @return Escaped string or null if s is null.
	 */
	public static String escape(String s) {
		return (s == null) ? null : s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\\\\\"");
	}

	/**
	 * Computes line and column of index within s.
	 * 
	 * @param s input
	 * @param index <= s.length
	 * @return <code>new int[] { line, column }</code>
	 */
	public static int[] lineAndColumn(String s, int index) {
		final String documentToCursor = s.substring(0, index);
		final Matcher matcher = EOL.matcher(documentToCursor);
		int line = 1;
		for (; matcher.find(); line++)
			;
		final int eol = max(documentToCursor.lastIndexOf("\r"), documentToCursor.lastIndexOf("\n"));
		final int len = documentToCursor.length();
		final int column = (len == 0) ? 1 : len - ((eol == -1) ? 0 : eol);
		return new int[] { line, column };
	}

	/**
	 * Tests if given String is null or empty.
	 * 
	 * @param s A String
	 * @return true, if s is null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(String s) {
		return s == null || "".equals(s);
	}

	/**
	 * Shortcut for
	 * <code>Arrays.asList(array).stream().map(Types::toString).collect(Collectors.joining(delimiter))</code>.
	 */
	public static <T> String mkString(T[] array, CharSequence delimiter) {
		return mkString(asList(array), delimiter);
	}

	/**
	 * Shortcut for
	 * <code>Arrays.asList(array).stream().map(Types::toString).collect(Collectors.joining(delimiter, prefix, suffix))</code>
	 * .
	 */
	public static <T> String mkString(T[] array, CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
		return mkString(asList(array), delimiter);
	}

	/**
	 * Shortcut for <code>collection.stream().map(Types::toString).collect(joining(delimiter))</code>.
	 */
	public static <T> String mkString(Collection<T> collection, CharSequence delimiter) {
		return collection.stream().map(Objects::toString).collect(joining(delimiter));
	}

	/**
	 * Shortcut for <code>collection.stream().map(Types::toString).collect(joining(delimiter, prefix, suffix))</code>.
	 */
	public static <T> String mkString(Collection<T> collection, CharSequence delimiter, CharSequence prefix,
			CharSequence suffix) {
		return collection.stream().map(Objects::toString).collect(joining(delimiter, prefix, suffix));
	}

}
