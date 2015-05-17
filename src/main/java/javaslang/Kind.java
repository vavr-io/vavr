/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * <strong>A {@code Kind} is the type of a type constructor.</strong>
 * <p>
 * It is denoted as decomposition of a type constructor, i.e. {@code List<T>} is {@code Kind<List<?>, T>}.
 * <p>
 * Higher-kinded/higher-order types cannot be expressed with Java. This is an approximation, it has its limitations.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * Javaslang uses {@code Kind} to define a function {@code flatMap(Function)}. The special thing about the
 * {@code Function} argument is, that it returns an instance of the type {@code flatMap()} is defined in.
 * <p>
 * In particular, {@code flatMap()} is defined in {@code Monad}, {@code Traversable}, {@code Seq} and {@code List},
 * where {@code List extends Seq, Monad} and {@code Seq extends Traversable}.
 * <p>
 * Exercise: Try to implement this example without the help of {@code Kind}.
 * <p>
 * Here is how it looks like:
 * <pre>
 * <code>
 * interface Monad&lt;M extends Kind&lt;M, ?&gt;, T&gt; extends Kind&lt;M, T&gt; {
 *     &lt;U&gt; Monad&lt;M, U&gt; flatMap(Function&lt;? super T, ? extends Kind&lt;M, U&gt;&gt; mapper);
 * }
 *
 * interface Traversable&lt;M extends Traversable&lt;M, ?&gt;, T&gt; extends Kind&lt;M, T&gt; {
 *     &lt;U&gt; Traversable&lt;M, U&gt; flatMap(Function&lt;? super T, ? extends Kind&lt;M, U&gt;&gt; mapper);
 * }
 *
 * interface Seq&lt;M extends Seq&lt;M, ?&gt;, T&gt; extends Kind&lt;M, T&gt;, Traversable&lt;M, T&gt; {
 *     &#64;Override
 *     &lt;U&gt; Seq&lt;M, U&gt; flatMap(Function&lt;? super T, ? extends Kind&lt;M, U&gt;&gt; mapper);
 * }
 *
 * interface List&lt;T&gt; extends Seq&lt;List&lt;?&gt;, T&gt;, Monad&lt;List&lt;?&gt;, T&gt; {
 *     &#64;Override
 *     &lt;U&gt; List&lt;U&gt; flatMap(Function&lt;? super T, ? extends Kind&lt;List&lt;?&gt;, U&gt;&gt; mapper);
 * }
 * </code>
 * </pre>
 *
 * <p>
 * See also
 * <ul>
 * <li><a href="http://adriaanm.github.io/files/higher.pdf">Generics of a Higher Kind</a> (Moors, Piessens, Odersky)</li>
 * <li><a href="http://en.wikipedia.org/wiki/Kind_(type_theory)">Kind (type theory)</a> (wikipedia)</li>
 * <li><a href="http://en.wikipedia.org/wiki/Type_constructor">Type constructor</a> (wikipedia)</li>
 * </ul>
 *
 * @param <TYPE> container type
 * @param <T>    component type
 * @since 1.1.0
 */
public interface Kind<TYPE extends Kind<TYPE, ?>, T> {
}
