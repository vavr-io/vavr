/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/**
 * Recursive self type representing {@code TYPE<T>}, which allows similar behavior to higher-kinded types.
 *
 * @param <TYPE> The recursive type described by {@code Kind1}
 * @param <T>    The first type, described by {@code Kind1}
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Kind1<TYPE extends Kind1<TYPE, ?>, T> {

}
