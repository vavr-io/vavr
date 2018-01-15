/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2018 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vavr.concurrent;

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
