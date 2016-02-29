/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

/**
 * Recursive self type representing {@code TYPE<T>}, which allows similar behavior to higher-kinded types.
 *
 * @param <TYPE> The recursive type described by {@code Kind1}
 * @param <T>    The component type of TYPE
 * @author Daniel Dietrich
 * @since 2.0.0
 */
public interface Kind1<TYPE extends Kind1<TYPE, ?>, T> {
}
