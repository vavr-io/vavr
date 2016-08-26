/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2016 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang.concurrent;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * <strong>INTERNAL API - Used for documentation purpose only.</strong>
 * <p>
 * An annotated field or method must only be accessed when holding the lock specified in {@code value()}.
 * <p>
 * This is an annotation known from <a href="http://jcip.net">Java Concurrency in Practice</a>.
 * See also <a href="https://jcp.org/en/jsr/detail?id=305">JSR 305</a>
 *
 * @author Daniel Dietrich
 * @since 2.1.0
 */
@Documented
@Target(value = { FIELD, METHOD })
@Retention(RUNTIME)
@interface GuardedBy {

    /**
     * Specifies the lock that guards the annotated field or method.
     *
     * @return a valid lock that guards the annotated field or method
     */
    String value();
}
