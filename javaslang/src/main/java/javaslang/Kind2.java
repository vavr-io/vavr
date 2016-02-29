/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * Recursive self type representing {@code TYPE<T1, T2>}, which allows similar behavior to higher-kinded types.
 *
 * @param <TYPE> The recursive type described by {@code Kind2}
 * @param <T1>   The first component type of {@code TYPE}
 * @param <T2>   The second component type of {@code TYPE}
 * @author Daniel Dietrich, Eric Nelson
 * @since 2.0.0
 */
public interface Kind2<TYPE extends Kind2<TYPE, ?, ?>, T1, T2> {
}
