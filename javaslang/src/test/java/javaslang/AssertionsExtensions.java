/*     / \____  _    _  ____   ______  / \ ____  __    _______
 *    /  /    \/ \  / \/    \ /  /\__\/  //    \/  \  //  /\__\   JΛVΛSLΛNG
 *  _/  /  /\  \  \/  /  /\  \\__\\  \  //  /\  \ /\\/ \ /__\ \   Copyright 2014-2017 Javaslang, http://javaslang.io
 * /___/\_/  \_/\____/\_/  \_/\__\/__/\__\_/  \_//  \__/\_____/   Licensed under the Apache License, Version 2.0
 */
package javaslang;

import org.assertj.core.api.Assertions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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

        public void isNotInstantiable() {
            final Constructor<?> cons;
            try {
                cons = clazz.getDeclaredConstructor();
                Assertions.assertThat(cons.isAccessible()).isFalse();
            } catch (NoSuchMethodException e) {
                throw new AssertionError("no default constructor found");
            }
        }
    }
}
