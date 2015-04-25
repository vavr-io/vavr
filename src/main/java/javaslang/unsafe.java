/*     / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014-2015 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Used to mark methods which make certain assumtions regarding types.
 * <p>
 * <strong>Example:</strong>
 * <p>
 * {@code List.of(List.of(1)).flatten()} returns {@code List.of(1)} as expected whereas a call to
 * {@code List.of(1).flatten()} does compile but throws at runtime.
 * <p>
 * Some powerful Javaslang functionality, like the {@code flatten()} method, requires the developer to use it with care.
 * To make this transparent, such functionality is marked as {@code @unsafe}.
 */
@Target(ElementType.METHOD)
public @interface unsafe {
}
