/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * A {@code Kind} is the type of a type constructor.
 * <p>
 * It is denoted as decomposition of a type constructor, i.e. {@code List<T>} is {@code Kind<List<?>, T>}.
 * <p>
 * Higher-kinded/higher-order types cannot be expressed with Java. This is an approximation, it has its limitations.
 * <p>
 * <strong>Examples:</strong>
 * <p>
 *
 * <strong>Limitations:</strong>
 * <ul>
 * <li>Once a type extends {@code Kind} with a concrete first generic parameter type, like {@code List} does, the
 * {@code Kind} is fixated. There is no way for subclasses to implement a more specific {@code Kind}.</li>
 * </ul>
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
 * @since 2.0.0
 */
public interface Kind<TYPE extends Kind<TYPE, ?>, T> {
}
