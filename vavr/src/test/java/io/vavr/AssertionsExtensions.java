/*  __    __  __  __    __  ___
 * \  \  /  /    \  \  /  /  __/
 *  \  \/  /  /\  \  \/  /  /
 *   \____/__/  \__\____/__/
 *
 * Copyright 2014-2019 Vavr, http://vavr.io
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
package io.vavr;

import org.assertj.core.api.Assertions;

import java.lang.reflect.Constructor;

public final class AssertionsExtensions {

    private AssertionsExtensions() {
    }

    public static ClassAssert assertThat(Class<?> clazz) {
        return new ClassAssert(clazz);
    }

    public static class ClassAssert {

        final Class<?> clazz;

        ClassAssert(Class<?> clazz) {
            this.clazz = clazz;
        }

        @SuppressWarnings("deprecation")
        public void isNotInstantiable() {
            try {
                final Constructor<?> cons = clazz.getDeclaredConstructor();
                Assertions.assertThat(cons.isAccessible()).isFalse();
            } catch (NoSuchMethodException e) {
                throw new AssertionError("no default constructor found");
            }
        }
    }
}
