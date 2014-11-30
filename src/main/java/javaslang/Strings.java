/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import javaslang.Tuple.Tuple2;
import javaslang.match.Match;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Extension methods for {@link java.lang.String}.
 */
public final class Strings {

    /**
     * Needed to detect indirect loops on recursive calls of {@link #toString(Object)}.
     */
    private static final ThreadLocal<Set<Object>> toStringVisited = ThreadLocal.withInitial(HashSet::new);

    /**
     * This class is not intended to be instantiated.
     */
    private Strings() {
        throw new AssertionError(Strings.class.getName() + " is not intended to be instantiated.");
    }

    /**
     * Returns a deep string representation of an Object.
     *
     * @param o An Object.
     * @return A deep string representation of o.
     */
    public static String toString(Object o) {
        final Set<Object> visited = toStringVisited.get();
        final boolean isEntryPoint = visited.isEmpty();
        try {
            return toString(o, visited);
        } finally {
            if (isEntryPoint) {
                visited.clear();
            }
        }
    }

    /**
     * Duplicate a string n times.
     *
     * @param s A String
     * @param n Duplication count, may be negative or zero.
     * @return A string, s duplicated n times or null, if s is null. If n is negative or zero, the empty string is
     * returned.
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
     * Duplicates a char n times.
     *
     * @param c A char.
     * @param n A count of spaces, may be negative or zero.
     * @return A string, c duplicated n times. If n is negative or zero, the empty string is returned.
     */
    public static String repeat(char c, int n) {
        if (n <= 0) {
            return "";
        } else {
            final char[] buf = new char[n];
            Arrays.fill(buf, c);
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
     * Escapes occurrences of character within s with the given escape character. The escape character is also escaped.
     *
     * @param s         The String to be escaped.
     * @param character The character to be escaped.
     * @param escape    The escape character.
     * @return The escaped String.
     */
    public static String escape(String s, char character, char escape) {
        if (s == null) {
            return null;
        } else {
            final IntFunction<String> escaper = i -> {
                final char c = (char) i;
                return (c == character || c == escape) ? ("" + escape + c) : ("" + c);
            };
            // TODO: Stream.of(s).map(c -> (c == character || c == escape) ? ("" + escape + c) : ("" + c)).join()
            return s.chars().mapToObj(escaper).collect(Collectors.joining());
        }
    }

    /**
     * Computes line and column of index within a given text.
     * <p/>
     * Line breaks:
     * <ul>
     * <li>{@code \r\n (Windows, DOS, OS/2, CP/M, TOS (Atari))}</li>
     * <li>{@code \n} (Unix, Linux, Android, Mac OS X, AmigaOS, BSD, etc.)</li>
     * <li>{@code \r} ((Mac OS up to Version 9, Apple II, C64))</li>
     * </ul>
     * <p/>
     * Examples:
     * <ul>
     * <li>{@code lineAndColumn(null, 0)} throws 's is null'</li>
     * <li>{@code lineAndColumn(emptyDocument, 0)} = (1,1)</li>
     * <li>{@code lineAndColumn("text", 0)} = (1,1)</li>
     * <li>{@code lineAndColumn("text", 1)} = (1,2)</li>
     * <li>{@code lineAndColumn("text", "text".length())} = (1,5)</li>
     * <li>{@code lineAndColumn("lorem\nipsum", "lorem\nipsum".indexOf('\n'))} = (1,6)</li>
     * <li>{@code lineAndColumn("lorem\nipsum", "lorem\nipsum".indexOf('\n') + 1)} = (2,1)</li>
     * </ul>
     *
     * @param s     input
     * @param index {@code <= s.length}
     * @return {@code new int[] line, column }
     */
    public static Tuple2<Integer, Integer> lineAndColumn(String s, int index) {
        final String text = Require.nonNull(s, "s is null").substring(0, index);
        final int line = ("$" + text + "$").split("\\r\\n|\\n|\\r").length;
        final int column = text.length() - Math.max(text.lastIndexOf("\r"), text.lastIndexOf("\n"));
        return Tuple.of(line, column);
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
     * Concatenates the given strings using the given separator character. Occurrences of escape or separator within a
     * string are escaped with the given escape character. Shortcut for
     * {@code Stream.of(strings).map(s -> Strings.escape(s, separator, escape)).collect(Collectors.joining(String.valueOf(separator)))}
     * .
     *
     * @param strings   An array of Strings, not null.
     * @param separator A separator character.
     * @param escape    An escape character.
     * @return The concatenation of the escaped strings using the given separator and escape character.
     * @throws IllegalArgumentException If separator equals escape.
     * @throws NullPointerException     If strings is null.
     */
    public static String join(String[] strings, char separator, char escape) {
        Require.nonNull(strings, "strings is null");
        Require.isTrue(separator != escape, "separator equals escape charater");
        return Stream
                .of(strings)
                .map(s -> escape(s, separator, escape))
                .collect(Collectors.joining(String.valueOf(separator)));
    }

    /**
     * Concatenates a given Iterable of strings using the given separator character. Occurrences of escape or separator
     * within a string are escaped with the given escape character. Shortcut for
     * {@code Streamz.stream(strings).map(s -> Strings.escape(s, separator, escape)).collect(Collectors.joining(String.valueOf(separator)))}
     * .
     *
     * @param strings   An Iterable of Strings, not null.
     * @param separator A separator character.
     * @param escape    An escape character.
     * @return The concatenation of the escaped strings using the given separator and escape character.
     * @throws IllegalArgumentException If separator equals escape.
     * @throws NullPointerException     If strings is null.
     */
    public static String join(Iterable<String> strings, char separator, char escape) {
        Require.nonNull(strings, "strings is null");
        Require.isTrue(separator != escape, "separator equals escape charater");
        return StreamSupport.stream(strings.spliterator(), false)
                .map(s -> escape(s, separator, escape))
                .collect(Collectors.joining(String.valueOf(separator)));
    }

    /**
     * Splits a string using a specific separator. By definition, a separator separates two string. This leads to the
     * following:
     * <p/>
     * <ul>
     * <li>split("", "#") = [""]</li>
     * <li>split("#", "#") = ["", ""]</li>
     * <li>split("##", "#") = ["", "" ,""]</li>
     * <li>split("123##456", "#") = ["123", "" ,"456"]</li>
     * <li>split("123##456", "##") = ["123", "456"]</li>
     * <li>split("#123#", "#") = ["", "123", ""]</li>
     * </ul>
     *
     * @param string    A string to be splitted, not null.
     * @param separator A token separator, not null and not empty.
     * @return The tokens contained in string, without separators.
     */
    public static String[] split(String string, String separator) {
        Require.nonNull(string, "string is null");
        Require.notNullOrEmpty(separator, "separator is empty");
        final List<String> tokens = new ArrayList<String>();
        int fromIndex = 0;
        int index;
        while ((index = string.indexOf(separator, fromIndex)) != -1) {
            final String finding = string.substring(fromIndex, index);
            tokens.add(finding);
            fromIndex = index + separator.length();
        }
        final String rest = string.substring(fromIndex);
        tokens.add(rest);
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Splits a string which consists of zero or more parts concatenated with the given separator. Occurrences of the
     * separator in the given string are assumed to be escaped with the given escape character. The escape character
     * itself is also assumed to be escaped.
     *
     * @param string    A string consisting of zero or more parts concatenated with the given separator.
     * @param separator A separator character.
     * @param escape    An escape character.
     * @return An array of unescaped parts of the given string.
     * @throws IllegalArgumentException If separator equals escape.
     * @throws NullPointerException     If strings is null.
     */
    public static String[] split(String string, char separator, char escape) {
        Require.nonNull(string, "string is null");
        Require.isTrue(separator != escape, "separator equals escape charater");
        final List<String> tokens = new ArrayList<String>();
        final StringBuilder buf = new StringBuilder();
        int fromIndex = 0;
        int index = nextIndex(string, separator, escape, fromIndex);
        while (index != -1) {
            final String original = string.substring(fromIndex, index);
            buf.append(original);
            final char finding = string.charAt(index);
            if (finding == escape) {
                final char unescaped = string.charAt(index + 1);
                buf.append(unescaped);
                fromIndex = index + 2;
            } else { // finding == separator
                tokens.add(buf.toString());
                buf.setLength(0);
                fromIndex = index + 1;
            }
            index = nextIndex(string, separator, escape, fromIndex);
        }
        final String rest = string.substring(fromIndex);
        buf.append(rest);
        tokens.add(buf.toString());
        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * Computes the index of the first occurrence of separator or escape within s starting at position fromIndex.
     * <p/>
     * Returns -1 only, and only if both of separator and escape are not found.
     *
     * @param s         A String.
     * @param separator A char.
     * @param escape    A char.
     * @param fromIndex A start index.
     * @return The index of the first occurrence of a separator or a escape charater within s.
     */
    private static int nextIndex(String s, char separator, char escape, int fromIndex) {
        final int index1 = s.indexOf(separator, fromIndex);
        final int index2 = s.indexOf(escape, fromIndex);
        return (index1 == -1) ? index2 : (index2 == -1) ? index1 : Math.min(index1, index2);
    }

    private static String toString(Object o, Set<Object> visited) {
        if (o == null) {
            return "null";
        } else if (visited.contains(o)) {
            return "...";
        } else if (o instanceof CharSequence) {
            return '"' + o.toString() + '"';
        } else if (o instanceof Class) {
            return toString((Class<?>) o, 0);
        } else if (o.getClass().isArray()) {
            return toString(ArrayExtensions.toStream(o), ", ", "Array(", ")", visited, o);
        } else if (o.getClass().getName().startsWith("javaslang.")) {
            return o.toString();
        } else if (o instanceof Iterable) {
            return toString(StreamSupport.stream(((Iterable<?>) o).spliterator(), false), ", ", o.getClass().getSimpleName() + "(", ")", visited, o);
        } else {
            return o.toString();
        }
    }

    private static String toString(Class<?> clazz, int dimension) {
        if (clazz.isArray()) {
            return toString(clazz.getComponentType(), dimension + 1);
        } else {
            return clazz.getName() + repeat("[]", dimension);
        }
    }

    private static String toString(Stream<?> stream, CharSequence delimiter, CharSequence prefix, CharSequence suffix,
                                   Set<Object> visited, Object o) {
        visited.add(o);
        final String result = stream.map(x -> toString(x, visited)).collect(
                Collectors.joining(delimiter, prefix, suffix));
        visited.remove(o);
        return result;
    }

    static interface ArrayExtensions {

        static final Match<Stream<?>> ARRAY_TO_STREAM_MATCHER = new Match.Builder<Stream<?>>()
                .caze((boolean[] a) -> stream(a))
                .caze((byte[] a) -> stream(a))
                .caze((char[] a) -> stream(a))
                .caze((double[] a) -> stream(a))
                .caze((float[] a) -> stream(a))
                .caze((int[] a) -> stream(a))
                .caze((long[] a) -> stream(a))
                .caze((short[] a) -> stream(a))
                .caze((Object[] a) -> Stream.of(a))
                .build();

        public static Stream<?> toStream(Object o) {
            return ARRAY_TO_STREAM_MATCHER.apply(o);
        }

        static Stream<Boolean> stream(boolean[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Byte> stream(byte[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Character> stream(char[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Double> stream(double[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Float> stream(float[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Integer> stream(int[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Long> stream(long[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static Stream<Short> stream(short[] array) {
            Require.nonNull(array, "array is null");
            return new StreamableList<>(array.length, i -> array[i]).stream();
        }

        static class StreamableList<E> extends AbstractList<E> {

            final int size;
            final Function<Integer, E> getter;

            StreamableList(int size, Function<Integer, E> getter) {
                this.size = size;
                this.getter = getter;
            }

            @Override
            public int size() {
                return size;
            }

            @Override
            public E get(int index) {
                return getter.apply(index);
            }

            @Override
            public Spliterator<E> spliterator() {
                return Spliterators.spliterator(this, Spliterator.ORDERED | Spliterator.IMMUTABLE);
            }
        }
    }
}
