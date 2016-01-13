/*     / \____  _    _  ____   ______  / \ ____  __    _ _____
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  / /  _  \   Javaslang
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/  \__/  /   Copyright 2014-now Daniel Dietrich
 * /___/\_/  \_/\____/\_/  \_/\__\/__/___\_/  \_//  \__/_____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.algebra;

/**
 * Recursive self type representing {@code TYPE<T1, T2>}, which allows similar behavior to higher-kinded types.
 *
 * @param <TYPE> The recursive type described by {@code Kind2}
 * @param <T1>   The first type, described by {@code Kind2}
 * @param <T2>   The second type, described by {@code Kind2}
 * @author Daniel Dietrich, Eric Nelson
 * @since 2.0.0
 */
public interface Kind2<TYPE extends Kind2<TYPE, ?, ?>, T1, T2> {

}
